package procyk.industries.rt4.scripts;
import java.awt.Graphics;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import procyk.industries.rt4.engines.FightingEngine;
import procyk.industries.rt4.engines.StatisticsEngine;
import procyk.industries.rt4.localplayer.Backpack;
import procyk.industries.shared.chat.Messages;
import procyk.industries.shared.constants.AreaConstants;
import procyk.industries.shared.constants.InteractConstants;
import procyk.industries.shared.constants.ItemIdConstants;
import procyk.industries.shared.constants.ItemNameConstants;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=4; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener, PaintListener{

	Area area = AreaConstants.VARROCK;
	Tile destination = new Tile(3217,3415,0);

	public void start()
	{
		StatisticsEngine.GetInstance().SetContext(ctx).SetName("Test").build().start();
	}
	public void stop()
	{
		System.out.println("Stopping script");
	}
	public void poll() {	
		
		switch(getState())
		{
		case doThings:
			//test to ensure avoid players is working.
			//find the closest npc that is in combat, 
			
			FightingEngine.GetInstance().SetContext(ctx).SetTargets("Cow").SetLoot(ItemNameConstants.BONES).build().run();
			
			if(Backpack.Has(ctx, ItemIdConstants.BONES) && Backpack.Count(ctx) > Random.nextInt(3, 25))
			{
				Backpack.Use(ctx, ItemIdConstants.BONES, InteractConstants.BURY);
			}
			break;
		case buryBones:

			break;
		}
		
	}
	public State getState()
	{
		return State.doThings;
	}
	public enum State
	{
		doThings,buryBones
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
	public void repaint(Graphics graphics) {
		StatisticsEngine.GetInstance().build().repaint(graphics);
		
	}
}
