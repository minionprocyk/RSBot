package procyk.industries.manager;

import org.powerbot.script.rt6.ClientContext;

public class MouseManager {
	private static MouseManager mm;
	private static ClientContext ctx;
	private MouseManager(){}
	public static MouseManager GetInstance()
	{
		if(mm==null)mm = new MouseManager();
		return mm;
	}
	public MouseManager SetContext(ClientContext ctx)
	{
		if(MouseManager.ctx==null)MouseManager.ctx = ctx;
		return this;
	}
	public void run()
	{
		
	}
	public MouseManager build()
	{
		return this;
	}

}
