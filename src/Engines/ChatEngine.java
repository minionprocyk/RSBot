package Engines;

import org.powerbot.script.rt6.ClientContext;

import Chat.Messages;

public class ChatEngine extends Thread{
	private ClientContext ctx;
	private boolean debug=false;
	Messages messages;
	public ChatEngine()
	{
	}
	public void run()
	{
		while(!ctx.controller.isStopping())
		{
			System.out.println(Messages.GetLastReadMessage().getMessage());
			Utility.Sleep.Wait(1000);
		}
		
	}
	public ChatEngine SetContext(ClientContext ctx)
	{
		this.ctx = ctx;
		return this;
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
