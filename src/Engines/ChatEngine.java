package Engines;

import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;

public class ChatEngine extends Engine {
	private boolean debug=false;
	Messages messages;
	public ChatEngine(ClientContext ctx)
	{
		super(ctx);
	}
	public void run()
	{
		while(!ctx.controller.isStopping())
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
		this.messages = new Messages();
		return this;
	}
}
