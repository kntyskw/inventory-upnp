package models;
import io.soracom.inventory.agent.core.lwm2m.*;
import java.util.Date;
import org.eclipse.leshan.core.node.ObjectLink;

/**
 * LwM2M device object model for UPnP Rendering Control service.
 **/
@LWM2MObject(objectId = 30001, name = "UPnP Rendering Control Service", multiple = true)
public class UPnPRenderingControlServiceObject extends AnnotatedLwM2mInstanceEnabler {

	/**
	 * Mute status of the renderer
	 **/
	@Resource(resourceId = 2215, operation = Operation.Read)
	public Boolean readMute()	{
		throw LwM2mInstanceResponseException.notFound();
	}

	/**
	 * Current audio volume of the renderer
	 **/
	@Resource(resourceId = 2216, operation = Operation.Read)
	public Integer readVolume()	{
		throw LwM2mInstanceResponseException.notFound();
	}

	/**
	 * Sets the renderer audio volume.
	 **/
	@Resource(resourceId = 2430, operation = Operation.Execute)
	public void executeSetVolume(String executeParameter)	{
		throw LwM2mInstanceResponseException.notFound();
	}
}
