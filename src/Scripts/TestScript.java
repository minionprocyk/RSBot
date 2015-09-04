package Scripts;
import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Constants.Areas;
import Constants.Interact;
import Engines.StatisticsEngine;
import Pathing.Traverse;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener	{

	Area area = Areas.VARROCK;
	Tile destination = new Tile(3217,3415,0);

	public void start()
	{
		StatisticsEngine.GetInstance().SetContext(ctx).build().start();
	}
	public void stop()
	{
		System.out.println("Stopping script");
	}
	public void poll() {	
		
		switch(getState())
		{
		case doThings:
			Utility.Sleep.Wait(20000);
			ctx.controller.stop();
			break;
		case buryBones:

			break;
		}
		
	}
	public State getState()
	{
		return State.doThings;
	}
	public enum State
	{
		doThings,buryBones
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
}
