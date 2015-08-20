package LocalPlayer;

import org.powerbot.script.rt6.ClientContext;

import Constants.Interact;

public class GroundItems {
	public static void Take(ClientContext ctx, String[] items)
	{
		ctx.groundItems.select();
		ctx.groundItems.name(items).nearest().poll().interact(Interact.TAKE);
	}

}
