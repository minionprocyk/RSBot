package Scripts;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Constants.ObjectName;
import Engines.FightingEngine;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext>{
	String[] rocks = new String[]{ObjectName.COPPER_ROCKS, ObjectName.TIN_ROCKS, ObjectName.IRON_ROCKS};
	Tile tile = new Tile(3381, 3270, 0);
	public void poll() {	
		switch(getState())
		{
		case mine:
			//new MiningEngine().SetContext(ctx).SetRocks(rocks).build().run();
			new FightingEngine().SetContext(ctx).build().run();
		}
		
	}
	
	public State getState()
	{
		return State.mine;
	}
	public enum State
	{
		mine
	}
}
