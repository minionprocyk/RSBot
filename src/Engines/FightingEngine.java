package Engines;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.Timer;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import Chat.Messages;
import Constants.GameMessage;
import Constants.Interact;
import Pathing.AvoidNpc;
import Pathing.AvoidNpcs;

public class FightingEngine implements Runnable{
	Area fightingArea;
	private static FightingEngine fe;
	private static ClientContext ctx;
	private String[] targetNames;
	private String[] foodNames;
	private Integer[] foodIds;
	private Integer[] targetIds;
	private boolean attackAtRandom=false;
	private boolean usePrayer=false;
	private boolean fightAnyway=false;
	private Integer lowHealth=60;
	private Integer currentHealth=100;
	private Timer timer;
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
			//were in combat. make sure we dont die
			currentHealth = ctx.players.local().healthPercent();
			if(currentHealth < lowHealth)
			{
				EatFood();
			}
			//determine if were going to lose this fight. if we are then run
			Utility.Sleep.WaitRandomTime(1000, 4000);
		}
		else
		{
			//find a target if our last actual known health was over 50%
			if(currentHealth > 50 || fightAnyway)
			{
				fightAnyway=false;
				Iterator<Npc> iNpc = ctx.npcs.select().nearest().iterator();
				Npc nextNpc=null;
				boolean targetFound=false;
				boolean getNextNpc=false;
				while(iNpc.hasNext())
				{
					nextNpc = iNpc.next();
					getNextNpc=false;
					Iterator<AvoidNpc> iAvoidNpcs = AvoidNpcs.GetList().iterator();
					while(iAvoidNpcs.hasNext())
					{
						if(iAvoidNpcs.next().id()==nextNpc.id())
						{
							getNextNpc=true;
							break;
						}
					}
					if(getNextNpc==false)
					{
						if(targetNames != null)
						{
							for(String n: targetNames)
							{
								if(nextNpc.name().equals(n))
								{
									targetFound=true;
									break;
								}
							}
						}
						else if(targetIds != null)
						{
							for(Integer id: targetIds)
							{
								if(nextNpc.id() == id)
								{
									targetFound=true;
									break;
								}
							}
						}
					}
					
					
					if(targetFound)break;
				}
				final Npc npc = nextNpc;
				System.out.println("We found "+npc.name()+" to fight!");
				if(Tiles.Calculations.isPlayerNearObject(ctx, npc.tile()))
				{
					System.out.println("Player is near "+npc.name()+". avoid!");
					Pathing.AvoidNpcs.AddAvoidableNpc(new AvoidNpc(npc));
				}
				else
				{
					Actions.Interact.InteractWithNPC(ctx, npc, Interact.ATTACK);
					if(timer!=null)timer.stop();
					timer = new Timer(100,new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							if(Messages.GetLastReadMessage().getMessage().equals(GameMessage.CantReach))
							{
								Pathing.AvoidNpcs.AddAvoidableNpc(new AvoidNpc(npc));
							}
						}
					});
				}
				
				
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
		return false;
	}
	public FightingEngine SetTargets(String[] targets)
	{
		this.targetNames = targets;
		return this;
	}
	public FightingEngine SetTargets(Integer[] targets)
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
	public FightingEngine SetFood(String[] food)
	{
		this.foodNames = food;
		return this;
	}
	public FightingEngine Setfood(Integer[] food)
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
