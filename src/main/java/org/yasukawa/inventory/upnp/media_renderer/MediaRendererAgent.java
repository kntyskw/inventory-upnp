package org.yasukawa.inventory.upnp.media_renderer;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceType;
import org.yasukawa.inventory.upnp.UpnpDeviceAgent;

public class MediaRendererAgent extends UpnpDeviceAgent {
    public static final String MEDIA_RENDERER_DEVICE_TYPE = "urn:schemas-upnp-org:device:MediaRenderer:1";
    public static final String RENDERING_CONTROL_SERVICE_TYPE = "urn:schemas-upnp-org:service:RenderingControl:1";
    public static final String AV_TRANSPORT_SERVICE_TYPE = "urn:schemas-upnp-org:service:AVTransport:1";
    public static final String ATTR_LAST_CHANGE = "LastChange";
    public static final String ATTR_INSTANCE_ID = "InstanceID";

    public MediaRendererAgent(RemoteDevice device){
        super(device);
        for ( Service service : device.getServices()){
            System.out.println(service.getServiceType());
            System.out.println(service.getServiceId());
        }
        super.initializer.addInstancesForObject(new UpnpRenderingControlServiceObject(
                device.findService(ServiceType.valueOf(RENDERING_CONTROL_SERVICE_TYPE)))
        );
        super.initializer.addInstancesForObject(new UpnpAvTransportServiceObject(
                device.findService(ServiceType.valueOf(AV_TRANSPORT_SERVICE_TYPE)))
        );
    }
}
