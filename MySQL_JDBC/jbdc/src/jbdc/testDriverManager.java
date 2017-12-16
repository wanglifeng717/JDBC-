package jbdc;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Test;

public class testDriverManager {

//	DriverManager�������Ĺ�����
//	1.����ͨ�����ص�getconnection������ȡ���ݿ����ӣ���Ϊ����
//	2.����ͬʱ��������������
//	
	public static void main(String[] args) {
		

	}
	@Test
//	���ݿ����ӵ����հ汾
	public void testDriverManager1() throws Exception{
//		1.׼���������ݿ��4���ַ���
		String driverClass =null;
		String jdbcUrl=null;
		String user=null;
		String password=null;
		//��ȡ��·���µ�jdbc.propertise�ļ�
		InputStream in =
				getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);
		driverClass= properties.getProperty("driver");
		jdbcUrl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
//		�������ݿ���������ע������,��Ӧ��Driverʵ��������ע�������ľ�̬�����,���Բ�ͬд�ˣ�
//		����ע��ķ�ʽ�ŵ��ǿ���������ע����������
		Class.forName(driverClass);
//		DriverManager.registerDriver((Driver) Class.forName(driverClass).newInstance());
//		ͨ��DriverManagerde��getConnection()������ȡ���ݿ����� 
		Connection connection =
				DriverManager.getConnection(jdbcUrl,user,password);
		
		System.out.println(connection);
		
	}
	

}
