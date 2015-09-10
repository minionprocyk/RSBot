package procyk.industries.rt4.tiles;

import java.util.Iterator;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Player;

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
	public static boolean isPlayerNearTile(ClientContext ctx, Tile tile)
	{
		//check 1 tile around the tile for a player
		Iterator<Player> iPlayer = ctx.players.select().iterator();
		while(iPlayer.hasNext())
		{
			Player player = iPlayer.next();
			if(player.name()!= ctx.players.local().name() && player.tile().distanceTo(tile) < 4)
			{
				return true;
			}
		}
		return false;
	}

}
