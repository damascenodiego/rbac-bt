package com.usp.icmc.labes.rbac.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RbacPolicy implements RbacTuple{
	
	private String name;
	private List<User> user;
	private List<Role> role;
	private List<Permission> permission;
	private List<Su> su;
	private List<Du> du;
	private List<Sr> sr;
	private List<Dr> dr;
	private List<UserRoleAssignment> userRoleAssignment;
	private List<UserRoleActivation> userRoleActivation;
	private List<PermissionRoleAssignment> permissionRoleAssignment;
	private List<SSoDConstraint> ssodConstraint;
	private List<DSoDConstraint> dsodConstraint;
	private List<ActivationHierarchy> activationHierarchy;
	private List<InheritanceHierarchy> inheritanceHierarchy;
	
	private Properties properties;
	
	
	public RbacPolicy() {
		user = new ArrayList<User>();
		role = new ArrayList<Role>();
		permission = new ArrayList<Permission>();
		userRoleAssignment = new ArrayList<UserRoleAssignment>();
		userRoleActivation= new ArrayList<UserRoleActivation>();
		permissionRoleAssignment = new ArrayList<PermissionRoleAssignment>();
		su = new ArrayList<Su>();
		sr = new ArrayList<Sr>();
		du = new ArrayList<Du>();
		dr = new ArrayList<Dr>();
		activationHierarchy = new ArrayList<ActivationHierarchy>();
		inheritanceHierarchy = new ArrayList<InheritanceHierarchy>();
		ssodConstraint = new ArrayList<SSoDConstraint>();
		dsodConstraint = new ArrayList<DSoDConstraint>();
		
		properties = new Properties();
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

	public List<Su> getSu() {
		return su;
	}

	public void setSu(List<Su> su) {
		this.su = su;
	}

	public List<Du> getDu() {
		return du;
	}

	public void setDu(List<Du> du) {
		this.du = du;
	}

	public List<Sr> getSr() {
		return sr;
	}

	public void setSr(List<Sr> sr) {
		this.sr = sr;
	}

	public List<Dr> getDr() {
		return dr;
	}

	public void setDr(List<Dr> dr) {
		this.dr = dr;
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

	public List<SSoDConstraint> getSSoDConstraint() {
		return ssodConstraint;
	}

	public void setSSoDConstraint(List<SSoDConstraint> ssodConstraint) {
		this.ssodConstraint = ssodConstraint;
	}

	public List<DSoDConstraint> getDSoDConstraint() {
		return dsodConstraint;
	}

	public void setDSoDConstraint(List<DSoDConstraint> dsodConstraint) {
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
		result = prime * result + ((dr == null) ? 0 : dr.hashCode());
		result = prime * result
				+ ((dsodConstraint == null) ? 0 : dsodConstraint.hashCode());
		result = prime * result + ((du == null) ? 0 : du.hashCode());
		result = prime
				* result
				+ ((inheritanceHierarchy == null) ? 0 : inheritanceHierarchy
						.hashCode());
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime
				* result
				+ ((permissionRoleAssignment == null) ? 0
						: permissionRoleAssignment.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((sr == null) ? 0 : sr.hashCode());
		result = prime * result
				+ ((ssodConstraint == null) ? 0 : ssodConstraint.hashCode());
		result = prime * result + ((su == null) ? 0 : su.hashCode());
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
	public String toString() {
		return "RbacPolicy [name=" + name + ", user=" + user + ", role=" + role
				+ ", permission=" + permission + ", su=" + su + ", du=" + du
				+ ", sr=" + sr + ", dr=" + dr + ", userRoleAssignment="
				+ userRoleAssignment + ", userRoleActivation="
				+ userRoleActivation + ", permissionRoleAssignment="
				+ permissionRoleAssignment + ", ssodConstraint="
				+ ssodConstraint + ", dsodConstraint=" + dsodConstraint
				+ ", activationHierarchy=" + activationHierarchy
				+ ", inheritanceHierarchy=" + inheritanceHierarchy + "]";
	}

	public Properties getProperties() {
		return properties;
	}
}
