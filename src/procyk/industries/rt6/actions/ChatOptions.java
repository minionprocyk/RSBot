package procyk.industries.rt6.actions;

import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.rt6.utility.Sleep;
import procyk.industries.shared.constants.WidgetIdConstants;

public class ChatOptions {
	public static boolean SelectNextOption(ClientContext ctx, String option)
	{
		ChatOption co = null;
		while(!(co = ctx.chat.select().text(option).first().poll()).valid())
		{
			Widgets.Click(ctx, WidgetIdConstants.CHATWINDOW,WidgetIdConstants.CHATWINDOW_CONTINUE);
			Sleep.WaitRandomTime(300,750);
			co = ctx.chat.select().text(option).first().poll();
		}
		co.select(true);
		Sleep.WaitRandomTime(2000, 3000);
		return true;
	}

}
