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

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrKey;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyError;
import it.cnr.jada.persistency.PersistencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class Configurazione_cnrComponent extends it.cnr.jada.comp.GenericComponent implements IConfigurazione_cnrMgr, Cloneable, Serializable {

    private transient final static Logger logger = LoggerFactory.getLogger(Configurazione_cnrComponent.class);

    public static final String ASTERISCO = "*";

    public Configurazione_cnrComponent() {

        /*Default constructor*/


    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto un istanza di Configurazioe_cnrBulk dalle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito un'istanza di Configurazione_cnrBulk o null se la ricerca non restituisce nulla
     */
    public Configurazione_cnrBulk getConfigurazione(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            return (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public Timestamp getDt01(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getDt01();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public Timestamp getDt01(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getDt01();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public Timestamp getDt02(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getDt02();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public Timestamp getDt02(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getDt02();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public BigDecimal getIm01(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getIm01();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public BigDecimal getIm01(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getIm01();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public BigDecimal getIm02(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getIm02();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public BigDecimal getIm02(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getIm02();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal01(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getVal01();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal01(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getVal01();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal02(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getVal02();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal02(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getVal02();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal03(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getVal03();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal03(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getVal03();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * esercizio nullo
     * PreCondition:
     * esercizio = null
     * PostCondition:
     * Effettua la ricerca usando la condizione SQL esercizio = '*'
     * unita funzionale nulla
     * PreCondition:
     * unita_funzionale = null
     * PostCondition:
     * Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
     * chiave secondaria nulla
     * PreCondition:
     * chiave_secondaria = null
     * PostCondition:
     * Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal04(UserContext userContext, Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws ComponentException {
        try {
            if (esercizio == null) esercizio = new Integer(0);
            if (unita_funzionale == null) unita_funzionale = ASTERISCO;
            if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
            if (bulk == null) return null;
            return bulk.getVal04();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     * Normale
     * PreCondition:
     * Viene richiesto il valore del campo val01 delle costanti cnr
     * PostCondition:
     * Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
     */
    public String getVal04(UserContext userContext, String chiave_primaria) throws ComponentException {
        try {
            Configurazione_cnrBulk bulk = (Configurazione_cnrBulk) getHome(userContext, Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, ASTERISCO, ASTERISCO, new Integer(0)));
            if (bulk == null) return null;
            return bulk.getVal04();
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    public Boolean isAttivoOrdini(UserContext userContext) throws ComponentException {
        try {
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_ORDINI,
                    Configurazione_cnrBulk.SK_GESTIONE_ORDINI,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        try {
                            return val01YesNo(userContext, configurazioneCnrKey.esercizio(0))
                                    .orElse(Boolean.FALSE);
                        } catch (PersistencyException|ComponentException e) {
                            throw new PersistencyError(e);
                        }
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public Boolean isEconomicaPatrimonialeAttivaImputazioneManuale(UserContext userContext) throws ComponentException {
        try {
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE,
                    Configurazione_cnrBulk.SK_IMPUTAZIONE_MANUALE,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        try {
                            return val01YesNo(userContext, configurazioneCnrKey.esercizio(0))
                                    .orElse(Boolean.FALSE);
                        } catch (PersistencyException|ComponentException e) {
                            throw new PersistencyError(e);
                        }
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    private Optional<Boolean> val01YesNo(UserContext userContext, Configurazione_cnrKey configurazioneCnrKey) throws PersistencyException, ComponentException {
        final BulkHome home = getHome(userContext, Configurazione_cnrBulk.class);
        return Optional.ofNullable(home.findByPrimaryKey(configurazioneCnrKey))
                .filter(Configurazione_cnrBulk.class::isInstance)
                .map(Configurazione_cnrBulk.class::cast)
                .map(bulk -> Optional.ofNullable(bulk.getVal01()).filter(val->val.equals("Y")).isPresent());
    }

    /**
     * Ritorna il codice uo della Ragioneria
     * <p><b>chiave_primaria: UO_SPECIALE</b>
     * <p><b>chiave_secondaria: UO_RAGIONERIA</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice uo della Ragioneria
     * @throws ComponentException, PersistencyException
     */
    public String getUoRagioneria(UserContext userContext, Integer esercizio) throws ComponentException {
        try {
            return ((Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class)).getUoRagioneria(esercizio);
        }catch (PersistencyException e){
            throw handleException(e);
        }
    }

    /**
     * Ritorna il codice cdr del personale
     * <p><b>chiave_primaria: ELEMENTO_VOCE_SPECIALE</b>
     * <p><b>chiave_secondaria: TEMPO_IND_SU_PROGETTI_FINANZIATI</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice cdr del personale
     * @throws ComponentException
     */
    public String getCdrPersonale(UserContext userContext, Integer esercizio) throws ComponentException {
        try {
            return ((Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class)).getCdrPersonale(esercizio);
        }catch (PersistencyException e){
            throw handleException(e);
        }
    }

    /**
     * Ritorna il codice uo distinta tutta sac
     * <p><b>chiave_primaria: UO_SPECIALE</b>
     * <p><b>chiave_secondaria: UO_DISTINTA_TUTTA_SAC</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice codice uo distinta tutta sac
     * @throws PersistencyException
     */
    public String getUoDistintaTuttaSac(UserContext userContext, Integer esercizio) throws ComponentException {
        try {
            return ((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).getUoDistintaTuttaSac(esercizio);
        }catch (PersistencyException e){
            throw handleException(e);
        }
    }

    /**
     * Indica se la uo indicata è proprio quella speciale tutta sac
     *
     * @param esercizio l'esercizio di ricerca - Lasciare vuoto per ricercare il parametro generale (esercizio=0).
     * @param cdUnitaOrganizzativa l'unità organizzativa di cui si chiede se si tratta della Uo Speciale Tutta SAC
     * @return Boolean
     * @throws PersistencyException
     */
    public Boolean isUOSpecialeDistintaTuttaSAC(UserContext userContext, Integer esercizio, String cdUnitaOrganizzativa) throws ComponentException {
        try {
            return ((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).isUOSpecialeDistintaTuttaSAC(esercizio,cdUnitaOrganizzativa);
        }catch (PersistencyException e){
            throw handleException(e);
        }
    }

    /**
     * Indica il cds sac
     *
     * @param esercizio l'esercizio di ricerca - Lasciare vuoto per ricercare il parametro generale (esercizio=0).
     * @throws PersistencyException
     */
    public String getCdsSAC(UserContext userContext, Integer esercizio) throws ComponentException {
        try {
            return ((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).getCdsSAC(esercizio);
        }catch (PersistencyException e){
            throw handleException(e);
        }
    }

    public void shutdowHook(UserContext userContext) throws ComponentException {
        logger.info("shutdow hook");
        final BulkHome home = getHome(userContext, Configurazione_cnrBulk.class);
        try {
            Configurazione_cnrBulk configurazione_cnrBulk  = new Configurazione_cnrBulk(
                    Configurazione_cnrBulk.PK_EMAIL_PEC,
                    Configurazione_cnrBulk.SK_SDI,
                    ASTERISCO,
                    new Integer(0));
            Optional.ofNullable(home.findByPrimaryKey(configurazione_cnrBulk))
                    .filter(Configurazione_cnrBulk.class::isInstance)
                    .map(Configurazione_cnrBulk.class::cast)
                    .filter(bulk -> bulk.getVal04().equalsIgnoreCase("Y"))
                    .ifPresent(bulk -> {
                        bulk.setVal04("N");
                        bulk.setToBeUpdated();
                        try {
                            home.update(bulk, userContext);
                        } catch (PersistencyException e) {
                            throw new RuntimeException(e);
                        }
                    });
            Configurazione_cnrBulk flussoOrdinativi  = new Configurazione_cnrBulk(
                    Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                    Configurazione_cnrBulk.SK_ATTIVO_SIOPEPLUS,
                    ASTERISCO,
                    LocalDateTime.now().getYear());
            Optional.ofNullable(home.findByPrimaryKey(flussoOrdinativi))
                    .filter(Configurazione_cnrBulk.class::isInstance)
                    .map(Configurazione_cnrBulk.class::cast)
                    .filter(bulk -> bulk.getVal04().equalsIgnoreCase("Y"))
                    .ifPresent(bulk -> {
                        bulk.setVal04("N");
                        bulk.setToBeUpdated();
                        try {
                            home.update(bulk, userContext);
                        } catch (PersistencyException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public Boolean getGestioneImpegnoChiusuraForzataCompetenza(UserContext userContext) throws ComponentException {
        try {
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_AGGIORNAMENTO_IMPEGNO_DA_ORDINE,
                    Configurazione_cnrBulk.IMPEGNO_CHIUSURA_FORZATA_A_COMPETENZA,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        return Boolean.FALSE;
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public Boolean getGestioneImpegnoChiusuraForzataResiduo(UserContext userContext) throws ComponentException {
        try {
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_AGGIORNAMENTO_IMPEGNO_DA_ORDINE,
                    Configurazione_cnrBulk.IMPEGNO_CHIUSURA_FORZATA_A_RESIDUO,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        return Boolean.FALSE;
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     *
     * @param userContext
     * @return É attiva la gestione dell'economico patrimononale parallela
     * @throws PersistencyException
     */
    public boolean isAttivaEconomicaParallela(UserContext userContext) throws ComponentException {
        try {
            return Optional.ofNullable(getHome(userContext, Configurazione_cnrBulk.class))
                    .filter(Configurazione_cnrHome.class::isInstance)
                    .map(Configurazione_cnrHome.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Configurazione Home not found"))
                    .isAttivaEconomicaParallela(userContext);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    /**
     *
     * @param userContext
     * @return É attivo il blocco delle scritture di economica
     * @throws PersistencyException
     */
    public boolean isBloccoScrittureProposte(UserContext userContext) throws ComponentException {
        try {
            return Optional.ofNullable(getHome(userContext, Configurazione_cnrBulk.class))
                    .filter(Configurazione_cnrHome.class::isInstance)
                    .map(Configurazione_cnrHome.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Configurazione Home not found"))
                    .isBloccoScrittureProposte(userContext);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }
    public Boolean getGestioneImpegnoChiusuraForzataResiduo(UserContext userContext) throws ComponentException {
        try {
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_AGGIORNAMENTO_IMPEGNO_DA_ORDINE,
                    Configurazione_cnrBulk.IMPEGNO_CHIUSURA_FORZATA_A_RESIDUO,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        return Boolean.FALSE;
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public Boolean isAccertamentoPluriennaleAttivo(UserContext userContext) throws ComponentException{
        try{
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_ACCERTAMENTI,
                    Configurazione_cnrBulk.SK_ACCERTAMENTI_PLURIENNALI,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        return Boolean.FALSE;
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public Boolean isImpegnoPluriennaleAttivo(UserContext userContext) throws ComponentException{
        try{
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_IMPEGNI,
                    Configurazione_cnrBulk.SK_IMPEGNI_PLURIENNALI,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        return Boolean.FALSE;
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public Boolean isAssPrgAnagraficoAttiva(UserContext userContext) throws ComponentException{
        try{
            Configurazione_cnrKey configurazioneCnrKey = new Configurazione_cnrKey(
                    Configurazione_cnrBulk.PK_GESTIONE_PROGETTI,
                    Configurazione_cnrBulk.SK_ASS_PROGETTI_ANGAGRAFICO,
                    ASTERISCO,
                    CNRUserContext.getEsercizio(userContext));
            return val01YesNo(userContext, configurazioneCnrKey)
                    .orElseGet(() -> {
                        return Boolean.FALSE;
                    });
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }
}