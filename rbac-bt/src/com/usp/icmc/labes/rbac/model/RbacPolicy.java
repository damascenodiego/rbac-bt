package com.usp.icmc.labes.rbac.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class RbacPolicy implements RbacElement{
	
	@XStreamAsAttribute
	private String name;
	@XStreamAlias("Users")
	private List<User> user;
	@XStreamAlias("Roles")
	private List<Role> role;
	@XStreamAlias("Permissions")
	private List<Permission> permission;
	@XStreamAlias("URAssignments")
	private List<UserRoleAssignment> userRoleAssignment;
	@XStreamAlias("URActivations")
	private List<UserRoleActivation> userRoleActivation;
	@XStreamAlias("PRAssignments")
	private List<PermissionRoleAssignment> permissionRoleAssignment;
	@XStreamAlias("SoDConstraints")
	private List<SoDConstraint> sodConstraint;
	@XStreamAlias("HierarchyRelations")
	private List<Hierarchy> hierarchy;
	
	
	public RbacPolicy() {
		user = new ArrayList<User>();
		role = new ArrayList<Role>();
		permission = new ArrayList<Permission>();
		userRoleAssignment = new ArrayList<UserRoleAssignment>();
		userRoleActivation= new ArrayList<UserRoleActivation>();
		permissionRoleAssignment = new ArrayList<PermissionRoleAssignment>();
		sodConstraint = new ArrayList<SoDConstraint>();
		hierarchy = new ArrayList<Hierarchy>();
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

	public List<UserRoleActivation> getUserRoleActivation() {
		return userRoleActivation;
	}

	public void setUserRoleActivation(List<UserRoleActivation> userRoleActivation) {
		this.userRoleActivation = userRoleActivation;
	}

	public List<PermissionRoleAssignment> getPermissionRoleAssignment() {
		return permissionRoleAssignment;
	}

	public void setPermissionRoleAssignment(
			List<PermissionRoleAssignment> permissionRoleAssignment) {
		this.permissionRoleAssignment = permissionRoleAssignment;
	}

	public List<SoDConstraint> getSodConstraint() {
		return sodConstraint;
	}

	public void setSodConstraint(List<SoDConstraint> sodConstraint) {
		this.sodConstraint = sodConstraint;
	}

	public List<Hierarchy> getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(List<Hierarchy> hierarchy) {
		this.hierarchy = hierarchy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hierarchy == null) ? 0 : hierarchy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime
				* result
				+ ((permissionRoleAssignment == null) ? 0
						: permissionRoleAssignment.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result
				+ ((sodConstraint == null) ? 0 : sodConstraint.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime
				* result
				+ ((userRoleActivation == null) ? 0 : userRoleActivation
						.hashCode());
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
		if (hierarchy == null) {
			if (other.hierarchy != null)
				return false;
		} else if (!hierarchy.equals(other.hierarchy))
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
		if (sodConstraint == null) {
			if (other.sodConstraint != null)
				return false;
		} else if (!sodConstraint.equals(other.sodConstraint))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (userRoleActivation == null) {
			if (other.userRoleActivation != null)
				return false;
		} else if (!userRoleActivation.equals(other.userRoleActivation))
			return false;
		if (userRoleAssignment == null) {
			if (other.userRoleAssignment != null)
				return false;
		} else if (!userRoleAssignment.equals(other.userRoleAssignment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RbacPolicy [name=" + name + ", user=" + user + ", role=" + role
				+ ", permission=" + permission + ", userRoleAssignment="
				+ userRoleAssignment + ", userRoleActivation="
				+ userRoleActivation + ", permissionRoleAssignment="
				+ permissionRoleAssignment + ", sodConstraint=" + sodConstraint
				+ ", hierarchy=" + hierarchy + "]";
	}

	
}
