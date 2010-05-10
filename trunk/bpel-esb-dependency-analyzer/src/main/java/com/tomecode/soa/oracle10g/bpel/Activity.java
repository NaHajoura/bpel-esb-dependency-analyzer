package com.tomecode.soa.oracle10g.bpel;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

/**
 * 
 * BPEL Activity
 * 
 * @author Tomas Frastia
 * 
 */
public final class Activity implements TreeNode {

	/**
	 * actvity type
	 */
	private ActivtyType activtyType;

	/**
	 * child activities
	 */
	private final Vector<Activity> activities;

	private Activity parent;

	private String type;

	private String name;

	/**
	 * Constructor
	 */
	public Activity() {
		this.activities = new Vector<Activity>();
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public Activity(String type) {
		this();
		this.type = type;
		this.activtyType = ActivtyType.parseActivtyType(type);
	}

	public Activity(String type, String name) {
		this(type);
		this.name = name;
	}

	public final void addActivity(Activity activity) {
		activity.setParent(this);
		this.activities.add(activity);
	}

	public final ActivtyType getActivtyType() {
		return activtyType;
	}

	private void setParent(Activity parent) {
		this.parent = parent;
	}

	public final String getName() {
		return name;
	}

	@Override
	public Enumeration<?> children() {
		return activities.elements();
	}

	@Override
	public boolean getAllowsChildren() {
		return !activities.isEmpty();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return activities.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return activities.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return activities.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return this.parent;
	}

	@Override
	public boolean isLeaf() {
		return activities.isEmpty();
	}

	public final String toString() {
		return type + (name == null ? "" : (":" + name));
	}
}