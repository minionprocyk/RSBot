package Tiles;

import java.util.Iterator;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

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
	public static boolean isPlayerNearObject(ClientContext ctx, Tile tile)
	{
		//check 1 tile around the tile for a player
		Iterator<Player> iPlayer = ctx.players.select().iterator();
		Player player=null;
		while(iPlayer.hasNext())
		{
			player = iPlayer.next();
			if(player.name()!= ctx.players.local().name() && player.tile().distanceTo(tile) < 2)
			{
				return true;
			}
		}
		return false;
	}

}
