package procyk.industries.scripts;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.actions.Interact;
import procyk.industries.chat.Messages;
import procyk.industries.constants.InteractConstants;
import procyk.industries.constants.ItemIdConstants;
import procyk.industries.constants.ObjectNameConstants;
import procyk.industries.localplayer.Backpack;
import procyk.industries.localplayer.Location;
import procyk.industries.pathing.ToObject;
import procyk.industries.pathing.Traverse;
import procyk.industries.tasks.SimpleTask;
@Manifest(name = "Smelting", description = "go to bank and smelt stuff", properties = "client=6; topic=0;")
public class Smelt extends PollingScript<ClientContext> implements MessageListener{
	Area bankArea = new Area(new Tile(3267,3158,0), new Tile(3276, 3175, 0));
	Tile[] pathToBank = new Tile[]{new Tile(3275, 3190,0), new Tile(3271, 3168, 0)};
	Area furnaceArea = new Area(new Tile(3270, 3186, 0), new Tile(3280, 3194, 0));
	int ItemToMake = ItemIdConstants.IRON_BAR;
	int oreRequired = ItemIdConstants.IRON_ORE;
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
			Interact.InteractWithObject(ctx, ObjectNameConstants.BANK_BOOTH, InteractConstants.BANK);
			SimpleTask.Deposit(ctx,false);
			break;
		case smelt:
			SimpleTask.Smelt(ctx);
			break;
		case walk_to_bank:
			Traverse.TraversePath(ctx, pathToBank);
			ToObject.WalkToObject(ctx, ObjectNameConstants.BANK_BOOTH);
			break;
		case walk_to_furnace:
			Traverse.TraversePathInReverse(ctx, pathToBank);
			ToObject.WalkToObject(ctx, ObjectNameConstants.FURNACE);
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
		if(Location.Within(ctx, bankArea) && Backpack.Has(ctx, ItemToMake))
		{
			System.out.println("Depositing");
			return State.deposit;
		}
		else if(!Location.Within(ctx, furnaceArea) && Backpack.Has(ctx, ItemIdConstants.IRON_ORE))
		{
			System.out.println("Walking to furnace");
			return State.walk_to_furnace;
		}
		else if(Location.Within(ctx, furnaceArea) && Backpack.Has(ctx, ItemIdConstants.IRON_ORE))
		{
			System.out.println("Smelting");
			return State.smelt;
		}
		else if(!Location.Within(ctx, bankArea) && Backpack.Has(ctx, ItemToMake))
		{
			System.out.println("Walking to bank");
			return State.walk_to_bank;
		}
		else if(Location.Within(ctx, bankArea) && Backpack.isEmpty(ctx))
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
