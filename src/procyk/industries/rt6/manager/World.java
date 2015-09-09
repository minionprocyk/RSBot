package procyk.industries.rt6.manager;

public class World {
	private int worldNumber=0;
	private int worldPlayers=0;
	private String location="";
	private String type="";
	private long timeLoggedIn;
	public World(int number, int numPlayers)
	{
		this.worldNumber=number;
		this.worldPlayers=numPlayers;
	}
	public int getWorldNumber() {
		return worldNumber;
	}
	public int getWorldPlayers() {
		return worldPlayers;
	}
	public void setWorldPlayers(int worldPlayers) {
		this.worldPlayers = worldPlayers;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getTimeLoggedIn() {
		return timeLoggedIn;
	}
	public void setTimeLoggedIn() {
		this.timeLoggedIn = System.currentTimeMillis();
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof World)
		{
			return this.worldNumber == ((World)o).getWorldNumber();
		}
		return false;
	}
	

}
