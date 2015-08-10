package Camera;

import java.awt.Point;
import java.awt.Rectangle;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

public class Focus {
	//high pitch raises camera to look at floor
	//yaw of 0 is north, >0 is looking more west and <0 is looking more east
	public static void OnRandomObject(ClientContext ctx)
	{
		//focus on a random object that is in the area
		
		//get the number of objects around
		int numObjects = ctx.objects.select().size();
	
		//pick a random index value
		int randomValue = Random.nextInt(0, numObjects-1);
		
		//use the index value to query an object
		int i=0;
		for(GameObject go: ctx.objects)
		{
			if(i!=randomValue)
			{
				i++;
			}
			else
			{
				//turn the camera to face that object
				ctx.camera.turnTo(go);
				
				//change the pitch 20% of the time
				if(Random.nextInt(0, 100)>80)ctx.camera.pitch(Random.nextInt(30, 60));
				break;
			}
		}
	}
	public static void OnObject(ClientContext ctx, String objectName)
	{
		final GameObject gameObject = ctx.objects.select().name(objectName).nearest().poll();
		Camera.Focus.OnObject(ctx, gameObject);
	}
	public static void OnObject(ClientContext ctx, GameObject gameObject)
	{
		ctx.camera.pitch(Random.nextInt(70,90));
		ctx.camera.turnTo(gameObject);
	}
	public static void OnNpc(ClientContext ctx, String npcName)
	{
		final Npc npc = ctx.npcs.select().name(npcName).nearest().poll();
		ctx.camera.pitch(Random.nextInt(70,90));
		ctx.camera.turnTo(npc);
	}



}
