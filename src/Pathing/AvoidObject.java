package Pathing;

import org.powerbot.script.Tile;

public class AvoidObject {
	private long timeCreated;
	private Tile tile;
	public AvoidObject(Tile tile)
	{
		this.tile = tile;
		this.timeCreated = System.currentTimeMillis();
	}
	public long getTimeCreated()
	{
		return this.timeCreated;
	}
	public Tile getTile()
	{
		return this.tile;
	}

}
