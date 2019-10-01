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
 * Created on Aug 29, 2005
 * @author rpagano
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.comp;

import java.io.Serializable;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.PrimaryKeyChangedException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

public class Classificazione_vociComponent extends CRUDComponent implements Cloneable,Serializable {

	public Classificazione_vociComponent()
	{
		super();
	}

	/**
	 * Verifica, per le Classificazioni associate a quella in questione, che:
	 *  
  	 * Nome: Struttura della classificazione non uguale a quella definita nei parametri
	 * Pre:  Viene invocato il metodo verificaLivelli e quest'ultimo genera una eccezione di validazione
	 * Post: Viene lasciata uscire l'eccezione senza salvare la classificazione
	 *
	 * Nome: Classificazione inserita di ultimo livello
	 * Pre:  Viene invocato il metodo validaMastrino
	 * Post: Viene restituita la Classificazione aggiornata 
	 **/
	protected void validateBulkListForPersistency(UserContext usercontext, BulkCollection bulkcollection) throws ComponentException, PersistencyException, OutdatedResourceException, BusyResourceException, PrimaryKeyChangedException {
		for (java.util.Iterator dett = bulkcollection.iterator();dett.hasNext();){
			Classificazione_vociBulk claAss = (Classificazione_vociBulk)dett.next();
			if (claAss.isToBeCreated()||claAss.isToBeUpdated()) {
				/*	
				 * 	Verifica che la struttura della classificazione rispetti quella definita nei parametri livelli
				 */ 
				verificaLivelli(usercontext, claAss);
				validaMastrino(usercontext, claAss);
				validaFlPianoRiparto(usercontext, claAss);
			}
		}			
		super.validateBulkListForPersistency(usercontext, bulkcollection);
	}

	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)	throws ComponentException {
		OggettoBulk cla = super.inizializzaBulkPerModifica(usercontext,oggettobulk);
		return caricaClassVociAssociate(usercontext, (Classificazione_vociBulk)cla, new CompoundFindClause());
	}

	/**
	  *  Struttura della classificazione non uguale a quella definita nei parametri
	  *	   PreCondition:
	  *		 Viene invocato il metodo verificaLivelli e quest'ultimo genera una eccezione di validazione
	  *    PostCondition:
	  *		 Viene lasciata uscire l'eccezione senza salvare la classificazione
	  *
	  *  Classificazione inserita di ultimo livello
	  *	   PreCondition:
	  *		 Viene invocato il metodo validaMastrino
	  *    PostCondition:
	  *		 Viene restituita la Classificazione aggiornata 
	  **/
	public OggettoBulk modificaConBulk(UserContext usercontext,	OggettoBulk oggettobulk) throws ComponentException {
		/*	
		 * 	Verifica che la struttura della classificazione rispetti quella definita nei parametri livelli
		 * 	e, in caso di cambio del codice sulla classificazione padre, aggiorna i codici anche sui figli 
		 */ 
		verificaLivelli(usercontext, (Classificazione_vociBulk)oggettobulk);
		validaMastrino(usercontext, (Classificazione_vociBulk)oggettobulk);
		validaFlPianoRiparto(usercontext, (Classificazione_vociBulk)oggettobulk);

		return super.modificaConBulk(usercontext, oggettobulk);
	}

	/**
	 *  Esegue una operazione di creazione di un Classificazione_vociBulk. 
	 *
	 *  Pre-post-conditions:
	 *
	 *  Nome: Struttura della classificazione non uguale a quella definita nei parametri
  	 *	Pre:  Viene invocato il metodo verificaLivelli e quest'ultimo genera una eccezione di validazione
	 *  Post: Viene lasciata uscire l'eccezione senza salvare la classificazione
	 *  
	 *  Nome: Classificazione inserita di ultimo livello
	 *	Pre:  Viene invocato il metodo validaMastrino
	 *  Post: Viene restituita la Classificazione aggiornata 
	 **/
	public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		/*	
		 * 	Verifica che la struttura della classificazione rispetti quella definita nei parametri livelli
		 * 	e, in caso di cambio del codice sulla classificazione padre, aggiorna i codici anche sui figli 
		 */ 
		verificaLivelli(usercontext, (Classificazione_vociBulk)oggettobulk);
		validaMastrino(usercontext, (Classificazione_vociBulk)oggettobulk);
		validaFlPianoRiparto(usercontext, (Classificazione_vociBulk)oggettobulk);
		return super.creaConBulk(usercontext, oggettobulk);
	}

	/**
	 * 	Ritorna l'oggetto <bulk> di tipo <Classificazione_vociBulk> riempito con le <Classificazione_vociBulk>
	 *  delle Classificazioni associate a quella <bulk> in questione
	 */
	public Classificazione_vociBulk caricaClassVociAssociate(UserContext usercontext, Classificazione_vociBulk bulk, CompoundFindClause compoundfindclause)	throws ComponentException {
		try {
			Classificazione_vociHome home = (Classificazione_vociHome)getHome(usercontext, bulk.getClass());
			bulk.setClassVociAssociate(new it.cnr.jada.bulk.BulkList(home.findClassVociAssociate(bulk, compoundfindclause)));			
			getHomeCache(usercontext).fetchAll(usercontext);
			return bulk;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch(Exception e) {
			throw handleException(e);
		}	
	}
	
	/**
	 * 	Ritorna l'oggetto <bulk> di tipo <Classificazione_vociBulk> riempito con i <Pdg_piano_ripartoBulk>
	 *  del Piano di riparto spese accentrate associati alla classificazione <bulk> in questione
	 */
	public Classificazione_vociBulk caricaPdgPianoRipartoSpese(UserContext usercontext, Classificazione_vociBulk bulk, CompoundFindClause compoundfindclause) throws ComponentException {
		try {
			Classificazione_vociHome home = (Classificazione_vociHome)getHome(usercontext, bulk.getClass());
			bulk.setPdgPianoRipartoSpese(new it.cnr.jada.bulk.BulkList(home.findPdgPianoRipartoSpese(bulk, compoundfindclause)));
			getHomeCache(usercontext).fetchAll(usercontext);
			return bulk;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch(Exception e) {
			throw handleException(e);
		}	
	}

	/**
	 * 	Ritorna il bulk dei parametri livelli <ParametriLivelliBulk> dell'Esercizio indicato <esercizio>.
	 */
	public Parametri_livelliBulk findParametriLivelli(UserContext userContext, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
		try
		{
			Parametri_livelliHome parametri_livelliHome = (Parametri_livelliHome) getHome(userContext, Parametri_livelliBulk.class );
			Parametri_livelliBulk parametri_livelliBulk = (Parametri_livelliBulk)parametri_livelliHome.findByPrimaryKey(new Parametri_livelliBulk(esercizio));
			if (parametri_livelliBulk==null)
				throw new ApplicationException("Parametri Livelli non definiti per l'esercizio " + esercizio + ".");
			return parametri_livelliBulk;
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
	}

	/**
	 * 	Ritorna la Descrizione del Livello a cui appartiene la classificazione <cla> indicata.
	 */
	public String getDsLivelloClassificazione(UserContext userContext, Classificazione_vociBulk cla) throws it.cnr.jada.comp.ComponentException {
		Parametri_livelliBulk parametri = findParametriLivelli(userContext, cla.getEsercizio());
		if (cla.getTi_gestione().equals(Elemento_voceHome.GESTIONE_ENTRATE))
			return parametri.getDs_livello_etr(cla.getLivelloMax().intValue());
		else
			return parametri.getDs_livello_spe(cla.getLivelloMax().intValue());
	}

	/**
	 * 	Ritorna la Descrizione del Livello a cui appartiene la classificazione <cla> indicata.
	 * 
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param esercizio <code>Integer</code> l'esercizio della classificazione da considerare
	 * @param tipo <code>String</code> il tipo Entrata/Spesa di classificazione da considerare
	 * @param livello <code>Integer</code> il livello della Classificazione da considerare
	 * 
	 * @return String <code>String</code> la descrizione della classificazione indicata.
	 *	 
	 **/
	public String getDsLivelloClassificazione(UserContext userContext, Integer esercizio, String tipo, Integer livello) throws it.cnr.jada.comp.ComponentException {
		Parametri_livelliBulk parametri = findParametriLivelli(userContext, esercizio);
		if (tipo.equals(Elemento_voceHome.GESTIONE_ENTRATE))
			return parametri.getDs_livello_etr(livello.intValue());
		else
			return parametri.getDs_livello_spe(livello.intValue());
	}

	/**
	  *  Controlla che la struttura della classificazione rispetti quella indicata in Parametri_livelli 
	  *  per l'esercizio della classificazione.
	  *
	  *  Numero Livelli della Classificazione superiore a quelli indicati in Parametri Livelli
	  *	   PreCondition:
	  *		 L'ultimo livello caricato è superiore al numero dei livelli indicati in parametri_livelli
	  *    PostCondition:
	  *		 Viene generata una ApplicationException con il messaggio "Non è possibile inserire classificazioni di <entrata/spesa> con più di <parametri_livelli.livelli_entrata><<parametri_livelli.livelli_spesa> livelli."
	  *  Lunghezza del livello non corrispondente con quella indicata nei parametri_livelli
	  *	   PreCondition:
	  *		 Il numero dei caratteri di un livello non corrispondente con quello indicato nei parametri
	  *    PostCondition:
	  *		 Viene generata una ApplicationException con il messaggio "Il codice del <NUMERO_LIVELLO> livello di <entrata/spesa> deve avere una lunghezza di <parametri_livelli.Lung_livello_etr> caratteri."
	  *  Codice della classificazione cambiato
	  *	   PreCondition:
	  *		 Il codice della classificazione è cambiato
	  *    PostCondition:
	  *		 Viene aggiornato il codice su tutti i livelli sottostanti
	  *  Default
	  *    PreCondition:
	  *      Nessun'altra precondizione è verificata
	  *    PostCondition:
	  *		 Esce senza alcuna eccezione
	 */	
	private void verificaLivelli(UserContext usercontext, Classificazione_vociBulk claNew) throws it.cnr.jada.comp.ComponentException {
		Parametri_livelliBulk parametri = findParametriLivelli(usercontext, claNew.getEsercizio());
		if (claNew.getTi_gestione().equals(Elemento_voceHome.GESTIONE_ENTRATE)) {
			for (int i=1; i<=Classificazione_vociHome.LIVELLO_MAX; i++){ 
				if (claNew.getCd_livello(i) != null) {
					if (parametri.getLivelli_entrata().compareTo(new Integer(i))==-1)
						throw new ApplicationException("Non è possibile inserire classificazioni di entrata con più di " + i + " livelli.");
					if (parametri.getLung_livello_etr(i).compareTo(new Integer(claNew.getCd_livello(i).length()))!=0)
						throw new ApplicationException("Il codice " + parametri.getDs_livello_etr(i) + " deve avere una lunghezza di " + parametri.getLung_livello_etr(i).toString() + " caratteri.");
				}
			}
		}
		else if (claNew.getTi_gestione().equals(Elemento_voceHome.GESTIONE_SPESE)) {
			for (int i=1; i <= Classificazione_vociHome.LIVELLO_MAX; i++){ 
				if (claNew.getCd_livello(i) != null) {
					if (parametri.getLivelli_spesa().compareTo(new Integer(i))==-1)
						throw new ApplicationException("Non è possibile inserire classificazioni di spesa con più di " + i + " livelli.");
					if (parametri.getLung_livello_spe(i).compareTo(new Integer(claNew.getCd_livello(i).length()))!=0)
						throw new ApplicationException("Il codice " + parametri.getDs_livello_spe(i) + " deve avere una lunghezza di " + parametri.getLung_livello_spe(i).toString() + " caratteri.");
				}
			}
		}		

		/*
		 * 	Se è cambiato il codice di uno dei livelli della classificazione, procedo ad aggiornare
		 * 	il valore anche sugli eventuali figli collegati alla classificazione in oggetto 
		 */
		if (claNew.isToBeUpdated()) {
			try {
				Classificazione_vociHome claHome = (Classificazione_vociHome)getHome(usercontext, Classificazione_vociBulk.class);
				Classificazione_vociBulk claOld = (Classificazione_vociBulk)claHome.findByPrimaryKey(claNew);
				//getHomeCache(usercontext).fetchAll(claHome);

				if (claOld.getLivelloMax().compareTo(claNew.getLivelloMax())==0 &&
					!claOld.getCd_livello(claOld.getLivelloMax().intValue()).equals(claNew.getCd_livello(claNew.getLivelloMax().intValue()))) {
					for (java.util.Iterator dett = claHome.findAllClassVociAssociate(claOld).iterator();dett.hasNext();){
						Classificazione_vociBulk claAss = (Classificazione_vociBulk)dett.next();
						claAss.setToBeUpdated();
						claAss.setCd_livello(claNew.getCd_livello(claNew.getLivelloMax().intValue()), claNew.getLivelloMax().intValue());
						updateBulk(usercontext, claAss); 
					}
				}
			} catch (PersistencyException e) {
				throw handleException(e);
			} catch (IntrospectionException e) {
				throw handleException(e);
			}
		}
	}

	/** 
	  *    PreCondition:
	  *      E' stato richiesto di verificare se la Classificazione è un mastrino e, in caso affermativo,
	  *      effettuare i controlli appositi 
	  *    PostCondition:
	  *		 Viene aggiornato il campo <FL_MASTRINO> e restituito il bulk aggiornato
	  *      
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>Classificazione_vociBulk</code> la classificazione da aggiornare
	  *
	  * @return Classificazione_vociBulk <code>Classificazione_vociBulk</code> il bulk aggiornato.
	**/ 
	private Classificazione_vociBulk validaMastrino(UserContext userContext, Classificazione_vociBulk bulk) throws ComponentException {
		Parametri_livelliBulk parametri = findParametriLivelli(userContext, bulk.getEsercizio());
		if ((bulk instanceof Classificazione_voci_etrBulk && bulk.getLivelloMax().compareTo(parametri.getLivelli_entrata())==0) ||
			(bulk instanceof Classificazione_voci_speBulk && bulk.getLivelloMax().compareTo(parametri.getLivelli_spesa())==0))
			bulk.setFl_mastrino(Boolean.TRUE);
		else
			bulk.setFl_mastrino(Boolean.FALSE);
		return bulk;
	}

	/** 
	  *    PreCondition:
	  *      E' stato richiesto di cambiare il valore del campo FL_PIANO_RIPARTO. 
	  *      Viene verificato se sono stati caricati dati nel Piano di riparto delle spese accentrate
	  *      per la Classificazione in oggetto. 
	  *    PostCondition:
	  *		 Viene restituito una ComponentException
	  *      
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>Classificazione_vociBulk</code> la classificazione da aggiornare
	  *
	  * @return Classificazione_vociBulk <code>Classificazione_vociBulk</code> il bulk aggiornato.
	**/ 
	private Classificazione_vociBulk validaFlPianoRiparto(UserContext userContext, Classificazione_vociBulk bulk) throws ComponentException {
		try {
			if (bulk instanceof Classificazione_voci_speBulk && bulk.isToBeUpdated() && 
			    bulk.getFl_piano_riparto().equals(Boolean.FALSE)) {
				Classificazione_vociHome home = (Classificazione_vociHome)getHome(userContext, bulk.getClass());
				BulkList pianoList = new BulkList(home.findPdgPianoRipartoSpese(bulk, null));
				if (!pianoList.isEmpty())
					throw new ApplicationException("Attenzione! Risultano caricati dati nel 'Piano di riparto delle Spese Accentrate' " + 
					"per la classificazione '"+ bulk.getCd_classificazione() + "'. Attivare il flag '" +
					bulk.getBulkInfo().getFieldProperty("fl_piano_riparto").getLabel() +"'.");
			}
			return bulk;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}	
	}

	/** 
	  *  Tutti i controlli superati.
	  *    PreCondition:
	  *      E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli 
	  *		di un Progetto.
	  *    PostCondition:
	  *		 Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli del progetto di riferimento.
	  *      
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il progetto di riferimento.
	  *
	  * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
	**/ 
	public RemoteIterator getChildren(UserContext userContext, OggettoBulk bulk) throws ComponentException {

		Classificazione_vociBulk ubi = (Classificazione_vociBulk)bulk;
		Classificazione_vociHome ubiHome = (Classificazione_vociHome)getHome(userContext,Classificazione_vociBulk.class);
		return iterator(userContext, ubiHome.selectChildrenFor(userContext,ubi), Classificazione_vociBulk.class, null);
	}

	/** 
	  *  Tutti i controlli superati.
	  *    PreCondition:
	  *      E' stata generata la richiesta di ricerca del Progetto padre del Progetto specificato negli argomenti.
	  *    PostCondition:
	  *		 Viene restituito l'oggetto ProgettoBulk che è il Progetto padre cercato.
	  *      
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il Progetto di riferimento.
	  *
	  * @return bulk <code>OggettoBulk</code> il Progetto cercato.
	**/ 
	public OggettoBulk getParent(UserContext userContext, OggettoBulk bulk) throws ComponentException{

		try{
			Classificazione_vociBulk ubi = (Classificazione_vociBulk)bulk;
			Classificazione_vociHome ubiHome = (Classificazione_vociHome)getHome(userContext,Classificazione_vociBulk.class);
			return ubiHome.getParent(ubi);
		
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(bulk,ex);
		}catch(it.cnr.jada.persistency.IntrospectionException ex){
			throw handleException(bulk, ex);
		}
	}        

	/**
	 * Controllo che il bulk specificato rappresenta una classificazione di ultimo livello.
	 *  PreCondition: Il livello della classificazione = LIVELLO_MAX o non ha figli
	 *  PpostCondition: Ritorna true
	 *
	 * @param context it.cnr.jada.UserContext
	 * @param bulk classificazione di cui stabilire se ultimo livello
	 * @return boolean true se ultimo livello
	 */
	public boolean isLeaf(UserContext context, OggettoBulk bulk) throws ComponentException{
		return ((((Classificazione_vociBulk)bulk).getLivelloMax().intValue() == Classificazione_vociHome.LIVELLO_MAX) ||
					!hasChildren(context, bulk) );
	}

	/**
	 * Recupera i figli della classificazione corrente
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Tutti i controlli superati
	 * Pre: Nessun errore
	 * Post: viene ritornato un RemoteIteretor sulla collezione dei figli del bulk specificato
	 * 
	 * @param	uc	lo UserContext che ha generato la richiesta
	 * @param	bulk	l'OggettoBulk di cui determinare il parent
	 * @return	il RemoteIterator sui figli
	 */
	private boolean hasChildren(UserContext context, OggettoBulk bulk) throws ComponentException{
		try {
			Classificazione_vociBulk claBulk = (Classificazione_vociBulk)bulk;
			Classificazione_vociHome claHome = (Classificazione_vociHome)getHome(context,Classificazione_vociBulk.class);
			if (claBulk.isToBeCreated() || claHome.findClassVociAssociate(claBulk).isEmpty()) 
				return false;
			else
				return true;
		} catch (IntrospectionException e) {
			throw handleException( e );
		} catch (PersistencyException e) {
			throw handleException( e );
		}
	}
}
