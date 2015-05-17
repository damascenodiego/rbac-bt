package com.usp.icmc.labes.rbac.model;

import java.util.HashSet;
import java.util.Set;

public abstract class SoDConstraint implements RbacElement{
	String name;
	int cardinality;
	Set<Role> sodSet;
	
	public SoDConstraint() {
		sodSet = new HashSet<Role>();
	}
}
