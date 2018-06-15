package org.yasukawa.inventory.upnp.media_renderer;

import io.soracom.inventory.agent.core.lwm2m.AnnotatedLwM2mInstanceEnabler;
import io.soracom.inventory.agent.core.lwm2m.LWM2MObject;
import io.soracom.inventory.agent.core.lwm2m.Operation;
import io.soracom.inventory.agent.core.lwm2m.Resource;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.yasukawa.inventory.upnp.UpnpController;

import java.net.URISyntaxException;

/**
 * LwM2M device object model for UPnP AV Transport service.
 **/
@LWM2MObject(objectId = 30002, name = "UPnP AV Transport Service", multiple = true)
public class UpnpAvTransportServiceObject extends AnnotatedLwM2mInstanceEnabler {
    private static final String ACTION_PLAY = "Play";
    private static final String ARG_INSTANCE_ID = "InstanceID";
    private static final String ARG_SPEED = "Speed";
    private static final String ACTION_STOP = "Stop";
    private static final String ACTION_PAUSE = "Pause";
    private static final String ARG_CURRENT_URI = "CurrentURI";
    private static final String ACTION_SET_AV_TRANSPORT_URI = "SetAVTransportURI";
    private final RemoteService avTransportService;

    public UpnpAvTransportServiceObject(RemoteService service) {
        this.avTransportService = service;
        UpnpController.getInstance().subscribeGenaEvent(service, (sub) -> {
            if (UpnpController.getInstance().getNode(sub, MediaRendererAgent.ATTR_LAST_CHANGE,
                    "//TransportState") != null){
                System.out.println("TransportState event received!");
            }
            return sub;
        });
    }

    /**
     * Sets the AV transport URI.
     **/
    @Resource(resourceId = 2401, operation = Operation.Execute)
    public void executeSetAVTransportURI(String executeParameter) throws URISyntaxException {
        ActionInvocation invocation =
                new ActionInvocation(avTransportService.getAction(ACTION_SET_AV_TRANSPORT_URI));
        invocation.setInput(ARG_INSTANCE_ID, new UnsignedIntegerFourBytes(0));
        invocation.setInput(ARG_CURRENT_URI, executeParameter);
        UpnpController.getInstance().executeUpnpAction(invocation);
    }

    /**
     * Stops playback.
     **/
    @Resource(resourceId = 2409, operation = Operation.Execute)
    public void executeStop(String executeParameter)	{
        ActionInvocation invocation =
                new ActionInvocation(avTransportService.getAction(ACTION_STOP));
        invocation.setInput(ARG_INSTANCE_ID, new UnsignedIntegerFourBytes(0));
        UpnpController.getInstance().executeUpnpAction(invocation);
    }

    /**
     * Starts playback.
     **/
    @Resource(resourceId = 2410, operation = Operation.Execute)
    public void executePlay(String executeParameter)	{
        ActionInvocation invocation =
                new ActionInvocation(avTransportService.getAction(ACTION_PLAY));
        invocation.setInput(ARG_INSTANCE_ID, new UnsignedIntegerFourBytes(0));
        invocation.setInput(ARG_SPEED, "1");
        UpnpController.getInstance().executeUpnpAction(invocation);
    }

    /**
     * Pauses playback.
     **/
    @Resource(resourceId = 2411, operation = Operation.Execute)
    public void executePause(String executeParameter)	{
        ActionInvocation invocation =
                new ActionInvocation(avTransportService.getAction(ACTION_PAUSE));
        invocation.setInput(ARG_INSTANCE_ID, new UnsignedIntegerFourBytes(0));
        UpnpController.getInstance().executeUpnpAction(invocation);
    }
}
