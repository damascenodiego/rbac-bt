package com.usp.icmc.labes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.RbacPolicy;

public class FsmTestingUtils {

	private static RbacAdministrativeCommands rbacAdmin = RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys = RbacSupportingSystemFunctions.getInstance();
	private static RbacUtils rbacUtils = RbacUtils.getInstance();
	private static RbacMutationUtils rbacMut = RbacMutationUtils.getInstance();
	private static FsmUtils fsmUtils = FsmUtils.getInstance();
	static FsmTestingUtils instance;

	
	
	public static  FsmTestingUtils getInstance() {
		if(instance ==null){
			instance = new FsmTestingUtils();
		}
		return instance;
	}

	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	private FsmTestingUtils() {
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

	public RbacTestConfiguration loadRbacTestConfiguration(File testCnfFile) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
		RbacTestConfiguration out = new RbacTestConfiguration();
	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(testCnfFile);
		doc.getDocumentElement().normalize();
		Element fsmElement = doc.getDocumentElement();
		out.setName(fsmElement.getAttribute("name"));
		out.setPath(testCnfFile.getParentFile());
		
		Node sutSpecNode = fsmElement.getElementsByTagName("SUT_SPEC").item(0);
		loadSutSpecFromNode(out,sutSpecNode);
		
		if(fsmElement.getElementsByTagName("TESTSUITE").getLength()>0){
			Node testSuiteNode = fsmElement.getElementsByTagName("TESTSUITE").item(0);
			loadTestSuiteFromNode(out,testSuiteNode);
		}

		
		Node sutMutants = fsmElement.getElementsByTagName("SUT_MUTANTS").item(0);
		NodeList el = ((Element)sutMutants).getElementsByTagName("SUT_MUTANT");
		for (int i = 0; i < el.getLength(); i++) {
			Element in = (Element) el.item(i);
			if(in.hasAttribute("equiv") && in.getAttribute("equiv").equalsIgnoreCase("true")) continue;
			File mutPolFile = new File(testCnfFile.getParentFile(),in.getAttribute("name"));
			RbacPolicy mutPol = rbacUtils.LoadRbacPolicyFromXML(mutPolFile);
			RbacAcut mutAcut = new RbacAcut(mutPol);
			out.getRbacMutant().add(mutAcut);
		}
		
		return out;
	}

	private void loadSutSpecFromNode(RbacTestConfiguration out, Node sutSpecNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		String filename = ((Element)sutSpecNode).getAttribute("name");
		out.setRbacSpecification(fsmUtils.LoadFsmFromXML(new File(out.getPath(),filename)));
	}

	private void loadTestSuiteFromNode(RbacTestConfiguration out, Node testSuiteNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		String filename = ((Element)testSuiteNode).getAttribute("name");
		FsmTestSuite testSuite = null;
		switch (((Element)testSuiteNode).getAttribute("type")) {
		case "fsm":
			testSuite = LoadFsmTestSuiteFromFile(new File(out.getPath(),filename));
			out.setTestSuite(testSuite);
			break;
		case "kk":
			testSuite = LoadFsmTestSuiteFromKK(out,new File(out.getPath(),filename));
			out.setTestSuite(testSuite);
			break;
		default:
			break;
		}
		out.setTestSuite(testSuite);
	}
	
	private FsmTestSuite LoadFsmTestSuiteFromKK(RbacTestConfiguration out, File file) throws FileNotFoundException,IOException  {
		FsmTestSuite ts = new FsmTestSuite(file.getName());
		ts.setGeneratedBy(file.getName());
		BufferedReader br = new BufferedReader(new FileReader(file));
		while (br.ready()) {
			String line = br.readLine();
			FsmTestCase tc = new FsmTestCase();
			for (int i = 0; i <= line.length()-3; i+=3) {
				String inStr = line.substring(i, i+3);
				int inInt = Integer.valueOf(inStr);
				FsmTransition transition = new FsmTransition();
				transition.setInput(out.getRbacSpecification().getInputs().get(inInt));
				tc.getPath().add(transition );
			}
			ts.getTestCases().add(tc);
			
		}
		br.close();
		return ts;
	}



	public FsmTestSuite stateCoverSet(FsmModel fsm){
		FsmTestSuite tSuite = new FsmTestSuite(fsm.getName());
		tSuite.setGeneratedBy("StateCoverSet");
		List<FsmTestCase> qSets = new ArrayList<FsmTestCase>();
		for (int i = 0; i < fsm.getStates().size(); i++) {
			qSets.add(new FsmTestCase());
			qSets.get(i).addTransition(new FsmTransition(fsm.getInitialState(), "", "", fsm.getInitialState()));
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
					if(qSets.get(indexFrom).getPath().size() > 1) qSets.get(indexTo).getPath().addAll(qSets.get(indexFrom).getPath().subList(1, qSets.get(indexFrom).getPath().size()-1));
					qSets.get(indexTo).addTransition(tr);
					if(qSets.get(indexTo).getInitialState().getName().equals(tr.getTo().getName())){
						visited.add(tr.getTo());
					}
					toVisit.add(tr.getTo());
				}
			}
		}
		tSuite.getTestCases().addAll(qSets);
		tSuite.getTestCases().sort((o1, o2) -> Integer.compare(o1.getPath().size(), o2.getPath().size()) );
		return tSuite;
	}
	
	public FsmTestSuite transitionCoverSet(FsmModel fsm) {
		FsmTestSuite qSet = stateCoverSet(fsm);
		FsmTestSuite pSet = new FsmTestSuite(fsm.getName());
		pSet.setGeneratedBy("TransitionCoverSet");

		for (FsmTestCase path : qSet.getTestCases()) {
			FsmState finalState = path.getFinalState();

			for (FsmTransition tr : finalState.getOut()) {
				if(tr.getInput().length()==0 && tr.getOutput().length()==0) continue;
				FsmTestCase pSetEl = new FsmTestCase();
				pSetEl.getPath().addAll(path.getPath());
				pSetEl.addTransition(tr);
				if(pSet.getTestCases().contains(pSetEl)) continue;
				pSet.getTestCases().add(pSetEl);
			}
		}
		return pSet;
	}

	public FsmTestSuite transitionTour(FsmModel fsm) {
		FsmTestSuite tt = new FsmTestSuite(fsm.getName());
		tt.setGeneratedBy("TransitionTour");

		CPP g = new CPP(fsm);
		List<FsmTransition> cptPath = g.getCPT();

		FsmTestCase  testCase = new FsmTestCase();
		testCase.getPath().addAll(cptPath);
		tt.getTestCases().add(testCase);
		
		return tt;
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

	public void WriteFsmTestSuiteAsTxt(FsmTestSuite suite, File fSuiteTxt) throws IOException {
		FileWriter result = new FileWriter(fSuiteTxt);
		

		result.write("#name: "+suite.getName()+"\n");
		result.write("#generatedBy: "+suite.getGeneratedBy()+"\n");
		result.write("#FsmTestSuiteLength: "+Integer.toString(suite.getTestCases().size())+"\n");
		int tcaseCount = 0;
		for (FsmTestCase in: suite.getTestCases()) {
			result.write("#FsmTestCase: "+Integer.toString(tcaseCount)+"\n");
			result.write("#FsmTestCaseLength: "+Integer.toString(in.getPath().size())+"\n");
			for (FsmTransition trIn: in.getPath()) {
				result.write(trIn.toString()+"\n");
			}
			tcaseCount++;
		}

		result.close();

	}
}