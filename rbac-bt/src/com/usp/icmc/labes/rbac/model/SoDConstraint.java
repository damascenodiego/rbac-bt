package com.usp.icmc.labes.rbac.model;

import java.util.Set;

public interface SoDConstraint {
	
	int getCardinality();
	Set<Role> getSodSet();

}
