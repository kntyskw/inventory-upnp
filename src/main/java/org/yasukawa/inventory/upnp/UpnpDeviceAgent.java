package org.yasukawa.inventory.upnp;

import io.soracom.inventory.agent.core.credential.PreSharedKey;
import io.soracom.inventory.agent.core.initialize.InventoryAgentInitializer;
import io.soracom.inventory.agent.core.initialize.LwM2mModelBuilder;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.util.Hex;
import org.fourthline.cling.model.meta.RemoteDevice;

public class UpnpDeviceAgent {
    protected final InventoryAgentInitializer initializer;
    protected final RemoteDevice device;
    protected LeshanClient leshanClient;

    public UpnpDeviceAgent(RemoteDevice device){
        this.device = device;
        this.initializer = new InventoryAgentInitializer();
        LwM2mModelBuilder lwM2mModelBuilder = new LwM2mModelBuilder();
        lwM2mModelBuilder.addPresetObjectModels();
        lwM2mModelBuilder.addObjectModelFromClassPath("/upnp-av-transport.xml");
        lwM2mModelBuilder.addObjectModelFromClassPath("/upnp-rendering-control.xml");
        this.initializer.setLwM2mModel(lwM2mModelBuilder.build());
        this.initializer.addInstancesForObject(new UpnpDeviceObject(device));
        this.initializer.setEndpoint(device.getIdentity().getUdn().getIdentifierString());
    }

    public void start(){
        leshanClient = initializer.buildClient();
        leshanClient.start();
    }

    public void stop(){
        if (leshanClient != null){
            leshanClient.stop(true);
        }
    }
}
