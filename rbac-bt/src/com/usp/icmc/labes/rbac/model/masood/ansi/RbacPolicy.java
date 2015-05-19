package com.usp.icmc.labes.rbac.model.masood.ansi;

import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.rbac.model.RbacElement;

public class RbacPolicy implements RbacElement{
	
	private String name;
	private List<User> user;
	private List<Role> role;
	private List<Permission> permission;
	private List<UserRoleAssignment> userRoleAssignment;
	private List<PermissionRoleAssignment> permissionRoleAssignment;
	private List<SSoDConstraint> ssodConstraint;
	private List<DSoDConstraint> dsodConstraint;
	private List<ActivationHierarchy> activationHierarchy;
	private List<InheritanceHierarchy> inheritanceHierarchy;
	
	
	public RbacPolicy() {
		user = new ArrayList<User>();
		role = new ArrayList<Role>();
		permission = new ArrayList<Permission>();
		userRoleAssignment = new ArrayList<UserRoleAssignment>();
		permissionRoleAssignment = new ArrayList<PermissionRoleAssignment>();
		ssodConstraint = new ArrayList<SSoDConstraint>();
		dsodConstraint = new ArrayList<DSoDConstraint>();
		activationHierarchy = new ArrayList<ActivationHierarchy>();
		inheritanceHierarchy = new ArrayList<InheritanceHierarchy>();
	}

	public RbacPolicy(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}

	public List<Permission> getPermission() {
		return permission;
	}

	public void setPermission(List<Permission> permission) {
		this.permission = permission;
	}

	public List<UserRoleAssignment> getUserRoleAssignment() {
		return userRoleAssignment;
	}

	public void setUserRoleAssignment(List<UserRoleAssignment> userRoleAssignment) {
		this.userRoleAssignment = userRoleAssignment;
	}

	public List<PermissionRoleAssignment> getPermissionRoleAssignment() {
		return permissionRoleAssignment;
	}

	public void setPermissionRoleAssignment(
			List<PermissionRoleAssignment> permissionRoleAssignment) {
		this.permissionRoleAssignment = permissionRoleAssignment;
	}

	public List<SSoDConstraint> getSsodConstraint() {
		return ssodConstraint;
	}

	public void setSsodConstraint(List<SSoDConstraint> ssodConstraint) {
		this.ssodConstraint = ssodConstraint;
	}

	public List<DSoDConstraint> getDsodConstraint() {
		return dsodConstraint;
	}

	public void setDsodConstraint(List<DSoDConstraint> dsodConstraint) {
		this.dsodConstraint = dsodConstraint;
	}

	public List<ActivationHierarchy> getActivationHierarchy() {
		return activationHierarchy;
	}

	public void setActivationHierarchy(List<ActivationHierarchy> activationHierarchy) {
		this.activationHierarchy = activationHierarchy;
	}

	public List<InheritanceHierarchy> getInheritanceHierarchy() {
		return inheritanceHierarchy;
	}

	public void setInheritanceHierarchy(
			List<InheritanceHierarchy> inheritanceHierarchy) {
		this.inheritanceHierarchy = inheritanceHierarchy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activationHierarchy == null) ? 0 : activationHierarchy
						.hashCode());
		result = prime * result
				+ ((dsodConstraint == null) ? 0 : dsodConstraint.hashCode());
		result = prime
				* result
				+ ((inheritanceHierarchy == null) ? 0 : inheritanceHierarchy
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime
				* result
				+ ((permissionRoleAssignment == null) ? 0
						: permissionRoleAssignment.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result
				+ ((ssodConstraint == null) ? 0 : ssodConstraint.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime
				* result
				+ ((userRoleAssignment == null) ? 0 : userRoleAssignment
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RbacPolicy))
			return false;
		RbacPolicy other = (RbacPolicy) obj;
		if (activationHierarchy == null) {
			if (other.activationHierarchy != null)
				return false;
		} else if (!activationHierarchy.equals(other.activationHierarchy))
			return false;
		if (dsodConstraint == null) {
			if (other.dsodConstraint != null)
				return false;
		} else if (!dsodConstraint.equals(other.dsodConstraint))
			return false;
		if (inheritanceHierarchy == null) {
			if (other.inheritanceHierarchy != null)
				return false;
		} else if (!inheritanceHierarchy.equals(other.inheritanceHierarchy))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (permissionRoleAssignment == null) {
			if (other.permissionRoleAssignment != null)
				return false;
		} else if (!permissionRoleAssignment
				.equals(other.permissionRoleAssignment))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (ssodConstraint == null) {
			if (other.ssodConstraint != null)
				return false;
		} else if (!ssodConstraint.equals(other.ssodConstraint))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (userRoleAssignment == null) {
			if (other.userRoleAssignment != null)
				return false;
		} else if (!userRoleAssignment.equals(other.userRoleAssignment))
			return false;
		return true;
	}

	

	
}
