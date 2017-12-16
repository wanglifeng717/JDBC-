package testUser;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

public class TestPrimarykey {

	/*ȡ�����ݿ��Զ����ɵ�������ֻ������MYSQL����������������orcal�ǲ��е�*/
	@Test
	public void testGetKeyValue() {
		Connection connection = null;
		PreparedStatement preparedStatement=null;
		try {
			connection = jdbcTools.getConnection();
			String sql="insert into customers(name,email) values(?,?)";
			//ͨ�����صķ�������ȡ�Զ����ɵ�ID
			preparedStatement=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, "abcde");
			preparedStatement.setString(2, "123@qq.com");
			preparedStatement.executeUpdate();
			//��ȡ������������������resultset���󣬽�����о�һ�У������Ǹ�����ֵ
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if(resultSet.next()){
				System.out.println(resultSet.getObject(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jdbcTools.release(preparedStatement, connection);
		}
		 
	}

}
