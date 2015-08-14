package Tiles;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Calculations {
	public static Tile getClosestTile(ClientContext ctx, Tile... tiles)
	{
		Tile closestTile = null;
		double smallestDistance=999;
		double distance=0;
		for(Tile t: tiles)
		{
			distance = ctx.players.local().tile().distanceTo(t);
			if(distance < smallestDistance)
			{
				smallestDistance=distance;
				closestTile = t;
			}
		}
		return closestTile;
	}

}
