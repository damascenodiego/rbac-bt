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
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
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

	public FsmTestSuite loadFsmTestSuiteFromKK(FsmModel model, File file) throws FileNotFoundException,IOException  {
		FsmTestSuite ts = new FsmTestSuite(file.getName());
		ts.setGeneratedBy(file.getName());
		BufferedReader br = new BufferedReader(new FileReader(file));
		while (br.ready()) {
			String line = br.readLine();
			if(line.isEmpty()) continue;
			FsmTestCase tc = new FsmTestCase();
			for (int i = 0; i <= line.length()-3; i+=3) {
				String inStr = line.substring(i, i+3);
				int inInt = Integer.valueOf(inStr);
				FsmTransition transition = new FsmTransition();
				transition.setInput(model.getInputs().get(inInt));
				tc.getPath().add(transition );
				if(!ts.getProperties().containsKey(model.getInputs().get(inInt)))
					ts.getProperties().put(model.getInputs().get(inInt), Integer.toString(inInt));
			}
			ts.getTestCases().add(tc);

		}
		br.close();
		return ts;
	}



	public FsmTestSuite randomTestSuite(FsmModel fsm, int resets, int length) {
		FsmTestSuite tSuite = new FsmTestSuite(fsm.getName());
		tSuite.setGeneratedBy("random."+resets+"."+length);
		
		for (int i = 0; i < resets; i++) {
			FsmTestCase tc = new FsmTestCase();
			for (int j = 0; j < length; j++) {
				int rndInputIndex = RandomGenerator.getInstance().getRnd().nextInt(fsm.getInputs().size());
				FsmTransition tr = new FsmTransition();
				tr.setInput(fsm.getInputs().get(rndInputIndex));
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
		System.out.print("medianLength"+"\n");

		
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
		System.out.print(Double.toString(	tStats.getMedianLength			())+"\n");

		
	}
	public void printConformanceTestingStatistics(RbacPolicy sutRbac, FsmModel sutSpec, FsmTestSuite testSuite, List<RbacPolicy> mutants) {

		RbacAcut acutSut= createAcutFromRbacPolicy(sutRbac);
		FsmSUT sutSpecFsm = new FsmSUT(sutSpec);
		List<RbacAcut> acutMutant = createAcutFromRbacPolicy(mutants);

		Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();

		List<RbacPolicy> alive = new ArrayList<RbacPolicy>();
		List<RbacPolicy> killed  = new ArrayList<RbacPolicy>();
		alive.addAll(mutants);

		
		for (int i = 0; i < testSuite.getTestCases().size(); i++) {

			FsmTestCase tc = testSuite.getTestCases().get(i);
			for (int j = 0; j < tc.getPath().size(); j++) {
				FsmTransition tr = tc.getPath().get(j);
				
				String specOut = sutSpecFsm.input(tr.getInput());
				boolean specBool = specOut.equals("grant");
				
				for (RbacAcut rbacAcut : acutMutant) {
					if(killed.contains(rbacAcut.getPolicy())) continue;
					rqMap.putIfAbsent(tr.getInput(), rbacUtils.createRbacRequest(tr.getInput(),acutSut));
					boolean mutBool = rbacAcut.request(rqMap.get(tr.getInput()));
					if(specBool ^ mutBool){
						killed.add(rbacAcut.getPolicy());
					}
						
				}
			}
			sutSpecFsm.setCurrentState(sutSpec.getInitialState());
			for (RbacAcut rbacAcut : acutMutant)  rbacAcut.reset();
		}
		int totAlive  = alive.size();
		int totKilled = killed.size();
		double score = ((double)totKilled)/(totAlive+totKilled);
		
		System.out.print(sutRbac.getName()+"\t");
		System.out.print(mutants.size()+"\t");
		System.out.print(testSuite.getName()+"\t");
		System.out.print(score);
		System.out.print("\n\n");
		
		for (RbacPolicy pol : alive) {
			if (!killed.contains(pol)) {
				System.out.println(pol.getName());
			}
			
		}
		

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
			for (FsmTransition tr : tc.getPath()) {
				if(tr.getInput() == null || tr.getInput().length()==0) continue;
				int in = Integer.valueOf((String) tsuite.getProperties().get(tr.getInput()));
				pw.print(stateFormat.format(in));
			}
			pw.print("\n");
		}
		pw.close();

	}



	public void setupTestFsmProperties(FsmModel fsm, FsmTestSuite suite) {
		for (String in : fsm.getInputs()) {
			if(!suite.getProperties().containsKey(in))
				suite.getProperties().put(in, Integer.toString(fsm.getInputs().indexOf(in)));
		}
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