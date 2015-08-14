package LocalPlayer;

import org.powerbot.script.Area;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Location {
	private static int notfar = 6;
	public static boolean Near(ClientContext ctx, Tile tile)
	{
		return ctx.players.local().tile().distanceTo(tile) < notfar ? true : false;
	}
	public static double DistanceTo(ClientContext ctx, Locatable locatable)
	{
		return ctx.players.local().tile().distanceTo(locatable);
	}
	public static double DistanceTo(ClientContext ctx, Area area)
	{
		return ctx.players.local().tile().distanceTo(area.getCentralTile());
	}
	public static double DistanceTo(ClientContext ctx, Tile... tiles)
	{
		//return the least distance
		double leastDistance=999;
		double distance=0;
		for(Tile t: tiles)
		{
			distance = ctx.players.local().tile().distanceTo(t);
			if(distance < leastDistance)
			{
				leastDistance = distance;
			}
		}
		return leastDistance;
	}
	public static boolean Within(ClientContext ctx, Area area)
	{
		return area.getPolygon().contains(ctx.players.local().tile().x(), ctx.players.local().tile().y());
	}
}
