package procyk.industries.rt4.localplayer;

import java.util.Iterator;

import org.powerbot.script.Area;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

public class Location {
	private static int notfar = 10;
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
	public static boolean NearObjects(ClientContext ctx, String... names)
	{
		for(Iterator<GameObject> iGameObject = ctx.objects.select().name(names).nearest().iterator(); iGameObject.hasNext();)
		{
			GameObject go = iGameObject.next();
			if(Location.DistanceTo(ctx, go.tile()) < notfar || go.inViewport())
			{
				return true;
			}
		}
		
		return false;
	}
	public static boolean NearHighLevelMobs(ClientContext ctx)
	{
		//check if high level creatures are within notfar
		Iterator<Npc> iNpc = ctx.npcs.select().iterator();
		Npc npc = null;
		while(iNpc.hasNext())
		{
			npc = iNpc.next();
			if(npc.combatLevel() - ctx.players.local().combatLevel() > 5)
			{
				if(npc.tile().distanceTo(ctx.players.local().tile()) < 10)
				{
					return true;
				}
			}
		}
		return false;
	}
}
