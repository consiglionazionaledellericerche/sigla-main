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

import java.io.Serializable;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

/**
 * Insert the type's description here.
 * Creation date: (08/03/2002 11.10.39)
 * @author: Roberto Fantino
 */
public class TipoTrattamentoComponent extends it.cnr.jada.comp.CRUDComponent implements ITipoTrattamentoMgr,Cloneable,Serializable{
/**
 * TipoTrattamentoComponent constructor comment.
 */
public TipoTrattamentoComponent() {
	super();
}
/**
 * Ricerca lista intervalli di validità Tipi Trattamento
 * PreCondition:
 *   Viene richiesta la lista degli intervalli di validità del tipo trattamento
 *        definiti con data inizio = a quella del tipo trattamento in processo
 * PostCondition:
 *   Viene restituita la lista dei Tipi trattamento o null nel caso il codice tipo trattamento
 *        in processo sia null
 *
*/
public java.util.List caricaIntervalli(UserContext userContext, Tipo_trattamentoBulk tratt) throws ComponentException{

	try{

		if (tratt.getCd_trattamento()==null)
			return null;

		Tipo_trattamentoHome home = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
		return home.caricaIntervalli(tratt);

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(tratt,ex);
	}
}
public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	super.validaCreaConBulk(userContext, bulk);
	
	Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bulk;
	validaTrattamento(userContext, tratt);

	return inserisciTrattamento(userContext, tratt);
}
/**
 * Cancellazione di un intervallo di validità con data anteriore alla data odierna
 * PreCondition:
 *   La data di inizio dell'intervallo è anteriore alla data odierna
 * PostCondition:
 *   La data di fine validità dell'intervallo viene posta uguale alla data corrente + 1
 *        Tutti gli intervalli successivi a quello in processo vengono eliminati fisicamente
 *
 * Cancellazione di un intervallo di validità con data uguale alla data odierna
 * PreCondition:
 *   La data di inizio dell'intervallo è anteriore alla data odierna
 * PostCondition:
 *        Tutti gli intervalli successivi a quello in processo vengono eliminati fisicamente
 */
public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	try{
		
		Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bulk;
		java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

		// cancellazione logica dell'oggetto attivo
		if (tratt.getDt_ini_validita().compareTo(dataOdierna)<=0){
			tratt.setDt_fin_validita(CompensoBulk.incrementaData(dataOdierna));
			updateBulk(userContext, tratt);
		}

		// cancellazione fisica di tutti gli intervalli successivi
		for(java.util.Iterator i = tratt.getIntervalli().iterator();i.hasNext();){
			Tipo_trattamentoBulk el = (Tipo_trattamentoBulk)i.next();
			if ( el.getDt_ini_validita().compareTo(dataOdierna)>0 && 
				 el.getDt_ini_validita().compareTo(tratt.getDt_ini_validita())>=0 )
				deleteBulk(userContext,el);
		}
	}catch(javax.ejb.EJBException ex){
		throw handleException(ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}

}
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)super.inizializzaBulkPerModifica(userContext,bulk);
	tratt.setIntervalli(caricaIntervalli(userContext, tratt));

	return tratt;
}
/**
 * Inserimento di un nuovo intervallo di validità di tipo trattamento (primo intervallo)
 * PreCondition:
 *   La lista degli intervalli di validità esistenti per il record in processo è vuota
 *        Il controllo di validità date è superato
 * PostCondition:
 *   Viene creato per il tipo trattamento il nuovo intervallo di validità (che è anche il primo)
 *
 *      Data di inizio validità nuovo intervallo non corretta
 * PreCondition:
 *   La lista degli intervalli di validità esistenti per il record in processo non è vuota
 *        Il controllo di validità date è superato
 *               La data di inizio validità dell'intervallo in processo <> dalla data di fine dell'ultimo intervallo + 1
 *        oppure la data di inizio validità dell'intervallo in processo <= della data odierna *        oppure la data di inizio validità dell'intervallo in processo > ultima data fine di intervallo esistente
 * PostCondition:
 *        Viene sollevata un'eccezione
 *
 * Inserimento di un nuovo intervallo di validità di record (intervallo n+1-esimo) futuro
 * PreCondition:
 *   La lista degli intervalli di validità esistenti per il record in processo non è vuota
 *        Il controllo di validità date è superato
 *        La data di inizio validità dell'intervallo in processo = alla data di fine dell'ultimo intervallo + 1
 *        La data di inizio validità dell'intervallo in processo > ultima data fine di intervallo esistente
 * PostCondition:
 *   Viene creato per il tipo trattamento il nuovo intervallo di validità
 *
 * Inserimento di un nuovo intervallo non valido (intervallo n+1-esimo) a spaccatura dell'intervallo corrente
 * PreCondition:
 *   La lista degli intervalli di validità esistenti per il record in processo non è vuota
 *        La data di inizio dell'intervallo nuovo è contenuta in un intervallo esistente 
 *        L'intervallo non è l'ultimo e la data di fine validità dell'intervallo in processo è maggiore della data di fine validità dell'intervallo corrente
 * PostCondition:
 *   Viene sollevata un'eccezione
 *
 * Inserimento di un nuovo intervallo di validità di record (intervallo n+1-esimo) a spaccatura del corrente
 * PreCondition:
 *   La lista degli intervalli di validità esistenti per il record in processo non è vuota
 *        La data di inizio dell'intervallo nuovo è contenuta in un intervallo esistente 
 *        La data di fine validità del nuovo intervallo è DATA_INFINITO
 * PostCondition:
 *   Viene aggiornata la data fine dell'intervallo corrente al giorno precedente alla data di inizio dell'intervallo nuovo
 *        Se la data di fine validità del nuovo intervallo è DATA_INFINITO, viene posta uguale alla data di fine dell'intervallo corrente
 *
 * Richiesta di Inserimento di un nuovo intervallo a copertura di intervalli temporali "occupati" parzialmente
 * PreCondition:
 *   La lista degli intervalli di validità esistenti per il record in processo non è vuota
 *        La data di inizio dell'intervallo nuovo non è contenuta in un intervallo esistente 
 *        La data di fine non è precedente ad ogni data di inizio validità di intervalli esistenti
 *      PostCondition:
 *   Viene sollevata un'eccezione
 *
 * Inserimento di un nuovo intervallo a copertura di intervalli temporali non "occupati"
 * PreCondition:
 *   La lista degli intervalli di validità esistenti per il record in processo non è vuota
 *        La data di inizio dell'intervallo nuovo non è contenuta in un intervallo esistente 
 *        La data di fine è precedente ad ogni data di inizio validità di intervalli esistenti
 * PostCondition:
 *   Viene inserito il nuovo intervallo
 *
 */
