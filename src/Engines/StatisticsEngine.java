package Engines;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.ClientContext;

public class StatisticsEngine extends Thread implements Runnable, PaintListener{
	private static StatisticsEngine se;
	private static ClientContext ctx;
	long start;
	int[] skillStartExperiences = new int[26];
	int[] skillStartLevels = new int[26];
	int[] skillExperiencePerHour = new int[26];
	int startCombatLevel;
	int skill=0;
	boolean runOnce=true;
	private StatisticsEngine(){}
	public void run()
	{
		Utility.Sleep.Wait(5000);
		if(runOnce)init();
		while(!ctx.controller.isStopping())
		{
			Utility.Sleep.Wait(1000);
			UpdateSkills();
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
		start = System.currentTimeMillis();
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
	
	public void repaint(Graphics graphics) {
		//paint on the client to show exp
		Graphics2D g = (Graphics2D) graphics;
		
		Rectangle rect = ctx.client().getCanvas().getBounds();
		int rectX = (int) rect.getWidth()-200;
		int rectY = (int) rect.getHeight()-150;
		g.setColor(Color.white);
		
		g.fillRect(rectX, rectY, 250, 100);
		
		int textModHeight = 15;
		int depth=1;
		g.setColor(Color.black);
		g.setFont(new Font("Arial", 1, 18));
		g.drawString("Train Skills", rectX+10, rectY+textModHeight*depth++);
		depth++;
		g.setFont(new Font("Arial", 1, 14));
		g.drawString("Runtime = "+getRuntimeString((int)Math.abs(System.currentTimeMillis()-start)), rectX+2, rectY+textModHeight*depth++);
		g.drawString("Levels Gained = "+getLevelsGained(skill), rectX+2, rectY+textModHeight*depth++);
		g.drawString("EXP Gained = "+getXPGained(skill)+"xp", rectX+2, rectY+textModHeight*depth++);
		g.drawString("EXP/HR = "+skillExperiencePerHour[skill]+"xp/hr", rectX+2, rectY+textModHeight*depth++);
	}
	private String getRuntimeString(int ms)
	{
		final int sec = (ms / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	private void UpdateSkills()
	{
		//calculate exp per hour based on the time difference from
		//now - start
		long now = System.currentTimeMillis();
		int xpGained=0;
		int time = (int) (now-start);
		int max=0;
		int skillIndex=0;
		for(int i=0; i < skillExperiencePerHour.length;i++)
		{
			xpGained =(ctx.skills.experience(i) - skillStartExperiences[i]);
			skillExperiencePerHour[i] = xpGained/(time*1000*60*60);
			
			if(skillExperiencePerHour[i] > max)
			{
				skillIndex=i;
				max = skillExperiencePerHour[i];
			}
				
		}
		skill = skillIndex;
	}
	private int getXPGained(int stat)
	{
		return ctx.skills.experience(stat) - skillStartExperiences[stat];
	}
	private int getLevelsGained(int stat)
	{
		return ctx.skills.level(stat) - skillStartLevels[stat];
	}
	
}
