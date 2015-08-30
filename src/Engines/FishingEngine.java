package Engines;

import java.util.Iterator;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import Constants.Animation;
import Constants.Interact;
import Constants.ObjectName;
import Exceptions.NoValidObjectsException;

public class FishingEngine implements Runnable{
	//this class should be a standard mining routine that scripts can call
		private String[] treesToChop;
		private boolean interacted=false;
		private boolean higherLevelWarning=false;
		private boolean runOnce=true;
		private Area fishingArea;
		private static ClientContext ctx;
		private static FishingEngine fe;
		private FishingEngine()
		{
		}
		public static FishingEngine GetInstsance()
		{
			if(fe==null)
			{
				fe = new FishingEngine();
			}
			return fe;
		}
		public FishingEngine SetContext(ClientContext ctx)
		{
			if(FishingEngine.ctx == null)
			{
				FishingEngine.ctx = ctx;
			}
			return this;
		}
		public void run() {
			//if(runOnce==true)activate timer
			if(LocalPlayer.Animation.PlayerAnimation(ctx)==Animation.PLAYER_IDLE)
			{
				//player is idle. we should find something to mine
				
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
				
					
				interacted = Actions.Interact.InteractWithObject(ctx, ObjectName.FISHING_SPOT, Interact.CAGE);
				if(interacted)
				{
					Utility.Sleep.WhilePlayer(ctx, Animation.PLAYER_FISHING);
				}			
			}
			else
			{
				//player is not idle. Check if were in combat
				if(ctx.players.local().inCombat())
				{
					FightingEngine.GetInstance().SetContext(ctx).SetFightingArea(fishingArea).run();;
				}
				
			}
			ClearFlags();
		}
		private void ClearFlags()
		{
			this.higherLevelWarning=false;
			this.interacted=false;
		}
		public FishingEngine SetFishingArea(Area area)
		{
			this.fishingArea = area;
			return this;
		}
		public FishingEngine build()
		{
			return this;
		}
}
