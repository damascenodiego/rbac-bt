package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmPath;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;

public class FsmTestingUtils {

	private FsmUtils fsmUtils = FsmUtils.getInstance();


	static FsmTestingUtils instance;

	private FsmTestingUtils() {
	}

	public static  FsmTestingUtils getInstance() {
		if(instance ==null){
			instance = new FsmTestingUtils();
		}
		return instance;
	}



	public void WriteFsmTestCaseAsDot(FsmModel fsm, File f) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(f);

		pw.println("digraph rbac2Fsm {");
		List<FsmTransition> transit = fsm.getTransitions();
		for (FsmTransition tr : transit) {
			if(tr.getOutput().equals("deny")) continue;
			pw.println("  "+
					tr.getFrom().getName()
					+" -> "
					+tr.getTo().getName()
					+" [ label =\""+tr.getInput()+"/"+tr.getOutput()+"\"];");
		}
		pw.println("}");
		pw.close();
	}

	public List<FsmPath> stateCoverSet(FsmModel fsm){
		FsmPath[] qSets = new FsmPath[fsm.getStates().size()];
		for (int i = 0; i < fsm.getStates().size(); i++) {
			qSets[i] = new FsmPath(fsm.getStates().get(i).getName());
		}
		FsmState current = fsm.getInitialState();
		Queue<FsmState> toVisit = new LinkedList<FsmState>();
		Set<FsmState> visited = new HashSet<FsmState>();
		toVisit.add(current);
		while(!toVisit.isEmpty()){
			current = toVisit.remove();
			visited.add(current);
			for (FsmTransition tr: fsm.getState(current).getOut()) {
				if(!visited.contains(tr.getTo())){
					int indexTo = fsm.getStates().indexOf(tr.getTo());
					int indexFrom = fsm.getStates().indexOf(tr.getFrom());
					qSets[indexTo].getPath().addAll(qSets[indexFrom].getPath());
					qSets[indexTo].addTransition(tr);
					if(qSets[indexTo].getName().equals(tr.getTo().getName())){
						visited.add(tr.getTo());
					}
					toVisit.add(tr.getTo());
				}
			}
		}
		return Arrays.asList(qSets);
	}

	public List<FsmPath> transitionCoverSet(FsmModel fsm) {
		List<FsmPath> qSet = stateCoverSet(fsm);
		List<FsmPath> pSet = new ArrayList<FsmPath>();

		for (FsmPath path : qSet) {
			FsmState finalState = null;
			if(path.getPath().isEmpty()){
				finalState = fsm.getInitialState();
			}
			else{
				finalState = path.getFinalState();
			}

			for (FsmTransition tr : finalState.getOut()) {
				FsmPath pSetEl = new FsmPath(finalState.getName()+"+"+tr.getInput());
				pSetEl.getPath().addAll(path.getPath());
				pSetEl.addTransition(tr);
				pSet.add(pSetEl);
			}
		}
		return pSet;
	}
}