package procyk.industries.localplayer;

import org.powerbot.script.rt6.ClientContext;

import procyk.industries.constants.InteractConstants;

public class GroundItems {
	public static void Take(ClientContext ctx, String[] items)
	{
		ctx.groundItems.select();
		ctx.groundItems.name(items).nearest().poll().interact(InteractConstants.TAKE);
	}

}
