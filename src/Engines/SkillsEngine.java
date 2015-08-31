package Engines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Player;

import Constants.Animation;
import Constants.Interact;
import Pathing.AvoidObject;
import Pathing.AvoidObjects;
import Tasks.SimpleTask;

public class SkillsEngine implements Runnable{
	private static SkillsEngine se;
	private static ClientContext ctx;
	private SkillType skillType;
	private String[] objectNames;
	private int[] objectIds;
	private Area site;
	private boolean runOnce=true;
	private boolean worldHopping=false;
	private int worldHoppingThreshold=100;
	//skill specific variables
	String interact;
	int animation;
	boolean expectBagIncrease=true;
	boolean isBanking=true;
	private SkillsEngine(){}
	
	public static SkillsEngine GetInstance()
	{
		if(se == null)
		{
			se = new SkillsEngine();
		}
		return se;
	}
	public SkillsEngine SetContext(ClientContext ctx)
	{
		if(SkillsEngine.ctx==null)
		{
			SkillsEngine.ctx=ctx;
		}
		return this;
	}
	private void init()
	{
		runOnce=false;
		//check if our skill was set to initialize skill specific variables
		if(skillType==null)
			throw new NullPointerException("Cannot use SkillsEngine without setting a skill");
		switch(skillType)
		{
		case Mining:
			interact=Interact.MINE;
			animation=Animation.PLAYER_MINING;
			break;
		case Woodcutting:
			interact=Interact.CHOP;
			animation=Animation.PLAYER_CHOPPING;
			break;
		case Fishing:
			interact=Interact.NET;
			animation=Animation.PLAYER_FISHING;
			break;
		}
	}
	public void run() {
		if(runOnce)init();
		
		//if we're in combat. deal with that first
		if(ctx.players.local().inCombat())
		{
			FightingEngine.GetInstance().SetContext(ctx)
				.SetTargets(ctx.npcs.select().nearest().poll().id()).build().run();
		}
		//if were going to be banking. keep the items in our backpack, otherwise drop them
		if(LocalPlayer.Backpack.isFull(ctx) && isBanking)
		{
			return;
		}
		else if(LocalPlayer.Backpack.isFull(ctx) && isBanking==false)
		{
			if(objectNames==null)LocalPlayer.Backpack.DropItems(ctx, objectIds);
			if(objectIds==null)LocalPlayer.Backpack.DropItems(ctx, objectNames);
		}
		//check to make sure we're within the requested site
		if(LocalPlayer.Location.Within(ctx, site))
		{
			System.out.println("We are within the site.");
		}
		else
		{
			System.out.println("We are "+Math.round(LocalPlayer.Location.DistanceTo(ctx, site)) +" units away");
			if(LocalPlayer.Location.DistanceTo(ctx, site) < 40)
			{
				//if less than 40 units away. try to walk to it
				System.out.println("The site seems close enough. Lets walk");
				Pathing.MoveTowards.Location(ctx, site);
				return; //we tried to fix the problem. try again
			}
			else
			{
				System.out.println("The site is too far away. Walk to it");
				Camera.Focus.OnObject(ctx, site.getCentralTile());
				ctx.controller.stop();
				return;
			}
		}
		
		if(worldHopping)
		{
			int numPlayerAroundMe=0;
			for(Iterator<Player> iPlayer = ctx.players.select().nearest().iterator(); iPlayer.hasNext();)
			{
				if(iPlayer.next().tile().distanceTo(ctx.players.local().tile()) < 7)
				{
					numPlayerAroundMe++;
				}
			}
			if(numPlayerAroundMe > worldHoppingThreshold)SimpleTask.WorldHop(ctx, 0);
		}
		
		
		//perform the actual skill
		List<GameObject> collection = new ArrayList<GameObject>();
		if(objectNames == null)
		{
			collection.addAll(GameObjects.Select.WithinArea(ctx, site, objectIds));
		}
		else
		{
			collection.addAll(GameObjects.Select.WithinArea(ctx, site, objectNames));
		}

		try
		{
			GameObject gameObject = AvoidObjects.GetNearestNonAvoidableObject(collection);
			//select the nearest tree that isnt on the avoid list
			
			System.out.println("We got an object that is not on the avoid list");
			if(gameObject.valid())
			{
				//we found a rock we want. check if someone else is mining it
				if(Tiles.Calculations.isPlayerNearTile(ctx, gameObject.tile()))
				{
					System.out.println("Player is near "+gameObject.name()+" avoid.");
					AvoidObjects.AddAvoidableObject(new AvoidObject(gameObject));
				}
				else if(LocalPlayer.Location.NearHighLevelMobs(ctx))
				{
					System.out.println("It's too dangerous to get this "+gameObject.name());
					AvoidObjects.AddAvoidableObject(new AvoidObject(gameObject));					
				}
				else
				{
					try
					{
						int currentBackpackCount = LocalPlayer.Backpack.Count(ctx);
						Actions.Interact.InteractWithObject(ctx, gameObject, interact);
						
						if(expectBagIncrease && currentBackpackCount == LocalPlayer.Backpack.Count(ctx))
						{
							System.out.println("Bags did not increase. Avoiding");
							AvoidObjects.AddAvoidableObject(new AvoidObject(gameObject));
								
							
						}
					}
					catch(NullPointerException e)
					{
						//object doesn't exist. avoid it
						AvoidObjects.AddAvoidableObject(new AvoidObject(gameObject));
					}
				}
			}
		}
		catch(NullPointerException e)
		{
			//every object is flagged as avoidable. world hop
			SimpleTask.WorldHop(ctx, 0);
		}
		
	}
	public SkillsEngine SetSkill(SkillType st)
	{
		skillType = st;
		return this;
	}
	public SkillsEngine SetObject(String... objectName)
	{
		objectNames=objectName;
		return this;
	}
	public SkillsEngine SetObject(int... objectId)
	{
		objectIds=objectId;
		return this;
	}
	public SkillsEngine SetArea(Area site)
	{
		this.site = site;
		return this;
	}
	public SkillsEngine EnableWorldHopping(int threshold)
	{
		this.worldHoppingThreshold = threshold;
		return this;
	}
	public SkillsEngine SetBanking(boolean isBanking)
	{
		this.isBanking=isBanking;
		return this;
	}
	public SkillsEngine build()
	{
		return this;
	}
	public enum SkillType
	{
		Mining, Fishing, Woodcutting
	}
}
