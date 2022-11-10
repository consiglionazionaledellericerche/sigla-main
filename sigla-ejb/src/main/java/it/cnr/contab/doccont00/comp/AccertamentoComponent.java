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

package it.cnr.contab.doccont00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaHome;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.prevent00.bulk.Pdg_vincoloBulk;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.*;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.ejb.EJBException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* Gestisce documenti di tipo
	ACR con fl_pgiro = 'N' - bilancio Ente
	ACR_RES con fl_pgiro = 'N'- bilancio Ente
	ACR_SIST - bilancio Cds
	ACR_PLUR - bilancio Ente
*/

public class AccertamentoComponent  extends CRUDComponent implements IDocumentoContabileMgr,ICRUDMgr,IAccertamentoMgr, IPrintMgr, Cloneable,Serializable
{
	private final static int INSERIMENTO = 1;
	private final static int MODIFICA    = 2;



    public  AccertamentoComponent()
    {

        /*Default constructor*/


    }
/**
/**
  *  creazione
  *    PreCondition:
  *      Un accertamento e' stato creato
  *      L'esercizio dell'accertamento e' uguale all'esercizio di scrivania
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInInserimento' che, per la voce del piano utilizzata nell'accertamento calcola
  *      l'importo dell'aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *  modifica/eliminazione accertamento
  *    PreCondition:
  *      Un accertamento e' stato modificato/eliminato
  *      L'esercizio dell'accertamento e' uguale all'esercizio di scrivania
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInModifica' che, per la voce del piano utilizzata nell'accertamento calcola
  *      l'importo dell'aggiornamento da apportare ai saldi e richiama il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param accertamento <code>AccertamentoBulk</code> l'accertamento da salvare
  * @param azione indica l'azione effettuata sull'accertamento e puo' assumere i valori INSERIMENTO, MODIFICA
  *
 */
private void aggiornaCapitoloSaldoAccertamento (UserContext userContext,AccertamentoBulk accertamento, int azione) throws ComponentException
{
	try
	{
		// non si aggiornano i saldi di accertamenti con esercizio di competenza diverso da esercizio di creazione
		if ( accertamento.getEsercizio().compareTo( accertamento.getEsercizio_competenza()) != 0 )
			return;
		if ( azione == INSERIMENTO )
			aggiornaSaldiInInserimento( userContext, accertamento );
		else if ( azione == MODIFICA )
			aggiornaSaldiInModifica( userContext,
											 accertamento,
//											 new Long( accertamento.getPg_ver_rec().longValue() - 1),
											 accertamento.getPg_ver_rec());

/*
		SaldoComponentSession session = createSaldoComponentSession();
		AccertamentoBulk accDaDB;
		Voce_fBulk voce = new Voce_fBulk( accertamento.getCd_voce(), accertamento.getEsercizio(), accertamento.getTi_appartenenza(), accertamento.getTi_gestione() );
		switch( accertamento.getCrudStatus() )
		{
			case accertamento.TO_BE_CREATED:
				session.aggiornaObbligazioniAccertamenti( aUC, voce, accertamento.getCd_cds(), accertamento.getIm_accertamento());
				break;
			case accertamento.TO_BE_DELETED:
				//rileggo il valore precedente e lo aggiorno
				accDaDB = (AccertamentoBulk) getHome(aUC, AccertamentoBulk.class).findByPrimaryKey( accertamento);
				session.aggiornaObbligazioniAccertamenti( aUC, voce, accDaDB.getCd_cds(), accDaDB.getIm_accertamento().negate());
				break;
			case accertamento.TO_BE_UPDATED:
				//rileggo il valore precedente e lo aggiorno
				accDaDB = (AccertamentoBulk) getHome(aUC, AccertamentoBulk.class).findByPrimaryKey( accertamento);
				if ( accDaDB.getIm_accertamento().compareTo( accertamento.getIm_accertamento()) != 0 )
					session.aggiornaObbligazioniAccertamenti( aUC, voce, accDaDB.getCd_cds(), accertamento.getIm_accertamento().subtract( accDaDB.getIm_accertamento()));
				break;

		}
				*/
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}

}
/**
 * aggiornaCogeCoanInDifferita method comment.
 */
public void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext userContext, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docContabile, java.util.Map values) throws it.cnr.jada.comp.ComponentException {

	try
	{
		if ( docContabile instanceof AccertamentoBulk )
		{
			AccertamentoBulk accertamento = (AccertamentoBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dello stato coge/coan dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( accertamento.getPg_accertamento().longValue() >= 0 ) //accertamento non temporaneo
				callDoRiprocAcc( userContext, accertamento, pg_ver_rec );
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}
}
/**
 * Aggiornamento in differita dei saldi dell'accertamento
 * Un documento amministrativo di entrata che agisce in modalità transazionale ha creato/modificato gli importi
 * relativi ad un accertamento; i saldi di tale accertamento non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbero l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'accertamento viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per accertamento creato
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       bilancio che e' stata creato nel contesto transazionale del documento ammninistrativo ( progressivo
 *       accertamento < 0)
 * Post: I saldi dell'accertamento sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per accertamento esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per un accertamento su capitoli di
 *       bilancio che non e' stata creato nel contesto transazionale del documento ammninistrativo ( progressivo
 *       accertamento > 0)
 * Post: I saldi dell'accertamento sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	il documento contabile di tipo AccertamentoBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'accertamento
 * @param	param paramtero non utilizzato per gli accertamenti
 *
*/

public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param ) throws ComponentException
{
	try
	{
		if ( docContabile instanceof AccertamentoBulk )
		{
			AccertamentoBulk accertamento = (AccertamentoBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dei saldi dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( accertamento.getPg_accertamento().longValue() < 0 ) //accertamento appena inserita
				aggiornaSaldiInInserimento( userContext, accertamento );
			else
				aggiornaSaldiInModifica( userContext, accertamento, pg_ver_rec );
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}

}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi
 * Pre:  Un accertamento e' stato creato
 * Post: Per la Voce del piano presente nell'accertamento viene richiamato il metodo sulla Component di gestione dei Saldi (SaldoComponent) per incrementare
 *       il saldo del capitolo corrispondente
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	accertamento	l'AccertamentoBulk per cui aggiornare i saldi
 */

private void aggiornaSaldiInInserimento( UserContext userContext, AccertamentoBulk accertamento) throws ComponentException, java.rmi.RemoteException, it.cnr.jada.persistency.PersistencyException
{
	SaldoComponentSession session = createSaldoComponentSession();
	Voce_fBulk voce = (Voce_fBulk) accertamento.getCapitolo();
	BigDecimal im_voce = accertamento.getIm_accertamento();
	session.aggiornaObbligazioniAccertamenti( userContext, voce, accertamento.getCd_cds(), im_voce, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.TIPO_COMPETENZA);
	/*
	 * Aggiorno i Saldi per CDR/Linea
	 */
	Accertamento_scad_voceBulk osv;
	Accertamento_scadenzarioBulk os;
	for ( Iterator j = accertamento.getAccertamento_scadenzarioColl().iterator(); j.hasNext(); )
	{
	  os = (Accertamento_scadenzarioBulk) j.next();
	  for ( int index = os.getAccertamento_scad_voceColl().size() - 1; index >= 0 ; index--)
	  {
		 osv = (Accertamento_scad_voceBulk) os.getAccertamento_scad_voceColl().get( index );
		 session.aggiornaObbligazioniAccertamenti( userContext, osv.getCd_centro_responsabilita(), osv.getCd_linea_attivita(), voce, accertamento.getEsercizio_originale(),accertamento.isAccertamentoResiduo()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA,osv.getIm_voce(),accertamento.getCd_tipo_documento_cont());
	  }
	}
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi
 * Pre:  Un accertamento e' stato modificato/eliminato
 * Post: Per ogni V_mod_saldi_accertBulk presente nel database a fronte dell'accertamento e del suo pg_ver_rec
 *       e' stato richiamato il metodo sulla Component di gestione dei Saldi (SaldoCompoennt) per aggiornare
 *       il saldo del capitolo corrispondente; se necessario
 *       anche i saldi relativi alle reversali e all'incassato vengono aggiornati.
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	accertamento	l'AccertamentoBulk per cui aggiornare i saldi
 * @param	pg_ver_rec	il pg_ver_rec iniziale dell'accertamento
 *
 */
private void aggiornaSaldiInModifica( UserContext userContext, AccertamentoBulk accertamento, Long pg_ver_rec  ) throws it.cnr.jada.persistency.PersistencyException, ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();

	String ti_competenza_residuo;
	if ( accertamento.isResiduo() )
		ti_competenza_residuo = ReversaleBulk.TIPO_RESIDUO;
	else
		ti_competenza_residuo = ReversaleBulk.TIPO_COMPETENZA;

	List saldiDaAggiornare = ((V_mod_saldi_accertHome)getHome( userContext, V_mod_saldi_accertBulk.class )).findModificheSaldiFor( accertamento, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornare.size() == 0 )
		throw new ApplicationException( "Attenzione! Il saldo relativo all'accertamento " + accertamento.getEsercizio_originale() + "/" + accertamento.getPg_accertamento() + " non può essere aggiornato perchè l'accertamento non e' presente nello storico.");
	/*
	* Aggiorno i Saldi per Voce_f_saldi_cmpBulk
	* e' sempre uno solo
	*/
	for ( Iterator i = saldiDaAggiornare.iterator(); i.hasNext(); )
	{
		V_mod_saldi_accertBulk modSaldo = (V_mod_saldi_accertBulk) i.next();
		if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
		{

			Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), accertamento.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
			session.aggiornaObbligazioniAccertamenti( userContext, voce, accertamento.getCd_cds(), modSaldo.getIm_delta_voce(), ti_competenza_residuo);

			if ( modSaldo.getIm_delta_rev_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaMandatiReversali( userContext, voce, accertamento.getCd_cds(), modSaldo.getIm_delta_rev_voce(), ti_competenza_residuo);

			if ( modSaldo.getIm_delta_inc_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaPagamentiIncassi( userContext, voce, accertamento.getCd_cds(), modSaldo.getIm_delta_inc_voce(), ti_competenza_residuo);
		}

	}
	/*
	* Aggiorno i Saldi per CDR/Linea
	*/
	List saldiDaAggiornareCdrLinea = ((V_mod_saldi_accert_scad_voceHome)getHome( userContext, V_mod_saldi_accert_scad_voceBulk.class )).findModificheSaldiFor( accertamento, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornareCdrLinea.size() == 0 )
		throw new ApplicationException( "Attenzione! Il saldo relativo all'accertamento " + accertamento.getEsercizio_originale() + "/" + accertamento.getPg_accertamento() + " non può essere aggiornato perchè l'accertamento non e' presente nello storico.");
	for ( Iterator i = saldiDaAggiornareCdrLinea.iterator(); i.hasNext(); )
	{
		V_mod_saldi_accert_scad_voceBulk modSaldo = (V_mod_saldi_accert_scad_voceBulk) i.next();

		boolean isNuovoPdg = ((Parametri_cnrHome)getHome(userContext,Parametri_cnrBulk.class)).isNuovoPdg(userContext);
		IVoceBilancioBulk voce = null;
        if (!isNuovoPdg)
        	voce = new Voce_fBulk( modSaldo.getCd_voce(), accertamento.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
        else
        	voce = new Elemento_voceBulk( modSaldo.getCd_voce(), accertamento.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );

        if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 ) {
			// MITODO - verificare se è necessario non effettuare l'aggiornamento in questo caso
			//if (!accertamento.isAccertamentoResiduo())
			session.aggiornaObbligazioniAccertamenti( userContext, modSaldo.getCd_centro_responsabilita(), modSaldo.getCd_linea_attivita(), voce, modSaldo.getEsercizio_originale(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_ACR_RES)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA,modSaldo.getIm_delta_voce(),modSaldo.getCd_tipo_documento_cont());
		}

		if ( modSaldo.getIm_delta_rev_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaMandatiReversali( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_rev_voce(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_ACR_RES)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA);

		if ( modSaldo.getIm_delta_inc_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaPagamentiIncassi( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(), modSaldo.getIm_delta_inc_voce());
	}
	// aggiorniamo i saldi legati alle modifiche agli acc. residui
	// qui non va messo, andrebbe in loop infinito
	//aggiornaSaldiAccertamentiResiduiPropri(userContext,accertamento);
	if (accertamento.isAccertamentoResiduo()) {
		// aggiorniamo il progressivo in definitivo
		Accertamento_modificaBulk obbMod = ((AccertamentoResiduoBulk) accertamento).getAccertamento_modifica();
		if (obbMod!=null && obbMod.isTemporaneo()) {
			aggiornaAccertamentoModificaTemporanea(userContext, obbMod);
			if (obbMod.getVariazione()!=null)
				approvaVarStanzRes(userContext, obbMod.getVariazione());
		}
	}
	annullaRigheDocAmm(userContext,accertamento);

}
private void annullaRigheDocAmm(UserContext userContext, AccertamentoBulk accertamento) throws ComponentException {
	if (accertamento.isAccertamentoResiduo()) {
		AccertamentoResiduoBulk obbRes = (AccertamentoResiduoBulk) accertamento;
		if (obbRes.getPg_accertamento()!=null) {
			try
			{
				for ( Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
				{
					Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) i.next();
					Accertamento_scadenzarioHome scadenzaHome = (Accertamento_scadenzarioHome) getHome( userContext, scadenza.getClass());

					if (scadenza.getPg_doc_attivo()!=null &&
						scadenza.getPg_reversale()==null &&
						scadenza.getIm_scadenza().compareTo(Utility.ZERO)==0) {

						List listaDocAttivi = null;
						boolean bFound = false;
						if (scadenza.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_E)) {
							listaDocAttivi = scadenzaHome.findDocAmm(scadenza, Documento_generico_rigaBulk.class);
							for(Iterator it=listaDocAttivi.iterator();it.hasNext();)
							{
								bFound = true;
								Documento_generico_rigaBulk docAttivo = (Documento_generico_rigaBulk) it.next();
								// ricarico rima del lock dato che se è stato updatato prima mi darebbe "Risorsa non più valida"
								docAttivo = (Documento_generico_rigaBulk) getHome(userContext, Documento_generico_rigaBulk.class).findByPrimaryKey(docAttivo);
								if (!docAttivo.getStato_cofi().equals(Documento_generico_rigaBulk.STATO_ANNULLATO)) {
									lockBulk(userContext,docAttivo);
									docAttivo.setStato_cofi(Documento_generico_rigaBulk.STATO_ANNULLATO);

									//	Se l'esercizio di scrivania e' diverso da quello solare
							        //	inizializzo la data di cancellazione al 31/12/esercizio
							        java.sql.Timestamp tsOdierno = ((Documento_generico_rigaHome) getHome(userContext, docAttivo)).getServerDate();
							       	GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
							       	tsOdiernoGregorian.setTime(tsOdierno);
							        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

									if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) != CNRUserContext.getEsercizio(userContext).intValue())
										docAttivo.setDt_cancellazione(new java.sql.Timestamp(sdf.parse("31/12/"+docAttivo.getEsercizio().intValue()).getTime()));
									else
									{
										String currentDate = Integer.toString(tsOdiernoGregorian.get(GregorianCalendar.DAY_OF_MONTH)) + "/" +
															Integer.toString(tsOdiernoGregorian.get(GregorianCalendar.MONTH)) + "/" +
															Integer.toString(tsOdiernoGregorian.get(GregorianCalendar.YEAR));
										docAttivo.setDt_cancellazione(new java.sql.Timestamp(sdf.parse(currentDate).getTime()));
									}

									Documento_genericoBulk docAttivoTestata = docAttivo.getDocumento_generico();
									docAttivoTestata = (Documento_genericoBulk) getHome(userContext, Documento_genericoBulk.class).findByPrimaryKey(docAttivoTestata);
									lockBulk(userContext,docAttivoTestata);
									docAttivoTestata.setIm_totale(docAttivoTestata.getIm_totale().subtract(docAttivo.getIm_riga()));
									updateBulk(userContext,docAttivo);
									updateBulk(userContext,docAttivoTestata);
								}
							}
							if (!bFound)
								throw new ApplicationException( "Attenzione! Non è stato trovato il documento attivo a cui è collegato la scadenza da azzerare!");
						}
/*
						if (scadenza.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA)) {
							listaDocAttivi = scadenzaHome.findDocAmm(scadenza, Fattura_attiva_rigaIBulk.class);
							for(Iterator it=listaDocAttivi.iterator();it.hasNext();)
							{
								bFound = true;
								Fattura_attiva_rigaIBulk docAttivo = (Fattura_attiva_rigaIBulk) it.next();
								lockBulk(userContext,docAttivo);
								docAttivo.setStato_cofi(Fattura_attiva_rigaIBulk.STATO_ANNULLATO);
								updateBulk(userContext,docAttivo);
							}
						}
						if (scadenza.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA)) {
							listaDocAttivi = scadenzaHome.findDocAmm(scadenza, Fattura_passiva_rigaIBulk.class);
							for(Iterator it=listaDocAttivi.iterator();it.hasNext();)
							{
								bFound = true;
								Fattura_passiva_rigaIBulk docAttivo = (Fattura_passiva_rigaIBulk) it.next();
								lockBulk(userContext,docAttivo);
								docAttivo.setStato_cofi(Fattura_passiva_rigaIBulk.STATO_ANNULLATO);
								updateBulk(userContext,docAttivo);
							}
						}
						if (scadenza.getCd_tipo_documento_amm().equals(Numerazione_doc_ammBulk.TIPO_RIMBORSO)) {
							listaDocAttivi = scadenzaHome.findDocAmm(scadenza, RimborsoBulk.class);
							for(Iterator it=listaDocAttivi.iterator();it.hasNext();)
							{
								bFound = true;
								RimborsoBulk docAttivo = (RimborsoBulk) it.next();
								lockBulk(userContext,docAttivo);
								docAttivo.setStato_cofi(Fattura_attiva_rigaIBulk.STATO_ANNULLATO);
								updateBulk(userContext,docAttivo);
							}
						}
						if (!bFound)
							throw new ApplicationException( "Attenzione! Non è stato trovato il documento attivo a cui è collegato la scadenza da azzerare!");
*/
					}
				}
			}
			catch ( Exception e )
			{
				throw handleException( e )	;
			}
		}
	}
}
private void aggiornaSaldiAccertamentiResiduiPropri (UserContext uc,AccertamentoBulk accertamento) throws ComponentException
{
	if (accertamento.isAccertamentoResiduo()) {
		//AccertamentoResiduoBulk obbRes = (AccertamentoResiduoBulk) accertamento;
		//Accertamento_modificaBulk acrMod = obbRes.getAccertamento_modifica();

		//aggiornaCapitoloSaldoAccertamento( uc, accertamento, MODIFICA );
		//aggiornaStatoCOAN_COGEDocAmm( uc, accertamento );

		if (accertamento.isAccertamentoResiduo()) {
			AccertamentoResiduoBulk obbRes = (AccertamentoResiduoBulk) accertamento;
			Accertamento_modificaBulk obbMod = obbRes.getAccertamento_modifica();
			if (obbMod!=null) {
				SaldoComponentSession session = createSaldoComponentSession();
				try
				{
					for ( Iterator i = obbMod.getAccertamento_mod_voceColl().iterator(); i.hasNext(); )
					{
						Accertamento_mod_voceBulk obbModVoce = (Accertamento_mod_voceBulk) i.next();

						session.aggiornaAccertamentiResiduiPropri(
							uc,
							obbModVoce.getCd_centro_responsabilita(),
							obbModVoce.getCd_linea_attivita(),
							obbModVoce.getVoce(),
							obbMod.getAccertamento().getEsercizio_originale(),
							obbModVoce.getIm_modifica());
					}
				}
				catch ( Exception e )
				{
					throw handleException( e )	;
				}
			}
		}

	}
}
//^^@@
/**
  *  Tutti controlli superati
  *    PreCondition:
  *      scadenza(n+1) esiste
  *      scadenza(n+1).importo > differenza in scadenza(n).importo
  *    PostCondition:
  *      Il sistema eseguirà l'aggiornamento dell'importo della scadenza successiva (n+1) dell'accertamento aggiungendo la differenza fra il nuovo e vecchio importo della scadenza in aggiornamento.
  *      La differenza è espressa come (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio)
  *  scadenza(n+1).importo <= differenza in scadenza(n).importo
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma l'aumento dell'importo della scadenza(n) supera il valore dell'importo dell'ultima scadenza dell'accertamento. Una formula per questa condizione sarebbe (scadenzario(n+1).importo - (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio) > 0)
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché l'aumento dell'importo della scadenza(n) è maggiore all'importo dell'ultima scadenza (cercarebbe settare l'importo <= 0). L'attività non è consentita.
  *  scadenza(n+1) non esiste
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma la scadenza in aggiornamento è l'ultima scadenza dell'accertamento.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché non esiste una scadenza successiva. L'attività non è consentita.
  *
  * @param	aUC	lo UserContext che ha generato la richiesta
  * @param	scadenza	l'Accertamento_scadenzarioBulk per cui aggiornare la scadenza successiva
  * @return l'AccertamentoBulk con l'importo della scadenza successiva modificato
  *
 */

public AccertamentoBulk aggiornaScadenzarioSuccessivoAccertamento (UserContext aUC,Accertamento_scadenzarioBulk scadenza) throws ComponentException
{
   	AccertamentoBulk accertamento = scadenza.getAccertamento();
   	Accertamento_scadenzarioBulk scadSuccessivaNew;

   	// verifico se l'utente ha impostato l'aggiornamento automatico dell'importo della successiva scadenza
	if((scadenza.getFl_aggiorna_scad_successiva() == null) || (!scadenza.getFl_aggiorna_scad_successiva().booleanValue()))  {return accertamento;}

   	// se non ho scadenze oppure ne ho solo una non faccio niente
	if (scadenza.getScadenza_iniziale() == null)  {return accertamento;}

	// Calcolo quanto aggiungere/togliere alla scadenza successiva
	java.math.BigDecimal delta = scadenza.getScadenza_iniziale().getIm_scadenza().subtract(scadenza.getIm_scadenza());

	/*
	if((accertamento.getAccertamento_scadenzarioColl().size() == 0) || (accertamento.getAccertamento_scadenzarioColl().size() == 1))
	{
		throw handleException( new it.cnr.jada.comp.ApplicationException( "Non esiste una scadenza successiva da aggiornare automaticamente !"));
	}
	*/

	// ricerco l'indice della scadenza selezionata
	int indice=0;
	for ( Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext();)
	{
		if(scadenza == (Accertamento_scadenzarioBulk) i.next())
		{
			break;
		}
		indice=indice+1;
	}
	indice = indice + 1;						// indice della scadenza successiva

	//non esiste scadenza successiva
	//se residuo proprio e il delta è positivo (è stata diminuito l'importo della scadenza precedente)
	//inserisco una nuova scadenza
	if ( indice == accertamento.getAccertamento_scadenzarioColl().size()) {
		if (delta.doubleValue() < 0)
			throw handleException( new ApplicationException( "Non esiste una scadenza successiva da aggiornare" ));
		else {
			scadSuccessivaNew = new Accertamento_scadenzarioBulk();
			scadSuccessivaNew.setDt_scadenza_incasso(scadenza.getDt_scadenza_incasso());
			scadSuccessivaNew.setDs_scadenza(scadenza.getDs_scadenza());
			scadSuccessivaNew.setIm_scadenza(Utility.ZERO);
			scadSuccessivaNew.setToBeCreated();
			indice = accertamento.addToAccertamento_scadenzarioColl(scadSuccessivaNew);
		}
	}

	// Cerco la scadenza successiva da modificare
	Accertamento_scadenzarioBulk scadenzaSuccessiva = (Accertamento_scadenzarioBulk) accertamento.getAccertamento_scadenzarioColl().get( indice );

	// Se la scadenza successiva e' collegata a fattura attiva non posso modificarla
	if ( scadenzaSuccessiva.getPg_doc_attivo() != null )
			throw new ApplicationException( "Modifica impossibile: la scadenza successiva e' associata a doc. amministrativi");


	if(delta.doubleValue() == 0) return accertamento;

	// L'importo della scadenza successiva va in negativo
	if((delta.doubleValue() < 0) && (scadenzaSuccessiva.getIm_scadenza().add(delta).doubleValue() <= 0))
		throw handleException( new ApplicationException( "L'importo della scadenza successiva e' inferiore all'importo da aggiornare" ));

	// Aggiorno importo scadenza successiva
	Accertamento_scadenzarioBulk scadenzaSuccessivaIniziale = new Accertamento_scadenzarioBulk();
	scadenzaSuccessivaIniziale.setIm_scadenza( scadenzaSuccessiva.getIm_scadenza());
	scadenzaSuccessiva.setScadenza_iniziale( scadenzaSuccessivaIniziale);
	scadenzaSuccessiva.setIm_scadenza(scadenzaSuccessiva.getIm_scadenza().add( delta ));
	if (!scadenzaSuccessiva.isToBeCreated())
		scadenzaSuccessiva.setToBeUpdated();
	scadenza.setToBeUpdated();

	// Rigenero i relativi dettagli
	generaDettagliScadenzaAccertamento(aUC, accertamento, scadenzaSuccessiva, accertamento.isAccertamentoResiduo()?false:true);

    return accertamento;
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna stato COAN/COGE di doc. amministrativi
 * Pre:  Una richiesta di modifica di un accertamento e' stata generata.
 * Post: E' stata richiamata la stored procedure che provvede ad aggiornare gli stati COAN/COGE degli eventuali doc. amministrativi
 *       contabilizzati sull'accertamento
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile l' AccertamentoBulk che e' stato modificato/annullato
 *
*/

private void aggiornaStatoCOAN_COGEDocAmm( UserContext userContext, AccertamentoBulk docContabile  )  throws  ComponentException
{
	callDoRiprocAcc(userContext, docContabile, null);
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Annullamento logico accertamento
 * Pre:  Una richiesta di annullamento di un accertamento e' stata generata
 *       L'accertamento non ha documenti amministartivi associati
 * Post: Viene impostata la data di cancellazione dell'accertamento, viene azzerato il suo importo e gli importi di tutte
 *       le scadenze e di tutti dettagli di scadenza. Viene aggiornato il saldo relativo al capitolo dell'accertamento.
 *
 *
 * Nome: Errore doc. amm.
 * Pre:  Una richiesta di annullamento di un accertamento e' stata generata
 *       L'accertamento ha documenti amministartivi associati
 * Post: Una segnalazione di errore viene restituita per comunicare all'utente l'impossibilità di eseguire l'operazione
 *       di annullamento
 *
 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	accertamento l' AccertamentoBulk da annullare
 * @return	accertamento l' AccertamentoBulk annullato
*/

//
// L'accertamento prevede solo una cancellazione logica.
// Se l'accertamento non risulta associato ad alcun
// documento amministrativo, tale cancellazione avverra'
// azzerando tutti gli importi dell'accertamento
//

public AccertamentoBulk annullaAccertamento( UserContext aUC, AccertamentoBulk accertamento ) throws ComponentException
{

	accertamento.setCrudStatus(OggettoBulk.NORMAL);

	try
	{
        if (accertamento.isAssociataADocAmm())
            throw new ApplicationException("Impossibile annullare un accertamento con documenti amministrativi collegati");

            /* simona
        accertamento.setDt_cancellazione(new java.sql.Timestamp(System.currentTimeMillis())); */
        accertamento.setDt_cancellazione( DateServices.getDt_valida(aUC));

		// azzero tutti gli importi
		accertamento.storna();
		// aggiornamento accertamento + scadenze + dettagli
		makeBulkPersistent( aUC, accertamento);

		//aggiorno i saldi
		aggiornaCapitoloSaldoAccertamento( aUC, accertamento, MODIFICA );


		/*
	    if ( !aUC.isTransactional() )
			aggiornaStatoCOAN_COGEDocAmm( aUC, accertamento );
		*/

        return accertamento;

    } catch (Exception e) {
        throw handleException(e);
    }

}
/**
  *  riprocessa lo stato coge/coan di documenti associati al doc. contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riprocessare lo stato coge/coan di doc. amm. associati al documento contabile
  *	 PostCondition:
  *      Vengono cambiati gli stati coge/coan dei doc amm associati al doc. cont
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da utilizzare
  * @param pg_ver_rec <code>Long</code> pg_ver_rec di riferimento
  *
  */

public void callDoRiprocAcc(
	UserContext userContext,
	IDocumentoContabileBulk doc,
	Long pg_ver_rec)
	throws it.cnr.jada.comp.ComponentException
{
	try
	{
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
			"call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB215.doRiprocAcc(?, ?, ?, ?, ?)",false,this.getClass());
		try
		{
			cs.setInt( 1, doc.getEsercizio().intValue());
			cs.setString( 2, doc.getCd_cds());
			cs.setInt( 3, doc.getEsercizio_originale().intValue());
			cs.setLong( 4, doc.getPg_doc_contabile().longValue());
			if (pg_ver_rec == null)
				cs.setNull( 5, Types.DECIMAL);
			else
				cs.setLong( 5, pg_ver_rec.longValue());
			cs.executeQuery();

		}
		catch ( SQLException e )
		{
			throw handleException( e );
		}
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException( e );
	}
}
/**
  *  riporta all'esercizio successivo di doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare all'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il doc.contabile è stato riportato all'esercizio successivo richiamando
  *      la stored procedure CNRCTB046.riportoEsNextDocCont
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaAvanti (UserContext userContext,IDocumentoContabileBulk doc) throws it.cnr.jada.comp.ComponentException
{
	try
	{
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
			"call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB046.riportoEsNextDocCont(?, ?, ?, ?, ?, ?)",false,this.getClass());
		try
		{
			cs.setString( 1, doc.getCd_cds());
			cs.setObject( 2, doc.getEsercizio());
			cs.setObject( 3, doc.getEsercizio_originale());
			cs.setObject( 4, doc.getPg_doc_contabile());
			cs.setString( 5, doc.getTi_entrata_spesa());
			cs.setString( 6, ((CNRUserContext)userContext).getUser() );
			cs.executeQuery();

		}
		catch ( SQLException e )
		{
			throw handleException( e );
		}
		catch ( Exception e )
		{
			throw handleException( e );
		}
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException( e );
	}
}
/**
  *  riporta indietro dall'esercizio successivo di un doc.contabile
  *    PreCondition:
  *      E' stata inoltrata una richiesta di riportare indietro dall'esercizio successivo un documento contabile
  *	 PostCondition:
  *		Il doc.contabile è stato riportato all'esercizio successivo richiamando
  *      la stored procedure CNRCTB046.deriportoEsNextDocCont
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param doc <code>IDocumentoContabileBulk</code> doc.contabile da riportare
  *
  */

public void callRiportaIndietro (UserContext userContext,IDocumentoContabileBulk doc) throws ComponentException
{
	try
	{
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
			"call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB046.deriportoEsNextDocCont(?, ?, ?, ?, ?, ?)",false,this.getClass());
		try
		{
			cs.setString( 1, doc.getCd_cds());
			cs.setObject( 2, doc.getEsercizio());
			cs.setObject( 3, doc.getEsercizio_originale());
			cs.setObject( 4, doc.getPg_doc_contabile());
			cs.setString( 5, doc.getTi_entrata_spesa());
			cs.setString( 6, ((CNRUserContext)userContext).getUser() );
			cs.executeQuery();

		}
		catch ( SQLException e )
		{
			throw handleException( e );
		}
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException( e );
	}
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Cancella i dettagli della scadenza
 * Pre:  L'utente ha modificato la selezione della linea di attività dell'accertamento
 * Post: Sono stati eliminati i dettagli della scadenza in quanto si riferivano alla precedente linea di attività
 *
 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	accertamento l' AccertamentoBulk la cui linea di attività e' stata modificata
 * @param	scadenzario l'Accertamento_scadenzarioBulk per cui eliminare i dettagli
 * @return	l'Accertamento_scadenzarioBulk senza dettagli
*/

public Accertamento_scadenzarioBulk cancellaDettagliScadenze (UserContext aUC,AccertamentoBulk accertamento, Accertamento_scadenzarioBulk scadenzario) throws ComponentException
{
	Accertamento_scad_voceBulk osv;
	Accertamento_scadenzarioBulk os;
	V_pdg_accertamento_etrBulk ppsd;
	Linea_attivitaBulk la;
	boolean found;
	int index = 0;

	BulkList listaLAComuni = dettagliScadenzaLAComuni(aUC, accertamento, scadenzario);

	//cancello i dettagli scadenze per le linee di attività che non esistono piu'

	for ( Iterator scadIterator = scadenzario.getAccertamento_scad_voceColl().iterator(); scadIterator.hasNext(); index++)
	{
		osv = (Accertamento_scad_voceBulk) scadIterator.next();
		found = false;
		for ( Iterator lattIterator = accertamento.getLineeAttivitaSelezionateColl().iterator(); lattIterator.hasNext(); )
		{
			ppsd = (V_pdg_accertamento_etrBulk) lattIterator.next();
			if ( osv.getCd_centro_responsabilita().equals( ppsd.getCd_centro_responsabilita()) &&
				 osv.getCd_linea_attivita().equals( ppsd.getCd_linea_attivita()) )
			{
				found = true;
				break;
			}
		}
		if ( !found )
			for ( Iterator lattIterator = accertamento.getNuoveLineeAttivitaColl().iterator(); lattIterator.hasNext(); )
			{
				la = (Linea_attivitaBulk) lattIterator.next();
				if ( osv.getCd_centro_responsabilita().equals( la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita()) &&
					 osv.getCd_linea_attivita().equals( la.getLinea_att().getCd_linea_attivita()) )
				{
					found = true;
					break;
				}
			}

		if ( !found )
		{
			// dettaglio da cancellare solo se la linea di attività non è
			// tra quelle comuni legate al cdr
			if (!listaLAComuni.containsByPrimaryKey(osv.getLinea_attivita())) {
				osv.setToBeDeleted();
				scadIterator.remove();
			}
		}
	}
	return scadenzario;
}
/**
  *  creazione dettaglio di scadenza di accertamento di sistema
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un dettaglio per una scadenza di un accertamento di sistema
  *    PostCondition:
  *      Un dettaglio per la scadenza di un accertamento di sistema e' stato creata con
  *      importo uguale all'importo della scadenza e linea di attività uguale alla linea di attività definita
  *      in Configurazione CNR come LINEA ATTIVITA' ENTE di ENTRATA
  *
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accert_scad <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento di sistema per la quale e' necessario creare un
  *        dettaglio
  * @return <code>Accertamento_scad_voceBulk</code> Il dettaglio della scadenza dell'accertamento di sistema creato
 */

private Accertamento_scad_voceBulk creaAccertamento_scad_voce (UserContext uc,Accertamento_scadenzarioBulk accert_scad ) throws ComponentException
{
	try
	{
		Accertamento_scad_voceBulk accert_scad_voce = new Accertamento_scad_voceBulk();
		accert_scad_voce.setUser( accert_scad.getAccertamento().getUser() );
		accert_scad_voce.setToBeCreated();


		accert_scad_voce.setAccertamento_scadenzario( accert_scad );

		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( uc, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_ATTIVITA_ENTRATA_ENTE );
		if ( config == null  )
			throw new ApplicationException("Configurazione CNR: manca la definizione del GAE ENTRATA ENTE");
		if ( config.getVal01() == null || config.getVal02() == null )
			throw new ApplicationException("Configurazione CNR: non sono stati impostati i valori per GAE SPECIALE - GAE ENTRATA ENTE");
		WorkpackageHome wphome = (WorkpackageHome) getHome(uc, WorkpackageBulk.class);
		WorkpackageBulk wp = (WorkpackageBulk) wphome.findByPrimaryKey(new WorkpackageBulk(config.getVal01(),config.getVal02()));
		//accert_scad_voce.setCd_linea_attivita( config.getVal02() );
		//accert_scad_voce.setCd_centro_responsabilita(config.getVal01() );
		accert_scad_voce.setLinea_attivita(wp);

		accert_scad_voce.setIm_voce( accert_scad.getAccertamento().getIm_accertamento() );
		accert_scad.getAccertamento_scad_voceColl().add( accert_scad_voce );
		return accert_scad_voce;

	}
	catch ( Exception e )
	{
		throw handleException( accert_scad.getAccertamento(), e );
	}
}
/**
  *  creazione scadenza di accertamento di sistema
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una scadenza di un accertamento di sistema
  *    PostCondition:
  *      Una scadenza per un accertamento di sistema e' stato creata con data scadenza la data di
  *      registrazione dell'accertamento e importo scadenza uguale all'importo dell'accertamento
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param accertamento <code>AccertamentoBulk</code> l'accertamento di sistema per il quale e' necessario creare una
  *        scadenza
  * @return <code>Accertamento_scadenzarioBulk</code> La scadenza dell'accertamento di sistema creata
 */

private Accertamento_scadenzarioBulk creaAccertamento_scadenzario (UserContext uc, AccertamentoBulk accertamento) throws ComponentException
{
		Accertamento_scadenzarioBulk accert_scad = new Accertamento_scadenzarioBulk();
		accert_scad.setUser( accertamento.getUser() );
		accert_scad.setToBeCreated();

		// campi chiave
		accert_scad.setAccertamento( accertamento );

		// altri campi
		accert_scad.setDt_scadenza_incasso( accertamento.getDt_registrazione() );

		accert_scad.setDs_scadenza( accertamento.getDs_accertamento() );

//		accert_scad.setDt_scadenza_emissione_fattura( accertamento.getDt_registrazione() );

		/*
		accertamento.getAccertamento_scadenzarioColl().add( accert_scad );
		*/
		accertamento.addToAccertamento_scadenzarioColl( accert_scad );
		accert_scad.setIm_associato_doc_amm( accertamento.getIm_accertamento() );
		accert_scad.setIm_scadenza( accertamento.getIm_accertamento() );
		//L'IMPORTO ASS. A DOC CONTABILE VIENE MESSO A 0 PERCHE' E' AGGIORNATO DALLA ReversaleComponent.creaConBulk()
		accert_scad.setIm_associato_doc_contabile( new BigDecimal(0) );
//		accert_scad.setIm_associato_doc_contabile( accertamento.getIm_accertamento() );


		return accert_scad;
}
/**
  *  creazione accertamento di sistema
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un accertamento di sistema di appartenenza di un Cds per completare
  *      la pratica della reversale di trasferimento dal CNR a favore del Cds
  *    PostCondition:
  *      Un accertamento di sistema (ACR_SIST) e' stato creato per il Cds beneficiario del mandato di accreditamento,
  *      con cds/uo origine uguale al cds/uo appartenenza e importo pari alla riga del mandato. Il capitolo di entrata Cds
  *      viene ricavato dalla relazione fra Capitolo di Spesa Cnr e Capitolo di Entrata Cds (metodo findAssociazioneCapSpesaCNRCapEntrataCdS),
  *      il debitore e' il CNR. Sono stati inoltre creati una scadenza di accertamento (metodo creaAccertamento_scadenzario)
  *      e un dettaglio di tale scadenza (metodo creaAccertamento_scad_voce)
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandatoRiga <code>MandatoAccreditamento_rigaBulk</code> la riga del mandato di accreditamento per la quale e'
  *        necessario creare una riga della reversale di trasferimento associata all'accertamento di sistema da creare
  * @param uo <code>Unita_organizzativaBulk</code> l'unità organizzativa beneficiaria del mandato di accreditamento
  * @return <code>AccertamentoBulk</code> L'accertamento di sistema creato
 */

public AccertamentoBulk creaAccertamentoDiSistema (UserContext userContext, MandatoAccreditamento_rigaBulk mandatoRiga, Unita_organizzativaBulk uo ) throws ComponentException
{
	try
	{
		//REVERSALE
		MandatoBulk mandato = mandatoRiga.getMandato();
		AccertamentoCdsBulk accertamento = new AccertamentoCdsBulk();
		accertamento.setToBeCreated();
		accertamento.setUser( mandato.getUser() );
		accertamento.setEsercizio( mandato.getEsercizio());
		accertamento.setEsercizio_originale( mandato.getEsercizio());
		accertamento.setEsercizio_competenza( mandato.getEsercizio());
		accertamento.setCd_cds( uo.getCd_unita_padre());
		accertamento.setCd_unita_organizzativa( uo.getCd_unita_organizzativa());
//		accertamento.setCd_cds_origine( mandato.getCd_cds_origine());
//		accertamento.setCd_uo_origine( mandato.getCd_uo_origine());
		accertamento.setCd_cds_origine( uo.getCd_unita_padre());
		accertamento.setCd_uo_origine(  uo.getCd_unita_organizzativa());
		accertamento.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR_SIST );
		accertamento.setFl_pgiro( new Boolean(false));
		accertamento.setRiportato( "N" );
		accertamento.setDt_registrazione( mandato.getDt_emissione());
		accertamento.setDs_accertamento( "ACCERTAMENTO DI SISTEMA PER REVERSALI DI TRASFERIMENTO" );
		accertamento.setIm_accertamento( mandatoRiga.getIm_mandato_riga());
		// terzo = CDS
		accertamento.setDebitore(findTerzoUO( userContext, mandato.getCd_unita_organizzativa()) );
		//voce
		Ass_ev_evBulk ass = findAssociazioneCapSpesaCNRCapEntrataCdS( userContext, mandatoRiga, uo );
		accertamento.setTi_appartenenza( ass.getTi_appartenenza_coll() );
		accertamento.setTi_gestione( ass.getTi_gestione_coll() );
		accertamento.setCd_elemento_voce( ass.getCd_elemento_voce_coll() );
		accertamento.setCd_voce( ass.getCd_elemento_voce_coll() );
		accertamento.getCapitolo().setEsercizio( accertamento.getEsercizio());


		Accertamento_scadenzarioBulk accert_scad = creaAccertamento_scadenzario( userContext, accertamento );
		creaAccertamento_scad_voce( userContext, accert_scad );

		//aggiorna i saldi e inserisce l'accertamento
		return (AccertamentoBulk) creaConBulk( userContext, accertamento );

	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}


}
/**
  *  Tutti i controlli superati - contesto non transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento'
  *      L'accertamento non e' stato creato in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene creato, il suo tipo documento viene impostato in base all'esercizio di competenza
  *      ( se esercizio competenza = esercizio di creazione il tipo e' ACR, altrimenti e' ACR_PLUR) e il saldo
  *      del capitolo dell'accertamento viene aggiornato (metodo 'aggiornaCapitoloSaldoAccertamento')
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento'
  *      L'accertamento e' stato creato in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene creato e il suo tipo documento viene impostato in base all'esercizio di competenza
  *      ( se esercizio competenza = esercizio di creazione il tipo e' ACR, altrimenti e' ACR_PLUR)
  *  Errore di verifica accertamento
  *    PreCondition:
  *      Una richiesta di creazione di un accertamento e' stata generata e l'accertamento non ha superato i
  *      controlli eseguiti dal metodo 'verificaAccertamento'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>AccertamentoBulk</code> l'accertamento da creare
  * @return <code>AccertamentoBulk</code> l'accertamento  creato
  *
 */

public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
	try
	{

		// Verifico che l'esercizio del CDS sia stato aperto
		if (! ((AccertamentoHome)getHome(uc, AccertamentoBulk.class)).verificaStatoEsercizio((AccertamentoBulk) bulk))
			throw handleException( new ApplicationException( "Non e' possibile creare accertamenti: esercizio del Cds non ancora aperto!") );
	}
	catch (Exception e )
	{
		throw handleException( e );
	}

	if ( accertamento.getCd_tipo_documento_cont() == null )
	{
		if ( accertamento.getEsercizio().equals( accertamento.getEsercizio_competenza()))
			accertamento.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR );
		else
			accertamento.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR_PLUR );
	}
	// MITODO - sull'obbligazione cè la seguente riga, verificare
	//generaDettagliScadenzaObbligazione( uc, obbligazione, null );
	verificaAccertamento( uc, (AccertamentoBulk) bulk );

	validaAccertamentoPluriennale(uc, (AccertamentoBulk) bulk);

	bulk = super.creaConBulk( uc, bulk );

	verificaCoperturaContratto( uc, (AccertamentoBulk) bulk );
	verificaCoerenzaGaeContratto( uc, (AccertamentoBulk) bulk );
	
	if ( !uc.isTransactional() )
		aggiornaCapitoloSaldoAccertamento( uc, (AccertamentoBulk) bulk, INSERIMENTO );

	try {
		accertamento = validaCreaModificaElementoVoceNext(uc, accertamento);
	} catch ( Exception e ) {
		throw handleException( e )	;
	}

	return bulk;

}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Linea attività non comune
 * Pre:  L'utente ha selezionato una linea di attività non comune per l'accertamento
 * Post: E' stato generato un dettaglio (Accertamento_scad_voceBulk) per la scadenza con importo pari alla scadenza stessa
 *
 * Nome: Linea attività comune
 * Pre:  L'utente ha selezionato una linea di attività comune per l'accertamento
 * Post: Per ogni linea di attività collegata a quella selezionata dall'utente e' stato generato un dettaglio (Accertamento_scad_voceBulk) per la scadenza;
 *       tutti i dettagli hanno importo uguale a 0 tranne il dettaglio che esattamente corrisponde alla linea di attività selezionata
 *       dall'utente che ha importo pari alla scadenza stessa

 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	accertamento l' AccertamentoBulk da cui recuperare lalinea di attività
 * @param	scadenza l'Accertamento_scadenzarioBulk per cui generare i dettagli
