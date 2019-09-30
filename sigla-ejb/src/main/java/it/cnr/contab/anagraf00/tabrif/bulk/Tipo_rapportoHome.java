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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_rapportoHome extends BulkHome {
public Tipo_rapportoHome(java.sql.Connection conn) {
	super(Tipo_rapportoBulk.class,conn);
}
public Tipo_rapportoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_rapportoBulk.class,conn,persistentCache);
}
/**
 *	Ritorna tutti i TIPI RAPPORTO associati
 *  al terzo <vTerzo> e validi in data inizio <dataIni> e data fine <dataFin>
 *  
 *  Parametri:
 *	 - vTerzo
 *	 - data inizio validità rapporto
 *	 - data fine validità rapporto
 *
**/
public java.util.List findTipiRapporto(it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk vTerzo, java.sql.Timestamp dataIni, java.sql.Timestamp dataFin) throws PersistencyException{

	// Ritorna tutti i tipi rapporto 
	
	SQLBuilder sql = createSQLBuilder();

	sql.addTableToHeader("V_TERZO_PER_COMPENSO");
	sql.addSQLJoin("TIPO_RAPPORTO.CD_TIPO_RAPPORTO","V_TERZO_PER_COMPENSO.CD_TIPO_RAPPORTO");
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.CD_TERZO",SQLBuilder.EQUALS,vTerzo.getCd_terzo());
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.TI_DIPENDENTE_ALTRO",SQLBuilder.EQUALS,vTerzo.getTi_dipendente_altro());
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.DT_INI_VALIDITA",SQLBuilder.LESS_EQUALS,dataIni);
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.DT_FIN_VALIDITA",SQLBuilder.GREATER_EQUALS,dataFin);

	return fetchAll(sql);
}
/**
 *	Ritorna tutti i TIPI RAPPORTO associati
 *  al terzo <vTerzo> e validi in data <data>
 *  
 *  Parametri:
 *	 - vTerzo
 *	 - data
 *
**/
public java.util.List findTipiRapporto(it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk vTerzo, java.sql.Timestamp data) throws PersistencyException{
    return findTipiRapporto(vTerzo, data, data);
}
/**
 *	Ritorna il TIPO RAPPORTO di codice <cdTipoRapporto> associato 
 *  al terzo <vTerzo> e valido in data <data>
 *  
 *  Parametri:
 *	 - vTerzo
 *   - cdTipoRapporto
 *	 - data inizio validità rapporto
 *	 - data fine validità rapporto
**/
public Tipo_rapportoBulk findTipoRapporto(it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk vTerzo, String cdTipoRapporto, java.sql.Timestamp dataIni, java.sql.Timestamp dataFin) throws PersistencyException{

	// Ritorna tutti i tipi rapporto 
	
	SQLBuilder sql = createSQLBuilder();

	sql.addTableToHeader("V_TERZO_PER_COMPENSO");
	sql.addSQLJoin("TIPO_RAPPORTO.CD_TIPO_RAPPORTO","V_TERZO_PER_COMPENSO.CD_TIPO_RAPPORTO");
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.CD_TERZO",SQLBuilder.EQUALS,vTerzo.getCd_terzo());
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.TI_DIPENDENTE_ALTRO",SQLBuilder.EQUALS,vTerzo.getTi_dipendente_altro());
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.CD_TIPO_RAPPORTO",SQLBuilder.EQUALS,cdTipoRapporto);
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.DT_INI_VALIDITA",SQLBuilder.LESS_EQUALS,dataIni);
	sql.addSQLClause(FindClause.AND,"V_TERZO_PER_COMPENSO.DT_FIN_VALIDITA",SQLBuilder.GREATER_EQUALS,dataFin);

	Tipo_rapportoBulk tipoRapp = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		tipoRapp = (Tipo_rapportoBulk)fetch(broker);
	broker.close();

	return tipoRapp;
}
/**
 *	Ritorna il TIPO RAPPORTO di codice <cdTipoRapporto> associato 
 *  al terzo <vTerzo> e valido in data <data>
 *  
 *  Parametri:
 *	 - vTerzo
 *   - cdTipoRapporto
 *	 - data
 *
**/
public Tipo_rapportoBulk findTipoRapporto(it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk vTerzo, String cdTipoRapporto, java.sql.Timestamp data) throws PersistencyException{
	return findTipoRapporto(vTerzo, cdTipoRapporto, data, data);
}
/**
 *	Ritorna il TIPO RAPPORTO con codice cdTipoRapporto
 *  
 *  Parametri:
 *	 - cdTipoRapporto
 *
**/
public Tipo_rapportoBulk findTipoRapporto(String cdTipoRapporto) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_TIPO_RAPPORTO",sql.EQUALS, cdTipoRapporto);
	
	Tipo_rapportoBulk rapp = null;
	SQLBroker broker = createBroker(sql);
	if (broker.next())
		rapp = (Tipo_rapportoBulk)fetch(broker);
	broker.close();

	return rapp;
}
/**
  *	Ritorna TRUE se esiste il tipo rapporto <cdTipoRapporto> associato 
  * al terzo <vTerzo> e valido in data <data>, FALSE altrimenti
  *  
  *  Parametri:
  *	 - vTerzo
  *  - cdTipoRapporto
  *	 - data
  *
**/
public boolean isTipoRapportoValido(it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk vTerzo, String cdTipoRapporto, java.sql.Timestamp data) throws java.sql.SQLException{

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("V_TERZO_PER_COMPENSO");
	sql.addSQLJoin("TIPO_RAPPORTO.CD_TIPO_RAPPORTO","V_TERZO_PER_COMPENSO.CD_TIPO_RAPPORTO");
	sql.addSQLClause("AND","V_TERZO_PER_COMPENSO.CD_TERZO",sql.EQUALS,vTerzo.getCd_terzo());
	sql.addSQLClause("AND","V_TERZO_PER_COMPENSO.TI_DIPENDENTE_ALTRO",sql.EQUALS,vTerzo.getTi_dipendente_altro());
	sql.addSQLClause("AND","V_TERZO_PER_COMPENSO.CD_TIPO_RAPPORTO",sql.EQUALS,cdTipoRapporto);
	sql.addSQLClause("AND","V_TERZO_PER_COMPENSO.DT_INI_VALIDITA",sql.LESS_EQUALS,data);
	sql.addSQLClause("AND","V_TERZO_PER_COMPENSO.DT_FIN_VALIDITA",sql.GREATER_EQUALS,data);

	return sql.executeExistsQuery(getConnection());
}
}

