package coupon.sys.core.connectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import coupon.sys.core.exceptions.CouponSystemException;

public class ConnectionPool {

	// a collection of 10 open database connections
	private Set<Connection> connections = new HashSet<>();
	private Set<Connection> connectionsBackup = new HashSet<>(); //mby ill add this in the feature
	public static final int POOL_SIZE = 10;
	private String url = "jdbc:derby://localhost:1527/CouponSystemDB;create = true";

	private static ConnectionPool instance;
	
	/**
	 * 
	 * @return a singleton instance 
	 * @throws CouponSystemException
	 */
	public static ConnectionPool getInstance() throws CouponSystemException {
		if (instance==null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	private ConnectionPool() throws CouponSystemException {
		for (int i = 0; i < POOL_SIZE; i++) {
			try {
				Connection con = DriverManager.getConnection(url);
				connections.add(con);
				connectionsBackup.add(con);
			} catch (SQLException e) {
				throw new CouponSystemException("Connection pool initialization error", e);
			}
		}

	}
	/**
	 * 
	 * @return connection to the database
	 */
	public synchronized Connection getConnection() {
		while (connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Iterator<Connection> it = connections.iterator();
		Connection con = it.next();
		it.remove();
		return con;
	}
    /**
     * return connection we dont need
     * 
     */
	public synchronized void returnConnection(Connection con) {
		connections.add(con);
		notifyAll();
	}
	
	/**
	 * Closes all connections
	 * @throws CouponSystemException
	 */
	public synchronized void closeAllConnections() throws CouponSystemException {
	
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (Connection connection : connections) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponSystemException("Connection pool shutdown error", e);
			}
		}
	}
	
}
