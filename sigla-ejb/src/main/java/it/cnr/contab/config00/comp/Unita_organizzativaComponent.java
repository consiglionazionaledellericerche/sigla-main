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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.util.Constants;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDDuplicateKeyException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su CdsBulk e Unita_organizzativaBulk
 */

public class Unita_organizzativaComponent extends it.cnr.jada.comp.CRUDComponent implements IUnita_organizzativaMgr, java.io.Serializable, Cloneable {
    //@@<< CONSTRUCTORCST
    public Unita_organizzativaComponent() {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
    /* Gestisce l'aggiornamento dell'attributo esercizio fine del Cds e di tutta la struttura organizzativa
     * da lui dipendente (Unita organizzative, Cdr e Linee di attività)
     *
     * Nome: Modifica dell'attributo esercizio fine
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cds e' stata generata e
     *       la validazione di tale attributo e' stata superata ( metodo 'verificaEsercizioFine')
     * Post: Sono stati aggiornati tutti gli esercizi fine di
     *       tutte le UO che dipendono dal CDS e hanno esercizio fine maggiore rispetto a quello del Cds; sono stati aggiornati
     *       in cascata anche tutti gli esercizi fine di tutti i Cdr che dipendono dalle UO che dipendono dal Cds e che hanno
     *       esercizio fine maggiore rispetto a quello del Cds; sono stati aggiornati in cascata anche tutti gli esercizi fine di
     *       tutte le linee di attività definite per i Cdr che dipendono dalle UO che dipendono dal Cds e che hanno esercizio
     *       fine maggiore rispetto a quello del Cds.
     *
     * Nome: Modifica dell'attributo esercizio fine - Errore
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cds e' stata generata e
     *       la validazione di tale attributo non e' stata superata ( metodo 'verificaEsercizioFine')
     * Post: Una Application Exception viene generata per segnalare all'utente l'impossibilità ad effettuare tale
     *		 modifica
     */

    private void aggiornaEsercizioFine(UserContext userContext, CdsBulk cds) throws it.cnr.jada.comp.ComponentException {
        try {
            //rileggo la Uo dal db per verificare se e' stato modificato l'esercizio fine
            CdsBulk cdsDB = (CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsKey(cds.getCd_unita_organizzativa()));
            if (cdsDB.getEsercizio_fine() == null && cds.getEsercizio_fine() == null)
                return;
            if (cdsDB.getEsercizio_fine() != null && cdsDB.getEsercizio_fine().compareTo(cds.getEsercizio_fine()) == 0)
                return;
            verificaEsercizioFine(userContext, cds);

            ((CdsHome) getHome(userContext, CdsBulk.class)).aggiornaEsercizioFinePerUoFigli(cds.getEsercizio_fine(), cds.getCd_unita_organizzativa());

        } catch (Throwable e) {
            throw handleException(cds, e);
        }

    }
    /* Gestisce l'aggiornamento dell'attributo esercizio fine dell'Unita organizzativa e di tutta la struttura organizzativa
     * da lei dipendente (Cdr e Linee di attività)
     *
     * Nome: Modifica dell'attributo esercizio fine
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un'Unita organizzativa e' stata generata e
     *       la validazione di tale attributo e' stata superata ( metodo 'verificaEsercizioFine')
     * Post: sono stati aggiornati tutti gli esercizi fine di tutti i Cdr che dipendono dall'Unita organizzativa e che hanno
     *       esercizio fine maggiore rispetto a quello dell'Unita' organizzativa; sono stati aggiornati in cascata anche tutti gli esercizi fine di
     *       tutte le linee di attività definite per i Cdr che dipendono dall'Unita' organizzativa e che hanno esercizio
     *       fine maggiore rispetto a quello del Cds.
     *
     * Nome: Modifica dell'attributo esercizio fine - Errore
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un'Unita organizzativa e' stata generata e
     *       la validazione di tale attributo non e' stata superata ( metodo 'verificaEsercizioFine')
     * Post: Una Application Exception viene generata per segnalare all'utente l'impossibilità ad effettuare tale
     *		 modifica
     */

    private void aggiornaEsercizioFine(UserContext userContext, Unita_organizzativaBulk uo) throws it.cnr.jada.comp.ComponentException {
        try {
            //rileggo la Uo dal db per verificare se e' stato modificato l'esercizio fine
            Unita_organizzativaBulk uoDB = (Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaKey(uo.getCd_unita_organizzativa()));
            if (uoDB.getEsercizio_fine() == null && uo.getEsercizio_fine() == null)
                return;
            if (uoDB.getEsercizio_fine() != null && uoDB.getEsercizio_fine().compareTo(uo.getEsercizio_fine()) == 0)
                return;
            verificaEsercizioFine(userContext, uo);
            ((Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class)).aggiornaEsercizioFinePerCdrFigli(uo.getEsercizio_fine(), uo.getCd_unita_organizzativa());

        } catch (Throwable e) {
            throw handleException(uo, e);
        }

    }

    /**
     * Esegue una operazione di ricerca di un CdsBulk o di una Unita_organizzativaBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Ricerca di un CdsBulk o di una Unita_organizzativaBulk
     * Pre:  La richiesta di ricerca di un CdsBulk o di una Unita_organizzativaBulk è stata generata
     * Post: La lista di CdsBulk o di Unita_organizzativaBulk che soddisfano i criteri di ricerca sono stati recuperati
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    clausole eventuali clausole di ricerca specificate dall'utente
     * @param    bulk un CdsBulk o di una Unita_organizzativaBulk che deve essere ricercato
     * @return la lista di CdsBulk o di Unita_organizzativaBulk risultante dopo l'operazione di ricerca.
     */

    public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clausole, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        if (bulk instanceof Unita_organizzativaBulk) {
            Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk) bulk;
            if (uoBulk.getCd_proprio_unita() != null && !uoBulk.getCd_proprio_unita().equals("") &&
                    uoBulk.getUnita_padre() != null && uoBulk.getUnita_padre().getCd_unita_organizzativa() != null && !uoBulk.getUnita_padre().getCd_unita_organizzativa().equals(""))
                uoBulk.setCd_unita_organizzativa(uoBulk.getUnita_padre().getCd_unita_organizzativa().concat(".").concat(uoBulk.getCd_proprio_unita()));
        } else if (bulk instanceof CdsBulk) {
            try {
                CdsBulk cdsBulk = (CdsBulk) bulk;
                cdsBulk.setCd_proprio_unita(getLunghezza_chiavi().formatCdsKey(userContext, cdsBulk.getCd_proprio_unita()));
            } catch (Exception e) {
                throw handleException(e);
            }
        }
        return super.cerca(userContext, clausole, bulk);
    }

    private void verificaAreeAssociate(UserContext userContext, Unita_organizzativaBulk uo) throws it.cnr.jada.comp.ComponentException {
        for (java.util.Iterator j = uo.getAssociazioneUoArea().iterator(); j.hasNext(); ) {
            Ass_uo_areaBulk assUoArea = (Ass_uo_areaBulk) j.next();
            checkPresidenteArea(userContext, assUoArea);

//		lock dell'area di ricerca collegata nel caso sia specificata
            if (assUoArea.getCds_area_ricerca() != null && assUoArea.getCds_area_ricerca().getCd_unita_organizzativa() != null) {
                try {
                    lockBulk(userContext, assUoArea.getCds_area_ricerca());
                } catch (it.cnr.jada.persistency.FindException e) {
                    throw new ApplicationException("Area ricerca inesistente");
                } catch (Exception e) {
                    throw new ApplicationException(e);
                }
            }
        }
    }

    /**
     * Controllo l'ammissibilità del flag presidente area sull'unità in processo
     * Solo una delle unità organizzative collegate all'area può essere presidente dell'area
     * Una UO del SAC non può essere presidente dell'AREA
     * <p>
     * Creation date: (05/04/2001 14:07:09)
     *
     * @param userContext contesto user
     * @param assUoArea Bulk UO in processo
     */

    private void checkPresidenteArea(UserContext userContext, Ass_uo_areaBulk assUoArea) throws it.cnr.jada.comp.ComponentException {
        try {

            if ((assUoArea.getCds_area_ricerca() == null) || (assUoArea.getCds_area_ricerca().getCd_unita_organizzativa() == null)) {
                // Se il fl_presidente_area è a true e l'area non è specificata ritorna  eccezione
                if (assUoArea.getFl_presidente_area() != null && assUoArea.getFl_presidente_area().equals(Boolean.TRUE))
                    throw new ApplicationException("Prima di impostare l'UO come Presidente dell'area, specificare l'area");
                else
                    // Area non specificata, esco
                    return;
            }

            if ((assUoArea.getCds_area_ricerca() != null) && (assUoArea.getCds_area_ricerca().getCd_unita_organizzativa() != null) && (assUoArea.getFl_presidente_area() == null || assUoArea.getFl_presidente_area().equals(Boolean.FALSE)))
                // Area specificata ma fl_presidente area no
                return;

            // Se l'UO appartiene al SAC, non può essere presidente di area
            if (assUoArea.getUnita_organizzativa().getCd_tipo_unita() != null && assUoArea.getUnita_organizzativa().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
                throw new ApplicationException("L'UO corrente appartiene al SAC e non può quindi essere presidente dell'area");

            Ass_uo_areaHome assUoAreaHome = (Ass_uo_areaHome) getHome(userContext, Ass_uo_areaBulk.class);

            // Se il numero attuale di UO collegate all'area è 0, devo aggiungere l'area come sottoarticolo
            Unita_organizzativaBulk aPresidente = null;

            if ((aPresidente = assUoAreaHome.getUOPresidenteArea(assUoArea)) != null) {
                lockBulk(userContext, aPresidente);
                if (!aPresidente.getCd_unita_organizzativa().equals(assUoArea.getUnita_organizzativa().getCd_unita_organizzativa()))
                    throw new ApplicationException("Esiste già un UO presidente dell'area " + assUoArea.getCd_area_ricerca() + " : " + aPresidente.getCd_unita_organizzativa() + " " + aPresidente.getDs_unita_organizzativa());
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Crea il CDR Responsabile dell'Unita Organizzativa. Tale Cdr puo' essere di I o II livello secondo la
     * seguente regola:
     * <ul>
     * <li>Se l'unita organizzativa da cui dipende e' una UO-CDS e il CDS non è di tipo AREA, il CDR è di I livello.</li>
     * <li>Se l'unita organizzativa da cui dipende e' una UO-CDS e il CDS è di tipo AREA, il CDR è di II livello.</li>
     * <li>Se l'unita organizzativa da cui dipende non e' una UO-CDS e il CDS è di tipo SAC e il flag rubrica e' true,
     * il CDR è di I livello.</li>
     * <li>Se l'unita organizzativa da cui dipende non e' una UO-CDS e il CDS è di tipo SAC e il flag rubrica e' false,
     * il CDR è di II livello e il Cdr di afferenza e' il Cdr di I livello dell'Unità organizativa di Riferimento.</li>
     * <li>Se l'unita organizzativa da cui dipende non e' una UO-CDS e il CDS non è di tipo SAC, il CDR è di II livello.</li>
     * </ul>
     *
     * @param userContext
     * @param uoBulk      unita organizzativa padre del Cdr da creare
     * @return CdrBulk Cdr responsabile dell'UO
     */
    private CdrBulk creaCdrBulk(UserContext userContext, Unita_organizzativaBulk uoBulk) throws it.cnr.jada.comp.ComponentException {
        try {

            CdrHome cdrHome = (CdrHome) getHome(userContext, CdrBulk.class);

            CdrBulk cdrBulk = new CdrBulk();
            cdrBulk.setUnita_padre(uoBulk);
            cdrBulk.setCd_unita_organizzativa(uoBulk.getCd_unita_organizzativa());

            cdrBulk.setEsercizio_inizio(uoBulk.getEsercizio_inizio());
            cdrBulk.setEsercizio_fine(uoBulk.getEsercizio_fine());

            cdrBulk.setDs_cdr(CdrHome.DEFAULT_DS_CDR);

// Se l'UO CDS non è di tipo AREA, il CDR responsabile dell'UO è di I livello.
// Se l'UO CDS è di tipo AREA, il CDR responsabile dell'UO è di II livello.
// Se l'UO non è UO CDS e appartiene al SAC e rubrica = true il CDR responsabile dell'UO è di I livello.
// Se l'UO non è UO CDS e appartiene al SAC e rubrica = false il CDR responsabile dell'UO è di II livello.
// Se l'UO non è UO CDS e non appartiene al SAC il CDR responsabile dell'UO è di II livello.

            if (uoBulk.isUoCds() & !uoBulk.isUoArea())
                cdrBulk.setLivello(CdrHome.CDR_PRIMO_LIVELLO);
            else if (uoBulk.isUoCds() & uoBulk.isUoArea())
                cdrBulk.setLivello(CdrHome.CDR_SECONDO_LIVELLO);
            else {

                CdsBulk cdsBulk = uoBulk.getUnita_padre();
                if (cdsBulk == null)
                    throw new ApplicationException("Codice CDS non presente");

                String tipoUO = cdsBulk.getCd_tipo_unita();
                if (tipoUO.equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_SAC)) {
                    if (uoBulk.getFl_rubrica().booleanValue())
                        cdrBulk.setLivello(CdrHome.CDR_PRIMO_LIVELLO);
                    else {
                        lockBulk(userContext, uoBulk.getUoDiRiferimento());
                        cdrBulk.setLivello(CdrHome.CDR_SECONDO_LIVELLO);
                        String keyAff = cdrHome.findCdCdrAfferenzaForSAC(uoBulk.getUoDiRiferimento().getCd_unita_organizzativa());
                        cdrBulk.setCd_cdr_afferenza(keyAff);
                    }
                } else // macro
                {
                    cdrBulk.setLivello(CdrHome.CDR_SECONDO_LIVELLO);
                    String keyAff = cdrHome.findCdCdrAfferenzaForMacro(cdsBulk.getCd_unita_organizzativa());
                    cdrBulk.setCd_cdr_afferenza(keyAff);
                }
            }
            cdrBulk.setCd_proprio_cdr(
                    getLunghezza_chiavi().formatCdrKey(userContext, "0", cdrBulk.getLivello()));
            cdrBulk.setCd_centro_responsabilita(uoBulk.getCd_unita_organizzativa().concat(".").concat(cdrBulk.getCd_proprio_cdr()));
//		cdrBulk.setCd_responsabile( uoBulk.getCd_responsabile());
            cdrBulk.setResponsabile(uoBulk.getResponsabile());
            cdrBulk.setUser(uoBulk.getUser());
            return cdrBulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    /**
     * Crea un Centro di Spesa, l'UO-CDS ad esso associato e il CDR Responsabile dell'UO;
     * nel caso di un Cds di tipo SAC viene fatta anche la verifica di unicità nella base dati
     *
     * @param bulk     UserContext che ha generato la richiesta
     * @param userContext unita organizzativa padre del Cdr da creare
     * @return CdrBulk Cdr responsabile dell'UO
     */

    private OggettoBulk creaCdsConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        // 05/09/2003
        // Aggiunto controllo sulla chiusura dell'esercizio
        if (isEsercizioChiuso(userContext))
            throw new ApplicationException("Non è possibile creare nuove unità organizzative ad esercizio chiuso.");

        try {


            CdsBulk cdsBulk = (CdsBulk) bulk;
            CdsHome cdsHome = (CdsHome) getHome(userContext, cdsBulk.getClass());

            if (cdsBulk.getArea_scientifica() == null || cdsBulk.getArea_scientifica().getCd_area_scientifica() == null)
                throw new ApplicationException("Area scientifica non definita");

            if (cdsBulk.getCd_proprio_unita() == null || cdsBulk.getCd_proprio_unita().equals("")) {
                String cod = cdsHome.creaNuovoCodice();
                cdsBulk.setCd_proprio_unita(getLunghezza_chiavi().formatCdsKey(userContext, cod));
            } else
                cdsBulk.setCd_proprio_unita(getLunghezza_chiavi().formatCdsKey(userContext, cdsBulk.getCd_proprio_unita()));
            cdsBulk.setCd_unita_organizzativa(cdsBulk.getCd_proprio_unita());

            if (cdsBulk.getResponsabile() != null && cdsBulk.getResponsabile().getCd_terzo() != null) {
                try {
                    lockBulk(userContext, cdsBulk.getResponsabile());
                } catch (it.cnr.jada.persistency.FindException e) {
                    throw handleException(new ApplicationException("Responsabile non esiste"));
                }
            }
            verificaEsercizioFine(userContext, cdsBulk);

//		getHomeCache().fetchAll();

            Unita_organizzativaBulk uoBulk = creaUoCdsBulk(userContext, cdsBulk);

            CdrBulk cdrBulk = creaCdrBulk(userContext, uoBulk);

            //insertBulk( userContext, cdsBulk );
            makeBulkPersistent(userContext, cdsBulk);
            insertBulk(userContext, uoBulk);
            insertBulk(userContext, cdrBulk);
            return cdsBulk;
        } catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) {
            if (e.getPersistent() instanceof Prc_copertura_obbligBulk)
                throw handleException(new ApplicationException("Errore di chiave duplicata per Percentuale di copertura impegno"));
            ;
            try {
                throw handleException(new CRUDDuplicateKeyException("Errore di chiave duplicata", e, bulk, (OggettoBulk) getHome(userContext, bulk).findByPrimaryKey(bulk)));
            } catch (Throwable ex) {
                throw handleException(bulk, ex);
            }
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }
    }

    /**
     * Esegue una operazione di creazione di un Cds o di una Unita' Organizzativa.
     * <p>
     * Pre-post-conditions:
     * ***********************************   CDS *************************************************
     * Nome: Creazione di Cds
     * Pre:  La richiesta di creazione di un Cds è stata generata
     * Post: Un Cds viene creato con i dati inseriti dall'utente, il suo codice se non specificato dall'utente
     * viene generato automaticamente; vengono create l'Unita' Organizzativa UO-CDS e il Cdr Responsabile dell'U0_CDS.
     * Al Cdr viene assegnato un livello in base alla regola descritta nel metodo 'creaCdrBulk'
     * <p>
     * <p>
     * Nome: Cds di tipo SAC gia' esistente
     * Pre:  La richiesta di creazione di un Cds con tiplogia SAC è stata generata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Responsabile inesistente
     * Pre:  Il Codice Terzo definito come responsabile del Cds non è presente
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di chiave duplicata
     * Pre:  Esiste già un CdsBulk persistente che possiede la stessa chiave
     * primaria di quello specificato.
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Esercizio Fine non valido
     * Pre:  Per un Cds di tipo SAC e' stato specificato un valore di esercizio fie diverso dal valore di default (2100)
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * <p>
     * ***********************************   UNITA ORGANIZZATIVA *************************************************
     * <p>
     * Nome: Creazione di Unità organizzativa dipendente da un CDS di tipo AREA
     * Pre:  La richiesta di creazione di una unità organizzativa che dipende da un CDS di tipo
     * Area di ricerca è stata generata
     * Post: Viene generate una ApplicationException con il messaggio "Non è possibile aggiungere una unità organizzativa ad un'area
     * di ricerca"
     * <p>
     * Nome: Creazione di Unita' organizzativa con specifica di area di ricerca collegata
     * Pre:  La richiesta di creazione di un' Unita' organizzativa è stata generata con specifica di codice CDS di area di ricerca collegata
     * Post:
     * Un' unita organizzativa viene creata con i dati inseriti dall'utente, il suo codice se non specificato dall'utente viene generato automaticamente; viene creato il Cdr Responsabile dell'U0 a cui viene assegnato un livello in base alla seguente regola:
     * - Se il CDS da cui dipende è di tipo SAC, il CDR è di I livello.
     * - Se il CDS da cui dipende non è di tipo SAC, il CDR è di II livello.
     * <p>
     * Viene aggiornato il piano dei conti finanziari per la parte 1 spese cnr categoria 1 come segue:
     * - Si determina se esiste il CDS area come SOTTOARTICOLO della linea del PDC Finanziario parte 1 spese cnr categoria 1 che contiene il CDS presidente dell'area collegata all'UO in processo come CAPITOLO. Nel caso tale SOTTOARTICOLO non sia presente, viene creato
     * <p>
     * Nome: Creazione di Unita' organizzativa collegata ad area di ricerca che è Presidente dell'Area
     * Pre: La richiesta di creazione di un' Unita' organizzativa collegata ad Area di ricerca e che sia Presidente dell'area è stata generata
     * Post: Un' unita organizzativa viene creata collegata ad un area come Presidente dell'area
     * <p>
     * Nome: Creazione di Unita' organizzativa
     * Pre:  La richiesta di creazione di un' Unita' organizzativa è stata generata
     * Post: Un' unita organizzativa viene creata con i dati inseriti dall'utente, il suo codice se non specificato
     * dall'utente viene generato automaticamente; viene creato il Cdr Responsabile dell'U0 a cui viene assegnato
     * un livello in base alla regola descritta nel metodo 'creaCdrBulk':
     * <p>
     * Nome: Errore di Responsabile inesistente
     * Pre:  Il Codice Terzo definito come responsabile dell' Unita' organizzativa non è presente
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Responsabile amministrativo inesistente
     * Pre:  Il Codice Terzo definito come responsabile amministrativo dell' Unita' organizzativa non è presente
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di chiave duplicata
     * Pre:  Esiste già un' Unita_organizzativaBulk persistente che possiede la stessa chiave
     * primaria di quella specificato.
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore Presidente dell'area già definito
     * Pre:  Esiste già di un presidente dell'area a cui è collegata l'UO
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Cds inesistente
     * Pre:  Il Cds specificato dall'utente non esiste
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Area Ricerca inesistente
     * Pre:  Il Cds specificato come Area di Ricerca per l'Unita' Organizzativa non esiste
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Una UO del SAC non può essere presidente dell'AREA
     * Pre:  L'UO appartiene al SAC ed è richiesto che sia Presidente dell'area a cui è collegata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Esercizio Fine non valido per unita padre
     * Pre:  L'esercio fine dell'UO è superiore all'esercizio fine del Cds da cui l'UO dipende
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk il CdsBulk o l'Unita_organizzativaBulk che deve essere creato
     * @return il CdsBulk o l'Unita_organizzativaBulk risultante dopo l'operazione di creazione.
     */

    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {

            if (bulk instanceof Unita_organizzativaBulk)
                return creaUOConBulk(userContext, bulk);
            else if (bulk instanceof CdsBulk)
                return creaCdsConBulk(userContext, bulk);
            return super.creaConBulk(userContext, bulk);
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }

    }

    /**
     * Nome: Gestisce l'aggiornamento del PDC finanziario
     * Pre: Viene richiesto l'aggiornamento del PDC finanziario  a causa di un cambiamento di specifica dell'area collegata all'UO.
     * Post: Viene invocato il metodo ORACLEcreaEsplSottArtArea() di CNRCTB001 per l'aggiornamento del PDC finanziario.
     * <p>
     * Nome: Si verifica errore nella stored procedure
     * Pre: Condizione di errore nella stored procedure ORACLEcreaEsplSottArtArea
     * Post: Viene rilanciata una ComponentExcpetion con messaggio dettagliato
     *
     * @param aUC      user CONTEXT
     * @param aEsercizio esercizio contabile
     * @param cdArea    codice area di ricerca
     */
    private void creaEsplSottArtArea(UserContext aUC, Integer aEsercizio, String cdArea) throws it.cnr.jada.comp.ComponentException {
        try {
            Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(aUC, aEsercizio);
            if (!parCnr.getFl_nuovo_pdg()) {
                LoggableStatement cs = new LoggableStatement(getConnection(aUC), "{call " + EJBCommonServices.getDefaultSchema()
                        + "CNRCTB001.creaEsplSottArtArea(?,?,?)}", false, this.getClass());
                try {
                    cs.setObject(1, aEsercizio);
                    cs.setString(2, cdArea);
                    cs.setObject(3, aUC.getUser());
                    cs.executeQuery();
                } catch (Throwable e) {
                    throw handleException(e);
                } finally {
                    cs.close();
                }
            }
        } catch (SQLException e) {
            throw handleException(e);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private void creaEsplSottArtArea(UserContext aUC, Integer aEsercizio, Unita_organizzativaBulk uo, Collection oldColl) throws it.cnr.jada.comp.ComponentException {
        boolean trovato = false;
        if ((oldColl == null || oldColl.isEmpty()) && (uo.getAssociazioneUoArea() == null || uo.getAssociazioneUoArea().isEmpty())) {
        } else if (oldColl == null || oldColl.isEmpty()) {
            for (java.util.Iterator j = uo.getAssociazioneUoArea().iterator(); j.hasNext(); ) {
                Ass_uo_areaBulk assUoAreaNew = (Ass_uo_areaBulk) j.next();
                creaEsplSottArtArea(aUC, aEsercizio, assUoAreaNew.getCd_area_ricerca());
            }
        } else if (uo.getAssociazioneUoArea() == null || uo.getAssociazioneUoArea().isEmpty()) {
            for (java.util.Iterator old = oldColl.iterator(); old.hasNext(); ) {
                Ass_uo_areaBulk assUoAreaOld = (Ass_uo_areaBulk) old.next();
                creaEsplSottArtArea(aUC, aEsercizio, assUoAreaOld.getCd_area_ricerca());
            }
        } else {
            for (java.util.Iterator j = uo.getAssociazioneUoArea().iterator(); j.hasNext(); ) {
                Ass_uo_areaBulk assUoAreaNew = (Ass_uo_areaBulk) j.next();
                trovato = false;
                for (java.util.Iterator old = oldColl.iterator(); !trovato && old.hasNext(); ) {
                    Ass_uo_areaBulk assUoAreaOld = (Ass_uo_areaBulk) old.next();
                    if (assUoAreaOld.getCd_area_ricerca().equals(assUoAreaNew.getCd_area_ricerca()))
                        trovato = true;
                }
                if (!trovato)
                    creaEsplSottArtArea(aUC, aEsercizio, assUoAreaNew.getCd_area_ricerca());
            }
            for (java.util.Iterator old = oldColl.iterator(); old.hasNext(); ) {
                Ass_uo_areaBulk assUoAreaOld = (Ass_uo_areaBulk) old.next();
                trovato = false;
                for (java.util.Iterator j = uo.getAssociazioneUoArea().iterator(); !trovato && j.hasNext(); ) {
                    Ass_uo_areaBulk assUoAreaNew = (Ass_uo_areaBulk) j.next();
                    if (assUoAreaNew.getCd_area_ricerca().equals(assUoAreaOld.getCd_area_ricerca()))
                        trovato = true;
                }
                if (!trovato)
                    creaEsplSottArtArea(aUC, aEsercizio, assUoAreaOld.getCd_area_ricerca());
            }
        }
    }

    /**
     * Crea l'UO CDS per un CDS specificato
     *
     * @param userContext
     * @param cdsBulk     il Cds per cui viene creata l'UO-CDS
     * @return Unita_organizzativaBulk l'UO-CDS creata
     */
    private Unita_organizzativaBulk creaUoCdsBulk(UserContext userContext, CdsBulk cdsBulk) throws it.cnr.jada.comp.ComponentException {
        try {
            Unita_organizzativaBulk uoBulk = new Unita_organizzativaBulk();
            uoBulk.setEsercizio_inizio(cdsBulk.getEsercizio_inizio());
            uoBulk.setEsercizio_fine(cdsBulk.getEsercizio_fine());
            uoBulk.setCd_proprio_unita(
                    getLunghezza_chiavi().formatUoKey(userContext, "0"));
            uoBulk.setCd_unita_organizzativa(cdsBulk.getCd_unita_organizzativa().concat(".").concat(uoBulk.getCd_proprio_unita()));
            uoBulk.setDs_unita_organizzativa(cdsBulk.getDs_unita_organizzativa());
            uoBulk.setCd_tipo_unita(cdsBulk.getCd_tipo_unita());
            uoBulk.setCd_unita_padre(cdsBulk.getCd_unita_organizzativa());
            uoBulk.setUnita_padre(cdsBulk);
            uoBulk.setFl_cds(new Boolean(false));
            uoBulk.setFl_rubrica(new Boolean(true));
            uoBulk.setFl_uo_cds(new Boolean(true));
            //uoBulk.setCd_responsabile( cdsBulk.getCd_responsabile());
            //uoBulk.setCd_responsabile_amm( cdsBulk.getCd_responsabile());
            uoBulk.setResponsabile(cdsBulk.getResponsabile());
            //uoBulk.setResponsabile_amm( cdsBulk.getResponsabile());
            uoBulk.setLivello(Constants.LIVELLO_UO);
            uoBulk.setFl_presidente_area(new Boolean(false));
            uoBulk.setUser(cdsBulk.getUser());

            return uoBulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    /**
     * Crea un'unita' organizzativa e il CDR responsabile di tale UO
     *
     * @param userContext
     * @param bulk        l'Unita_organizzativaBulk coi dati specificati dall'utente
     * @return Unita_organizzativaBulk l'Unita_organizzativaBulk creata
     */

    private OggettoBulk creaUOConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        // 05/09/2003
        // Aggiunto controllo sulla chiusura dell'esercizio
        if (isEsercizioChiuso(userContext))
            throw new ApplicationException("Non è possibile creare nuove unità organizzative ad esercizio chiuso.");

        try {

            Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk) bulk;
            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, bulk.getClass());

//		uoBulk.setCd_unita_padre(getLunghezza_chiaviHome().create().formatCdsKey( uoBulk.getCd_xx_unita_padre(), uoBulk.getEsercizio() ));

            if (uoBulk.getUnita_padre().getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_AREA))
                throw new ApplicationException("Non è possibile aggiungere unità organizzative ad un'area di ricerca");

            verificaEsercizioFine(userContext, uoBulk);

            try {
                lockBulk(userContext, uoBulk.getUnita_padre());
            } catch (it.cnr.jada.persistency.FindException e) {
                throw new ApplicationException("Codice CDS inesistente");
            }

            if (uoBulk.getCd_proprio_unita() == null || uoBulk.getCd_proprio_unita().equals("")) {
                String codice = uoHome.creaNuovoCodice(uoBulk.getUnita_padre().getCd_unita_organizzativa());
                uoBulk.setCd_proprio_unita(getLunghezza_chiavi().formatUoKey(userContext, codice));
            } else
                uoBulk.setCd_proprio_unita(getLunghezza_chiavi().formatUoKey(userContext, uoBulk.getCd_proprio_unita()));

            uoBulk.setCd_unita_organizzativa(uoBulk.getUnita_padre().getCd_unita_organizzativa().concat(".").concat(uoBulk.getCd_proprio_unita()));
            uoBulk.setCd_tipo_unita(uoBulk.getUnita_padre().getCd_tipo_unita());

            if (uoBulk.getResponsabile() != null && uoBulk.getResponsabile().getCd_terzo() != null) {
                try {
                    lockBulk(userContext, uoBulk.getResponsabile());
                } catch (it.cnr.jada.persistency.FindException e) {
                    throw new ApplicationException("Responsabile inesistente");
                }
            }

            if (uoBulk.getResponsabile_amm() != null && uoBulk.getResponsabile_amm().getCd_terzo() != null) {
                try {

                    lockBulk(userContext, uoBulk.getResponsabile_amm());
                } catch (it.cnr.jada.persistency.FindException e) {
                    throw new ApplicationException("Responsabile amministrativo inesistente");
                }
            }

            verificaAreeAssociate(userContext, uoBulk);

            CdrBulk cdrBulk = creaCdrBulk(userContext, uoBulk);

            insertBulk(userContext, uoBulk);
            insertBulk(userContext, cdrBulk);

            creaEsplSottArtArea(userContext, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio(), uoBulk, null);

            return uoBulk;
        } catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) {
            if (e.getPersistent() != bulk)
                throw handleException(bulk, e);
            try {
                throw handleException(new CRUDDuplicateKeyException("Errore di chiave duplicata", e, bulk, (OggettoBulk) getHome(userContext, bulk).findByPrimaryKey(bulk)));
            } catch (Throwable ex) {
                throw handleException(bulk, ex);
            }
        } catch (Throwable e) {
            throw handleException(e);
        }

    }

    /**
     * Esegue una operazione di eliminazione di CdsBulk o di Unita_organizzativaBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Cancellazione di un Cds
     * Pre:  La richiesta di cancellazione di un Cds e' stata generata
     * Post: Il Cds e' stato cancellato
     * <p>
     * Nome: Cancellazione di un'Unita' Organizzativa
     * Pre:  La richiesta di cancellazione di un'Unita' Organizzativa e' stata generata
     * Post: L'Unita' Organizzativa  e' stata cancellata
     * <p>
     * Nome: Cancellazione di un'Unita' Organizzativa con Area di Ricerca
     * Pre:  La richiesta di cancellazione di un'Unita' Organizzativa con area di Ricerca e' stata generata
     * Post: L'Unita' Organizzativa  e' stata cancellata e il Piano dei Conti finanziario viene aggiornato
     * <p>
     * Nome: Cancellazione di un'Unita' Organizzativa UO-CDS
     * Pre:  La richiesta di cancellazione di un'Unita' Organizzativa UO-CDS e' stata generata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di CdsBulk o di UnitaOrganizzativaBulk che deve essere cancellata
     */

    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        Collection aAreeCollegateIniziali = null;
        try {
            if (bulk instanceof CdsBulk) {

                CdsBulk cds = (CdsBulk) bulk;

                // 05/09/2003
                // Aggiunto controllo sulla chiusura dell'esercizio
                if (isEsercizioChiuso(userContext, cds))
                    throw new ApplicationException("Non è possibile eliminare cds con esercizio di terminazione chiuso.");

                lockBulk(userContext, cds);

                String uoCode = cds.getCd_proprio_unita().concat(".").concat(getLunghezza_chiavi().formatUoKey(userContext, "0"));

                deleteBulk(userContext, cds);
            } else if (bulk instanceof Unita_organizzativaBulk) {
                Unita_organizzativaBulk uo = (Unita_organizzativaBulk) bulk;

                // 05/09/2003
                // Aggiunto controllo sulla chiusura dell'esercizio
                if (isEsercizioChiuso(userContext, uo))
                    throw new ApplicationException("Non è possibile eliminare uo con esercizio di terminazione chiuso.");

                if (uo.getFl_uo_cds().booleanValue())
                    throw new ApplicationException("Non è possibile cancellare una UO CDS");

                // gestione cleanup su piano dei conti finanziario nel caso
                // all'UO sia collegata un'area

                // lock delle eventuali aree di ricerce collegate

                // Legge le aree correntemente collegate all'UO in processo
                // ed effettua il lock
                aAreeCollegateIniziali = getCdAreeCollegateIniziali(userContext, uo);
            }

            makeBulkPersistent(userContext, bulk);

            if (bulk instanceof Unita_organizzativaBulk) {
                Unita_organizzativaBulk uo = (Unita_organizzativaBulk) bulk;
                if (!aAreeCollegateIniziali.isEmpty())
                    creaEsplSottArtArea(userContext, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio(), null, aAreeCollegateIniziali);
            }
        } catch (it.cnr.jada.persistency.sql.ReferentialIntegrityException e) {
            throw handleException(new ApplicationException("La cancellazione non e' consentita in quanto la struttura organizzativa selezionata e' utilizzata. Si consiglia l'impostazione dell'Esercizio di Terminazione. "));
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }

    }

    /**
     * Ritorna l'area correntemente collegata all'UO in processo.
     */

    private Collection getCdAreeCollegateIniziali(UserContext userContext, Unita_organizzativaBulk uo) throws it.cnr.jada.comp.ComponentException {
        try {
            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, uo.getClass());
            Ass_uo_areaHome assUoAreaHome = (Ass_uo_areaHome) getHome(userContext, Ass_uo_areaBulk.class);

            Unita_organizzativaBulk uoIniziale = (Unita_organizzativaBulk) uoHome.findByPrimaryKey(uo);
            uoHome.lock(uoIniziale);

            uoIniziale.setAssociazioneUoArea(new it.cnr.jada.bulk.BulkList(uoHome.findAssociazioneUoArea(userContext, uoIniziale)));

            for (java.util.Iterator j = uoIniziale.getAssociazioneUoArea().iterator(); j.hasNext(); ) {
                Ass_uo_areaBulk assUoArea = (Ass_uo_areaBulk) j.next();
                lockBulk(userContext, assUoArea);
                assUoAreaHome.lock(assUoArea);
            }
            return uoIniziale.getAssociazioneUoArea();
        } catch (it.cnr.jada.persistency.FindException e) {
            throw new ApplicationException("Errore di eliminazione di UO collegata ad area di ricerca!");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Recupera la Home dell'ejb Lunghezza_chiaviComponent.
     *
     * @return Lunghezza_chiaviComponentSessionHome la Home dell'ejb Lunghezza_chiaviComponent
     */
    private it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession getLunghezza_chiavi() throws it.cnr.jada.comp.ComponentException {
        try {
            return (Lunghezza_chiaviComponentSession) EJBCommonServices.createEJB("CNRCONFIG00_EJB_Lunghezza_chiaviComponentSession", Lunghezza_chiaviComponentSession.class);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException {
        if (oggettobulk instanceof Parametri_cdsBulk) {
            inizializzaParametriCds(usercontext, (Parametri_cdsBulk) oggettobulk);
        }
        return super.inizializzaBulkPerRicerca(usercontext, oggettobulk);
    }

    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException {
        if (oggettobulk instanceof Parametri_cdsBulk) {
            inizializzaParametriCds(usercontext, (Parametri_cdsBulk) oggettobulk);
        }
        return super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile
     * operazione di modifica.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Oggetto non esistente
     * Pre: L'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore.
     * <p>
     * Nome: Tutti i controlli superati
     * Pre: L'OggettoBulk specificato esiste.
     * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
     * per l'operazione di presentazione e modifica nell'interfaccia visuale.
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
     * ottenuto concatenando il nome della component con la stringa ".edit"
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    bulk    l'OggettoBulk da preparare
     * @return l'OggettoBulk preparato
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            bulk = super.inizializzaBulkPerModifica(userContext, bulk);

            if (bulk instanceof CdsBulk) {    // carica le percentuali di copertura obbligazioni

                CdsBulk cds = (CdsBulk) bulk;

                cds.setPercentuali(new BulkList(((CdsHome) getHome(userContext, CdsBulk.class)).findPercentuali(cds)));
                cds.setEsercizioDiScrivania(((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());

                // 05/09/2003
                // Aggiunto controllo sulla chiusura dell'esercizio
                if (isEsercizioChiuso(userContext, cds))
                    bulk = asRO(bulk, "Non è possibile modificare cds con esercizio di terminazione chiuso.");

            } else if (bulk instanceof Unita_organizzativaBulk) {
                Unita_organizzativaBulk uo = (Unita_organizzativaBulk) bulk;
                Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);

                if (!uo.getFl_rubrica().booleanValue())
                    //recupero il Cdr Responsabile dell'UO
                    uo.setUoDiRiferimento(((Unita_organizzativaHome) getHome(userContext, uo.getClass())).findUoDiRiferimento(uo));

                // 05/09/2003
                // Aggiunto controllo sulla chiusura dell'esercizio
                if (isEsercizioChiuso(userContext, uo))
                    bulk = asRO(bulk, "Non è possibile modificare uo con esercizio di terminazione chiuso.");

                // 12/09/2005
                // Aggiunta la possibilità di associare più aree
                uo.setAssociazioneUoArea(new it.cnr.jada.bulk.BulkList(uoHome.findAssociazioneUoArea(userContext, uo)));
                getHomeCache(userContext).fetchAll(userContext);
            }

            return bulk;

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue una operazione di inserimento di un CdsBulk o di una UnitaOrganizzativaBulk nel database
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inserimento di Cds o UO
     * Pre: La richiesta di inserimento di un Cds o di una Unita' Organizzativa e' stata generata
     * Post: Il Cds o l'Unita' Organizzativa e' stato inserito nel database e la stored procedure ORACLEcreaEsplVociUO
     * che genera la struttura degli elementi voci dipendenti dall'entità inserita e' stata richiamata
     * <p>
     * Nome: Inserimento di Cdr
     * Pre: La richiesta di inserimento di un Cdr stata generata
     * Post: Il Cdr e' stato inserito nel database e la stored procedure ORACLEcreaEsplVociCdr
     * che genera la struttura degli elementi voci dipendenti dall'entità inserita e' stata richiamata
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    o il CdsBulk o l'Unita_organizzativaBulk che deve essere inserito
     */

    public void insertBulk(UserContext userContext, OggettoBulk o) throws PersistencyException, ComponentException {
        super.insertBulk(userContext, o);
        if (o instanceof CdrBulk) {
            try {
                Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
                if (!parCnr.getFl_nuovo_pdg()) {
                    CdrBulk cdr = (CdrBulk) o;
                    lockBulk(userContext, cdr);
                    /* CNRCTB001.creaEsplVociCDR(?,?,?) */
                    LoggableStatement cs = new LoggableStatement(getConnection(userContext), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB001.creaEsplVociCDR(?,?,?)}", false, this.getClass());
                    try {
                        cs.setObject(1, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
                        cs.setString(2, cdr.getCd_centro_responsabilita());
                        cs.setString(3, null); // passando null come user, i dati relativi a dacr/duva/utcr/utuv vengono ereditati dal cdr specificato
                        cs.executeQuery();
                    } catch (Exception e) {
                        throw handleException(o, e);
                    } finally {
                        cs.close();
                    }
                }
            } catch (Exception e) {
                throw handleException(e);
            }
        }
        if (o instanceof CdsBulk || o instanceof Unita_organizzativaBulk) {
            try {
                Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
                if (!parCnr.getFl_nuovo_pdg()) {

                    lockBulk(userContext, o);
                    /* CNRCTB001.creaEsplVociUO(?,?) */
                    LoggableStatement cs = new LoggableStatement(getConnection(userContext), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB001.creaEsplVociUO(?,?,?)}", false, this.getClass());
                    try {
                        cs.setObject(1, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
                        if (o instanceof CdsBulk)
                            cs.setString(2, ((CdsBulk) o).getCd_unita_organizzativa());
                        else
                            cs.setString(2, ((Unita_organizzativaBulk) o).getCd_unita_organizzativa());
                        cs.setString(3, null); // settando user a null, duva/dacr/utcr e utuv sono ereditati dall'UO in processo
                        cs.executeQuery();
                    } catch (Exception e) {
                        throw handleException(o, e);
                    } finally {
                        cs.close();
                    }
                }
            } catch (Exception e) {
                throw handleException(o, e);
            }
        }
    }

    /**
     * Inizializza Parametri_cdsBulk.
     */
    protected void inizializzaParametriCds(UserContext aUC, Parametri_cdsBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(aUC, Unita_organizzativa_enteBulk.class).findAll().get(0);
            if (!((CNRUserContext) aUC).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa())) {
                ((Parametri_cdsBulk) bulk).setCentro_spesa(
                        (CdsBulk) getHome(
                                aUC,
                                CdsBulk.class
                        ).findByPrimaryKey(
                                new CdsBulk(
                                        CNRUserContext.getCd_cds(aUC)
                                )
                        )
                );
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    protected boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return home.isEsercizioChiuso(userContext);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    protected boolean isEsercizioChiuso(UserContext userContext, CdsBulk cds) throws ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return home.isEsercizioChiuso(userContext, cds.getEsercizio_fine(), cds.getCd_unita_organizzativa());
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    protected boolean isEsercizioChiuso(UserContext userContext, Unita_organizzativaBulk uo) throws ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return home.isEsercizioChiuso(userContext, uo.getEsercizio_fine(), uo.getCd_unita_padre());
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue una operazione di modifica di un Cds o di una Unita' Organizzativa.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Modifica di Cds
     * Pre:  La richiesta di modifica di un Cds è stata generata
     * Post: Un Cds e' stato modificato con i dati inseriti dall'utente
     * <p>
     * Nome: Modifica della descrizione di un  Cds
     * Pre:  La richiesta di modifica della descrizione di un Cds è stata generata
     * Post: La descrizione del Cds e' stata modificata e la descrizione dell'UO-CDS e' stata modificata
     * <p>
     * Nome: Modifica dell'esercizio fine di un  Cds - OK
     * Pre:  La richiesta di modifica dell'esercizio fine di un CDS è stata generata e il nuovo esercizio fine e' valido
     * (metodo 'verificaEsercizioFine')
     * Post: L'esercizio fine del Cds e' stato modificato; sono stati aggiornati in cascata anche tutti gli esercizi fine di
     * tutte le UO che dipendono dal CDS e hanno esercizio fine maggiore rispetto a quello del Cds; sono stati aggiornati
     * in cascata anche tutti gli esercizi fine di tutti i Cdr che dipendono dalle UO che dipendono dal Cds e che hanno
     * esercizio fine maggiore rispetto a quello del Cds; sono stati aggiornati in cascata anche tutti gli esercizi fine di
     * tutte le linee di attività definite per i Cdr che dipendono dalle UO che dipendono dal Cds e che hanno esercizio
     * fine maggiore rispetto a quello del Cds.
     * <p>
     * Nome: Modifica dell'esercizio fine di un  Cds - Errore
     * Pre:  La richiesta di modifica dell'esercizio fine di un CDS è stata generata e il nuovo esercizio fine non e' valido
     * (metodo 'verificaEsercizioFine')
     * Post: Una ApplicationException viene lanciata per segnalare all'utente l'impossibilità di aggiornare l'esercizio fine
     * <p>
     * Nome: Modifica dell'esercizio fine di una Unita organizzativa - OK
     * Pre:  La richiesta di modifica dell'esercizio fine di un'Unita organizzativa è stata generata e il nuovo esercizio fine e' valido
     * (metodo 'verificaEsercizioFine')
     * Post: L'esercizio fine dell'Unita organizzativa e' stato modificato; sono stati aggiornati
     * in cascata anche tutti gli esercizi fine di tutti i Cdr che dipendono dall'Unita organizzativa e che hanno
     * esercizio fine maggiore rispetto a quello dell'Unita organizzativa; sono stati aggiornati in cascata anche tutti
     * gli esercizi fine di tutte le linee di attività definite per i Cdr che dipendono dall'Unita organizzativa e che hanno esercizio
     * fine maggiore rispetto a quello dell'Unita organizzativa
     * <p>
     * Nome: Modifica dell'esercizio fine di un'Unita organizzativa - Errore
     * Pre:  La richiesta di modifica dell'esercizio fine di un'Unita organizzativa è stata generata e il nuovo esercizio fine non e' valido
     * (metodo 'verificaEsercizioFine')
     * Post: Una ApplicationException viene lanciata per segnalare all'utente l'impossibilità di aggiornare l'esercizio fine
     * <p>
     * Nome: Errore di Responsabile inesistente
     * Pre:  Il Codice Terzo definito come responsabile del Cds non è presente
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Modifica di Unita' Organizzativa
     * Pre:  La richiesta di modifica di un' Unita' Organizzativa è stata generata
     * Post: L'Unita' Organizzativa e' stato modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato
     * aggiornato di conseguenza
     * <p>
     * Nome: Errore di Responsabile inesistente
     * Pre:  Il Codice Terzo definito come responsabile dell' Unita' organizzativa non è presente
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Responsabile amministrativo inesistente
     * Pre:  Il Codice Terzo definito come responsabile amministrativo dell' Unita' organizzativa non è presente
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Errore di Area Ricerca inesistente
     * Pre:  Il Cds specificato come Area di Ricerca per l'Unita' Organizzativa non esiste
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Modifica di Unita' Organizzativa con modifica di Area di Ricerca specificata
     * Pre: L'area di ricerca specificata è cambiata
     * Post: L'Unità Organizzativa è stata modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato aggiornato eventualemente eliminando il SOTTOARTICOLO corrispondente all'area di origine e creando quello corrispondente all'area di destinazione
     * n.b. "eventualmente" si riferisce al fatto che l'aggiunta o l'eliminazione del SOTTOARTICOLO sono subordinati al cambiamento o eliminazione di presidente di area. In uqesto caso l'intervento sul PDC finanziario potrebbe essere diretto all'aggiunzione o eliminazione di sottoarticoli relativi alle due aree iniziale e finale.
     * <p>
     * Nome: Modifica di Unita' Organizzativa con rimozione di Area di Ricerca specificata
     * Pre: L'area di ricerca non è più specificata.
     * Post: L'Unità Organizzativa è stata modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato aggiornato eventualemente eliminando il SOTTOARTICOLO corrispondente all'area di origine
     * n.b. "eventualmente" si riferisce al fatto che l'eliminazione del SOTTOARTICOLO è subordinato al fatto che l'uo eliminata era il presidente dell'areadente dell'area. Per una definizione di SOTTOARTICOLO vedi specifica di "creaConBulk"
     * <p>
     * Nome: Modifica di Unita' Organizzativa con aggiunzione di Area di Ricerca specificata
     * Pre: L'area di ricerca non era specificata ed ora lo è
     * Post: L'Unità Organizzativa è stata modificata con i dati inseriti dall'utente e il piano dei conti finanziario e' stato aggiornato eventualemente aggiungendo il SOTTOARTICOLO corrispondente all'area di destinazione.
     * n.b. "eventualmente" si riferisce al fatto che l'aggiunta del SOTTOARTICOLO è subordinato al fatto che l'uo collegata all'area risulti anche presidente dell'area. Per una definizione di SOTTOARTICOLO vedi specifica di "creaConBulk"
     * <p>
     * Nome: Errore di esistenza del Presidente dell'area
     * Pre: L'UO in modifica specifica i essere Presidente dell'area ed esiste per l'area di ricerca un presidente dell'area diverso dall'UO stessa
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da visualizzare all'utente
     * <p>
     * Nome: Una UO del SAC non può essere presidente dell'AREA
     * Pre:  L'UO appartiene al SAC ed è richiesto che sia Presidente dell'area a cui è collegata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk il CdsBulk o l'Unita_organizzativaBulk che deve essere modiifcato
     * @return il CdsBulk o l'Unita_organizzativaBulk risultante dopo l'operazione di modifica
     */

    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk)
            throws it.cnr.jada.comp.ComponentException {

        Collection aAreeCollegateIniziali = null;

        try {
            lockBulk(userContext, bulk);
            if (bulk instanceof Unita_organizzativaBulk) {
                Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk) bulk;

                // 05/09/2003
                // Aggiunto controllo sulla chiusura dell'esercizio
                if (isEsercizioChiuso(userContext, uoBulk))
                    throw new ApplicationException("Non è possibile modificare uo con esercizio di terminazione chiuso.");

                if (uoBulk.getResponsabile() != null
                        && uoBulk.getResponsabile().getCd_terzo() != null) {
                    try {
                        lockBulk(userContext, uoBulk.getResponsabile());
                    } catch (it.cnr.jada.persistency.FindException e) {
                        throw new ApplicationException("Responsabile inesistente");
                    }
                }

                if (uoBulk.getResponsabile_amm() != null
                        && uoBulk.getResponsabile_amm().getCd_terzo() != null) {
                    try {
                        lockBulk(userContext, uoBulk.getResponsabile_amm());
                    } catch (it.cnr.jada.persistency.FindException e) {
                        throw new ApplicationException("Responsabile amministrativo inesistente");
                    }
                }
                //aggiornamento a casacata dell'esercizio fine
                aggiornaEsercizioFine(userContext, uoBulk);

                // Legge le aree correntemente collegate all'UO in processo ed effettua il lock
                aAreeCollegateIniziali = getCdAreeCollegateIniziali(userContext, uoBulk);
                verificaAreeAssociate(userContext, uoBulk);

                getHomeCache(userContext).fetchAll(userContext);
                makeBulkPersistent(userContext, bulk);

                if (!aAreeCollegateIniziali.isEmpty())
                    creaEsplSottArtArea(
                            userContext,
                            ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio(),
                            uoBulk,
                            aAreeCollegateIniziali);
            } else {
                if (bulk instanceof CdsBulk) {
                    CdsBulk cdsBulk = (CdsBulk) bulk;

                    // 05/09/2003
                    // Aggiunto controllo sulla chiusura dell'esercizio
                    if (isEsercizioChiuso(userContext, cdsBulk))
                        throw new ApplicationException("Non è possibile modificare cds con esercizio di terminazione chiuso.");

                    if (cdsBulk.getArea_scientifica() == null
                            || cdsBulk.getArea_scientifica().getCd_area_scientifica() == null)
                        throw new ApplicationException("Area scientifica non definita");

                    if (cdsBulk.getResponsabile() != null
                            && cdsBulk.getResponsabile().getCd_terzo() != null) {
                        try {
                            lockBulk(userContext, cdsBulk.getResponsabile());
                        } catch (it.cnr.jada.persistency.FindException e) {
                            throw handleException(new ApplicationException("Responsabile non esiste"));
                        }
                    }

                    //			getHomeCache().fetchAll();

                    // se e' stato modificato l'esercizio fine aggiorno tutte le uo, i cdr e le linee attività sotto al cds

                    aggiornaEsercizioFine(userContext, cdsBulk);

                    //cerca UO_CDS per aggiornanrne la descrizione

                    String cdProprio =
                            getLunghezza_chiavi().formatUoKey(userContext, "0");
                    String cdCompleto =
                            cdsBulk.getCd_unita_organizzativa().concat(".").concat(cdProprio);
                    try {
                        lockBulk(userContext, cdsBulk);
                        Unita_organizzativaBulk uoCds =
                                (Unita_organizzativaBulk) getHome(
                                        userContext,
                                        Unita_organizzativaBulk.class).findByPrimaryKey(
                                        new Unita_organizzativaKey(cdCompleto));
                        lockBulk(userContext, uoCds);
                        uoCds.setDs_unita_organizzativa(cdsBulk.getDs_unita_organizzativa());
                        updateBulk(userContext, uoCds);
                        //updateBulk( userContext,cdsBulk );
                        makeBulkPersistent(userContext, cdsBulk);
                    } catch (it.cnr.jada.persistency.FindException e) {
                        throw new ApplicationException("Non esiste la UO CDS");
                    }

                } else {
                    super.modificaConBulk(userContext, bulk);
                }
            }
            return bulk;
        } catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) {
            if (e.getPersistent() instanceof Prc_copertura_obbligBulk)
                throw handleException(
                        new ApplicationException("Errore di chiave duplicata per Percentuale di copertura obbligazione"));
            else
                throw handleException(bulk, e);
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }

    }
    /*
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su CdsBulk o una Unita_organizzativa, per visualizzare solo
     * i cds o le uo validi per l'esercizio di scrivania
     *
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca CdsBulk o Unita_organizzativaBulk
     * Pre:  E' stata generata la richiesta di ricerca di un CdsBulk o Unita_organizzativaBulk
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
     *       clausola che il cds/uo sia valido per l'esercizio presente in scrivania
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @param bulk istanza di CdsBulk o Unita_organizzativaBulk che deve essere utilizzata per la ricerca
     * @return il SQLBuilder con la clausola aggiuntive
     */

    protected Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
        sql.addClause("AND", "esercizio_inizio", sql.LESS_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        sql.addClause("AND", "esercizio_fine", sql.GREATER_EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        return sql;
    }

    /*
     * Aggiunge alcune clausole a tutte le operazioni di ricerca del Cds area di ricerca
     *
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca di un cds
     * Pre:  E' stata generata la richiesta di ricerca di un Cds da utilizzare come area di ricerca
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
     *       clausola che il Cds sia valido per l'esercizio di scrivania e che la sua tipologia sia AREA
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param uo istanza di Unita_organizzativaBulk
     * @param cds istanza di CdsBulk che deve essere utilizzata per la ricerca
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @return il SQLBuilder con la clausola aggiuntive
     */
    public SQLBuilder selectCentro_spesaByClause(UserContext userContext, Parametri_cdsBulk uo, CdsBulk cds, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
        sql.addClause(clauses);
        sql.addClause("AND", "cd_tipo_unita", SQLBuilder.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        try {
            Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
            if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa())) {
                sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, ((CNRUserContext) userContext).getCd_cds());
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }

        return sql;
    }

    public SQLBuilder selectCds_area_ricercaByClause(UserContext userContext, Unita_organizzativaBulk uo, CdsBulk cds, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
        sql.addClause("AND", "cd_tipo_unita", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_AREA);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        sql.addClause(clauses);
        return sql;
    }

    public SQLBuilder selectCds_area_ricercaByClause(UserContext userContext, Ass_uo_areaBulk assUoArea, CdsBulk cds, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
        sql.addClause("AND", "cd_tipo_unita", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_AREA);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        sql.addClause(clauses);
        return sql;
    }

    /*
     * Aggiunge alcune clausole a tutte le operazioni di ricerca del Cds, padre dell'Unita' organizzativa
     *
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca del Cds padre
     * Pre:  E' stata generata la richiesta di ricerca di un CdsBulk da utilizzare come padre
     *		 per l'Unità organizzativa
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
     *       clausola che il Cds sia valido per l'esercizio di scrivania e che la sua tipologia sia diversa da ENTE
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param uo istanza di Unita_organizzativaBulk
     * @param cds istanza di CdsBulk che deve essere utilizzata per la ricerca
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @return il SQLBuilder con la clausola aggiuntive
     */

    public SQLBuilder selectUnita_padreByClause(UserContext userContext, Unita_organizzativaBulk uo, CdsBulk cds, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
        sql.addClause(clauses);
        sql.addClause("AND", "cd_tipo_unita", SQLBuilder.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        return sql;
    }
    /*
     * Aggiunge alcune clausole a tutte le operazioni di ricerca dell'unita organizzativa di riferimento per
     * le unità organizzative che dipendono da un Cds di tipo SAC e che non sono rubrica
     * Pre-post-conditions:
     *
     * Nome: Richiesta di ricerca dell'unita organizzativa di riferimento
     * Pre:  E' stata generata la richiesta di ricerca di una Unita_organizzativaBulk da utilizzare come riferimento per
     *		 le unità organizzative che dipendono da un Cds di tipo SAC e che non sono rubrica. Tale Uo di Riferimento
     *       viene utilizzata alla creazione del Cdr Responsabile dell'UO, per impostare come Cdr di afferenza il Cdr responsabile
     *		 dell'Uo di riferimento
     * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
     *       clausola che l'unita' organizzativa sia valida per l'esercizio di scrivania e che il Cds padre dell'unità organizzativa
     *		 sia quello di scrivania e che sotto a tale Unità Organizzativa esista un CDR di I livello
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param uo istanza di Unita_organizzativaBulk
     * @param uoDiRiferimento istanza di Unita_organizzativaBulk che deve essere utilizzata per la ricerca
     * @param clauses clausole di ricerca gia' specificate dall'utente
     * @return il SQLBuilder con la clausola aggiuntive
     */

    public SQLBuilder selectUoDiRiferimentoByClause(UserContext userContext, Unita_organizzativaBulk uo, Unita_organizzativaBulk uoDiRiferimento, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, uo.getClass(), "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
        sql.addTableToHeader("CDR");
        sql.addClause(clauses);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        sql.addSQLClause("AND", "V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_PADRE", SQLBuilder.EQUALS, uo.getUnita_padre().getCd_unita_organizzativa());
        sql.addSQLClause("AND", "CDR.LIVELLO", SQLBuilder.EQUALS, CdrHome.CDR_PRIMO_LIVELLO);
        sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA", "V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA");
        return sql;
    }

    /**
     * Esegue una operazione di aggiornamento di un CdsBulk o di una Unita_organizzativaBulk nel database
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Aggiornamento di Cds o di UO
     * Pre: La richiesta di aggiornamento di un Cds o di una Unità Organizzativa e' stata generata
     * Post: Il Cds/UO e' stato aggiornato nel database e la stored procedure ORACLEcreaEsplVociUO
     * che aggiorna la struttura degli elementi voci dipendenti dall'entità modificata e' stata richiamata
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    o il CdsBulk/Unita_organizzativabulk che deve essere aggiornato
     */

    public void updateBulk(UserContext userContext, OggettoBulk o) throws PersistencyException, ComponentException {
        super.updateBulk(userContext, o);
        if (o instanceof CdsBulk || o instanceof Unita_organizzativaBulk) {
            try {
                Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
                if (!parCnr.getFl_nuovo_pdg()) {
                    lockBulk(userContext, o);
                    /* CNRCTB001.creaEsplVociUO(?,?) */
                    LoggableStatement cs = new LoggableStatement(getConnection(userContext), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB001.creaEsplVociUO(?,?,?)}", false, this.getClass());
                    try {
                        cs.setObject(1, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
                        if (o instanceof CdsBulk)
                            cs.setString(2, ((CdsBulk) o).getCd_unita_organizzativa());
                        else
                            cs.setString(2, ((Unita_organizzativaBulk) o).getCd_unita_organizzativa());
                        cs.setString(3, null); // settando user a null, duva/dacr/utcr e utuv sono ereditati dall'UO in processo
                        cs.executeQuery();
                    } catch (Exception e) {
                        throw handleException(o, e);
                    } finally {
                        cs.close();
                    }
                }
            } catch (Exception e) {
                throw handleException(o, e);
            }
        }
    }
    /* Verifica la correttezza dell'attributo fine.
     *
     * Nome: Errore per Cds SAC
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cds SAC e' stata generata
     * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
     *
     * Nome: Errore per PDG
     * Pre:  Per un Cds non SAC e' stata specificato un esercizio fine ed esistono dei Cdr dipendenti da UO che dipendono
     *       dal Cds per i quali e' già stato aperto il piano di gestione
     * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
     *
     * Nome: Controlli superati
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un Cds non SAC e' stata generata
     * Post: L'esercizio fine del Cds ha superato tutti i controlli
     */

    private void verificaEsercizioFine(UserContext userContext, CdsBulk cds) throws it.cnr.jada.comp.ComponentException {
        try {
            //non e' possibile impostare l'esercizio di terminazione x CDS SAC
            if (cds.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC) &&
                    !cds.getEsercizio_fine().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.ESERCIZIO_FINE))
                throw handleException(new ApplicationException("Non e' possibile impostare l'esercizio di terminazione per un Cds di tipo SAC"));
            //l'esercizio fine deve essere > dell'esercizio per cui e' gia' stato previsto un dettaglio nel preventivo
            if (!((CdsHome) getHome(userContext, CdsBulk.class)).verificaEsercizioPreventivo(cds))
                throw handleException(new ApplicationException("L'Esercizio di terminazione deve essere superiore agli esercizi per cui sono stati definiti dei preventivi"));

        } catch (Throwable e) {
            throw handleException(cds, e);
        }

    }
    /* Verifica la correttezza dell'attributo fine di un'unità organizzativa
     *
     * Nome: Errore per UO-CDS
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un'UO-CDS e' stata generata
     * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
     *
     * Nome: Errore per CDS padre
     * Pre:  Per un'unità organizzativa non UO-CDS e' stata specificato un esercizio fine superiore rispetto a quello
     *		 del Cds da cui dipende
     * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
     *
     * Nome: Errore per PDG
     * Pre:  Per un'unità organizzativa non UO-CDS e' stata specificato un esercizio fine ed esistono dei Cdr dipendenti
     *       dall'UO per i quali e' già stato aperto il piano di gestione
     * Post: E' stata generata un'Application Exception per segnalare all'utente l'impossibilità di effettuare tale modifica
     *
     * Nome: Controlli superati
     * Pre:  La richiesta di modifica dell'attributo esercizio fine di un'unità organizzativa non UO-CDS e' stata generata
     * Post: L'esercizio fine dell'unità organizzativa ha superato tutti i controlli
     */

    private void verificaEsercizioFine(UserContext userContext, Unita_organizzativaBulk uo) throws it.cnr.jada.comp.ComponentException {
        try {
            // non e' possibile modificare l'eser. terminaz. per UO CDS
            if (uo.getFl_uo_cds().booleanValue())
                throw new ApplicationException("Non è possibile impostare l'esercizio di terminazione per una UO CDS");

            //l'esercizio fine deve essere <= dell'esercizio fine del cds da cui dipende
            if (uo.getUnita_padre().getEsercizio_fine() != null &&
                    uo.getUnita_padre().getEsercizio_fine().compareTo(uo.getEsercizio_fine()) < 0)
                throw handleException(new ApplicationException(" Esercizio di terminazione deve essere minore o uguale a " + uo.getUnita_padre().getEsercizio_fine().toString()));

            //l'esercizio fine deve essere > dell'esercizio per cui e' gia' stato previsto un dettaglio nel preventivo
            if (!((Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class)).verificaEsercizioPreventivo(uo))
                throw handleException(new ApplicationException("L'Esercizio di terminazione deve essere superiore agli esercizi per cui sono stati definiti dei preventivi"));

        } catch (Throwable e) {
            throw handleException(uo, e);
        }

    }

    public OggettoBulk findUOByCodice(UserContext userContext, String cd_uo) throws ComponentException {
        try {
            Unita_organizzativaBulk uo = new Unita_organizzativaBulk(cd_uo);
            uo = (Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(uo);
            return uo;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    public OggettoBulk getUoEnte(UserContext userContext) throws ComponentException {
        try {
            return (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    /**
     * Recupera l'indirizzo della sede dell'unità organizzativa
     *
     * @param userContext
     * @param uo            -- l'Unita' Organizzativa di cui si vuole conoscere l'indirizzo
     * @param withoutComune -- se "true"  restituisce solo l'indirizzo
     *                      se "false" restituisce l'indirizzo completo di cap e comune
     * @return l'indirizzo della sede dell'unità organizzativa
     * @throws it.cnr.jada.comp.ComponentException
     */
    public String getIndirizzoUnitaOrganizzativa(UserContext userContext, Unita_organizzativaBulk uo, boolean withoutComune) throws it.cnr.jada.comp.ComponentException {
        try {
            TerzoBulk terzo = Utility.createTerzoComponentSession().cercaTerzoPerUnitaOrganizzativa(userContext, uo);
            String indirizzo = "";
            if (terzo != null) {
                if (terzo.getVia_sede() != null)
                    indirizzo = indirizzo + terzo.getVia_sede();
                if (terzo.getNumero_civico_sede() != null)
                    indirizzo = indirizzo + ", " + terzo.getNumero_civico_sede();
                if (!withoutComune) {
                    if (terzo.getCap_comune_sede() != null)
                        indirizzo = indirizzo + " - " + terzo.getCap_comune_sede();
                    if (terzo.getComune_sede() != null) {
                        ComuneBulk comune = (ComuneBulk) getHome(userContext, ComuneBulk.class).findByPrimaryKey(terzo.getComune_sede());
                        if (comune != null && comune.getDs_comune() != null)
                            indirizzo = indirizzo + "  " + comune.getDs_comune();
                    }
                }
            }
            return indirizzo;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public java.util.List findListaUOWS(UserContext userContext, String query, String dominio, String tipoRicerca, String cds) throws ComponentException {
        try {
            Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
            SQLBuilder sql = (SQLBuilder) super.select(userContext, null, new Unita_organizzativaBulk());
            if (cds != null)
                sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, cds);
            sql.addSQLClause("AND", "esercizio_inizio", sql.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "esercizio_fine", sql.GREATER_EQUALS, CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "LIVELLO", sql.EQUALS, Constants.LIVELLO_UO);
            if (dominio.equalsIgnoreCase("codice"))
                sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, query);
            else if (dominio.equalsIgnoreCase("descrizione")) {

                sql.openParenthesis("AND");
                for (StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements(); ) {
                    String queryDetail = stringtokenizer.nextToken();
                    if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva")) || tipoRicerca == null) {
                        if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
                            sql.addSQLClause("AND", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.CONTAINS, queryDetail);
                        else {
                            sql.openParenthesis("AND");
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.CONTAINS, queryDetail);
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.CONTAINS, RemoveAccent.convert(queryDetail));
                            sql.closeParenthesis();
                        }
                    } else if (tipoRicerca.equalsIgnoreCase("puntuale")) {
                        if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))) {
                            sql.openParenthesis("AND");
                            sql.addSQLClause("AND", "UPPER(DS_UNITA_ORGANIZZATIVA)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.STARTSWITH, queryDetail + " ");
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.ENDSWITH, " " + queryDetail);
                            sql.closeParenthesis();
                        } else {
                            sql.openParenthesis("AND");
                            sql.openParenthesis("AND");
                            sql.addSQLClause("OR", "UPPER(DS_UNITA_ORGANIZZATIVA)", SQLBuilder.EQUALS, queryDetail.toUpperCase());
                            sql.addSQLClause("OR", "UPPER(DS_UNITA_ORGANIZZATIVA)", SQLBuilder.EQUALS, RemoveAccent.convert(queryDetail).toUpperCase());
                            sql.closeParenthesis();
                            sql.openParenthesis("OR");
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.STARTSWITH, queryDetail + " ");
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.STARTSWITH, RemoveAccent.convert(queryDetail) + " ");
                            sql.closeParenthesis();
                            sql.openParenthesis("OR");
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.ENDSWITH, " " + queryDetail);
                            sql.addSQLClause("OR", "DS_UNITA_ORGANIZZATIVA", SQLBuilder.ENDSWITH, " " + RemoveAccent.convert(queryDetail));
                            sql.closeParenthesis();
                            sql.closeParenthesis();
                        }
                    }
                }
                sql.closeParenthesis();
                sql.addOrderBy("CD_UNITA_ORGANIZZATIVA");
            }

            return home.fetchAll(sql);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public java.util.List<CdsBulk> findListaCds(UserContext userContext, int esercizio) throws ComponentException {
        try {
            return ((CdsHome)getHome(userContext, CdsBulk.class)).findAllCds(userContext, esercizio);
        } catch (it.cnr.jada.persistency.PersistencyException | IntrospectionException ex) {
            throw handleException(ex);
        }
    }

}
