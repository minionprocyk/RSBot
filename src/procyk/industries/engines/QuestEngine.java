package procyk.industries.engines;

import org.powerbot.script.rt6.ClientContext;

import procyk.industries.quests.Objective;

public class QuestEngine implements Runnable{
	private static QuestEngine qe;
	private static ClientContext ctx;
	private Objective[] objectives;
	private QuestEngine(){}
	public void run()
	{
		for(Objective o: objectives)
		{
			if(o.ready())
			{
				System.out.println("Ready to perform the following objective:");
				System.out.println(o.objectiveName);
				o.perform();
				
			}
		}
	}
	public static QuestEngine GetInstance()
	{
		if(qe==null)
		{
			qe = new QuestEngine();
		}
		return qe;
	}
	public QuestEngine SetContext(ClientContext context)
	{
		if(ctx==null)
		{
			ctx=context;
		}
		return this;
	}
	public QuestEngine SetObjectives(Objective... objectives)
	{
		if(this.objectives==null)
		{
			this.objectives=objectives;
		}
		return this;
	}
	public QuestEngine build()
	{
		return this;
	}
	
}
