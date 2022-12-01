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

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.Anagrafico_terzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseHome;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewBulk;
import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk;
import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.doccont00.bp.CRUDObbligazioneBP;
import it.cnr.contab.doccont00.core.DatiFinanziariScadenzeDTO;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioHome;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_anagraficoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.ejb.EJBCommonServices;

/* Gestisce documenti di tipo
	OBB - bilancio Cds
*/	

public class ObbligazioneComponent extends it.cnr.jada.comp.CRUDComponent implements IDocumentoContabileMgr,IObbligazioneMgr, ICRUDMgr, IPrintMgr, Cloneable,Serializable
{
	private final static int INSERIMENTO = 1;
	private final static int MODIFICA    = 2;
	private final static int CANCELLAZIONE    = 3;		
//@@<< CONSTRUCTORCST
    public  ObbligazioneComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
private Vector accorpaLineeAttivita (UserContext aUC,Vector lineeAttivita) throws ComponentException
{
	try
	{
		Vector newLineeAttivita = new Vector();
		HashMap hm = new HashMap();
		V_pdg_obbligazione_speBulk latt, stessaLatt;
		String key;
		for ( Iterator i = lineeAttivita.iterator(); i.hasNext(); )
		{
			latt = (V_pdg_obbligazione_speBulk) i.next();
			key = latt.getEsercizio().toString() + '-' + latt.getCd_centro_responsabilita() + '-' + latt.getCd_linea_attivita();
			stessaLatt = (V_pdg_obbligazione_speBulk) hm.get( key );
			if ( stessaLatt == null )
				hm.put( key, latt );
			else
			{
				/* anno 1 */
				stessaLatt.setIm_rj_ccs_spese_odc_altra_uo( stessaLatt.getIm_rj_ccs_spese_odc_altra_uo().add( latt.getIm_rj_ccs_spese_odc_altra_uo()));
				stessaLatt.setIm_rl_ccs_spese_ogc_altra_uo( stessaLatt.getIm_rl_ccs_spese_ogc_altra_uo().add( latt.getIm_rl_ccs_spese_ogc_altra_uo()));
				stessaLatt.setIm_rr_ssc_costi_odc_altra_uo( stessaLatt.getIm_rr_ssc_costi_odc_altra_uo().add( latt.getIm_rr_ssc_costi_odc_altra_uo()));
				stessaLatt.setIm_rt_ssc_costi_ogc_altra_uo( stessaLatt.getIm_rt_ssc_costi_ogc_altra_uo().add( latt.getIm_rt_ssc_costi_ogc_altra_uo()));
				/* anno 2 */				
				stessaLatt.setIm_rad_a2_spese_odc_altra_uo( stessaLatt.getIm_rad_a2_spese_odc_altra_uo().add(latt.getIm_rad_a2_spese_odc_altra_uo()));
				stessaLatt.setIm_raf_a2_spese_ogc_altra_uo( stessaLatt.getIm_raf_a2_spese_ogc_altra_uo().add(latt.getIm_raf_a2_spese_ogc_altra_uo()));
				/* anno 3 */				
				stessaLatt.setIm_ram_a3_spese_odc_altra_uo( stessaLatt.getIm_ram_a3_spese_odc_altra_uo().add( latt.getIm_ram_a3_spese_odc_altra_uo()));
				stessaLatt.setIm_rao_a3_spese_ogc_altra_uo( stessaLatt.getIm_rao_a3_spese_ogc_altra_uo().add( latt.getIm_rao_a3_spese_ogc_altra_uo()));
				hm.put( key, stessaLatt );	
				
			}		
			
		}

		for ( Iterator i = hm.values().iterator(); i.hasNext(); )
			newLineeAttivita.add( i.next());
			
		return newLineeAttivita;
			
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  creazione/modifica/eliminazione obbligazione con esercizio competenza diverso da esercizio scrivania
  *    PreCondition:
  *      Un'obbligazione e' stata creata/modificata/eliminata e l'esercizio di competenza dell'obbligazione
  *      e' maggiore dell'esercizio di scrivania.
  *    PostCondition:
  *      Nessun aggiornamento viene effettuato
  *  creazione obbligazione con esercizio competenza = esercizio scrivania
  *    PreCondition:
  *      Un'obbligazione e' stata creata e l'esercizio di competenza dell'obbligazione
  *      e' uguale all'esercizio di scrivania dell'esercizio di scrivania.
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInInserimento' che, per ogni voce del piano utilizzata nell'obbligazione calcola
  *      gli importi degli aggiornamenti da apportare ai saldi  e viene richiamato il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  *  modifica/eliminazione obbligazione con esercizio competenza = esercizio scrivania
  *    PreCondition:
  *      Un'obbligazione e' stata modificata/eliminata e l'esercizio di competenza dell'obbligazione
  *      e' uguale all'esercizio di scrivania dell'esercizio di scrivania.
  *    PostCondition:
  *      Viene chiamato il metodo 'aggiornaSaldiInModifica' che, per ogni voce del piano utilizzata nell'obbligazione calcola
  *      gli importi degli aggiornamenti da apportare ai saldi  e viene richiamato il metodo sulla Component di Gestione Saldi (SaldoComponent) che
  *      effettua tale aggiornamento
  
  * @param userContext lo user context
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> per la quale devono essere aggiornati i saldi relativi ai capitoli di spesa
  * @param azione indica l'azione effettuata sull'obbligazione e puo' assumere i valori INSERIMENTO, MODIFICA, CANCELLAZIONE
  *
 */

private void aggiornaCapitoloSaldoObbligazione (UserContext userContext,ObbligazioneBulk obbligazione, int azione) throws ComponentException
{
	try
	{
		// non si aggiornano i saldi di obbligazioni con esercizio di competenza diverso da esercizio di creazione
		if ( obbligazione.getEsercizio().compareTo( obbligazione.getEsercizio_competenza()) != 0 )
			return;
		if ( azione == INSERIMENTO )
			aggiornaSaldiInInserimento( userContext, obbligazione, Boolean.TRUE );
		else if ( azione == MODIFICA )
			aggiornaSaldiInModifica( userContext, 
											 obbligazione, 
											 obbligazione.getPg_ver_rec(),
											 Boolean.TRUE );
		else if ( azione == CANCELLAZIONE )
			aggiornaSaldiInModifica( userContext, 
											 obbligazione, 
											 new Long(obbligazione.getPg_ver_rec().longValue() + 1),
											 Boolean.TRUE);		
			
		
		/*		
		SaldoComponentSession session = createSaldoComponentSession();
		PrimaryKeyHashMap saldoVociMap = calcolaVociMapPerObbligazione( userContext, obbligazione );
		
		Voce_fBulk voce;
		BigDecimal im_voce;
		for ( Iterator i = saldoVociMap.keySet().iterator(); i.hasNext(); )
		{
			voce = (Voce_fBulk) i.next();
			im_voce = (BigDecimal) saldoVociMap.get( voce );
			if ( im_voce.compareTo( new BigDecimal(0) ) !=  0 )
				session.aggiornaObbligazioniAccertamenti( userContext, voce, obbligazione.getCd_cds(), im_voce );
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
		if ( docContabile instanceof ObbligazioneBulk )
		{
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) docContabile;
			Long pg_ver_rec = (Long) values.get("pg_ver_rec");
			if ( pg_ver_rec == null )
				throw new ApplicationException( "Aggiornamento in differita dello stato coge/coan dei documenti contabili impossibile (pg_ver_rec nullo)");
			if ( obbligazione.getPg_obbligazione().longValue() >= 0 ) //accertamento non temporaneo
				callDoRiprocObb(userContext, obbligazione, pg_ver_rec );			
		}
	}
	catch ( Exception e )
	{
		throw handleException(e);
	}
}
/**
 * Aggiornamento in differita dei saldi dell'obbligazione .
 * Un documento amministrativo di spesa che agisce in modalità transazionale ha creato/modificato gli importi 
 * relativi ad un'obbligazione; i saldi di tale obbligazione non possono essere aggiornati subito in quanto
 * tale operazione genererebbe dei lock sulle voci del piano che non ne consentirebbere l'utilizzo ad altri utenti;
 * pertanto l'aggiornamento dei saldi dell'obbligazione viene differito al momento del salvataggio
 * del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna saldi per obbligazione creata 
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       bilancio che e' stata creata nel contesto transazionale del documento ammninistrativo ( progressivo
 *       obbligazione < 0)
 * Post: I saldi dell'obbligazione sono stati aggiornati nel metodo 'aggiornaSaldiInInserimento'
 *
 * Nome: Aggiorna saldi per obbligazione esistente
 * Pre:  Una richiesta di aggiornamento dei saldi in differita e' stata generata per una obbligazione su capitoli di
 *       bilancio che non e' stata creata nel contesto transazionale del documento ammninistrativo ( progressivo
 *       obbligazione > 0)
 * Post: I saldi dell'obbligazione sono stati aggiornati nel metodo 'aggiornaSaldiInModifica'
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	docContabile	l'ObbligazioneBulk per cui aggiornare i saldi
 * @param	values	la Map che contiene il "pg_ver_rec" iniziale dell'obbligazione e il "checkDisponibilitaCassaEseguito" che indica se
 *          l'utente ha richiesto la forzatura della disponibilità di cassa (parametro impostato dalla Gestione Obbligazione)
 * @param	param il parametro che indica se il controllo della disp. di cassa e' necessario (parametro impostato dalla Gestione dei doc. amm.)
*/
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile, Map values, OptionRequestParameter param  ) throws ComponentException
{
	try
	{
		if ( docContabile instanceof ObbligazioneBulk )
		{
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) docContabile;
			Boolean forzaDispCassa = (Boolean) values.get("checkDisponibilitaCassaEseguito");
			Boolean isObbTemp = (Boolean) values.get("isObbTemp");	
			if ( forzaDispCassa == null && param != null )
				forzaDispCassa = new Boolean (!param.isCheckDisponibilitaDiCassaRequired().booleanValue());
			if ( isObbTemp != null || obbligazione.getPg_obbligazione().longValue() < 0 ) //obbligazione appena inserita
				aggiornaSaldiInInserimento( userContext, obbligazione, forzaDispCassa );
			else {
				/* MI - Modifica effettuata per la risoluzione di una anomalia
				 * dovuta alla conversione a WAS 5 (???)
				*/
				Long pg_ver_rec = (Long) values.get("pg_ver_rec");
				if ( pg_ver_rec == null )
					throw new ApplicationException( "Aggiornamento in differita dei saldi dei documenti contabili impossibile (pg_ver_rec nullo)");
				aggiornaSaldiInModifica( userContext, obbligazione, pg_ver_rec, forzaDispCassa );			
			}
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
 * Nome: Aggiorna saldi in contesto non transazionale
 * Pre:  Un'obbligazione e' stata creata in un contesto non transazionale
 *			La disponibilità di cassa e' già stata verificata
 * Post: Per ogni Voce del piano presente nell'obbligazione viene richiamato il metodo sulla Component di gestione dei Saldi (SaldoComponent) per incrementare
 *       il saldo del capitolo corrispondente senza effettuare la verifica della disponibilità di cassa.
 *
 * Nome: Aggiorna saldi in contesto transazionale senza verifica disponibilità di cassa sul capitolo
 * Pre:  Un'obbligazione e' stata creata in un contesto transazionale
 *       L'utente ha già confermato che intende forzare la verifica della disponibilità di cassa sui capitoli interessati dall'obbligazione.
 * Post: Per ogni Voce del piano presente nell'obbligazione viene richiamato il metodo sulla Component di gestione dei Saldi (SaldoComponent) per incrementare
 *       il saldo del capitolo corrispondente senza effettuare la verifica della disponibilità di cassa.
 *
 * Nome: Aggiorna saldi in contesto transazionale con verifica disponibilità di cassa sul capitolo
 * Pre:  Un'obbligazione e' stata creata in un contesto transazionale
 *       L'utente non ha ancora confermato che intende forzare la verifica della disponibilità di cassa sui capitoli interessati dall'obbligazione.
 * Post: Per ogni Voce del piano presente nell'obbligazione viene richiamato il metodo sulla Component di gestione dei Saldi (SaldoComponent) per incrementare
 *       il saldo del capitolo corrispondente richiedendo di effettuare la verifica della disponibilità di cassa.
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	obbligazione	l'ObbligazioneBulk per cui aggiornare i saldi
 * @param	forzaDispCassa il parametro che indica se l'utente ha confermato di forzare il controllo della disp. di cassa 
 *
 */
private void aggiornaSaldiInInserimento( UserContext userContext, ObbligazioneBulk obbligazione, Boolean forzaDispCassa) throws ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();
	PrimaryKeyHashMap saldiDaAggiornare;
	try {
		saldiDaAggiornare = obbligazione.getVociMap(((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(userContext));
	} catch (PersistencyException e) {
		throw handleException(e);
	}
	for ( Iterator i = saldiDaAggiornare.keySet().iterator(); i.hasNext(); )
	{
		IVoceBilancioBulk voce = (IVoceBilancioBulk) i.next();
		BigDecimal im_voce = (BigDecimal) saldiDaAggiornare.get(voce);
					/* il check della disponabilità di cassa deve essere eseguito solo se 
					    l'utente non ha ancora avuto il warning sulla disp.cassa oppure
					    l'utente ha avuto il warning sulla disp.cassa e ha risposto no */
		boolean checkDispCassa = forzaDispCassa == null || 
 								 forzaDispCassa != null && !forzaDispCassa.booleanValue();
		if (voce instanceof Voce_fBulk)
			session.aggiornaObbligazioniAccertamenti( userContext, (Voce_fBulk)voce, obbligazione.getCd_cds(), im_voce,
					Voce_f_saldi_cmpBulk.TIPO_COMPETENZA,
					checkDispCassa);
		/*
		 * Aggiorno i Saldi per CDR/Linea
		 */
		Obbligazione_scad_voceBulk osv;
		Obbligazione_scadenzarioBulk os;
		for ( Iterator j = obbligazione.getObbligazione_scadenzarioColl().iterator(); j.hasNext(); )
		{
	      os = (Obbligazione_scadenzarioBulk) j.next();
	      for ( int index = os.getObbligazione_scad_voceColl().size() - 1; index >= 0 ; index--)
	      {
		     osv = (Obbligazione_scad_voceBulk) os.getObbligazione_scad_voceColl().get( index );
			 session.aggiornaObbligazioniAccertamenti( userContext, osv.getCd_centro_responsabilita(), osv.getCd_linea_attivita(), voce, obbligazione.getEsercizio_originale(),obbligazione.isObbligazioneResiduoImproprio()?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,osv.getIm_voce(),obbligazione.getCd_tipo_documento_cont());
	      }
        }
	}
	checkDispObbligazioniAccertamenti(userContext,obbligazione);	
}
/**
 *
 * Pre-post-conditions:
 *
 *
 * Nome: Aggiorna saldi in contesto non transazionale
 * Pre:  Un'obbligazione e' stata modificata/eliminata in un contesto non transazionale
 *			La disponibilità di cassa e' già stata verificata
 * Post: Per ogni V_mod_saldi_obbligBulk presente nel database a fronte dell'obbligazione e del suo pg_ver_rec
 *       e' stato richiamato il metodo sulla Component di gestione dei Saldi (SaldoCompoennt) per aggiornare
 *       il saldo del capitolo corrispondente senza effettuare la verifica della disponibilità di cassa; se necessario
 *       anche i saldi relativi ai mandati e al pagato vengono aggiornati.
 *
 * Nome: Aggiorna saldi in contesto transazionale senza verifica disponibilità di cassa sul capitolo
 * Pre:  Un'obbligazione e' stata modificata in un contesto transazionale
 *       L'utente ha già confermato che intende forzare la verifica della disponibilità di cassa sui capitoli interessati dall'obbligazione.
 * Post: Per ogni V_mod_saldi_obbligBulk presente nel database a fronte dell'obbligazione e del suo pg_ver_rec
 *       e' stato richiamato il metodo sulla Component di gestione dei Saldi (SaldoCompoennt) per aggiornare
 *       il saldo del capitolo corrispondente senza effettuare la verifica della disponibilità di cassa
 *
 * Nome: Aggiorna saldi in contesto transazionale con verifica disponibilità di cassa sul capitolo
 * Pre:  Un'obbligazione e' stata modificata in un contesto transazionale
 *       L'utente non ha ancora confermato che intende forzare la verifica della disponibilità di cassa sui capitoli interessati dall'obbligazione.
 * Post: Per ogni V_mod_saldi_obbligBulk presente nel database a fronte dell'obbligazione e del suo pg_ver_rec
 *       e' stato richiamato il metodo sulla Component di gestione dei Saldi (SaldoCompoennt) per aggiornare
 *       il saldo del capitolo corrispondente richiedendo di effettuare la verifica della disponibilità di cassa.
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	obbligazione	l'ObbligazioneBulk per cui aggiornare i saldi
 * @param	pg_ver_rec	il "pg_ver_rec" iniziale dell'obbligazione 
 * @param	forzaDispCassa il parametro che indica se l'utente ha confermato di forzare il controllo della disp. di cassa e' necessario
 *
 */
private void aggiornaSaldiInModifica( UserContext userContext, ObbligazioneBulk obbligazione, Long pg_ver_rec, Boolean  forzaDispCassa  ) throws it.cnr.jada.persistency.PersistencyException, ComponentException, java.rmi.RemoteException
{
	SaldoComponentSession session = createSaldoComponentSession();
	
	/* in realtà siamo sempre a competenza ma è meglio verificare */
	String ti_competenza_residuo;
	String tipo_imp;
	if ( obbligazione.isResiduo() )
		ti_competenza_residuo = ReversaleBulk.TIPO_RESIDUO;
	else
		ti_competenza_residuo = ReversaleBulk.TIPO_COMPETENZA;
		
	List saldiDaAggiornare = ((V_mod_saldi_obbligHome)getHome( userContext, V_mod_saldi_obbligBulk.class )).findModificheSaldiFor( obbligazione, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornare.size() == 0 )
		throw new ApplicationException( "Attenzione! I saldi relativi all'impegno " + obbligazione.getEsercizio_originale() + "/" + obbligazione.getPg_obbligazione() + " non possono essere aggiornati perchè l'impegno non e' presente nello storico.");
	
	for ( Iterator i = saldiDaAggiornare.iterator(); i.hasNext(); )
	{
		V_mod_saldi_obbligBulk modSaldo = (V_mod_saldi_obbligBulk) i.next();
		if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 )
		{
			Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), obbligazione.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );

			/* il check della disponabilità di cassa deve essere eseguito solo se 
			    l'importo delta del saldo e' positivo e
			    l'utente non ha ancora avuto il warning sulla disp.cassa oppure
		 	   l'utente ha avuto il warning sulla disp.cassa e ha risposto no */
			boolean checkDispCassa = modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) > 0 &&
								(forzaDispCassa == null || 
								forzaDispCassa != null && !forzaDispCassa.booleanValue());
			session.aggiornaObbligazioniAccertamenti( userContext, voce, obbligazione.getCd_cds(), modSaldo.getIm_delta_voce(), ti_competenza_residuo, checkDispCassa);

			if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaMandatiReversali( userContext, voce, obbligazione.getCd_cds(), modSaldo.getIm_delta_man_voce(), ti_competenza_residuo, checkDispCassa);

			if ( modSaldo.getIm_delta_pag_voce().compareTo( new BigDecimal(0) ) != 0 )
				session.aggiornaPagamentiIncassi( userContext, voce, obbligazione.getCd_cds(), modSaldo.getIm_delta_pag_voce(), ti_competenza_residuo);
		}		
			
	}	
	/*
	* Aggiorno i Saldi per CDR/Linea
	*/	
	List saldiDaAggiornareCdrLinea = ((V_mod_saldi_obblig_scad_voceHome)getHome( userContext, V_mod_saldi_obblig_scad_voceBulk.class )).findModificheSaldiFor( obbligazione, pg_ver_rec );
	if ( userContext.isTransactional() && saldiDaAggiornareCdrLinea.size() == 0 )
		throw new ApplicationException( "Attenzione! I saldi relativi all'impegno " + obbligazione.getEsercizio_originale() + "/" + obbligazione.getPg_obbligazione() + " non possono essere aggiornati perchè l'impegno non e' presente nello storico.");
		
	for ( Iterator i = saldiDaAggiornareCdrLinea.iterator(); i.hasNext(); )
	{
		V_mod_saldi_obblig_scad_voceBulk modSaldo = (V_mod_saldi_obblig_scad_voceBulk) i.next();
		Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), obbligazione.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
		/* il check della disponabilità di cassa deve essere eseguito solo se 
			l'importo delta del saldo e' positivo e
			l'utente non ha ancora avuto il warning sulla disp.cassa oppure
		   l'utente ha avuto il warning sulla disp.cassa e ha risposto no */
		boolean checkDispCassa = modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) > 0 &&
							(forzaDispCassa == null || 
							forzaDispCassa != null && !forzaDispCassa.booleanValue());
		
		if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0)) != 0 ) {
			// nel caso di modifiche agli impegni residui propri 
			// non deve variare il valore sul campo IM_OBBL_RES_IMP
			if (!obbligazione.isObbligazioneResiduo())
				session.aggiornaObbligazioniAccertamenti( userContext, modSaldo.getCd_centro_responsabilita(), modSaldo.getCd_linea_attivita(), voce, modSaldo.getEsercizio_originale(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,modSaldo.getIm_delta_voce(),modSaldo.getCd_tipo_documento_cont());
		}
		if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaMandatiReversali( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(),modSaldo.getIm_delta_man_voce(),modSaldo.getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA)?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO,checkDispCassa);
		if ( modSaldo.getIm_delta_pag_voce().compareTo( new BigDecimal(0) ) != 0 )
			session.aggiornaPagamentiIncassi( userContext,modSaldo.getCd_centro_responsabilita(),modSaldo.getCd_linea_attivita(),voce,modSaldo.getEsercizio_originale(), modSaldo.getIm_delta_pag_voce());
				
	}
	// aggiorniamo i saldi legati alle modifiche agli impegni residui
	aggiornaSaldiImpegniResiduiPropri(userContext,obbligazione);
	if (obbligazione.isObbligazioneResiduo()) {
		// aggiorniamo il progressivo in definitivo
		Obbligazione_modificaBulk obbMod = ((ObbligazioneResBulk) obbligazione).getObbligazione_modifica();
		if (obbMod!=null && obbMod.isTemporaneo()) {
			aggiornaObbligazioneModificaTemporanea(userContext, obbMod);
		}
	}
	checkDispObbligazioniAccertamenti(userContext,obbligazione);
}
private void aggiornaSaldiImpegniResiduiPropri (UserContext uc,ObbligazioneBulk obbligazione) throws ComponentException
{
	if (obbligazione.isObbligazioneResiduo()) {
		ObbligazioneResBulk obbRes = (ObbligazioneResBulk) obbligazione;
		Obbligazione_modificaBulk obbMod = obbRes.getObbligazione_modifica();
		if (obbMod!=null && obbMod.isTemporaneo()) {
			SaldoComponentSession session = createSaldoComponentSession();
			try
			{
				for ( Iterator i = obbMod.getObbligazione_mod_voceColl().iterator(); i.hasNext(); ) 
				{
					Obbligazione_mod_voceBulk obbModVoce = (Obbligazione_mod_voceBulk) i.next();
					
					session.aggiornaImpegniResiduiPropri(
						uc,
						obbModVoce.getCd_centro_responsabilita(),
						obbModVoce.getCd_linea_attivita(),
						obbModVoce.getVoce(),
						obbMod.getObbligazione().getEsercizio_originale(),
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
private void checkDispObbligazioniAccertamenti(UserContext userContext, ObbligazioneBulk obbligazione) throws ComponentException {
  BigDecimal importo = Utility.ZERO;
  Obbligazione_scad_voceBulk scad_voceDB;
  Parametri_cdsBulk parametri_cds = null;
  try{
	parametri_cds = (Parametri_cdsBulk)getHome( userContext,Parametri_cdsBulk.class ).findByPrimaryKey(new Parametri_cdsBulk(CNRUserContext.getCd_cds(userContext),CNRUserContext.getEsercizio(userContext)));
  }catch(PersistencyException e){
  	throw new ComponentException(e);
  }
  String messaggio = "";
  if (parametri_cds != null && (parametri_cds.getBlocco_impegni_cdr_gae().equals(Parametri_cdsBulk.BLOCCO_IMPEGNI_ERROR) ||
								parametri_cds.getBlocco_impegni_cdr_gae().equals(Parametri_cdsBulk.BLOCCO_IMPEGNI_WARNING))){
	try {
		SQLBroker broker = getHome(userContext,Obbligazione_scad_voceBulk.class).createBroker(((ObbligazioneHome)getHome( userContext,ObbligazioneBulk.class )).findObbligazione_scad_voceDistinctList(obbligazione));
		while(broker.next()){
		   String cd_voce = (String)broker.fetchPropertyValue("cd_voce",String.class);
		   String cd_centro_responsabilita = (String)broker.fetchPropertyValue("cd_centro_responsabilita",String.class);
		   String cd_linea_attivita = (String)broker.fetchPropertyValue("cd_linea_attivita",String.class);
		   if ((!obbligazione.isCheckDisponibilitaCdrGAEEseguito()||
				parametri_cds != null && parametri_cds.getBlocco_impegni_cdr_gae().equals(Parametri_cdsBulk.BLOCCO_IMPEGNI_ERROR))){
			  Voce_fBulk voce = new Voce_fBulk( cd_voce, obbligazione.getEsercizio(), obbligazione.getTi_appartenenza(), obbligazione.getTi_gestione() );
			  try {
				  String mess = createSaldoComponentSession().checkDispObbligazioniAccertamenti(
					   userContext,
					   cd_centro_responsabilita,
					   cd_linea_attivita,
					   voce,
					   obbligazione.getEsercizio_originale(),
					   (obbligazione.isObbligazioneResiduo()||obbligazione.isObbligazioneResiduoImproprio())?Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO:Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA,
					   parametri_cds.getBlocco_impegni_cdr_gae());
				  if (parametri_cds != null && mess != null && parametri_cds.getBlocco_impegni_cdr_gae().equals(Parametri_cdsBulk.BLOCCO_IMPEGNI_ERROR))
					throw new ApplicationException(mess);			       
				  if (mess != null)     
					messaggio = messaggio + (messaggio.equals("")?"":"<BR>") + mess;
			  }catch (RemoteException e) {
				throw new ComponentException(e);
			  }
		   }
		}
	}catch (IntrospectionException e) {
		throw new ComponentException(e);
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
  }  		
  if (!messaggio.equals("")){
	if (parametri_cds != null && parametri_cds.getBlocco_impegni_cdr_gae().equals(Parametri_cdsBulk.BLOCCO_IMPEGNI_WARNING))
	   throw handleException( new CheckDisponibilitaCdrGAEFailed(messaggio));
  }

}

//^^@@
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      scadenza(n+1) esiste
  *      scadenza(n+1).importo > differenza in scadenza(n).importo
  *      scadenza(n+1) non ha documenti amministrativi associati
  *    PostCondition:
  *      Il sistema eseguirà l'aggiornamento dell'importo della scadenza successiva (n+1) dell'obbligazione aggiungendo la differenza fra il nuovo e vecchio importo della scadenza in aggiornamento. 
  *      La differenza è espressa come (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio)
  *  scadenza(n+1).importo <= differenza in scadenza(n).importo
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma l'aumento dell'importo della scadenza(n) supera il valore dell'importo dell'ultima scadenza dell'obbligazione. Una formula per questa condizione sarebbe (scadenzario(n+1).importo - (scadenzario(n).importo_nuovo - scadenzario(n).importo_vecchio) > 0)
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché l'aumento dell'importo della scadenza(n) è maggiore all'importo dell'ultima scadenza (cercarebbe settare l'importo <= 0). L'attività non è consentita.
  *  scadenza(n+1) non esiste
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) alla scadenza in elaborazione (scadenza(n)), ma la scadenza in aggiornamento è l'ultima scadenza dell'obbligazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'aggiornamento in automatico dell'importo non è possibile perché non esiste una scadenza successiva. L'attività non è consentita.
  *  scadenza(n+1) ha doc amministrativi associati
  *    PreCondition:
  *      L'utente richiede l'aggiornamento in automatico dell'importo della scadenza successiva (scadenza(n+1)) 
  *      alla scadenza in elaborazione (scadenza(n)), ma la scadenza (n+1) ha documenti amministrativi associati
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la modifica della scadenza non è valida.
  *
  * @param aUC lo user context 
  * @param scadenzario l'istanza di  <code>Obbligazione_scadenzarioBulk</code> della quale deve essere individuata la scadenza successiva per aggiornarne l'importo
  * @return la scadenza successiva con l'importo modificato
 */

public Obbligazione_scadenzarioBulk aggiornaScadenzaSuccessivaObbligazione (UserContext aUC,Obbligazione_scadenzarioBulk scadenzario) throws ComponentException
{
	ObbligazioneBulk obbligazione = scadenzario.getObbligazione();
	Obbligazione_scadenzarioBulk scadSuccessiva, scadSuccessivaIniziale, scadSuccessivaNew;

	//individuo la scadenza successiva e calcolo quanto aggiungere/togliere alla scadenza successiva
	BigDecimal delta = scadenzario.getScadenza_iniziale().getIm_scadenza().subtract(scadenzario.getIm_scadenza());
	int index = 0, scadSuccessivaIndex = 0;

	
	for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	{
		if ( ((Obbligazione_scadenzarioBulk) i.next()).equalsByPrimaryKey( scadenzario ) )
			scadSuccessivaIndex = index + 1;
		index++;
	}
	//non esiste scadenza successiva
	//se residuo proprio e il delta è positivo (è stata diminuito l'importo della scadenza precedente)
	//inserisco una nuova scadenza		
	if ( scadSuccessivaIndex == obbligazione.getObbligazione_scadenzarioColl().size() ) {
		//Lello - Sdoppiamento scadenza anche su obbligazione 
		//if (!obbligazione.isObbligazioneResiduo() || delta.doubleValue() < 0)
		if (delta.doubleValue() < 0)
			throw handleException( new ApplicationException( "Non esiste una scadenza successiva da aggiornare" ));
		else {
			scadSuccessivaNew = new Obbligazione_scadenzarioBulk();
			scadSuccessivaNew.setDt_scadenza(scadenzario.getDt_scadenza());
			scadSuccessivaNew.setDs_scadenza(scadenzario.getDs_scadenza());
			scadSuccessivaNew.setIm_scadenza(Utility.ZERO);
			scadSuccessivaIndex = obbligazione.addToObbligazione_scadenzarioColl(scadSuccessivaNew);
			generaDettagliScadenzaObbligazione( aUC, obbligazione, scadSuccessivaNew, false);
		}
	}
		
	scadSuccessiva = (Obbligazione_scadenzarioBulk) obbligazione.getObbligazione_scadenzarioColl().get( scadSuccessivaIndex );

	//scadenza successiva ha importo inferiore a delta		
	if ( delta.doubleValue() < 0 && (scadSuccessiva.getIm_scadenza().add(delta).doubleValue() < 0 ))
		throw handleException( new ApplicationException( "Modifica impossibile: l'importo della scadenza successiva e' inferiore all'importo da aggiornare" ));
		
	//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati
	if ( scadSuccessiva.getPg_doc_passivo() != null )		
		throw new ApplicationException( "Modifica impossibile: la scadenza successiva e' associata a doc. amministrativi");

	//aggiorno importo scadenza successiva
	scadSuccessivaIniziale = new Obbligazione_scadenzarioBulk();
	scadSuccessivaIniziale.setIm_scadenza( scadSuccessiva.getIm_scadenza());
	scadSuccessiva.setScadenza_iniziale( scadSuccessivaIniziale);
	
	scadSuccessiva.setIm_scadenza( scadSuccessiva.getIm_scadenza().add( delta ) );

	scadenzario.setFl_aggiorna_scad_successiva( new Boolean( false) );
		
	scadSuccessiva.setToBeUpdated();
	scadenzario.setToBeUpdated();
		
	//Carico la lista delle voci con gli importi da ripartire
	PrimaryKeyHashtable hashVociList = new PrimaryKeyHashtable();  

	Obbligazione_scadenzarioBulk os;
	BigDecimal totaleSelVoci = new BigDecimal(0);

	for (int i=0; i<2; i++)
	{
		if (i==0) 
			os = scadenzario;
		else 
			os = scadSuccessiva;

		for ( Iterator s = os.getObbligazione_scad_voceColl().iterator(); s.hasNext(); )
		{
			Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk) s.next();
			Boolean trovato = new Boolean(Boolean.FALSE);
	
			for ( Enumeration h = hashVociList.keys(); h.hasMoreElements(); ) 
			{
				V_assestatoBulk key = (V_assestatoBulk)h.nextElement();
				if (key.getEsercizio().equals(osv.getEsercizio()) &&
			        key.getEsercizio_res().equals(osv.getObbligazione_scadenzario().getObbligazione().getEsercizio_originale()) &&
			        key.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
			        key.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&
			        key.getTi_appartenenza().equals(osv.getTi_appartenenza()) &&
			        key.getTi_gestione().equals(osv.getTi_gestione()) &&
			        key.getCd_voce().equals(osv.getCd_voce()))
				{
					key.setImp_da_assegnare(key.getImp_da_assegnare().add(osv.getIm_voce()));
					hashVociList.put(key, new BigDecimal(0));
					totaleSelVoci = totaleSelVoci.add(osv.getIm_voce());
					trovato = Boolean.TRUE;
					break;
				}
			}
			if (!trovato)
			{
				V_assestatoBulk voceSel = new V_assestatoBulk(osv.getEsercizio(),
														      osv.getObbligazione_scadenzario().getObbligazione().getEsercizio_originale(),
														      osv.getCd_centro_responsabilita(),
														      osv.getCd_linea_attivita(),
														      osv.getTi_appartenenza(),
														      osv.getTi_gestione(),
															  osv.getObbligazione_scadenzario().getObbligazione().getCd_elemento_voce());
				voceSel.setCd_voce(osv.getCd_voce());
				voceSel.setImp_da_assegnare(Utility.nvl(osv.getIm_voce()));
				hashVociList.put(voceSel, new BigDecimal(0));
				totaleSelVoci = totaleSelVoci.add(Utility.nvl(osv.getIm_voce()));
			}
		}
	}

	//Valorizzo il campo Percentuale che utilizzerò per individuare gli importi da attribuire ad ogni scadenza
	for ( Enumeration e = hashVociList.keys(); e.hasMoreElements(); ) 
	{
		V_assestatoBulk voceSel = (V_assestatoBulk)e.nextElement();
		if (totaleSelVoci.compareTo(Utility.ZERO)<=0)
			voceSel.setPrc_da_assegnare(new BigDecimal(0));
		else
			voceSel.setPrc_da_assegnare(Utility.nvl(voceSel.getImp_da_assegnare()).divide(totaleSelVoci, 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
	}				

	spalmaImportiSuScadenza(aUC, scadenzario, hashVociList);
	spalmaImportiSuScadenza(aUC, scadSuccessiva, hashVociList);

	return scadSuccessiva;
}
/**
 *
 * Pre-post-conditions:
 *
 * Nome: Aggiorna stato COAN/COGE di doc. amministrativi
 * Pre:  Una richiesta di modifica di una obbligazione o di storno di una obbligazione definitiva e' stata generata.
 * Post: E' stata richiamata la stored procedure che provvede ad aggiornare gli stati COAN/COGE degli eventuali doc. amministrativi
 *       contabilizzati sull'obbligazione
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param docContabile <code>ObbligazioneBulk</code> l'obbligazione modificata o eliminata
 * 
*/

private void aggiornaStatoCOAN_COGEDocAmm( UserContext userContext, ObbligazioneBulk docContabile  )  throws  ComponentException
{
	callDoRiprocObb(userContext, docContabile, null);
}
/**
  *  creazione obbligazione
  *    PreCondition:
  *   	 Una nuova obbligazione deve essere creata e l'importo dell'obbligazione supera il controllo della copertura finanziaria
  *      effettuato dalla stored procedure CNRCTB030.checkAssunzObblig 
  *    PostCondition:
  *      L'utente può proseguire con la definizione dell'imputazione finanziaria dell'obbligazione o con il suo salvataggio
  *  creazione obbligazione - errore
  *    PreCondition:
  *   	 Una nuova obbligazione deve essere creata e l'importo dell'obbligazione non supera il controllo della copertura finanziaria
  *      effettuato dalla stored procedure CNRCTB030.checkAssunzObblig 
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente l'impossibilità di proseguire nella definizione dell'obbligazione
  *  aumento importo obbligazione
  *    PreCondition:
  *   	 Ad una obbligazione esistente e' stato incrementato l'importo e la differenza fra l'importo attuale dell'obbligazione 
  *      e l'importo che aveva in precedenza supera il controllo della copertura finanziaria
  *      effettuato dalla stored procedure CNRCTB030.checkAssunzObblig 
  *    PostCondition:
  *      L'utente può proseguire con la definizione dell'imputazione finanziaria dell'obbligazione o con il suo salvataggio
  *  aumento importo obbligazione - errore
  *    PreCondition:
  *   	 Ad una obbligazione esistente e' stato incrementato l'importo e la differenza fra l'importo attuale dell'obbligazione 
  *      e l'importo che aveva in precedenza non supera il controllo della copertura finanziaria
  *      effettuato dalla stored procedure CNRCTB030.checkAssunzObblig 
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente l'impossibilità di proseguire nella modifica dell'obbligazione
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> che il Cds dovrebbe assumere
  */
private void calcolaLimiteAssunzioneObbligazioni (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
		/**
		 * @author mspasiano
		 * @since 04.03.2006
		 * @value Se è attiva la gestione di competenza del 2006 non controlla il limite di assunzione obbligazioni
		 */
		if (((Parametri_cnrBulk)getHome(aUC,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(aUC)))).getFl_regolamento_2006().booleanValue())
		   return;
		
}
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesto il calcolo delle percentuali d'imputazione per l'obbligazione e la ripartizione dell'importo
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
  *      Nell'imputazione finanziaria dell'obbligazione e' stata specificata una linea di attività non presente nel 
  *      Piano di Gestione e per tale linea non e' stata specificata la percentuale da usare nella ripartizione 
  *      dell'importo delle scadenze sui dettagli
  *    PostCondition:
  *      Una segnalazione di errore viene restituita all'utente per comunicare il problema
  *
  *  errore - non esistono spese e costi nel piano di gestione per linea attivita SINGOLA
  *    PreCondition:
  *      Nell'imputazione finanziaria dell'obbligazione e' stata specificata una linea di attività presente nel 
  *      Piano di Gestione con categoria di dettaglio SINGOLA e per la quale la somma delle colonne I,K,Q,S,U risulta
  *      essere 0
  *    PostCondition:
  *      Una segnalazione di errore viene restituita all'utente per comunicare che i costi/spese della linea di attività sono nulli
  *
  *  errore - non esistono spese e costi nel piano di gestione per linea attivita SCARICO
  *    PreCondition:
  *      Nell'imputazione finanziaria dell'obbligazione e' stata specificata una linea di attività presente nel 
  *      Piano di Gestione con categoria di dettaglio SCARICO e per la quale la somma delle colonne J,L,R,T risulta
  *      essere 0
  *    PostCondition:
  *      Una segnalazione di errore viene restituita all'utente per comunicare che i costi/spese relativi ad altra UO della linea di attività sono nulli
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> per la quale devono essere calcolati gli importi e le percentuali dei suoi
  *        dettagli di scadenza
  * @return l'obbligazione con le percentuali e gli importi dei dettagli delle scadenze aggiornati
  */

protected ObbligazioneBulk calcolaPercentualeImputazioneObbligazione (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	BigDecimal percentuale = new BigDecimal( 100);
	BigDecimal totaleLattDaPdg = new BigDecimal( 0 );
	BigDecimal totalePerScadenza;	
	V_pdg_obbligazione_speBulk lattDaPdg;
	Linea_attivitaBulk latt;
	Obbligazione_scad_voceBulk osv;
	Obbligazione_scadenzarioBulk os;


	// calcolo le percentuali di imputazione finanziaria per le linee di attivita da pdg
	// 100 - percentuali specificate x linee att non da PDG
	for ( Iterator i = obbligazione.getNuoveLineeAttivitaColl().iterator(); i.hasNext(); )
		percentuale = percentuale.subtract( ((Linea_attivitaBulk) i.next()).getPrcImputazioneFin());
	if ( obbligazione.getNuoveLineeAttivitaColl().size() > 0 && percentuale.compareTo( new BigDecimal(100)) == 0
		&& obbligazione.getLineeAttivitaSelezionateColl().size() == 0 )
		throw new ApplicationException( "Non sono state specificate le percentuali per i nuovi GAE!");	
	for ( Iterator i = obbligazione.getLineeAttivitaSelezionateColl().iterator(); i.hasNext(); )
		totaleLattDaPdg = totaleLattDaPdg.add(((V_pdg_obbligazione_speBulk) i.next()).getImporto());
	if ( obbligazione.getLineeAttivitaSelezionateColl().size() > 0 && totaleLattDaPdg.doubleValue() == 0 )
		throw new ApplicationException( "GAE da PdG con costi/spese nulli. Imputazione automatica impossibile!");
	for ( Iterator i = obbligazione.getLineeAttivitaSelezionateColl().iterator(); i.hasNext(); )
	{
		lattDaPdg = (V_pdg_obbligazione_speBulk) i.next();
		lattDaPdg.setPrcImputazioneFin( lattDaPdg.getImporto().multiply(percentuale).divide(totaleLattDaPdg, 2, BigDecimal.ROUND_HALF_UP) );
	}	

	// calcolo gli importi e le percentuali per i dettagli delle scadenze

	for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	{
		os = (Obbligazione_scadenzarioBulk) i.next();
		totalePerScadenza = new BigDecimal( 0 );
		
		for ( Iterator j = os.getObbligazione_scad_voceColl().iterator(); j.hasNext(); )
		{
			osv = (Obbligazione_scad_voceBulk) j.next();
			
			for ( Iterator k = obbligazione.getLineeAttivitaSelezionateColl().iterator(); k.hasNext(); )
			{
				lattDaPdg = (V_pdg_obbligazione_speBulk) k.next();
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
			for ( Iterator k = obbligazione.getNuoveLineeAttivitaColl().iterator(); k.hasNext(); )
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
		if (  os.getObbligazione_scad_voceColl().size() > 0 && totalePerScadenza.compareTo( os.getIm_scadenza()) != 0 )
		{
			for (Iterator scad_voce = os.getObbligazione_scad_voceColl().iterator(); scad_voce.hasNext();){
				osv = (Obbligazione_scad_voceBulk)scad_voce.next();
				if (osv.getIm_voce().add( os.getIm_scadenza().subtract( totalePerScadenza )).compareTo( new BigDecimal(0)) > 0){
					osv.setIm_voce( osv.getIm_voce().add( os.getIm_scadenza().subtract( totalePerScadenza )));
					break;
				}
			}
		}
				
	}	
		
	return obbligazione;
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

public void callDoRiprocObb(
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
			"CNRCTB215.doRiprocObb(?, ?, ?, ?, ?)",false,this.getClass());
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
  *  cancella dettaglio
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria dell'obbligazione e una linea di attività 
  *      prima selezionata ora non lo e' più
  *    PostCondition:
  *      Il dettaglio della scadenza dell'obbligazione riferito alla linea di attività non più selezionata
  *      viene cancellato
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione la cui imputazione finanziaria e' stata modificata
  * @param scadenzario <code>Obbligazione_scadenzarioBulk</code> la scadenza dell'obbligazione per cui eliminare i dettagli non più validi
  *  
 */

protected void cancellaDettaglioScadenze (UserContext aUC,ObbligazioneBulk obbligazione, Obbligazione_scadenzarioBulk scadenzario) throws ComponentException
{
	Obbligazione_scad_voceBulk osv;
	Obbligazione_scadenzarioBulk os;	
	V_pdg_obbligazione_speBulk ppsd;
	Linea_attivitaBulk la;
	boolean found;
	int index = 0;

	//cancello i dettagli scadenze per le linee di attività che non esistono piu'

	for ( Iterator scadIterator = scadenzario.getObbligazione_scad_voceColl().iterator(); scadIterator.hasNext(); index++)
	{
		osv = (Obbligazione_scad_voceBulk) scadIterator.next();
		found = false;
		for ( Iterator lattIterator = obbligazione.getLineeAttivitaSelezionateColl().iterator(); lattIterator.hasNext(); )
		{
			ppsd = (V_pdg_obbligazione_speBulk) lattIterator.next();
			if ( osv.getCd_centro_responsabilita().equals( ppsd.getCd_centro_responsabilita()) &&
				 osv.getCd_linea_attivita().equals( ppsd.getCd_linea_attivita()) )
			{
				found = true;
				break;
			}	
		}
		if ( !found )
			for ( Iterator lattIterator = obbligazione.getNuoveLineeAttivitaColl().iterator(); lattIterator.hasNext(); )
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
			osv.setToBeDeleted();
			scadIterator.remove();
		}	
	}
}
//^^@@
/** 
  *  Lo stato dell'obbligazione è Provvisoria e non esistono ordini
  *    PreCondition:
  *      Lo stato dell'obbligazione è Provvisoria e non esiste nessun ordine associato all'obbligazione
  *    PostCondition:
  *      Il sistema eseguirà le seguente attività:
  *      1) L'aggiornamento dei saldi 'obbligazioni' dei capitoli di spesa CdS.
  *         (Questo processo viene eseguito dal metodo 'aggiornaCapitoloSaldoObbligazione').
  *      2) L'eliminazione di ogni scadenza nello scadenzario dell'obbligazione,
  *      3) L'eliminazione dell'obbligazione propria.
  *
  *  Esiste un ordine per l'obbligazione
  *    PreCondition:
  *      Per l'obbligazione provvisoria e' stato definito un ordine
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che per cancellare un'obbligazione provvisoria
  *      per la quale e' già stato emesso un ordine e' necessario prima cancellare l'ordine
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da cancellare
 */
public void cancellaObbligazioneProvvisoria (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
    try
	{
		// controllo se esiste un ordine associato all'obbligazione
		// In caso affermativo blocco l'operazione di eliminazione
		it.cnr.contab.doccont00.ordine.bulk.OrdineBulk ordine = findOrdineFor(aUC, obbligazione);
		if (ordine!=null)
			throw new ApplicationException("L'impegno selezionato è collegato ad un ordine. Cancellare prima l'ordine");

		//imposto a TO_BE_DELETED l'obbligazione e tutte le sue scadenze e tutte le sue scad_voce
		obbligazione.setToBeDeleted();


		obbligazione.getObbligazioniPluriennali().stream().forEach(e->{
			e.setToBeDeleted();
		});

		//		aggiornaCapitoloSaldoObbligazione( aUC, obbligazione );
		makeBulkPersistent( aUC, obbligazione );
		aggiornaCapitoloSaldoObbligazione( aUC, obbligazione, CANCELLAZIONE );		
	}
	catch ( ObjectNotFoundException e )
	{
		throw new ApplicationException( "L'impegno è già stato cancellato" );
	}
	catch ( Exception e )
	{
		throw handleException( obbligazione, e )	;
	}	
	

}
//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto il cdr dell'utente connesso
  *    PostCondition:
  *      Restituisce il cdr a cui appartiene l'utente specificato:
  *      esercizio = esercizio selezionato in scrivania
  *      codice = UTENTE.CD_CDR
 */
//^^@@
private CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {
	try {
		it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
		user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

		CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );
		return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);

	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw new ComponentException(e);
	}
}
/** 
  *  Lo stato dell'obbligazione è Provvisoria - esercizio ok
  *    PreCondition:
  *      Lo stato dell'obbligazione è Provvisoria.
  *      L'esercizio di competenza dell'obbligazione e' uguale all'esercizio di creazione
  *    PostCondition:
  *      L'obbligazione viene aggiornata allo stato di 'Definitiva'.
  *
  *  Lo stato dell'obbligazione è Provvisoria - esercizio errore
  *    PreCondition:
  *      Lo stato dell'obbligazione è Provvisoria.
  *      L'esercizio di competenza dell'obbligazione e' maggiore all'esercizio di creazione
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che un'obbligazione con esercizio competenza
  *      maggiore all'esercizio di creazione non può essere resa definitiva
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione da confermare
  * @return ObbligazioneBulk l'obbligazione con lo stato modificato
  *  
 */
public ObbligazioneBulk confermaObbligazioneProvvisoria (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		if ( obbligazione instanceof V_obbligazione_im_mandatoBulk )
		{
			V_obbligazione_im_mandatoBulk v_obbligazione = (V_obbligazione_im_mandatoBulk) obbligazione;

			if (obbligazione.isObbligazioneResiduo())
				obbligazione = (ObbligazioneBulk) getHome( aUC, ObbligazioneResBulk.class).findByPrimaryKey( new ObbligazioneResBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));
			else if (obbligazione.isObbligazioneResiduoImproprio())
				obbligazione = (ObbligazioneBulk) getHome( aUC, ObbligazioneRes_impropriaBulk.class).findByPrimaryKey( new ObbligazioneRes_impropriaBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));
			else
				obbligazione = (ObbligazioneBulk) getHome( aUC, ObbligazioneOrdBulk.class).findByPrimaryKey( new ObbligazioneOrdBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));

			if ( obbligazione == null )
				throw new ApplicationException( "L'impegno e' stato cancellato" );
			
		}		

		lockBulk( aUC, obbligazione );
		if ( obbligazione.getFl_gara_in_corso()!=null && obbligazione.getFl_gara_in_corso().booleanValue()  )
			throw new ApplicationException("Non e' possibile confermare un'impegno ("+obbligazione.getEsercizio()+"/"+obbligazione.getEsercizio_originale()+"/"+obbligazione.getPg_obbligazione()+") con gara di appalto in corso di espletamento.");
		if ( obbligazione.getEsercizio().compareTo( obbligazione.getEsercizio_competenza()) != 0 )
			throw new ApplicationException("Non e' possibile confermare un'impegno con esercizio di competenza successivo all'esercizio di scrivania");
		obbligazione.setStato_obbligazione( obbligazione.STATO_OBB_DEFINITIVO );
		obbligazione.setUser( aUC.getUser());
		updateBulk( aUC, obbligazione );
		return obbligazione;
	}
	catch ( Exception e )
	{
		if ( obbligazione != null )
			obbligazione.setStato_obbligazione( obbligazione.STATO_OBB_PROVVISORIO );
		throw handleException( obbligazione, e )	;
	}
}
/** 
  *  anno di competenza maggiore da anno di creazione obbligazione + 2
  *    PreCondition:
  *      L'anno di competenza dell'obbligazione è maggiore dell'anno di creazione dell'obbligazione + 2
  *    PostCondition:
  *      Non viene effetuato il controllo della copertura finanziaria.
  *  anno di competenza inferiore anno di creazione obbligazione + 2
  *    PreCondition:
  *      L'anno di competenza dell'obbligazione è uguale all'anno di creazione dell'obbligazione oppure
  *      l'anno di competenza dell'obbligazione è uguale all'anno di creazione dell'obbligazione + 1 oppure
  *      L'anno di competenza dell'obbligazione è uguale all'anno di creazione dell'obbligazione + 2   
  *    PostCondition:
  *      La copertura finanziaria viene controllata per l'obbligazione in elaborazione con il metodo
  *      calcolaLimiteAssunzioneObbligazioni
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui verificare la copertura finanziaria
  *
 */
private void controllaCoperturaFinanziariaObbligazione (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	/* eliminato 
	if ( !obbligazione.getElemento_voce().getFl_limite_ass_obblig().booleanValue())
		return;
	*/	

	int esercizioScrivania = ((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio().intValue();
	if ( (esercizioScrivania + 2 ) < obbligazione.getEsercizio().intValue() )
		return;
	
	calcolaLimiteAssunzioneObbligazioni( aUC, obbligazione );
}
/** 
  *  creazione/modifica di obbligazione con esercizio competenza diverso da esercizio scrivania
  *    PreCondition:
  *      Un'obbligazione e' stata creata/modificata e l'esercizio di competenza dell'obbligazione
  *      e' maggiore dell'esercizio di scrivania.
  *    PostCondition:
  *      Nessun controllo viene effettuato
  *  creazione obbligazione con esercizio competenza = esercizio scrivania 
  *    PreCondition:
  *      Un'obbligazione e' stata creata
  *      l'esercizio dell'obbligazione e' uguale all'esercizio di scrivania.
  *    PostCondition:
  *      Viene richiamato il metodo 'controllaDisponibilitaCassaPerVoceInInserimento' che
  *      esegue il controllo della disponibilità di cassa per ogni voce presente nell'obbligazione
  *  modifica obbligazione con esercizio competenza = esercizio scrivania 
  *    PreCondition:
  *      Un'obbligazione e' stata modificata
  *      l'esercizio dell'obbligazione e' uguale all'esercizio di scrivania.
  *    PostCondition:
  *      Viene richiamato il metodo 'controllaDisponibilitaCassaPerVoceInModifica' che
  *      esegue il controllo della disponibilità di cassa per ogni voce presente nell'obbligazione
  
  * @param userContext lo user context
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> per i cui capitoli deve essere verificata la disponibilità di cassa
  * @param azione indica l'azione effettuata sull'obbligazione e puo' assumere i valori INSERIMENTO, MODIFICA, CANCELLAZIONE  
  *
 */
private void controllaDisponibilitaCassaPerVoce (UserContext userContext,ObbligazioneBulk obbligazione, int azione) throws ComponentException
{
	try
	{
		
		// non si aggiornano i saldi di obbligazioni con esercizio di competenza diverso da esercizio di creazione
		if ( obbligazione.getEsercizio().compareTo( obbligazione.getEsercizio_competenza()) != 0 )
			return ;
		if ( obbligazione.isCheckDisponibilitaCassaEseguito() )
			return;
		if ( azione == INSERIMENTO )
			controllaDisponibilitaCassaPerVoceInInserimento( userContext, obbligazione );
		else if (azione == MODIFICA )
			controllaDisponibilitaCassaPerVoceInModifica( userContext, obbligazione );		
	
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
}
/** 
  *
  *  disp. cassa
  *    PreCondition:
  *      Un'obbligazione e' stata creata
  *    PostCondition:
  *      Per ogni voce del piano presente nell'obbligazione viene
  *      eseguito il controllo della disponibilità di cassa
  
  *
  * @param userContext lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> per la quale deve essere verificata la disponibilità di cassa sui capitoli
  *
 */

private void controllaDisponibilitaCassaPerVoceInInserimento (UserContext userContext,ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		
		SaldoComponentSession session = createSaldoComponentSession();
		IVoceBilancioBulk voce;
		BigDecimal im_voce;
		PrimaryKeyHashMap saldoVociMap;
		try {
			saldoVociMap = obbligazione.getVociMap(((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(userContext));
		} catch (PersistencyException e) {
			throw handleException(e);
		}
		
		for ( Iterator i = saldoVociMap.keySet().iterator(); i.hasNext(); )
		{
			voce = (IVoceBilancioBulk) i.next();
			if (voce instanceof Voce_fBulk) {
				im_voce = (BigDecimal) saldoVociMap.get( voce );
				session.checkDisponabilitaCassaObbligazioni( userContext, (Voce_fBulk)voce, obbligazione.getCd_cds(), im_voce, Voce_f_saldi_cmpBulk.TIPO_COMPETENZA );
			}
		}	
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
}
/** 
  *  disp. cassa senza mandati
  *    PreCondition:
  *      Un'obbligazione e' stata modificata
  *      L'obbligazione non ha mandati associati
  *    PostCondition:
  *      Per ogni voce del piano presente nell'obbligazione viene calcolato quale e' il valore da considerare
  *      (V_mod_saldi_obbligBulk) per verificare la disponibilità di cassa relativa sull'assunzione di obbligazioni
  *
  *  disp. cassa con mandati
  *    PreCondition:
  *      Un'obbligazione e' stata modificata
  *      L'obbligazione ha mandati associati
  *    PostCondition:
  *      Per ogni voce del piano presente nell'obbligazione viene calcolato quale e' il valore da considerare
  *      (V_mod_saldi_obbligBulk) per verificare la disponibilità di cassa relativa sia all'assunzione di mandati
  *      (controllo bloccante) che all'assunzione di obbligazioni (controllo non bloccante)
  *
  *
  * @param userContext lo user context
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> per la quale devono essere aggiornati i saldi relativi ai capitoli di spesa  
  *
 */

private void controllaDisponibilitaCassaPerVoceInModifica (UserContext userContext,ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		
		List saldiDaAggiornare = ((V_mod_saldi_obbligHome)getHome( userContext, V_mod_saldi_obbligBulk.class )).findModificheSaldiFor( obbligazione, obbligazione.getPg_ver_rec() );
		
		SaldoComponentSession session = createSaldoComponentSession();
		for ( Iterator i = saldiDaAggiornare.iterator(); i.hasNext(); )
		{
			V_mod_saldi_obbligBulk modSaldo = (V_mod_saldi_obbligBulk) i.next();
			Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), obbligazione.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
			if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) > 0 )
				session.checkDisponabilitaCassaMandati( userContext, voce, obbligazione.getCd_cds(), modSaldo.getIm_delta_man_voce(), Voce_f_saldi_cmpBulk.TIPO_COMPETENZA);
			if ( modSaldo.getIm_delta_voce().compareTo( new BigDecimal(0) ) > 0 )
				session.checkDisponabilitaCassaObbligazioni( userContext, voce, obbligazione.getCd_cds(), modSaldo.getIm_delta_voce(), Voce_f_saldi_cmpBulk.TIPO_COMPETENZA );			
		}
		/*
		* Aggiorno i Saldi per CDR/Linea
		*/	
		List saldiDaAggiornareCdrLinea = ((V_mod_saldi_obblig_scad_voceHome)getHome( userContext, V_mod_saldi_obblig_scad_voceBulk.class )).findModificheSaldiFor( obbligazione, obbligazione.getPg_ver_rec() );
		for ( Iterator i = saldiDaAggiornareCdrLinea.iterator(); i.hasNext(); )
		{
			V_mod_saldi_obblig_scad_voceBulk modSaldo = (V_mod_saldi_obblig_scad_voceBulk) i.next();
			Voce_fBulk voce = new Voce_fBulk( modSaldo.getCd_voce(), obbligazione.getEsercizio(), modSaldo.getTi_appartenenza(), modSaldo.getTi_gestione() );
			if ( modSaldo.getIm_delta_man_voce().compareTo( new BigDecimal(0) ) > 0 )
				session.checkDisponabilitaCassaMandati( userContext, modSaldo.getCd_centro_responsabilita(), modSaldo.getCd_linea_attivita(), voce, modSaldo.getIm_delta_man_voce());
		}
		
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
}
/** 
 *  Effettua controlli sulle combinazione Cdr/Voce/Linea selezionate e scelte dall'utente.
 *  Il controllo viene effettuato sull'insieme delle Linee di attività (da PDG e non) selezionate dall'utente.
 *   
 *    PreCondition:
 *      E' stata confermata l'imputazione finanziaria dell'obbligazione da creare  
 *    PostCondition:
 *      se l'impegno creato è di competenza viene verificato che la disponibilità residua ad assumere
 * 		impegni residui impropri non sia superiore al limite indicato nei Parametri CNR per ogni 
 * 		Cdr/Voce/Linea selezionata
 *
 * @param userContext lo user context 
 * @param obbligazione l'obbligazione da creare
 * @return
 * @throws ComponentException
*/
public ObbligazioneBulk validaImputazioneFinanziaria(UserContext userContext, ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		V_pdg_obbligazione_speBulk ppsd;
		Linea_attivitaBulk la;
		IVoceBilancioBulk voce;
	
		// recupero le percentuali di imputazione finanziaria per le linee di attivita da pdg
		// 100 - percentuali specificate x linee att non da PDG
		PrimaryKeyHashtable oldRipartizioneCdrVoceLinea = getOldRipartizioneCdrVoceLinea(userContext, obbligazione); 

		//individuo le combinazioni CDR/VOCE/Linea scelti dall'utente
		for ( Iterator lattIterator = obbligazione.getLineeAttivitaSelezionateColl().iterator(); lattIterator.hasNext(); )
		{
			ppsd = (V_pdg_obbligazione_speBulk) lattIterator.next();
			if ( obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
			{
				if ( ppsd.getCategoria_dettaglio().equals( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SINGOLO) ||
					 ppsd.getCd_centro_responsabilita_clgs()==null)
					voce = obbligazione.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita());
				else
					voce = obbligazione.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita_clgs());
				
				/*
				 * Nella gestione 2006, il campo voce potrebbe essere null se la ricerca è attuata tramite
				 * getCd_centro_responsabilita_clgs(). In questo caso cerco di trovarla tramite 
				 * ppsd.getCd_centro_responsabilita()
				 */ 
				if (voce == null) 
					voce = obbligazione.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita());
			}
			else
				voce = obbligazione.getCapitolo( ppsd.getCd_funzione() );

			validaCdrLineaVoce(userContext, obbligazione, oldRipartizioneCdrVoceLinea, ppsd.getCd_centro_responsabilita(), ppsd.getCd_linea_attivita(), voce);
		}
	
		for ( Iterator lattIterator = obbligazione.getNuoveLineeAttivitaColl().iterator(); lattIterator.hasNext(); )
		{
			la = (Linea_attivitaBulk) lattIterator.next();
	
			if ( obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC) )
				voce = obbligazione.getArticolo( la.getLinea_att().getFunzione().getCd_funzione(), la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita());
			else
				voce = obbligazione.getCapitolo( la.getLinea_att().getFunzione().getCd_funzione() );
	
			validaCdrLineaVoce(userContext, obbligazione, oldRipartizioneCdrVoceLinea, la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita(), la.getLinea_att().getCd_linea_attivita(), voce);
		}	

		return obbligazione;
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
}
/** 
 *  Effettua controlli sulle combinazione Cdr/Voce/Linea indicati come parametri
 *    PreCondition:
 *      Una nuova combinazione Cdr/Voce/Linea è stata scelta o è stato aumentato l'importo 
 * 		complessivo assegnato al Cdr/Voce/Linea 
 *    PostCondition:
 *      Se l'impegno creato è di competenza viene verificato che la disponibilità residua ad assumere
 * 		impegni residui impropri non sia superiore al limite indicato nei Parametri CNR
 *
 * @param userContext lo user context 
 * @param obbligazione l'obbligazione di cui sto aggiornando l'imputazione finanziaria 
 * @param oldRipartizioneCdrVoceLinea la chiave restituita dalla chiamata alla procedura getOldRipartizioneCdrVoceLinea
 * @param cdr il centro di responsabilità
 * @param latt il codice della GAE
 * @param voce l'istanza di <code>Voce_fBulk</code> contenente l'elemento voce da verificare
 * @throws ComponentException
 *
*/
private void validaCdrLineaVoce(UserContext userContext, ObbligazioneBulk obbligazione, PrimaryKeyHashtable oldRipartizioneCdrVoceLinea, String cdr, String latt, IVoceBilancioBulk voce) throws ComponentException
{
	BigDecimal totaleOldScad = new BigDecimal(0);
	BigDecimal totaleNewScad = new BigDecimal(0);
	Obbligazione_scad_voceBulk key = new Obbligazione_scad_voceBulk();
	boolean found=false;

	try
	{
		Parametri_cdsBulk param_cds = (Parametri_cdsBulk)(getHome(userContext, Parametri_cdsBulk.class)).findByPrimaryKey(new Parametri_cdsBulk(CNRUserContext.getCd_cds(userContext),CNRUserContext.getEsercizio(userContext)));
		boolean isObbligazioneResiduo = obbligazione.isObbligazioneResiduo() || obbligazione.isObbligazioneResiduoImproprio();
		SaldoComponentSession saldoSession = Utility.createSaldoComponentSession();

		//Questo controllo parte se cambiato l'importo per il CDR/GAE indicato e serve per:
		// Obbligazione Competenza: per impedire di creare obbligazioni di cometenza in presenza di stanziamento residuo improprio
		// Obbligazione Residuo: per bloccare la creazione/modifica del residuo se attiva la gestione del limite sui residui sia sul CDS che sulla voce e per la natura e tipo
		// finanziamento indicato in CONFIGURAZIONE_CNR
		if ((obbligazione.isCompetenza() && Optional.ofNullable(param_cds.getIm_soglia_consumo_residuo()).isPresent()) || isObbligazioneResiduo) {
			totaleNewScad = obbligazione.getObbligazione_scadenzarioColl().stream()
						.flatMap(os -> os.getObbligazione_scad_voceColl().stream())
						.filter(osv -> osv.getCd_centro_responsabilita().equals(cdr))
						.filter(osv -> osv.getCd_linea_attivita().equals(latt))
						.filter(osv -> osv.getCd_voce().equals(voce.getCd_voce()))
						.map(Obbligazione_scad_voceBulk::getIm_voce)
						.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);

			for (Enumeration e = oldRipartizioneCdrVoceLinea.keys(); e.hasMoreElements(); ) {
				key = (Obbligazione_scad_voceBulk) e.nextElement();
				found = false;
				if (key.getCd_centro_responsabilita().equals(cdr) &&
						key.getCd_linea_attivita().equals(latt) &&
						key.getCd_voce().equals(voce.getCd_voce())) {
					totaleOldScad = (BigDecimal) oldRipartizioneCdrVoceLinea.get(key);
					found = true;
					break;
				}
			}

			/*
			 * Controllo, in caso di creazione impegni di competenza o aumento dell'importo assegnato,
			 * che la disponibilità ad assumere impegni residui impropri non sia superiore al limite
			 * previsto nei parametri CNR
			 *
			 **/
			if (totaleOldScad.compareTo(totaleNewScad) == -1 || !found) {
				if (!UtenteBulk.isAbilitatoSbloccoImpegni(userContext)) {
					if (obbligazione.isCompetenza())
						saldoSession.checkBloccoImpegniNatfin(userContext, cdr, latt, (Elemento_voceBulk) voce, ObbligazioneBulk.TIPO_COMPETENZA);
					if (obbligazione.isObbligazioneResiduo())
						saldoSession.checkBloccoImpegniNatfin(userContext, cdr, latt, (Elemento_voceBulk) voce, ObbligazioneBulk.TIPO_RESIDUO_PROPRIO);
					if (obbligazione.isObbligazioneResiduoImproprio())
						saldoSession.checkBloccoImpegniNatfin(userContext, cdr, latt, (Elemento_voceBulk) voce, ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO);
				}

				if (obbligazione.isCompetenza()) {
					BigDecimal totaleResidui = saldoSession.getTotaleSaldoResidui(userContext, cdr, latt, voce);

					if (totaleResidui.compareTo(param_cds.getIm_soglia_consumo_residuo()) == 1)
						if (!found)
							throw new ApplicationException("Non è possibile assumere impegni di competenza per il CDR/GAE/Voce (" +
									cdr + "/" + latt + "/" + voce.getCd_voce() +
									"), in quanto esiste una disponibilità ad assumere impegni " +
									"su stanziamenti residui impropri (" +
									new it.cnr.contab.util.EuroFormat().format(totaleResidui) + ").");
						else
							throw new ApplicationException("Non è possibile aumentare di " +
									new it.cnr.contab.util.EuroFormat().format(totaleNewScad.subtract(totaleOldScad)) +
									" l'importo di competenza già assegnato per il CDR/GAE/Voce (" +
									cdr + "/" + latt + "/" + voce.getCd_voce() +
									"), in quanto esiste una disponibilità ad assumere impegni " +
									"su stanziamenti residui impropri (" +
									new it.cnr.contab.util.EuroFormat().format(totaleResidui) + ").");
				}
			}
		}

		//Controllo valido solo per residui nati nell'anno (impropri e competenza)
		//per i residui propri il controllo viene fatto solo all'atto della modifica totale positiva dell'importo. Il controllo
		//è già presente in ObbligazioneResComponent
		if (!obbligazione.isObbligazioneResiduo() &&
				Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(userContext, CNRUserContext.getEsercizio(userContext))) {
			WorkpackageBulk linea = ((WorkpackageHome)getHome(userContext, WorkpackageBulk.class))
					.searchGAECompleta(userContext,CNRUserContext.getEsercizio(userContext), cdr, latt);
			ProgettoBulk progetto = linea.getProgetto();
				Optional.ofNullable(progetto.getOtherField())
						.filter(el->el.isStatoApprovato()||el.isStatoChiuso())
						.orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
								+ "Il progetto associato "+progetto.getCd_progetto()+" non risulta in stato Approvato o Chiuso."));

			if (progetto.isDatePianoEconomicoRequired()) {
				//Negli impegni controllare la più piccola data tra data inizio progetto e data stipula contratto definitivo
				ProgettoHome progettoHome = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
				java.util.Collection<ContrattoBulk> contratti = progettoHome.findContratti(progetto.getPg_progetto());

				Optional<ContrattoBulk> optContratto = 
						contratti.stream().filter(el->el.isAttivo()||el.isAttivo_e_Passivo())
						 .min((p1, p2) -> p1.getDt_stipula().compareTo(p2.getDt_stipula()))
		    			 .filter(el->el.getDt_stipula().before(progetto.getOtherField().getDtInizio()));
				
				if (optContratto.isPresent())
					optContratto
	 	    			.filter(ctr->ctr.getDt_stipula().after(obbligazione.getDt_registrazione()))
	 	    			.ifPresent(ctr->{
	 	    				throw new ApplicationRuntimeException(
	 	    						"Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
									  + "La data stipula ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(ctr.getDt_stipula())
								  + ") del primo contratto " + ctr.getEsercizio()+"/"+ctr.getStato()+"/"+ctr.getPg_contratto()
		    				   	  + " associato al progetto "+progetto.getCd_progetto()+" è successiva "
								  + "rispetto alla data di registrazione dell'obbligazione ("
		    				   	  + new java.text.SimpleDateFormat("dd/MM/yyyy").format(obbligazione.getDt_registrazione())+").");
		    			});
				else
					Optional.of(progetto.getOtherField().getDtInizio())
			 	    		.filter(dt->!dt.after(obbligazione.getDt_registrazione()))
							.orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
									+ "La data inizio ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
									+ ") del progetto "+progetto.getCd_progetto()+" associato è successiva "
									+ "rispetto alla data di registrazione dell'obbligazione ("
									+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(obbligazione.getDt_registrazione())+")."));
			}

			LocalDate localDateFineProgetto = Optional.ofNullable(progetto.getOtherField().getDtProroga())
					.orElse(Optional.ofNullable(progetto.getOtherField().getDtFine())
							.orElse(DateUtils.firstDateOfTheYear(3000))).toLocalDateTime().toLocalDate();

			int ggProroga = Optional.ofNullable(obbligazione.getElemento_voce())
										.flatMap(el->{
											if (obbligazione.isCompetenza())
												return Optional.ofNullable(el.getGg_deroga_obbl_comp_prg_scad());
											else
												return Optional.ofNullable(el.getGg_deroga_obbl_res_prg_scad());
										})
										.filter(el->el.compareTo(0)>0)
										.orElse(0);

			localDateFineProgetto = localDateFineProgetto.plusDays(ggProroga);

			if (localDateFineProgetto.isBefore(obbligazione.getDt_registrazione().toLocalDateTime().toLocalDate()))
				throw new ApplicationMessageFormatException("Attenzione! GAE {0} non selezionabile. "
						+ "La data fine/proroga del progetto {1} {2} ({3}) è precedente rispetto alla data di registrazione dell''impegno ({4}).",
						linea.getCd_linea_attivita(),
						progetto.getCd_progetto(),
						(ggProroga>0?", aumentata di " + ggProroga +" giorni,":""),
						localDateFineProgetto.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						new java.text.SimpleDateFormat("dd/MM/yyyy").format(obbligazione.getDt_registrazione()));
		}
	} catch ( Exception e )
	{
		throw handleException( e );
	}	
}
/** 
  *  Tutti i controlli superati - contesto non transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione non e' stata creata in un contesto transazionale
  *    PostCondition:
  *      L'obbligazione viene creata, i dettagli di tutte le scadenze vengono creati (metodo generaDettagliScadenzaObbligazione) e i saldi 
  *      dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione e' stata creata in un contesto transazionale
  *    PostCondition:
  *      L'obbligazione viene creata e i dettagli di tutte le sue scadenze vengono creati (metodo generaDettagliScadenzaObbligazione) 
  *  Errore di verifica obbligazione
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli eseguiti dal metodo 'verificaObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa - forzatura
  *    PreCondition:
  *      Una richiesta di creazione di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'  
  *		 e l'utente ha scelto di forzare l'emissione dell'obbligazione
  *    PostCondition:
  *      L'obbligazione viene creata, i dettagli di tutte le scadenze vengono creati (metodo generaDettagliScadenzaObbligazione) e i saldi 
  *      dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *
  * @param uc lo user context
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da creare
  * @return l'istanza di  <code>ObbligazioneBulk</code> creata
  *   
 */
