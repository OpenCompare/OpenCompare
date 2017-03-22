package data_off;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OFFCSVProductFactory {
	public static class A {
		public int[] b = new int[]{8};
	}
	public static void main(String[] args) throws IOException {
		ObjectMapper om = new ObjectMapper();
		String res = om.writeValueAsString(Arrays.asList("Bidule", "Coin'\"Coin", new A()));
		System.out.println(res);
		A youpi = om.readValue("{\"c\": [3, 4, 6]}", A.class);
		System.out.println(youpi.b[1]);
	}
	
	
	
	
}
