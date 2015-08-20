package Pathing;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.GameObject;

public class AvoidObject implements Avoidable{
	private long timeCreated;
	private GameObject gameObject;
	public AvoidObject(GameObject gameObject)
	{
		this.gameObject=gameObject;
		
		this.timeCreated = System.currentTimeMillis();
	}
	public long getTimeCreated()
	{
		return this.timeCreated;
	}
	public Tile getTile()
	{
		return gameObject.tile();
	}
	public int id()
	{
		return this.gameObject.id();
	}
	public GameObject getGameObject()
	{
		return this.gameObject;
	}

}
