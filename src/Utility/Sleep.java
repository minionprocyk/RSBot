package Utility;

import org.powerbot.script.Random;

public class Sleep {
	public static void WaitRandomTime(int arg1, int arg2)
	{
		try {
			Thread.sleep(Random.nextInt(arg1, arg2));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
