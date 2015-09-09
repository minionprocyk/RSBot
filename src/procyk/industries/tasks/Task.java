package procyk.industries.tasks;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Task extends ClientAccessor<ClientContext>{
	public Task(ClientContext ctx)
	{
		super(ctx);
	}
	public abstract boolean ready();
	public abstract void execute();
}
