package procyk.industries.rt4.engines;

import org.powerbot.script.rt4.ClientContext;

import procyk.industries.rt4.utility.Sleep;
import procyk.industries.shared.chat.Message;
import procyk.industries.shared.chat.Messages;

public class ChatEngine extends Thread implements Runnable {
	private boolean debug=false;
	private static ChatEngine ce;
	private static ClientContext ctx;
	private static Messages messages;
	private Message lastMessage;
	private ChatEngine()
	{
	}
	public static ChatEngine GetInstance()
	{
		if(ce==null)
		{
			if(messages==null)
			{
				messages = new Messages();
			}
			ce = new ChatEngine();
		}
		return ce;
	}
	public ChatEngine SetContext(ClientContext ctx)
	{
		if(ChatEngine.ctx==null)
		{
			ChatEngine.ctx=ctx;
		}
		return this;
	}
	public void run()
	{
		while(!ChatEngine.ctx.controller.isStopping())
		{
			lastMessage = Messages.GetLastReadMessage();
			if(Messages.GetLastReadMessage().getMessage().equals(lastMessage.getMessage()))
			{
				//if the last message was the same do nothing
				Sleep.Wait(2500);
			}
			else
			{
				System.out.println(Messages.GetLastReadMessage().getMessage());
			}
			Sleep.Wait(2500);
		}
		
	}
	public ChatEngine SetDebug(boolean fSet)
	{
		this.debug=fSet;
		return this;
	}
	public ChatEngine build()
	{
		return this;
	}
}
