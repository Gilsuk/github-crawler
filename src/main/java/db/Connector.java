package db;

import java.sql.Connection;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.JournalMode;
import org.sqlite.SQLiteConfig.SynchronousMode;

public class Connector {
	
	public Connection connect(String dbPath) {
		StringBuilder path = new StringBuilder("jdbc:sqlite:").append(dbPath);
		try {
			return getConfig().createConnection(path.toString());
		} catch (SQLException e) { e.printStackTrace(); }
		
		return null;
	}

	private SQLiteConfig getConfig() {
		SQLiteConfig config = new SQLiteConfig();
		config.setBusyTimeout(5000);
		config.setJournalMode(JournalMode.WAL);
		config.setSynchronous(SynchronousMode.FULL);
		config.enforceForeignKeys(true);
		return config;
	}
}
