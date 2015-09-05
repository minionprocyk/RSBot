package Scripts;

import java.awt.Graphics;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
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
import Pathing.Traverse;
import Tasks.SimpleTask;

@Manifest(name = "Train Skills", description = "Train Mining, Fishing, Woodcutting in various locations", properties = "client=6; topic=0;")
public class TrainSkill extends PollingScript<ClientContext> implements MessageListener, PaintListener{
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
		StatisticsEngine.GetInstance().SetContext(ctx).SetName("Train Skills").build().start();
		
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
		System.out.println("Script is stopped");
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
			ctx.controller.stop();
			break;
		case deposit:
			SimpleTask.Deposit(ctx, true);
			break;
		case walk_to_bank:
			Traverse.TraversePath(ctx, pathToBank);
			break;
		case walk_to_site:
			Traverse.TraversePathInReverse(ctx, pathToBank);
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
		if(LocalPlayer.Location.Within(ctx, bankArea) && LocalPlayer.Backpack.hasStuff(ctx) && isBanking)
		{
			System.out.println("Depositing");
			TrainSkillGui.GetInstance().AppendDebugText("Depositing");
			return State.deposit;
		}
		else if(LocalPlayer.Location.Within(ctx, bankArea) && !LocalPlayer.Backpack.hasStuff(ctx))
		{
			System.out.println("Walking to site");
			TrainSkillGui.GetInstance().AppendDebugText("Walking to site");
			return State.walk_to_site;
		}
		else if(LocalPlayer.Location.Within(ctx, siteArea) && !LocalPlayer.Backpack.isFull(ctx))
		{
			System.out.println("Skilling up");
			TrainSkillGui.GetInstance().AppendDebugText("Skilling up");
			return State.start;
		}
		else if(LocalPlayer.Location.Within(ctx, siteArea) && LocalPlayer.Backpack.isFull(ctx))
		{
			System.out.println("Walking to bank");
			TrainSkillGui.GetInstance().AppendDebugText("Walking to bank");		
			return State.walk_to_bank;
		}
		else
		{
			System.out.println("No criteria met. Lets work this out");
			TrainSkillGui.GetInstance().AppendDebugText("No criteria met. Lets work this out");
			return State.start;
		}
	}
	public enum State
	{
		start, stop, deposit, walk_to_bank, walk_to_site
	}
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
	
	public void repaint(Graphics arg0) {
		StatisticsEngine.GetInstance().SetStringsToDraw(
				"Leveling "+StatisticsEngine.GetInstance().getSkill(StatisticsEngine.skill),
				"Levels Gained = "+StatisticsEngine.GetInstance().getLevelsGained(StatisticsEngine.skill),
				"EXP Gained = "+StatisticsEngine.GetInstance().getXPGained(StatisticsEngine.skill)+"xp",
				"EXP/HR = "+StatisticsEngine.GetInstance().getExperiencePerHour(StatisticsEngine.skill)+"xp/hr"
				).repaint(arg0);
		
	}
}
