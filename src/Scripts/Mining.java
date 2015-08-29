package Scripts;

import org.powerbot.script.Area;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Constants.Interact;
import Constants.ObjectName;
import Engines.SkillsEngine;
import Engines.SkillsEngine.SkillType;
import Pathing.ToObject;
import Pathing.Traverse;
import Tasks.SimpleTask;

@Manifest(name = "CopperMining", description = "spawn crafter and tan stuff bitch", properties = "client=6; topic=0;")

public class Mining extends PollingScript<ClientContext>{
	boolean interacted=false;
	static int currentPlayerAnimation=-1;
	private static State previousState = State.initialize;
	private static State currentState = State.initialize;
	//tiles from furnace to cave entrance
	Tile furnaceLocation = new Tile(2884,3506,0);
	Tile bankLocation = new Tile(2889,3539,0);
	Tile exitCave1 = new Tile(2278, 4511, 0);
	Tile exitCave2 = new Tile(2292, 4516,0);
	Tile caveEntrance = new Tile(2876,3503,0);
	Area bankArea = new Area(new Tile(2880, 3530),new Tile(2896, 3540));
	
	//tiles from bank to cave
	Tile btc1 = new Tile(2894, 3528, 0);
	Tile btc2 = new Tile(2890, 3521, 0);
	Tile btc3 = new Tile(2887, 3513, 0);
	Tile btc4 = new Tile(2880, 3508, 0);
	Tile btc5 = new Tile(2879, 3502, 0);
	
	//rock locations inside the cave
	Area miningArea = new Area(new Tile(2274, 4515, 0), new Tile(2267, 4496, 0));
	
	Tile[] exitCave = new Tile[]{exitCave1, exitCave2};
	Tile[] fromFurnaceToCaveEntrance = new Tile[]{furnaceLocation, caveEntrance};
	Tile[] fromBankToCaveEntrance = new Tile[]{bankLocation, btc1, 
							btc2, btc3, btc4, btc5, caveEntrance};
	String[] rocks = new String[]{ObjectName.COPPER_ROCK};
	
	public void poll() {
		switch(currentState=getState())
		{
		case mining:
			SkillsEngine.GetInstance().SetContext(ctx).SetSkill(SkillType.Mining)
				.SetObject(rocks).SetArea(miningArea).build();
			break;
		case deposit:
			//deposit everything into the bank
			Desposit();
			break;
		case stop:
			//logout if were logged in and then stop the script
			if(ctx.game.loggedIn())ctx.game.logout(true);
			ctx.controller.stop();
		
		case walk_to_bank:
			//walk to the bank
			Traverse.TraversePathInReverse(ctx, fromBankToCaveEntrance);
			break;
		case walk_to_cave:
			if(ctx.objects.select().name(ObjectName.CAVE_ENTRANCE).nearest().poll().tile().distanceTo(ctx.players.local().tile()) > 6)
			{
				//walk back to the cave
				Traverse.TraversePath(ctx, fromBankToCaveEntrance);
				//enter the cave
				EnterCave();
			}
			else
			{
				EnterCave();
			}
			
			break;
		case walk_to_furnace:
			break;
		case smelt:
			SimpleTask.Smelt(ctx);
			break;
		case exit_cave:
			//exit the cave
			ExitCave();
			break;
		default:
			System.out.println("No condition is being met.");
		}
		previousState=currentState;
	}
	private void ExitCave()
	{
		Traverse.TraversePath(ctx, exitCave);
		Actions.Interact.InteractWithObject(ctx, ObjectName.CAVE_EXIT, Interact.EXIT);
		//wait for some time to load
		Utility.Sleep.WaitRandomTime(1000, 3000);
		if(insideCave())
		{
			Actions.Interact.InteractWithObject(ctx, ObjectName.CAVE_EXIT, Interact.EXIT);		
		}
	}
	private void EnterCave()
	{
		Actions.Interact.InteractWithObject(ctx, ObjectName.CAVE_ENTRANCE, Interact.ENTER);
	}
	private void WalkToFurnace()
	{
		//walk to the furnace from the cave entrance	
		ToObject.WalkToObject(ctx, ObjectName.FURNACE, furnaceLocation);
	}
	private void Desposit()
	{
		Actions.Interact.InteractWithObject(ctx, ObjectName.BANK_BOOTH, Interact.BANK);
		SimpleTask.Deposit(ctx,true);
	}
	private boolean insideCave()
	{
		if((ctx.players.local().tile().x() < 2500) || (ctx.players.local().tile().y() > 4000))
		{
			//we are still in the cave
			return true;
		}
		else
		{
			return false;
		}
	}
	private void Smelt()
	{
		Actions.Interact.InteractWithObject(ctx, ObjectName.FURNACE, Interact.SMELT);
		
		//click the 'smelt' component
		ctx.widgets.component(1370, 12).click();
	}
	
	public State getState()
	{
		if(insideCave() && LocalPlayer.Backpack.isFull(ctx))
		{
			return State.exit_cave;
		}
		else if(insideCave() && !LocalPlayer.Backpack.isFull(ctx))
		{
			return State.mining;
		}
		else if(LocalPlayer.Backpack.hasStuff(ctx) && !LocalPlayer.Location.Within(ctx, bankArea))
		{
			return State.walk_to_bank;
		}
		else if(LocalPlayer.Location.Within(ctx, bankArea) && LocalPlayer.Backpack.hasStuff(ctx))
		{
			return State.deposit;
		}
		else if(LocalPlayer.Location.Within(ctx, bankArea) && !LocalPlayer.Backpack.hasStuff(ctx))
		{
			return State.walk_to_cave;
		}
		else
		{
			return null;
		}
	}
	public enum State
	{
		mining, deposit, smelt, stop,
		walk_to_furnace, walk_to_cave, walk_to_bank, exit_cave, initialize
	}
}