*/
/*
private void creaDettagliScadenza(UserContext aUC,AccertamentoBulk accertamento, Accertamento_scadenzarioBulk scadenza) throws ComponentException
{
	Accertamento_scad_voceBulk dettaglio;

	try
	{
		//	Linea attività non comune
		if ( !accertamento.getLinea_attivita().getCd_linea_attivita().startsWith( "C" ))
		{
			//	Creo un solo dettaglio (Accertamento_scad_voceBulk) della scadenza
			dettaglio = new Accertamento_scad_voceBulk();
			dettaglio.setToBeCreated();
			dettaglio.setLinea_attivita(accertamento.getLinea_attivita());
			dettaglio.setCd_linea_attivita( accertamento.getLinea_attivita().getCd_linea_attivita());
			dettaglio.setCd_centro_responsabilita( accertamento.getLinea_attivita().getCd_centro_responsabilita());
			dettaglio.setUser( aUC.getUser());
			dettaglio.setAccertamento_scadenzario( scadenza );
			dettaglio.setIm_voce( scadenza.getIm_scadenza());
			scadenza.getAccertamento_scad_voceColl().add(  dettaglio );

		}
		// Linea di attivita' comune
    	else
    	{
	    	// Cerco le Linee di Attivita' comuni a quella selezionata
	  	  	List lineeAttComuni = ((AccertamentoHome)getHome( aUC, AccertamentoBulk.class )).findLineeAttivitaComuni( accertamento.getLinea_attivita(), ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getEsercizio() );
	  	  	it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAtt;

    		// per ogni Linea di Attivita' creo	un nuovo dettaglio (Accertamento_scad_voceBulk) della scadenza
	 	   	for ( Iterator i = lineeAttComuni.iterator(); i.hasNext(); )
		    {
				lineaAtt = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk) i.next();
				dettaglio = new Accertamento_scad_voceBulk();
				dettaglio.setToBeCreated();
				dettaglio.setLinea_attivita(lineaAtt);
				dettaglio.setCd_linea_attivita( lineaAtt.getCd_linea_attivita());
				dettaglio.setCd_centro_responsabilita( lineaAtt.getCd_centro_responsabilita());
				dettaglio.setUser( aUC.getUser());
				dettaglio.setAccertamento_scadenzario( scadenza );
				if ( accertamento.getLinea_attivita().getCd_centro_responsabilita().equals( dettaglio.getCd_centro_responsabilita()))
					dettaglio.setIm_voce( scadenza.getIm_scadenza());
				else
					dettaglio.setIm_voce( new java.math.BigDecimal(0));
				scadenza.getAccertamento_scad_voceColl().add(  dettaglio );
			    dettaglio.ordinaPerCdr(scadenza.getAccertamento_scad_voceColl());
		    }
	    }
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
*/

protected void creaDettagliScadenzaLAComuni(UserContext aUC, AccertamentoBulk accertamento, Accertamento_scadenzarioBulk scadenza) throws ComponentException
{

	//aggiungiamo i dettagli per le linea attività comuni
	Accertamento_scad_voceBulk dettaglio;
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAtt;
	BulkList listaFinale = dettagliScadenzaLAComuni(aUC, accertamento, scadenza);
	for ( Iterator i = listaFinale.iterator(); i.hasNext(); )
	{
		lineaAtt = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk) i.next();
		dettaglio = new Accertamento_scad_voceBulk();
		dettaglio.setToBeCreated();
		dettaglio.setLinea_attivita(lineaAtt);
		dettaglio.setCd_linea_attivita( lineaAtt.getCd_linea_attivita());
		dettaglio.setCd_centro_responsabilita( lineaAtt.getCd_centro_responsabilita());
		dettaglio.setUser( aUC.getUser());
		dettaglio.setAccertamento_scadenzario( scadenza );
		dettaglio.setPrc( new java.math.BigDecimal(0));
		dettaglio.setIm_voce( new java.math.BigDecimal(0));
		if (!scadenza.getAccertamento_scad_voceColl().containsByPrimaryKey(dettaglio)) {
			scadenza.getAccertamento_scad_voceColl().add(  dettaglio );
			dettaglio.ordinaPerCdr(scadenza.getAccertamento_scad_voceColl());
		}
	}
}

private BulkList dettagliScadenzaLAComuni(UserContext aUC, AccertamentoBulk accertamento, Accertamento_scadenzarioBulk scadenza) throws ComponentException
{
	BulkList listaFinale = new BulkList();
	try
	{
		//aggiungiamo i dettagli per le linea attività comuni
		V_pdg_accertamento_etrBulk lineav;
		WorkpackageBulk linea;
		//Collection lineeColl = accertamento.getLineeAttivitaSelezionateColl();
		Iterator it = accertamento.getLineeAttivitaSelezionateColl().iterator();
		while (it.hasNext()) {
			lineav = (V_pdg_accertamento_etrBulk) it.next();
			linea = new WorkpackageBulk();
			linea.setCd_linea_attivita(lineav.getCd_linea_attivita());
			if (linea.getCd_linea_attivita().startsWith( "C" ))
			{
				// Cerco le Linee di Attivita' comuni a quella selezionata
				List lineeAttComuni = ((AccertamentoHome)getHome( aUC, AccertamentoBulk.class )).findLineeAttivitaComuni( linea, ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getEsercizio() );
				it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAtt;

				// per ogni Linea di Attivita' creo	un nuovo dettaglio (Accertamento_scad_voceBulk) della scadenza
				for ( Iterator i = lineeAttComuni.iterator(); i.hasNext(); )
				{
					lineaAtt = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk) i.next();
					if ( !lineav.getCd_centro_responsabilita().equals( lineaAtt.getCentro_responsabilita().getCd_centro_responsabilita()))
					{
						listaFinale.add(lineaAtt);
					}
				}
			}
		}
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
	return listaFinale;
}
 /**
 * Crea la ComponentSession da usare per effettuare le operazioni di lettura della Configurazione CNR
 *
 * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code> che serve per leggere i parametri di configurazione del CNR
 */
private Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession() throws ComponentException
{
	try
	{
		return (Configurazione_cnrComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di aggiornamento Saldi delle voci del piano
 *
  * @return <code>SaldoComponentSession</code> La component dei Saldi delle voci del piano
 */
private it.cnr.contab.doccont00.ejb.SaldoComponentSession createSaldoComponentSession() throws ComponentException
{
	try
	{
		return (SaldoComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_SaldoComponentSession");
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}
}
/**
  *  ricerca associazione fra capitolo di spesa CNR e capitolo di entrata Cds
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca del capitolo di entrata Cds dato un capitolo di spesa CNR
  *    PostCondition:
  *      Viene restituita l'assoziazione fra capitolo spesa CNR e capitolo di entrata Cds che soddisfa le seguenti condizioni:
  *      - capitolo di spesa CNR = capitolo specificato nell'impegno associato alla riga del mandato
  *      - natura = natura del capitolo di spesa CNR
  *      - cd_cds = tipo del cds beneficiario del mandato di accreditamento
  *  ricerca associazione fra capitolo di spesa CNR e capitolo di entrata Cds - errore
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca del capitolo di entrata Cds dato un capitolo di spesa CNR, ma
  *      non esiste nessuna associazione fra il capitolo di spesa CNR, la natura di tale capitolo, il tipo Cds,
  *      e un capitolo di entrata Cds
  *    PostCondition:
  *      Viene restituito un errore all'utente per segnalare l'assenza di tale associazione
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param mandatoRiga <code>MandatoAccreditamento_rigaBulk</code> la riga del mandato di accreditamento associata all'impegno
  *        da cui ricavare il capitolo di spesa CNRper la quale e'
  * @param uo <code>Unita_organizzativaBulk</code> l'unità organizzativa beneficiaria del mandato di accreditamento da
  *        cui ricavare il tipo del Cds da cui dipende
  * @return <code>Ass_ev_evBulk</code> L'associazione identificata oppure null se l'associazione non esiste
 */

private Ass_ev_evBulk findAssociazioneCapSpesaCNRCapEntrataCdS (UserContext userContext, MandatoAccreditamento_rigaBulk mandatoRiga, Unita_organizzativaBulk uo ) throws ComponentException
{
	try
	{
		Voce_fBulk impegno = (Voce_fBulk) getHome( userContext, Voce_fBulk.class ).findByPrimaryKey(
			new Voce_fBulk( mandatoRiga.getImpegno().getCd_voce(),
							mandatoRiga.getEsercizio(),
							mandatoRiga.getImpegno().getTi_appartenenza(),
							mandatoRiga.getImpegno().getTi_gestione()));
		SQLBuilder sql = getHome( userContext, Ass_ev_evBulk.class ).createSQLBuilder();
		sql.addClause( "AND", "ti_appartenenza", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CNR );
		sql.addClause( "AND", "ti_gestione", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
		sql.addClause( "AND", "ti_elemento_voce", sql.EQUALS, Elemento_voceHome.TIPO_TITOLO );
		sql.addClause( "AND", "ti_appartenenza_coll", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CDS );
		sql.addClause( "AND", "ti_gestione_coll", sql.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE );
		sql.addClause( "AND", "ti_elemento_voce_coll", sql.EQUALS, Elemento_voceHome.TIPO_CAPITOLO );
		sql.addClause( "AND", "cd_natura", sql.EQUALS, impegno.getCd_natura() );
		/* tipo cds */
		sql.addClause( "AND", "cd_cds", sql.EQUALS, uo.getUnita_padre().getCd_tipo_unita() );
		sql.addSQLClause( "AND", "substr( '" + impegno.getCd_voce() + "', 1, length( cd_elemento_voce)) = cd_elemento_voce " );

		List result = getHome( userContext, Ass_ev_evBulk.class ).fetchAll( sql );
		if ( result.size() == 0 )
			throw new ApplicationException( "Non esiste la relazione fra il capitolo di spesa CNR e il capitolo di entrata CDS");
		return (Ass_ev_evBulk) result.get(0);

	}
	catch( Exception e )
	{
		throw handleException( e );
	}

}
/**
  *  ricerca anagrafica per UO
  *    PreCondition:
  *      E' stata generata la richiesta di ricercare la codifica in anagrafica di una unità organizzativa
  *    PostCondition:
  *      Viene restituito il TerzoBulk con cd_unita_organizzativa uguale al codice dell'UO da ricercare
  *  ricerca anagrafica per UO - errore
  *    PreCondition:
  *      E' stata generata la richiesta di ricercare la codifica in anagrafica di una unità organizzativa ma
  *      tale UO non e' ancora stata codificata in anagrafica
  *    PostCondition:
  *      Viene restituito un errore all'utente per segnalare l'assenza di tale anagrafica
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_unita_organizzativa <code>String</code> il codice della UO da ricercare in anagrafica
  * @return <code>TerzoBulk</code> L'istanza di Terzobulk identificata in anagrafica oppure null se l'istanza non esiste
 */

private TerzoBulk findTerzoUO (UserContext userContext, String cd_unita_organizzativa) throws ComponentException
{
	try
	{
		SQLBuilder sql = getHomeCache(userContext).getHome( TerzoBulk.class ).createSQLBuilder();
		sql.addClause("AND","cd_unita_organizzativa", sql.EQUALS, cd_unita_organizzativa);
		List result = getHomeCache(userContext).getHome( TerzoBulk.class ).fetchAll( sql );
		if ( result.size() == 0)
			throw new ApplicationException("Non e' stato definito in anagrafico il terzo per l'unità organizzativa " + cd_unita_organizzativa);
		return (TerzoBulk) result.get(0);
	}
	catch( Exception e )
	{
		throw handleException( e );
	}

}
/**
  *  Inizializzazione di scadenze e dettagli scadenza
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento e' stata generata
  *    PostCondition:
  *      Tutti le scadenze dell'accertamento e i relativi dettagli sono state inizializzati
  *
  * @param accertamento <code>AccertamentoBulk</code> l'istanza di AccertamentoBulk o AccertamentoCdsBulk per cui inizializzare scadenze e dettagli
  * @param dettagliScadenze la lista con tutti i dettagli di tutte le scadenze dell'accertamento
  * @param scadenzaDaFattura  <code>Accertamento_scadenzarioBulk</code> nel caso in cui l'inizializzazione e' stata richiesta
  *        da un doc. amministrativo oppure <code>null</code> se l'inizializzazione non e' stata richiesta da doc. amm.
  * @return <code>AccertamentoBulk</code> l'accertamento con scadenze e dettagli inizializzati
  *
 */

//
// Inizializzo le scadenze con i relativi dettagli
// Inizializzo i dettagli con le relative scadenze e la linea di attivita
//
/* MITODO - remmo per ora, verificare
private AccertamentoBulk fineInizializzazioneScadenzeEDettagli(AccertamentoBulk accertamento, BulkList dettagliScadenze, Accertamento_scadenzarioBulk scadenzaDaFattura)
{
	Accertamento_scadenzarioBulk scadenza;
	Accertamento_scad_voceBulk dettaglio;

	for (ListIterator i = accertamento.getAccertamento_scadenzarioColl().listIterator(); i.hasNext();)
	{
		scadenza = (Accertamento_scadenzarioBulk) i.next();

		/*
		if((scadenzaDaFattura != null) &&
		   (scadenzaDaFattura.getPg_accertamento_scadenzario().longValue() == scadenza.getPg_accertamento_scadenzario().longValue()))
		{
			i.set(scadenza = scadenzaDaFattura);	// stessa istanza

			// Il doc. amm. ha gia' popolato i dettagli della scadenza e siccome
			// io vado in aggiunta ripulisco la collection
			scadenza.setAccertamento_scad_voceColl(new BulkList());
		}
		*/
/*
		for (Iterator j = dettagliScadenze.iterator(); j.hasNext();)
		{
			dettaglio = (Accertamento_scad_voceBulk) j.next();
			if(dettaglio.getPg_accertamento_scadenzario().longValue() == scadenza.getPg_accertamento_scadenzario().longValue())
			{
				scadenza.getAccertamento_scad_voceColl().add(  dettaglio );
				dettaglio.setLinea_attivita(accertamento.getLinea_attivita());
				dettaglio.setAccertamento_scadenzario( scadenza );
			}
		}
	}

	return accertamento;
}
*/
/**
 *
 * Pre-post-conditions:
 *
 * Nome: creazione scadenza
 * Pre:  Una richiesta di creazione di una nuova scadenza di un accertamento e' stata generata
 * Post: Vengono creati i dettagli (metodo 'creaDettagliScadenza') per la scadenza creata
 *
 * Nome: modifica scadenza
 * Pre:  Una richiesta di modifica di una scadenza di un accertamento e' stata generata
 * Post: Vengono modificati i dettagli (metodo 'modificaDettagliScadenza') per la scadenza modificata
 *
 * Nome: modifica linea att.
 * Pre:  Una richiesta di modifica della linea di attività di un accertamento e' stata generata
 * Post: Vengono eliminati tutti i dettagli di tutte le scadenze dell'accertamento (metodo 'cancellaDettagliScadenze') e vengono creati dei nuovi dettagli
 *       relativi alla nuova linea di attività (metodo 'creaDettagliScadenza')
 *
 * @param	aUC	lo UserContext che ha generato la richiesta
 * @param	accertamento l' AccertamentoBulk per cui rivedere i dettagli della scadenza
 * @param	scadenzaSelezionata l' Accertamento_scadenzarioBulk per cui rivedere i dettagli della scadenza oppure null
 *          nel caso in cui siano da rivedere i dettagli di tutte le scadenze dell'accertamento
*/
/*
public AccertamentoBulk generaDettagliScadenzaAccertamento (UserContext aUC,AccertamentoBulk accertamento,Accertamento_scadenzarioBulk scadenzaSelezionata) throws ComponentException
{
	Accertamento_scadenzarioBulk scadenza;
	Accertamento_scad_voceBulk dettaglio;

	// CREAZIONE / MODIFICA scadenza
	if ( scadenzaSelezionata != null )
	{
		// Modifico i dettagli della scadenza appena creata
		if(scadenzaSelezionata.getAccertamento_scad_voceColl().size() > 0)
		{
			modificoDettagliScadenza(aUC, accertamento, scadenzaSelezionata);

		}
		// Creo i dettagli della scadenza appena creata
		else
		{
			creaDettagliScadenza( aUC, accertamento, scadenzaSelezionata );
		}
	}
	// Cambio selezione Linea di Attivita'
	else
	{
		// Per ogni scadenza rigenero i relativi dettagli
		for ( Iterator scadIterator = accertamento.getAccertamento_scadenzarioColl().iterator(); scadIterator.hasNext(); )
		{
			scadenza = (Accertamento_scadenzarioBulk) scadIterator.next();

			// Cancello i dettagli della scadenza
			cancellaDettagliScadenze( aUC, accertamento, scadenza );

			// Ricreo i dettagli della scadenza per la nuova linea attivita selezionata
			creaDettagliScadenza( aUC, accertamento, scadenza );
		}
	}

	return accertamento;
}
*/
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws ComponentException {

	try
	{
		return getHome(userContext, MandatoIBulk.class).getServerDate();
	}
	catch(it.cnr.jada.persistency.PersistencyException ex)
	{
		throw handleException(ex);
	}
}
/**
  *  inizializzazione Accertamento di Sistema per modifica
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un accertamento di sistema ed e' pertanto
  *      necessario procedere alla sua inizializzazione
  *    PostCondition:
  *      Viene restituito l'accertamento di sistema e viene caricata la sua scadenza e il suo dettaglio di scadenza
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoBulk inizializzata
 */
private OggettoBulk inizializzaAccertamentoCdsPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) super.inizializzaBulkPerModifica( aUC, bulk);
		AccertamentoHome accertHome = (AccertamentoHome) getHome( aUC, accertamento.getClass());
		Accertamento_scad_voceHome dettaglioHome = (Accertamento_scad_voceHome) getHome( aUC, Accertamento_scad_voceBulk.class );
		// Carico i Cdr con Codice Unita' Organizzativa uguale al Codice Unita Organizzativa dell'accertamento
//		accertamento.getCdrColl().addAll( accertHome.findCdr( accertamento ));

		accertamento.setAccertamentiPluriennali(new BulkList(accertHome.findAccertamentiPluriennali(aUC,accertamento)));
		// Carico la scadenza
		accertamento.setAccertamento_scadenzarioColl( new BulkList( accertHome.findAccertamento_scadenzarioList( accertamento ) ));
		for (Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext();)
		{
			Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) i.next();

			scadenza.setAccertamento_scad_voceColl(new BulkList( dettaglioHome.findDettagli_scadenze( accertamento )));

			scadenza.setStatus( Accertamento_scadenzarioBulk.STATUS_CONFIRMED);
			scadenza.setAccertamento(accertamento);
//			inizializzaScadenzaConDocumenti(aUC, scadenza);
		}

		// Lettura dei dettagli delle singole scadenze

		BulkList dettagliScadenze = new BulkList();
		dettagliScadenze =  new BulkList( dettaglioHome.findDettagli_scadenze( accertamento ));
		String cdLA = ((Accertamento_scad_voceBulk)dettagliScadenze.get(0)).getCd_linea_attivita();


		// Inizializzazione delle lista di tutte le linee di attivita eleggibili e di quella selezionata
		// per l'accertamento
//		accertamento = (AccertamentoOrdBulk) inizializzoLineeAttivita(aUC, accertamento, cdLA);

		// MITODO - verificare se serve
		// Inizializzo le scadenze con i relativi dettagli
		// Inizializzo i dettagli con le relative scadenze e la linea di attivita
		//accertamento =  fineInizializzazioneScadenzeEDettagli(accertamento, dettagliScadenze, null);

//		accertamento.setCd_terzo_iniziale( accertamento.getCd_terzo());

		return accertamento;
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/**
  *  Esercizio non aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in uno stato diverso da APERTO
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare accertamenti
  *  Esercizio aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in stato APERTO
  *    PostCondition:
  *      una istanza di AccertamentoBulk viene restituita con impostata la data del giorno come data di emissione e
  *      il Cds Ente (999) come Cds dell'accertamento
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoBulk inizializzata
  *
 */

public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	AccertamentoBulk accertamento = (AccertamentoBulk)  super.inizializzaBulkPerInserimento(aUC, bulk );
	try
	{
		AccertamentoHome accertamentoHome = (AccertamentoHome) getHome( aUC, accertamento.getClass());

		// Inizializzo il Cd_unita_organizzativa/cd_cds con l'Unita Organizzativa Ente e
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( aUC, Unita_organizzativa_enteBulk.class ).findAll().get(0);

		if (!accertamento.isAccertamentoResiduo())
			if ( ((CNRUserContext)aUC).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() ))
				throw new ApplicationException( "Funzione non consentita per Unità Organizzativa " + uoEnte.getCd_unita_organizzativa());

		accertamento.setUnita_organizzativa(uoEnte);
		accertamento.setCd_unita_organizzativa(uoEnte.getCd_unita_organizzativa());
		//accertamento.setCd_cds( uoEnte.getCd_unita_padre());
		accertamento.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( accertamento.getUnita_organizzativa().getUnita_padre() ));

		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome( aUC, Unita_organizzativaBulk.class ).findByPrimaryKey( new Unita_organizzativaBulk( ((CNRUserContext)aUC).getCd_unita_organizzativa()));
		accertamento.setCd_uo_origine( uoScrivania.getCd_unita_organizzativa());
		accertamento.setCd_cds_origine( uoScrivania.getCd_unita_padre());

		// Imposto la data di registrazione con time zero
		/*
		java.util.Calendar gc = java.util.Calendar.getInstance();
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		accertamento.setDt_registrazione(new java.sql.Timestamp(gc.getTime().getTime()));
		*/
		accertamento.setDt_registrazione(DateServices.getDt_valida(aUC));

		// Verifico che l'esercizio del Cds sia stato aperto
		if (!accertamentoHome.verificaStatoEsercizio(accertamento))
			throw handleException( new ApplicationException( "Non e' possibile creare accertamenti: esercizio del Cds non ancora aperto!") );


		// Carico i Cdr con Codice Unita' Organizzativa uguale al Cd_uo_origine
		// dell'accertamento
		accertamento.getCdrColl().addAll( accertamentoHome.findCdr( accertamento ));
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}

	return accertamento;
}
/**
  *  Inizializzazione di un accertamento
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento e' stata generata
  *    PostCondition:
  *      L'accertamento e' stato inizializzato, tutte le sue scadenze ed i loro dettagli sono stati inizializzati; le
  *      linee di attività eleggibili sono state caricate
  *
  *  Inizializzazione di un accertamento di sistema
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento di sistema e' stata generata
  *    PostCondition:
  *      L'accertamento e' stato inizializzato (metodo inizializzaAccertamentoCdsPerModifica)
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoBulk o AccertamentoCdsBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoBulk o AccertamentoCdsBulk inizializzata
  *
 */

public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		if ( bulk instanceof AccertamentoCdsBulk )
			return inizializzaAccertamentoCdsPerModifica( aUC, bulk );

		AccertamentoBulk accertamento = (AccertamentoBulk) super.inizializzaBulkPerModifica( aUC, bulk);

/* caricamento scadenze originario
		AccertamentoHome accertHome = (AccertamentoHome) getHome( aUC, accertamento.getClass());

		// Carico i Cdr con Codice Unita' Organizzativa uguale al Codice Unita Organizzativa dell'accertamento
		accertamento.getCdrColl().addAll( accertHome.findCdr( accertamento ));

		// Carico le scadenze
		accertamento.setAccertamento_scadenzarioColl( new BulkList( accertHome.findAccertamento_scadenzarioList( accertamento ) ));
		for (Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext();)
		{
			Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) i.next();
			initializeKeysAndOptionsInto( aUC, scadenza );
			scadenza.setStatus( Accertamento_scadenzarioBulk.STATUS_CONFIRMED);
			scadenza.setAccertamento(accertamento);
			inizializzaScadenzaConDocumenti(aUC, scadenza);
		}

		// Lettura dei dettagli delle singole scadenze
		Accertamento_scad_voceHome dettaglioHome = (Accertamento_scad_voceHome) getHome( aUC, Accertamento_scad_voceBulk.class );
		BulkList dettagliScadenze = new BulkList();
		dettagliScadenze =  new BulkList( dettaglioHome.findDettagli_scadenze( accertamento ));
		String cdLA = ((Accertamento_scad_voceBulk)dettagliScadenze.get(0)).getCd_linea_attivita();
		String cdCdr = ((Accertamento_scad_voceBulk)dettagliScadenze.get(0)).getCd_centro_responsabilita();
*/

		// carica lo scadenzario e i suoi dettagli
		AccertamentoHome accertHome = (AccertamentoHome) getHome( aUC, accertamento.getClass());
		Accertamento_scadenzarioHome osHome = (Accertamento_scadenzarioHome) getHome( aUC, Accertamento_scadenzarioBulk.class );

		accertamento.setPdgVincoliColl( new BulkList( accertHome.findPdgVincoloList( accertamento ) ));
		accertamento.setAccertamentoVincoliPerentiColl( new BulkList( accertHome.findAccertamentoVincoloPerenteList( accertamento ) ));
		if (accertamento instanceof AccertamentoResiduoBulk)
			((AccertamentoResiduoBulk)bulk).setIm_quota_inesigibile(((AccertamentoResiduoBulk)bulk).getIm_quota_inesigibile_ripartita());

		accertamento.setAccertamentiPluriennali(new BulkList(accertHome.findAccertamentiPluriennali(aUC,accertamento)));
		accertamento.setAccertamento_scadenzarioColl( new BulkList( accertHome.findAccertamento_scadenzarioList( accertamento ) ));


		for ( Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
		{
			Accertamento_scadenzarioBulk os = (Accertamento_scadenzarioBulk) i.next();
			initializeKeysAndOptionsInto( aUC, os );
			os.setAccertamento( accertamento );
			os.setStatus( os.STATUS_CONFIRMED);
			os.setAccertamento_scad_voceColl( new BulkList( osHome.findAccertamento_scad_voceList(aUC, os )));

			//per ogni scadenza carico l'eventuale doc.attivo
			V_doc_attivo_accertamentoBulk docPassivo = osHome.findDoc_attivo( os );
			if ( docPassivo != null)
			{
				os.setEsercizio_doc_attivo( docPassivo.getEsercizio());
				os.setPg_doc_attivo( docPassivo.getPg_documento_amm());
				os.setCd_tipo_documento_amm(docPassivo.getCd_tipo_documento_amm());
			}

			//per ogni scadenza carico l'eventuale reversale
			Reversale_rigaBulk reversale = osHome.findReversale( os );
			if ( reversale != null )
			{
				os.setEsercizio_reversale( reversale.getEsercizio());
				os.setPg_reversale( reversale.getPg_reversale());
				if (accertamento.getIm_reversali()==null)
					accertamento.setIm_reversali(new BigDecimal(0));
				accertamento.setIm_reversali( accertamento.getIm_reversali().add( reversale.getIm_reversale_riga()));
			}
			// per ogni dettaglio imposto la percentuale
			for ( Iterator j = os.getAccertamento_scad_voceColl().iterator(); j.hasNext(); )
			{
				Accertamento_scad_voceBulk osv = (Accertamento_scad_voceBulk)j.next();
				osv.setAccertamento_scadenzario( os );
				if ( os.getIm_scadenza().doubleValue() != 0 )
					osv.setPrc( (osv.getIm_voce().multiply( new BigDecimal(100)).divide( os.getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP)));
				else
					osv.setPrc( new BigDecimal(0))	;
			}
		}

		// carica i capitoli di spesa del CDS
		accertamento = listaCapitoliPerCdsVoce( aUC, accertamento );
		accertamento.refreshCapitoliDiEntrataCdsSelezionatiColl();

		// MITODO - verificare se serve
		// Inizializzazione delle lista di tutte le linee di attivita eleggibili e di quella selezionata
		// per l'accertamento
		//accertamento = (AccertamentoBulk) inizializzoLineeAttivita(aUC, accertamento, cdLA, cdCdr);

		// MITODO - verificare se serve
		// Inizializzo le scadenze con i relativi dettagli
		// Inizializzo i dettagli con le relative scadenze e la linea di attivita
		//accertamento = (AccertamentoBulk) fineInizializzazioneScadenzeEDettagli(accertamento, dettagliScadenze, null);


		// carica i cdr
		accertamento.setCdrColl( listaCdrPerCapitoli( aUC,  accertamento));
		accertamento.refreshCdrSelezionatiColl();

		// carica le linee di attività da PDG
		accertamento.setLineeAttivitaColl( listaLineeAttivitaPerCapitoliCdr( aUC,  accertamento));
		accertamento.refreshLineeAttivitaSelezionateColl();

		// carica le nuove linee di attività
		accertamento = accertHome.refreshNuoveLineeAttivitaColl( aUC, accertamento );

		accertamento.setInternalStatus( ObbligazioneBulk.INT_STATO_LATT_CONFERMATE );
		accertamento.setIm_iniziale_accertamento( accertamento.getIm_accertamento());
		// MITODO a che serve questa istruzione???
		//accertamento.setCd_iniziale_elemento_voce( accertamento.getCd_elemento_voce());
		accertamento.setCd_terzo_iniziale( accertamento.getCd_terzo());

		// MITODO serve questa ??? sembra che serva per calcolare la massa spendibile legata all'obbligazione, ha senso per l'accertamento???
		//accertamento = calcolaDispCassaPerCds( aUC, accertamento );

		// SETTO IL FLAG CHE SERVE PER CAPIRE SE OCCORRE RICHIEDERE L'INSERIMENTO DELLA VOCE NUOVA DA UTILIZZARE PER IL RIBALTAMENTO
		// LA VOCE VIENE RICHIESTA SOLO SE NON PRESENTE L'ASSOCIAZIONE NELLA TABELLA ASS_EVOLD_EVNEWBULK
		accertamento.setEnableVoceNext(!existAssElementoVoceNew(aUC,(AccertamentoBulk)accertamento));

		return accertamento;
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/**
  *  Inizializzazione di un accertamento per ricerca - cds non ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è diversa dall'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato con il cds e l'uo di origine uguali a quelli di scrivania
  *
  *  Inizializzazione di un accertamento per ricerca - cds ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è l'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato e il cds e l'uo di origine non vengono valorizzati
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoBulk inizializzata
  *
 */

public OggettoBulk inizializzaBulkPerRicerca (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
		//accertamento.setCds( (CdsBulk) getHome( userContext, CdsBulk.class).findByPrimaryKey( new CdsBulk(((CNRUserContext) userContext).getCd_cds())));
		//imposto cds e uo origine
		if ( !uoEnte.getCd_unita_organizzativa().equals(((CNRUserContext)userContext).getCd_unita_organizzativa()))
		{
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome( userContext, Unita_organizzativaBulk.class ).findByPrimaryKey( new Unita_organizzativaBulk( ((CNRUserContext)userContext).getCd_unita_organizzativa()));
			accertamento.setCd_uo_origine( uoScrivania.getCd_unita_organizzativa());
			accertamento.setCd_cds_origine( uoScrivania.getCd_unita_padre());
		}
		return accertamento;
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}
/**
  *  Inizializzazione di un accertamento per ricerca - cds non ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è diversa dall'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato con il cds e l'uo di origine uguali a quelli di scrivania
  *
  *  Inizializzazione di un accertamento per ricerca - cds ente
  *    PreCondition:
  *      La richiesta di inizializzare un accertamento per ricerca e' stata generata
  *      L'uo di scrivania è l'ente
  *    PostCondition:
  *      L'accertamento e' stato inizializzato e il cds e l'uo di origine non vengono valorizzati
  *
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'istanza di AccertamentoBulk da inizializzare
  * @return <code>OggettoBulk</code> l'istanza di AccertamentoBulk inizializzata
  *
 */

public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	return inizializzaBulkPerRicerca( userContext, bulk );
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_registro_accertamentiBulk stampa) throws it.cnr.jada.comp.ComponentException {

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
	stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));
	//stampa.setUoForPrint(new Unita_organizzativaBulk());

	stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(getDataOdierna(userContext));
	stampa.setPgInizio(new Long(0));
	stampa.setPgFine(new Long("9999999999"));

	stampa.setCd_tipo_documento_cont(stampa.TIPO_TUTTI);
	stampa.setRiporto("N");

	try{

		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

		if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
			stampa.setCds_origine(new CdsBulk());
			stampa.setIsCdsForPrintEnabled(true);
			stampa.setUo_cds_origine(new Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		} else {
			stampa.setCds_origine(cds_scrivania);
			stampa.setIsCdsForPrintEnabled(false);

			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

			if (!uo.isUoCds()){
				stampa.setUo_cds_origine(uo);
				stampa.setIsUOForPrintEnabled(false);
			} else {
				stampa.setUo_cds_origine(new Unita_organizzativaBulk());
				stampa.setIsUOForPrintEnabled(true);
			}

		}


	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_registro_annotazione_entrate_pgiroBulk stampa) throws it.cnr.jada.comp.ComponentException {

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
	//stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

	//stampa.setCdsOrigineForPrint(new CdsBulk());
	//stampa.setUoForPrint(new Unita_organizzativaBulk());

	stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(getDataOdierna(userContext));
	stampa.setPgInizio(new Integer(0));
	stampa.setPgFine(new Integer(999999999));

	try{
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

		if (stampa.isStampa_cnr()){
			if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
				stampa.setCdsOrigineForPrint(new CdsBulk());
				stampa.setIsCdsForPrintEnabled(true);

				//stampa.setUoForPrint(new Unita_organizzativaBulk());
				//stampa.setIsUOForPrintEnabled(true);
			} else {
				stampa.setCdsOrigineForPrint(cds_scrivania);
				stampa.setIsCdsForPrintEnabled(false);
			}
		} else {
			stampa.setCdsOrigineForPrint(cds_scrivania);
		}

		if (!uo.isUoCds()){
			stampa.setUoForPrint(uo);
			stampa.setIsUOForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		}

	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}

}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_scadenzario_accertamentiBulk stampa) throws it.cnr.jada.comp.ComponentException {

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
	stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));
	//stampa.setUoForPrint(new Unita_organizzativaBulk());

	stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));

	stampa.setCd_tipo_documento_cont(stampa.TIPO_TUTTI);
	stampa.setRiporto("N");

	try{

		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

		if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
			stampa.setCdsOrigineForPrint(new CdsBulk());
			stampa.setIsCdsForPrintEnabled(true);

			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		} else {
			stampa.setCdsOrigineForPrint(cds_scrivania);
			stampa.setIsCdsForPrintEnabled(false);

			Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

			if (!uo.isUoCds()){
				stampa.setUoForPrint(uo);
				stampa.setIsUOForPrintEnabled(false);
			} else {
				stampa.setUoForPrint(new Unita_organizzativaBulk());
				stampa.setIsUOForPrintEnabled(true);
			}

		}


	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try{
		EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class).findAll().get(0);
		 ((AccertamentoBulk)bulk).setCdsEnte(ente);
		if (bulk instanceof Stampa_registro_accertamentiBulk)
			inizializzaBulkPerStampa(userContext, (Stampa_registro_accertamentiBulk)bulk);
		else if (bulk instanceof Stampa_registro_annotazione_entrate_pgiroBulk)
			inizializzaBulkPerStampa(userContext, (Stampa_registro_annotazione_entrate_pgiroBulk)bulk);
		else if (bulk instanceof Stampa_scadenzario_accertamentiBulk)
			inizializzaBulkPerStampa(userContext, (Stampa_scadenzario_accertamentiBulk)bulk);
		return bulk;
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	}
}
/**
  *  Inizializzazione di una scadenza
  *    PreCondition:
  *      La richiesta di inzializzazione di una scadenza e' stata generata
  *    PostCondition:
  *      La scadenza e' stata inizializzata con i dati relativi all'eventuale doc. amministrativo attivo e all'eventuale
  *      reversale collegati alla scadenza stessa
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param scadenzaAccertamento <code>Accertamento_scadenzarioBulk</code> da inizializzare
  *
 */


