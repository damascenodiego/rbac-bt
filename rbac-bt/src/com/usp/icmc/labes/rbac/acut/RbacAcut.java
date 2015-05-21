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
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
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
		initialState = new RbacState(p);
	}

	private void saveState(RbacPolicy p) {
		currentState = new RbacState(p);
	}

	public void reset(RbacState state) {
		policy.getUserRoleAssignment()		.clear();
		policy.getUserRoleActivation()		.clear();
		policy.getPermissionRoleAssignment().clear();

		for (UserRoleAssignment el : state.urAsCopy)
			policy.getUserRoleAssignment()
					.add(new UserRoleAssignment(el.getUser(), el.getRole()));
		for (UserRoleActivation el : state.urAcCopy)
			policy.getUserRoleActivation()
					.add(new UserRoleActivation(el.getUser(), el.getRole()));
		for (PermissionRoleAssignment el : state.prAsCopy) 
			policy.getPermissionRoleAssignment()
					.add(new PermissionRoleAssignment(el.getPermission(), el.getRole()));
		saveState(policy);
	}

	public void reset() {
		reset(initialState);
	}

	public RbacState getCurrentState() {
		return currentState;
	}

	@Override
	public String getName() {
		return currentState.getName();
	}

	public boolean request(RbacRequest rq) {
		String transition = getCurrentState().toString();
		boolean output = false;
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

		saveState(policy);
		//transition +="--"+rq.toString()+"/"+(output ? "granted": "denied")+"-->"+getCurrentState().toString();
		//		transition +=" -> "+getCurrentState().toString()+"  [ label=\""+rq.toString()+"/"+(output ? "granted": "denied")+"\"];";
		return output;
	}

	@Override
	public String toString() {
		return getName();
	}

}
