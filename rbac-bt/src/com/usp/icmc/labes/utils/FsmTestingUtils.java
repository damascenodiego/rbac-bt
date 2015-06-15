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

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmPath;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestStep;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;

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
		
		Element tsAttr = null ;
		int count = 0;
		for (FsmTestCase in: suite.getTestCases()) {
			Element tc = doc.createElement("FsmTestCase");
			tc.setAttribute("name", Integer.toString(count));
			count++;
			for (FsmTransition trIn: in.getPath()) {
				Element ts = doc.createElement("FsmTestStep");
				
				tsAttr = doc.createElement("from");
				tsAttr.setAttribute("input",trIn.getFrom().getName());
				ts.appendChild(tsAttr);
				
				tsAttr = doc.createElement("in");
				tsAttr.setAttribute("input",trIn.getInput());
				ts.appendChild(tsAttr);
				
				tsAttr = doc.createElement("out");
				tsAttr.setAttribute("input",trIn.getOutput());
				ts.appendChild(tsAttr);
				
				tsAttr = doc.createElement("to");
				tsAttr.setAttribute("input",trIn.getTo().getName());
				ts.appendChild(tsAttr);
				
				tc.appendChild(ts);
			}
			rootElement.appendChild(tc);
		}
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

	public FsmTestSuite stateCoverSet(FsmModel fsm){
		FsmTestSuite tSuite = new FsmTestSuite("StateCoverSet");
		FsmTestCase[] qSets = new FsmTestCase[fsm.getStates().size()];
		for (int i = 0; i < fsm.getStates().size(); i++) {
			qSets[i] = new FsmTestCase(fsm.getStates().get(i).getName());
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
		tSuite.setTestCases(Arrays.asList(qSets));
		return tSuite;
	}

	public FsmTestSuite transitionCoverSet(FsmModel fsm) {
		FsmTestSuite qSet = stateCoverSet(fsm);
		FsmTestSuite pSet = new FsmTestSuite("TransitionCoverSet");

		for (FsmTestCase path : qSet.getTestCases()) {
			FsmState finalState = null;
			if(path.getPath().isEmpty()){
				finalState = fsm.getInitialState();
			}
			else{
				finalState = path.getFinalState();
			}

			for (FsmTransition tr : finalState.getOut()) {
				FsmTestCase pSetEl = new FsmTestCase(finalState.getName()+"+"+tr.getInput());
				pSetEl.getPath().addAll(path.getPath());
				pSetEl.addTransition(tr);
				pSet.getTestCases().add(pSetEl);
			}
		}
		return pSet;
	}
}