public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
	try {
	verificaStatoEsercizio( 
							uc, 
							((CNRUserContext)uc).getEsercizio(), 
							obbligazione.getCd_cds());
	} catch (Exception e) {
		throw handleException(e);
	}
	
	validaCampi(uc, obbligazione);
	validaObbligazionePluriennale(uc, obbligazione);


	try {
		if (Utility.createConfigurazioneCnrComponentSession().isVariazioneAutomaticaSpesa(uc) && obbligazione.getGaeDestinazioneFinale()!=null &&
				obbligazione.getGaeDestinazioneFinale().getCd_linea_attivita()!=null) {
			Utility.createCRUDPdgVariazioneGestionaleComponentSession().generaVariazioneAutomaticaDaObbligazione(uc, obbligazione);
			//Essendo stata effettuata la variazione possiamo cambiare sull'impegno lìimputazione della GAE emttendo quella di destinazione
			final WorkpackageBulk gaeFinale = obbligazione.getGaeDestinazioneFinale();
			obbligazione.getObbligazione_scadenzarioColl().stream().flatMap(el -> el.getObbligazione_scad_voceColl().stream())
					.forEach(osv -> {
						osv.setLinea_attivita(gaeFinale);
					});

			if (obbligazione.getGaeDestinazioneFinale().getCentro_responsabilita().getUnita_padre().getCd_unita_organizzativa()!=obbligazione.getCd_unita_organizzativa()) {
				obbligazione.setUnita_organizzativa(obbligazione.getGaeDestinazioneFinale().getCentro_responsabilita().getUnita_padre());
				obbligazione.setCd_cds_origine(obbligazione.getGaeDestinazioneFinale().getCentro_responsabilita().getUnita_padre().getCd_unita_padre());
				obbligazione.setCd_uo_origine(obbligazione.getGaeDestinazioneFinale().getCentro_responsabilita().getUnita_padre().getCd_unita_organizzativa());
			}

			// carica le linee di attività da PDG
			obbligazione.setLineeAttivitaColl(listaLineeAttivitaPerCapitoliCdr(uc, obbligazione));
			obbligazione.refreshLineeAttivitaSelezionateColl();

			// carica le nuove linee di attività
			ObbligazioneHome obbligHome = (ObbligazioneHome) getHome(uc, obbligazione.getClass());
			obbligazione = obbligHome.refreshNuoveLineeAttivitaColl(uc, obbligazione);
		}
	} catch ( Exception e ) {
		throw handleException(e);
	}

	/* simona 23.10.2002 : invertito l'ordine della verifica e della generzione dettagli x problema 344 */
	generaDettagliScadenzaObbligazione( uc, obbligazione, null );	
	verificaObbligazione( uc, obbligazione );

	//verifica la correttezza dell'imputazione finanziaria
	validaImputazioneFinanziaria( uc, obbligazione );

	obbligazione = (ObbligazioneBulk) super.creaConBulk( uc, bulk );


	//esegue il check di disponibilita di cassa
	controllaDisponibilitaCassaPerVoce( uc, obbligazione, INSERIMENTO );
	verificaCoperturaContratto( uc, obbligazione);
	verificaCoerenzaGaeContratto(uc, obbligazione);
	verificaCoperturaIncaricoRepertorio(uc, obbligazione);

	if ( !uc.isTransactional() )
		//aggiorna il capitolo saldo
		aggiornaCapitoloSaldoObbligazione( uc, obbligazione, INSERIMENTO );	
	
	obbligazione.setIm_iniziale_obbligazione( obbligazione.getIm_obbligazione());
	obbligazione.setCd_iniziale_elemento_voce( obbligazione.getCd_elemento_voce());

	
	if (obbligazione.isCompetenza()) 
	  controllaAssunzioneImpegni(uc);
	
	if (obbligazione.isObbligazioneResiduoImproprio()) 
	  controllaAssunzioneImpResImpro(uc);
	validaCreaModificaOrigineFonti(uc, obbligazione);	
	try {
		obbligazione = validaCreaModificaElementoVoceNext(uc, obbligazione);
	} catch ( Exception e ) {
		throw handleException( e )	;
	}			
	return obbligazione;
}
/** 
  *  CDS SAC - non scarico
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo una nuova linea di attività da piano di gestione 
  *      ad una obbligazione di appartenenza del Cds SAC. La linea di attività non e' di scarico.
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'obbligazione riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza l'articolo selezionato 
  *      in imputazione finanziaria avente funzione e codice CdR uguale a quello della linea di attività
  *  CDS SAC - scarico
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo una nuova linea di attività da piano di gestione 
  *      ad una obbligazione di appartenenza del Cds SAC. La linea di attività e' di scarico.
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'obbligazione riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza l'articolo selezionato 
  *      in imputazione finanziaria avente funzione e codice CdR uguale a quello della linea di attività collegata nel 
  *      piano di gestione alla linea di attività selezionata
  *  CDS diverso da SAC 
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo una nuova linea di attività da piano di gestione 
  *      ad una obbligazione di appartenenza di un Cds con tipologia diversa da SAC. 
  *      La linea di attività non e' di scarico.
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'obbligazione riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza il capitolo selezionato 
  *      in imputazione finanziaria avente funzione uguale a quello della linea di attività
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui creare i dettagli scadenza
  * @param scadenzario <code>Obbligazione_scadenzarioBulk</code> la scadenza dell'obbligazione per cui creare i dettagli
  *  
  */

