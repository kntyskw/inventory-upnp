package org.yasukawa.inventory.upnp;

import io.soracom.inventory.agent.core.lwm2m.LwM2mInstanceResponseException;
import org.eclipse.leshan.ResponseCode;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.state.StateVariableValue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class UpnpController {
    private static final long ACTION_TIMEOUT = 500;
    private final ControlPoint upnpControlPoint;
    private static UpnpController instance;
    private final XPathFactory xPathFactory;
    private final DocumentBuilder documentBuilder;

    private UpnpController(UpnpService upnpService) throws ParserConfigurationException {
        this.upnpControlPoint = upnpService.getControlPoint();
        this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.xPathFactory = XPathFactory.newInstance();
        this.instance = this;
    }

    public static void initialize(UpnpService upnpService) throws ParserConfigurationException {
        new UpnpController(upnpService);
    }

    public static UpnpController getInstance() {
        if (instance == null){
            throw new RuntimeException("UpnpConroller is not initialized");
        }
        return instance;
    }

    public Map<String, ActionArgumentValue> executeUpnpAction(ActionInvocation invocation) throws LwM2mInstanceResponseException {
        Future future = upnpControlPoint.execute(
                new ActionCallback(invocation) {

                    @Override
                    public void success(ActionInvocation invocation) {
                        System.out.println("Successfully called action: " + invocation.getAction().getName());
                    }

                    @Override
                    public void failure(ActionInvocation invocation,
                                        UpnpResponse operation,
                                        String defaultMsg) {
                        System.err.println(operation.getResponseDetails());
                        System.err.println(defaultMsg);
                    }
                }
        );
        try {
            future.get(ACTION_TIMEOUT, TimeUnit.MILLISECONDS);
            if (invocation.getFailure() == null){
                return invocation.getOutputMap();
            } else {
                throw new LwM2mInstanceResponseException(
                        ResponseCode.fromCode(invocation.getFailure().getErrorCode()),
                        invocation.getFailure().getMessage());
            }
        } catch (TimeoutException e) {
            throw new LwM2mInstanceResponseException(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (InterruptedException e) {
            throw new LwM2mInstanceResponseException(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ExecutionException e) {
            throw new LwM2mInstanceResponseException(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void subscribeGenaEvent(RemoteService service, Function<GENASubscription, GENASubscription> callback) {
        upnpControlPoint.execute(new SubscriptionCallback(service, 600) {
            @Override
            protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {

            }

            @Override
            protected void established(GENASubscription subscription) {

            }

            @Override
            protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {

            }

            @Override
            protected void eventReceived(GENASubscription subscription) {
                callback.apply(subscription);
            }

            @Override
            protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {

            }
        });
    }

    public Node getNode(GENASubscription sub, String attrName, String expression) {
        if (sub.getCurrentValues().containsKey(attrName)){
            StateVariableValue value = (StateVariableValue) sub.getCurrentValues().get(attrName);
            XPath xpath = xPathFactory.newXPath();
            try {
                System.out.println(value.getValue());
                Document document = documentBuilder.parse(new ByteArrayInputStream(value.getValue().toString().getBytes()));
                return (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}