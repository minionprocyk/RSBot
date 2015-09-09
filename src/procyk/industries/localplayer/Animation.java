package procyk.industries.localplayer;

import org.powerbot.script.rt6.ClientContext;

import procyk.industries.constants.AnimationConstants;
import procyk.industries.utility.Sleep;
public class Animation {
	public static int PREVIOUS_ANIMATION=-1;
	public static int CheckPlayerIdle(ClientContext ctx)
	{
		return PlayerAnimation(ctx) != AnimationConstants.PLAYER_IDLE ? AnimationConstants.PLAYER_NOT_IDLE : AnimationConstants.PLAYER_IDLE;
	}
	public static int PlayerAnimation(ClientContext ctx)
	{
		int currentAnimation=0, numTests=10;
		int[] animationValues = new int[numTests];
		for(int i=0;i<numTests;i++)
		{
			currentAnimation = ctx.players.local().animation();
			animationValues[i]=currentAnimation;
			Sleep.Wait(100);
		}
		
		//return a non idle animation
		for(int i : animationValues)
		{
			if(i != AnimationConstants.PLAYER_IDLE)return PREVIOUS_ANIMATION=i;
		}
		
		return PREVIOUS_ANIMATION=AnimationConstants.PLAYER_IDLE;
	}
}
