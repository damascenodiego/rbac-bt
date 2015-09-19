package com.usp.icmc.labes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.descriptive.rank.Min;
import org.xml.sax.SAXException;

import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacCardinality;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.SoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.utils.RbacUtils;

public class CalcStatistics {

	private static RbacUtils rbacUtils = RbacUtils.getInstance();

	public static void main(String[] args) {
		statsPrioritizationEffectiveness(args);
	}

	
	private static void statsPrioritizationTotMut(String[] args) {
		String argStr = args[0];
		File outList = new File(argStr);
		File statSummary = new File(outList.getParentFile(),outList.getName()+".statSummary.prtz.txt");
		
		try {
			Map<String,Map<String,Map<Integer,List<Integer>>>> stats = new HashMap<String,Map<String,Map<Integer,List<Integer>>>> ();

			BufferedReader br = new BufferedReader(new FileReader(outList));
			BufferedWriter bw = new BufferedWriter(new FileWriter(statSummary));
			List<String> name = new ArrayList<String>();
			List<String> meth = new ArrayList<String>();
			List<Integer> fragment = new ArrayList<Integer>();
			END: while (br.ready()) {
				Pattern notTestStats = Pattern.compile("^[a-zA-Z0-9_-_]*$");
				Pattern testPrtzFragmentPattern = Pattern.compile("\\.test\\.([a-zA-Z]+)\\.test\\.([0-9]+)\\.test");
				String line = br.readLine();
				while(line.equals(" ") || line.isEmpty() || notTestStats.matcher(line).matches()){
					line = br.readLine();
					if(!br.ready()) break END;
				}
				String test[] = line.split("\t");
				Matcher mat = testPrtzFragmentPattern.matcher(test[2]);
				String testPrtzMethod = "NONE";
				String testName = test[2];
				Integer testPrtzFragment = 100;
				if(mat.find()) {
//					System.out.println(mat.groupCount());
					testPrtzMethod = mat.group(1);
					testPrtzFragment = Integer.valueOf(mat.group(2));
					testName = mat.replaceAll(".test");
				}
				Double testEffectiveness = Double.valueOf(test[3]);
				int mutants = (int)(Integer.valueOf(test[1])*testEffectiveness);
				stats.putIfAbsent(testName, new HashMap<String, Map<Integer,List<Integer>>> ());
				stats.get(testName).putIfAbsent(testPrtzMethod, new HashMap<Integer, List<Integer>>());
				stats.get(testName).get(testPrtzMethod).putIfAbsent(testPrtzFragment,new ArrayList<Integer>());
				stats.get(testName).get(testPrtzMethod).get(testPrtzFragment).add(mutants);

				if(!name.contains(testName)) name.add(testName);
				if(!meth.contains(testPrtzMethod)) meth.add(testPrtzMethod);
				if(!fragment.contains(testPrtzFragment)) fragment.add(testPrtzFragment);
				
			}
			br.close();
			
			Collections.sort(name);
			Collections.sort(meth);
			Collections.sort(fragment);

			Mean mean = new Mean();
			
			meth.remove("NONE");
			
			for (String testName  : name) {
				bw.write("TestName\tPercent\t");
				for (String prtz : meth) bw.write(prtz+"\t");
				bw.write("\n");
				for (Integer perc  : fragment) {
					bw.write(testName); bw.write("\t");
					bw.write(Integer.toString(perc)); bw.write("\t");
					for (String prtz : meth) {
						if(perc == 100){
							double[] values = new double[stats.get(testName).get("NONE").get(100).size()];
							for (int i = 0; i < values.length; i++)  values[i] = stats.get(testName).get("NONE").get(100).get(i);
							bw.write(Double.toString(mean.evaluate(values))+"\t");
						}else if(stats.get(testName).containsKey(prtz) && stats.get(testName).get(prtz).containsKey(perc)) {
							double[] values = new double[stats.get(testName).get(prtz).get(perc).size()];
							for (int i = 0; i < values.length; i++)  values[i] = stats.get(testName).get(prtz).get(perc).get(i);
							bw.write(Double.toString(mean.evaluate(values))+"\t");
						} else{
							bw.write("-\t");
						}
					}
					bw.write("\n");
				}	
				bw.write("\n\n");

			}
			
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		

		
	}

	
	
	private static void statsPrioritizationEffectiveness(String[] args) {
		Mean calc = new Mean();
		
//		String argStr = args[0];
		String argStr = "/home/damasceno/Dropbox/run2015-09-17_21-21-41/conformanceTest.run2015-09-17_21-21-41.out";
		File outList = new File(argStr);
		File statSummary = new File(outList.getParentFile(),outList.getName()+".statSummary.prtz.txt");
		
		try {
			Map<String,Map<String,Map<Integer,List<Double>>>> stats = new HashMap<String,Map<String,Map<Integer,List<Double>>>> ();

			BufferedReader br = new BufferedReader(new FileReader(outList));
			BufferedWriter bw = new BufferedWriter(new FileWriter(statSummary));
			List<String> name = new ArrayList<String>();
			List<String> meth = new ArrayList<String>();
			List<Integer> fragment = new ArrayList<Integer>();
			int greater = 0;
			END: while (br.ready()) {
				Pattern notTestStats = Pattern.compile("^[a-zA-Z0-9_-_]*$");
				Pattern testPrtzFragmentPattern = Pattern.compile("\\.test\\.([a-zA-Z]+)\\.test\\.([0-9]+)\\.test");
				String line = br.readLine();
				while(line.equals(" ") || line.isEmpty() || notTestStats.matcher(line).matches()){
					line = br.readLine();
					if(!br.ready()) break END;
				}
				String test[] = line.split("\t");
				Matcher mat = testPrtzFragmentPattern.matcher(test[2]);
				String testPrtzMethod = "NONE";
				String testName = test[2];
				Integer testPrtzFragment = 100;
				if(mat.find()) {
//					System.out.println(mat.groupCount());
					testPrtzMethod = mat.group(1);
					testPrtzFragment = Integer.valueOf(mat.group(2));
					testName = mat.replaceAll(".test");
				}
				Double testEffectiveness = Double.valueOf(test[3]);
				stats.putIfAbsent(testName, new HashMap<String, Map<Integer,List<Double>>> ());
				stats.get(testName).putIfAbsent(testPrtzMethod, new HashMap<Integer, List<Double>>());
				stats.get(testName).get(testPrtzMethod).putIfAbsent(testPrtzFragment,new ArrayList<Double>());
				stats.get(testName).get(testPrtzMethod).get(testPrtzFragment).add(testEffectiveness);
				if(greater<stats.get(testName).get(testPrtzMethod).get(testPrtzFragment).size())
					greater=stats.get(testName).get(testPrtzMethod).get(testPrtzFragment).size();

				if(!name.contains(testName)) name.add(testName);
				if(!meth.contains(testPrtzMethod)) meth.add(testPrtzMethod);
				if(!fragment.contains(testPrtzFragment)) fragment.add(testPrtzFragment);
				
			}
			System.out.println(greater);
			br.close();
			
//			Collections.sort(name);
			Collections.sort(meth);
			Collections.sort(fragment);

			meth.remove("NONE");
			
			for (String testName  : name) {
				bw.write("TestName\tPercent\t");
				for (String prtz : meth) bw.write(prtz+"\t");
				bw.write("\n");
				for (Integer perc  : fragment) {
					bw.write(testName); bw.write("\t");
					bw.write(Integer.toString(perc)); bw.write("\t");
					for (String prtz : meth) {
						if(perc == 100){
							double[] values = new double[stats.get(testName).get("NONE").get(100).size()];
							for (int i = 0; i < values.length; i++)  values[i] = stats.get(testName).get("NONE").get(100).get(i);
							bw.write(Double.toString(calc.evaluate(values))+"\t");
						}else if(stats.get(testName).containsKey(prtz) && stats.get(testName).get(prtz).containsKey(perc)) {
							double[] values = new double[stats.get(testName).get(prtz).get(perc).size()];
							for (int i = 0; i < values.length; i++)  values[i] = stats.get(testName).get(prtz).get(perc).get(i);
							bw.write(Double.toString(calc.evaluate(values))+"\t");
						} else{
							bw.write("-\t");
						}
					}
					bw.write("\n");
				}	
				bw.write("\n\n");

			}
			
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		

		
	}

	private static void statsSelection(String[] args) {
//		String argStr = args[0];
		String argStr = "/home/damasceno/Dropbox/run2015-09-05_15-44-55/conformanceTest.run2015-09-05_15-44-55.out";
		File outList = new File(argStr);
		File statSummary = new File(outList.getParentFile(),outList.getName()+".statSummary.txt");
		try {
			//			Map<String,List<Double>> stats = new HashMap<String,List<Double>>();
			Map<String,List<Double>> stats = new HashMap<String,List<Double>> ();

			Pattern notTestStats = Pattern.compile("^[a-zA-Z0-9_-_]*$");
			FileReader fr = new FileReader(outList);
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(new FileWriter(statSummary));
			List<String> name = new ArrayList<String>();
			List<String> meth = new ArrayList<String>();
			List<String> rnd = new ArrayList<String>();
			END: while (br.ready()) {
				String line = br.readLine();
				while(line.equals(" ") || line.isEmpty() || notTestStats.matcher(line).matches()){
					line = br.readLine();
					if(!br.ready()) break END;
				}
				String test[] = line.split("\t");
				line = br.readLine();
				String crts[] = line.split("\t");
				name.add(test[2].replaceAll("\\.test","").replaceAll("\\.subset","").replaceAll("\\.", ""));
				meth.add(test[3]);
				rnd.add(crts[3]);
			}
			br.close();
			HashMap<String,String> map = new HashMap<String,String>();
			for (int i = 0; i < name.size(); i++) {
				map.putIfAbsent(name.get(i)+"_fsm", " <- c(");
				map.putIfAbsent(name.get(i)+"_rnd", " <- c(");
				map.put(name.get(i)+"_fsm", map.get(name.get(i)+"_fsm")+meth.get(i)+", ");
				map.put(name.get(i)+"_rnd", map.get(name.get(i)+"_rnd")+rnd.get(i)+", ");
			}
			
			for (String key : map.keySet()) map.put(key, map.get(key).replaceAll(", $", ")"));
			for (String key : map.keySet()) bw.write(key+ map.get(key)+"\n");
			
			bw.write("\n\n");
			
			Set<String> polNames = new HashSet<String>();
			for (String key : map.keySet()) polNames.add(key.replaceAll("_.+$", ""));
			
			for (String key: polNames) bw.write("wilcox.test("+key+"_rnd"+", "+key+"_fsm"+", paired=TRUE)\n");
			

			
			bw.write("\n\n");
			
			for (String key: polNames) bw.write("png(\"./"+key+"_rnd.png\")\n"+"hist("+key+"_rnd)\ndev.off()\n");
			for (String key: polNames) bw.write("png(\"./"+key+"_fsm.png\")\n"+"hist("+key+"_fsm)\ndev.off()\n");
			
			bw.write("\n\n");

			List<String> pols = new ArrayList<String>();
			pols.addAll(map.keySet());
			Collections.sort(pols);
			for (int i = 0; i < pols.size(); i+=4) {
				String out = "counts <- table(";
				out+=(pols.get(i)+" ,");
				out+=(pols.get(i+1)+" ,");
				out+=(pols.get(i+2)+" ,");
				out+=(pols.get(i+3)+"");
				out += ")\n";
				bw.write(out);
				out = "names <- c(";
				out+=("\""+pols.get(i)+"\" ,");
				out+=("\""+pols.get(i+1)+"\" ,");
				out+=("\""+pols.get(i+2)+"\" ,");
				out+=("\""+pols.get(i+3)+"\"");
				out += ")\n\n";
				bw.write(out);

			}
			
			
			
			bw.write("\n\n");
			
			
//			bw.write("meth <- c"+meth.toString()+"\n");
//			bw.write("\n");
//			bw.write("rnd <- c"+rnd.toString()+"\n");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	private static List<File> openFilesFromOutList(File outList) throws IOException {
		List<File> outs = new ArrayList<File>();
		FileReader fr = new FileReader(outList);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			File f = new File(outList.getParentFile(),br.readLine());
			if(f.exists()) outs.add(f);
		}
		return outs;
	}

}
