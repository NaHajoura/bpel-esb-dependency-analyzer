package com.tomecode.soa.ora.osb10g.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.tomecode.soa.ora.osb10g.activity.Alert;
import com.tomecode.soa.ora.osb10g.activity.Assign;
import com.tomecode.soa.ora.osb10g.activity.Branch;
import com.tomecode.soa.ora.osb10g.activity.BranchNodeCondition;
import com.tomecode.soa.ora.osb10g.activity.DefaultBranch;
import com.tomecode.soa.ora.osb10g.activity.Delete;
import com.tomecode.soa.ora.osb10g.activity.DynamicRoute;
import com.tomecode.soa.ora.osb10g.activity.ElseIf;
import com.tomecode.soa.ora.osb10g.activity.Error;
import com.tomecode.soa.ora.osb10g.activity.Foreach;
import com.tomecode.soa.ora.osb10g.activity.If;
import com.tomecode.soa.ora.osb10g.activity.IfDefault;
import com.tomecode.soa.ora.osb10g.activity.IfThenElse;
import com.tomecode.soa.ora.osb10g.activity.Insert;
import com.tomecode.soa.ora.osb10g.activity.JavaCallout;
import com.tomecode.soa.ora.osb10g.activity.Log;
import com.tomecode.soa.ora.osb10g.activity.MflTransform;
import com.tomecode.soa.ora.osb10g.activity.OsbActivity;
import com.tomecode.soa.ora.osb10g.activity.PipelinePairNode;
import com.tomecode.soa.ora.osb10g.activity.PipelineRequest;
import com.tomecode.soa.ora.osb10g.activity.PipelineResponse;
import com.tomecode.soa.ora.osb10g.activity.Rename;
import com.tomecode.soa.ora.osb10g.activity.Replace;
import com.tomecode.soa.ora.osb10g.activity.Reply;
import com.tomecode.soa.ora.osb10g.activity.Report;
import com.tomecode.soa.ora.osb10g.activity.Route;
import com.tomecode.soa.ora.osb10g.activity.RouteNode;
import com.tomecode.soa.ora.osb10g.activity.Router;
import com.tomecode.soa.ora.osb10g.activity.RoutingTableCase;
import com.tomecode.soa.ora.osb10g.activity.RoutingTableDefaultCase;
import com.tomecode.soa.ora.osb10g.activity.Skip;
import com.tomecode.soa.ora.osb10g.activity.Stage;
import com.tomecode.soa.ora.osb10g.activity.TransportHeaders;
import com.tomecode.soa.ora.osb10g.activity.Validate;
import com.tomecode.soa.ora.osb10g.activity.WsCallout;
import com.tomecode.soa.ora.osb10g.activity.WsCalloutRequestTransform;
import com.tomecode.soa.ora.osb10g.activity.WsCalloutResponseTransform;
import com.tomecode.soa.ora.osb10g.services.Binding;
import com.tomecode.soa.ora.osb10g.services.Proxy;
import com.tomecode.soa.ora.osb10g.services.ProxyStructure;
import com.tomecode.soa.ora.osb10g.services.Binding.BindingType;
import com.tomecode.soa.ora.osb10g.services.Binding.WsdlServiceBinding;
import com.tomecode.soa.ora.osb10g.services.Binding.WsldServiceBindingType;
import com.tomecode.soa.parser.AbstractParser;
import com.tomecode.soa.parser.ServiceParserException;

/**
 * Oracle OSB 10g Proxy Parser
 * 
 * @author Tomas Frastia
 * 
 */
public final class OraSB10gProxyParser extends AbstractParser {

	public final Proxy parseProxy(File file) throws ServiceParserException {
		Element eXml = parseXml(file);
		Element eCoreEntry = eXml.element("coreEntry");
		Proxy proxy = new Proxy(file);
		parseCoreEntrty(eCoreEntry, proxy);

		parseFlow(eXml.element("router"), proxy);

		return proxy;
	}

	/**
	 * parse element coreEntry
	 * 
	 * @param eCoreEntry
	 * @param proxy
	 */
	private final void parseCoreEntrty(Element eCoreEntry, Proxy proxy) {
		if ("false".equals(eCoreEntry.attributeValue("isProxy"))) {
			// TODO: THROW IS NOT PROXY
		}

		proxy.setIsEnabled(Boolean.parseBoolean(eCoreEntry.attributeValue("isEnabled")));
		proxy.setIsAutoPublish(Boolean.parseBoolean(eCoreEntry.attributeValue("isAutoPublish")));
		proxy.setDescription(eCoreEntry.elementText("description"));

		parseBinding(eCoreEntry.element("binding"), proxy);
		proxy.toString();
	}

