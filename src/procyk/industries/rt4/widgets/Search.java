package procyk.industries.rt4.widgets;

import java.util.Iterator;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Widget;

public class Search {
	
	public static Widget ForWidget(ClientContext ctx, String name)
	{
		Iterator<Widget> iWidget = ctx.widgets.select().iterator();
		while(iWidget.hasNext())
		{
			Widget nextWidget = iWidget.next();
			for(Component c: nextWidget.components())
			{
				if(c.componentCount() > 1)
				{
					for(Component c2: c.components())
					{
						if(c2.text().equals(name))return c2.widget();
						if(c2.tooltip().equals(name))return c2.widget();
					}
				}
				if(c.text().equals(name))return c.widget();
				if(c.tooltip().equals(name))return c.widget();
			}
		}
		throw new NullPointerException("No widget found based on criteria "+name);
	}
	public static Component ForComponent(ClientContext ctx, String name)
	{
		Iterator<Widget> iWidget = ctx.widgets.select().iterator();
		while(iWidget.hasNext())
		{
			Widget nextWidget = iWidget.next();
			for(Component c: nextWidget.components())
			{
				if(c.componentCount() > 1)
				{
					for(Component c2: c.components())
					{
						if(c2.text().equals(name))return c2;
						if(c2.tooltip().equals(name))return c2;
					}
				}
				if(c.text().equals(name))return c;
				if(c.tooltip().equals(name))return c;
			}
		}
		throw new NullPointerException("No component found based on criteria: "+name);
	}
}
