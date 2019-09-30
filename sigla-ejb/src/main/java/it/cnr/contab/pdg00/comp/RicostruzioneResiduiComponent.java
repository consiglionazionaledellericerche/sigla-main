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
 * Created on Apr 6, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioHome;
import it.cnr.contab.pdg00.bulk.Pdg_residuoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_residuoHome;
import it.cnr.contab.pdg00.bulk.Pdg_residuo_detBulk;
import it.cnr.contab.pdg00.bulk.Stampa_ricostruzione_residui_LAVBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteHome;
import it.cnr.contab.utenze00.bulk.UtenteKey;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RicostruzioneResiduiComponent extends CRUDComponent implements Cloneable, IPrintMgr, Serializable {

	private OggettoBulk validaBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
	  try{	
		Pdg_residuoBulk testata = (Pdg_residuoBulk)bulk;
		if (testata.getStato().equals(testata.STATO_CHIUSO)&&testata.getImTotaleDifferenza().compareTo(new BigDecimal("0"))!=0)
		   throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare la ricostruzione residui è necessario che la differenza sia nulla!");

		Timestamp data, datain;
		try {
			data = getHome(userContext, UtenteBulk.class).getServerTimestamp();
		} catch (PersistencyException e) {
			throw handleException( e );
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		try {
			datain = new java.sql.Timestamp(sdf.parse("01/01/"+CNRUserContext.getEsercizio(userContext).intValue()).getTime());
		} catch (ParseException e) {
			throw handleException(e);
		}

		if (testata.getStato().equals(testata.STATO_MODIFICA)) {
			Iterator it = ((Pdg_residuoBulk)bulk).getDettagli().iterator();
			while (it.hasNext()) {
				Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk)it.next();
				if (dett.getDt_registrazione()==null||dett.isToBeUpdated())
					dett.setDt_registrazione(data);
			}
		}
		else {
			Iterator it = ((Pdg_residuoBulk)bulk).getDettagli().iterator();
			while (it.hasNext()) {
				Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk)it.next();
				if (dett.getDt_registrazione()==null||dett.isToBeUpdated())
					dett.setDt_registrazione(datain);
			}
		}
		// alla fine di tutto si lanciano i messaggi
		// se lo stato è portato in aperto per modifica
		if (testata.getEsercizio()!=null&&testata.getCd_centro_responsabilita()!=null) {
			try {	
				Pdg_residuoHome oldResHome = (Pdg_residuoHome) getHomeCache(userContext).getHome( Pdg_residuoBulk.class );
				Pdg_residuoBulk oldResiduoKey = new Pdg_residuoBulk(testata.getEsercizio(),testata.getCd_centro_responsabilita());
				Pdg_residuoBulk oldResiduo = (Pdg_residuoBulk)oldResHome.findByPrimaryKey(oldResiduoKey);

				if (!oldResiduo.getStato().equals(Pdg_residuoBulk.STATO_MODIFICA)&&testata.getStato().equals(Pdg_residuoBulk.STATO_MODIFICA)) {
					MessaggioHome messHome = (MessaggioHome)getHome(userContext,MessaggioBulk.class);
					UtenteHome utenteHome = (UtenteHome)getHome(userContext,UtenteBulk.class);
					for (java.util.Iterator i = utenteHome.findUtenteByCDR(testata.getCd_centro_responsabilita()).iterator();i.hasNext();){
						UtenteBulk utente = (UtenteBulk)i.next();
						MessaggioBulk messaggio = generaMessaggio(userContext,utente,testata);
						super.creaConBulk(userContext, messaggio);
					}
				}
			} catch (PersistencyException e) {
			   throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
			}	
		}
	  }catch(NullPointerException e){
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare la ricostruzione residui è necessario valorizzare i campi obbligatori!");
	  }
	  return bulk;
	}
	
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
		validaBulk(userContext, (Pdg_residuoBulk)bulk );
		return super.creaConBulk(userContext, bulk);
	}

	public void eliminaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		for(int i = 0; ((Pdg_residuoBulk)bulk).getDettagli().size() > i; i++) {
			((Pdg_residuo_detBulk) ((Pdg_residuoBulk)bulk).getDettagli().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
		}
	  	super.eliminaConBulk(userContext, bulk);
	}
	public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
		validaBulk(userContext, (Pdg_residuoBulk)bulk );
		return super.modificaConBulk(userContext, bulk);
	}  
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Pdg_residuoBulk testata = (Pdg_residuoBulk)super.inizializzaBulkPerModifica(userContext,bulk);
			Pdg_residuoHome testataHome = (Pdg_residuoHome)getHome(userContext, Pdg_residuoBulk.class);
			// lock sul record in modo che nessuno ci possa lavorare
			bulk = (OggettoBulk)testataHome.findAndLock(bulk);
			testata.setDettagli(new it.cnr.jada.bulk.BulkList(testataHome.findDettagli(testata)));
			getHomeCache(userContext).fetchAll(userContext);
			return testata;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public Pdg_residuoBulk caricaDettagliFiltrati(UserContext userContext, CompoundFindClause clauses, Pdg_residuoBulk testata) throws it.cnr.jada.comp.ComponentException {
		try {
			Pdg_residuoHome testataHome = (Pdg_residuoHome)getHome(userContext, Pdg_residuoBulk.class);
			testata.setDettagli(new it.cnr.jada.bulk.BulkList(testataHome.findDettagli(testata, clauses)));
			getHomeCache(userContext).fetchAll(userContext);
			return testata;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public Query select(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException
	{
		SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
		Pdg_residuoBulk var = (Pdg_residuoBulk) bulk;

		sql = selectCdr(userContext,sql);

		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.setOrderBy("pg_dettaglio", it.cnr.jada.util.OrderConstants.ORDER_ASC);
	
		return sql;
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectCentro_responsabilitaByClause(UserContext userContext, Pdg_residuoBulk residuo, CdrBulk cdr, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, CdrBulk.class).createSQLBuilder();
		sql.addClause(clauses);

		sql = selectCdr(userContext,sql);

		return sql;
	}

	/**
	 * Ricerca il CdR legato all'UO di scrivania
	 *
	 * @param userContext	lo UserContext che ha generato la richiesta
	 * @return CdR di tipo UO CDS legata alla UO di scrivania
	 */
	public CdrBulk findCdrUo(UserContext userContext) throws ComponentException {
		try
		{
			// uso createSQLBuilderEsteso perchè il metodo createSQLBuilder filtra alcune cose
			SQLBuilder sql = ((CdrHome)getHome(userContext, CdrBulk.class)).createSQLBuilderEsteso();
			sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addClause("AND", "cd_proprio_cdr", sql.EQUALS, "000");

			List result = getHome( userContext, CdrBulk.class ).fetchAll( sql );
			if ( result.size() > 1 )
				throw new ComponentException("Impossibile determinare il CdR legato alla UO in scrivania.");

			return (CdrBulk) result.get(0);	
		}
		catch( Exception e )
		{
			throw handleException( e );
		}
	}

	/**
	 * Utile a ricercare sia i CDR utilizzabili dall'utente che usa la mappa
	 * sia per la selezione dei record in ricerca dei residui già inseriti 
	 * 
	 * @param userContext
	 * @param sql
	 * @return
	 * @throws ComponentException
	 * @throws it.cnr.jada.persistency.PersistencyException
	 */
	private it.cnr.jada.persistency.sql.SQLBuilder selectCdr(UserContext userContext, SQLBuilder sql) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		UtenteBulk utente = (UtenteBulk)getHome(userContext,UtenteBulk.class).findByPrimaryKey(new UtenteKey(CNRUserContext.getUser(userContext)));
		if (utente == null || it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) == null)
			throw new ComponentException("L'utente non è stato assegnato a nessun CDR quindi non è possibile impostare questo livello di visibilità.");

		// cerchiamo il cdr per l'uo
		//CdrBulk cdrUtente = (CdrBulk)getHome(userContext,CdrBulk.class).findByPrimaryKey(new CdrBulk(utente.getCd_cdr()));
		CdrBulk cdrUoCds = findCdrUo(userContext);

		// cerchiamo l'uo legata al cdr utente per capirne il tipo
		Unita_organizzativaBulk aUO = (Unita_organizzativaBulk)getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdrUoCds.getCd_unita_organizzativa()));

		// se la UO in scrivania è di tipo Ente l'utente deve vedere tutti i record
		if (!isUOScrivaniaEnte(userContext)) {
			// se il cdr è di livello 1 oppure il cdr è di tipo SAC o AREA l'utente vede se quel cdr altrimenti vede il suo afferente
			if (cdrUoCds.getLivello().equals(new Integer("1"))
				|| aUO.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC) 
				|| aUO.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA)) {
				sql.addClause("AND", "cd_centro_responsabilita", sql.EQUALS, cdrUoCds.getCd_centro_responsabilita());
			}
			else {	
				sql.addClause("AND", "livello", sql.EQUALS, new Integer("1"));
				CdrBulk cdrAffer = null;
				try {
					cdrAffer = (CdrBulk)getHome(userContext, CdrBulk.class).find(new CdrBulk(cdrUoCds.getCd_centro_responsabilita())).get(0);
				} catch (it.cnr.jada.persistency.PersistencyException e) {
					handleException(e);
				}
				if ( cdrAffer.getCd_cdr_afferenza()!=null)
					sql.addClause("AND", "cd_centro_responsabilita", sql.EQUALS, cdrAffer.getCd_cdr_afferenza());
				else
					sql.addSQLClause("AND", "1=2");
			}
		}
		 	
		return sql;
	}
	
	/**
	 * verifica se il cdr è di tipo SAC,
	 * può essere utilizzato anche in altri contesti
	 * 
	 * @param userContext
	 * @return
	 * @throws ComponentException
	 */
	public boolean isCdrSAC(UserContext userContext, CdrBulk cdr) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		if (cdr==null)
			return false;
		// cerchiamo l'uo legata al cdr per capirne il tipo
		Unita_organizzativaBulk aUO = (Unita_organizzativaBulk)getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa()));

		if (aUO.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
			return true;
		else
			return false;		 	
	}

	/**
	 * Ricerca il CdR a cui deve afferire il residuo
	 * cioè il CdR di primo livello legato alla UO in scrivania
	 * oppure il CdR di secondo livello se la UO è della SAC
	 *
	 * @param userContext	lo UserContext che ha generato la richiesta
	 * @return Il CdR del residuo per l'utente operatore
	 */
	public CdrBulk findCdr(UserContext userContext) throws ComponentException {
		try
		{
			// uso createSQLBuilderEsteso perchè il metodo createSQLBuilder filtra alcune cose
			SQLBuilder sql = ((CdrHome)getHome(userContext, CdrBulk.class)).createSQLBuilderEsteso();
			sql = selectCdr(userContext,sql);

			List result = getHome( userContext, CdrBulk.class ).fetchAll( sql );
			if ( result.size() != 1 )
				return(null);
			return (CdrBulk) result.get(0);	
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectCentro_responsabilita_laByClause(UserContext userContext, Pdg_residuo_detBulk residuo, CdrBulk cdr, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, CdrBulk.class).createSQLBuilder();
		sql.addClause(clauses);

		CdrBulk cdr2 = findCdr(userContext);
		if (isCdrSAC(userContext, cdr2))
			sql.addClause("AND", "cd_centro_responsabilita", sql.EQUALS, cdr2.getCd_centro_responsabilita());
		else
			sql.addSQLClause("AND", "(cd_centro_responsabilita = '"+cdr2.getCd_centro_responsabilita()+"' or cd_centro_responsabilita in (select cd_centro_responsabilita from cdr where cd_cdr_afferenza = '"+cdr2.getCd_centro_responsabilita()+"'))");

		return sql;
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectLinea_attivitaByClause(UserContext userContext, Pdg_residuo_detBulk dettaglio, WorkpackageBulk la, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		sql.addClause(clauses);
		
		Parametri_cdsBulk param = (Parametri_cdsBulk)getHome(userContext,Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(CNRUserContext.getCd_cds(userContext),CNRUserContext.getEsercizio(userContext)));

		sql.addClause("AND", "ti_gestione", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
		//sql.addClause("AND", "esercizio", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
		sql.addSQLClause("AND", "esercizio = "+((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
		if (dettaglio.getCentro_responsabilita_la().getCd_centro_responsabilita()!=null)
			sql.addClause("AND", "cd_centro_responsabilita", sql.EQUALS, dettaglio.getCentro_responsabilita_la().getCd_centro_responsabilita());
		CdrBulk cdr = findCdr(userContext);
		if (isCdrSAC(userContext, cdr))
			sql.addClause("AND", "cd_centro_responsabilita", sql.EQUALS, cdr.getCd_centro_responsabilita());
		else
			sql.addSQLClause("AND", "(cd_centro_responsabilita = '"+cdr.getCd_centro_responsabilita()+"' or cd_centro_responsabilita in (select cd_centro_responsabilita from cdr where cd_cdr_afferenza = '"+cdr.getCd_centro_responsabilita()+"'))");
		if (param.getFl_commessa_obbligatoria().equals(new Boolean(true)))
			sql.addClause("AND", "pg_progetto", sql.ISNOTNULL, null);
		return sql;
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectElemento_voceByClause(UserContext userContext, Pdg_residuo_detBulk dettaglioSpesa, Elemento_voceBulk elementoVoce, CompoundFindClause clause) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_SPE").createSQLBuilder();
		if(clause != null) sql.addClause(clause);
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		if (dettaglioSpesa.getLinea_attivita() != null)
			sql.addSQLClause("AND","CD_FUNZIONE",sql.EQUALS,dettaglioSpesa.getLinea_attivita().getCd_funzione());
		//if ((dettaglioSpesa.getCategoria_economica_finanziaria() != null) && (dettaglioSpesa.getCategoria_economica_finanziaria().getCd_capoconto_fin() != null))
		//	sql.addSQLClause("AND","CD_CAPOCONTO_FIN",sql.EQUALS,dettaglioSpesa.getCategoria_economica_finanziaria().getCd_capoconto_fin());
		CdrBulk cdr = null;
		if (dettaglioSpesa.getCentro_responsabilita() != null)
			cdr = dettaglioSpesa.getCentro_responsabilita();
		else if (dettaglioSpesa.getPdg_residuo().getCentro_responsabilita() != null)
			cdr = dettaglioSpesa.getPdg_residuo().getCentro_responsabilita();
		if (cdr != null)
			sql.addSQLClause("AND","CD_TIPO_UNITA",sql.EQUALS,cdr.getUnita_padre().getCd_tipo_unita());

		if (clause != null) sql.addClause(clause);

		return sql;

	}
	/**
	 * verifica se l'uo in scrivania selezionata è di tipo ente,
	 * può essere utilizzato anche in altri contesti
	 * 
	 * @param userContext
	 * @return
	 * @throws ComponentException
	 */
	public boolean isUOScrivaniaEnte(UserContext userContext) throws ComponentException {
		try
		{
			CdrBulk cdrUoCds = findCdrUo(userContext);

			// Inizializzo il Cd_unita_organizzativa/cd_cds con l'Unita Organizzativa Ente e 
			List listaUoEnte = getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll();
			Unita_organizzativa_enteBulk uoEnte;
			Iterator it = listaUoEnte.iterator();
			while (it.hasNext()) {
				uoEnte = (Unita_organizzativa_enteBulk) it.next();
				if (cdrUoCds.getCd_unita_organizzativa().equals(uoEnte.getCd_unita_organizzativa()))
					return true;
			}
			return false;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	/**
	  *  calcolo disponibilità di cassa del Cdr per l'esercizio precedente al residuo
	  *    PreCondition:
	  *   	 Viene richiesta la visualizzazione della disponibilità di cassa del Cds per 
	  *		 l'esercizio precedente a quello di scrivania
	  *    PostCondition:
	  *      L'utente può visualizzare la disponibilità di cassa del Cds, effettuata dalla 
	  *		 stored procedure CNRCTB030.getMassaSpendibile 
	  *		 (esercizio di competenza = esercizio di scrivania)
	  *
	  * @param aUC lo user context 
	  * @param residuo l'istanza di  <code>Pdg_residuoBulk</code> che il Cds dovrebbe assumere
	  */
	public Pdg_residuoBulk calcolaDispCassaPerCds (UserContext userContext, Pdg_residuoBulk residuo) throws ComponentException
	{
		try
		{
			Integer esercizio = ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio();
			esercizio = new Integer(esercizio.intValue()-1);
			BigDecimal disp_cassa = getMassaSpendibile( userContext, esercizio, esercizio, CNRUserContext.getCd_cds(userContext), null );
			residuo.setIm_massa_spendibile(disp_cassa);
			return residuo;		
		}
		catch ( Exception e )
		{
			throw handleException( e );
		}	
	}


	/** 
	  *  Calcolo della disponibilità di cassa del Cds per l'esercizio di scrivania o per 
	  *	 i due esercizi successivi a quello di scrivania.
	  *    PreCondition:
	  *      E' stato richiesto di visualizzare la disponibilità di cassa per
	  *		 il residuo che ha esercizio corrente uguale a quello di scrivania
	  *		 (esercizio di competenza = esercizio) o per i suoi due esercizi successivi
	  *		 (esercizio di competenza = esercizio + n, con n=1,2). In quest'ultimo caso
	  *		 l'utente deve aver selezionato la voce del piano.
	  *    PostCondition:
	  *      Viene richiamata una stored procedure (getMassaSpendibile) che calcola 
	  *		 la disponibilità di cassa del Cds dell'obbligazione.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param esercizio_competenza <code>String</code> esercizio di competenza
	  * @param esercizio <code>String</code> esercizio di scrivania
	  * @param cd_cds <code>String</code> codice del centro di spesa
	  * @param cd_elemento_voce <code>String</code> codice dell'elemento voce
	  * @return disp_cassa_cds <code>BigDecimal</code> disponibilità di cassa del Cds
	  *
	*/
	private BigDecimal getMassaSpendibile ( UserContext userContext, Integer esercizio_competenza, Integer esercizio, String cd_cds, String cd_elemento_voce ) throws ComponentException
	{
		try
		{
			BigDecimal disp_cassa_cds;
		
			LoggableStatement cs =new LoggableStatement( getConnection( userContext ), 
				"{ ? = call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
				"CNRCTB030.getMassaSpendibileResidui(?, ?, ?, ?)}",false,this.getClass());
			try
			{
				cs.registerOutParameter( 1, java.sql.Types.NUMERIC );
				cs.setObject( 2, esercizio_competenza );
				cs.setObject( 3, esercizio );			
				cs.setString( 4, cd_cds );
				cs.setString( 5, cd_elemento_voce);
				cs.executeQuery();	
			
				disp_cassa_cds = cs.getBigDecimal( 1 );
				return disp_cassa_cds;
			}
			catch ( SQLException e )
			{
				throw handleException(e);
			}
			finally
			{
					cs.close();
			}
		}
		catch ( SQLException e )
		{
			throw handleException(e);
		}	
	}
	private MessaggioBulk generaMessaggio(UserContext userContext, UtenteBulk utente, Pdg_residuoBulk residuo, String tipo) throws ComponentException, PersistencyException{
		MessaggioHome messHome = (MessaggioHome)getHome(userContext,MessaggioBulk.class);
		MessaggioBulk messaggio = new MessaggioBulk();
		messaggio.setPg_messaggio(new Long(messHome.fetchNextSequenceValue(userContext,"CNRSEQ00_PG_MESSAGGIO").longValue()));
		messaggio.setCd_utente(utente.getCd_utente());
		messaggio.setPriorita(new Integer(1));
		if (tipo == null){
			messaggio.setDs_messaggio("È stato aperto in modifica la ricostruzione del residuo relativo al CdR "+residuo.getCd_centro_responsabilita());
		}
		messaggio.setCorpo("Residuo per il CdR "+residuo.getCd_centro_responsabilita());
		messaggio.setSoggetto(messaggio.getDs_messaggio());
		messaggio.setToBeCreated(); 
		return messaggio;	 		
	}
	private MessaggioBulk generaMessaggio(UserContext userContext, UtenteBulk utente, Pdg_residuoBulk residuo) throws ComponentException, PersistencyException{
		return generaMessaggio(userContext, utente, residuo, null);	 	
	}
	/**
	 * stampaConBulk method comment.
	 */
	public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof Stampa_ricostruzione_residui_LAVBulk)
			validateBulkForPrint(aUC, (Stampa_ricostruzione_residui_LAVBulk)bulk);

		return bulk;
	}
	/**
	 * Validazione dell'oggetto in fase di stampa
	 *
	*/
	private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_ricostruzione_residui_LAVBulk stampa) throws ComponentException{


		/*try{
			
		if (stampa.getCdrForPrint()==null)
				throw new ValidationException("Attenzione: il campo CDR è obbligatorio");
		}catch(ValidationException ex){
			throw new ApplicationException(ex);
		}*/	

	}
	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof Stampa_ricostruzione_residui_LAVBulk)
			inizializzaBulkPerStampa(userContext, (Stampa_ricostruzione_residui_LAVBulk)bulk);
		return bulk;
	}
	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	private void inizializzaBulkPerStampa(UserContext userContext, Stampa_ricostruzione_residui_LAVBulk stampa) throws ComponentException{

	    //stampa.setCdrForPrint(stampa.getCdrUtente());
		//stampa.setCdCdsForPrint(CNRUserContext.getCd_cds(userContext));
		try{
			String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		
			CdsHome cdsHome = (CdsHome)getHome(userContext, CdsBulk.class);
			CdsBulk cds = (CdsBulk)cdsHome.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		
			if (!cds.getCd_unita_organizzativa().equals(ente.getCd_unita_padre())){
				stampa.setCdsForPrint(cds);
				stampa.setCdsForPrintEnabled(false);
			} else {
				stampa.setCdsForPrint(new CdsBulk());
				stampa.setCdsForPrintEnabled(true);
			}
			
		} catch (it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException(pe);
		}	
		
		CdrBulk cdrUtente = cdrFromUserContext(userContext);
			Unita_organizzativaBulk uoPadre = null;
	
			try{
				uoPadre = (Unita_organizzativaBulk)getHome(userContext, cdrUtente.getUnita_padre()).findByPrimaryKey(cdrUtente.getUnita_padre());
			} catch (it.cnr.jada.persistency.PersistencyException pe){
				throw new it.cnr.jada.comp.ComponentException(pe);
			}

			stampa.setCdrUtente(cdrUtente);
			//stampa.setCdrForPrint(cdrUtente);
	
		//stampa.setPgInizioMand(new Long(0));
		//String cd_uo = CNRUserContext.getCd_unita_organizzativa(userContext);
		
		/*try{
			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));

			if (!uo.isUoCds()){
				stampa.setUoEmittenteForPrint(uo);
				stampa.setFindUOForPrintEnabled(false);
			} else {
				stampa.setUoEmittenteForPrint(new Unita_organizzativaBulk());
				stampa.setFindUOForPrintEnabled(true);
			}
			
		} catch (it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException(pe);
		}*/
		
		
	}

	protected CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {
		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );
			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);

		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	
	public SQLBuilder selectDipartimentoForPrintByClause (UserContext userContext, 
	Stampa_ricostruzione_residui_LAVBulk stampa, DipartimentoBulk dipartimento, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, dipartimento.getClass()).createSQLBuilder();
		java.sql.Timestamp lastDayOfYear = it.cnr.contab.doccont00.comp.DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext));
	 	sql.addClause("AND", "dt_istituzione", sql.LESS, lastDayOfYear);
		sql.openParenthesis("AND");
		sql.addClause("AND", "dt_soppressione", sql.GREATER_EQUALS, lastDayOfYear);
		sql.addClause("OR","dt_soppressione",sql.ISNULL,null);
		sql.closeParenthesis();
		sql.addClause( clause );
		return sql;
	}		
	public SQLBuilder selectProgettoForPrintByClause (UserContext userContext, 
	Stampa_ricostruzione_residui_LAVBulk stampa, ProgettoBulk progetto, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, progetto.getClass()).createSQLBuilder();
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectCdsForPrintByClause (UserContext userContext, 
	Stampa_ricostruzione_residui_LAVBulk stampa, CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
				
		SQLBuilder sql = getHome(userContext, cds.getClass()).createSQLBuilder();
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectModuloForPrintByClause (UserContext userContext, 
	Stampa_ricostruzione_residui_LAVBulk stampa, ProgettoBulk modulo, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, modulo.getClass()).createSQLBuilder();
		sql.addClause( clause );
		return sql;
	}
	/*
	public SQLBuilder selectCdrForPrintByClause (UserContext userContext, 
	Stampa_ricostruzione_residui_LAVBulk stampa, CdrBulk cdr, CompoundFindClause clause) throws ComponentException, PersistencyException
	{	
		SQLBuilder sql = getHome(userContext, cdr.getClass()).createSQLBuilder();
		sql.addClause( clause );
		return sql;
	}*/
	public SQLBuilder selectCdrForPrintByClause (UserContext userContext, 
	Stampa_ricostruzione_residui_LAVBulk stampa, CdrBulk cdr, CompoundFindClause clause) throws ComponentException, PersistencyException
	{
		SQLBuilder aSQL = (listaCdrPdGPerUtente (userContext,stampa));		
		
		if(clause != null)
		 aSQL.addClause(clause);
		return aSQL;
	}
	
	private SQLBuilder listaCdrPdGPerUtente (UserContext userContext, Stampa_ricostruzione_residui_LAVBulk stampa) throws it.cnr.jada.comp.ComponentException {

			CdrBulk cdr = cdrFromUserContext(userContext);
			int livelloResponsabilita = getLivelloResponsabilitaCDR(userContext, cdr);
			//lista dei CDR visibili all'utente
			Vector lista = new Vector();
/*			
			if (livelloResponsabilita == LV_NRUO) {
				lista.add(cdr);
				return lista;
			}
*/
			it.cnr.contab.config00.sto.bulk.CdrHome home;
			it.cnr.jada.persistency.sql.SQLBuilder sql;

			if (livelloResponsabilita == PdGComponent.LV_AC) {
				home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class,"V_CDR_VALIDO_CDS","none");
				sql = home.createSQLBuilder();
				sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, stampa.getCdCdsForPrint());
			} else {
//				lista.add(cdr);
				home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class, "V_PDG_CDR_FIGLI_PADRE","none");
				sql = home.createSQLBuilder();
				sql.addSQLClause("AND", "CD_CDR_ROOT", sql.EQUALS, cdr.getCd_centro_responsabilita());
				//sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
			}
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

			return sql;
