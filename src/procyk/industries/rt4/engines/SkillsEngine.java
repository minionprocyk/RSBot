package procyk.industries.rt4.engines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Player;

import procyk.industries.rt4.actions.Interact;
import procyk.industries.rt4.camera.Focus;
import procyk.industries.rt4.gameobjects.Select;
import procyk.industries.rt4.localplayer.Backpack;
import procyk.industries.rt4.localplayer.Location;
import procyk.industries.rt4.manager.AvoidObject;
import procyk.industries.rt4.manager.AvoidObjectsManager;
import procyk.industries.rt4.manager.WorldHopManager;
import procyk.industries.rt4.pathing.MoveTowards;
import procyk.industries.rt4.tiles.Calculations;
import procyk.industries.shared.constants.AnimationConstants;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.gui.SkillType;

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
			interact=InteractConstants.MINE;
			animation=AnimationConstants.PLAYER_MINING;
			break;
		case Woodcutting:
			interact=InteractConstants.CHOP;
			animation=AnimationConstants.PLAYER_CHOPPING;
			break;
		case Fishing:
			interact=InteractConstants.NET;
			animation=AnimationConstants.PLAYER_FISHING;
			break;
		}
	}
	public void run() {
		if(runOnce)init();
		
		//if we're in combat. deal with that first
		if(ctx.players.local().inCombat())
		{
			System.out.println("We're in combat. Starting fighting engine");
			FightingEngine.GetInstance().SetContext(ctx)
				.SetTargets(ctx.npcs.select().nearest().poll().id()).build().run();
		}
		//if were going to be banking. keep the items in our backpack, otherwise drop them
		if(Backpack.isFull(ctx) && isBanking)
		{
			return;
		}
		else if(Backpack.isFull(ctx) && isBanking==false)
		{
			System.out.println("Backpack is full. Dropping items");
			while(Backpack.hasStuff(ctx))
			{
				Backpack.DropItems(ctx, ctx.inventory.select().poll().id());
			}
		}
		//check to make sure we're within the requested site
		if(Location.Within(ctx, site))
		{
			System.out.println("We are within the site.");
		}
		else
		{
			System.out.println("We are "+Math.round(Location.DistanceTo(ctx, site)) +" units away");
			if(Location.DistanceTo(ctx, site) < 100)
			{
				//if less than 40 units away. try to walk to it
				System.out.println("The site seems close enough. Lets walk");
				MoveTowards.Location(ctx, site);
				return; //we tried to fix the problem. try again
			}
			else
			{
				System.out.println("The site is too far away. Walk to it");
				Focus.OnObject(ctx, site.getCentralTile());
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
			if(numPlayerAroundMe > worldHoppingThreshold)
				WorldHopManager.GetInstance().SetContext(ctx).build().WorldHop(ctx);
		}
		
		
		//perform the actual skill
		try
			{
			
			if(skillType.equals(SkillType.Fishing))throw new NullPointerException("Fishing");//this is an awful hotfix
			
			List<GameObject> collection = new ArrayList<GameObject>();
			if(objectNames == null)
			{
				collection.addAll(Select.WithinArea(ctx, site, objectIds));
			}
			else
			{
				collection.addAll(Select.WithinArea(ctx, site, objectNames));
			}

			GameObject gameObject = AvoidObjectsManager.GetNearestNonAvoidableObject(collection);

			System.out.println("We got an object that is not on the avoid list");
			if(gameObject.valid())
			{
				//we found a rock we want. check if someone else is mining it
				if(Calculations.isPlayerNearTile(ctx, gameObject.tile()))
				{
					System.out.println("Player is near "+gameObject.name()+" avoid.");
					AvoidObjectsManager.AddAvoidableObject(new AvoidObject(gameObject));
				}
				else if(Location.NearHighLevelMobs(ctx))
				{
					System.out.println("It's too dangerous to get this "+gameObject.name());
					AvoidObjectsManager.AddAvoidableObject(new AvoidObject(gameObject));					
				}
				else
				{
					try
					{
						int currentBackpackCount = Backpack.Count(ctx);
						Interact.InteractWithObject(ctx, gameObject, interact);
						
						if(expectBagIncrease && currentBackpackCount == Backpack.Count(ctx))
						{
							System.out.println("Bags did not increase. Avoiding");
							AvoidObjectsManager.AddAvoidableObject(new AvoidObject(gameObject));
								
							
						}
					}
					catch(NullPointerException e)
					{
						//object doesn't exist. avoid it
						AvoidObjectsManager.AddAvoidableObject(new AvoidObject(gameObject));
					}
				}
			}
		}
		catch(NullPointerException e)
		{
			//every object is flagged as avoidable. world hop
			if(skillType.equals(SkillType.Fishing))//awful hotfix
			{
				Interact.InteractWithNPC(ctx, objectNames[Random.nextInt(0, objectNames.length)], InteractConstants.NET);
				return;
			}
			WorldHopManager.GetInstance().SetContext(ctx).build().WorldHop(ctx);
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
}
