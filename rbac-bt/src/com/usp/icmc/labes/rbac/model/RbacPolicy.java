package com.usp.icmc.labes.rbac.model;

import java.util.ArrayList;
import java.util.List;

public class RbacPolicy {
	List<User> user;
	List<Role> role;
	List<Permission> permission;
	List<Session> session;
	List<SoDConstraint> sodConstraint;
	
	public RbacPolicy() {
		user = new ArrayList<User>();
		role = new ArrayList<Role>();
		permission = new ArrayList<Permission>();
		session = new ArrayList<Session>();
		sodConstraint = new ArrayList<SoDConstraint>();
	}
}
