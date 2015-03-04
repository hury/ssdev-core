package ctd.test.unit;

import org.springframework.util.AntPathMatcher;

public class AntPathMatcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AntPathMatcher ant = new AntPathMatcher();
		ant.setPathSeparator(".");
		
		System.out.println(ant.match("192.168.*.*", "192.168.2.23"));

	}

}
