package Player;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

public class Animation {
	public static int CheckPlayerAnimation(ClientContext ctx)
	{
		//take one second-ish to figure out what animation is going on
		
		//poll for the animation every tenth of a second. 
		//whatever animation has the greatest value is what were doing
		int animationValue=0;
		int[] animationCount = new int[2];

		for(int i=0; i<Random.nextInt(10, 30);i++)
		{
			animationValue = ctx.players.local().animation();
			if(animationValue == Constants.Animation.PLAYER_IDLE)
			{
				animationCount[0]++; //count up idle animations
			}
			else
			{
				animationCount[1]++; // count up non idle animation
			}
			Utility.Sleep.Wait(100);
		}
		return animationCount[0] > animationCount[1] ? Constants.Animation.PLAYER_IDLE : Constants.Animation.PLAYER_NOT_IDLE;
		
	}
}
