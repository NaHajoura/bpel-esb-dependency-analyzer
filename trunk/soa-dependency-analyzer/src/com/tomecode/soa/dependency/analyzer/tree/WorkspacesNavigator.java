package com.tomecode.soa.dependency.analyzer.tree;

import java.util.List;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.tomecode.soa.dependency.analyzer.core.ApplicationManager;
import com.tomecode.soa.dependency.analyzer.gui.actions.LinkWithNavigatorAction;
import com.tomecode.soa.dependency.analyzer.gui.utils.PopupMenuUtils;
import com.tomecode.soa.dependency.analyzer.icons.ImageFactory;
import com.tomecode.soa.dependency.analyzer.tree.node.WorkspaceRootNode;
import com.tomecode.soa.dependency.analyzer.view.PropertiesView;
import com.tomecode.soa.dependency.analyzer.view.VisualGraphView;
import com.tomecode.soa.workspace.MultiWorkspace;
import com.tomecode.soa.workspace.Workspace;

/**
 * 
 * 
 * Tree navigator for workspaces, contains all workspaces and show dependencies
 * between
 * 
 * @author Tomas Frastia
 * 
 */
public final class WorkspacesNavigator extends ViewPart implements ISelectionChangedListener {

	public static final String ID = "view.workspacenavigator";

	private final WorkspaceRootNode rootNode;

	/**
	 * if is true then if click in {@link VisualGraphView} then show in tree
	 */
	private LinkWithNavigatorAction linkWithNavigatorAction;
	private TreeViewer tree;

	public WorkspacesNavigator() {
		rootNode = new WorkspaceRootNode();
		setTitleToolTip("Workspace Navigator");
		setTitleImage(ImageFactory.WORKSPACE_NAVIGATOR);
	}

	@Override
	public final void createPartControl(Composite parent) {
		tree = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tree.addSelectionChangedListener(this);
		tree.setLabelProvider(new WorkspacesLabelProvider());
		tree.setContentProvider(new WorkspacesContentProvider());

		hookContextMenu();

		List<MultiWorkspace> multiWorkspaces = ApplicationManager.getInstance().getMultiWorkspaces();
		for (MultiWorkspace multiWorkspace : multiWorkspaces) {
			rootNode.add(multiWorkspace);
		}

		tree.setInput(rootNode);

		// linkWithNavigatorAction = new LinkWithNavigatorAction();
		// IActionBars actionBars = getViewSite().getActionBars();
		// actionBars.getToolBarManager().add(linkWithNavigatorAction);
		// actionBars.getMenuManager().add(linkWithNavigatorAction);
	}

	/**
	 * hook context menu
	 */
	private final void hookContextMenu() {
		MenuManager menuManager = new MenuManager("#PopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public final void menuAboutToShow(IMenuManager manager) {

				IStructuredSelection selection = (IStructuredSelection) tree.getSelection();
				if (!selection.isEmpty()) {
					PopupMenuUtils.fillWorksapceNavigator(selection.getFirstElement(), manager);
				} else {
					PopupMenuUtils.fillEmptyWorksapceNavigator(manager);
				}
			}
		});

		Menu menu = menuManager.createContextMenu(tree.getControl());
		tree.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, tree);
	}

	@Override
	public final void setFocus() {
		tree.getTree().setFocus();
	}

	@Override
	public final void selectionChanged(SelectionChangedEvent event) {

		IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart iViewPart = workbenchPage.findView(BpelProcessStructureNavigator.ID);

		IStructuredSelection selection = (IStructuredSelection) tree.getSelection();
		if (!selection.isEmpty()) {

			if (iViewPart != null) {
				BpelProcessStructureNavigator navigator = (BpelProcessStructureNavigator) iViewPart;
				navigator.showProcessStructure(selection.getFirstElement());
			}

			iViewPart = workbenchPage.findView(ServiceOperationsDepNavigator.ID);
			if (iViewPart != null) {
				ServiceOperationsDepNavigator navigator = (ServiceOperationsDepNavigator) iViewPart;
				navigator.show(selection.getFirstElement());
			}

			iViewPart = workbenchPage.findView(VisualGraphView.ID);
			if (iViewPart != null) {
				VisualGraphView graphView = (VisualGraphView) iViewPart;
				graphView.showGraph(selection.getFirstElement(), true);
			}

			iViewPart = workbenchPage.findView(PropertiesView.ID);
			if (iViewPart != null) {
				PropertiesView propertiesView = (PropertiesView) iViewPart;
				propertiesView.showProperties(selection.getFirstElement());
			}
			iViewPart = workbenchPage.findView(ProjectStructureNavigator.ID);
			if (iViewPart != null) {
				ProjectStructureNavigator projectStructureNavigator = (ProjectStructureNavigator) iViewPart;
				projectStructureNavigator.showProjectFiles(selection.getFirstElement());
			}

			iViewPart = workbenchPage.findView(ServiceBusStructureNavigator.ID);
			if (iViewPart != null) {
				ServiceBusStructureNavigator navigator = (ServiceBusStructureNavigator) iViewPart;
				navigator.showStructure(selection.getFirstElement());
			}
		}

	}

	public void getMWorkspaceNames() {

	}

	/**
	 * add new {@link MultiWorkspace} to tree
	 * 
	 * @param multiWorkspace
	 */
	public final void newMultiWorkspace(MultiWorkspace multiWorkspace) {
		rootNode.add(multiWorkspace);
		tree.refresh(rootNode);
	}

	/**
	 * refresh multi workspace nodes
	 * 
	 * @param multiWorkspace
	 */
	public final void updateMultiWorkspace(MultiWorkspace oldMultiWorkspace, MultiWorkspace newMultiWorkspace) {
		rootNode.refreshMultiWorkspaceNode(oldMultiWorkspace, newMultiWorkspace);
	}

	/**
	 * show selected node in {@link VisualGraphView} if
	 * {@link #linkWithNavigatorAction} is checked
	 * 
	 * @param data
	 */
	public final void showInTree(Object data) {
		if (linkWithNavigatorAction.isChecked()) {
			if (data instanceof Workspace) {
				Workspace workspace = (Workspace) data;
				TreePath treePath = new TreePath(new Object[] { workspace.getMultiWorkspace(), data });
				// tree.expandToLevel(treePath, 2);
				// tree.getTree().sc
				tree.setExpandedState(treePath, true);
			}
		}
	}

	/**
	 * remove selected {@link MultiWorkspace}
	 * 
	 * @return
	 */
	public final MultiWorkspace removeMultiWorkspace() {
		IStructuredSelection selection = (IStructuredSelection) tree.getSelection();
		if (!selection.isEmpty()) {
			rootNode.remove(selection.getFirstElement());
			tree.refresh();
			return ApplicationManager.getInstance().removeMultiWorkspace(selection.getFirstElement());
		}
		return null;
	}

	/**
	 * remove selected {@link Workspace}
	 * 
	 * @return
	 */
	public final Workspace removeSelectedWorkspace() {
		IStructuredSelection selection = (IStructuredSelection) tree.getSelection();
		if (!selection.isEmpty()) {
			return (Workspace) selection.getFirstElement();
		}
		return null;
	}

	public final void refreshTree() {
		tree.refresh();
	}

}
