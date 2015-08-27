package Pathing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.rt6.Npc;

public class AvoidNpcs {
private static List<AvoidNpc> avoidNpcs = new ArrayList<AvoidNpc>();
	
	public static Npc GetNearestNonAvoidableNpc(List<Npc> npcs)
	{
		UpdateAvoidedNpcs();
		if(npcs==null)throw new NullPointerException("No npcs in GetNearestNonAvoidableNpc");
		boolean getNextNpc=false;
		for(Iterator<Npc> iNpcs = npcs.iterator();iNpcs.hasNext();)
		{
			Npc goodNpc = iNpcs.next();
			for(Iterator<AvoidNpc> iAvoidNpc = avoidNpcs.iterator();iAvoidNpc.hasNext();)
			{
				AvoidNpc an = iAvoidNpc.next();
				if(an.getTile().distanceTo(goodNpc.tile())==0 &&
						an.id() == goodNpc.id() &&
						an.getNpc().name().equals(goodNpc.name()))
				{
					System.out.println(goodNpc.name()+" is on the avoid list. Getting next npc");
					getNextNpc=true;
					break;
				}
			}
			if(getNextNpc==false)return goodNpc;
		}
		
		//all npcs are being ignored. returning the first one
		return npcs.get(0);
		
	}
	public static void AddAvoidableNpc(AvoidNpc avoidable)
	{
		avoidNpcs.add(avoidable);
	}
	public static List<AvoidNpc> GetList()
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
