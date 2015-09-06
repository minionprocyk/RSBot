package Tasks;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import Constants.Animation;
import Constants.Interact;
import Constants.ObjectName;
import Manager.WorldHopManager;

public class SimpleTask{
	public static void Smelt(ClientContext ctx)
	{
		//search for a widget
		try
		{
			Component component = Widgets.Search.ForComponent(ctx, Constants.WidgetSearchName.SMELT);
		
			component.click();
			
			int lastCount = 0;
			boolean smelting=false;
			System.out.println("Smelting in progress...");
			do
			{
				
				lastCount = LocalPlayer.Backpack.Count(ctx);
				
				Utility.Sleep.WhilePlayer(ctx, Animation.PLAYER_SMELTING);
				smelting = LocalPlayer.Animation.PlayerAnimation(ctx) == Animation.PLAYER_SMELTING ? true 
						   :lastCount != LocalPlayer.Backpack.Count(ctx) ? true: false;
			}while(smelting);
		}
		catch(NullPointerException e)
		{
			System.out.println("Cannot find smelt widget");
			Actions.Interact.InteractWithObject(ctx, ObjectName.FURNACE, Interact.SMELT);
		}
		
		
		
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
	public static void Loot(ClientContext ctx, String... item)
	{
		
		ctx.groundItems.select().name(item).nearest().poll().interact(Interact.TAKE);
		Utility.Sleep.WhileRunning(ctx);
		if(ctx.widgets.component(1622, 14).valid())ctx.widgets.component(1622, 14).click();//loot all
	}
	public static void LootAll(ClientContext ctx)
	{
		Widgets.Search.ForComponent(ctx, Constants.WidgetSearchName.LOOT).click();
	}
	public static void WorldHop(ClientContext ctx)
	{
		WorldHopManager.GetInstance().SetContext(ctx).build().WorldHop(ctx);
		
	}

}
