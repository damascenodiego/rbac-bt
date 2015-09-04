package com.usp.icmc.labes.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

import org.apache.commons.cli.CommandLine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmSUT;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestStatistics;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.fsm.testing.FsmTestSuiteIterator;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.utils.RbacUtils.RbacFaultType;

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



	public FsmTestSuite loadFsmTestSuiteFromFile(File fSuite) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {

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
				Integer from = 	Integer.valueOf(((Element)((NodeList)subEl).item(j)).getAttribute("from"));
				String in 	=	((Element)((NodeList)subEl).item(j)).getAttribute("in");
				String out 	= 	((Element)((NodeList)subEl).item(j)).getAttribute("out");
				Integer to 	= 	Integer.valueOf(((Element)((NodeList)subEl).item(j)).getAttribute("to"));

				FsmState f = states.getOrDefault(from, new FsmState(from));
				FsmState t = states.getOrDefault(to, new FsmState(to));
				testCase.addTransition(new FsmTransition(f, in, out, t));
			}
			suite.getTestCases().add(testCase);
		}

		return suite;
	}

	//	public List<RbacTestConfiguration> loadRbacTestConfiguration(File testCnfFile) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
	//		List<RbacTestConfiguration> cfgs = new ArrayList<RbacTestConfiguration>();
	//
	//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	//		Document doc = dBuilder.parse(testCnfFile);
	//		doc.getDocumentElement().normalize();
	//		Element fsmElement = doc.getDocumentElement();
	//		NodeList el = ((Element)fsmElement).getElementsByTagName("SUT_RBAC");
	//		for (int i = 0; i < el.getLength(); i++) {
	//			Element sutRbac = (Element)el.item(i);
	//			if(sutRbac.hasAttribute("ignore") && sutRbac.getAttribute("ignore").equalsIgnoreCase("true")) continue;
	//
	//			RbacTestConfiguration out = new RbacTestConfiguration();
	//			out.setName(sutRbac.getAttribute("name"));
	//			out.setPath(testCnfFile.getParentFile());
	//			
	//
	//			if(sutRbac.hasAttribute("type")){
	//				switch (sutRbac.getAttribute("type")) {
	//				case "generator":
	//					out.setTestConfigurationType(RbacTestConfiguration.ConfigurationType.TEST_GENERATOR);
	//					break;
	//				case "executor":
	//				default:
	//					out.setTestConfigurationType(RbacTestConfiguration.ConfigurationType.TEST_EXECUTION);
	//					break;
	//				}
	//			}
	//
	//			Node sutSpecNode = sutRbac.getElementsByTagName("SUT_SPEC").item(0);
	//			loadSutSpecFromNode(out,sutSpecNode);
	//
	//			NodeList testsuites = sutRbac.getElementsByTagName("TESTSUITE");
	//			for (int j = 0; j < testsuites.getLength(); j++) {
	//				Element in = (Element)testsuites.item(j);
	//				if(in.hasAttribute("ignore") && in.getAttribute("ignore").equalsIgnoreCase("true")) continue;
	//				out.getTestSuites().add(loadTestSuiteFromNode(out,in));
	//			}
	//
	//			NodeList sutMutants = sutRbac.getElementsByTagName("SUT_MUTANT");
	//			for (int j = 0; j < sutMutants.getLength(); j++) {
	//				Element in = (Element) sutMutants.item(j);
	//				if(in.hasAttribute("equiv") && in.getAttribute("equiv").equalsIgnoreCase("true")) continue;
	//				if(in.hasAttribute("ignore") && in.getAttribute("ignore").equalsIgnoreCase("true")) continue;
	//				File mutPolFile = new File(testCnfFile.getParentFile(),in.getAttribute("name"));
	//				RbacPolicy mutPol = rbacUtils.loadRbacPolicyFromXML(mutPolFile);
	//				RbacAcut mutAcut = new RbacAcut(mutPol);
	//				out.getRbacMutants().add(mutAcut);
	//			}
	//			cfgs.add(out);
	//		}
	//		return cfgs;
	//	}
	////
	////	private void loadSutSpecFromNode(RbacTestConfiguration out, Node sutSpecNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
	////		String filename = ((Element)sutSpecNode).getAttribute("name");
	////		out.setRbacSpecification(fsmUtils.LoadFsmFromXML(new File(out.getPath(),filename)));
	////	}
	//
	//	private FsmTestSuite loadTestSuiteFromNode(RbacTestConfiguration tConf, Node testSuiteNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
	//		String filename = ((Element)testSuiteNode).getAttribute("name");
	//		FsmTestSuite result = null;
	//		switch (((Element)testSuiteNode).getAttribute("format")) {
	//		case "fsm":
	//			result = LoadFsmTestSuiteFromFile(new File(tConf.getPath(),filename));
	//			break;
	//		case "kk":
	//			result = LoadFsmTestSuiteFromKK(tConf,new File(tConf.getPath(),filename));
	//			break;
	//		default:
	//			break;
	//		}
	//		if(result !=null && ((Element)testSuiteNode).hasAttribute("shortname")) {
	//			result.setGeneratedBy(((Element)testSuiteNode).getAttribute("shortname"));
	//		}
	//		if(result !=null && ((Element)testSuiteNode).hasAttribute("generatedBy")) {
	//			result.setGeneratedBy(((Element)testSuiteNode).getAttribute("generatedBy"));
	//		}
	//
	//		return result;
	//	}

	public FsmTestSuite loadFsmTestSuiteFromKK(RbacPolicy sutRbac, File file) throws FileNotFoundException,IOException  {

		RbacAcut acut = new RbacAcut(sutRbac);

		List<RbacRequest> rqs = rbacUtils.generateRequests(acut);
		rqs.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));

		FsmTestSuite testSuite = new FsmTestSuite(file.getName());
		testSuite.setName(file.getName());
		testSuite.setGeneratedBy(file.getName());
		BufferedReader br = new BufferedReader(new FileReader(file));
		while (br.ready()) {
			String line = br.readLine();
			if(line.isEmpty()) continue;
			FsmTestCase tc = new FsmTestCase();
			for (int i = 0; i <= line.length()-3; i+=3) {
				String inStr = line.substring(i, i+3);
				int inInt = Integer.valueOf(inStr);
				FsmTransition transition = new FsmTransition();
				transition.setInput(rqs.get(inInt).toString());
				tc.getPath().add(transition );
			}
			testSuite.getTestCases().add(tc);

		}
		br.close();


		Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();
		Map<Integer, FsmState> stateMap = new HashMap<Integer, FsmState>();

		List<FsmTestCase> testList = new ArrayList<>();
		testList.addAll(testSuite.getTestCases());
		for (FsmTestCase testCase : testList) {
			for (FsmTransition tr: testCase.getPath()) {
				rqMap.putIfAbsent(tr.getInput(), rbacUtils.createRbacRequest(tr.getInput(),acut));

				FsmState state = rbacUtils.rbacToFsmState(acut);
				stateMap.putIfAbsent(state.getId(),state);
				tr.setFrom(stateMap.get(state.getId()));
				boolean outBool = acut.request(rqMap.get(tr.getInput()));
				testSuite.getProperties().put(tr.getInput(),rqs.indexOf(rqMap.get(tr.getInput())));

				state = rbacUtils.rbacToFsmState(acut);
				stateMap.putIfAbsent(state.getId(),state);
				tr.setTo(stateMap.get(state.getId()));
				tr.setOutput((outBool?"grant":"deny"));
				
				for (RbacFaultType faultType: RbacFaultType.values()) {
					tr.getProperties().putIfAbsent(faultType, new HashSet<>());
					if(!acut.getPolicy().getProperties().containsKey(faultType)) continue;
					((Set) tr.getProperties().get(faultType)).addAll((Set) acut.getPolicy().getProperties().get(faultType));
				}
//				System.out.println(tr);
			}
			acut.reset();
		}

		return testSuite;
	}

	
	public FsmTestSuite loadRandomSubsetFsmTestSuiteFromKK(RbacPolicy sutRbac, File file, int limit) throws FileNotFoundException,IOException  {
		BufferedReader br = new BufferedReader(new FileReader(file));
		int totTests = 0;
		while (br.readLine()!=null) totTests++;
		List<Integer> selectedTestIndex = new ArrayList<Integer>();
		for (int i = 0; i < totTests; i++) {
			selectedTestIndex.add(Integer.valueOf(i));
		}
		br.close();

		Collections.shuffle(selectedTestIndex,RandomGenerator.getInstance().getRnd());
		while (selectedTestIndex.size()!=limit) selectedTestIndex.remove(selectedTestIndex.size()-1);		
		
		RbacAcut acut = new RbacAcut(sutRbac);
		

		List<RbacRequest> rqs = rbacUtils.generateRequests(acut);
		rqs.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));

		FsmTestSuite testSuite = new FsmTestSuite(file.getName());
		testSuite.setName(file.getName());
		testSuite.setGeneratedBy(file.getName());
		br = new BufferedReader(new FileReader(file));
		int counter = 0;
		while (br.ready()) {
			if(testSuite.getTestCases().size()==selectedTestIndex.size()) break;
			if(!selectedTestIndex.contains(counter)) {
				counter++;
				continue;
			}
			String line = br.readLine();
			if(line.isEmpty()) continue;
			FsmTestCase tc = new FsmTestCase();
			for (int i = 0; i <= line.length()-3; i+=3) {
				String inStr = line.substring(i, i+3);
				int inInt = Integer.valueOf(inStr);
				FsmTransition transition = new FsmTransition();
				transition.setInput(rqs.get(inInt).toString());
				tc.getPath().add(transition );
			}
			testSuite.getTestCases().add(tc);
			counter++;
		}
		br.close();


		Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();
		Map<Integer, FsmState> stateMap = new HashMap<Integer, FsmState>();

		List<FsmTestCase> testList = new ArrayList<>();
		testList.addAll(testSuite.getTestCases());
		for (FsmTestCase testCase : testList) {
			for (FsmTransition tr: testCase.getPath()) {
				rqMap.putIfAbsent(tr.getInput(), rbacUtils.createRbacRequest(tr.getInput(),acut));

				FsmState state = rbacUtils.rbacToFsmState(acut);
				stateMap.putIfAbsent(state.getId(),state);
				tr.setFrom(stateMap.get(state.getId()));
				boolean outBool = acut.request(rqMap.get(tr.getInput()));
				testSuite.getProperties().put(tr.getInput(),rqs.indexOf(rqMap.get(tr.getInput())));

				state = rbacUtils.rbacToFsmState(acut);
				stateMap.putIfAbsent(state.getId(),state);
				tr.setTo(stateMap.get(state.getId()));
				tr.setOutput((outBool?"grant":"deny"));
				
				for (RbacFaultType faultType: RbacFaultType.values()) {
					tr.getProperties().putIfAbsent(faultType, new HashSet<>());
					if(!acut.getPolicy().getProperties().containsKey(faultType)) continue;
					((Set) tr.getProperties().get(faultType)).addAll((Set) acut.getPolicy().getProperties().get(faultType));
				}
//				System.out.println(tr);
			}
			acut.reset();
		}

		return testSuite;
	}



	public FsmTestSuite randomTestSuite(RbacPolicy sutRbac, int resets, int length) {
		List<RbacRequest> rqs = rbacUtils.generateRequests(sutRbac);
		rqs.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));

		FsmTestSuite tSuite = new FsmTestSuite(sutRbac.getName());
		tSuite.setGeneratedBy("random."+resets+"."+length);

		for (int i = 0; i < resets; i++) {
			FsmTestCase tc = new FsmTestCase();
			for (int j = 0; j < length; j++) {
				int rndInputIndex = RandomGenerator.getInstance().getRnd().nextInt(rqs.size());
				FsmTransition tr = new FsmTransition();
				tr.setInput(rqs.get(rndInputIndex).toString());
				tSuite.getProperties().put(rqs.get(rndInputIndex).toString(), rndInputIndex);
				tc.addTransition(tr);
			}
			tSuite.getTestCases().add(tc);
		}


		return tSuite;

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
					if(qSets.get(indexTo).getInitialState().getId() == (tr.getTo().getId())){
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
				ts.setAttribute("from",Integer.toString(trIn.getFrom().getId()));
				ts.setAttribute("in",trIn.getInput());
				ts.setAttribute("out",trIn.getOutput());
				ts.setAttribute("to",Integer.toString(trIn.getTo().getId()));
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

	public void printTestSuiteCharacteristics(FsmTestSuite testSuite) {
		System.out.print("testSuite"+"\t");
		System.out.print("noResets"+"\t");
		System.out.print("testSuiteLength"+"\t");
		System.out.print("testSuiteLengthNoRst"+"\t");
		System.out.print("minLength"+"\t");
		System.out.print("avgLength"+"\t");
		System.out.print("maxLength"+"\t");
		System.out.print("sdLength"+"\t");
		System.out.print("varLength"+"\t");
		System.out.print("\n");


		FsmTestStatistics tStats = new FsmTestStatistics(testSuite);
		System.out.print(testSuite.getName()+"\t");
		System.out.print(Long.toString(	tStats.getNoResets					())+"\t");
		System.out.print(Long.toString(	tStats.getTestSuiteLength			())+"\t");
		System.out.print(Long.toString(	tStats.getTestSuiteLengthNoResets	())+"\t");
		System.out.print(Long.toString(	tStats.getMinLength					())+"\t");
		System.out.print(Double.toString(	tStats.getAvgLength				())+"\t");
		System.out.print(Long.toString(	tStats.getMaxLength					())+"\t");
		System.out.print(Double.toString(	tStats.getSdLength				())+"\t");
		System.out.print(Double.toString(	tStats.getVarLength				())+"\t");
		System.out.print("\n");

	}
	
	public void printTestSuiteCharacteristics(FsmTestSuiteIterator testSuite) {
		System.out.print("testSuite"+"\t");
		System.out.print("noResets"+"\t");
		System.out.print("testSuiteLength"+"\t");
		System.out.print("testSuiteLengthNoRst"+"\t");
		System.out.print("minLength"+"\t");
		System.out.print("avgLength"+"\t");
		System.out.print("maxLength"+"\t");
		System.out.print("sdLength"+"\t");
		System.out.print("varLength"+"\t");
		System.out.print("\n");

		FsmTestStatistics tStats = new FsmTestStatistics(testSuite);
		System.out.print(testSuite.getName()+"\t");
		System.out.print(Long.toString(	tStats.getNoResets					())+"\t");
		System.out.print(Long.toString(	tStats.getTestSuiteLength			())+"\t");
		System.out.print(Long.toString(	tStats.getTestSuiteLengthNoResets	())+"\t");
		System.out.print(Long.toString(	tStats.getMinLength					())+"\t");
		System.out.print(Double.toString(	tStats.getAvgLength				())+"\t");
		System.out.print(Long.toString(	tStats.getMaxLength					())+"\t");
		System.out.print(Double.toString(	tStats.getSdLength				())+"\t");
		System.out.print(Double.toString(	tStats.getVarLength				())+"\t");
		System.out.print("\n");

	}
	public void printConformanceTestingStatistics(RbacPolicy sutRbac, FsmTestSuite testSuite, List<RbacPolicy> mutants) {


		RbacAcut acutSut= createAcutFromRbacPolicy(sutRbac);
		List<RbacAcut> acutMutant = createAcutFromRbacPolicy(mutants);

		Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();

		List<RbacPolicy> killed  = new ArrayList<RbacPolicy>();

		for (int i = 0; i < testSuite.getTestCases().size(); i++) {

			FsmTestCase tc = testSuite.getTestCases().get(i);
			for (int j = 0; j < tc.getPath().size(); j++) {
				FsmTransition tr = tc.getPath().get(j);

				rqMap.putIfAbsent(tr.getInput(), rbacUtils.createRbacRequest(tr.getInput(),acutSut));

				boolean specBool = acutSut.request(rqMap.get(tr.getInput()));

				for (RbacAcut rbacAcut : acutMutant) {
					if(killed.contains(rbacAcut.getPolicy())) continue;
					boolean mutBool = rbacAcut.request(rqMap.get(tr.getInput()));
					if(specBool ^ mutBool){
						killed.add(rbacAcut.getPolicy());
					}

				}
			}
			acutSut.reset();
			for (RbacAcut rbacAcut : acutMutant)  rbacAcut.reset();
		}

		int totKilled = killed.size();
		double score = ((double)totKilled)/(mutants.size());

		System.out.print(sutRbac.getName()+"\t");
		System.out.print(mutants.size()+"\t");
		System.out.print(testSuite.getGeneratedBy()+"\t");
		System.out.print(score);
		System.out.print("\n\n");

		Set<String> alivePolNames = new HashSet<String>();
		for (RbacPolicy pol : mutants) alivePolNames.add(pol.getName());

		Set<String> killedPolNames = new HashSet<String>();
		for (RbacPolicy pol : killed) killedPolNames.add(pol.getName());

		alivePolNames.removeAll(killedPolNames);
		for (String polName : alivePolNames) {
			System.out.println(polName);
		}
	}

	public void printConformanceTestingStatistics(RbacPolicy sutRbac, FsmTestSuiteIterator testSuiteIter,
			List<RbacPolicy> mutants) throws IOException {
		RbacAcut acutSut= createAcutFromRbacPolicy(sutRbac);
		List<RbacAcut> acutMutant = createAcutFromRbacPolicy(mutants);

		Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();

		List<RbacPolicy> killed  = new ArrayList<RbacPolicy>();

		testSuiteIter.openFile();
		while(testSuiteIter.hasNextTestCase()){
			FsmTestCase tc = testSuiteIter.nextTestCase();
			
			for (FsmTransition tr: tc.getPath()) {

				rqMap.putIfAbsent(tr.getInput(), rbacUtils.createRbacRequest(tr.getInput(),acutSut));

				boolean specBool = acutSut.request(rqMap.get(tr.getInput()));

				for (RbacAcut rbacAcut : acutMutant) {
					if(killed.contains(rbacAcut.getPolicy())) continue;
					boolean mutBool = rbacAcut.request(rqMap.get(tr.getInput()));
					if(specBool ^ mutBool){
						killed.add(rbacAcut.getPolicy());
					}

				}
			}
			acutSut.reset();
			for (RbacAcut rbacAcut : acutMutant)  rbacAcut.reset();
		}

		long totKilled = killed.size();
		double score = ((double)totKilled)/(mutants.size());

		System.out.print(sutRbac.getName()+"\t");
		System.out.print(mutants.size()+"\t");
		System.out.print(testSuiteIter.getGeneratedBy()+"\t");
		System.out.print(score);
		System.out.print("\n\n");

		Set<String> alivePolNames = new HashSet<String>();
		for (RbacPolicy pol : mutants) alivePolNames.add(pol.getName());

		Set<String> killedPolNames = new HashSet<String>();
		for (RbacPolicy pol : killed) killedPolNames.add(pol.getName());

		alivePolNames.removeAll(killedPolNames);
		for (String polName : alivePolNames) {
			System.out.println(polName);
		}
		testSuiteIter.close();
	}



	private List<RbacAcut> createAcutFromRbacPolicy(List<RbacPolicy> mutants) {
		List<RbacAcut> acuts = new ArrayList<>();
		for (RbacPolicy mut : mutants)  acuts.add(new RbacAcut(mut));
		return acuts;
	}


	private RbacAcut createAcutFromRbacPolicy(RbacPolicy sutRbac) {
		RbacAcut acut = new RbacAcut(sutRbac);
		return acut;
	}

	DecimalFormat stateFormat = new DecimalFormat("000");

	public void WriteFsmTestSuiteAsKK(FsmTestSuite tsuite, File f) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(f);

		for (FsmTestCase tc : tsuite.getTestCases()) {
			boolean printed = false;
			for (FsmTransition tr : tc.getPath()) {
				if(tr.getInput() == null || tr.getInput().length()==0) continue;
				int in = (Integer)tsuite.getProperties().get(tr.getInput());
				String inStr = stateFormat.format(in);
				pw.print(inStr);
				printed=true;
			}
			if(printed) pw.print("\n");
		}
		pw.close();

	}


	public void setupTestFsmProperties(FsmModel fsm, FsmTestSuite suite) {
		for (String in : fsm.getInputs()) {
			if(!suite.getProperties().containsKey(in))
				suite.getProperties().put(in, Integer.valueOf(fsm.getInputs().indexOf(in)));
		}
	}


	public FsmTestSuite selectSubset(FsmTestSuite testSuite, int i) {
		FsmTestSuite subset = new FsmTestSuite();
		subset.setName(testSuite.getName());
		for (int j = 0; j < testSuite.getTestCases().size(); j++) {
			if(subset.getTestCases().size()==i) break;
			subset.getTestCases().add(testSuite.getTestCases().get(j));
		}
		for (Object key : testSuite.getProperties().keySet()) {
			subset.getProperties().put(key,testSuite.getProperties().get(key));
		}
		return subset;
	}


	//	public List<RbacTestConfiguration> loadRbacTestConfiguration(File testCnfFile) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
	//		List<RbacTestConfiguration> cfgs = new ArrayList<RbacTestConfiguration>();
	//
	//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	//		Document doc = dBuilder.parse(testCnfFile);
	//		doc.getDocumentElement().normalize();
	//		Element fsmElement = doc.getDocumentElement();
	//		NodeList el = ((Element)fsmElement).getElementsByTagName("SUT_RBAC");
	//		for (int i = 0; i < el.getLength(); i++) {
	//			Element sutRbac = (Element)el.item(i);
	//			if(sutRbac.hasAttribute("ignore") && sutRbac.getAttribute("ignore").equalsIgnoreCase("true")) continue;
	//
	//			RbacTestConfiguration out = new RbacTestConfiguration();
	//			out.setName(sutRbac.getAttribute("name"));
	//			out.setPath(testCnfFile.getParentFile());
	//			
	//
	//			if(sutRbac.hasAttribute("type")){
	//				switch (sutRbac.getAttribute("type")) {
	//				case "generator":
	//					out.setTestConfigurationType(RbacTestConfiguration.ConfigurationType.TEST_GENERATOR);
	//					break;
	//				case "executor":
	//				default:
	//					out.setTestConfigurationType(RbacTestConfiguration.ConfigurationType.TEST_EXECUTION);
	//					break;
	//				}
	//			}
	//
	//			Node sutSpecNode = sutRbac.getElementsByTagName("SUT_SPEC").item(0);
	//			loadSutSpecFromNode(out,sutSpecNode);
	//
	//			NodeList testsuites = sutRbac.getElementsByTagName("TESTSUITE");
	//			for (int j = 0; j < testsuites.getLength(); j++) {
	//				Element in = (Element)testsuites.item(j);
	//				if(in.hasAttribute("ignore") && in.getAttribute("ignore").equalsIgnoreCase("true")) continue;
	//				out.getTestSuites().add(loadTestSuiteFromNode(out,in));
	//			}
	//
	//			NodeList sutMutants = sutRbac.getElementsByTagName("SUT_MUTANT");
	//			for (int j = 0; j < sutMutants.getLength(); j++) {
	//				Element in = (Element) sutMutants.item(j);
	//				if(in.hasAttribute("equiv") && in.getAttribute("equiv").equalsIgnoreCase("true")) continue;
	//				if(in.hasAttribute("ignore") && in.getAttribute("ignore").equalsIgnoreCase("true")) continue;
	//				File mutPolFile = new File(testCnfFile.getParentFile(),in.getAttribute("name"));
	//				RbacPolicy mutPol = rbacUtils.loadRbacPolicyFromXML(mutPolFile);
	//				RbacAcut mutAcut = new RbacAcut(mutPol);
	//				out.getRbacMutants().add(mutAcut);
	//			}
	//			cfgs.add(out);
	//		}
	//		return cfgs;
	//	}
	////
	////	private void loadSutSpecFromNode(RbacTestConfiguration out, Node sutSpecNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
	////		String filename = ((Element)sutSpecNode).getAttribute("name");
	////		out.setRbacSpecification(fsmUtils.LoadFsmFromXML(new File(out.getPath(),filename)));
	////	}
	//
	//	private FsmTestSuite loadTestSuiteFromNode(RbacTestConfiguration tConf, Node testSuiteNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
	//		String filename = ((Element)testSuiteNode).getAttribute("name");
	//		FsmTestSuite result = null;
	//		switch (((Element)testSuiteNode).getAttribute("format")) {
	//		case "fsm":
	//			result = LoadFsmTestSuiteFromFile(new File(tConf.getPath(),filename));
	//			break;
	//		case "kk":
	//			result = LoadFsmTestSuiteFromKK(tConf,new File(tConf.getPath(),filename));
	//			break;
	//		default:
	//			break;
	//		}
	//		if(result !=null && ((Element)testSuiteNode).hasAttribute("shortname")) {
	//			result.setGeneratedBy(((Element)testSuiteNode).getAttribute("shortname"));
	//		}
	//		if(result !=null && ((Element)testSuiteNode).hasAttribute("generatedBy")) {
	//			result.setGeneratedBy(((Element)testSuiteNode).getAttribute("generatedBy"));
	//		}
	//
	//		return result;
	//	}


}