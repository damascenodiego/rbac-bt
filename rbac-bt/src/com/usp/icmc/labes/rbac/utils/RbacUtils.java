package com.usp.icmc.labes.rbac.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.usp.icmc.labes.rbac.model.RbacPolicy;

import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.Permission;

import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;

public class RbacUtils {

	private XStream xstream;

	static RbacUtils instance;

	private RbacUtils() {
		xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.ID_REFERENCES);
	} 

	public static RbacUtils getInstance() {
		if(instance ==null){
			instance = new RbacUtils();
		}
		return instance;
	}

	public RbacPolicy LoadRbacPolicyFromXML(File xmlFile){
		RbacPolicy result = (RbacPolicy) xstream.fromXML(xmlFile);
		return result;

	}

	public void WriteRbacPolicyAsXML(RbacPolicy rbacPol, File xmlFile) throws FileNotFoundException{
		OutputStream fos = new FileOutputStream(xmlFile);
		xstream.toXML(rbacPol, fos);
	}

	//////////////////////////////////////////////////////
	// check existance of user, roles and permissions	// 
	//////////////////////////////////////////////////////

	public boolean userExists(RbacPolicy pol, User usr){
		return pol.getUser().contains(usr);
	}

	public boolean roleExists(RbacPolicy pol, Role rol){
		return pol.getRole().contains(rol);
	}

	boolean permissionExists(RbacPolicy pol, Permission pr){
		return pol.getPermission().contains(pr);
	}


	boolean addPermission(RbacPolicy pol, Permission prm){
		if(	!permissionExists(pol, prm)){
			pol.getPermission().add(prm);
			return true;
		}
		return false;
	}

	boolean assignUser(RbacPolicy pol, User usr, Role rol){
		if(		userExists(pol, usr) &&
				roleExists(pol, rol) &&
				!userRoleAssigned(pol,usr,rol)
				){
			pol.getUserRoleAssignment().add(new UserRoleAssignment(usr,rol));
			return true;
		}
		return false;
	}

	boolean deassignUser(RbacPolicy pol, User usr, Role rol){
		if(		userExists(pol, usr) &&
				roleExists(pol, rol) &&
				userRoleAssigned(pol,usr,rol)
				){
			UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
			pol.getUserRoleAssignment().remove(ur);
			return true;
		}
		return false;
	}


	public boolean userRoleAssigned(RbacPolicy pol, User usr, Role rol) {
		if(getUserRoleAssignment(pol, usr, rol) != null){
			return true;
		}
		return false;
	}

	public UserRoleAssignment getUserRoleAssignment(RbacPolicy pol, User usr, Role rol) {
		for (UserRoleAssignment el : pol.getUserRoleAssignment()) {
			if(el.getUser().equals(usr) && el.getRole().equals(rol)){
				return el;
			}
		}
		return null;
	}

	boolean activateRole(RbacPolicy pol, User usr, Role rol){
		if(		userExists(pol, usr) &&
				roleExists(pol, rol) &&
				userRoleAssigned(pol,usr,rol) &&
				!userRoleActive(pol,usr,rol)
				){
			pol.getUserRoleAssignment().add(new UserRoleAssignment(usr,rol));
			return true;
		}
		return false;
	}

	boolean deactivateRole(RbacPolicy pol, User usr, Role rol){
		if(		userExists(pol, usr) &&
				roleExists(pol, rol) &&
				userRoleAssigned(pol,usr,rol) &&
				userRoleActive(pol,usr,rol)
				){
			UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
			ur.getActiveRoles().remove(usr);
			return true;
		}
		return false;
	}

	public boolean userRoleActive(RbacPolicy pol, User usr, Role rol) {
		UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
		if(ur.getActiveRoles().contains(rol)){
			return true;
		}
		return false;
	}

	public boolean grantPermission(RbacPolicy pol, Permission pr, Role rol) {
		if(		roleExists(pol, rol) &&
				permissionExists(pol, pr) &&
				!permissionGranted(pol,pr,rol)
				){
			pol.getPermissionRoleAssignment().add(new PermissionRoleAssignment(pr,rol));
			return true;
		}
		return false;
	}
	public boolean permissionGranted(RbacPolicy pol, Permission pr, Role rol) {
		if(getPermissionRoleAssignment(pol, pr, rol) != null){
			return true;
		}
		return false;
	}

	private PermissionRoleAssignment getPermissionRoleAssignment(RbacPolicy pol, Permission pr, Role rol) {
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getPermission().equals(pr) && el.getRole().equals(rol)){
				return el;
			}
		}
		return null;
	}

}
