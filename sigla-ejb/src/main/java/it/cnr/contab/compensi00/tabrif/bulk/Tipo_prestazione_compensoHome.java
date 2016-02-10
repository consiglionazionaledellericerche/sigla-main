package it.cnr.contab.compensi00.tabrif.bulk;

import java.sql.*;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_prestazione_compensoHome extends BulkHome {
public Tipo_prestazione_compensoHome(java.sql.Connection conn) {
	super(Tipo_prestazione_compensoBulk.class,conn);
}
public Tipo_prestazione_compensoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_prestazione_compensoBulk.class,conn,persistentCache);
}
/*
public Tipo_prestazione_compensoBulk findTipoPrestazioneCompenso(String cdTipoRapporto) throws PersistencyException{

	// Ritorna tutti i tipi prestazione compenso associati al rapporto 
	
	SQLBuilder sql = createSQLBuilder();

	sql.addTableToHeader("ASS_TI_RAPP_TI_PREST");
	sql.addSQLJoin("ASS_TI_RAPP_TI_PREST.CD_TIPO_PRESTAZIONE","TIPO_PRESTAZIONE_COMPENSO.CD_TIPO_PRESTAZIONE");
	sql.addSQLClause(FindClause.AND,"ASS_TI_RAPP_TI_PREST.CD_TIPO_RAPPORTO",SQLBuilder.EQUALS,cdTipoRapporto);

	Tipo_prestazione_compensoBulk tipoPrest = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		tipoPrest = (Tipo_prestazione_compensoBulk)fetch(broker);
	broker.close();

	return tipoPrest;
	
}
*/
public java.util.List findTipiPrestazioneCompenso(String cdTipoRapporto) throws PersistencyException{

	// Ritorna tutti i tipi rapporto 
	
	SQLBuilder sql = createSQLBuilder();

	sql.addTableToHeader("ASS_TI_RAPP_TI_PREST");
	sql.addSQLJoin("ASS_TI_RAPP_TI_PREST.CD_TI_PRESTAZIONE","TIPO_PRESTAZIONE_COMPENSO.CD_TI_PRESTAZIONE");
	sql.addSQLClause(FindClause.AND,"ASS_TI_RAPP_TI_PREST.CD_TIPO_RAPPORTO",SQLBuilder.EQUALS,cdTipoRapporto);

	return fetchAll(sql);
}
public java.util.List findTipiPrestazioneCompensoDaMinicarriera(String cdTipoRapporto) throws PersistencyException{


	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND", "FL_CONTRATTO", sql.EQUALS, new String("N"));
	sql.addTableToHeader("ASS_TI_RAPP_TI_PREST");
	sql.addSQLJoin("ASS_TI_RAPP_TI_PREST.CD_TI_PRESTAZIONE","TIPO_PRESTAZIONE_COMPENSO.CD_TI_PRESTAZIONE");
	sql.addSQLClause(FindClause.AND,"ASS_TI_RAPP_TI_PREST.CD_TIPO_RAPPORTO",SQLBuilder.EQUALS,cdTipoRapporto);

	return fetchAll(sql);
}
}
