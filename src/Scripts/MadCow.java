package Scripts;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

import Constants.Animation;
import Constants.Interact;
import Constants.ItemName;
import Constants.NpcName;
import Constants.ObjectName;
import Constants.WidgetId;
import Exceptions.NoValidObjectsException;
import Pathing.ToObject;
import Pathing.Traverse;

@Manifest(name = "Cow Killing", description = "kill cows and shit", properties = "client=6; topic=0;")

public class MadCow extends PollingScript<ClientContext> implements MessageListener{
	State previousState=null;
	State currentState=null;
	boolean interacted=false;
	boolean init=true;
	Area bankArea = new Area(new Tile(3177, 3290, 0),new Tile(3160, 3271, 0));
	Area cowArea = new Area(new Tile(3156, 3316,0), new Tile(3193, 3330, 0));

	Tile tcfb1 = new Tile(3168, 3296, 0);
	Tile tcfb2 = new Tile(3176, 3313, 0);
	
	Tile[] fromBankToCows = new Tile[]{tcfb1, tcfb2};
	public void poll() {
		if(init)
		{
			//i can initialize stuff here
			//like .... new ChatEngine().run();
		}
		switch(currentState=getState())
		{
		case kill:
			System.out.println("here i go killing again");
			//find a cow and kill it
			final Npc npc = ctx.npcs.select().name(NpcName.NULL).nearest().poll();
			if(!npc.valid())
			{
				Traverse.TraversePath(ctx, cowArea.getRandomTile());
				return;
			}
			interacted = Actions.Interact.InteractWithNPC(ctx, NpcName.NULL, Interact.ATTACK);
			
			//wait until the cow is dead and loot it
			if(interacted)
			{
				//wait for this cow to not exist or for 30 seconds
				long now = System.currentTimeMillis();
				do
				{
					Utility.Sleep.Wait(100);
				}while(npc.valid() && (Math.abs(now-System.currentTimeMillis())) < 1000*30);
				System.out.println("Cow dead.");
				ctx.groundItems.select().name(ObjectName.RAW_BEEF).nearest().poll().interact("Take");
				Utility.Sleep.WaitRandomTime(1000, 3000);
				if(ctx.widgets.component(1622, 14).valid())ctx.widgets.component(1622, 14).click();//loot all
			}
			break;
		case usebags:
			//if we have bones bury them
			System.out.println("Using stuff in our bags");
			if(LocalPlayer.Backpack.Has(ctx, ObjectName.BONES))
			{
				//bury the bones
				LocalPlayer.Backpack.Use(ctx, ObjectName.BONES, Interact.BURY);
			}
			else if(LocalPlayer.Backpack.Has(ctx, ObjectName.BURNT_MEAT))
			{
				LocalPlayer.Backpack.Use(ctx, ObjectName.BURNT_MEAT, Interact.DROP);
			}
			else if(LocalPlayer.Backpack.Has(ctx, ObjectName.RAW_BEEF))
			{
				System.out.println("We have raw beef");
				//check if we have logs 
				if(ctx.objects.select().name(ObjectName.FIRE).nearest().poll().valid() &&
				ctx.players.local().tile().distanceTo(ctx.objects.select().name(ObjectName.FIRE).nearest().poll()) < 10)
				{
					System.out.println("There's a fire we can use");
					//theres a fire around to use
					LocalPlayer.Backpack.Use(ctx, ObjectName.RAW_BEEF, Interact.USE);
					Actions.Interact.InteractWithObject(ctx, ObjectName.FIRE, Interact.USE);
					Utility.Sleep.WaitRandomTime(1000, 2000);
					if(ctx.widgets.component(WidgetId.CHOOSE_A_TOOL, WidgetId.CHOOSE_A_TOOL_COOK).component(1).valid())
						ctx.widgets.component(WidgetId.CHOOSE_A_TOOL, WidgetId.CHOOSE_A_TOOL_COOK).component(1).click();
					if(ctx.widgets.component(1370, 20).valid())ctx.widgets.component(1370, 20).click();
					do
					{
						Utility.Sleep.WaitRandomTime(1000, 2000);
					}while(LocalPlayer.Animation.CheckPlayerIdle(ctx)==Animation.PLAYER_NOT_IDLE);
				}
				else
				{
					System.out.println("No fire we can use. Lets make our own");
					//no fire around. we need to make our own
					if(LocalPlayer.Backpack.Has(ctx, ObjectName.LOGS))
					{
						System.out.println("Making a fire");
						LocalPlayer.Backpack.Use(ctx, ObjectName.LOGS, Interact.LIGHT);						
						do
						{
							Utility.Sleep.Wait(100);
						}while(LocalPlayer.Animation.CheckPlayerIdle(ctx) == Animation.PLAYER_NOT_IDLE);
					}
					else
					{
						System.out.println("Chopping some trees");
						//chop a tree for some wood
						long now = System.currentTimeMillis();
						final GameObject tree = GameObjects.Select.WithinArea(ctx, cowArea, ObjectName.TREE);
						Actions.Interact.InteractWithObject(ctx, tree, Interact.CHOP);
						do
						{
							Utility.Sleep.Wait(100);
						}while(tree.valid()&& (Math.abs(now-System.currentTimeMillis())) < 1000*60);
					}
				}
			}
			break;
		case deposit:
			System.out.println("Depositing at bank");
			Actions.Interact.InteractWithObject(ctx, ObjectName.BANK_CHEST, Interact.USE);
			ctx.bank.depositInventory();
			ctx.bank.close();
			break;
		case walk_to_bank:
			System.out.println("Walking to bank");
			new Thread(new Runnable() {
				public void run() {
					while(currentState==State.walk_to_bank)
					{
						if(ctx.players.local().inMotion())
						{
							if(LocalPlayer.Backpack.Has(ctx, ObjectName.BONES))
							{
								//bury the bones
								LocalPlayer.Backpack.Use(ctx, ObjectName.BONES, Interact.BURY);
							}
							else if(LocalPlayer.Backpack.Has(ctx, ObjectName.BURNT_MEAT))
							{
								//drop the meat
								LocalPlayer.Backpack.Use(ctx, ObjectName.BURNT_MEAT, Interact.DROP);
							}
							Utility.Sleep.WaitRandomTime(500, 2000);
						}
					}
				}
			}).start();
			Traverse.TraversePathInReverse(ctx, fromBankToCows);
			ToObject.WalkToObject(ctx, ObjectName.BANK_CHEST,bankArea.getRandomTile());
			break;
		case walk_to_cows:
			System.out.println("Walking to cows");
			Traverse.TraversePath(ctx, fromBankToCows);
			break;
		}
		previousState=currentState;
	}
	
	public State getState()
	{
		if(LocalPlayer.Backpack.isFull(ctx) && !LocalPlayer.Location.Within(ctx, bankArea))
		{
			return State.walk_to_bank;
		}
		else if(LocalPlayer.Backpack.hasStuff(ctx) && LocalPlayer.Location.Within(ctx, bankArea))
		{
			return State.deposit;
		}
		else if(LocalPlayer.Location.Within(ctx, bankArea) && !LocalPlayer.Backpack.isFull(ctx))
		{
			return State.walk_to_cows;
		}
		else
		{
			//this adds a little bit of randomness and usually will only bury bones randomly 
			if(LocalPlayer.Backpack.Count(ctx, ItemName.RAW_BEEF) > Random.nextInt(3, 5))
			{
				return State.usebags;
			}
			else
			{
				return State.kill;
			}
		}
	}
	
	public enum State
	{
		kill, usebags, walk_to_bank, walk_to_cows, deposit
	}

	
	public void messaged(MessageEvent arg0) {
		
	}

}