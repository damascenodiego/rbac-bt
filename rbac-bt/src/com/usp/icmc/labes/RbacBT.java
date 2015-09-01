package com.usp.icmc.labes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.fsm.testing.FsmSUT;
import com.usp.icmc.labes.fsm.testing.FsmTestCase;
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.fsm.testing.RbacTestConfiguration;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.MutantType;
import com.usp.icmc.labes.rbac.model.RbacMutant;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.utils.Chronometer;
import com.usp.icmc.labes.utils.FsmTestCaseSimilarityUtils;
import com.usp.icmc.labes.utils.FsmTestingUtils;
import com.usp.icmc.labes.utils.FsmUtils;
import com.usp.icmc.labes.utils.PolicyUnderTestUtils;
import com.usp.icmc.labes.utils.RbacMutationUtils;
import com.usp.icmc.labes.utils.RbacUtils;

public class RbacBT {

	private static RbacAdministrativeCommands rbacAdmin		= RbacAdministrativeCommands.getInstance();
	private static RbacSupportingSystemFunctions rbacSys 	= RbacSupportingSystemFunctions.getInstance();
	private static RbacUtils rbacUtils 						= RbacUtils.getInstance();
	private static RbacMutationUtils rbacMut 				= RbacMutationUtils.getInstance();
	private static FsmTestingUtils testingUtils 			= FsmTestingUtils.getInstance();
	private static FsmUtils fsmUtils 						= FsmUtils.getInstance();
	private static FsmTestingUtils fsmTestingutils			= FsmTestingUtils.getInstance();
	private static FsmTestCaseSimilarityUtils testSimiliaryt= FsmTestCaseSimilarityUtils.getInstance(); 
	private static PolicyUnderTestUtils putUtils 			= PolicyUnderTestUtils.getInstance();

	private static final String 	RBAC2FSM_PARAMETER 		= "r2f";
	private static final String 	RBAC_MUTATION_PARAMETER = "rmut";
	private static final String 	OUTPUT_PARAMETER 		= "o";
	private static final String 	KISS_PARAMETER 			= "kiss";
	private static final String 	DOT_PARAMETER 			= "dot";
	private static final String 	KK_PARAMETER 			= "kk";

	private static final String HELP_PARAMETER = "help";
	private static final String HELP_SHORT_PARAMETER = "h";
	private static final String FSM_PARAMETER = "fsm";
	private static final String Q_SET_PARAMETER = "qset";
	private static final String P_SET_PARAMETER = "pset";
	private static final String TT_PARAMETER = "tt";
	private static final String FSMCONV_PARAMETER = "fsmConv";

	private static final String CONFORMANCETEST_PARAMETER = "ct";
	private static final String SUT_SPEC_CT_PARAMETER = "spec";
	private static final String SUT_MUTANTLIST_CT_PARAMETER = "mutants";
	private static final String TESTSUITE_PARAMETER = "test";

	private static final String TESTPRTZ_PARAMETER = "prtz";
	private static final String RBACPOL_PARAMETER = "rbac";
	
	private static final String TEST_CHARACTERISTICS_PARAMETER = "testCharact";
	
	private static final String MODE_PARAMETER = "mode";
	private static final String PRTZ_DAMASCENO_PARAMETER 	= "damasc";
	private static final String PRTZ_RANDOM_PARAMETER 		= "random";
	private static final String PRTZ_CARTAXO_PARAMETER 		= "cartax";
	
	private static final String RANDOMTEST_PARAMETER = "rnd";
	private static final String RANDOMTEST_LENGTH_PARAMETER = "length";
	private static final String RANDOMTEST_RESETS_PARAMETER = "resets";

	private static Options options;

	private static DefaultParser parser;

