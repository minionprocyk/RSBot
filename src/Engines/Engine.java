package Engines;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Engine extends ClientAccessor<ClientContext> implements Runnable{
	public Engine(ClientContext ctx)
	{
		super(ctx);
	}
	public abstract void run();
	public abstract Engine build();
}
