package org.yasukawa.inventory.upnp.media_renderer;

import io.soracom.inventory.agent.core.lwm2m.AnnotatedLwM2mInstanceEnabler;
import io.soracom.inventory.agent.core.lwm2m.LWM2MObject;
import io.soracom.inventory.agent.core.lwm2m.Operation;
import io.soracom.inventory.agent.core.lwm2m.Resource;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.yasukawa.inventory.upnp.UpnpController;

import java.util.Map;

/**
 * LwM2M device object model for UPnP Rendering Control service.
 **/
@LWM2MObject(objectId = 30001, name = "UPnP Rendering Control Service", multiple = true)
public class UpnpRenderingControlServiceObject extends AnnotatedLwM2mInstanceEnabler {
    private static final String ACTION_GET_MUTE = "GetMute";
    private static final String ACTION_SET_VOLUME = "SetVolume";
    private static final String ACTION_GET_VOLUME = "GetVolume";
    private static final String ARG_DESIRED_VOLUME = "DesiredVolume";
    private static final String ARG_INSTANCE_ID = "InstanceID";
    private static final String ARG_CHANNEL = "Channel";
    private static final String ARG_VOLUME = "Volume";
    private static final String ARG_CURRENT_MUTE = "CurrentMute";
    private static final String ARG_CURRENT_VOLUME = "CurrentVolume";
    private static final String VALUE_CHANNEL_MASTER = "Master";
    private final RemoteService renderingControlService;

    public UpnpRenderingControlServiceObject(RemoteService service) {
        this.renderingControlService = service;
        UpnpController.getInstance().subscribeGenaEvent(service, (sub) -> {
            if (UpnpController.getInstance().getNode(sub, MediaRendererAgent.ATTR_LAST_CHANGE,
                    "//Mute") != null){
                System.out.println("Mute event received!");
                fireResourcesChange(2215);
            }
            if (UpnpController.getInstance().getNode(sub, MediaRendererAgent.ATTR_LAST_CHANGE,
                    "//Volume") != null){
                System.out.println("Volume event received!");
                fireResourcesChange(2216);
            }
            return sub;
        });
    }

    /**
     * Mute status of the renderer
     **/
    @Resource(resourceId = 2215, operation = Operation.Read)
    public Boolean readMute()	{
        ActionInvocation actionInvocation =
                new ActionInvocation(renderingControlService.getAction(ACTION_GET_MUTE));
        actionInvocation.setInput(ARG_INSTANCE_ID, new UnsignedIntegerFourBytes(0));
        actionInvocation.setInput(ARG_CHANNEL, VALUE_CHANNEL_MASTER);
        Map<String, ActionArgumentValue> output = UpnpController.getInstance().executeUpnpAction(actionInvocation);
        return (Boolean) output.get(ARG_CURRENT_MUTE).getValue();
    }

    /**
     * Current audio volume of the renderer
     **/
    @Resource(resourceId = 2216, operation = Operation.Read)
    public Long readVolume()	{
        ActionInvocation actionInvocation =
                new ActionInvocation(renderingControlService.getAction(ACTION_GET_VOLUME));
        actionInvocation.setInput(ARG_INSTANCE_ID, new UnsignedIntegerFourBytes(0));
        actionInvocation.setInput(ARG_CHANNEL, VALUE_CHANNEL_MASTER);
        Map<String, ActionArgumentValue> output = UpnpController.getInstance().executeUpnpAction(actionInvocation);
        return ((UnsignedIntegerTwoBytes)output.get(ARG_CURRENT_VOLUME).getValue()).getValue();
    }

    /**
     * Sets the renderer audio volume.
     **/
    @Resource(resourceId = 2430, operation = Operation.Execute)
    public void executeSetVolume(String executeParameter)	{

        ActionInvocation setVolumeInvocation =
                new ActionInvocation(renderingControlService.getAction(ACTION_SET_VOLUME));
        setVolumeInvocation.setInput(ARG_INSTANCE_ID, new UnsignedIntegerFourBytes(0));
        setVolumeInvocation.setInput(ARG_CHANNEL, VALUE_CHANNEL_MASTER);
        setVolumeInvocation.setInput(ARG_DESIRED_VOLUME, new UnsignedIntegerTwoBytes(executeParameter));
        UpnpController.getInstance().executeUpnpAction(setVolumeInvocation);
    }
}
