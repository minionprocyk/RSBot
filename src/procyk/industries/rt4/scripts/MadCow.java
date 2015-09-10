
package procyk.industries.rt4.scripts;

import java.awt.Graphics;

import org.powerbot.script.Area;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import procyk.industries.rt4.actions.Interact;
import procyk.industries.rt4.engines.FightingEngine;
import procyk.industries.rt4.engines.StatisticsEngine;
import procyk.industries.rt4.gameobjects.Select;
import procyk.industries.rt4.localplayer.Animation;
import procyk.industries.rt4.localplayer.Backpack;
import procyk.industries.rt4.localplayer.Location;
import procyk.industries.rt4.pathing.ToObject;
import procyk.industries.rt4.pathing.Traverse;
import procyk.industries.rt4.utility.Sleep;
import procyk.industries.shared.constants.AnimationConstants;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.ItemIdConstants;
import procyk.industries.shared.constants.ItemNameConstants;
import procyk.industries.shared.constants.NpcNameConstants;
import procyk.industries.shared.constants.ObjectNameConstants;
import procyk.industries.shared.constants.WidgetIdConstantsRT6;

@Manifest(name = "Cow Killing", description = "starts next to bank in lumbridge or in cow farm", properties = "client=4; topic=0;")

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
			final Npc npc = ctx.npcs.select().name(NpcNameConstants.NULL).nearest().poll();
			if(!npc.valid())
			{
				Traverse.TraversePath(ctx, cowArea.getRandomTile());
				return;
			}
			FightingEngine.GetInstance().SetContext(ctx).SetFightingArea(cowArea).SetLoot(ObjectNameConstants.RAW_BEEF, ObjectNameConstants.COWHIDE).
										SetFood(ItemIdConstants.COOKED_MEAT).SetTargets(NpcNameConstants.NULL).build().run();
			//stats during the run
			if(Backpack.Count(ctx, ItemIdConstants.COWHIDE) != lastHideCount)
			{
				lastHideCount = Backpack.Count(ctx,ItemIdConstants.COWHIDE);
				if(lastHideCount!=0)cowHides++;//don't add when depositing hides
			}
			break;
		case usebags:
			//if we have bones bury them
			System.out.println("Using stuff in our bags");
			if(Backpack.Has(ctx, ObjectNameConstants.BONES))
			{
				//bury the bones
				bonesBuried++;
				Backpack.Use(ctx, ObjectNameConstants.BONES, InteractConstants.BURY);
			}
			else if(Backpack.Has(ctx, ObjectNameConstants.BURNT_MEAT))
			{
				Backpack.Use(ctx, ObjectNameConstants.BURNT_MEAT, InteractConstants.DROP);
			}
			else if(Backpack.Has(ctx, ObjectNameConstants.RAW_BEEF))
			{
				System.out.println("We have raw beef");
				//check if we have logs 
				if(ctx.objects.select().name(ObjectNameConstants.FIRE).nearest().poll().valid() &&
				ctx.players.local().tile().distanceTo(ctx.objects.select().name(ObjectNameConstants.FIRE).nearest().poll()) < 10)
				{
					System.out.println("There's a fire we can use");
					//theres a fire around to use
					Backpack.Use(ctx, ObjectNameConstants.RAW_BEEF, InteractConstants.USE);
					try
					{
						Interact.InteractWithObject(ctx, ObjectNameConstants.FIRE, InteractConstants.USE);
					}
					catch(NullPointerException e)
					{
						return;
					}
					Sleep.WaitRandomTime(1000, 2000);
					if(ctx.widgets.component(WidgetIdConstantsRT6.CHOOSE_A_TOOL, WidgetIdConstantsRT6.CHOOSE_A_TOOL_COOK).component(1).valid())
						ctx.widgets.component(WidgetIdConstantsRT6.CHOOSE_A_TOOL, WidgetIdConstantsRT6.CHOOSE_A_TOOL_COOK).component(1).click();
					if(ctx.widgets.component(1370, 20).valid())ctx.widgets.component(1370, 20).click();
					running=true;
					new Thread(new Runnable() {
						public void run() {
							Sleep.Wait(1000);
							while(running)
							{
								if(Backpack.Count(ctx,ItemIdConstants.COOKED_MEAT) != lastMeatCount)
								{
									lastMeatCount = Backpack.Count(ctx, ItemIdConstants.COOKED_MEAT);
									if(lastMeatCount!=0)meatsCooked++;
								}
								Sleep.Wait(500);
							}
						}
					}).start();
					do
					{
						Sleep.WaitRandomTime(1000, 2000);
					}while(Animation.CheckPlayerIdle(ctx)==AnimationConstants.PLAYER_NOT_IDLE);
					running=false;
				}
				else
				{
					System.out.println("No fire we can use. Lets make our own");
					//no fire around. we need to make our own
					if(Backpack.Has(ctx, ObjectNameConstants.LOGS))
					{
						System.out.println("Making a fire");
						Traverse.TraversePath(ctx, cowArea.getRandomTile());
						Backpack.Use(ctx, ObjectNameConstants.LOGS, InteractConstants.LIGHT);						
						do
						{
							Sleep.Wait(100);
						}while(Animation.CheckPlayerIdle(ctx) == AnimationConstants.PLAYER_NOT_IDLE);
					}
					else
					{
						System.out.println("Chopping some trees");
						//chop a tree for some wood
						long now = System.currentTimeMillis();
						final GameObject tree = Select.WithinArea(ctx, cowArea, ObjectNameConstants.TREE).get(0);
						Interact.InteractWithObject(ctx, tree, InteractConstants.CHOP);
						do
						{
							Sleep.Wait(100);
						}while(tree.valid()&& (Math.abs(now-System.currentTimeMillis())) < 1000*60);
					}
				}
			}
			break;
		case deposit:
			System.out.println("Depositing at bank");
			Interact.InteractWithObject(ctx, ObjectNameConstants.BANK_CHEST, InteractConstants.USE);
			ctx.bank.depositInventory();
			ctx.bank.close();
			break;
		case walk_to_bank:
			System.out.println("Walking to bank");
			Traverse.TraversePathInReverse(ctx, fromBankToCows);
			ToObject.WalkToObject(ctx, ObjectNameConstants.BANK_CHEST);
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
		if(Backpack.Count(ctx)>26 && !Location.Within(ctx, bankArea))
		{
			if(Backpack.Has(ctx, ItemIdConstants.BONES,ItemIdConstants.BURNT_MEAT))return State.usebags;
			return State.walk_to_bank;
		}
		else if(Backpack.hasStuff(ctx) && Location.Within(ctx, bankArea))
		{
			return State.deposit;
		}
		else if(Location.Within(ctx, bankArea) && !Backpack.isFull(ctx))
		{
			return State.walk_to_cows;
		}
		else
		{
			//this adds a little bit of randomness and usually will only bury bones randomly 
			if(Backpack.Count(ctx, ItemNameConstants.RAW_BEEF) > Random.nextInt(3, 5))
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