	private final void parseFlow(Element eRouter, Proxy proxy) {
		ProxyStructure proxyStructure = new ProxyStructure(proxy);
		proxy.setProxyStructure(proxyStructure);
		Router router = new Router();
		proxyStructure.addActivity(router);
		// parseActivities(eRouter.elements(), router);

		Element eFlow = eRouter.element("flow");
		if (eFlow != null) {
			parseFlowStructure(eFlow.elements(), router);
			List<OsbActivity> pipelines = parseElements(eRouter.elements("pipeline"));
			linkingPipeLines(pipelines, router);
		}
	}

	/**
	 * linking pipeLine-s with flow
	 * 
	 * @param pipelines
	 * @param router
	 */
	private final void linkingPipeLines(List<OsbActivity> pipelines, Router router) {
		for (OsbActivity osbActivity : pipelines) {
			if (osbActivity instanceof PipelineRequest) {
				PipelineRequest pipelineRequest = (PipelineRequest) osbActivity;
				replacePipelineRequest(pipelineRequest, router.getActivities());
			} else if (osbActivity instanceof PipelineResponse) {
				PipelineResponse pipelineResponse = (PipelineResponse) osbActivity;
				replacePipelineResponse(pipelineResponse, router.getActivities());
			}
		}
	}

	/**
	 * 
	 * @param pipelineResponse
	 * @param root
	 */
	private final void replacePipelineResponse(PipelineResponse pipelineResponse, List<OsbActivity> activities) {
		for (OsbActivity osbActivity : activities) {
			if (osbActivity instanceof PipelineResponse && osbActivity.getActivities().isEmpty()) {
				PipelineResponse pipelineResponseInFlow = (PipelineResponse) osbActivity;
				if (pipelineResponseInFlow.getName().endsWith(pipelineResponse.getName())) {
					pipelineResponseInFlow.merge(pipelineResponse.getActivities());
				}
			}
//			else {
//				toString();
//	//			replacePipelineResponse(pipelineResponse, osbActivity.getActivities());
//			}
		}
	}

	/**
	 * 
	 * @param pipelineRequest
	 * @param root
	 */
	private final void replacePipelineRequest(PipelineRequest pipelineRequest, List<OsbActivity> activities) {
		for (OsbActivity osbActivity : activities) {
			if (osbActivity instanceof PipelineRequest && osbActivity.getActivities().isEmpty()) {
				PipelineRequest pipelineRequestInFlow = (PipelineRequest) osbActivity;
				if (pipelineRequestInFlow.getName().equals(pipelineRequest.getName())) {
					pipelineRequestInFlow.merge(pipelineRequest.getActivities());
				}
			} 
			
//			else {
//				replacePipelineRequest(pipelineRequest, osbActivity.getActivities());
//			}
		}
	}

	/**
	 * parse element: flow
	 * 
	 * @param eFlow
	 * @param root
	 */
	private final void parseFlowContent(Element eFlow, OsbActivity root) {
		if (eFlow != null) {
			parseFlowStructure(eFlow.elements(), root);
		}
	}

	private final List<OsbActivity> parseElements(List<?> ePipelines) {
		List<OsbActivity> osbActivities = new ArrayList<OsbActivity>();
		if (ePipelines != null) {
			for (Object o : ePipelines) {
				Element element = (Element) o;
				if ("request".equals(element.attributeValue("type"))) {
					PipelineRequest pipelineRequest = new PipelineRequest(element.attributeValue("name"));
					parseActivities(element.elements(), pipelineRequest);
					osbActivities.add(pipelineRequest);
				} else if ("response".equals(element.attributeValue("type"))) {
					PipelineResponse pipelineResponse = new PipelineResponse(element.attributeValue("name"));
					parseActivities(element.elements(), pipelineResponse);
					osbActivities.add(pipelineResponse);
				}
			}
		}
		return osbActivities;
	}

