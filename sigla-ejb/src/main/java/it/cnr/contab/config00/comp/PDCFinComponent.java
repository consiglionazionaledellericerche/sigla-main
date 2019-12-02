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

import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPBulk;
import it.cnr.contab.compensi00.docs.bulk.VCompensoSIPHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPHome;
import it.cnr.contab.missioni00.docs.bulk.VMissioneSIPBulk;
import it.cnr.contab.missioni00.docs.bulk.VMissioneSIPHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDDuplicateKeyException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Classe che ridefinisce alcune operazioni di CRUD su Elemento_voceBulk
 */
public class PDCFinComponent extends it.cnr.jada.comp.CRUDComponent implements IPDCFinMgr, java.io.Serializable, Cloneable, IPrintMgr {


    //@@<< CONSTRUCTORCST
    public PDCFinComponent() {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }

    /**
     * Esegue una operazione di ricerca di un Elemento_voceBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca di Elemento voce
     * Pre:  La richiesta di ricerca di un Elemento voce è stata generata
     * Post: La lista di Elemento_vocebulk che soddisfano i criteri di ricerca sono stati recuperati
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    clausole eventuali clausole di ricerca specificate dall'utente
     * @param    bulk l'Elemento_voceBulk che deve essere ricercato
     * @return la lista di Elemento_voceBulk risultante dopo l'operazione di ricerca.
     */

    public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clausole, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        Elemento_voceBulk ev = (Elemento_voceBulk) bulk;
        Elemento_voceHome evHome = (Elemento_voceHome) getHomeCache(userContext).getHome(ev.getClass());

