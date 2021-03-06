package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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
import com.usp.icmc.labes.rbac.model.RbacMutableElement;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.utils.RbacUtils.RbacFaultType;

public class FsmUtils {

	static FsmUtils instance;

	public static  FsmUtils getInstance() {
		if(instance ==null){
			instance = new FsmUtils();
		}
		return instance;
	}

	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	DecimalFormat stateFormat;

	private FsmUtils() {
		stateFormat = new DecimalFormat("000");
	}

//	public void fsmDiff(FsmModel f1, FsmModel f2, File fDiff) throws FileNotFoundException{
//		String edgeColor = "";
//		PrintWriter pw = new PrintWriter(fDiff);
//		pw.println("digraph rbac2Fsm {");
//		pw.println("  {");
//		pw.println("  node [style=filled]");
//		for (FsmState s : f1.getStates()) {
//			if(!f2.getStates().contains(s)){
//				pw.println("  "+s.getName()+" [color=red]");
//			}	
//		}
//		pw.println("  }");
//		Set<FsmTransition> allTransitions = new HashSet<FsmTransition>();
//		allTransitions.addAll(f1.getTransitions());
//		allTransitions.addAll(f2.getTransitions());
//		for (FsmTransition tr : allTransitions) {
//			if(tr.getOutput().equals("grant")){
//				if(f1.getTransitions().contains(tr) && f2.getTransitions().contains(tr)){
//					edgeColor = "";
//				}else if(f1.getTransitions().contains(tr)){
//					edgeColor = ", color=red";
//				}else if(f2.getTransitions().contains(tr)){
//					edgeColor = ", color=green";
//				}	
//				pw.println("  "+
//						tr.getFrom().getName()
//						+" -> "
//						+tr.getTo().getName()
//						+" [ label =\""+tr.getInput()+"/"+tr.getOutput()+"\""+edgeColor+"];");
//			}
//		}
//		pw.println("}");
//		pw.close();
//
//	}


	public FsmState getState(Collection<FsmState> states2, int id){
		for (FsmState fsmState : states2) {
			if(fsmState.getId() == id){
				return fsmState;
			}
		}
		return null;
	}

	FsmTransition getTransition(FsmModel fsm, String toStr){
		for (FsmTransition tr : fsm.getTransitions()) {
			if(tr.toString().equals(toStr)){
				return tr;
			}
		}
		return null;
	}

	public FsmModel loadFsmFromXML(File fsmFile)  throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {
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
			fsm.getStates().add(new FsmState(Integer.valueOf(in.getAttribute("name"))));
		}

		fsm.setInitialState(fsm.getState(Integer.valueOf(fsmElement.getAttribute("initialState"))));
		node = fsmElement.getElementsByTagName("transitions").item(0);
		el = ((Element)node).getElementsByTagName("transition");
		for (int i = 0; i < el.getLength(); i++) {
			Element in = (Element) el.item(i);
			FsmState f = fsm.getState(Integer.valueOf(in.getAttribute("from")));
			String input = in.getAttribute("in");
			String output = in.getAttribute("out");
			FsmState t = fsm.getState(Integer.valueOf(in.getAttribute("to")));
			fsm.addTransition(new FsmTransition(f, input, output, t));
		}

		node = fsmElement.getElementsByTagName("failures").item(0);
		el = ((Element)node).getElementsByTagName("failure");
		for (int i = 0; i < el.getLength(); i++) {
			Element in = (Element) el.item(i);
			String constraint = in.getAttribute("constraint");
			String transition = in.getAttribute("transition");
			String faultType = in.getAttribute("type");
			FsmTransition tr = getTransition(fsm, transition);
			tr.getProperties().putIfAbsent(faultType, new HashSet<String>());
			Set<String> constraintsFailed = (Set<String>)tr.getProperties().get(faultType);
			constraintsFailed.add(constraint);
		}

