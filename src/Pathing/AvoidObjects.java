package Pathing;

import java.util.ArrayList;
import java.util.Iterator;

public class AvoidObjects {
	private static ArrayList<AvoidObject> avoidObjects = new ArrayList<AvoidObject>();
	
	public static void AddAvoidableObject(AvoidObject avoidable)
	{
		avoidObjects.add(avoidable);
	}
	public static ArrayList<AvoidObject> GetList()
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
