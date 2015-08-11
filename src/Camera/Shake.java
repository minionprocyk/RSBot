package Camera;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

public class Shake {
	//camera pitch at 0 = looking at floor
	public static void ShakeIt(ClientContext ctx)
	{
		for(int i=0;i<3;i++)
		{
			SetRandomCameraAngle(ctx);
		}
	}
	public static void Random(ClientContext ctx)
	{
		//shake the camera as if we're just having fun looking around
		int selectedMode = Random.nextInt(0, Mode.values().length-1);
		
		switch(Mode.values()[selectedMode])
		{
		case left:
			ctx.camera.pitch(Random.nextInt(0, 20));
			ctx.camera.pitch(Random.nextInt(20, 80));
			SetRandomCameraAngle(ctx);
			break;
		case right:
			ctx.camera.pitch(Random.nextInt(0, 20));
			ctx.camera.pitch(Random.nextInt(20, 80));
			SetRandomCameraDirection(ctx);
			break;
		case shake:
			ShakeIt(ctx);
			break;
		}
		
	}
	private static void SetRandomCameraAngle(ClientContext ctx)
	{
		ctx.camera.angle(Random.nextInt(0, 360));
	}
	private static void SetRandomCameraDirection(ClientContext ctx)
	{
		switch(Random.nextInt(0, 4))
		{
		case 0:
			ctx.camera.angle('n');
			break;
		case 1:
			ctx.camera.angle('s');
			break;
		case 2:
			ctx.camera.angle('w');
			break;
		case 3:
			ctx.camera.angle('e');
			break;
		}
	}
	
	private enum Mode
	{
		left, right, shake
	}
}
