package Scripts;
import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;

import Actions.ChatOptions;
import Chat.Messages;
import Constants.Interact;
import Constants.ItemName;
import Constants.NpcId;
import Constants.NpcName;
import Constants.ObjectName;
import Constants.WidgetId;
import Engines.ChatEngine;
import Engines.QuestEngine;
import Engines.StatisticsEngine;
import Quests.TheBloodPact;

@Manifest(name = "Test", description = "We do crazy things", properties = "client=6; topic=0;")
public class TestScript  extends PollingScript<ClientContext> implements MessageListener	{
	String[] rocks = new String[]{ObjectName.COPPER_ROCKS, ObjectName.TIN_ROCKS, ObjectName.IRON_ROCKS};
	Tile tile = new Tile(3381, 3270, 0);
	boolean init=true;
	String[] trees = new String[]{ObjectName.TREE};
	String[] targets = new String[]{NpcName.DWARF, NpcName.GUARD, NpcName.IMP};
	String[] food = new String[]{"Cooked Meat"};
	int timer=0;
	Area fightingArea = new Area(new Tile(3007,3434,0), new Tile(3027,3458,0));
	public void poll() {	
		if(init)
		{
			init=false;
			//ChatEngine.GetInstance().SetContext(ctx).SetDebug(true).build().start();
			//StatisticsEngine.GetInstance().SetContext(ctx).build().run();
		}
		switch(getState())
		{
		case doThings:
			//QuestEngine.GetInstance().SetContext(ctx).SetObjectives(new TheBloodPact(ctx).getObjectives()).build().run();
			
			ctx.controller.stop();
			break;
		case buryBones:
			//light the logs
			LocalPlayer.Backpack.Use(ctx, ItemName.BONES, Constants.Interact.BURY);
			break;
		}
		
	}
	
	public State getState()
	{
		
		if(LocalPlayer.Backpack.Has(ctx, ItemName.BONES))
		{
			return State.buryBones;
		}
		else
		{
			return State.doThings;			
		}
	}
	public enum State
	{
		doThings,buryBones
	}
	
	public void messaged(MessageEvent msg) {
		Messages.AddPastReadMessages(msg.source(), msg.text());
	}
}
