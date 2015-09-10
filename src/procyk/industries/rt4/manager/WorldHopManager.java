package procyk.industries.rt4.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

import procyk.industries.rt4.actions.Widgets;
import procyk.industries.rt4.utility.Sleep;
import procyk.industries.rt4.widgets.Search;
import procyk.industries.shared.constants.WidgetIdConstantsRT6;

public class WorldHopManager {
	private static WorldHopManager whm;
	private static ClientContext ctx;
	public static World CURRENT_WORLD=new World(0,0);
	List<World> previousWorlds = new ArrayList<World>();
	private WorldHopManager(){}
	public static WorldHopManager GetInstance()
	{
		if(whm==null)whm = new WorldHopManager();
		return whm;
	}
	public WorldHopManager SetContext(ClientContext ctx)
	{
		if(WorldHopManager.ctx==null)WorldHopManager.ctx=ctx;
		return this;
	}
	public void WorldHop(ClientContext ctx)
	{
		UpdatePreviousWorlds();
		GetCurrentWorld();
		try
		{
			int worldModifier=8;
			//ctx.hud.open(Menu.OPTIONS);
			Search.ForComponent(ctx, "Hop Worlds").click();
			Sleep.Wait(1500);
			for(int i=0;i<10;i++)
			{
				Component componentWorldPlayers = ctx.widgets.component(WidgetIdConstantsRT6.WORLD_SELECT, WidgetIdConstantsRT6.WORLD_SELECT_WORLD).component(WidgetIdConstantsRT6.WORLD_SELECT_WORLD_PLAYERS +i*worldModifier);
				Component componentWorldNumber = ctx.widgets.component(WidgetIdConstantsRT6.WORLD_SELECT, WidgetIdConstantsRT6.WORLD_SELECT_WORLD).component(WidgetIdConstantsRT6.WORLD_SELECT_WORLD_NUMBER +i*worldModifier);
				int worldNumber = Integer.parseInt(componentWorldNumber.text().trim());
				int worldPlayers = Integer.parseInt(componentWorldPlayers.text().trim());
				System.out.println("World "+worldNumber+" | Players = "+worldPlayers);
				World hopWorld = new World(worldNumber, worldPlayers);
				if(worldPlayers < 200 && worldNumber != CURRENT_WORLD.getWorldNumber() && !previousWorlds.contains(hopWorld))
				{
					previousWorlds.add(hopWorld);
					//click the row
					Widgets.Click(ctx, WidgetIdConstantsRT6.WORLD_SELECT,WidgetIdConstantsRT6.WORLD_SELECT_WORLD, (i * worldModifier));
					
					//click yes
					Widgets.Click(ctx, WidgetIdConstantsRT6.WORLD_SELECT,WidgetIdConstantsRT6.WORLD_SELECT_CONFIRM_YES);
					break;
				}
			}
			Sleep.Wait(10000);
		}
		catch(NumberFormatException e)
		{
			System.out.println("Integer.parseInt failed to read a value");
		}
		
	}
	private void GetCurrentWorld()
	{
		int currentWorldNumber = Integer.parseInt(ctx.widgets.component(WidgetIdConstantsRT6.FRIENDS_LIST, WidgetIdConstantsRT6.FRIENDS_LIST_WORLDNUMBER).text().split("RuneScape ")[1].trim());
		CURRENT_WORLD = new World(currentWorldNumber,0);
		System.out.println("Current World = "+CURRENT_WORLD.getWorldNumber());
	}
	private void UpdatePreviousWorlds()
	{
		//removes Worlds logged int more than 30 seconds ago
		for(Iterator<World> iWorlds = previousWorlds.iterator(); iWorlds.hasNext();)
		{
			World nextWorld = iWorlds.next();				
			if(Math.abs(System.currentTimeMillis()-nextWorld.getTimeLoggedIn())>(30*1000)){
				//if current time in mili > message.time in mili 
				System.out.println("Removing World: "+nextWorld.getWorldNumber());
				iWorlds.remove();
			}
		}
		System.out.println("Avoidable objects updated.");
	}
	public WorldHopManager build()
	{
		return this;
	}
}
