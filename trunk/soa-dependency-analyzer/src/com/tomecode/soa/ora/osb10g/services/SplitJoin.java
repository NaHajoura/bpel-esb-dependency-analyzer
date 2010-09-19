package com.tomecode.soa.ora.osb10g.services;

import java.io.File;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;
import com.tomecode.soa.ora.osb10g.services.dependnecies.ServiceDependency.ServiceDependencyType;

/**
 * 
 * 
 * SplitJoin service
 * 
 * @author Tomas Frastia
 * 
 */
public final class SplitJoin extends Service {

	private final SplitJoinStructure structure;

	/**
	 * Constructor
	 * 
	 * @param file
	 *            SplitJoin file
	 * @param name
	 *            SplitJoin flow name
	 */
	public SplitJoin(File file, String name) {
		super(file, ServiceDependencyType.SPLITJOIN);
		this.name = name;
		this.structure = new SplitJoinStructure(this);
	}

	public final SplitJoinStructure getStructure() {
		return structure;
	}

	@Override
	public final Image getImage() {
		return ImageFactory.OSB_10G_SPLITJOIN;
	}

}
