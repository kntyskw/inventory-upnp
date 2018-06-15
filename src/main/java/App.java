import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.yasukawa.inventory.upnp.InventoryUpnpRegistryListener;
import org.yasukawa.inventory.upnp.UpnpController;

public class App {

	public static void main(String[] args) throws Exception {

		UpnpService upnpService = new UpnpServiceImpl();
		UpnpController.initialize(upnpService);
		upnpService.getRegistry().addListener(new InventoryUpnpRegistryListener(upnpService));
		upnpService.getControlPoint().search(new STAllHeader());
	}
}
