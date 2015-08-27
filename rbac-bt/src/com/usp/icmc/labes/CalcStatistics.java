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
		File outList = new File(args[0]);
		File statSummary = new File(outList.getParentFile(),"statSummary.txt");
		Mean mean = new Mean();
		Median median= new Median();
		Min min= new Min();
		Max max= new Max();
		List<String> methods = new ArrayList<String>();
		List<Integer> percents = new ArrayList<Integer>();
		try {
//			Map<String,List<Double>> stats = new HashMap<String,List<Double>>();
			Map<String,Map<String,Map<Integer,List<Double>>>> stats = new HashMap<String,Map<String,Map<Integer,List<Double>>>>();
			List<File>outs = openFilesFromOutList(outList);
			
			Pattern notTestStats = Pattern.compile("^[a-zA-Z0-9_-_]*$");
			for (File file : outs) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					String line = br.readLine();
					if(line.isEmpty() || notTestStats.matcher(line).matches()) continue;
					String cols[] = line.split("\t");
//					System.out.println(cols[2]);
					cols[2]=cols[2].replace(".test", "").replace(".rnd.", ".");
					String [] info = cols[2].split("\\.");
					stats.putIfAbsent(info[0]+"."+info[1]+"."+info[2], new HashMap<>());
					stats.get(info[0]+"."+info[1]+"."+info[2]).putIfAbsent(info[3],  new HashMap<>());
					stats.get(info[0]+"."+info[1]+"."+info[2]).get(info[3]).putIfAbsent(Integer.valueOf(info[4]), new ArrayList<Double>());
					stats.get(info[0]+"."+info[1]+"."+info[2]).get(info[3]).get(Integer.valueOf(info[4])).add(Double.valueOf(cols[3]));
					
					if(!methods.contains(info[3])) methods.add(info[3]);
					if(!percents.contains(Integer.valueOf(info[4]))) percents.add(Integer.valueOf(info[4]));
					
				}
				br.close();
			}
			List<String> logs = new ArrayList<String>();
			logs.addAll(stats.keySet());
			Collections.sort(logs);
			Collections.sort(methods);
			Collections.sort(percents);
			BufferedWriter bw = new BufferedWriter(new FileWriter(statSummary));
			bw.write("File\t%");
			for (String m : methods) bw.write("\tMean("+m+")");
			for (String m : methods) bw.write("\tMedian("+m+")");
			for (String m : methods) bw.write("\tMin("+m+")");
			for (String m : methods) bw.write("\tMax("+m+")");
			bw.write("\n");
			
			for (String k : logs) {
				//System.out.println(stats.get(k));
//				System.out.println(k+"\t"+stats.get(k));
				for (Integer prcnt : percents) {
					bw.write(k+"\t"+prcnt);
					double[] values = null;
					Map<String,double[]> methodStats = new HashMap<String,double[]>();
					for (String m : methods) {
						values = new double[stats.get(k).get(m).get(prcnt).size()];
						for (int i = 0; i < values.length; i++) values[i] = stats.get(k).get(m).get(prcnt).get(i);
						double meanVal = mean.evaluate(values);
						double medianVal = median.evaluate(values);
						double minVal = min.evaluate(values);
						double maxVal = max.evaluate(values);
						double[] statsArr = {meanVal,medianVal,minVal,maxVal};
						methodStats.put(m, statsArr);
					}
					for (String m : methods) bw.write("\t"+methodStats.get(m)[0]);
					for (String m : methods) bw.write("\t"+methodStats.get(m)[1]);
					for (String m : methods) bw.write("\t"+methodStats.get(m)[2]);
					for (String m : methods) bw.write("\t"+methodStats.get(m)[3]);
					bw.write("\n");
				} 

			}
			bw.close();
//			System.out.println(stats.size());
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
