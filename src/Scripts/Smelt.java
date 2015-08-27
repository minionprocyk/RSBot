package Scripts;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Constants.Interact;
import Constants.ItemId;
import Constants.ItemName;
import Constants.ObjectName;
import Exceptions.NoValidObjectsException;
import Pathing.ToObject;
import Tasks.SimpleTask;
@Manifest(name = "Smelting", description = "go to bank and smelt stuff", properties = "client=6; topic=0;")
public class Smelt extends PollingScript<ClientContext> implements MessageListener{
	Area bankArea = new Area(new Tile(2880,3532,0), new Tile(2892, 3541, 0));
	Tile[] pathToBank = new Tile[]{new Tile(2888, 3518,0), new Tile(2891, 3530, 0)};
	Area furnaceArea = new Area(new Tile(2880, 3499, 0), new Tile(2895, 3510, 0));
	String barToMake = ItemName.BRONZE_BAR;
	String[] oreRequired = new String[]{ItemName.COPPER_ORE, ItemName.TIN_ORE};
	public void poll() {
		switch(getState())
		{
		case retrieveItems:
			if(ctx.bank.opened())
			{
				//get the items, not a note
				if(ctx.bank.withdrawMode())ctx.bank.withdrawMode(false);
				ctx.bank.withdraw(ItemId.COPPER_ORE, 14);
				ctx.bank.withdraw(ItemId.TIN_ORE, 14);
				ctx.bank.close();
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
			Actions.Interact.InteractWithObject(ctx, ObjectName.FURNACE, Interact.SMELT);
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
		if(LocalPlayer.Location.Within(ctx, bankArea) && LocalPlayer.Backpack.Has(ctx, barToMake))
		{
			System.out.println("Depositing");
			return State.deposit;
		}
		else if(!LocalPlayer.Location.Within(ctx, furnaceArea) && LocalPlayer.Backpack.Has(ctx, ItemName.TIN_ORE)
														       && LocalPlayer.Backpack.Has(ctx, ItemName.COPPER_ORE))
		{
			System.out.println("Walking to furnace");
			return State.walk_to_furnace;
		}
		else if(LocalPlayer.Location.Within(ctx, furnaceArea) && LocalPlayer.Backpack.Has(ctx, ItemName.TIN_ORE)
															  && LocalPlayer.Backpack.Has(ctx, ItemName.COPPER_ORE))
		{
			System.out.println("Smelting");
			return State.smelt;
		}
		else if(!LocalPlayer.Location.Within(ctx, bankArea) && LocalPlayer.Backpack.Has(ctx, barToMake))
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
