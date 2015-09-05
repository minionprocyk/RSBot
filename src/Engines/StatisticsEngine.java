package Engines;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.reflect.Field;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.ClientContext;

import Constants.Skills;

public class StatisticsEngine extends Thread implements Runnable, PaintListener{
	private static StatisticsEngine se;
	private static ClientContext ctx;
	long start;
	static int[] skillStartExperiences = new int[26];
	static int[] skillStartLevels = new int[26];
	static int[] skillExperiencePerHour = new int[26];
	int startCombatLevel;
	public static int skill=0;
	boolean runOnce=true;
	private String paintTitle="name";
	private String[] stringsToDraw;
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
		runOnce=false;
		for(int i =0;i<ctx.skills.levels().length;i++)
		{
			skillStartExperiences[i]=ctx.skills.experience(i);
			skillStartLevels[i]=ctx.skills.level(i);
			System.out.println("Skill "+i+" : "+skillStartLevels[i]+": "+skillStartExperiences[i]);
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
	public StatisticsEngine SetName(String name)
	{
		this.paintTitle = name;
		return this;
	}
	public StatisticsEngine SetStringsToDraw(String... strings)
	{
		this.stringsToDraw = strings;
		return this;
	}
	public StatisticsEngine build()
	{
		return this;
	}
	
	public void repaint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		
		Rectangle rect = ctx.client().getCanvas().getBounds();
		int rectX = (int) rect.getWidth()-210;
		int rectY = (int) rect.getHeight()-150;
		g.setColor(Color.white);
		
		g.fillRect(rectX, rectY, 200, 100);
		
		int textModHeight = 15;
		int depth=1;
		
		g.setColor(Color.black);
		g.setFont(new Font("Arial", 1, 18));
		g.drawString(paintTitle, rectX+100-(5*paintTitle.length()), rectY+textModHeight*depth++);
		g.setFont(new Font("Arial", 1, 14));
		g.drawString("Runtime = "+getRuntimeString((int)Math.abs(System.currentTimeMillis()-start)), rectX+2, rectY+textModHeight*depth++);
		
		for(String s: stringsToDraw)
		{
			g.drawString(s, rectX+2, rectY+textModHeight*depth++);
		}
		
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
		double time = Math.abs(now-start);
		int max=0;
		int skillIndex=0;
		for(int i=0; i < skillExperiencePerHour.length;i++)
		{
			xpGained =(ctx.skills.experience(i) - skillStartExperiences[i]);
			skillExperiencePerHour[i] = (int) ((xpGained*1.0/(time))*1000*60*60);
			if(skillExperiencePerHour[i] > max)
			{
				skillIndex=i;
				max = skillExperiencePerHour[i];
			}
				
		}
		skill = skillIndex;
	}
	public int getXPGained(int stat)
	{
		return ctx.skills.experience(stat) - skillStartExperiences[stat];
	}
	public int getLevelsGained(int stat)
	{
		return ctx.skills.level(stat) - skillStartLevels[stat];
	}
	public int getExperiencePerHour(int stat)
	{
		return skillExperiencePerHour[stat];
	}
	public String getSkill(int skill)
	{
		for(Field f: Skills.class.getFields())
		{
			try {
				if((int)f.get(null) == skill)
				{
					return f.getName();
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return "UNKNOWN";
	}
	public int getCombatLevelsGained()
	{
		return ctx.players.local().combatLevel()-this.startCombatLevel;
	}
	
}
