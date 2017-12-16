package insertDigdata;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;
//���ݿ����ӳغʹ���������
public class JDBCDBCP {

	/**
	 * ʹ��DBCP���ݿ����ӳ�
	 * 1.����jar�� 2����common pool
	 * 2.�������ݿ����ӳ�
	 * 3.Ϊ����Դʵ��ָ�����������
	 * 4.������Դ�л�ȡ���ݿ�����
	 * @throws SQLException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testDBCP() throws SQLException, InterruptedException {
		BasicDataSource dataSource=null;
		//����DBCP����Դʵ��
		 dataSource = new BasicDataSource();
		 final BasicDataSource dataSource2=dataSource;
		//Ϊ����Դʵ��ָ�����������
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setUrl("jdbc:mysql:///mydata");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		//����ԴһЩ��ѡ������
		//ָ����ʼ�����ӳ��г�ʼ����������
		dataSource.setInitialSize(5);
		//ָ��������������ͬһʱ�̿���ͬʱ�����ݿ������������
		dataSource.setMaxTotal(5);
		//ָ��С�������������ݿ����ӳ��б������С�Ŀ������ӵ�����
		dataSource.setMinIdle(2);
		//�ȴ����ݿ����ӳط������ӵ��ʱ�䣬��λ���룬�������׳��쳣
		dataSource.setMaxWaitMillis(5*1000);		
		//������Դ�л�ȡ���ݿ�����
		Connection connection = dataSource.getConnection();
		System.out.println(connection.getClass());
		
		 connection = dataSource.getConnection();
		System.out.println(connection.getClass());
		
		 connection = dataSource.getConnection();
		System.out.println(connection.getClass());
		
		connection = dataSource.getConnection();
		System.out.println(connection.getClass());
		
		Connection connection2 = dataSource.getConnection();
		System.out.println("��������ӿ���"+connection2.getClass());
		/*����֮ǰ�����Ӷ�û�أ����ǵ���������ʱ�����3�뻹�����Ӳ��Ͼͻ��׳��쳣
		 * ������3���ڰ��Ǹ������ͷŵ��Ϳ��ԣ����Ե�ʱ��������һ���̻߳�ȡ���ӣ���һ���߳��ͷ�����*/
		
		new Thread(){
			public void run() {
				Connection conn;
				
				try {
					//�����°汾�����ԣ�������
					conn = dataSource2.getConnection();
					System.out.println("�������������"+conn.getClass());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			};
		}.start();
		
		Thread.sleep(4000);
		System.out.println("��������ӹر�");
		connection2.close();
		
	}
	//�������ļ��ķ�ʽȥ�������ݿ����ӣ����ַ�ʽ���ӵ�ͨ��
	@Test
	public void testDBCPWithDataSourceFactory() throws Exception{
		Properties properties = new Properties();
		InputStream inputStream= JDBCDBCP.class.getClassLoader().getResourceAsStream("dbcp.properties");
		properties.load(inputStream);
		DataSource dataSource = 
				BasicDataSourceFactory.createDataSource(properties);
		
		System.out.println(dataSource.getConnection());
		BasicDataSource basicDataSource =(BasicDataSource)dataSource;
		System.out.println(basicDataSource.getInitialSize());
		System.out.println(basicDataSource.getMaxTotal());
		System.out.println(basicDataSource.getMinIdle());
		System.out.println(basicDataSource.getMaxWaitMillis());
		
		//basicDataSource.close();
	}

}
