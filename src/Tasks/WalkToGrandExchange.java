package Tasks;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Constants.Areas;
import Constants.WidgetId;
import Pathing.Traverse;

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
		if(!LocalPlayer.Location.Within(ctx, Areas.GRAND_EXCHANGE))
		{
			double distance = LocalPlayer.Location.DistanceTo(ctx, Areas.GRAND_EXCHANGE);
			if(distance > 20)
			{
				//teleport to varrock and walk
				Actions.Widgets.Click(ctx, WidgetId.HUD, WidgetId.HUD_MINIMAP, WidgetId.HUD_MINIMAP_TELEPORT);
				Actions.Widgets.Click(ctx, WidgetId.TELEPORT_MAP,WidgetId.TELEPORT_MAP_VARROCK);
				Utility.Sleep.Wait(15000);
				Traverse.TraversePath(ctx, lodestoneToCenter);
				Traverse.TraversePath(ctx, varrockCenterToGrandExchange);
			}
			
		}
	}

}
