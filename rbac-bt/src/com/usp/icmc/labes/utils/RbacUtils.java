package com.usp.icmc.labes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacMutableElement;
import com.usp.icmc.labes.rbac.model.RbacMutant;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.RbacTuple;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;

public class RbacUtils {


	public enum RbacFaultType {
		SuFailed,
		SrFailed,
		SsodFailed,
		DuFailed,
		DrFailed,
		DsodFailed,
	}

	static RbacUtils instance;

	public static RbacUtils getInstance() {
		if(instance ==null){
			instance = new RbacUtils();
		}
		return instance;
	};


	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance(); 

	private RbacUtils() {
	}

	public boolean afterActivateDrIsValid(RbacTuple policy, Role role) {
		Set<UserRoleActivation> urList = getUserRoleActivationWithRole(policy, role);
		int total = urList.size();
		Dr constr = getDr(policy, role);
		if(constr==null) return true;
		return total<constr.getCardinality();
	}

	public boolean afterActivateDuIsValid(RbacTuple policy, User user) {
		Set<UserRoleActivation> urList = getUserRoleActivationWithUser(policy, user);
		int total = urList.size();
		Du constr = getDu(policy, user);
		if(constr==null) return true;
		return total<constr.getCardinality();
	}

	public boolean afterAssignSrIsValid(RbacTuple policy, Role role) {
		Set<UserRoleAssignment> urList = getUserRoleAssignmentWithRole(policy, role);
		int total = urList.size();
		Sr constr = getSr(policy, role);
		if(constr==null) return true;
		return total<constr.getCardinality();
	}

	public boolean afterAssignSuIsValid(RbacTuple policy, User user) {
		Set<UserRoleAssignment> urList = getUserRoleAssignmentWithUser(policy, user);
		int total = urList.size();
		Su constr = getSu(policy, user);
		if(constr==null) return true; 
		return total<constr.getCardinality();
	}

	public void failedDueTo(RbacTuple p, RbacFaultType faultType, RbacMutableElement reason) {
		synchronized (p.getProperties()) {
			p.getProperties().putIfAbsent(faultType, new HashSet<RbacMutableElement>());
			((Set)p.getProperties().get(faultType)).add(reason);	
		}
	}


	public Dr getDr(RbacTuple pol, Role rol) {
		for (Dr card: pol.getDr()) {
			if(card.getRole().equals(rol)){
				return card;
			}
		}
		return null;
	}

	public List<DSoDConstraint> getDSoD(RbacTuple pol, Role rol) {
		List<DSoDConstraint> result = new ArrayList<DSoDConstraint>();
		for (DSoDConstraint set : pol.getDSoDConstraint()) {
			if(set.getSodSet().contains(rol)) result.add(set);
		}
		return result;
	}

	public Du getDu(RbacTuple pol, User usr) {
		for (Du card: pol.getDu()) {
			if(card.getUser().equals(usr)){
				return card;
			}
		}
		return null;
	}

	public Permission getPermissionByName(RbacTuple t, String n){
		for (Permission u: t.getPermission()) {
			if(u.getName().equals(n)){
				return u;
			}
		}
		return null;
	}

