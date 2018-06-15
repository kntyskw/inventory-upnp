# inventory-upnp 
This project contains a sample application of SORACOM Inventory that uses UPnP to discover devices in local area network and register them as a device. Currently it only supports UPnP/DLNA media renderer.

## To build and run
1. Run gradle and create a distribution package. The following command creates a zip file containing executable package at `build/distributions/inventory-upnp.zip`
```
$ gradle distZip
```
2. Copy and unzip `build/distributions/inventory-upnp.zip`
3. run 
```
# ./bin/inventory-upnp
```

It starts UPnP control point, discovers UPnP/DLNA media renderers in the LAN and registers them to SORACOM Inventory. You can control and monitor volume and mute values, and play media file by calling `SetAvControlUri` and `Play` actions.


