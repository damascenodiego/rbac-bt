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
		try {
			Map<String,List<Double>> stats = new HashMap<String,List<Double>>();
			List<File>outs = openFilesFromOutList(outList);
			for (File file : outs) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					String line = br.readLine();
					String cols[] = line.split("\t");
//					System.out.println(cols[2]);
					stats.putIfAbsent(cols[2], new ArrayList<Double>());
					stats.get(cols[2]).add(Double.valueOf(cols[3]));

				}
				br.close();
			}
			List<String> logs = new ArrayList<String>();
			logs.addAll(stats.keySet());
			Collections.sort(logs);
			BufferedWriter bw = new BufferedWriter(new FileWriter(statSummary));
			bw.write("File\tMean\tMedian\tMin\tMax");
			bw.write("\n");
			for (String k : logs) {
				//System.out.println(stats.get(k));
//				System.out.println(k+"\t"+stats.get(k));
				double[] values = new double[stats.get(k).size()];
				for (int i = 0; i < values.length; i++) values[i] = stats.get(k).get(i);
				double meanVal = mean.evaluate(values);
				double medianVal = median.evaluate(values);
				double minVal = min.evaluate(values);
				double maxVal = max.evaluate(values);
				bw.write(k+"\t"+meanVal+"\t"+medianVal+"\t"+minVal+"\t"+maxVal);
				bw.write("\n");
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
