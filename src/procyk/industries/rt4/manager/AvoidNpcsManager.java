package procyk.industries.rt4.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.rt4.Npc;

public class AvoidNpcsManager {
private static List<AvoidNpc> avoidNpcs = new ArrayList<AvoidNpc>();
	
	public static Npc GetNearestNonAvoidableNpc(List<Npc> npcs)
	{
		UpdateAvoidedNpcs();
		if(npcs==null || npcs.size()==0)throw new NullPointerException("No npcs in GetNearestNonAvoidableNpc");
		loopNpcs: for(Iterator<Npc> iNpcs = npcs.iterator();iNpcs.hasNext();)
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
					continue loopNpcs;
				}
			}
			return goodNpc;
		}
		
		throw new NullPointerException("Pathing.AvoidNpcs.GetNearestNonAvoidableNpc: All npcs are being avoided.");

		
	}
	public static boolean IsAvoided(Npc npc)
	{
		//check if an npc is being avoided
		for(Iterator<AvoidNpc> iAvoidNpc = avoidNpcs.iterator(); iAvoidNpc.hasNext();)
		{
			if(iAvoidNpc.next().equals(npc))return true;
		}
		return false;
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
		for(Iterator<AvoidNpc> iAvoidable = avoidNpcs.iterator();iAvoidable.hasNext();)
		{
			AvoidNpc nextAvoidable = iAvoidable.next();				
			if(Math.abs(System.currentTimeMillis()-nextAvoidable.getTimeCreated())>(30*1000)){
				//if current time in mili > message.time in mili 
				System.out.println("Removing avoidable npc at coords: "+nextAvoidable.getTile().x() + ", "+nextAvoidable.getTile().y());
				iAvoidable.remove();
			}
		}
		System.out.println("Avoidable Npcs updated.");
	}
}