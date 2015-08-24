package com.usp.icmc.labes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class PrintRbacPolicy {

	private static RbacUtils rbacUtils = RbacUtils.getInstance();

	public static void main(String[] args) {
		File rbacFile = new File(args[0]);
		try {
			RbacPolicy rbacPolicy = rbacUtils.loadRbacPolicyFromXML(rbacFile);
			System.out.println("RBAC policy");
			System.out.println("name:\t"+rbacPolicy.getName());
			System.out.println("users ("+rbacPolicy.getUser().size()+"):\t"+userListToString(rbacPolicy.getUser()));
			System.out.println("roles ("+rbacPolicy.getRole().size()+"):\t"+roleListToString(rbacPolicy.getRole()));
//			System.out.println("permissions ("+rbacPolicy.getPermission().size()+"):\t"+permissionListToString(rbacPolicy.getPermission()));
			System.out.println("Su ("+rbacPolicy.getSu().size()+"):\t"+suToString(rbacPolicy.getSu()));
			System.out.println("Du ("+rbacPolicy.getDu().size()+"):\t"+duToString(rbacPolicy.getDu()));
			System.out.println("Sr ("+rbacPolicy.getSr().size()+"):\t"+srToString(rbacPolicy.getSr()));
			System.out.println("Dr ("+rbacPolicy.getDr().size()+"):\t"+drToString(rbacPolicy.getDr()));
			System.out.println("UR ("+rbacPolicy.getUserRoleAssignment().size()+"):\t"+urToString(rbacPolicy.getUserRoleAssignment()));
//			System.out.println("PR ("+rbacPolicy.getPermissionRoleAssignment().size()+"):\t"+prToString(rbacPolicy.getPermissionRoleAssignment()));
			System.out.println("SSoD ("+rbacPolicy.getSSoDConstraint().size()+"):\t"+ssodToString(rbacPolicy.getSSoDConstraint()));
			System.out.println("DSoD ("+rbacPolicy.getDSoDConstraint().size()+"):\t"+dsodToString(rbacPolicy.getDSoDConstraint()));

			//					+ ", activationHierarchy=" + activationHierarchy
			//					+ ", inheritanceHierarchy=" + inheritanceHierarchy + "]";
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String ssodToString(List<SSoDConstraint> dsoDConstraint) {
		String out = "{";
		List<Role> roleList = new ArrayList<>();
		if(dsoDConstraint.size()>0) {
			roleList.addAll(dsoDConstraint.get(0).getSodSet());
			out += "("+roleListToString(roleList)+","+dsoDConstraint.get(0).getCardinality()+")";
		}
		for (int i = 1; i < dsoDConstraint.size(); i++) {
			roleList.clear();
			roleList.addAll(dsoDConstraint.get(0).getSodSet());
			out += ",("+roleListToString(roleList)+","+dsoDConstraint.get(0).getCardinality()+")";
		}
		out += "}";
		return out;
	}
	
	private static String dsodToString(List<DSoDConstraint> ssoDConstraint) {
		String out = "{";
		List<Role> roleList = new ArrayList<>();
		if(ssoDConstraint.size()>0) {
			roleList.addAll(ssoDConstraint.get(0).getSodSet());
			out += "("+roleListToString(roleList)+","+ssoDConstraint.get(0).getCardinality()+")";
		}
		for (int i = 1; i < ssoDConstraint.size(); i++) {
			roleList.clear();
			roleList.addAll(ssoDConstraint.get(0).getSodSet());
			out += ",("+roleListToString(roleList)+","+ssoDConstraint.get(0).getCardinality()+")";
		}
		out += "}";
		return out;
	}

	private static String urToString(List<UserRoleAssignment> userRoleAssignment) {
		String out = "{";
		if(userRoleAssignment.size()>0) out += "("+userRoleAssignment.get(0).getUser().getName()+","+userRoleAssignment.get(0).getRole().getName()+")";
		for (int i = 1; i < userRoleAssignment.size(); i++) {
			out += ","+ "("+userRoleAssignment.get(i).getUser().getName()+","+userRoleAssignment.get(i).getRole().getName()+")";
		}
		out += "}";
		return out;
	}
	
	private static String prToString(List<PermissionRoleAssignment> userRoleAssignment) {
		String out = "{";
		if(userRoleAssignment.size()>0) out += "("+userRoleAssignment.get(0).getPermission().getName()+","+userRoleAssignment.get(0).getRole().getName()+")";
		for (int i = 1; i < userRoleAssignment.size(); i++) {
			out += ","+ "("+userRoleAssignment.get(i).getPermission().getName()+","+userRoleAssignment.get(i).getRole().getName()+")";
		}
		out += "}";
		return out;
	}
	private static String suToString(List<Su> su) {
		String out = "{";
		if(su.size()>0) out += "("+su.get(0).getUser().getName()+","+su.get(0).getCardinality()+")";
		for (int i = 1; i < su.size(); i++) {
			out += ",("+su.get(i).getUser().getName()+","+su.get(i).getCardinality()+")";
		}
		out += "}";
		return out;
	}
	
	private static String srToString(List<Sr> su) {
		String out = "{";
		if(su.size()>0) out += "("+su.get(0).getRole().getName()+","+su.get(0).getCardinality()+")";
		for (int i = 1; i < su.size(); i++) {
			out += ",("+su.get(i).getRole().getName()+","+su.get(i).getCardinality()+")";
		}
		out += "}";
		return out;
	}
	
	private static String duToString(List<Du> su) {
		String out = "{";
		if(su.size()>0) out += "("+su.get(0).getUser().getName()+","+su.get(0).getCardinality()+")";
		for (int i = 1; i < su.size(); i++) {
			out += ",("+su.get(i).getUser().getName()+","+su.get(i).getCardinality()+")";
		}
		out += "}";
		return out;
	}
	
	private static String drToString(List<Dr> su) {
		String out = "{";
		if(su.size()>0) out += "("+su.get(0).getRole().getName()+","+su.get(0).getCardinality()+")";
		for (int i = 1; i < su.size(); i++) {
			out += ",("+su.get(i).getRole().getName()+","+su.get(i).getCardinality()+")";
		}
		out += "}";
		return out;
	}

	private static String permissionListToString(List<Permission> permission) {
		String out = "{";
		if(permission.size()>0) out += permission.get(0).getName();
		for (int i = 1; i < permission.size(); i++) {
			out += ","+permission.get(i).getName();
		}
		out += "}";
		return out;
	}

	private static String roleListToString(List<Role> role) {
		String out = "{";
		if(role.size()>0) out += role.get(0).getName();
		for (int i = 1; i < role.size(); i++) {
			out += ","+role.get(i).getName();
		}
		out += "}";
		return out;
	}

	private static String userListToString(List<User> user) {
		String out = "{";
		if(user.size()>0) out += user.get(0).getName();
		for (int i = 1; i < user.size(); i++) {
			out += ","+user.get(i).getName();
		}
		out += "}";
		return out;
	}

}
