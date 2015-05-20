package com.usp.icmc.labes.rbac.acut;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacElement;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.RoleAssignable;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.rbac.utils.RbacUtils;

public class RbacState implements RbacElement {


	RbacPolicy policy;

	protected ArrayList<UserRoleAssignment> urCopy;
	protected ArrayList<PermissionRoleAssignment> prCopy;

	RbacUtils utils = RbacUtils.getInstance();

	public RbacState(RbacPolicy p) {
		this.policy=p;
		urCopy = new ArrayList<UserRoleAssignment>();
		prCopy = new ArrayList<PermissionRoleAssignment>();

		for (UserRoleAssignment el : policy.getUserRoleAssignment()) {
			UserRoleAssignment tmp = new UserRoleAssignment(el.getUser(), el.getRole());
			tmp.getActiveRoles().addAll(el.getActiveRoles());
			urCopy.add(tmp);
		}

		for (PermissionRoleAssignment el : policy.getPermissionRoleAssignment()) {
			PermissionRoleAssignment tmp = new PermissionRoleAssignment(el.getPermission(), el.getRole());
			prCopy.add(tmp);
		}

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
				}

			}
		}
		return stateName;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((policy == null) ? 0 : policy.hashCode());
		result = prime * result + ((prCopy == null) ? 0 : prCopy.hashCode());
		result = prime * result + ((urCopy == null) ? 0 : urCopy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RbacState))
			return false;
		RbacState other = (RbacState) obj;
		if (policy == null) {
			if (other.policy != null)
				return false;
		} else if (!policy.equals(other.policy))
			return false;
		if (prCopy == null) {
			if (other.prCopy != null)
				return false;
		} else if (!prCopy.equals(other.prCopy))
			return false;
		if (urCopy == null) {
			if (other.urCopy != null)
				return false;
		} else if (!urCopy.equals(other.urCopy))
			return false;
		return true;
	}
	
	

}