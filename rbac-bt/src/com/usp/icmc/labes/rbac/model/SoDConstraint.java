package com.usp.icmc.labes.rbac.model;

import java.util.Set;

public interface SoDConstraint extends RbacElement, RbacMutableElement {
	
	int getCardinality();
	Set<Role> getSodSet();

}
