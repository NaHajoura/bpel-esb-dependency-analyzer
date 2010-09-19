package com.tomecode.soa.ora.osb10g.activity;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;

/**
 * 
 * element: routing-options
 * 
 * @author Tomas Frastia
 * 
 */
public final class RoutingOptions extends OsbActivity {

	public final String toString() {
		return "Rounting Options";
	}

	@Override
	public final Image getImage() {
		return ImageFactory.OSB_10G_ROUTING_OPTIONS;
	}
}