        try {
            if (!((Parametri_cnrBulk) getHome(userContext, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_nuovo_pdg().booleanValue())
                ev.setCd_proprio_elemento(evHome.formatKey(ev.getCd_proprio_elemento(), ev.getTi_appartenenza(), ev.getTi_gestione(), ev.getTi_elemento_voce()));
            else
                ev.setCd_parte(null);
        } catch (PersistencyException e) {
            throw handleException(bulk, e);
        }
	
	 	/* DA FARE se si vuole permettere una ricerca impostando il titolo padre 
		if ( ev instanceof EV_cnr_spese_capitoloBulk )
			return leggiCnrSpeseCapitoloBulk ( (EV_cnr_spese_capitoloBulk) ev );
		*/

        return super.cerca(userContext, clausole, bulk);
    }

    /**
     * Esegue una operazione di creazione di un capitolo di spesa del CNR. Come elemento padre viene assegnata la categoria
     * con Codice = 2 sotto al Titolo specificato dall'utente
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    bulk capitolo di spesa del Cnr da creare
     * @return OggettoBulk capitolo di spesa del cnr con elemento_padre impostato
     */


    private OggettoBulk creaCnrSpeseCapitoloBulk(UserContext userContext, EV_cnr_spese_capitoloBulk bulk) throws it.cnr.jada.comp.ComponentException {

        Elemento_voceHome evHome = (Elemento_voceHome) getHome(userContext, Elemento_voceBulk.class);

        try {
            // cerca la categoria padre con codice = 2
            Elemento_voceBulk categoriaBulk = (Elemento_voceBulk) evHome.findAndLock(
                    new Elemento_voceKey(bulk.getTitolo_padre().getCd_elemento_voce().concat(".").concat(evHome.CD_CNR_SPESE_CATEGORIA_2),
                            bulk.getEsercizio(),
                            bulk.getTi_appartenenza(),
                            bulk.getTi_gestione()));

            bulk.setElemento_padre(categoriaBulk);


            if (bulk.getCd_proprio_elemento() == null || bulk.getCd_proprio_elemento().equals("")) {
                // genero il codice
                String codice = evHome.creaNuovoCodice(bulk);
                bulk.setCd_proprio_elemento(evHome.formatKey(codice, bulk.getTi_appartenenza(), bulk.getTi_gestione(), bulk.getTi_elemento_voce()));
            } else
                bulk.setCd_proprio_elemento(evHome.formatKey(bulk.getCd_proprio_elemento(), bulk.getTi_appartenenza(), bulk.getTi_gestione(), bulk.getTi_elemento_voce()));

            bulk.setCd_elemento_voce(bulk.getElemento_padre().getCd_elemento_voce().concat(".").concat(bulk.getCd_proprio_elemento()));

            insertBulk(userContext, bulk);

            return bulk;
        } catch (it.cnr.jada.persistency.FindException e) {
            throw handleException(new ApplicationException("Il titolo o la categoria non esistono"));
        } catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) {
            if (e.getPersistent() != bulk)
                throw handleException(bulk, e);
            try {
                throw handleException(new CRUDDuplicateKeyException("Errore di chiave duplicata", e, bulk, (OggettoBulk) getHome(userContext, bulk).findByPrimaryKey(bulk)));
            } catch (Throwable ex) {
                throw handleException(bulk, ex);
            }
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Esegue una operazione di creazione di un Elemento_voceBulk.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Creazione di Elemento_voce senza codice proprio
     * Pre:  La richiesta di creazione di un Elemento_voce senza aver specificato un codice proprio è stata generata
     * Post: Un Elemento_voceBulk stato creato con i dati inseriti dall'utente e il suo codice e' stato generato
     * automaticamente
     * <p>
     * Nome: Creazione di Elemento_voce con codice proprio
     * Pre:  La richiesta di creazione di un Elemento_voce con codice proprio specificato è stata generata
     * Post: Un Elemento_voceBulk stato creato con i dati inseriti dall'utente e il suo codice e' stato formattato
     * <p>
     * Nome: Creazione di Elemento_voce di tipo CNR Spese Capitolo
     * Pre:  La richiesta di creazione di un Elemento_voce di tipo Capitolo di Spesa CNR è stata generata
     * Post: Un Capitolo di Spesa del CNR viene creato con i dati inseriti dall'utente; come elemento padre di tale capitolo
     * viene ricercata la categoria con Codice = 2 (già creata in automatico) sotto al Titolo specificato dall'utente; .
     * <p>
     * Nome: Creazione di Elemento_voce di tipo CDS Spese Capitolo
     * Pre:  La richiesta di creazione di un Elemento_voce di tipo CDS Spese Capitolo è stata generata
     * Post: Un capitolo di spesa del CDS  e' stato creato e tutte le associazioni Ass_ev_funz_tipoCdsBulk
     * selezionate dall'utente sono state create
     * <p>
     * Nome: Errore di elemento_voce padre inesistente
     * Pre:  L'elemento voce specificato come padre dell'elemento voce da creare non esiste
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di chiave duplicata
     * Pre:  Esiste già un Elemento_voceBulk persistente che possiede la stessa chiave
     * primaria di quello specificato.
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    bulk l'Elemento_voceBulk che deve essere creato
     * @return il Elemento_voceBulk risultante dopo l'operazione di creazione.
     */


    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        // 05/09/2003
        // Aggiunto controllo sulla chiusura dell'esercizio
        if (isEsercizioChiuso(userContext))
            throw new ApplicationException("Non è possibile creare nuove voci ad esercizio chiuso.");

        Elemento_voceBulk evBulk = (Elemento_voceBulk) bulk;
        Elemento_voceHome subEvHome = (Elemento_voceHome) getHome(userContext, evBulk);
        try {

            if (evBulk.getFl_prelievo().booleanValue() && findElementoVocePrelievo(userContext) != null)
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: esiste già un elemento voce di prelievo per l'esercizio.");


            if (!((Parametri_cnrBulk) getHome(userContext, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_nuovo_pdg().booleanValue()) {
                if (evBulk instanceof EV_cnr_spese_capitoloBulk)
                    return creaCnrSpeseCapitoloBulk(userContext, (EV_cnr_spese_capitoloBulk) evBulk);

                if (evBulk instanceof EV_cds_spese_capitoloBulk && evBulk.getElemento_padre() == null || OggettoBulk.isNullOrEmpty(evBulk.getElemento_padre().getCd_elemento_voce()))
                    throw new it.cnr.jada.comp.ApplicationException("Inserire il codice titolo.");

                if (evBulk instanceof EV_cnr_entrate_capitoloBulk && evBulk.getElemento_padre() == null || OggettoBulk.isNullOrEmpty(evBulk.getElemento_padre().getCd_elemento_voce()))
                    throw new it.cnr.jada.comp.ApplicationException("Inserire il codice categoria.");

                //lock sul padre
                lockBulk(userContext, evBulk.getElemento_padre());

                //generazione e formattazione del codice
                if (evBulk.getCd_proprio_elemento() == null || evBulk.getCd_proprio_elemento().equals("")) {
                    String codice = subEvHome.creaNuovoCodice(evBulk);
                    evBulk.setCd_proprio_elemento(subEvHome.formatKey(codice, evBulk.getTi_appartenenza(), evBulk.getTi_gestione(), evBulk.getTi_elemento_voce()));
                } else
                    evBulk.setCd_proprio_elemento(subEvHome.formatKey(evBulk.getCd_proprio_elemento(), evBulk.getTi_appartenenza(), evBulk.getTi_gestione(), evBulk.getTi_elemento_voce()));

                evBulk.setCd_elemento_voce(evBulk.getElemento_padre().getCd_elemento_voce().concat(".").concat(evBulk.getCd_proprio_elemento()));

                if (evBulk instanceof EV_cds_spese_capitoloBulk)
                    inizializzaCdsSpeseCapitoloBulk((EV_cds_spese_capitoloBulk) evBulk);
            } else {
                if (evBulk.getCd_proprio_elemento() == null || evBulk.getCd_proprio_elemento().equals(""))
                    throw new it.cnr.jada.comp.ApplicationException("Inserire il campo Codice Proprio.");

                evBulk.setCd_elemento_voce(evBulk.getCd_proprio_elemento());
            }

            makeBulkPersistent(userContext, evBulk);

            return evBulk;
        } catch (it.cnr.jada.persistency.FindException e) {
            throw handleException(new ApplicationException("L'elemento voce padre non è definito", e));
        } catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) {
            if (e.getPersistent() != bulk)
                throw handleException(bulk, e);
            try {
                throw handleException(new CRUDDuplicateKeyException("Errore di chiave duplicata", e, bulk, (OggettoBulk) getHome(userContext, bulk).findByPrimaryKey(bulk)));
            } catch (Throwable ex) {
                throw handleException(bulk, ex);
            }
        } catch (Exception e) {
            throw handleException(bulk, e);
        }


    }

    /**
     * Esegue una operazione di eliminazione di Elemento_voceBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Cancellazione di un Elemento voce senza altri elementi voce a lui associati
     * Pre:  La richiesta di cancellazione di un Elemento voce che non ha altri elementi voce associati
     * e' stata generata
     * Post: L'Elemento_voceBulk e' stato cancellato
     * <p>
     * Nome: Cancellazione di un Elemento voce con altri elementi a lui associati
     * Pre:  La richiesta di cancellazione di un Elemento voce che ha elementi voce associati
     * e' stata generata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Cancellazione di un Elemento voce di tipo CNR Spese Capitolo
     * Pre:  La richiesta di cancellazione di un Elemento voce di tipo CNR Spese Capitolo e' stata generata
     * Post: L'Elemento_voceBulk e' stato cancellato e tutte le associazioni Ass_ev_funz_tipoCds a lui associato sono
     * state cancellate
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di Elemento_voceBulk che deve essere cancellata
     */

    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        // 05/09/2003
        // Aggiunto controllo sulla chiusura dell'esercizio
        if (isEsercizioChiuso(userContext))
            throw new ApplicationException("Non è possibile eliminare voci ad esercizio chiuso.");

        try {
            Elemento_voceBulk evBulk = (Elemento_voceBulk) bulk;
//		lockBulk( evBulk ); lo fa la makeBulkPersistent()

            if (esistonoReferenzeAElementoVoce(userContext, evBulk))
                throw new ApplicationException("Non è possibile cancellare l'elemento voce perchè utilizzato nelle associazioni");

            if (evBulk instanceof EV_cds_spese_capitoloBulk) {
                for (Iterator i = ((EV_cds_spese_capitoloBulk) evBulk).getAssociazioni().iterator(); i.hasNext(); )
                    ((Ass_ev_funz_tipocdsBulk) i.next()).setToBeDeleted();

            }

            makeBulkPersistent(userContext, evBulk);

/*		
		if ( evBulk instanceof EV_cnr_spese_capitoloBulk )
		{
			// cancella anche la sezione se non ha altri capitoli figlio
			Elemento_voceBulk sezione = new Elemento_voceBulk();
			sezione.setTi_appartenenza( Elemento_voceHome.APPARTENENZA_CNR );
			sezione.setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
			sezione.setTi_elemento_voce(Elemento_voceHome.TIPO_SEZIONE);
			sezione.setEsercizio( evBulk.getEsercizio());
			sezione.setCd_elemento_voce( evBulk.getElemento_padre().getCd_elemento_voce());
			sezione = (Elemento_voceBulk) getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey( sezione );
			if ( sezione == null )
				throw new ApplicationException( "Non esiste la sezione padre del capitolo" );				
			if (!esistonoReferenzeAElementoVoce( userContext,sezione ))
			{
				sezione.setToBeDeleted();
				makeBulkPersistent(userContext,sezione);
			}	
		}
*/

        } catch (it.cnr.jada.persistency.FindException e) {
            throw handleException(new ApplicationException("Elemento voce non trovato"));
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }

    }

    /**
     * verifica se l'elemnto voce e' stato utilizzato nelle associazioni, in tal caso ne impedisce la cancellazione
     */
    private boolean esistonoReferenzeAElementoVoce(UserContext userContext, Elemento_voceBulk evBulk) throws it.cnr.jada.comp.ComponentException {

        try {

            Ass_ev_evBulk find = new Ass_ev_evBulk();

            find.setEsercizio(evBulk.getEsercizio());
            find.setTi_appartenenza_coll(evBulk.getTi_appartenenza());
            find.setTi_gestione_coll(evBulk.getTi_gestione());
            find.setCd_elemento_voce_coll(evBulk.getCd_elemento_voce());


            List results = getHome(userContext, find.getClass()).find(find, false);
            if (results.isEmpty())
                return false;
            else
                return true;

        } catch (Throwable e) {
            throw handleException(e);
        }

    }

    /**
     * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
     * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
     * Post: L'elenco di funzioni e di tipologie di CDS viene caricato
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di Elemento_voceBulk che deve essere inizializzata
     * @return l' Elemento_voceBulk inizializzato
     */


    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            super.inizializzaBulkPerInserimento(userContext, bulk);
            if (bulk instanceof EV_cds_spese_capitoloBulk) {
                EV_cds_spese_capitoloBulk capitolo = (EV_cds_spese_capitoloBulk) bulk;
                capitolo.setFunzioni(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadFunzioni(capitolo));
                capitolo.setTipiCds(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadTipiCds((EV_cds_spese_capitoloBulk) bulk));
            }
            return bulk;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
     * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
     * Post: L' Elemento_voceBulk viene aggiornato con l'elenco delle istanze di Ass_ev_funz_tipoCdsBulk assegnate in
     * precedenza a questo Elemento voce
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di Elemento_voceBulk che deve essere inizializzata
     * @return l' Elemento_voceBulk inizializzato
     */

    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            bulk = super.inizializzaBulkPerModifica(userContext, bulk);


            if (bulk instanceof EV_cds_spese_capitoloBulk) {
                // carica funzioni e tipi cds
                EV_cds_spese_capitoloBulk capitolo = (EV_cds_spese_capitoloBulk) bulk;
                capitolo.setFunzioni(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadFunzioni(capitolo));
                capitolo.setTipiCds(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadTipiCds((EV_cds_spese_capitoloBulk) bulk));
                //carica le associazioni per i capitoli di spesa cnr
                Ass_ev_funz_tipocdsBulk ass = new Ass_ev_funz_tipocdsBulk();
                ass.setEsercizio(((EV_cds_spese_capitoloBulk) bulk).getEsercizio());
                ass.setCd_conto(((EV_cds_spese_capitoloBulk) bulk).getCd_elemento_voce());
                List result = getHome(userContext, Ass_ev_funz_tipocdsBulk.class).find(ass);
                for (Iterator i = result.iterator(); i.hasNext(); ) {
                    ass = (Ass_ev_funz_tipocdsBulk) i.next();
                    ((EV_cds_spese_capitoloBulk) bulk).getAssociazioni().put(ass.getMapKey(), ass);
                }

            }

            // 05/09/2003
            // Aggiunto controllo sulla chiusura dell'esercizio
            if (isEsercizioChiuso(userContext))
                bulk = asRO(bulk, "Non è possibile modificare voci ad esercizio chiuso.");

            return bulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
     * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
     * Post: L'elenco di funzioni e di tipologie di CDS viene caricato
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di Elemento_voceBulk che deve essere inizializzata
     * @return l' Elemento_voceBulk inizializzato
     */


    public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            super.inizializzaBulkPerRicerca(userContext, bulk);
            if (bulk instanceof EV_cds_spese_capitoloBulk) {
                EV_cds_spese_capitoloBulk capitolo = (EV_cds_spese_capitoloBulk) bulk;
                capitolo.setFunzioni(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadFunzioni(capitolo));
                capitolo.setTipiCds(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadTipiCds((EV_cds_spese_capitoloBulk) bulk));
            }
            return bulk;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue l'inizializzazione di una istanza di Elemento_voceBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione Elemento_voceBulk di tipo CDS spese capitolo
     * Pre:  L'inizializzazione di un Elemento_voceBulk per eventuale modifica e' stata generata
     * Post: L'elenco di funzioni e di tipologie di CDS viene caricato
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di Elemento_voceBulk che deve essere inizializzata
     * @return l' Elemento_voceBulk inizializzato
     */


    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            super.inizializzaBulkPerRicercaLibera(userContext, bulk);
            if (bulk instanceof EV_cds_spese_capitoloBulk) {
                EV_cds_spese_capitoloBulk capitolo = (EV_cds_spese_capitoloBulk) bulk;
                capitolo.setFunzioni(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadFunzioni(capitolo));
                capitolo.setTipiCds(((EV_cds_spese_capitoloHome) getHome(userContext, bulk.getClass())).loadTipiCds((EV_cds_spese_capitoloBulk) bulk));
            }
            return bulk;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Imposta il codice conto alle associazioni definite per un capitolo di spesa cds
     */

    private void inizializzaCdsSpeseCapitoloBulk(EV_cds_spese_capitoloBulk evBulk) throws it.cnr.jada.comp.ComponentException {
        // imposta il codice conto alle associazioni definite per capitolo spesa cds
        Ass_ev_funz_tipocdsBulk ass;
        for (Iterator i = evBulk.getAssociazioni().iterator(); i.hasNext(); ) {
            ass = (Ass_ev_funz_tipocdsBulk) i.next();
            ass.setCd_conto(evBulk.getCd_elemento_voce());
        }

    }

    protected boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return home.isEsercizioChiusoPerAlmenoUnCds(userContext);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Carica in una hashtable l'elenco di Tipologie di CDS  presenti nel database
     *
     * @return it.cnr.jada.util.OrderedHashtable
     */
    public it.cnr.jada.util.OrderedHashtable loadTipologieCdsKeys(UserContext userContext) throws ComponentException {

        it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome home = (it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome) getHome(userContext, it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk.class);
        return home.loadTipologiaCdsKeys();
    }

    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        // 05/09/2003
        // Aggiunto controllo sulla chiusura dell'esercizio
        if (isEsercizioChiuso(userContext))
            throw new ApplicationException("Non è possibile modificare voci ad esercizio chiuso.");
        try {
            Elemento_voceBulk evBulk = (Elemento_voceBulk) bulk;
            if (evBulk.getFl_prelievo().booleanValue() && findElementoVocePrelievo(userContext) != null)
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: esiste già un elemento voce di prelievo per l'esercizio.");
        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw handleException(pe);
        }
        return super.modificaConBulk(userContext, bulk);
    }

