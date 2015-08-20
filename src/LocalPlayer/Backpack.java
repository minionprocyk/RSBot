package LocalPlayer;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

public class Backpack {
	public static boolean isFull(ClientContext ctx)
	{
		return ctx.backpack.select().count() == 28 ? true : false;
	}
	public static boolean isEmpty(ClientContext ctx)
	{
		return ctx.backpack.select().isEmpty();
	}
	public static boolean hasStuff(ClientContext ctx)
	{
		return ctx.backpack.select().count() > 0 ? true : false;
	}
	public static boolean Has(ClientContext ctx, String name)
	{
		return ctx.backpack.select().name(name).first().poll().valid() ? true:false;
	}
	public static boolean Has(ClientContext ctx, Integer id)
	{
		return ctx.backpack.select().id(id).first().poll().valid() ? true:false;
	}
	public static int Count(ClientContext ctx, String name)
	{
		//count the number of this item
		return ctx.backpack.select().name(name).count();
	}
	public static int Count(ClientContext ctx)
	{
		return ctx.backpack.select().count();
	}
	public static void Use(ClientContext ctx, Item item, String action)
	{
		item.interact(action);
		Utility.Sleep.WhileNotIdle(ctx);
	}
	public static void Use(ClientContext ctx, String name, String action)
	{
		final Item item = ctx.backpack.select().name(name).first().poll();
		Use(ctx,item,action);
	}
	public static void Use(ClientContext ctx, Integer id, String action)
	{
		final Item item = ctx.backpack.select().id(id).first().poll();
		Use(ctx,item,action);
	}

}
