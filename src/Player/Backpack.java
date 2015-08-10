package Player;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

public class Backpack {
	public static boolean isFull(ClientContext ctx)
	{
		return ctx.backpack.select().count() == 28 ? true : false;
	}
	public static boolean Contains(ClientContext ctx, String name)
	{
		//get the item by name
		final Item item = ctx.backpack.select().name(name).first().poll();
		return item.valid() ? true:false;
	}
	public static int Count(ClientContext ctx, String name)
	{
		//count the number of this item
		return ctx.backpack.select().name(name).count();
	}
	public static void Use(ClientContext ctx, String name, String action)
	{
		final Item item = ctx.backpack.select().name(name).first().poll();
		item.interact(action);
	}

}
