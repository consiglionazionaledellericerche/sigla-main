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

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Esenzioni_addizionaliComponent extends it.cnr.jada.comp.CRUDComponent {

    public Esenzioni_addizionaliComponent() {
        super();
    }

    public Esenzioni_addizionaliBulk verifica_aggiornamento(UserContext usercontext, Esenzioni_addizionaliBulk esenzioni_addizionali) throws ComponentException {
        Esenzioni_addcomBulk Esenzioni_addcom = null;
        try {
            ComuneHome home = (ComuneHome) getHome(usercontext, ComuneBulk.class);
            ComuneBulk comune = home.findComune(usercontext, esenzioni_addizionali.getCd_catastale());
            if (comune != null && comune.getPg_comune() != null && (esenzioni_addizionali.getDs_comune().toUpperCase().startsWith(comune.getDs_comune().toUpperCase()))) {
                Esenzioni_addcomHome home_esenzioni = (Esenzioni_addcomHome) getHome(usercontext, Esenzioni_addcomBulk.class);
                Esenzioni_addcom = home_esenzioni.findEsenzione(usercontext, comune);
            } else if (comune != null && comune.getPg_comune() != null && !(esenzioni_addizionali.getDs_comune().toUpperCase().startsWith(comune.getDs_comune().toUpperCase()))) {
                //throw new ApplicationException("Corrispondenza Descrizione comune non trovata "+esenzioni_addizionali.getCd_catastale()+" - "+esenzioni_addizionali.getDs_comune()+" - "+comune.getDs_comune());
                Esenzioni_addcomHome home_esenzioni = (Esenzioni_addcomHome) getHome(usercontext, Esenzioni_addcomBulk.class);
                Esenzioni_addcom = home_esenzioni.findEsenzione(usercontext, comune);
                esenzioni_addizionali.setNota("Corrispondenza Descrizione comune non trovata " + esenzioni_addizionali.getCd_catastale() + " - " + esenzioni_addizionali.getDs_comune() + " - " + comune.getDs_comune());
            } else
                throw new ApplicationException("Comune non trovato - Codice Catastale: " + esenzioni_addizionali.getCd_catastale() + " - " + esenzioni_addizionali.getDs_comune());

            if ((Esenzioni_addcom != null && Esenzioni_addcom.getImporto().compareTo(new java.math.BigDecimal(0)) != 0 && Esenzioni_addcom.getImporto().compareTo(esenzioni_addizionali.getImporto()) != 0) || (Esenzioni_addcom != null && esenzioni_addizionali.getNota() != null)) {
                esenzioni_addizionali.setOld_importo(Esenzioni_addcom.getImporto());
                esenzioni_addizionali.setToBeCreated();
                creaConBulk(usercontext, esenzioni_addizionali);
                return esenzioni_addizionali;
            } else if (Esenzioni_addcom == null) {
                esenzioni_addizionali.setOld_importo(new java.math.BigDecimal(0));
                if (esenzioni_addizionali.getImporto().compareTo(esenzioni_addizionali.getOld_importo()) != 0) {
                    esenzioni_addizionali.setToBeCreated();
                    creaConBulk(usercontext, esenzioni_addizionali);
                    return esenzioni_addizionali;
                } else
                    return null;
            } else
                return null;
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    public void Aggiornamento(UserContext usercontext, Esenzioni_addizionaliBulk esenzioni_addizionali) throws ComponentException {
        try {
            Esenzioni_addcomBulk Esenzioni_addcom = null;
            Esenzioni_addcomBulk nuovo_Esenzioni_addcom = null;
            for (Iterator i = esenzioni_addizionali.getDettagli().iterator(); i.hasNext(); ) {
                Esenzioni_addizionaliBulk nuova = (Esenzioni_addizionaliBulk) i.next();
                ComuneHome home = (ComuneHome) getHome(usercontext, ComuneBulk.class);
                ComuneBulk comune = home.findComune(usercontext, nuova.getCd_catastale());
                if (comune != null) {
                    Esenzioni_addcomHome home_esenzioni = (Esenzioni_addcomHome) getHome(usercontext, Esenzioni_addcomBulk.class);
                    Esenzioni_addcom = home_esenzioni.findEsenzione(usercontext, comune);
                }
                if (Esenzioni_addcom != null) {
                    Esenzioni_addcom.setToBeUpdated();
                    Esenzioni_addcom.setDt_fine_validita(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
                    modificaConBulk(usercontext, Esenzioni_addcom);
                } else {
                    Esenzioni_addcom = new Esenzioni_addcomBulk();
                    Esenzioni_addcom.setCd_catastale(nuova.getCd_catastale());
                    Esenzioni_addcom.setPg_comune(comune.getPg_comune());
                }
                nuovo_Esenzioni_addcom = Esenzioni_addcom;
                java.util.GregorianCalendar gc = getGregorianCalendar();
                gc.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
                gc.add(java.util.Calendar.DAY_OF_YEAR, +1);
                nuovo_Esenzioni_addcom.setDt_inizio_validita(new Timestamp(gc.getTime().getTime()));
                nuovo_Esenzioni_addcom.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
                nuovo_Esenzioni_addcom.setImporto(nuova.getImporto());
                nuovo_Esenzioni_addcom.setCrudStatus(OggettoBulk.TO_BE_CREATED);
                super.creaConBulk(usercontext, nuovo_Esenzioni_addcom);
                nuova.setToBeDeleted();
                eliminaConBulk(usercontext, nuova);
            }

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }

    }

    protected java.util.GregorianCalendar getGregorianCalendar() {

        java.util.GregorianCalendar gc = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();

        gc.set(java.util.Calendar.HOUR, 0);
        gc.set(java.util.Calendar.MINUTE, 0);
        gc.set(java.util.Calendar.SECOND, 0);
        gc.set(java.util.Calendar.MILLISECOND, 0);
        gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

        return gc;
    }

    public void cancella_pendenti(UserContext usercontext) throws ComponentException {
        Esenzioni_addizionaliHome home = (Esenzioni_addizionaliHome) getHome(usercontext, Esenzioni_addizionaliBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        try {
            List canc = home.fetchAll(sql);
            for (Iterator i = canc.iterator(); i.hasNext(); ) {
                Esenzioni_addizionaliBulk bulk = (Esenzioni_addizionaliBulk) i.next();
                bulk.setToBeDeleted();
                eliminaConBulk(usercontext, bulk);
            }
        } catch (PersistencyException e) {
            handleException(e);
        }
    }

    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        if (bulk instanceof Esenzioni_addcomBulk) {
            Esenzioni_addcomBulk esenzione = (Esenzioni_addcomBulk) bulk;
            valida(userContext, esenzione);
            return super.creaConBulk(userContext, bulk);
        } else
            return super.creaConBulk(userContext, bulk);
    }

    private void valida(UserContext userContext, Esenzioni_addcomBulk esenzione) throws ComponentException {

        try {
            Esenzioni_addcomHome home = (Esenzioni_addcomHome) getHome(userContext, esenzione);
            home.validaPeriodoInCreazione(userContext, esenzione);

        } catch (Throwable ex) {
            throw handleException(ex);
        }
    }

    /**
     * Viene richiesta l'eliminazione dell'Oggetto bulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Cancellazione di un intervallo futuro (cancellazione fisica)
     * Pre: Viene richiesta la cancellazione di un oggetto bulk con data inizio validita successiva alla data odierna
     * Post: L'oggetto bulk specificato viene cancellato fisicamente dalla Tabella e la versione precedente del record
     * (se esiste) viene aggiornata impostanto la sua data Fine validita ad infinito (31/12/2200)
     * <p>
     * Nome: Cancellazione di un intervallo attivo (cancellazione logica)
     * Pre: Viene richiesta la cancellazione di un oggetto bulk con data inizio validita precedente alla Data odierna
     * Post: Imposto la data Fine validita dell'oggetto a data odierna e aggiorno il record della Tabella
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk        l'OggettoBulk da eliminare
     **/
    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {
            if (bulk instanceof Esenzioni_addcomBulk) {
                Esenzioni_addcomBulk esenzione = (Esenzioni_addcomBulk) bulk;

                java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

                if (esenzione.getDt_inizio_validita().compareTo(dataOdierna) > 0) {
                    Esenzioni_addcomHome home = (Esenzioni_addcomHome) getHome(userContext, esenzione);
                    Esenzioni_addcomBulk tipoPrec = home.findIntervalloPrecedente(esenzione, true);
                    if (tipoPrec != null) {
                        tipoPrec.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
                        updateBulk(userContext, tipoPrec);
                    }
                    super.eliminaConBulk(userContext, esenzione);
                } else {
                    esenzione.setDt_fine_validita(dataOdierna);
                    updateBulk(userContext, esenzione);
                }
            } else
                super.eliminaConBulk(userContext, bulk);
        } catch (javax.ejb.EJBException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.bulk.BusyResourceException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.bulk.OutdatedResourceException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
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
		Esenzioni_addcomHome home = (Esenzioni_addcomHome) getHome(usercontext, oggettobulk, "COMUNE");
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(compoundfindclause);
		sql.generateJoin("comune", "COMUNE");
		return sql;
	}

}
 