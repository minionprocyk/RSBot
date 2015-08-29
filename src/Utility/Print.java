package Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

import Scripts.TestScript;

public class Print {
	public static void PrintItemIds(int startingId, int max)
	{
				
		try {
			File editFile = new File(TestScript.storageDirectory,"edit.txt");
			
			PrintWriter pw = new PrintWriter(editFile);
			while(startingId < max)
			{
				final int searchId = startingId;

				new Thread(new Runnable() {
		
					public void run() {
						GeItem geItem = GeItem.profile(searchId);
						pw.write("public static final int "+geItem.name().toUpperCase().replace(" ", "_")+" = "+geItem.id()+System.lineSeparator());
						System.out.println("public static final int "+geItem.name().toUpperCase().replace(" ", "_")+" = "+geItem.id());
						
					}
				}).start();
				startingId++;
				Utility.Sleep.Wait(5000);
			}	
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static void PrintSkillIds(ClientContext ctx)
	{
		for(int i =0;i<ctx.skills.levels().length;i++)
		{
			System.out.println("Skill ["+i+"]:");
			System.out.println("   Level = "+ctx.skills.level(i));
			System.out.println("   Experience = "+ctx.skills.experience(i));
		}
	}
	public static void test()
	{
		String filePath = new File("res/edit.txt").getAbsolutePath();
		System.out.println(filePath);
	}
}

