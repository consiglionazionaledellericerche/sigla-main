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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (12/10/2005 16.00.00)
 * @author: Raffaele Pagano
 */
public class ObbligazioneRes_impropriaHome extends ObbligazioneHome {
/**
 * ObbligazioneRes_impropriaHome constructor comment.
 * @param conn java.sql.Connection
 */
public ObbligazioneRes_impropriaHome(java.sql.Connection conn) {
	super(ObbligazioneRes_impropriaBulk.class, conn);
}
/**
 * ObbligazioneRes_impropriaHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public ObbligazioneRes_impropriaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(ObbligazioneRes_impropriaBulk.class, conn, persistentCache);
}
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, new Boolean(false));
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA );
	return sql;
}
/**
 * Metodo per cercare la voce del piano dei conti 
 *
 * @param bulk <code>ObbligazioneRes_impropriaBulk</code> il contesto (obbligazione) in cui viene fatta la ricerca dell'elemento voce
 * @param home istanza di <code>Elemento_voceHome</code>
 * @param bulkClause <code>OggettoBulk</code> elemento voce su cui viene fatta la ricerca
 * @param clause <code>CompoundFindClause</code> le clausole della selezione
 *
 * @return sql il risultato della selezione
 *
 */
public SQLBuilder selectVoceByClause( ObbligazioneRes_impropriaBulk bulk, Voce_fHome home,OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = getHomeCache().getHome( Voce_fBulk.class, "V_VOCE_F_PARTITA_GIRO").createSQLBuilder();
	sql.addClause( clause );
	sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addSQLClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR );
	sql.addSQLClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	sql.addSQLClause("AND", "fl_partita_giro", SQLBuilder.EQUALS, "N" );
	sql.addSQLClause("AND", "fl_mastrino", SQLBuilder.EQUALS, "Y" );		
	return sql;
		
}
/* (non-Javadoc)
 * @see it.cnr.contab.doccont00.core.bulk.ObbligazioneHome#initializePrimaryKeyForInsert(it.cnr.jada.UserContext, it.cnr.jada.bulk.OggettoBulk)
 */
 public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
 {
	 ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
	 if (obbligazione.getPg_obbligazione() == null) 
		super.initializePrimaryKeyForInsert(userContext, bulk);
 }

 /**
  * <!-- @TODO: da completare -->
  * 
  *
  * @param cdrList	
  * @param capitoliList	
  * @return 
  * @throws IntrospectionException	
  * @throws PersistencyException	
  */
 public java.util.List findLineeAttivita( List cdrList, List capitoliList, ObbligazioneBulk obbligazione ) throws IntrospectionException,PersistencyException 
 {
	int size = cdrList.size() ;
		
	if ( size == 0 )
		return Collections.EMPTY_LIST;
			
	IVoceBilancioBulk capitolo = (IVoceBilancioBulk) capitoliList.iterator().next();
			
	V_pdg_obbligazione_speHome home = (V_pdg_obbligazione_speHome)getHomeCache().getHome(V_pdg_obbligazione_speBulk.class);

	SQLBuilder sql = home.createSQLBuilder();

	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, capitolo.getEsercizio() );
	sql.addClause("AND", "esercizio_res", SQLBuilder.EQUALS, obbligazione.getEsercizio_originale()==null?capitolo.getEsercizio():obbligazione.getEsercizio_originale());
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	sql.addClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, capitolo.getCd_titolo_capitolo());

	sql.openParenthesis("AND");
		sql.addClause("OR", "categoria_dettaglio", SQLBuilder.EQUALS, Pdg_preventivo_detBulk.CAT_SINGOLO );		
		sql.addClause("OR", "categoria_dettaglio", SQLBuilder.EQUALS, Pdg_modulo_spese_gestBulk.CAT_DIRETTA );
	sql.closeParenthesis();
					
	sql.openParenthesis("AND");
		Iterator a = cdrList.iterator();
		sql.addClause("OR", "cd_centro_responsabilita", SQLBuilder.EQUALS, ((CdrBulk)a.next()).getCd_centro_responsabilita() );		
		for ( int j = 0; a.hasNext(); j++ )
			sql.addClause("OR", "cd_centro_responsabilita", SQLBuilder.EQUALS, ((CdrBulk)a.next()).getCd_centro_responsabilita() );		
	sql.closeParenthesis();

	sql.openParenthesis("AND");
		Iterator i = capitoliList.iterator();
		sql.addClause("OR", "cd_funzione", SQLBuilder.EQUALS, ((IVoceBilancioBulk)i.next()).getCd_funzione());
		for ( int j = 0; i.hasNext(); j++ )
			sql.addClause("OR", "cd_funzione", SQLBuilder.EQUALS, ((IVoceBilancioBulk)i.next()).getCd_funzione());
	sql.closeParenthesis();

	return home.fetchAll(sql);
 }
}
