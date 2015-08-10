package Actions;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

import Pathing.ToObject;

public class Interact {
	public static boolean InteractWithObject(ClientContext ctx,String objectName, String action, int helper)
	{
		if(helper!=0)helper++;
		final GameObject gameObject = ctx.objects.select().name(objectName).nearest().poll();
		if(gameObject.valid())
		{
			if(gameObject.inViewport())
			{
				//object is valid and in viewport. We're also not doing anything
				Camera.Focus.CheckFocus(ctx, gameObject);
				gameObject.interact(action);
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
				InteractWithObject(ctx, objectName, action,helper);
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
	public static boolean InteractWithObject(ClientContext ctx,GameObject gameObject, String action, int helper)
	{
		if(helper!=0)helper++;
		if(gameObject.valid())
		{
			if(gameObject.inViewport())
			{
				//object is valid and in viewport. We're also not doing anything
				gameObject.interact(action);
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
	public static boolean InteractWithItem(ClientContext ctx, String item, String action, int helper)
	{
		return false;
		
	}
	public static boolean InteractWithNPC(ClientContext ctx, String npcName, String action, int helper)
	{
		if(helper!=0)helper++;
		final Npc npc = ctx.npcs.select().name(npcName).nearest().poll();
		
		if(npc.valid())
		{
			if(npc.inViewport())
			{
				//npc is valid and in viewport
				npc.interact(action);
				return true;
			}
			else
			{
				//npc is valid and not in viewport
				if(helper%2==0)
				{
					Camera.Focus.OnNpc(ctx, npcName);
					
				}
				else
				{
					ToObject.WalkToObject(ctx, npc);
				}
				Utility.Sleep.WaitRandomTime(500, 1500);
				
				InteractWithNPC(ctx,npcName,action,helper);
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
	public static boolean InteractWithNPC(ClientContext ctx, Npc npc, String action, int helper)
	{
		if(helper!=0)helper++;
		
		if(npc.valid())
		{
			if(npc.inViewport())
			{
				//npc is valid and in viewport
				npc.interact(action);
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

}
