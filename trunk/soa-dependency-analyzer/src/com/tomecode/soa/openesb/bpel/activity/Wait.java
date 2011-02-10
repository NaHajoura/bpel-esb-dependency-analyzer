package com.tomecode.soa.openesb.bpel.activity;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.activity.Activity;
import com.tomecode.soa.activity.ActivityType;

/**
 * (c) Copyright Tomecode.com, 2010. All rights reserved.
 * 
 * wait activity in Open ESB - BPEL process
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 * 
 */
public final class Wait extends Activity {

	private static final long serialVersionUID = -244298324165649381L;

	private String forr;

	private String until;

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public Wait(ActivityType type) {
		super(type, null);
	}

	/**
	 * @return the for
	 */
	public final String getFor() {
		return forr;
	}

	/**
	 * @param forr
	 *            the for to set
	 */
	public final void setFor(String forr) {
		this.forr = forr;
	}

	/**
	 * @return the until
	 */
	public final String getUntil() {
		return until;
	}

	/**
	 * @param until
	 *            the until to set
	 */
	public final void setUntil(String until) {
		this.until = until;
	}

	@Override
	public Image getImage() {
		return null;
	}

}
