package com.tomecode.soa.dependency.analyzer.usages;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import com.tomecode.soa.oracle10g.bpel.activity.Activity;
import com.tomecode.soa.oracle10g.bpel.activity.Variable;

/**
 * 
 * @author Tomas Frastia
 * 
 */
public final class FindUsageVariableResult implements FindUsage {

	private Variable variable;

	private List<Usage> activities;

	/**
	 * Constructor
	 * 
	 * @param variable
	 */
	public FindUsageVariableResult(Variable variable) {
		this.variable = variable;
		this.activities = new ArrayList<Usage>();
	}

	public final Variable getVariable() {
		return variable;
	}

	public void addUsage(Activity activity) {
		if (!contains(activity)) {
			activities.add(new Usage(activity));
		}
	}

	private final boolean contains(Activity activity) {
		for (Usage usage : activities) {
			if (usage.getActivity().equals(activity)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Enumeration<?> children() {
		return null;
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
		return null;
	}

	@Override
	public boolean isLeaf() {
		return activities.isEmpty();
	}

	public final String toString() {
		return variable.toString();
	}

	@Override
	public final ImageIcon getIcon() {
		return variable.getActivtyType().getImageIcon();
	}

}