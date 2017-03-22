package misc;

import java.util.regex.Pattern;

public class Dummy {

	public static void main(String[] args) {
		String text = "az\rert\tyui\nop12";
		System.out.println(text);
		text = text.replaceAll("[\\n\\r]", "");
		System.out.println(text);
	}

}
