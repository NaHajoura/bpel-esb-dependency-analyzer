package com.tomecode.soa.ora.osb10g.services;

import java.io.File;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;
import com.tomecode.soa.ora.osb10g.services.dependnecies.ServiceDependency.ServiceDependencyType;

/**
 * 
 * Resource file
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 */
public final class ResourceFile extends Service {

	public ResourceFile(File file, ServiceDependencyType type) {
		super(file, type);
		name = file.getName();
	}

	@Override
	public final Image getImage() {
		if (type == ServiceDependencyType.ARCHIVE) {
			return ImageFactory.OSB_10G_ARCHIVE;
		} else if (type == ServiceDependencyType.WSDL) {
			return ImageFactory.OSB_10G_WSDL_TRANSFORMATION;
		} else if (type == ServiceDependencyType.XMLSCHEMA) {
			return ImageFactory.OSB_10G_XML_SCHEMA;
		} else if (type == ServiceDependencyType.XQUERY) {
			return ImageFactory.OSB_10G_XQ_TRANSFORMATION;
		}
		return null;
	}
}