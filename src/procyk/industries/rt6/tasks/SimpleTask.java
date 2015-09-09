package procyk.industries.rt6.tasks;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import procyk.industries.rt6.actions.Interact;
import procyk.industries.rt6.localplayer.Animation;
import procyk.industries.rt6.localplayer.Backpack;
import procyk.industries.rt6.manager.WorldHopManager;
import procyk.industries.rt6.utility.Sleep;
import procyk.industries.rt6.widgets.Search;
import procyk.industries.shared.constants.AnimationConstants;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.ObjectNameConstants;
import procyk.industries.shared.constants.WidgetSearchNameConstants;

public class SimpleTask{
	public static void Smelt(ClientContext ctx)
	{
		//search for a widget
		try
		{
			Component component = Search.ForComponent(ctx, WidgetSearchNameConstants.SMELT);
		
			component.click();
			
			int lastCount = 0;
			boolean smelting=false;
			System.out.println("Smelting in progress...");
			do
			{	
				lastCount = Backpack.Count(ctx);
				
				Sleep.WhilePlayer(ctx, AnimationConstants.PLAYER_SMELTING);
				smelting = Animation.PlayerAnimation(ctx) == AnimationConstants.PLAYER_SMELTING ? true 
						   :lastCount != Backpack.Count(ctx) ? true: false;
			}while(smelting);
		}
		catch(NullPointerException e)
		{
			System.out.println("Cannot find smelt widget");
			Interact.InteractWithObject(ctx, ObjectNameConstants.FURNACE, InteractConstants.SMELT);
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
		
		ctx.groundItems.select().name(item).nearest().poll().interact(InteractConstants.TAKE);
		Sleep.WhileRunning(ctx);
		if(ctx.widgets.component(1622, 14).valid())ctx.widgets.component(1622, 14).click();//loot all
	}
	public static void LootAll(ClientContext ctx)
	{
		Search.ForComponent(ctx, WidgetSearchNameConstants.LOOT).click();
	}
	public static void WorldHop(ClientContext ctx)
	{
		WorldHopManager.GetInstance().SetContext(ctx).build().WorldHop(ctx);
		
	}

}