private void inizializzaScadenzaConDocumenti(UserContext aUC,OggettoBulk scadenzaAccertamento) throws ComponentException
{
	try
	{
		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) scadenzaAccertamento;
		Accertamento_scadenzarioHome scadenzaHome = (Accertamento_scadenzarioHome) getHome( aUC, scadenza.getClass());

		// Carico nella scadenza l'eventuale documento attivo (fattuta attiva o generico di entrata)
		V_doc_attivo_accertamentoBulk docAttivo = scadenzaHome.findDoc_attivo( scadenza );
		if ( docAttivo != null)
		{
			scadenza.setCd_tipo_documento_amm(docAttivo.getCd_tipo_documento_amm());
			scadenza.setEsercizio_doc_attivo( docAttivo.getEsercizio());
			scadenza.setPg_doc_attivo( docAttivo.getPg_documento_amm());
		}

		// Per ogni scadenza carico l'eventuale Reversale

		Reversale_rigaBulk reversale = scadenzaHome.findReversale( scadenza );
		if ( reversale != null )
		{
			scadenza.setCd_tipo_documento_amm(reversale.getCd_tipo_documento_amm());
			scadenza.setEsercizio_reversale( reversale.getEsercizio());
			scadenza.setPg_reversale( reversale.getPg_reversale());
//			scadenza.getAccertamento().setIm_reversali( scadenza.getAccertamento().getIm_reversali().add( reversale.getIm_reversale_riga()));
		}


		return;
	}
	catch( Exception e )
	{
		throw handleException( e );
	}
}

public Vector listaCodiciNaturaPerCapitolo (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	try
	{
		Vector codiciNatura = new Vector();
		AccertamentoHome accertamentoHome = (AccertamentoHome) getHome( aUC, accertamento );
		codiciNatura.addAll( accertamentoHome.findCodiciNatura( accertamento ));
		return codiciNatura;
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}

}
/* normale
 *		PreCondition :
 *			Una richiesta di estrazione delle linee di attività valide per un accertamento e' stata generata
 *		PostCondition :
 *			L'applicazione ritorna l'elenco delle linee di attività di entrata con cdr appartenente all'unità organizzativa di
 *          scrivania e con natura compatibile con il capitolo selezionato per l'accertamento
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
 * @param accertamento l'istanza di AccertamentoBulk per cui elencare le linee di attività valida
 * @return <code>Vector</code> con le istanze di Linea_attivitaBulk
 *
*/
public Vector listaLineeAttivitaPerCapitolo (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	try
	{
		Vector lineeAttivita = new Vector();
		AccertamentoHome accertamentoHome = (AccertamentoHome) getHome( aUC, accertamento );
		List cdrColl = (List) accertamento.getCdrSelezionatiColl();
		List capitoliColl = (List) accertamento.getCapitoliDiEntrataCdsSelezionatiColl();

		lineeAttivita.addAll( accertamentoHome.findLineeAttivita( cdrColl, capitoliColl, accertamento ));
		return lineeAttivita;
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}

}
/**
  *  Lock scadenza
  *		PreCondition:
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un accertamento
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param scadenza l'istanza di Accertamento_scadenzarioBulk per cui mettere un lock
  *
 */

public void lockScadenza( UserContext userContext,IScadenzaDocumentoContabileBulk scadenza) throws ComponentException
{
	try
	{
		getHome( userContext, scadenza.getClass()).lock( (OggettoBulk)scadenza );
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}
}
/**
  *  Tutti i controlli superati - contesto non transazionale
  *    PreCondition:
  *      Una richiesta di modifica di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento'
  *      L'accertamento non e' stato creato in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene modificato
  *      Il saldo del capitolo dell'accertamento viene aggiornato (metodo 'aggiornaCapitoloSaldoAccertamento')
  *      Lo stato COAN/COGE degli eventuali doc. amministrativi collegati alle scadenza dell'accertamento viene
  *      aggiornato (metodo 'aggiornaStatoCOAN_COGEDocAmm')
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di modifica di un accertamento e' stata generata
  *      L'accertamento ha superato i controlli eseguiti dal metodo 'verificaAccertamento'
  *      L'accertamento e' stato modifica in un contesto transazionale
  *    PostCondition:
  *      L'accertamento viene modificato
  *  Errore di verifica accertamento
  *    PreCondition:
  *      Una richiesta di modifica di un accertamento e' stata generata e l'accertamento non ha superato i
  *      controlli eseguiti dal metodo 'verificaAccertamento'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>AccertamentoBulk</code> l'accertamento da modificare
  * @return <code>AccertamentoBulk</code> l'accertamento  modificato
  *
 */
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	generaDettagliScadenzaAccertamento( aUC, (AccertamentoBulk) bulk, null );
	verificaAccertamento( aUC, (AccertamentoBulk) bulk );
	validaAccertamentoPluriennale(aUC, (AccertamentoBulk) bulk);

	bulk =  super.modificaConBulk( aUC, bulk );

	verificaCoperturaContratto( aUC, (AccertamentoBulk) bulk );
	verificaCoerenzaGaeContratto( aUC, (AccertamentoBulk) bulk );

	if ( !aUC.isTransactional() )
   {
  		aggiornaCapitoloSaldoAccertamento( aUC, (AccertamentoBulk) bulk, MODIFICA );
		aggiornaStatoCOAN_COGEDocAmm( aUC, (AccertamentoBulk) bulk );
   }
   AccertamentoBulk accertamento = (AccertamentoBulk) bulk;
	if (accertamento.isAccertamentoResiduo()) {
		if (((AccertamentoResiduoBulk)accertamento).isSaldiDaAggiornare()) {
			// aggiorniamo i saldi legati alle modifiche agli acc. residui
			aggiornaSaldiAccertamentiResiduiPropri(aUC,accertamento);
			// aggiorniamo il progressivo in definitivo
			Accertamento_modificaBulk acrMod = ((AccertamentoResiduoBulk) accertamento).getAccertamento_modifica();
			if (acrMod!=null && acrMod.isTemporaneo()) {
				aggiornaAccertamentoModificaTemporanea(aUC, acrMod);
				if (acrMod.getVariazione()!=null)
					approvaVarStanzRes(aUC, acrMod.getVariazione());
			}
			annullaRigheDocAmm(aUC,accertamento);
		}
		verificaDisponibilitaVincoliSpese(aUC, (AccertamentoResiduoBulk)accertamento);
	}

	try
	{

		// Verifico che l'esercizio del CDS sia stato aperto
		if (! ((AccertamentoHome)getHome(aUC, AccertamentoBulk.class)).verificaStatoEsercizio((AccertamentoBulk) bulk))
			throw handleException( new ApplicationException( "Non e' possibile salvare accertamenti: esercizio del Cds non aperto!") );

		accertamento = validaCreaModificaElementoVoceNext(aUC, accertamento);
	}
	catch (Exception e )
	{
		throw handleException( e );
	}

	return bulk;

}
/*
 * Modifica l'importo di una scadenza e aggiunge la differenza alla scadenza successiva oppure modifica l'importo di una
 * scadenza e l'importo della testata dell'accertamento
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica Scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza
 * Post: L'importo della scadenza e della testata dell'accertamento sono stati modificati dal metodo 'modificaScadenzaNonInAutomatico'
 *
 * Nome: Modifica Scadenza successiva
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la differenza fra il nuovo importo
 *       e l'importo precedente deve essere riportato sulla scadenza successiva
 * Post: L'importo della scadenza e della scadenza successiva sono stati modificati
 *
 * Nome: Scadenza con più di 1 dettaglio - Errore
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la scadenza ha più di un dettaglio
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * Nome: Scadenza successiva - Errore ultima scadenza
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e non esiste una scadenza
 *       successiva su cui scaricare la differenza fra l'importo attuale scadenza e il nuovo importo
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * Nome: Scadenza successiva -  Errore importo scadenza successiva
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e (im_scadenza_successisva -
 *       nuovo_im_scadenza + im_scadenza) e' minore di 0
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * Nome: Scadenza successiva -  Errore doc amministrativi associati
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la scadenza successiva ha
 *       già dei documenti amministrativi associati
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad l'istanza di Accertamento_scadenzarioBulk il cui importo deve essere modificato
 * @param nuovoImporto il valore del nuovo importo che la scadenza di accertamento dovrà assumere
 * @param modificaScadenzaSuccessiva il flag che indica se modificare la testata dell'accertamento o modificare la scadenza
 *        successiva dell'accertamento
 * @return l'istanza di Accertamento_scadenzarioBulk con l'importo modificato
 */


public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext, IScadenzaDocumentoContabileBulk scad, java.math.BigDecimal nuovoImporto, boolean modificaScadenzaSuccessiva) throws ComponentException
{
	return modificaScadenzaInAutomatico(userContext, scad, nuovoImporto, modificaScadenzaSuccessiva,false);
}
public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext, IScadenzaDocumentoContabileBulk scad, java.math.BigDecimal nuovoImporto, boolean modificaScadenzaSuccessiva, Boolean aggiornaCalcoloAutomatico) throws ComponentException
{
	Accertamento_scadenzarioBulk scadenzaDaFattura = (Accertamento_scadenzarioBulk)scad;
	if (  nuovoImporto.compareTo( scad.getIm_scadenza()) == 0  )
		throw handleException( new ApplicationException( "Aggiornamento in automatico non necessario!" ));
	if (  nuovoImporto.compareTo( new BigDecimal(0)) < 0  )
		throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));
	try
	{
		// Inizializzo l'accertamento con la linea di attivita', le sue scadenze e i suoi dettagli
		AccertamentoBulk accertamento = (AccertamentoBulk) inizializzaBulkPerModifica(userContext, scadenzaDaFattura.getAccertamento());
        scadenzaDaFattura = accertamento.getAccertamento_scadenzarioColl().stream()
                    .filter(accertamentoScadenzario -> accertamentoScadenzario.equalsByPrimaryKey(scad))
                    .findAny()
                    .orElse(scadenzaDaFattura);
        if (aggiornaCalcoloAutomatico){
        	accertamento.setFl_calcolo_automatico(true);
        	accertamento.setToBeUpdated();
        }
        
		if ( accertamento.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_RES) &&
				!modificaScadenzaSuccessiva)
			throw handleException( new ApplicationException( "Non è consentita la modifica dell'importo di testata di un accertamento residuo." ));

		// Se la scadenza (e quindi tutte le scadenze) hanno piu' di un dettaglio, non consento l'aggiornamento
		// automatico.
		// (Infatti nel caso di piu' dettagli l'utente avrebbe potuto suddividere l'importo sugli 'n'
		// dettagli)
		if(	nuovoImporto.compareTo(BigDecimal.ZERO)!=0 && scadenzaDaFattura.getAccertamento_scad_voceColl() != null &&
			scadenzaDaFattura.getAccertamento_scad_voceColl().size() > 1)
			throw new ApplicationException( "Impossibile aggiornare automaticamente la scadenza perche' i suoi dettagli sono stati imputati manualmente !");

		// aggiorno solo l'importo della scadenza (+ dettagli) modificata dalla
		// fattura e l'importo del relativo accertamento
		if(!modificaScadenzaSuccessiva)
			return modificaScadenzaNonInAutomatico(userContext, scadenzaDaFattura, nuovoImporto, aggiornaCalcoloAutomatico);
		else
		{
			scadenzaDaFattura.setFl_aggiorna_scad_successiva( new Boolean( true) );

			// Salvo i dati iniziali
			Accertamento_scadenzarioBulk scadenzaInizialeDaFattura = new Accertamento_scadenzarioBulk();
			scadenzaInizialeDaFattura.setIm_scadenza( scadenzaDaFattura.getIm_scadenza());
			scadenzaDaFattura.setScadenza_iniziale( scadenzaInizialeDaFattura );

			//imposto il nuovo importo
			scadenzaDaFattura.setIm_scadenza(nuovoImporto);
			scadenzaDaFattura.setToBeUpdated();

			// aggiorno l'importo della scadenza successiva ed i suoi dettagli
			accertamento = aggiornaScadenzarioSuccessivoAccertamento (userContext, scadenzaDaFattura);

			// all modifica di una scadenza rigenero i relativi dettagli
			generaDettagliScadenzaAccertamento(userContext, accertamento, scadenzaDaFattura);

			scadenzaDaFattura.setAccertamento((AccertamentoBulk)modificaConBulk(userContext, accertamento));
			return scadenzaDaFattura;
		}
	}
	catch ( Exception e )
	{
		throw handleException( scadenzaDaFattura, e  );
	}
}
/*
 * Pre-post-conditions:
 *
 * Nome: Sdoppiamento scadenza in automatico
 *
 * Pre:  E' stata generata la richiesta di sdoppiare in automatico l'importo di una scadenza, aggiornando
 * 	     l'importo della prima scadenza e scaricando al differenza su una nuova scadenza
 * Post: L'importo della scadenza viene aggiornato al nuovo valore e i suoi dettagli rigenerati
 * 		 Viene creata una nuova scadenza su cui vengono scaricati gli importi sottratti alla scadenza precedente
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad l'istanza di Accertamento_scadenzarioBulk il cui importo deve essere sdoppiato
 * @param nuovoImportoScadenzaVecchia il valore del nuovo importo che la scadenza indicata dovrà assumere
 * @return l'istanza di Accertamento_scadenzarioBulk nuova creata in seguito allo sdoppiamento
 */
public IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImportoScadenzaVecchia) throws ComponentException
{
	Accertamento_scadenzarioBulk scadenzaVecchia = (Accertamento_scadenzarioBulk)scad;
	if (  nuovoImportoScadenzaVecchia.compareTo( scad.getIm_scadenza()) == 0  && (nuovoImportoScadenzaVecchia.compareTo(Utility.ZERO)!=0))
		throw handleException( new ApplicationException( "Sdoppiamento in automatico non necessario!" ));
	if (  nuovoImportoScadenzaVecchia.compareTo( new BigDecimal(0)) < 0  )
		throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));
	if (  nuovoImportoScadenzaVecchia.compareTo( scad.getIm_scadenza()) == 1 )
		throw new ApplicationException("L'importo nuovo da assegnare alla scadenza dell'accertamento deve essere inferiore al valore originario!");

	try {
		java.math.BigDecimal vecchioImportoScadenzaVecchia = scadenzaVecchia.getIm_scadenza();
		java.math.BigDecimal importoScadenzaNuova = vecchioImportoScadenzaVecchia.subtract(nuovoImportoScadenzaVecchia);

		BigDecimal newImportoAsv = Utility.ZERO, totImporto = Utility.ZERO;
		AccertamentoHome accertamentoHome = (AccertamentoHome) getHome( userContext, AccertamentoBulk.class );
		AccertamentoBulk accertamento = (AccertamentoBulk)accertamentoHome.findByPrimaryKey(scadenzaVecchia.getAccertamento());
		accertamento = (AccertamentoBulk)inizializzaBulkPerModifica(userContext, (OggettoBulk)accertamento);

		//cerco nell'accertamento riletto la scadenza indicata
		for (Iterator s = accertamento.getAccertamento_scadenzarioColl().iterator(); s.hasNext(); ) {
			Accertamento_scadenzarioBulk as = (Accertamento_scadenzarioBulk)s.next();
			if (as.equalsByPrimaryKey(scadenzaVecchia)) {
				scadenzaVecchia = as;
				break;
			}
		}

		if (scadenzaVecchia == null)
			throw new ApplicationException("Scadenza da sdoppiare non trovata nell'accertamento indicato!");

		Accertamento_scadenzarioBulk scadenzaNuova = new Accertamento_scadenzarioBulk();
		accertamento.addToAccertamento_scadenzarioColl(scadenzaNuova);
		scadenzaNuova.setDt_scadenza_incasso(scadenzaVecchia.getDt_scadenza_incasso());
		scadenzaNuova.setDs_scadenza(scadenzaVecchia.getDs_scadenza());

		// Rigenero i relativi dettagli
		generaDettagliScadenzaAccertamento(userContext, accertamento, scadenzaNuova, false);
	if(vecchioImportoScadenzaVecchia.compareTo(Utility.ZERO)!=0){
		for (Iterator s = scadenzaVecchia.getAccertamento_scad_voceColl().iterator(); s.hasNext(); ) {
			Accertamento_scad_voceBulk asvOld = (Accertamento_scad_voceBulk)s.next();
			newImportoAsv = nuovoImportoScadenzaVecchia.multiply(asvOld.getIm_voce()).divide(vecchioImportoScadenzaVecchia, 2, BigDecimal.ROUND_HALF_UP);

			for (Iterator n = scadenzaNuova.getAccertamento_scad_voceColl().iterator(); n.hasNext(); ) {
				Accertamento_scad_voceBulk asvNew = (Accertamento_scad_voceBulk)n.next();
				if (asvNew.getCd_centro_responsabilita().equals(asvOld.getCd_centro_responsabilita()) &&
				    asvNew.getCd_linea_attivita().equals(asvOld.getCd_linea_attivita()) &&
					asvNew.getCd_voce().equals(asvOld.getCd_voce()))
					asvNew.setIm_voce(asvOld.getIm_voce().subtract(newImportoAsv));
			}

			asvOld.setIm_voce(newImportoAsv);
			asvOld.setToBeUpdated();
		}
	}
		//Quadro la sommatoria sulla vecchia scadenza
		for (Iterator s = scadenzaVecchia.getAccertamento_scad_voceColl().iterator(); s.hasNext(); )
			totImporto = totImporto.add(((Accertamento_scad_voceBulk)s.next()).getIm_voce());

		if (totImporto.compareTo(nuovoImportoScadenzaVecchia)!=0) {
			//recupero il primo dettaglio e lo aggiorno per quadrare
			for (Iterator s = scadenzaVecchia.getAccertamento_scad_voceColl().iterator(); s.hasNext(); ) {
				Accertamento_scad_voceBulk asv = (Accertamento_scad_voceBulk)s.next();
				if (asv.getIm_voce().add(nuovoImportoScadenzaVecchia.subtract(totImporto)).compareTo(Utility.ZERO)!=-1) {
					asv.setIm_voce(asv.getIm_voce().add(nuovoImportoScadenzaVecchia.subtract(totImporto)));
					break;
				}
			}
		}

		totImporto = Utility.ZERO;

		//Quadro la sommatoria sulla nuova scadenza
		for (Iterator s = scadenzaNuova.getAccertamento_scad_voceColl().iterator(); s.hasNext(); )
			totImporto = totImporto.add(((Accertamento_scad_voceBulk)s.next()).getIm_voce());

		if (totImporto.compareTo(importoScadenzaNuova)!=0) {
			//recupero il primo dettaglio e lo aggiorno per quadrare
			for (Iterator s = scadenzaNuova.getAccertamento_scad_voceColl().iterator(); s.hasNext(); ) {
				Accertamento_scad_voceBulk asv = (Accertamento_scad_voceBulk)s.next();
				if (asv.getIm_voce().add(importoScadenzaNuova.subtract(totImporto)).compareTo(Utility.ZERO)!=-1) {
					asv.setIm_voce(asv.getIm_voce().add(importoScadenzaNuova.subtract(totImporto)));
					break;
				}
			}
		}

		scadenzaVecchia.setIm_scadenza(nuovoImportoScadenzaVecchia);
		scadenzaVecchia.setToBeUpdated();
		scadenzaNuova.setIm_scadenza(importoScadenzaNuova);
		scadenzaNuova.setToBeCreated();
		accertamento.setToBeUpdated();
		/**
		 * Viene posto il campo che verifica se il controllo della disponibilità
		 * è stato effettuato a TRUE, in quanto, l'accertamento è in modifica
		 * e lo sdoppiamento delle righe non cambia il valore complessivo
		 * dell'accertamento
		 */
		accertamento.setCheckDisponibilitaContrattoEseguito(true);
		modificaConBulk(userContext, accertamento);
		return scadenzaNuova;
	} catch (PersistencyException e) {
		throw handleException( e );
	}
}
/*
 * Pre-post-conditions:
 *
 * Nome: Modifica in automatico
 * Pre:  E' stata generata la richiesta di modifica in automatico dell'importo di una scadenza e dell'importo di testata
 *       dell'accertamneto
 * Post: L'importo della scadenza viene aggiornato al nuovo valore e i suoi dettagli rigenerati
 *       L'importo della testata dell'accertamento viene aggiornato nel modo seguente:
 *     		im_nuovo_testata = im_precedente_testata - im_precedente_scadenza + im_nuovo_scadenza
 *
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad l'istanza di Accertamento_scadenzarioBulk il cui importo deve essere modificato
 * @param nuovoImporto il valore del nuovo importo che la scadenza di accertamento dovrà assumere
 * @return l'istanza di Accertamento_scadenzarioBulk con l'importo modificato
 */


private Accertamento_scadenzarioBulk modificaScadenzaNonInAutomatico( UserContext userContext, Accertamento_scadenzarioBulk scadenza, java.math.BigDecimal nuovoImporto, Boolean aggiornaCalcoloAutomatico) throws ComponentException
{
	// aggiorno importo accertamento : importo accertamento = importo accertamento - importo vecchia scadenza + importo nuovo scadenza
	java.math.BigDecimal vecchioImportoAccertamento = scadenza.getAccertamento().getIm_accertamento();
	java.math.BigDecimal nuovoImportoAccertamento = (vecchioImportoAccertamento.subtract(scadenza.getIm_scadenza())).add(nuovoImporto);
	scadenza.getAccertamento().setIm_accertamento(nuovoImportoAccertamento);
	scadenza.getAccertamento().setToBeUpdated();

	// aggiorno importo scadenza corrente
	scadenza.setIm_scadenza( nuovoImporto );
	scadenza.setToBeUpdated();

	// aggiorno dettagli scadenza
	// MITODO - remmo per ora
	//modificoDettagliScadenza(userContext, scadenza.getAccertamento(), scadenza);
	if (aggiornaCalcoloAutomatico){
		scadenza.getAccertamento().setCheckDisponibilitaContrattoEseguito(true);
	}
	modificaConBulk(userContext, scadenza.getAccertamento());
	return scadenza;
}
/*
 * Pre-post-conditions:
 *
 * Nome: Linea attività non comune
 * Pre:  E' stata generata la richiesta di modifica di una scadenza
 *       La linea di attività dell'accertamento non e' comune
 * Post: L'importo dell'unico dettaglio della scadenza viene aggiornato con l'importo della scadenza
 *
 * Nome: Linea attività comune
 * Pre:  E' stata generata la richiesta di modifica di una scadenza
 *       La linea di attività dell'accertamento e' comune
 * Post: L'importo del dettaglio della scadenza che corrisponde alla linea di attività dell'accertamneto
 *       viene aggiornato con l'importo della scadenza
 *		 L'importo di tutti gli altri dettagli della scadenza vengono aggiornati a 0
 *
 * @param aUC lo userContext che ha generato la richiesta
 * @param accertamento l'istanza di AccertamentoBulk la cui scadenza e' stata modificata
 * @param scadenza l'istanza di Accertamento_scadenzarioBulk modificata
 */

/* MITODO - remmo per ora, verificare
private void modificoDettagliScadenza(UserContext aUC,AccertamentoBulk accertamento, Accertamento_scadenzarioBulk scadenza) throws ComponentException
{
	BulkList dettagliScadenze =	scadenza.getAccertamento_scad_voceColl();
	Accertamento_scad_voceBulk dettaglio;
	try
	{
		//	Linea attività non comune
		if ( !accertamento.getLinea_attivita().getCd_linea_attivita().startsWith( "C" ))
		{
			dettaglio = (Accertamento_scad_voceBulk) dettagliScadenze.get(0);
			dettaglio.setAccertamento_scadenzario( scadenza );
			dettaglio.setIm_voce( scadenza.getIm_scadenza());
			dettaglio.setToBeUpdated();
		}
		// Linea di attivita' comune
    	else
    	{
	 	   	for ( Iterator i = dettagliScadenze.iterator(); i.hasNext(); )
		    {
				dettaglio = (Accertamento_scad_voceBulk) i.next();
				dettaglio.setAccertamento_scadenzario( scadenza );

				if ( accertamento.getLinea_attivita().getCd_centro_responsabilita().equals( dettaglio.getCd_centro_responsabilita()))
					dettaglio.setIm_voce( scadenza.getIm_scadenza());
				else
					dettaglio.setIm_voce( new java.math.BigDecimal(0) );

				dettaglio.setToBeUpdated();
		    }
	    }
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
*/
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUo_cds_origineByClause(UserContext userContext, Stampa_registro_accertamentiBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCds_origine().getCd_proprio_unita());
	sql.addClause(clauses);
	return sql;

}
/**
 *
 * @param userContext
 * @param accertamento
 * @param contratto
 * @param clauses
 * @return
 * @throws ComponentException
 * @throws it.cnr.jada.persistency.PersistencyException
 */
