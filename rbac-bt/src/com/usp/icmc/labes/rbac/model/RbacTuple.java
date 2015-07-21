package com.usp.icmc.labes.rbac.model;

import java.util.List;

public interface RbacTuple {
	List<ActivationHierarchy> getActivationHierarchy();
	List<Dr> getDr();
	List<DSoDConstraint> getDSoDConstraint();
	List<Du> getDu();
	List<InheritanceHierarchy> getInheritanceHierarchy();
	List<Permission> getPermission();
	List<PermissionRoleAssignment> getPermissionRoleAssignment();
	List<Role> getRole();
	List<Sr> getSr();
	List<SSoDConstraint> getSSoDConstraint();
	List<Su> getSu();
	List<User> getUser();
	List<UserRoleActivation> getUserRoleActivation();
	List<UserRoleAssignment> getUserRoleAssignment();
}
