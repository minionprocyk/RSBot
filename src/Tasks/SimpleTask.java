package Tasks;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Hud.Menu;

import Constants.Animation;
import Constants.Interact;
import Constants.ObjectName;
import Constants.WidgetId;

public class SimpleTask extends ClientAccessor<ClientContext>{
	public SimpleTask(ClientContext ctx)
	{
		super(ctx);
	}
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
	public static void LootAll(ClientContext ctx)
	{
		Widgets.Search.ForComponent(ctx, Constants.WidgetSearchName.LOOT).click();
	}
	public static void WorldHop(ClientContext ctx, int world)
	{
		//open the game context menu
		//select hop worlds
		//hop a world
		//from the 'World_Select_World' each world has 8 components
		try
		{
			int worldModifier=8;
			int currentWorld= Integer.parseInt(ctx.widgets.component(WidgetId.FRIENDS_LIST, WidgetId.FRIENDS_LIST_WORLDNUMBER).text().split("RuneScape ")[1].trim());
			System.out.println("Current World = "+currentWorld);
			ctx.hud.open(Menu.OPTIONS);
			Widgets.Search.ForComponent(ctx, "Hop Worlds").click();
			Utility.Sleep.Wait(1500);
			for(int i=0;i<10;i++)
			{
				if(world>0)
				{
					world--;
					continue;
				}
				Component worldPlayers = ctx.widgets.component(WidgetId.WORLD_SELECT, WidgetId.WORLD_SELECT_WORLD).component(WidgetId.WORLD_SELECT_WORLD_PLAYERS +i*worldModifier);
				Component worldNumber = ctx.widgets.component(WidgetId.WORLD_SELECT, WidgetId.WORLD_SELECT_WORLD).component(WidgetId.WORLD_SELECT_WORLD_NUMBER +i*worldModifier);
				System.out.println("World "+worldNumber.text()+" | Players = "+worldPlayers.text());
				if(Integer.parseInt(worldPlayers.text().trim()) < 200 && Integer.parseInt(worldNumber.text()) != currentWorld)
				{
					//click the row
					Actions.Widgets.Click(ctx, WidgetId.WORLD_SELECT,WidgetId.WORLD_SELECT_WORLD, (i * worldModifier));
					
					//click yes
					Actions.Widgets.Click(ctx, WidgetId.WORLD_SELECT,WidgetId.WORLD_SELECT_CONFIRM_YES);
					break;
				}
			}
			Utility.Sleep.Wait(10000);
		}
		catch(NumberFormatException e)
		{
			System.out.println("Integer.parseInt failed to read a value");
		}
		
	}

}
