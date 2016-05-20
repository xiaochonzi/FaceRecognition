package com.face.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.face.model.Admin;

/*
 * 管理员dao类
 */
public class AdminDAO {
	private Connection conn;

	public AdminDAO() {
		openConnection();
	}

	public Admin searchAdmin(String name, String password) {
		PreparedStatement ps = null;
		String sql = "select * from t_admin where name=? and password=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Admin admin = new Admin();
				admin.setName(rs.getString("name"));
				admin.setPassword("password");
				admin.setIsSuper(rs.getString("is_super"));
				return admin;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

	public void openConnection() {
		if (conn == null) {
			DBInfo dbInfo = DBInfo.getInstance();
			conn = dbInfo.getConnection();
		}
	}

}
