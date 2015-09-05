package Engines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import Constants.Interact;
import Constants.WidgetId;
import Manager.AvoidNpc;
import Manager.AvoidNpcsManager;
import Tasks.SimpleTask;

public class FightingEngine implements Runnable{
	private Area fightingArea;
	private static FightingEngine fe;
	private static ClientContext ctx;
	private String[] targetNames;
	private String[] foodNames;
	private int[] foodIds;
	private int[] targetIds;
	private String[] loot;
	private boolean inCombat=false;
	private boolean usePrayer=false;
	private String textHealth;
	private int lowHealth=60, currentHealth=100, maxHealth=100, currentHealthPercent=100;
	private int enemyCurrentHealth=100, enemyMaxHealth=100, enemyCurrentHealthPercent=100;
	private Timer timer;
	private Npc currentTarget;
	private Npc previousTarget;
	private FightingEngine()
	{
	}
	public static FightingEngine GetInstance()
	{
		if(fe == null)
		{
			fe = new FightingEngine();
		}
		return fe;
		
	}
	public FightingEngine SetContext(ClientContext ctx) {
		if(FightingEngine.ctx==null)
		{
			FightingEngine.ctx = ctx;
		}
		return this;
	}
	public void run() 
	{
		if(currentTarget==null)
		{
			//check if we're already in combat
			inCombat = ctx.players.local().inCombat();
			
			//find the closest npc that is in combat, 
			for(Iterator<Npc> npc = ctx.npcs.select().iterator(); npc.hasNext();)
			{
				Npc nextNpc = npc.next();
				if(nextNpc.valid() && nextNpc.inCombat()   
								   && nextNpc.tile().distanceTo(ctx.players.local().tile())< 4)
				{
					currentTarget = nextNpc;
					inCombat = true;
				}
			}
		}
		
		/*
		 * we can be in combat because ctx.player.local.inCombat is feeding us bull shit.
		 * we cna also be in combat because we just set a target
		 * check if we have a current target
		 *
		 */
		if(inCombat && currentTarget!=null)
		{
			//we have a target and we're in combat. treat this as if we were actually in combat
			//collect combat stats
			while(inCombat)
			{
				//make sure we've interacted with the target
				try
				{
					if(!ctx.players.local().inCombat())Actions.Interact.InteractWithNPC(ctx, currentTarget, Interact.ATTACK);
					Utility.Sleep.Wait(1000);
				}
				catch(NullPointerException e)
				{
					inCombat=false;
				}
				
				
				textHealth = ctx.widgets.select().id(WidgetId.COMBAT_BAR).poll().component(WidgetId.COMBAT_BAR_HEALTH).
										component(WidgetId.COMBAT_BAR_HEALTH_TEXT).text();
				
				maxHealth = Integer.parseInt(textHealth.split("/")[1]);
				currentHealth = Integer.parseInt(textHealth.split("/")[0]);
				currentHealthPercent = (currentHealth*100)/maxHealth;
				System.out.println("Player current health = "+currentHealthPercent + " ["+
						currentHealth+" / "+maxHealth+"]");
				
				System.out.println("Enemy Current Health = "+currentTarget.healthPercent());
				if(currentHealthPercent < lowHealth)
				{
					EatFood();
				}
				//determine if were going to lose this fight. if we are then run
				Utility.Sleep.WaitRandomTime(500,1000);
				if(inCombat && !currentTarget.valid() || currentTarget.healthPercent()==-1 || currentTarget.actions().length==0)inCombat=false;
			}
			//target is dead. loot if set
			if(loot!=null)
			{
				System.out.println("Current Target is dead.");
				Utility.Sleep.Wait(1000);
				SimpleTask.Loot(ctx, loot);	
			}
			previousTarget=currentTarget;
			currentTarget=null;
		}
		else
		{
			
			/*
			 * we're not in combat or the target is null
			 * This should only happen if we want to find a target for the purposes of training
			 */
			try
			{
				List<Npc> collection = new ArrayList<Npc>();
				if(targetNames == null)
				{
					collection.addAll(GameObjects.Select.NpcWithinArea(ctx, fightingArea, targetIds));
				}
				else
				{
					collection.addAll(GameObjects.Select.NpcWithinArea(ctx, fightingArea, targetNames));
				}

				Npc npc = AvoidNpcsManager.GetNearestNonAvoidableNpc(collection);
				System.out.println("We got an object that is not on the avoid list");
				if(npc.valid())
				{
					//we found an npc to fight
					if(Tiles.Calculations.isPlayerNearTile(ctx, npc.tile()))
					{
						System.out.println("Player is near "+npc.name()+" avoid.");
						AvoidNpcsManager.AddAvoidableNpc(new AvoidNpc(npc));
					}
					else if(LocalPlayer.Location.NearHighLevelMobs(ctx))
					{
						System.out.println("It's too dangerous to get this "+npc.name());
						AvoidNpcsManager.AddAvoidableNpc(new AvoidNpc(npc));					
					}
					else
					{
						//set the current target. allow the next iteration to handle this target
						currentTarget = npc;
						inCombat=true;
					}
				}
			}
			catch(NullPointerException e)
			{
				System.out.println("No npcs found within area.");
				if(fightingArea!=null)Pathing.Traverse.TraversePath(ctx, fightingArea.getRandomTile());
			}
			
		}
	}
	private boolean EatFood()
	{
		//use food if we have it
		if(foodNames != null)
		{
			for(String food: foodNames)
			{
				if(LocalPlayer.Backpack.Has(ctx, food))
				{
					LocalPlayer.Backpack.Use(ctx, food, Interact.EAT);
					return true;
				}
			}
		}else if(foodIds != null)
		{
			for(Integer food: foodIds)
			{
				if(LocalPlayer.Backpack.Has(ctx, food))
				{
					LocalPlayer.Backpack.Use(ctx, food, Interact.EAT);
					return true;
				}
			}
		}
		else
		{
			throw new NullPointerException("No food has been specified using SetFood context");
		}
		return false;
	}
	public FightingEngine SetTargets(String... targets)
	{
		this.targetNames = targets;
		return this;
	}
	public FightingEngine SetTargets(int... targets)
	{
		targetIds = targets;
		return this;
	}
	public FightingEngine SetLowHealth(Integer health)
	{
		this.lowHealth = health;
		return this;
	}
	public FightingEngine SetFood(String... food)
	{
		this.foodNames = food;
		return this;
	}
	public FightingEngine SetFood(int... food)
	{
		this.foodIds = food;
		return this;
	}
	public FightingEngine SetLoot(String... items)
	{
		this.loot = items;
		return this;
	}
	public FightingEngine SetPrayer(boolean usePrayer)
	{
		this.usePrayer=usePrayer;
		return this;
	}
	public FightingEngine SetFightingArea(Area area)
	{
		this.fightingArea = area;
		return this;
	}
	public FightingEngine FindTarget()
	{
		return this;
	}
	public FightingEngine build()
	{
		return this;
	}
	
}