protected void creaDettagliScadenzaPerLineeAttivitaDaPdG(UserContext aUC,ObbligazioneBulk obbligazione, Obbligazione_scadenzarioBulk scadenzario) throws ComponentException
{
	
	Obbligazione_scad_voceBulk osv;
	Obbligazione_scadenzarioBulk os;	
	V_pdg_obbligazione_speBulk ppsd;
	Linea_attivitaBulk la;
	boolean found;

	//creo i dettagli scadenze se non esistono per le linee di attività da PDG

	for ( Iterator lattIterator = obbligazione.getLineeAttivitaSelezionateColl().iterator(); lattIterator.hasNext(); )
	{
		found = false;
		ppsd = (V_pdg_obbligazione_speBulk) lattIterator.next();
		if (scadenzario.getDatiFinanziariScadenzeDTO() == null  || scadenzario.getDatiFinanziariScadenzeDTO().getCdLineaAttivita() == null  || 
				scadenzario.getDatiFinanziariScadenzeDTO().getCdCentroResponsabilita() == null  || 
				scadenzario.getDatiFinanziariScadenzeDTO().getCdVoce() == null  || 
				(scadenzario.getDatiFinanziariScadenzeDTO().getCdLineaAttivita().equals(ppsd.getCd_linea_attivita()) && 
				 scadenzario.getDatiFinanziariScadenzeDTO().getCdCentroResponsabilita().equals(ppsd.getCd_centro_responsabilita())
//				 && scadenzario.getDatiFinanziariScadenzeDTO().getCdVoce().equals(ppsd.getCd_elemento_voce())
				 )){
			for ( Iterator i = scadenzario.getObbligazione_scad_voceColl().iterator(); i.hasNext(); )
			{
				osv = (Obbligazione_scad_voceBulk) i.next();
				if ( osv.getCd_centro_responsabilita().equals( ppsd.getCd_centro_responsabilita()) &&
					 osv.getCd_linea_attivita().equals( ppsd.getCd_linea_attivita()) )
				{
						found = true;
						break;
				}		
			}
			
			if ( !found )
			{
				//creo	nuovo Obbligazione_scad_voceBulk
				osv = new Obbligazione_scad_voceBulk();
				osv.setToBeCreated();
				if ( obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
				{
					IVoceBilancioBulk articolo;
					if ( ppsd.getCategoria_dettaglio().equals( it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk.CAT_SINGOLO) ||
						 ppsd.getCd_centro_responsabilita_clgs()==null)
						articolo = obbligazione.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita());
					else
						articolo = obbligazione.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita_clgs());
					/*
					 * Nella gestione 2006, il campo "articolo" potrebbe essere null se la ricerca è attuata tramite
					 * getCd_centro_responsabilita_clgs(). In questo caso cerco di trovarla tramite 
					 * ppsd.getCd_centro_responsabilita()
					 */ 
					if (articolo == null) 
						articolo = obbligazione.getArticolo( ppsd.getCd_funzione(), ppsd.getCd_centro_responsabilita());

					if (articolo != null){
						osv.setTi_appartenenza( articolo.getTi_appartenenza());
						osv.setTi_gestione( articolo.getTi_gestione());
						osv.setCd_voce( articolo.getCd_voce() );
					}
				}
				else
				{
					IVoceBilancioBulk capitolo = obbligazione.getCapitolo( ppsd.getCd_funzione() );
					osv.setTi_appartenenza( capitolo.getTi_appartenenza());
					osv.setTi_gestione( capitolo.getTi_gestione());
					osv.setCd_voce( capitolo.getCd_voce() );
				}			
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
				osv.setCd_fondo_ricerca( obbligazione.getCd_fondo_ricerca() );
				//osv.setUser( aUC.getUser())		;
				osv.setUser( scadenzario.getObbligazione().getUser() );
				osv.setObbligazione_scadenzario( scadenzario );
				((BulkList) scadenzario.getObbligazione_scad_voceColl()).add( osv );
			}	
		}
	}
}
/** 
  *  CDS SAC 
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo ad una obbligazione di appartenenza del Cds SAC
  *      una nuova linea di attività che non e' presente nel piano di gestione   
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'obbligazione riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza l'articolo selezionato 
  *      in imputazione finanziaria avente funzione e codice CdR uguale a quello della linea di attività
  *  CDS diverso da SAC 
  *    PreCondition:
  *      E' stata modificato l'imputazione finanziaria aggiungendo ad una obbligazione di appartenenza ad un Cds diverso da SAC
  *      una nuova linea di attività che non e' presente nel piano di gestione   
  *    PostCondition:
  *      Viene creato un nuovo dettaglio di scadenza dell'obbligazione riferito alla nuova linea di attività e viene
  *      impostata come voce del piano dei conti del dettaglio della scadenza il capitolo selezionato 
  *      in imputazione finanziaria avente funzione uguale a quello della linea di attività
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui creare i dettagli scadenza
  * @param scadenzario <code>Obbligazione_scadenzarioBulk</code> la scadenza dell'obbligazione per cui creare i dettagli
  *  
 */
protected void creaDettagliScadenzaPerNuoveLineeAttivita (UserContext aUC,ObbligazioneBulk obbligazione, Obbligazione_scadenzarioBulk scadenzario) throws ComponentException
{
	
	Obbligazione_scad_voceBulk osv;
	Obbligazione_scadenzarioBulk os;	
	Linea_attivitaBulk la;
	boolean found;

	//creo i dettagli scadenze se non esistono per le nuove linee di attività

	for ( Iterator lattIterator = obbligazione.getNuoveLineeAttivitaColl().iterator(); lattIterator.hasNext(); )
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
		for ( Iterator i = scadenzario.getObbligazione_scad_voceColl().iterator(); i.hasNext(); )
		{
			osv = (Obbligazione_scad_voceBulk) i.next();
			if ( osv.getCd_centro_responsabilita().equals( la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita()) &&
				 osv.getCd_linea_attivita().equals( la.getLinea_att().getCd_linea_attivita()) )
					found = true;
		}
		if ( !found )
		{
			//creo	nuovo Obbligazione_scad_voceBulk			
			osv = new Obbligazione_scad_voceBulk();
			osv.setToBeCreated();
			if ( obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
			{
				IVoceBilancioBulk articolo = obbligazione.getArticolo( la.getLinea_att().getFunzione().getCd_funzione(), la.getLinea_att().getCentro_responsabilita().getCd_centro_responsabilita());
				osv.setTi_appartenenza( articolo.getTi_appartenenza());
				osv.setTi_gestione( articolo.getTi_gestione());
				osv.setCd_voce( articolo.getCd_voce() );
			}
			else
			{
				IVoceBilancioBulk capitolo = obbligazione.getCapitolo( la.getLinea_att().getFunzione().getCd_funzione() );
				osv.setTi_appartenenza( capitolo.getTi_appartenenza());
				osv.setTi_gestione( capitolo.getTi_gestione());
				osv.setCd_voce( capitolo.getCd_voce() );
			}

			osv.setLinea_attivita( la.getLinea_att() );			
			osv.setCd_linea_attivita( la.getLinea_att().getCd_linea_attivita() );
			osv.setCd_centro_responsabilita( la.getLinea_att().getCd_centro_responsabilita());
			osv.setIm_voce( new java.math.BigDecimal(0));						
			osv.setCd_fondo_ricerca( obbligazione.getCd_fondo_ricerca() );
			// osv.setUser( scadenzario.getUser())		;
			osv.setUser( scadenzario.getObbligazione().getUser() );
			osv.setObbligazione_scadenzario( scadenzario );
			((BulkList) scadenzario.getObbligazione_scad_voceColl()).add( osv );
			
		}	
	}
}
/**
 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
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
  *  Obbligazione provvisoria
  *    PreCondition:
  *      E' stata generata la richiesta di cancellazione di un' obbligazione che ha lo stato provvisorio
  *    PostCondition:
  *      L'obbligazione viene fisicamente cancellata tramite il metodo cancellaObbligazioneProvvisoria
  *
  *  Obbligazione definitiva
  *    PreCondition:
  *      E' stata generata la richiesta di cancellazione di un' obbligazione che ha lo stato definitivo
  *    PostCondition:
  *      L'obbligazione viene stornata e cancellata logicamente tramite il metodo annullaObbligazioneDefinitiva
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da eliminare
  *  
 */ 
  

public void eliminaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		if ( bulk instanceof V_obbligazione_im_mandatoBulk )
		{
			V_obbligazione_im_mandatoBulk v_obbligazione = (V_obbligazione_im_mandatoBulk) bulk;

			if (v_obbligazione.isObbligazioneResiduo())
				bulk = (OggettoBulk) getHome( aUC, ObbligazioneResBulk.class).findByPrimaryKey( new ObbligazioneResBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));
			else if (v_obbligazione.isObbligazioneResiduoImproprio())
				bulk = (OggettoBulk) getHome( aUC, ObbligazioneRes_impropriaBulk.class).findByPrimaryKey( new ObbligazioneRes_impropriaBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));
			else
				bulk = (OggettoBulk) getHome( aUC, ObbligazioneOrdBulk.class).findByPrimaryKey( new ObbligazioneOrdBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));

			if ( bulk == null )
				throw new ApplicationException( "L'impegno e' stato cancellato" );
		}		

		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
		if ( obbligazione.getStato_obbligazione().equals( obbligazione.STATO_OBB_PROVVISORIO ))
			cancellaObbligazioneProvvisoria( aUC, obbligazione );
		else if ( obbligazione.getStato_obbligazione().equals( obbligazione.STATO_OBB_DEFINITIVO ))
			stornaObbligazioneDefinitiva( aUC, obbligazione );
		else // stato = STORNATA
			throw handleException( new it.cnr.jada.comp.ApplicationException( "Lo stato dell'impegno non ne consente la cancellazione/storno"));
	}
	catch ( it.cnr.jada.persistency.PersistencyException e)
	{
		throw handleException( bulk, e );
	}		

}
/**
  * ricerca ordine
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca dell'ordine (se esiste) associato all'obbligazione
  *    PostCondition:
  *      L'ordine associato all'obbligazione viene restituito
  *
  * @param userContext it.cnr.jada.UserContext lo userContext
  * @param obblig ObbligazioneBulk l'oobligazione per la quale e' necessario individuare l'ordine
  * @return it.cnr.contab.doccont00.ordine.bulk.OrdineBulk l'ordine asssociato all'obbligazione oppure null se nessun ordine e'
  *         stato definito per l'obbligazione 
 */
public it.cnr.contab.doccont00.ordine.bulk.OrdineBulk findOrdineFor(UserContext userContext, ObbligazioneBulk obblig) throws ComponentException {

	try{
		
		it.cnr.contab.doccont00.ordine.bulk.OrdineHome ordHome = (it.cnr.contab.doccont00.ordine.bulk.OrdineHome)getHome(userContext,it.cnr.contab.doccont00.ordine.bulk.OrdineBulk.class);
		return ordHome.findOrdineFor(obblig);
		
	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(obblig,ex);
	}
}
/** 
  *  creazione scadenza/modifica importo - imputazione automatica
  *    PreCondition:
  *      L'utente ha richiesto l'imputazione automatica dell'obbligazione e ha creato una scadenza o ha modificato l'importo
  *      di una scadenza esistente
  *    PostCondition:
  *      Per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneObbligazione viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per riaprtire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  creazione scadenza/modifica importo - imputazione manuale
  *    PreCondition:
  *      L'utente ha specificato l'imputazione manuale dell'obbligazione e ha creato una scadenza o ha modificato l'importo
  *      di una scadenza esistente
  *    PostCondition:
  *      Per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  conferma imputazione finanziaria - imputazione automatica
  *    PreCondition:
  *      L' utente ha completato l'imputazione finanziaria, confermando le linee di attività selezionate, e ha richiesto la ripartizione automatica degli importi
  *      delle scadenze
  *    PostCondition:
  *      Per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneObbligazione viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per ripartire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  conferma imputazione finanziaria - imputazione manuale
  *    PreCondition:
  *      L' utente ha completato l'imputazione finanziaria, confermando le linee di attività selezionate, e ha selezionato la ripartizione manuale degli importi
  *      delle scadenze
  *    PostCondition:
  *      Per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *  modifica imputazione finanziaria - imputazione automatica
  *    PreCondition:
  *      L' utente ha modificato l'imputazione finanziaria definita per l'obbligazione e ha richiesto la ripartizione automatica degli importi
  *      delle scadenze
  *    PostCondition:
  *      Tutti i dettagli delle scadenze dell'obbligazione che facevano riferimento a linee di attività non più selezionate
  *      vengono cancellati
  *      Per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
  *      il metodo calcolaPercentualeImputazioneObbligazione viene utilizzato per determinare le percentuali
  *      assegnate ad ogni linea d'attività/capitolo e per ripartire l'importo della scadenza sui vari dettagli
  *      in base a tali percentuali
  *  modifica imputazione finanziaria - imputazione manuale
  *    PreCondition:
  *      L' utente ha modificato l'imputazione finanziaria definita per l'obbligazione e ha selezionato la ripartizione manuale degli importi
  *      delle scadenze
  *    PostCondition:
  *      Tutti i dettagli delle scadenze dell'obbligazione che facevano riferimento a linee di attività non più selezionate
  *      vengono cancellati
  *      Per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e presente nel piano di gestione viene creato un dettaglio di 
  *      scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerLineeAttivitaDaPdG);
  *      Analogamente, per ogni scadenza dell'obbligazione e per ogni nuova linea di attività selezionata dall'utente e non presente nel piano di gestione viene creato 
  *      un dettaglio di scadenza Obbligazione_scad_vocebulk (metodo creaDettagliScadenzaPerNuoveLineeAttivita);
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
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui creare i dettagli scadenza
  * @param scadenzario <code>Obbligazione_scadenzarioBulk</code> la scadenza dell'obbligazione per cui creare i dettagli oppure
  *        <code>null</code> se e' necessario generare i dettagli per tutte le scadenze
  *  
  *      
 */
public ObbligazioneBulk generaDettagliScadenzaObbligazione (UserContext aUC,ObbligazioneBulk obbligazione,Obbligazione_scadenzarioBulk scadenzario) throws ComponentException {
	return generaDettagliScadenzaObbligazione(aUC, obbligazione, scadenzario, true);
}	

protected ObbligazioneBulk generaDettagliScadenzaObbligazione (UserContext aUC,ObbligazioneBulk obbligazione,Obbligazione_scadenzarioBulk scadenzario, boolean allineaImputazioneFinanziaria) throws ComponentException
{
	Obbligazione_scadenzarioBulk os;

	// non e' ancora stata selezionata l'imputazione finanziaria
	if (obbligazione.getLineeAttivitaSelezionateColl().size() == 0 &&
		obbligazione.getNuoveLineeAttivitaColl().size() == 0)
		return obbligazione;

	// la somma delle percentuali delle nuove linee di attività e' diversa da 100
	if (obbligazione.getLineeAttivitaSelezionateColl().size() == 0 &&
		obbligazione.getNuoveLineeAttivitaColl().size() > 0)
	{
		BigDecimal tot = new BigDecimal(0);
		for ( Iterator i = obbligazione.getNuoveLineeAttivitaColl().iterator(); i.hasNext(); )
			tot = tot.add( ((Linea_attivitaBulk)i.next()).getPrcImputazioneFin());
		if ( tot.compareTo( new BigDecimal(100)) != 0 )
			throw new ApplicationException( "La somma delle percentuali dei nuovi GAE e' diversa da 100");			
			
	}
	// la somma delle percentuali delle nuove linee di attività e' maggiore di 100
	else if ( obbligazione.getNuoveLineeAttivitaColl().size() > 0)
	{
		BigDecimal tot = new BigDecimal(0);
		for ( Iterator i = obbligazione.getNuoveLineeAttivitaColl().iterator(); i.hasNext(); )
			tot = tot.add( ((Linea_attivitaBulk)i.next()).getPrcImputazioneFin());
		if ( tot.compareTo( new BigDecimal(100)) > 0 )
			throw new ApplicationException( "La somma delle percentuali dei nuovi GAE e' maggiore di 100");			
			
	}		
	//imputazione automatica impossibile
	if ( obbligazione.getFl_calcolo_automatico().booleanValue() )
	{
		BigDecimal totaleLattDaPdg = new BigDecimal(0);
		for ( Iterator i = obbligazione.getLineeAttivitaSelezionateColl().iterator(); i.hasNext(); )
			totaleLattDaPdg = totaleLattDaPdg.add(((V_pdg_obbligazione_speBulk) i.next()).getImporto());
		if ( obbligazione.getLineeAttivitaSelezionateColl().size() > 0 && totaleLattDaPdg.doubleValue() == 0 )
			throw new ApplicationException( "GAE da PdG con costi/spese nulli. Imputazione automatica impossibile!");
	}		
		

	// non sono ancora state inserite le scadenze
	if (obbligazione.getObbligazione_scadenzarioColl().size() == 0 )
		return obbligazione;

	
	if ( scadenzario != null ) // una sola scadenza e' stata modificata
	{
		//creo i dettagli della scadenza per le linee attivita da PDG
		creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, obbligazione, scadenzario );

		//creo i dettagli della scadenza per le nuove linee attivita 		
		creaDettagliScadenzaPerNuoveLineeAttivita( aUC, obbligazione, scadenzario );
	}	// imputazione finanziaria e' stata modificata, quindi rigenero i dettagli per tutte le scadenze
	else
	{
			
		// per ogni scadenza aggiorno i suoi dettagli in base alle linee di attività specificate dall'utente
		for ( Iterator scadIterator = obbligazione.getObbligazione_scadenzarioColl().iterator(); scadIterator.hasNext(); )
		{
				os = (Obbligazione_scadenzarioBulk) scadIterator.next();		
				//cancello i dettagli della scadenza per le linee attivita che non esistono piu'
				cancellaDettaglioScadenze( aUC, obbligazione, os );

				//creo i dettagli della scadenza per le linee attivita da PDG
				creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, obbligazione, os );

				//creo i dettagli della scadenza per le nuove linee attivita 		
				creaDettagliScadenzaPerNuoveLineeAttivita( aUC, obbligazione, os );
		}
	}
	
	if ( obbligazione.getFl_calcolo_automatico().booleanValue() && allineaImputazioneFinanziaria)
		obbligazione = calcolaPercentualeImputazioneObbligazione( aUC, obbligazione );

	return obbligazione;
}
/** 
  *  creazione prospetto
  *    PreCondition:
  *      L'utente richiede la visualizzazione del prospetto spese per una obbligazione.
  *    PostCondition:
  *      L'applicazione crea un report contenente la situazione 'spese' per una obbligazione e per i cdr che
  *      l'utente ha seelzionato.
  *      Il prospetto avrà una riga per ogni linea di attività relativa ai piani di gestione dei CdR 
  *      considerati nell'obbligazione. Il formatto sarà:
  *      
  *      Colonna 1: Linea di attività
  *      Colonna 2: Spese previste nel pdg, calcolati per il 1° esercizio = somma degli importi delle colonne (I), (K), (Q), (S) e (U)
  *      Colonna 3: Spese previste nel pdg, calcolati per il 2° esercizio = somma degli importi delle colonne (AC), (AE) e (AG)
  *      Colonna 4: Spese previste nel pdg, calcolati per il 3° esercizio = somma degli importi delle colonne (AC), (AE) e (AG)  
  *      Colonna 5: Totale Obbligazioni emesse i cui dettagli corrispondono per CdR e LdA nel 1° esercizio
  *      Colonna 6: Totale Obbligazioni emesse i cui dettagli corrispondono per CdR e LdA nel 2° esercizio
  *      Colonna 7: Totale Obbligazioni emesse i cui dettagli corrispondono per CdR e LdA nel 3° esercizio    
  *  valutazione prospetto
  *    PreCondition:
  *      I dati necessari per il prospetto sono stati raccolti.
  *    PostCondition:
  *      Il delta risultante dal prospetto (Colonna 5 - Colonna 2, Colonna 6 - Colonna 3, Colonna 7 - Colonna4 ) viene confrontato 
  *      con l'importo del totale delle linee di attività 
  *      appartenenti allo stesso CdR, nel caso che detto importo sia maggiore del delta risultante, 
  *      il sistema restituisce un messaggio di 'segnalazione' (non bloccante) con il quale avverte il responsabile 
  *      della possibilità di  'sfondamento'. Il controllo sarà ripetuto per ogni cdr coinvolto nei dettagli delle obbligazioni.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cdrList la lista di CdrBulk per cui generare il prospetto spese
  * @return la lista di <code>V_obblig_pdg_saldo_laBulk</code> coi dati relativi alle linee di attività dei Cdr selezionati
  *  
 */