//			lista.addAll(home.fetchAll(sql));
//			return lista;
	}
	protected int getLivelloResponsabilitaCDR(UserContext userContext, CdrBulk cdr) throws ComponentException {
		try {
			cdr = (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);

			// Se il livello del CDR è 1
			if(cdr.getLivello().intValue() == 1) {
				// Se il codice proprio del cdr è 0
				if (Integer.parseInt(cdr.getCd_proprio_cdr()) == 0) {
					Unita_organizzativaBulk uo = new Unita_organizzativaBulk( cdr.getCd_unita_organizzativa() );
					uo = (Unita_organizzativaBulk)getHome(userContext, uo).findByPrimaryKey(uo);

					Unita_organizzativaBulk cds = new Unita_organizzativaBulk( uo.getCd_unita_padre() );
					cds = (Unita_organizzativaBulk)getHome(userContext, cds).findByPrimaryKey(cds);

					// e ha come padre il cds dell'ente
					if (uo.getFl_uo_cds().booleanValue() == true &&
						cds.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
						// AC
						return PdGComponent.LV_AC;
				}
				return PdGComponent.LV_CDRI;
			} else if( (cdr.getLivello().intValue() == 2 && Integer.parseInt(cdr.getCd_proprio_cdr()) == 0)) {
				// Sel cdr.livello == 2 e codice proprio = 0
				return PdGComponent.LV_RUO;
			} else {
				// Ogni altro livello o combinazione è livello 3
				return PdGComponent.LV_NRUO;
			}
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
}
