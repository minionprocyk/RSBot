package Pathing;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

public class ToObject {
	public static void WalkToObject(ClientContext ctx, String GameObject, Tile... path)
	{
		//walk the path
		Traverse.TraversePath(ctx, path);
		
		//get the game object reference
		final GameObject gameObject = ctx.objects.select().name(GameObject).nearest().poll();
		//setup the camera and inch to the object
		ctx.camera.turnTo(gameObject);
		ctx.movement.step(gameObject);
		
		//wait until we're close to the object
		do
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(ctx.players.local().tile().distanceTo(gameObject) > 4);
	
		//done
		return;
	}
	public static void WalkToObject(ClientContext ctx, String GameObject)
	{
		//setup the camera and inch to the object
		final GameObject gameObject = ctx.objects.select().name(GameObject).nearest().poll();
		ctx.camera.turnTo(gameObject);
		ctx.movement.step(gameObject);
				
		//wait until we're close to the object
		do
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(ctx.players.local().tile().distanceTo(gameObject) > 4);
	
		//done
		return;
	}
	public static void WalkToObject(ClientContext ctx, GameObject gameObject)
	{
		//setup the camera and inch to the object
		ctx.camera.turnTo(gameObject);
		ctx.movement.step(gameObject);
				
		//wait until we're close to the object
		do
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(ctx.players.local().tile().distanceTo(gameObject) > 4);
	
		//done
		return;
	}
	public static void WalkToObject(ClientContext ctx, GameObject gameObject, Tile... path)
	{
		//walk the path
		Traverse.TraversePath(ctx, path);
		
		//setup the camera and inch to the object
		ctx.camera.turnTo(gameObject);
		ctx.movement.step(gameObject);
		
		//wait until we're close to the object
		do
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(ctx.players.local().tile().distanceTo(gameObject) > 4);
	
		//done
		return;
	}
	public static void WalkToObject(ClientContext ctx, Npc npc)
	{		
		//setup the camera and inch to the object
		ctx.camera.turnTo(npc);
		ctx.movement.step(npc);
		
		//wait until we're close to the object
		do
		{
			try {					
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(ctx.players.local().tile().distanceTo(npc) > 4);
	
		//done
		return;
	}
	
}
