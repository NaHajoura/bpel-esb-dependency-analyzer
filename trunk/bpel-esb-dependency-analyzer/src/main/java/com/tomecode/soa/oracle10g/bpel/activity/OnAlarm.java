package com.tomecode.soa.oracle10g.bpel.activity;

import com.tomecode.soa.bpel.dependency.analyzer.utils.FindUsageVariableResult;

/**
 * onAlarm activity in bpel process
 * 
 * @author Tomas Frastia
 * 
 */
public final class OnAlarm extends Activity {

	private String variable;

	public OnAlarm(String variable) {
		super(ActivityType.ONALARM, null);
		this.variable = variable;
	}

	public final void findVariable(FindUsageVariableResult findUsageVariableResult) {
		if (variable != null && findUsageVariableResult.getVariable().toString().equals(variable)) {
			findUsageVariableResult.addUsage(this);
		}
	}
}