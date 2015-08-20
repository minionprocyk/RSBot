package Engines;

import org.powerbot.script.rt6.ClientContext;

public class StatisticsEngine extends Thread implements Runnable{
	private static StatisticsEngine se;
	private static ClientContext ctx;
	private StatisticsEngine()
	{
	}
	public void run()
	{
		for(int i=0;i<ctx.skills.levels().length;i++)
		{
			System.out.println("Skill ["+i+"]:");
			System.out.println("   Level = "+ctx.skills.level(i));
			System.out.println("   Experience = "+ctx.skills.experience(i));
		}
	}
	public static StatisticsEngine GetInstance()
	{
		if(se==null)
		{
			se = new StatisticsEngine();
		}
		return se;
	}
	public StatisticsEngine SetContext(ClientContext context)
	{
		if(ctx==null)
		{
			ctx=context;
		}
		return this;
	}
	public StatisticsEngine build()
	{
		return this;
	}
	
}
