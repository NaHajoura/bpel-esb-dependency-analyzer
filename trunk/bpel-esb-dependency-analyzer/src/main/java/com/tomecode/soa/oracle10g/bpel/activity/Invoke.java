package com.tomecode.soa.oracle10g.bpel.activity;

import com.tomecode.soa.bpel.dependency.analyzer.utils.FindUsagePartnerLinkResult;

/**
 * Invoke bpel activity
 * 
 * @author Tomas Frastia
 * 
 */
public final class Invoke extends Activity {

	private String inputVariable;

	private String outputVariable;

	private String partnerLink;

	private String operation;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param inputVariable
	 * @param outputVariable
	 * @param partnerLink
	 * @param operation
	 */
	public Invoke(String name, String inputVariable, String outputVariable, String partnerLink, String operation) {
		super(ActivityType.INVOKE, name);
		this.inputVariable = inputVariable;
		this.outputVariable = outputVariable;
		this.partnerLink = partnerLink;
		this.operation = operation;
	}

	public final String getOperation() {
		return operation;
	}

	public final String getPartnerLink() {
		return partnerLink;
	}

	public final String getInputVariable() {
		return inputVariable;
	}

	public final String getOutputVariable() {
		return outputVariable;
	}

	/**
	 * find partnerLink in activity
	 */
	public final void findPartnerLink(FindUsagePartnerLinkResult usage) {
		if (partnerLink != null && usage.getPartnerLink().getName().equals(partnerLink)) {
			usage.addUsage(this);
		}
	}
}
