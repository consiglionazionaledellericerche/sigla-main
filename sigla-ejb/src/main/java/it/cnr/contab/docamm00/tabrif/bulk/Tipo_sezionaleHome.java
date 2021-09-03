/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_sezionaleHome extends BulkHome {
public Tipo_sezionaleHome(java.sql.Connection conn) {
	super(Tipo_sezionaleBulk.class,conn);
}
public Tipo_sezionaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_sezionaleBulk.class,conn,persistentCache);
}
public java.util.Collection findTipiSezionali(
	Integer esercizio,
	String unita_organizzativa,
	String ti_istituz_commerc,
	String ti_acquisti_vendite,
	String ti_fattura)
	throws PersistencyException, IntrospectionException{
	
	return findTipiSezionali(esercizio,
							unita_organizzativa,
							ti_istituz_commerc,
							ti_acquisti_vendite,
							ti_fattura,
							null);
}
public java.util.Collection findTipiSezionali(
	Integer esercizio,
	String unita_organizzativa,
	String ti_istituz_commerc,
	String ti_acquisti_vendite,
	String ti_fattura,
	java.util.Vector options)
	throws PersistencyException, IntrospectionException{
	
	return fetchAll(selectTipiSezionali(
									esercizio, 
									unita_organizzativa, 
									ti_istituz_commerc, 
									ti_acquisti_vendite,
									ti_fattura,
									options));
}
public java.util.Collection findTipiSezionaliPerRistampa(
	Integer esercizio,
	String unita_organizzativa,
	String ti_istituz_commerc,
	String ti_acquisti_vendite,
	String ti_fattura,
	java.util.Vector options)
	throws PersistencyException, IntrospectionException{
	
	SQLBuilder sql = selectTipiSezionali(
									esercizio, 
									unita_organizzativa, 
									ti_istituz_commerc, 
									ti_acquisti_vendite,
									ti_fattura,
									options);
	sql.setDistinctClause(true);
	return fetchAll(sql);
}
private SQLBuilder selectTipiSezionali(
	Integer esercizio,
	String unita_organizzativa,
	String ti_istituz_commerc,
	String ti_acquisti_vendite,
	String ti_fattura,
	java.util.Vector options)
	throws PersistencyException, IntrospectionException{
	
	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("SEZIONALE");
	sql.addSQLJoin("SEZIONALE.CD_TIPO_SEZIONALE","TIPO_SEZIONALE.CD_TIPO_SEZIONALE");
	sql.addSQLClause("AND","SEZIONALE.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,unita_organizzativa);
	sql.addSQLClause("AND","TIPO_SEZIONALE.TI_ISTITUZ_COMMERC",sql.EQUALS,(ti_istituz_commerc == null || it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.PROMISCUA.equalsIgnoreCase(ti_istituz_commerc)?
			TipoIVA.COMMERCIALE.value() : ti_istituz_commerc));
	sql.addSQLClause("AND","TIPO_SEZIONALE.TI_ACQUISTI_VENDITE",sql.EQUALS,ti_acquisti_vendite);
	sql.addSQLClause("AND","SEZIONALE.TI_FATTURA",sql.EQUALS,ti_fattura);
	sql.addSQLClause("AND","SEZIONALE.ESERCIZIO",sql.EQUALS,esercizio);
	
	if (options != null) {
		for (java.util.Iterator i = options.iterator(); i.hasNext();) {
			String[][] option = (String[][])i.next();
			if (option.length != 0) {
				if (option.length != 1)
					sql.openParenthesis(option[0][2]);
					
				for (int count = 0; count < option.length; count++)
					sql.addSQLClause(option[count][2], option[count][0], sql.EQUALS, option[count][1]);
					
				if (option.length != 1)
					sql.closeParenthesis();
			}
		}
	}
sql.addOrderBy("ORDINA");
	return sql;
}
public SQLBuilder selectTipo_sezionale_venditaByClause(Tipo_sezionaleBulk tipo_sezionale,Tipo_sezionaleHome tipo_sezionaleHome,Tipo_sezionaleBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException,PersistencyException,IntrospectionException {
	
	SQLBuilder sql = tipo_sezionaleHome.selectByClause(clause);
	sql.addClause("AND","ti_acquisti_vendite",sql.EQUALS,"V");
	return sql;		
}
}
