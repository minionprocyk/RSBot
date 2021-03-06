package procyk.industries.rt4.actions;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import procyk.industries.rt4.camera.Focus;
import procyk.industries.rt4.manager.AvoidNpc;
import procyk.industries.rt4.manager.AvoidNpcsManager;
import procyk.industries.rt4.manager.AvoidObject;
import procyk.industries.rt4.manager.AvoidObjectsManager;
import procyk.industries.rt4.pathing.ToObject;
import procyk.industries.rt4.pathing.Traverse;
import procyk.industries.rt4.utility.Sleep;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.WidgetIdConstantsRT6;

public class Interact {
	private static final int highDistance=15;
	private static final int lowDistance=4;
	private static final int avoidObjectThreshold=5;
	private static int adjustPitch=3;
	
	
	private static boolean InteractWithObject(ClientContext ctx,GameObject gameObject, String action, int helper)
	{
		if(helper!=0)helper++;
		Component widget = ctx.widgets.select().id(WidgetIdConstantsRT6.EXAMINE_PORTRAIT).poll()
				.component(WidgetIdConstantsRT6.EXAMINE_PORTRAIT_TITLE)
				.component(WidgetIdConstantsRT6.EXAMINE_PORTRAIT_TITLE_TEXT);
		if(widget!= null && widget.valid() && widget.visible() && !widget.text().isEmpty())
		{
			//close it
			Widgets.Click(ctx, WidgetIdConstantsRT6.EXAMINE_PORTRAIT, WidgetIdConstantsRT6.EXAMINE_PORTRAIT_CLOSE, WidgetIdConstantsRT6.EXAMINE_PORTRAIT_CLOSE_BUTTON);
		}	
		if(helper>avoidObjectThreshold)AvoidObjectsManager.AddAvoidableObject(new AvoidObject(gameObject));
		if(gameObject!=null && gameObject.valid())
		{
			if(gameObject.inViewport())
			{
				//object is valid and in viewport.
				if(ctx.players.local().tile().distanceTo(gameObject) > highDistance)
				{
					//distance to the object is greater than 4 tiles. we should move towards it 
					System.out.println("Were a little far from the object. Lets move in");
					ToObject.WalkToObject(ctx, gameObject);
					
				}
				//check that the camera's focused viewport is visible of the object
				//Camera.Focus.OnObject(ctx, gameObject);
				System.out.println("Interacting with "+gameObject.name());
				gameObject.interact(action);
				//wait for the player to not be doing things
				//check if were running
				Sleep.WhileRunning(ctx);
				
				//check if were doing some sort of animation, unless attacking
				Sleep.WhileNotIdle(ctx);
				
				//if were not right next to the object we were supposed to interact with. we have to try again
				if(ctx.players.local().tile().distanceTo(gameObject) > lowDistance)
				{
					System.out.println("We didn't end up next to the object we were trying to use. Trying again");
					helper++;
					InteractWithObject(ctx, gameObject, action, helper);
				}
				
				return true;
			}
			else
			{
				//object is valid and not in viewport. try to put it on screen
				if(helper%2==0)
				{
					System.out.println("Object not in viewport. Adjusting Camera");
					Focus.OnObject(ctx, gameObject);
				}else
				{
					System.out.println("Object not in viewport. Walking to Object");
					ToObject.WalkToObject(ctx, gameObject);
				}
				if(helper >= adjustPitch)Focus.AdjustPitch(ctx);
				if(helper > adjustPitch+1)Traverse.TraversePath(ctx, gameObject.tile());
				helper++;
				InteractWithObject(ctx,gameObject,action,helper);
				return true;
			}
		}
		else
		{
			throw new NullPointerException();
			//game object not valid. nothing we can do
		}
	}
	public static boolean InteractWithObject(ClientContext ctx,GameObject gameObject, String action)
	{
		return InteractWithObject(ctx,gameObject,action,0);
	}
	public static boolean InteractWithObject(ClientContext ctx,String objectName, String action)
	{
		final GameObject gameObject = ctx.objects.select().name(objectName).nearest().poll();
		InteractWithObject(ctx,gameObject,action,0);
		return true;
	}
	public static boolean InteractWithNPC(ClientContext ctx, Npc npc, String action)
	{
		return InteractWithNPC(ctx,npc,action,0);
	}
	private static boolean InteractWithNPC(ClientContext ctx, Npc npc, String action, int helper)
	{
		if(helper!=0)helper++;
		if(helper>avoidObjectThreshold)AvoidNpcsManager.AddAvoidableNpc(new AvoidNpc(npc));
		if(npc.valid())
		{
			if(npc !=null && npc.inViewport())
			{
				if(ctx.players.local().tile().distanceTo(npc) > highDistance)
				{
					//npc is valid and in viewport
					System.out.println("Not very close to NPC. Walking");
					ToObject.WalkToObject(ctx, npc);
				}
				//check that the camera's focused viewport is visible of the object
				//Camera.Focus.OnNpc(ctx, npc.name());
				System.out.println("Interacting with "+npc.name());
				npc.interact(action);
				//wait for the player to not be doing things
				//check if were running
				Sleep.WhileRunning(ctx);
				
				//check if were doing some sort of animation
				if(!action.equals(InteractConstants.ATTACK))
				{
					Sleep.WhileNotIdle(ctx);	
					
					//if were not right next to the object we were supposed to interact with. we have to try again
					if(ctx.players.local().tile().distanceTo(npc) > lowDistance)
					{
						System.out.println("We didn't end up next to the npc we were trying to use. Trying again");
						helper++;
						InteractWithNPC(ctx, npc, action, helper);
					}
				}
				
				
				return true;
			}
			else
			{
				//npc is valid and not in viewport
				if(helper%2==0)
				{
					System.out.println("NPC not in viewport. Adjusting Camera");
					Focus.OnNpc(ctx, npc.name());
				}
				else
				{
					System.out.println("NPC not in viewport. Walking to NPC");
					ToObject.WalkToObject(ctx, npc);
				}
				if(helper > adjustPitch)Focus.AdjustPitch(ctx);
				if(helper > adjustPitch+1)Traverse.TraversePath(ctx, npc.tile());
				helper++;
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
	public static boolean InteractWithNPC(ClientContext ctx, String npcName, String action)
	{
		final Npc npc = ctx.npcs.select().name(npcName).nearest().poll();
		InteractWithNPC(ctx,npc,action,0);
		return true;	
	}
	public static boolean InteractWithNPC(ClientContext ctx, int[] npcId, String action)
	{
		final Npc npc = ctx.npcs.select().id(npcId).nearest().poll();
		InteractWithNPC(ctx,npc,action,0);
		return true;
	}
	public static boolean InteractWithNPC(ClientContext ctx, int npcId, String action)
	{
		final Npc npc = ctx.npcs.select().id(npcId).nearest().poll();
		InteractWithNPC(ctx,npc,action,0);
		return true;
	}
}