public List generaProspettoSpeseObbligazione (UserContext userContext,List cdrList) throws ComponentException
{
	try
	{
		
		if ( cdrList.size() == 0 )
			throw new ApplicationException( "E' necessario selezionare almeno un Cdr");
		SQLBuilder sql = getHome( userContext, V_obblig_pdg_saldo_laBulk.class ).createSQLBuilder();
		sql.addClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext) userContext).getEsercizio());
		Iterator i = cdrList.iterator();
		sql.addClause( "AND", "cd_centro_responsabilita", sql.EQUALS, ((CdrBulk)i.next()).getCd_centro_responsabilita());
//in realtà l'utente può entrare sempre con un solo cdr		
		while ( i.hasNext() )
			sql.addClause( "OR", "cd_centro_responsabilita", sql.EQUALS, ((CdrBulk)i.next()).getCd_centro_responsabilita());
//		sql.addOrderBy( "cd_centro_responsabilita" );
		return getHome( userContext, V_obblig_pdg_saldo_laBulk.class ).fetchAll( sql );
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}		
	
 }
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
  *  CDR NRUO
  *    PreCondition:
  *      se CD_PROPRIO != '00'
  *    PostCondition:
  *      Restituisce 3
  *  CDR RUO
  *    PreCondition:
  *      CD_PROPRIO = '00' e LIVELLO = 2
  *      
  *    PostCondition:
  *      Restituisce 2
  *  CDR I
  *    PreCondition:
  *      LIVELLO = 1
  *    PostCondition:
  *      Restituisce 1
  *  AC
  *    PreCondition:
  *      LIVELLO = 1 e UNITA_ORGANIZZATIVA.CD_TIPO_UNITA = 'ENTE'
  *    PostCondition:
  *      Restituisce 0
 */

private int getLivelloResponsabilitaCDR(UserContext userContext, CdrBulk cdr) throws ComponentException {
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
					return Stampa_obbligazioni_LAVBulk.LV_AC;

			}

			return Stampa_obbligazioni_LAVBulk.LV_CDRI;

		} else if( (cdr.getLivello().intValue() == 2 && Integer.parseInt(cdr.getCd_proprio_cdr()) == 0)) {
			// Sel cdr.livello == 2 e codice proprio = 0

			return Stampa_obbligazioni_LAVBulk.LV_RUO;

		} else {
			// Ogni altro livello o combinazione è livello 3

			return Stampa_obbligazioni_LAVBulk.LV_NRUO;

		}
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw new ComponentException(e);
	}
}

/** 
  *  Calcolo della disponibilità di cassa del Cds per l'esercizio di scrivania o per 
  *	 i due esercizi successivi a quello di scrivania.
  *    PreCondition:
  *      E' stato richiesto di visualizzare la disponibilità di cassa per
  *		 l'obbligazione che ha esercizio corrente uguale a quello di scrivania
  *		 (esercizio di competenza = esercizio) o per i suoi due esercizi successivi
  *		 (esercizio di competenza = esercizio + n, con n=1,2). In quest'ultimo caso
  *		 l'utente deve aver selezionato la voce del piano dell'obbligazione.
  *    PostCondition:
  *      Viene richiamata una stored procedure (getMassaSpendibile) che calcola 
  *		 la disponibilità di cassa del Cds dell'obbligazione.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param esercizio_competenza <code>String</code> esercizio di competenza dell'obbligazione
  * @param esercizio <code>String</code> esercizio di scrivania
  * @param cd_cds <code>String</code> codice del centro di spesa dell'obbligazione
  * @param cd_elemento_voce <code>String</code> codice dell'elemento voce dell'obbligazione
  * @return disp_cassa_cds <code>BigDecimal</code> disponibilità di cassa del Cds
  *
*/
private BigDecimal getMassaSpendibile ( UserContext userContext, Integer esercizio_competenza, Integer esercizio, String cd_cds, String cd_elemento_voce ) throws ComponentException
{
	try
	{
		BigDecimal disp_cassa_cds;
		
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
			"{ ? = call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"CNRCTB030.getMassaSpendibile(?, ?, ?, ?)}",false,this.getClass());
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
/** 
  *  Esercizio non aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in uno stato diverso da APERTO
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che non e' possibile creare obbligazioni.
  *  Esercizio aperto
  *    PreCondition:
  *      L'esercizio di scrivania e' in stato APERTO
  *    PostCondition:
  *      una istanza di ObbligazioneBulk viene restituita con impostata la data del giorno come data di emissione e
  *      il Cds da cui dipende l'UO di scrivania come Cds dell'obbligazione
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da inizializzare
  * @return l'istanza di  <code>ObbligazioneBulk</code> inizializzata
  *
 */
public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException
 {
	ObbligazioneBulk obbligazione = (ObbligazioneBulk)  bulk;	
	try
	{
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk)getHome( aUC, Unita_organizzativa_enteBulk.class).findAll().get(0);

		// se l'unità organizzativa è uguale a quella dell'Ente, non è possibile creare obbligazioni
		if ( obbligazione.getCd_unita_organizzativa().equals( uoEnte.getCd_unita_organizzativa() ))
			throw new ApplicationException("Funzione non consentita per utente abilitato a " + uoEnte.getCd_unita_organizzativa() ); 

		obbligazione.setDt_registrazione( DateServices.getDt_valida(aUC));
		obbligazione.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( obbligazione.getUnita_organizzativa().getUnita_padre() ));
		verificaStatoEsercizio( aUC, obbligazione.getEsercizio(), obbligazione.getCd_cds());
//		if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
//			throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );
			
		if(obbligazione.getCreditore()!=null && obbligazione.getCreditore().getCd_terzo()!=null){
			ObbligazioneHome obbligHome = (ObbligazioneHome) getHome( aUC, obbligazione.getClass());
			TerzoHome terzoHome = (TerzoHome) getHome( aUC, TerzoBulk.class);
			
			if(obbligHome.selectCreditoreByClause(obbligazione, terzoHome, new TerzoBulk(obbligazione.getCreditore().getCd_terzo()), null).executeCountQuery(getHomeCache(aUC).getConnection())==0)
				throw new ApplicationException("Funzione non consentita per terzo disabilitato.");
		}
		return super.inizializzaBulkPerInserimento(aUC, obbligazione );
		
	}
	catch ( Exception e )
	{
		throw handleException(obbligazione, e);
	}
	
}
//^^@@

	@Override
	public OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk[] aoggettobulk) throws ComponentException {
		return super.modificaConBulk(usercontext, aoggettobulk);
	}

/**
  *  Obbligazione non esiste
  *    PreCondition:
  *      L'obbligazione richiesta non esiste.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'obbligazione non è stata trovata. L'attività non è consentita.
  *  Obbligazione trovata
  *    PreCondition:
  *      L'obbligazione richiesta è stata trovata.
  *    PostCondition:
  *      L'obbligazione viene caricata normalmente. L'imputazione finanziaria è impostata una volta sola al livello di testata, e poi vale per tutte le scadenze nello scadenzario. In questo caso l'applicazione ricava le informazione per l'imputazione finanziaria dalla prima scadenza dello scadenzario.
  *  Scadenzario dell'obbligazione non esiste
  *    PreCondition:
  *      L'obbligazione richiesta esiste, ma lo scadenzario per l'obbligazione non esiste.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che lo scadenzario non è stato trovato. L'attività non è consentita.
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> da inizializzare
  * @return l'istanza di  <code>ObbligazioneBulk</code> inizializzata
  *
 */
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk obbligazione) throws ComponentException
 {
	try
	{
		if ( obbligazione instanceof V_obbligazione_im_mandatoBulk ) {
			V_obbligazione_im_mandatoBulk v_obbligazione = (V_obbligazione_im_mandatoBulk) obbligazione;

			if (((ObbligazioneBulk)v_obbligazione).isObbligazioneResiduo())
				obbligazione = (OggettoBulk) getHome( aUC, ObbligazioneResBulk.class).findByPrimaryKey( new ObbligazioneResBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));
			else if (((ObbligazioneBulk)v_obbligazione).isObbligazioneResiduoImproprio())
				obbligazione = (OggettoBulk) getHome( aUC, ObbligazioneRes_impropriaBulk.class).findByPrimaryKey( new ObbligazioneRes_impropriaBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));
			else
				obbligazione = (OggettoBulk) getHome( aUC, ObbligazioneOrdBulk.class).findByPrimaryKey( new ObbligazioneOrdBulk( v_obbligazione.getCd_cds(), v_obbligazione.getEsercizio(), v_obbligazione.getEsercizio_originale(), v_obbligazione.getPg_obbligazione() ));

			if ( obbligazione == null )
				throw new ApplicationException( "L'impegno e' stato cancellato" );
		}		

		ObbligazioneBulk obblig = (ObbligazioneBulk) super.inizializzaBulkPerModifica( aUC, obbligazione );

		//imposta l'unita' organizzativa 
//		Unita_organizzativaBulk uo = new Unita_organizzativaBulk();
//		uo.setCd_unita_organizzativa(obblig.getCd_unita_organizzativa());
//		obblig.setUnita_organizzativa( (Unita_organizzativaBulk) getHome( aUC, Unita_organizzativaBulk.class ).findByPrimaryKey( uo ) );

		//imposto il cds
//		obblig.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( obblig.getUnita_organizzativa().getUnita_padre() ));		

		obblig.setIm_mandati( new BigDecimal(0));
	
		// carica lo scadenzario e i suoi dettagli
		ObbligazioneHome obbligHome = (ObbligazioneHome) getHome( aUC, obbligazione.getClass());
		Obbligazione_scadenzarioHome osHome = (Obbligazione_scadenzarioHome) getHome( aUC, Obbligazione_scadenzarioBulk.class );
		
		obblig.setObbligazione_scadenzarioColl( new BulkList( obbligHome.findObbligazione_scadenzarioList( obblig ) ));
		obblig.setObbligazioniPluriennali(new BulkList(obbligHome.findObbligazioniPluriennali(aUC,obblig)));


		for ( Iterator i = obblig.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
		{
			Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk) i.next();
			initializeKeysAndOptionsInto( aUC, os );
			os.setObbligazione( obblig );
			os.setStatus( os.STATUS_CONFIRMED);
			os.setObbligazione_scad_voceColl( new BulkList( osHome.findObbligazione_scad_voceList(aUC, os )));
			
			//per ogni scadenza carico l'eventuale doc.passivo
			V_doc_passivo_obbligazioneBulk docPassivo = osHome.findDoc_passivo( os );
			if ( docPassivo != null)
			{
				os.setEsercizio_doc_passivo( docPassivo.getEsercizio());
				os.setPg_doc_passivo( docPassivo.getPg_documento_amm());
				os.setCd_tipo_documento_amm(docPassivo.getCd_tipo_documento_amm());
			}	

			//per ogni scadenza carico l'eventuale mandato
			Mandato_rigaBulk mandato = osHome.findMandato( os );
			if ( mandato != null )
			{
				os.setEsercizio_mandato( mandato.getEsercizio());
				os.setPg_mandato( mandato.getPg_mandato());
				obblig.setIm_mandati( obblig.getIm_mandati().add( mandato.getIm_mandato_riga()));
			}				
			// per ogni dettaglio imposto la percentuale
			for ( Iterator j = os.getObbligazione_scad_voceColl().iterator(); j.hasNext(); )
			{
				Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk)j.next();
				osv.setObbligazione_scadenzario( os );
				if ( os.getIm_scadenza().doubleValue() != 0 )
					osv.setPrc( (osv.getIm_voce().multiply( new BigDecimal(100)).divide( os.getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP)));
				else
					osv.setPrc( new BigDecimal(0))	;
			}	


			/*	// per ogni dettaglio carico la linea di attività
			for ( Iterator j = os.getObbligazione_scad_voceColl().iterator(); j.hasNext(); )
			{
				Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk)j.next();
				osv.setLinea_attivita( (Linea_attivitaBulk) getHome( aUC, Linea_attivitaBulk.class ).findByPrimaryKey( new Linea_attivitaKey( osv.getObbligazione_scadenzario().getObbligazione().getCds().getEsercizio(), osv.getCd_linea_attivita(), osv.getCd_centro_responsabilita() )));
			}	
			*/
		}

		// carica i capitoli di spesa del CDS
		obblig = listaCapitoliPerCdsVoce( aUC, obblig );
		obblig.refreshCapitoliDiSpesaCdsSelezionatiColl();

		// carica i cdr
		obblig.setCdrColl( listaCdrPerCapitoli( aUC,  obblig));
		obblig.refreshCdrSelezionatiColl();

		// carica le linee di attività da PDG
		obblig.setLineeAttivitaColl( listaLineeAttivitaPerCapitoliCdr( aUC,  obblig));
		obblig.refreshLineeAttivitaSelezionateColl();

		// carica le nuove linee di attività
		obblig = obbligHome.refreshNuoveLineeAttivitaColl( aUC, obblig );

		obblig.setInternalStatus( ObbligazioneBulk.INT_STATO_LATT_CONFERMATE );
		obblig.setIm_iniziale_obbligazione( obblig.getIm_obbligazione());
		obblig.setCd_iniziale_elemento_voce( obblig.getCd_elemento_voce());
		obblig.setCd_terzo_iniziale( obblig.getCd_terzo());

		// SETTO IL FLAG CHE SERVE PER CAPIRE SE OCCORRE RICHIEDERE L'INSERIMENTO DELLA VOCE NUOVA DA UTILIZZARE PER IL RIBALTAMENTO
		// LA VOCE VIENE RICHIESTA SOLO SE NON PRESENTE L'ASSOCIAZIONE NELLA TABELLA ASS_EVOLD_EVNEWBULK
		obblig.setEnableVoceNext(!existAssElementoVoceNew(aUC,(ObbligazioneBulk)obbligazione));

		return obblig;
	}
	catch( Exception e )
	{
		throw handleException( e );
	}		

}
/** 
  *  inizializzazione per inserimento
  *    PreCondition:
  *      La richiesta di inizializzazione di un ImpegnoPGiroBulk per inserimento
  *      e' stata generata
  *    PostCondition:
  *      Viene impostata la data di registrazione dell'obbligazione con la data odierna, 
  *		 il codice Cds e il codice Cds di origine con il codice Cds di scrivania
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da inizializzare
  * @return l'istanza di  <code>ObbligazioneBulk</code> inizializzata
 */
public OggettoBulk inizializzaBulkPerRicerca (UserContext aUC,OggettoBulk bulk) throws ComponentException
{

	try
	{
		if ( bulk instanceof ObbligazioneBulk)
		{
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
			obbligazione.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( new CdsBulk(((CNRUserContext) aUC).getCd_cds())));
			obbligazione.setCd_cds_origine( ((CNRUserContext) aUC).getCd_cds() );
		// if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
		//	throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );
   	
			return super.inizializzaBulkPerRicerca( aUC, obbligazione );
		}
		else
			return super.inizializzaBulkPerRicerca( aUC, bulk );		
	}
	catch ( it.cnr.jada.persistency.PersistencyException e )
	{
		throw handleException(bulk, e);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_obbligazioni_LAVBulk stampa) throws ComponentException{

	// Imposta i parametri CdS, Esercizio, UO, prendeno come rif. i valori di scrivania
	stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));
	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
	stampa.setCdUoForPrint(CNRUserContext.getCd_unita_organizzativa(userContext));

	// Setta la Data Inizio e Data Fine di default	
	stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(getDataOdierna(userContext));
	
	
	CdrBulk cdrUtente = cdrFromUserContext(userContext);
	Unita_organizzativaBulk uoPadre = null;
	
	try{
		uoPadre = (Unita_organizzativaBulk)getHome(userContext, cdrUtente.getUnita_padre()).findByPrimaryKey(cdrUtente.getUnita_padre());
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new it.cnr.jada.comp.ComponentException(pe);
	}

	stampa.setCdrUtente(cdrUtente);
	
	stampa.setLivello_Responsabilita(getLivelloResponsabilitaCDR(userContext, cdrUtente));

	stampa.setCdrForPrint(new CdrBulk());
	stampa.setIsCdrForPrintEnabled(true);
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_obbligazioni_riportabiliVBulk stampa) throws ComponentException{


	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));	
	stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));
	
	try {
		Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));

	
		if (!uoScrivania.isUoCds()){
			stampa.setUoForPrint(uoScrivania);
			stampa.setIsUOForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		}
		
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(stampa, e);
	}


}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_registro_annotazione_spese_pgiroBulk stampa) throws ComponentException{

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));	

	stampa.setStato_obbligazione(stampa.STATO_OBB_TUTTI);
	
	stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(getDataOdierna(userContext));
	stampa.setPgInizio(new Integer(0));
	stampa.setPgFine(new Integer(999999999));

	stampa.setCdsOrigineForPrint(new CdsBulk());
	try {
		Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));

		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);			
		CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

		if (stampa.isStampa_cnr()){				
			if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
				stampa.setCdsOrigineForPrint(new CdsBulk());
				stampa.setIsCdsForPrintEnabled(true);
			} else {
				stampa.setCdsOrigineForPrint(cds_scrivania);
				stampa.setIsCdsForPrintEnabled(false);
			}
		} else {
			stampa.setCdsOrigineForPrint(cds_scrivania);			
		}
	
		if (!uoScrivania.isUoCds()){
			stampa.setUoForPrint(uoScrivania);
			stampa.setIsUOForPrintEnabled(false);
		} else {
			stampa.setUoForPrint(new Unita_organizzativaBulk());
			stampa.setIsUOForPrintEnabled(true);
		}
		
		
		//stampa.setCdsUOInScrivania(uoScrivania.isUoCds());
		//stampa.setUoForPrint((stampa.isCdsUOInScrivania())?new Unita_organizzativaBulk():uoScrivania);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(stampa, e);
	}
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_registro_obbligazioniBulk stampa) throws ComponentException{

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
	stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

	try {
		Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
		stampa.setCdsUOInScrivania(uoScrivania.isUoCds());
		stampa.setUoForPrint((stampa.isCdsUOInScrivania())?new Unita_organizzativaBulk():uoScrivania);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(stampa, e);
	}
	
	stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(getDataOdierna(userContext));
	stampa.setPgInizio(new Long(0));
	stampa.setPgFine(new Long("9999999999"));

	stampa.setStato_obbligazione(stampa.STATO_OBB_TUTTI);
	stampa.setCd_tipo_documento_cont(stampa.TIPO_TUTTI);
}
/**
 * inizializzaBulkPerStampa method comment.
 */
private void inizializzaBulkPerStampa(UserContext userContext, Stampa_scadenzario_obbligazioniBulk stampa) throws ComponentException{

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
	stampa.setCd_cds(CNRUserContext.getCd_cds(userContext));

	try {
		Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
		stampa.setCdsUOInScrivania(uoScrivania.isUoCds());
		stampa.setUoForPrint((stampa.isCdsUOInScrivania())?new Unita_organizzativaBulk():uoScrivania);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(stampa, e);
	}

	stampa.setDataInizio(DateServices.getFirstDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
	stampa.setDataFine(DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue()));
}


private void inizializzaBulkPerStampa(UserContext userContext, Stampa_obb_doc_ammBulk stampa) throws ComponentException{

	stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));	

	try {
//		Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
//		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)home.findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));

		String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);			
		CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
		CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));
		
			if (!cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
				stampa.setCdsForPrint(cds_scrivania);
				stampa.setCdsForPrintEnabled(true);
				stampa.setEsercizioDocForPrintEnabled(true);
			} else {
				stampa.setCdsForPrintEnabled(false);
				stampa.setEsercizioDocForPrintEnabled(false);
			}
		
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(stampa, e);
	}
}

/**
 * inizializzaBulkPerStampa method comment.
 */
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
try{
	if (bulk instanceof Stampa_registro_obbligazioniBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_registro_obbligazioniBulk)bulk);
	else if (bulk instanceof Stampa_registro_annotazione_spese_pgiroBulk){
		EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class).findAll().get(0);
		((Stampa_registro_annotazione_spese_pgiroBulk)bulk).setCdsEnte(ente);
		inizializzaBulkPerStampa(userContext, (Stampa_registro_annotazione_spese_pgiroBulk)bulk);
	}
	else if (bulk instanceof Stampa_scadenzario_obbligazioniBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_scadenzario_obbligazioniBulk)bulk);
	else if (bulk instanceof Stampa_obbligazioni_riportabiliVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_obbligazioni_riportabiliVBulk)bulk);
	else if (bulk instanceof Stampa_obbligazioni_LAVBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_obbligazioni_LAVBulk)bulk);
	else if (bulk instanceof Stampa_obb_doc_ammBulk)
		inizializzaBulkPerStampa(userContext, (Stampa_obb_doc_ammBulk)bulk);
		
	return bulk;
} catch (it.cnr.jada.persistency.PersistencyException e) {
	throw handleException( e);
}
}
/** 
  *  Tipologia CdS è 'SAC'
  *    PreCondition:
  *      L'utente ha specificato una voce del piano in testata di una obbligazione appartenente al cds SAC
  *    PostCondition:
  *      L'elenco degli articoli di spesa CDS presenti nel piano dei conti Parte 1, aventi come titolo-capitolo la voce del piano selezionata dall'utente,
  *      viene presentato all'utente, evidenziandone la funzione
  *  Tipologia CdS è diverso da 'SAC'
  *    PreCondition:
  *      L'utente ha specificato una voce del piano in testata di una obbligazione appartenente ad un cds con tipologia diversa da SAC
  *    PostCondition:
  *      L'elenco dei capitoli di spesa CDS presenti nel piano dei conti Parte 1, aventi come titolo-capitolo la voce del piano selezionata dall'utente,
  *      viene presentato all'utente, evidenziandone la funzione
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui recuperare i capitoli
  * @return ObbligazioneBulk l'obbligazione con i capitoli impostati
  *
 */
public ObbligazioneBulk listaCapitoliPerCdsVoce (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		ObbligazioneHome obbligazioneHome = (ObbligazioneHome) getHome( aUC, obbligazione.getClass());
		obbligazione.setCapitoliDiSpesaCdsColl( obbligazioneHome.findCapitoliDiSpesaCds( obbligazione ));
	}
	catch ( Exception e )
	{
		throw handleException( obbligazione, e );
	}

	return obbligazione;
}
/** 
  *  CdS diverso da 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato dei capitoli di spesa CDS per un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Viene estratto l'elenco dei Cdr appartenenti all'uo di scrivania per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SINGOLA e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente
  *  CdS diverso da 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato dei capitoli di spesa CDS per un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Viene estratto l'elenco dei Cdr appartenenti all'uo di scrivania per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SINGOLA e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente; a tale elenco viene aggiunto
  *      quello ottenuto estraendo i Cdr per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SCARICO e la cui linea di attività collegata appartiene all'uo di scrivania e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente
  *  CdS 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato degli articoli di spesa CDS per un'obbligazione appartenente al cds SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Fra tutti i Cdr selezionati implicitamente dall'utente con la selezione degli articoli viene estratto 
  *      l'elenco di quelli per i quali sono presenti nel piano di gestione delle linee di attività
  *      con categoria dettaglio = SINGOLA e
  *      il cui cdr e funzione sono uguali ad uno di quelli degli articoli selezionati dall'utente
  *  CdS 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato degli articoli di spesa CDS per un'obbligazione appartenente al cds SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Fra tutti i Cdr selezionati implicitamente dall'utente con la selezione degli articoli viene estratto 
  *      l'elenco di quelli per i quali sono presenti nel piano di gestione delle linee di attività
  *      con categoria dettaglio = SINGOLA e
  *      il cui cdr e funzione e' uguale ad una di quelle degli articoli selezionati dall'utente; a tale elenco viene aggiunto
  *      quello ottenuto estraendo i Cdr per i quali sono presenti nel piano di gestione delle linee di attività,
  *      con categoria dettaglio = SCARICO e la cui linea di attività collegata ha cdr e funzione uguali
  *      ad uno di quelli selezionati dall'utente con la selezione degli articoli di spesa
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui recuperare i cdr
  * @return ObbligazioneBulk l'obbligazione con i cdr impostati
  
  */
public Vector listaCdrPerCapitoli (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		Vector cdr = new Vector();
		ObbligazioneHome obbligazioneHome = (ObbligazioneHome) getHome( aUC, obbligazione );
		if(obbligazione.getEsercizio_originale()==null)
			throw new ApplicationException("Valorizzare Esercizio Impegno");
		if ( !obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
		{
		
			cdr.addAll( obbligazioneHome.findCdr( (List) obbligazione.getCapitoliDiSpesaCdsSelezionatiColl(), obbligazione ));
			if (obbligazione.isSpesePerCostiAltrui() )
				cdr.addAll( obbligazioneHome.findCdrPerSpesePerCostiAltrui( (List) obbligazione.getCapitoliDiSpesaCdsSelezionatiColl(), obbligazione ));
		}		
		else
		{
			cdr.addAll( obbligazioneHome.findCdrPerSAC( (List) obbligazione.getCapitoliDiSpesaCdsSelezionatiColl(), obbligazione ));
			if (obbligazione.isSpesePerCostiAltrui() )
				cdr.addAll( obbligazioneHome.findCdrPerSpesePerCostiAltruiPerSAC( (List) obbligazione.getCapitoliDiSpesaCdsSelezionatiColl(), obbligazione ));
			
		}	
		
		return cdr;
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}
}
/** 
  *  CdS diverso da 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e funzione uguale ad una di quelle selezionate implicitamente dall'utente con la selezione dei capitoli.
  *  CdS diverso da 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente ad un cds diverso da SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e funzione uguale ad una di quelle selezionate implicitamente dall'utente con la selezione dei capitoli;
  *      a tale elenco viene aggiunto quello ottenuto estraendo le linee di attività presenti nel piano di gestione
  *      con categoria dettaglio = SCARICO e la cui linea di attività collegata appartiene all'uo di scrivania e
  *      la cui funzione e' uguale ad una di quelle dei capitoli di spesa selezionati dall'utente
  *  CdS 'SAC' - obbligazione non Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente al cds SAC e per la
  *      quale non e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e cdr e funzione uguali ad uno di quelli selezionati implicitamente dall'utente con la selezione degli articoli.
  *  CdS 'SAC' - obbligazione Spese per Costi Altrui
  *    PreCondition:
  *      L'utente ha selezionato i capitoli di spesa CDS e i cdr per eseguire l'imputazione finanziaria
  *      di un'obbligazione appartenente al cds SAC e per la
  *      quale e' stato selezionato il flag Spese per Costi Altrui
  *    PostCondition:
  *      Vengono estratte tutte le linee di attività presenti nel piano di gestione con categoria dettaglio = SINGOLA e
  *      cdr uguale ad uno di quelli selezionati dall'utente
  *      e cdr e funzione uguali ad uno di quelli selezionati implicitamente dall'utente con la selezione degli articoli;
  *      a tale elenco viene aggiunto quello ottenuto estraendo le linee di attività presenti nel piano di gestione
  *      con categoria dettaglio = SCARICO il cui cdr e' uno di quelli selezionati dall'utente e la cui linea di attività 
  *      collegata ha cdr e funzione uguale ad uno di quelli selezionati implicitamente dall'utente 
  *      con la selezione degli articoli
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param obbligazione <code>ObbligazioneBulk</code> l'obbligazione per cui recuperare le linee di attività
  * @return ObbligazioneBulk l'obbligazione con le linee di attività impostate
  *
  */