public SQLBuilder selectContrattoByClause(UserContext userContext, AccertamentoBulk accertamento, ContrattoBulk contratto, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	Parametri_cdsHome paramHome = (Parametri_cdsHome)getHome(userContext, Parametri_cdsBulk.class);
	Parametri_cdsBulk param_cds;
	try {
		param_cds =
			(Parametri_cdsBulk) paramHome.findByPrimaryKey(
				new Parametri_cdsBulk(
					accertamento.getCd_cds(),
					accertamento.getEsercizio()));
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}

	SQLBuilder sql = getHome(userContext,ContrattoBulk.class).createSQLBuilder();
	if (clauses != null)
	  sql.addClause(clauses);
	sql.openParenthesis("AND");
	  sql.addSQLClause("AND","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO);
	  sql.addSQLClause("OR","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO);
	sql.closeParenthesis();
	if(param_cds != null && param_cds.getFl_contratto_cessato().booleanValue()){
		sql.openParenthesis("AND");
		  sql.addSQLClause("AND","STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
		  sql.addSQLClause("OR","STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_CESSSATO);
		sql.closeParenthesis();
	}
	else
	  sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
	// Se uo 999.000 in scrivania: visualizza tutti i contratti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
	  sql.openParenthesis("AND");
		sql.addSQLClause("AND","CONTRATTO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
		SQLBuilder sqlAssUo = getHome(userContext,Ass_contratto_uoBulk.class).createSQLBuilder();
		sqlAssUo.addSQLJoin("CONTRATTO.ESERCIZIO","ASS_CONTRATTO_UO.ESERCIZIO");
		sqlAssUo.addSQLJoin("CONTRATTO.PG_CONTRATTO","ASS_CONTRATTO_UO.PG_CONTRATTO");
		sqlAssUo.addSQLClause("AND","ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
		sql.addSQLExistsClause("OR",sqlAssUo);
	  sql.closeParenthesis();
	}
	sql.addTableToHeader("TERZO");
	sql.addSQLJoin("CONTRATTO.FIG_GIUR_EST", SQLBuilder.EQUALS,"TERZO.CD_TERZO");
	sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);

	if((accertamento.getDebitore() != null && accertamento.getDebitore().getCd_terzo()!=null)){
		sql.addClause("AND", "figura_giuridica_esterna.cd_terzo",SQLBuilder.EQUALS,accertamento.getDebitore().getCd_terzo());
	}

	/*sql.openParenthesis("AND");
	  sql.addSQLClause("AND","TRUNC(NVL(DT_FINE_VALIDITA,SYSDATE)) >= TRUNC(SYSDATE)");
	  sql.addSQLClause("OR","(DT_PROROGA IS NOT NULL AND TRUNC(DT_PROROGA) >= TRUNC(SYSDATE))");
	sql.closeParenthesis();*/
	return sql;
}
/**
 *
 * @param userContext
 * @param accertamento
 * @param contratto
 * @param clauses
 * @return
 * @throws ComponentException
 * @throws it.cnr.jada.persistency.PersistencyException
 */
public void validaContratto(UserContext userContext, AccertamentoBulk accertamento, ContrattoBulk contratto, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	SQLBuilder sql = selectContrattoByClause(userContext, accertamento, contratto, clauses);
	sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS, contratto.getEsercizio());
	sql.addSQLClause("AND","STATO",SQLBuilder.EQUALS, contratto.getStato());
	sql.addSQLClause("AND","PG_CONTRATTO",SQLBuilder.EQUALS, contratto.getPg_contratto());
	ContrattoHome home = (ContrattoHome)getHome(userContext,ContrattoBulk.class);
	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
	if(!broker.next())
	  throw new ApplicationException("Contratto non valido!");
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_registro_accertamentiBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
	sql.addClause(clauses);
	return sql;

}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_registro_annotazione_entrate_pgiroBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCdsOrigineForPrint().getCd_proprio_unita());
	sql.addClause(clauses);
	return sql;
}
/**
  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
  *
  * Nome: Richiesta di ricerca di una Unita Organizzativa
  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
  * Post: Viene restituito l'SQLBuilder per filtrare le UO
  *		  in base al cds di scrivania
  *
  * @param userContext	lo userContext che ha generato la richiesta
  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
  *			della query.
  *
**/
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_scadenzario_accertamentiBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCdsOrigineForPrint().getCd_proprio_unita());
	sql.addClause(clauses);
	return sql;
}
/**
 * stampaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if (bulk instanceof Stampa_registro_accertamentiBulk)
		validateBulkForPrint(aUC, (Stampa_registro_accertamentiBulk)bulk);
	else if (bulk instanceof Stampa_registro_annotazione_entrate_pgiroBulk)
		validateBulkForPrint(aUC, (Stampa_registro_annotazione_entrate_pgiroBulk)bulk);
	else if (bulk instanceof Stampa_scadenzario_accertamentiBulk)
		validateBulkForPrint(aUC, (Stampa_scadenzario_accertamentiBulk)bulk);

	return bulk;
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_registro_accertamentiBulk stampa) throws ComponentException{

	try{
		Timestamp dataOdierna = getDataOdierna(userContext);

		//if (stampa.getEsercizio()==null)
			//throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		//if (stampa.getCd_cds()==null)
			//throw new ValidationException("Il campo CDS e' obbligatorio");

		//if (stampa.getCdUoForPrint()==null)
			//throw new ValidationException("Il campo UNITA ORGANIZZATIVA è obbligatorio");

		/**** Controlli sulle Date DA A	*****/
		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");


		java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
		java.sql.Timestamp lastDayOfYear = DateServices.getLastDayOfYear(stampa.getEsercizio().intValue());

		// La Data di Inizio Periodo è superiore alla data di Fine Periodo
		if (stampa.getDataInizio().compareTo(stampa.getDataFine())>0)
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");

		// La Data di Inizio Periodo è ANTECEDENTE al 1 Gennaio dell'Esercizio di scrivania
		if (stampa.getDataInizio().compareTo(firstDayOfYear)<0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
		}
		// La Data di Fine periodo è SUPERIORE alla data odierna
		if (stampa.getDataFine().compareTo(lastDayOfYear)>0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(lastDayOfYear));
		}

		/**** Controlli sui PG_INIZIO/PG_FINE *****/
		if (stampa.getPgInizio()==null)
			throw new ValidationException("Il campo NUMERO INIZIO è obbligatorio");
		if (stampa.getPgFine()==null)
			throw new ValidationException("Il campo NUMERO FINE è obbligatorio");
		if (stampa.getPgInizio().compareTo(stampa.getPgFine())>0)
			throw new ValidationException("Il NUMERO INIZIO non può essere superiore al NUMERO FINE");

	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_registro_annotazione_entrate_pgiroBulk stampa) throws ComponentException{

	try{
		Timestamp dataOdierna = getDataOdierna(userContext);

		//if (stampa.getEsercizio()==null)
			//throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		//if (stampa.getCd_cds()==null)
			//throw new ValidationException("Il campo CDS e' obbligatorio");

		//try{
			//CDRComponentSession sess = (CDRComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", CDRComponentSession.class);
			//if (sess.isEnte(userContext) && stampa.getCdCdsOrigineForPrint()==null)
				//throw new ValidationException("Il campo CDS di ORIGINE è obbligatorio");
			//if (stampa.getCdUoForPrint()==null)
				//throw new ValidationException("Il campo UNITA ORGANIZZATIVA è obbligatorio");
		//}catch(javax.ejb.EJBException ex){
			//throw handleException(ex);
		//}catch(java.rmi.RemoteException ex){
			//throw handleException(ex);
		//}

		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");

		java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
		java.sql.Timestamp lastDayOfYear = DateServices.getLastDayOfYear(stampa.getEsercizio().intValue());
		if (stampa.getDataInizio().compareTo(stampa.getDataFine())>0)
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
		if (stampa.getDataInizio().compareTo(firstDayOfYear)<0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
		}
		if (stampa.getDataFine().compareTo(lastDayOfYear)>0){
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(lastDayOfYear));
		}

		if (stampa.getPgInizio()==null)
			throw new ValidationException("Il campo NUMERO INIZIO MANDATO è obbligatorio");
		if (stampa.getPgFine()==null)
			throw new ValidationException("Il campo NUMERO FINE MANDATO è obbligatorio");
		if (stampa.getPgInizio().compareTo(stampa.getPgFine())>0)
			throw new ValidationException("Il NUMERO INIZIO MANDATO non può essere superiore al NUMERO FINE MANDATO");

	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_scadenzario_accertamentiBulk stampa) throws ComponentException{

	try{
		Timestamp dataOdierna = getDataOdierna(userContext);
		Timestamp lastDayOfYear = DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());

		//if (stampa.getEsercizio()==null)
			//throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		//if (stampa.getCd_cds()==null)
			//throw new ValidationException("Il campo CDS e' obbligatorio");

		//if (stampa.getCdUoForPrint()==null)
			//throw new ValidationException("Il campo UNITA ORGANIZZATIVA è obbligatorio");

		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");
		if (stampa.getDataInizio().compareTo(stampa.getDataFine())>0)
			throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");

	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
/**
  *  Tutti controlli superati
  *    PreCondition:
  *      L'accertamento ha almeno una scadenza
  *      La somma degli importi delle scadenze e' uguale all'importo dell'accertamento
  *      La somma degli importi dei dettagli di ogni scadenza e' uguale all'importo della scadenza
  *    PostCondition:
  *      L'accertamento supera la validazione ed il sistema può pertanto proseguire con il suo salvataggio
  *
  *  sum(scadenzario.importo) not = accertamento.importo
  *    PreCondition:
  *      La somma degli importi delle scadenze dell'accertamento non è uguale all'importo dell'accertamento in elaborazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'accertamento non è consentito
  *      se l'importo non è uguale alla somma degli importi delle scadenze dell'accertamento.
  *
  *  sum(scad_voce.importo) not = scadenzario.importo
  *    PreCondition:
  *      La somma degli importi
  *      dei dettagli di una scadenza dell' accertamento non è uguale all'importo della scadenza
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'accertamento non è consentito
  *      se l'importo della scadenza non è uguale alla somma degli importi dei dettagli della scadenza dell'accertamento.
  *
  *  scadenze non definite
  *    PreCondition:
  *      Non sono state definite scadenze per l'accertamento
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'accertamento non è consentito
  *      se non viene definita almento una scadenza
  *
  * @param aUC lo user context
  * @param accertamento l'istanza di  <code>AccertamentoBulk</code> da verificare
  *
  *
 */

public void verificaAccertamento (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	Accertamento_scadenzarioBulk scadenza;
	java.math.BigDecimal total = new java.math.BigDecimal(0);
	java.math.BigDecimal totScadenza;
	java.math.BigDecimal importoScadenza = new java.math.BigDecimal(0);

	if ( accertamento.isToBeCreated() )
	{
		Timestamp lastDayOfTheYear = DateServices.getLastDayOfYear( accertamento.getEsercizio().intValue());

		if ( accertamento.getDt_registrazione().before(DateServices.getFirstDayOfYear( accertamento.getEsercizio().intValue())) ||
			  accertamento.getDt_registrazione().after(lastDayOfTheYear))
			throw  new ApplicationException( "La data di registrazione deve appartenere all'esercizio di scrivania" );

		if ( getDataOdierna( aUC).after(lastDayOfTheYear ) &&
			  accertamento.getDt_registrazione().compareTo( lastDayOfTheYear) != 0 )
			throw  new ApplicationException( "La data di registrazione deve essere " +
		   									java.text.DateFormat.getDateInstance().format( lastDayOfTheYear ));
	}
	if((accertamento.getAccertamento_scadenzarioColl() == null) ||
	   (accertamento.getAccertamento_scadenzarioColl().size() == 0))
		throw handleException( new it.cnr.jada.comp.ApplicationException( "L'accertamento non ha alcuna SCADENZA associata !"))	;

	// La somma degli importi delle scadenze dell'accertamento non è uguale all'importo dell'accertamento
	// in elaborazione.

	for ( Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
	{
		scadenza = (Accertamento_scadenzarioBulk) i.next();
		total = total.add(scadenza.getIm_scadenza());
		totScadenza = new java.math.BigDecimal(0);

		if((scadenza.getAccertamento_scad_voceColl() == null) ||
		   (scadenza.getAccertamento_scad_voceColl().size() == 0))
			throw handleException( new it.cnr.jada.comp.ApplicationException( "Esistono scadenze senza DETTAGLI associati !"))	;

		for ( Iterator j = scadenza.getAccertamento_scad_voceColl().iterator(); j.hasNext(); )
		{
			importoScadenza = ((Accertamento_scad_voceBulk) j.next()).getIm_voce();
			if(importoScadenza == null)
				importoScadenza = new java.math.BigDecimal(0);
			totScadenza = totScadenza.add(importoScadenza);
		}
		if ( totScadenza.compareTo( scadenza.getIm_scadenza()) != 0 )
			throw handleException( new it.cnr.jada.comp.ApplicationException( "La somma degli importi dei dettagli di una scadenza differisce dall'importo di scadenza"))	;

	}

	if(total.compareTo(accertamento.getIm_accertamento()) == 1)
		throw handleException( new it.cnr.jada.comp.ApplicationException( "La somma degli importi delle scadenze supera l'importo complessivo dell'accertamento"))	;

	if(total.compareTo(accertamento.getIm_accertamento()) == -1)
		throw handleException( new it.cnr.jada.comp.ApplicationException( "La somma degli importi delle scadenze e' inferiore all'importo complessivo dell'accertamento"))	;
	if(accertamento.getContratto() != null && accertamento.getContratto().getFigura_giuridica_esterna()!= null &&
	   !accertamento.getDebitore().equalsByPrimaryKey(accertamento.getContratto().getFigura_giuridica_esterna()))
	  throw new it.cnr.jada.comp.ApplicationException( "Il Debitore (Codice Terzo:"+accertamento.getDebitore().getCd_terzo()+") \n"+"non è congruente con quello del contratto (Codice Terzo:"+accertamento.getContratto().getFigura_giuridica_esterna().getCd_terzo()+")");

	/*
	 * Controllo l'eventuale obbligatorietà del Contratto
	 */
	Elemento_voceHome home = (Elemento_voceHome)getHome(aUC, Elemento_voceBulk.class);
	Elemento_voceBulk elemento_voce;
	try {
		elemento_voce =
			(Elemento_voceBulk) home.findByPrimaryKey(
				new Elemento_voceBulk(
					accertamento.getCd_elemento_voce(),
					accertamento.getEsercizio(),
					accertamento.getTi_appartenenza(),
					accertamento.getTi_gestione()));
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	if(elemento_voce!=null && elemento_voce.getFl_recon().booleanValue()){
		Parametri_cdsHome paramHome = (Parametri_cdsHome)getHome(aUC, Parametri_cdsBulk.class);
		Parametri_cdsBulk param_cds;
		try {
			param_cds =
				(Parametri_cdsBulk) paramHome.findByPrimaryKey(
					new Parametri_cdsBulk(
						CNRUserContext.getCd_cds(aUC),
			            CNRUserContext.getEsercizio(aUC)));
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		if(param_cds != null && param_cds.getIm_soglia_contratto_e()!= null &&
		   accertamento.getIm_accertamento().compareTo(param_cds.getIm_soglia_contratto_e())!=-1){
		  if(accertamento.getPg_contratto() == null)
		    throw new it.cnr.jada.comp.ApplicationException( "Il campo contratto non può essere nullo. Importo dell'Accertamento superiore al limite stabilito!");
		}

	}
	verificaGestioneTrovato(aUC, accertamento, elemento_voce);
}
private void verificaGestioneTrovato(UserContext aUC,
		AccertamentoBulk accertamento, Elemento_voceBulk elemento_voce)
				throws ComponentException {
//	if (obbligazione.getCd_iniziale_elemento_voce() != null && !elemento_voce.getCd_elemento_voce().equals(obbligazione.getCd_iniziale_elemento_voce())){
	if (accertamento.getPg_accertamento() != null){
		try {
			if(elemento_voce.isObbligatoriaIndicazioneTrovato()){
				controlliGestioneTrovatoAttiva(aUC, accertamento);
			} else if (elemento_voce.isInibitaIndicazioneTrovato()){
				controlliGestioneTrovatoNonAttiva(aUC, accertamento);
			}
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
//	}
}
private void controlliGestioneTrovatoNonAttiva(UserContext aUC,
		AccertamentoBulk accertamento) throws ComponentException, SQLException,
		ApplicationException {
	SQLBuilder sql = condizioneRigheFatturaConTrovatoValorizzato(aUC,accertamento);
	if (sql.executeCountQuery(getConnection(aUC)) > 0){
		throw new it.cnr.jada.comp.ApplicationException("Attenzione! La voce del piano indicata non ha attiva la gestione dei brevetti ma sono state trovate fatture con l'indicazione del trovato.");
	}
}
private void controlliGestioneTrovatoAttiva(UserContext aUC,
		AccertamentoBulk accertamento) throws ComponentException, SQLException,
		ApplicationException {
	SQLBuilder sql = condizioneRigheFatturaConTrovatoNonValorizzato(aUC, accertamento);
	if (sql.executeCountQuery(getConnection(aUC)) > 0){
		throw new it.cnr.jada.comp.ApplicationException("Attenzione! La voce del piano indicata ha attiva la gestione dei brevetti ma sono state trovate fatture senza l'indicazione del trovato.");
	}
}
private SQLBuilder condizioneRigheFatturaConTrovatoNonValorizzato(
		UserContext aUC, AccertamentoBulk accertamento)
		throws ComponentException {
	SQLBuilder sql = preparaCondizionePerTrovato(aUC, accertamento);
	sql.addSQLClause(FindClause.AND,"FATTURA_ATTIVA_RIGA.pg_trovato",SQLBuilder.ISNULL, null);
	return sql;
}
private SQLBuilder condizioneRigheFatturaConTrovatoValorizzato(UserContext aUC,
		AccertamentoBulk accertamento) throws ComponentException {
	SQLBuilder sql = preparaCondizionePerTrovato(aUC, accertamento);
	sql.addSQLClause(FindClause.AND,"FATTURA_ATTIVA_RIGA.pg_trovato",SQLBuilder.ISNOTNULL, null);
	return sql;
}
private SQLBuilder preparaCondizionePerTrovato(UserContext aUC,
		AccertamentoBulk accertamento) throws ComponentException {
	PersistentHome osHome = getHomeCache(aUC).getHome(Accertamento_scadenzarioBulk.class);
	SQLBuilder sql = osHome.createSQLBuilder();
	sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, accertamento.getCds().getCd_unita_organizzativa());
	sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, accertamento.getEsercizio());
	sql.addClause(FindClause.AND,"esercizio_originale",SQLBuilder.EQUALS, accertamento.getEsercizio_originale());
	sql.addClause(FindClause.AND,"pg_accertamento",SQLBuilder.EQUALS, accertamento.getPg_accertamento());
	sql.addTableToHeader("FATTURA_ATTIVA_RIGA");
	sql.addSQLClause(FindClause.AND,"FATTURA_ATTIVA_RIGA.ESERCIZIO",SQLBuilder.EQUALS, accertamento.getEsercizio());
	sql.addSQLJoin( "FATTURA_ATTIVA_RIGA.CD_CDS_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.CD_CDS");
	sql.addSQLJoin( "FATTURA_ATTIVA_RIGA.ESERCIZIO_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.ESERCIZIO");
	sql.addSQLJoin( "FATTURA_ATTIVA_RIGA.ESERCIZIO_ORI_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.ESERCIZIO_ORIGINALE");
	sql.addSQLJoin( "FATTURA_ATTIVA_RIGA.PG_ACCERTAMENTO", "ACCERTAMENTO_SCADENZARIO.PG_ACCERTAMENTO");
	sql.addSQLJoin( "FATTURA_ATTIVA_RIGA.PG_ACCERTAMENTO_SCADENZARIO", "ACCERTAMENTO_SCADENZARIO.PG_ACCERTAMENTO_SCADENZARIO");
	return sql;
}

public void verificaCoperturaContratto (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	//	Controllo che l'accertamento non abbia sfondato il contratto
	if (accertamento.isCheckDisponibilitaContrattoEseguito())
	  return;
	if (accertamento.getContratto() != null && accertamento.getContratto().getPg_contratto() != null){
	  try {
		  ContrattoHome contrattoHome = (ContrattoHome)getHome(aUC, ContrattoBulk.class);
		  SQLBuilder sql = contrattoHome.calcolaTotAccertamenti(aUC,accertamento.getContratto());
		  BigDecimal totale = null;
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sql.prepareStatement(getConnection(aUC));
					try {
						rs = ps.executeQuery();
						if (rs.next())
						totale = rs.getBigDecimal(1);
					} catch (java.sql.SQLException e) {
						throw handleSQLException(e);
					} finally {
						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
				}
			} catch (java.sql.SQLException ex) {
				throw handleException(ex);
			}
		  if (totale != null ){
			  if (totale.compareTo(accertamento.getContratto().getIm_contratto_attivo()) > 0){
				  throw handleException( new CheckDisponibilitaContrattoFailed("La somma degli accertamenti associati supera l'importo definito nel contratto."));
			  }
		  }
	  } catch (IntrospectionException e1) {
		  throw new it.cnr.jada.comp.ComponentException(e1);
	  } catch (PersistencyException e1) {
		  throw new it.cnr.jada.comp.ComponentException(e1);
	  }
  }
}

public void verificaCoerenzaGaeContratto(UserContext aUC,AccertamentoBulk accertamento) throws ComponentException {
	try {
		Optional<Integer> optPrgContratto = Optional.ofNullable(accertamento)
				.flatMap(el->Optional.ofNullable(el.getContratto()))
				.flatMap(el->Optional.ofNullable(el.getPg_progetto()));
		
		if (optPrgContratto.isPresent()) {
			ProgettoHome progettoHome = (ProgettoHome)getHome(aUC, ProgettoBulk.class);
			WorkpackageHome home = (WorkpackageHome)getHome(aUC, WorkpackageBulk.class);
			accertamento.getAccertamento_scadenzarioColl().stream()
				.flatMap(el->Optional.ofNullable(el.getAccertamento_scad_voceColl()).map(List::stream).orElse(Stream.empty()))
				.map(el->el.getLinea_attivita())
				.distinct()
				.forEach(el->{
					try {
						WorkpackageBulk lineaAttivita = home.searchGAECompleta(aUC, accertamento.getEsercizio(), el.getCd_centro_responsabilita(), el.getCd_linea_attivita());
						if (!lineaAttivita.getPg_progetto().equals(optPrgContratto.get())) {
							ProgettoBulk prgContratto = (ProgettoBulk)progettoHome.findByPrimaryKey(new ProgettoBulk(accertamento.getEsercizio(), optPrgContratto.get(), ProgettoBulk.TIPO_FASE_NON_DEFINITA));
							if (Optional.ofNullable(prgContratto).isPresent()) {
								throw new ApplicationRuntimeException("Linea di Attività "+el.getCd_linea_attivita()+" del CDR "+el.getCd_centro_responsabilita()+
										" non selezionabile in quanto appartenente al progetto "+lineaAttivita.getCd_progetto()+" diverso dal progetto " +
										prgContratto.getCd_progetto()+" del contratto associato all'accertamento.");
							}
						}
					} catch (ComponentException|PersistencyException e) {
						throw new ApplicationRuntimeException(e);
					}
				});
		}
	} catch (Throwable e) {
		throw handleException(e);
	}
}

/**
  *  Tutti controlli superati - creazione
  *    PreCondition:
  *      Non esiste già una scadenza per la data.
  *      Attività = creazione
  *    PostCondition:
  *      Alla scrittura dell'accertamento il sistema aggiungerà questo scadenzario e genererà tutti i dettagli della
  *      scadenza (metodo 'generaDettagliScadenzaAccertamento')
  *  Tutti controlli superati - aggiornamento con agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'accertamento il sistema aggiornerà questo scadenzario.
  *      In più, il metodo aggiornaScadenzaSuccessivaAccertamento viene utilizzato per aggiornare la scadenza successiva
  *      a quella in aggiornamento.
  *  Tutti controlli superati - aggiornamento senza agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente NON ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'accertamento il sistema aggiornerà questo scadenzario.
  *      Sarà il compito dell'utente aggiornare una delle scadenze per garantire che la somma degli importi
  *      delle scadenze sia uguale all'importo dell'accertamento.
  *  creazione/modifica - esiste già una scadenza per la data
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica la data di una scadenza.
  *      Per la data scadenza specificata esiste già una scadenza per l'accertamento.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la data della scadenza non è valida.
  *  modifica - la scadenza ha doc amministrativi associati e non proviene da documenti amministrativi
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza che ha documenti amministrativi associati
  *      e la richiesta non proviene dal BusinessProcess che gestisce i documenti amministrativi
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la modifica della scadenza non è valida.
  *  modifica - la scadenza ha doc amministrativi associati e proviene da documenti amministrativi
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza che ha documenti amministrativi associati e la
  *      richiesta proviene dal BusinessProcess che gestisce i documenti amministartivi
  *    PostCondition:
  *      L'aggiornamento dell'importo della scadenza e' consentito
  *  modifica - la scadenza ha reversali associate
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza
  *      La scadenza ha reversali associate
  *      La richiesta di modifica proviene dal BusinessProcess che gestisce i documenti amministrativi
  *    PostCondition:
  *      L'aggiornamento dell'importo della scadenza non e' consentito
  *
  * @param aUC lo user context
  * @param scadenza l'istanza di  <code>Accertamento_scadenzarioBulk</code> da verificare
  * @return l' AccertamentoBulk a cui appartiene la scadenza
  *
  */

public AccertamentoBulk verificaScadenzarioAccertamento (UserContext aUC, Accertamento_scadenzarioBulk scadenza) throws ComponentException
{
	return verificaScadenzarioAccertamento (aUC, scadenza, true);
}
public AccertamentoBulk verificaScadenzarioAccertamento (UserContext aUC, Accertamento_scadenzarioBulk scadenza, boolean effettuaControlliDocAmm) throws ComponentException
{
	AccertamentoBulk accertamento = scadenza.getAccertamento();

	/*
	for ( Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext();)
	{
		Accertamento_scadenzarioBulk s = (Accertamento_scadenzarioBulk) i.next();
		if ( !s.equals(scadenza) && s.getDt_scadenza_incasso().equals( scadenza.getDt_scadenza_incasso()))
			throw handleException( new it.cnr.jada.comp.ApplicationException( "Esiste gia' una scadenza per la data specificata"));
	}
	*/
	//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati e non provengo dal BP dei
	//doc. amministrativi
	// se richiamato dal tasto di "Azzera" per accertamenti residui
	// è possibile annullare le righe contabili dei documenti associati
	// se non è stato associato a reversali
	// quindi il controllo non viene fatto
	if (effettuaControlliDocAmm) {
		if(	!scadenza.isFromDocAmm() &&
			scadenza.getScadenza_iniziale() != null &&
			scadenza.getIm_scadenza().compareTo(scadenza.getScadenza_iniziale().getIm_scadenza()) != 0 &&
			scadenza.getPg_doc_attivo() != null)
			throw new ApplicationException( "Impossibile variare importo di una scadenza con doc. amministrativi associati");
	}

	if(	scadenza.isFromDocAmm() &&
		scadenza.getScadenza_iniziale() != null &&
		scadenza.getIm_scadenza().compareTo(scadenza.getScadenza_iniziale().getIm_scadenza()) != 0 &&
		scadenza.getPg_reversale() != null)
		throw new ApplicationException( "Impossibile variare importo di una scadenza con reversale associata");


	// Gestisco l'eventuale aggiornamento dell'importo della scadenza successiva a quella appena modificata
	aggiornaScadenzarioSuccessivoAccertamento(aUC, scadenza);

	// Alla conferma (inserimento/modifica) di una scadenza creo/rigenero i relativi dettagli
	generaDettagliScadenzaAccertamento(aUC, accertamento, scadenza);

	scadenza.setStatus( Accertamento_scadenzarioBulk.STATUS_CONFIRMED);
	scadenza.setScadenza_iniziale( null );

    return accertamento;
}
/**
 * Verifica dello stato dell'esercizio precedenet per residui
 *
 * @param userContext <code>UserContext</code>
 *
 * @return FALSE se per il cds interessato l'esercizio precedente non è in stato di "chiuso"
 *		   TRUE in tutti gli altri casi
 *
 */

void verificaStatoEsercizioEsPrecedente( UserContext userContext, Integer es, String cd_cds ) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	EsercizioBulk esPrec = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey(
																									new EsercizioBulk( cd_cds, new Integer( es.intValue() - 1 )));
	if (esPrec != null && !esPrec.STATO_CHIUSO_DEF.equals(esPrec.getSt_apertura_chiusura()))
			throw handleException( new ApplicationException( "Operazione impossibile: esercizio precedente non chiuso!") );

}

public Vector listaCdrPerCapitoli (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	try
	{
		Vector cdr = new Vector();
		AccertamentoHome accertamentoHome = (AccertamentoHome) getHome( aUC, accertamento );
		if (accertamento.getCds().getCd_tipo_unita() != null){
			if (!accertamento.getCds().getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
			{

				cdr.addAll( accertamentoHome.findCdr( (List) accertamento.getCapitoliDiEntrataCdsSelezionatiColl(), accertamento ));
				//if (accertamento.isSpesePerCostiAltrui() )
				//	cdr.addAll( accertamentoHome.findCdrPerSpesePerCostiAltrui( (List) accertamento.getCapitoliDiSpesaCdsSelezionatiColl() ));
			}
			else
			{
				cdr.addAll( accertamentoHome.findCdrPerSAC( (List) accertamento.getCapitoliDiEntrataCdsSelezionatiColl(), accertamento ));
			/*
				if (accertamento.isSpesePerCostiAltrui() )
					cdr.addAll( accertamentoHome.findCdrPerSpesePerCostiAltruiPerSAC( (List) accertamento.getCapitoliDiSpesaCdsSelezionatiColl() ));
			*/
			}

		}
		return cdr;
		}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
/**
  *  creazione scadenza/modifica importo - imputazione automatica
  *    PreCondition:
  *      L'utente ha richiesto l'imputazione automatica dell'accertamento e ha creato una scadenza o ha modificato l'importo
  *      di una scadenza esistente
  *    PostCondition:
  *      Per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di
  *      scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato
  *      un dettaglio di scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneAccertamento viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per riaprtire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  creazione scadenza/modifica importo - imputazione manuale
  *    PreCondition:
  *      L'utente ha specificato l'imputazione manuale dell'accertamento e ha creato una scadenza o ha modificato l'importo
  *      di una scadenza esistente
  *    PostCondition:
  *      Per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di
  *      scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato
  *      un dettaglio di scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  conferma imputazione finanziaria - imputazione automatica
  *    PreCondition:
  *      L' utente ha completato l'imputazione finanziaria, confermando le linee di attività selezionate, e ha richiesto la ripartizione automatica degli importi
  *      delle scadenze
  *    PostCondition:
  *      Per ogni scadenza dell'accertamento e per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di
  *      scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'accertamento e per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato
  *      un dettaglio di scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneAccertamento viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per ripartire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  conferma imputazione finanziaria - imputazione manuale
  *    PreCondition:
  *      L' utente ha completato l'imputazione finanziaria, confermando le linee di attività selezionate, e ha selezionato la ripartizione manuale degli importi
  *      delle scadenze
  *    PostCondition:
  *      Per ogni scadenza dell'accertamento e per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di
  *      scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'accertamento e per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato
  *      un dettaglio di scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  modifica imputazione finanziaria - imputazione automatica
  *    PreCondition:
  *      L' utente ha modificato l'imputazione finanziaria definita per l'accertamento e ha richiesto la ripartizione automatica degli importi
  *      delle scadenze
  *    PostCondition:
  *      Tutti i dettagli delle scadenze dell'accertamento che facevano riferimento a linee di attività non più selezionate
  *      vengono cancellati
  *      Per ogni scadenza dell'accertamento e per ogni nuova linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di
  *      scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'accertamento e per ogni nuova linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato
  *      un dettaglio di scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneAccertamento viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per ripartire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  modifica imputazione finanziaria - imputazione manuale
  *    PreCondition:
  *      L' utente ha modificato l'imputazione finanziaria definita per l'accertamento e ha selezionato la ripartizione manuale degli importi
  *      delle scadenze
  *    PostCondition:
  *      Tutti i dettagli delle scadenze dell'accertamento che facevano riferimento a linee di attività non più selezionate
  *      vengono cancellati
  *      Per ogni scadenza dell'accertamento e per ogni nuova linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di
  *      scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'accertamento e per ogni nuova linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato
  *      un dettaglio di scadenza Accertamento_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  Errore - imputazione automatica per linea att SINGOLA
  *    PreCondition:
  *      L'utente ha richiesto l'imputazione automatica, ha inoltre selezionato delle linee di attività dal piano di gestione
  *      con categoria di dettaglio = SINGOLA e per le quali la somma delle colonne I,K,Q,S,U e' nullo
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare all'utente l'impossibilità di effettuare in automatico la
  *      ripartizione dell'importo della scadenza sulle linee di attività scelte
  *  Errore - imputazione automatica per linea att SCARICO
  *    PreCondition:
  *      L'utente ha richiesto l'imputazione automatica, ha inoltre selezionato delle linee di attività dal piano di gestione
  *      con categoria di dettaglio = SCARICO e per le quali la somma delle colonne J,L,R,T e' nullo
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare all'utente l'impossibilità di effettuare in automatico la
  *      ripartizione dell'importo della scadenza sulle linee di attività scelte
  *  Errore - percentuali per nuove linee att.
  *    PreCondition:
  *      L'utente ha specificato solo delle linee di attività che non sono presenti nel piano di gestione e la somma
  *      delle percentuali inserite dall'utente da utilizzare nella ripartizione dell'importo di ogni scadenza e' diversa
  *      da 100.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare l'errore all'utente
  *  Errore - percentuali per nuove linee att. > 100
  *    PreCondition:
  *      L'utente ha specificato per le linee di attività che non sono presenti nel piano di gestione
  *      delle percentuali  da utilizzare nella ripartizione dell'importo di ogni scadenza e la loro somma e'
  *      maggiore di 100
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare l'errore all'utente
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param accertamento <code>AccertamentoBulk</code> l'accertamento per cui creare i dettagli scadenza
  * @param scadenzario <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento per cui creare i dettagli oppure
  *        <code>null</code> se e' necessario generare i dettagli per tutte le scadenze
  *
  *
 */
public AccertamentoBulk generaDettagliScadenzaAccertamento (UserContext aUC,AccertamentoBulk accertamento,Accertamento_scadenzarioBulk scadenzario) throws ComponentException {
	return generaDettagliScadenzaAccertamento(aUC, accertamento, scadenzario, true);
}
protected AccertamentoBulk generaDettagliScadenzaAccertamento (UserContext aUC,AccertamentoBulk accertamento,Accertamento_scadenzarioBulk scadenzario, boolean allineaImputazioneFinanziaria) throws ComponentException
{
	Accertamento_scadenzarioBulk os;

	// non e' ancora stata selezionata l'imputazione finanziaria
	if (accertamento.getLineeAttivitaSelezionateColl().size() == 0 &&
		accertamento.getNuoveLineeAttivitaColl().size() == 0)
		return accertamento;

	// la somma delle percentuali delle nuove linee di attività e' diversa da 100
	if (accertamento.getLineeAttivitaSelezionateColl().size() == 0 &&
		accertamento.getNuoveLineeAttivitaColl().size() > 0)
	{
		BigDecimal tot = new BigDecimal(0);
		for ( Iterator i = accertamento.getNuoveLineeAttivitaColl().iterator(); i.hasNext(); )
			tot = tot.add( Utility.nvl(((Linea_attivitaBulk)i.next()).getPrcImputazioneFin()));
		if ( tot.compareTo( new BigDecimal(100)) != 0 )
			throw new ApplicationException( "La somma delle percentuali dei nuovi GAE e' diversa da 100");

	}
	// la somma delle percentuali delle nuove linee di attività e' maggiore di 100
	else if ( accertamento.getNuoveLineeAttivitaColl().size() > 0)
	{
		BigDecimal tot = new BigDecimal(0);
		for ( Iterator i = accertamento.getNuoveLineeAttivitaColl().iterator(); i.hasNext(); )
			tot = tot.add( ((Linea_attivitaBulk)i.next()).getPrcImputazioneFin());
		if ( tot.compareTo( new BigDecimal(100)) > 0 )
			throw new ApplicationException( "La somma delle percentuali dei nuovi GAE e' maggiore di 100");

	}

	//imputazione automatica impossibile
	if ( accertamento.getFl_calcolo_automatico().booleanValue() )
	{
		BigDecimal totaleLattDaPdg = new BigDecimal(0);
		for ( Iterator i = accertamento.getLineeAttivitaSelezionateColl().iterator(); i.hasNext(); )
			totaleLattDaPdg = totaleLattDaPdg.add(((V_pdg_accertamento_etrBulk) i.next()).getImporto());
		if ( accertamento.getLineeAttivitaSelezionateColl().size() > 0 && totaleLattDaPdg.doubleValue() == 0 )
			throw new ApplicationException( "Workpackages da PdG con ricavi/entrate nulle. Imputazione automatica impossibile!");
	}


	// non sono ancora state inserite le scadenze
	if (accertamento.getAccertamento_scadenzarioColl().size() == 0 )
		return accertamento;


	if ( scadenzario != null ) // una sola scadenza e' stata modificata
	{
		//creo i dettagli della scadenza per le linee attivita da PDG
		creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, accertamento, scadenzario );

		//creo i dettagli della scadenza per le nuove linee attivita
		creaDettagliScadenzaPerNuoveLineeAttivita( aUC, accertamento, scadenzario );

		// aggiunta delle linee di attività comuni nelle scadenze con importo nullo
		/*
		 * 21/04/2006 Gestione ripartizione dei ricavi utilizzata fino al 2005.
		 *            Eliminata solo a partire dal 2006
		 */
		try {
			if (!((Parametri_cnrBulk)getHome(aUC,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(aUC)))).getFl_regolamento_2006().booleanValue())
				creaDettagliScadenzaLAComuni(aUC, accertamento, scadenzario);
		}
		catch ( Exception e )
		{
			throw handleException( e );
		}

	}	// imputazione finanziaria e' stata modificata, quindi rigenero i dettagli per tutte le scadenze
	else
	{

		// per ogni scadenza aggiorno i suoi dettagli in base alle linee di attività specificate dall'utente
		for ( Iterator scadIterator = accertamento.getAccertamento_scadenzarioColl().iterator(); scadIterator.hasNext(); )
		{
				os = (Accertamento_scadenzarioBulk) scadIterator.next();
				//cancello i dettagli della scadenza per le linee attivita che non esistono piu'
				cancellaDettagliScadenze( aUC, accertamento, os );

				//creo i dettagli della scadenza per le linee attivita da PDG
				creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, accertamento, os );

				//creo i dettagli della scadenza per le nuove linee attivita
				creaDettagliScadenzaPerNuoveLineeAttivita( aUC, accertamento, os );

				// aggiunta delle linee di attività comuni nelle scadenze con importo nullo
				/*
				 * 21/04/2006 Gestione ripartizione dei ricavi utilizzata fino al 2005.
				 *            Eliminata solo a partire dal 2006
				 */
				try {
					if (!((Parametri_cnrBulk)getHome(aUC,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(aUC)))).getFl_regolamento_2006().booleanValue())
						creaDettagliScadenzaLAComuni(aUC, accertamento, os);
				}
				catch ( Exception e )
				{
					throw handleException( e );
				}
		}
	}

	if ( accertamento.getFl_calcolo_automatico().booleanValue() && allineaImputazioneFinanziaria)
		accertamento = calcolaPercentualeImputazioneAccertamento( aUC, accertamento );

	return accertamento;
}

