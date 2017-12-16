package testUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class DAO {

	// insert��update��delete���������԰�������
	public void  update(String sql,Object...args){
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		try{
			connection = jdbcTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1,args[i] );
			}
			preparedStatement.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(preparedStatement, connection);
			
		}
	}
	//��ѯһ����¼�����ض�Ӧ�Ķ���
	public <T> T get(Class<T> clazz,String sql,Object ...  args){
		T entity =null;
		Connection connection=null;
		PreparedStatement preparedstatement=null;
		ResultSet resultset =null;
		try{
			//1.��ȡconnection����
			connection = jdbcTools.getConnection();
			//2.��ȡprepare��statement����
			preparedstatement=connection.prepareStatement(sql);
			//3.���ռλ��
			for(int i=0;i<args.length;i++)
			{
				preparedstatement.setObject(i+1, args[i]);
			}
			//4.���в�ѯ���õ�resultSet
			resultset=preparedstatement.executeQuery();
			//5.��resultset���м�¼��
			if(resultset.next()){
				//׼��һ��map<String,Object>:��:����еı�����ֵ������е�ֵ
				Map<String,Object>values=new HashMap<String,Object>();
				//6.�õ�resultSetMetaData����
				ResultSetMetaData rsmd = resultset.getMetaData();
				//7.����resultset��ָ�������ƶ�һ����λ
				//8.��resultsetmetadata����õ�������ж�����
				int columnCount = rsmd.getColumnCount();
				//9.��resultsetmetadata�õ������;����е�ֵ
				for(int i=0;i<columnCount;i++){
					String columnLabel =rsmd.getColumnLabel(i+1);
					Object columnValue=resultset.getObject(i+1);
					//10.���map����
					values.put(columnLabel,columnValue);
				}
				//11.�÷��䴴��class��Ӧ�Ķ���
				entity = clazz.newInstance();
				//12.����map�����÷��������������ֵ��������Ϊmap�е�key��
				for(Map.Entry<String,Object> entry:values.entrySet())
				{
					String propertyName = entry.getKey();
					Object value = entry.getValue();
					//ReflectionUtils.setFieldValue(entity, propertyName, value);
					//�����ַ��䲻������ͳ�ķ�ʽ����������ǣ���Ϊ������Ҫ��set�������õġ�
					BeanUtils.setProperty(entity, propertyName, value);
				}
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			
		}
		finally{
			jdbcTools.release(preparedstatement, connection,resultset);
		}

		return entity;
	}
	//��ѯ������¼�����ض�Ӧ�Ķ���ļ���
	public <T> List<T> getForList(Class<T>clazz,String sql,Object...args){
		
		List<T> list = new ArrayList<T>();
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			connection=jdbcTools.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet=preparedStatement.executeQuery();
			//��ȡһ����󣬲�ͬ�����
			
			//5.׼��һ��List<Map<String,Object>>����ű�����ֵ������е�ֵ������map�����Ӧ��һ����¼
			List<Map<String, Object>> values = new ArrayList<Map<String,Object>>();
			
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			Map<String,Object> map = null;
			//7.����resultsetʹ��while
			while (resultSet.next())
			{
				map =new HashMap<String,Object>();
				for(int i=0;i<resultSetMetaData.getColumnCount();i++)
				{
					String columnLabel = resultSetMetaData.getColumnLabel(i+1);
					Object value = resultSet.getObject(i+1);
					map.put(columnLabel, value);
				}
				//11.�����õ�Map�������5׼����List��
				values.add(map);
			}
			//12.�ж�List�Ƿ�Ϊ�ռ��ϣ�����Ϊ�գ������List �õ�һ����Map�����ڰ�һ��Map����תΪһ��class
			T bean = null;
			if(values.size()>0)
			{
				for(Map<String,Object> m:values)//��������
				{
					bean = clazz.newInstance();
					for(Map.Entry<String, Object> entry:m.entrySet())
					{
						String propertyName = entry.getKey();
						Object value = entry.getValue();
						//������Ӧ��Object����
						BeanUtils.setProperty(bean, propertyName, value);
						
					}
					//13.��Object������뵽List�С�
					list.add(bean);
				}
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(preparedStatement, connection,resultSet);
		}
		
		return list;
	}
	//����ĳ����¼��ĳһ���ֶε�ֵ��һ��ͳһ��ֵ��һ����������¼�ȣ�
	public <E> E getForValue(String sql,Object...argss){
		return null;
	}
}