public Vector listaLineeAttivitaPerCapitoliCdr (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		Vector lineeAttivita = new Vector();
		List cdrDiScrivaniaColl = (List) obbligazione.getCdrDiScrivaniaSelezionatiColl( ((CNRUserContext)aUC).getCd_unita_organizzativa());
		ObbligazioneHome obbligazioneHome = (ObbligazioneHome) getHome( aUC, obbligazione );
		if ( !obbligazione.getCds().getCd_tipo_unita().equalsIgnoreCase( it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
		{	
			lineeAttivita.addAll( obbligazioneHome.findLineeAttivita( cdrDiScrivaniaColl, (List)obbligazione.getCapitoliDiSpesaCdsSelezionatiColl(), obbligazione ));
			if (obbligazione.isSpesePerCostiAltrui() )
				lineeAttivita.addAll( obbligazioneHome.findLineeAttivitaPerSpesePerCostiAltrui( (List)obbligazione.getCdrSelezionatiColl(),(List) obbligazione.getCapitoliDiSpesaCdsSelezionatiColl(), ((CNRUserContext) aUC).getCd_unita_organizzativa(), obbligazione ));
		}
		else //SAC
		{
			List capitoli = new LinkedList(); //elimina dalla lista dei capitoli selezionati quelli che hanno cdr non
			//selezionati dall'utente nella lista Cdr
			for (Iterator i = obbligazione.getCapitoliDiSpesaCdsSelezionatiColl().iterator(); i.hasNext(); )
			{
				IVoceBilancioBulk voce = (IVoceBilancioBulk) i.next();
				if (voce instanceof Voce_fBulk) {
					for ( Iterator j = cdrDiScrivaniaColl.iterator(); j.hasNext(); )
						if (((CdrBulk)j.next()).getCd_centro_responsabilita().equals( ((Voce_fBulk)voce).getCd_centro_responsabilita()))
							capitoli.add( voce );
				} else
					capitoli.add( voce );
			}
			lineeAttivita.addAll( obbligazioneHome.findLineeAttivitaSAC( cdrDiScrivaniaColl, capitoli, obbligazione ));
			if (obbligazione.isSpesePerCostiAltrui() )
				lineeAttivita.addAll( obbligazioneHome.findLineeAttivitaPerSpesePerCostiAltruiSAC( (List)obbligazione.getCapitoliDiSpesaCdsSelezionatiColl(),(List) obbligazione.getCdrSelezionatiColl(), obbligazione));			
		}	

		if ( obbligazione.isSpesePerCostiAltrui() )
			lineeAttivita = accorpaLineeAttivita( aUC, lineeAttivita );
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
  *			E' stato richiesto l'inserimento di un lock sulla scadenza di un'obbligazione
  *    PostCondition:
  *  		Il record relativo alla scadenza e' stato messo in lock e non e' pertanto consentito ad altre transazioni
  *         l'accesso a tale scadenza
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param scadenza <code>Obbligazione_scadenzarioBulk</code> da mettere in lock
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
  *      Una richiesta di modifica di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione non e' stata modificata in un contesto transazionale  
  *    PostCondition:
  *      L'obbligazione viene aggiornata
  *		 I dettagli di tutte le scadenze vengono aggiornati (metodo generaDettagliScadenzaObbligazione) 
  *      I saldi dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *      Lo stato COFI/COGE degli eventuali doc. amministrativi associati all'obbligazione e' stato aggiornato
  *  Tutti i controlli superati - contesto transazionale
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata
  *      L'obbligazione ha superato i controlli eseguiti dal metodo 'verificaObbligazione' 
  *      L'obbligazione ha superato i controlli sulla disponibilità di cassa delle voci del piano eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *      L'obbligazione e' stata modificata in un contesto transazionale  
  *    PostCondition:
  *      L'obbligazione viene aggiornata e i dettagli di tutte le scadenze vengono aggiornati (metodo generaDettagliScadenzaObbligazione) 
  *  Errore di verifica obbligazione
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli eseguiti dal metodo 'verificaObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'
  *    PostCondition:
  *      Viene generata un'ApplicationException che descrive all'utente l'errore che si e' verificato
  *  Errore di disponibilità di cassa - forzatura
  *    PreCondition:
  *      Una richiesta di modifica di un'obbligazione e' stata generata e l'obbligazione non ha superato i
  *      controlli di disponibilità di cassa eseguiti dal metodo 'aggiornaCapitoloSaldoObbligazione'  
  *		 e l'utente ha scelto di forzare l'emissione dell'obbligazione
  *    PostCondition:
  *      L'obbligazione viene modificata, i dettagli di tutte le scadenze vengono modificati (metodo generaDettagliScadenzaObbligazione) e i saldi 
  *      dei capitoli dei dettagli delle scadenze vengono aggiornati (metodo aggiornaCapitoloSaldoObbligazione)
  *
  * @param aUC lo user context 
  * @param bulk l'istanza di  <code>ObbligazioneBulk</code> da modificare
  * @return l'istanza di  <code>ObbligazioneBulk</code> modificata
  *  
 */

public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try
	{
		Obbligazione_scadenzarioBulk scadenza;
		ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;

		validaCampi(aUC, obbligazione);

		/* simona 23.10.2002 : invertito l'ordine della verifica e della generzione dettagli x problema 344 */		
			//genera eventualmente i dettagli che non erano ancora stati generati
		generaDettagliScadenzaObbligazione( aUC, obbligazione, null );

		//verifica la correttezza dell'obbligazione
		verificaObbligazione( aUC, obbligazione );

		//verifica la correttezza dell'imputazione finanziaria
		validaImputazioneFinanziaria( aUC, obbligazione );

		validaObbligazionePluriennale(aUC, obbligazione);
		//aggiorna il db:
		updateBulk( aUC, obbligazione);
		makeBulkListPersistent( aUC, obbligazione.getObbligazione_scadenzarioColl());

		makeBulkListPersistent( aUC, obbligazione.getObbligazioniPluriennali());

		//esegue il check di disponibilita di cassa
		controllaDisponibilitaCassaPerVoce( aUC, obbligazione, MODIFICA );

		verificaCoperturaContratto( aUC, obbligazione );
		verificaCoerenzaGaeContratto(aUC, obbligazione);
		verificaCoperturaIncaricoRepertorio(aUC, obbligazione);
		
		if ( !aUC.isTransactional() )
		{
			//aggiorna il capitolo saldo
			aggiornaCapitoloSaldoObbligazione( aUC, obbligazione, MODIFICA );
			aggiornaStatoCOAN_COGEDocAmm( aUC, obbligazione );
		}	
		// Se utente non è supervisore e la voce va azzerata non è possibile aumentare l'importo dell'impegno residuo
		if (obbligazione.isObbligazioneResiduo()) 
			if(verificaVoceResidua(aUC, obbligazione))		
				if(obbligazione.getIm_iniziale_obbligazione().compareTo(obbligazione.getIm_obbligazione()) <0)
					throw  new ApplicationException( "Aggiornamento non consentito! L'impegno residuo non può essere aumentato" );
	     
		obbligazione.setIm_iniziale_obbligazione( obbligazione.getIm_obbligazione());
		obbligazione.setCd_iniziale_elemento_voce( obbligazione.getCd_elemento_voce());	

		if (obbligazione.isObbligazioneResiduo()) {
			if (((ObbligazioneResBulk)obbligazione).isSaldiDaAggiornare()) {
				// aggiorniamo i saldi legati alle modifiche agli impegni residui
				aggiornaSaldiImpegniResiduiPropri(aUC,obbligazione);
				// aggiorniamo il progressivo in definitivo
				Obbligazione_modificaBulk obbMod = ((ObbligazioneResBulk) obbligazione).getObbligazione_modifica();
				if (obbMod!=null && obbMod.isTemporaneo()) {
					aggiornaObbligazioneModificaTemporanea(aUC, obbMod);
				}
			}
		}
		
		verificaStatoEsercizio( 
							aUC, 
							((CNRUserContext)aUC).getEsercizio(), 
							obbligazione.getCd_cds());
   
		validaCreaModificaOrigineFonti(aUC, obbligazione);
		
		obbligazione = validaCreaModificaElementoVoceNext(aUC, obbligazione);
		
		return obbligazione;
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
}
/*
 * Modifica l'importo di una scadenza e aggiunge la differenza alla scadenza successiva oppure modifica l'importo di una
 * scadenza e l'importo della testata dell'obbligazione
 *	
 * Pre-post-conditions:
 *
 * Nome: Modifica Scadenza 
 * Pre:  E' stata generata la richiesta di modifica l'importo di una scadenza 
 * Post: L'importo della scadenza e della testata dell'obbligazione sono stati modificati
 *
 * Nome: Modifica Scadenza successiva
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e la differenza fra il nuovo importo
 *       e l'importo precedente deve essere riportato sulla scadenza successiva
 * Post: L'importo della scadenza e della scadenza successiva sono stati modificati
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
 * Nome: Errore imputazione manuale
 * Pre:  E' stata generata la richiesta di modifica dell'importo di una scadenza e l'imputazione finanziaria
 *       dell'obbligazione non e' automatica
 * Post: Viene generata un'ApplicationException per segnalare l'impossibilità di aggiornamento della scadenza
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param scad l'istanza di Obbligazione_scadenzarioBulk il cui importo deve essere modificato
 * @param nuovoImporto il valore del nuovo importo che la scadenza di obbligazione dovrà assumere
 * @param modificaScadenzaSuccessiva il flag che indica se modificare la testata dell'obbligazione o modificare la scadenza
 *        successiva dell'obbligazione
 * @return l'istanza di Obbligazione_scadenzarioBulk con l'importo modificato
 */

public IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico( UserContext userContext,	IScadenzaDocumentoContabileBulk scad,	BigDecimal nuovoImporto, boolean modificaScadenzaSuccessiva ) throws ComponentException 
{
	Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)scad;
	if (scadenza.getObbligazione().isObbligazioneResiduo())
		throw handleException( new ApplicationException( "Aggiornamento in automatico non consentito! L'impegno residuo non è modificabile" ));
	if ( !scadenza.getObbligazione().getFl_calcolo_automatico().booleanValue() )
		throw handleException( new ApplicationException( "Aggiornamento in automatico non consentito! L'impegno consente solo l'imputazione manuale" ));
	if ( scadenza.getIm_scadenza().compareTo( nuovoImporto ) == 0 )
		throw handleException( new ApplicationException( "Aggiornamento in automatico non necessario" ));
	if (  nuovoImporto.compareTo( new BigDecimal(0)) < 0  )
		throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));			
			
	if ( modificaScadenzaSuccessiva )
	{
		scadenza.setFl_aggiorna_scad_successiva( new Boolean( true) );

		//salvo i dati iniziali
		Obbligazione_scadenzarioBulk scadIniziale = new Obbligazione_scadenzarioBulk();
		scadIniziale.setIm_scadenza( scadenza.getIm_scadenza());
		scadenza.setScadenza_iniziale( scadIniziale );
		//aggiorno importo scadenza corrente
		scadenza.setIm_scadenza( nuovoImporto );
		scadenza.setToBeUpdated();
		//aggiorna l'importo della scadenza successiva ed i suoi dettagli
		Obbligazione_scadenzarioBulk scadSuccessiva = aggiornaScadenzaSuccessivaObbligazione( userContext,scadenza );
		scadSuccessiva.setToBeUpdated();

		//aggiorna i dettagli della scadenza corrente e della scadenza successiva
		calcolaPercentualeImputazioneObbligazione( userContext, scadenza.getObbligazione() );

		modificaConBulk( userContext, scadenza.getObbligazione());
		return scadenza;
	}
	else
	{
		//aggiorno importo testata
		ObbligazioneBulk obbligazione = scadenza.getObbligazione();
		obbligazione.setIm_obbligazione( obbligazione.getIm_obbligazione().add(nuovoImporto).subtract( scadenza.getIm_scadenza()) );
		obbligazione.setToBeUpdated();

		//aggiorno importo scadenza corrente
		scadenza.setIm_scadenza( nuovoImporto );
		scadenza.setToBeUpdated();		
		//aggiorna i dettagli della scadenza corrente 
		calcolaPercentualeImputazioneObbligazione( userContext, obbligazione );
		modificaConBulk( userContext, obbligazione );
		return scadenza;
	}
	

}
/*
 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su ObbligazioniBulk
 *	
 * Pre-post-conditions:
 *
 * Nome: Richiesta di ricerca di un'obbligazione
 * Pre:  E' stata generata la richiesta di ricerca di un'obbligazione
 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
 *       clausole che l'obbligazione sia stata originata dall'uo di scrivania e che il suo progressivo sia
 *       un numero positivo (in modo da escludere le obbligazioni temporaneamente create dalla gestione dei
 *       documenti amministrativi)
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param clauses clausole di ricerca gia' specificate dall'utente
 * @param bulk istanza di CdsBulk o Unita_organizzativaBulk che deve essere utilizzata per la ricerca
 * @return sql Query con le clausole aggiuntive 
 */

protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
	sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa()); 
	sql.addClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
	sql.addClause( "AND", "pg_obbligazione", sql.GREATER_EQUALS, new Long(0)); 	
	if ( bulk instanceof V_obbligazione_im_mandatoBulk  )
	{
		verificaStatoEsercizio( userContext, ((CNRUserContext)userContext).getEsercizio(), ((CNRUserContext)userContext).getCd_cds() );
		sql.addClause( "AND", "stato_obbligazione", sql.EQUALS, ((V_obbligazione_im_mandatoBulk) bulk).getStato_obbligazione());
	}	
	return sql;
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
 public SQLBuilder selectCdrForPrintByClause(UserContext userContext, Stampa_obbligazioni_LAVBulk stampa, it.cnr.contab.config00.sto.bulk.CdrBulk cdr, CompoundFindClause clauses ) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
 {



	 CdrBulk cdr_scrivania = cdrFromUserContext(userContext);
	 int livelloResponsabilita = getLivelloResponsabilitaCDR(userContext, cdr_scrivania);

	 it.cnr.contab.config00.sto.bulk.CdrHome home;
	 it.cnr.jada.persistency.sql.SQLBuilder sql;

	 if (livelloResponsabilita == Stampa_obbligazioni_LAVBulk.LV_AC) {
		 home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class,"V_CDR_VALIDO","none");
		 sql = home.createSQLBuilder();
	 } else {
		 home = (it.cnr.contab.config00.sto.bulk.CdrHome)getHome(userContext, CdrBulk.class, "V_PDG_CDR_FIGLI_PADRE","none");
		 sql = home.createSQLBuilder();
		 sql.addSQLClause("AND", "CD_CDR_ROOT", sql.EQUALS, cdr_scrivania.getCd_centro_responsabilita());
	 }
	 sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	 sql.addClause(clauses);

	 return sql;

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
public SQLBuilder selectLinea_attByClause(UserContext userContext, it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk bulk, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	return selectLinea_attByClause( userContext, context.getObbligazione(), clauses );
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
public SQLBuilder selectLinea_attByClause(UserContext userContext, ObbligazioneBulk obbligazione, CompoundFindClause clauses ) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{

	SQLBuilder sql = getHome(userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class, "V_LINEA_ATT_NOT_IN_PDG").createSQLBuilder();
	sql.setHeader( "SELECT DISTINCT " + sql.getColumnMap().getDefaultSelectHeaderSQL() );
	sql.addClause( clauses );
	sql.addSQLClause( "AND", "ESERCIZIO",  sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio() );
	sql.addSQLClause( "AND", "CD_CENTRO_RESPONSABILITA",  sql.LIKE, obbligazione.getCd_unita_organizzativa() + ".%" );	
	//condizioni sulla funzione 	
	Iterator i = obbligazione.getCapitoliDiSpesaCdsSelezionatiColl().iterator();
	if ( i.hasNext() )
	{
		sql.openParenthesis( "AND" );
		sql.addClause("AND", "cd_funzione", SQLBuilder.EQUALS, ((IVoceBilancioBulk)i.next()).getCd_funzione());
		while ( i.hasNext() )	
			sql.addClause( "OR", "cd_funzione", SQLBuilder.EQUALS, ((IVoceBilancioBulk)i.next()).getCd_funzione());
		sql.closeParenthesis();
	}

	sql.openParenthesis( "AND");
	sql.addSQLClause( "AND",  "cd_elemento_voce", sql.ISNULL, null );
	sql.openParenthesis( "OR");
	sql.addSQLClause( "AND", "cd_elemento_voce", sql.NOT_EQUALS, obbligazione.getCd_elemento_voce() );
	
	String condizione = "not exists ( select 1 from " +
						  it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 					
						 "pdg_preventivo_spe_det " +
						 "where pdg_preventivo_spe_det.categoria_dettaglio = '" + Pdg_preventivo_spe_detBulk.CAT_SINGOLO +"' " +
						 "and pdg_preventivo_spe_det.stato = '" + Pdg_preventivo_spe_detBulk.ST_CONFERMA +"' " +
						 "and pdg_preventivo_spe_det.ti_appartenenza = '" + Elemento_voceHome.APPARTENENZA_CDS + "' " +					 					 					 					 
						 "and pdg_preventivo_spe_det.ti_gestione = '" + Elemento_voceHome.GESTIONE_SPESE +"' " +					 					 					 					 
						 "and pdg_preventivo_spe_det.esercizio = V_LINEA_ATT_NOT_IN_PDG.ESERCIZIO " +
						 "and pdg_preventivo_spe_det.cd_centro_responsabilita = V_LINEA_ATT_NOT_IN_PDG.cd_centro_responsabilita " +
						 "and pdg_preventivo_spe_det.cd_linea_attivita = V_LINEA_ATT_NOT_IN_PDG.cd_linea_attivita "  +
						 "and pdg_preventivo_spe_det.cd_elemento_voce = '" + obbligazione.getCd_elemento_voce() + "' ";

	if (obbligazione.isObbligazioneResiduo() || obbligazione.isObbligazioneResiduoImproprio())
		condizione = condizione + 
					"union " +
					"select 1 from " +
					 it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 					
					"voce_f_saldi_cdr_linea " +
					"where voce_f_saldi_cdr_linea.esercizio > voce_f_saldi_cdr_linea.esercizio_res " +
					"and voce_f_saldi_cdr_linea.esercizio_res = " + obbligazione.getEsercizio_originale() + 
					"and voce_f_saldi_cdr_linea.ti_appartenenza = '" + Elemento_voceHome.APPARTENENZA_CDS + "' " +					 					 					 					 
					"and voce_f_saldi_cdr_linea.ti_gestione = '" + Elemento_voceHome.GESTIONE_SPESE +"' " +					 					 					 					 
					"and voce_f_saldi_cdr_linea.esercizio = V_LINEA_ATT_NOT_IN_PDG.ESERCIZIO " +
					"and voce_f_saldi_cdr_linea.cd_centro_responsabilita = V_LINEA_ATT_NOT_IN_PDG.cd_centro_responsabilita " +
					"and voce_f_saldi_cdr_linea.cd_linea_attivita = V_LINEA_ATT_NOT_IN_PDG.cd_linea_attivita "  +
					"and voce_f_saldi_cdr_linea.cd_elemento_voce = '" + obbligazione.getCd_elemento_voce() + "' ";
	else 
		condizione = condizione + 
					"union " +
					"select 1 from " +
					 it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 					
					"pdg_modulo_spese_gest " +
					"where pdg_modulo_spese_gest.categoria_dettaglio = '" + Pdg_modulo_spese_gestBulk.CAT_DIRETTA +"' " +
					"and pdg_modulo_spese_gest.ti_appartenenza = '" + Elemento_voceHome.APPARTENENZA_CDS + "' " +					 					 					 					 
					"and pdg_modulo_spese_gest.ti_gestione = '" + Elemento_voceHome.GESTIONE_SPESE +"' " +					 					 					 					 
					"and pdg_modulo_spese_gest.esercizio = V_LINEA_ATT_NOT_IN_PDG.ESERCIZIO " +
					"and pdg_modulo_spese_gest.cd_centro_responsabilita = V_LINEA_ATT_NOT_IN_PDG.cd_centro_responsabilita " +
					"and pdg_modulo_spese_gest.cd_linea_attivita = V_LINEA_ATT_NOT_IN_PDG.cd_linea_attivita "  +
					"and pdg_modulo_spese_gest.cd_elemento_voce = '" + obbligazione.getCd_elemento_voce() + "' "; 
	
	condizione = condizione + ") ";

	sql.addSQLClause( "AND", condizione);
	sql.closeParenthesis();
	sql.closeParenthesis();
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
		sql.addSQLClause( "AND", "CD_LINEA_ATTIVITA",  sql.NOT_EQUALS, config.getVal01());
	}
	/**
	 * Se è attivo il nuovo regolamento 2006 e non è un residuo proprio
	 * Vedo solo le Linee sfondabili
	 */
    try {
		if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,CNRUserContext.getEsercizio(userContext)).getFl_regolamento_2006().booleanValue() && !obbligazione.isObbligazioneResiduo()){
			sql.addSQLClause( "AND", "FL_LIMITE_ASS_OBBLIG",  sql.EQUALS, "N");
		}
	}catch (RemoteException e) {
		throw new ComponentException(e);
	} catch (EJBException e) {
		throw new ComponentException(e);
	}
	return sql;
}
/**
 * 
 * @param userContext
 * @param obbligazione
 * @param contratto
 * @param clauses
 * @return
 * @throws ComponentException
 * @throws it.cnr.jada.persistency.PersistencyException
 */ 
public SQLBuilder selectContrattoByClause(UserContext userContext, ObbligazioneBulk obbligazione, ContrattoBulk contratto, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	Parametri_cdsHome paramHome = (Parametri_cdsHome)getHome(userContext, Parametri_cdsBulk.class);
	Parametri_cdsBulk param_cds;
	try {
		param_cds =
			(Parametri_cdsBulk) paramHome.findByPrimaryKey(
				new Parametri_cdsBulk(
		           obbligazione.getCd_cds(),
		           obbligazione.getEsercizio()));
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	
	SQLBuilder sql = getHome(userContext,ContrattoBulk.class).createSQLBuilder();
	
	if (clauses != null) 
	  sql.addClause(clauses);
	sql.openParenthesis("AND");  
	  sql.addSQLClause("AND","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_PASSIVO);
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
		
	if((obbligazione.getCreditore() != null && obbligazione.getCreditore().getCd_terzo()!=null)){
		sql.openParenthesis("AND");
	    sql.openParenthesis("AND");
	    sql.addSQLClause(FindClause.AND, "FIG_GIUR_EST",SQLBuilder.EQUALS,obbligazione.getCreditore().getCd_terzo());
		AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
		sql.closeParenthesis();
		try {
			for (Iterator<Anagrafico_terzoBulk> i = anagraficoHome.findAssociatiStudio(obbligazione.getCreditore().getAnagrafico()).iterator(); i.hasNext();) {
				sql.openParenthesis("OR");
					Anagrafico_terzoBulk associato = i.next();
					sql.addSQLClause("OR", "CONTRATTO.FIG_GIUR_EST",SQLBuilder.EQUALS, associato.getCd_terzo());
				sql.closeParenthesis();
			}
		} catch (IntrospectionException e) { 
		}
	  sql.closeParenthesis();
	}
	/*
    sql.openParenthesis("AND");	   
	  sql.addSQLClause("AND","TRUNC(NVL(DT_FINE_VALIDITA,SYSDATE)) >= TRUNC(SYSDATE)");
	  sql.addSQLClause("OR","(DT_PROROGA IS NOT NULL AND TRUNC(DT_PROROGA) >= TRUNC(SYSDATE))");
	sql.closeParenthesis();
	*/  
	return sql;
}
/**
 * 
 * @param userContext
 * @param obbligazione
 * @param contratto
 * @param clauses
 * @return
 * @throws ComponentException
 * @throws it.cnr.jada.persistency.PersistencyException
 */
public void validaContratto(UserContext userContext, ObbligazioneBulk obbligazione, ContrattoBulk contratto, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = selectContrattoByClause(userContext, obbligazione, contratto, clauses);
	sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS, contratto.getEsercizio());
	sql.addSQLClause("AND","STATO",SQLBuilder.EQUALS, contratto.getStato());
	sql.addSQLClause("AND","PG_CONTRATTO",SQLBuilder.EQUALS, contratto.getPg_contratto());
	ContrattoHome home = (ContrattoHome)getHome(userContext,ContrattoBulk.class);
	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
	if(!broker.next())
	  throw new ApplicationException("Contratto non valido!");
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
public SQLBuilder selectLineaAttForPrintByClause(UserContext userContext, 
	Stampa_obbligazioni_LAVBulk stampa, 
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_att, 
	CompoundFindClause clauses ) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	
	SQLBuilder sql = getHome(userContext,it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
	if (clauses != null) sql.addClause(clauses);

	sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, stampa.getCdrForPrint().getCd_centro_responsabilita());

	sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

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
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_obbligazioni_riportabiliVBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

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
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_registro_annotazione_spese_pgiroBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	//try{
		//Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
		//SQLBuilder sql = home.createSQLBuilder();

		//CDRComponentSession sess = (CDRComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", CDRComponentSession.class);
		//if (sess.isEnte(userContext)){
			//if(stampa.getCdCdsOrigineForPrint()==null)
				//throw new ApplicationException("Inserire il CDS di ORIGINE");
			//sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCdCdsOrigineForPrint());
		//}else
			//sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
		//sql.addClause(clauses);
		//return sql;

	//}catch(javax.ejb.EJBException ex){
		//throw handleException(ex);
	//}catch(java.rmi.RemoteException ex){
		//throw handleException(ex);
	//}

	
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
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_registro_obbligazioniBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

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
public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_scadenzario_obbligazioniBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());
	sql.addClause(clauses);
	return sql;
}


public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_obb_doc_ammBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException, PersistencyException {


		SQLBuilder sql = ((Unita_organizzativaHome)getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilder();
	    sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
		sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCdCdsForPrint());
		sql.addClause(clauses);
		return sql;
}

public SQLBuilder selectCdsForPrintByClause (UserContext userContext, Stampa_obb_doc_ammBulk stampa, it.cnr.contab.config00.sto.bulk.CdsBulk cds, CompoundFindClause clause) throws ComponentException, PersistencyException
{	
	SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
	sql.addClause( clause );
	
	String cd_cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);			
	it.cnr.contab.config00.sto.bulk.CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
	it.cnr.contab.config00.sto.bulk.CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(cd_cds_scrivania));

	if (cds_scrivania.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)){
	sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
	}
	else{
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getCd_cds());
		sql.addClause("AND","FL_CDS",sql.EQUALS, new Boolean(true) );
	}
	return sql;
}	
public SQLBuilder selectElementoVoceForPrintByClause(UserContext userContext, Stampa_obb_doc_ammBulk stampa, Elemento_voceBulk elementoVoce, CompoundFindClause clauses) throws ComponentException {

	Elemento_voceHome home = (Elemento_voceHome)getHome(userContext, elementoVoce);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND","TI_GESTIONE", sql.EQUALS, home.GESTIONE_SPESE);
	sql.addClause( clauses );
	return sql;
}

public SQLBuilder selectEsercizioDocForPrintByClause(UserContext userContext, Stampa_obb_doc_ammBulk stampa, Esercizio_baseBulk es, CompoundFindClause clauses) throws ComponentException, PersistencyException {

	Esercizio_baseHome home = (Esercizio_baseHome)getHome(userContext,es);
	SQLBuilder sql = home.createSQLBuilder();
    sql.addTableToHeader("PRT_OBB_DOC_AMM");
	sql.addSQLJoin("PRT_OBB_DOC_AMM.ESERCIZIO_DOC_AMM","ESERCIZIO_BASE.ESERCIZIO");
	sql.addSQLClause("AND", "PRT_OBB_DOC_AMM.CD_CDS_OBBLIG", sql.EQUALS, stampa.getCdCdsForPrint());
	sql.addSQLClause("AND", "PRT_OBB_DOC_AMM.CD_ELEMENTO_VOCE", sql.EQUALS, stampa.getCdElementoVoceForPrint());
	sql.addSQLClause("AND", "PRT_OBB_DOC_AMM.ESERCIZIO_OBBLIG", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.setDistinctClause(true);
	sql.addClause(clauses);
	return sql;
}

/**
 * stampaConBulk method comment.
 */
public OggettoBulk stampaConBulk(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	if (bulk instanceof Stampa_registro_obbligazioniBulk)
		validateBulkForPrint(aUC, (Stampa_registro_obbligazioniBulk)bulk);
	else if (bulk instanceof Stampa_registro_annotazione_spese_pgiroBulk)
		validateBulkForPrint(aUC, (Stampa_registro_annotazione_spese_pgiroBulk)bulk);
	else if (bulk instanceof Stampa_scadenzario_obbligazioniBulk)
		validateBulkForPrint(aUC, (Stampa_scadenzario_obbligazioniBulk)bulk);
	else if (bulk instanceof Stampa_obbligazioni_riportabiliVBulk)
		validateBulkForPrint(aUC, (Stampa_obbligazioni_riportabiliVBulk)bulk);
	else if (bulk instanceof Stampa_obbligazioni_LAVBulk)
		validateBulkForPrint(aUC, (Stampa_obbligazioni_LAVBulk)bulk);
	else if (bulk instanceof Stampa_obb_doc_ammBulk)
		stampaConBulk(aUC, (Stampa_obb_doc_ammBulk)bulk);

	return bulk;
}



public OggettoBulk stampaConBulk(UserContext userContext, Stampa_obb_doc_ammBulk stampa) throws ComponentException {
	if ( stampa.getCdsForPrint()==null || stampa.getCdsForPrint().getCd_proprio_unita()==null)
			throw new ApplicationException( "E' necessario selezionare il CDS");
	if ( stampa.getElementoVoceForPrint()==null || stampa.getElementoVoceForPrint().getCd_elemento_voce()==null)
			throw new ApplicationException( "E' necessario selezionare la Voce");
	return stampa;
}	

/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Non esistono documenti amministrativi per l'obbligazione.
  *      Non esitono ordini associati all'obbligazione
  *      Lo stato dell'obbligazione è 'DEFINITIVA'
  *    PostCondition:
  *      Il sistema eseguirà le seguente attività:
  *      1) L'aggiornamento dei saldi 'obbligazioni' dei capitoli di spesa CdS 
  *         (Questo processo viene eseguito dal metodo 'aggiornaCapitoloSaldoObbligazione').
  *      2) L'azzeramento dell'importo di ogni dettaglio di ogni scadenza dell'obbligazione,  
  *      3) L'azzeramento dell'importo di ogni scadenza dell'obbligazione,
  *      4) L'azzeramento dell'importo dell'obbligazione propria,
  *      5) L'aggiornamento dello stato dell'obbligazione a 'STORNATA'.
  *      
  *  Esistono documenti amministrativi per l'obbligazione
  *    PreCondition:
  *      Per l'obbligazione definitiva ci sono documenti amministrativi già collegati all'obbligazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che per stornare una obbligazione definitiva, 
  *      qualsiasi documento amministrativo collegato all'obbligazione deve essere sganciato prima di eseguire 
  *      lo storno. L'attività non è consentita.
  *
  *  Esiste un ordine per l'obbligazione
  *    PreCondition:
  *      Per l'obbligazione definitiva e' stato definito un ordine
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che per stornare una obbligazione definitiva 
  *      per la quale e' già stato emesso un ordine e' necessario prima cancellare l'ordine
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> da annullare
  * @return l'istanza di  <code>ObbligazioneBulk</code> annullata
  *  
 */
