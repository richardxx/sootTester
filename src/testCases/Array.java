package testCases;

import java.util.Random;

public class Array {
	private Random ran = new Random(1);

	public static void main(String args[]) {
		new Array().onCreate();
	}

	public void onCreate() {
		method1();
	}

	public void method1() {
		String[][] arr = method2();

		/**
		 * PTA query with main->onCreate as context edge for local arr does not
		 * include allocnode returned from method2()
		 **/
		if (arr == null)
			arr = new String[2][2];

		method3(arr);
	}

	public String[][] method2() {
		if (ran.nextBoolean())
			return new String[2][2];

		return null;
	}

	public void method3(String[][] arr) {
		System.out.println(arr);
	}
}

