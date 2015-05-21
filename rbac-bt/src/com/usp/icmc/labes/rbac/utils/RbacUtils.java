package com.usp.icmc.labes.rbac.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.acut.RbacState;
import com.usp.icmc.labes.rbac.model.ActivationHierarchy;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Hierarchy;
import com.usp.icmc.labes.rbac.model.InheritanceHierarchy;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.SoDConstraint;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;

public class RbacUtils {

	private XStream xstream;

	static RbacUtils instance;

	private RbacUtils() {
		xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.ID_REFERENCES);
		xstream.alias("rbac", RbacPolicy.class);
		xstream.alias("user", User.class);
		xstream.alias("role", Role.class);
		xstream.alias("prms", Permission.class);
		xstream.alias("uras", UserRoleAssignment.class);
		xstream.alias("urac", UserRoleActivation.class);
		xstream.alias("pras", PermissionRoleAssignment.class);
		xstream.alias("pras", PermissionRoleAssignment.class);
		xstream.alias("sod", SoDConstraint.class);
		xstream.alias("ssd", SSoDConstraint.class);
		xstream.alias("dsd", DSoDConstraint.class);
		xstream.alias("hie", Hierarchy.class);
		xstream.alias("ah", ActivationHierarchy.class);
		xstream.alias("ih", InheritanceHierarchy.class);
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

	public boolean userExists(RbacPolicy pol, User usr){
		return pol.getUser().contains(usr);
	}

	public boolean roleExists(RbacPolicy pol, Role rol){
		return pol.getRole().contains(rol);
	}

	public boolean permissionExists(RbacPolicy pol, Permission pr){
		return pol.getPermission().contains(pr);
	}


	public boolean userRoleAssignmentExists(RbacPolicy pol, User usr, Role rol) {
		return (getUserRoleAssignment(pol, usr, rol) != null);
	}

	public UserRoleAssignment getUserRoleAssignment(RbacPolicy pol, User usr, Role rol) {
		for (UserRoleAssignment el : pol.getUserRoleAssignment()) {
			if(el.getUser().equals(usr) && el.getRole().equals(rol)){
				return el;
			}
		}
		return null;
	}

	public Set<UserRoleAssignment> getUserRoleAssignmentWithUser(RbacPolicy pol, User usr) {
		Set<UserRoleAssignment> result = new HashSet<UserRoleAssignment>(); 
		for (UserRoleAssignment el : pol.getUserRoleAssignment()) {
			if(el.getUser().equals(usr)){
				result.add(el);
			}
		}
		return result;
	}

	public Set<UserRoleAssignment> getUserRoleAssignmentWithRole(RbacPolicy pol, Role rol) {
		Set<UserRoleAssignment> result = new HashSet<UserRoleAssignment>(); 
		for (UserRoleAssignment el : pol.getUserRoleAssignment()) {
			if(el.getRole().equals(rol)){
				result.add(el);
			}
		}
		return result;
	}

	public boolean userRoleActivationExists(RbacPolicy policy, User user, Role role) {
		return (getUserRoleActivation(policy, user, role)!=null);
	}	

	public UserRoleActivation getUserRoleActivation(RbacPolicy policy, User user, Role role) {
		for (UserRoleActivation el : policy.getUserRoleActivation()) {
			if(el.getUser().equals(user) && el.getRole().equals(role)){
				return el;
			}
		}
		return null;
	}

	public Set<UserRoleActivation> getUserRoleActivationWithUser(RbacPolicy policy, User user) {
		Set<UserRoleActivation> result = new HashSet<UserRoleActivation>();
		for (UserRoleActivation el : policy.getUserRoleActivation()) {
			if(el.getUser().equals(user)){
				result.add(el);
			}
		}
		return result;
	}

	public Set<UserRoleActivation> getUserRoleActivationWithRole(RbacPolicy policy, Role role) {
		Set<UserRoleActivation> result = new HashSet<UserRoleActivation>();
		for (UserRoleActivation el : policy.getUserRoleActivation()) {
			if(el.getRole().equals(role)){
				result.add(el);
			}
		}
		return result;
	}

