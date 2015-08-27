package Utility;

import java.io.File;
import java.io.IOException;

import org.powerbot.script.rt6.GeItem;

public class Print {
	public static void PrintItemIds(int startingId)
	{
		final int max = startingId+1000;		
		
		while(startingId < max)
		{
			final int searchId = startingId;

			new Thread(new Runnable() {
	
				public void run() {
					GeItem geItem = GeItem.profile(searchId);
					System.out.println("public static final int "+geItem.name().toUpperCase().replace(" ", "_")+" = "+geItem.id());
					
				}
			}).start();
			startingId++;
			Utility.Sleep.Wait(2500);
		}	
	}
	public static void test()
	{
		String filePath = new File("res/edit.txt").getAbsolutePath();
		System.out.println(filePath);
	}
}

