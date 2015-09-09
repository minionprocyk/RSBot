package procyk.industries.rt6.manager;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Npc;

public class AvoidNpc implements Avoidable{
	private Npc npc;
	private long timeCreated;
	public AvoidNpc(Npc npc)
	{
		this.npc=npc;
		this.timeCreated=System.currentTimeMillis();
	}
	public Npc getNpc()
	{
		return this.npc;
	}
	public long getTimeCreated()
	{
		return this.timeCreated;
	}
	public Tile getTile()
	{
		return this.npc.tile();
	}
	public int id()
	{
		return this.npc.id();
	}
}
