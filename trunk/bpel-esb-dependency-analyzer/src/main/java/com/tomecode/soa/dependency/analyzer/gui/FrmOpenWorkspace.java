package com.tomecode.soa.dependency.analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tomecode.soa.dependency.analyzer.gui.components.TabbedManager;
import com.tomecode.soa.dependency.analyzer.icons.IconFactory;
import com.tomecode.soa.oracle10g.Workspace;
import com.tomecode.soa.oracle10g.bpel.BpelProject;
import com.tomecode.util.gui.Dialog;
import com.tomecode.util.gui.Frame;
import com.tomecode.util.gui.HideNotifiListener;
import com.tomecode.util.gui.PanelFactory;

/**
 * Form for add new {@link Workspace} or {@link BpelProject} to
 * {@link TabbedManager}
 * 
 * @author Frastia Tomas
 * 
 */
public final class FrmOpenWorkspace extends Dialog {

	private static final long serialVersionUID = 3580997825429547608L;

	/**
	 * last selected path in {@link JFileChooser}
	 */
	private File lastSelectedPath;

	/**
	 * workspace name
	 */
	private String workspaceName;
	/**
	 * button for close this form and start parse
	 */
	private final JButton buttonOpen;
	/**
	 * button for display {@link JFileChooser}
	 */
	private final JButton bFindPath;

	private final JTextField textFieldPath;

	private final JTextField textFieldName;

	/**
	 * Constructor
	 * 
	 * @param owner
	 */
	public FrmOpenWorkspace(Frame owner, final boolean isMultipleWorkspace, HideNotifiListener notifiListener) {
		super(owner, "Open Oracle 10g BPEL/ESB workspace", 600, 140, false, true, true, true);
		setIconImage(IconFactory.WORKSPACE.getImage());
		buttonOpen = new JButton("Open");
		this.addHideListeners(notifiListener);
		buttonOpen.setEnabled(false);
		buttonOpen.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(ActionEvent e) {
				if (isMultipleWorkspace) {
					hideMe(textFieldName.getText().trim(), getSelectedPath());
				} else {
					hideMe(getSelectedPath());
				}

			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(ActionEvent e) {
				textFieldPath.setText("");
			}
		});

		textFieldPath = new JTextField("");
		textFieldPath.addKeyListener(new KeyAdapter() {
			public final void keyTyped(KeyEvent e) {
				enableButton(e, isMultipleWorkspace);
			}

			public final void keyReleased(KeyEvent e) {
				enableButton(e, isMultipleWorkspace);
			}
		});
		textFieldName = new JTextField();
		textFieldName.addKeyListener(new KeyAdapter() {
			public final void keyReleased(KeyEvent e) {
				workspaceName = textFieldName.getText().trim();
				enableButton(e, isMultipleWorkspace);
			}

			public final void keyTyped(KeyEvent e) {
				enableButton(e, isMultipleWorkspace);
			}
		});
		addToButtonLayout(buttonOpen);
		bFindPath = new JButton("...");
		bFindPath.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				chooseWorkspace(isMultipleWorkspace);
			}
		});

		JPanel pPath = PanelFactory.createBorderLayout(0, 0, 0, 0);
		pPath.add(PanelFactory.wrapWithLabelNorm("Path", textFieldPath, 20), BorderLayout.CENTER);
		pPath.add(PanelFactory.wrapByBorderLayout(bFindPath, BorderLayout.NORTH), BorderLayout.EAST);
		bFindPath.setPreferredSize(new Dimension(30, 20));

		JPanel panelTxts = null;
		if (isMultipleWorkspace) {
			JPanel pName = PanelFactory.createBorderLayout(0, 0, 0, 0);
			pName.add(PanelFactory.wrapWithLabelNorm("Name", textFieldName, 13), BorderLayout.CENTER);
			panelTxts = PanelFactory.createGridLayout(2, 1);
			panelTxts.add(pName);
		} else {
			panelTxts = PanelFactory.createBorderLayout();// .createGridLayout(2,
		}

		panelTxts.add(pPath);
		panelRoot.add(PanelFactory.wrapWithTile("", panelTxts, 10, 15, 10, 15), BorderLayout.NORTH);
	}

	/**
	 * enable button
	 */
	private final void enableButton(KeyEvent e, boolean isMultipleWorkspace) {
		File workspace = new File(textFieldPath.getText());
		if (workspace.isDirectory() && workspace.exists()) {

			if (isMultipleWorkspace) {
				if (textFieldName.getText().trim().length() == 0) {
					buttonOpen.setEnabled(false);
					return;
				}
			}
			buttonOpen.setEnabled(true);
			if ((e != null) && e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (isMultipleWorkspace) {
					hideMe(textFieldName.getText().trim(), getSelectedPath());
				} else {
					hideMe(getSelectedPath());
				}
			}
		} else {
			buttonOpen.setEnabled(false);
		}
	}

	public final String getWorkspaceName() {
		return workspaceName;
	}

	private final void chooseWorkspace(boolean isMultipleWorkspace) {
		final JFileChooser fc = (lastSelectedPath == null) ? new JFileChooser() : new JFileChooser(lastSelectedPath.getPath());
		fc.setMultiSelectionEnabled(false);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			lastSelectedPath = fc.getSelectedFile().getParentFile();
			textFieldPath.setText(fc.getSelectedFile().getPath());
			enableButton(null, isMultipleWorkspace);
		}
	}

	/**
	 * 
	 * display {@link FrmOpenWorkspace}
	 * 
	 * @param owner
	 */
	public static final void showMe(final Frame owner, final boolean isMultipleWorkspace, final HideNotifiListener notifiListener) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FrmOpenWorkspace(owner, isMultipleWorkspace, notifiListener).setVisible(true);
			}
		});
	}

	public final File getSelectedPath() {
		if (textFieldPath.getText().trim().length() == 0) {
			return null;
		}
		return new File(textFieldPath.getText().trim());
	}

}