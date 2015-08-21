package Tasks;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Widget;

import Constants.Interact;
import Constants.ObjectName;

public class SimpleTask extends ClientAccessor<ClientContext>{
	public SimpleTask(ClientContext ctx)
	{
		super(ctx);
	}
	public static void Smelt(ClientContext ctx)
	{
		//search for a widget
		Widget widget = Widgets.Search.ForWidget(ctx, Constants.WidgetSearchName.SMELT);
		if(widget == null)
		{
			//we nede to interact with a furnace first. if we do it successfully. try smelting again
			if(Actions.Interact.InteractWithObject(ctx, ObjectName.FURNACE, Interact.SMELT))
			{
				Smelt(ctx);
			}
		}
		Component component = Widgets.Search.ForComponent(ctx, Constants.WidgetSearchName.SMELT);
		if(component == null)
		{
			System.out.println("Cannot find component");
		}
		else
		{
			component.click();
		}
		int lastCount = 0;
		do
		{
			
			lastCount = LocalPlayer.Backpack.Count(ctx);
			Utility.Sleep.WhileNotIdle(ctx);
		}while(lastCount != LocalPlayer.Backpack.Count(ctx));
		
	}
	public static void Smith()
	{
		
	}
	public static void Deposit(ClientContext ctx, boolean closeBank)
	{
		if(!ctx.bank.opened())ctx.bank.open();
		ctx.bank.depositInventory();
		if(closeBank)ctx.bank.close();
	}
	public static void LootAll(ClientContext ctx)
	{
		Widgets.Search.ForComponent(ctx, Constants.WidgetSearchName.LOOT).click();
	}

}
