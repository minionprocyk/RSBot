package Quests;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

import Actions.ChatOptions;
import Constants.Interact;
import Constants.NpcId;
import Constants.WidgetId;
import Pathing.Traverse;
public class TheBloodPact extends ClientAccessor{
	private Objective[] objectives = new Objective[]{new Objective1(ctx), new Objective2(ctx)};
	public TheBloodPact(ClientContext arg0) {
		super(arg0);
	}

	public Objective[] getObjectives()
	{
		return objectives;
	}
	
	public class Objective1 extends Objective
	{
		Area lodestone = new Area(new Tile(3230,3219,0), new Tile(3236,3225,0));
		public Objective1(ClientContext ctx) {
			super(ctx);
			objectiveName = "Use the home teleport spell to travel to the lumbridge lodestone";
		}

		public boolean ready() {
			if(!completed)
			{
				return true;
			}
			return false;
		}

		public void perform() {
			Actions.Widgets.Click(ctx, WidgetId.HUD, WidgetId.HUD_MINIMAP, WidgetId.HUD_MINIMAP_TELEPORT);
			Actions.Widgets.Click(ctx, WidgetId.TELEPORT_MAP,WidgetId.TELEPORT_MAP_LUMBRIDGE);
			Utility.Sleep.Wait(15000);
			if(LocalPlayer.Location.Within(ctx, lodestone))
			{
				completed=true;
			}
			else
			{
				perform();
			}
		}
	}
	public class Objective2 extends Objective
	{
		Tile[] pathToXeniaFromLodestone = new Tile[]{new Tile(3236, 3209, 0), new Tile(3243,3199,0)};
		public Objective2(ClientContext ctx) {
			super(ctx);
			objectiveName = "Talk to Xenia in the lumbridge graveyard";
		}

		public boolean ready() {
			if(objectives[0].completed) return true;
			return false;
		}
		
		public void perform() {
			Traverse.TraversePath(ctx, pathToXeniaFromLodestone);
			
			Actions.Interact.InteractWithNPC(ctx, NpcId.XENIA, Interact.TALK);
			
			ChatOptions.SelectNextOption(ctx, "What help do you need?");
			ChatOptions.SelectNextOption(ctx, "I'll help you.");
			
			//accept the quest
			
		}
		
	}

}
