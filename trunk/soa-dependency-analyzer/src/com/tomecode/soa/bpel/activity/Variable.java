package com.tomecode.soa.bpel.activity;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;

/**
 * 
 * Variable in BPEL process
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 * 
 */
public final class Variable extends Activity {

	private static final long serialVersionUID = 1387914145237466640L;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            activity name
	 * @param messageType
	 */
	public Variable(String name, String messageType) {
		super(ActivityType.ORACLE_10G_VARIABLE, name);
	}

	public final Image getImage() {
		return ImageFactory.ORACLE_10G_VARIABLE;
	}

	public final String toString() {
		return name;
	}

}
