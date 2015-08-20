package Pathing;

import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class ToObject {
	private static void WalkToObject(ClientContext ctx, Locatable locatable, int helper)
	{
		if(locatable != null)
		{
			Camera.Focus.OnObject(ctx, locatable);
			ctx.movement.step(locatable);
			//wait until we're close to the object
			Utility.Sleep.WhileRunning(ctx);
			
			if(ctx.players.local().tile().distanceTo(locatable) > 4 && helper < 3)
			{
				helper++;
				WalkToObject(ctx, locatable,helper);
			}
			else
			{
				return;
			}
		}
		else
		{
			System.out.println("WalkToObject: The object doesnt exist. ");
		}
	}
	public static void WalkToObject(ClientContext ctx, Locatable locatable)
	{
		WalkToObject(ctx,locatable, 0);
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
