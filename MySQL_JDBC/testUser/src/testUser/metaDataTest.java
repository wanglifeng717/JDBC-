package testUser;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;
/*Ԫ���ݵ���Ҫ�����ڣ������Ի�ȡ���еĸ������е����ƣ��еı����������ǳ���������ȥ��ֵ�����ԡ�*/
public class metaDataTest {

	/**
	 * DatabaseMetaData���������ݿ��Ԫ���ݶ���
	 * ������connection�õ�
	 * �˽⼴��
	 */
	@Test
	public void testDatabaseMetaData() {
		Connection connection=null;
		DatabaseMetaData data =null;
		ResultSet resultSet=null;
		try {
			connection=jdbcTools.getConnection();
			data = connection.getMetaData();
			//���Եõ����ݿ�汾
			int version = data.getDatabaseMajorVersion();
			System.out.println(version);
			//���ݿ���û���,����ò���
			String user = data.getUserName();
			System.out.println(user);
			
			//�õ�����Щ���ݿ�
			resultSet = data.getCatalogs();
			while(resultSet.next())
			{
				System.out.println(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jdbcTools.release(null, connection);
		}
	}
	/**
	 * ResultSetMetaData:�����������Ԫ����
	 * ���Եõ�������еĻ�����Ϣ�������������Щ�У��������еı�����
	 */
	@Test 
	public void testResultSetMetaData(){
		Connection connection =null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			connection = jdbcTools.getConnection();
			String sql="select id,name,email,birth from customers";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			//1.�õ�resultSetMeteData ����
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			//2.�õ��еĸ���
			int columncount = resultSetMetaData.getColumnCount();
			System.out.println(columncount);
			//3.�õ�����
			for(int i=0;i<columncount;i++)
			{
				String columnName = resultSetMetaData.getColumnName(i+1); 
				//4.�õ��еı���
				String columnLabel = resultSetMetaData.getColumnLabel(i+1);
				System.out.println(columnName+":"+columnLabel);
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			jdbcTools.release(preparedStatement, connection);
		}
	}
	

}