	public static void main(String[] args) {
		HelpFormatter formatter = new HelpFormatter();
		parser = new DefaultParser();
		try {
			setupCliOptions();
			CommandLine cmd;
			cmd = parser.parse( options, args);

			File output = null;
			if(cmd.hasOption(OUTPUT_PARAMETER)) {
				String outputStr = cmd.getOptionValue(OUTPUT_PARAMETER);
				output = new File(outputStr);
			}
			if		(cmd.hasOption(RBAC2FSM_PARAMETER)) 		rbac2fsm(cmd,output);
			else if	(cmd.hasOption(RBAC_MUTATION_PARAMETER)) 	rbacMutation(cmd,output);
			else if	(cmd.hasOption(Q_SET_PARAMETER)) 			qSet(cmd,output);
			else if	(cmd.hasOption(P_SET_PARAMETER)) 			pSet(cmd,output);
			else if	(cmd.hasOption(TT_PARAMETER))				ttMethod(cmd,output);
			else if	(cmd.hasOption(RANDOMTEST_PARAMETER))		randomMethod(cmd,output);
//			else if	(cmd.hasOption(FSMCONV_PARAMETER)) 			fsmConverter(cmd,output);
			else if	(cmdIsValidForConfTest(cmd)) 				runConformanceTest(cmd,output);
			else if	(cmdIsValidForTestPrtz(cmd)) 				testPrtz(cmd,output);
			else if	(cmdIsValidForTestCharacts(cmd)) 			runPrintTestCharacteristics(cmd,output);
			else if	(cmd.hasOption(HELP_PARAMETER))  			formatter.printHelp( "rbacBt", options );
			else 	formatter.printHelp( "rbacBt", options );
		} catch (ParseException e) {
			System.err.println( "Parsing failed.  Reason: " + e.getMessage() );
			formatter.printHelp( "rbacBt", options );
		} catch (ParserConfigurationException e) {
			System.err.println("Error loading/writing RBAC policy file!!!!");
		} catch (SAXException | IOException e) {
			System.err.println("Error loading RBAC policy file!!!!");
		} catch (TransformerException e) {
			System.err.println("XML Parsing error!!!!");
		} catch (Exception e) {
			System.err.println("Unhandled Exception!!!!");
			e.printStackTrace();
		}

	}

	private static boolean cmdIsValidForTestCharacts(CommandLine cmd) throws ParseException {
		return cmd.hasOption(RBACPOL_PARAMETER)
				&& cmd.hasOption(TESTSUITE_PARAMETER)
				&& cmd.hasOption(TEST_CHARACTERISTICS_PARAMETER);
	}

	private static boolean cmdIsValidForTestPrtz(CommandLine cmd) throws ParseException {
		return cmd.hasOption(TESTPRTZ_PARAMETER)
				&& cmd.hasOption(RBACPOL_PARAMETER)
				;
	}

	private static boolean cmdIsValidForConfTest(CommandLine cmd) {
		return cmd.hasOption(CONFORMANCETEST_PARAMETER)
				&& cmd.hasOption(SUT_MUTANTLIST_CT_PARAMETER)
				&& cmd.hasOption(TESTSUITE_PARAMETER);
	}

