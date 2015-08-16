package Pathing;

import org.powerbot.script.Area;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Traverse {
	public static void TraversePath(ClientContext ctx,Tile... path)
	{
		for(Tile t: path)
		{
			while(ctx.players.local().tile().distanceTo(t) > 6)
			{
				//shake the camera randomly 5% of the time while moving
				if(Random.nextInt(0, 100)>94)Camera.Focus.OnRandomObject(ctx);
				
				ctx.movement.step(t);
				Utility.Sleep.WhileRunning(ctx);
			}
		}
	}
	public static void TraversePath(ClientContext ctx, Area area, Tile destination)
	{
		//take an area and create a path to a tile
	}
	public static void TraversePathInReverse(ClientContext ctx, Tile... path)
	{
		//load all of the paths and loop through their index in reverse
		for(Tile t: ctx.movement.newTilePath(path).reverse().array())
		{
			TraversePath(ctx,t);
		}
	}
	public static void TraverseRandomPath(ClientContext ctx, Tile... path)
	{
		//get number of tiles/paths in the path
		int numTiles = path.length;
		
		//if we have no paths then wtf
		if(numTiles==0 || path==null)return;
		
		//get a random path
		int selectedTile = Random.nextInt(0, numTiles-1);
		
		//traverse the path
		TraversePath(ctx,path[selectedTile]);
	}
}
