package Scripts;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener{
	MessageListener ml=null;
	public void poll() {		
		Utility.Sleep.WaitRandomTime(1,1000);
		ctx.controller.stop();
	}
	
	public void messaged(MessageEvent message) {
		Messages.AddPastReadMessages(message.source(), message.text());
	}

}
