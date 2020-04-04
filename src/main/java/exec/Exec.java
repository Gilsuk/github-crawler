package exec;

import controller.Controller;

public class Exec {

	public static void main(String[] args) throws Exception {
		
//		String path = "E:/Downloads/crawl_02.db";
		String path = args[0];

		Controller controller = new Controller();
		controller.createDB(path);
		
		controller.crawl(
			path,
			"https://github.com/search?utf8=%E2%9C%93&q=%EB%94%A5%EB%9F%AC%EB%8B%9D&ref=simplesearch",
			10,
			6000);
	}
}
