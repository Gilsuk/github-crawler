package exec;

import controller.Controller;

public class Exec {

	public static void main(String[] args) throws Exception {
		
		String dbPath = "repos.db";
		String url = "https://github.com/search?utf8=%E2%9C%93&q=%EB%94%A5%EB%9F%AC%EB%8B%9D&ref=simplesearch";
		if (args.length == 2) {
			dbPath = args[0];
			url = args[1];
		}

		// Create scheme if not exist
		Controller controller = new Controller();
		controller.createDB(dbPath);

		// crawl(dbpath, url to parse, trys if fail to connect, interval)
		controller.crawl(dbPath, url, 10, 6000);
	}
}
