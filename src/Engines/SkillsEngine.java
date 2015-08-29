package Engines;

import java.util.ArrayList;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Constants.Animation;
import Constants.Interact;
import Pathing.AvoidObject;
import Pathing.AvoidObjects;

public class SkillsEngine implements Runnable{
	private static SkillsEngine se;
	private static ClientContext ctx;
	private SkillType skillType;
	private String[] objectNames;
	private int[] objectIds;
	private Area site;
	private boolean runOnce=true;
	
	//skill specific variables
	String interact;
	int animation;
	boolean expectBagIncrease=true;
	
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
		
		if(LocalPlayer.Backpack.isFull(ctx))return;
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
		
		//perform the actual skill
		List<GameObject> collection = new ArrayList<GameObject>();
		if(objectNames == null)
		{
			ctx.objects.select().id(objectIds).nearest().addTo(collection);
		}
		else
		{
			ctx.objects.select().name(objectNames).nearest().addTo(collection);
		}

		//select the nearest tree that isnt on the avoid list
		GameObject gameObject = AvoidObjects.GetNearestNonAvoidableObject(collection);
		
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
	public SkillsEngine build()
	{
		return this;
	}
	public enum SkillType
	{
		Mining, Fishing, Woodcutting
	}
}
