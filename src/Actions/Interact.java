package Actions;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Npc;

import Constants.Animation;
import Pathing.ToObject;

public class Interact {
	
	public static boolean InteractWithObject(ClientContext ctx,GameObject gameObject, String action, int helper)
	{
		if(helper!=0)helper++;
		if(gameObject.valid())
		{
			if(gameObject.inViewport())
			{
				//object is valid and in viewpor.
				if(ctx.players.local().tile().distanceTo(gameObject) > 4)
				{
					//distance to the object is greater than 4 tiles. we should move towards it 
					ToObject.WalkToObject(ctx, gameObject);
					
				}
				//check that the camera's focused viewport is visible of the object
				Camera.Focus.OnObject(ctx, gameObject);
				
				gameObject.interact(action);
				
				//wait for the player to not be doing things
				//check if were running
				do
				{
					Utility.Sleep.WaitRandomTime(1000, 3000);
					System.out.println("in motion");
				}
				while(ctx.players.local().inMotion());
				
				//check if were doing some sort of animation
				do
				{
					Utility.Sleep.WaitRandomTime(1000, 3000);
				}while(Player.Animation.CheckPlayerAnimation(ctx) == Animation.PLAYER_NOT_IDLE);
				return true;
			}
			else
			{
				//object is valid and not in viewport. try to put it on screen
				if(helper%2==0)
				{
					Camera.Focus.OnObject(ctx, gameObject);
				}else
				{
					Pathing.ToObject.WalkToObject(ctx, gameObject);
				}
				
				Utility.Sleep.WaitRandomTime(500, 1500);
				
				InteractWithObject(ctx,gameObject,action,helper);
				return true;
			}
		}
		else
		{
			//game object not valid. nothing we can do
			System.out.println("InteractWithObject failed to find a valid game object");
			return false;
		}
	}
	public static boolean InteractWithObject(ClientContext ctx,String objectName, String action, int helper)
	{
		final GameObject gameObject = ctx.objects.select().name(objectName).nearest().poll();
		InteractWithObject(ctx,gameObject,action,0);
		return true;
	}
	public static boolean InteractWithItem(ClientContext ctx, Item item, String action, int helper)
	{
		return false;
		
	}
	public static boolean InteractWithNPC(ClientContext ctx, Npc npc, String action, int helper)
	{
		if(helper!=0)helper++;
		
		if(npc.valid())
		{
			if(npc.inViewport())
			{
				//npc is valid and in viewport
				if(ctx.players.local().tile().distanceTo(npc) > 4)
				{
					//distance to the npc is greater than 4 tiles. we should move towards it 
					ToObject.WalkToObject(ctx, npc);
					
				}
				//check that the camera's focused viewport is visible of the object
				Camera.Focus.OnNpc(ctx, npc.name());
				
				npc.interact(action);
				
				//wait for the player to not be doing things
				//check if were running
				do
				{
					Utility.Sleep.WaitRandomTime(1000, 3000);
				}
				while(ctx.players.local().inMotion());
				
				//check if were doing some sort of animation
				do
				{
					Utility.Sleep.WaitRandomTime(1000, 3000);
				}while(Player.Animation.CheckPlayerAnimation(ctx) == Animation.PLAYER_NOT_IDLE);
				return true;
			}
			else
			{
				//npc is valid and not in viewport
				if(helper%2==0)
				{
					Camera.Focus.OnNpc(ctx, npc.name());
					
				}
				else
				{
					ToObject.WalkToObject(ctx, npc);
				}
				Utility.Sleep.WaitRandomTime(500, 1500);
				
				InteractWithNPC(ctx,npc,action,helper);
				return true;
				
			}
		}
		else
		{
			//npc is not valid...durp
			System.out.println("Could not find a valid npc");
			return false;
		}
	}
	public static boolean InteractWithNPC(ClientContext ctx, String npcName, String action, int helper)
	{
		final Npc npc = ctx.npcs.select().name(npcName).nearest().poll();
		InteractWithNPC(ctx,npc,action,0);
		return true;
		
	}
	

}
