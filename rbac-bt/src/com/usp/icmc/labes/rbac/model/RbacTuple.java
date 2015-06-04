package com.usp.icmc.labes.rbac.model;

import java.util.List;

public interface RbacTuple {
	List<UserRoleAssignment> getUserRoleAssignment();
	List<UserRoleActivation> getUserRoleActivation();
	List<PermissionRoleAssignment> getPermissionRoleAssignment();
	List<User> getUser();
	List<Role> getRole();
	List<Permission> getPermission();
	List<Su> getSu();
	List<Sr> getSr();
	List<Du> getDu();
	List<Dr> getDr();
	List<SSoDConstraint> getSsodConstraint();
	List<DSoDConstraint> getDsodConstraint();
	List<ActivationHierarchy> getActivationHierarchy();
	List<InheritanceHierarchy> getInheritanceHierarchy();
}
