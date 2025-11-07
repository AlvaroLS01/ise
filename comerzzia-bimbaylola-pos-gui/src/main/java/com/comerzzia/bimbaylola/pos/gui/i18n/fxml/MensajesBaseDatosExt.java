package com.comerzzia.bimbaylola.pos.gui.i18n.fxml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.BooleanTypeHandler;

import com.comerzzia.core.util.config.DynamicMappersSqlSessionFactory;
import com.comerzzia.core.util.mybatis.interceptors.BlankStringToNullInterceptor;
import com.comerzzia.core.util.mybatis.typehandlers.BooleanStringTypeHandler;
import com.comerzzia.pos.persistence.core.menu.MenuBean;
import com.comerzzia.pos.persistence.core.menu.MenuExample;
import com.comerzzia.pos.persistence.core.menu.MenuMapper;
import com.comerzzia.pos.persistence.core.variables.VariableBean;
import com.comerzzia.pos.persistence.core.variables.VariableExample;
import com.comerzzia.pos.persistence.core.variables.VariableMapper;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoExample;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoMapper;

public class MensajesBaseDatosExt {
	
	private static Map<String, String> mapGettextConfig = new HashMap<String, String>();
	
	public static Map<String, String> obtenerCadenas() throws MensajesExceptionExt {
		SqlSession sqlSession = createSqlSessionFactory().openSession();

		MenuExample exampleMenu = new MenuExample();
		MenuMapper menuMapper = sqlSession.getMapper(MenuMapper.class);
		exampleMenu.or().andAplicacionEqualTo("JPOS").andActivoEqualTo(true);
		exampleMenu.setOrderByClause("ORDEN ASC");
		List<MenuBean> menusBean = menuMapper.selectByExample(exampleMenu);

		for (MenuBean menu : menusBean) {
			String opcion = menu.getOpcion();
			StringTokenizer token = new StringTokenizer(opcion, "\\");
			String key = token.nextToken();

			if (!mapGettextConfig.containsKey(key)) {
				mapGettextConfig.put(key, key);
			}

			if (!mapGettextConfig.containsKey(menu.getAccion().getTitulo())) {
				mapGettextConfig.put(menu.getAccion().getTitulo(), menu.getAccion().getTitulo());
			}
		}

		/* Medios de pago */
		MedioPagoMapper medioPagoMapper = sqlSession.getMapper(MedioPagoMapper.class);
		MedioPagoExample example = new MedioPagoExample();
		List<MedioPagoBean> listaMediosPago = medioPagoMapper.selectByExample(example);

		for (MedioPagoBean medioPago : listaMediosPago) {
			String descMedioPago = medioPago.getDesMedioPago();
			// String key = medioPago.getDesMedioPago();

			if (!mapGettextConfig.containsKey(descMedioPago)) {
				mapGettextConfig.put(descMedioPago, descMedioPago);
			}
		}

		return mapGettextConfig;
	}
	
	public static DynamicMappersSqlSessionFactory createSqlSessionFactory() {
		PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
		Properties prop = new Properties();
		prop.put("driver", "net.sourceforge.jtds.jdbc.Driver");
		prop.put("url", "jdbc:jtds:sqlserver://vm-dbserver.tier1.es:1433/czz_bimbaylola");
		prop.put("username", "czz_bimbaylola");
		prop.put("password", "fuego");
		prop.put("poolMaximumActiveConnections", "2");
		dataSourceFactory.setProperties(prop);
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, dataSourceFactory.getDataSource());
		Configuration configuration = new Configuration(environment);
		//Configuramos databaseId para diferenciar statements en los Mappers. Valores posibles: "Oracle", "MySQL", "Microsoft SQL Server" 
        //y otros valores de las implementaciones de java.util.DatabaseMetaData.getDatabaseProductName()
//        configuration.setDatabaseId(new VendorDatabaseIdProvider().getDatabaseId(dataSourceFactory.getDataSource()));
        configuration.setDatabaseId(new VendorDatabaseIdProvider().getDatabaseId(dataSourceFactory.getDataSource()));
        if(!configuration.getDatabaseId().contains("Oracle")) {
        	configuration.addInterceptor(new BlankStringToNullInterceptor());
        }
		configuration.getTypeHandlerRegistry().register(BooleanTypeHandler.class);
		configuration.getTypeHandlerRegistry().register(BooleanStringTypeHandler.class);
		DynamicMappersSqlSessionFactory dynamicMappersSqlSessionFactory = new DynamicMappersSqlSessionFactory(new SqlSessionFactoryBuilder().build(configuration));

		return dynamicMappersSqlSessionFactory;
	}
}