/**
  *  CDS SAC - non scarico
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo una nuova linea di attività da piano di gestione
  *      ad una accertamento di appartenenza del Cds SAC. La linea di attività non e' di scarico.
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'accertamento riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza l'articolo selezionato
  *      in imputazione finanziaria avente funzione e codice CdR uguale a quello della linea di attività
  *  CDS SAC - scarico
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo una nuova linea di attività da piano di gestione
  *      ad una accertamento di appartenenza del Cds SAC. La linea di attività e' di scarico.
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'accertamento riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza l'articolo selezionato
  *      in imputazione finanziaria avente funzione e codice CdR uguale a quello della linea di attività collegata nel
  *      piano di gestione alla linea di attività selezionata
  *  CDS diverso da SAC
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo una nuova linea di attività da piano di gestione
  *      ad una accertamento di appartenenza di un Cds con tipologia diversa da SAC.
  *      La linea di attività non e' di scarico.
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'accertamento riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza il capitolo selezionato
  *      in imputazione finanziaria avente funzione uguale a quello della linea di attività
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param accertamento <code>AccertamentoBulk</code> l'accertamento per cui creare i dettagli scadenza
  * @param scadenzario <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento per cui creare i dettagli
  *
  */

protected void creaDettagliScadenzaPerLineeAttivitaDaPdG(UserContext aUC,AccertamentoBulk accertamento, Accertamento_scadenzarioBulk scadenzario) throws ComponentException
{

	Accertamento_scad_voceBulk osv;
	Accertamento_scadenzarioBulk os;
	V_pdg_accertamento_etrBulk ppsd;
	Linea_attivitaBulk la;
	boolean found;

	//creo i dettagli scadenze se non esistono per le linee di attività da PDG

	for ( Iterator lattIterator = accertamento.getLineeAttivitaSelezionateColl().iterator(); lattIterator.hasNext(); )
	{
		found = false;
		ppsd = (V_pdg_accertamento_etrBulk) lattIterator.next();
		for ( Iterator i = scadenzario.getAccertamento_scad_voceColl().iterator(); i.hasNext(); )
		{
			osv = (Accertamento_scad_voceBulk) i.next();
			if ( osv.getCd_centro_responsabilita().equals( ppsd.getCd_centro_responsabilita()) &&
				 osv.getCd_linea_attivita().equals( ppsd.getCd_linea_attivita()) )
			{
					found = true;
					break;
			}
		}

		if ( !found )
		{
			//creo	nuovo Accertamento_scad_voceBulk
			osv = new Accertamento_scad_voceBulk();
			osv.setToBeCreated();
			// MITODO - questi campi non esistono in Accertamento_scad_voce, è ok???
			//if ( accertamento.getCds().getCd_tipo_unita().equalsIgnoreCase( "SAC" ) )
			/*
			{
				Voce_fBulk articolo;
				if ( ppsd.getCategoria_dettaglio().equals( it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk.CAT_SINGOLO))
					articolo = accertamento.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita());
				else
					articolo = accertamento.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita_clgs());
				osv.setTi_appartenenza( articolo.getTi_appartenenza());
				osv.setTi_gestione( articolo.getTi_gestione());
				osv.setCd_voce( articolo.getCd_voce() );

			}
			else
			{
				Voce_fBulk capitolo = accertamento.getCapitolo( ppsd.getCd_funzione() );
				osv.setTi_appartenenza( capitolo.getTi_appartenenza());
				osv.setTi_gestione( capitolo.getTi_gestione());
				osv.setCd_voce( capitolo.getCd_voce() );
			}
			*/
			//linea attivita'
			CdrBulk cdr = new CdrBulk();
			cdr.setCd_centro_responsabilita( ppsd.getCd_centro_responsabilita());
			osv.getLinea_attivita().setCentro_responsabilita( cdr );
			FunzioneBulk funzione = new FunzioneBulk();
			osv.getLinea_attivita().setCd_linea_attivita( ppsd.getCd_linea_attivita());
			osv.getLinea_attivita().setFunzione( funzione );
			osv.getLinea_attivita().setCd_funzione( ppsd.getCd_funzione());
			NaturaBulk natura = new NaturaBulk();
			osv.getLinea_attivita().setNatura( natura );
			osv.getLinea_attivita().setCd_natura( ppsd.getCd_natura());
			osv.setCd_linea_attivita( ppsd.getCd_linea_attivita() );
			osv.setCd_centro_responsabilita( ppsd.getCd_centro_responsabilita());

			osv.setIm_voce( new java.math.BigDecimal(0));
			// MITODO - verificare come mai ho dovuto anticipare la valorizzazione rispetto all'obbligazione
			osv.setAccertamento_scadenzario( scadenzario );
			osv.setCd_fondo_ricerca( accertamento.getCd_fondo_ricerca() );
			//osv.setUser( aUC.getUser())		;
			osv.setUser( scadenzario.getAccertamento().getUser() );
			//osv.setAccertamento_scadenzario( scadenzario );
			((BulkList) scadenzario.getAccertamento_scad_voceColl()).add( osv );
		}
	}
}
/**
  *  CDS SAC
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo ad una accertamento di appartenenza del Cds SAC
  *      una nuova linea di attività che non e' presente nel piano di gestione
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'accertamento riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza l'articolo selezionato
  *      in imputazione finanziaria avente funzione e codice CdR uguale a quello della linea di attività
  *  CDS diverso da SAC
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo ad una accertamento di appartenenza ad un Cds diverso da SAC
  *      una nuova linea di attività che non e' presente nel piano di gestione
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'accertamento riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza il capitolo selezionato
  *      in imputazione finanziaria avente funzione uguale a quello della linea di attività
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param accertamento <code>AccertamentoBulk</code> l'accertamento per cui creare i dettagli scadenza
  * @param scadenzario <code>Accertamento_scadenzarioBulk</code> la scadenza dell'accertamento per cui creare i dettagli
  *
 */

protected void creaDettagliScadenzaPerNuoveLineeAttivita (UserContext aUC,AccertamentoBulk accertamento, Accertamento_scadenzarioBulk scadenzario) throws ComponentException
{

	Accertamento_scad_voceBulk osv;
	Accertamento_scadenzarioBulk os;
	Linea_attivitaBulk la;
	boolean found;

	//creo i dettagli scadenze se non esistono per le nuove linee di attività

	for ( Iterator lattIterator = accertamento.getNuoveLineeAttivitaColl().iterator(); lattIterator.hasNext(); )
	{
		la = (Linea_attivitaBulk) lattIterator.next();
		try
		{
			la.validate();
		}
		catch ( ValidationException e)
		{
			throw handleException( new ApplicationException( e.getMessage()));
		}
		found = false;
		for ( Iterator i = scadenzario.getAccertamento_scad_voceColl().iterator(); i.hasNext(); )
		{
			osv = (Accertamento_scad_voceBulk) i.next();
			if ( osv.getCd_centro_responsabilita().equals( la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita()) &&
				 osv.getCd_linea_attivita().equals( la.getLinea_att().getCd_linea_attivita()) )
					found = true;
		}
		if ( !found )
		{
			//creo	nuovo Accertamento_scad_voceBulk
			osv = new Accertamento_scad_voceBulk();
			osv.setToBeCreated();
			// MITODO - che devo fare di questo???
			/*
			if ( accertamento.getCds().getCd_tipo_unita().equalsIgnoreCase( "SAC" ) )
			{
				Voce_fBulk articolo = accertamento.getArticolo( la.getLinea_att().getFunzione().getCd_funzione(), la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita());
				osv.setTi_appartenenza( articolo.getTi_appartenenza());
				osv.setTi_gestione( articolo.getTi_gestione());
				osv.setCd_voce( articolo.getCd_voce() );
			}
			else
			{
				Voce_fBulk capitolo = accertamento.getCapitolo( la.getLinea_att().getFunzione().getCd_funzione() );
				osv.setTi_appartenenza( capitolo.getTi_appartenenza());
				osv.setTi_gestione( capitolo.getTi_gestione());
				osv.setCd_voce( capitolo.getCd_voce() );
			}
			*/
			osv.setLinea_attivita( la.getLinea_att() );
			osv.setCd_linea_attivita( la.getLinea_att().getCd_linea_attivita() );
			osv.setCd_centro_responsabilita( la.getLinea_att().getCd_centro_responsabilita());
			osv.setIm_voce( new java.math.BigDecimal(0));
			osv.setCd_fondo_ricerca( accertamento.getCd_fondo_ricerca() );
			// osv.setUser( scadenzario.getUser())		;
			osv.setUser( scadenzario.getAccertamento().getUser() );
			osv.setAccertamento_scadenzario( scadenzario );
			((BulkList) scadenzario.getAccertamento_scad_voceColl()).add( osv );

		}
	}
}

/**
  *  normale
  *    PreCondition:
  *      Viene richiesto il calcolo delle percentuali d'imputazione per l'accertamento e la ripartizione dell'importo
  *      delle sacdenze sui vari dettagli secondo tali percentuali
  *    PostCondition:
  *      Il sistema calcola la percentuale di ripartizione delle linee di attività nel modo seguente:
  *      - Per le linee di attività non presenti nel Piano di Gestione la percentuale e' inserita dall'utente
  *      - Per le linee di attività presenti nel Piano di Gestione e con categoria di dettaglio = SINGOLO viene
  *        calcolata la somma delle colonne I,K,Q,S,U e viene calcolata la percentuale di questo importo rispetto alla
  *        somma di tutti questi importi per tutte le linee attività selezionate
  *      - Per le linee di attività presenti nel Piano di Gestione e con categoria di dettaglio = SCARICO viene
  *        calcolata la somma delle colonne J,L,R,T e viene calcolata la percentuale di questo importo rispetto alla
  *        somma di tutti questi importi per tutte le linee attività selezionate
  *      Esempio di imputazione finanziaria:
  *      - Linea attività L0001 non da PdG con percentuale specificata dall'utente: 10%
  *      - Linea attività L0002 da PdG con categoria dettaglio = SINGOLO e somma delle colonne I,K,Q,S,U = 50.000,00
  *      - Linea attività L0003 da PdG con categoria dettaglio = SCARICO e somma delle colonne I,K,Q,S,U = 40.000,00
  *      Il sistema calcola le seguenti percentuali di ripartizione:
  *      - Linea attività L0001 : 10%
  *      - Linea attività L0002 : 50%
  *      - Linea attività L0003 : 40%
  *      Determinate tali percentuali il sistema procede a ripartire l'importo di ogni scadenza sui singoli dettagli
  *      secondo tali percentuali. Se, per problemi di arrotondamento, alla fine della ripartizione la somma degli
  *      importi dei dettagli della scadenza non e' uguale all'importo della scadenza, il sistema quadra tale somma
  *      assegnando il delta al primo dettaglio della scadenza
  *
  *  errore - manca percentuale per nuova linea attività
  *    PreCondition:
  *      Nell'imputazione finanziaria dell'accertamento e' stata specificata una linea di attività non presente nel
  *      Piano di Gestione e per tale linea non e' stata specificata la percentuale da usare nella ripartizione
  *      dell'importo delle scadenze sui dettagli
  *    PostCondition:
  *      Una segnalazione di errore viene restituita all'utente per comunicare il problema
  *
  *  errore - non esistono spese e costi nel piano di gestione per linea attivita SINGOLA
  *    PreCondition:
  *      Nell'imputazione finanziaria dell'accertamento e' stata specificata una linea di attività presente nel
  *      Piano di Gestione con categoria di dettaglio SINGOLA e per la quale la somma delle colonne I,K,Q,S,U risulta
  *      essere 0
  *    PostCondition:
  *      Una segnalazione di errore viene restituita all'utente per comunicare che i costi/spese della linea di attività sono nulli
  *
  *  errore - non esistono spese e costi nel piano di gestione per linea attivita SCARICO
  *    PreCondition:
  *      Nell'imputazione finanziaria dell'accertamento e' stata specificata una linea di attività presente nel
  *      Piano di Gestione con categoria di dettaglio SCARICO e per la quale la somma delle colonne J,L,R,T risulta
  *      essere 0
  *    PostCondition:
  *      Una segnalazione di errore viene restituita all'utente per comunicare che i costi/spese relativi ad altra UO della linea di attività sono nulli
  *
  * @param aUC lo user context
  * @param accertamento l'istanza di  <code>AccertamentoBulk</code> per la quale devono essere calcolati gli importi e le percentuali dei suoi
  *        dettagli di scadenza
  * @return l'accertamento con le percentuali e gli importi dei dettagli delle scadenze aggiornati
  */

