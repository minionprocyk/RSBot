package Scripts;

import java.util.Iterator;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

import Chat.Messages;
import Constants.ObjectName;
import Engines.SkillsEngine;
import Engines.SkillsEngine.SkillType;
import Pathing.Traverse;
import Tasks.SimpleTask;

@Manifest(name = "ChopChop", description = "Chop trees and bank em", properties = "client=6; topic=0;")
public class Chopping extends PollingScript<ClientContext> implements MessageListener{
	State currentState=null, previousState=null;
	Area bankSite = new Area(new Tile(3077, 3241, 0),new Tile(3099, 3248, 0));
	Area chopSite = new Area(new Tile(3077, 3241, 0), new Tile(3101, 3216, 0));
	Tile[] chopSiteToBank = new Tile[]{new Tile(3090, 3233, 0), new Tile(3092, 3243, 0)};
	String[] trees = new String[]{ObjectName.WILLOW};
	public void start()
	{
	}
	public void stop()
	{
		//if(ctx.game.loggedIn())ctx.game.logout(true);
	}
	public void poll() {
		switch(currentState=getState())
		{
		case chop:
			SkillsEngine.GetInstance().SetContext(ctx).SetSkill(SkillType.Woodcutting)
				.SetObject(trees).SetArea(chopSite).build().run();
			
			if(ctx.skills.level(Constants.Skills.WOODCUTTING) == 40)
			{
				System.out.println("Woodcutting is at skill = 40 | Stopping");
				ctx.controller.stop();
			}
			int numNpcsAroundMe=0;
			for(Iterator<Player> iPlayer = ctx.players.select().nearest().iterator(); iPlayer.hasNext();)
			{
				if(iPlayer.next().tile().distanceTo(ctx.players.local().tile()) < 7)
				{
					numNpcsAroundMe++;
				}
			}
			if(numNpcsAroundMe > 4)SimpleTask.WorldHop(ctx, 0);
			break;
		case walk_to_bank:
			Traverse.TraversePath(ctx, chopSiteToBank);
			break;
		case walk_to_site:
			Traverse.TraversePathInReverse(ctx, chopSiteToBank);
			break;
		case deposit:
			SimpleTask.Deposit(ctx, true);
			break;
		case stop:
			ctx.controller.stop();
			break;
		}
		previousState=currentState;
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
	public State getState()
	{
		if(LocalPlayer.Location.Within(ctx, bankSite) && LocalPlayer.Backpack.hasStuff(ctx))
		{
			System.out.println("Depositing");
			return State.deposit;
		}
		else if(LocalPlayer.Location.Within(ctx, bankSite) && !LocalPlayer.Backpack.hasStuff(ctx))
		{
			System.out.println("Walking to chopping site");
			return State.walk_to_site;
		}
		else if(LocalPlayer.Location.Within(ctx, chopSite) && !LocalPlayer.Backpack.isFull(ctx))
		{
			System.out.println("Chopping");
			return State.chop;
		}
		else if(LocalPlayer.Location.Within(ctx, chopSite) && LocalPlayer.Backpack.isFull(ctx))
		{
			System.out.println("Walking to the bank");
			return State.walk_to_bank;
		}
		else
		{
			System.out.println("We're not in the bank site or the copping site. stopping");
			return State.stop;
		}
	}
	public enum State
	{
		chop, walk_to_bank, walk_to_site, deposit, stop;
	}
}
