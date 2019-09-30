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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.Vsx_liquidazione_rateBulk;
import it.cnr.contab.compensi00.docs.bulk.Vsx_liquidazione_rateHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * Insert the type's description here.
 * Creation date: (24/12/2002 12.30.11)
 * @author: Roberto Fantino
 */
public class LiquidazioneRateMinicarrieraComponent extends it.cnr.jada.comp.CRUDComponent implements ILiquidazioneRateMinicarrieraMgr {
/**
 * LiquidazioneRateMinicarrieraComponent constructor comment.
 */
public LiquidazioneRateMinicarrieraComponent() {
	super();
}
private void addJoinConditions(SQLBuilder sql){

	sql.addTableToHeader("MINICARRIERA");
	sql.addSQLJoin("MINICARRIERA.CD_CDS", "MINICARRIERA_RATA.CD_CDS");
	sql.addSQLJoin("MINICARRIERA.CD_UNITA_ORGANIZZATIVA", "MINICARRIERA_RATA.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLJoin("MINICARRIERA.ESERCIZIO", "MINICARRIERA_RATA.ESERCIZIO");
	sql.addSQLJoin("MINICARRIERA.PG_MINICARRIERA", "MINICARRIERA_RATA.PG_MINICARRIERA");
}
private void addJoinConditions(SQLBuilder sql, Boolean toBeAdded){

	if (toBeAdded.booleanValue()){
		sql.addTableToHeader("MINICARRIERA");
		sql.addSQLJoin("MINICARRIERA.CD_CDS", "MINICARRIERA_RATA.CD_CDS");
		sql.addSQLJoin("MINICARRIERA.CD_UNITA_ORGANIZZATIVA", "MINICARRIERA_RATA.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("MINICARRIERA.ESERCIZIO", "MINICARRIERA_RATA.ESERCIZIO");
		sql.addSQLJoin("MINICARRIERA.PG_MINICARRIERA", "MINICARRIERA_RATA.PG_MINICARRIERA");
		toBeAdded = Boolean.FALSE;
	}
}
/**
 * Richiede il numeratore di protocollo vsx.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @return il N. liquidazione <code>Long</code> richiesto.
 * 
**/
private Long callGetPgPerLiquidazioneRateMinicarriera(UserContext userContext) throws ComponentException {

	LoggableStatement cs = null;
	Long pg = null;
	try {
		try	{
			cs = new LoggableStatement(getConnection(userContext),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
					+ "IBMUTL020.vsx_get_pg_call() }",false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.NUMERIC);
			cs.executeQuery();
			pg = new Long(cs.getLong(1));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs!=null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}

	if (pg == null)
		throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere un progressivo valido per la vista di Liquidazione Rate Minicarriera!");
	return pg;		
}
/**
 * Ricerca il capitolo (VoceF) dato l'elemento voce e la linea di attivita
 *
 * Pre-post-conditions
 *
 * Nome: Esiste un solo capitolo
 * Pre: Viene richiesto il capitolo associato all'elemento voce e alla linea di attivita selezionate
 * Post: Viene restituito il capitolo corrispondente
 *
 * Nome: Esiste + di un capitolo
 * Pre: Esistono + capitoli associati all'elemento voce e alla linea di attivita selezionate
 * Post: Viene restituito una eccezione con la descrizione dell'errore
 *
 * Nome: Non esistono capitoli
 * Pre: Non esistono capitoli associati all'elemento voce e alla linea di attivita selezionate
 * Post: Viene restituito una eccezione con la descrizione dell'errore
 *
 * @param	userContext lo userContext che ha generato la richiesta
 * @param	bulk il filtro che contiene l'elemento voce e la linea di attivita selezionate dall'utente
 * @return	il capitolo (Voce_fBulk) associato
*/
public Voce_fBulk findVoceF(UserContext userContext, Liquidazione_rate_minicarrieraBulk bulk) throws ComponentException{

	try{
		Voce_fHome voceHome = (Voce_fHome)getHome(userContext, Voce_fBulk.class);
		SQLBuilder sql = (SQLBuilder)voceHome.createSQLBuilder();

		sql.addClause("AND", "esercizio", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addClause("AND", "cd_cds", sql.EQUALS, bulk.getUoScrivania().getCd_unita_padre());
		sql.addClause("AND", "ti_appartenenza", sql.EQUALS, bulk.getElementoVoce().getTi_appartenenza());
		sql.addClause("AND", "ti_gestione", sql.EQUALS, bulk.getElementoVoce().getTi_gestione());
		sql.addClause("AND", "fl_mastrino", sql.EQUALS, Boolean.TRUE);
		sql.addClause("AND", "cd_titolo_capitolo", sql.EQUALS, bulk.getElementoVoce().getCd_elemento_voce());
		sql.addClause("AND", "cd_parte", sql.EQUALS, bulk.getElementoVoce().getCd_parte());
		sql.addClause("AND", "cd_funzione", sql.EQUALS, bulk.getLineaAttivita().getCd_funzione());

		if(Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(bulk.getUoScrivania().getCd_tipo_unita())){
			sql.addClause("AND", "cd_proprio_voce", sql.EQUALS, bulk.getUoScrivania().getCd_proprio_unita());
			sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, recuperaCdUOPerVoceF(userContext, bulk.getUoScrivania()));
		}else
			sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, bulk.getUoScrivania().getCd_unita_organizzativa());
		
		java.util.List l = voceHome.fetchAll(sql);
		if (l.isEmpty() || l.size()>1)
			throw new ApplicationException("Impossibile recuperare il capitolo per l'elemento voce e i GAE selezionati");

		return (Voce_fBulk)l.get(0);
	}catch(PersistencyException ex){
		throw handleException(ex);
	}
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Verifica stato esercizio
 * Pre:  Esercizio aperto
 * Post: Il sistema prosegue con l'inizializzazione della liquidazione 
 *		 massima delle minicarriere
 *
 * Nome: Verifica stato esercizio
 * Pre:  Esercizio chiuso
 * Post: Viene generata una ApplicationException "Impossibile proseguire".
 *
 * @param	uc		lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da inizializzare per l'inserimento
 * @return	l'OggettoBulk inizializzato
 */
 
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException 
{
	Liquidazione_rate_minicarrieraBulk rata = (Liquidazione_rate_minicarrieraBulk)super.inizializzaBulkPerInserimento(userContext, bulk);
	
	if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk( rata.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
         throw new it.cnr.jada.comp.ApplicationException("Esercizio chiuso! Impossibile proseguire con la liquidazione massiva delle rate.");
         
	return rata;
}
/**
 * Viene richiesta l'esecuzione della procedura Oracle CNRCTBxxx.xxx
 *
 * Pre-post-conditions:
 *
 * Nome: Liquidazione rate
 * Pre: Viene richiesta la liquidazione massiva delle rate selezionate
 * Post: Viene eseguita la procedura per la liquidazione massiva delle rate.
 *			Per ogni rata selezionata viene generato un compenso e viene automaticamente
 *			contabilizzato, generando mandati e reversali collegati ad esso
 *
 * @param userContext		lo userContext che ha generato la richiesta
 * @param bulk				istanza di Liquidazione_rateBulk che deve essere utilizzata
 * @param rateDaLiquidare	lista delle rate che devono essere liquidate
 *
*/
public void liquidaRate(UserContext userContext, Liquidazione_rate_minicarrieraBulk bulk, java.util.List rateDaLiquidare) throws ComponentException{

	try{
		Vsx_liquidazione_rateHome home = (Vsx_liquidazione_rateHome)getHome(userContext, Vsx_liquidazione_rateBulk.class);
		
		int count = 0;
		Long pg_call = callGetPgPerLiquidazioneRateMinicarriera(userContext);
		for (java.util.Iterator i = rateDaLiquidare.iterator();i.hasNext();){
			Liquidazione_rate_minicarrieraBulk rata = (Liquidazione_rate_minicarrieraBulk)i.next();
			rata.setElementoVoce(bulk.getElementoVoce());
			rata.setLineaAttivita(bulk.getLineaAttivita());
			rata.setVoceF(bulk.getVoceF());

			Vsx_liquidazione_rateBulk vsx_liqid = new Vsx_liquidazione_rateBulk();
			vsx_liqid.setPg_call(pg_call);
			vsx_liqid.setPar_num(new Integer(count++));
			vsx_liqid.setEsercizio_competenza(CNRUserContext.getEsercizio(userContext));
			vsx_liqid.completeFrom(rata);
			vsx_liqid.setToBeCreated();
			insertBulk(userContext, vsx_liqid);
		}
		
		liquidazMassivaMinicarriere(userContext, pg_call);

	}catch(PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  *	Richiama la procedura Oracle CNRCTB610.LIQUIDAZMASSIVAMINICARRIERE
  *	per la liquidazione massiva delle rate selezionate
  *
**/
private void liquidazMassivaMinicarriere(UserContext userContext, Long pg_call) throws ComponentException{

	try{
		LoggableStatement cs = new LoggableStatement(getConnection(userContext),"{call "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
				+"CNRCTB610.LIQUIDAZMASSIVAMINICARRIERE(?,?,?,?,?)}",false,this.getClass());
		try{
			cs.setObject( 1, pg_call);
			cs.setObject( 2, CNRUserContext.getCd_unita_organizzativa(userContext));
			cs.setObject( 3, CNRUserContext.getCd_cds(userContext));
			cs.setObject( 4, CNRUserContext.getEsercizio(userContext));
			cs.setObject( 5, CNRUserContext.getUser(userContext));
			cs.execute();

		}finally{
			cs.close();
		}
	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}
}
/**
 * Recupera il codice dell'Unita Organizzativa da utilizzare nella query di ricerca 
 * della VoceF nel caso in cui l'Unita Organizzativa di Scrivania sia di TIPO SAC.
 *
*/
private String recuperaCdUOPerVoceF(UserContext userContext, Unita_organizzativaBulk uo) throws ComponentException{

	try{
		CdrHome home = (CdrHome)getHome(userContext, CdrBulk.class);
		SQLBuilder sql = (SQLBuilder)home.createSQLBuilder();

		sql.addTableToHeader("CDR", "B");
		sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA","B.CD_CDR_AFFERENZA");
		sql.addSQLClause("AND", "B.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, uo.getCd_unita_organizzativa());

		java.util.List l = home.fetchAll(sql);
		if (l.isEmpty())
			return uo.getCd_unita_organizzativa();

		if (l.size()>1)
			throw new ApplicationException("Impossibile recuperare il capitolo per l'elemento voce e il GAE selezionato");

		CdrBulk unitaOrg = (CdrBulk)l.get(0);
		return unitaOrg.getCd_unita_organizzativa();

	}catch(PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  * Aggiunge le clausole di ricerca impostate nella finestra
  *	
  * Pre-post-conditions:
  *
  * Nome: Richiesta di ricerca delle rate da liquidare di minicarriere attive
  * Pre:  E' stata generata la richiesta di ricerca delle rate da liquidare
  * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e,
  *		  in aggiunta, le clausole che le rate abbiano ESERCIZIO di creazione minore-uguale a quello di scrivania,
  *       CDS di scrivania, Unita Organizzativa di scrivania
  * 
  * @param userContext lo userContext che ha generato la richiesta
  * @param clauses clausole di ricerca gia' specificate dall'utente
  * @param bulk istanza di CompensoBulk che deve essere utilizzata per la ricerca
  * @return il SQLBuilder con le clausole aggiuntive
  *
**/
public Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, PersistencyException {

	Liquidazione_rate_minicarrieraBulk rata = (Liquidazione_rate_minicarrieraBulk)bulk;
	SQLBuilder sql = (SQLBuilder)super.select(userContext, clauses, bulk);
	addJoinConditions(sql);

	sql.addSQLClause("AND", "MINICARRIERA_RATA.CD_CDS", sql.EQUALS, CNRUserContext.getCd_cds(userContext));
	sql.addSQLClause("AND", "MINICARRIERA_RATA.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
	sql.addSQLClause("AND", "MINICARRIERA_RATA.ESERCIZIO", sql.LESS_EQUALS, rata.getEsercizioScrivania().getEsercizio());
	sql.addSQLClause("AND", "MINICARRIERA_RATA.STATO_ASS_COMPENSO", sql.EQUALS, rata.STATO_NON_ASS_COMPENSO);
	sql.addSQLClause("AND", "MINICARRIERA.STATO", sql.EQUALS, MinicarrieraBulk.STATO_ATTIVA);

	sql.addSQLClause("AND", "MINICARRIERA_RATA.PG_MINICARRIERA", sql.GREATER_EQUALS, rata.getNrMinicarrieraDa());
	sql.addSQLClause("AND", "MINICARRIERA_RATA.PG_MINICARRIERA", sql.LESS_EQUALS, rata.getNrMinicarrieraA());

	sql.addSQLClause("AND", "MINICARRIERA_RATA.DT_SCADENZA", sql.GREATER_EQUALS, rata.getDtScadenzaDa());
	sql.addSQLClause("AND", "MINICARRIERA_RATA.DT_SCADENZA", sql.LESS_EQUALS, rata.getDtScadenzaA());

	sql.addSQLClause("AND", "MINICARRIERA.DT_REGISTRAZIONE", sql.GREATER_EQUALS, rata.getDtRegistrazioneDa());
	sql.addSQLClause("AND", "MINICARRIERA.DT_REGISTRAZIONE", sql.LESS_EQUALS, rata.getDtRegistrazioneA());

	sql.addSQLClause("AND", "MINICARRIERA.DT_INIZIO_MINICARRIERA", sql.GREATER_EQUALS, rata.getDtInizioMinicarrieraDa());
	sql.addSQLClause("AND", "MINICARRIERA.DT_INIZIO_MINICARRIERA", sql.LESS_EQUALS, rata.getDtInizioMinicarrieraA());

	sql.addSQLClause("AND", "MINICARRIERA.CD_TERZO", sql.EQUALS, rata.getCdTerzo());

	

	return sql;
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Linee di Attivita
  *
  * Nome: Richiesta di ricerca di una Linea di Attivita
  * Pre: E' stata generata la richiesta di ricerca della Linea di Attivita
  * Post: Viene restituito l'SQLBuilder per filtrare le Linee di Attivita
  *
  * @param userContext		lo userContext che ha generato la richiesta
  * @param compenso			l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param lineaAttivita	l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *							costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param					clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectElementoVoceByClause(UserContext userContext, Liquidazione_rate_minicarrieraBulk bulk, Elemento_voceBulk elementoVoce, CompoundFindClause clauses) throws ComponentException {

	Elemento_voceHome home = (Elemento_voceHome)getHome(userContext, elementoVoce);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND","esercizio",sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND","fl_partita_giro", sql.EQUALS, Boolean.FALSE);
	sql.addClause("AND","ti_elemento_voce", sql.EQUALS, home.TIPO_CAPITOLO);
	sql.addClause("AND","ti_appartenenza", sql.EQUALS, home.APPARTENENZA_CDS);
	sql.addClause("AND","ti_gestione", sql.EQUALS, home.GESTIONE_SPESE);
	sql.addClause("AND","cd_parte", sql.EQUALS, home.PARTE_1);
	if (!Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(bulk.getUoScrivania().getCd_tipo_unita()))
		sql.addClause("AND", "fl_voce_sac", SQLBuilder.EQUALS, Boolean.FALSE);

	sql.addClause(clauses);
	return sql;
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulle Linee di Attivita
  *
  * Nome: Richiesta di ricerca di una Linea di Attivita
  * Pre: E' stata generata la richiesta di ricerca della Linea di Attivita
  * Post: Viene restituito l'SQLBuilder per filtrare le Linee di Attivita
  *
  * @param userContext		lo userContext che ha generato la richiesta
  * @param compenso			l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param lineaAttivita	l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *							costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param					clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectLineaAttivitaByClause(UserContext userContext, Liquidazione_rate_minicarrieraBulk bulk, WorkpackageBulk lineaAttivita, CompoundFindClause clauses) throws ComponentException {

	if (bulk.getCdElementoVoce()==null)
		throw new ApplicationException("E' necessario inserire l'elemento voce");

	WorkpackageHome home = (WorkpackageHome)getHome(userContext, lineaAttivita,"V_LINEA_ATTIVITA_VALIDA");
	SQLBuilder sql = home.createSQLBuilder();

	sql.addTableToHeader("ASS_EV_FUNZ_TIPOCDS");
	sql.addSQLJoin("ASS_EV_FUNZ_TIPOCDS.CD_FUNZIONE", "V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE");
	sql.addSQLClause("AND", "ASS_EV_FUNZ_TIPOCDS.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));

	sql.addSQLClause("AND", "ASS_EV_FUNZ_TIPOCDS.CD_TIPO_UNITA", sql.EQUALS, bulk.getUoScrivania().getCd_tipo_unita());
	sql.addSQLClause("AND", "ASS_EV_FUNZ_TIPOCDS.CD_CONTO", sql.EQUALS, bulk.getCdElementoVoce());
	sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", sql.EQUALS,CNRUserContext.getEsercizio(userContext));

	sql.openParenthesis(FindClause.AND);
	sql.addSQLClause("OR", "V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE", SQLBuilder.EQUALS, WorkpackageBulk.TI_GESTIONE_SPESE);
	sql.addSQLClause("OR", "V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE", SQLBuilder.EQUALS, WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
	sql.closeParenthesis();
	
	sql.addSQLClause("AND", "V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", sql.LIKE, bulk.getUoScrivania().getCd_unita_organizzativa() + "%");

	sql.addClause(clauses);
	return sql;
}
public boolean verificaStatoEsercizio(UserContext userContext,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio) throws ComponentException 
{
	try 
	{
		it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome)getHome(userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class);
		
		return !eHome.isEsercizioChiuso(userContext, anEsercizio.getEsercizio(), anEsercizio.getCd_cds());
	} 
	catch (it.cnr.jada.persistency.PersistencyException e) 
	{
		throw handleException(e);
	}
}
}
