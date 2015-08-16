package Scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;
import Tasks.Task;
import Tasks.WalkToBurd;

//@Manifest(name = "scriptName", description = "scriptDescription", properties = "client=6; topic=0;")
public class Script extends PollingScript<ClientContext> implements MessageListener{
	List<Task> tasks  = new ArrayList<Task>();
	
	public void start()
	{
		tasks.addAll(Arrays.asList( new WalkToBurd(ctx)));
	}
	
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
	public State getState()
	{
		return null;
	}
	public enum State
	{
		start, stop
	}
}
