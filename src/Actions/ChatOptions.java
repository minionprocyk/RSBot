package Actions;

import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;

import Constants.WidgetId;

public class ChatOptions {
	public static boolean SelectNextOption(ClientContext ctx, String option)
	{
		ChatOption co = null;
		while(!(co = ctx.chat.select().text(option).first().poll()).valid())
		{
			Actions.Widgets.Click(ctx, WidgetId.CHATWINDOW,WidgetId.CHATWINDOW_CONTINUE);
			Utility.Sleep.WaitRandomTime(300,750);
			co = ctx.chat.select().text(option).first().poll();
		}
		co.select(true);
		Utility.Sleep.WaitRandomTime(2000, 3000);
		return true;
	}

}
