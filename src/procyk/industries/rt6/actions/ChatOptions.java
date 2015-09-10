package procyk.industries.rt6.actions;

import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.rt6.utility.Sleep;
import procyk.industries.shared.constants.WidgetIdConstantsRT6;

public class ChatOptions {
	public static boolean SelectNextOption(ClientContext ctx, String option)
	{
		ChatOption co = null;
		while(!(co = ctx.chat.select().text(option).first().poll()).valid())
		{
			Widgets.Click(ctx, WidgetIdConstantsRT6.CHATWINDOW,WidgetIdConstantsRT6.CHATWINDOW_CONTINUE);
			Sleep.WaitRandomTime(300,750);
			co = ctx.chat.select().text(option).first().poll();
		}
		co.select(true);
		Sleep.WaitRandomTime(2000, 3000);
		return true;
	}

}
