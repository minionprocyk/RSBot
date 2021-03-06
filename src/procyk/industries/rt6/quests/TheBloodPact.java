package procyk.industries.rt6.quests;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.rt6.actions.ChatOptions;
import procyk.industries.rt6.actions.Interact;
import procyk.industries.rt6.actions.Widgets;
import procyk.industries.rt6.pathing.Traverse;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.NpcIdConstants;
import procyk.industries.shared.constants.WidgetIdConstantsRT6;
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
			Widgets.Click(ctx, WidgetIdConstantsRT6.HUD, WidgetIdConstantsRT6.HUD_MINIMAP, WidgetIdConstantsRT6.HUD_MINIMAP_TELEPORT);
			Widgets.Click(ctx, WidgetIdConstantsRT6.TELEPORT_MAP,WidgetIdConstantsRT6.TELEPORT_MAP_LUMBRIDGE);
			procyk.industries.rt6.utility.Sleep.Wait(15000);
			if(procyk.industries.rt6.localplayer.Location.Within(ctx, lodestone))
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
			
			Interact.InteractWithNPC(ctx, NpcIdConstants.XENIA, InteractConstants.TALK);
			
			ChatOptions.SelectNextOption(ctx, "What help do you need?");
			ChatOptions.SelectNextOption(ctx, "I'll help you.");
			
			//accept the quest
			
		}
		
	}

}
