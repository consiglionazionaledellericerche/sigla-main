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

package it.cnr.contab.doccont00.core.bulk;

import java.sql.*;
import java.util.List;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Reversale_rigaHome extends BulkHome {
public Reversale_rigaHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public Reversale_rigaHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Reversale_rigaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Reversale_rigaHome(java.sql.Connection conn) {
	super(Reversale_rigaBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Reversale_rigaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Reversale_rigaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Reversale_rigaBulk.class,conn,persistentCache);
}
/**
 * Metodo per inizializzare il campo ti_fattura di una riga di una reversale legata ad una fattura passiva o attiva
 *
 * @param riga <code>Reversale_rigaBulk</code> la riga della reversale
 * @param tableName <code>String</code> il nome della tabella in cui effettuare la ricerca; può assumere i
 *				valori "FATTURA_PASSIVA" o "FATTTURA_ATTIVA"
 *
 *
 */
public void initializeTi_fatturaPerFattura( Reversale_rigaBulk riga, String tableName ) throws SQLException
{
	LoggableStatement ps = new LoggableStatement(getConnection(),"SELECT TI_FATTURA FROM " +
										  it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
										  tableName +
										  " WHERE CD_CDS = ? AND " +
										  "CD_UNITA_ORGANIZZATIVA = ? AND " +
										  "ESERCIZIO = ? AND " +
										  "PG_" + tableName + " = ? " ,true,this.getClass());
		try
		{
			ps.setString( 1, riga.getCd_cds_doc_amm() );
			ps.setString( 2, riga.getCd_uo_doc_amm());
			ps.setObject( 3, riga.getEsercizio_doc_amm());
			ps.setObject( 4, riga.getPg_doc_amm());
			ResultSet rs = ps.executeQuery();
			try
			{
				if ( rs.next() )
					riga.setTi_fattura( rs.getString(1));
			}
			finally
			{
				try{rs.close();}catch( java.sql.SQLException e ){};
			}		
		}
		finally
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}		
}
/**
 * Recupera tutti i dati nella tabella REVERSALE_SIOPE relativi alla riga reversale in uso.
 *
 * @param riga La riga di reversale in uso.
 *
 * @return java.util.Collection Collezione di oggetti <code>Reversale_siopeBulk</code>
 */
public java.util.Collection findCodiciCollegatiSIOPE(UserContext usercontext, Reversale_rigaBulk riga) throws PersistencyException {
	PersistentHome reversale_siopeHome = getHomeCache().getHome(Reversale_siopeIBulk.class);
	SQLBuilder sql = reversale_siopeHome.createSQLBuilder();
	sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, riga.getCd_cds());
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
	sql.addClause("AND", "pg_reversale", SQLBuilder.EQUALS, riga.getPg_reversale());
	sql.addClause("AND", "esercizio_accertamento", SQLBuilder.EQUALS, riga.getEsercizio_accertamento());
	sql.addClause("AND", "esercizio_ori_accertamento", SQLBuilder.EQUALS, riga.getEsercizio_ori_accertamento());
	sql.addClause("AND", "pg_accertamento", SQLBuilder.EQUALS, riga.getPg_accertamento());
	sql.addClause("AND", "pg_accertamento_scadenzario", SQLBuilder.EQUALS, riga.getPg_accertamento_scadenzario());
	sql.addClause("AND", "cd_cds_doc_amm", SQLBuilder.EQUALS, riga.getCd_cds_doc_amm());
	sql.addClause("AND", "cd_uo_doc_amm", SQLBuilder.EQUALS, riga.getCd_uo_doc_amm());
	sql.addClause("AND", "esercizio_doc_amm", SQLBuilder.EQUALS, riga.getEsercizio_doc_amm());
	sql.addClause("AND", "cd_tipo_documento_amm", SQLBuilder.EQUALS, riga.getCd_tipo_documento_amm());
	sql.addClause("AND", "pg_doc_amm", SQLBuilder.EQUALS, riga.getPg_doc_amm());
	return reversale_siopeHome.fetchAll(sql);
}
/**
 * Recupera tutti i dati nella tabella CODICI_SIOPE associabili alla riga reversale in uso.
 *
 * @param riga La riga di reversale in uso.
 *
 * @return java.util.Collection Collezione di oggetti <code>Codici_siopeBulk</code>
 */

