package Scripts;
import java.io.File;

import javax.swing.SwingUtilities;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Actions.GrandExchange;
import Chat.Messages;
import Constants.ItemId;
import Constants.ItemName;
import Engines.ChatEngine;
import Engines.StatisticsEngine;
import GUI.TestGui;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener	{
	
	int timer=0;
	Area fightingArea = new Area(new Tile(3007,3434,0), new Tile(3027,3458,0));
	public static File storageDirectory;
	public int setThisInt=0;
	boolean canRun=false;
	public void start()
	{
		ChatEngine.GetInstance().SetContext(ctx).SetDebug(true).build().start();
		StatisticsEngine.GetInstance().SetContext(ctx).build().run();
		storageDirectory = this.getStorageDirectory();
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				TestGui gui = new TestGui();
			}
		});
	}
	
	public void stop()
	{
		
	}
	public void poll() {	
		if(canRun==false)return;
		switch(getState())
		{
		case doThings:
			/*
			ctx.bank.open();
			ctx.bank.withdrawMode(false);
			ctx.bank.withdraw(ItemId.COWHIDE, Amount.ALL);
			ctx.bank.close();
			*/
			GrandExchange.GetPrice(ItemId.YEW_LOGS);
			ctx.controller.stop();
			break;
		case buryBones:
			//light the logs
			LocalPlayer.Backpack.Use(ctx, ItemName.BONES, Constants.Interact.BURY);
			break;
		}
		
	}
	
	public State getState()
	{
		
		if(LocalPlayer.Backpack.Has(ctx, ItemName.BONES))
		{
			return State.buryBones;
		}
		else
		{
			return State.doThings;			
		}
	}
	public enum State
	{
		doThings,buryBones
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
}