private Tipo_trattamentoBulk inserisciTrattamento(UserContext userContext, Tipo_trattamentoBulk tratt) throws ComponentException{

	try{

		// Carico tutti gli intervalli definiti per quel tipo Trattamento
		Tipo_trattamentoHome home = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
		java.util.List l = caricaIntervalli(userContext, tratt);

		if (l.isEmpty()){
			validaDate(userContext, tratt);
			insertBulk(userContext, tratt);
			return tratt;
		}
			
		java.sql.Timestamp dataOdierna = home.getServerDate();
		Tipo_trattamentoBulk ultimo = (Tipo_trattamentoBulk)l.get(l.size()-1);

		// caso 1: inserimenti futuri
		validaDate(userContext, tratt);
		if (tratt.getDt_ini_validita().after(ultimo.getDt_fin_validita())){
			if (tratt.getDt_ini_validita().after(dataOdierna)){
				if (tratt.getDt_ini_validita().equals(CompensoBulk.incrementaData(ultimo.getDt_fin_validita()))){
					insertBulk(userContext, tratt);
				}else
					throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida.\nGli intervalli devono essere contigui");
			}else
				throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita deve essere superiore alla data odierna");

		}else{

			// caso 2: inserimenti in intervallo corrente
			Tipo_trattamentoBulk corrente = home.findIntervallo(tratt);

			if (corrente != null){
				if (tratt.getDt_ini_validita().after(dataOdierna)){
					if (tratt.getDt_fin_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO))
						tratt.setDt_fin_validita(corrente.getDt_fin_validita());

					if (!isUltimoIntervallo(userContext, corrente)){
						if(tratt.getDt_fin_validita().after(corrente.getDt_fin_validita()))
							throw new it.cnr.jada.comp.ApplicationException("La Data Fine Validita non è valida");
					}
					corrente.setDt_fin_validita(CompensoBulk.decrementaData(tratt.getDt_ini_validita()));
					updateBulk(userContext, corrente);
					insertBulk(userContext, tratt);
				}else
					throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita deve essere superiore alla data odierna");
		
			}else{

				// caso 3: riempimento "buchi" in intervalli preesistenti
				for(int i = 0;i<l.size();i++){
					Tipo_trattamentoBulk el = (Tipo_trattamentoBulk)l.get(i);
					if (tratt.getDt_fin_validita().before(el.getDt_ini_validita())){
						insertBulk(userContext, tratt);
						return(tratt);
					}else if (tratt.getDt_ini_validita().before(el.getDt_fin_validita()))
						throw new it.cnr.jada.comp.ApplicationException("Intervallo non valido.\nSovrapposizione con intervalli preesistenti");
				}
			}
		}
			
		return(tratt);

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(tratt,ex);
	}
}
/**
  *    L'intervallo in processo è l'ultimo intervallo esistente
  *    PreCondition:
  *       La data di inizio validità dell'intervallo in processo >= della massima data di inizio di intervalli
  *    PostCondition:
  *       Viene ritornato TRUE
  *
  *    L'intervallo in processo non è l'ultimo intervallo esistente
  *    PreCondition:
  *       La data di inizio validità dell'intervallo in processo < della massima data di inizio di intervalli
  *    PostCondition:
  *       Viene ritornato FALSE
 */
