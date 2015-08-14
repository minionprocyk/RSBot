package Engines;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Pathing.AvoidObject;

public class MiningEngine implements Runnable{
	
	//important objects
	private String[] rocksToMine;
	private AvoidObject[] avoidObjects;
	
	//important flags
	private boolean miningAreaSet=false;
	private boolean miningLocations=false;
	private boolean rocksSpecificed=false;
	
	private boolean inMiningLocation=false;
	private boolean closeToRocks=false;
	private boolean otherPlayersMiningRock=false;
	private boolean dangerous=false;
	
	private boolean interacted=false;
	private boolean higherLevelWarning=false;
	private boolean runOnce=true;
	
	private Area miningArea;
	private Tile[] rockLocations;
	private static ClientContext ctx;
	private static MiningEngine me;
	private MiningEngine()
	{
	}
	public static MiningEngine GetInstance()
	{
		if(me == null)
		{
			me = new MiningEngine();
		}
		return me;
	}
	public MiningEngine SetContext(ClientContext ctx)
	{
		if(MiningEngine.ctx == null)
		{
			MiningEngine.ctx = ctx;
		}
		return this;
	}
	public void run() {
		//determine if we're in a mining location
		if(miningAreaSet)
		{
			if(LocalPlayer.Location.Within(ctx, miningArea))
			{
				System.out.println("We are within the mining area. Nice");
				inMiningLocation=true;
			}
			else
			{
				System.out.println("We are "+Math.round(LocalPlayer.Location.DistanceTo(ctx, miningArea)) +" units away");
				if(LocalPlayer.Location.DistanceTo(ctx, miningArea) < 40)
				{
					//if less than 40 units away. try to walk to it
					System.out.println("The mining area seems close enough.");
					Pathing.MoveTowards.Location(ctx, miningArea);
					return; //we tried to fix the problem. try again
				}
				else
				{
					System.out.println("The mining area is too far");
					return;
				}
			}
		}
		else if(miningLocations)
		{
			if(LocalPlayer.Location.DistanceTo(ctx, rockLocations) < 40)
			{
				System.out.println("We are within the mining area. Nice");
				inMiningLocation=true;
			}
			else
			{
				System.out.println("We are "+Math.round(LocalPlayer.Location.DistanceTo(ctx, rockLocations)) +" units away");
				if(LocalPlayer.Location.DistanceTo(ctx, rockLocations) < 40)
				{
					//if less than 40 units away. try to walk to it
					System.out.println("The mining area seems close enough.");
					Pathing.MoveTowards.Location(ctx, rockLocations);
					return; //we tried to fix the problem. try again
				}
				else
				{
					System.out.println("The mining area is too far");
					return;
				}
			}
			
		}
		else
		{
			//no mining area is set. We're going rogue
		}
		
		//we are either in the mining area or it is not set
		
	}
	private void ClearFlags()
	{
		higherLevelWarning=false;
		interacted=false;
		
		inMiningLocation=false;
		closeToRocks=false;
		otherPlayersMiningRock=false;
		dangerous=false;
	}
	public MiningEngine SetRocks(String[] rocks)
	{
		this.rocksSpecificed=true;
		this.rocksToMine = rocks;
		return this;
	}
	public MiningEngine SetMiningArea(Tile[] area)
	{
		if(this.miningAreaSet || this.miningLocations)
		{
			System.out.println("Cannot declare mining area twice");
			return this;
		}
		else
		{
			this.miningLocations=true;
			this.rockLocations=area;
			return this;	
		}
	}
	public MiningEngine SetMiningArea(Area area)
	{
		if(this.miningAreaSet || this.miningLocations)
		{
			System.out.println("Cannot declare mining area twice");
			return this;
		}
		else
		{
			this.miningAreaSet=true;
			this.miningArea = area;
			return this;
		}
	}
	public MiningEngine build()
	{
		return this;
	}
}
