package com.tomecode.soa.ora.suite10g.activity;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.activity.Activity;
import com.tomecode.soa.activity.ActivityType;
import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;

/**
 * Invoke BPEL activity
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 * 
 */
public final class Invoke extends Activity {

	private static final long serialVersionUID = 4576862167470771828L;

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
		super(ActivityType.ORACLE_10G_INVOKE, name);
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

	public final Image getImage() {
		return ImageFactory.ORACLE_10G_INVOKE;
	}

	// /**
	// * find partnerLink in activity
	// */
	// public final void findPartnerLink(FindUsagePartnerLinkResult usage) {
	// if (partnerLink != null &&
	// usage.getPartnerLink().getName().equals(partnerLink)) {
	// usage.addUsage(this);
	// }
	// }

	public final boolean compare(Activity activity) {
		if (super.compare(activity)) {
			if (activity instanceof Invoke) {
				Invoke invoke = (Invoke) activity;
				if (partnerLink != null && invoke.getPartnerLink() != null) {
					if (partnerLink.equals(invoke.getPartnerLink())) {
						if (operation != null && invoke.getOperation() != null) {
							return operation.equals(invoke.getOperation());
						}
					}
				}
				if (operation != null && invoke.getOperation() != null) {
					return operation.equals(invoke.getOperation());
				}
			}
		}
		return false;
	}
}