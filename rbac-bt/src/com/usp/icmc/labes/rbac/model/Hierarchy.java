package com.usp.icmc.labes.rbac.model;

public interface Hierarchy extends RbacElement, RbacMutableElement {
	Role getSenior();
	Role getJunior();
}
