package com.tomecode.soa.ora.osb10g.activity.splitjoin;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;
import com.tomecode.soa.ora.osb10g.activity.OsbActivity;

/**
 * element: partnerLink
 * 
 * @author Tomas Frastia
 * 
 */
public final class PartnerLink extends OsbActivity {

	private String partnerLinkType;

	private String myRole;

	private String partnerRole;

	public PartnerLink(String name, String partnerLinkType, String myRole, String partnerRole) {
		super(name);
		this.partnerLinkType = partnerLinkType;
		this.myRole = myRole;
		this.partnerRole = partnerRole;
	}

	/**
	 * @return the partnerLinkType
	 */
	public final String getPartnerLinkType() {
		return partnerLinkType;
	}

	/**
	 * @return the myRole
	 */
	public final String getMyRole() {
		return myRole;
	}

	/**
	 * @return the partnerRole
	 */
	public final String getPartnerRole() {
		return partnerRole;
	}

	@Override
	public final Image getImage() {
		return ImageFactory.OSB_10G_SPLITJOIN_EXTERNAL_SERVICE;
	}

}
