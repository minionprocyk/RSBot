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
		Widget widget = Widgets.Search.ForWidget(ctx, "smelt");
		if(widget == null)
		{
			//we nede to interact with a furnace first. if we do it successfully. try smelting again
			if(Actions.Interact.InteractWithObject(ctx, ObjectName.FURNACE, Interact.SMELT))
			{
				Smelt(ctx);
			}
		}
		Component component = Widgets.Search.ForComponent(ctx, "smelt");
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
			
			lastCount = ctx.backpack.select().count();
			Utility.Sleep.WhileNotIdle(ctx);
		}while(lastCount != ctx.backpack.select().count());
		
	}
	public static void Smith()
	{
		
	}
	public static void Deposit(ClientContext ctx)
	{
		if(!ctx.bank.opened())ctx.bank.open();
		ctx.bank.depositInventory();
		ctx.bank.close();
	}

}
