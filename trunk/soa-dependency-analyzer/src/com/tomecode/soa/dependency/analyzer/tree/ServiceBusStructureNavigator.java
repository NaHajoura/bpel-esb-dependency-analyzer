package com.tomecode.soa.dependency.analyzer.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;
import com.tomecode.soa.dependency.analyzer.tree.node.EmptyNode;
import com.tomecode.soa.ora.osb10g.activity.OsbActivity;
import com.tomecode.soa.ora.osb10g.project.OraSB10gProject;
import com.tomecode.soa.ora.osb10g.services.Proxy;
import com.tomecode.soa.ora.osb10g.services.ProxyStructure;
import com.tomecode.soa.ora.osb10g.services.SplitJoin;
import com.tomecode.soa.ora.osb10g.services.SplitJoinStructure;
import com.tomecode.soa.workspace.MultiWorkspace;
import com.tomecode.soa.workspace.Workspace;

/**
 * 
 * Show structure of selected service bus
 * 
 * @author Tomas Frastia
 * 
 */
public final class ServiceBusStructureNavigator extends ViewPart {

	public static final String ID = "view.servicebusstructurenavigator";

	private final ServiceBusStructureContentProvider contentProvider;

	private final LabelProviderImpl labelProvider;

	private final EmptyNode rootNode;

	private TreeViewer tree;

	public ServiceBusStructureNavigator() {
		rootNode = new EmptyNode();
		contentProvider = new ServiceBusStructureContentProvider();
		labelProvider = new LabelProviderImpl();
		setTitleToolTip("Service Bus - (Proxy or SplitJoin) Structure");
	}

	@Override
	public void createPartControl(Composite parent) {
		tree = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tree.setContentProvider(contentProvider);
		tree.setLabelProvider(labelProvider);
	}

	@Override
	public void setFocus() {

	}

	public final void showStructure(Object source) {
		if (source == null) {
			clearTree();
		} else {
			if (source instanceof Proxy) {
				rootNode.set(((Proxy) source).getStructure());
				tree.setInput(rootNode);
				tree.expandAll();
			} else if (source instanceof OraSB10gProject) {
				rootNode.set((OraSB10gProject) source);
				tree.setInput(rootNode);
				tree.expandAll();
			} else if (source instanceof SplitJoin) {
				rootNode.set(((SplitJoin) source).getStructure());
				tree.setInput(rootNode);
				tree.expandAll();
			} else {
				clearTree();
			}
		}
	}

	private final void clearTree() {
		rootNode.set(null);
		tree.setInput(rootNode);
	}

	public final void removeMultiWorkspace(MultiWorkspace multiWorkspace) {
		MultiWorkspace multiWorkspaceInTree = findMutliWorkspaceInTree();
		if (multiWorkspace.equals(multiWorkspaceInTree)) {
			clearTree();
		}
	}

	private final MultiWorkspace findMutliWorkspaceInTree() {
		if (rootNode.hasChildren()) {
			Object objectInTree = rootNode.getChildren()[0];
			if (objectInTree instanceof ProxyStructure) {
				return ((ProxyStructure) objectInTree).getProxy().getProject().getWorkpsace().getMultiWorkspace();
			} else if (objectInTree instanceof OraSB10gProject) {
				return ((OraSB10gProject) objectInTree).getWorkpsace().getMultiWorkspace();
			}
		}
		return null;
	}

	private final Workspace findWorkspaceInTree() {
		if (rootNode.hasChildren()) {
			Object objectInTree = rootNode.getChildren()[0];
			if (objectInTree instanceof ProxyStructure) {
				return ((ProxyStructure) objectInTree).getProxy().getProject().getWorkpsace();
			} else if (objectInTree instanceof OraSB10gProject) {
				return ((OraSB10gProject) objectInTree).getWorkpsace();
			}
		}
		return null;
	}

	public final void removeWorkspace(Workspace workspace) {
		Workspace workspaceInTree = findWorkspaceInTree();
		if (workspace.equals(workspaceInTree)) {
			clearTree();
		}
	}

	/**
	 * 
	 * Label provider for {@link ServiceBusStructureNavigator}
	 * 
	 * @author Tomas Frastia
	 * 
	 */
	private static final class LabelProviderImpl extends LabelProvider {

		public final Image getImage(Object element) {
			if (element instanceof OsbActivity) {
				return ((OsbActivity) element).getImage();
			} else if (element instanceof ProxyStructure) {
				return ((ProxyStructure) element).getImage();
			} else if (element instanceof Proxy) {
				return ((Proxy) element).getImage();
			} else if (element instanceof OraSB10gProject) {
				return ImageFactory.OSB_10G_PROJECT;
			} else if (element instanceof SplitJoinStructure) {
				return ((SplitJoinStructure) element).getImage();
			} else if (element instanceof SplitJoin) {
				return ((SplitJoin) element).getImage();
			}
			return null;
		}
	}
}