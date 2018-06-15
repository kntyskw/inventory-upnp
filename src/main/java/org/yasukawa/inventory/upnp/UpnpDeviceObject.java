package org.yasukawa.inventory.upnp;

import io.soracom.inventory.agent.core.lwm2m.LwM2mInstanceResponseException;
import io.soracom.inventory.agent.core.lwm2m.typed_object.DeviceObject;
import org.eclipse.leshan.ResponseCode;
import org.fourthline.cling.model.meta.RemoteDevice;

public class UpnpDeviceObject extends DeviceObject {
    private final RemoteDevice device;

    public UpnpDeviceObject(RemoteDevice device){
        this.device = device;
    }

    /**
     * Human readable manufacturer name
     **/
    @Override
    public String readManufacturer() {
        return device.getDetails().getManufacturerDetails().getManufacturer();
    }

    /**
     * A model identifier (manufacturer specified string)
     **/
    @Override
    public String readModelNumber()	{
        return device.getDetails().getModelDetails().getModelNumber();
    }

    /**
     * Serial Number
     **/
    @Override
    public String readSerialNumber()	{
        return device.getDetails().getSerialNumber();
    }

    /**
     * Current firmware version of the Device.The Firmware Management function could rely on this resource.
     **/
    @Override
    public String readFirmwareVersion()	{
        return device.getVersion().toString();
    }

    @Override
    public void executeReboot(String executeParameter) {
        throw new LwM2mInstanceResponseException(ResponseCode.BAD_REQUEST, "Reboot not supported");
    }

    @Override
    public Integer readErrorCode() {
        return null;
    }

    @Override
    public String readSupportedBindingAndModes() {
        return null;
    }

    /**
     * Type of the device (manufacturer specified string: e.g., smart meters / dev Class…)
     **/
    @Override
    public String readDeviceType()	{
        return device.getType().getDisplayString();
    }
}
