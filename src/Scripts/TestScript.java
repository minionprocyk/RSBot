package Scripts;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Actions.Interact;
import Chat.Messages;
import Constants.ItemName;
import Constants.ObjectName;
import Engines.ChatEngine;
import Tasks.SimpleTask;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener	{
	String[] rocks = new String[]{ObjectName.COPPER_ROCKS, ObjectName.TIN_ROCKS, ObjectName.IRON_ROCKS};
	Tile tile = new Tile(3381, 3270, 0);
	boolean init=true;
	String[] trees = new String[]{ObjectName.TREE};
	public void poll() {	
		if(init)
		{
			init=false;
			ChatEngine.GetInstance().SetContext(ctx).SetDebug(true).build().start();
		}
		switch(getState())
		{
		case doThings:
			SimpleTask.Smelt(ctx);
			ctx.controller.stop();
			break;
		case goal:
			//light the logs
			System.out.println("Ok we reached out goal");
			ctx.controller.stop();
			break;
		}
		
	}
	
	public State getState()
	{
		
		if(LocalPlayer.Backpack.Count(ctx, ItemName.LOGS) == 18)
		{
			return State.goal;
		}
		else
		{
			return State.doThings;			
		}
	}
	public enum State
	{
		doThings,goal
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
}
