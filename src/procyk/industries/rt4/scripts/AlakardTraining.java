package procyk.industries.rt4.scripts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Bank.Amount;
import org.powerbot.script.rt4.ClientContext;

import procyk.industries.rt4.actions.Interact;
import procyk.industries.rt4.engines.ChatEngine;
import procyk.industries.rt4.engines.SkillsEngine;
import procyk.industries.rt4.localplayer.Backpack;
import procyk.industries.rt4.localplayer.Location;
import procyk.industries.rt4.pathing.Traverse;
import procyk.industries.rt4.tasks.SimpleTask;
import procyk.industries.shared.chat.Messages;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.ItemIdConstants;
import procyk.industries.shared.constants.ObjectNameConstants;
import procyk.industries.shared.gui.SkillType;

@Manifest(name = "AlAkard Scorpians", description = "kill scorpians and restock food", properties = "client=4; topic=0;")

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
				.SetArea(killSite).SetObject(ObjectNameConstants.IRON_ROCKS).build().run();
			
			break;
		case withdraw:
			
			Interact.InteractWithObject(ctx, ObjectNameConstants.BANK_CHEST, InteractConstants.USE);
			if(ctx.bank.withdrawModeNoted())ctx.bank.withdrawModeNoted(false);
			ctx.bank.withdraw(ItemIdConstants.COOKED_MEAT,Amount.ALL);
			ctx.bank.close();
			break;
		case deposit:
			Interact.InteractWithObject(ctx, ObjectNameConstants.BANK_CHEST, InteractConstants.USE);
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
		if(Backpack.isFull(ctx) && !Location.Within(ctx, bankSite))
		{
			//if backpack doesnt have cooked meat and were not in the bank site. walk to the bank site
			return State.walk_to_bank;
		}
		else if(Backpack.hasStuff(ctx) && Location.Within(ctx, bankSite))
		{
			//if backpack doesnt have cooked meat and were in the bank site withdraw food
			return State.deposit;
		}
		else if(Location.Within(ctx, killSite) && !Backpack.isFull(ctx))
		{
			//if were in the kill site and have cooked meat
			return State.kill;
		}
		else if(Location.Within(ctx, bankSite) && Backpack.isEmpty(ctx) || previousState==State.deposit)
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
