package procyk.industries.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.rt6.GameObject;

public class AvoidObjectsManager {
	private static List<AvoidObject> avoidObjects = new ArrayList<AvoidObject>();
	
	public static GameObject GetNearestNonAvoidableObject(List<GameObject> gameObjects)
	{
		UpdateAvoidedObjects();
		if(gameObjects==null || gameObjects.size()==0)throw new NullPointerException("No objects in GetNearestNonAvoidableObject");
		loopGameObjects: for(Iterator<GameObject> iGameObject = gameObjects.iterator();iGameObject.hasNext();)
		{
			GameObject goodObject = iGameObject.next();
			for(Iterator<AvoidObject> iAvoidObject = avoidObjects.iterator();iAvoidObject.hasNext();)
			{
				AvoidObject ao = iAvoidObject.next();
				if(ao.getTile().distanceTo(goodObject.tile())==0 &&
						ao.id() == goodObject.id() &&
						ao.getGameObject().name().equals(goodObject.name()))
				{
					System.out.println(goodObject.name()+" is on the avoid list. Getting next object");
					continue loopGameObjects;
				}
			}
			return goodObject;
		}
		throw new NullPointerException("Pathing.AvoidObjects.GetNearestNonAvoidableObject: All objects are being avoided.");
		
	}
	public static void AddAvoidableObject(AvoidObject avoidable)
	{
		avoidObjects.add(avoidable);
	}
	public static List<AvoidObject> GetList()
	{
		UpdateAvoidedObjects();
		return avoidObjects;
	}
	private static void UpdateAvoidedObjects()
	{
		//removes messages sent more than 30 seconds ago
		for(Iterator<AvoidObject> iAvoidable = avoidObjects.iterator(); iAvoidable.hasNext();)
		{
			AvoidObject nextAvoidable = iAvoidable.next();				
			if(Math.abs(System.currentTimeMillis()-nextAvoidable.getTimeCreated())>(30*1000)){
				//if current time in mili > message.time in mili 
				System.out.println("Removing avoidable object at coords: "+nextAvoidable.getTile().x() + ", "+nextAvoidable.getTile().y());
				iAvoidable.remove();
			}
		}
		System.out.println("Avoidable objects updated.");
	}
}