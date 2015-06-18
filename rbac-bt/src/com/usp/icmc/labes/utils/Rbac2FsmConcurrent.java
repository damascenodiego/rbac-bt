package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.acut.RbacState;
import com.usp.icmc.labes.rbac.acut.RbacTransition;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class Rbac2FsmConcurrent {

	private final RbacAcut acut;
	private final ExecutorService pool;
	private final int THREADS_NUM;
	private final int MAX_DEPTH;
	private final CountDownLatch latch;
	//	private final ConcurrentMap<RbacState, Integer> depth = new ConcurrentHashMap<RbacState, Integer>();
	private final ConcurrentMap<String, Boolean> used = new ConcurrentHashMap<String, Boolean>();
	private LinkedBlockingQueue<RbacState> curQ = new LinkedBlockingQueue<RbacState>();
	private LinkedBlockingQueue<RbacState> nextQ = new LinkedBlockingQueue<RbacState>();

	private LinkedBlockingQueue<FsmState> states = new LinkedBlockingQueue<FsmState>();
	private LinkedBlockingQueue<FsmTransition> transitions = new LinkedBlockingQueue<FsmTransition>();
	private LinkedBlockingQueue<String> inputs = new LinkedBlockingQueue<String>();
	private LinkedBlockingQueue<String> outputs = new LinkedBlockingQueue<String>();

	private volatile int lvl = 0; // really need this???
	private CyclicBarrier barrier;
	private List<RbacRequest> rqs; 

	private FsmModel fsmModel;

	public Rbac2FsmConcurrent(RbacAcut sut, int threadNum) {
		this.acut = sut;
		THREADS_NUM = threadNum;
		MAX_DEPTH = 6*sut.getUser().size()*sut.getRole().size();
		pool = Executors.newFixedThreadPool(THREADS_NUM);
		latch = new CountDownLatch(THREADS_NUM + 1);
		barrier = new CyclicBarrier(THREADS_NUM);
		rqs = new ArrayList<RbacRequest>();
		for (Role rol: sut.getRole()) {
			for (User usr: sut.getUser()) {
				rqs.add(new RbacRequestAssignUR(usr, rol));
				rqs.add(new RbacRequestDeassignUR(usr, rol));
				rqs.add(new RbacRequestActivateUR(usr, rol));
				rqs.add(new RbacRequestDeactivateUR(usr, rol));
			}
			//			for (Permission prms: rbac.getPermission()) {
			//				input.add(new RbacRequestAssignPR(prms, rol));
			//				input.add(new RbacRequestDeassignPR(prms, rol));
			//			}
		}
	}

	//	public int[] getDepth() {
	//		int n = depth.length();
	//		int[] a = new int[n];
	//		for(int i = 0; i < n; i++) {
	//			a[i] = depth.get(i);
	//		}
	//		return a;
	//	}

	public void start() {
		try {
			curQ.add(acut.getCurrentState());
			used.put(acut.getCurrentState().getName(), true);
			for (int i = 0; i < THREADS_NUM; i++) {
				pool.execute(new SearchTask(i));
			}
			latch.countDown();
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			pool.shutdown();
		}
		fsmModel = new FsmModel(acut.getPolicy().getName());
		fsmModel.getInputs().addAll(inputs);
		fsmModel.getOutputs().addAll(outputs);
		fsmModel.getStates().addAll(states);
		fsmModel.getTransitions().addAll(transitions);
		fsmModel.setInitialState(FsmUtils.getInstance().getState(states,acut.getName()));
		
		
	}

	public FsmModel getFsmModel(){
		return fsmModel;
	}

	protected class SearchTask implements Runnable {
		private int name;

		public SearchTask(int name) {
			this.name = name;
		}

		@Override
		public void run() {
			//System.out.println("[Thread # " + name + " start execution]");
			try {
				while (true) {
					TimeUnit.MILLISECONDS.sleep(10);
//					while (!toVisit.isEmpty()) {
//					origin = toVisit.remove();
					RbacState u = curQ.poll();
					if (u != null) {
//						acut.reset(origin);
//						if(!visited.contains(origin)){
//							visited.add(origin);
//							rbac2fsm.addState(new FsmState(origin.getName()));
//							for (RbacRequest in : input) {
//								out = acut.request(in);
//								destination = ((RbacState) acut.getCurrentState().clone());
//								rbac2fsm.addState(new FsmState(destination.getName()));
//								rbac2fsm.addTransition(new FsmTransition(rbac2fsm.getState(origin.getName()), in.toString(), (out? "grant" : "deny"), rbac2fsm.getState(destination.getName())));
//								if(!visited.contains(destination)) 
//									toVisit.add(destination);
//								else{
//									toVisit.remove(destination);
//								}
//								acut.reset(origin);
//							}
//						}
//					}

						used.putIfAbsent(u.getName(),true);
						//						List<FsmTransition> trs = new ArrayList<FsmTransition>();
						RbacAcut localAcut = new RbacAcut(u.getPolicy());
						for (RbacRequest in : rqs) {
							localAcut.reset(u);
							RbacState origin = (RbacState) localAcut.getCurrentState().clone();
							boolean out = localAcut.request(in);
							RbacState destination = ((RbacState) localAcut.getCurrentState().clone());

							FsmState from = new FsmState(origin.getName());
							FsmState to = new FsmState(destination.getName());
							FsmTransition transition = new FsmTransition(from, in.toString(), (out? "grant" : "deny"), to);
							//							trs.add(transition);
							if(!states.contains(transition.getFrom())) 		states.add(transition.getFrom());
							if(!inputs.contains(transition.getInput())) 	inputs.add(transition.getInput());
							if(!outputs.contains(transition.getOutput())) 	outputs.add(transition.getOutput());
							if(!transitions.contains(transition)) transitions.add(transition);
							if(!states.contains(transition.getTo())) states.add(transition.getTo());

							used.putIfAbsent(transition.getFrom().getName(), true);
							
							if(used.putIfAbsent(transition.getTo().getName(), true) == null) {
								nextQ.add(destination);
//								curQ.add(destination);
								//								depth.put(destination, depth.getOrDefault(destination,0) + 1);
							}
						}
					} else  {
						//System.out.println("[Thread # " + name + " is arrived to cs:");
						//magic here
						int num = barrier.getNumberWaiting();
						if(num == THREADS_NUM - 1) {
							//broken barrier exception??
							lvl = nextQ.isEmpty() ? MAX_DEPTH : lvl + 1;
							LinkedBlockingQueue<RbacState> tmp = curQ;
							curQ = nextQ;
							nextQ = tmp; 
							barrier.await();
							barrier.reset();
						}else {
							barrier.await();
						}
						//System.out.println("[Thread # " + name + " exit cs:");
						if(lvl == MAX_DEPTH) {
							break;
						}
					}
				}
			} catch (InterruptedException | BrokenBarrierException  | CloneNotSupportedException e) {
				e.printStackTrace();
			} finally {
				//System.out.println("[Thread # " + name + " finish execution]");
				latch.countDown();
			}
		}
	}

}