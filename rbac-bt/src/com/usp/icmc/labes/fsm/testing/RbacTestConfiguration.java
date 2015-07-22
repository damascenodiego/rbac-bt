package com.usp.icmc.labes.fsm.testing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.rbac.acut.RbacAcut;

public class RbacTestConfiguration {

	String name;
	FsmModel rbacSpecification;
	List<RbacAcut> rbacMutant;
	File path;
	FsmTestSuite testSuite;

	public RbacTestConfiguration() {
		rbacMutant = new ArrayList<RbacAcut>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FsmModel getRbacSpecification() {
		return rbacSpecification;
	}
	public void setRbacSpecification(FsmModel rbacSpecification) {
		this.rbacSpecification = rbacSpecification;
	}
	public List<RbacAcut> getRbacMutant() {
		return rbacMutant;
	}
	public void setRbacMutant(List<RbacAcut> rbacMutant) {
		this.rbacMutant = rbacMutant;
	}
	public File getPath() {
		return path;
	}
	public void setPath(File path) {
		this.path = path;
	}
	public FsmTestSuite getTestSuite() {
		return testSuite;
	}
	public void setTestSuite(FsmTestSuite testSuite) {
		this.testSuite = testSuite;
	}
	
}
