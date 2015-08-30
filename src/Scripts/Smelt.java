package Scripts;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Constants.Interact;
import Constants.ItemId;
import Constants.ObjectName;
import Pathing.ToObject;
import Tasks.SimpleTask;
@Manifest(name = "Smelting", description = "go to bank and smelt stuff", properties = "client=6; topic=0;")
public class Smelt extends PollingScript<ClientContext> implements MessageListener{
	Area bankArea = new Area(new Tile(3267,3158,0), new Tile(3276, 3175, 0));
	Tile[] pathToBank = new Tile[]{new Tile(3275, 3190,0), new Tile(3271, 3168, 0)};
	Area furnaceArea = new Area(new Tile(3270, 3186, 0), new Tile(3280, 3194, 0));
	int ItemToMake = ItemId.IRON_BAR;
	int oreRequired = ItemId.IRON_ORE;
	public void start()
	{
		//itemtomake = blag blah
		//orerequired = blag bla blag
	}
	public void poll() {
		switch(getState())
		{
		case retrieveItems:
			if(ctx.bank.opened())
			{
				//get the items, not a note
				if(ctx.bank.withdrawMode())ctx.bank.withdrawMode(false);
				if(ctx.bank.select().id(oreRequired).poll().valid())
				{
					ctx.bank.withdraw(oreRequired,Amount.ALL);
					ctx.bank.close();
				}
				else
				{
					System.out.println("No more ore to smelt. stopping");
					ctx.controller.stop();
				}
				
			}
			else
			{
				ctx.bank.open();
			}
			break;
		case deposit:
			Actions.Interact.InteractWithObject(ctx, ObjectName.BANK_BOOTH, Interact.BANK);
			SimpleTask.Deposit(ctx,false);
			break;
		case smelt:
			SimpleTask.Smelt(ctx);
			break;
		case walk_to_bank:
			Pathing.Traverse.TraversePath(ctx, pathToBank);
			ToObject.WalkToObject(ctx, ObjectName.BANK_BOOTH);
			break;
		case walk_to_furnace:
			Pathing.Traverse.TraversePathInReverse(ctx, pathToBank);
			ToObject.WalkToObject(ctx, ObjectName.FURNACE);
			break;
		case stop:
			System.out.println("well. i have no idea what to do. stopping");
			ctx.controller.stop();
		}
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
	public State getState()
	{
		if(LocalPlayer.Location.Within(ctx, bankArea) && LocalPlayer.Backpack.Has(ctx, ItemToMake))
		{
			System.out.println("Depositing");
			return State.deposit;
		}
		else if(!LocalPlayer.Location.Within(ctx, furnaceArea) && LocalPlayer.Backpack.Has(ctx, ItemId.IRON_ORE))
		{
			System.out.println("Walking to furnace");
			return State.walk_to_furnace;
		}
		else if(LocalPlayer.Location.Within(ctx, furnaceArea) && LocalPlayer.Backpack.Has(ctx, ItemId.IRON_ORE))
		{
			System.out.println("Smelting");
			return State.smelt;
		}
		else if(!LocalPlayer.Location.Within(ctx, bankArea) && LocalPlayer.Backpack.Has(ctx, ItemToMake))
		{
			System.out.println("Walking to bank");
			return State.walk_to_bank;
		}
		else if(LocalPlayer.Location.Within(ctx, bankArea) && LocalPlayer.Backpack.isEmpty(ctx))
		{
			System.out.println("Retrieving items from bank");
			return State.retrieveItems;
		}
		else
		{
			System.out.println("State not accounted for");
			return State.stop;
		}
	}
	public enum State
	{
	retrieveItems, deposit, smelt, walk_to_bank, walk_to_furnace,
	stop
	}

}
