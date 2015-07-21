package com.usp.icmc.labes.rbac.acut;

import java.util.List;

import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacAdvancedReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.rbac.model.ActivationHierarchy;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.InheritanceHierarchy;
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

	public RbacAcut(RbacAcut acut2) {
		this(acut2.getPolicy());
	}

	//	private void saveState(RbacPolicy p) {
	//		currentState = new RbacState(p);
	//	}

	public RbacAcut(RbacPolicy p) { 
		policy = p;
		//		saveState(p);
		initialState = new RbacState(p);
		currentState = new RbacState(p);
		response = new RbacResponse();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RbacTuple))
			return false;
		RbacTuple other = (RbacTuple) obj;
		if (getActivationHierarchy() == null) {
			if (other.getActivationHierarchy() != null)
				return false;
		} else if (!getActivationHierarchy().equals(other.getActivationHierarchy()))
			return false;
		if (getDr()== null) {
			if (other.getDr()!= null)
				return false;
		} else if (!getDr().equals(other.getDr()))
			return false;
		if (getDSoDConstraint() == null) {
			if (other.getDSoDConstraint() != null)
				return false;
		} else if (!getDSoDConstraint().equals(other.getDSoDConstraint()))
			return false;
		if (getDu() == null) {
			if (other.getDu() != null)
				return false;
		} else if (!getDu().equals(other.getDu()))
			return false;
		if (getInheritanceHierarchy() == null) {
			if (other.getInheritanceHierarchy() != null)
				return false;
		} else if (!getInheritanceHierarchy().equals(other.getInheritanceHierarchy()))
			return false;
		if (getPermission() == null) {
			if (other.getPermission() != null)
				return false;
		} else if (!getPermission().equals(other.getPermission()))
			return false;
		if (getPermissionRoleAssignment() == null) {
			if (other.getPermissionRoleAssignment() != null)
				return false;
		} else if (!getPermissionRoleAssignment()
				.equals(other.getPermissionRoleAssignment()))
			return false;
		if (getRole() == null) {
			if (other.getRole() != null)
				return false;
		} else if (!getRole().equals(other.getRole()))
			return false;
		if (getSr() == null) {
			if (other.getSr() != null)
				return false;
		} else if (!getSr().equals(other.getSr()))
			return false;
		if (getSSoDConstraint() == null) {
			if (other.getSSoDConstraint() != null)
				return false;
		} else if (!getSSoDConstraint().equals(other.getSSoDConstraint()))
			return false;
		if (getSu() == null) {
			if (other.getSu() != null)
				return false;
		} else if (!getSu().equals(other.getSu()))
			return false;
		if (getUser() == null) {
			if (other.getUser() != null)
				return false;
		} else if (!getUser().equals(other.getUser()))
			return false;
		if (getUserRoleActivation() == null) {
			if (other.getUserRoleActivation() != null)
				return false;
		} else if (!getUserRoleActivation().equals(other.getUserRoleActivation()))
			return false;
		if (getUserRoleAssignment() == null) {
			if (other.getUserRoleAssignment() != null)
				return false;
		} else if (!getUserRoleAssignment().equals(other.getUserRoleAssignment()))
			return false;
		return true;
	}

	@Override
	public List<ActivationHierarchy> getActivationHierarchy() {
		return policy.getActivationHierarchy();
	}

	public RbacState getCurrentState() {
		return currentState;
	}

	@Override
	public List<Dr> getDr() {
		return policy.getDr();
	}

	@Override
	public List<DSoDConstraint> getDSoDConstraint() {
		return policy.getDSoDConstraint();
	}

	@Override
	public List<Du> getDu() {
		return policy.getDu();
	}

	@Override
	public List<InheritanceHierarchy> getInheritanceHierarchy() {
		return policy.getInheritanceHierarchy();
	}

	public String getName() {
		return currentState.getName();
	}

	@Override
	public List<Permission> getPermission() {
		return policy.getPermission();
	}

	@Override
	public List<PermissionRoleAssignment> getPermissionRoleAssignment() {
		return getCurrentState().prAsCopy;
	}

	public RbacPolicy getPolicy() {
		return policy;
	}

	public RbacResponse getResponse() {
		return response;
	}

	@Override
	public List<Role> getRole() {
		return policy.getRole();
	}

	@Override
	public List<Sr> getSr() {
		return policy.getSr();
	}

	@Override
	public List<SSoDConstraint> getSSoDConstraint() {
		return policy.getSSoDConstraint();
	}

	@Override
	public List<Su> getSu() {
		return policy.getSu();
	}

	@Override
	public List<User> getUser() {
		return policy.getUser();
	}

	@Override
	public List<UserRoleActivation> getUserRoleActivation() {
		return getCurrentState().urAcCopy;
	}

	@Override
	public List<UserRoleAssignment> getUserRoleAssignment() {
		return getCurrentState().urAsCopy;
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

	public void reset() {
		reset(initialState);
	}

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

	public void setPolicy(RbacPolicy policy) {
		this.policy = policy;
	}
	
	public void setResponse(RbacResponse response) {
		this.response = response;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
