package procyk.industries.rt4.quests;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

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