	/**
	 * parse all activities
	 * 
	 * @param root
	 */
	private final void parseActivities(List<?> elements, OsbActivity root) {
		for (Object o : elements) {
			Element e = (Element) o;
			if ("stage".equals(e.getName())) {
				Stage stage = new Stage(e.attributeValue("name"));
				root.addActivity(stage);
				Element eActions = e.element("actions");
				if (eActions != null) {
					parseActivities(eActions.elements(), stage);
				}
			} else if ("actions".equals(e.getName())) {
				parseActivities(e.elements(), root);
			} else if ("ifThenElse".equals(e.getName())) {
				IfThenElse ifThenElse = new IfThenElse();
				root.addActivity(ifThenElse);
				parseActivities(e.elements(), ifThenElse);
			} else if ("case".equals(e.getName())) {
				if (root instanceof IfThenElse) {
					IfThenElse ifThenElse = (IfThenElse) root;
					if (ifThenElse.hasIf()) {
						ifThenElse.addActivity(new ElseIf());
					} else {
						ifThenElse.addActivity(new If());
					}
				}
			} else if ("delete".equals(e.getName())) {
				root.addActivity(new Delete());
			} else if ("default".equals(e.getName())) {
				if (root instanceof IfThenElse) {
					IfDefault ifDefault = new IfDefault();
					root.addActivity(ifDefault);
					parseActivities(e.elements(), ifDefault);
				}
			} else if ("Error".equals(e.getName())) {
				root.addActivity(new Error(e.elementTextTrim("errCode"), e.elementTextTrim("message")));
			} else if ("reply".equals(e.getName())) {
				root.addActivity(new Reply());
			} else if ("skip".equals(e.getName())) {
				root.addActivity(new Skip());
			} else if ("assign".equals(e.getName())) {
				root.addActivity(new Assign());
			} else if ("insert".equals(e.getName())) {
				root.addActivity(new Insert());
			} else if ("javaCallout".equals(e.getName())) {
				root.addActivity(new JavaCallout());
			} else if ("mflTransform".equals(e.getName())) {
				root.addActivity(new MflTransform());
			} else if ("rename".equals(e.getName())) {
				root.addActivity(new Rename());
			} else if ("replace".equals(e.getName())) {
				root.addActivity(new Replace());
			} else if ("validate".equals(e.getName())) {
				root.addActivity(new Validate());
			} else if ("alert".equals(e.getName())) {
				root.addActivity(new Alert(e.elementText("severity")));
			} else if ("report".equals(e.getName())) {
				root.addActivity(new Report());
			} else if ("log".equals(e.getName())) {
				root.addActivity(new Log(e.elementTextTrim("logLevel")));
			} else if ("foreach".equals(e.getName())) {
				Foreach foreach = new Foreach();
				root.addActivity(foreach);

				Element eActions = e.element("actions");
				if (eActions != null) {
					parseActivities(eActions.elements(), foreach);
				}
			} else if ("dynamic-route".equals(e.getName())) {
				DynamicRoute dynamicRoute = new DynamicRoute();
				root.addActivity(dynamicRoute);
				parseOutboundTransform(e, dynamicRoute);
			} else if ("route".equals(e.getName())) {
				Route route = new Route();
				root.addActivity(root);
				parseOutboundTransform(e, route);
			} else if ("routingTable".equals(e.getName())) {
				parseRoutingTable(e.getParent(), root);
			} else if ("transport-headers".equals(e.getName())) {
				TransportHeaders transportHeaders = new TransportHeaders();
				root.addActivity(transportHeaders);
			} else if ("wsCallout".equals(e.getName())) {
				parseWsCallout(e, root);
			}
		}
	}

	/**
	 * element: wsCallout
	 * 
	 * @param eWsCallout
	 * @param root
	 */
	private final void parseWsCallout(Element eWsCallout, OsbActivity root) {

		WsCallout wsCallout = new WsCallout();
		root.addActivity(wsCallout);
		List<?> elements = eWsCallout.elements();
		for (Object o : elements) {
			Element element = (Element) o;
			if ("requestTransform".equals(element.getName())) {
				WsCalloutRequestTransform requestTransform = new WsCalloutRequestTransform();
				root.addActivity(requestTransform);
				parseActivities(element.elements(), requestTransform);
			} else if ("responseTransform".equals(element.getName())) {
				WsCalloutResponseTransform responseTransform = new WsCalloutResponseTransform();
				root.addActivity(responseTransform);
				parseActivities(element.elements(), responseTransform);
			}
		}
	}

	/**
	 * parse content of element: router
	 * 
	 * @param elements
	 * @param root
	 */
	private final void parseFlowStructure(List<?> elements, OsbActivity root) {
		for (Object o : elements) {
			Element e = (Element) o;

			if ("pipeline-node".equals(e.getName())) {
				parsePipeLineNodeFlow(e, root);
			} else if ("branch-node".equals(e.getName())) {
				parseBranchNodeFlow(e, root);
			} else if ("route-node".equals(e.getName())) {
				parseRouteNode(e, root);
			}
		}

		root.toString();
	}

	/**
	 * element: route-node
	 * 
	 * @param eRouteNode
	 * @param root
	 */
	private final void parseRouteNode(Element eRouteNode, OsbActivity root) {
		RouteNode routeNode = new RouteNode(eRouteNode.attributeValue("name"));
		root.addActivity(routeNode);
		Element eActions = eRouteNode.element("actions");
		if (eActions != null) {
			parseRoutingTable(eActions, root);
		}
	}

