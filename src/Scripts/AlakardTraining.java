package Scripts;

import org.powerbot.script.Area;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;

import Constants.Interact;
import Constants.ItemId;
import Constants.NpcName;
import Constants.ObjectName;
import Engines.FightingEngine;
import Pathing.Traverse;

@Manifest(name = "AlAkard Scorpians", description = "kill scorpians and restock food", properties = "client=6; topic=0;")

public class AlakardTraining extends PollingScript<ClientContext>{
	State currentState, previousState;
	Area killSite = new Area(new Tile(3289, 3270, 0), new Tile(3321, 3318, 0));
	//ObjectName.BANK_CHEST Interact.USE
	//withdraw ItemId.COOKED_MEAT Amount.ALL
	Area bankSite = new Area(new Tile(3376, 3264, 0), new Tile(3384, 3273, 0));
	Tile[] bankToKillSite = new Tile[]{new Tile(3380, 3270, 0), new Tile(3381, 3280, 0),
					new Tile(3346, 3274, 0), new Tile(3381, 3276, 0), new Tile(3318, 3280, 0),
					new Tile(3302, 3283, 0)};
	public void poll() {
		switch(currentState = getState())
		{
		case kill:
			FightingEngine.GetInstance().SetContext(ctx).SetFightingArea(killSite).SetFood(ItemId.COOKED_MEAT)
						.SetTargets(NpcName.SCORPION).build().run();
			break;
		case withdraw:
			Actions.Interact.InteractWithObject(ctx, ObjectName.BANK_CHEST, Interact.USE);
			if(ctx.bank.withdrawMode()==true)ctx.bank.withdrawMode(false);
			ctx.bank.withdraw(ItemId.COOKED_MEAT,Amount.ALL);
			ctx.bank.close();
			break;
		case walk_to_bank:
			Traverse.TraversePathInReverse(ctx, bankToKillSite);
			break;
		case walk_to_site:
			Traverse.TraversePath(ctx, bankToKillSite);
			break;
		}
		previousState = currentState;
	}

	private State getState()
	{
		if(!LocalPlayer.Backpack.Has(ctx, ItemId.COOKED_MEAT) && !LocalPlayer.Location.Within(ctx, bankSite))
		{
			//if backpack doesnt have cooked meat and were not in the bank site. walk to the bank site
			return State.walk_to_bank;
		}
		else if(!LocalPlayer.Backpack.Has(ctx, ItemId.COOKED_MEAT) && LocalPlayer.Location.Within(ctx, bankSite))
		{
			//if backpack doesnt have cooked meat and were in the bank site withdraw food
			return State.withdraw;
		}
		else if(LocalPlayer.Location.Within(ctx, killSite) && LocalPlayer.Backpack.Has(ctx, ItemId.COOKED_MEAT))
		{
			//if were in the kill site and have cooked meat
			return State.kill;
		}
		else if(LocalPlayer.Location.Within(ctx, bankSite) && LocalPlayer.Backpack.Has(ctx, ItemId.COOKED_MEAT))
		{
			//if were in the bank site and have cooked meat
			return State.walk_to_site;
		}
		else if(LocalPlayer.Location.Within(ctx, killSite) && !LocalPlayer.Backpack.Has(ctx, ItemId.COOKED_MEAT))
		{
			//if were in the kill site without cooked meat
			return State.walk_to_bank;
		}
		return State.kill;
	}
	public enum State
	{
		walk_to_site, walk_to_bank, kill, withdraw
	}
}
