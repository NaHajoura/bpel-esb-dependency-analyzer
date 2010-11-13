package com.tomecode.soa.dependency.analyzer.view.graph.flow.policies;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

//import org.eclipse.gef.examples.flow.model.Activity;
//import org.eclipse.gef.examples.flow.model.StructuredActivity;
//import org.eclipse.gef.examples.flow.model.commands.OrphanChildCommand;

/**
 * ActivityContainerEditPolicy
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 * 
 */
public class ActivityContainerEditPolicy extends ContainerEditPolicy {

	/**
	 * @see ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command getOrphanChildrenCommand(GroupRequest request) {
		List parts = request.getEditParts();
		CompoundCommand result = new CompoundCommand();
		for (int i = 0; i < parts.size(); i++) {
			// OrphanChildCommand orphan = new OrphanChildCommand();
			// orphan.setChild((Activity)((EditPart)parts.get(i)).getModel());
			// orphan.setParent((StructuredActivity)getHost().getModel());
			// result.add(orphan);
		}
		return result.unwrap();
	}

}
