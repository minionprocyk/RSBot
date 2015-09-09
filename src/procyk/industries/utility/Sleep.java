package procyk.industries.utility;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.constants.AnimationConstants;

public class Sleep {
	private static final int lowRandomWaitTime=500;
	private static final int highRandomWaitTime=750;
	
	public static void WaitRandomTime(int arg1, int arg2)
	{
		try {
			Thread.sleep(Random.nextInt(arg1, arg2));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void Wait(double time)
	{
		try {
			Thread.sleep((long) time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void WhileRunning(ClientContext ctx)
	{		
		//check if were actually moving. if were not then we have a problem
		Tile currentTile = ctx.players.local().tile();
		long now = System.currentTimeMillis();
		Sleep.Wait(1000);
		do
		{
			System.out.println("In Motion");

			Sleep.WaitRandomTime(lowRandomWaitTime,highRandomWaitTime);
			
			if(currentTile.x() == ctx.players.local().tile().x() &&
			   currentTile.y() == ctx.players.local().tile().y())
			{
				//player has not moved. we need to exit the loop
				System.out.println("Seems like we never started moving.");
				break;
			}
			if(java.lang.Math.abs(System.currentTimeMillis()-now) > 1000*60)
			{
				System.out.println("Timer exceeded 60 seconds, cancelling");
				break;
			}
		}while(ctx.players.local().inMotion());
	}
	public static void WhileNotIdle(ClientContext ctx)
	{
		long now = System.currentTimeMillis();
		Sleep.Wait(1000);
		do
		{
			System.out.println("Player not idle");
			Sleep.WaitRandomTime(lowRandomWaitTime, highRandomWaitTime);
			if(java.lang.Math.abs(System.currentTimeMillis()-now) > 1000*60)
			{
				System.out.println("Timer exceeded 60 seconds, cancelling");
				break;
			}
		}while(procyk.industries.localplayer.Animation.CheckPlayerIdle(ctx) == AnimationConstants.PLAYER_NOT_IDLE);
	}
	public static void WhilePlayer(ClientContext ctx, int animation)
	{
		long now = System.currentTimeMillis();
		do
		{
			Sleep.WaitRandomTime(lowRandomWaitTime, highRandomWaitTime);
			if(java.lang.Math.abs(System.currentTimeMillis()-now) > 1000*60)
			{
				System.out.println("Timer exceeded 60 seconds, cancelling");
				break;
			}
		}while(procyk.industries.localplayer.Animation.PlayerAnimation(ctx) == animation);
	}
}
