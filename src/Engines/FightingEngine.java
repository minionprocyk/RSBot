package Engines;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import Constants.Interact;

public class FightingEngine extends Engine{
	private boolean higherLevelWarning=false;
	private boolean interacted=false;
	Area fightingArea;
	//
	private ClientContext ctx;
	public FightingEngine(ClientContext ctx)
	{
		super(ctx);
	}
	public void run() {
		//if were not in combat. find a target
		if(ctx.players.local().inCombat())
		{
			//attack the thing
			Utility.Sleep.WaitRandomTime(1000, 4000);
		}
		else
		{
			if(ctx.players.local().healthPercent() > 50)
			{
				System.out.println("Health is good. Were at "+ctx.players.local().healthRatio());
				Npc npc = ctx.npcs.select().nearest().poll();
				Actions.Interact.InteractWithNPC(ctx, npc, Interact.ATTACK);
				//wait for this npc to not exist or for 30 seconds
				long now = System.currentTimeMillis();
				do
				{
					Utility.Sleep.Wait(100);
				}while(npc.valid() && (Math.abs(now-System.currentTimeMillis())) < 1000*30);
				System.out.println(npc.name()+" dead.");
			}
			else
			{
				//check if we have food
			}
			
		}
		ClearFlags();
	}
	private void ClearFlags()
	{
		this.higherLevelWarning=false;
		this.interacted=false;
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
