package procyk.industries.rt4.pathing;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import procyk.industries.rt4.camera.Focus;
import procyk.industries.rt4.tiles.Calculations;

public class MoveTowards {
	public static void Location(ClientContext ctx, Area area)
	{
		System.out.println("Trying to move towards area");
		//move towards this area
		Focus.OnObject(ctx, area.getClosestTo(area.getCentralTile()));
		ToObject.WalkToObject(ctx, area.getClosestTo(area.getCentralTile()));
	}
	public static void Location(ClientContext ctx, Tile... tiles)
	{
		System.out.println("Moving towards tiles");
		Tile closestTile = Calculations.getClosestTile(ctx, tiles);
		Focus.OnObject(ctx, closestTile);
		ToObject.WalkToObject(ctx, closestTile);
		
	}
}
