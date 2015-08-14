package Engines;

import org.powerbot.script.rt6.ClientContext;

public class BankingEngine {
	private static ClientContext ctx;
	private static BankingEngine be;
	
	private BankingEngine()
	{
	}
	public static BankingEngine GetInstance()
	{
		if(be == null)
		{
			be = new BankingEngine();
		}
		return be;
	}
	public BankingEngine SetContext(ClientContext context)
	{
		if(ctx == null)
		{
			ctx = context;
		}
		return this;
	}
	public BankingEngine build()
	{
		return this;
	}
	public void run()
	{
		
	}

}
