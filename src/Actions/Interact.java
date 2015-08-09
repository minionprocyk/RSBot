package Actions;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class Interact {
	public static boolean InteractWithObject(ClientContext ctx,String objectName, String action)
	{
		final GameObject gameObject = ctx.objects.select().name(objectName).nearest().poll();
		if(gameObject.valid())
		{
			if(gameObject.inViewport())
			{
				//object is valid and in viewport. interact with it
				gameObject.interact(action);
			}
			else
			{
				//object is valid and not in viewport. try to put it on screen
				ctx.camera.turnTo(gameObject);
				ctx.movement.step(gameObject);
				
				Utility.Sleep.WaitRandomTime(500, 1000);
				InteractWithObject(ctx,objectName,action);
				
			}
		}
		else
		{
			//game object not valid. nothing we can do
			System.out.println("InteractWithObject failed to find a valid game object");
			return false;
		}
		return true;
	}
}
