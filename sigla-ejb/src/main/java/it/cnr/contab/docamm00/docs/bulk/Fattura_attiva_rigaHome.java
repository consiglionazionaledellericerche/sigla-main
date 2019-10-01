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

package it.cnr.contab.docamm00.docs.bulk;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_attiva_rigaHome extends BulkHome {
public Fattura_attiva_rigaHome(Class classe, java.sql.Connection conn) {
	super(classe,conn);
}

public Fattura_attiva_rigaHome(Class classe, java.sql.Connection conn,PersistentCache persistentCache) {
	super(classe,conn,persistentCache);
}

public Fattura_attiva_rigaHome(java.sql.Connection conn) {
	super(Fattura_attiva_rigaBulk.class,conn);
}

public Fattura_attiva_rigaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_attiva_rigaBulk.class,conn,persistentCache);
}

public java.util.List findAddebitiForAccertamentoExceptFor(
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk scadenza,
	Fattura_attivaBulk fattura) 
	throws PersistencyException {

	return fetchAll(selectForAccertamentoExceptFor(scadenza, fattura));
}

public java.util.List findStorniForAccertamentoExceptFor(
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk scadenza,
	Fattura_attivaBulk fattura) 
	throws PersistencyException {

	return fetchAll(selectForAccertamentoExceptFor(scadenza, fattura));
}

/**
 * Inizializza la chiave primaria di un OggettoBulk per un
 * inserimento. Da usare principalmente per riempire i progressivi
 * automatici.
 * @param bulk l'OggettoBulk da inizializzare  
 */
public SQLBuilder selectAccertamentiPer(
	it.cnr.jada.UserContext userContext,
	Fattura_attivaBulk fatturaAttiva,
	java.math.BigDecimal minIm_scadenza)
	throws PersistencyException {

	if (fatturaAttiva == null) return null;
	
	TerzoBulk cliente = fatturaAttiva.getCliente();
	if (cliente != null) {
		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("FATTURA_ATTIVA");
		sql.addTableToHeader("ACCERTAMENTO_SCADENZARIO");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.ESERCIZIO", "FATTURA_ATTIVA.ESERCIZIO");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_CDS", "FATTURA_ATTIVA.CD_CDS");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_UNITA_ORGANIZZATIVA", "FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.PG_FATTURA_ATTIVA", "FATTURA_ATTIVA.PG_FATTURA_ATTIVA");
		
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_CDS_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.CD_CDS");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.ESERCIZIO_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.ESERCIZIO");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.ESERCIZIO_ORI_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.PG_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.PG_ACCERTAMENTO");
		sql.addSQLJoin("FATTURA_ATTIVA_RIGA.PG_ACCERTAMENTO_SCADENZARIO", "ACCERTAMENTO_SCADENZARIO.PG_ACCERTAMENTO_SCADENZARIO");

		sql.addSQLClause("AND","FATTURA_ATTIVA.CD_TERZO",sql.EQUALS, cliente.getCd_terzo());
		sql.addSQLClause("AND","FATTURA_ATTIVA.TI_FATTURA",sql.EQUALS, Fattura_attiva_IBulk.TIPO_FATTURA_ATTIVA);
		sql.addSQLClause("AND","FATTURA_ATTIVA_RIGA.STATO_COFI",sql.EQUALS, Fattura_attiva_rigaBulk.STATO_CONTABILIZZATO);
		sql.addSQLClause("AND","FATTURA_ATTIVA.CD_CDS_ORIGINE", sql.EQUALS, fatturaAttiva.getCd_cds_origine());
		sql.addSQLClause("AND","FATTURA_ATTIVA.CD_UO_ORIGINE", sql.EQUALS, fatturaAttiva.getCd_uo_origine());
		sql.addSQLClause("AND","FATTURA_ATTIVA.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		
		sql.addSQLClause("AND","ACCERTAMENTO_SCADENZARIO.ESERCIZIO",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","ACCERTAMENTO_SCADENZARIO.IM_SCADENZA",sql.GREATER_EQUALS, minIm_scadenza);
		
		return sql;	
	}
	return null;
}

protected SQLBuilder selectForAccertamentoExceptFor(
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk scadenza,
	Fattura_attivaBulk fattura) {
	
	SQLBuilder sql = createSQLBuilder();

	sql.addTableToHeader("FATTURA_ATTIVA");
	sql.addSQLJoin("FATTURA_ATTIVA_RIGA.ESERCIZIO", "FATTURA_ATTIVA.ESERCIZIO");
	sql.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_CDS", "FATTURA_ATTIVA.CD_CDS");
	sql.addSQLJoin("FATTURA_ATTIVA_RIGA.CD_UNITA_ORGANIZZATIVA", "FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLJoin("FATTURA_ATTIVA_RIGA.PG_FATTURA_ATTIVA", "FATTURA_ATTIVA.PG_FATTURA_ATTIVA");
	sql.addSQLClause("AND","FATTURA_ATTIVA_RIGA.CD_CDS_ACCERTAMENTO",sql.EQUALS, scadenza.getCd_cds());
	sql.addSQLClause("AND","FATTURA_ATTIVA_RIGA.ESERCIZIO_ACCERTAMENTO",sql.EQUALS, scadenza.getEsercizio());
	sql.addSQLClause("AND","FATTURA_ATTIVA_RIGA.ESERCIZIO_ORI_ACCERTAMENTO",sql.EQUALS, scadenza.getEsercizio_originale());
	sql.addSQLClause("AND","FATTURA_ATTIVA_RIGA.PG_ACCERTAMENTO",sql.EQUALS, scadenza.getPg_accertamento());
	sql.addSQLClause("AND","FATTURA_ATTIVA_RIGA.PG_ACCERTAMENTO_SCADENZARIO",sql.EQUALS, scadenza.getPg_accertamento_scadenzario());
	sql.addSQLClause("AND","FATTURA_ATTIVA_RIGA.STATO_COFI",sql.NOT_EQUALS, Fattura_attiva_rigaBulk.STATO_ANNULLATO);

	if (fattura != null) {
		sql.addSQLClause("AND", "FATTURA_ATTIVA.PG_FATTURA_ATTIVA", sql.NOT_EQUALS, fattura.getPg_fattura_attiva());
		sql.addSQLClause("AND", "FATTURA_ATTIVA.CD_CDS", sql.EQUALS, fattura.getCd_cds());
		sql.addSQLClause("AND", "FATTURA_ATTIVA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, fattura.getCd_unita_organizzativa());
	}

	return sql;
}

private SQLBuilder selectRigaFor(Fattura_attiva_rigaIBulk rigaFattura) {
	
	SQLBuilder sql = createSQLBuilder();

	if (rigaFattura != null) {
		sql.addSQLClause("AND", "FATTURA_ATTIVA_RIGA.CD_CDS_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getCd_cds());
		sql.addSQLClause("AND", "FATTURA_ATTIVA_RIGA.CD_UO_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "FATTURA_ATTIVA_RIGA.ESERCIZIO_RIGA_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getEsercizio());
		sql.addSQLClause("AND", "FATTURA_ATTIVA_RIGA.PG_FATTURA_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getPg_fattura_attiva());
		sql.addSQLClause("AND", "FATTURA_ATTIVA_RIGA.PG_RIGA_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getProgressivo_riga());
	}
	return sql;
}
}