package procyk.industries.rt6.manager;

import org.powerbot.script.Tile;

public interface Avoidable {
	
	public long getTimeCreated();
	public Tile getTile();
	public int id();

}
