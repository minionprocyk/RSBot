package procyk.industries.rt6.pathing;

import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import procyk.industries.rt6.camera.Focus;
import procyk.industries.rt6.utility.Sleep;

public class ToObject {
	private static void WalkToObject(ClientContext ctx, Locatable locatable, int helper)
	{
		if(locatable != null)
		{
			Focus.OnObject(ctx, locatable);
			ctx.movement.step(locatable);
			//wait until we're close to the object
			Sleep.WhileRunning(ctx);
			
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
