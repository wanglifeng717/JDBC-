package insertDigdata;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.junit.Test;

//import com.mysql.jdbc.PreparedStatement;

public class JDBCInsertDigData {

	/**
	 * �����ݿ�customer���ݱ����10������¼
	 * ������β��룬��ʱ���
	 * 1.ʹ��Statement
	 */
	@Test
	public void testBatchWithStatement() {
		Connection connection=null;
		Statement statement=null;
		try {
			connection = jdbcTools.getConnection();
			//System.out.println(connection);
			jdbcTools.beginTx(connection);
			long begin = System.currentTimeMillis();
			statement = connection.createStatement();
			for(int i=0;i<50000;i++)
			{
				String sql="insert into customers (name,birth) values("
						+"'name_"+i+"','2009-1-1')";
				statement.executeUpdate(sql);
			}
			long end = System.currentTimeMillis();
			System.out.println("time:"+(end-begin));
			jdbcTools.commit(connection);
			//���ݿ���鿴��¼�������� select count(id)from customer;
			//time:2238
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTools.rollback(connection);
		}
		finally {
			
			jdbcTools.release(statement, connection);
		}
	}
	/*
	 * ʹ��preparedstatement����Ч���Ƿ������Щ*/
	@Test
	public void testBatchWithPreparedStatement() {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		try {
			connection = jdbcTools.getConnection();
			String sql="insert into customers (name,birth) values(?,?)";
			preparedStatement = connection.prepareStatement(sql);
			
			jdbcTools.beginTx(connection);
			long begin = System.currentTimeMillis();
			for(int i=0;i<50000;i++)
			{  
				preparedStatement.setString(1, "name_"+i);
				preparedStatement.setString(2, "2009-1-1");
				preparedStatement.executeUpdate();
			}
			long end = System.currentTimeMillis();
			System.out.println("time:"+(end-begin));
			jdbcTools.commit(connection);
			//���ݿ���鿴��¼�������� select count(id)from customers;
			//������ݱ� truncate table customers;
			//time:2236
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTools.rollback(connection);
		}
		finally {
			
			jdbcTools.release(preparedStatement, connection);
		}	
	}
	
	/*
	 * ������һ��һ����ִ�У��������ڵȵ�����һ����SQL֮��һ��ִ��
	 * addBatch(String)�����Ҫ���������SQL
	 * executeBatch()ִ�������������*/
	@Test
	public void testBatchWithPreparedStatement2() {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		try {
			connection = jdbcTools.getConnection();
			String sql="insert into customers (name,birth) values(?,?)";
			preparedStatement = connection.prepareStatement(sql);
			
			jdbcTools.beginTx(connection);
			long begin = System.currentTimeMillis();
			for(int i=0;i<50000;i++)
			{  
				preparedStatement.setString(1, "name_"+i);
				preparedStatement.setString(2, "2009-1-1");
				//����SQL
				preparedStatement.addBatch();
				//����һ������ͳһִ�У������֮ǰ�Ļ���
				if((i+1)%300==0)
				{
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
				}	
			}
			//�����������ֵ��������������Ҫ�ڶ����ִ��һ��
			if(50000%300!=0){
				preparedStatement.executeBatch();
				preparedStatement.clearBatch();
			}
			
			long end = System.currentTimeMillis();
			System.out.println("time:"+(end-begin));
			jdbcTools.commit(connection);
			//���ݿ���鿴��¼�������� select count(id)from customers;
			//������ݱ� truncate table customers;
			//time:2214
		} catch (Exception e) {
			e.printStackTrace();
			jdbcTools.rollback(connection);
		}
		finally {
			
			jdbcTools.release(preparedStatement, connection);
		}	
	}
	

}
