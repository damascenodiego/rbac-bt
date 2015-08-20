

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentTest {

	public static void main(String[] args) {
		int fragments[] = {01,05,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95};

		String fname = args[0];
		String method =  null; 
		if(args.length>1) method = args[1];
		DecimalFormat stateFormat = new DecimalFormat("000");
		File testCnfFile = new File(fname);
		try {
			BufferedReader br = new BufferedReader(new FileReader(testCnfFile));
			List<String> lines = new ArrayList<String>();

			while (br.ready())  lines.add(br.readLine());
			br.close();

			int total = lines.size();

			File outTestCfgFile = new File(new File("./prioritization"),testCnfFile.getName().split("\\.")[0]+"/"+testCnfFile.getName()+".fragmented.testcfg");
			outTestCfgFile.getParentFile().mkdirs();
			BufferedWriter outTestCfg = new BufferedWriter(new FileWriter(outTestCfgFile));
			
			for (int  frag : fragments) {
				File outFile = new File(new File("./prioritization"),testCnfFile.getName().split("\\.")[0]+"/"+testCnfFile.getName()+"."+stateFormat.format(frag)+".test");
				outFile.getParentFile().mkdirs();
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
				int i=0;
				for (i = 0; !eqGreaterThan(frag/(double)100, i, total); i++) {
					bw.write(lines.get(i)+"\n");
				}	
				bw.close();
				
				outTestCfg.write("\t<TESTSUITE name=\"");
				outTestCfg.write(outFile.getAbsolutePath());
				outTestCfg.write("\" format=\"kk\" generatedBy=\"");
				outTestCfg.write(((method!=null)?method+"\t":"")+stateFormat.format(frag)+"%");
				outTestCfg.write("\" />");
				outTestCfg.write("\n");
				

			}
			outTestCfg.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static boolean eqGreaterThan(double percent,int val, int tot){
		return (val/((double)tot))>=percent;
	}

}
