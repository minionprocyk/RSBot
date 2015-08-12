package Engines;

import java.util.Iterator;

import org.powerbot.script.Area;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Player;

import Constants.Animation;
import Constants.Interact;

public class MiningEngine extends Engine{
	//this class should be a standard mining routine that scripts can call
	private String[] rocksToMine;
	private boolean interacted=false;
	private boolean higherLevelWarning=false;
	private boolean runOnce=true;
	private Area miningArea;
	private ClientContext ctx;
	public MiningEngine(ClientContext ctx)
	{
		super(ctx);
	}
	public void run() {
		//if(runOnce==true)activate timer
		if(LocalPlayer.Animation.GetPlayerAnimation(ctx)==Animation.PLAYER_IDLE)
		{
			//player is idle. we should find something to mine
			
			
			//get a rock name
			String rock= rocksToMine[Random.nextInt(0, rocksToMine.length)];
			
			//find the closest one
			final GameObject rockObject = ctx.objects.select().name(rock).nearest().poll();
			
			if(rockObject.valid())
			{
				//check if there are enemies over there
				Iterator<Npc> iNpcs = ctx.npcs.select().iterator();
				while(iNpcs.hasNext())
				{
					if((iNpcs.next().combatLevel() - ctx.players.local().combatLevel()) > 4)
					{
						//flag a warning that the npcs combat level is higher than yours
						higherLevelWarning=true;
					}
				}
				
				//check if a player is already mining that rock
				Iterator<Player> iPlayer = ctx.players.select().iterator();
				while(iPlayer.hasNext())
				{
					if(iPlayer.next().tile().distanceTo(rockObject) < 2)
					{
						//we need to find a new rock. someone is probably using this one
					}
				}
			
				
				//interact with it
				interacted = Actions.Interact.InteractWithObject(ctx, rockObject, Interact.MINE);
				if(interacted)
				{
					do
					{
						Utility.Sleep.Wait(300);
					}while(LocalPlayer.Animation.GetPlayerAnimation(ctx) == Animation.PLAYER_MINING);
				}
			}
			
		}
		else
		{
			//player is not idle. Check if were in combat
			if(ctx.players.local().inCombat())
			{
				new FightingEngine(ctx).SetFightingArea(miningArea);
			}
			
		}
		ClearFlags();
	}
	private void ClearFlags()
	{
		this.higherLevelWarning=false;
		this.interacted=false;
	}
	public MiningEngine SetRocks(String[] rocks)
	{
		this.rocksToMine = rocks;
		return this;
	}
	public MiningEngine SetMiningArea(Area area)
	{
		this.miningArea = area;
		return this;
	}
	public MiningEngine build()
	{
		return this;
	}
}
