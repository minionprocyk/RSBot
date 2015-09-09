package procyk.industries.quests;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Objective extends ClientAccessor{
	public String objectiveName="";
	public boolean completed=false;
	public Objective(ClientContext ctx)
	{
		super(ctx);
	}
	public abstract boolean ready();
	public abstract void perform();
	
}
