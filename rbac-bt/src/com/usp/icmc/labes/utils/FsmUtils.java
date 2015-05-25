package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.usp.icmc.labes.fsm.FsmElement;
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
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public class FsmUtils {

	static FsmUtils instance;

	private FsmUtils() {}

	public static  FsmUtils getInstance() {
		if(instance ==null){
			instance = new FsmUtils();
		}
		return instance;
	}

	public void WriteFsmAsGML(FsmModel fsm, File f) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(f);

		pw.println("graph [");
		pw.println("    directed 1");
		pw.println("   	id 1");
		pw.println("    label \""+fsm.getName()+"\"");

		List<FsmState> states = fsm.getStates();

		for (FsmElement el : states) {
			pw.println("    node [");
			pw.println("        id "+states.indexOf(el));
			pw.println("        label \""+el.getName()+"\"");
			//pw.println("    		thisIsASampleAttribute 42");
			pw.println("    	]");
		}		
		List<FsmTransition> transit = fsm.getTransitions();

		for (FsmTransition tr : transit) {
			if(tr.getOutput().equals("deny")) continue;
			pw.println("    edge [");
			pw.println("        source "+states.indexOf(tr.getFrom()));
			pw.println("        target "+states.indexOf(tr.getTo()));
			pw.println("        label \""+tr.getInput()+"/"+tr.getOutput()+"\"");
			pw.println("    	]");
		}
		pw.println("]");
		pw.close();
	}


	public void WriteFsmAsDot(FsmModel fsm, File f) throws FileNotFoundException{
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

	public void WriteFsmAsKiss(FsmModel fsm, File f) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(f);

		List<FsmTransition> transit = fsm.getTransitions();
		for (FsmTransition tr : transit) {
			if(tr.getOutput().equals("deny")) continue;
			pw.println(tr.getFrom().getName()
						+" -- "
						+tr.getInput()+"/"+tr.getOutput()
						+" -> "
						+tr.getTo().getName());
		}
		pw.println("}");
		pw.close();
	}

	public void WriteFsmAsCsv(FsmModel fsm, File f) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(f);

		List<FsmTransition> transit = fsm.getTransitions();
		pw.println("\"origin\",\"input\",\"output\",\"destination\"");
		for (FsmTransition tr : transit) {
			if(tr.getOutput().equals("deny")) continue;
			pw.println(
					"\""+tr.getFrom().getName()+"\","
					+"\""+tr.getInput()+"\","
					+"\""+tr.getOutput()+"\","
					+"\""+tr.getTo().getName()+"\",");
		}
		pw.println("}");
		pw.close();
	}
	
	public FsmModel rbac2Fsm(RbacPolicy rbac) {
		rbac.getUserRoleAssignment().clear();
		List<RbacRequest> input = new ArrayList<RbacRequest>();
		for (Role rol: rbac.getRole()) {
			for (User usr: rbac.getUser()) {
				input.add(new RbacRequestAssignUR(usr, rol));
				input.add(new RbacRequestDeassignUR(usr, rol));
				input.add(new RbacRequestActivateUR(usr, rol));
				input.add(new RbacRequestDeactivateUR(usr, rol));
			}
//			for (Permission prms: rbac.getPermission()) {
//				input.add(new RbacRequestAssignPR(prms, rol));
//				input.add(new RbacRequestDeassignPR(prms, rol));
//			}
		}

		RbacAcut acut = new RbacAcut(rbac);

		RbacState 	origin 			= null;
		boolean 	out 			= false;
		RbacState	 destination 	= null;

		FsmModel rbac2fsm = new FsmModel(rbac.getName());

		Queue<RbacState> toVisit = new LinkedList<RbacState>();
		toVisit.add(acut.getCurrentState());

		List<RbacState> visited = new ArrayList<RbacState>();

		while (!toVisit.isEmpty()) {
			origin = toVisit.remove();
			acut.reset(origin);
			if(!visited.contains(origin)){
				visited.add(origin);
				rbac2fsm.addState(new FsmState(origin.getName()));
				for (RbacRequest in : input) {
					out = acut.request(in);
					destination = acut.getCurrentState();
					rbac2fsm.addTransition(new FsmTransition(new FsmState(origin.getName()), new FsmState(destination.getName()), in.toString(), (out? "grant" : "deny")));
					if(!visited.contains(destination)) 
						toVisit.add(destination);
					acut.reset(origin);
				}
			}
		}
		return rbac2fsm;
	}
}
