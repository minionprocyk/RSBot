package procyk.industries.rt4.localplayer;

import org.powerbot.script.rt4.ClientContext;

import procyk.industries.shared.constants.InteractConstants;

public class GroundItems {
	public static void Take(ClientContext ctx, String[] items)
	{
		ctx.groundItems.select();
		ctx.groundItems.name(items).nearest().poll().interact(InteractConstants.TAKE);
	}

}
