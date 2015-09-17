package com.usp.icmc.labes.fsm.testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.usp.icmc.labes.fsm.FsmState;
import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.utils.RbacUtils;
import com.usp.icmc.labes.utils.RbacUtils.RbacFaultType;

public class FsmTestSuiteIterator{

	private RbacPolicy sutRbac;
	private RbacAcut acut;
	private List<RbacRequest> rqs;
	private File testSuiteFile;
	private String name;
	private String generatedBy;
	private BufferedReader br;


	public FsmTestSuiteIterator(RbacPolicy pol, File tsuiteFile) {
		this.sutRbac = pol;
		this.acut = new RbacAcut(sutRbac);
		this.rqs = RbacUtils.getInstance().generateRequests(acut);
		this.rqs.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
		this.testSuiteFile = tsuiteFile;
		setName(tsuiteFile.getName());
		setGeneratedBy(tsuiteFile.getName());

	}

	public void openFile() throws IOException{
		if(this.br!=null) this.br.close();
		this.br = null;
		this.br = new BufferedReader(new FileReader(this.testSuiteFile));

	}

	public boolean hasNextTestCase() throws IOException{
		return this.br.ready();
	}
	
	public void reset() throws IOException{
		openFile();
	}
	
	public void close() throws IOException{
		this.br.close();
	}
	

	public FsmTestCase nextTestCase() throws IOException{
		FsmTestCase tc = new FsmTestCase();
		if(hasNextTestCase()){
			String line = br.readLine();
			while(line.isEmpty()) {
				line = br.readLine();
				if(!br.ready()) return tc;
			}
			for (int i = 0; i <= line.length()-3; i+=3) {
				String inStr = line.substring(i, i+3);
				int inInt = Integer.valueOf(inStr);
				FsmTransition transition = new FsmTransition();
				transition.setInput(rqs.get(inInt).toString());
				tc.getPath().add(transition );
			}
			Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();
			Map<Integer, FsmState> stateMap = new HashMap<Integer, FsmState>();

			for (FsmTransition tr: tc.getPath()) {
				rqMap.putIfAbsent(tr.getInput(), RbacUtils.getInstance().createRbacRequest(tr.getInput(),acut));

				FsmState state = RbacUtils.getInstance().rbacToFsmState(acut);
				stateMap.putIfAbsent(state.getId(),state);
				tr.setFrom(stateMap.get(state.getId()));
				boolean outBool = acut.request(rqMap.get(tr.getInput()));
				tc.getProperties().put(tr.getInput(),rqs.indexOf(rqMap.get(tr.getInput())));

				state = RbacUtils.getInstance().rbacToFsmState(acut);
				stateMap.putIfAbsent(state.getId(),state);
				tr.setTo(stateMap.get(state.getId()));
				tr.setOutput((outBool?"grant":"deny"));

				for (RbacFaultType faultType: RbacFaultType.values()) {
					tr.getProperties().putIfAbsent(faultType, new HashSet<>());
					if(!acut.getProperties().containsKey(faultType)) continue;
					((Set) tr.getProperties().get(faultType)).addAll((Set) acut.getProperties().get(faultType));
				}
			}
			acut.reset();
		}

		return tc;
	}

	public String getGeneratedBy() {
		return generatedBy;
	}

	public String getName() {
		return name;
	}

	public List<RbacRequest> getRqs() {
		return rqs;
	}

	public RbacPolicy getRbacPolicy() {
		return sutRbac;
	}

	public File getTestSuiteFile() {
		return testSuiteFile;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}
	public void setName(String name) {
		this.name = name;
	}


}