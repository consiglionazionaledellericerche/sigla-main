package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_passiva_intraHome extends BulkHome {
public Fattura_passiva_intraHome(java.sql.Connection conn) {
	super(Fattura_passiva_intraBulk.class,conn);
}
public Fattura_passiva_intraHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_passiva_intraBulk.class,conn,persistentCache);
}
/**
 * Inizializza la chiave primaria di un OggettoBulk per un
 * inserimento. Da usare principalmente per riempire i progressivi
 * automatici.
 * @param bulk l'OggettoBulk da inizializzare  
 */
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {

	if (bulk == null) return;
	try {
		Fattura_passiva_intraBulk riga = (Fattura_passiva_intraBulk)bulk;
		riga.setPg_riga_intra(new Long(
				((Long)findAndLockMax( bulk, "pg_riga_intra", new Long(0))).longValue()+1));
		
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new PersistencyException(e);
	}
}

public SQLBuilder selectNomenclatura_combinataByClause( Fattura_passiva_intraBulk bulk, Nomenclatura_combinataHome home, Nomenclatura_combinataBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause("AND", "esercizio_inizio", SQLBuilder.LESS_EQUALS, bulk.getEsercizio() );
	sql.addClause("AND", "esercizio_fine", SQLBuilder.GREATER_EQUALS, bulk.getEsercizio() );
	sql.addClause( clause );
	return sql;
}
public SQLBuilder selectNatura_transazioneByClause( Fattura_passiva_intraBulk bulk, Natura_transazioneHome home, Natura_transazioneBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause );
	return sql;
}
public SQLBuilder selectCodici_cpaByClause( Fattura_passiva_intraBulk bulk,Codici_cpaHome home, Codici_cpaBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause("AND", "fl_utilizzabile", SQLBuilder.EQUALS,new Boolean(true));
	sql.addClause("AND","ti_bene_servizio", SQLBuilder.EQUALS,"S");
	sql.addClause( clause );
	return sql;
}
public SQLBuilder selectNazione_provenienzaByClause( Fattura_passiva_intraBulk bulk,NazioneHome home, NazioneBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause( clause );
	if(bulk.getFattura_passiva().getTi_bene_servizio().compareTo(Bene_servizioBulk.SERVIZIO)!=0)
	{
		sql.addClause("AND","ti_nazione", SQLBuilder.EQUALS,NazioneBulk.CEE);
		sql.addClause("AND","struttura_piva",SQLBuilder.ISNOTNULL,null);
	}
	return sql;
}
}
