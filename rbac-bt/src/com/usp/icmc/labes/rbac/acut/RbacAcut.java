package com.usp.icmc.labes.rbac.acut;

import java.util.List;

import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacAdvancedReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.RbacTuple;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.utils.RbacUtils;

public class RbacAcut implements RbacTuple{

	private RbacPolicy policy;

	private RbacState initialState;
	private RbacState currentState;
	
	private RbacResponse response;

	private RbacUtils utils = RbacUtils.getInstance();

	private RbacAdministrativeCommands admin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions sys = RbacSupportingSystemFunctions.getInstance();
	private RbacReviewFunctions rev = RbacReviewFunctions.getInstance();
	private RbacAdvancedReviewFunctions adv = RbacAdvancedReviewFunctions.getInstance();

	public RbacAcut(RbacPolicy p) { 
		policy = p;
//		saveState(p);
		initialState = new RbacState(p);
		currentState = new RbacState(p);
		response = new RbacResponse();
	}

//	private void saveState(RbacPolicy p) {
//		currentState = new RbacState(p);
//	}

	public void reset(RbacState state) {
		getUserRoleAssignment()		.clear();
		getUserRoleActivation()		.clear();
		getPermissionRoleAssignment().clear();

		for (UserRoleAssignment el : state.urAsCopy)
			getUserRoleAssignment()
					.add(new UserRoleAssignment(el.getUser(), el.getRole()));
		for (UserRoleActivation el : state.urAcCopy)
			getUserRoleActivation()
					.add(new UserRoleActivation(el.getUser(), el.getRole()));
		for (PermissionRoleAssignment el : state.prAsCopy) 
			getPermissionRoleAssignment()
					.add(new PermissionRoleAssignment(el.getPermission(), el.getRole()));
//		saveState(this);
	}

	public void reset() {
		reset(initialState);
	}

	public RbacState getCurrentState() {
		return currentState;
	}

	public String getName() {
		return currentState.getName();
	}

	public boolean request(RbacRequest rq) {
		boolean output = false;
		if(rq instanceof RbacRequestAssignUR){
			output = admin.assignUser(this, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestDeassignUR){
			output = admin.deassignUser(this, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestActivateUR){
			output = sys.addActiveRole(this, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestDeactivateUR){
			output = sys.dropActiveRole(this, rq.getUser(), rq.getRole());
		}else if(rq instanceof RbacRequestAssignPR){
			output = admin.grantPermission(this, rq.getPermission(), rq.getRole());
		}else if(rq instanceof RbacRequestDeassignPR){
			output = admin.revokePermission(this, rq.getPermission(), rq.getRole());
		}

//		saveState(policy);
		//transition +="--"+rq.toString()+"/"+(output ? "granted": "denied")+"-->"+getCurrentState().toString();
		//		transition +=" -> "+getCurrentState().toString()+"  [ label=\""+rq.toString()+"/"+(output ? "granted": "denied")+"\"];";
		return output;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public List<UserRoleAssignment> getUserRoleAssignment() {
		return getCurrentState().urAsCopy;
	}

	@Override
	public List<UserRoleActivation> getUserRoleActivation() {
		return getCurrentState().urAcCopy;
	}

	@Override
	public List<PermissionRoleAssignment> getPermissionRoleAssignment() {
		return getCurrentState().prAsCopy;
	}

	@Override
	public List<User> getUser() {
		return policy.getUser();
	}

	@Override
	public List<Role> getRole() {
		return policy.getRole();
	}

	@Override
	public List<Permission> getPermission() {
		return policy.getPermission();
	}

	@Override
	public List<Su> getSu() {
		return policy.getSu();
	}

	@Override
	public List<Sr> getSr() {
		return policy.getSr();
	}

	@Override
	public List<Du> getDu() {
		return policy.getDu();
	}

	@Override
	public List<Dr> getDr() {
		return policy.getDr();
	}

	@Override
	public List<SSoDConstraint> getSsodConstraint() {
		return policy.getSsodConstraint();
	}

	@Override
	public List<DSoDConstraint> getDsodConstraint() {
		return policy.getDsodConstraint();
	}

	public void setResponse(RbacResponse response) {
		this.response = response;
	}
	
	public RbacResponse getResponse() {
		return response;
	}
}
