package com.usp.icmc.labes;

import java.io.File;
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
import com.usp.icmc.labes.fsm.testing.FsmTestSuite;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.MutantType;
import com.usp.icmc.labes.rbac.model.RbacMutant;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.utils.Chronometer;
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
	private static PolicyUnderTestUtils putUtils 			= PolicyUnderTestUtils.getInstance();

	private static final String 	RBAC2FSM_PARAMETER 		= "r2f";
	private static final String 	RBAC_MUTATION_PARAMETER = "rmut";
	private static final String 	OUTPUT_PARAMETER 		= "o";
	private static final String 	KISS_PARAMETER 			= "kiss";
	private static final String 	DOT_PARAMETER 			= "dot";

	private static final String 	RUR		=	"rur";
	private static final String 	RURR	=	"rurr";
	private static final String 	RURU	=	"ruru";
	private static final String 	SUDE	=	"sude";
	private static final String 	SUIN	=	"suin";
	private static final String 	DUDE	=	"dude";
	private static final String 	DUIN	=	"duin";
	private static final String 	SRDE	=	"srde";
	private static final String 	SRIN	=	"srin";
	private static final String 	DRDE	=	"drde";
	private static final String 	DRIN	=	"drin";
	private static final String 	DSDE	=	"dsde";
	private static final String 	DSIN	=	"dsin";
	private static final String 	SSDE	=	"ssde";
	private static final String 	DSOD	=	"dsod";
	private static final String 	SSIN	=	"ssin";
	private static final String 	SSOD	=	"ssod";
	private static final String 	ALL_OPS	=	"allMut";

	private static final String HELP_PARAMETER = "help";
	private static final String HELP_SHORT_PARAMETER = "h";
	private static final String FSM_PARAMETER = "fsm";
	private static final String Q_SET_PARAMETER = "qset";
	private static final String P_SET_PARAMETER = "pset";
	private static final String TT_PARAMETER = "tt";
	private static final String FSMCONV_PARAMETER = "fsmConv";

	private static Options options;

	public static void main(String[] args) {
		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new DefaultParser();
		try {
			setupCliOptions();
			CommandLine cmd;
			cmd = parser.parse( options, args);

			File output = null;
			if(cmd.hasOption(OUTPUT_PARAMETER)) {
				String outputStr = cmd.getOptionValue(OUTPUT_PARAMETER);
				output = new File(outputStr);
			}
			if		(cmd.hasOption(RBAC2FSM_PARAMETER)) 			rbac2fsm(cmd,output);
			else if	(cmd.hasOption(RBAC_MUTATION_PARAMETER)) 	rbacMutation(cmd,output);
			else if	(cmd.hasOption(Q_SET_PARAMETER)) 			qSet(cmd,output);
			else if	(cmd.hasOption(P_SET_PARAMETER)) 			pSet(cmd,output);
			else if	(cmd.hasOption(TT_PARAMETER))				ttMethod(cmd,output); 
			else if	(cmd.hasOption(FSMCONV_PARAMETER)) 			fsmConverter(cmd,output);
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
		}

	}

	private static void fsmConverter(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		String fsmStr = cmd.getOptionValue(FSMCONV_PARAMETER);
		File fsmFile = new File(fsmStr);

		FsmModel fsm = fsmUtils.LoadFsmFromXML(fsmFile);

		if(output == null) output = fsmFile.getParentFile();
		output.mkdirs();

		String outFormat = null;
		if(cmd.hasOption(DOT_PARAMETER)) outFormat  = "dot";
		else if(cmd.hasOption(KISS_PARAMETER)) outFormat = "kiss";
		else outFormat = "kiss";

		File newFsmFile = new File(output,fsm.getName()+"."+outFormat);
		if(cmd.hasOption(KISS_PARAMETER)) fsmUtils.WriteFsmAsKiss(fsm, newFsmFile);
		else if(cmd.hasOption(DOT_PARAMETER)) fsmUtils.WriteFsmAsDot(fsm, newFsmFile);
		else fsmUtils.WriteFsmAsKiss(fsm, newFsmFile);		
	}

	private static void ttMethod(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "tt";

		String fsmStr = cmd.getOptionValue(TT_PARAMETER);
		File fsmFile = new File(fsmStr);

		FsmModel fsm = fsmUtils.LoadFsmFromXML(fsmFile);
		FsmTestSuite suite = testingUtils.transitionTour(fsm);

		if(output == null) output = fsmFile.getParentFile();
		output.mkdirs();

		File suiteFile = new File(output,fsmFile.getName().concat(".tt.test"));
		testingUtils.WriteFsmTestSuite(suite, suiteFile);

		chron.stop();
		System.out.println("%"+operation+" | "+fsmFile.getName()+" | "+chron.getInSeconds()+" seconds");		
	}

	private static void pSet(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "p set";

		String fsmStr = cmd.getOptionValue(P_SET_PARAMETER);
		File fsmFile = new File(fsmStr);

		FsmModel fsm = fsmUtils.LoadFsmFromXML(fsmFile);
		FsmTestSuite suite = testingUtils.transitionCoverSet(fsm);

		if(output == null) output = fsmFile.getParentFile();
		output.mkdirs();

		File suiteFile = new File(output,fsmFile.getName().concat(".p.test"));
		testingUtils.WriteFsmTestSuite(suite, suiteFile);

		chron.stop();
		System.out.println("%"+operation+" | "+fsmFile.getName()+" | "+chron.getInSeconds()+" seconds");
		
	}

	private static void qSet(CommandLine cmd, File output) throws TransformerConfigurationException, ParserConfigurationException, TransformerException, SAXException, IOException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "q set";

		String fsmStr = cmd.getOptionValue(Q_SET_PARAMETER);
		File fsmFile = new File(fsmStr);

		FsmModel fsm = fsmUtils.LoadFsmFromXML(fsmFile);

		if(output == null) output = fsmFile.getParentFile();
		output.mkdirs();

		File suiteFile = new File(output,fsmFile.getName().concat(".q.test"));

		FsmTestSuite suite = testingUtils.stateCoverSet(fsm);
		testingUtils.WriteFsmTestSuite(suite, suiteFile);
		chron.stop();
		System.out.println("%"+operation+" | "+fsmFile.getName()+" | "+chron.getInSeconds()+" seconds");

	}

	private static void rbacMutation(CommandLine cmd, File output) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "rbacMutation";

		String rbacPolicyStr = cmd.getOptionValue(RBAC_MUTATION_PARAMETER);


		List<MutantType> types = getMutationOperators(cmd);
		List<RbacMutant> mutants = new ArrayList<RbacMutant>();

		File rbacFile = new File(rbacPolicyStr);
		RbacPolicy rbacPolicy = rbacUtils.LoadRbacPolicyFromXML(rbacFile);

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

	private static void rbac2fsm(CommandLine cmd, File output) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
		Chronometer chron = new Chronometer();
		chron.start();
		String operation = "rbac2fsm";
		String rbacPolicyStr = cmd.getOptionValue(RBAC2FSM_PARAMETER);
		File rbacFile = new File(rbacPolicyStr);
		RbacPolicy rbacPolicy = rbacUtils.LoadRbacPolicyFromXML(rbacFile);
		FsmModel rbacFsm = fsmUtils.rbac2FsmConcurrent(rbacPolicy);
		fsmUtils.sorting(rbacFsm);

		File rbacFsmFile = null;

		String outFormat = "fsm";

		if(cmd.hasOption(KISS_PARAMETER)) outFormat = "kiss";
		else if(cmd.hasOption(DOT_PARAMETER)) outFormat = "dot";

		if(output == null) output = rbacFile.getParentFile();
		output.mkdirs();

		rbacFsmFile = new File(output,rbacPolicy.getName()+"."+outFormat);

		//TODO count time interval
		if(cmd.hasOption(KISS_PARAMETER)) fsmUtils.WriteFsmAsKiss(rbacFsm, rbacFsmFile);
		else if(cmd.hasOption(DOT_PARAMETER)) fsmUtils.WriteFsmAsDot(rbacFsm, rbacFsmFile);
		else fsmUtils.WriteFsm(rbacFsm, rbacFsmFile);
		chron.stop();
		System.out.println("%"+operation+" | "+rbacFile.getName()+" | "+rbacFsm.getStates().size()+" states | "+rbacFsm.getTransitions().size()+" transitions | "+chron.getInSeconds()+" seconds");
	}

	private static List<MutantType> getMutationOperators(CommandLine cmd) {
		List<MutantType> types = new ArrayList<MutantType>();
		if (cmd.hasOption(RUR)) types.add(MutantType.UR_REPLACE_UR);
		if(cmd.hasOption(RURR))	types.add(MutantType.UR_REPLACE_R); 
		if(cmd.hasOption(RURU))	types.add(MutantType.UR_REPLACE_U); 
		if(cmd.hasOption(SUDE))	types.add(MutantType.Su_DECREMENT);
		if(cmd.hasOption(SUIN))	types.add(MutantType.Su_INCREMENT); 
		if(cmd.hasOption(DUDE))	types.add(MutantType.Du_DECREMENT);
		if(cmd.hasOption(DUIN))	types.add(MutantType.Du_INCREMENT); 
		if(cmd.hasOption(SRDE))	types.add(MutantType.Sr_DECREMENT);
		if(cmd.hasOption(SRIN))	types.add(MutantType.Sr_INCREMENT); 
		if(cmd.hasOption(DRDE))	types.add(MutantType.Dr_DECREMENT);
		if(cmd.hasOption(DRIN))	types.add(MutantType.Dr_INCREMENT); 
		if(cmd.hasOption(DSDE))	types.add(MutantType.Ds_DECREMENT);
		if(cmd.hasOption(DSIN))	types.add(MutantType.Ds_INCREMENT); 
		if(cmd.hasOption(SSDE))	types.add(MutantType.Ss_DECREMENT);
		if(cmd.hasOption(SSIN))	types.add(MutantType.Ss_INCREMENT); 
		if(cmd.hasOption(DSOD))	types.add(MutantType.DSoD_REPLACE);
		if(cmd.hasOption(SSOD))	types.add(MutantType.SSoD_REPLACE);

		if(types.isEmpty() || cmd.hasOption(ALL_OPS)){
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

	private static void setupCliOptions() {
		// create Options object
		options = new Options();

		OptionGroup grp = new OptionGroup();

		Option r2fOption = new Option(RBAC2FSM_PARAMETER, true, 		"Run rbac2fsm over an RBAC policy passed as parameter.\n");
		Option rbacMutOption = new Option(RBAC_MUTATION_PARAMETER, true, 	"Run rbac mutation over an RBAC policy passed as parameter.");
		Option helpOption = new Option(HELP_SHORT_PARAMETER,HELP_PARAMETER, false, "Help menu");
		Option qSetOption = new Option(Q_SET_PARAMETER, true, "Generate Q Set (State cover) given an fsm");
		Option pSetOption = new Option(P_SET_PARAMETER, true, "Generate P Set (Transition cover) given an fsm") ;
		Option ttSetOption = new Option(TT_PARAMETER, true, "Generate Transition Tour given an fsm") ;
		Option fsm2SetOption = new Option(FSMCONV_PARAMETER, true, "Convert FSM file to other formats (default: .kiss)") ;

		grp.addOption(r2fOption);
		grp.addOption(rbacMutOption);
		grp.addOption(helpOption);
		grp.addOption(qSetOption);
		grp.addOption(pSetOption);
		grp.addOption(ttSetOption);
		grp.addOption(fsm2SetOption);
		grp.setRequired(true);
		options.addOptionGroup(grp);

		OptionGroup grpFsmFormat = new OptionGroup();
		grpFsmFormat.addOption(new Option(KISS_PARAMETER, 		"Writes fsm in .kiss format."));
		grpFsmFormat.addOption(new Option(FSM_PARAMETER, 		"Writes fsm in .fsm format. (DEFAULT)"));
		grpFsmFormat.addOption(new Option(DOT_PARAMETER, 		"Writes fsm in .dot format."));
		options.addOptionGroup(grpFsmFormat);

		options.addOption(OUTPUT_PARAMETER, true, 		"Sets the file/directory destination.");

		options.addOption(new Option(RUR,"MUTATION OPERATOR: Replace User and Role from UR"));

		options.addOption(new Option(RURR,"MUTATION OPERATOR: Replace Role from UR"));
		options.addOption(new Option(RURU,"MUTATION OPERATOR: Replace User from UR"));

		options.addOption(new Option(SUIN,"MUTATION OPERATOR: Static User cardinality increment (Su++)"));
		options.addOption(new Option(SUDE,"MUTATION OPERATOR: Static User cardinality decrement (Su--)"));

		options.addOption(new Option(DUIN,"MUTATION OPERATOR: Dynamic User cardinality increment (Du++)"));
		options.addOption(new Option(DUDE,"MUTATION OPERATOR: Dynamic User cardinality decrement (Du--) "));

		options.addOption(new Option(SRIN,"MUTATION OPERATOR: Static Role cardinality increment (Sr++)"));
		options.addOption(new Option(SRDE,"MUTATION OPERATOR: Static Role cardinality decrement (Sr--)"));

		options.addOption(new Option(DRIN,"MUTATION OPERATOR: Dynamic Role cardinality increment (Dr++)"));
		options.addOption(new Option(DRDE,"MUTATION OPERATOR: Dynamic Role cardinality decrement (Dr--)"));

		options.addOption(new Option(SSDE,"MUTATION OPERATOR: SSoD set role replacement"));
		options.addOption(new Option(DSOD,"MUTATION OPERATOR: DSoD set role replacement"));

		options.addOption(new Option(SSIN,"MUTATION OPERATOR: SSoD cardinality increment (SSoD++)"));
		options.addOption(new Option(SSOD,"MUTATION OPERATOR: SSoD cardinality decrement (SSoD--)"));

		options.addOption(new Option(DSIN,"MUTATION OPERATOR: DSoD cardinality increment (DSoD++)"));
		options.addOption(new Option(DSDE,"MUTATION OPERATOR: DSoD cardinality decrement (DSoD--)"));

		options.addOption(new Option(ALL_OPS,"ALL MUTATION OPERATORS WILL BE USED. (DEFAULT)"));


	}

}
