package Engines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Constants.Interact;
import Constants.ObjectName;
import Pathing.AvoidObject;
import Pathing.AvoidObjects;
public class MiningEngine implements Runnable{
	
	//important objects
	private static String[] allRocks = new String[]{ObjectName.COPPER_ROCK, ObjectName.COPPER_ROCKS,
				ObjectName.TIN_ROCK, ObjectName.TIN_ROCKS, ObjectName.CLAY_ROCK, ObjectName.IRON_ROCKS};
	private String[] rocksToMine;
	
	//important flags
	private boolean miningAreaSet=false;
	private boolean miningLocations=false;
	private boolean rocksSpecificed=false;
	
	private boolean inMiningLocation=false;
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
		//if were in combat. that takes priority
		if(ctx.players.local().inCombat())
		{
			FightingEngine.GetInstance().SetContext(ctx).build().run();
		}
		if(LocalPlayer.Backpack.isFull(ctx))return;//do nothing if backpack is full
		else
		{
			//determine if we're in a mining location
			if(miningAreaSet)
			{
				if(LocalPlayer.Location.Within(ctx, miningArea))
				{
					System.out.println("We are within the mining area.");
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
					System.out.println("We are within the mining area.");
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
				//check if there are rocks around us
				if(LocalPlayer.Location.NearObjects(ctx, rocksToMine))
				{
					//close to rocks
					inMiningLocation=true;
				}
				else
				{
					//find rocks and walk towards them
					if(!ctx.objects.select().name(allRocks).isEmpty())
					{
						//theres rocks somewhere. walk towards it
						Pathing.MoveTowards.Location(ctx, ctx.objects.select().name(allRocks).nearest().poll().tile());
					}
				}
			}
			
			//at this point. we assume that we're within the mining location
			//or at least we are close to some rocks
			
			// determine what list we are going to use
			String[] rockList=null;
			if(rocksSpecificed)
			{
				rockList=rocksToMine;
			}
			else
			{
				rockList=allRocks;
			}
			
			//select the nearest rock that isnt on the avoid list
			List<GameObject> collection = new ArrayList<GameObject>();
			ctx.objects.select().name(rockList).nearest().addTo(collection);
			GameObject rockWeWant = Pathing.AvoidObjects.GetNearestNonAvoidableObject(collection);
			
			System.out.println("We got a rock that is not on the avoid list");
			if(inMiningLocation)
			{
				if(rockWeWant.valid())
				{				
					//we found a rock we want. check if someone else is mining it
					if(Tiles.Calculations.isPlayerNearObject(ctx, rockWeWant.tile()))
					{
						System.out.println("Player is near "+rockWeWant.name()+" avoid.");
						//flag this rock as bad
						Pathing.AvoidObjects.AddAvoidableObject(new AvoidObject(rockWeWant));
					}
					else if(LocalPlayer.Location.NearHighLevelMobs(ctx))
					{
						System.out.println("It's too dangerous to get this rock");
						//its dangerous
						Pathing.AvoidObjects.AddAvoidableObject(new AvoidObject(rockWeWant));					
					}
					else
					{
						try
						{
							Actions.Interact.InteractWithObject(ctx, rockWeWant, Interact.MINE);	
						}
						catch(NullPointerException e)
						{
							//object doesnt exist. avoid it for a bit
							Pathing.AvoidObjects.AddAvoidableObject(new AvoidObject(rockWeWant));
						}
					}
				}
			}
		}
		
		
	}
	public MiningEngine SetRocks(String... rocks)
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
