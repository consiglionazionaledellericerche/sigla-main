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

package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.AbicabHome;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.CRUDReferentialIntegrityException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Iterator;

/**
 * Insert the type's description here.
 * Creation date: (13/11/2002 12.35.36)
 *
 * @author: Roberto Fantino
 */
public class AbiCabComponent extends CRUDComponent implements IAbiCabMgr {
    /**
     * AbiCabComponent constructor comment.
     */
    public AbiCabComponent() {
        super();
    }

    /**
     * Viene richiesta l'eliminazione dell'Abicab selezionato
     * <p>
     * Pre-post-conditions:
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk l'OggettoBulk da eliminare
     * @return void
     **/
    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {
            AbicabBulk abicab = (AbicabBulk) bulk;
            try {
                super.eliminaConBulk(userContext, abicab);
            } catch (ComponentException ex) {
                if (ex.getClass().equals(CRUDReferentialIntegrityException.class)) {
                    abicab.setFl_cancellato(Boolean.TRUE);
                    updateBulk(userContext, abicab);
                } else
                    throw ex;
            }
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }


    }

    /**
     * Ricerca cap legati al comune selezionato
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param abiCab      L'abiCab in uso
     * @return La collezione di cap associati al comune selezionato
     */
    public AbicabBulk findCaps(UserContext userContext, AbicabBulk abiCab) throws ComponentException {

        try {
            ComuneHome home = (ComuneHome) getHome(userContext, ComuneBulk.class);
            ComuneBulk comune = abiCab.getComune();

            if (comune == null || comune.getPg_comune() == null)
                return abiCab;

            java.util.Collection coll = home.findCaps(comune);
            abiCab.setCapsComune(coll);
            if (abiCab.getCap() == null)
                if (comune.getCd_cap() != null)
                    abiCab.setCap(comune.getCd_cap());
                else
                    abiCab.setCap(null);

            return abiCab;

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(ex);
        }
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
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk    l'OggettoBulk da preparare
     * @return l'OggettoBulk preparato
     * <p>
     * Metodo privato chiamato:
     * completaCompenso(UserContaxt userContext, CompensoBulk compenso);
     **/
    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        AbicabBulk abicab = (AbicabBulk) super.inizializzaBulkPerModifica(userContext, bulk);
        abicab = findCaps(userContext, abicab);

        return abicab;
    }

    /**
     * Ricerca TRUE se l'oggetto bulk è cancellato logicamento
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param abiCab      L'abiCab in uso
     * @return TRUE se l'oggetto byulk è cancellato logicamento, FALSE altrimenti
     */
    public boolean isCancellatoLogicamente(UserContext userContext, AbicabBulk abiCab) throws ComponentException {

        try {
            AbicabHome home = (AbicabHome) getHome(userContext, abiCab);
            AbicabBulk bulk = (AbicabBulk) home.findByPrimaryKey(abiCab);
            if (bulk == null)
                return false;
            return bulk.isCancellatoLogicamente();
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }

    }

    public BancaBulk caricaStrutturaIban(UserContext userContext, BancaBulk banca) throws ComponentException {
        if (banca == null || banca.getCodice_iban() == null) return banca;
        if (banca.getCodice_iban().contains(" "))
            throw new ApplicationException("Caricamento Struttura IBAN non possibile. Esistono spazi nel codice IBAN presente.");

        try {
            int contaNazioni = 0;
            NazioneBulk nazione = null;

            if (banca.getNazioniIban() != null) {
                for (Iterator i = banca.getNazioniIban().iterator(); i.hasNext(); ) {
                    NazioneBulk nazioneIterator = (NazioneBulk) i.next();
                    if (nazioneIterator.getCd_iso().equals(banca.getCodice_iban().substring(0, 2))) {
                        contaNazioni++;
                        nazione = nazioneIterator;
                    }
                }
            }

            if (contaNazioni == 0) {
                NazioneHome home = (NazioneHome) getHome(userContext, NazioneBulk.class);
                SQLBuilder sql = home.createSQLBuilder();
                sql.addClause("AND", "cd_iso", SQLBuilder.EQUALS, banca.getCodice_iban().substring(0, 2));

                it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
                while (broker.next()) {
                    contaNazioni++;
                    nazione = (NazioneBulk) broker.fetch(NazioneBulk.class);
                }
            }
            if (contaNazioni == 0)
                throw new ApplicationException("Caricamento Struttura IBAN non possibile. Non esistono nazioni con codice ISO uguale a " + banca.getCodice_iban().substring(0, 2) + ".");
            if (contaNazioni > 1)
                throw new ApplicationException("Caricamento Struttura IBAN non possibile. Esistono più nazioni con codice ISO uguale a " + banca.getCodice_iban().substring(0, 2) + ".");

            int lunghIban = 0;

            for (int i = 0; i < nazione.getStrutturaIbanNrLivelli(); i++)
                lunghIban = lunghIban + nazione.getStrutturaIbanLivello(i + 1).length();

            if (banca.getCodice_iban().substring(2).length() != lunghIban)
                throw new ApplicationException("Caricamento Struttura IBAN non possibile. La lunghezza del codice IBAN presente non corrisponde a quella richiesta per la nazione con codice ISO pari a " + nazione.getCd_iso() + ".");

            banca.setNazione_iban(nazione);
            String iban = banca.getCodice_iban().substring(2);

            for (int i = 0; i < nazione.getStrutturaIbanNrLivelli(); i++) {
                if (i == 0)
                    banca.setCodice_iban_parte1(iban.substring(0, nazione.getStrutturaIbanLivello(i + 1).length()));
                if (i == 1)
                    banca.setCodice_iban_parte2(iban.substring(0, nazione.getStrutturaIbanLivello(i + 1).length()));
                if (i == 2)
                    banca.setCodice_iban_parte3(iban.substring(0, nazione.getStrutturaIbanLivello(i + 1).length()));
                if (i == 3)
                    banca.setCodice_iban_parte4(iban.substring(0, nazione.getStrutturaIbanLivello(i + 1).length()));
                if (i == 4)
                    banca.setCodice_iban_parte5(iban.substring(0, nazione.getStrutturaIbanLivello(i + 1).length()));
                if (i == 5)
                    banca.setCodice_iban_parte6(iban.substring(0, nazione.getStrutturaIbanLivello(i + 1).length()));

                iban = iban.substring(nazione.getStrutturaIbanLivello(i + 1).length());
            }

            return banca;
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

	@Override
	protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if (compoundfindclause == null) {
			if (oggettobulk != null)
				compoundfindclause = oggettobulk.buildFindClauses(null);
		} else {
			compoundfindclause = CompoundFindClause.and(compoundfindclause, oggettobulk.buildFindClauses(Boolean.FALSE));
		}
		final AbicabHome home = (AbicabHome) getHome(usercontext, oggettobulk);
		final SQLBuilder fullSQLBuilder = home.createFullSQLBuilder();
		fullSQLBuilder.addClause(compoundfindclause);
		return fullSQLBuilder;
	}
}
