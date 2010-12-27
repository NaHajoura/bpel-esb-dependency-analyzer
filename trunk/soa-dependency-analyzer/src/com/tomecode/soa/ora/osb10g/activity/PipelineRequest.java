package com.tomecode.soa.ora.osb10g.activity;

import org.eclipse.swt.graphics.Image;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;

/**
 * (c) Copyright Tomecode.com, 2010. All rights reserved.
 * 
 * element: pipeline type="request"
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 */
public final class PipelineRequest extends OsbActivity {

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public PipelineRequest(String name, String errorHandler) {
		super(name, errorHandler);
	}

	public final void merge(PipelineRequest request) {
		this.errorHandler = request.getErrorHandlerName();
		for (OsbActivity copyActivity : request.activities) {
			copyActivity.setParent(this);
			this.activities.add(copyActivity);
		}
	}

	public final String toString() {
		return name == null ? "Pipeline Request" : "Pipeline Request - " + name;
	}

	@Override
	public final Image getImage() {
		return ImageFactory.OSB_10G_PIPELINE_REQUEST;
	}

	public final String getToolTip() {
		return "Type: Pipeline Request\nName: " + (name != null ? name : "");
	}
}
