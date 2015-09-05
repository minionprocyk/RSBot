
package Scripts;

import java.awt.Graphics;

import org.powerbot.script.Area;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

import Constants.Animation;
import Constants.Interact;
import Constants.ItemId;
import Constants.ItemName;
import Constants.NpcName;
import Constants.ObjectName;
import Constants.WidgetId;
import Engines.FightingEngine;
import Engines.StatisticsEngine;
import Pathing.ToObject;
import Pathing.Traverse;

@Manifest(name = "Cow Killing", description = "starts next to bank in lumbridge or in cow farm", properties = "client=6; topic=0;")

public class MadCow extends PollingScript<ClientContext> implements PaintListener{
	State previousState=null;
	State currentState=null;

	boolean interacted=false;
	Area bankArea = new Area(new Tile(3177, 3290, 0),new Tile(3160, 3271, 0));
	Area cowArea = new Area(new Tile(3156, 3316,0), new Tile(3193, 3330, 0));

	Tile tcfb1 = new Tile(3168, 3296, 0);
	Tile tcfb2 = new Tile(3176, 3313, 0);
	
	Tile[] fromBankToCows = new Tile[]{tcfb1, tcfb2};
	int cowHides=0;
	int bonesBuried=0;
	int meatsCooked=0;
	int lastMeatCount=0;
	int lastHideCount=0;
	private volatile boolean running=true;
	public void start()
	{
		StatisticsEngine.GetInstance().SetContext(ctx).SetName("Cow Killing").build().start();
	}
	public void stop()
	{
		System.out.println("Script is stopping");
	}
	public void poll() {
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
			FightingEngine.GetInstance().SetContext(ctx).SetFightingArea(cowArea).SetLoot(ObjectName.RAW_BEEF, ObjectName.COWHIDE).
										SetFood(ItemId.COOKED_MEAT).SetTargets(NpcName.NULL).build().run();
			//stats during the run
			if(LocalPlayer.Backpack.Count(ctx, ItemId.COWHIDE) != lastHideCount)
			{
				lastHideCount = LocalPlayer.Backpack.Count(ctx,ItemId.COWHIDE);
				if(lastHideCount!=0)cowHides++;//don't add when depositing hides
			}
			break;
		case usebags:
			//if we have bones bury them
			System.out.println("Using stuff in our bags");
			if(LocalPlayer.Backpack.Has(ctx, ObjectName.BONES))
			{
				//bury the bones
				bonesBuried++;
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
					running=true;
					new Thread(new Runnable() {
						public void run() {
							Utility.Sleep.Wait(1000);
							while(running)
							{
								if(LocalPlayer.Backpack.Count(ctx,ItemId.COOKED_MEAT) != lastMeatCount)
								{
									lastMeatCount = LocalPlayer.Backpack.Count(ctx, ItemId.COOKED_MEAT);
									if(lastMeatCount!=0)meatsCooked++;
								}
								Utility.Sleep.Wait(500);
							}
						}
					}).start();
					do
					{
						Utility.Sleep.WaitRandomTime(1000, 2000);
					}while(LocalPlayer.Animation.CheckPlayerIdle(ctx)==Animation.PLAYER_NOT_IDLE);
					running=false;
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
						final GameObject tree = GameObjects.Select.WithinArea(ctx, cowArea, ObjectName.TREE).get(0);
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
			Traverse.TraversePathInReverse(ctx, fromBankToCows);
			ToObject.WalkToObject(ctx, ObjectName.BANK_CHEST);
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
		if(LocalPlayer.Backpack.Count(ctx)>26 && !LocalPlayer.Location.Within(ctx, bankArea))
		{
			if(LocalPlayer.Backpack.Has(ctx, ItemId.BONES,ItemId.BURNT_MEAT))return State.usebags;
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

	public void repaint(Graphics graphics) {
		StatisticsEngine.GetInstance().SetStringsToDraw(
				"Combat Levels Gained = "+StatisticsEngine.GetInstance().getCombatLevelsGained(),
				"Cow Hides Collected = "+cowHides,
				"Bones buried = "+bonesBuried,
				"Meats cooked = "+meatsCooked)
				.repaint(graphics);
	}
}