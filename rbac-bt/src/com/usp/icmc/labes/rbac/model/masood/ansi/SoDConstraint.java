package com.usp.icmc.labes.rbac.model.masood.ansi;

import java.util.Set;

public interface SoDConstraint {
	
	int getCardinality();
	Set<Role> getSodSet();

}
