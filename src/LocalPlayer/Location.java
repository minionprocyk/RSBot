package LocalPlayer;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Location {
	private static int notfar = 6;
	public static boolean Near(ClientContext ctx, Tile tile)
	{
		return ctx.players.local().tile().distanceTo(tile) < notfar ? true : false;
	}
	public static boolean Within(ClientContext ctx, Area area)
	{
		return area.getPolygon().contains(ctx.players.local().tile().x(), ctx.players.local().tile().y());
	}

}
