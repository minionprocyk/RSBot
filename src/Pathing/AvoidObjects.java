package Pathing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.rt6.GameObject;

import Exceptions.NoValidObjectsException;

public class AvoidObjects {
	private static List<AvoidObject> avoidObjects = new ArrayList<AvoidObject>();
	
	private static GameObject GetNearestNonAvoidableObject(List<GameObject> gameObjects)
	{
		UpdateAvoidedObjects();
		boolean getNextObject=false;
		for(Iterator<GameObject> iGameObject = gameObjects.iterator();iGameObject.hasNext();)
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
					getNextObject=true;
					break;
				}
			}
			if(getNextObject==false)return goodObject;
		}
		throw new NullPointerException("All objects are being ignored");
	}
	public static GameObject GetNearestNonAvoidableObject(List<GameObject> gameObjects, String... name)
	{
		try
		{
			return GetNearestNonAvoidableObject(gameObjects);
		}
		catch(NullPointerException e)
		{
			return gameObjects.get(0);
		}
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
		Iterator<AvoidObject> iAvoidable = avoidObjects.iterator();
		while(iAvoidable.hasNext()){
			AvoidObject nextAvoidable = iAvoidable.next();				
			if((int) (System.currentTimeMillis()-nextAvoidable.getTimeCreated())>((int) 30*1000)){
				//if current time in mili > message.time in mili 
				System.out.println("Removing avoidable object at coords: "+nextAvoidable.getTile().x() + ", "+nextAvoidable.getTile().y());
				iAvoidable.remove();
			}
		}
		System.out.println("Avoidable objects updated.");
	}
}
