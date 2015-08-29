package Scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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
import Engines.ChatEngine;
import Engines.SkillsEngine;
import Engines.SkillsEngine.SkillType;
import Pathing.Traverse;
import Tasks.SimpleTask;

@Manifest(name = "AlAkard Scorpians", description = "kill scorpians and restock food", properties = "client=6; topic=0;")

public class AlakardTraining extends PollingScript<ClientContext> implements MessageListener{
	State currentState=null, previousState=null;
	Area killSite = new Area(new Tile(3289, 3270, 0), new Tile(3321, 3318, 0));

	Area bankSite = new Area(new Tile(3373, 3265, 0), new Tile(33855, 3276, 0));
	Tile[] bankToKillSite = new Tile[]{new Tile(3380, 3270, 0), new Tile(3346, 3274, 0), 
						 	new Tile(3318, 3280, 0), new Tile(3302, 3283, 0)};
	long now = System.currentTimeMillis();
	public void start()
	{
		
		ChatEngine.GetInstance().SetContext(ctx).build().start();
		new Timer(1000,new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(Math.abs(now-System.currentTimeMillis()) > 1000*60*10)
				{
					System.out.println("Timer is up. Stopping execution");
					ctx.controller.stop();
					
				}
			}
		}).start();
	}
	
	public void poll() {
		switch(currentState = getState())
		{
		case kill:
			//FightingEngine.GetInstance().SetContext(ctx).SetFightingArea(killSite).SetFood(ItemId.COOKED_MEAT).SetTargets(NpcName.SCORPION).build().run();
			
			SkillsEngine.GetInstance().SetContext(ctx).SetSkill(SkillType.Mining)
				.SetArea(killSite).SetObject(ObjectName.IRON_ROCKS).build().run();
			
			break;
		case withdraw:
			Actions.Interact.InteractWithObject(ctx, ObjectName.BANK_CHEST, Interact.USE);
			if(ctx.bank.withdrawMode()==true)ctx.bank.withdrawMode(false);
			ctx.bank.withdraw(ItemId.COOKED_MEAT,Amount.ALL);
			ctx.bank.close();
			break;
		case deposit:
			Actions.Interact.InteractWithObject(ctx, ObjectName.BANK_CHEST, Interact.USE);
			SimpleTask.Deposit(ctx, true);
			break;
		case walk_to_bank:
			Traverse.TraversePathInReverse(ctx, bankToKillSite);
			break;
		case walk_to_site:
			Traverse.TraversePath(ctx, bankToKillSite);
			break;
		}
		previousState = currentState;
		System.out.println("Last state = "+previousState.toString());
	}

	private State getState()
	{
		if(LocalPlayer.Backpack.isFull(ctx) && !LocalPlayer.Location.Within(ctx, bankSite))
		{
			//if backpack doesnt have cooked meat and were not in the bank site. walk to the bank site
			return State.walk_to_bank;
		}
		else if(LocalPlayer.Backpack.hasStuff(ctx) && LocalPlayer.Location.Within(ctx, bankSite))
		{
			//if backpack doesnt have cooked meat and were in the bank site withdraw food
			return State.deposit;
		}
		else if(LocalPlayer.Location.Within(ctx, killSite) && !LocalPlayer.Backpack.isFull(ctx))
		{
			//if were in the kill site and have cooked meat
			return State.kill;
		}
		else if(LocalPlayer.Location.Within(ctx, bankSite) && LocalPlayer.Backpack.isEmpty(ctx) || previousState==State.deposit)
		{
			//if were in the bank site and have cooked meat
			return State.walk_to_site;
		}
		return State.kill;
	}
	public enum State
	{
		walk_to_site, walk_to_bank, kill, withdraw, deposit
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
}
