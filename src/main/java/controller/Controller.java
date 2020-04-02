package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import crawling.Repo;
import crawling.RepoCrawler;
import db.Connector;
import db.StatementFactory;

public class Controller {
	
	public void createDB(String dbPath) {
		
		Connector connector = new Connector();
		Connection connection = connector.connect(dbPath);
		
		try(StatementFactory factory = new StatementFactory(connection)) {
			PreparedStatement createRepoStmt = factory.getStatement("createTableRepo");
			PreparedStatement createLangStmt = factory.getStatement("createTableLang");
			PreparedStatement createTagStmt = factory.getStatement("createTableTag");
			
			createRepoStmt.execute();
			createLangStmt.execute();
			createTagStmt.execute();
		} catch (SQLException e) { e.printStackTrace();
		} catch (Exception e) { e.printStackTrace(); }
		
		try {
			connection.close();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	public void crawl(String dbPath, String url, int attempt, int interval) {
		Connector connector = new Connector();
		Connection connection = connector.connect(dbPath);
		StatementFactory factory = new StatementFactory(connection);

		RepoCrawler crawler = new RepoCrawler(url, attempt);
		while(crawler.hasNext()) {
			store(factory, crawler.next());
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}

		try { factory.close();
		} catch (Exception e) { e.printStackTrace(); }
		try { connection.close();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	private void store(StatementFactory factory, List<Repo> repos) {
		for (int i = 0; i < repos.size(); i++) {
			Repo repo = repos.get(i);
			try {
				insert(factory, repo);
			} catch (SQLException e) { continue; }
		}
	}

	private void insert(StatementFactory factory, Repo repo) throws SQLException {
		insertRepo(factory, repo);
		getId(factory, repo);
		insertLangs(factory, repo);
		insertTags(factory, repo);
	}

	private void insertTags(StatementFactory factory, Repo repo) throws SQLException {
		List<String> tags = repo.getTags();
		int id = repo.getId();
		PreparedStatement statement = factory.getStatement("insertTag");

		for (String tag : tags) {
			statement.setInt(1, id);
			statement.setString(2, tag);
			statement.executeUpdate();
		}
	}

	private void insertLangs(StatementFactory factory, Repo repo) throws SQLException {
		List<String> langs = repo.getLangs();
		int id = repo.getId();
		PreparedStatement statement = factory.getStatement("insertLang");

		for (String lang : langs) {
			statement.setInt(1, id);
			statement.setString(2, lang);
			statement.executeUpdate();
		}
	}

	private void getId(StatementFactory factory, Repo repo) throws SQLException {
		PreparedStatement statement = factory.getStatement("selectRepoByTitle");
		statement.setString(1, repo.getTitle());
		ResultSet resultSet = statement.executeQuery();
		
		resultSet.next();
		int id = resultSet.getInt(1);
		
		repo.setId(id);
	}

	private void insertRepo(StatementFactory factory, Repo repo) throws SQLException {
		PreparedStatement statement = factory.getStatement("insertRepo");
		statement.setString(1, repo.getTitle());
		statement.setString(2, repo.getDescription());
		statement.setInt(3, repo.getStar());
		statement.executeUpdate();
	}
}
