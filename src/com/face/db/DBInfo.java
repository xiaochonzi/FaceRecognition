package com.face.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBInfo {

	private static DBInfo dbInfo;
	private String driver;
	private String url;
	private String user;
	private String password;

	private DBInfo() {
		config();
	}

	public static DBInfo getInstance() {
		if (dbInfo == null) {
			dbInfo = new DBInfo();
		}
		return dbInfo;
	}

	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取配置信息
	 */
	private void config() {
		Properties pro = new Properties();
		String path = DBInfo.class.getResource("db.properties").getPath();
		try {
			pro.load(new FileInputStream(new File(path)));
			driver = pro.getProperty("driver");
			url = pro.getProperty("url");
			user = pro.getProperty("user");
			password = pro.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
