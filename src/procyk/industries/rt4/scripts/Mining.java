package procyk.industries.rt4.scripts;

import org.powerbot.script.Area;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import procyk.industries.rt4.actions.Interact;
import procyk.industries.rt4.engines.SkillsEngine;
import procyk.industries.rt4.localplayer.Backpack;
import procyk.industries.rt4.localplayer.Location;
import procyk.industries.rt4.pathing.ToObject;
import procyk.industries.rt4.pathing.Traverse;
import procyk.industries.rt4.tasks.SimpleTask;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.ObjectNameConstants;
import procyk.industries.shared.gui.SkillType;

@Manifest(name = "CopperMining", description = "spawn crafter and tan stuff bitch", properties = "client=4; topic=0;")

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
	String[] rocks = new String[]{ObjectNameConstants.COPPER_ROCK};
	
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
			ctx.controller.stop();
		
		case walk_to_bank:
			//walk to the bank
			Traverse.TraversePathInReverse(ctx, fromBankToCaveEntrance);
			break;
		case walk_to_cave:
			if(ctx.objects.select().name(ObjectNameConstants.CAVE_ENTRANCE).nearest().poll().tile().distanceTo(ctx.players.local().tile()) > 6)
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
		Interact.InteractWithObject(ctx, ObjectNameConstants.CAVE_EXIT, InteractConstants.EXIT);
		//wait for some time to load
		procyk.industries.rt6.utility.Sleep.WaitRandomTime(1000, 3000);
		if(insideCave())
		{
			Interact.InteractWithObject(ctx, ObjectNameConstants.CAVE_EXIT, InteractConstants.EXIT);		
		}
	}
	private void EnterCave()
	{
		Interact.InteractWithObject(ctx, ObjectNameConstants.CAVE_ENTRANCE, InteractConstants.ENTER);
	}
	private void WalkToFurnace()
	{
		//walk to the furnace from the cave entrance	
		ToObject.WalkToObject(ctx, ObjectNameConstants.FURNACE, furnaceLocation);
	}
	private void Desposit()
	{
		Interact.InteractWithObject(ctx, ObjectNameConstants.BANK_BOOTH, InteractConstants.BANK);
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
		Interact.InteractWithObject(ctx, ObjectNameConstants.FURNACE, InteractConstants.SMELT);
		
		//click the 'smelt' component
		ctx.widgets.component(1370, 12).click();
	}
	
	public State getState()
	{
		if(insideCave() &&Backpack.isFull(ctx))
		{
			return State.exit_cave;
		}
		else if(insideCave() && !Backpack.isFull(ctx))
		{
			return State.mining;
		}
		else if(Backpack.hasStuff(ctx) && !Location.Within(ctx, bankArea))
		{
			return State.walk_to_bank;
		}
		else if(Location.Within(ctx, bankArea) &&Backpack.hasStuff(ctx))
		{
			return State.deposit;
		}
		else if(Location.Within(ctx, bankArea) && !Backpack.hasStuff(ctx))
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