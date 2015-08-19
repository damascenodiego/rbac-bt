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

	public List<RbacTestConfiguration> loadRbacTestConfiguration(File testCnfFile) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
		List<RbacTestConfiguration> cfgs = new ArrayList<RbacTestConfiguration>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(testCnfFile);
		doc.getDocumentElement().normalize();
		Element fsmElement = doc.getDocumentElement();
		NodeList el = ((Element)fsmElement).getElementsByTagName("SUT_RBAC");
		for (int i = 0; i < el.getLength(); i++) {
			Element sutRbac = (Element)el.item(i);
			if(sutRbac.hasAttribute("ignore") && sutRbac.getAttribute("ignore").equalsIgnoreCase("true")) continue;

			RbacTestConfiguration out = new RbacTestConfiguration();
			out.setName(sutRbac.getAttribute("name"));
			out.setPath(testCnfFile.getParentFile());
			

			if(sutRbac.hasAttribute("type")){
				switch (sutRbac.getAttribute("type")) {
				case "generator":
					out.setTestConfigurationType(RbacTestConfiguration.ConfigurationType.TEST_GENERATOR);
					break;
				case "executor":
				default:
					out.setTestConfigurationType(RbacTestConfiguration.ConfigurationType.TEST_EXECUTION);
					break;
				}
			}

			Node sutSpecNode = sutRbac.getElementsByTagName("SUT_SPEC").item(0);
			loadSutSpecFromNode(out,sutSpecNode);

			NodeList testsuites = sutRbac.getElementsByTagName("TESTSUITE");
			for (int j = 0; j < testsuites.getLength(); j++) {
				Element in = (Element)testsuites.item(j);
				if(in.hasAttribute("ignore") && in.getAttribute("ignore").equalsIgnoreCase("true")) continue;
				out.getTestSuites().add(loadTestSuiteFromNode(out,in));
			}

			NodeList sutMutants = sutRbac.getElementsByTagName("SUT_MUTANT");
			for (int j = 0; j < sutMutants.getLength(); j++) {
				Element in = (Element) sutMutants.item(j);
				if(in.hasAttribute("equiv") && in.getAttribute("equiv").equalsIgnoreCase("true")) continue;
				if(in.hasAttribute("ignore") && in.getAttribute("ignore").equalsIgnoreCase("true")) continue;
				File mutPolFile = new File(testCnfFile.getParentFile(),in.getAttribute("name"));
				RbacPolicy mutPol = rbacUtils.LoadRbacPolicyFromXML(mutPolFile);
				RbacAcut mutAcut = new RbacAcut(mutPol);
				out.getRbacMutants().add(mutAcut);
			}
			cfgs.add(out);
		}
		return cfgs;
	}

	private void loadSutSpecFromNode(RbacTestConfiguration out, Node sutSpecNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		String filename = ((Element)sutSpecNode).getAttribute("name");
		out.setRbacSpecification(fsmUtils.LoadFsmFromXML(new File(out.getPath(),filename)));
	}

	private FsmTestSuite loadTestSuiteFromNode(RbacTestConfiguration tConf, Node testSuiteNode) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		String filename = ((Element)testSuiteNode).getAttribute("name");
		FsmTestSuite result = null;
		switch (((Element)testSuiteNode).getAttribute("format")) {
		case "fsm":
			result = LoadFsmTestSuiteFromFile(new File(tConf.getPath(),filename));
			break;
		case "kk":
			result = LoadFsmTestSuiteFromKK(tConf,new File(tConf.getPath(),filename));
			break;
		default:
			break;
		}
		if(result !=null && ((Element)testSuiteNode).hasAttribute("shortname")) {
			result.setGeneratedBy(((Element)testSuiteNode).getAttribute("shortname"));
		}
		if(result !=null && ((Element)testSuiteNode).hasAttribute("generatedBy")) {
			result.setGeneratedBy(((Element)testSuiteNode).getAttribute("generatedBy"));
		}

		return result;
	}

	private FsmTestSuite LoadFsmTestSuiteFromKK(RbacTestConfiguration out, File file) throws FileNotFoundException,IOException  {
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
				transition.setInput(out.getRbacSpecification().getInputs().get(inInt));
				tc.getPath().add(transition );
				if(!ts.getProperties().containsKey(out.getRbacSpecification().getInputs().get(inInt)))
				ts.getProperties().put(out.getRbacSpecification().getInputs().get(inInt), Integer.toString(inInt));
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

	public void saveStatistics(RbacTestConfiguration testCfgs, File testCnfFile) throws IOException {
		List<String> testMethods = new ArrayList<String>();
		Map<String,Map<String,FsmTestStatistics>> results = new HashMap<String,Map<String,FsmTestStatistics>>();
		for (RbacTestConfiguration rbacTestConfiguration : testCfgs) {
			results.put(rbacTestConfiguration.getRbacSpecification().getName(), new HashMap<String,FsmTestStatistics>());
			for (FsmTestSuite testSuite : rbacTestConfiguration.getTestSuites()) {
				if(!testMethods.contains(testSuite.getGeneratedBy())) testMethods.add(testSuite.getGeneratedBy());
				results.get(rbacTestConfiguration.getRbacSpecification().getName()).put(testSuite.getGeneratedBy(), new FsmTestStatistics(testSuite));
			}
		}
		File testResultsFile = new File(testCnfFile.getAbsolutePath()+".results");
		testResultsFile.getParentFile().mkdirs();
		BufferedWriter testResults = new BufferedWriter(new FileWriter(testResultsFile));

		
		if(testMethods.contains("p")) testMethods.add(0, testMethods.remove(testMethods.indexOf("p")));
		if(testMethods.contains("spy")) testMethods.add(0, testMethods.remove(testMethods.indexOf("spy")));
		if(testMethods.contains("h")) testMethods.add(0, testMethods.remove(testMethods.indexOf("h")));
		if(testMethods.contains("hsi")) testMethods.add(0, testMethods.remove(testMethods.indexOf("hsi")));
		if(testMethods.contains("w")) 	testMethods.add(0, testMethods.remove(testMethods.indexOf("w")));

		System.out.println(testMethods);
		testResults.write("ACUT");
		testResults.write("\t");
		for (String method : testMethods) {
			testResults.write("noResets-"+method);testResults.write("\t");
			testResults.write("testSuiteLength-"+method);testResults.write("\t");
			testResults.write("testSuiteLengthNoRst-"+method);testResults.write("\t");
			testResults.write("minLength-"+method);testResults.write("\t");
			testResults.write("avgLength-"+method);testResults.write("\t");
			testResults.write("maxLength-"+method);testResults.write("\t");
			testResults.write("sdLength-"+method);testResults.write("\t");
			testResults.write("varLength-"+method);testResults.write("\t");
			testResults.write("medianLength-"+method);testResults.write("\t");
		}
		testResults.write("\n");
		for (String spec: results.keySet()) {
			testResults.write(spec);
			testResults.write("\t");
			for (String method : testMethods) {
				if(!results.get(spec).containsKey(method)){
					for (int i = 0; i < 9; i++) testResults.write("-\t");
					continue;
				}
				testResults.write(Long.toString(	results.get(spec).get(method).getNoResets())); testResults.write("\t");
				testResults.write(Long.toString(	results.get(spec).get(method).getTestSuiteLength())); testResults.write("\t");
				testResults.write(Long.toString(	results.get(spec).get(method).getTestSuiteLengthNoResets())); testResults.write("\t");
				testResults.write(Long.toString(	results.get(spec).get(method).getMinLength())); testResults.write("\t");
				testResults.write(Double.toString(	results.get(spec).get(method).getAvgLength())); testResults.write("\t");
				testResults.write(Long.toString(	results.get(spec).get(method).getMaxLength())); testResults.write("\t");
				testResults.write(Double.toString(	results.get(spec).get(method).getSdLength())); testResults.write("\t");
				testResults.write(Double.toString(	results.get(spec).get(method).getVarLength())); testResults.write("\t");
				testResults.write(Double.toString(	results.get(spec).get(method).getMedianLength())); testResults.write("\t");

			}
			testResults.write("\n");
		}		
		testResults.write("\n");
		testResults.write("\n");


		testResults.write("ACUT\t"
				+ "totalMutants\t");
		for (String method : testMethods) {
			testResults.write(
//			method+"-alive\t"+
//			method+"-killed\t"+
			method+"-score\t");
		}
		testResults.write("\n");
		
		String strAlivePolicies = "";
		for (RbacTestConfiguration testCfg : testCfgs) {
			testResults.write(testCfg.getName()+"\t");

			List<RbacAcut> mutants = testCfg.getRbacMutants();
			int totMutants = mutants.size();
			testResults.write(totMutants+"\t");

			FsmModel spec = testCfg.getRbacSpecification();
			FsmSUT specSut = new FsmSUT(spec);
			List<RbacAcut> alive = new ArrayList<RbacAcut>();
			alive.addAll(mutants);
			for (FsmTestSuite ts : testCfg.getTestSuites()) {
				Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();
				Map<RbacAcut, Map<FsmTestCase,Integer>> killed = new HashMap<RbacAcut, Map<FsmTestCase,Integer>>();

				
				for (int i = 0; i < ts.getTestCases().size(); i++) {
					FsmTestCase tc = ts.getTestCases().get(i);

					for (int j = 0; j < tc.getPath().size(); j++) {
						FsmTransition tr = tc.getPath().get(j);

						String specOut = specSut.input(tr.getInput());
						boolean specBool = specOut.equals("grant");

						//System.out.println(specOut);
						for (RbacAcut rbacAcut : mutants) {
							if(killed.containsKey(rbacAcut) && killed.get(rbacAcut).containsKey(tc)) continue;
							rqMap.putIfAbsent(tr.getInput(), rbacUtils.createRbacRequest(tr.getInput(),rbacAcut));
							boolean out = rbacAcut.request(rqMap.get(tr.getInput()));
							//System.out.println(out);
							if(out ^ specBool){
								killed.putIfAbsent(rbacAcut, new HashMap<FsmTestCase,Integer>());
								killed.get(rbacAcut).putIfAbsent(tc,j);
							}
						}	
					}
					specSut.setCurrentState(spec.getInitialState());
					for (RbacAcut rbacAcut : mutants)  rbacAcut.reset();
				}
				
				alive.removeAll(killed.keySet());
				int totAlive = alive.size();
				int totKilled = killed.size();
				double score = ((double)totKilled)/(totAlive+totKilled);

				testResults.write(
//						Integer	.toString(totAlive)+"\t"+
//						Integer	.toString(totKilled)+"\t"+
						Double	.toString(score)+"\t");
				killed.clear();

			}
			testResults.write("\n");
			for (RbacAcut acut : alive) {
				strAlivePolicies+=acut.getPolicy().getName()+"\n";
			}
			
		}
		testResults.write("\nalivePolicies\n");
		testResults.write(strAlivePolicies);
		testResults.close();

	}


	DecimalFormat stateFormat = new DecimalFormat("000");

	public void WriteFsmTestSuiteAsKK(FsmTestSuite tsuite, File f) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(f);

		for (FsmTestCase tc : tsuite.getTestCases()) {
			for (FsmTransition tr : tc.getPath()) {
				int in = Integer.valueOf((String) tsuite.getProperties().get(tr.getInput()));
				pw.print(stateFormat.format(in));
			}
			pw.print("\n");
		}
		pw.close();
		
	}

}