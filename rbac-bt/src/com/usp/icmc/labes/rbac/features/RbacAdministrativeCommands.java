package com.usp.icmc.labes.rbac.features;

import java.util.Set;

import com.usp.icmc.labes.rbac.model.*;

public class RbacAdministrativeCommands {
	
	RbacAdministrativeCommands instance;
	
	public RbacAdministrativeCommands getInstance() {
		if(instance==null){
			instance = new RbacAdministrativeCommands();
		}
		return instance;
	}
	
	private RbacAdministrativeCommands() {}

	// core rbac
	public void addUser(RbacPolicy policy, User user) throws RbacConstraintException{

	}
	public void deleteUser(RbacPolicy policy, User user) throws RbacConstraintException{

	}
	public void addRole(RbacPolicy policy, Role role) throws RbacConstraintException{

	}
	public void deleteRole(RbacPolicy policy, Role role) throws RbacConstraintException{

	}
	public void assignUser(RbacPolicy policy, User user, Role role) throws RbacConstraintException{

	}
	public void deassignUser(RbacPolicy policy, User user, Role role) throws RbacConstraintException{

	}
	public void grantPermission(RbacPolicy policy, Role role, Permission permission) throws RbacConstraintException{

	}
	public void revokePermission(RbacPolicy policy, Role role, Permission permission) throws RbacConstraintException{

	}
	// hierarchical rbac
	public void addInheritance(RbacPolicy policy, Role r_asc, Role r_desc) throws RbacConstraintException{

	}
	public void deleteInheritance(RbacPolicy policy, Role r_asc, Role r_desc) throws RbacConstraintException{

	}
	public void addAscendant(RbacPolicy policy, Role r_asc, Role r_desc) throws RbacConstraintException{

	}
	public void addDescendant(RbacPolicy policy, Role r_asc, Role r_desc) throws RbacConstraintException{

	}
	// static SoD
	public void createSsodSet(RbacPolicy policy, String name , Set<Role> set, int set_card) throws RbacConstraintException{

	}
	public void deleteSsodSet(RbacPolicy policy, SSoDConstraint ssod) throws RbacConstraintException{

	}
	public void addSsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role) throws RbacConstraintException{

	}
	public void setSsodSetCardinality(RbacPolicy policy, SSoDConstraint ssod, int set_card) throws RbacConstraintException{

	}
	public void deleteSsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role) throws RbacConstraintException{

	}
	// dynamic SoD
	public void createDsodSet(RbacPolicy policy, String name , Set<Role> set, int set_card) throws RbacConstraintException{

	}
	public void deleteDsodSet(RbacPolicy policy, SSoDConstraint ssod) throws RbacConstraintException{

	}
	public void addDsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role) throws RbacConstraintException{

	}
	public void setDsodSetCardinality(RbacPolicy policy, SSoDConstraint ssod, int set_card) throws RbacConstraintException{

	}
	public void deleteDsodRoleMember(RbacPolicy policy, SSoDConstraint ssod, Role role) throws RbacConstraintException{

	}
}
