package Engines;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;

public class FightingEngine{
	private boolean higherLevelWarning=false;
	private boolean interacted=false;
	Area fightingArea;
	private ClientContext ctx;
	public FightingEngine()
	{
	}
	public void run() {
		
		ClearFlags();
	}
	private void ClearFlags()
	{
		this.higherLevelWarning=false;
		this.interacted=false;
	}
	public FightingEngine SetFightingArea(Area area)
	{
		this.fightingArea = area;
		return this;
	}
	public FightingEngine SetContext(ClientContext ctx)
	{
		this.ctx = ctx;
		return this;
	}
	public FightingEngine build()
	{
		return this;
	}
}
