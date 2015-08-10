package Scripts;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Constants.ObjectName;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener{
	MessageListener ml=null;
	public void poll() {		
		if(Player.Backpack.Contains(ctx, ObjectName.RAW_BEEF))System.out.println("We have "+Player.Backpack.Count(ctx, ObjectName.RAW_BEEF)+" raw beef");
		Utility.Sleep.WaitRandomTime(1,1000);
	}
	
	public void messaged(MessageEvent message) {
		Messages.AddPastReadMessages(message.source(), message.text());
	}

}
