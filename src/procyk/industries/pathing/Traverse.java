package procyk.industries.pathing;

import java.util.ArrayList;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import procyk.industries.camera.Focus;
import procyk.industries.utility.Math;
import procyk.industries.utility.Sleep;
public class Traverse {
	private static int distanceThreshold=4;
	public static void TraversePath(ClientContext ctx,Tile... path)
	{
		for(Tile t: path)
		{
			while(ctx.players.local().tile().distanceTo(t) > distanceThreshold)
			{
				//focus on a random object 5% of the time
				if(Random.nextInt(0, 100)>94)Focus.OnRandomObject(ctx);
				
				ctx.movement.step(t);
				while(ctx.players.local().tile().distanceTo(t) > distanceThreshold && ctx.players.local().inMotion())
				{
					Sleep.Wait(100);
				}
			}
		}
	}
	public static void TraversePath(ClientContext ctx, Area area, Tile destination)
	{
		//take an area and create a path to a tile
		
		//get a random tile, check if it brings you closer to the destination.
		Area currentArea=null;
		Tile startingLocation = ctx.players.local().tile();
		Tile lastTile = startingLocation;
		Tile randomTile = null;
		boolean gotNextTile=false;
		List<Tile> path = new ArrayList<Tile>();
		double distance=100d;
		while(lastTile.distanceTo(destination) > distanceThreshold)
		{
			//define an area to choose a tile from
			currentArea = new Area(new Tile(lastTile.x()-30,lastTile.y()-30), new Tile(lastTile.x()+30,lastTile.y()+30));
			distance = lastTile.distanceTo(destination);
			while(!gotNextTile)
			{
				randomTile = currentArea.getRandomTile();
				if(Math.IsBetween(randomTile.distanceTo(lastTile), 10, 20) && randomTile.distanceTo(destination) < distance)
				{
					lastTile = randomTile;
					path.add(lastTile);
					gotNextTile=true;
				}
			}
			gotNextTile=false;
		}
		//now we should have our path. traverse it.
		for(Tile t: path)
		{
			TraversePath(ctx,t);
		}
		
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
