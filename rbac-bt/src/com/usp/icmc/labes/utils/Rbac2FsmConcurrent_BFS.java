package com.usp.icmc.labes.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class Rbac2FsmConcurrent_BFS {

	private final RbacAcut acut;
	private final ExecutorService pool;
	private final int THREADS_NUM;
	private final int MAX_DEPTH;
	private final CountDownLatch latch;
	//	private final ConcurrentMap<RbacState, Integer> depth = new ConcurrentHashMap<RbacState, Integer>();
	private final ConcurrentMap<String, Boolean> used = new ConcurrentHashMap<String, Boolean>();
	private LinkedBlockingQueue<RbacState> curQ = new LinkedBlockingQueue<RbacState>();
	private LinkedBlockingQueue<RbacState> nextQ = new LinkedBlockingQueue<RbacState>();

	private volatile int lvl = 0; // really need this???
	private CyclicBarrier barrier;
	private List<RbacRequest> rqs; 

	private FsmModel fsmModel;

	public Rbac2FsmConcurrent_BFS(RbacAcut sut, int threadNum) {
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
		fsmModel = new FsmModel(acut.getPolicy().getName());
		String initialStateName = acut.getName();
		SearchTask[] tasks = new SearchTask[THREADS_NUM];
		try {
			curQ.add(acut.getCurrentState());
			used.put(acut.getCurrentState().getName(), true);
			for (int i = 0; i < THREADS_NUM; i++) {
				SearchTask t = new SearchTask(i);
				pool.execute(t);
				tasks[i]=t;
			}
			latch.countDown();
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			pool.shutdown();
		}
		Set<FsmState> states = new HashSet<FsmState>();
		Set<FsmTransition> transitions = new HashSet<FsmTransition>();
		Set<String> inputs = new HashSet<String>();
		Set<String>outputs = new HashSet<String>();
		for (SearchTask t : tasks) {
			states.addAll(t.getStates());
			transitions.addAll(t.getTransitions());
			inputs.addAll(t.getInputs());
			outputs.addAll(t.getOutputs());
		}
		fsmModel.getStates()		.addAll(states);
		fsmModel.getTransitions()	.addAll(transitions);
		fsmModel.getInputs()		.addAll(inputs);
		fsmModel.getOutputs()		.addAll(outputs);
		fsmModel.setInitialState(FsmUtils.getInstance().getState(states,initialStateName ));
		FsmUtils.getInstance().updateElements(fsmModel);
		FsmUtils.getInstance().sorting(fsmModel);
	}


	
	public FsmModel getFsmModel(){
		return fsmModel;
	}

	protected class SearchTask implements Runnable {
		private int name;

		private Set<FsmState> states = new HashSet<FsmState>();
		private Set<FsmTransition> transitions = new HashSet<FsmTransition>();
		private Set<String> inputs = new HashSet<String>();
		private Set<String> outputs = new HashSet<String>();

		public SearchTask(int name) {
			this.name = name;
		}

		@Override
		public void run() {
			//System.out.println("[Thread # " + name + " start execution]");
			try {
				while (true) {
					TimeUnit.MILLISECONDS.sleep(10);
					RbacState u = curQ.poll();
					if (u != null && used.putIfAbsent(u.getName(), true)) {
						RbacAcut localAcut = new RbacAcut(u.getPolicy());
						
						Map<FsmTransition,RbacState> generatedTrs = new HashMap<FsmTransition,RbacState>();
						
						for (RbacRequest in : rqs) {
							localAcut.reset(u);
							RbacState origin = u;
							boolean out = localAcut.request(in);
							RbacState destination = ((RbacState) localAcut.getCurrentState().clone());
							
							FsmState from = new FsmState(origin.getName());
							FsmState to = new FsmState(destination.getName());
							
							FsmTransition transition = new FsmTransition(from, in.toString(), (out? "grant" : "deny"), to);
							
							if(!out) {
								Properties trProp = (Properties) localAcut.getPolicy().getProperties().clone();
								transition.setProperties(trProp);
							}
							
							generatedTrs.put(transition,destination);
						}
						for (FsmTransition tr : generatedTrs.keySet()) {
							
							if(!states.contains(tr.getFrom())) 		states.add(tr.getFrom());
							if(!inputs.contains(tr.getInput())) 	inputs.add(tr.getInput());
							if(!outputs.contains(tr.getOutput())) 	outputs.add(tr.getOutput());
							if(!transitions.contains(tr)) transitions.add(tr);
							if(!states.contains(tr.getTo())) states.add(tr.getTo());

							if(used.putIfAbsent(tr.getTo().getName(), true) == null) {
								nextQ.add(generatedTrs.get(tr));
								//if(nextQ.size()%100==0) System.out.println(nextQ.size());
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
			} catch (InterruptedException | BrokenBarrierException | CloneNotSupportedException e) {
				e.printStackTrace();
			} finally {
				//System.out.println("[Thread # " + name + " finish execution]");
				latch.countDown();
			}
		}
		public Set<FsmState> getStates() {
			return states;
		}
		public Set<String> getInputs() {
			return inputs;
		}
		public Set<String> getOutputs() {
			return outputs;
		}
		public Set<FsmTransition> getTransitions() {
			return transitions;
		}
	}

}