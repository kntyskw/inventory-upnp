package models;
import io.soracom.inventory.agent.core.lwm2m.*;
import java.util.Date;
import org.eclipse.leshan.core.node.ObjectLink;

/**
 * LwM2M device object model for UPnP AV Transport service.
 **/
@LWM2MObject(objectId = 30002, name = "UPnP AV Transport Service", multiple = true)
public class UPnPAVTransportServiceObject extends AnnotatedLwM2mInstanceEnabler {

	/**
	 * Sets the AV transport URI.
	 **/
	@Resource(resourceId = 2401, operation = Operation.Execute)
	public void executeSetAVTransportURI(String executeParameter)	{
		throw LwM2mInstanceResponseException.notFound();
	}

	/**
	 * Stops playback.
	 **/
	@Resource(resourceId = 2409, operation = Operation.Execute)
	public void executeStop(String executeParameter)	{
		throw LwM2mInstanceResponseException.notFound();
	}

	/**
	 * Starts playback.
	 **/
	@Resource(resourceId = 2410, operation = Operation.Execute)
	public void executePlay(String executeParameter)	{
		throw LwM2mInstanceResponseException.notFound();
	}

	/**
	 * Pauses playback.
	 **/
	@Resource(resourceId = 2411, operation = Operation.Execute)
	public void executePause(String executeParameter)	{
		throw LwM2mInstanceResponseException.notFound();
	}
}
