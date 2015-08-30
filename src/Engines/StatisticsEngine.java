package Engines;

import org.powerbot.script.rt6.ClientContext;

public class StatisticsEngine extends Thread implements Runnable{
	private static StatisticsEngine se;
	private static ClientContext ctx;
	int[] skillStartExperiences = new int[26];
	int[] skillStartLevels = new int[26];
	int startCombatLevel;
	boolean runOnce=true;
	private StatisticsEngine(){}
	public void run()
	{
		Utility.Sleep.Wait(5000);
		if(runOnce)init();
		while(!ctx.controller.isStopping())
		{
			
		}
	}
	private void init()
	{
		//load skill information
		for(int i =0;i<ctx.skills.levels().length;i++)
		{
			skillStartExperiences[i]=ctx.skills.experience(i);
			skillStartLevels[i]=ctx.skills.level(i);
		}
		startCombatLevel=ctx.players.local().combatLevel();
	}
	private void UpdateSkills()
	{
		
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
