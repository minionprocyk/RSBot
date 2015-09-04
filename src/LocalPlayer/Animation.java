package LocalPlayer;

import org.powerbot.script.rt6.ClientContext;
public class Animation {
	public static int PREVIOUS_ANIMATION=-1;
	public static int CheckPlayerIdle(ClientContext ctx)
	{
		return PlayerAnimation(ctx) != Constants.Animation.PLAYER_IDLE ? Constants.Animation.PLAYER_NOT_IDLE : Constants.Animation.PLAYER_IDLE;
	}
	public static int PlayerAnimation(ClientContext ctx)
	{
		int currentAnimation=0, numTests=10;
		int[] animationValues = new int[numTests];
		for(int i=0;i<numTests;i++)
		{
			currentAnimation = ctx.players.local().animation();
			animationValues[i]=currentAnimation;
			Utility.Sleep.Wait(100);
		}
		
		//return a non idle animation
		for(int i : animationValues)
		{
			if(i != Constants.Animation.PLAYER_IDLE)return PREVIOUS_ANIMATION=i;
		}
		
		return PREVIOUS_ANIMATION=Constants.Animation.PLAYER_IDLE;
	}
}