		return fsm;
	}

	public FsmModel rbac2FsmConcurrent(RbacPolicy rbac) {
		RbacAcut acut = new RbacAcut(rbac);

		int threadsNum = Runtime.getRuntime().availableProcessors();
		Rbac2FsmConcurrent_BFS rbac2FsmConc = new Rbac2FsmConcurrent_BFS(acut,threadsNum);

		rbac2FsmConc.start();

		return rbac2FsmConc.getFsmModel();
	}

	public void sorting(FsmModel fsmGenerated) {
		fsmGenerated.getStates()		.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		fsmGenerated.getTransitions()	.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		fsmGenerated.getInputs()		.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		fsmGenerated.getOutputs()		.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));

	}

	public void updateElements(FsmModel fsmModel) {
		for (FsmState s: fsmModel.getStates()) {
			s.getIn().clear();
			s.getOut().clear();
		}
		for (FsmTransition tr : fsmModel.getTransitions()) {
			FsmState fr = fsmModel.getState(tr.getFrom().getId());
			FsmState to = fsmModel.getState(tr.getTo().getId());
			tr.setFrom(fr);
			tr.setTo(to);
			if(!fr.getOut().contains(tr)) fr.getOut().add(tr);
			if(!to.getIn().contains(tr)) to.getIn().add(tr);

		}

	}

	public void WriteFsm(FsmModel fsm, File fsmFile)  throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
		sorting(fsm);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement("FSM");
		rootElement.setAttribute("initialState",Integer.toString(fsm.getInitialState().getId()));
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
			child.setAttribute("name", Integer.toString(s.getId()));
			states.appendChild(child);
		}
		rootElement.appendChild(states);

		Element transitions = doc.createElement("transitions");
		for (FsmTransition t: fsm.getTransitions()) {
			Element transition = doc.createElement("transition");
			transition.setAttribute("from", Integer.toString(t.getFrom().getId()));
			transition.setAttribute("in", t.getInput());
			transition.setAttribute("out", t.getOutput());
			transition.setAttribute("to", Integer.toString(t.getTo().getId()));
			transitions.appendChild(transition);
		}
		rootElement.appendChild(transitions);

		Element failures = doc.createElement("failures");
		for (FsmTransition t: fsm.getTransitions()) {

			if(!t.getProperties().isEmpty()) {
				//				Enumeration<Object> keys = t.getProperties().keys();
				//				while (keys.hasMoreElements()) {
				for (RbacFaultType faultType: RbacFaultType.values()) {
//					String k = (String) keys.nextElement();
					if(!t.getProperties().containsKey(faultType)) continue;
					Set<RbacMutableElement> fEls = (Set<RbacMutableElement>) t.getProperties().get(faultType);
					for (RbacMutableElement el : fEls) {
						Element failure = doc.createElement("failure");
						failure.setAttribute("transition", t.toString());
						failure.setAttribute("type", faultType.name());
						failure.setAttribute("constraint", el.toString());
						failures.appendChild(failure);
					}
				}
			}
		}
		rootElement.appendChild(failures);

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
	/*
	public void WriteFsmAsCsv(FsmModel fsm, File f) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(f);

		List<FsmTransition> transit = fsm.getTransitions();
		pw.println("\"origin\",\"input\",\"output\",\"destination\"");
		for (FsmTransition tr : transit) {
			//			if(tr.getOutput().equals("deny")) continue;
			pw.println(
					"\""+tr.getFrom().getId()+"\","
							+"\""+tr.getInput()+"\","
							+"\""+tr.getOutput()+"\","
							+"\""+tr.getTo().getId()+"\",");
		}
		pw.println("}");
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

	public void WriteFsmAsJff(FsmModel fsm, File jff)  throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
		sorting(fsm);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement("structure");
		Element inputs = doc.createElement("type");
		inputs.appendChild(inputs.getOwnerDocument().createTextNode("mealy"));
		rootElement.appendChild(inputs);

		inputs = doc.createElement("automaton");


		for (FsmState s: fsm.getStates()) {
			Element child = doc.createElement("state");
			child.setAttribute("id", Integer.toString(fsm.getStates().indexOf(s)));
			child.setAttribute("name", s.getName());
			if(s.equals(fsm.getInitialState())){
				child.appendChild(doc.createElement("initial"));
			}
			inputs.appendChild(child);
		}
		for (FsmTransition t: fsm.getTransitions()) {
			Element transition = doc.createElement("transition");
			Element from = doc.createElement("from");
			from.appendChild(from.getOwnerDocument().createTextNode(Integer.toString(fsm.getStates().indexOf(t.getFrom()))));
			Element to = doc.createElement("to");
			to.appendChild(to.getOwnerDocument().createTextNode(Integer.toString(fsm.getStates().indexOf(t.getTo()))));
			Element read = doc.createElement("read");
			read.appendChild(read.getOwnerDocument().createTextNode(t.getInput()));
			Element transout = doc.createElement("transout");
			transout.appendChild(transout.getOwnerDocument().createTextNode(t.getOutput()));
			transition.appendChild(from);
			transition.appendChild(to);
			transition.appendChild(read);
			transition.appendChild(transout);
			inputs.appendChild(transition);
		}
		rootElement.appendChild(inputs);

		doc.appendChild(rootElement);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(jff);

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		//		OutputStream fos = new FileOutputStream(fsmFile);
		//		xstream.toXML(fsm, fos);
	}

	public void WriteFsmAsKiss(FsmModel fsm, File f) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(f);

		List<FsmTransition> transit = new ArrayList<FsmTransition>();
		transit.addAll(fsm.getTransitions());
		putInitialAsFirst(transit,fsm.getInitialState());

		for (FsmTransition tr : transit) {
			//			if(tr.getOutput().equals("deny")) continue;
			pw.println(tr.getFrom().getName()
					+" -- "
					+tr.getInput()+" / "+tr.getOutput()
					+" -> "
					+tr.getTo().getName());
		}
		pw.close();
	}
	 */
	public void WriteFsmAsKissSimple(FsmModel fsm, File f) throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(f);

		int from;
		int in;
		int out;
		int to;
		
		List<FsmTransition> transit = new ArrayList<FsmTransition>();
		transit.addAll(fsm.getTransitions());
		putInitialAsFirst(transit,fsm.getInitialState());
		
		for (FsmTransition tr : transit) {
			from = fsm.getStates().indexOf(tr.getFrom());
			in = fsm.getInputs().indexOf(tr.getInput());
			out = fsm.getOutputs().indexOf(tr.getOutput());
			to = fsm.getStates().indexOf(tr.getTo());
			pw.println(Integer.toString(from)
					+" -- "
					+stateFormat.format(in)+" / "+Integer.toString(out)
					+" -> "
					+Integer.toString(to));
		}
		pw.close();
	}

	private void putInitialAsFirst(List<FsmTransition> transit, FsmState initial) {
		List<FsmTransition> initStateTrs = new ArrayList<FsmTransition>();
		for (int i = 0; i < transit.size(); i++) {
			if(transit.get(i).getFrom().equals(initial)){
				initStateTrs.add(transit.get(i));
			}
		}
		for (FsmTransition tr : initStateTrs) {
			transit.remove(tr);
			transit.add(0,tr);
		}
	}
}