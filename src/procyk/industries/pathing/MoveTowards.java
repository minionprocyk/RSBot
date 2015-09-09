package procyk.industries.pathing;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.camera.Focus;

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
		Tile closestTile = procyk.industries.tiles.Calculations.getClosestTile(ctx, tiles);
		Focus.OnObject(ctx, closestTile);
		ToObject.WalkToObject(ctx, closestTile);
		
	}
}
