package com.usp.icmc.labes.rbac.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.usp.icmc.labes.rbac.model.masood.ansi.Permission;
import com.usp.icmc.labes.rbac.model.masood.ansi.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.masood.ansi.RbacPolicy;
import com.usp.icmc.labes.rbac.model.masood.ansi.Role;
import com.usp.icmc.labes.rbac.model.masood.ansi.User;
import com.usp.icmc.labes.rbac.model.masood.ansi.UserRoleAssignment;

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

	public boolean permissionExists(RbacPolicy pol, Permission pr){
		return pol.getPermission().contains(pr);
	}


	boolean assignUser(RbacPolicy pol, User usr, Role rol){
		boolean userExists = userExists(pol, usr);
		boolean roleExists = roleExists(pol, rol); 
		boolean userRoleAssigned = userRoleAssignmentExists(pol,usr,rol);
//		boolean commandIsValid = nextStateUserCardinality
		
		if(		userExists &&
				roleExists &&
				!userRoleAssigned 
				){
			pol.getUserRoleAssignment().add(new UserRoleAssignment(usr,rol));
			return true;
		}
		return false;
	}

	boolean deassignUser(RbacPolicy pol, User usr, Role rol){
		boolean userExists = userExists(pol, usr);
		boolean roleExists = roleExists(pol, rol); 
		boolean userRoleAssigned = userRoleAssignmentExists(pol,usr,rol);
		
		if(		userExists &&
				roleExists &&
				userRoleAssigned 
				){
			UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
			pol.getUserRoleAssignment().remove(ur);
			return true;
		}
		return false;
	}

	public boolean userRoleAssignmentExists(RbacPolicy pol, User usr, Role rol) {
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

	boolean deactivateRole(RbacPolicy pol, User usr, Role rol){
		boolean userExists = userExists(pol, usr);
		boolean roleExists = roleExists(pol, rol); 
		boolean userRoleAssigned = userRoleAssignmentExists(pol,usr,rol);
		boolean userRoleActive = userRoleActive(pol,usr,rol);

		if(		userExists &&
				roleExists &&
				userRoleAssigned &&
				userRoleActive
				){
			UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
			ur.getActiveRoles().remove(usr);
			return true;
		}
		return false;
	}

	public boolean userRoleActive(RbacPolicy pol, User usr, Role rol) {
		UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
		if(
				ur!=null && //TODO check with findbugs 
				ur.getActiveRoles().contains(rol)){
			return true;
		}
		return false;
	}

	public boolean grantPermission(RbacPolicy pol, Permission pr, Role rol) {
		boolean permissionExists = permissionExists(pol, pr);
		boolean roleExists = roleExists(pol, rol);
		boolean permissionRoleAssigned = permissionRoleAssignmentExists(pol,pr,rol);

		if(		permissionExists &&
				roleExists &&
				!permissionRoleAssigned
				){
			pol.getPermissionRoleAssignment().add(new PermissionRoleAssignment(pr,rol));
			return true;
		}
		return false;
	}
	public boolean permissionRoleAssignmentExists(RbacPolicy pol, Permission pr, Role rol) {
		if(getPermissionRoleAssignment(pol, pr, rol) != null){
			return true;
		}
		return false;
	}

	public PermissionRoleAssignment getPermissionRoleAssignment(RbacPolicy pol, Permission pr, Role rol) {
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getPermission().equals(pr) && el.getRole().equals(rol)){
				return el;
			}
		}
		return null;
	}

	public boolean afterAssignSuIsValid(RbacPolicy policy, User user, Role role) {
		List<UserRoleAssignment> urList = policy.getUserRoleAssignment();
		int total=0;
		for (UserRoleAssignment ur : urList) {
			if(ur.getUser().equals(user)) total++;
		}
		return total<=user.getStaticCardinality();
	}
	
	public boolean afterAssignSrIsValid(RbacPolicy policy, User user, Role role) {
		List<UserRoleAssignment> urList = policy.getUserRoleAssignment();
		int total=0;
		for (UserRoleAssignment ur : urList) {
			if(ur.getRole().equals(role)) total++;
		}
		return total<=user.getStaticCardinality();
	}

	public boolean afterActivateDuIsValid(RbacPolicy policy, User user, Role role) {
		List<UserRoleAssignment> urList = policy.getUserRoleAssignment();
		int total=0;
		for (UserRoleAssignment ur : urList) {
			if(ur.getUser().equals(user)) total+=ur.getActiveRoles().size();
		}
		return total<=user.getDynamicCardinality();
	}
	
	public boolean afterActivateDrIsValid(RbacPolicy policy, User user, Role role) {
		List<UserRoleAssignment> urList = policy.getUserRoleAssignment();
		int total=0;
		for (UserRoleAssignment ur : urList) {
			if(ur.getActiveRoles().contains(role)) total++;
		}
		return total<=role.getDynamicCardinality();
	}

	public boolean afterAssignSsodIsValid(RbacPolicy policy, User user,
			Role role) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean afterAssignDsodIsValid(RbacPolicy policy, User user,
			Role role) {
		// TODO Auto-generated method stub
		return true;
	}

}
