package Scripts;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Tasks.Task;
import Tasks.WalkToBurd;

public class Generic extends PollingScript<ClientContext> implements MessageListener{
	Task[] tasks = new Task[]{new WalkToBurd(ctx)};
	public void poll() {
		for(Task t: tasks)
		{
			if(t.ready())
			{
				t.execute();
			}
		}
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}

}
