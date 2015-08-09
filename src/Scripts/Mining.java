package Scripts;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

import Constants.Animation;
import Constants.Interact;
import Constants.ObjectName;
import Pathing.ToObject;
import Pathing.Traverse;

@Manifest(name = "CopperMining", description = "spawn crafter and tan stuff bitch", properties = "client=6; topic=0;")

public class Mining extends PollingScript<ClientContext>{
	boolean oreValid=false;
	Tile furnaceLocation = new Tile(2884,3506,0);
	Tile bankLocation = new Tile(2889,3539,0);
	Tile exitCave1 = new Tile(2278, 4511, 0);
	Tile exitCave2 = new Tile(2292, 4516,0);
	Tile caveEntrance = new Tile(2876,3503,0);
	
	
	//tiles from bank to cave
	Tile btc1 = new Tile(2894, 3528, 0);
	Tile btc2 = new Tile(2890, 3521, 0);
	Tile btc3 = new Tile(2887, 3513, 0);
	Tile btc4 = new Tile(2880, 3508, 0);
	Tile btc5 = new Tile(2879, 3502, 0);
	
	//ore locations inside the cave
	Tile ore1 = new Tile(2274, 4515, 0);
	Tile ore2 = new Tile(2272, 4526, 0);
	Tile ore3 = new Tile(2264, 4515, 0);
	Tile ore4 = new Tile(2258, 4503, 0);
	Tile ore5 = new Tile(2267, 4496, 0);
	
	Tile[] oreLocations = new Tile[]{ore1, ore2, ore3, ore4, ore5};
	Tile[] exitCave = new Tile[]{exitCave1, exitCave2};
	Tile[] fromFurnaceToCaveEntrance = new Tile[]{furnaceLocation, caveEntrance};
	Tile[] fromBankToCaveEntrance = new Tile[]{bankLocation, btc1, 
							btc2, btc3, btc4, btc5, caveEntrance};
	public void poll() {
		switch(getState())
		{
		case mining:
			if(ctx.players.local().animation()==Animation.PLAYER_IDLE)
			{
				//look for a rock
				oreValid=Actions.Interact.InteractWithObject(ctx, ObjectName.TIN_ROCK, Interact.MINE);
				
				if(oreValid)
				{
					//rock has been interacted with
					Utility.Sleep.WaitRandomTime(100, 700);
				}
				else
				{
					//we could not find a rock. so we should try a new location
					Traverse.TraverseRandomPath(ctx, oreLocations);
				}
			}
			else
			{
				//player animation is not idle... so were probably mining something
				Utility.Sleep.WaitRandomTime(250, 2000);
			}
			break;
		case deposit:
			//exit the cave
			ExitCave();
			
			//walk to the bank
			Traverse.TraversePathInReverse(ctx, fromBankToCaveEntrance);
			
			//deposit everything into the bank
			Desposit();
			
			//walk back to the cave
			Traverse.TraversePath(ctx, fromBankToCaveEntrance);
			
			//enter the cave
			EnterCave();
			break;
		case smelt:
			//walk out of the cave
			ExitCave();
			
			//walk to the furnance
			WalkToFurnace();
			
			//smelt
			Smelt();
			
			//walk to cave
			Traverse.TraversePath(ctx, fromFurnaceToCaveEntrance);
			EnterCave();
			break;
		default:
			//panic
		}
	}
	private void ExitCave()
	{
		Traverse.TraversePath(ctx, exitCave);
		Actions.Interact.InteractWithObject(ctx, ObjectName.CAVE_EXIT, Interact.EXIT);
		//wait for some time to load
		Utility.Sleep.WaitRandomTime(1000, 3000);
		if((ctx.players.local().tile().x() < 2500) || (ctx.players.local().tile().y() > 4000))
		{
			//we are still in the cave
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
		ctx.bank.open();
		ctx.bank.depositInventory();
		ctx.bank.close();
	}
	private void Smelt()
	{
		Actions.Interact.InteractWithObject(ctx, ObjectName.FURNACE, Interact.SMELT);
		
		//click the 'smelt' component
		ctx.widgets.component(1370, 12).click();
	}
	public State getState()
	{
		if(ctx.backpack.select().count()==28)
		{
			//backpack is full. sometimes we should smelt first
			
			return State.deposit;
		}
		else
		{
			return State.mining;
		}
	}
	public enum State
	{
		mining, deposit, smelt
	}

}