protected AccertamentoBulk calcolaPercentualeImputazioneAccertamento (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	BigDecimal percentuale = new BigDecimal( 100);
	BigDecimal totaleLattDaPdg = new BigDecimal( 0 );
	BigDecimal totalePerScadenza;
	V_pdg_accertamento_etrBulk lattDaPdg;
	Linea_attivitaBulk latt;
	Accertamento_scad_voceBulk osv;
	Accertamento_scadenzarioBulk os;


	// calcolo le percentuali di imputazione finanziaria per le linee di attivita da pdg
	// 100 - percentuali specificate x linee att non da PDG
	for ( Iterator i = accertamento.getNuoveLineeAttivitaColl().iterator(); i.hasNext(); )
		percentuale = percentuale.subtract( ((Linea_attivitaBulk) i.next()).getPrcImputazioneFin());
	if ( accertamento.getNuoveLineeAttivitaColl().size() > 0 && percentuale.compareTo( new BigDecimal(100)) == 0
		&& accertamento.getLineeAttivitaSelezionateColl().size() == 0 )
		throw new ApplicationException( "Non sono state specificate le percentuali per i nuovi GAE!");
	for ( Iterator i = accertamento.getLineeAttivitaSelezionateColl().iterator(); i.hasNext(); )
		totaleLattDaPdg = totaleLattDaPdg.add(((V_pdg_accertamento_etrBulk) i.next()).getImporto());
	if ( accertamento.getLineeAttivitaSelezionateColl().size() > 0 && totaleLattDaPdg.doubleValue() == 0 )
		throw new ApplicationException( "GAE da PdG con ricavi/entrate nulle. Imputazione automatica impossibile!");
	for ( Iterator i = accertamento.getLineeAttivitaSelezionateColl().iterator(); i.hasNext(); )
	{
		lattDaPdg = (V_pdg_accertamento_etrBulk) i.next();
		lattDaPdg.setPrcImputazioneFin( lattDaPdg.getImporto().multiply(percentuale).divide(totaleLattDaPdg, 2, BigDecimal.ROUND_HALF_UP) );
	}

	// calcolo gli importi e le percentuali per i dettagli delle scadenze

	for ( Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
	{
		os = (Accertamento_scadenzarioBulk) i.next();
		totalePerScadenza = new BigDecimal( 0 );

		for ( Iterator j = os.getAccertamento_scad_voceColl().iterator(); j.hasNext(); )
		{
			osv = (Accertamento_scad_voceBulk) j.next();
			for ( Iterator k = accertamento.getLineeAttivitaSelezionateColl().iterator(); k.hasNext(); )
			{
				lattDaPdg = (V_pdg_accertamento_etrBulk) k.next();
				if ( lattDaPdg.getCd_centro_responsabilita().equals( osv.getCd_centro_responsabilita()) &&
					 lattDaPdg.getCd_linea_attivita().equals( osv.getCd_linea_attivita()))
				{
					osv.setIm_voce( os.getIm_scadenza().multiply( lattDaPdg.getPrcImputazioneFin()).divide( new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
					totalePerScadenza = totalePerScadenza.add( osv.getIm_voce() );
					if ( os.getIm_scadenza().doubleValue() != 0 )
						osv.setPrc( lattDaPdg.getPrcImputazioneFin() );
					else
						osv.setPrc( new BigDecimal(0) );
					osv.setToBeUpdated();
					break;
				}
			}
			for ( Iterator k = accertamento.getNuoveLineeAttivitaColl().iterator(); k.hasNext(); )
			{
				latt = (Linea_attivitaBulk) k.next();
				if ( latt.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita().equals( osv.getCd_centro_responsabilita()) &&
					 latt.getLinea_att().getCd_linea_attivita().equals( osv.getCd_linea_attivita()))
				{
					osv.setIm_voce( os.getIm_scadenza().multiply(latt.getPrcImputazioneFin()).divide( new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
					totalePerScadenza = totalePerScadenza.add( osv.getIm_voce() );
					if ( os.getIm_scadenza().doubleValue() != 0 )
						osv.setPrc( latt.getPrcImputazioneFin() );
					else
						osv.setPrc( new BigDecimal(0) );
					osv.setToBeUpdated();
					break;
				}
			}

		}
		// quadro il totale della scadenza con la somma dei dettagli
		if (  os.getAccertamento_scad_voceColl().size() > 0 && totalePerScadenza.compareTo( os.getIm_scadenza()) != 0 )
		{
			Iterator it = os.getAccertamento_scad_voceColl().iterator();
			while(it.hasNext()) {
				osv = (Accertamento_scad_voceBulk) it.next();
				if (osv.getIm_voce().add( os.getIm_scadenza().subtract( totalePerScadenza )).compareTo(new BigDecimal(0))>0)
				{
					osv.setIm_voce( osv.getIm_voce().add( os.getIm_scadenza().subtract( totalePerScadenza )));
					break;
				}
			}
		}
	}

	return accertamento;
}
public SQLBuilder selectLinea_attByClause(UserContext userContext, it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk bulk, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	return selectLinea_attByClause( userContext, context.getAccertamento(), clauses );
}
/*
 * Aggiunge alcune clausole a tutte le operazioni di ricerca delle Linee di Attività non da PDG
 *
 * Pre-post-conditions:
 *
 * Nome: Richiesta di ricerca di una linea di attività
 * Pre:  E' stata generata la richiesta di ricerca di una linea di attività non presente nel PDG
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che la Linea Attività sia valida per l'esercizio di scrivania, che il suo CDR appartenga
 *       all'UO di scrivania, che la Linea di Attività non sia presente nel PDG e che la sua funzione
 *       sia uguale a quella di uno dei capitoli di spesa selezionati dall'utente
 * @param userContext lo userContext che ha generato la richiesta
 * @param uo istanza di Unita_organizzativaBulk
 * @param cds istanza di CdsBulk che deve essere utilizzata per la ricerca
 * @param clauses clausole di ricerca gia' specificate dall'utente
 * @return il SQLBuilder con la clausola aggiuntive
 */
public SQLBuilder selectLinea_attByClause(UserContext userContext, AccertamentoBulk accertamento, CompoundFindClause clauses ) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{

	SQLBuilder sql = getHome(userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class, "V_LINEA_ATT_NOT_IN_PDG_ETR").createSQLBuilder();
	sql.setHeader( "SELECT DISTINCT " + sql.getColumnMap().getDefaultSelectHeaderSQL() );
	sql.addClause( clauses );
	sql.addSQLClause( "AND", "ESERCIZIO",  sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio() );
	//sql.addSQLClause( "AND", "CD_CENTRO_RESPONSABILITA",  sql.LIKE, accertamento.getCd_unita_organizzativa() + ".%" );
	sql.addSQLClause( "AND", "CD_CENTRO_RESPONSABILITA",  sql.LIKE, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_unita_organizzativa()+ ".%");
	//condizioni sulla funzione
	Iterator i = accertamento.getCapitoliDiEntrataCdsSelezionatiColl().iterator();
	if ( i.hasNext() )
	{
		sql.openParenthesis( "AND" );
		sql.addClause("AND", "cd_funzione", SQLBuilder.EQUALS, ((Voce_fBulk)i.next()).getCd_funzione());
		while ( i.hasNext() )
			sql.addClause( "OR", "cd_funzione", SQLBuilder.EQUALS, ((Voce_fBulk)i.next()).getCd_funzione());
		sql.closeParenthesis();
	}

	Iterator k = accertamento.getCdrSelezionatiColl().iterator();
	if ( k.hasNext() )
	{
		sql.openParenthesis( "AND" );
		sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, ((CdrBulk)k.next()).getCd_centro_responsabilita());
		while ( k.hasNext() )
			sql.addClause( "OR", "cd_centro_responsabilita", SQLBuilder.EQUALS, ((CdrBulk)k.next()).getCd_centro_responsabilita());
		sql.closeParenthesis();
	}
//	da inserire per selezionare solo le gae associabili
//	blocco da testare con Claudia
	SQLBuilder sql2 = getHome(userContext, Ass_ev_evBulk.class).createSQLBuilder();
	sql2.resetColumns();
	sql2.addColumn("CD_NATURA");
	sql2.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
	sql2.addSQLClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, accertamento.getCd_elemento_voce());
	sql2.addSQLClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
	sql2.addSQLClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);

	sql.openParenthesis("AND");
	sql.addSQLINClause("AND", "cd_natura", sql2);
	sql.closeParenthesis();
//	fine blocco da testare


	sql.openParenthesis( "AND");
	sql.addSQLClause( "AND",  "cd_elemento_voce", sql.ISNULL, null );
	sql.openParenthesis( "OR");
	sql.addSQLClause( "AND", "cd_elemento_voce", sql.NOT_EQUALS, accertamento.getCd_elemento_voce() );

	String condizione = "not exists ( select 1 from " +
										it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
									   "pdg_preventivo_etr_det " +
									   "where pdg_preventivo_etr_det.categoria_dettaglio = '" + Pdg_preventivo_etr_detBulk.CAT_SINGOLO +"' " +
									   "and pdg_preventivo_etr_det.stato = '" + Pdg_preventivo_etr_detBulk.ST_CONFERMA +"' " +
									   "and pdg_preventivo_etr_det.ti_appartenenza = '" + Elemento_voceHome.APPARTENENZA_CNR +"' " +
									   "and pdg_preventivo_etr_det.ti_gestione = '" + Elemento_voceHome.GESTIONE_ENTRATE +"' " +
									   "and pdg_preventivo_etr_det.esercizio = V_LINEA_ATT_NOT_IN_PDG_ETR.ESERCIZIO " +
									   "and pdg_preventivo_etr_det.cd_centro_responsabilita = V_LINEA_ATT_NOT_IN_PDG_ETR.cd_centro_responsabilita " +
									   "and pdg_preventivo_etr_det.cd_linea_attivita = V_LINEA_ATT_NOT_IN_PDG_ETR.cd_linea_attivita "  +
									   "and pdg_preventivo_etr_det.cd_elemento_voce = '" + accertamento.getCd_elemento_voce() + "' ";

	if (accertamento.isResiduo())
		condizione = condizione +
					   "union " +
					   "select 1 from " +
						it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					   "voce_f_saldi_cdr_linea " +
					   "where voce_f_saldi_cdr_linea.esercizio > voce_f_saldi_cdr_linea.esercizio_res " +
					   "and voce_f_saldi_cdr_linea.ti_appartenenza = '" + Elemento_voceHome.APPARTENENZA_CNR + "' " +
					   "and voce_f_saldi_cdr_linea.ti_gestione = '" + Elemento_voceHome.GESTIONE_ENTRATE +"' " +
					   "and voce_f_saldi_cdr_linea.esercizio = V_LINEA_ATT_NOT_IN_PDG_ETR.ESERCIZIO " +
					   "and voce_f_saldi_cdr_linea.cd_centro_responsabilita = V_LINEA_ATT_NOT_IN_PDG_ETR.cd_centro_responsabilita " +
					   "and voce_f_saldi_cdr_linea.cd_linea_attivita = V_LINEA_ATT_NOT_IN_PDG_ETR.cd_linea_attivita "  +
					   "and voce_f_saldi_cdr_linea.cd_elemento_voce = '" + accertamento.getCd_elemento_voce() + "' ";
	else
		condizione = condizione +
					   "union " +
					   "select 1 from " +
					    it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					   "pdg_modulo_entrate_gest " +
					   "where pdg_modulo_entrate_gest.categoria_dettaglio = '" + Pdg_modulo_entrate_gestBulk.CAT_DIRETTA +"' " +
					   "and pdg_modulo_entrate_gest.ti_appartenenza = '" + Elemento_voceHome.APPARTENENZA_CNR + "' " +
					   "and pdg_modulo_entrate_gest.ti_gestione = '" + Elemento_voceHome.GESTIONE_ENTRATE +"' " +
					   "and pdg_modulo_entrate_gest.esercizio = V_LINEA_ATT_NOT_IN_PDG_ETR.ESERCIZIO " +
					   "and pdg_modulo_entrate_gest.cd_centro_responsabilita = V_LINEA_ATT_NOT_IN_PDG_ETR.cd_centro_responsabilita " +
					   "and pdg_modulo_entrate_gest.cd_linea_attivita = V_LINEA_ATT_NOT_IN_PDG_ETR.cd_linea_attivita "  +
					   "and pdg_modulo_entrate_gest.cd_elemento_voce = '" + accertamento.getCd_elemento_voce() + "' ";

	condizione = condizione + ") ";
	sql.addSQLClause( "AND", condizione);

	sql.closeParenthesis();
	sql.closeParenthesis();

	return sql;
}

public Vector listaLineeAttivitaPerCapitoliCdr (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	try
	{
		Vector lineeAttivita = new Vector();
		List cdrDiScrivaniaColl = (List) accertamento.getCdrDiScrivaniaSelezionatiColl( ((CNRUserContext)aUC).getCd_unita_organizzativa());
		AccertamentoHome accertamentoHome = (AccertamentoHome) getHome( aUC, accertamento );
		if ( !accertamento.getCds().getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
		{
			lineeAttivita.addAll( accertamentoHome.findLineeAttivita( cdrDiScrivaniaColl, (List)accertamento.getCapitoliDiEntrataCdsSelezionatiColl(), accertamento ));
		}
		else //SAC
		{
			List capitoli = new LinkedList(); //elimina dalla lista dei capitoli selezionati quelli che hanno cdr non
			//selezionati dall'utente nella lista Cdr
			for (Iterator i = accertamento.getCapitoliDiEntrataCdsSelezionatiColl().iterator(); i.hasNext(); )
			{
				Voce_fBulk voce = (Voce_fBulk) i.next();
				for ( Iterator j = cdrDiScrivaniaColl.iterator(); j.hasNext(); )
					if (((CdrBulk)j.next()).getCd_centro_responsabilita().equals( voce.getCd_centro_responsabilita()))
						capitoli.add( voce );
			}
			lineeAttivita.addAll( accertamentoHome.findLineeAttivitaSAC( capitoli, accertamento ));
		}

		return lineeAttivita;

	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
public AccertamentoBulk listaCapitoliPerCdsVoce (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
{
	try
	{
		AccertamentoHome accertamentoHome = (AccertamentoHome) getHome( aUC, accertamento.getClass());
		accertamento.setCapitoliDiEntrataCdsColl( accertamentoHome.findCapitoliDiEntrataCds( accertamento ));
	}
	catch ( Exception e )
	{
		throw handleException( accertamento, e );
	}

	return accertamento;
}
public PrimaryKeyHashtable getOldRipartizioneCdrVoceLinea(UserContext userContext, AccertamentoBulk accertamento) throws it.cnr.jada.comp.ComponentException {
	BigDecimal totaleScad = new BigDecimal(0);
	Accertamento_scad_voceBulk asv;
	Accertamento_scadenzarioBulk as;
	Accertamento_scad_voceBulk key = new Accertamento_scad_voceBulk();
	PrimaryKeyHashtable prcImputazioneFinanziariaTable = new PrimaryKeyHashtable();

	try {
		AccertamentoHome accertHome = (AccertamentoHome)getTempHome(userContext, AccertamentoBulk.class);
		Accertamento_scadenzarioHome accertScadHome = (Accertamento_scadenzarioHome)getTempHome(userContext, Accertamento_scadenzarioBulk.class);

		AccertamentoBulk accertDB = (AccertamentoBulk)accertHome.findAccertamento(accertamento);
		/*se non esiste l'accertamento ritorno un valore vuoto*/
		if (accertDB == null)
			return prcImputazioneFinanziariaTable;

		accertDB.setAccertamentiPluriennali(new BulkList(accertHome.findAccertamentiPluriennali(userContext,accertamento)));
		accertDB.setAccertamento_scadenzarioColl(new BulkList(accertHome.findAccertamento_scadenzarioList(accertDB)));

		for ( Iterator i = accertDB.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
		{
			Accertamento_scadenzarioBulk accertScadDB = (Accertamento_scadenzarioBulk) i.next();
			accertScadDB.setAccertamento_scad_voceColl( new BulkList( accertScadHome.findAccertamento_scad_voceList( userContext,accertScadDB )));
		}
		getHomeCache(userContext).fetchAll(userContext);

		for ( Iterator s = accertDB.getAccertamento_scadenzarioColl().iterator(); s.hasNext(); )
		{
			as = (Accertamento_scadenzarioBulk) s.next();
			for ( Iterator d = as.getAccertamento_scad_voceColl().iterator(); d.hasNext(); )
			{
				asv = (Accertamento_scad_voceBulk) d.next();
				// totale per Cdr e per scadenza
				key = new Accertamento_scad_voceBulk(asv.getCd_cds(),
													 asv.getCd_centro_responsabilita(),
													 asv.getCd_linea_attivita(),
													 asv.getEsercizio(),
													 asv.getEsercizio_originale(),
													 asv.getPg_accertamento(),
													 new Long(1));

				totaleScad = (BigDecimal) prcImputazioneFinanziariaTable.get( key );
				if ( totaleScad == null || totaleScad.compareTo(new BigDecimal(0)) == 0)
					prcImputazioneFinanziariaTable.put( key, asv.getIm_voce());
				else
				{
					totaleScad = totaleScad.add( asv.getIm_voce());
					prcImputazioneFinanziariaTable.put( key, totaleScad );
				}
			}
		}
		return prcImputazioneFinanziariaTable;
	} catch (PersistencyException e) {
		throw handleException( e );
	} catch (IntrospectionException e) {
		throw handleException( e );
	}
}
private void aggiornaAccertamentoModificaTemporanea(UserContext userContext,Accertamento_modificaBulk accertamentoModTemporanea) throws ComponentException {

	try {
		Var_stanz_resBulk var_stanz_res = accertamentoModTemporanea.getVariazione();
		it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
			(it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
				it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession",
																it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
		Long pgVar = numerazione.creaNuovoProgressivo(userContext,CNRUserContext.getEsercizio(userContext), "VAR_STANZ_RES", "PG_VARIAZIONE", CNRUserContext.getUser(userContext));
		Var_stanz_resBulk varDaCanc = confirmVarStanzResTemporanea(userContext, var_stanz_res, pgVar);

		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
		Long pg = numHome.getNextPg(userContext,
				accertamentoModTemporanea.getEsercizio(),
				accertamentoModTemporanea.getCd_cds(),
				accertamentoModTemporanea.getCd_tipo_documento_cont(),
				accertamentoModTemporanea.getUser()!=null?accertamentoModTemporanea.getUser():((CNRUserContext)userContext).getUser());
		confirmAccertamentoModTemporanea(userContext, accertamentoModTemporanea, pg, varDaCanc);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(accertamentoModTemporanea, e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(accertamentoModTemporanea, e);
	} catch (Throwable e) {
		throw handleException(accertamentoModTemporanea, e);
	}
}
private void confirmAccertamentoModTemporanea(
	UserContext userContext,
	Accertamento_modificaBulk accertamentoModTemporanea,
	Long pg,
	Var_stanz_resBulk varDaCanc)
	throws IntrospectionException,PersistencyException, ComponentException {

	confirmAccertamentoModTemporanea(userContext,accertamentoModTemporanea, pg, varDaCanc, true);
}
private void confirmAccertamentoModTemporanea(
	UserContext userContext,
	Accertamento_modificaBulk accertamentoModTemporanea,
	Long pg,
	Var_stanz_resBulk varDaCanc,
	boolean deleteTemp)
	throws IntrospectionException,PersistencyException, ComponentException {

	if (pg == null)
		throw new PersistencyException("Impossibile ottenere un progressivo definitivo per la modifica dell'accertamento inserito!");

	Long tempPg = accertamentoModTemporanea.getPg_modifica();
	Accertamento_modificaHome omHome = (Accertamento_modificaHome) getHome(userContext, Accertamento_modificaBulk.class);
	SQLBuilder sql = omHome.createSQLBuilder();
	sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, accertamentoModTemporanea.getCd_cds());
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, accertamentoModTemporanea.getEsercizio());
	sql.addSQLClause( "AND", "pg_modifica", sql.EQUALS, tempPg);
	List result = omHome.fetchAll( sql );
	if (result.isEmpty())
		throw new ApplicationException("Modifica temporanea ad Accertamento residuo proprio non trovato. Impossibile procedere.");
	if (result.size()>1)
		throw new ApplicationException("Modifica temporanea ad Accertamento residuo proprio non individuata correttamente. Impossibile procedere.");

	Accertamento_modificaBulk acrModDaCanc = (Accertamento_modificaBulk) result.get(0);
	if (deleteTemp) {
		deleteBulk(userContext,acrModDaCanc);
		if (varDaCanc!=null)
			deleteBulk(userContext,varDaCanc);
	}
	Accertamento_modificaKey omkey = (Accertamento_modificaKey) accertamentoModTemporanea.getKey();
	omkey.setPg_modifica(pg);
	accertamentoModTemporanea.setPg_modifica(pg);
	insertBulk(userContext, accertamentoModTemporanea);

	BulkList dett = accertamentoModTemporanea.getAccertamento_mod_voceColl();

	for(Iterator it=dett.iterator();it.hasNext();) {
		Accertamento_mod_voceBulk acrModVoceTemp = (Accertamento_mod_voceBulk) it.next();

		Accertamento_mod_voceHome omvHome = (Accertamento_mod_voceHome) getHome(userContext, Accertamento_mod_voceBulk.class);
		sql = omvHome.createSQLBuilder();
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, acrModVoceTemp.getCd_cds());
		sql.addSQLClause( "AND", "esercizio", sql.EQUALS, acrModVoceTemp.getEsercizio());
		sql.addSQLClause( "AND", "pg_modifica", sql.EQUALS, tempPg);
		sql.addSQLClause( "AND", "ti_appartenenza", sql.EQUALS, acrModVoceTemp.getTi_appartenenza());
		sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, acrModVoceTemp.getTi_gestione());
		sql.addSQLClause( "AND", "cd_voce", sql.EQUALS, acrModVoceTemp.getCd_voce());
		sql.addSQLClause( "AND", "cd_centro_responsabilita", sql.EQUALS, acrModVoceTemp.getCd_centro_responsabilita());
		sql.addSQLClause( "AND", "cd_linea_attivita", sql.EQUALS, acrModVoceTemp.getCd_linea_attivita());
		result = omvHome.fetchAll( sql );
		if (result.isEmpty())
			throw new ApplicationException("Modifica temporanea ad Accertamento residuo dettaglio proprio non trovato. Impossibile procedere.");
		if (result.size()>1)
			throw new ApplicationException("Modifica temporanea ad Accertamento residuo dettaglio proprio non individuata correttamente. Impossibile procedere.");
		Accertamento_mod_voceBulk acrModVoceDaCanc = (Accertamento_mod_voceBulk) result.get(0);
		if (deleteTemp)
			deleteBulk(userContext, acrModVoceDaCanc);

		Accertamento_mod_voceKey omvkey = (Accertamento_mod_voceKey) acrModVoceTemp.getKey();
		omvkey.setPg_modifica(pg);
		acrModVoceTemp.setPg_modifica(pg);
		insertBulk(userContext, acrModVoceTemp);
	}
}
private Var_stanz_resBulk confirmVarStanzResTemporanea(
		UserContext userContext,
		Var_stanz_resBulk varTemporanea,
		Long pg)
		throws IntrospectionException,PersistencyException, ComponentException {

		return confirmVarStanzResTemporanea(userContext,varTemporanea, pg, true);
	}
private Var_stanz_resBulk confirmVarStanzResTemporanea(
	UserContext userContext,
	Var_stanz_resBulk varTemporanea,
	Long pg,
	boolean deleteTemp)
	throws IntrospectionException,PersistencyException, ComponentException {

	Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
	// in tal caso non esiste la variazione allo stanziamento residuo perchè non viene creata
	if (((CNRUserContext)userContext).getCd_unita_organizzativa().equals(uoEnte.getCd_unita_organizzativa()))
		return null;

	if (pg == null)
		throw new PersistencyException("Impossibile ottenere un progressivo definitivo per la modifica dell'accertamento inserito!");

	Long tempPg = varTemporanea.getPg_variazione();
	Var_stanz_resHome varHome = (Var_stanz_resHome) getHome(userContext, Var_stanz_resBulk.class);
	Var_stanz_resBulk varDaCanc = (Var_stanz_resBulk) varHome.findByPrimaryKey(new Var_stanz_resBulk(varTemporanea.getEsercizio(), tempPg));
	if (varDaCanc==null)
		throw new ApplicationException("Variazione allo stanziamento residuo temporaneo non trovato. Impossibile procedere.");

	Var_stanz_resKey varkey = (Var_stanz_resKey) varTemporanea.getKey();
	varkey.setPg_variazione(pg);
	varTemporanea.setPg_variazione(pg);
	insertBulk(userContext, varTemporanea);

	Ass_var_stanz_res_cdrHome assVarCdrHome = (Ass_var_stanz_res_cdrHome) getHome(userContext, Ass_var_stanz_res_cdrBulk.class);
	SQLBuilder sql = assVarCdrHome.createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, varTemporanea.getEsercizio());
	sql.addSQLClause( "AND", "pg_variazione", sql.EQUALS, tempPg);
	List dett = assVarCdrHome.fetchAll( sql );
	if (dett.isEmpty())
		throw new ApplicationException("Dettagli della ass. variazione allo stanziamento residuo temporaneo non trovati. Impossibile procedere.");

	// solo inserimento
	for(Iterator it=dett.iterator();it.hasNext();) {
		Ass_var_stanz_res_cdrBulk assVarCdrTemp = (Ass_var_stanz_res_cdrBulk) it.next();

		Ass_var_stanz_res_cdrKey assVarCdrkey = (Ass_var_stanz_res_cdrKey) assVarCdrTemp.getKey();
		assVarCdrkey.setPg_variazione(pg);
		assVarCdrTemp.setPg_variazione(pg);
		insertBulk(userContext, assVarCdrTemp);
	}

	Var_stanz_res_rigaHome varRigaHome = (Var_stanz_res_rigaHome) getHome(userContext, Var_stanz_res_rigaBulk.class);
	sql = varRigaHome.createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, varTemporanea.getEsercizio());
	sql.addSQLClause( "AND", "pg_variazione", sql.EQUALS, tempPg);
	dett = varRigaHome.fetchAll( sql );
	if (dett.isEmpty())
		throw new ApplicationException("Dettagli della variazione allo stanziamento residuo temporaneo non trovati. Impossibile procedere.");

	for(Iterator it=dett.iterator();it.hasNext();) {
		Var_stanz_res_rigaBulk varRigaTemp = (Var_stanz_res_rigaBulk) it.next();

		varRigaHome = (Var_stanz_res_rigaHome) getHome(userContext, Var_stanz_res_rigaBulk.class);
		Var_stanz_res_rigaBulk varRigaDaCanc = (Var_stanz_res_rigaBulk) varRigaHome.findByPrimaryKey(new Var_stanz_res_rigaBulk(varRigaTemp.getEsercizio(), tempPg, varRigaTemp.getPg_riga()));
		if (varRigaDaCanc==null)
			throw new ApplicationException("Modifica temporanea al dettaglio della variazione allo stanziamento residuo temporaneo non trovata. Impossibile procedere.");
		varRigaDaCanc.setPg_variazione(tempPg);
		if (deleteTemp)
			deleteBulk(userContext, varRigaDaCanc);

		Var_stanz_res_rigaKey varRigakey = (Var_stanz_res_rigaKey) varRigaTemp.getKey();
		varRigakey.setPg_variazione(pg);
		varRigaTemp.setPg_variazione(pg);
		insertBulk(userContext, varRigaTemp);
	}

	assVarCdrHome = (Ass_var_stanz_res_cdrHome) getHome(userContext, Ass_var_stanz_res_cdrBulk.class);
	sql = assVarCdrHome.createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio", sql.EQUALS, varTemporanea.getEsercizio());
	sql.addSQLClause( "AND", "pg_variazione", sql.EQUALS, tempPg);
	dett = assVarCdrHome.fetchAll( sql );
	if (dett.isEmpty())
		throw new ApplicationException("Dettagli della ass. variazione allo stanziamento residuo temporaneo non trovati. Impossibile procedere.");

	// solo cancellazione
	for(Iterator it=dett.iterator();it.hasNext();) {
		Ass_var_stanz_res_cdrBulk assVarCdrTemp = (Ass_var_stanz_res_cdrBulk) it.next();

		assVarCdrHome = (Ass_var_stanz_res_cdrHome) getHome(userContext, Ass_var_stanz_res_cdrBulk.class);
		Ass_var_stanz_res_cdrBulk assVarCdrDaCanc = (Ass_var_stanz_res_cdrBulk) assVarCdrHome.findByPrimaryKey(new Ass_var_stanz_res_cdrBulk(assVarCdrTemp.getEsercizio(), tempPg, assVarCdrTemp.getCd_centro_responsabilita()));
		if (assVarCdrDaCanc==null)
			throw new ApplicationException("Modifica temporanea alla ass. variazione allo stanziamento residuo temporaneo non trovata. Impossibile procedere.");
		assVarCdrDaCanc.setPg_variazione(tempPg);
		if (deleteTemp)
			deleteBulk(userContext, assVarCdrDaCanc);

	}

	// update su VAR_BILANCIO
	Var_bilancioHome varBilHome = (Var_bilancioHome) getHome(userContext, Var_bilancioBulk.class);
	sql = varBilHome.createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio_var_stanz_res", sql.EQUALS, varTemporanea.getEsercizio());
	sql.addSQLClause( "AND", "pg_var_stanz_res", sql.EQUALS, tempPg);
	dett = varBilHome.fetchAll( sql );

	for(Iterator it=dett.iterator();it.hasNext();) {
		Var_bilancioBulk varBil = (Var_bilancioBulk) it.next();

		varBil.setPg_var_stanz_res(pg);
		//varBil.setVar_stanz_res(varTemporanea);
		varBil.setDs_variazione(varBil.getDs_variazione().replaceAll(tempPg.toString(),pg.toString()));
		updateBulk(userContext, varBil);
	}

	//if (deleteTemp)
	//	deleteBulk(userContext,varDaCanc);
	// rimettiamo a negativo il progressivo
	varkey = (Var_stanz_resKey) varDaCanc.getKey();
	varkey.setPg_variazione(tempPg);
	varDaCanc.setPg_variazione(tempPg);
	return varDaCanc;
}
private void approvaVarStanzRes(
		UserContext userContext,
		Var_stanz_resBulk var) throws it.cnr.jada.comp.ComponentException {
		VariazioniStanziamentoResiduoComponentSession comp;
		try {
			comp = (VariazioniStanziamentoResiduoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRVARSTANZ00_EJB_VariazioniStanziamentoResiduoComponentSession", VariazioniStanziamentoResiduoComponentSession.class);
			Var_stanz_resBulk var_stanz_res = (Var_stanz_resBulk)comp.approva(userContext, var);
			var_stanz_res = (Var_stanz_resBulk)comp.esitaVariazioneBilancio(userContext, var_stanz_res);
		} catch (EJBException e) {
			throw handleException( e );
		} catch (RemoteException e) {
			throw handleException( e );
		}
}


public SQLBuilder selectElemento_voce_nextByClause(UserContext userContext, AccertamentoBulk accertamento, Elemento_voceBulk elemento_voce, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = getHome(userContext,Elemento_voceBulk.class).createSQLBuilder();
	if (clauses != null)
	  sql.addClause(clauses);

	sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, accertamento.getEsercizio()+1 );
	sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE );
    sql.addClause(FindClause.AND, "fl_solo_competenza", SQLBuilder.EQUALS, Boolean.FALSE );
	sql.addClause(FindClause.AND, "fl_azzera_residui", SQLBuilder.EQUALS, new Boolean( false) );

	sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL", "CLASSVOCENEW");
	sql.addTableToHeader("CLASSIFICAZIONE_VOCI", "CLASSLIV1NEW");
	sql.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE", "CLASSVOCENEW.ID_CLASSIFICAZIONE");
	sql.addSQLJoin("CLASSVOCENEW.ID_LIV1", "CLASSLIV1NEW.ID_CLASSIFICAZIONE");
	sql.addSQLClause(FindClause.AND, "CLASSLIV1NEW.TI_CLASSIFICAZIONE", SQLBuilder.ISNOTNULL, null);

	if (accertamento!=null && accertamento.getCapitolo()!=null) {
		Elemento_voceBulk evAccertamento = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
								new Elemento_voceBulk(accertamento.getCapitolo().getCd_elemento_voce(), accertamento.getCapitolo().getEsercizio(),
													  accertamento.getCapitolo().getTi_appartenenza(), accertamento.getCapitolo().getTi_gestione()));

		if (evAccertamento!=null && evAccertamento.getV_classificazione_voci().getId_classificazione()!=null) {
			SQLBuilder sqlExists = getHome(userContext,Classificazione_vociBulk.class).createSQLBuilder();
			sqlExists.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL", "CLASSVOCEOLD");
			sqlExists.addSQLClause(FindClause.AND, "CLASSVOCEOLD.ID_CLASSIFICAZIONE", SQLBuilder.EQUALS, evAccertamento.getV_classificazione_voci().getId_classificazione());
			sqlExists.addSQLJoin("CLASSVOCEOLD.ID_LIV1", "CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
			sqlExists.addSQLClause(FindClause.AND, "CLASSIFICAZIONE_VOCI.TI_CLASSIFICAZIONE", SQLBuilder.ISNOTNULL, null);
			sqlExists.addSQLJoin("CLASSIFICAZIONE_VOCI.TI_CLASSIFICAZIONE", "CLASSLIV1NEW.TI_CLASSIFICAZIONE" );
			sql.addSQLExistsClause(FindClause.AND, sqlExists);
		} else {
			sql.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
		}
	} else {
		sql.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
	}

	return sql;
}

public boolean existAssElementoVoceNew(UserContext userContext, AccertamentoBulk accertamento) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		Ass_evold_evnewHome ass_evold_evnewHome = (Ass_evold_evnewHome) getHome( userContext, Ass_evold_evnewBulk.class);
		Elemento_voceBulk evAccertamento = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
				new Elemento_voceBulk(accertamento.getCapitolo().getCd_elemento_voce(), accertamento.getCapitolo().getEsercizio(),
									  accertamento.getCapitolo().getTi_appartenenza(), accertamento.getCapitolo().getTi_gestione()));
		if (!ass_evold_evnewHome.findAssElementoVoceNewList(evAccertamento).isEmpty())
			return Boolean.TRUE;
	} catch (IntrospectionException e) {
		handleException(e);
	} catch (PersistencyException e) {
		handleException(e);
	}
	return Boolean.FALSE;
}