public java.util.Collection findCodiciCollegabiliSIOPE (UserContext userContext, Reversale_rigaBulk riga) throws PersistencyException {
	String uoEnte = riga.getReversale().getCd_uo_ente();
	if (uoEnte == null) {
		PersistentHome uoEnteHome = getHomeCache().getHome(Unita_organizzativa_enteBulk.class);
		List result = uoEnteHome.fetchAll( uoEnteHome.createSQLBuilder() );
		uoEnte = ((Unita_organizzativaBulk) result.get(0)).getCd_unita_organizzativa();
	}

	if (uoEnte != null && 
		riga.getReversale().getCd_unita_organizzativa() != null && 
  	   !uoEnte.equals( riga.getReversale().getCd_unita_organizzativa()) &&
	    riga.getReversale().isTrasferimento()) {
		try
		{
			it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, riga.getEsercizio(), null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_CODICE_SIOPE_DEFAULT, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_REVERSALE_TRASFERIMENTO );
			if (config.getVal01()==null)
				return null;
			else
			{
				PersistentHome codice_siopeHome = getHomeCache().getHome(Codici_siopeBulk.class);
				SQLBuilder sql = codice_siopeHome.createSQLBuilder();
				sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
				sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
				sql.addClause("AND", "cd_siope", SQLBuilder.EQUALS, config.getVal01());
				return codice_siopeHome.fetchAll(sql);
			}	
		} catch ( Exception e ) {
			return null;
		}
	}
	else if (uoEnte != null && 
			 uoEnte.equals(CNRUserContext.getCd_unita_organizzativa(userContext)) && 
	  	     !uoEnte.equals( riga.getReversale().getCd_uo_origine()) &&
	  	     riga.getReversale().isSiopeDaCompletare() &&
	  	     !riga.getTipoAssociazioneSiope().equals(Reversale_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO)) {
		PersistentHome codice_siopeHome = getHomeCache().getHome(Codici_siopeBulk.class);
		SQLBuilder sql = codice_siopeHome.createSQLBuilder();
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
		return codice_siopeHome.fetchAll(sql);
	}
	else
	{
		initializeElemento_voce(userContext, riga);
	
		Elemento_voceHome elemento_voceHome = (Elemento_voceHome)getHomeCache().getHome(Elemento_voceBulk.class);
	
		if (riga.getElemento_voce().getFl_check_terzo_siope().booleanValue()) {
			TerzoBulk terzo = (TerzoBulk)getHomeCache().getHome(TerzoBulk.class).findByPrimaryKey(new TerzoBulk(riga.getCd_terzo()));
			AnagraficoBulk anagrafico = (AnagraficoBulk)getHomeCache().getHome(AnagraficoBulk.class).findByPrimaryKey(new AnagraficoBulk(terzo.getCd_anag()));
					
			return elemento_voceHome.findCodiciCollegatiSIOPE(userContext, riga.getElemento_voce(), anagrafico.getTipologia_istat());
		}
		else
			return elemento_voceHome.findCodiciCollegatiSIOPE(userContext, riga.getElemento_voce(), null);
	}
}
/**
 * Metodo per inizializzare il campo Elemento_voce di una riga di un mandato
 *
 * @param riga <code>Mandato_rigaBulk</code> la riga del mandato
 *
 */
public void initializeElemento_voce(UserContext userContext, Reversale_rigaBulk riga ) throws PersistencyException {
	if (riga.getElemento_voce()!=null) return;
	AccertamentoBulk acc = (AccertamentoBulk)getHomeCache().getHome(AccertamentoBulk.class).findByPrimaryKey(new AccertamentoBulk(riga.getCd_cds(),riga.getEsercizio_accertamento(),riga.getEsercizio_ori_accertamento(),riga.getPg_accertamento()));
	Elemento_voceBulk elemento_voce = (Elemento_voceBulk)getHomeCache().getHome(Elemento_voceBulk.class).findByPrimaryKey(new Elemento_voceBulk(acc.getCd_elemento_voce(), acc.getEsercizio(), acc.getTi_appartenenza(), acc.getTi_gestione()));
	riga.setElemento_voce(elemento_voce);
}
public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
	if (persistent instanceof Reversale_rigaBulk)
		initializeElemento_voce(userContext, (Reversale_rigaBulk)persistent);
	return super.completeBulkRowByRow(userContext, persistent);
}

public java.util.Collection findCodiciCupCollegati(UserContext usercontext, Reversale_rigaBulk riga) throws PersistencyException {
	PersistentHome reversaleCupHome = getHomeCache().getHome(ReversaleCupIBulk.class);
	SQLBuilder sql = reversaleCupHome.createSQLBuilder();
	sql.addClause("AND", "cdCds", SQLBuilder.EQUALS, riga.getCd_cds());
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
	sql.addClause("AND", "pgReversale", SQLBuilder.EQUALS, riga.getPg_reversale());
	sql.addClause("AND", "esercizioAccertamento", SQLBuilder.EQUALS, riga.getEsercizio_accertamento());
	sql.addClause("AND", "esercizioOriAccertamento", SQLBuilder.EQUALS, riga.getEsercizio_ori_accertamento());
	sql.addClause("AND", "pgAccertamento", SQLBuilder.EQUALS, riga.getPg_accertamento());
	sql.addClause("AND", "pgAccertamentoScadenzario", SQLBuilder.EQUALS, riga.getPg_accertamento_scadenzario());
	sql.addClause("AND", "cdCdsDocAmm", SQLBuilder.EQUALS, riga.getCd_cds_doc_amm());
	sql.addClause("AND", "cdUoDocAmm", SQLBuilder.EQUALS, riga.getCd_uo_doc_amm());
	sql.addClause("AND", "esercizioDocAmm", SQLBuilder.EQUALS, riga.getEsercizio_doc_amm());
	sql.addClause("AND", "cdTipoDocumentoAmm", SQLBuilder.EQUALS, riga.getCd_tipo_documento_amm());
	sql.addClause("AND", "pgDocAmm", SQLBuilder.EQUALS, riga.getPg_doc_amm());
	return reversaleCupHome.fetchAll(sql);
}
}