	/**
	 * parse element: routingTable
	 * 
	 * @param e
	 * @param root
	 */
	private final void parseRoutingTable(Element e, OsbActivity root) {
		Element eRoutingTable = e.element("routingTable");
		if (eRoutingTable != null) {
			List<?> elements = eRoutingTable.elements();
			if (elements != null) {
				for (Object o : elements) {
					Element element = (Element) o;
					if ("case".equals(element.getName())) {
						RoutingTableCase routingTableCase = new RoutingTableCase();
						root.addActivity(routingTableCase);
						parseRoute(element.element("route"), routingTableCase);
					} else if ("defaultCase".equals(element.getName())) {
						RoutingTableDefaultCase routingTableDefaultCase = new RoutingTableDefaultCase();
						root.addActivity(routingTableDefaultCase);
						parseRoute(element.element("route"), routingTableDefaultCase);
					}
				}
			}
		}
	}

	/**
	 * parse element: route
	 * 
	 * @param element
	 * @param root
	 */
	private final void parseRoute(Element element, OsbActivity root) {
		Route route = new Route();
		root.addActivity(route);
		parseOutboundTransform(element, route);

	}

	/**
	 * parse element: outboundTransform
	 * 
	 * @param element
	 * @param root
	 */
	private final void parseOutboundTransform(Element element, OsbActivity root) {
		Element eOutboundTransform = element.element("outboundTransform");
		if (eOutboundTransform != null) {
			parseActivities(eOutboundTransform.elements(), root);
		}
	}

	/**
	 * parse branch-node
	 * 
	 * @param e
	 * @param root
	 */
	private final void parseBranchNodeFlow(Element eBranchNode, OsbActivity root) {
		if ("condition".equals(eBranchNode.attributeValue("type"))) {
			BranchNodeCondition branchNodeCondition = new BranchNodeCondition(eBranchNode.attributeValue("name"));
			root.addActivity(branchNodeCondition);
			Element eBranchTable = eBranchNode.element("branch-table");
			if (eBranchTable != null) {
				List<?> eBranchs = eBranchTable.elements();
				for (Object o : eBranchs) {
					Element eBranch = (Element) o;
					if ("branch".equals(eBranch.getName())) {
						Branch branch = new Branch(eBranch.attributeValue("name"));
						branchNodeCondition.addActivity(branch);
						parseFlowContent(eBranch.element("flow"), branch);
					} else if ("default-branch".equals(eBranch.getName())) {
						DefaultBranch defaultBranch = new DefaultBranch();
						branchNodeCondition.addActivity(defaultBranch);
						parseFlowContent(eBranch.element("flow"), defaultBranch);
					}

				}
			}

		}
	}

	/**
	 * parse pipeline-node
	 * 
	 * @param ePipelineNode
	 * @param root
	 */
	private final void parsePipeLineNodeFlow(Element ePipelineNode, OsbActivity root) {
		PipelinePairNode pipelinePairNode = new PipelinePairNode(ePipelineNode.attributeValue("name"));
		root.addActivity(pipelinePairNode);
		List<?> elements = ePipelineNode.elements();
		for (Object o : elements) {
			Element element = (Element) o;
			if ("request".equals(element.getName())) {
				pipelinePairNode.addActivity(new PipelineRequest(element.getTextTrim()));
			} else if ("response".equals(element.getName())) {
				pipelinePairNode.addActivity(new PipelineResponse(element.getTextTrim()));
			}
		}
	}

	/**
	 * parse element binding
	 * 
	 * @param eBinding
	 * @param proxy
	 */
	private final void parseBinding(Element eBinding, Proxy proxy) {
		if (eBinding != null) {
			BindingType bindingType = BindingType.parse(eBinding.attributeValue("type"));
			Binding binding = new Binding(bindingType, Boolean.parseBoolean(eBinding.attributeValue("isSoap12")));
			proxy.setBinding(binding);
			if (bindingType == BindingType.SOAP_SERVICES) {
				Element eWsdl = eBinding.element("wsdl");
				binding.setWsdlRef(eWsdl.attributeValue("ref"));

				Element eWsdlBinding = eBinding.element("binding");
				if (eWsdlBinding != null) {
					String name = eWsdlBinding.elementTextTrim("name");
					String namespace = eWsdlBinding.elementTextTrim("namespace");
					WsdlServiceBinding wsdlServiceBinding = new WsdlServiceBinding(WsldServiceBindingType.BINDING, name, namespace);
					binding.setWsdlServiceBinding(wsdlServiceBinding);
				}

				Element eWsdlPort = eBinding.element("port");
				if (eWsdlPort != null) {
					String name = eWsdlPort.elementTextTrim("name");
					String namespace = eWsdlPort.elementTextTrim("namespace");
					WsdlServiceBinding wsdlServiceBinding = new WsdlServiceBinding(WsldServiceBindingType.PORT, name, namespace);
					binding.setWsdlServiceBinding(wsdlServiceBinding);
				}

				Element eSelector = eBinding.element("selector");
				if (eSelector != null) {
					Element eMapping = eSelector.element("mapping");
					if (eMapping != null) {
						binding.setWsdlOperation(eMapping.attributeValue("operation"));
					}
				}
			}
		}
	}
}