	public PermissionRoleAssignment getPermissionRoleAssignment(RbacTuple pol, Permission pr, Role rol) {
		Set<PermissionRoleAssignment> result = new HashSet<PermissionRoleAssignment>();
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getPermission().equals(pr) && el.getRole().equals(rol)){
				return el;
			}
		}
		return null;
	}	

	public Set<PermissionRoleAssignment> getPermissionRoleAssignmentWithPermission(RbacTuple pol, Permission pr) {
		Set<PermissionRoleAssignment> result = new HashSet<PermissionRoleAssignment>();
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getPermission().equals(pr)){
				result.add(el);
			}
		}
		return result;
	}

	public Set<PermissionRoleAssignment> getPermissionRoleAssignmentWithRole(RbacTuple pol, Role rol) {
		Set<PermissionRoleAssignment> result = new HashSet<PermissionRoleAssignment>();
		for (PermissionRoleAssignment el : pol.getPermissionRoleAssignment()) {
			if(el.getRole().equals(rol)){
				result.add(el);
			}
		}
		return result;
	}

	public Role getRoleByName(RbacTuple t, String n){
		for (Role u: t.getRole()) {
			if(u.getName().equals(n)){
				return u;
			}
		}
		return null;
	}

	public Set<Role> getRolesActivatedByUser(RbacTuple pol, User usr){
		Set<Role> result = new HashSet<Role>();
		List<UserRoleActivation> userRoleActivation = pol.getUserRoleActivation();
		for (UserRoleActivation ur : userRoleActivation) {
			if(ur.getUser().equals(usr))
				result.add(ur.getRole());
		}
		return result;
	}

	public Set<Role> getRolesAssignedToUser(RbacTuple pol, User usr){
		Set<Role> result = new HashSet<Role>();
		List<UserRoleAssignment> userRoleAssignments = pol.getUserRoleAssignment();
		for (UserRoleAssignment ur : userRoleAssignments) {
			if(ur.getUser().equals(usr))
				result.add(ur.getRole());
		}
		return result;
	}

	public Sr getSr(RbacTuple pol, Role rol) {
		for (Sr card: pol.getSr()) {
			if(card.getRole().equals(rol)){
				return card;
			}
		}
		return null;
	}

	public List<SSoDConstraint> getSSoD(RbacTuple pol, Role rol) {
		List<SSoDConstraint> result = new ArrayList<SSoDConstraint>();
		for (SSoDConstraint set : pol.getSSoDConstraint()) {
			if(set.getSodSet().contains(rol)) result.add(set);
		}
		return result;
	}

	public Su getSu(RbacTuple pol, User usr) {
		for (Su card: pol.getSu()) {
			if(card.getUser().equals(usr)){
				return card;
			}
		}
		return null;
	}

	public User getUserByName(RbacTuple t, String n){
		for (User u: t.getUser()) {
			if(u.getName().equals(n)){
				return u;
			}
		}
		return null;
	}

	public UserRoleActivation getUserRoleActivation(RbacTuple policy, User user, Role role) {
		for (UserRoleActivation el : policy.getUserRoleActivation()) {
			if(el.getUser().equals(user) && el.getRole().equals(role)){
				return el;
			}
		}
		return null;
	}

	public Set<UserRoleActivation> getUserRoleActivationWithRole(RbacTuple policy, Role role) {
		Set<UserRoleActivation> result = new HashSet<UserRoleActivation>();
		for (UserRoleActivation el : policy.getUserRoleActivation()) {
			if(el.getRole().equals(role)){
				result.add(el);
			}
		}
		return result;
	}

	public Set<UserRoleActivation> getUserRoleActivationWithUser(RbacTuple policy, User user) {
		Set<UserRoleActivation> result = new HashSet<UserRoleActivation>();
		for (UserRoleActivation el : policy.getUserRoleActivation()) {
			if(el.getUser().equals(user)){
				result.add(el);
			}
		}
		return result;
	}

	public UserRoleAssignment getUserRoleAssignment(RbacTuple pol, User usr, Role rol) {
		for (UserRoleAssignment el : pol.getUserRoleAssignment()) {
			if(el.getUser().equals(usr) && el.getRole().equals(rol)){
				return el;
			}
		}
		return null;
	}

	public Set<UserRoleAssignment> getUserRoleAssignmentWithRole(RbacTuple pol, Role rol) {
		Set<UserRoleAssignment> result = new HashSet<UserRoleAssignment>(); 
		for (UserRoleAssignment el : pol.getUserRoleAssignment()) {
			if(el.getRole().equals(rol)){
				result.add(el);
			}
		}
		return result;
	}

	public Set<UserRoleAssignment> getUserRoleAssignmentWithUser(RbacTuple pol, User usr) {
		Set<UserRoleAssignment> result = new HashSet<UserRoleAssignment>(); 
		for (UserRoleAssignment el : pol.getUserRoleAssignment()) {
			if(el.getUser().equals(usr)){
				result.add(el);
			}
		}
		return result;
	}

	public boolean isValidPolicy(RbacMutant mutPol) {

		int[] staticUser 	= new int[mutPol.getUser().size()];
		int[] staticRole 	= new int[mutPol.getRole().size()];

		int[] dynamicUser 	= new int[mutPol.getUser().size()];
		int[] dynamicRole 	= new int[mutPol.getRole().size()];

		Map<User,Set<Role>> urAssignment = new HashMap<User,Set<Role>>();
		Map<User,Set<Role>> urActivation = new HashMap<User,Set<Role>>();

		for (UserRoleAssignment el : mutPol.getUserRoleAssignment()) {
			int userIndex = mutPol.getUser().indexOf(el.getUser());
			int roleIndex = mutPol.getRole().indexOf(el.getRole());
			staticUser[userIndex]++;
			staticRole[roleIndex]++;
			urAssignment.putIfAbsent(el.getUser(), new HashSet<>());
			urAssignment.get(el.getUser()).add(el.getRole());
		}
		for (UserRoleActivation el : mutPol.getUserRoleActivation()) {
			int userIndex = mutPol.getUser().indexOf(el.getUser());
			int roleIndex = mutPol.getRole().indexOf(el.getRole());
			dynamicUser[userIndex]++;
			dynamicRole[roleIndex]++;
			urActivation.putIfAbsent(el.getUser(), new HashSet<>());
			urActivation.get(el.getUser()).add(el.getRole());
		}
		for (Sr i : mutPol.getSr()) {
			int roleIndex = mutPol.getRole().indexOf(i.getRole());
			if (staticRole[roleIndex]>i.getCardinality()) return false; 

		}
		for (Dr i : mutPol.getDr()) {
			int roleIndex = mutPol.getRole().indexOf(i.getRole());
			if (dynamicRole[roleIndex]>i.getCardinality()) return false;
		}
		for (Su i : mutPol.getSu()) {
			int userIndex = mutPol.getUser().indexOf(i.getUser());
			if (staticUser[userIndex]>i.getCardinality()) return false;
		}
		for (Du i : mutPol.getDu()) {
			int userIndex = mutPol.getUser().indexOf(i.getUser());
			if (dynamicUser[userIndex]>i.getCardinality()) return false;
		}
		for (SSoDConstraint i : mutPol.getSSoDConstraint()) {
			if(i.getCardinality()<=0) return false;
			for (User u : urAssignment.keySet()) {
				Set<Role> set = new HashSet<Role>();
				set.addAll(i.getSodSet());
				set.retainAll(urAssignment.get(u));
				if(set.size()>i.getCardinality()) return false;
			}
		}
		for (DSoDConstraint i : mutPol.getDSoDConstraint()) {
			if(i.getCardinality()<=0) return false;
			for (User u : urActivation.keySet()) {
				Set<Role> set = new HashSet<Role>();
				set.addAll(i.getSodSet());
				set.retainAll(urActivation.get(u));
				if(set.size()>i.getCardinality()) return false;
			}

		}
		return true;
	}

	public RbacPolicy loadRbacPolicyFromXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException{
		RbacPolicy result = new RbacPolicy(); //(RbacPolicy) xstream.fromXML(xmlFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		Element fsmElement = doc.getDocumentElement();
		result.setName(fsmElement.getAttribute("name"));

		Node node = null;
		NodeList el = null;
		NodeList subEl = null;
		if(fsmElement.getElementsByTagName("users").getLength()>0){
			node = fsmElement.getElementsByTagName("users").item(0);
			el = ((Element)node).getElementsByTagName("user");
			subEl = null;
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				result.getUser().add(new User(in.getAttribute("name")));
			}
		}

		if(fsmElement.getElementsByTagName("roles").getLength()>0){
			node = fsmElement.getElementsByTagName("roles").item(0);
			el = ((Element)node).getElementsByTagName("role");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				result.getRole().add(new Role(in.getAttribute("name")));
			}
		}

		if(fsmElement.getElementsByTagName("permissions").getLength()>0){
			node = fsmElement.getElementsByTagName("permissions").item(0);
			el = ((Element)node).getElementsByTagName("permission");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				result.getPermission().add(new Permission(in.getAttribute("name")));
			}
		}

		if(fsmElement.getElementsByTagName("SuConstraints").getLength()>0){
			node = fsmElement.getElementsByTagName("SuConstraints").item(0);
			el = ((Element)node).getElementsByTagName("Su");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				result.getSu().add(new Su(getUserByName(result, in.getAttribute("user")), Integer.valueOf(in.getAttribute("cardinality"))));
			}
		}

		if(fsmElement.getElementsByTagName("DuConstraints").getLength()>0){
			node = fsmElement.getElementsByTagName("DuConstraints").item(0);
			el = ((Element)node).getElementsByTagName("Du");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				result.getDu().add(new Du(getUserByName(result, in.getAttribute("user")), Integer.valueOf(in.getAttribute("cardinality"))));
			}
		}

		if(fsmElement.getElementsByTagName("SrConstraints").getLength()>0){
			node = fsmElement.getElementsByTagName("SrConstraints").item(0);
			el = ((Element)node).getElementsByTagName("Sr");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				result.getSr().add(new Sr(getRoleByName(result, in.getAttribute("role")), Integer.valueOf(in.getAttribute("cardinality"))));
			}
		}

		if(fsmElement.getElementsByTagName("DrConstraints").getLength()>0){
			node = fsmElement.getElementsByTagName("DrConstraints").item(0);
			el = ((Element)node).getElementsByTagName("Dr");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				result.getDr().add(new Dr(getRoleByName(result, in.getAttribute("role")), Integer.valueOf(in.getAttribute("cardinality"))));
			}
		}

		if(fsmElement.getElementsByTagName("URAssignments").getLength()>0){
			node = fsmElement.getElementsByTagName("URAssignments").item(0);
			el = ((Element)node).getElementsByTagName("AS");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				User u = getUserByName(result, in.getAttribute("user"));
				Role r = getRoleByName(result, in.getAttribute("role"));
				result.getUserRoleAssignment().add(new UserRoleAssignment(u, r));
			}
		}

		if(fsmElement.getElementsByTagName("URActivations").getLength()>0){
			node = fsmElement.getElementsByTagName("URActivations").item(0);
			el = ((Element)node).getElementsByTagName("AC");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				User u = getUserByName(result, in.getAttribute("user"));
				Role r = getRoleByName(result, in.getAttribute("role"));
				result.getUserRoleActivation().add(new UserRoleActivation(u, r));
			}
		}

		if(fsmElement.getElementsByTagName("PRAssignments").getLength()>0){
			node = fsmElement.getElementsByTagName("PRAssignments").item(0);
			el = ((Element)node).getElementsByTagName("PA");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				Permission p = getPermissionByName(result, in.getAttribute("permission"));
				Role r = getRoleByName(result, in.getAttribute("role"));
				result.getPermissionRoleAssignment().add(new PermissionRoleAssignment(p, r));
			}
		}

		if(fsmElement.getElementsByTagName("SSoDConstraints").getLength()>0){
			node = fsmElement.getElementsByTagName("SSoDConstraints").item(0);
			el = ((Element)node).getElementsByTagName("SSoD");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				int card = Integer.valueOf(in.getAttribute("cardinality"));
				subEl = in.getElementsByTagName("role");
				List<Role> set = new ArrayList<Role>();
				for (int j = 0; j < subEl.getLength(); j++) {
					Element subIn = (Element) subEl.item(j);
					String roleName = subIn.getAttribute("name");
					set.add(getRoleByName(result, roleName));
				}
				result.getSSoDConstraint().add(new SSoDConstraint(set, card));
			}
		}

		if(fsmElement.getElementsByTagName("DSoDConstraints").getLength()>0){
			node = fsmElement.getElementsByTagName("DSoDConstraints").item(0);
			el = ((Element)node).getElementsByTagName("DSoD");
			for (int i = 0; i < el.getLength(); i++) {
				Element in = (Element) el.item(i);
				int card = Integer.valueOf(in.getAttribute("cardinality"));
				subEl = in.getElementsByTagName("role");
				List<Role> set = new ArrayList<Role>();
				for (int j = 0; j < subEl.getLength(); j++) {
					Element subIn = (Element) subEl.item(j);
					String roleName = subIn.getAttribute("name");
					set.add(getRoleByName(result, roleName));
				}
				result.getDSoDConstraint().add(new DSoDConstraint(set, card));
			}
		}

		return result;

	}


	public boolean permissionExists(RbacTuple pol, Permission pr){
		return pol.getPermission().contains(pr);
	}

	public boolean permissionRoleAssignmentExists(RbacTuple pol, Permission pr, Role rol) {
		if(getPermissionRoleAssignment(pol, pr, rol) != null){
			return true;
		}
		return false;
	}

	public boolean roleExists(RbacTuple pol, Role rol){
		return pol.getRole().contains(rol);
	}

	public boolean userExists(RbacTuple pol, User usr){
		return pol.getUser().contains(usr);
	}

	public boolean userRoleActivationExists(RbacTuple policy, User user, Role role) {
		return (getUserRoleActivation(policy, user, role)!=null);
	}

	public boolean userRoleAssignmentExists(RbacTuple pol, User usr, Role rol) {
		return (getUserRoleAssignment(pol, usr, rol) != null);
	}

	public void WriteRbacPolicyAsXML(RbacTuple rbacPol, File xmlFile) throws TransformerException, ParserConfigurationException{
		//		OutputStream fos = new FileOutputStream(xmlFile);
		//		xstream.toXML(rbacPol, fos);

		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement("RBAC");
		String name = null;
		if(rbacPol instanceof RbacPolicy){
			name = ((RbacPolicy) rbacPol).getName();
		}else if( rbacPol instanceof RbacAcut){
			name = ((RbacAcut) rbacPol).getPolicy().getName();
		}
		rootElement.setAttribute("name",name);

		Element users = doc.createElement("users");
		for (User el: rbacPol.getUser()) {
			Element child = doc.createElement("user");
			child.setAttribute("name", el.getName());
			users.appendChild(child);
		}
		rootElement.appendChild(users);

		Element roles = doc.createElement("roles");
		for (Role el: rbacPol.getRole()) {
			Element child = doc.createElement("role");
			child.setAttribute("name", el.getName());
			roles.appendChild(child);
		}
		rootElement.appendChild(roles);


		Element permissions = doc.createElement("permissions");
		for (Permission el: rbacPol.getPermission()) {
			Element child = doc.createElement("permission");
			child.setAttribute("name", el.getName());
			permissions.appendChild(child);
		}
		rootElement.appendChild(permissions);

		Element card = doc.createElement("SuConstraints");
		for (Su el: rbacPol.getSu()) {
			Element child = doc.createElement("Su");
			child.setAttribute("user", el.getUser().getName());
			child.setAttribute("cardinality", Integer.toString(el.getCardinality()));
			card.appendChild(child);
		}
		rootElement.appendChild(card);

		card = doc.createElement("DuConstraints");
		for (Du el: rbacPol.getDu()) {
			Element child = doc.createElement("Du");
			child.setAttribute("user", el.getUser().getName());
			child.setAttribute("cardinality", Integer.toString(el.getCardinality()));
			card.appendChild(child);
		}
		rootElement.appendChild(card);

		card = doc.createElement("SrConstraints");
		for (Sr el: rbacPol.getSr()) {
			Element child = doc.createElement("Sr");
			child.setAttribute("role", el.getRole().getName());
			child.setAttribute("cardinality", Integer.toString(el.getCardinality()));
			card.appendChild(child);
		}
		rootElement.appendChild(card);

		card = doc.createElement("DrConstraints");
		for (Dr el: rbacPol.getDr()) {
			Element child = doc.createElement("Dr");
			child.setAttribute("role", el.getRole().getName());
			child.setAttribute("cardinality", Integer.toString(el.getCardinality()));
			card.appendChild(child);
		}
		rootElement.appendChild(card);

		card = doc.createElement("URAssignments");
		for (UserRoleAssignment el: rbacPol.getUserRoleAssignment()) {
			Element child = doc.createElement("AS");
			child.setAttribute("user", el.getUser().getName());
			child.setAttribute("role", el.getRole().getName());
			card.appendChild(child);
		}
		rootElement.appendChild(card);

		card = doc.createElement("URActivations");
		for (UserRoleActivation el: rbacPol.getUserRoleActivation()) {
			Element child = doc.createElement("AC");
			child.setAttribute("user", el.getUser().getName());
			child.setAttribute("role", el.getRole().getName());
			card.appendChild(child);
		}
		rootElement.appendChild(card);

		card = doc.createElement("PRAssignments");
		for (PermissionRoleAssignment el: rbacPol.getPermissionRoleAssignment()) {
			Element child = doc.createElement("PA");
			child.setAttribute("permission", el.getPermission().getName());
			child.setAttribute("role", el.getRole().getName());
			card.appendChild(child);
		}
		rootElement.appendChild(card);

		Element sods = doc.createElement("SSoDConstraints");
		Element rEl = null;
		for (SSoDConstraint el: rbacPol.getSSoDConstraint()) {
			Element sod = doc.createElement("SSoD");
			sod.setAttribute("cardinality", Integer.toString(el.getCardinality()));
			for (Role r : el.getSodSet()) {
				rEl = doc.createElement("role");
				rEl.setAttribute("name", r.getName());
				sod.appendChild(rEl);
			}
			sods.appendChild(sod);
		}
		rootElement.appendChild(sods);

		sods = doc.createElement("DSoDConstraints");
		rEl = null;
		for (DSoDConstraint el: rbacPol.getDSoDConstraint()) {
			Element sod = doc.createElement("DSoD");
			sod.setAttribute("cardinality", Integer.toString(el.getCardinality()));
			for (Role r : el.getSodSet()) {
				rEl = doc.createElement("role");
				rEl.setAttribute("name", r.getName());
				sod.appendChild(rEl);
			}
			sods.appendChild(sod);
		}
		rootElement.appendChild(sods);

		doc.appendChild(rootElement);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(xmlFile);

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		//		OutputStream fos = new FileOutputStream(fsmFile);
		//		xstream.toXML(fsm, fos);
	}

	public RbacRequest createRbacRequest(String input, RbacTuple rbacAcut) {
		Pattern p = Pattern.compile("([AD][SC])\\(([a-zA-Z0-9_-]+),([a-zA-Z0-9_-]+)\\)");
		Matcher m = p.matcher(input);
		m.matches();
		String rqType = m.group(1);
		String uStr = m.group(2);
		String rStr = m.group(3);
		User u = getUserByName(rbacAcut,uStr);
		Role r = getRoleByName(rbacAcut,rStr);
		switch (rqType) {
		case RbacRequest.ASSIGN_UR:
			return new RbacRequestAssignUR(u, r);
		case RbacRequest.DEASSIGN_UR:
			return new RbacRequestDeassignUR(u, r);
		case RbacRequest.ACTIVATE_UR:
			return new RbacRequestActivateUR(u, r);
		case RbacRequest.DEACTIVATE_UR:
			return new RbacRequestDeactivateUR(u, r);
		} 
		return null;
	}

	public List<RbacPolicy> loadMutantsFromTxTFile(File sutMutantsFile) throws IOException, ParserConfigurationException, SAXException {
		List<RbacPolicy> mutList = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(sutMutantsFile));
		while (br.ready()) {
			File mut = new File(sutMutantsFile.getParentFile(),br.readLine());
			RbacPolicy mutPol = loadRbacPolicyFromXML(mut);
			mutList.add(mutPol);
		}
		br.close();
		return mutList;
	}

	public List<RbacRequest> generateRequests(RbacTuple rbac) {
		List<RbacRequest> input = new ArrayList<RbacRequest>();
		for (Role rol: rbac.getRole()) {
			for (User usr: rbac.getUser()) {
				input.add(new RbacRequestAssignUR(usr, rol));
				input.add(new RbacRequestDeassignUR(usr, rol));
				input.add(new RbacRequestActivateUR(usr, rol));
				input.add(new RbacRequestDeactivateUR(usr, rol));
			}
		}
		return input;
	}
	
	public List<RbacRequest> generateRequestsWithPrms(RbacPolicy rbac) {
		List<RbacRequest> input = generateRequests(rbac);
		for (Role rol: rbac.getRole()) {
			for (Permission prms: rbac.getPermission()) {
				input.add(new RbacRequestAssignPR(prms, rol));
				input.add(new RbacRequestDeassignPR(prms, rol));
			}
		}
		return input;
	}

	public FsmState rbacToFsmState(RbacTuple p) {
		int totUser = p.getUser().size();
		int totRole = p.getRole().size();
		int totPrms = p.getPermission().size();
		
		BitSet state = new BitSet(2*(totUser*totRole+totRole*totPrms));
		
		for (int ui = 0; ui < totUser; ui++) {
			User usr = p.getUser().get(ui);
			for (int ri = 0; ri < totRole; ri++) {
				Role rol = p.getRole().get(ri);
				int index = ui*2*totRole+2*ri;
				if(userRoleAssignmentExists(p, usr, rol)) state.set(index);
				if(userRoleActivationExists(p, usr, rol)) state.set(index+1);
			}
		}
		for (int ri = 0; ri < totRole; ri++) {
			Role rol = p.getRole().get(ri);
			for (int pi = 0; pi < totPrms; pi++) {
				Permission pr = p.getPermission().get(pi);
				int index = 2*totUser*totRole+2*pi;
				if(permissionRoleAssignmentExists(p, pr, rol)) state.set(index);
			}	
		}
		
		FsmState fsmState= new FsmState(state.hashCode());
		fsmState.getProperties().put(BitSet.class, state);
		return fsmState;
	}

}