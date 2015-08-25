package com.usp.icmc.labes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

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
		File outList = new File("/home/damasceno/Dropbox/run2015-08-24_23-23-03/outList.txt");
		try {
			Map<String,Set<Double>> stats = new HashMap<String,Set<Double>>();
			List<File>outs = openFilesFromOutList(outList);
			for (File file : outs) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					String line = br.readLine();
					String cols[] = line.split("\t");
					System.out.println(cols[2]);
					stats.putIfAbsent(cols[1]+"\t"+cols[2], new HashSet<Double>());
					stats.get(cols[1]+"\t"+cols[2]).add(Double.valueOf(cols[3]));

				}
				br.close();
			}
			System.out.println(stats.size());
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
