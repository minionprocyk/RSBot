package procyk.industries.rt4.manager;

import org.powerbot.script.Tile;

public interface Avoidable {
	
	public long getTimeCreated();
	public Tile getTile();
	public int id();

}
