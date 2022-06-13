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

/*
* Created by Generator 1.0
* Date 25/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import java.util.Dictionary;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_entrata_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Pdg_variazioneHome extends BulkHome {
	public Pdg_variazioneHome(java.sql.Connection conn) {
		super(Pdg_variazioneBulk.class, conn);
	}
	public Pdg_variazioneHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_variazioneBulk.class, conn, persistentCache);
	}
	/**
	 * Recupera tutti i dati nella tabella ASS_PDG_VARIAZIONE_CDR relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Ass_pdg_variazione_cdrBulk</code>
	 */

	public java.util.Collection findAssociazioneCDR(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Ass_pdg_variazione_cdrBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera tutti i dati nella tabella PDG_VARIAZIONE_ARCHIVIO relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_variazione_archivioBulk</code>
	 */

	public java.util.Collection findArchivioConsultazioni(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_archivioBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addOrderBy("PROGRESSIVO_RIGA");
		return dettHome.fetchAll(sql);
	}	

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk pdg) throws PersistencyException,ApplicationException {
		try {
			String jndiName = "CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession";
			//Nel caso in cui la variazione viene creata in automatico da altri processi (tipo obbligazione) allora la richiesta deve usare la stessa
			// transazione e non crearne una nuova, in modo che se la procedura va in errore  il numeratore non viene incrementato inutilmente
			if (Pdg_variazioneBulk.MOTIVAZIONE_VARIAZIONE_AUTOMATICA.equals(((Pdg_variazioneBulk)pdg).getTiMotivazioneVariazione()))
				jndiName = "CNRCONFIG00_TABNUM_EJB_TREQUIRED_Numerazione_baseComponentSession";

			it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
				(it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
					it.cnr.jada.util.ejb.EJBCommonServices.createEJB(jndiName,
							it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
			((Pdg_variazioneBulk)pdg).setPg_variazione_pdg(
				 numerazione.creaNuovoProgressivo(userContext,CNRUserContext.getEsercizio(userContext), "PDG_VARIAZIONE", "PG_VARIAZIONE_PDG", CNRUserContext.getUser(userContext))
			);
			((Pdg_variazioneBulk)pdg).setEsercizio(CNRUserContext.getEsercizio(userContext));
		}catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new ApplicationException(e);
		}catch(Throwable e) {
			throw new PersistencyException(e);
		}
	}
	public java.util.Collection findDettagliSpesa(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_preventivo_spe_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliEntrata(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_preventivo_etr_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliSpesaGestionale(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_spese_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliEntrataGestionale(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_entrate_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO_PDG_VARIAZIONE",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		return dettHome.fetchAll(sql);
	}
	public Dictionary loadCausaliMancataApprovazione(it.cnr.jada.UserContext userContext) throws PersistencyException, IntrospectionException {
		java.util.Dictionary list = new it.cnr.jada.util.OrderedHashtable();

		PersistentHome dettHome = getHomeCache().getHome(Configurazione_cnrBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","CD_UNITA_FUNZIONALE",sql.EQUALS,"*");		
		sql.addSQLClause("AND","CD_CHIAVE_PRIMARIA",sql.EQUALS,"PDG_VARIAZIONE");

		java.util.Collection coll = dettHome.fetchAll(sql);

		for (java.util.Iterator causali = coll.iterator();causali.hasNext();){
			Configurazione_cnrBulk rec_causali = (Configurazione_cnrBulk)causali.next();			
			list.put(rec_causali.getCd_chiave_secondaria(), rec_causali.getVal01());
		}
		return list;
	}
	/**
	 * Recupera tutti i dati nella tabella PDG_VARIAZIONE_RIGA_GEST relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_variazione_riga_gestBulk</code>
	 */
	public java.util.Collection findDettagliVariazioneGestionale(Pdg_variazioneBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",sql.EQUALS,testata.getPg_variazione_pdg());

		return dettHome.fetchAll(sql);
	}	

	/**
	 * Recupera tutti i dati di entrata nella tabella PDG_VARIAZIONE_RIGA_GEST relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_variazione_riga_gestBulk</code>
	 */
	public java.util.Collection findDettagliEntrataVariazioneGestionale(Pdg_variazioneBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addClause("AND","ti_gestione",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);

		return dettHome.fetchAll(sql);
	}	

	/**
	 * Recupera tutti i dati di spesa nella tabella PDG_VARIAZIONE_RIGA_GEST relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_variazione_riga_gestBulk</code>
	 */
	public java.util.Collection<Pdg_variazione_riga_gestBulk> findDettagliSpesaVariazioneGestionale(Pdg_variazioneBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addClause("AND","ti_gestione",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);

		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera tutti i dati nella tabella PDG_VARIAZIONE_RIGA_GEST relativi alla testata in uso che
	 * devono ancora essere oggetto di visto da parte dei dipartimenti
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_variazione_riga_gestBulk</code>
	 */
	public java.util.Collection findDettagliVariazioneGestionaleDaVistare(Pdg_variazioneBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione_pdg",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addClause("AND","fl_visto_dip_variazioni",sql.EQUALS,Boolean.FALSE);

		return dettHome.fetchAll(sql);
	}	
	public java.util.Collection findRiepilogoEntrate(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(V_pdg_variazione_riepilogoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
		return dettHome.fetchAll(sql);
	}	
	public java.util.Collection findRiepilogoSpese(Pdg_variazioneBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(V_pdg_variazione_riepilogoBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
		return dettHome.fetchAll(sql);
	}	
	public java.util.Collection findDettagliSpesaVariazioneGestionalePrelievo(Pdg_variazioneBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("ELEMENTO_VOCE");
		sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_GESTIONE","PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_APPARTENENZA","PDG_VARIAZIONE_RIGA_GEST.TI_APPARTENENZA");
		sql.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE","PDG_VARIAZIONE_RIGA_GEST.CD_ELEMENTO_VOCE");
		sql.addSQLClause("AND","FL_PRELIEVO",sql.EQUALS,"Y");
		sql.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);

		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliEntrateVariazioneGestionaleSoggettePrelievo(Pdg_variazioneBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_variazione_riga_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addTableToHeader("ELEMENTO_VOCE");
		sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_GESTIONE","PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_APPARTENENZA","PDG_VARIAZIONE_RIGA_GEST.TI_APPARTENENZA");
		sql.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE","PDG_VARIAZIONE_RIGA_GEST.CD_ELEMENTO_VOCE");
		sql.addSQLClause("AND","FL_SOGGETTO_PRELIEVO",sql.EQUALS,"Y");
		sql.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE_PDG",sql.EQUALS,testata.getPg_variazione_pdg());
		sql.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);

		return dettHome.fetchAll(sql);
	}	

}