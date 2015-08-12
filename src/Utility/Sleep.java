package Utility;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import Constants.Animation;

public class Sleep {
	private static final int lowRandomWaitTime=1000;
	private static final int highRandomWaitTime=3000;
	
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
		do
		{
			
			Utility.Sleep.WaitRandomTime(lowRandomWaitTime,highRandomWaitTime);
			
			if(currentTile.x() == ctx.players.local().tile().x() &&
			   currentTile.y() == ctx.players.local().tile().y())
			{
				//player has not moved. we need to exit the loop
				System.out.println("Seems like we never started moving. Weird.");
				break;
			}
			System.out.println("In Motion");
		}while(ctx.players.local().inMotion());
	}
	public static void WhileNotIdle(ClientContext ctx)
	{
		do
		{
			Utility.Sleep.WaitRandomTime(lowRandomWaitTime, highRandomWaitTime);
			System.out.println("Player not idle");
		}while(LocalPlayer.Animation.CheckPlayerIdle(ctx) == Animation.PLAYER_NOT_IDLE);
	}
	public static void WhilePlayer(ClientContext ctx, int animation)
	{
		do
		{
			Utility.Sleep.WaitRandomTime(lowRandomWaitTime, highRandomWaitTime);
			System.out.println("Player is doing that animation");
		}while(LocalPlayer.Animation.GetPlayerAnimation(ctx) == animation);
	}
}
