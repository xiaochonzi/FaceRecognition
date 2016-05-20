package com.face.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.face.model.Person;

/**
 * 危险人群dao类
 * @author stone
 *
 */
public class PersonDAO {

	private Connection conn;
	public PersonDAO(){
		transaction();
	}
	/**
	 * 增加危险人群
	 * @param p
	 * @return
	 */
	public boolean insert(Person p) {
		PreparedStatement prestmt = null;
		StringBuilder sql = new StringBuilder().append("insert into t_person ")
				.append("(personNo,personName,personImage,personCardId,personAddr,personLevel,date) values (")
				.append("?,?,?,?,?,?,?").append(")");
		try {
			prestmt = conn.prepareStatement(sql.toString());
			prestmt.setString(1, p.getPersonNo());
			prestmt.setString(2, p.getPersonName());
			prestmt.setString(3, p.getPersonImage());
			prestmt.setString(4, p.getPersonCardId());
			prestmt.setString(5, p.getPersonAddr());
			prestmt.setString(6, p.getPersonLevel());
			prestmt.setString(7, p.getDate());
			int flag = prestmt.executeUpdate();
			if (flag > 0) {
				commit();
				return true;
			} else {
				rollback();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (prestmt != null) {
				try {
					prestmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		}
		return false;
	}
	/**
	 * 通过编号查询
	 * @param no
	 * @return
	 */
	public Person findByNo(String no){
		PreparedStatement ps = null;
		Person person = null;
		String sql = "select * from t_person where personNo=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, no);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				person = new Person();
				person.setId(rs.getInt(1));
				person.setPersonNo(rs.getString(2));
				person.setPersonName(rs.getString(3));
				person.setPersonImage(rs.getString(4));
				person.setPersonCardId(rs.getString(5));
				person.setPersonAddr(rs.getString(6));
				person.setPersonLevel(rs.getString(7));
				person.setDate(rs.getString(8));
				return person;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(ps!=null){
				try{
					ps.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return person;
	}
	
	/**
	 * 查询所有人员
	 * @return
	 */
	public List<Person> findAll(){
		List<Person> persons = new ArrayList<Person>();
		PreparedStatement ps = null;
		String sql = "select * from t_person";
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setId(rs.getInt(1));
				person.setPersonNo(rs.getString(2));
				person.setPersonName(rs.getString(3));
				person.setPersonImage(rs.getString(4));
				person.setPersonCardId(rs.getString(5));
				person.setPersonAddr(rs.getString(6));
				person.setPersonLevel(rs.getString(7));
				person.setDate(rs.getString(8));
				persons.add(person);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(ps != null){
				try{
					ps.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return persons;
	}
	
	/**
	 * 修改人员信息
	 * @param p
	 * @return
	 */
	public boolean modify(Person p){ 
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder()
				.append("update t_person ")
				.append("set personNo=?,")
				.append("personName=?,")
				.append("personImage=?,")
				.append("personCardId=?,")
				.append("personAddr=?,")
				.append("personLevel=?,")
				.append("Date=? ")
				.append("where personNo=?");
		try {
			ps = conn.prepareStatement(sql.toString());
			ps.setString(1, p.getPersonNo());
			ps.setString(2, p.getPersonName());
			ps.setString(3, p.getPersonImage());
			ps.setString(4, p.getPersonCardId());
			ps.setString(5, p.getPersonAddr());
			ps.setString(6, p.getPersonLevel());
			ps.setString(7, p.getDate());
			ps.setString(8, p.getPersonNo());
			int flag = ps.executeUpdate();
			if(flag>0){
				commit();
				return true;
			}else{
				rollback();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (ps!=null) {
				try {
					ps.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
		}
		return false;	
	}
	/**
	 * 通过编号删除
	 * @param no
	 * @return
	 */
	public boolean deleteByNo(String no){
		PreparedStatement ps = null;
		String sql = "delete from t_person where personNo=?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, no);
			int flag = ps.executeUpdate();
			if(flag > 0){
				commit();
				return true;
			}else{
				rollback();
				return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			if(ps != null){
				try {
					ps.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 开启事务
	 */
	public void transaction() {
		try {
			openConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void openConnection() {
		if (conn == null) {
			DBInfo dbInfo = DBInfo.getInstance();
			conn = dbInfo.getConnection();
		}
	}
	
	/**
	 * 提交
	 */
	public void commit(){
		try{
			conn.commit();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	/**
	 * 回滚
	 */
	public void rollback(){
		try{
			conn.rollback();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
