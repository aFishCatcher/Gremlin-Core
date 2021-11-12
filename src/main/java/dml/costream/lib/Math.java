package dml.costream.lib;

public final class Math {

	public static double abs(double x) {
		return java.lang.Math.abs(x);
	}
	
	public static double acos(double x) {
		return java.lang.Math.acos(x);
	}
	
	public static double acosh(double x) {
		return java.lang.Math.log(x+Math.sqrt(x*x-1));
	}
	
	public static double asin(double x) {
		return java.lang.Math.asin(x);
	}
	
	public static double asinh(double x) {
		return java.lang.Math.log(x+Math.sqrt(x*x+1));
	}
	
	public static double atan(double x) {
		return java.lang.Math.atan(x);
	}
	
	public static double atan2(double y, double x) {
		return java.lang.Math.atan2(y, x);
	}
	
	public static double atanh(double x) {
		return 0.5*java.lang.Math.log((1+x)/(1-x));
	}
	
	public static double ceil(double x) {
		return java.lang.Math.ceil(x);
	}
	
	public static double cos(double x) {
		return java.lang.Math.cos(x);
	}
	
	public static double cosh(double x) {
		return java.lang.Math.cosh(x);
	}
	
	public static double exp(double x) {
		return java.lang.Math.exp(x);
	}
	
	public static double expm1(double x) {
		return java.lang.Math.expm1(x);
	}
	
	public static double floor(double x) {
		return java.lang.Math.floor(x);
	}
	
	public static int fmod(int x, int y) {
		return java.lang.Math.floorMod(x, y);
	}
	
	public static long fmod(long x, long y) {
		return java.lang.Math.floorMod(x, y);
	}
	
	public static float fmod(float x, float y) {
		return x-y*(float)java.lang.Math.floor(x/y);
	}
	
	public static double fmod(double x, double y) {
		return x-y*java.lang.Math.floor(x/y);
	}
	
	// mark
	public static double frexp(double x, NumPtr eptr) {
		int e=0;
		double flag=1;
		if(x<0) {
			flag=-1;
			x=-x;
		}
		while(x<0.5||x>=1) {
			if(x<0.5) {
				x*=2;
				e--;
			}
			else {
				x/=2;
				e++;
			}
		}
		eptr.setValue(e);
		return x*flag;
	}

	public static double log(double x) {
		return java.lang.Math.log(x);
	}
	
	public static double log10(double x) {
		return java.lang.Math.log10(x);
	}
	
	public static double log1p(double x) {
		return java.lang.Math.log1p(x);
	}
	
	public static int max(int x, int y) {
		return java.lang.Math.max(x, y);
	}
	
	public static long max(long x, long y) {
		return java.lang.Math.max(x, y);
	}
	
	public static float max(float x, float y) {
		return java.lang.Math.max(x, y);
	}
	
	public static double max(double x, double y) {
		return java.lang.Math.max(x, y);
	}
	
	// mark �ݶ�
	public static double modf(double x, NumPtr integer) {
		integer.setValue((int)x);
		return x-integer.getInt();
	}
	
	public static int min(int x, int y) {
		return java.lang.Math.min(x, y);
	}
	
	public static long min(long x, long y) {
		return java.lang.Math.min(x, y);
	}
	
	public static float min(float x, float y) {
		return java.lang.Math.min(x, y);
	}
	
	public static double min(double x, double y) {
		return java.lang.Math.min(x, y);
	}
	
	public static double pow(double x, double y) {
		return java.lang.Math.pow(x, y);
	}
	
	public static double sin(double x) {
		return java.lang.Math.sin(x);
	}
	
	public static double sinh(double x) {
		return java.lang.Math.sinh(x);
	}
	
	public static double sqrt(double x) {
		return java.lang.Math.sqrt(x);
	}
	
	public static double tan(double x) {
		return java.lang.Math.tan(x);
	}
	
	public static double tanh(double x) {
		return java.lang.Math.tanh(x);
	}
	
	public static double round(double x) {
		return java.lang.Math.round(x);
	}
	
	public static void print(Object obj) {
		System.out.print(obj);
	}
	
	public static void printf(Object obj) {
		// Mark
	}
	
	public static void println() {
		System.out.println();
	}
	public static void println(Object obj) {
		System.out.println(obj);
	}
}
