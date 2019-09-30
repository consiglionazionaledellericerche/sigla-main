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

package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import java.io.Serializable;
import java.util.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su Ass_ev_evBulk e Ass_ev_voceepBulk
 */


public class PDCFinAssComponent extends it.cnr.jada.comp.CRUDComponent implements IPDCFinAssMgr, java.io.Serializable, Cloneable {


//@@<< CONSTRUCTORCST
	public  PDCFinAssComponent()
	{
//>>

//<< CONSTRUCTORCSTL
		/*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

	}
/**
 * Esegue eventuali controlli di consistenza dei dati in processo
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l'Ass_ev_evBulk  che deve essere controllato
 */	


private void checkAssData(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException 
{
	try
	{	
		if ( bulk instanceof Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk )
		{
         /* 
            Implementazione delle regole di associazione:
			Titolo '1' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '1' e '3' con capitolo 'CdS non Area'.
			Titolo '2' entrata Cds => Titolo '1' e '3' spesa CNR avente articolo(natura) '2' con capitolo 'Tutti i CdS'.
			Titolo '3' entrata Cds => Titolo '1' e '2' spesa CNR avente articolo(natura) '1','3',4' e '5' con capitolo 'Tutti i CdS'.
			Titolo '4' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '1' e '3' con capitolo 'CdS Area'.
            Titolo '4' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '4' e '5' con capitolo 'Tutti i CdS'.
            Titolo '5' entrata Cds => Titolo '5' spesa CNR con capitolo 'Tutti i CdS'.
     */
	        
			Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk ass = ( Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk ) bulk;
			Elemento_voceHome evHome = (Elemento_voceHome) getHomeCache(userContext).getHome( Elemento_voceBulk.class );
			Elemento_voceBulk aTitoloSpesa = (Elemento_voceBulk)evHome.findAndLock( ass.getElemento_voce());
			Elemento_voceBulk aCapitoloEntrata = (Elemento_voceBulk)evHome.findAndLock( ass.getElemento_voce_coll());
			Tipo_unita_organizzativaHome cdsHome = (Tipo_unita_organizzativaHome) getHomeCache(userContext).getHome( Tipo_unita_organizzativaBulk.class );
			Tipo_unita_organizzativaBulk aTCDS = (Tipo_unita_organizzativaBulk)cdsHome.findAndLock(ass.getTipo_unita());

			String cdTitoloEntrata = aCapitoloEntrata.estraiCodiceTitoloPerCapitoloEntrataCDS();
			
			if(

			 !(
			   (
			       aTitoloSpesa.getCd_proprio_elemento().equals("03")
			    && (ass.getCd_natura().equals("1") || ass.getCd_natura().equals("3"))
			    && !aTCDS.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA)
			    && cdTitoloEntrata.equals("01")
			   )
			 ||
			   (
			       (aTitoloSpesa.getCd_proprio_elemento().equals("01") || aTitoloSpesa.getCd_proprio_elemento().equals("03"))
			    && (ass.getCd_natura().equals("2"))
			    && cdTitoloEntrata.equals("02")
			   )
			 ||
			   (
			       (aTitoloSpesa.getCd_proprio_elemento().equals("01") || aTitoloSpesa.getCd_proprio_elemento().equals("02"))
			    && (!ass.getCd_natura().equals("2"))
			    && cdTitoloEntrata.equals("03")
			   )
			 ||
			   (
			       aTitoloSpesa.getCd_proprio_elemento().equals("03")
			    && (ass.getCd_natura().equals("1") || ass.getCd_natura().equals("3"))
			    && aTCDS.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA)
			    && cdTitoloEntrata.equals("04")
			   )
			 ||
			   (
			       aTitoloSpesa.getCd_proprio_elemento().equals("03")
			    && (ass.getCd_natura().equals("4") || ass.getCd_natura().equals("5"))
			    && cdTitoloEntrata.equals("04")
			   )
			 ||
			   (
			       aTitoloSpesa.getCd_proprio_elemento().equals("05")
			    && cdTitoloEntrata.equals("05")
			   )
			 )
			)
			 throw handleException( new it.cnr.jada.comp.ApplicationException("Associazione non possibile. Violazione regole di associazione!"));

		// Controllo di univocità dell'associazione
		
			Ass_cap_spesa_Cnr_natura_cap_entrata_CdsHome assHome = (Ass_cap_spesa_Cnr_natura_cap_entrata_CdsHome) getHomeCache(userContext).getHome( Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk.class );
			Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk assTemp = new Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk();
			assTemp.setEsercizio(ass.getEsercizio());
			assTemp.setElemento_voce(ass.getElemento_voce());
			assTemp.setTipo_unita(ass.getTipo_unita());
			assTemp.setCd_natura(ass.getCd_natura());
			if(!assHome.find(assTemp).isEmpty())
			 throw handleException( new it.cnr.jada.comp.ApplicationException("Spesa CNR già collegata ad altra entrata CDS o associazione già presente!"));

// Controllo eliminato per eplicita richiesta del cliente 04/01/2002
//			assTemp = new Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk();
//			assTemp.setEsercizio(ass.getEsercizio());
//			assTemp.setElemento_voce_coll(ass.getElemento_voce_coll());
//			if(!assHome.find(assTemp).isEmpty())
//			 throw handleException( new it.cnr.jada.comp.ApplicationException("Entrata CDS già collegata ad altra spesa CNR o associazione già presente!"));
	 }

	 if ( bulk instanceof Ass_titolo_Cnr_titolo_CdsBulk )
	 {
		// Controllo di univocità dell'associazione
		// In elemento voce c'è il titolo di spesa CNR
			
			Ass_titolo_Cnr_titolo_CdsBulk ass = (Ass_titolo_Cnr_titolo_CdsBulk ) bulk;

			Ass_titolo_Cnr_titolo_CdsHome assHome = (Ass_titolo_Cnr_titolo_CdsHome) getHomeCache(userContext).getHome( Ass_titolo_Cnr_titolo_CdsBulk.class );
			Ass_titolo_Cnr_titolo_CdsBulk assTemp = new Ass_titolo_Cnr_titolo_CdsBulk();

			assTemp.setEsercizio(ass.getEsercizio());
			assTemp.setElemento_voce(ass.getElemento_voce());
			if(!assHome.find(assTemp).isEmpty())
			 throw handleException( new it.cnr.jada.comp.ApplicationException("Titolo CNR già collegata ad altro titolo CDS o associazione già presente!"));

		// In elemento voce collegato c'è il titolo di spesa CDS

			assTemp = new Ass_titolo_Cnr_titolo_CdsBulk();

			assTemp.setEsercizio(ass.getEsercizio());
			assTemp.setElemento_voce_coll(ass.getElemento_voce_coll());
			if(!assHome.find(assTemp).isEmpty())
			 throw handleException( new it.cnr.jada.comp.ApplicationException("Titolo CDS già collegata ad altro titolo CNR o associazione già presente!"));
	 }		
	 if ( bulk instanceof Ass_cap_spesa_Cds_tipo_interventoBulk )
	 {
		// Controllo di univocità dell'associazione
		// Ogni voce del piano di spesa può essere collegata ad una e una sola tipologia di intervento
			
			Ass_cap_spesa_Cds_tipo_interventoBulk ass = (Ass_cap_spesa_Cds_tipo_interventoBulk) bulk;

			Ass_cap_spesa_Cds_tipo_interventoHome assHome = (Ass_cap_spesa_Cds_tipo_interventoHome) getHomeCache(userContext).getHome( Ass_cap_spesa_Cds_tipo_interventoBulk.class );
			Ass_cap_spesa_Cds_tipo_interventoBulk assTemp = new Ass_cap_spesa_Cds_tipo_interventoBulk();

			assTemp.setEsercizio(ass.getEsercizio());
			assTemp.setElemento_voce(ass.getElemento_voce());
			if(!assHome.find(assTemp).isEmpty())
			 throw handleException( new it.cnr.jada.comp.ApplicationException("Esiste già una tipologia di intervento collegata alla voce di spesa CDS!"));
	 }		
	}
	catch ( it.cnr.jada.persistency.FindException e )
	{
		throw handleException( new it.cnr.jada.comp.ApplicationException("Non è possibile inserire l'associazione"));
	}
	catch ( it.cnr.jada.persistency.sql.DuplicateKeyException d)
	{
		throw new ApplicationException( "Inserimento impossibile - chiave duplicata" );
	}			
	catch ( Exception e )
	{
		throw handleException(bulk,e);
	}		
}
/**
 * Esegue una operazione di creazione di una associazione fra un Elemento_voceBulk e un altro Elemento_voceBulk oppure
 * fra un Elemento_vocebulk e una Voce_epBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione di una Ass_ev_evBulk
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e un altro Elemento_voceBulk
 *       è stata generata
 * Post: Un Ass_ev_evBulk e' stato creato 
 *
 * Nome: Creazione di una associazione tra spesa CNR ed entrata CDS
 * Pre:   Le seguenti regole di associazione sono verificate:
 *			Titolo '1' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '1' e '3' con capitolo 'CdS non Area'.
 *			Titolo '2' entrata Cds => Titolo '1' e '3' spesa CNR avente articolo(natura) '2' con capitolo 'Tutti i CdS'.
 *			Titolo '3' entrata Cds => Titolo '1' e '2' spesa CNR avente articolo(natura) '1','3',4' e '5' con capitolo 'Tutti i CdS'.
 *			Titolo '4' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '1' e '3' con capitolo 'CdS Area'.
 *          Titolo '4' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '4' e '5' con capitolo 'Tutti i CdS'.
 *          Titolo '5' entrata Cds => Titolo '5' spesa CNR con capitolo 'Tutti i CdS'.
 *        Non esiste un'altra associazione della spesa CDS con altra entrata CNR
 * Post:
 *		  Una Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk è stata creata
 *
 * Nome: Creazione di una associazione tra titolo CNR e titolo CDS
 * Pre:   Le seguenti regole di associazione sono verificate:
 *          Non esiste un'altra associazione del titolo CDS con altro titolo CNR
 *          Non esiste un'altra associazione del titolo CNR con altro titolo CDS
 * Post:
 *		  Una Ass_titolo_Cnr_titolo_CdsBulk è stata creata
 *
 * Nome: Creazione di una Ass_ev_evBulk - errore
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e un altro Elemento_voceBulk
 *       è stata generata e uno dei due elementi non e' piu' presente o e' stato modificato da un altro utente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Creazione di una Ass_ev_voce_epBulk
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e una Voce_epBulk
 *       è stata generata
 * Post: Un Ass_ev_voce_epBulk e' stato creato 
 *
 * Nome: Creazione di una Ass_ev_voce_epBulk - errore
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e una Voce_epBulk
 *       è stata generata e uno dei due elementi non e' piu' presente o e' stato modificato da un altro utente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l'Ass_ev_evBulk o Ass_ev_voceepBulk  che deve essere creato
 * @return	l'Ass_ev_evBulk o Ass_ev_voceepBulk risultante dopo l'operazione di creazione.
 */	


public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException 
{
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Non è possibile creare nuove associazioni ad esercizio chiuso.");
	
	try
	{	
		if ( bulk instanceof Ass_ev_evBulk )
		{
            checkAssData(userContext, bulk);
			
			Ass_ev_evBulk ass = ( Ass_ev_evBulk ) bulk;
			Elemento_voceHome evHome = (Elemento_voceHome) getHomeCache(userContext).getHome( Elemento_voceBulk.class );
			evHome.findAndLock( ass.getElemento_voce());
			if ( ! (bulk instanceof Ass_cap_entrata_Cnr_naturaBulk) )
				evHome.findAndLock( ass.getElemento_voce_coll());
			super.makeBulkPersistent(userContext,bulk);
			return bulk;
		}
	
		else if ( bulk instanceof Ass_ev_voceepBulk )
		{
			Ass_ev_voceepBulk ass = ( Ass_ev_voceepBulk ) bulk;
			Elemento_voceHome evHome = (Elemento_voceHome) getHomeCache(userContext).getHome( Elemento_voceBulk.class );
			Voce_epHome voceepHome = (Voce_epHome) getHomeCache(userContext).getHome( Voce_epBulk.class );		
			evHome.findAndLock( ass.getElemento_voce());
			voceepHome.findAndLock( ass.getVoce_ep());
			super.makeBulkPersistent(userContext,bulk);
			return bulk;
		}
	}
	catch ( it.cnr.jada.persistency.FindException e )
	{
		throw handleException( new it.cnr.jada.comp.ApplicationException("Non è possibile inserire l'associazione"));
	}
	catch ( it.cnr.jada.persistency.sql.DuplicateKeyException d)
	{
		throw new ApplicationException( "Inserimento impossibile - chiave duplicata" );
	}			
	catch ( Exception e )
	{
		throw handleException(bulk,e);
	}		
		
	return null;

}
public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Non è possibile eliminare associazioni ad esercizio chiuso.");
	
	super.eliminaConBulk(userContext,bulk);
}
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	bulk = super.inizializzaBulkPerModifica(userContext,bulk);
	if (isEsercizioChiuso(userContext))
		bulk = asRO(bulk,"Non è possibile modificare associazioni ad esercizio chiuso.");
	return bulk;
}
protected boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiusoPerAlmenoUnCds(userContext);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Non è possibile modificare associazioni ad esercizio chiuso.");
	
	return super.modificaConBulk(userContext,bulk);
}
}
