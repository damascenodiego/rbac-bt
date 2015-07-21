package com.usp.icmc.labes.rbac.model;

public interface Hierarchy extends RbacElement, RbacMutableElement {
	Role getJunior();
	Role getSenior();
}
