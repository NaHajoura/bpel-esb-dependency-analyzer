package com.tomecode.soa.ora.suite10g.project;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.activity.Activity;

/**
 * (c) Copyright Tomecode.com, 2010. All rights reserved.
 * 
 * View the BPEL process tree
 * 
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/ *
 */
public final class Ora10gBpelProcessStrukture extends Activity {

	private static final long serialVersionUID = -8082029461398462336L;

	private Ora10gBpelProject bpelProject;

	/**
	 * Constructor
	 */
	public Ora10gBpelProcessStrukture() {
		// super();
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param bpelProcess
	 */
	public Ora10gBpelProcessStrukture(Ora10gBpelProject bpelProcess) {
		this();
		this.bpelProject = bpelProcess;
	}

	public void addActivity(Activity activity) {
		super.addActivity(activity);// childs.add(activity);
	}

	public final String toString() {
		return bpelProject.toString();
	}

	public final Ora10gBpelProject getProject() {
		return bpelProject;
	}

	@Override
	public Image getImage() {
		return null;
	}

	// public final FindUsagePartnerLinkResult findUsage(PartnerLink
	// partnerLink) {
	// FindUsagePartnerLinkResult findUsagePartnerLinkResult = new
	// FindUsagePartnerLinkResult(partnerLink);
	// for (Activity activity : childs) {
	// activity.findUsage(findUsagePartnerLinkResult);
	// }
	// return findUsagePartnerLinkResult;
	// }
	//
	// public final FindUsageVariableResult findUsage(Variable variable) {
	// FindUsageVariableResult findUsageVariableResult = new
	// FindUsageVariableResult(variable);
	// for (Activity activity : childs) {
	// activity.findUsage(findUsageVariableResult);
	// }
	// return findUsageVariableResult;
	// }
}
