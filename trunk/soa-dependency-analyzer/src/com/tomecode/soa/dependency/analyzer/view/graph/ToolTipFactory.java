package com.tomecode.soa.dependency.analyzer.view.graph;

import java.util.Iterator;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;

import com.tomecode.soa.dependency.analyzer.gui.utils.GuiUtils;
import com.tomecode.soa.dependency.analyzer.icons.ImageFace;
import com.tomecode.soa.ora.osb10g.services.BusinessService;
import com.tomecode.soa.ora.osb10g.services.Proxy;
import com.tomecode.soa.ora.osb10g.services.Service;
import com.tomecode.soa.ora.osb10g.services.config.EndpointConfig;
import com.tomecode.soa.ora.osb10g.services.config.EndpointConfig.ProviderProtocol;
import com.tomecode.soa.ora.osb10g.services.config.EndpointDsp;
import com.tomecode.soa.ora.osb10g.services.config.EndpointEJB;
import com.tomecode.soa.ora.osb10g.services.config.EndpointHttp;
import com.tomecode.soa.ora.osb10g.services.config.EndpointJms;
import com.tomecode.soa.ora.osb10g.services.config.EndpointUNKNOWN;
import com.tomecode.soa.ora.osb10g.services.config.ProviderSpecificJms;
import com.tomecode.soa.ora.osb10g.services.dependnecies.ServiceDependency.ServiceDependencyType;
import com.tomecode.soa.ora.suite10g.project.PartnerLinkBinding;
import com.tomecode.soa.project.Project;
import com.tomecode.soa.services.BpelProcess;
import com.tomecode.soa.workspace.MultiWorkspace;
import com.tomecode.soa.workspace.Workspace;

/**
 * tool tip factory - helper class
 * 
 * @author Tomas Frastia
 * @see http://www.tomecode.com
 *      http://code.google.com/p/bpel-esb-dependency-analyzer/
 */
public final class ToolTipFactory {

	private ToolTipFactory() {

	}

	/**
	 * create tool tip for {@link BpelProcess}
	 * 
	 * @param process
	 * @return
	 */
	// TODO: detail information about bpel process
	public static final IFigure createToolTip(BpelProcess process) {
		IFigure tooltip = createDefaultToolTip();
		tooltip.add(new org.eclipse.draw2d.Label(((ImageFace) process).getImage()));
		tooltip.add(new org.eclipse.draw2d.Label(process.getName()));
		tooltip.add(GuiUtils.createLabel2dBold("Path:"));
		tooltip.add(new org.eclipse.draw2d.Label(process.getFile() != null ? process.getFile().toString() : ""));
		return tooltip;
	}

	/**
	 * create tool tip for {@link Service}
	 * 
	 * @param service
	 * @return
	 */
	public static final IFigure createToolTip(Service service) {
		IFigure tooltip = createDefaultToolTip();
		tooltip.add(new org.eclipse.draw2d.Label(((ImageFace) service).getImage()));
		tooltip.add(new org.eclipse.draw2d.Label(service.getName()));
		tooltip.add(GuiUtils.createLabel2dBold("Path:"));
		tooltip.add(new org.eclipse.draw2d.Label(service.getFile() != null ? service.getFile().toString() : ""));

		if (service.getType() == ServiceDependencyType.BUSINESS_SERVICE) {
			BusinessService businessService = (BusinessService) service;
			addEndpointConfingToTooltip(businessService.getEndpointConfig(), tooltip);
		} else if (service.getType() == ServiceDependencyType.PROXY_SERVICE) {
			Proxy proxy = (Proxy) service;
			addEndpointConfingToTooltip(proxy.getEndpointConfig(), tooltip);
		}
		return tooltip;
	}

	/**
	 * add endpoint config for tooltip
	 * 
	 * @param endpointConfig
	 * @param tooltip
	 */
	private static final void addEndpointConfingToTooltip(EndpointConfig endpointConfig, IFigure tooltip) {
		tooltip.add(GuiUtils.createLabel2dBold("Endpoint Type:"));
		if (endpointConfig.getProtocol() == ProviderProtocol.UNKNOWN) {
			tooltip.add(new org.eclipse.draw2d.Label(((EndpointUNKNOWN) endpointConfig).getProviderId()));
		} else {
			tooltip.add(new org.eclipse.draw2d.Label(endpointConfig.getProtocol().toString()));
		}
		if (!endpointConfig.getUris().isEmpty()) {
			tooltip.add(GuiUtils.createLabel2dBold("URIs:"));
			Iterator<String> i = endpointConfig.getUris().iterator();
			while (i.hasNext()) {
				tooltip.add(new org.eclipse.draw2d.Label(i.next()));
				if (i.hasNext()) {
					tooltip.add(new org.eclipse.draw2d.Label(""));
				}
			}

		}
		fillProviderSpecific(endpointConfig, tooltip);
	}

