package Engines;

import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;

public class ChatEngine extends Thread implements Runnable {
	private boolean debug=false;
	private static ChatEngine ce;
	private static ClientContext ctx;
	private static Messages messages;
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
			System.out.println(Messages.GetLastReadMessage().getMessage());
			Utility.Sleep.Wait(5000);
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
