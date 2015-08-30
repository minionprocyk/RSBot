package GameObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

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

}
