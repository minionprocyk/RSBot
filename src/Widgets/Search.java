package Widgets;

import java.util.Iterator;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Widget;

public class Search {
	
	public static Widget ForWidget(ClientContext ctx, String name)
	{
		Iterator<Widget> iWidget = ctx.widgets.select().iterator();
		while(iWidget.hasNext())
		{
			Widget nextWidget = iWidget.next();
			for(Component c: nextWidget.components())
			{
				if(c.childrenCount() > 1)
				{
					for(Component c2: c.components())
					{
						if(c2.text().toLowerCase().equals(name.toLowerCase()))
						{
							return c2.widget();
						}
					}
				}
				if(c.text().toLowerCase().equals(name.toLowerCase()))
				{
					return c.widget();
				}
			}
		}
		System.out.println("No widget found based on criteria: "+name);
		return null;
	}
	public static Component ForComponent(ClientContext ctx, String name)
	{
		Iterator<Widget> iWidget = ctx.widgets.select().iterator();
		while(iWidget.hasNext())
		{
			Widget nextWidget = iWidget.next();
			for(Component c: nextWidget.components())
			{
				if(c.childrenCount() > 1)
				{
					for(Component c2: c.components())
					{
						if(c2.text().toLowerCase().equals(name.toLowerCase()))
						{
							return c2;
						}
					}
				}
				if(c.text().toLowerCase().equals(name.toLowerCase()))
				{
					return c;
				}
			}
		}
		System.out.println("No component found based on criteria: "+name);
		return null;
	}
}
