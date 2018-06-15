package org.yasukawa.inventory.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yasukawa.inventory.upnp.media_renderer.MediaRendererAgent;

import java.util.HashMap;

public class InventoryUpnpRegistryListener implements RegistryListener {
    private final UpnpService upnpService;
    Logger logger = LoggerFactory.getLogger(InventoryUpnpRegistryListener.class.getName());
    private HashMap<String, UpnpDeviceAgent> agentMap = new HashMap<>();

    public InventoryUpnpRegistryListener(UpnpService upnpService) {
        this.upnpService = upnpService;
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {

    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        logger.info("Device {} with type {} is added", device.getIdentity().toString(), device.getType().toString());
        if (MediaRendererAgent.MEDIA_RENDERER_DEVICE_TYPE.equals(device.getType().toString())){
            logger.info("Media renderer found: " + device.getDetails().getFriendlyName());
            MediaRendererAgent agent = new MediaRendererAgent(device);
            logger.info("Starting agent for {} and registering to SORACOM Inventory",
                    device.getIdentity().toString());
            agent.start();
            agentMap.put(device.getIdentity().toString(), agent);
        }
    }

    @Override
    public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {

    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        logger.info("Device {} with type {} is removed", device.getIdentity().toString(), device.getType().toString());
        if (agentMap.containsKey(device.getIdentity().toString())){
            logger.info("Stopping agent for {} and deregistering from SORACOM Inventory",
                    device.getIdentity().toString());
            agentMap.remove(device.getIdentity().toString()).stop();
        }
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {

    }

    @Override
    public void beforeShutdown(Registry registry) {

    }

    @Override
    public void afterShutdown() {

    }
}
