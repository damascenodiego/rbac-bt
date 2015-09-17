package com.usp.icmc.labes.rbac.acut;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Properties;

import com.usp.icmc.labes.fsm.FsmState;
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

	private FsmState initialState;
	private FsmState currentState;

	private List<UserRoleAssignment> userRoleAssignment;
	private List<UserRoleActivation> userRoleActivation;
	private List<PermissionRoleAssignment> permissionRoleAssignment;
	
	private RbacUtils utils = RbacUtils.getInstance();

	private RbacAdministrativeCommands admin = RbacAdministrativeCommands.getInstance();
	private RbacSupportingSystemFunctions sys = RbacSupportingSystemFunctions.getInstance();
	private RbacReviewFunctions rev = RbacReviewFunctions.getInstance();
	private RbacAdvancedReviewFunctions adv = RbacAdvancedReviewFunctions.getInstance();

	private Properties properties;
	
	public RbacAcut(RbacAcut acut2) {
		this(acut2.getPolicy());
	}

	public RbacAcut(RbacPolicy p) { 
		policy = p;
		initialState = RbacUtils.getInstance().rbacToFsmState(p);
		currentState = RbacUtils.getInstance().rbacToFsmState(p);
		userRoleAssignment = new ArrayList<UserRoleAssignment>();
		userRoleActivation= new ArrayList<UserRoleActivation>();
		permissionRoleAssignment = new ArrayList<PermissionRoleAssignment>();
		properties = new Properties();
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

	@Override
	public List<Permission> getPermission() {
		return policy.getPermission();
	}

	@Override
	public List<PermissionRoleAssignment> getPermissionRoleAssignment() {
		return permissionRoleAssignment;
	}

	public RbacPolicy getPolicy() {
		return policy;
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
		return userRoleActivation;
	}

	@Override
	public List<UserRoleAssignment> getUserRoleAssignment() {
		return userRoleAssignment;
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
		updateCurrentState();

		return output;
	}

	private void updateCurrentState() {

		int totUser = policy.getUser().size();
		int totRole = policy.getRole().size();
		int totPrms = policy.getPermission().size();
		
		BitSet state = (BitSet) currentState.getProperties().get(BitSet.class);
		
		for (int ui = 0; ui < totUser; ui++) {
			User usr = policy.getUser().get(ui);
			for (int ri = 0; ri < totRole; ri++) {
				Role rol = policy.getRole().get(ri);
				int index = ui*2*totRole+2*ri;
				if(RbacUtils.getInstance().userRoleAssignmentExists(policy, usr, rol)) state.set(index);
				if(RbacUtils.getInstance().userRoleActivationExists(policy, usr, rol)) state.set(index+1);
			}
		}
		for (int ri = 0; ri < totRole; ri++) {
			Role rol = policy.getRole().get(ri);
			for (int pi = 0; pi < totPrms; pi++) {
				Permission pr = policy.getPermission().get(pi);
				int index = 2*totUser*totRole+2*pi;
				if(RbacUtils.getInstance().permissionRoleAssignmentExists(policy, pr, rol)) state.set(index);
			}	
		}

		
	}

	public void reset() {
		reset(initialState);
	}

	public void reset(FsmState state) {
		getUserRoleAssignment()		.clear();
		getUserRoleActivation()		.clear();
		getPermissionRoleAssignment().clear();

		BitSet bits = (BitSet) state.getProperties().get(BitSet.class);
		
		int totUser = policy.getUser().size();
		int totRole = policy.getRole().size();
		int totPrms = policy.getPermission().size();
		
		for (int ui = 0; ui < totUser; ui++) {
			User usr = policy.getUser().get(ui);
			for (int ri = 0; ri < totRole; ri++) {
				Role rol = policy.getRole().get(ri);
				int index = ui*2*totRole+2*ri;
				if(bits.get(index))   getUserRoleAssignment().add(new UserRoleAssignment(usr, rol));
				if(bits.get(index+1)) getUserRoleActivation().add(new UserRoleActivation(usr, rol));
			}
		}
		for (int ri = 0; ri < totRole; ri++) {
			Role rol = policy.getRole().get(ri);
			for (int pi = 0; pi < totPrms; pi++) {
				Permission pr = policy.getPermission().get(pi);
				int index = 2*totUser*totRole+2*pi;
				if(bits.get(index))   getPermissionRoleAssignment().add(new PermissionRoleAssignment(pr, rol));
			}	
		}
	}

	public void setPolicy(RbacPolicy policy) {
		this.policy = policy;
	}
		
	public Properties getProperties() {
		return properties;
	}
	
}
