package com.tomecode.soa.ora.osb10g.activity;

/**
 * pipeline-node in proxy
 * 
 * @author frastia
 * 
 */
public final class PipelinePairNode extends OsbActivity {

	public PipelinePairNode(String name) {
		super(name);
	}

	public final String toString() {
		return name == null ? "PipelinePairNode" : "PipelinePairNode - " + name;
	}
}
