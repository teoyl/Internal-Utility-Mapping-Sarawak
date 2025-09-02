package com;

import java.sql.Types;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.type.StandardBasicTypes;


/**
 *
 * @author thensw
 */
public class OwnSQLServerDialect extends SQLServer2008Dialect {

	public OwnSQLServerDialect() {
		      registerColumnType( Types.NVARCHAR, "nvarchar(MAX)" );
		      registerColumnType( Types.NVARCHAR, 8000, "nvarchar($l)" );
		      registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
        registerHibernateType(Types.LONGVARCHAR, StandardBasicTypes.STRING.getName());
    }
}