	private static void setupCliOptions() {
		// create Options object
		options = new Options();
		

		OptionGroup grp = new OptionGroup();

		Option r2fOption = new Option(RBAC2FSM_PARAMETER, true, 		"Run rbac2fsm over an RBAC policy passed as parameter.\n");
		Option runTestOption = new Option(CONFORMANCETEST_PARAMETER, true, 		"Runs a conformance testing procedure of an FSM/RBAC specification against a set of RBAC policies using an .testcnf file.\n");
		Option rbacMutOption = new Option(RBAC_MUTATION_PARAMETER, true, 	"Run rbac mutation over an RBAC policy passed as parameter.");
		Option helpOption = new Option(HELP_SHORT_PARAMETER,HELP_PARAMETER, false, "Help menu");
		Option qSetOption = new Option(Q_SET_PARAMETER, true, "Generate Q Set (State cover) given an fsm");
		Option pSetOption = new Option(P_SET_PARAMETER, true, "Generate P Set (Transition cover) given an fsm") ;
		Option ttSetOption = new Option(TT_PARAMETER, true, "Generate Transition Tour given an fsm") ;
		Option fsm2SetOption = new Option(FSMCONV_PARAMETER, true, "Convert FSM file to other formats (default: .kiss)") ;
		Option randomTestOption = new Option(RANDOMTEST_PARAMETER, true, "Generate a random test suite given an FSM") ;

		Option testPrtzOption = new Option(TESTPRTZ_PARAMETER, true, "Prioritize a test suite") ;
		
		Option testCharactsOption = new Option(TEST_CHARACTERISTICS_PARAMETER, "Print the characteristics of a test suite") ;
		
		grp.addOption(r2fOption);
		grp.addOption(rbacMutOption);
		grp.addOption(helpOption);
		grp.addOption(qSetOption);
		grp.addOption(pSetOption);
		grp.addOption(ttSetOption);
		grp.addOption(fsm2SetOption);
		grp.addOption(runTestOption);
		grp.addOption(testPrtzOption);
		grp.addOption(testCharactsOption);
		grp.addOption(randomTestOption);
		
		grp.setRequired(true);
		options.addOptionGroup(grp);

		OptionGroup grpFsmFormat = new OptionGroup();
		grpFsmFormat.addOption(new Option(KISS_PARAMETER, 		"Writes fsm in .kiss format."));
		grpFsmFormat.addOption(new Option(FSM_PARAMETER, 		"Writes fsm in .fsm format. (DEFAULT)"));
		grpFsmFormat.addOption(new Option(DOT_PARAMETER, 		"Writes fsm in .dot format."));
		grpFsmFormat.addOption(new Option(KK_PARAMETER, 		"Writes fsm in .kk format (Clean KISS format)."));
		options.addOptionGroup(grpFsmFormat);

		options.addOption(OUTPUT_PARAMETER, true, 		"Sets the file/directory destination.");

		Option sutSpec_ct = new Option(SUT_SPEC_CT_PARAMETER,true,"FSM generated from the RBAC policy under test");
		Option testSuite = new Option(TESTSUITE_PARAMETER,true,"test suite");
		Option mutLst = new Option(SUT_MUTANTLIST_CT_PARAMETER,true,"text file with the path for all the mutants generated from the RBAC policy under test");
		Option rbac_acut = new Option(RBACPOL_PARAMETER,true,"RBAC policy under test");
		Option prtz_mode= new Option(MODE_PARAMETER,true,"Defines the test prioritization mode");
		
		
		options.addOption(mutLst);
		options.addOption(sutSpec_ct);
		options.addOption(testSuite);
		options.addOption(rbac_acut);
		options.addOption(prtz_mode);

		Option randomTestLengthOption = new Option(RANDOMTEST_LENGTH_PARAMETER, true, "Defines the length of the random test cases") ;
		Option randomTestResetsOption = new Option(RANDOMTEST_RESETS_PARAMETER, true, "Defines the length of the random test suite") ;

		options.addOption(randomTestLengthOption);
		options.addOption(randomTestResetsOption);
		
	}

	private static void rbac2fsm(CommandLine cmd, File output) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "rbac2fsm";
		String rbacPolicyStr = cmd.getOptionValue(RBAC2FSM_PARAMETER);
		File rbacFile = new File(rbacPolicyStr);
		RbacPolicy rbacPolicy = rbacUtils.loadRbacPolicyFromXML(rbacFile);
		FsmModel rbacFsm = fsmUtils.rbac2FsmConcurrent(rbacPolicy);
		fsmUtils.sorting(rbacFsm);

		File rbacFsmFile = null;

		String outFormat = "fsm";

		if(cmd.hasOption(KK_PARAMETER)) outFormat = "kk";
		else outFormat = "fsm";
		
		if(output == null) output = rbacFile.getParentFile();
		output.mkdirs();

		rbacFsmFile = new File(output,rbacPolicy.getName()+"."+outFormat);

		if(cmd.hasOption(KK_PARAMETER)) fsmUtils.WriteFsmAsKissSimple(rbacFsm, rbacFsmFile);
		else fsmUtils.WriteFsm(rbacFsm, rbacFsmFile);

