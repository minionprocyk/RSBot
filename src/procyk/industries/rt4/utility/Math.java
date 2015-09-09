package procyk.industries.rt4.utility;

public class Math {

	public static boolean IsBetween(int x, int a, int b)
	{
		if(x>=a && x<=b)return true;
		return false;
	}

	public static boolean IsBetween(double distanceTo, int a, int b) {
		if(distanceTo>=a && distanceTo<=b)return true;
		return false;
	}
	
}