	public boolean permissionRoleAssignmentExists(RbacPolicy pol, Permission pr, Role rol) {
		if(getPermissionRoleAssignment(pol, pr, rol) != null){
			return true;
		}
		return false;
	}

	public PermissionRoleAssignment getPermissionRoleAssignment(RbacPolicy pol, Permission pr, Role rol) {
		Set<PermissionRoleAssignment> result = new HashSet<PermissionRoleAssignment>();
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getPermission().equals(pr) && el.getRole().equals(rol)){
				return el;
			}
		}
		return null;
	}

	public Set<PermissionRoleAssignment> getPermissionRoleAssignmentWithRole(RbacPolicy pol, Role rol) {
		Set<PermissionRoleAssignment> result = new HashSet<PermissionRoleAssignment>();
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getRole().equals(rol)){
				result.add(el);
			}
		}
		return result;
	}

	public Set<PermissionRoleAssignment> getPermissionRoleAssignmentWithPermission(RbacPolicy pol, Permission pr) {
		Set<PermissionRoleAssignment> result = new HashSet<PermissionRoleAssignment>();
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getPermission().equals(pr)){
				result.add(el);
			}
		}
		return result;
	}

	public boolean afterAssignSuIsValid(RbacPolicy policy, User user, Role role) {
		Set<UserRoleAssignment> urList = getUserRoleAssignmentWithUser(policy, user);
		int total = urList.size();
		return total<user.getStaticCardinality() ;
	}

	public boolean afterAssignSrIsValid(RbacPolicy policy, User user, Role role) {
		Set<UserRoleAssignment> urList = getUserRoleAssignmentWithRole(policy, role);
		int total = urList.size();
		return total<role.getStaticCardinality();
	}

	public boolean afterActivateDuIsValid(RbacPolicy policy, User user, Role role) {
		Set<UserRoleActivation> urList = getUserRoleActivationWithUser(policy, user);
		int total = urList.size();
		return total<user.getDynamicCardinality();
	}

	public boolean afterActivateDrIsValid(RbacPolicy policy, User user, Role role) {
		Set<UserRoleActivation> urList = getUserRoleActivationWithRole(policy, role);
		int total = urList.size();
		return total<role.getDynamicCardinality();
	}

	public Set<Role> getRolesAssignedToUser(RbacPolicy pol, User usr){
		Set<Role> result = new HashSet<Role>();
		List<UserRoleAssignment> userRoleAssignments = pol.getUserRoleAssignment();
		for (UserRoleAssignment ur : userRoleAssignments) {
			if(ur.getUser().equals(usr))
				result.add(ur.getRole());
		}
		return result;
	}

	//	boolean assignUser(RbacPolicy pol, User usr, Role rol){
	//		boolean userExists = userExists(pol, usr);
	//		boolean roleExists = roleExists(pol, rol); 
	//		boolean userRoleAssigned = userRoleAssignmentExists(pol,usr,rol);
	//		//		boolean commandIsValid = nextStateUserCardinality
	//
	//		if(		userExists &&
	//				roleExists &&
	//				!userRoleAssigned 
	//				){
	//			pol.getUserRoleAssignment().add(new UserRoleAssignment(usr,rol));
	//			return true;
	//		}
	//		return false;
	//	}
	//
	//	boolean deassignUser(RbacPolicy pol, User usr, Role rol){
	//		boolean userExists = userExists(pol, usr);
	//		boolean roleExists = roleExists(pol, rol); 
	//		boolean userRoleAssigned = userRoleAssignmentExists(pol,usr,rol);
	//
	//		if(		userExists &&
	//				roleExists &&
	//				userRoleAssigned 
	//				){
	//			UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
	//			pol.getUserRoleAssignment().remove(ur);
	//			return true;
	//		}
	//		return false;
	//	}
	//

	//
	//	boolean deactivateRole(RbacPolicy pol, User usr, Role rol){
	//		boolean userExists = userExists(pol, usr);
	//		boolean roleExists = roleExists(pol, rol); 
	//		boolean userRoleAssigned = userRoleAssignmentExists(pol,usr,rol);
	//		boolean userRoleActive = userRoleActive(pol,usr,rol);
	//
	//		if(		userExists &&
	//				roleExists &&
	//				userRoleAssigned &&
	//				userRoleActive
	//				){
	//			UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
	//			ur.getActiveRoles().remove(usr);
	//			return true;
	//		}
	//		return false;
	//	}
	//
	//	public boolean userRoleActive(RbacPolicy pol, User usr, Role rol) {
	//		UserRoleAssignment ur = getUserRoleAssignment(pol, usr, rol);
	//		if(
	//				ur!=null && //TODO check with findbugs 
	//				ur.getActiveRoles().contains(rol)){
	//			return true;
	//		}
	//		return false;
	//	}
	//
	//	public boolean grantPermission(RbacPolicy pol, Permission pr, Role rol) {
	//		boolean permissionExists = permissionExists(pol, pr);
	//		boolean roleExists = roleExists(pol, rol);
	//		boolean permissionRoleAssigned = permissionRoleAssignmentExists(pol,pr,rol);
	//
	//		if(		permissionExists &&
	//				roleExists &&
	//				!permissionRoleAssigned
	//				){
	//			pol.getPermissionRoleAssignment().add(new PermissionRoleAssignment(pr,rol));
	//			return true;
	//		}
	//		return false;
	//	}

	//

	//
	//	public boolean afterAssignSsodIsValid(RbacPolicy policy, User user,
	//			Role role) {
	//		// TODO Auto-generated method stub
	//		return true;
	//	}
	//
	//	public boolean afterAssignDsodIsValid(RbacPolicy policy, User user,
	//			Role role) {
	//		// TODO Auto-generated method stub
	//		return true;
	//	}
	//
	//	public List<Role> getSeniorsActivation(RbacPolicy policy, Role role) {
	//		List<Role> toCheck = new ArrayList<Role>();
	//		List<Role> sr = new ArrayList<Role>();
	//		toCheck.add(role);
	//		Role current = null;
	//		while(!toCheck.isEmpty()){
	//			current = toCheck.get(0);
	//			for (ActivationHierarchy ah: policy.getActivationHierarchy()) {
	//				if(ah.getJunior().equals(current)){
	//					toCheck.add(ah.getSenior());
	//					sr.add(ah.getSenior());
	//				}
	//			}
	//			toCheck.remove(current);
	//		}
	//		return sr;
	//	}
	//
	//	public List<Role> getJuniorsActivation(RbacPolicy policy, Role role) {
	//		List<Role> toCheck = new ArrayList<Role>();
	//		List<Role> jr = new ArrayList<Role>();
	//		toCheck.add(role);
	//		Role current = null;
	//		while(!toCheck.isEmpty()){
	//			current = toCheck.get(0);
	//			for (ActivationHierarchy ah: policy.getActivationHierarchy()) {
	//				if(ah.getSenior().equals(current)){
	//					toCheck.add(ah.getJunior());
	//					jr.add(ah.getJunior());
	//				}
	//			}
	//			toCheck.remove(current);
	//		}
	//		return jr;
	//	}
	//
	//
	//	public List<ActivationHierarchy> getActivationHierarchiesWithJunior(RbacPolicy policy, Role role) {
	//		List<ActivationHierarchy> result = new ArrayList<ActivationHierarchy>();
	//		for (ActivationHierarchy ah: policy.getActivationHierarchy()) {
	//			if(ah.getJunior().equals(role)){
	//				result.add(ah);
	//			}
	//		}
	//		return result;
	//	}
	//	
	//	public List<ActivationHierarchy> getActivationHierarchyWithSenior(RbacPolicy policy, Role role) {
	//		List<ActivationHierarchy> result = new ArrayList<ActivationHierarchy>();
	//		for (ActivationHierarchy ah: policy.getActivationHierarchy()) {
	//			if(ah.getSenior().equals(role)){
	//				result.add(ah);
	//			}
	//		}
	//		return result;
	//	}
	//
	//	public ActivationHierarchy getActivationHierarchy(RbacPolicy policy,
	//			Role senior, Role junior) {
	//		for (ActivationHierarchy ah: policy.getActivationHierarchy()) {
	//			if(ah.getSenior().equals(senior) && ah.getJunior().equals(junior)){
	//				return ah;
	//			}
	//		}
	//		return null;
	//	}
}