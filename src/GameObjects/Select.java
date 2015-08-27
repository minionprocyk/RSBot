package GameObjects;

import java.util.Iterator;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class Select {
	public static GameObject WithinArea(ClientContext ctx, Area area, String name)
	{
		for(Iterator<GameObject> i = ctx.objects.select().name(name).nearest().iterator();i.hasNext();)
		{
			GameObject go = i.next();
			System.out.println("GameObject ("+go.name()+") is located at "+go.tile().x()+", "+go.tile().y());
			if(area.contains(go))return go;
		}
		throw new NullPointerException();
	}
	

}
