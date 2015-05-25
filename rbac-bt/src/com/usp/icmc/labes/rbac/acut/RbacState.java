package com.usp.icmc.labes.rbac.acut;

import java.util.ArrayList;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.RoleAssignable;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;
import com.usp.icmc.labes.utils.RbacUtils;

public class RbacState {


	RbacPolicy policy;

	protected ArrayList<UserRoleAssignment> urAsCopy;
	protected ArrayList<UserRoleActivation> urAcCopy;
	protected ArrayList<PermissionRoleAssignment> prAsCopy;

	RbacUtils utils = RbacUtils.getInstance();

	public RbacState(RbacPolicy p) {
		this.policy=p;
		urAsCopy = new ArrayList<UserRoleAssignment>();
		urAcCopy = new ArrayList<UserRoleActivation>();
		prAsCopy = new ArrayList<PermissionRoleAssignment>();

		for (UserRoleAssignment el : policy.getUserRoleAssignment()) 
			urAsCopy.add(new UserRoleAssignment(el.getUser(), el.getRole()));

		for (UserRoleActivation el : policy.getUserRoleActivation()) 
			urAcCopy.add(new UserRoleActivation(el.getUser(), el.getRole()));

		for (PermissionRoleAssignment el : policy.getPermissionRoleAssignment()) 
			prAsCopy.add(new PermissionRoleAssignment(el.getPermission(), el.getRole()));

	}

	public String getName() {
		String stateName = "";
		for (User usr: policy.getUser()) {
			for (Role rol: policy.getRole()) {
				stateName += getBinaryRepresentation(usr, rol);
			}	
		}
		for (Role rol: policy.getRole()) {
			for (Permission prm: policy.getPermission()) {
				stateName += getBinaryRepresentation(prm, rol);

			}
		}
		return stateName;
	}

	private String getBinaryRepresentation(RoleAssignable assigned, Role rol) {

		if (assigned instanceof User){
			for (UserRoleActivation el : urAcCopy)
				if(el.getUser().equals(assigned) && el.getRole().equals(rol)) 
					return "11";
			for (UserRoleAssignment el : urAsCopy)
				if(el.getUser().equals(assigned) && el.getRole().equals(rol)) 
					return "10";
		}else if (assigned instanceof Permission){
			for (PermissionRoleAssignment el : prAsCopy) 
				if(el.getPermission().equals(assigned) && el.getRole().equals(rol)) 
					return "10";
		}

		return "00";
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((prAsCopy == null) ? 0 : prAsCopy.hashCode());
		result = prime * result
				+ ((urAcCopy == null) ? 0 : urAcCopy.hashCode());
		result = prime * result
				+ ((urAsCopy == null) ? 0 : urAsCopy.hashCode());
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
		if (prAsCopy == null) {
			if (other.prAsCopy != null)
				return false;
		} else if (!prAsCopy.equals(other.prAsCopy))
			return false;
		if (urAcCopy == null) {
			if (other.urAcCopy != null)
				return false;
		} else if (!urAcCopy.equals(other.urAcCopy))
			return false;
		if (urAsCopy == null) {
			if (other.urAsCopy != null)
				return false;
		} else if (!urAsCopy.equals(other.urAsCopy))
			return false;
		return true;
	}

}