    /* Selezione dei soli record
     * presenti sulle tabelle delle classificazioni
     * definiti mastrino (FL_MASTRINO=Y) per
     * l'anno in scrivania
     * */
    public SQLBuilder selectClassificazione_entrateByClause(UserContext userContext,
                                                            OggettoBulk bulk,
                                                            Classificazione_entrateBulk classificazione,
                                                            CompoundFindClause clause)
            throws ComponentException, PersistencyException {
        if (clause == null)
            clause = classificazione.buildFindClauses(null);
        SQLBuilder sql = getHome(userContext, classificazione).createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        sql.addSQLClause("AND", "FL_MASTRINO", sql.EQUALS, "Y");
        if (clause != null)
            sql.addClause(clause);
        return sql;
    }

    public SQLBuilder selectClassificazione_speseByClause(UserContext userContext,
                                                          OggettoBulk bulk,
                                                          Classificazione_speseBulk classificazione,
                                                          CompoundFindClause clause)
            throws ComponentException, PersistencyException {
        if (clause == null)
            clause = classificazione.buildFindClauses(null);
        SQLBuilder sql = getHome(userContext, classificazione).createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        sql.addSQLClause("AND", "FL_MASTRINO", sql.EQUALS, "Y");
        if (clause != null)
            sql.addClause(clause);
        return sql;
    }

//^^@@

