package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_passiva_rigaHome extends BulkHome {
public Fattura_passiva_rigaHome(Class classe, java.sql.Connection conn) {
	super(classe,conn);
}
public Fattura_passiva_rigaHome(Class classe, java.sql.Connection conn,PersistentCache persistentCache) {
	super(classe,conn,persistentCache);
}
public Fattura_passiva_rigaHome(java.sql.Connection conn) {
	super(Fattura_passiva_rigaBulk.class,conn);
}
public Fattura_passiva_rigaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_passiva_rigaBulk.class,conn,persistentCache);
}
public java.util.List findAddebitiForObbligazioneExceptFor(
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
	Fattura_passivaBulk fattura) 
	throws PersistencyException {

	return fetchAll(selectForObbligazioneExceptFor(scadenza, fattura));
}
public java.util.List findStorniForObbligazioneExceptFor(
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
	Fattura_passivaBulk fattura) 
	throws PersistencyException {

	return fetchAll(selectForObbligazioneExceptFor(scadenza, fattura));
}
protected SQLBuilder selectForObbligazioneExceptFor(
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
	Fattura_passivaBulk fattura) {
	
	SQLBuilder sql = createSQLBuilder();

	sql.addTableToHeader("FATTURA_PASSIVA");
	sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO", "FATTURA_PASSIVA.ESERCIZIO");
	sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS", "FATTURA_PASSIVA.CD_CDS");
	sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA", "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA", "FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
	sql.addSQLClause("AND","FATTURA_PASSIVA_RIGA.CD_CDS_OBBLIGAZIONE",sql.EQUALS, scadenza.getCd_cds());
	sql.addSQLClause("AND","FATTURA_PASSIVA_RIGA.ESERCIZIO_OBBLIGAZIONE",sql.EQUALS, scadenza.getEsercizio());
	sql.addSQLClause("AND","FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE",sql.EQUALS, scadenza.getEsercizio_originale());
	sql.addSQLClause("AND","FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE",sql.EQUALS, scadenza.getPg_obbligazione());
	sql.addSQLClause("AND","FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE_SCADENZARIO",sql.EQUALS, scadenza.getPg_obbligazione_scadenzario());
	sql.addSQLClause("AND","FATTURA_PASSIVA_RIGA.STATO_COFI",sql.NOT_EQUALS, Fattura_passiva_rigaBulk.STATO_ANNULLATO);
//FL_BOLLA_DOGANALE, FL_SPEDIZIONIERE
	//sql.addSQLClause("AND","FATTURA_PASSIVA.FL_BOLLA_DOGANALE",sql.EQUALS, Boolean.FALSE);
	//sql.addSQLClause("AND","FATTURA_PASSIVA.FL_SPEDIZIONIERE",sql.EQUALS, Boolean.FALSE);

	if (fattura != null) {
		sql.addSQLClause("AND", "FATTURA_PASSIVA.PG_FATTURA_PASSIVA", sql.NOT_EQUALS, fattura.getPg_fattura_passiva());
		sql.addSQLClause("AND", "FATTURA_PASSIVA.CD_CDS", sql.EQUALS, fattura.getCd_cds_origine());
		sql.addSQLClause("AND", "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, fattura.getCd_uo_origine());
		sql.addSQLClause("AND", "FATTURA_PASSIVA.PG_LETTERA", sql.ISNULL, null);
	}

	return sql;
}
/**
 * Inizializza la chiave primaria di un OggettoBulk per un
 * inserimento. Da usare principalmente per riempire i progressivi
 * automatici.
 * @param bulk l'OggettoBulk da inizializzare  
 */
public SQLBuilder selectObbligazioniPer(
	it.cnr.jada.UserContext userContext,
	Fattura_passivaBulk fatturaPassiva,
	java.math.BigDecimal minIm_scadenza)
	throws PersistencyException {

	if (fatturaPassiva == null) return null;
	
	TerzoBulk fornitore = fatturaPassiva.getFornitore();
	if (fornitore != null) {
		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("FATTURA_PASSIVA");
		sql.addTableToHeader("OBBLIGAZIONE_SCADENZARIO");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO", "FATTURA_PASSIVA.ESERCIZIO");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS", "FATTURA_PASSIVA.CD_CDS");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_UNITA_ORGANIZZATIVA", "FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_FATTURA_PASSIVA", "FATTURA_PASSIVA.PG_FATTURA_PASSIVA");
		
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.CD_CDS_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.CD_CDS");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE");
		sql.addSQLJoin("FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE_SCADENZARIO", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO");

		sql.addSQLClause("AND","FATTURA_PASSIVA.TI_FATTURA",sql.EQUALS, Fattura_passiva_IBulk.TIPO_FATTURA_PASSIVA);
		sql.addSQLClause("AND","FATTURA_PASSIVA.CD_TERZO",sql.EQUALS, fornitore.getCd_terzo());
		//sql.addSQLClause("AND","FATTURA_PASSIVA.PG_LETTERA",sql.ISNULL, null);
		//sql.addSQLClause("AND","FATTURA_PASSIVA.PROTOCOLLO_IVA",sql.ISNULL, null);
		//sql.addSQLClause("AND","FATTURA_PASSIVA.PROTOCOLLO_IVA_GENERALE",sql.ISNULL, null);
		sql.addSQLClause("AND","FATTURA_PASSIVA.STATO_PAGAMENTO_FONDO_ECO",sql.EQUALS, Fattura_passiva_IBulk.NO_FONDO_ECO);
		sql.addSQLClause("AND","FATTURA_PASSIVA_RIGA.STATO_COFI",sql.EQUALS, Fattura_passiva_rigaBulk.STATO_CONTABILIZZATO);
		sql.addSQLClause("AND","FATTURA_PASSIVA.CD_CDS", sql.EQUALS, fatturaPassiva.getCd_cds_origine());
		sql.addSQLClause("AND","FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, fatturaPassiva.getCd_uo_origine());
		sql.addSQLClause("AND","FATTURA_PASSIVA.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		
		//sql.addSQLClause("AND","FATTURA_PASSIVA.FL_BOLLA_DOGANALE",sql.EQUALS, Boolean.FALSE);
		//sql.addSQLClause("AND","FATTURA_PASSIVA.FL_SPEDIZIONIERE",sql.EQUALS, Boolean.FALSE);

		sql.addSQLClause("AND","OBBLIGAZIONE_SCADENZARIO.ESERCIZIO",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA",sql.GREATER_EQUALS, minIm_scadenza);
		
		return sql;
	}
	return null;
}
private SQLBuilder selectRigaFor(Fattura_passiva_rigaIBulk rigaFattura) {
	
	SQLBuilder sql = createSQLBuilder();

	if (rigaFattura != null) {
		sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.CD_CDS_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getCd_cds());
		sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.CD_UO_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.ESERCIZIO_RIGA_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getEsercizio());
		sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.PG_FATTURA_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getPg_fattura_passiva());
		sql.addSQLClause("AND", "FATTURA_PASSIVA_RIGA.PG_RIGA_ASSNCNA_FIN", sql.EQUALS, rigaFattura.getProgressivo_riga());
	}
	return sql;
}
public SQLBuilder selectModalita(Fattura_passiva_rigaBulk rigaFattura,it.cnr.contab.docamm00.tabrif.bulk.DivisaHome divisaHome,it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk clause) {
	
	return divisaHome.createSQLBuilder();
}
}
