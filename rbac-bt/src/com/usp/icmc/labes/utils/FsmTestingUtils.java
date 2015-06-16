package com.usp.icmc.labes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import com.usp.icmc.labes.fsm.FsmPath;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestStep;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.rbac.model.User;

public class FsmTestingUtils {

	private FsmUtils fsmUtils = FsmUtils.getInstance();
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	static FsmTestingUtils instance;

	private FsmTestingUtils() {
	}

	public static  FsmTestingUtils getInstance() {
		if(instance ==null){
			instance = new FsmTestingUtils();
		}
		return instance;
	}



	public void WriteFsmTestSuite(FsmTestSuite suite, File fSuite) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement("FsmTestSuite");

		rootElement.setAttribute("name",suite.getName());
		rootElement.setAttribute("generatedBy",suite.getGeneratedBy());
		
		int testSuiteLength = 0;
		int noResets = 0;
		int tcaseCount = 0;
		for (FsmTestCase in: suite.getTestCases()) {
			Element tc = doc.createElement("FsmTestCase");
			tc.setAttribute("_id", Integer.toString(tcaseCount));
			noResets++;
			int testCaseLength = 0;
			for (FsmTransition trIn: in.getPath()) {
				Element ts = doc.createElement("FsmTestStep");
				ts.setAttribute("_id", Integer.toString(tcaseCount)+"."+Integer.toString(testCaseLength));
				ts.setAttribute("from",trIn.getFrom().getName());
				ts.setAttribute("in",trIn.getInput());
				ts.setAttribute("out",trIn.getOutput());
				ts.setAttribute("to",trIn.getTo().getName());
				tc.appendChild(ts);
				testCaseLength++;
			}
			testSuiteLength+=testCaseLength;
			tc.setAttribute("_testCaseLength", Integer.toString(testCaseLength));
			rootElement.appendChild(tc);
			tcaseCount++;
		}
		rootElement.setAttribute("_length", Integer.toString(testSuiteLength));
		rootElement.setAttribute("_noResets", Integer.toString(noResets));

		doc.appendChild(rootElement);


		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fSuite);

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
	}

	public FsmTestSuite LoadFsmTestSuiteFromFile(File fSuite) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {

		FsmTestSuite suite = new FsmTestSuite(); //(FsmModel) xstream.fromXML(fsmFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fSuite);
		doc.getDocumentElement().normalize();
		Element fsmElement = doc.getDocumentElement();
		suite.setName(fsmElement.getAttribute("name"));
		suite.setGeneratedBy(fsmElement.getAttribute("generatedBy"));

		Map<String,FsmState> states = new HashMap<String,FsmState>();
		NodeList el = null;
		NodeList subEl = null;

		for (int i = 0; i < fsmElement.getElementsByTagName("FsmTestCase").getLength(); i++) {
			el = (NodeList) fsmElement.getElementsByTagName("FsmTestCase").item(i);
			subEl = ((Element)el).getElementsByTagName("FsmTestStep");
			FsmTestCase testCase = new FsmTestCase(); 
			for (int j = 0; j < subEl.getLength(); j++) {
				String from = 	((Element)((NodeList)subEl).item(j)).getAttribute("from");
				String in 	=	((Element)((NodeList)subEl).item(j)).getAttribute("in");
				String out 	= 	((Element)((NodeList)subEl).item(j)).getAttribute("out");
				String to 	= 	((Element)((NodeList)subEl).item(j)).getAttribute("to");

				FsmState f = states.getOrDefault(from, new FsmState(from));
				FsmState t = states.getOrDefault(to, new FsmState(to));
				testCase.addTransition(new FsmTransition(f, in, out, t));
			}
			suite.getTestCases().add(testCase);
		}

		return suite;
	}

	public FsmTestSuite stateCoverSet(FsmModel fsm){
		FsmTestSuite tSuite = new FsmTestSuite(fsm.getName());
		tSuite.setGeneratedBy("StateCoverSet");
		FsmTestCase[] qSets = new FsmTestCase[fsm.getStates().size()];
		for (int i = 0; i < fsm.getStates().size(); i++) {
			qSets[i] = new FsmTestCase();
			qSets[i].addTransition(new FsmTransition(fsm.getInitialState(), "", "", fsm.getInitialState()));
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
					if(qSets[indexFrom].getPath().size() > 1) qSets[indexTo].getPath().addAll(qSets[indexFrom].getPath().subList(1, qSets[indexFrom].getPath().size()-1));
					qSets[indexTo].addTransition(tr);
					if(qSets[indexTo].getInitialState().getName().equals(tr.getTo().getName())){
						visited.add(tr.getTo());
					}
					toVisit.add(tr.getTo());
				}
			}
		}
		tSuite.setTestCases(Arrays.asList(qSets));
		return tSuite;
	}
	
	public FsmTestSuite transitionCoverSet(FsmModel fsm) {
		FsmTestSuite qSet = stateCoverSet(fsm);
		FsmTestSuite pSet = new FsmTestSuite(fsm.getName());
		pSet.setGeneratedBy("TransitionCoverSet");

		for (FsmTestCase path : qSet.getTestCases()) {
			FsmState finalState = path.getFinalState();

			for (FsmTransition tr : finalState.getOut()) {
				FsmTestCase pSetEl = new FsmTestCase();
				pSetEl.getPath().addAll(path.getPath());
				pSetEl.addTransition(tr);
				pSet.getTestCases().add(pSetEl);
			}
		}
		return pSet;
	}
}