public ObbligazioneBulk stornaObbligazioneDefinitiva(
    UserContext aUC,
    ObbligazioneBulk obbligazione)
    throws ComponentException {
    try {
        // Controllo se esiste un ordine associato all'obbligazione
        // In caso affermativo blocco l'operazione di eliminazione
        it.cnr.contab.doccont00.ordine.bulk.OrdineBulk ordine =
            findOrdineFor(aUC, obbligazione);
        if (ordine != null)
            throw new ApplicationException("L'impegno selezionato è collegato ad un ordine. Cancellare prima l'ordine");

        if (obbligazione.isAssociataADocAmm())
            throw new ApplicationException("Impossibile stornare impegni con documenti amministrativi associati");

        //test per obbligazioni collegate a spese del fondo economale
        for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	        if ( ((Obbligazione_scadenzarioBulk)i.next()).getIm_associato_doc_amm().compareTo( new BigDecimal(0)) > 0 )
            throw new ApplicationException("Impossibile stornare impegni collegati a spese del fondo economale o a documenti amministrativi");
            
        obbligazione.storna();
        obbligazione.setDt_cancellazione( DateServices.getDt_valida(aUC));


/*        //e' necessario aggiornare prima i dettagli e poi la testata per consentire lo storico delle modifiche
        makeBulkListPersistent(aUC, obbligazione.getObbligazione_scadenzarioColl());

        obbligazione.setUser(aUC.getUser());
        updateBulk(aUC, obbligazione);


 */
      makeBulkPersistent( aUC, obbligazione);
      /*
	  if ( !aUC.isTransactional() )	
		 aggiornaStatoCOAN_COGEDocAmm( aUC, obbligazione );
		*/
        aggiornaCapitoloSaldoObbligazione(aUC, obbligazione, MODIFICA);		
        return obbligazione;
    } catch (Exception e) {
        throw handleException(e);
    }

	}
	/**
	 * Validazione dell'oggetto in fase di stampa
	 *
	*/
	private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_obbligazioni_LAVBulk stampa) throws ComponentException{

		try{
			Timestamp dataOdierna = getDataOdierna(userContext);
			Timestamp lastDayOfYear = DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());


			if (stampa.getDataInizio()==null)
				throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
			if (stampa.getDataFine()==null)
				throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");

			java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
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

	

		
			if (stampa.getEsercizio()==null)
				throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
			if (stampa.getCd_cds()==null)
				throw new ValidationException("Il campo CDS e' obbligatorio");


		} catch(ValidationException ex) {
			throw new it.cnr.jada.comp.ApplicationException(ex);
		}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_obbligazioni_riportabiliVBulk stampa) throws ComponentException{

	try{
		Timestamp lastDay = DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());
	
		if (stampa.getEsercizio()==null)
			throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		

	}catch(ValidationException ex){
		throw new ApplicationException(ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_registro_annotazione_spese_pgiroBulk stampa) throws ComponentException{

	try{
		Timestamp dataOdierna = getDataOdierna(userContext);
		Timestamp lastDayOfYear = DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());
		if (stampa.getEsercizio()==null)
			throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		//if (stampa.getCd_cds()==null)
			//throw new ValidationException("Il campo CDS e' obbligatorio");

		//try{
			//CDRComponentSession sess = (CDRComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_CDRComponentSession", CDRComponentSession.class);
			//if (sess.isEnte(userContext) && stampa.getCdCdsOrigineForPrint()==null)
				//throw new ValidationException("Il campo CDS di ORIGINE è obbligatorio");
		//}catch(javax.ejb.EJBException ex){
			//throw handleException(ex);
		//}catch(java.rmi.RemoteException ex){
			//throw handleException(ex);
		//}

		//if (!stampa.isCdsUOInScrivania() && stampa.getCdUoForPrint() == null)
			//throw new ValidationException("Il campo UNITA ORGANIZZATIVA è obbligatorio");
			
		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");

		java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
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
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_registro_obbligazioniBulk stampa) throws ComponentException{

	try{
		Timestamp dataOdierna = getDataOdierna(userContext);
		Timestamp lastDayOfYear = DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());
		if (stampa.getEsercizio()==null)
			throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		if (stampa.getCd_cds()==null)
			throw new ValidationException("Il campo CDS e' obbligatorio");

		if (!stampa.isCdsUOInScrivania() && stampa.getCdUoForPrint() == null)
			throw new ValidationException("Il campo UNITA ORGANIZZATIVA è obbligatorio");
//			if (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC.equalsIgnoreCase(uo.getCd_tipo_unita())){

		if (stampa.getDataInizio()==null)
			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
		if (stampa.getDataFine()==null)
			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");

		java.sql.Timestamp firstDayOfYear = DateServices.getFirstDayOfYear(stampa.getEsercizio().intValue());
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
			throw new ValidationException("Il campo NUMERO INIZIO è obbligatorio");
		if (stampa.getPgFine()==null)
			throw new ValidationException("Il campo NUMERO FINE è obbligatorio");
		if (stampa.getPgInizio().compareTo(stampa.getPgFine())>0)
			throw new ValidationException("Il NUMERO INIZIO non può essere superiore al NUMERO FINE");

	} catch(ValidationException ex) {
		throw new ApplicationException(ex);
	}
}
/**
 * Validazione dell'oggetto in fase di stampa
 *
*/
private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_scadenzario_obbligazioniBulk stampa) throws ComponentException{

	try{
		Timestamp lastDay = DateServices.getLastDayOfYear(CNRUserContext.getEsercizio(userContext).intValue());
	
		if (stampa.getEsercizio()==null)
			throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");
		if (stampa.getCd_cds()==null)
			throw new ValidationException("Il campo CDS e' obbligatorio");

		if (!stampa.isCdsUOInScrivania() && stampa.getCdUoForPrint() == null)
			throw new ValidationException("Il campo UNITA ORGANIZZATIVA è obbligatorio");

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
  *  Obbligazione spese per costi altrui
  *    PreCondition:
  *  	 Obbligazione con flag spese per costi altrui selezionato
  *    PostCondition:
  *      Nessun controllo viene effettuato e l'obbligazione supera questa validazione
  *
  *  Obbligazione non spese per costi altrui - ok
  *    PreCondition:
  *  	 Obbligazione con flag spese per costi altrui non selezionato e con linee di attività, specificate nell'imputazione
  *      finanziaria, tutte appartenenti a cdr che dipendono dall'unità organizzativa di scrivania
  *    PostCondition:
  *      L'obbligazione supera questa validazione
  *
  *  Obbligazione non spese per costi altrui - errore
  *    PreCondition:
  *  	 Obbligazione con flag spese per costi altrui non selezionato e con almeno una linea di attività, specificata nell'imputazione
  *      finanziaria, che appartene ad un cdr che non dipende dall'unità organizzativa di scrivania
  *    PostCondition:
  *      Una segnalazione di errore viene restituita all'utente per informarlo dell'impossibilità di effettuare la creazione/modifica
  *      dell'obbligazione.
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> da verificare
  *
  */ 
  
private void verificaFl_spese_costi_altrui (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	try
	{
		if ( !obbligazione.getFl_spese_costi_altrui().booleanValue() )
		{
			V_pdg_obbligazione_speBulk latt;
			for (Iterator i = obbligazione.getLineeAttivitaSelezionateColl().iterator(); i.hasNext(); )
			{
				latt = (V_pdg_obbligazione_speBulk) i.next();
				if ( !latt.getCd_centro_responsabilita().startsWith( obbligazione.getCd_unita_organizzativa()))
					throw new ApplicationException( "Sono state selezionate Workpckages di CdR che non appartengono all'Unità Organizzativa! E' necessario modificare l'imputazione finanziaria");
			}		

		}	
			
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
	
}
/** 
  *  Linea attività in Pdg
  *    PreCondition:
  *      L'utente ha selezionato una nuova linea di attività e la nuova linea di attività e' nel Piano di Gestione
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente l'impossibilità di assegnare come nuova linea di attività
  *      una presente nel P.d.G.
  *  Linea attività in Pdg
  *    PreCondition:
  *      L'utente ha selezionato una nuova linea di attività e la nuova linea di attività non e' presente nel Piano di Gestione
  *    PostCondition:
  *      La nuova linea di attività ha superato la validazione
  *
  * @param userContext lo user context 
  * @param latt l'istanza di  <code>Linea_attivitaBulk</code> da verificare
  *  
 */
public void verificaNuovaLineaAttivita (UserContext userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt) throws ComponentException
{
	/*
	try
	
	{
		SQLBuilder sql = getHome(userContext, latt.getClass(), "V_LINEA_ATT_NOT_IN_PDG").createSQLBuilder();
		sql.addClause( "AND", "cd_centro_responsabilita", sql.EQUALS, latt.getCd_centro_responsabilita());
		sql.addClause( "AND", "cd_linea_attivita", sql.EQUALS, latt.getCd_linea_attivita());
		List result = getHome(userContext, latt.getClass(), "V_LINEA_ATT_NOT_IN_PDG").fetchAll( sql );
		if ( result == null  || result.size() == 0)
			throw new ApplicationException( "Il GAE " + 
										 latt.getCd_centro_responsabilita() + "-" + 
										 latt.getCd_linea_attivita() +
										 "e' presente nel piano di gestione");

	}
	catch ( it.cnr.jada.persistency.PersistencyException e )
	{
		throw handleException(e);
	}
	*/

	
}
/** 
  *  Tutti controlli superati - anno di creazione obbligazione < anno competenza dell'obbligazione
  *    PreCondition:
  *      testata dell'obbligazione verificata (controllato nel metodo verificaTestataObbligazione).
  *      sum(scadenzario.importo) = obbligazione.importo
  *      sum(scad_voce.importo)  = scadenzario.importo
  *		 dettagli d'imputazione finanziaria specificati
  *      almeno una scadenza definita
  *      verfiche per spese per costi altrui superate (metodo verificaFl_spese_costi_altrui)
  *      L'anno di competenza dell'obbligazione è superiore all'anno di creazione dell'obbligazione
  *    PostCondition:
  *      Il sistema può proseguire con la creazione/modifica dell'obbligazione, ma non verranno aggiornati i saldi
  *      dei capitoli di spesa CdS.
  *
  *  Tutti controlli superati - anno di creazione obbligazione = anno competenza
  *    PreCondition:
  *      testata dell'obbligazione verificata (controllato nel metodo verificaTestataObbligazione).
  *      sum(scadenzario.import) = obbligazione.import.
  *      sum(scad_voce.importo)  = scadenzario.importo
  *		 dettagli d'imputazione finanziaria specificati
  *      almeno una scadenza definita
  *      verfiche per spese per costi altrui superate (metodo verificaFl_spese_costi_altrui)  
  *      L'anno di competenza dell'obbligazione è uguale all'anno di creazione dell'obbligazione
  *    PostCondition:
  *      Il sistema può proseguire con la creazione/modifica dell'obbligazione e dovrà effettuare l'aggiornamento
  *      dei saldi dei capitoli di spesa CdS. (Questo processo viene eseguito dal metodo 'aggiornaCapitoloSaldoObbligazione').
  *
  *  sum(scadenzario.importo) not = obbligazione.importo
  *    PreCondition:
  *      La somma degli importi delle scadenze dell'obbligazione non è uguale all'importo dell'obbligazione in elaborazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito 
  *      se l'importo non è uguale alla somma degli importi delle scadenze dell'obbligazione.
  *
  *  sum(scad_voce.importo) not = scadenzario.importo
  *    PreCondition:
  *      L'utente ha selezionato l'imputazione manuale degli importi dei dettagli delle scadenze e la somma degli importi 
  *      dei dettagli di una scadenza dell'obbligazione non è uguale all'importo della scadenza
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito 
  *      se l'importo della scadenza non è uguale alla somma degli importi dei dettagli della scadenza dell'obbligazione.
  *
  *  dettagli d'imputazione finanziaria non specificati al livello di obbligazione
  *    PreCondition:
  *      I dettagli d'imputazione finanziaria (capitolo di spesa, linea d'attività) non sono stati specificati 
  *      al livello di obbligazione 
  *    PostCondition:
  *      Il sistema segnala l'impossibilità di craere/aggiornare l'obbligazione fino a quando l'imputazione finanziaria non viene completata
  * 
  *  scadenze non definite
  *    PreCondition:
  *      Non sono state definite scadenze per l'obbligazione
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito 
  *      se non viene definita almento una scadenza
  *
  *  spese per costi altrui
  *    PreCondition:
  *      L'utente ha specificato di voler emettere un'obbligazione non di tipo spese per costi altrui
  *      ma ha selezionato linee di attività appartenenti a cdr che non sipendono dall'uo di scrivania
  *      (questo controllo viene effettuato dal metodo 'verificaFl_spese_costi_altrui')
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che il salvataggio dell'obbligazione non è consentito
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> da verificare
  *  
  *
 */
public void verificaObbligazione (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	Obbligazione_scadenzarioBulk os;
	Obbligazione_scad_voceBulk   osv;	
	BigDecimal totalObbligazione = new BigDecimal(0);
	BigDecimal totalScadenza;
	verificaTestataObbligazione( aUC, obbligazione );

	// non e' stata fatta l'imputazione finanziaria
	if ( obbligazione.getLineeAttivitaSelezionateColl().size() == 0  &&
		 obbligazione.getNuoveLineeAttivitaColl().size() == 0 )
			throw handleException( new it.cnr.jada.comp.ApplicationException( "E' necessario effettuare l'imputazione finanziaria"))	;	
	// non ci sono scadenze
	if ( obbligazione.getObbligazione_scadenzarioColl().isEmpty() )
		throw handleException( new it.cnr.jada.comp.ApplicationException( "E' necessario creare almeno una scadenza"))	;		
	//  La somma degli importi delle scadenze dell'obbligazione non è uguale all'importo dell'obbligazione in elaborazione.

	
	for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	{
		os = (Obbligazione_scadenzarioBulk) i.next();
		totalObbligazione = totalObbligazione.add( os.getIm_scadenza());
		totalScadenza = new BigDecimal(0);
		for (Iterator j = os.getObbligazione_scad_voceColl().iterator(); j.hasNext(); )
		{
			osv = (Obbligazione_scad_voceBulk) j.next();
			if ( osv.getIm_voce() == null )
				throw handleException( new it.cnr.jada.comp.ApplicationException( "E' necessario inserire l'importo di un dettaglio per la scadenza del " + String.valueOf( os.getDt_scadenza()).substring(0,10))	);					
			totalScadenza = totalScadenza.add( osv.getIm_voce() );
		}
		if ( totalScadenza.compareTo( os.getIm_scadenza()) != 0 )	
			throw handleException( new it.cnr.jada.comp.ApplicationException( "La somma degli importi dei singoli dettagli della scadenza e' diverso dall'importo complessivo della scadenza"))	;		
			
	}
	
	if ( totalObbligazione.compareTo( obbligazione.getIm_obbligazione()) > 0 )
		throw handleException( new it.cnr.jada.comp.ApplicationException( "La somma degli importi delle singole scadenze supera l'importo complessivo dell'impegno"))	;

	if ( totalObbligazione.compareTo( obbligazione.getIm_obbligazione()) < 0 )
		throw handleException( new it.cnr.jada.comp.ApplicationException( "La somma degli importi delle singole scadenze e' inferiore all'importo complessivo dell'impegno"))	;
	if((obbligazione.getContratto() != null && obbligazione.getContratto().getFigura_giuridica_esterna()!= null && 
	   !(obbligazione.getCreditore().equalsByPrimaryKey(obbligazione.getContratto().getFigura_giuridica_esterna())||verificaAssociato(aUC, obbligazione.getContratto().getFigura_giuridica_esterna(),obbligazione))))
	     throw new it.cnr.jada.comp.ApplicationException( "Il Creditore (Codice Terzo:"+obbligazione.getCreditore().getCd_terzo()+") \n"+"non è congruente con quello del contratto (Codice Terzo:"+obbligazione.getContratto().getFigura_giuridica_esterna().getCd_terzo()+")");
	if ((obbligazione.getIncarico_repertorio() != null && obbligazione.getIncarico_repertorio().getTerzo()!= null &&
			!(obbligazione.getCreditore().equalsByPrimaryKey(obbligazione.getIncarico_repertorio().getTerzo())||verificaAssociato(aUC, obbligazione.getIncarico_repertorio().getTerzo(),obbligazione))))
		     throw new it.cnr.jada.comp.ApplicationException( "Il Creditore (Codice Terzo:"+obbligazione.getCreditore().getCd_terzo()+") \n"+"non è congruente con quello dell'incarico (Codice Terzo:"+obbligazione.getIncarico_repertorio().getTerzo().getCd_terzo()+")");
 
	verificaFl_spese_costi_altrui( aUC, obbligazione );

	/*
	 * Controllo l'eventuale obbligatorietà del Contratto
	 */
	Elemento_voceHome home = (Elemento_voceHome)getHome(aUC, Elemento_voceBulk.class);
	Elemento_voceBulk elemento_voce;
	try {
		elemento_voce =
			(Elemento_voceBulk) home.findByPrimaryKey(
				new Elemento_voceBulk(
						obbligazione.getCd_elemento_voce(),
						obbligazione.getEsercizio(),
						obbligazione.getTi_appartenenza(),
						obbligazione.getTi_gestione()));
		} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	if(elemento_voce.getFl_recon().booleanValue()){
		Parametri_cdsHome paramHome = (Parametri_cdsHome)getHome(aUC, Parametri_cdsBulk.class);
		Parametri_cdsBulk param_cds;
		try {
			param_cds =
				(Parametri_cdsBulk) paramHome.findByPrimaryKey(
					new Parametri_cdsBulk(
						obbligazione.getCd_cds(),
						obbligazione.getEsercizio()));
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}

		if (obbligazione.getFl_gara_in_corso().booleanValue() &&
			obbligazione.getDs_gara_in_corso() == null)
		  throw new it.cnr.jada.comp.ApplicationException("La gara di appalto risulta essere in corso di espletamento. Campo \"Descrizione gara di appalto\" obbligatorio!");

		if (obbligazione.getFl_gara_in_corso().booleanValue() && 
			(obbligazione.getPg_contratto() != null || obbligazione.getPg_repertorio() != null))
		  throw new it.cnr.jada.comp.ApplicationException("La gara di appalto risulta essere in corso di espletamento. Non è possibile valorizzare il campo contratto e/o incarico. Disattivare il flag \"Gara in corso di espletamento\" e ripetere l'operazione!");		

		if (obbligazione.getPg_contratto() != null && obbligazione.getPg_repertorio() != null)
		  throw new it.cnr.jada.comp.ApplicationException("Il campo contratto e incarico non possono essere valorizzati contemporaneamente. Eliminare uno dei due e ripetere l'operazione!");		

 	    if(param_cds != null && param_cds.getIm_soglia_contratto_s()!= null &&
			obbligazione.getIm_obbligazione().compareTo(param_cds.getIm_soglia_contratto_s())!=-1)
 			if (!obbligazione.getFl_gara_in_corso().booleanValue() &&
 				obbligazione.getPg_contratto() == null && obbligazione.getPg_repertorio() == null)
 				throw new it.cnr.jada.comp.ApplicationException("I campi contratto e incarico non possono essere contemporaneamente nulli in assenza di una gara in corso di espletamento. Importo dell'Impegno superiore al limite stabilito!");		
	}
	verificaGestioneTrovato(aUC, obbligazione, elemento_voce);
	verificaGestioneMissioni(aUC, obbligazione, elemento_voce);

	if (obbligazione.getArchivioAllegati().stream()
			.filter(AllegatoObbligazioneBulk.class::isInstance)
			.map(AllegatoObbligazioneBulk.class::cast)
			.filter(el->el.getEsercizioDiAppartenenza().equals(obbligazione.getEsercizio()))
			.filter(AllegatoObbligazioneBulk::isTipoDetermina).count()>1)
		throw new it.cnr.jada.comp.ApplicationException("E' possibile allegare solo un file di tipo 'Determina' per l'esercizio in corso ("+obbligazione.getEsercizio()+").");

	if (obbligazione.getArchivioAllegati().stream()
			.filter(AllegatoObbligazioneBulk.class::isInstance)
			.map(AllegatoObbligazioneBulk.class::cast)
			.filter(el->el.getEsercizioDiAppartenenza().equals(obbligazione.getEsercizio()))
			.filter(AllegatoObbligazioneBulk::isTipoRiaccertamentoResidui).count()>1)
		throw new it.cnr.jada.comp.ApplicationException("E' possibile allegare solo un file di tipo 'Riaccertamento Residui' per l'esercizio in corso ("+obbligazione.getEsercizio()+").");

	if (obbligazione.getAllegatoDetermina() != null && obbligazione.getAllegatoDetermina().getDeterminaDataProtocollo() == null)
		throw new it.cnr.jada.comp.ApplicationException("Indicare la data di protocollo sul file di tipo 'Determina'.");

	if (obbligazione.getAllegatoDetermina() == null &&
			(obbligazione.isProvvisoria() || obbligazione.isToBeCreated() || (obbligazione.getFl_determina_allegata()!=null && obbligazione.getFl_determina_allegata().equals(Boolean.TRUE)))) {
		obbligazione.setFl_determina_allegata(Boolean.FALSE);
		try {
			Parametri_cdsBulk parametriCds = Utility.createParametriCdsComponentSession().
				getParametriCds(aUC, obbligazione.getCd_cds(), CNRUserContext.getEsercizio(aUC));
			if (parametriCds.getFl_allega_determina_obblig()!=null && parametriCds.getFl_allega_determina_obblig().equals(Boolean.TRUE))
				throw new it.cnr.jada.comp.ApplicationException("Allegare all'obbligazione un file di tipo 'Determina' per l'esercizio in corso ("+obbligazione.getEsercizio()+").");
		} catch(Throwable e) {
			throw handleException(e);
		}
	} else {
		obbligazione.setFl_determina_allegata(Boolean.TRUE);
	}
}
private void verificaGestioneTrovato(UserContext aUC,
		ObbligazioneBulk obbligazione, Elemento_voceBulk elemento_voce)
				throws ComponentException {
//	if (obbligazione.getCd_iniziale_elemento_voce() != null && !elemento_voce.getCd_elemento_voce().equals(obbligazione.getCd_iniziale_elemento_voce())){
	if (obbligazione.getPg_obbligazione() != null){
		try {
			if(elemento_voce.isObbligatoriaIndicazioneTrovato()){
				controlliGestioneTrovatoAttiva(aUC, obbligazione);
			} else if (elemento_voce.isInibitaIndicazioneTrovato()){
				controlliGestioneTrovatoNonAttiva(aUC, obbligazione);
			}
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
//	}
}
private void verificaGestioneMissioni(UserContext aUC,
		ObbligazioneBulk obbligazione, Elemento_voceBulk elemento_voce)
				throws ComponentException {
//	if (obbligazione.getPg_obbligazione() != null){
//		try {
//			if (!elemento_voce.getFl_missioni() && esistonoMissioniCollegate(aUC, obbligazione)){
//				throw new it.cnr.jada.comp.ApplicationException("Attenzione! La voce del piano indicata non ha attiva la gestione delle missioni ma esistono missioni collegate all'obbligazione.");		
//			}
//		} catch(Throwable e) {
//			throw handleException(e);
//		}
//	}
//	}
}
private void controlliGestioneTrovatoNonAttiva(UserContext aUC,
		ObbligazioneBulk obbligazione) throws ComponentException, SQLException,
		ApplicationException {
	SQLBuilder sql = condizioneRigheFatturaConTrovatoValorizzato(aUC,obbligazione);
	if (sql.executeCountQuery(getConnection(aUC)) > 0){
		throw new it.cnr.jada.comp.ApplicationException("Attenzione! La voce del piano indicata non ha attiva la gestione dei brevetti ma sono state trovate fatture con l'indicazione del trovato.");		
	}
}
private void controlliGestioneTrovatoAttiva(UserContext aUC,
		ObbligazioneBulk obbligazione) throws ComponentException, SQLException,
		ApplicationException {
	SQLBuilder sql = condizioneRigheFatturaConTrovatoNonValorizzato(aUC, obbligazione);
	if (sql.executeCountQuery(getConnection(aUC)) > 0){
		throw new it.cnr.jada.comp.ApplicationException("Attenzione! La voce del piano indicata ha attiva la gestione dei brevetti ma sono state trovate fatture senza l'indicazione del trovato.");		
	}
}
private SQLBuilder condizioneRigheFatturaConTrovatoNonValorizzato(
		UserContext aUC, ObbligazioneBulk obbligazione)
		throws ComponentException {
	SQLBuilder sql = preparaCondizionePerTrovato(aUC, obbligazione);
	sql.addSQLClause(FindClause.AND,"FATTURA_PASSIVA_RIGA.pg_trovato",sql.ISNULL, null);
	return sql;
}
private SQLBuilder condizioneRigheFatturaConTrovatoValorizzato(UserContext aUC,
		ObbligazioneBulk obbligazione) throws ComponentException {
	SQLBuilder sql = preparaCondizionePerTrovato(aUC, obbligazione);
	sql.addSQLClause(FindClause.AND,"FATTURA_PASSIVA_RIGA.pg_trovato",sql.ISNOTNULL, null);
	return sql;
}
private SQLBuilder preparaCondizionePerTrovato(UserContext aUC,
		ObbligazioneBulk obbligazione) throws ComponentException {
	PersistentHome osHome = getHomeCache(aUC).getHome(Obbligazione_scadenzarioBulk.class);
	SQLBuilder sql = osHome.createSQLBuilder();
	sql.addClause(FindClause.AND,"cd_cds",sql.EQUALS, obbligazione.getCds().getCd_unita_organizzativa());
	sql.addClause(FindClause.AND,"esercizio",sql.EQUALS, obbligazione.getEsercizio());
	sql.addClause(FindClause.AND,"esercizio_originale",sql.EQUALS, obbligazione.getEsercizio_originale());
	sql.addClause(FindClause.AND,"pg_obbligazione",sql.EQUALS, obbligazione.getPg_obbligazione());
	sql.addTableToHeader("FATTURA_PASSIVA_RIGA");
	sql.addSQLClause(FindClause.AND,"FATTURA_PASSIVA_RIGA.ESERCIZIO",SQLBuilder.EQUALS, obbligazione.getEsercizio());
	sql.addSQLJoin( "FATTURA_PASSIVA_RIGA.CD_CDS_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.CD_CDS");
	sql.addSQLJoin( "FATTURA_PASSIVA_RIGA.ESERCIZIO_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO");
	sql.addSQLJoin( "FATTURA_PASSIVA_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE");
	sql.addSQLJoin( "FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE");
	sql.addSQLJoin( "FATTURA_PASSIVA_RIGA.PG_OBBLIGAZIONE_SCADENZARIO", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO");
	return sql;
}

private Boolean esistonoMissioniCollegate(UserContext aUC,
		ObbligazioneBulk obbligazione) throws ComponentException, SQLException {
	PersistentHome osHome = getHomeCache(aUC).getHome(Obbligazione_scadenzarioBulk.class);
	SQLBuilder sql = osHome.createSQLBuilder();
	sql.addClause(FindClause.AND,"cd_cds",sql.EQUALS, obbligazione.getCds().getCd_unita_organizzativa());
	sql.addClause(FindClause.AND,"esercizio",sql.EQUALS, obbligazione.getEsercizio());
	sql.addClause(FindClause.AND,"esercizio_originale",sql.EQUALS, obbligazione.getEsercizio_originale());
	sql.addClause(FindClause.AND,"pg_obbligazione",sql.EQUALS, obbligazione.getPg_obbligazione());
	sql.addTableToHeader("MISSIONE");
	sql.addSQLClause(FindClause.AND,"MISSIONE.ESERCIZIO",SQLBuilder.EQUALS, obbligazione.getEsercizio());
	sql.addSQLJoin( "MISSIONE.CD_CDS_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.CD_CDS");
	sql.addSQLJoin( "MISSIONE.ESERCIZIO_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO");
	sql.addSQLJoin( "MISSIONE.ESERCIZIO_ORI_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE");
	sql.addSQLJoin( "MISSIONE.PG_OBBLIGAZIONE", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE");
	sql.addSQLJoin( "MISSIONE.PG_OBBLIGAZIONE_SCADENZARIO", "OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE_SCADENZARIO");
	if (sql.executeCountQuery(getConnection(aUC)) > 0){
		return true;
	}
	return false;
}

public void verificaCoperturaContratto (UserContext aUC,ObbligazioneBulk obbligazione, int flag) throws ComponentException
{
	//	Controllo che l'obbligazione non abbia sfondato il contratto
	if (obbligazione.isCheckDisponibilitaContrattoEseguito())
	  return;
	if (obbligazione.getContratto() != null && obbligazione.getContratto().getPg_contratto() != null){
	  try {	
		  ContrattoHome contrattoHome = (ContrattoHome)getHome(aUC, ContrattoBulk.class);
		  SQLBuilder sql = contrattoHome.calcolaTotObbligazioni(aUC,obbligazione.getContratto());
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
		  if (flag == INSERIMENTO)
		    totale = totale.add(obbligazione.getIm_obbligazione());			  
		  if (totale != null ){
			  if (totale.compareTo(obbligazione.getContratto().getIm_contratto_passivo()) > 0){
				  throw handleException( new CheckDisponibilitaContrattoFailed("La somma degli impegni associati supera l'importo definito nel contratto."));
			  }
		  }
	  } catch (IntrospectionException e1) {
		  throw new it.cnr.jada.comp.ComponentException(e1);
	  } catch (PersistencyException e1) {
		  throw new it.cnr.jada.comp.ComponentException(e1);
	  }
  }		

}
public void verificaCoperturaContratto (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	verificaCoperturaContratto (aUC,obbligazione, MODIFICA);
}

	public void verificaCoerenzaGaeContratto(UserContext aUC, ObbligazioneBulk obbligazione) throws ComponentException {
		try {
			Optional<Integer> optPrgContratto = Optional.ofNullable(obbligazione)
					.flatMap(el->Optional.ofNullable(el.getContratto()))
					.flatMap(el->Optional.ofNullable(el.getPg_progetto()));

			if (optPrgContratto.isPresent()) {
				ProgettoHome progettoHome = (ProgettoHome)getHome(aUC, ProgettoBulk.class);
				WorkpackageHome home = (WorkpackageHome)getHome(aUC, WorkpackageBulk.class);
				obbligazione.getObbligazione_scadenzarioColl().stream()
						.flatMap(el->Optional.ofNullable(el.getObbligazione_scad_voceColl()).map(List::stream).orElse(Stream.empty()))
						.map(el->el.getLinea_attivita())
						.distinct()
						.forEach(el->{
							try {
								WorkpackageBulk lineaAttivita = home.searchGAECompleta(aUC, obbligazione.getEsercizio(), el.getCd_centro_responsabilita(), el.getCd_linea_attivita());
								if (!lineaAttivita.getPg_progetto().equals(optPrgContratto.get())) {
									ProgettoBulk prgContratto = (ProgettoBulk)progettoHome.findByPrimaryKey(new ProgettoBulk(obbligazione.getEsercizio(), optPrgContratto.get(), ProgettoBulk.TIPO_FASE_NON_DEFINITA));
									throw new ApplicationRuntimeException("Linea di Attività "+el.getCd_linea_attivita()+" del CDR "+el.getCd_centro_responsabilita()+
											" non selezionabile in quanto appartenente al progetto "+lineaAttivita.getCd_progetto()+" diverso dal progetto " +
											prgContratto.getCd_progetto()+" del contratto associato all'impegno.");
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
  *      Alla scrittura dell'obbligazione il sistema aggiungerà questo scadenzario e genererà tutti i dettagli della
  *      scadenza (metodo 'generaDettagliScadenzaObbligazione')
  *  Tutti controlli superati - aggiornamento con agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'obbligazione il sistema aggiornerà questo scadenzario. 
  *      In più, il metodo aggiornaScadenzaSuccessivaObbligazione viene utilizzato per aggiornare la scadenza successiva 
  *      a quella in aggiornamento. 
  *  Tutti controlli superati - aggiornamento senza agg. auto. scad. succ.
  *    PreCondition:
  *      Attività = aggiornamento
  *      L'utente NON ha scelto l'aggiornamento in automatico della scadenza successiva.
  *    PostCondition:
  *      Alla scrittura dell'obbligazione il sistema aggiornerà questo scadenzario. 
  *      Sarà il compito dell'utente aggiornare una delle scadenze per garantire che la somma degli importi 
  *      delle scadenze sia uguale all'importo dell'obbligazione.
  *  creazione/modifica - esiste già una scadenza per la data
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica la data di una scadenza. 
  *      Per la data scadenza specificata esiste già una scadenza per l'obbligazione.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la data della scadenza non è valida.
  *  creazione/modifica - importo negativo
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica l'importo di una scadenza
  *      Il nuovo importo e' negativo.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'importo della scadenza deve essere > 0
  *  creazione/modifica - importo nullo 
  *    PreCondition:
  *      L'utente richiede la creazione di una scadenza o modifica l'importo di una scadenza
  *      Il nuovo importo e' nulla e la scadenza non è associata a documenti amministrativi
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'importo della scadenza deve essere >= 0
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
  *  modifica - la scadenza ha mandati associati
  *    PreCondition:
  *      L'utente richiede la modifica dell'importo di una scadenza 
  *      La scadenza ha mandati associati
  *      La richiesta di modifica proviene dal BusinessProcess che gestisce i documenti amministrativi
  *    PostCondition:
  *      L'aggiornamento dell'importo della scadenza non e' consentito
  
  * @param aUC lo user context 
  * @param scadenzario l'istanza di  <code>Obbligazione_scadenzarioBulk</code> da verificare
  * @return l' ObbligazioneBulk a cui appartiene la scadenza
  *
  */
public ObbligazioneBulk verificaScadenzarioObbligazione (UserContext aUC,Obbligazione_scadenzarioBulk scadenzario) throws ComponentException
{
	ObbligazioneBulk obbligazione = scadenzario.getObbligazione();
	Obbligazione_scadenzarioBulk os, osNew;

	//segnalo impossibilità di modificare importo se ci sono doc amministrativi associati
	if ( //!scadenzario.getObbligazione().isFromDocAmm() &&
		!scadenzario.isFromDocAmm() && !scadenzario.getFlAssociataOrdine() &&
		scadenzario.getScadenza_iniziale() != null && 
		scadenzario.getIm_scadenza().compareTo(scadenzario.getScadenza_iniziale().getIm_scadenza()) != 0 &&
//		scadenzario.getIm_associato_doc_amm().compareTo( new BigDecimal(0)) > 0 &&
		scadenzario.getPg_doc_passivo() != null
	)
		throw new ApplicationException( "Impossibile variare importo di una scadenza con doc. amministrativi associati");

	if ( //!scadenzario.getObbligazione().isFromDocAmm() &&
		scadenzario.isFromDocAmm() &&
		scadenzario.getScadenza_iniziale() != null && 
		scadenzario.getIm_scadenza().compareTo(scadenzario.getScadenza_iniziale().getIm_scadenza()) != 0 &&
//		scadenzario.getIm_associato_doc_amm().compareTo( new BigDecimal(0)) > 0 &&
		scadenzario.getPg_mandato() != null
	)
		throw new ApplicationException( "Impossibile variare importo di una scadenza con mandati associati");
		
		
	// riordino lo scadenzario
	/* simona 13.2.2002 */
	/* commentato l'ordinamento delle scadenze perche' può generare problemi quando l'accertamento e' richiamato dai documenti
	   amministrativi con una scadenza selezionata */	
//	obbligazione.setObbligazione_scadenzarioColl( scadenzario.ordinaPerDataScadenza( obbligazione.getObbligazione_scadenzarioColl()));	
	
	/*
	// segnala errore se scadenze duplicate o se importo <= 0
	for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
	{
		os = (Obbligazione_scadenzarioBulk) i.next();
		if ( !os.equals(scadenzario) && os.getDt_scadenza().equals( scadenzario.getDt_scadenza()))
			throw handleException( new it.cnr.jada.comp.ApplicationException( "Esiste gia' una scadenza per la data specificata"));
	}
	*/
	if ( scadenzario.getIm_scadenza().doubleValue() < 0 )
			throw handleException( new it.cnr.jada.comp.ApplicationException( "L'importo della scadenza deve essere maggiore di 0 "));

	/* rimosso questo controllo per consentire comunque l'inserimento/modifica dell'importo a 0 -
	   questo è utile quando è già stato creato il mandato e pertanto non è possibile effettuare la cancellazione
	   fisica della scadenza (per i vincoli di integrità referenziale con le righe del mandato, pertanto 
	   l'unica alternativa è quella di impostare a 0 la scadenza */
	   /*	
/*			
	if ( scadenzario.getIm_scadenza().doubleValue() == 0  && scadenzario.getPg_doc_passivo() == null)
			throw handleException( new it.cnr.jada.comp.ApplicationException( "L'importo della scadenza deve essere maggiore a 0 "));

*/
	//aggiorno scadenza succcessiva ed i suoi dettagli
	if ( scadenzario.getFl_aggiorna_scad_successiva() != null && 
		 scadenzario.getFl_aggiorna_scad_successiva().booleanValue() && 
		 scadenzario.getScadenza_iniziale() != null && 
		 scadenzario.getIm_scadenza().compareTo( scadenzario.getScadenza_iniziale().getIm_scadenza()) != 0 )
		aggiornaScadenzaSuccessivaObbligazione( aUC, scadenzario );
	else
//	calcolaDisponibilitaCassaObbligazione( aUC, scadenzario );
		generaDettagliScadenzaObbligazione( aUC, obbligazione, scadenzario, obbligazione.isObbligazioneResiduo()?false:true); 

	//genero i dettagli anche per la riga nuova che, nel caso di residui propri 
	//potrebbe essere stata creata 
	//Lello - Sdoppiamento scadenza anche su obbligazione 
//	if (osNew != null && obbligazione.isObbligazioneResiduo())
//	if (osNew != null)
//		generaDettagliScadenzaObbligazione( aUC, obbligazione, osNew, false);

	scadenzario.setStatus( scadenzario.STATUS_CONFIRMED);
	scadenzario.setScadenza_iniziale( null );
	
	return obbligazione;

}
/**
 * Verifica dello stato dell'esercizio 
 *
 * @param userContext <code>UserContext</code> 
 *
 * @return FALSE se per il cds interessato non è stato inserito nessun esercizio o se l'esercizio non è in stato di "aperto"
 *		   TRUE in tutti gli altri casi
 *
 */

protected void  verificaStatoEsercizio( UserContext userContext, Integer es, String cd_cds ) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	EsercizioBulk esercizio = verificaStatoEsercizio( userContext, cd_cds, es);
	if ( !esercizio.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()))
			throw new ApplicationException( "Inserimento impossibile: esercizio non aperto!") ;
}			
			
public EsercizioBulk  verificaStatoEsercizio( UserContext userContext, String cd_cds, Integer es ) throws ComponentException
{
	try
	{
		EsercizioBulk esercizio = (EsercizioBulk) getHome(userContext, EsercizioBulk.class).findByPrimaryKey( 
																										new EsercizioBulk( cd_cds, es ));
		return esercizio;
	}
	catch ( Exception e )
	{
		throw handleException(e)	;
	}	
}			
			
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Copertura finanziaria è sufficiente.
  *      Data obbligazione è valida.
  *      Esercizio competenza >= esercizio creazione dell'obbligazione
  *    PostCondition:
  *      La testata dell'obbligazione è valida. E' consentito eseguire l'attività di salvataggio o di passaggio 
  *		alle pagine successive (Configurazione Imputazione Finanziaria o Scadenzario).
  *  Copertura finanziaria insufficiente
  *    PreCondition:
  *      La copertura finanziaria in riferimento al limite di assunzione di obbligazioni risulta insufficiente.
  *      Il controllo della copertura finanziaria dipende dal metodo 'controllaCoperturaFinanziariaObbligazione', 
  *      che calcola un valore per questo limite secondo i dettagli dell'obbligazione in aggiornamento. 
  *      Per l'aggiornamento, il valore del limite viene confrontato con la differenza fra l'importo vecchio e 
  *      l'importo nuovo dell'obbligazione, quando l'importo nuovo supera l'importo vecchio. 
  *      (Se l'importo rimane uguale, o diminuisce, questo controllo della copertura finanziaria non viene eseguito.)
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'importo dell'obbligazione in aggiornamento supera 
  *      il limite di assunzione di obbligazioni. L'attività non è consentita.
  *  Data obbligazione non è valida
  *    PreCondition:
  *      La data dell'obbligazione in inserimento antecede la data dell'ultima obbligazione inserita per questo CdS. 
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che la data dell'obbligazione non può essere antecedente
  *      la data dell'ultima obbligazione inserita per questo CdS. L'attività non è consentita.
  *  Esercizio competenza non valido
  *    PreCondition:
  *      L'esercizio di competenza dell'obbligazione e' inferiore all'esercizio di scrivania e quindi all'esercizio
  *      di creazione dell'obbligazione
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare che l'esercizio di competenza non può essere antecedente
  *      all'esercizio di creazione dell'obbligazione
  *
  * @param aUC lo user context 
  * @param obbligazione l'istanza di  <code>ObbligazioneBulk</code> per cui verificare la testata
  *  
 */
public void verificaTestataObbligazione (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
{
	
	try
	{
	 	controllaCoperturaFinanziariaObbligazione( aUC, obbligazione );
	// verifica data dell'obbligazione
		if ( obbligazione.isToBeCreated() )
		{
			Timestamp lastDayOfTheYear = DateServices.getLastDayOfYear( obbligazione.getEsercizio().intValue());

			if ( obbligazione.getDt_registrazione().before(DateServices.getFirstDayOfYear( obbligazione.getEsercizio().intValue())) ||
				  obbligazione.getDt_registrazione().after(lastDayOfTheYear))
				throw  new ApplicationException( "La data di registrazione deve appartenere all'esercizio di scrivania" );

			if ( getDataOdierna( aUC).after(lastDayOfTheYear ) &&
				  obbligazione.getDt_registrazione().compareTo( lastDayOfTheYear) != 0 )
				throw  new ApplicationException( "La data di registrazione deve essere " +
   									java.text.DateFormat.getDateInstance().format( lastDayOfTheYear ));					
			
			Timestamp dataUltObbligazione = ((ObbligazioneHome) getHome( aUC, ObbligazioneBulk.class )).findDataUltimaObbligazionePerCds( obbligazione );
			if ( dataUltObbligazione != null && dataUltObbligazione.after( obbligazione.getDt_registrazione() ) )
				throw  new ApplicationException( "Non è possibile inserire un'obbligazione con data anteriore a " +  
   									java.text.DateFormat.getDateInstance().format( dataUltObbligazione ));
		}
		if ( (obbligazione.getEsercizio_competenza()).intValue() < (obbligazione.getEsercizio()).intValue() )
			throw new ApplicationException("Non è possibile creare un'obbligazione con esercizio antecedente a quello di scrivania.");
	}
	catch ( Exception e )
	{
		throw handleException( e );
	}	
}
	public void controllaAssunzioneImpegni(UserContext userContext) throws it.cnr.jada.comp.ComponentException{
		try
		{
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
				"call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
				"CNRCTB270.controllaAssunzioneImpegni(?, ?)",false,this.getClass());
			try
			{
				cs.setString( 1, CNRUserContext.getCd_cds(userContext));			
				cs.setObject( 2, CNRUserContext.getEsercizio(userContext));
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
	public void controllaAssunzioneImpResImpro(UserContext userContext) throws it.cnr.jada.comp.ComponentException{
		try
		{
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
				"call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
				"CNRCTB270.controllaAssunzioneImpResImpro(?, ?)",false,this.getClass());
			try
			{
				cs.setString( 1, CNRUserContext.getCd_cds(userContext));			
				cs.setObject( 2, CNRUserContext.getEsercizio(userContext));
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
	 * Aggiorna l'obbligazione in base alla selezione delle voci effettuate dall'utente.
	 * L'utente, in fase di selezione voci, può o meno indicare l'importo da imputare per ogni Cdr/Gae/Voce.
	 * Gli importi indicati dall'utente per CDR/GAE/Voce vengono presi in considerazione solo se 
	 * l'obbligazione è formata da un'unica scadenza.
	 * In caso di più scadenze vengono assegnate solo le combinazioni scelte CDR/Gae/Voce mentre gli importi vengono 
	 * ricalcolati con le modalità abituali (es. percentuale GAE all'interno del Bilancio).
	 * 
	 * @param userContext
	 * @param obbligazione
	 * @param vociList
	 * @return
	 * @throws it.cnr.jada.comp.ComponentException
	 */
	public ObbligazioneBulk riportaSelezioneVoci(UserContext userContext, ObbligazioneBulk obbligazione, java.util.List vociList)  throws it.cnr.jada.comp.ComponentException{
		ObbligazioneHome obbligHome = (ObbligazioneHome) getHome( userContext, obbligazione.getClass());
		Obbligazione_scadenzarioHome osHome = (Obbligazione_scadenzarioHome) getHome( userContext, Obbligazione_scadenzarioBulk.class );

		// carica i capitoli di spesa del CDS
		obbligazione = listaCapitoliPerCdsVoce( userContext, obbligazione );
		obbligazione.refreshCapitoliDiSpesaCdsSelezionatiColl(vociList);

		// carica i cdr
		obbligazione.setCdrColl( listaCdrPerCapitoli( userContext,  obbligazione));
		obbligazione.refreshCdrSelezionatiColl(vociList);

		// carica le linee di attività da PDG
		obbligazione.setLineeAttivitaColl( listaLineeAttivitaPerCapitoliCdr( userContext,  obbligazione));
		obbligazione.refreshLineeAttivitaSelezionateColl(vociList);

		obbligazione.setInternalStatus( ObbligazioneBulk.INT_STATO_LATT_CONFERMATE );
		obbligazione.setIm_iniziale_obbligazione( obbligazione.getIm_obbligazione());
		obbligazione.setCd_iniziale_elemento_voce( obbligazione.getCd_elemento_voce());
		obbligazione.setCd_terzo_iniziale( obbligazione.getCd_terzo());

		BigDecimal totaleSelVoci = new BigDecimal( 0 ).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		//Carico la lista delle voci con gli importi da ripartirre
		PrimaryKeyHashtable hashVociList = new PrimaryKeyHashtable();  

		if (!vociList.isEmpty() && vociList.get(0) instanceof V_assestatoBulk) {
			for ( Iterator s = vociList.iterator(); s.hasNext(); )
			{
				V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
				hashVociList.put(voceSel, new BigDecimal(0)); 
				if (Utility.nvl(voceSel.getImp_da_assegnare()).compareTo(new BigDecimal(0))!=-1)
					totaleSelVoci = totaleSelVoci.add( Utility.nvl(voceSel.getImp_da_assegnare()) );
			}
		}			

		//Valorizzo il campo Percentuale che utilizzerò per individuare gli importi da attribuire ad ogni scadenza
		if (totaleSelVoci.compareTo(Utility.ZERO)>0)
		{
			for ( Enumeration e = hashVociList.keys(); e.hasMoreElements(); ) 
			{
				V_assestatoBulk voceSel = (V_assestatoBulk)e.nextElement();
				voceSel.setPrc_da_assegnare(Utility.nvl(voceSel.getImp_da_assegnare()).divide(totaleSelVoci, 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));				
			}
		}

		if (totaleSelVoci.compareTo(obbligazione.getIm_obbligazione())>0)
			obbligazione.setIm_obbligazione(totaleSelVoci);

		//Crea l'eventuale scadenza mancante
		creaScadenzaResiduale(userContext, obbligazione);

		//Rigenero i dettagli di tutte le scadenze
		for ( Iterator s = obbligazione.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); )
			generaDettagliScadenzaObbligazione( userContext, obbligazione, ((Obbligazione_scadenzarioBulk)s.next()), false);

		if (totaleSelVoci.compareTo(new BigDecimal(0))>0) {

			Obbligazione_scadenzarioBulk os = null;
			Obbligazione_scad_voceBulk osv;
			
			BigDecimal impDaAssegnareObb = totaleSelVoci;
			BigDecimal impAssegnatoObb = new BigDecimal( 0 );

			//DEVO PRIMA VERIFICARE CHE L'IMPORTO SELEZIONATO SIA ALMENO SUFFICIENTE A COLMARE LA QUOTA ASSOCIATA A DOC.AMM.
			BigDecimal impAssociatoDocAmm = new BigDecimal( 0 ); 
			for ( Iterator iteratorOs = obbligazione.getObbligazione_scadenzarioColl().iterator(); iteratorOs.hasNext(); )
				impAssociatoDocAmm = impAssociatoDocAmm.add(((Obbligazione_scadenzarioBulk)iteratorOs.next()).getIm_associato_doc_amm());

			if (impAssociatoDocAmm.compareTo(impDaAssegnareObb)>0)
				throw new ApplicationException("Non è possibile attribuire all'obbligazione un importo " + new it.cnr.contab.util.EuroFormat().format(impDaAssegnareObb) +
						" inferiore a quanto risulta associato a documenti amministrativi " + new it.cnr.contab.util.EuroFormat().format(impAssociatoDocAmm) + ".");

			//AGGIORNO PRIMA LE SCADENZE ASSOCIATE A DOCUMENTI AMMINISTRATIVI
			for ( Iterator iteratorOs = obbligazione.getObbligazione_scadenzarioColl().iterator(); iteratorOs.hasNext(); ) {

				os = (Obbligazione_scadenzarioBulk)iteratorOs.next();

				//SELEZIONO QUELLE ASSOCIATE A DOCUMENTI AMMINISTRATIVI
				if (os.getIm_associato_doc_amm().compareTo(new BigDecimal(0))>0){

					//SE L'IMPORTO DELLA SCADENZA è MAGGIORE DI QUANTO DEVE ESSERE ANCORA RIPARTITO
					//NE RIDUCO L'IMPORTO CONTROLLANDO DI NON RENDERLO INFERIORE A QUANTO ASSOCIATO A 
					//DOCUMENTI AMMINISTRATIVI
					if (os.getIm_scadenza().compareTo(impDaAssegnareObb)>0){
						if (os.getIm_associato_doc_amm().compareTo(impDaAssegnareObb)<0){
							throw new ApplicationException("Non è possibile inserire un importo " +	new it.cnr.contab.util.EuroFormat().format(impDaAssegnareObb) +
									"inferiore a quanto risulta associato a documenti amministrativi " + new it.cnr.contab.util.EuroFormat().format(impAssociatoDocAmm) + ".");
						}
						os.setIm_scadenza(impDaAssegnareObb);
					}
					
					spalmaImportiSuScadenza(userContext, os, hashVociList);
					os.setToBeUpdated();
					impDaAssegnareObb = impDaAssegnareObb.subtract(os.getIm_scadenza());
					impAssegnatoObb = impAssegnatoObb.add(os.getIm_scadenza());
				}
			}
		
			BulkList recDaEliminare = new BulkList();
			//	POI LE SCADENZE NON ASSOCIATE A DOCUMENTI AMMINISTRATIVI
			for ( Iterator iteratorOs = obbligazione.getObbligazione_scadenzarioColl().iterator(); iteratorOs.hasNext(); ) {
				os = (Obbligazione_scadenzarioBulk)iteratorOs.next();

				//SELEZIONO QUELLE NON ASSOCIATE A DOCUMENTI AMMINISTRATIVI
				if (os.getIm_associato_doc_amm().compareTo(new BigDecimal(0))<=0){

					if (impDaAssegnareObb.compareTo(Utility.ZERO)==0) {
						for ( Iterator iteratorOsv = os.getObbligazione_scad_voceColl().iterator(); iteratorOsv.hasNext(); )
							((Obbligazione_scad_voceBulk)iteratorOsv.next()).setToBeDeleted();
						os.setToBeDeleted();
						recDaEliminare.add(os);
					}
					else
					{
						//SE L'IMPORTO DELLA SCADENZA è MAGGIORE DI QUANTO DEVE ESSERE ANCORA RIPARTITO
						//NE RIDUCO L'IMPORTO 
						if (os.getIm_scadenza().compareTo(impDaAssegnareObb)>0)
							os.setIm_scadenza(impDaAssegnareObb);

						spalmaImportiSuScadenza(userContext, os, hashVociList);
						os.setToBeUpdated();
						impDaAssegnareObb = impDaAssegnareObb.subtract(os.getIm_scadenza());
						impAssegnatoObb = impAssegnatoObb.add(os.getIm_scadenza());
					}
				}
			}
			
			//Elimino dallo scadenzario dell'obbligazione le scadenze segnate come da eliminare 
			for ( Iterator iteratorDel = recDaEliminare.iterator(); iteratorDel.hasNext(); )
				obbligazione.getObbligazione_scadenzarioColl().remove(iteratorDel.next());

			obbligazione.setIm_obbligazione(impAssegnatoObb.setScale(2,BigDecimal.ROUND_HALF_UP));
			obbligazione.setFl_calcolo_automatico(Boolean.FALSE);
		} 
		else
		{
			if ( obbligazione.getFl_calcolo_automatico().booleanValue() && !obbligazione.isObbligazioneResiduo())
				obbligazione = calcolaPercentualeImputazioneObbligazione( userContext, obbligazione );
		}
		return obbligazione;
	}
	
	/**
	 * Metodo che spalma su una scadenza gli importi dei CDR/GAE/Voce secondo le percentuali indicate.
	 *    PreCondition:
	 *      E' stato richiesto di assegnare alla scadenza indicata gli importi dei CDR/GAE/Voce tenendo conto
	 *      della ripartizione percentuale e degli importi da assegnare indicati nella HashList
	 *    PostCondition:
	 *      Viene verificato che gli importi da ripartire dei CDR/GAE/Voce siano sufficienti a coprire l'importo 
	 *      della scadenza. Quindi viene caricato lo scadenzario/voce aggiornando l'importo assegnato alla voce
	 *      della HashList
	 *
	 * @param userContext
	 * @param os <code>Obbligazione_scadenzarioBulk</code>lo scadenzario da aggiornare
	 * @param hashVociList la lista di oggetti V_assestatoBulk da cui prendere le percentuali e gli importi
	 * 		  da spalmare.
	 * @return la scadenza <code>Obbligazione_scadenzarioBulk</code> aggiornata
	 * @throws it.cnr.jada.comp.ComponentException
	 */
	private Obbligazione_scadenzarioBulk spalmaImportiSuScadenza(UserContext userContext, 
															     Obbligazione_scadenzarioBulk os, 
															     PrimaryKeyHashtable hashVociList) throws it.cnr.jada.comp.ComponentException
	{
		Obbligazione_scad_voceBulk osv;

		BigDecimal impDaAssegnareScadenza = os.getIm_scadenza();
		BigDecimal impAssegnatoScadenza = new BigDecimal(0);

		//AGGIORNO LO SCADENZARIO VOCI IN PERCENTUALE ALLE SELEZIONI EFFETTUATE
		for ( Iterator iteratorOsv = os.getObbligazione_scad_voceColl().iterator(); iteratorOsv.hasNext(); )
		{
			osv = (Obbligazione_scad_voceBulk) iteratorOsv.next();
			osv.setIm_voce( Utility.ZERO );
			osv.setToBeUpdated();

			for ( Enumeration e = hashVociList.keys(); e.hasMoreElements(); ) 
			{
				V_assestatoBulk voceSel = (V_assestatoBulk)e.nextElement();
				BigDecimal impDaAssegnareVoce = Utility.nvl(voceSel.getImp_da_assegnare()).subtract((BigDecimal) hashVociList.get( voceSel ));
				BigDecimal prcDaAssegnareVoce = os.getIm_scadenza().multiply(voceSel.getPrc_da_assegnare().divide(new BigDecimal(100)));

				if (prcDaAssegnareVoce.compareTo(impDaAssegnareVoce)>0)
					prcDaAssegnareVoce = impDaAssegnareVoce;
				if (prcDaAssegnareVoce.compareTo(impDaAssegnareScadenza)>0)
					prcDaAssegnareVoce = impDaAssegnareScadenza;
				
				if (prcDaAssegnareVoce.compareTo(Utility.ZERO)>0 &&
					voceSel.getEsercizio().equals(osv.getEsercizio()) &&
					voceSel.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
					voceSel.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&								
					voceSel.getTi_appartenenza().equals(osv.getTi_appartenenza()) &&
					voceSel.getTi_gestione().equals(osv.getTi_gestione()) &&
					voceSel.getCd_voce().equals(osv.getCd_voce()))
				{
					//Importo scadenza moltiplicato per la percentuale ottenuto dalla divisione tra
					//l'importo assegnato al CDR/VOCE/GAE e l'importo totale
					osv.setIm_voce( prcDaAssegnareVoce.setScale(2,BigDecimal.ROUND_HALF_UP));

					//importo assegnato CDR/VOCE/LINEA
					hashVociList.put(voceSel, ((BigDecimal) hashVociList.get( voceSel )).add(osv.getIm_voce()));
					break;
				}
			}
				
			impDaAssegnareScadenza = impDaAssegnareScadenza.subtract(osv.getIm_voce());
			impAssegnatoScadenza = impAssegnatoScadenza.add(osv.getIm_voce());
		}
		
		//Se la scadenza non è stata completamente coperta, vado a recuperare sulle combinazioni CDR/VOCE/GAE 
		//gli importi ancora disponibili 
		if (os.getIm_scadenza().compareTo(impAssegnatoScadenza)>0) {
			for ( Iterator iteratorOsv = os.getObbligazione_scad_voceColl().iterator(); iteratorOsv.hasNext(); )
			{
				osv = (Obbligazione_scad_voceBulk) iteratorOsv.next();
				BigDecimal impDaAssegnareVoce = new BigDecimal(0);
				for ( Enumeration e = hashVociList.keys(); e.hasMoreElements(); ) 
				{
					V_assestatoBulk voceSel = (V_assestatoBulk)e.nextElement();
					impDaAssegnareVoce = Utility.nvl(voceSel.getImp_da_assegnare()).subtract((BigDecimal) hashVociList.get( voceSel ));

					if (impDaAssegnareVoce.compareTo(impDaAssegnareScadenza)>0)
						impDaAssegnareVoce = impDaAssegnareScadenza;
					
					if (impDaAssegnareVoce.compareTo(Utility.ZERO)>0 &&
						voceSel.getEsercizio().equals(osv.getEsercizio()) &&
						voceSel.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
						voceSel.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&								
						voceSel.getTi_appartenenza().equals(osv.getTi_appartenenza()) &&
						voceSel.getTi_gestione().equals(osv.getTi_gestione()) &&
						voceSel.getCd_voce().equals(osv.getCd_voce()))
					{
						//Importo scadenza moltiplicato per la percentuale ottenuto dalla divisione tra
						//l'importo assegnato al CDR/VOCE/GAE e l'importo totale
						osv.setIm_voce( osv.getIm_voce().add(impDaAssegnareVoce.setScale(2,BigDecimal.ROUND_HALF_UP)));
						osv.setToBeUpdated();

						//importo assegnato CDR/VOCE/LINEA
						hashVociList.put(voceSel, ((BigDecimal) hashVociList.get( voceSel )).add(impDaAssegnareVoce.setScale(2,BigDecimal.ROUND_HALF_UP)));

						impDaAssegnareScadenza = impDaAssegnareScadenza.subtract(impDaAssegnareVoce.setScale(2,BigDecimal.ROUND_HALF_UP));
						impAssegnatoScadenza = impAssegnatoScadenza.add(impDaAssegnareVoce.setScale(2,BigDecimal.ROUND_HALF_UP));

						break;
					}
				}
			}
		}
			
		os.setIm_scadenza(impAssegnatoScadenza.setScale(2,BigDecimal.ROUND_HALF_UP));
		
		if (os.getIm_scadenza().compareTo(os.getIm_associato_doc_amm())==-1)
			throw new ApplicationException("Non è possibile attribuire all'obbligazione un importo inferiore a quanto "
					+ "risulta associato a documenti amministrativi.");


		//Aggiusto le percentuali
		for ( Iterator j = os.getObbligazione_scad_voceColl().iterator(); j.hasNext(); )
		{
			osv = (Obbligazione_scad_voceBulk) j.next();
			if ( os.getIm_scadenza().doubleValue() != 0 )
				osv.setPrc( (osv.getIm_voce().multiply( new BigDecimal(100)).divide( os.getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP)));
			else
				osv.setPrc( new BigDecimal(0));
		}
		return os;
	}
	/*
	 * Crea una scadenza di importo pari alla quota residua dell'impegno ancora da scadenziare.
	 * Attribuisce come descrizione e data scadenza la descrizione e data di emissione dell'obbligazione   
	 */
	public ObbligazioneBulk creaScadenzaResiduale(UserContext userContext, ObbligazioneBulk obbligazione)  throws it.cnr.jada.comp.ComponentException{
		BigDecimal imResiduo = obbligazione.getIm_obbligazione();
		Obbligazione_scadenzarioBulk os;

		for ( Iterator s = obbligazione.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); )
			imResiduo = imResiduo.subtract(((Obbligazione_scadenzarioBulk) s.next()).getIm_scadenza());
			
		if (imResiduo.compareTo(Utility.ZERO)>0) {
			Obbligazione_scadenzarioBulk scadenza = new Obbligazione_scadenzarioBulk();
			obbligazione.addToObbligazione_scadenzarioColl(scadenza);
			scadenza.setDt_scadenza(obbligazione.getDt_registrazione());
			scadenza.setDs_scadenza(obbligazione.getDs_obbligazione());
			scadenza.setIm_scadenza(imResiduo);

			Obbligazione_scadenzarioBulk scadIniziale = new Obbligazione_scadenzarioBulk();
			scadIniziale.setIm_scadenza( scadenza.getIm_scadenza());
			scadenza.setScadenza_iniziale( scadIniziale);
			scadenza.setToBeCreated();
			
			generaDettagliScadenzaObbligazione( userContext, obbligazione, scadenza, obbligazione.isObbligazioneResiduo()?false:true);
		}
		return obbligazione;
	}

	public PrimaryKeyHashtable getOldRipartizioneCdrVoceLinea(UserContext userContext, ObbligazioneBulk obbligazione) throws it.cnr.jada.comp.ComponentException {
		BigDecimal totaleScad = new BigDecimal(0);
		Obbligazione_scad_voceBulk osv;
		Obbligazione_scadenzarioBulk os;
		Obbligazione_scad_voceBulk key = new Obbligazione_scad_voceBulk();
		PrimaryKeyHashtable prcImputazioneFinanziariaTable = new PrimaryKeyHashtable();	
		
		try {
			ObbligazioneHome obbligHome = (ObbligazioneHome)getTempHome(userContext, ObbligazioneBulk.class);
			Obbligazione_scadenzarioHome obbligScadHome = (Obbligazione_scadenzarioHome)getTempHome(userContext, Obbligazione_scadenzarioBulk.class);
	
			ObbligazioneBulk obbligDB = (ObbligazioneBulk)obbligHome.findObbligazione(obbligazione);
			/*se non esiste l'obbligazione ritorno un valore vuoto*/
			if (obbligDB == null)
				return prcImputazioneFinanziariaTable;

			obbligDB.setObbligazione_scadenzarioColl(new BulkList(obbligHome.findObbligazione_scadenzarioList(obbligDB)));
	
			for ( Iterator i = obbligDB.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
			{
				Obbligazione_scadenzarioBulk obbligScadDB = (Obbligazione_scadenzarioBulk) i.next();
				obbligScadDB.setObbligazione_scad_voceColl( new BulkList( obbligScadHome.findObbligazione_scad_voceList( userContext,obbligScadDB )));
			}							    	
			getHomeCache(userContext).fetchAll(userContext);
	
			for ( Iterator s = obbligDB.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); )
			{
				os = (Obbligazione_scadenzarioBulk) s.next();
				for ( Iterator d = os.getObbligazione_scad_voceColl().iterator(); d.hasNext(); )
				{
					osv = (Obbligazione_scad_voceBulk) d.next();
					// totale per Cdr e per scadenza				
					key = new Obbligazione_scad_voceBulk(osv.getCd_cds(),
														 osv.getCd_centro_responsabilita(),
														 osv.getCd_linea_attivita(),
														 osv.getCd_voce(),
														 osv.getEsercizio(),
														 osv.getEsercizio_originale(),
														 osv.getPg_obbligazione(),
														 new Long(1),
														 osv.getTi_appartenenza(),
														 osv.getTi_gestione());
	
					totaleScad = (BigDecimal) prcImputazioneFinanziariaTable.get( key );			
					if ( totaleScad == null || totaleScad.compareTo(new BigDecimal(0)) == 0)
						prcImputazioneFinanziariaTable.put( key, osv.getIm_voce());				
					else
					{
						totaleScad = totaleScad.add( osv.getIm_voce());
						prcImputazioneFinanziariaTable.put( key, totaleScad );
					}			
				}
			}
			return prcImputazioneFinanziariaTable;
		} catch (PersistencyException e) {
			throw handleException( e );
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
	 * @param scad l'istanza di Obbligazione_scadenzarioBulk il cui importo deve essere sdoppiato
	 * @param nuovoImportoScadenzaVecchia il valore del nuovo importo che la scadenza indicata dovrà assumere
	 * @return l'istanza di Obbligazione_scadenzarioBulk nuova creata in seguito allo sdoppiamento
	 */
	public IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico( UserContext userContext, IScadenzaDocumentoContabileBulk scad, BigDecimal nuovoImportoScadenzaVecchia) throws ComponentException 
	{
		DatiFinanziariScadenzeDTO dati = new DatiFinanziariScadenzeDTO();
		dati.setNuovoImportoScadenzaVecchia(nuovoImportoScadenzaVecchia);
		return sdoppiaScadenzaInAutomatico(userContext, scad, dati);
	}
	
	public IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico( UserContext userContext, IScadenzaDocumentoContabileBulk scad, DatiFinanziariScadenzeDTO dati) throws ComponentException 
	{
		String nuovaGae = dati.getCdLineaAttivita();
		String nuovaDescrizione = dati.getNuovaDescrizione();
		Timestamp nuovaScadenza = dati.getNuovaScadenza();
		String cdr = dati.getCdCentroResponsabilita();
		String voce = dati.getCdVoce();
		BigDecimal nuovoImportoScadenzaVecchia = dati.getNuovoImportoScadenzaVecchia();
		Obbligazione_scadenzarioBulk scadenzaVecchia = (Obbligazione_scadenzarioBulk)scad;
		if (  nuovoImportoScadenzaVecchia.compareTo( scad.getIm_scadenza()) == 0  )
			throw handleException( new ApplicationException( "Sdoppiamento in automatico non necessario!" ));			
		if (  nuovoImportoScadenzaVecchia.compareTo( new BigDecimal(0)) < 0  )
			throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));	
		if (  nuovoImportoScadenzaVecchia.compareTo( scad.getIm_scadenza()) == 1 )
			throw new ApplicationException("L'importo nuovo da assegnare alla scadenza dell'impegno deve essere inferiore al valore originario!");	

		try {
			java.math.BigDecimal importoAssociatoScadenzaVecchia = scadenzaVecchia.getIm_associato_doc_amm();
			java.math.BigDecimal vecchioImportoScadenzaVecchia = scadenzaVecchia.getIm_scadenza();
			java.math.BigDecimal importoScadenzaNuova = vecchioImportoScadenzaVecchia.subtract(nuovoImportoScadenzaVecchia);

			BigDecimal newImportoOsv = Utility.ZERO, totImporto = Utility.ZERO;
			ObbligazioneHome obbligazioneHome = (ObbligazioneHome) getHome( userContext, ObbligazioneBulk.class );
			ObbligazioneBulk obbligazione = (ObbligazioneBulk)obbligazioneHome.findByPrimaryKey(scadenzaVecchia.getObbligazione());
			obbligazione = (ObbligazioneBulk)inizializzaBulkPerModifica(userContext, (OggettoBulk)obbligazione);

			//cerco nell'obbligazione riletto la scadenza indicata
			for (Iterator s = obbligazione.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); ) {
				Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk)s.next();
				if (os.equalsByPrimaryKey(scadenzaVecchia)) {
					scadenzaVecchia = os;
					if (dati.getMantieniImportoAssociatoScadenza()){
						scadenzaVecchia.setIm_associato_doc_amm(importoAssociatoScadenzaVecchia);
					}
					break;
				}
			}

			if (scadenzaVecchia == null)
				throw new ApplicationException("Scadenza da sdoppiare non trovata nell'impegno indicato!");

			Obbligazione_scadenzarioBulk scadenzaNuova = new Obbligazione_scadenzarioBulk();
			scadenzaNuova.setDt_scadenza(nuovaScadenza!=null ? nuovaScadenza : scadenzaVecchia.getDt_scadenza());
			scadenzaNuova.setDs_scadenza(nuovaDescrizione!=null ? nuovaDescrizione : scadenzaVecchia.getDs_scadenza());
			obbligazione.addToObbligazione_scadenzarioColl(scadenzaNuova);
		
			// Rigenero i relativi dettagli	
			generaDettagliScadenzaObbligazione(userContext, obbligazione, scadenzaNuova, false);

			for (Iterator s = scadenzaVecchia.getObbligazione_scad_voceColl().iterator(); s.hasNext(); ) {
				Obbligazione_scad_voceBulk osvOld = (Obbligazione_scad_voceBulk)s.next();
				if (nuovaGae != null && cdr != null){
					if (osvOld.getCd_linea_attivita() != null && osvOld.getCd_linea_attivita().equals(nuovaGae) &&
							osvOld.getCd_centro_responsabilita() != null && osvOld.getCd_centro_responsabilita().equals(cdr)){
						aggiornaImportoScadVoceScadenzaNuova(importoScadenzaNuova, scadenzaNuova, osvOld);
						osvOld.setIm_voce(nuovoImportoScadenzaVecchia);
						osvOld.setToBeUpdated();
					}
				} else {
					newImportoOsv = nuovoImportoScadenzaVecchia.multiply(osvOld.getIm_voce()).divide(vecchioImportoScadenzaVecchia, 2, BigDecimal.ROUND_HALF_UP);
					
					aggiornaImportoScadVoceScadenzaNuova(newImportoOsv, scadenzaNuova, osvOld);
						
					osvOld.setIm_voce(newImportoOsv);
					osvOld.setToBeUpdated();
				}
			}

			//Quadro la sommatoria sulla vecchia scadenza
			for (Iterator s = scadenzaVecchia.getObbligazione_scad_voceColl().iterator(); s.hasNext(); )
				totImporto = totImporto.add(((Obbligazione_scad_voceBulk)s.next()).getIm_voce()); 
		
			if (totImporto.compareTo(nuovoImportoScadenzaVecchia)!=0) {
				//recupero il primo dettaglio e lo aggiorno per quadrare
				for (Iterator s = scadenzaVecchia.getObbligazione_scad_voceColl().iterator(); s.hasNext(); ) {
					Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk)s.next();
					if (osv.getIm_voce().add(nuovoImportoScadenzaVecchia.subtract(totImporto)).compareTo(Utility.ZERO)!=-1) {
						osv.setIm_voce(osv.getIm_voce().add(nuovoImportoScadenzaVecchia.subtract(totImporto)));
						break;
					}
				}
			}

			totImporto = Utility.ZERO;	

			//Quadro la sommatoria sulla nuova scadenza
			for (Iterator s = scadenzaNuova.getObbligazione_scad_voceColl().iterator(); s.hasNext(); )
				totImporto = totImporto.add(((Obbligazione_scad_voceBulk)s.next()).getIm_voce()); 
		
			if (totImporto.compareTo(importoScadenzaNuova)!=0) {
				//recupero il primo dettaglio e lo aggiorno per quadrare
				for (Iterator s = scadenzaNuova.getObbligazione_scad_voceColl().iterator(); s.hasNext(); ) {
					Obbligazione_scad_voceBulk osv = (Obbligazione_scad_voceBulk)s.next();
					if (osv.getIm_voce().add(importoScadenzaNuova.subtract(totImporto)).compareTo(Utility.ZERO)!=-1) {
						osv.setIm_voce(osv.getIm_voce().add(importoScadenzaNuova.subtract(totImporto)));
						break;
					}
				}
			}
		
			scadenzaVecchia.setIm_scadenza(nuovoImportoScadenzaVecchia);
			scadenzaVecchia.setToBeUpdated();
			scadenzaNuova.setIm_scadenza(importoScadenzaNuova);
			scadenzaNuova.setToBeCreated();
			obbligazione.setToBeUpdated();
			/**
			 * Viene posto il campo che verifica se il controllo della disponibilità
			 * è stato effettuato a TRUE, in quanto, l'obbligazione è in modifica
			 * e lo sdoppiamento delle righe non cambia il valore complessivo
			 * dell'obbligazione
			 */
			obbligazione.setCheckDisponibilitaContrattoEseguito(true);
			obbligazione.setCheckDisponibilitaIncaricoRepertorioEseguito(true);			
			modificaConBulk(userContext, obbligazione);
			return scadenzaNuova;
		} catch (PersistencyException e) {
			throw handleException( e );
		}
	}
	public IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomaticoLight( UserContext userContext, IScadenzaDocumentoContabileBulk scad, DatiFinanziariScadenzeDTO dati) throws ComponentException
	{
		BigDecimal nuovoImportoScadenzaVecchia = dati.getNuovoImportoScadenzaVecchia();
		Obbligazione_scadenzarioBulk scadenzaVecchia = (Obbligazione_scadenzarioBulk)scad;
		if (  nuovoImportoScadenzaVecchia.compareTo( scad.getIm_scadenza()) == 0  )
			throw handleException( new ApplicationException( "Sdoppiamento in automatico non necessario!" ));
		if (  nuovoImportoScadenzaVecchia.compareTo( new BigDecimal(0)) < 0  )
			throw handleException( new ApplicationException( "L'importo della scadenza deve essere maggiore di 0" ));
		if (  nuovoImportoScadenzaVecchia.compareTo( scad.getIm_scadenza()) == 1 )
			throw new ApplicationException("L'importo nuovo da assegnare alla scadenza dell'impegno deve essere inferiore al valore originario!");

		try {
			java.math.BigDecimal importoAssociatoScadenzaVecchia = scadenzaVecchia.getIm_associato_doc_amm();
			java.math.BigDecimal vecchioImportoScadenzaVecchia = scadenzaVecchia.getIm_scadenza();
			java.math.BigDecimal importoScadenzaNuova = vecchioImportoScadenzaVecchia.subtract(nuovoImportoScadenzaVecchia);

			ObbligazioneHome obbligazioneHome = (ObbligazioneHome) getHome(userContext, ObbligazioneBulk.class);
			ObbligazioneBulk obbligazione = obbligazioneHome.findObbligazione(scadenzaVecchia.getObbligazione());
			obbligazione.setObbligazione_scadenzarioColl(new BulkList(obbligazioneHome.findObbligazione_scadenzarioList(obbligazione)));

			//cerco nell'obbligazione riletto la scadenza indicata
			scadenzaVecchia = obbligazione.getObbligazione_scadenzarioColl().stream().filter(el->el.equalsByPrimaryKey(scad)).findFirst()
					.orElseThrow(()->new ApplicationException("Scadenza da sdoppiare non trovata nell'impegno indicato!"));

			Obbligazione_scadenzarioHome osHome = (Obbligazione_scadenzarioHome)getHome(userContext, Obbligazione_scadenzarioBulk.class);
			scadenzaVecchia.setObbligazione_scad_voceColl(new BulkList(osHome.findObbligazione_scad_voceList(userContext, scadenzaVecchia)));

			Obbligazione_scadenzarioBulk scadenzaNuova = new Obbligazione_scadenzarioBulk();
			obbligazione.addToObbligazione_scadenzarioColl(scadenzaNuova);
			scadenzaNuova.setDt_scadenza(scadenzaVecchia.getDt_scadenza());
			scadenzaNuova.setDs_scadenza(scadenzaVecchia.getDs_scadenza());
			scadenzaNuova.setIm_scadenza(importoScadenzaNuova);
			if (dati.getNuovoPgObbligazioneScadenzario()!=null)
				scadenzaNuova.setPg_obbligazione_scadenzario(dati.getNuovoPgObbligazioneScadenzario());
			else
				scadenzaNuova.setPg_obbligazione_scadenzario(obbligazione.getObbligazione_scadenzarioColl().stream().mapToLong(Obbligazione_scadenzarioBulk::getPg_obbligazione).max().getAsLong()+1);

			scadenzaNuova.setToBeCreated();
			makeBulkPersistent(userContext, scadenzaNuova);

			BigDecimal impDaRipartire = nuovoImportoScadenzaVecchia;
			for (Iterator s = scadenzaVecchia.getObbligazione_scad_voceColl().iterator(); s.hasNext(); ) {
				Obbligazione_scad_voceBulk osvOld = (Obbligazione_scad_voceBulk)s.next();

				BigDecimal importoOsvOld = BigDecimal.ZERO;
				if (!s.hasNext())
					importoOsvOld = impDaRipartire;
				else
					importoOsvOld = nuovoImportoScadenzaVecchia.multiply(osvOld.getIm_voce()).divide(vecchioImportoScadenzaVecchia, 2, BigDecimal.ROUND_HALF_UP);

				impDaRipartire = impDaRipartire.subtract(importoOsvOld);

				BigDecimal importoOsvNew = osvOld.getIm_voce().subtract(importoOsvOld);

				osvOld.setIm_voce(importoOsvOld);
				osvOld.setToBeUpdated();
				makeBulkPersistent(userContext, osvOld);

				Obbligazione_scad_voceBulk osvNew = new Obbligazione_scad_voceBulk();
				osvNew.setObbligazione_scadenzario(scadenzaNuova);
				osvNew.setLinea_attivita(osvOld.getLinea_attivita());
				osvNew.setTi_gestione(osvOld.getTi_gestione());
				osvNew.setTi_appartenenza(osvOld.getTi_appartenenza());
				osvNew.setCd_voce(osvOld.getCd_voce());
				osvNew.setIm_voce(importoOsvNew);
				osvNew.setToBeCreated();
				makeBulkPersistent(userContext, osvNew);
			};

			scadenzaVecchia.setIm_scadenza(nuovoImportoScadenzaVecchia);
			scadenzaVecchia.setToBeUpdated();
			makeBulkPersistent(userContext, scadenzaVecchia);

			return scadenzaNuova;
		} catch (PersistencyException e) {
			throw handleException( e );
		}
	}
	private void aggiornaImportoScadVoceScadenzaNuova(BigDecimal newImportoOsv, Obbligazione_scadenzarioBulk scadenzaNuova,
			Obbligazione_scad_voceBulk osvOld) {
		for (Iterator n = scadenzaNuova.getObbligazione_scad_voceColl().iterator(); n.hasNext(); ) {
			Obbligazione_scad_voceBulk osvNew = (Obbligazione_scad_voceBulk)n.next();
			if (osvNew.getCd_centro_responsabilita().equals(osvOld.getCd_centro_responsabilita()) &&
				osvNew.getCd_linea_attivita().equals(osvOld.getCd_linea_attivita())
	//		    && osvNew.getCd_voce().equals(osvOld.getCd_voce())
				)
				osvNew.setIm_voce(osvOld.getIm_voce().subtract(newImportoOsv));
		}
	}
	private void aggiornaObbligazioneModificaTemporanea(UserContext userContext,Obbligazione_modificaBulk obbligazioneModTemporanea) throws ComponentException {

		try {
			Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
			Long pg = numHome.getNextPg(userContext,
					obbligazioneModTemporanea.getEsercizio(), 
					obbligazioneModTemporanea.getCd_cds(), 
					obbligazioneModTemporanea.getCd_tipo_documento_cont(),
					obbligazioneModTemporanea.getUser()!=null?obbligazioneModTemporanea.getUser():((CNRUserContext)userContext).getUser());
			confirmObbligazioneModTemporanea(userContext, obbligazioneModTemporanea, pg);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(obbligazioneModTemporanea, e);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(obbligazioneModTemporanea, e);
		}	
	}
	private void confirmObbligazioneModTemporanea( 
		UserContext userContext,
		Obbligazione_modificaBulk obbligazioneModTemporanea,
		Long pg)
		throws IntrospectionException,PersistencyException, ComponentException {

		confirmObbligazioneModTemporanea(userContext,obbligazioneModTemporanea, pg, true);
	}
	private void confirmObbligazioneModTemporanea( 
		UserContext userContext,
		Obbligazione_modificaBulk obbligazioneModTemporanea,
		Long pg,
		boolean deleteTemp)
		throws IntrospectionException,PersistencyException, ComponentException {
		
		if (pg == null)
			throw new PersistencyException("Impossibile ottenere un progressivo definitivo per la modifica dell'impegno inserito!");

		Long tempPg = obbligazioneModTemporanea.getPg_modifica();
		Obbligazione_modificaHome omHome = (Obbligazione_modificaHome) getHome(userContext, Obbligazione_modificaBulk.class);
		SQLBuilder sql = omHome.createSQLBuilder();
		sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, obbligazioneModTemporanea.getCd_cds());
		sql.addSQLClause( "AND", "esercizio", sql.EQUALS, obbligazioneModTemporanea.getEsercizio());
		sql.addSQLClause( "AND", "pg_modifica", sql.EQUALS, tempPg);
		List result = omHome.fetchAll( sql );
		if (result.isEmpty())
			throw new ApplicationException("Modifica temporanea ad Impegno residuo proprio non trovato. Impossibile procedere.");
		if (result.size()>1)
			throw new ApplicationException("Modifica temporanea ad Impegno residuo proprio non individuata correttamente. Impossibile procedere.");

		Obbligazione_modificaBulk obbModDaCanc = (Obbligazione_modificaBulk) result.get(0);
		if (deleteTemp)
			deleteBulk(userContext,obbModDaCanc);

		Obbligazione_modificaKey omkey = (Obbligazione_modificaKey) obbligazioneModTemporanea.getKey();
		omkey.setPg_modifica(pg);
		obbligazioneModTemporanea.setPg_modifica(pg);
		insertBulk(userContext, obbligazioneModTemporanea);

		BulkList dett = obbligazioneModTemporanea.getObbligazione_mod_voceColl();
		
		for(Iterator it=dett.iterator();it.hasNext();) {
			Obbligazione_mod_voceBulk obbModVoceTemp = (Obbligazione_mod_voceBulk) it.next();

			Obbligazione_mod_voceHome omvHome = (Obbligazione_mod_voceHome) getHome(userContext, Obbligazione_mod_voceBulk.class);
			sql = omvHome.createSQLBuilder();
			sql.addSQLClause( "AND", "cd_cds", sql.EQUALS, obbModVoceTemp.getCd_cds());
			sql.addSQLClause( "AND", "esercizio", sql.EQUALS, obbModVoceTemp.getEsercizio());
			sql.addSQLClause( "AND", "pg_modifica", sql.EQUALS, tempPg);
			sql.addSQLClause( "AND", "ti_appartenenza", sql.EQUALS, obbModVoceTemp.getTi_appartenenza());
			sql.addSQLClause( "AND", "ti_gestione", sql.EQUALS, obbModVoceTemp.getTi_gestione());
			sql.addSQLClause( "AND", "cd_voce", sql.EQUALS, obbModVoceTemp.getCd_voce());
			sql.addSQLClause( "AND", "cd_centro_responsabilita", sql.EQUALS, obbModVoceTemp.getCd_centro_responsabilita());
			sql.addSQLClause( "AND", "cd_linea_attivita", sql.EQUALS, obbModVoceTemp.getCd_linea_attivita());
			result = omvHome.fetchAll( sql );
			if (result.isEmpty())
				throw new ApplicationException("Modifica temporanea ad Impegno residuo dettaglio proprio non trovato. Impossibile procedere.");
			if (result.size()>1)
				throw new ApplicationException("Modifica temporanea ad Impegno residuo dettaglio proprio non individuata correttamente. Impossibile procedere.");
			Obbligazione_mod_voceBulk obbModVoceDaCanc = (Obbligazione_mod_voceBulk) result.get(0);
			if (deleteTemp)
				deleteBulk(userContext, obbModVoceDaCanc);

			Obbligazione_mod_voceKey omvkey = (Obbligazione_mod_voceKey) obbModVoceTemp.getKey();
			omvkey.setPg_modifica(pg);
			obbModVoceTemp.setPg_modifica(pg);
			insertBulk(userContext, obbModVoceTemp);
		}
	}
	public SQLBuilder selectAssestatoSpeseByClause (UserContext userContext, ObbligazioneBulk obbligazione, V_assestatoBulk assestato, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, V_assestatoBulk.class).createSQLBuilder();
		sql.addClause( clause );
		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND, "esercizio_res", SQLBuilder.EQUALS, obbligazione.getEsercizio_originale());
		sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_SPESE);		
		sql.addClause(FindClause.AND, "ti_appartenenza", SQLBuilder.NOT_EQUALS, "C");
		sql.addClause(FindClause.AND, "cd_elemento_voce", SQLBuilder.EQUALS, obbligazione.getCd_elemento_voce());
		if (obbligazione.getCd_unita_organizzativa() != null){
			BulkHome bulkHome = getHome(userContext, V_struttura_organizzativaBulk.class);
			SQLBuilder sqlStruttura = bulkHome.createSQLBuilder();
			sqlStruttura.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			sqlStruttura.addSQLClause(FindClause.AND, "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, obbligazione.getCd_unita_organizzativa());
			sqlStruttura.addSQLClause(FindClause.AND, "CD_CENTRO_RESPONSABILITA", SQLBuilder.ISNOTNULL, true);			
			List<V_struttura_organizzativaBulk> strutture = bulkHome.fetchAll(sqlStruttura);
			sql.openParenthesis(FindClause.AND);
			for (V_struttura_organizzativaBulk v_struttura_organizzativaBulk : strutture) {
				sql.addClause(FindClause.OR, "cd_centro_responsabilita", SQLBuilder.EQUALS, v_struttura_organizzativaBulk.getCd_centro_responsabilita());
			}
			sql.closeParenthesis();
		}
		return sql;
	}
	
	public List<V_assestatoBulk> listaAssestatoSpese(UserContext userContext, ObbligazioneBulk obbligazione) throws ComponentException, PersistencyException{	
		SQLBuilder sql = selectAssestatoSpeseByClause(userContext, obbligazione, null, null);
		return getHome(userContext, V_assestatoBulk.class).fetchAll( sql );
	}

	private void validaCampi(UserContext uc, ObbligazioneBulk obbligazione) throws ComponentException {
	try {
		// controlli di validazione del campo MOTIVAZIONE
		Parametri_cnrBulk bulkCNR = (Parametri_cnrBulk)getHome(uc, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(uc)));		
		if (bulkCNR == null)
		  throw new ApplicationException("Parametri CNR non presenti per l'anno "+CNRUserContext.getEsercizio(uc));
		
		if (!obbligazione.isObbligazioneResiduo() &&
			bulkCNR.getFl_motivazione_su_imp() &&
			obbligazione.getMotivazione()==null) {

			if (obbligazione.isToBeUpdated()) {
				BigDecimal impObbl=obbligazione.getIm_obbligazione();
				ObbligazioneBulk oldObbl = (ObbligazioneBulk)getHome(uc, ObbligazioneBulk.class).findByPrimaryKey(obbligazione);
				BigDecimal oldImpObbl = oldObbl.getIm_obbligazione();
				if (Utility.nvl(impObbl.subtract(oldImpObbl)).compareTo(Utility.nvl(bulkCNR.getImporto_max_imp()))>=0 &&
					Utility.nvl(oldImpObbl).compareTo(Utility.nvl(impObbl))!=0)
					throw new ApplicationException("Attenzione: il campo MOTIVAZIONE è obbligatorio.");
			}
			else {
				throw new ApplicationException("Attenzione: il campo MOTIVAZIONE è obbligatorio.");
			}
		}
	}
	catch ( Exception e )
	{
		throw handleException( e )	;
	}	
	}
	/**
	 * 
	 * @param userContext
	 * @param obbligazione
	 * @param incarico
	 * @param clauses
	 * @return
	 * @throws ComponentException
	 * @throws it.cnr.jada.persistency.PersistencyException
	 */ 
	public SQLBuilder selectIncarico_repertorioByClause(UserContext userContext, ObbligazioneBulk obbligazione, Incarichi_repertorioBulk incarico, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext,Incarichi_repertorioBulk.class).createSQLBuilder();
		if (clauses != null) 
		  sql.addClause(clauses);

		SQLBuilder sqlUoExists = getHome(userContext,Ass_incarico_uoBulk.class).createSQLBuilder();
		sqlUoExists.addSQLJoin("ASS_INCARICO_UO.ESERCIZIO","INCARICHI_REPERTORIO.ESERCIZIO");
		sqlUoExists.addSQLJoin("ASS_INCARICO_UO.PG_REPERTORIO","INCARICHI_REPERTORIO.PG_REPERTORIO");
		sqlUoExists.addSQLClause("AND","ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, obbligazione.getCd_unita_organizzativa());

		sql.openParenthesis(FindClause.AND);
			sql.addClause(FindClause.OR, "cd_unita_organizzativa", SQLBuilder.EQUALS, obbligazione.getCd_unita_organizzativa());
			sql.addSQLExistsClause(FindClause.OR,sqlUoExists);
		sql.closeParenthesis();

		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_DEFINITIVO);
		sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_INVIATO);
		sql.closeParenthesis();
	
	 

	    sql.addTableToHeader("INCARICHI_REPERTORIO_ANNO");
	    sql.addSQLJoin("INCARICHI_REPERTORIO_ANNO.ESERCIZIO", "INCARICHI_REPERTORIO.ESERCIZIO");
	    sql.addSQLJoin("INCARICHI_REPERTORIO_ANNO.PG_REPERTORIO", "INCARICHI_REPERTORIO.PG_REPERTORIO");
	    sql.addSQLClause("AND", "INCARICHI_REPERTORIO_ANNO.ESERCIZIO_LIMITE", SQLBuilder.EQUALS, obbligazione.getEsercizio_originale());
	    sql.addTableToHeader("TERZO");
		sql.addSQLJoin("INCARICHI_REPERTORIO.CD_TERZO", SQLBuilder.EQUALS,"TERZO.CD_TERZO");
		sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);
		if((obbligazione.getCreditore() != null && obbligazione.getCreditore().getCd_terzo()!=null)){
			sql.openParenthesis("AND");
				sql.openParenthesis("AND");
					sql.addClause(FindClause.AND, "cd_terzo",SQLBuilder.EQUALS,obbligazione.getCreditore().getCd_terzo());
					AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
				sql.closeParenthesis();
			try {
				for (Iterator<Anagrafico_terzoBulk> i = anagraficoHome.findAssociatiStudio(obbligazione.getCreditore().getAnagrafico()).iterator(); i.hasNext();) {
					sql.openParenthesis("OR");
					   Anagrafico_terzoBulk associato = i.next();
				       sql.addSQLClause("OR", "INCARICHI_REPERTORIO.CD_TERZO",SQLBuilder.EQUALS, associato.getCd_terzo());
					sql.closeParenthesis();
				}
			} catch (IntrospectionException e) {
			}
		sql.closeParenthesis();
		}
		return sql;
	}
	/**
	 * 
	 * @param userContext
	 * @param obbligazione
	 * @param incarico
	 * @param clauses
	 * @return
	 * @throws ComponentException
	 * @throws it.cnr.jada.persistency.PersistencyException
	 */
	public void validaIncaricoRepertorio(UserContext userContext, ObbligazioneBulk obbligazione, Incarichi_repertorioBulk incarico, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = selectIncarico_repertorioByClause(userContext, obbligazione, incarico, clauses);
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS, incarico.getEsercizio());
		sql.addSQLClause("AND","PG_REPERTORIO",SQLBuilder.EQUALS, incarico.getPg_repertorio());
		Incarichi_repertorioHome home = (Incarichi_repertorioHome)getHome(userContext,Incarichi_repertorioBulk.class);
		it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
		if(!broker.next())
		  throw new ApplicationException("Incarico non valido!");
	}
	public void verificaCoperturaIncaricoRepertorio (UserContext aUC,ObbligazioneBulk obbligazione, int flag) throws ComponentException
	{
		//	Controllo che l'obbligazione non abbia sfondato l'incarico
		if (obbligazione.isCheckDisponibilitaIncaricoRepertorioEseguito())
		  return;
		if (obbligazione.getIncarico_repertorio() != null && obbligazione.getIncarico_repertorio().getPg_repertorio() != null){
		  try {	
			  Incarichi_repertorioHome incaricoHome = (Incarichi_repertorioHome)getHome(aUC, Incarichi_repertorioBulk.class);
			  SQLBuilder sql = incaricoHome.calcolaTotObbligazioni(aUC,obbligazione.getIncarico_repertorio());
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
			  if (flag == INSERIMENTO)
			    totale = totale.add(obbligazione.getIm_obbligazione());			  
			  if (totale != null ){
				  if (totale.compareTo(obbligazione.getIncarico_repertorio().getImporto_complessivo()) > 0){
					  throw handleException( new CheckDisponibilitaIncaricoRepertorioFailed("La somma degli impegni associati supera l'importo definito nell'incarico."));
				  }
			  }
		  } catch (IntrospectionException e1) {
			  throw new it.cnr.jada.comp.ComponentException(e1);
		  } catch (PersistencyException e1) {
			  throw new it.cnr.jada.comp.ComponentException(e1);
		  }
	  }		
	
	}
	public void verificaCoperturaIncaricoRepertorio (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
	{
		verificaCoperturaIncaricoRepertorio (aUC,obbligazione, MODIFICA);
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
	
	public void validaOrigineFontiPerAnnoResiduo(UserContext usercontext, Integer annoResiduo, String origineFonti, String pk) throws ComponentException {
		try{
			UtenteBulk utente = (UtenteBulk)(getHome(usercontext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(usercontext))));
			if (!utente.isSupervisore()) {
				Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( usercontext, CNRUserContext.getEsercizio(usercontext), CNRUserContext.getCd_cds(usercontext), pk, String.valueOf(annoResiduo));	
				if ( config == null  )
					config = createConfigurazioneCnrComponentSession().getConfigurazione( usercontext, CNRUserContext.getEsercizio(usercontext), null, pk, String.valueOf(annoResiduo));
				if (config != null){
					if (!origineFonti.equalsIgnoreCase(config.getVal01()) && !origineFonti.equalsIgnoreCase(config.getVal02())){
						throw new ApplicationException("Non è consentito emettere/modificare Impegni Residui Propri/Impropri\n"+
						   "su GAE di "+ NaturaBulk.tipo_naturaKeys.get(origineFonti)+" su fondi dell'esercizio residuo "+annoResiduo);	
					}
				}
			}
		}catch ( Exception e ){
			throw handleException( e )	;
		}	
		
	}
	
	protected void validaCreaModificaOrigineFonti(UserContext usercontext, ObbligazioneBulk obbligazione) throws ComponentException {
		try {
			getHomeCache(usercontext).fetchAll(usercontext);
			if (obbligazione.isObbligazioneResiduoImproprio() || obbligazione.isObbligazioneResiduo()){
				HashSet<String> origineFonti = new HashSet<String>();
				for (Iterator<Obbligazione_scadenzarioBulk> iterator = obbligazione.getObbligazione_scadenzarioColl().iterator(); iterator.hasNext();) {
					Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk = iterator.next();
					for (Iterator<Obbligazione_scad_voceBulk> iterator2 = obbligazione_scadenzarioBulk.getObbligazione_scad_voceColl().iterator(); iterator2.hasNext();) {
						Obbligazione_scad_voceBulk obbligazione_scad_voceBulk = iterator2.next();
						NaturaBulk natura = (NaturaBulk) getHome(usercontext, NaturaBulk.class).findByPrimaryKey(obbligazione_scad_voceBulk.getLinea_attivita().getNatura());
						origineFonti.add(natura.getTipo());
					}
				}
				for (Iterator<String> iterator = origineFonti.iterator(); iterator.hasNext();) {
					String fonte = iterator.next();
					String pk = null;
					if (obbligazione.isObbligazioneResiduoImproprio())
						pk = Configurazione_cnrBulk.PK_ANNI_RESIDUI_IM_RES_IMP;
					else if (obbligazione.isObbligazioneResiduo())
						pk = Configurazione_cnrBulk.PK_ANNI_RESIDUI_IM_RES_PRO;
					if (pk != null)
						validaOrigineFontiPerAnnoResiduo(usercontext, obbligazione.getEsercizio_originale(), fonte, pk);
				}
			}
		} catch (PersistencyException e) {
			handleException(e);
		}
		
	}	
	private Boolean verificaAssociato(UserContext usercontext, TerzoBulk terzo, ObbligazioneBulk obbligazione)throws ComponentException {
		try {
			AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(usercontext, AnagraficoBulk.class);
			for (Iterator<Anagrafico_terzoBulk> i = anagraficoHome.findAssociatiStudio(obbligazione.getCreditore().getAnagrafico()).iterator(); i.hasNext();) {
				Anagrafico_terzoBulk associato = i.next();
				if( associato.getCd_terzo().compareTo(terzo.getCd_terzo())==0)
					return true;
			}
		} catch (IntrospectionException e) {
			handleException(e);
		} catch (PersistencyException e) {
			handleException(e);
		}	
		return false;
	}
	private Boolean verificaVoceResidua(UserContext usercontext, ObbligazioneBulk obbligazione)throws ComponentException {
		try{
		UtenteBulk utente = (UtenteBulk)(getHome(usercontext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(usercontext))));
		  if (!utente.isSupervisore()) 
			if(obbligazione!=null && obbligazione.getElemento_voce()!=null && obbligazione.getElemento_voce().getFl_azzera_residui())
					return true;
		} catch (PersistencyException e) {
			handleException(e);
		}
		return false;
	}

	public SQLBuilder selectElemento_voce_nextByClause(UserContext userContext, ObbligazioneBulk obbligazione, Elemento_voceBulk elemento_voce, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException { 
		SQLBuilder sql = getHome(userContext,Elemento_voceBulk.class).createSQLBuilder();
		if (clauses != null) 
		  sql.addClause(clauses);

		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, obbligazione.getEsercizio()+1 );		
		sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
		sql.addClause(FindClause.AND, "fl_limite_spesa", SQLBuilder.EQUALS, Boolean.FALSE );
	    sql.addClause(FindClause.AND, "fl_solo_competenza", SQLBuilder.EQUALS, Boolean.FALSE );		 
		sql.addClause(FindClause.AND, "fl_azzera_residui", SQLBuilder.EQUALS, new Boolean( false) );
	    sql.addClause(FindClause.AND, "fl_voce_fondo", SQLBuilder.EQUALS, Boolean.FALSE );		 

		sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL", "CLASSVOCENEW");
		sql.addTableToHeader("CLASSIFICAZIONE_VOCI", "CLASSLIV1NEW");
		sql.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE", "CLASSVOCENEW.ID_CLASSIFICAZIONE");
		sql.addSQLJoin("CLASSVOCENEW.ID_LIV1", "CLASSLIV1NEW.ID_CLASSIFICAZIONE");
		sql.addSQLClause(FindClause.AND, "CLASSLIV1NEW.TI_CLASSIFICAZIONE", SQLBuilder.ISNOTNULL, null);

		if (obbligazione!=null && obbligazione.getElemento_voce()!=null && obbligazione.getElemento_voce().getV_classificazione_voci()!=null &&
				obbligazione.getElemento_voce().getV_classificazione_voci().getId_classificazione()!=null) {
			SQLBuilder sqlExists = getHome(userContext,Classificazione_vociBulk.class).createSQLBuilder();
			sqlExists.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL", "CLASSVOCEOLD");
			sqlExists.addSQLClause(FindClause.AND, "CLASSVOCEOLD.ID_CLASSIFICAZIONE", SQLBuilder.EQUALS, obbligazione.getElemento_voce().getV_classificazione_voci().getId_classificazione());
			sqlExists.addSQLJoin("CLASSVOCEOLD.ID_LIV1", "CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
			sqlExists.addSQLClause(FindClause.AND, "CLASSIFICAZIONE_VOCI.TI_CLASSIFICAZIONE", SQLBuilder.ISNOTNULL, null);
			sqlExists.addSQLJoin("CLASSIFICAZIONE_VOCI.TI_CLASSIFICAZIONE", "CLASSLIV1NEW.TI_CLASSIFICAZIONE" );
			sql.addSQLExistsClause(FindClause.AND, sqlExists);
		} else {
			sql.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
		}

		return sql; 
	}
	
	public boolean existAssElementoVoceNew(UserContext userContext, ObbligazioneBulk obbligazione) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			Ass_evold_evnewHome ass_evold_evnewHome = (Ass_evold_evnewHome) getHome( userContext, Ass_evold_evnewBulk.class);
			if (!ass_evold_evnewHome.findAssElementoVoceNewList(obbligazione.getElemento_voce()).isEmpty())
				return Boolean.TRUE;
		} catch (IntrospectionException e) {
			handleException(e);
		} catch (PersistencyException e) {
			handleException(e);
		}
		return Boolean.FALSE;
	}

	protected ObbligazioneBulk validaCreaModificaElementoVoceNext(UserContext userContext, ObbligazioneBulk obbligazione) throws RemoteException, ComponentException {
		try {
			if (obbligazione.getElemento_voce_next()!=null && obbligazione.getElemento_voce_next().getCd_elemento_voce()!=null){
				if (existAssElementoVoceNew(userContext, obbligazione)) {
					obbligazione.setElemento_voce_next(null);
					obbligazione.setToBeUpdated();
				} else {
					Elemento_voceHome home = (Elemento_voceHome)getHome(userContext,Elemento_voceBulk.class);
					SQLBuilder sql = selectElemento_voce_nextByClause(userContext, obbligazione, obbligazione.getElemento_voce(), new CompoundFindClause());
					sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.ESERCIZIO", SQLBuilder.EQUALS, obbligazione.getElemento_voce_next().getEsercizio());
					sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.TI_APPARTENENZA", SQLBuilder.EQUALS, obbligazione.getElemento_voce_next().getTi_appartenenza());
					sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.TI_GESTIONE", SQLBuilder.EQUALS, obbligazione.getElemento_voce_next().getTi_gestione());
					sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, obbligazione.getElemento_voce_next().getCd_elemento_voce());
					
					List listEv = home.fetchAll(sql);
					if (listEv.isEmpty())
						throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''impegno e quella di ribaltamento. Modificare la voce di ribaltamento!");
					
					if (obbligazione.getElemento_voce().getFl_recon().equals(Boolean.FALSE) && obbligazione.getElemento_voce_next().getFl_recon().equals(Boolean.TRUE) &&
					   (obbligazione.getIncarico_repertorio()==null || obbligazione.getIncarico_repertorio().getPg_repertorio()==null) &&
					   (obbligazione.getContratto()==null || obbligazione.getContratto().getPg_contratto()==null))
						throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''impegno che non richiede l'indicazione "+
								"del contratto/incarico e quella di ribaltamento che ne richiede l'inserimento. Modificare la voce di ribaltamento "+
								"o inserire i dati relativi al contratto/incarico!");
					
					try {
						verificaGestioneTrovato(userContext, obbligazione, obbligazione.getElemento_voce_next());
					} catch (ApplicationException e) {
						throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''impegno e quella di ribaltamento! " + e.getMessage());
					}
					verificaGestioneMissioni(userContext, obbligazione, obbligazione.getElemento_voce_next());
					if (!obbligazione.getElemento_voce().getFl_inv_beni_patr().equals(obbligazione.getElemento_voce_next().getFl_inv_beni_patr()))
						throw new ApplicationException("Attenzione! Non esiste congruenza tra la voce dell''impegno e quella di ribaltamento " +
									"nella gestione dell''inventario. Modificare la voce di ribaltamento!");

					if (obbligazione.getElemento_voce_next().getFl_limite_spesa())
						throw new ApplicationException("Attenzione! La voce di ribaltamento è soggetta ai limiti di spesa. Associazione non consentita!");
				}
			}
		} catch (PersistencyException e) {
			handleException(e);
		}
		return obbligazione;
	}

	private void validaObbligazionePluriennale(UserContext uc, ObbligazioneBulk bulk) throws ComponentException{


		for(Obbligazione_pluriennaleBulk obbPlur : bulk.getObbligazioniPluriennali()){
			if(obbPlur.getAnno() == null || obbPlur.getAnno() == 0){
				throw new ApplicationException("Impostare Anno Impegno Pluriennale");
			}
			if( !isAnnoPluriennaleSuccessivo(bulk.getEsercizio(),obbPlur.getAnno())){
				throw new ApplicationException("L'anno dell'Obbligazione Pluriennale deve essere successivo all'anno corrente");
			}
			if(isAnnoDuplicato(bulk)){
				throw new ApplicationException("Risulta presente più volte lo stesso anno per l'Obbligazione Pluriennale");
			}
			if(obbPlur.getImporto() == null){
				throw new ApplicationException("Impostare Importo dell'Obbligazione Pluriennale");
			}
		}
	}

	private boolean isAnnoDuplicato(ObbligazioneBulk bulk){
		if ( bulk.getObbligazioniPluriennali().stream()
				.filter(op -> Optional.ofNullable(op.getAnno()).isPresent())
				.collect(Collectors.groupingBy(Obbligazione_pluriennaleBulk::getAnno, Collectors.counting()))
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


	public SQLBuilder selectGaeDestinazioneFinaleByClause(UserContext userContext, ObbligazioneBulk obbligazione, WorkpackageBulk lineaAttivita, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		WorkpackageHome home = (WorkpackageHome)getHome(userContext, lineaAttivita,"V_LINEA_ATTIVITA_VALIDA");
		SQLBuilder sql = home.createSQLBuilder();

		sql.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.openParenthesis(FindClause.AND);
		sql.addSQLClause("OR", "V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE", SQLBuilder.EQUALS, WorkpackageBulk.TI_GESTIONE_SPESE);
		sql.addSQLClause("OR", "V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE", SQLBuilder.EQUALS, WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
		sql.closeParenthesis();

		sql.addClause(clauses);
		return sql;
	}
}
