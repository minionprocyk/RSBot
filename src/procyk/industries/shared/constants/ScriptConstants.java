package procyk.industries.shared.constants;

import org.powerbot.script.rt6.ClientContext;

public class ScriptConstants {
	//provide a basic context for basic scripts
	public static State currentState;
	public static State previousState;
	public static boolean loot=false;
	
	public static State GetState(ClientContext ctx)
	{
		if(procyk.industries.rt6.localplayer.Backpack.isFull(ctx))
		{
			currentState = State.walk_to_bank;
		}
		else
		{
			currentState = State.do_objective;
		}
		previousState=currentState;
		return currentState;
	}
	
	public static enum State
	{
		do_objective, 
		walk_to_bank, walk_to_objective
	}
}
