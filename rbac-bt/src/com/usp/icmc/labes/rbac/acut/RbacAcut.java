package com.usp.icmc.labes.rbac.acut;

import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacAdvancedReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacElement;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;

public class RbacAcut implements RbacElement{

	private RbacPolicy policy;

	private RbacState initialState;

	private RbacState currentState;

	private RbacUtils utils = RbacUtils.getInstance();
	
	private RbacAdministrativeCommands admin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions sys = RbacSupportingSystemFunctions.getInstance();
	private RbacReviewFunctions rev = RbacReviewFunctions.getInstance();
	private RbacAdvancedReviewFunctions adv = RbacAdvancedReviewFunctions.getInstance();

	public RbacAcut(RbacPolicy p) { 
		policy = p;
		saveState(p);
		currentState = new RbacState(policy);
	}

	private void saveState(RbacPolicy p) {
		initialState = new RbacState(p);
	}

	public void reset(RbacState state) {
		for (UserRoleAssignment el : policy.getUserRoleAssignment()) {
			el.setRole(null);
			el.setUser(null);
			el.getActiveRoles().clear();
		}
		policy.getUserRoleAssignment().clear();

		for (PermissionRoleAssignment el : policy.getPermissionRoleAssignment()) {
			el.setPermission(null);
			el.setRole(null);
		}
		policy.getPermissionRoleAssignment().clear();

		for (UserRoleAssignment el : state.urCopy) {
			UserRoleAssignment newUr = new UserRoleAssignment(el.getUser(), el.getRole());
			newUr	.getActiveRoles()
			.addAll(el.getActiveRoles());
			policy	.getUserRoleAssignment()
			.add(newUr);
		}

		for (PermissionRoleAssignment el : state.prCopy) {
			policy	.getPermissionRoleAssignment()
			.add(new PermissionRoleAssignment(el.getPermission(), el.getRole()));
		}

	}

	public void reset() {
		reset(initialState);
	}

	public RbacState getCurrentState() {
		return currentState;
	}

	@Override
	public String getName() {
		String stateName = "";
		for (User usr: policy.getUser()) {
			for (Role rol: policy.getRole()) {
				if(utils.userRoleAssignmentExists(policy, usr, rol) &&
						utils.isRoleActiveByUser(policy, rol, usr)){
					stateName += "11";
				} else if(utils.userRoleAssignmentExists(policy, usr, rol)){
					stateName += "10";
				}else stateName += "00";

			}	
		}
		for (Role rol: policy.getRole()) {
			for (Permission prm: policy.getPermission()) {
				if(utils.permissionRoleAssignmentExists(policy, prm, rol)){
					stateName += "10";
				}else{
					stateName += "00";
				}

			}
		}
		return stateName;
	}

	public boolean request(RbacRequest rq) {
		String transition = getCurrentState().toString();
		boolean output = true;
		if(rq instanceof RbacRequestAssignUR){
			output = admin.assignUser(policy, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestDeassignUR){
			output = admin.deassignUser(policy, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestActivateUR){
			output = sys.addActiveRole(policy, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestDeactivateUR){
			output = sys.dropActiveRole(policy, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestAssignPR){
			output = admin.grantPermission(policy, rq.getPermission(), rq.getRole());
		}else if(rq instanceof RbacRequestDeassignPR){
			output = admin.revokePermission(policy, rq.getPermission(), rq.getRole());
		}

		currentState = null;
		currentState = new RbacState(policy);
		//transition +="--"+rq.toString()+"/"+(output ? "granted": "denied")+"-->"+getCurrentState().toString();
//		transition +=" -> "+getCurrentState().toString()+"  [ label=\""+rq.toString()+"/"+(output ? "granted": "denied")+"\"];";
		return output;
	}

	@Override
	public String toString() {
		return getName();
	}

}
