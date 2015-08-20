package Pathing;

import java.util.ArrayList;
import java.util.Iterator;

public class AvoidNpcs {
private static ArrayList<AvoidNpc> avoidNpcs = new ArrayList<AvoidNpc>();
	
	public static void AddAvoidableNpc(AvoidNpc avoidable)
	{
		avoidNpcs.add(avoidable);
	}
	public static ArrayList<AvoidNpc> GetList()
	{
		UpdateAvoidedNpcs();
		return avoidNpcs;
	}
	private static void UpdateAvoidedNpcs()
	{
		//removes messages sent more than 30 seconds ago
		Iterator<AvoidNpc> iAvoidable = avoidNpcs.iterator();
		while(iAvoidable.hasNext()){
			AvoidNpc nextAvoidable = iAvoidable.next();				
			if((int) (System.currentTimeMillis()-nextAvoidable.getTimeCreated())>((int) 30*1000)){
				//if current time in mili > message.time in mili 
				System.out.println("Removing avoidable npc at coords: "+nextAvoidable.getTile().x() + ", "+nextAvoidable.getTile().y());
				iAvoidable.remove();
			}
		}
		System.out.println("Avoidable Npcs updated.");
	}
}
