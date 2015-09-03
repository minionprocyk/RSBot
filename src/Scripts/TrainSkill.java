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
import Engines.ChatEngine;
import Engines.SkillsEngine;
import Engines.SkillsEngine.SkillType;
import Engines.StatisticsEngine;
import GUI.TrainSkillGui;

@Manifest(name = "Train Skills", description = "Train Mining, Fishing, Woodcutting in various locations", properties = "client=6; topic=0;")
public class TrainSkill extends PollingScript<ClientContext> implements MessageListener{
	State currentState=null, previousState=null;
	boolean runonce=true;
	public static File STORAGE_DIRECTORY;
	Area siteArea, bankArea;
	SkillType skillType;
	String[] objects;
	Tile[] pathToBank;
	boolean isBanking=true;
	public void start()
	{
		ChatEngine.GetInstance().SetContext(ctx).build().start();
		StatisticsEngine.GetInstance().SetContext(ctx).build().start();
		STORAGE_DIRECTORY=getStorageDirectory();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					TrainSkillGui.GetInstance().setVisible(true);
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
		TrainSkillGui.GetInstance().destroy();
	}
	public void poll() {
		if(TrainSkillGui.GetInstance().stop)ctx.controller.stop();
		if(TrainSkillGui.GetInstance().wait)return;
		if(runonce)
		{
			runonce=false;
			SetVariables();
		}
		switch(currentState=getState())
		{
		case start:
			SkillsEngine.GetInstance().SetContext(ctx).SetSkill(skillType).SetArea(siteArea).SetBanking(isBanking).SetObject(objects).build().run();
			break;
		case stop:
			break;
		default:
			break;
		
		}
		previousState=currentState;
	}
	
	private void SetVariables()
	{
		siteArea = TrainSkillGui.GetInstance().getSiteArea();
		bankArea = TrainSkillGui.GetInstance().getBankArea();
		skillType = TrainSkillGui.GetInstance().getSkillType();
		objects = TrainSkillGui.GetInstance().getObjects();
		pathToBank = TrainSkillGui.GetInstance().getPathToBank();
		isBanking = TrainSkillGui.GetInstance().getBanking();
	}
	public State getState()
	{
		
		return State.start;
	}
	public enum State
	{
		start, stop
	}
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
}
