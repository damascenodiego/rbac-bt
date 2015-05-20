package com.usp.icmc.labes.rbac.model.acut;

import com.usp.icmc.labes.rbac.model.RbacElement;

public class RbacState implements RbacElement {
	
	public static final String USER_EXISTS  = "USER_EXISTS";
	public static final String ROLE_EXISTS  = "ROLE_EXISTS";
	public static final String PRMS_EXISTS  = "PRMS_EXISTS";
	public static final String UR_ASSIGNED  = "UR_ASSIGNED";
	public static final String UR_ACTIVATED = "UR_ACTIVATED";
	public static final String PR_ASSIGNED  = "PR_ASSIGNED";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
