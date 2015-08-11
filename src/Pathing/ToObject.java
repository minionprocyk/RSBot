package Pathing;

import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class ToObject {
	public static void WalkToObject(ClientContext ctx, Locatable locateable)
	{
		ctx.camera.turnTo(locateable);
		ctx.movement.step(locateable);
		//wait until we're close to the object
		do
		{
			try {					
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(ctx.players.local().tile().distanceTo(locateable) > 4);
			
		//done
		return;
	}
	public static void WalkToObject(ClientContext ctx, String GameObject)
	{
		//setup the camera and inch to the object
		final GameObject gameObject = ctx.objects.select().name(GameObject).nearest().poll();
		ToObject.WalkToObject(ctx, gameObject);
	}
	public static void WalkToObject(ClientContext ctx, GameObject gameObject, Tile... path)
	{
		//walk the path
		Traverse.TraversePath(ctx, path);
		
		ToObject.WalkToObject(ctx, gameObject);
	}
	public static void WalkToObject(ClientContext ctx, String GameObject, Tile... path)
	{
		//walk the path
		Traverse.TraversePath(ctx, path);
		
		//get the game object reference
		final GameObject gameObject = ctx.objects.select().name(GameObject).nearest().poll();
		ToObject.WalkToObject(ctx, gameObject);
	}
}
