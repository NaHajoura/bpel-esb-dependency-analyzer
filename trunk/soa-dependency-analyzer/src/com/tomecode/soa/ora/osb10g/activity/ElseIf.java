package com.tomecode.soa.ora.osb10g.activity;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;

/**
 * parent element: ifThenElse - target element: case
 * 
 * @author Tomas Frastia
 * 
 */
public final class ElseIf extends OsbActivity {

	public final String toString() {
		return "Else If";
	}

	@Override
	public final Image getImage() {
		return ImageFactory.OSB_10G_ELSEIF;
	}
}
