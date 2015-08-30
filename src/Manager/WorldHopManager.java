package Manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Hud.Menu;

import Constants.WidgetId;

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
			ctx.hud.open(Menu.OPTIONS);
			Widgets.Search.ForComponent(ctx, "Hop Worlds").click();
			Utility.Sleep.Wait(1500);
			for(int i=0;i<10;i++)
			{
				Component componentWorldPlayers = ctx.widgets.component(WidgetId.WORLD_SELECT, WidgetId.WORLD_SELECT_WORLD).component(WidgetId.WORLD_SELECT_WORLD_PLAYERS +i*worldModifier);
				Component componentWorldNumber = ctx.widgets.component(WidgetId.WORLD_SELECT, WidgetId.WORLD_SELECT_WORLD).component(WidgetId.WORLD_SELECT_WORLD_NUMBER +i*worldModifier);
				int worldNumber = Integer.parseInt(componentWorldNumber.text().trim());
				int worldPlayers = Integer.parseInt(componentWorldPlayers.text().trim());
				System.out.println("World "+worldNumber+" | Players = "+worldPlayers);
				World hopWorld = new World(worldNumber, worldPlayers);
				if(worldPlayers < 200 && worldNumber != CURRENT_WORLD.getWorldNumber() && !previousWorlds.contains(hopWorld))
				{
					previousWorlds.add(hopWorld);
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
	private void GetCurrentWorld()
	{
		int currentWorldNumber = Integer.parseInt(ctx.widgets.component(WidgetId.FRIENDS_LIST, WidgetId.FRIENDS_LIST_WORLDNUMBER).text().split("RuneScape ")[1].trim());
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
	public void testEquals()
	{
		World world1 = new World(1,50);
		World world2 = new World(2,60);
		previousWorlds.add(world2);
		previousWorlds.add(world1);
		System.out.println("World1 contained in list? : "+previousWorlds.contains(new World(1,200)));
	}
	public WorldHopManager build()
	{
		return this;
	}
}