protected AccertamentoBulk validaCreaModificaElementoVoceNext(UserContext userContext, AccertamentoBulk accertamento) throws RemoteException, ComponentException {
	try {
		if (accertamento.getElemento_voce_next()!=null && accertamento.getElemento_voce_next().getCd_elemento_voce()!=null){
			if (existAssElementoVoceNew(userContext, accertamento)) {
				accertamento.setElemento_voce_next(null);
				accertamento.setToBeUpdated();
			} else {
				Elemento_voceBulk evAccertamento = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
						new Elemento_voceBulk(accertamento.getCapitolo().getCd_elemento_voce(), accertamento.getCapitolo().getEsercizio(),
											  accertamento.getCapitolo().getTi_appartenenza(), accertamento.getCapitolo().getTi_gestione()));

				Elemento_voceHome home = (Elemento_voceHome)getHome(userContext,Elemento_voceBulk.class);
				SQLBuilder sql = selectElemento_voce_nextByClause(userContext, accertamento, evAccertamento, new CompoundFindClause());
				sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.ESERCIZIO", SQLBuilder.EQUALS, accertamento.getElemento_voce_next().getEsercizio());
				sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.TI_APPARTENENZA", SQLBuilder.EQUALS, accertamento.getElemento_voce_next().getTi_appartenenza());
				sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.TI_GESTIONE", SQLBuilder.EQUALS, accertamento.getElemento_voce_next().getTi_gestione());
				sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, accertamento.getElemento_voce_next().getCd_elemento_voce());

				List listEv = home.fetchAll(sql);
				if (listEv.isEmpty())
					throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''impegno e quella di ribaltamento. Modificare la voce di ribaltamento!");

				if (evAccertamento.getFl_recon().equals(Boolean.FALSE) && accertamento.getElemento_voce_next().getFl_recon().equals(Boolean.TRUE) &&
						(accertamento.getContratto()==null || accertamento.getContratto().getPg_contratto()==null))
					throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''accertamento che non richiede l'indicazione "+
							"del contratto e quella di ribaltamento che ne richiede l'inserimento. Modificare la voce di ribaltamento "+
							"o inserire i dati relativi al contratto!");

				try {
					verificaGestioneTrovato(userContext, accertamento, accertamento.getElemento_voce_next());
				} catch (ApplicationException e) {
					throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''accertamento e quella di ribaltamento! " + e.getMessage());
				}

				if (!evAccertamento.getFl_inv_beni_patr().equals(accertamento.getElemento_voce_next().getFl_inv_beni_patr()))
					throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''accertamento e quella di ribaltamento " +
								"nella gestione dell''inventario. Modificare la voce di ribaltamento!");
			}
		}
	} catch (PersistencyException e) {
		handleException(e);
	}
	return accertamento;
}
public SQLBuilder selectLineaAttivita_centro_responsabilitaByClause(UserContext userContext, Pdg_vincoloBulk pdgVincolo, CdrBulk cdr, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	SQLBuilder sql = getHome(userContext, cdr, "V_CDR_VALIDO").createSQLBuilder();

	V_struttura_organizzativaHome strHome = (V_struttura_organizzativaHome) getHome(userContext, V_struttura_organizzativaBulk.class);
	sql.openParenthesis(FindClause.AND);
	for (java.util.Iterator j = strHome.findUoCollegateCDS(new Unita_organizzativaBulk(CNRUserContext.getCd_cds(userContext)), CNRUserContext.getEsercizio( userContext )).iterator(); j.hasNext();) {
		Unita_organizzativaBulk uoAfferente = (Unita_organizzativaBulk) j.next();
		for (java.util.Iterator x = strHome.findCDRCollegatiUO(uoAfferente, CNRUserContext.getEsercizio( userContext )).iterator(); x.hasNext();) {
			CdrBulk cdrAfferente = (CdrBulk) x.next();
			sql.addClause(FindClause.OR,"cd_centro_responsabilita", SQLBuilder.EQUALS, cdrAfferente.getCd_centro_responsabilita());
		}
	}
	sql.closeParenthesis();

	if (clauses != null) sql.addClause(clauses);

	return sql;
}
public SQLBuilder selectLineaAttivitaByClause(UserContext userContext, Pdg_vincoloBulk pdgVincolo, WorkpackageBulk linea, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	SQLBuilder sql = getHome(userContext, linea, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();

	sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND","ti_gestione",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);

	if (pdgVincolo.getCd_centro_responsabilita()!=null)
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,pdgVincolo.getCd_centro_responsabilita());
	else {
		V_struttura_organizzativaHome strHome = (V_struttura_organizzativaHome) getHome(userContext, V_struttura_organizzativaBulk.class);
		sql.openParenthesis(FindClause.AND);
		for (java.util.Iterator j = strHome.findUoCollegateCDS(new Unita_organizzativaBulk(CNRUserContext.getCd_cds(userContext)), CNRUserContext.getEsercizio( userContext )).iterator(); j.hasNext();) {
			Unita_organizzativaBulk uoAfferente = (Unita_organizzativaBulk) j.next();
			for (java.util.Iterator x = strHome.findCDRCollegatiUO(uoAfferente, CNRUserContext.getEsercizio( userContext )).iterator(); x.hasNext();) {
				CdrBulk cdrAfferente = (CdrBulk) x.next();
				sql.addClause(FindClause.OR,"cd_centro_responsabilita", SQLBuilder.EQUALS, cdrAfferente.getCd_centro_responsabilita());
			}
		}
		sql.closeParenthesis();
	}

	/**
	 * Escludo la linea di attività dell'IVA C20
	 */
	it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
	try {
		config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
	} catch (RemoteException e) {
		throw new ComponentException(e);
	} catch (EJBException e) {
		throw new ComponentException(e);
	}
	if (config != null){
		sql.addSQLClause( "AND", "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",  sql.NOT_EQUALS, config.getVal01());
	}

	if (clauses != null) sql.addClause(clauses);

	return sql;
}
public SQLBuilder selectElementoVoceByClause (UserContext userContext, Pdg_vincoloBulk pdgVincolo, Elemento_voceBulk elementoVoce, CompoundFindClause clause) throws ComponentException, PersistencyException {
	Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
	Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));

	SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_SPE").createSQLBuilder();

	sql.addSQLClause(FindClause.AND, "V_ELEMENTO_VOCE_PDG_SPE.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

	sql.openParenthesis(FindClause.AND);
	sql.addSQLClause(FindClause.OR, "V_ELEMENTO_VOCE_PDG_SPE.FL_PARTITA_GIRO", SQLBuilder.ISNULL, null);
	sql.addSQLClause(FindClause.OR, "V_ELEMENTO_VOCE_PDG_SPE.FL_PARTITA_GIRO", SQLBuilder.EQUALS, "N");
	sql.closeParenthesis();

	if (clause != null) sql.addClause(clause);

	return sql;
}
public SQLBuilder selectAssestatoRisorseCoperturaByClause (UserContext userContext, Pdg_vincoloBulk pdgVincolo, V_assestatoBulk assestato, CompoundFindClause clause) throws ComponentException, PersistencyException {
	SQLBuilder sql = getHome(userContext, assestato).createSQLBuilder();

	sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio( userContext ) );
	sql.addClause(FindClause.AND, "ti_appartenenza", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CDS );
	sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
	sql.addSQLClause(FindClause.AND, "IMPORTO_DISPONIBILE+IMPORTO_VINCOLI", SQLBuilder.GREATER, BigDecimal.ZERO);

	if (CNRUserContext.getEsercizio( userContext )<2022)
		sql.addClause(FindClause.AND, "esercizio_res", SQLBuilder.LESS, CNRUserContext.getEsercizio( userContext ) );
	else {
		sql.openParenthesis(FindClause.AND);
			sql.addClause(FindClause.OR, "esercizio_res", SQLBuilder.LESS, CNRUserContext.getEsercizio(userContext));
			sql.openParenthesis(FindClause.OR);
				sql.addClause(FindClause.AND, "progetto_dt_inizio", SQLBuilder.LESS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
				sql.openParenthesis(FindClause.AND);
					sql.addClause(FindClause.OR, "progetto_dt_fine", SQLBuilder.ISNOTNULL, null);
					sql.addClause(FindClause.OR, "progetto_dt_proroga", SQLBuilder.ISNOTNULL, null);
				sql.closeParenthesis();
				sql.openParenthesis(FindClause.AND);
					sql.openParenthesis(FindClause.OR);
						sql.addClause(FindClause.AND, "progetto_dt_fine", SQLBuilder.ISNOTNULL, null);
						sql.addClause(FindClause.AND, "progetto_dt_proroga", SQLBuilder.ISNULL, null);
						sql.addClause(FindClause.AND, "progetto_dt_fine", SQLBuilder.LESS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
					sql.closeParenthesis();
					sql.openParenthesis(FindClause.OR);
						sql.addClause(FindClause.AND, "progetto_dt_fine", SQLBuilder.ISNOTNULL, null);
						sql.addClause(FindClause.AND, "progetto_dt_proroga", SQLBuilder.ISNOTNULL, null);
						sql.addClause(FindClause.AND, "progetto_dt_proroga", SQLBuilder.LESS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
					sql.closeParenthesis();
				sql.closeParenthesis();
			sql.closeParenthesis();
		sql.closeParenthesis();
	}

	V_struttura_organizzativaHome strHome = (V_struttura_organizzativaHome) getHome(userContext, V_struttura_organizzativaBulk.class);
	sql.openParenthesis(FindClause.AND);
	for (java.util.Iterator j = strHome.findUoCollegateCDS(new Unita_organizzativaBulk(CNRUserContext.getCd_cds(userContext)), CNRUserContext.getEsercizio( userContext )).iterator(); j.hasNext();) {
		Unita_organizzativaBulk uoAfferente = (Unita_organizzativaBulk) j.next();
		for (java.util.Iterator x = strHome.findCDRCollegatiUO(uoAfferente, CNRUserContext.getEsercizio( userContext )).iterator(); x.hasNext();) {
			CdrBulk cdrAfferente = (CdrBulk) x.next();
			sql.addClause(FindClause.OR,"cd_centro_responsabilita", SQLBuilder.EQUALS, cdrAfferente.getCd_centro_responsabilita());
		}
	}
	sql.closeParenthesis();

	if (clause != null) sql.addClause(clause);

	return sql;
}

public void verificaDisponibilitaVincoliSpese(UserContext aUC,AccertamentoResiduoBulk accertamento) throws ComponentException
{
	try {
		if (accertamento.isStatoInesigibile() || accertamento.isStatoParzialmenteInesigibile() || accertamento.isStatoDubbio() || accertamento.isStatoGiudizialmenteControverso()) {
			if (accertamento.isStatoInesigibile() || accertamento.isStatoParzialmenteInesigibile()) { 
				if (accertamento.getIm_quota_inesigibile_da_ripartire().compareTo(BigDecimal.ZERO)!=0)
					throw new ApplicationException("Attenzione! Non risulta correttamente coperta con spese vincolate la quota inesigibile dell'accertamento residuo. Operazione non possibile!");
			} else { //accertamento.isStatoDubbio() || accertamento.isStatoGiudizialmenteControverso()
				if (Optional.ofNullable(accertamento.getImportoNonIncassato()).orElse(BigDecimal.ZERO).compareTo(
							Optional.ofNullable(accertamento.getIm_quota_inesigibile_ripartita()).orElse(BigDecimal.ZERO))<0)
					throw new ApplicationException("Attenzione! Le spese vincolate ("
							+ new it.cnr.contab.util.EuroFormat().format(Optional.ofNullable(accertamento.getIm_quota_inesigibile_ripartita()).orElse(BigDecimal.ZERO))							
							+ ") risultano superiori alla quota ancora da incassare ("
							+ new it.cnr.contab.util.EuroFormat().format(Optional.ofNullable(accertamento.getImportoNonIncassato()).orElse(BigDecimal.ZERO))							
							+ ") dell'accertamento residuo. Operazione non possibile!");
			}
			SaldoComponentSession session = createSaldoComponentSession();
			for (Pdg_vincoloBulk vincolo : accertamento.getPdgVincoliColl()) {
				if (vincolo.getIm_vincolo().compareTo(BigDecimal.ZERO)<=0)
					throw new ApplicationException("L'importo di vincolo relativo al CDR "+vincolo.getCd_centro_responsabilita()+
							" G.A.E. "+vincolo.getCd_linea_attivita()+" Voce "+vincolo.getElementoVoce().getCd_voce()+
							" non può essere negativo.");

				String error = session.checkDispObbligazioniAccertamenti(aUC, vincolo.getCd_centro_responsabilita(),
															   vincolo.getCd_linea_attivita(),
															   vincolo.getElementoVoce(),
															   vincolo.getEsercizio_res(),
															   vincolo.getEsercizio().equals(vincolo.getEsercizio_res())?
																	   Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA:
																	   Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO,
															   Parametri_cdsBulk.BLOCCO_IMPEGNI_ERROR);
				if (error!=null)
					throw new ApplicationException(error);
			}

			//verifico che le variazioni non siano completamente coperte da accertamenti
			Accertamento_vincolo_perenteHome home = (Accertamento_vincolo_perenteHome)getHome(aUC, Accertamento_vincolo_perenteBulk.class);
			VariazioniStanziamentoResiduoComponentSession session2 = Utility.createVariazioniStanziamentoResiduoComponentSession();
			for (Accertamento_vincolo_perenteBulk vincolo : accertamento.getAccertamentoVincoliPerentiColl()) {
				if (vincolo.getIm_vincolo().compareTo(BigDecimal.ZERO)<=0)
					throw new ApplicationException("L'importo di vincolo relativo alla variazione residua "+
							vincolo.getVariazioneResidua().getEsercizio()+"/"+vincolo.getVariazioneResidua().getPg_variazione()+
							" non può essere negativo.");

				List<Accertamento_vincolo_perenteBulk> listVincoli = home.cercaDettagliVincolati(vincolo.getVariazioneResidua());
				BigDecimal impVincolo = listVincoli.stream().map(e->e.getIm_vincolo()).reduce((x,y)->x.add(y)).get();

				//EFFETTUO LA SOMMA DEGLI IMPORTI MOVIMENTATI SOLO DALLE UO DI CUI DEVO RITIRARE LA SOMMA
				Var_stanz_resHome varHome = (Var_stanz_resHome)getHome(aUC, Var_stanz_resBulk.class);
				Collection<Var_stanz_res_rigaBulk> righeVar = varHome.findVariazioniRiga(vincolo.getVariazioneResidua());

				BigDecimal totVariazioneCDS = BigDecimal.ZERO;

				V_struttura_organizzativaHome strHome = (V_struttura_organizzativaHome) getHome(aUC, V_struttura_organizzativaBulk.class);
				for (java.util.Iterator j = strHome.findUoCollegateCDS(new Unita_organizzativaBulk(CNRUserContext.getCd_cds(aUC)), CNRUserContext.getEsercizio( aUC )).iterator(); j.hasNext();) {
					Unita_organizzativaBulk uoAfferente = (Unita_organizzativaBulk) j.next();
					for (java.util.Iterator x = strHome.findCDRCollegatiUO(uoAfferente, CNRUserContext.getEsercizio( aUC )).iterator(); x.hasNext();) {
						CdrBulk cdrAfferente = (CdrBulk) x.next();
						totVariazioneCDS = totVariazioneCDS.add(
							righeVar.stream().filter(e->e.getCd_cdr().equals(cdrAfferente.getCd_centro_responsabilita()))
															 .map(Var_stanz_res_rigaBulk::getIm_variazione)
															 .reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
					}
				}

				//recupero
				BigDecimal diff = totVariazioneCDS.abs().subtract(impVincolo);
				if (diff.compareTo(BigDecimal.ZERO)<0)
					throw new ApplicationException("La variazione residua "+vincolo.getVariazioneResidua().getEsercizio()+"/"+
					vincolo.getVariazioneResidua().getPg_variazione()+
				   " non può essere associata all'accertamento residuo per un importo superiore a "
				   + new it.cnr.contab.util.EuroFormat().format(diff.add(vincolo.getIm_vincolo()))+". Modificare il valore.");
			}

		}
	} catch (Exception e1) {
		throw new it.cnr.jada.comp.ComponentException(e1);
	}
}

public SQLBuilder selectVariazioneResiduaByClause (UserContext userContext, Accertamento_vincolo_perenteBulk vincolo, Var_stanz_resBulk variazione, CompoundFindClause clause) throws ComponentException, PersistencyException {
	SQLBuilder sql = getHome(userContext, variazione).createSQLBuilder();

	sql.addClause(FindClause.AND, "esercizio", SQLBuilder.LESS, CNRUserContext.getEsercizio( userContext ) );
	sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Var_stanz_resBulk.STATO_APPROVATA );
	sql.addClause(FindClause.AND, "tipologia", SQLBuilder.EQUALS, Var_stanz_resBulk.TIPOLOGIA_STO );
	sql.addClause(FindClause.AND, "fl_perenzione", SQLBuilder.EQUALS, Boolean.TRUE );
	sql.addClause(FindClause.AND, "tipologia_fin", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_ESTERNE );

	V_struttura_organizzativaHome strHome = (V_struttura_organizzativaHome) getHome(userContext, V_struttura_organizzativaBulk.class);
	sql.openParenthesis(FindClause.AND);
	for (java.util.Iterator j = strHome.findUoCollegateCDS(new Unita_organizzativaBulk(CNRUserContext.getCd_cds(userContext)), CNRUserContext.getEsercizio( userContext )).iterator(); j.hasNext();) {
		Unita_organizzativaBulk uoAfferente = (Unita_organizzativaBulk) j.next();
		for (java.util.Iterator x = strHome.findCDRCollegatiUO(uoAfferente, CNRUserContext.getEsercizio( userContext )).iterator(); x.hasNext();) {
			CdrBulk cdrAfferente = (CdrBulk) x.next();
			sql.addClause(FindClause.OR,"cd_centro_responsabilita", SQLBuilder.EQUALS, cdrAfferente.getCd_centro_responsabilita());
		}
	}
	sql.closeParenthesis();

	if (clause != null) sql.addClause(clause);

	sql.setOrderBy("esercizio", it.cnr.jada.util.OrderConstants.ORDER_DESC);
	sql.setOrderBy("pg_variazione", it.cnr.jada.util.OrderConstants.ORDER_DESC);
	return sql;
}

	private void validaAccertamentoPluriennale(UserContext uc, AccertamentoBulk bulk) throws ComponentException{


		for(Accertamento_pluriennaleBulk accPlur : bulk.getAccertamentiPluriennali()){
			if(accPlur.getAnno() == null || accPlur.getAnno() == 0){
				throw new ApplicationException("Impostare Anno Accertamento Pluriennale");
			}
			if( !isAnnoPluriennaleSuccessivo(bulk.getEsercizio(),accPlur.getAnno())){
				throw new ApplicationException("L'anno dell'Accertamento Pluriennale deve essere successivo all'anno corrente");
			}
			if(isAnnoDuplicato(bulk)){
				throw new ApplicationException("Risulta presente più volte lo stesso anno per l'Accertamento Pluriennale");
			}
			if(accPlur.getImporto() == null){
				throw new ApplicationException("Impostare Importo dell'Accertamento Pluriennale");
			}
		}
	}

	private boolean isAnnoDuplicato(AccertamentoBulk bulk){
		if ( bulk.getAccertamentiPluriennali().stream()
				.filter(ap -> Optional.ofNullable(ap.getAnno()).isPresent())
				.collect(Collectors.groupingBy(Accertamento_pluriennaleBulk::getAnno, Collectors.counting()))
				.values().stream().filter(e->e.compareTo(Long.decode("1"))>0).count()>0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private boolean isAnnoPluriennaleSuccessivo(Integer annoObbligazione, Integer annoObbPluriennale) {

		if(annoObbPluriennale.compareTo(annoObbligazione) <= 0){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}

}