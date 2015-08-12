package LocalPlayer;

import java.util.HashMap;
import java.util.Iterator;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

public class Animation {
	public static int CheckPlayerIdle(ClientContext ctx)
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
	public static int GetPlayerAnimation(ClientContext ctx)
	{
		//get the 3 most used animation values
		Integer animationValue = new Integer(0);
		//int[] animationCount = new int[3];
		HashMap<Integer,Integer> animationCount = new HashMap<Integer,Integer>();
		
		for(int i=0;i<Random.nextInt(10, 30);i++)
		{
			animationValue = ctx.players.local().animation();
			if(!animationCount.containsKey(animationValue))
			{
				//we don't have this value yet. lets add it
				animationCount.put(animationValue, 1);
			}
			else
			{
				//we have this key. increment it 
				animationCount.put(animationValue,animationCount.get(animationValue)+1);
			}
		}
		//return the animation value that was incremented most
		Iterator<Integer> iCount = animationCount.keySet().iterator();
		Integer animationValueWithHighestCount = new Integer(0);
		Integer highestCount = new Integer(0);
		while(iCount.hasNext())
		{
			Integer nextValue = iCount.next();
			if(animationCount.get(nextValue) > highestCount)
			{
				animationValueWithHighestCount = nextValue;
				highestCount = animationCount.get(nextValue);
			}
		}
		return animationValueWithHighestCount;
	}
}
