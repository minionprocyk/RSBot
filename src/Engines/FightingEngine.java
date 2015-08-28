package Engines;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import Chat.Messages;
import Constants.GameMessage;
import Constants.Interact;
import Constants.WidgetId;
import Pathing.AvoidNpc;
import Pathing.AvoidNpcs;

public class FightingEngine implements Runnable{
	private Area fightingArea;
	private static FightingEngine fe;
	private static ClientContext ctx;
	private String[] targetNames;
	private String[] foodNames;
	private int[] foodIds;
	private int[] targetIds;
	private boolean attackAtRandom=false;
	private boolean usePrayer=false;
	private boolean fightAnyway=false;
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
	public void run() {
		//if were not in combat. find a target
		if(fightingArea!=null && !LocalPlayer.Location.Within(ctx, fightingArea))
		{		
			Pathing.MoveTowards.Location(ctx, fightingArea);
		}
		if(ctx.players.local().inCombat())
		{
			if(currentTarget != null && currentTarget.inCombat() && currentTarget.healthPercent() > 0)
			{
				//were in combat. make sure we dont die
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
				Utility.Sleep.WaitRandomTime(250,500);
			}
			else
			{
				//current target is either not instantiated, not in combat, or dead
				
			}
		}
		else
		{
			//find a target if our last actual known health was over 50%
			if(currentHealthPercent > 50 || fightAnyway)
			{
				do
				{
					fightAnyway=false;
					List<Npc> collection= new ArrayList<Npc>();
					if(targetNames != null)
					{
						ctx.npcs.select().name(targetNames).nearest().addTo(collection);
					}
					else if(targetIds != null)
					{
						ctx.npcs.select().id(targetIds).nearest().addTo(collection);
					}
					if(collection.size()==0)
					{
						System.out.println("No targets found. Trying again");
						Utility.Sleep.Wait(1000);
						return;
					}
					System.out.println("Fighting Engine: Collection list has "+collection.size()+" items");
					previousTarget=currentTarget;
					currentTarget=AvoidNpcs.GetNearestNonAvoidableNpc(collection);
			
					System.out.println("We found "+currentTarget.name()+" to fight!");
					if(Tiles.Calculations.isPlayerNearObject(ctx, currentTarget.tile()))
					{
						System.out.println("Player is near "+currentTarget.name()+". avoid!");
						Pathing.AvoidNpcs.AddAvoidableNpc(new AvoidNpc(currentTarget));
					}
					else
					{
						Actions.Interact.InteractWithNPC(ctx, currentTarget, Interact.ATTACK);
						if(timer!=null)timer.stop();
						timer = new Timer(100,new ActionListener() {
							
							public void actionPerformed(ActionEvent arg0) {
								if(Messages.GetLastReadMessage().getMessage().equals(GameMessage.CantReach))
								{
									Pathing.AvoidNpcs.AddAvoidableNpc(new AvoidNpc(currentTarget));
								}
							}
						});
					}
				}while(currentTarget==null || currentTarget.equals(previousTarget) || Pathing.AvoidNpcs.IsAvoided(currentTarget));
			}
			else
			{
				//current health is less than 50. its too dangerous
				System.out.println("Our health is less than 50. What do we do?");
				if(EatFood()==false)
				{
					System.out.println("There's northing we can do. Stopping.");
					if(ctx.game.loggedIn())ctx.game.logout(true);
					ctx.controller.stop();
				}
				else
				{
					fightAnyway=true;
				}
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
	public FightingEngine AttackAtRandom(boolean aar)
	{
		attackAtRandom=true;
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
	public FightingEngine build()
	{
		return this;
	}
	
}
