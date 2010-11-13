package com.tomecode.soa.dependency.analyzer.view.graph.flow.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;

import com.tomecode.soa.dependency.analyzer.view.graph.flow.Activity;
import com.tomecode.soa.dependency.analyzer.view.graph.flow.FlowElement;
import com.tomecode.soa.dependency.analyzer.view.graph.flow.Transition;
import com.tomecode.soa.dependency.analyzer.view.graph.flow.policies.ActivityEditPolicy;
import com.tomecode.soa.dependency.analyzer.view.graph.flow.policies.ActivityNodeEditPolicy;

/**
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 * 
 */
public abstract class ActivityPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart {

	protected DirectEditManager manager;

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
		getActivity().addPropertyChangeListener(this);
	}

	protected void applyGraphResults(CompoundDirectedGraph graph, Map<?, ?> map) {
		Node n = (Node) map.get(this);
		getFigure().setBounds(new Rectangle(n.x, n.y, n.width, n.height));

		for (int i = 0; i < getSourceConnections().size(); i++) {
			TransitionPart trans = (TransitionPart) getSourceConnections().get(i);
			trans.applyGraphResults(graph, map);
		}
	}

	public void contributeEdgesToGraph(CompoundDirectedGraph graph, Map<?, ?> map) {
		List<?> outgoing = getSourceConnections();
		for (int i = 0; i < outgoing.size(); i++) {
			TransitionPart part = (TransitionPart) getSourceConnections().get(i);
			part.contributeToGraph(graph, map);
		}
		for (int i = 0; i < getChildren().size(); i++) {
			ActivityPart child = (ActivityPart) children.get(i);
			child.contributeEdgesToGraph(graph, map);
		}
	}

	public abstract void contributeNodesToGraph(CompoundDirectedGraph graph, Subgraph s, Map<?, ?> map);

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ActivityNodeEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new ActivitySourceEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActivityEditPolicy());
		// installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new
		// ActivityDirectEditPolicy());
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		super.deactivate();
		getActivity().removePropertyChangeListener(this);
	}

	/**
	 * Returns the Activity model associated with this EditPart
	 * 
	 * @return the Activity model
	 */
	protected Activity getActivity() {
		return (Activity) getModel();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected final List<Transition> getModelSourceConnections() {
		return getActivity().getOutgoingTransitions();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected final List<Transition> getModelTargetConnections() {
		return getActivity().getIncomingTransitions();
	}

	abstract int getAnchorOffset();

	/**
	 * @see NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new BottomAnchor(getFigure(), getAnchorOffset());
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new BottomAnchor(getFigure(), getAnchorOffset());
	}

	/**
	 * @see NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new TopAnchor(getFigure(), getAnchorOffset());
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new TopAnchor(getFigure(), getAnchorOffset());
	}

	protected void performDirectEdit() {
	}

	/**
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (FlowElement.CHILDREN.equals(prop))
			refreshChildren();
		else if (FlowElement.INPUTS.equals(prop))
			refreshTargetConnections();
		else if (FlowElement.OUTPUTS.equals(prop))
			refreshSourceConnections();
		else if (Activity.NAME.equals(prop))
			refreshVisuals();

		// Causes Graph to re-layout
		((GraphicalEditPart) (getViewer().getContents())).getFigure().revalidate();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#setFigure(org.eclipse.draw2d.IFigure)
	 */
	protected void setFigure(IFigure figure) {
		figure.getBounds().setSize(0, 0);
		super.setFigure(figure);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#toString()
	 */
	public String toString() {
		return getModel().toString();
	}

}
