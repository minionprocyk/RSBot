package procyk.industries.rt6.gameobjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

public class Select {

	public static List<GameObject> WithinArea(ClientContext ctx, Area area, String... name)
	{
		List<GameObject> gameObjects = new ArrayList<GameObject>();
		for(Iterator<GameObject> i = ctx.objects.select().name(name).nearest().iterator();i.hasNext();)
		{
			GameObject go = i.next();
			System.out.println("GameObject ("+go.name()+") is located at "+go.tile().x()+", "+go.tile().y());
			if(area.contains(go))gameObjects.add(go);
		}
		if(gameObjects.size()==0)throw new NullPointerException("No game objects found");
		return gameObjects;
	}
	public static List<GameObject> WithinArea(ClientContext ctx, Area area, int... id)
	{
		List<GameObject> gameObjects = new ArrayList<GameObject>();
		for(Iterator<GameObject> i = ctx.objects.select().id(id).nearest().iterator();i.hasNext();)
		{
			GameObject go = i.next();
			System.out.println("GameObject ("+go.name()+") is located at "+go.tile().x()+", "+go.tile().y());
			if(area.contains(go))gameObjects.add(go);
		}
		if(gameObjects.size()==0)throw new NullPointerException("No game objects found");
		return gameObjects;
	}
	public static List<Npc> NpcWithinArea(ClientContext ctx, Area area, String... name)
	{
		List<Npc> gameObjects = new ArrayList<Npc>();
		for(Iterator<Npc> iNpc = ctx.npcs.select().name(name).nearest().iterator();iNpc.hasNext();)
		{
			Npc npc = iNpc.next();
			System.out.println("Npc ("+npc.name()+") is located at "+npc.tile().x()+", "+npc.tile().y());
			if(area.contains(npc))gameObjects.add(npc);
		}
		if(gameObjects.size()==0)throw new NullPointerException("No npcs found");
		return gameObjects;
	}
	public static List<Npc> NpcWithinArea(ClientContext ctx, Area area, int... id)
	{
		List<Npc> gameObjects = new ArrayList<Npc>();
		for(Iterator<Npc> iNpc = ctx.npcs.select().id(id).nearest().iterator();iNpc.hasNext();)
		{
			Npc npc = iNpc.next();
			System.out.println("Npc ("+npc.name()+") is located at "+npc.tile().x()+", "+npc.tile().y());
			if(area.contains(npc))gameObjects.add(npc);
		}
		if(gameObjects.size()==0)throw new NullPointerException("No npcs found");
		return gameObjects;
	}

}
