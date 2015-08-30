package Actions;

import org.powerbot.script.rt6.ClientContext;

public class Widgets {
	public static boolean Click(ClientContext ctx, int...ids)
	{
		switch(ids.length)
		{
		case 2:
			if(ctx.widgets.select().id(ids[0]).poll().component(ids[1]).valid())
			{
				ctx.widgets.select().id(ids[0]).poll().component(ids[1]).click();
				Utility.Sleep.WhileNotIdle(ctx);
				return true;
			}
			break;
		case 3:
			if(ctx.widgets.select().id(ids[0]).poll().component(ids[1]).component(ids[2]).valid())
			{
				ctx.widgets.select().id(ids[0]).poll().component(ids[1]).component(ids[2]).click();
				Utility.Sleep.WhileNotIdle(ctx);
				return true;
			}
			break;
		default:
			System.out.println("No functionality for "+ids.length+" number of arguments");
			break;
		}
		System.out.println("Could not find widget with the following ids: ");
		for(int id: ids)
		{
			System.out.println(id);
		}
		return false;
		
	}

}