    /**
     * Normale
     * PreCondition:
     * Viene richiesto l'elenco delle Classificazioni Ufficiali
     * PostCondition:
     * Viene restituita una query sulla vista V_CLASSIFICAZIONE_VOCI che contiene solo le classificazioni
     * associabili al piano dei conti finanziario
     */
//^^@@
    public SQLBuilder selectV_classificazione_vociByClause(UserContext userContext,
                                                           Elemento_voceBulk elementoVoce,
                                                           V_classificazione_vociBulk classificazioneVoci,
                                                           CompoundFindClause clause)
            throws ComponentException, PersistencyException {
        SQLBuilder sql = getHome(userContext, classificazioneVoci).createSQLBuilder();
        sql.addClause(clause);
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
        sql.addSQLClause("AND", "TI_GESTIONE", sql.EQUALS, elementoVoce.getTi_gestione());
        sql.addClause("AND", "fl_mastrino", sql.EQUALS, Boolean.TRUE);
        if (clause != null)
            sql.addClause(clause);
        return sql;
    }

    /*
     *  Recupero della voce di bilancio del CNR
     *    PreCondition:
     *      E' stata generata la richiesta di recuperare la voce di bilancio del CNR
     *    PostCondition:
     *      Viene recuperata la voce di bilancio CNR in base alla linea di attività e la voce del CDS indicati
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param voceCds <code>Elemento_voceBulk</code> la voce del CDS
     * @param linea_attivita <code>WorkpackageBulk</code> la linea di attività del CDS
     *
     * @return voceCnr <code>String</code> il codice della voce CNR individuata
     */
    public String getVoceCnr(UserContext userContext, Elemento_voceBulk voceCds, WorkpackageBulk linea_attivita) throws it.cnr.jada.comp.ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(getConnection(userContext), "{? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                    + "CNRCTB075.getvocecnrfromvocecds(?,?,?,?,?,?,?)}", false, this.getClass());
            try {
                cs.registerOutParameter(1, java.sql.Types.VARCHAR);
                cs.setObject(2, voceCds.getEsercizio());
                cs.setString(3, linea_attivita.getCd_centro_responsabilita());
                cs.setString(4, linea_attivita.getCd_linea_attivita());
                cs.setObject(5, voceCds.getEsercizio());
                cs.setString(6, voceCds.getTi_gestione());
                cs.setString(7, voceCds.getTi_appartenenza());
                cs.setString(8, voceCds.getCd_elemento_voce());
                cs.execute();

                return cs.getString(1);
            } finally {
                cs.close();
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public java.util.List findListaVociWS(UserContext userContext, String uo, String tipo, String query, String dominio, String tipoRicerca) throws ComponentException {
        return findListaVociWS(userContext, uo, tipo, query, dominio, tipoRicerca, null);
    }

    public java.util.List findListaVociWS(UserContext userContext, String uo, String tipo, String query, String dominio, String tipoRicerca, String tipoFiltro) throws ComponentException {
        try {
            Unita_organizzativaHome uo_home = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
            Unita_organizzativaBulk u_org = (Unita_organizzativaBulk) uo_home.findByPrimaryKey(new Unita_organizzativaBulk(uo));
            SQLBuilder sql;
            if (tipo.compareTo(Elemento_voceHome.GESTIONE_SPESE) == 0) {
                Elemento_voceHome home = (Elemento_voceHome) getHome(userContext, Elemento_voceBulk.class);
                //sql=home.createSQLBuilder();
                sql = (SQLBuilder) super.select(userContext, null, new Elemento_voceBulk());
                sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
                sql.addSQLClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
                sql.addSQLClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS);
                sql.addSQLClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_CAPITOLO);
                sql.addSQLClause("AND", "cd_parte", SQLBuilder.EQUALS, Elemento_voceHome.PARTE_1);
                sql.addSQLClause("AND", "FL_INV_BENI_PATR", SQLBuilder.EQUALS, "N");
                if (u_org.getCd_cds() != null && u_org.getCd_tipo_unita() != null && !u_org.getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
                    sql.addSQLClause("AND", "fl_voce_sac", SQLBuilder.EQUALS, "N");
                if (dominio.equalsIgnoreCase("codice"))
                    sql.addSQLClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, query);
                else if (dominio.equalsIgnoreCase("descrizione")) {
                    sql.openParenthesis("AND");
                    for (StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements(); ) {
                        String queryDetail = stringtokenizer.nextToken();
                        if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva")) || tipoRicerca == null) {
                            if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
                                sql.addSQLClause("AND", "ds_elemento_voce", SQLBuilder.CONTAINS, queryDetail);
                            else {
                                sql.openParenthesis("AND");
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.CONTAINS, queryDetail);
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.CONTAINS, RemoveAccent.convert(queryDetail));
                                sql.closeParenthesis();
                            }
                        } else if (tipoRicerca.equalsIgnoreCase("puntuale")) {
                            if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))) {
                                sql.openParenthesis("AND");
                                sql.addSQLClause("AND", "UPPER(ds_elemento_voce)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.STARTSWITH, queryDetail + " ");
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.ENDSWITH, " " + queryDetail);
                                sql.closeParenthesis();
                            } else {
                                sql.openParenthesis("AND");
                                sql.openParenthesis("AND");
                                sql.addSQLClause("OR", "UPPER(ds_elemento_voce)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                                sql.addSQLClause("OR", "UPPER(ds_elemento_voce)", SQLBuilder.EQUALS, RemoveAccent.convert(queryDetail).toUpperCase());
                                sql.closeParenthesis();
                                sql.openParenthesis("OR");
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.STARTSWITH, queryDetail + " ");
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.STARTSWITH, RemoveAccent.convert(queryDetail) + " ");
                                sql.closeParenthesis();
                                sql.openParenthesis("OR");
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.ENDSWITH, " " + queryDetail);
                                sql.addSQLClause("OR", "ds_elemento_voce", SQLBuilder.ENDSWITH, " " + RemoveAccent.convert(queryDetail));
                                sql.closeParenthesis();
                                sql.closeParenthesis();
                            }
                        }
                    }
                    sql.closeParenthesis();
                    sql.addOrderBy("cd_elemento_voce,ds_elemento_voce");
                }
                if (tipoFiltro != null && tipoFiltro.equalsIgnoreCase("fattura")) {
                    VFatturaPassivaSIPHome homeFat = (VFatturaPassivaSIPHome) getHome(userContext, VFatturaPassivaSIPBulk.class, "VFATTURAPASSIVASIP_RID");
                    SQLBuilder sql2 = homeFat.createSQLBuilder();
                    sql2.setDistinctClause(true);
                    sql2.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "VFATTURAPASSIVASIP.ESERCIZIO");
                    sql2.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE", "VFATTURAPASSIVASIP.CD_ELEMENTO_VOCE");
                    sql2.addSQLClause("AND", "VFATTURAPASSIVASIP.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
                    sql.addSQLExistsClause("AND", sql2);
                } else if (tipoFiltro != null && tipoFiltro.equalsIgnoreCase("compenso")) {
                    VCompensoSIPHome homeComp = (VCompensoSIPHome) getHome(userContext, VCompensoSIPBulk.class, "VCOMPENSOSIP_RID");
                    SQLBuilder sql2 = homeComp.createSQLBuilder();
                    sql2.setDistinctClause(true);
                    sql2.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "VCOMPENSOSIP.ESERCIZIO");
                    sql2.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE", "VCOMPENSOSIP.CD_ELEMENTO_VOCE");
                    sql2.addSQLClause("AND", "VCOMPENSOSIP.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
                    sql.addSQLExistsClause("AND", sql2);
                } else if (tipoFiltro != null && tipoFiltro.equalsIgnoreCase("missione")) {
                    VMissioneSIPHome homeMis = (VMissioneSIPHome) getHome(userContext, VMissioneSIPBulk.class, "VMISSIONESIP_RID");
                    SQLBuilder sql2 = homeMis.createSQLBuilder();
                    sql2.setDistinctClause(true);
                    sql2.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "VMISSIONESIP.ESERCIZIO");
                    sql2.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE", "VMISSIONESIP.CD_ELEMENTO_VOCE");
                    sql2.addSQLClause("AND", "VMISSIONESIP.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
                    sql.addSQLExistsClause("AND", sql2);
                }
                return home.fetchAll(sql);
            } else {
                V_voce_f_partita_giroHome home = (V_voce_f_partita_giroHome) getHome(userContext, V_voce_f_partita_giroBulk.class);
                //sql=home.createSQLBuilder();
                sql = (SQLBuilder) super.select(userContext, null, new V_voce_f_partita_giroBulk());
                sql.addTableToHeader("ELEMENTO_VOCE");
                sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
                sql.openParenthesis("AND");
                sql.openParenthesis("AND");
                sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.cd_unita_organizzativa", sql.EQUALS, u_org.getCd_unita_organizzativa());
                sql.addSQLClause("OR", "V_VOCE_F_PARTITA_GIRO.cd_unita_organizzativa", sql.ISNULL, null);
                sql.closeParenthesis();
                sql.closeParenthesis();

                sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.FL_SOLO_RESIDUO", sql.EQUALS, "N");
                sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.ti_appartenenza", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);
                sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.ti_gestione", sql.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
                sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.ti_voce", sql.EQUALS, Elemento_voceHome.TIPO_ARTICOLO);
                sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.FL_PARTITA_GIRO", sql.EQUALS, "N");
                sql.addSQLJoin("V_VOCE_F_PARTITA_GIRO.ESERCIZIO", sql.EQUALS, "ELEMENTO_VOCE.ESERCIZIO");
                sql.addSQLJoin("V_VOCE_F_PARTITA_GIRO.ti_appartenenza", sql.EQUALS, "ELEMENTO_VOCE.ti_appartenenza");
                sql.addSQLJoin("V_VOCE_F_PARTITA_GIRO.ti_gestione", sql.EQUALS, "ELEMENTO_VOCE.ti_gestione");
                sql.addSQLJoin("V_VOCE_F_PARTITA_GIRO.CD_TITOLO_CAPITOLO", sql.EQUALS, "ELEMENTO_VOCE.CD_ELEMENTO_VOCE");
                sql.addSQLClause("AND", "FL_INV_BENI_PATR", SQLBuilder.EQUALS, "N");

                SQLBuilder sqlExist = ((Ass_ev_evHome) getHome(userContext, Ass_ev_evBulk.class)).createSQLBuilder();
                sqlExist.addSQLClause("AND", "CD_NATURA", SQLBuilder.ISNOTNULL, null);
                sqlExist.addSQLJoin("V_VOCE_F_PARTITA_GIRO.TI_GESTIONE", SQLBuilder.EQUALS, "ASS_EV_EV.TI_GESTIONE");
                sqlExist.addSQLJoin("V_VOCE_F_PARTITA_GIRO.TI_APPARTENENZA", SQLBuilder.EQUALS, "ASS_EV_EV.TI_APPARTENENZA");
                sqlExist.addSQLJoin("V_VOCE_F_PARTITA_GIRO.CD_TITOLO_CAPITOLO", SQLBuilder.EQUALS, "ASS_EV_EV.CD_ELEMENTO_VOCE");
                sqlExist.addSQLJoin("V_VOCE_F_PARTITA_GIRO.ESERCIZIO", SQLBuilder.EQUALS, "ASS_EV_EV.ESERCIZIO");
                sql.addSQLExistsClause("AND", sqlExist);

                if (u_org.getCd_cds() != null && u_org.getCd_tipo_unita() != null && !u_org.getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
                    sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.fl_voce_sac", SQLBuilder.EQUALS, "N");
                if (dominio.equalsIgnoreCase("codice"))
                    sql.addSQLClause("AND", "V_VOCE_F_PARTITA_GIRO.cd_voce", SQLBuilder.EQUALS, query);
                else if (dominio.equalsIgnoreCase("descrizione")) {
                    sql.openParenthesis("AND");
                    for (StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements(); ) {
                        String queryDetail = stringtokenizer.nextToken();
                        if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva")) || tipoRicerca == null) {
                            if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
                                sql.addSQLClause("AND", "ds_titolo_capitolo", SQLBuilder.CONTAINS, queryDetail);
                            else {
                                sql.openParenthesis("AND");
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.CONTAINS, queryDetail);
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.CONTAINS, RemoveAccent.convert(queryDetail));
                                sql.closeParenthesis();
                            }
                        } else if (tipoRicerca.equalsIgnoreCase("puntuale")) {
                            if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))) {
                                sql.openParenthesis("AND");
                                sql.addSQLClause("AND", "UPPER(ds_titolo_capitolo)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.STARTSWITH, queryDetail + " ");
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.ENDSWITH, " " + queryDetail);
                                sql.closeParenthesis();
                            } else {
                                sql.openParenthesis("AND");
                                sql.openParenthesis("AND");
                                sql.addSQLClause("OR", "UPPER(ds_titolo_capitolo)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                                sql.addSQLClause("OR", "UPPER(ds_titolo_capitolo)", SQLBuilder.EQUALS, RemoveAccent.convert(queryDetail).toUpperCase());
                                sql.closeParenthesis();
                                sql.openParenthesis("OR");
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.STARTSWITH, queryDetail + " ");
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.STARTSWITH, RemoveAccent.convert(queryDetail) + " ");
                                sql.closeParenthesis();
                                sql.openParenthesis("OR");
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.ENDSWITH, " " + queryDetail);
                                sql.addSQLClause("OR", "ds_titolo_capitolo", SQLBuilder.ENDSWITH, " " + RemoveAccent.convert(queryDetail));
                                sql.closeParenthesis();
                                sql.closeParenthesis();
                            }
                        }
                    }
                    sql.closeParenthesis();
                    sql.addOrderBy("cd_voce,ds_titolo_capitolo");
                }
                if (tipoFiltro != null && tipoFiltro.equalsIgnoreCase("fattura")) {
                    VFatturaPassivaSIPHome homeFat = (VFatturaPassivaSIPHome) getHome(userContext, VFatturaPassivaSIPBulk.class, "VFATTURAPASSIVASIP_RID");
                    SQLBuilder sql2 = homeFat.createSQLBuilder();
                    sql2.setDistinctClause(true);
                    sql2.addSQLJoin("V_VOCE_F_PARTITA_GIRO.ESERCIZIO", "VFATTURAPASSIVASIP.ESERCIZIO");
                    sql2.addSQLJoin("V_VOCE_F_PARTITA_GIRO.CD_VOCE", "VFATTURAPASSIVASIP.CD_ELEMENTO_VOCE");
                    sql2.addSQLClause("AND", "VFATTURAPASSIVASIP.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
                    sql.addSQLExistsClause("AND", sql2);
                } else if (tipoFiltro != null && tipoFiltro.equalsIgnoreCase("compenso")) {
                    VCompensoSIPHome homeComp = (VCompensoSIPHome) getHome(userContext, VCompensoSIPBulk.class, "VCOMPENSOSIP_RID");
                    SQLBuilder sql2 = homeComp.createSQLBuilder();
                    sql2.setDistinctClause(true);
                    sql2.addSQLJoin("V_VOCE_F_PARTITA_GIRO.ESERCIZIO", "VCOMPENSOSIP.ESERCIZIO");
                    sql2.addSQLJoin("V_VOCE_F_PARTITA_GIRO.CD_VOCE", "VCOMPENSOSIP.CD_ELEMENTO_VOCE");
                    sql2.addSQLClause("AND", "VCOMPENSOSIP.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
                    sql.addSQLExistsClause("AND", sql2);
                } else if (tipoFiltro != null && tipoFiltro.equalsIgnoreCase("missione")) {
                    VMissioneSIPHome homeMis = (VMissioneSIPHome) getHome(userContext, VMissioneSIPBulk.class, "VMISSIONESIP_RID");
                    SQLBuilder sql2 = homeMis.createSQLBuilder();
                    sql2.setDistinctClause(true);
                    sql2.addSQLJoin("V_VOCE_F_PARTITA_GIRO.ESERCIZIO", "VMISSIONESIP.ESERCIZIO");
                    sql2.addSQLJoin("V_VOCE_F_PARTITA_GIRO.CD_VOCE", "VMISSIONESIP.CD_ELEMENTO_VOCE");
                    sql2.addSQLClause("AND", "VMISSIONESIP.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo);
                    sql.addSQLExistsClause("AND", sql2);
                }
                return home.fetchAll(sql);
            }
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        if (bulk instanceof V_stampa_pdc_fin_ent_speBulk)
            inizializzaBulkPerStampa(userContext, (V_stampa_pdc_fin_ent_speBulk) bulk);

        return bulk;
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(UserContext userContext, V_stampa_pdc_fin_ent_speBulk stampa) throws ComponentException {
        stampa.setEsercizio(CNRUserContext.getEsercizio(userContext));
        stampa.setTi_gestione(stampa.TI_GESTIONE_ENTRATE);
        return stampa;
    }

    public OggettoBulk stampaConBulk(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        if (bulk instanceof V_stampa_pdc_fin_ent_speBulk)
            return stampaConBulk(aUC, (V_stampa_pdc_fin_ent_speBulk) bulk);
        return bulk;
    }

    public OggettoBulk stampaConBulk(UserContext userContext, V_stampa_pdc_fin_ent_speBulk stampa) throws ComponentException {
        if (stampa.getEsercizio() == null)
            throw new ApplicationException("Il campo ESERCIZIO e' obbligatorio");
        return stampa;
    }

    private Elemento_voceBulk findElementoVocePrelievo(UserContext userContext) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Elemento_voceHome home = (Elemento_voceHome) getHome(userContext, Elemento_voceBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
        sql.addSQLClause("AND", "FL_PRELIEVO", sql.EQUALS, "Y");
        it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
        if (!broker.next())
            return null;

        return (Elemento_voceBulk) broker.fetch(Elemento_voceBulk.class);
    }
}
