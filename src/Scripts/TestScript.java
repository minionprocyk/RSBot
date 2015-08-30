package Scripts;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Constants.ItemName;
import Constants.ObjectName;
import Engines.ChatEngine;
import Engines.SkillsEngine;
import Engines.SkillsEngine.SkillType;
import Engines.StatisticsEngine;
import GUI.TestGui;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener	{
	Area chopSite = new Area(new Tile(3077, 3241, 0), new Tile(3101, 3216, 0));

	int timer=0;
	Area fightingArea = new Area(new Tile(3007,3434,0), new Tile(3027,3458,0));
	public static File storageDirectory;
	public int setThisInt=0;
	boolean runonce=true;
	public void start()
	{
		ChatEngine.GetInstance().SetContext(ctx).SetDebug(true).build().start();
		StatisticsEngine.GetInstance().SetContext(ctx).build().start();
		storageDirectory = this.getStorageDirectory();
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					TestGui.GetInstance();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void stop()
	{
		TestGui.GetInstance().destroy();
	}
	public void poll() {	
		if(TestGui.GetInstance().stop)ctx.controller.stop();
		if(TestGui.GetInstance().wait)return;
		if(runonce)
		{
			runonce=false;
			SetVariables();
		}
		switch(getState())
		{
		case doThings:
			/*
			ctx.bank.open();
			ctx.bank.withdrawMode(false);
			ctx.bank.withdraw(ItemId.COWHIDE, Amount.ALL);
			ctx.bank.close();
			*/
			SkillsEngine.GetInstance().SetContext(ctx).SetSkill(SkillType.Woodcutting)
			.SetObject(ObjectName.WILLOW).SetArea(chopSite).build().run();
			//WorldHopManager.GetInstance().SetContext(ctx).testEquals();
			ctx.controller.stop();
			break;
		case buryBones:
			//light the logs
			LocalPlayer.Backpack.Use(ctx, ItemName.BONES, Constants.Interact.BURY);
			break;
		}
		
	}
	private void SetVariables()
	{
		String skillArea = TestGui.GetInstance().getSkillArea();
		boolean isBanking = TestGui.GetInstance().getBanking();
		SkillType skillType = TestGui.GetInstance().getSkillType();
		
		System.out.println("Skill Area = "+skillArea);
		System.out.println("isBanking = "+isBanking);
		System.out.println("skillType = "+skillType.toString());
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
