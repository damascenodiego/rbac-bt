package com.usp.icmc.labes.fsm.testing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.fsm.FsmModel;
import com.usp.icmc.labes.rbac.acut.RbacAcut;

public class RbacTestConfiguration {

	String name;
	FsmModel rbacSpecification;
	List<RbacAcut> rbacMutants;
	List<FsmTestSuite> testSuites;
	File path;
	public static enum ConfigurationType{
		TEST_EXECUTION,
		TEST_GENERATOR,
	}
	
	ConfigurationType testConfigurationType;

	public RbacTestConfiguration() {
		rbacMutants = new ArrayList<RbacAcut>();
		testSuites = new ArrayList<FsmTestSuite>();
		testConfigurationType = ConfigurationType.TEST_EXECUTION;
	}
	public void setTestConfigurationType(ConfigurationType testConfigurationType) {
		this.testConfigurationType = testConfigurationType;
	}
	public ConfigurationType getTestConfigurationType() {
		return testConfigurationType;
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
	public File getPath() {
		return path;
	}
	public void setPath(File path) {
		this.path = path;
	}
	public List<RbacAcut> getRbacMutants() {
		return rbacMutants;
	}
	public void setRbacMutants(List<RbacAcut> rbacMutants) {
		this.rbacMutants = rbacMutants;
	}
	public List<FsmTestSuite> getTestSuites() {
		return testSuites;
	}
	public void setTestSuites(List<FsmTestSuite> testSuites) {
		this.testSuites = testSuites;
	}
	
}