	/**
	 * add provider specific from {@link EndpointConfig}
	 * 
	 * @param endpointConfig
	 * @param tooltip
	 */
	private static final void fillProviderSpecific(EndpointConfig endpointConfig, IFigure tooltip) {
		if (endpointConfig.getProtocol() == ProviderProtocol.JMS) {
			EndpointJms endpointJms = (EndpointJms) endpointConfig;
			ProviderSpecificJms jms = endpointJms.getProviderSpecificJms();
			if (jms.getResponseURI() != null && jms.getResponseURI().trim().length() != 0) {
				tooltip.add(GuiUtils.createLabel2dBold("Response URI:"));
				tooltip.add(new org.eclipse.draw2d.Label(jms.getResponseURI()));
			}
		} else if (endpointConfig.getProtocol() == ProviderProtocol.HTTP) {
			EndpointHttp endpointHttp = (EndpointHttp) endpointConfig;
			if (endpointHttp.getProviderSpecificHttp().getRequestMethod() != null) {
				tooltip.add(GuiUtils.createLabel2dBold("Request Method:"));
				tooltip.add(new org.eclipse.draw2d.Label(endpointHttp.getProviderSpecificHttp().getRequestMethod()));
			}
		} else if (endpointConfig.getProtocol() == ProviderProtocol.DSP) {
			EndpointDsp endpointDsp = (EndpointDsp) endpointConfig;
			tooltip.add(GuiUtils.createLabel2dBold("Request Response:"));
			tooltip.add(new org.eclipse.draw2d.Label(String.valueOf(endpointDsp.getProviderSpecificDsp().isRequestResponse())));
		} else if (endpointConfig.getProtocol() == ProviderProtocol.EJB) {
			EndpointEJB endpointEJB = (EndpointEJB) endpointConfig;
			if (endpointEJB.getProviderSpecificEJB().getClientJar() != null) {
				tooltip.add(GuiUtils.createLabel2dBold("Client Jar:"));
				tooltip.add(new org.eclipse.draw2d.Label(endpointEJB.getProviderSpecificEJB().getClientJar()));
			}

		}
	}

	/**
	 * create tool tip for {@link Project}
	 * 
	 * @param project
	 * @return
	 */
	public static final IFigure createToolTip(Project project) {
		IFigure tooltip = createDefaultToolTip();
		tooltip.add(new org.eclipse.draw2d.Label(((ImageFace) project).getImage()));
		tooltip.add(new org.eclipse.draw2d.Label(project.getName()));
		tooltip.add(GuiUtils.createLabel2dBold("Path:"));
		tooltip.add(new org.eclipse.draw2d.Label(project.getFile() != null ? project.getFile().toString() : ""));
		return tooltip;
	}

	/**
	 * create tool tip for {@link Workspace}
	 * 
	 * @param workspace
	 * @return
	 */
	public static final IFigure createToolTip(Workspace workspace) {
		IFigure tooltip = createDefaultToolTip();
		tooltip.add(new org.eclipse.draw2d.Label(((ImageFace) workspace).getImage()));
		tooltip.add(new org.eclipse.draw2d.Label(workspace.getName()));
		tooltip.add(GuiUtils.createLabel2dBold("Path:"));
		tooltip.add(new org.eclipse.draw2d.Label(workspace.getFile() != null ? workspace.getFile().toString() : ""));
		return tooltip;
	}

	/**
	 * create tool tip for {@link MultiWorkspace}
	 * 
	 * @param multiWorkspace
	 * @return
	 */
	public static final IFigure createToolTip(MultiWorkspace multiWorkspace) {
		IFigure tooltip = createDefaultToolTip();
		tooltip.add(new org.eclipse.draw2d.Label(((ImageFace) multiWorkspace).getImage()));
		tooltip.add(new org.eclipse.draw2d.Label(multiWorkspace.getName()));
		tooltip.add(GuiUtils.createLabel2dBold("Path:"));
		tooltip.add(new org.eclipse.draw2d.Label(multiWorkspace.getFile() != null ? multiWorkspace.getFile().toString() : ""));
		return tooltip;
	}

	/**
	 * create default and empty tool tip
	 * 
	 * @return
	 */
	private static final IFigure createDefaultToolTip() {
		IFigure tooltip = new Figure();
		tooltip.setBorder(new MarginBorder(5, 5, 5, 5));
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.numColumns = 2;
		tooltip.setLayoutManager(gridLayout);
		return tooltip;
	}

	/**
	 * create tool tip for {@link PartnerLinkBinding}
	 * 
	 * @param partnerLinkBinding
	 * @return
	 */
	public final static IFigure createToolTip(PartnerLinkBinding partnerLinkBinding) {
		IFigure tooltip = createDefaultToolTip();
		tooltip.add(new org.eclipse.draw2d.Label(partnerLinkBinding.getImage()));
		tooltip.add(new org.eclipse.draw2d.Label(partnerLinkBinding.toString()));
		tooltip.add(GuiUtils.createLabel2dBold("WSDL Location:"));
		tooltip.add(new org.eclipse.draw2d.Label(partnerLinkBinding.getWsdlLocation()));
		return tooltip;
	}
}