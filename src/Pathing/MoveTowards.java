package Pathing;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class MoveTowards {
	public static void Location(ClientContext ctx, Area area)
	{
		System.out.println("Trying to move towards area");
		//move towards this area
		Camera.Focus.OnObject(ctx, area.getClosestTo(area.getCentralTile()));
		Pathing.ToObject.WalkToObject(ctx, area.getClosestTo(area.getCentralTile()));
	}
	public static void Location(ClientContext ctx, Tile... tiles)
	{
		System.out.println("Moving towards tiles");
		Tile closestTile = Tiles.Calculations.getClosestTile(ctx, tiles);
		Camera.Focus.OnObject(ctx, closestTile);
		Pathing.ToObject.WalkToObject(ctx, closestTile);
		
	}
}
