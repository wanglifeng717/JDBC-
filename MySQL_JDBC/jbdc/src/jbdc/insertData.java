package jbdc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class insertData {
  /*1.��ȡ���ݿ�����
	2.׼�������SQL���	
	3.ִ�в���
		1)��ȡ�����ӣѣ����ģ���������������
			����connection��createStatement����������ȡ
		�������ã���������������ģ����������գ�����壨���죩ִ�����
			executeUpdate����ִ��insert,update,delete ���ǲ�����select;
		�����رգ���������������
			����������Դ����Ҫ�رա����쳣Ҳ��Ҫ�رգ��쳣���Բ������������ӱ���Ҫ�رա� 
	4.�ر�����:
			�ر���Դ��ʱ�������������ȹ������ڹ����棬�и���εĹ�ϵ��
*/
	public static void main(String[] args)
	{
		Connection conn = null;
		Statement statement=null;
		try{
			conn= jdbcTools.getConnection();
			
			String sql =null; 
//			�����ӣ�ɾ�����޸Ĺ���
			sql="insert into dept (deptno,dname,loc) VALUES(3,'cc','df')";
			//sql = "delete from dept where deptno= 1";
			//sql="update dept set dname='zhongguo' where deptno=2";
			
			statement = conn.createStatement();
			
			statement.executeUpdate(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			jdbcTools.release(statement,conn);
		}
	
	}
	
}