		chron.stop();
		System.out.println("%"+operation+" | "+rbacFile.getName()+" | "+rbacFsm.getStates().size()+" states | "+rbacFsm.getTransitions().size()+" transitions | "+chron.getInSeconds()+" seconds");
	}

	private static List<MutantType> getMutationOperators(CommandLine cmd) {
		List<MutantType> types = new ArrayList<MutantType>();

		if(types.isEmpty() /*|| cmd.hasOption(ALL_OPS)*/){
			types.clear();
			types.add(MutantType.UR_REPLACE_UR);
			types.add(MutantType.UR_REPLACE_R); 
			types.add(MutantType.UR_REPLACE_U); 
			types.add(MutantType.Su_DECREMENT);
			types.add(MutantType.Su_INCREMENT); 
			types.add(MutantType.Du_DECREMENT);
			types.add(MutantType.Du_INCREMENT); 
			types.add(MutantType.Sr_DECREMENT);
			types.add(MutantType.Sr_INCREMENT); 
			types.add(MutantType.Dr_DECREMENT);
			types.add(MutantType.Dr_INCREMENT); 
			types.add(MutantType.Ds_DECREMENT);
			types.add(MutantType.Ds_INCREMENT); 
			types.add(MutantType.Ss_DECREMENT);
			types.add(MutantType.DSoD_REPLACE);
			types.add(MutantType.Ss_INCREMENT); 
			types.add(MutantType.SSoD_REPLACE);

		}

		return types;
	}

	private static void rbacMutation(CommandLine cmd, File output) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "rbacMutation";

		String rbacPolicyStr = cmd.getOptionValue(RBAC_MUTATION_PARAMETER);


		List<MutantType> types = getMutationOperators(cmd);
		List<RbacMutant> mutants = new ArrayList<RbacMutant>();

		File rbacFile = new File(rbacPolicyStr);
		RbacPolicy rbacPolicy = rbacUtils.loadRbacPolicyFromXML(rbacFile);

		for (MutantType mutantType : types) 
			mutants.addAll(rbacMut.generateMutants(rbacPolicy , mutantType));

		if(output == null) output = rbacFile.getParentFile();
		output.mkdirs();

		for (RbacMutant rbacMutant : mutants) {
			File mutTypeDir = new File(output, rbacMutant.getType().name());
			if(!mutTypeDir.exists()) mutTypeDir.mkdirs();

			File rbacFsmFile = new File(mutTypeDir,rbacMutant.getName()+".rbac");
			rbacUtils.WriteRbacPolicyAsXML(rbacMutant, rbacFsmFile);
		}
		chron.stop();
		System.out.println("%"+operation+" | "+rbacFile.getName()+" | "+mutants.size()+" mutants | "+chron.getInSeconds()+" seconds");		
	}

