package Scripts;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Engines.ChatEngine;
import Engines.StatisticsEngine;
import GUI.TestGui;

//@Manifest(name = "scriptName", description = "scriptDescription", properties = "client=6; topic=0;")
public class Script extends PollingScript<ClientContext> implements MessageListener{
	State currentState=null, previousState=null;
	boolean runonce=true;
	public static File STORAGE_DIRECTORY;
	public void start()
	{
		ChatEngine.GetInstance().SetContext(ctx).build().start();
		StatisticsEngine.GetInstance().SetContext(ctx).build().start();
		STORAGE_DIRECTORY=getStorageDirectory();
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
		switch(currentState=getState())
		{
		case start:
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
		//variable = TestGui.GetInstance().GetVariable();
	}
	public State getState()
	{
		return null;
	}
	public enum State
	{
		start, stop
	}
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
}
