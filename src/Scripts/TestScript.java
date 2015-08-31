package Scripts;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Constants.Areas;
import Constants.Interact;
import Constants.ItemName;
import Engines.ChatEngine;
import Engines.SkillsEngine;
import Engines.SkillsEngine.SkillType;
import Engines.StatisticsEngine;
import GUI.TestGui;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener	{
	boolean runonce=true;
	//script initialize variables
	SkillType skillType;
	String[] objectNames;
	Area site;
	Area bankSite;
	int[] objectIds;
	boolean isBanking=true;
	public void start()
	{
		ChatEngine.GetInstance().SetContext(ctx).SetDebug(true).build().start();
		StatisticsEngine.GetInstance().SetContext(ctx).build().start();
		
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
			SkillsEngine.GetInstance().SetContext(ctx).SetSkill(skillType)
			.SetObject(objectNames).SetArea(site).SetBanking(isBanking).build().run();
			ctx.controller.stop();
			break;
		case buryBones:
			//light the logs
			LocalPlayer.Backpack.Use(ctx, ItemName.BONES, Interact.BURY);
			break;
		}
		
	}
	private void SetVariables()
	{
		String skillArea = TestGui.GetInstance().getSkillArea();
		if(skillArea.equals("Draynor Manor"))
		{
			site = Areas.VARROCK;
			bankSite = Areas.GRAND_EXCHANGE;
		}
		
		isBanking = TestGui.GetInstance().getBanking();
		skillType = TestGui.GetInstance().getSkillType();
		
		System.out.println("Skill Area = "+skillArea);
		System.out.println("isBanking = "+isBanking);
		System.out.println("skillType = "+skillType.toString());
	}
	public State getState()
	{
		
		if(LocalPlayer.Backpack.Has(ctx, ItemName.BONES))
		{
			TestGui.GetInstance().AppendDebugText("Burying bones");
			return State.buryBones;
		}
		else
		{
			TestGui.GetInstance().AppendDebugText("Doing things");
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
