package procyk.industries.tasks;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.actions.Widgets;
import procyk.industries.constants.AreaConstants;
import procyk.industries.constants.WidgetIdConstants;
import procyk.industries.localplayer.Location;
import procyk.industries.pathing.Traverse;

public class WalkToGrandExchange extends Task{

	public WalkToGrandExchange(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean ready() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		Tile[] lodestoneToCenter = new Tile[]{new Tile(3212, 3393,0), new Tile(3212, 3415,0),
								new Tile(3207, 3429, 0)};
		Tile[] varrockCenterToGrandExchange = new Tile[]{new Tile(3202, 3429, 0), new Tile(3185, 3441,0),
								new Tile(3173, 3456,0), new Tile(3172, 3472,0)};
		if(!Location.Within(ctx, AreaConstants.GRAND_EXCHANGE))
		{
			double distance = Location.DistanceTo(ctx, AreaConstants.GRAND_EXCHANGE);
			if(distance > 20)
			{
				//teleport to varrock and walk
				Widgets.Click(ctx, WidgetIdConstants.HUD, WidgetIdConstants.HUD_MINIMAP, WidgetIdConstants.HUD_MINIMAP_TELEPORT);
				Widgets.Click(ctx, WidgetIdConstants.TELEPORT_MAP,WidgetIdConstants.TELEPORT_MAP_VARROCK);
				procyk.industries.utility.Sleep.Wait(15000);
				Traverse.TraversePath(ctx, lodestoneToCenter);
				Traverse.TraversePath(ctx, varrockCenterToGrandExchange);
			}
			
		}
	}

}