public boolean isUltimoIntervallo(UserContext userContext, Tipo_trattamentoBulk tratt) throws ComponentException{

	try{
		Tipo_trattamentoHome home = (Tipo_trattamentoHome)getHome(userContext, tratt);

		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
		java.sql.Timestamp maxData = (java.sql.Timestamp)home.findMax(tratt, "dt_ini_validita", null, false);
		if (maxData == null)
			return(true);

		if (!tratt.getDt_ini_validita().before(maxData) )
			return (true);

		return (false);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}catch(it.cnr.jada.bulk.BusyResourceException ex){
		throw handleException(ex);
	}
}
/**
 * Modifica di intervallo ponendo la data fine nel futuro
 * PreCondition:
 *   Il controllo di validità date è superato
 * PostCondition:
 *   Viene aggiornato l'intervallo in processo
 *
 *      Modifica di intervallo ponendo la data fine < alla data odierna
 * PreCondition: La data di fine intervallo = alla data odierna
 * PostCondition:
 *        Viene sollevata un'eccezione
 *
 *      Modifica di intervallo ponendo la data fine nel passato
 * PreCondition: La data di fine intervallo = alla data odierna
 * PostCondition:
 *        La data di fine validità dell'intervallo corrente viene posta = data odierna
 *        Viene creato il nuovo intervallo con data di inizio validità = alla data odierna + 1
 *
 */
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	try{
		super.validaModificaConBulk(userContext, bulk);
		
		Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)bulk;
		Tipo_trattamentoHome home = (Tipo_trattamentoHome)getHome(userContext, tratt);
		java.sql.Timestamp dataOdierna = home.getServerDate();

		// Intervallo futuro
		if (tratt.getDt_ini_validita().after(dataOdierna)){
			validaDate(userContext, tratt);
			updateBulk(userContext, tratt);
		}else{

		// Intervallo corrente: spezzo in due
			Tipo_trattamentoBulk current = (Tipo_trattamentoBulk)home.findByPrimaryKey(tratt, true);
			if (!isUltimoIntervallo(userContext, current) && tratt.getDt_fin_validita()==null){
				tratt.setDt_fin_validita(current.getDt_fin_validita());
			}
			validaDate(userContext, tratt);

			if (!tratt.getDt_fin_validita().before(dataOdierna)){
				current.setDt_fin_validita(dataOdierna);
				updateBulk(userContext, current);
				tratt.setDt_ini_validita(CompensoBulk.incrementaData(dataOdierna));
				insertBulk(userContext, tratt);
			}else{
				throw new it.cnr.jada.comp.ApplicationException("La data fine validità deve essere superiore alla data odierna");
			}
		}

		return tratt;
	
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  *    Controlli di validazione del periodo di inizio/fine validita' 
  *    del nuovo record non superati
  *    PreCondition:
  *  validazione periodo inizio/fine non superata (data inizio validita del nuovo 
  *  record non definita o maggiore della data di fine validità)
  *    PostCondition:
  *      Viene sollevata un'eccezione
  *
  *    Impostazione data di fine periodo INFINITO
  *    PreCondition:
  *  la data di fine validità periodo non impostata
  *    PostCondition:
  *      La data di fine periodo viene impostata a DATA_INFINITO
  */
private void validaDate(UserContext userContext, Tipo_trattamentoBulk tratt) throws ComponentException{

	if (tratt.getDt_ini_validita()==null)
		throw new it.cnr.jada.comp.ApplicationException("Il campo Data Inizio Validità deve essere valorizzato");

	if (tratt.getDt_fin_validita()==null)
		tratt.setDt_fin_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);

//	if (tratt.getDt_fin_validita().before(CompensoBulk.getDataOdierna()))
//		throw new it.cnr.jada.comp.ApplicationException("La Data Fine Validità deve essere superiore alla Data Odierna");

	if (tratt.getDt_ini_validita().after(tratt.getDt_fin_validita()))
		throw new it.cnr.jada.comp.ApplicationException("La Data Fine Validità deve essere superiore alla Data Inizio Validità");
}
/**
  *    Controlli di validazione del record tipo trattamento non superati 
  *    PreCondition:
  *       Il codice o la descrizione del tipo trattamento non sono definiti
  *    PostCondition:
  *       Viene sollevata un'eccezione
 */
private void validaTrattamento(UserContext userContext, Tipo_trattamentoBulk tratt) throws ComponentException{

	// controllo su campo CD_CONTRIBUTO_RITENUTA
	if ( tratt.getCd_trattamento() == null )
		throw new it.cnr.jada.comp.ApplicationException( "Il campo Codice deve essere valorizzato !" );
	
	// controllo su campo DS_CONTRIBUTO_RITENUTA
	if ( tratt.getDs_ti_trattamento() == null )
		throw new it.cnr.jada.comp.ApplicationException( "Il campo Descrizione deve essere valorizzato !" );
}
}
