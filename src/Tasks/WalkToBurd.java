package Tasks;

import org.powerbot.script.rt6.ClientContext;

public class WalkToBurd extends Task{
	
	public WalkToBurd(ClientContext ctx) {
		super(ctx);
	}
	public boolean ready()
	{
		
		return false;
		
	}
	public void execute()
	{
		System.out.println("Task executed");
	}

}
