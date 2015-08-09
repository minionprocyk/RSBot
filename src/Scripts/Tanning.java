package Scripts;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Player;

@Manifest(name = "Dragonhide Tanning", description = "spawn crafter and tan stuff bitch", properties = "client=6; topic=0;")
public class Tanning extends PollingScript<ClientContext>{
	Player localPlayer = ctx.players.local();
	int idValue;
	boolean tried=false;
	public void poll() {
		
		smelt();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctx.controller.stop();
	}
	
	private void smelt()
	{
		final GameObject object = ctx.objects.select().name("Furnace").nearest().poll();
		final Component Smith = ctx.widgets.component(1370, 33);
		Smith.interact("Make");
		
		
		//ctx.menu.hover(Menu.filter("Make", "1 Bronze bar"));
		//ctx.menu.click(Menu.filter("Make","1 Bronze bar"));
		
		//System.out.println("Menu is opened = "+opened);
	}
	private boolean hasPortableCrafter()
	{
		final GameObject object = ctx.objects.select().name("Tree").nearest().poll();
		object.interact("Examine");
		if(object.inViewport())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}

}
