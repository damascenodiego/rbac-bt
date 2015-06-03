package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	
	static FsmUtils instance;

	private FsmUtils() {
//		xstream = new XStream(new DomDriver());
//		xstream.alias("FSM", FsmModel.class);
//		xstream.alias("state", FsmState.class);
//		xstream.alias("transition", FsmTransition.class);
//		xstream.processAnnotations(FsmModel.class);
//		xstream.processAnnotations(FsmState.class);
//		xstream.processAnnotations(FsmTransition.class);
	}

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

		for (FsmState el : states) {
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

	public FsmModel rbac2Fsm(RbacPolicy rbac) throws Exception {
//		rbac.getUserRoleAssignment().clear();
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
		toVisit.add((RbacState) acut.getCurrentState().clone());

		List<RbacState> visited = new ArrayList<RbacState>();

		while (!toVisit.isEmpty()) {
			origin = toVisit.remove();
			acut.reset(origin);
			if(!visited.contains(origin)){
				visited.add(origin);
				rbac2fsm.addState(new FsmState(origin.getName()));
				for (RbacRequest in : input) {
					out = acut.request(in);
					destination = ((RbacState) acut.getCurrentState().clone());
					rbac2fsm.addState(new FsmState(destination.getName()));
					rbac2fsm.addTransition(new FsmTransition(rbac2fsm.getState(origin.getName()), in.toString(), (out? "grant" : "deny"), rbac2fsm.getState(destination.getName())));
					if(!visited.contains(destination)) 
						toVisit.add(destination);
					else{
						toVisit.remove(destination);
					}
					acut.reset(origin);
				}
			}
		}
		return rbac2fsm;
	}

	public FsmModel rbac2FsmConcurrent(RbacPolicy rbac) {
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
					rbac2fsm.addState(new FsmState(destination.getName()));
					rbac2fsm.addTransition(new FsmTransition(rbac2fsm.getState(origin.getName()), in.toString(), (out? "grant" : "deny"), rbac2fsm.getState(destination.getName())));
					if(!visited.contains(destination)) 
						toVisit.add(destination);
					acut.reset(origin);
				}
			}
		}
		return rbac2fsm;
	}

	public void WriteFsm(FsmModel fsm, File fsmFile) throws ParserConfigurationException, TransformerException {
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement("FSM");
		rootElement.setAttribute("initialState",fsm.getInitialState().getName());
		rootElement.setAttribute("name",fsm.getName());
		
		Element inputs = doc.createElement("inputs");
		for (String in: fsm.getInputs()) {
			Element child = doc.createElement("input");
			child.setAttribute("name", in);
			inputs.appendChild(child);
		}
		rootElement.appendChild(inputs);
		
		Element outputs = doc.createElement("outputs");
		for (String in: fsm.getOutputs()) {
			Element child = doc.createElement("output");
			child.setAttribute("name", in);
			outputs.appendChild(child);
		}
		rootElement.appendChild(outputs);
		
		
		Element states = doc.createElement("states");
		for (FsmState s: fsm.getStates()) {
			Element child = doc.createElement("state");
			child.setAttribute("name", s.getName());
			states.appendChild(child);
		}
		rootElement.appendChild(states);
		
		Element transitions = doc.createElement("transitions");
		for (FsmTransition t: fsm.getTransitions()) {
			Element transition = doc.createElement("transition");
			transition.setAttribute("from", t.getFrom().getName());
			transition.setAttribute("in", t.getInput());
			transition.setAttribute("out", t.getOutput());
			transition.setAttribute("to", t.getTo().getName());
			transitions.appendChild(transition);
		}
		rootElement.appendChild(transitions);
		
		doc.appendChild(rootElement);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fsmFile);
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
		
//		OutputStream fos = new FileOutputStream(fsmFile);
//		xstream.toXML(fsm, fos);

	}
	
	public FsmModel LoadFsmFromXML(File fsmFile) throws ParserConfigurationException, SAXException, IOException {
		FsmModel fsm = new FsmModel(); //(FsmModel) xstream.fromXML(fsmFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fsmFile);
		doc.getDocumentElement().normalize();
		Element fsmElement = doc.getDocumentElement();
		fsm.setName(fsmElement.getAttribute("name"));
		
		Node node = fsmElement.getElementsByTagName("inputs").item(0);
		NodeList el = ((Element)node).getElementsByTagName("input");
		for (int i = 0; i < el.getLength(); i++) {
			Element in = (Element) el.item(i);
			fsm.getInputs().add(in.getAttribute("name"));
		}
		
		node = fsmElement.getElementsByTagName("outputs").item(0);
		el = ((Element)node).getElementsByTagName("output");
		for (int i = 0; i < el.getLength(); i++) {
			Element in = (Element) el.item(i);
			fsm.getOutputs().add(in.getAttribute("name"));
		}
		
		node = fsmElement.getElementsByTagName("states").item(0);
		el = ((Element)node).getElementsByTagName("state");
		for (int i = 0; i < el.getLength(); i++) {
			Element in = (Element) el.item(i);
			fsm.getStates().add(new FsmState(in.getAttribute("name")));
		}
		
		fsm.setInitialState(fsm.getState(fsmElement.getAttribute("initialState")));
		node = fsmElement.getElementsByTagName("transitions").item(0);
		el = ((Element)node).getElementsByTagName("transition");
		for (int i = 0; i < el.getLength(); i++) {
			Element in = (Element) el.item(i);
			FsmState f = fsm.getState(in.getAttribute("from"));
			String input = in.getAttribute("in");
			String output = in.getAttribute("out");
			FsmState t = fsm.getState(in.getAttribute("to"));
			fsm.addTransition(new FsmTransition(f, input, output, t));
		}
		
		return fsm;
	}

}

class ConcurrentVisitRbacState extends Thread{

	RbacState state;
	
	
	
	
	public ConcurrentVisitRbacState(RbacState s) {
		this.state = s;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
}