//	private static void fsmConverter(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
//		Chronometer chron = new Chronometer();
//		chron.start();
//		String fsmStr = cmd.getOptionValue(FSMCONV_PARAMETER);
//		File fsmFile = new File(fsmStr);
//
//		FsmModel fsm = fsmUtils.loadFsmFromXML(fsmFile);
//
//		if(output == null) output = fsmFile.getParentFile();
//		output.mkdirs();
//
//		String outFormat = null;
//		if(cmd.hasOption(DOT_PARAMETER)) outFormat  = "dot";
//		else if(cmd.hasOption(KISS_PARAMETER)) outFormat = "kiss";
//		else if(cmd.hasOption(KK_PARAMETER)) outFormat = "kk";
//		else outFormat = "kiss";
//
//		String operation = "fsmConverter"+'('+outFormat+')';
//
//		File newFsmFile = new File(output,fsm.getName()+"."+outFormat);
//		//TODO to fix it
////		if(cmd.hasOption(KISS_PARAMETER)) fsmUtils.WriteFsmAsKiss(fsm, newFsmFile);
////		else if(cmd.hasOption(DOT_PARAMETER)) fsmUtils.WriteFsmAsDot(fsm, newFsmFile);
////		else if(cmd.hasOption(KK_PARAMETER)) fsmUtils.WriteFsmAsKissSimple(fsm, newFsmFile);
////		else fsmUtils.WriteFsmAsKiss(fsm, newFsmFile);
//		if(cmd.hasOption(KK_PARAMETER)) fsmUtils.WriteFsmAsKissSimple(fsm, newFsmFile);
//		chron.stop();
//
//		System.out.println("%"+operation+" | "+fsmFile.getName()+" | "+chron.getInSeconds()+" seconds");
//	}

	private static void pSet(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "p set";

		String fsmStr = cmd.getOptionValue(P_SET_PARAMETER);
		File fsmFile = new File(fsmStr);

		FsmModel fsm = fsmUtils.loadFsmFromXML(fsmFile);
		FsmTestSuite suite = testingUtils.transitionCoverSet(fsm);
		testingUtils.setupTestFsmProperties(fsm,suite);
		
		if(output == null) output = fsmFile.getParentFile();
		output.mkdirs();

		File suiteFile = new File(output,fsmFile.getName().concat(".p.test"));
		testingUtils.WriteFsmTestSuiteAsKK(suite, suiteFile);

		chron.stop();
		System.out.println("%"+operation+" | "+fsmFile.getName()+" | "+chron.getInSeconds()+" seconds");

	}

	private static void qSet(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "q set";

		String fsmStr = cmd.getOptionValue(Q_SET_PARAMETER);
		File fsmFile = new File(fsmStr);

		FsmModel fsm = fsmUtils.loadFsmFromXML(fsmFile);
		FsmTestSuite suite = testingUtils.stateCoverSet(fsm);
		testingUtils.setupTestFsmProperties(fsm,suite);

		if(output == null) output = fsmFile.getParentFile();
		output.mkdirs();

		File suiteFile = new File(output,fsmFile.getName().concat(".q.test"));
		testingUtils.WriteFsmTestSuiteAsKK(suite, suiteFile);
		
		chron.stop();
		System.out.println("%"+operation+" | "+fsmFile.getName()+" | "+chron.getInSeconds()+" seconds");

	}

	private static void ttMethod(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "tt";

		String fsmStr = cmd.getOptionValue(TT_PARAMETER);
		File fsmFile = new File(fsmStr);

		FsmModel fsm = fsmUtils.loadFsmFromXML(fsmFile);
		FsmTestSuite suite = testingUtils.transitionTour(fsm);
		testingUtils.setupTestFsmProperties(fsm,suite);

		if(output == null) output = fsmFile.getParentFile();
		output.mkdirs();

		File suiteFile = new File(output,fsmFile.getName().concat(".tt.test"));
		testingUtils.WriteFsmTestSuiteAsKK(suite, suiteFile);

		chron.stop();
		System.out.println("%"+operation+" | "+fsmFile.getName()+" | "+chron.getInSeconds()+" seconds");		
	}

	private static void randomMethod(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "random";

		String sutRbacStr = cmd.getOptionValue(RANDOMTEST_PARAMETER);
		int resets = 50;
		int length = 10;
		
		if(cmd.hasOption(RANDOMTEST_RESETS_PARAMETER)) resets = Math.round(Float.valueOf(cmd.getOptionValue(RANDOMTEST_RESETS_PARAMETER)));
		if(cmd.hasOption(RANDOMTEST_LENGTH_PARAMETER)) length = Math.round(Float.valueOf(cmd.getOptionValue(RANDOMTEST_LENGTH_PARAMETER)));
		
		File sutRbacFile 	= new File(sutRbacStr);
		RbacPolicy sutRbac 	= rbacUtils.loadRbacPolicyFromXML(sutRbacFile);
		
		if(output == null) output = sutRbacFile.getParentFile();
		output.mkdirs();

		File suiteFile = new File(output,sutRbac.getName().concat(".rnd."+resets+"."+length+".test"));

		FsmTestSuite suite = testingUtils.randomTestSuite(sutRbac,resets,length);
		testingUtils.WriteFsmTestSuiteAsKK(suite, suiteFile);
		chron.stop();
		System.out.println("%"+operation+" | "+sutRbac.getName()+" | "+chron.getInSeconds()+" seconds");

		
	}

	private static void runConformanceTest(CommandLine cmd, File output) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
		Chronometer chron = new Chronometer();
		chron.start();
		String sutRbacStr 		= cmd.getOptionValue(CONFORMANCETEST_PARAMETER);
		String sutMutantsStr 	= cmd.getOptionValue(SUT_MUTANTLIST_CT_PARAMETER);
		String testSuiteStr 	= cmd.getOptionValue(TESTSUITE_PARAMETER);

		File sutRbacFile = new File(sutRbacStr);
		File testSuiteFile = new File(testSuiteStr);
		File sutMutantsFile = new File(sutMutantsStr);


		RbacPolicy sutRbac 			= rbacUtils.loadRbacPolicyFromXML(sutRbacFile);
		FsmTestSuite testSuite 		= fsmTestingutils.loadFsmTestSuiteFromKK(sutRbac, testSuiteFile);
		List<RbacPolicy> mutants 	= rbacUtils.loadMutantsFromTxTFile(sutMutantsFile);

		testingUtils.printConformanceTestingStatistics(sutRbac,testSuite,mutants);

		chron.stop();
	}

	private static void runPrintTestCharacteristics(CommandLine cmd, File output) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
		Chronometer chron = new Chronometer();
		chron.start();
		
		String sutRbacStr 	= cmd.getOptionValue(RBACPOL_PARAMETER);
		File sutRbacFile 	= new File(sutRbacStr);
		RbacPolicy sutRbac 	= rbacUtils.loadRbacPolicyFromXML(sutRbacFile);

		
		String testSuiteStr 		= cmd.getOptionValue(TESTSUITE_PARAMETER);
		File testSuiteFile 			= new File(testSuiteStr);
		FsmTestSuite testSuite 		= fsmTestingutils.loadFsmTestSuiteFromKK(sutRbac, testSuiteFile);
	
		testingUtils.printTestSuiteCharacteristics(testSuite);
	
		chron.stop();
	}

	private static void testPrtz(CommandLine cmd, File output) throws FileNotFoundException, IOException, TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException {
		Chronometer chron = new Chronometer();
		chron.start();
		String testSuiteStr = cmd.getOptionValue(TESTPRTZ_PARAMETER);
		String sutRbacStr 	= cmd.getOptionValue(RBACPOL_PARAMETER);

		File testSuiteFile = new File(testSuiteStr);
		File sutRbacFile = new File(sutRbacStr);

		RbacPolicy sutRbac 			= rbacUtils.loadRbacPolicyFromXML(sutRbacFile);
		FsmTestSuite testSuite 		= fsmTestingutils.loadFsmTestSuiteFromKK(sutRbac, testSuiteFile);

		String prtz_mode = cmd.getOptionValue(MODE_PARAMETER);
		if(prtz_mode==null) prtz_mode = "";
		switch (prtz_mode) {
		case PRTZ_DAMASCENO_PARAMETER:
			testSimiliaryt.sortSimilarityDamasceno(sutRbac,testSuite);
			break;
		case PRTZ_RANDOM_PARAMETER:
			testSimiliaryt.sortSimilarityRandom(testSuite);
			break;
		case PRTZ_CARTAXO_PARAMETER:
		default:
			testSimiliaryt.sortSimilarityCartaxo(sutRbac,testSuite);
			break;
		}

		File testResultsFile = new File(testSuiteFile.getParentFile(),testSuiteFile.getName()+"."+prtz_mode+".test");
		testResultsFile.getParentFile().mkdirs();
		testingUtils.WriteFsmTestSuiteAsKK(testSuite, testResultsFile);

		chron.stop();
	}

}
