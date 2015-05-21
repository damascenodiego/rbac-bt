package com.usp.icmc.labes.rbac.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmElement;
import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;

public class FsmUtils {

	void WriteFsmAsGML(FsmModel fsm, File f) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(f);

		pw.println("graph [");
		pw.println("   	directed 1");
		pw.println("   	id 1");
		pw.println("    label "+fsm.getName());

		List<FsmState> states = fsm.getStates();

		for (FsmElement el : states) {
			pw.println("    node [");
			pw.println("    		id "+states.indexOf(el));
			pw.println("    		label \""+el.getName()+"\"");
			//pw.println("    		thisIsASampleAttribute 42");
			pw.println("    	]");
		}		
		List<FsmTransition> transit = fsm.getTransitions();

		for (FsmTransition tr : transit) {
			pw.println("    edge [");
			pw.println("    		source "+states.indexOf(tr.getFrom()));
			pw.println("    		target "+states.indexOf(tr.getTo()));
			pw.println("    		label \""+tr.getInput()+"/"+tr.getOutput()+"\"");
			pw.println("    	]");
		}
		pw.println("]");
		pw.close();
	}

}
