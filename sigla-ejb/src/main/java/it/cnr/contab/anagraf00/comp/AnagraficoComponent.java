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

import feign.FeignException;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.EcfBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.EcfHome;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.util.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaHome;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.incarichi00.bp.CRUDIncarichiProceduraBP;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneHome;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.SendMail;
import it.cnr.si.service.AceService;
import it.cnr.si.service.dto.anagrafica.enums.TipoAppartenenza;
import it.cnr.si.service.dto.anagrafica.enums.TipoContratto;
import it.cnr.si.service.dto.anagrafica.letture.PersonaEntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.letture.PersonaWebDto;
import it.cnr.si.service.dto.anagrafica.scritture.PersonaEntitaOrganizzativaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import javax.ejb.EJBException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Reader;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e filtro dei dati
 * immessi o richiesti dall'utente. In oltre sovrintende alla gestione e creazione dati a cui
 * l'utente stesso non ha libero accesso e/o non gli sono trasparenti.
 */

public class AnagraficoComponent extends UtilitaAnagraficaComponent implements ICRUDMgr, IAnagraficoMgr, IPrintMgr {
    private transient static final Logger logger = LoggerFactory.getLogger(AnagraficoComponent.class);
    private static final String ACCESSO_MODIFICA_STRUTTURA_CNR = "CFGANAGCFCOREANAGSOM";
    private AceService aceService;

    public AnagraficoComponent() {
        try {
            aceService = SpringUtil.getBean("aceService", AceService.class);
        } catch (NoSuchBeanDefinitionException _ex) {
            logger.warn("URL of ACE is not defined");
        }
    }

    protected void adeguaDt_fine_rapportoTerzi(UserContext userContext, AnagraficoBulk anagrafico) throws it.cnr.jada.comp.ComponentException {
        try {
            TerzoHome home = (TerzoHome) getHome(userContext, TerzoBulk.class);

            SQLBuilder sql = home.createSQLBuilder();
            sql.addClause("AND", "cd_anag", sql.EQUALS, anagrafico.getCd_anag());
            it.cnr.jada.persistency.Broker broker = home.createBroker(sql);

            while (broker.next()) {
                TerzoBulk terzo = (TerzoBulk) broker.fetch(TerzoBulk.class);
                // su tutti i terzi con dt_fine_rapporto nulla o superiore
                // alla dt_fine_validita dell'anagrafico.
                if (terzo.getDt_fine_rapporto() == null ||
                        terzo.getDt_fine_rapporto().after(anagrafico.getDt_fine_rapporto())) {
                    terzo.setDt_fine_rapporto(anagrafico.getDt_fine_rapporto());
                    updateBulk(userContext, terzo);
                }
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public String calcolaCodiceFiscale(UserContext userContext, AnagraficoBulk anagBulk) throws it.cnr.jada.comp.ComponentException {
        GregorianCalendar dataNascita = new GregorianCalendar();
        dataNascita.setTime(new java.util.Date(anagBulk.getDt_nascita().getTime()));

        String cd_catastale = null;
        if (anagBulk.getComune_nascita() != null) {
            if (NazioneBulk.ITALIA.equals(anagBulk.getComune_nascita().getTi_italiano_estero()))
                cd_catastale = anagBulk.getComune_nascita().getCd_catastale();
            else if (anagBulk.getComune_nascita().getNazione() != null)
                cd_catastale = anagBulk.getComune_nascita().getNazione().getCd_catastale();
        }

        return CodiceFiscaleControllo.makeCodiceFiscale(
                anagBulk.getCognome(),
                anagBulk.getNome(),
                "" + (dataNascita.get(GregorianCalendar.YEAR) % 100),
                "" + dataNascita.get(GregorianCalendar.MONTH),
                "" + dataNascita.get(GregorianCalendar.DAY_OF_MONTH),
                anagBulk.getTi_sesso(),
                cd_catastale);
    }

    /**
     * Prima di richiamare il metodo cerca di <code>CRUDComponent</code> vengono reimpostati
     * i valori dei radio button che non sono visibili all'utente nell'interfaccia selezionata.
     * <p>
     * Nome: Restituire un risultato di ricerca;
     * Pre:  Restituire un elenco di oggetti con i parametri uguali a quelli impostati;
     * Post: Prima di avviare la ricerca ripulisce le condizioni da eventuali "dati sporchi".
     *
     * @param clausole <code>CompoundFindClause</code>
     * @param bulk     <code>AnagraficoBulk</code> di modello per la ricerca.
     * @return <code>RemoteIterator</code> Risultato della ricerca.
     */

    public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clausole, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = (it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk) bulk;
        if (anagrafico.isPersonaFisica()) {
            anagrafico.setTi_entita_giuridica(null);
        } else if (anagrafico.isPersonaGiuridica()) {
            anagrafico.setTi_entita_fisica(null);
        } else {
            //anagrafico.setTi_italiano_estero(NazioneBulk.ITALIA);
            anagrafico.setTi_entita_giuridica(null);
            anagrafico.setTi_entita_fisica(null);
        }
        return super.cerca(userContext, clausole, bulk);
    }

    private void controllaCodiceFiscale(AnagraficoBulk anagrafico) throws it.cnr.contab.anagraf00.util.ExCodiceFiscale {

        GregorianCalendar dataNascita = new GregorianCalendar();
        dataNascita.setTime(new java.util.Date(anagrafico.getDt_nascita().getTime()));

        String cd_catastale = null;
        if (anagrafico.getComune_nascita() != null) {
            if (NazioneBulk.ITALIA.equals(anagrafico.getComune_nascita().getTi_italiano_estero()))
                cd_catastale = anagrafico.getComune_nascita().getCd_catastale();
            else if (anagrafico.getComune_nascita().getNazione() != null)
                cd_catastale = anagrafico.getComune_nascita().getNazione().getCd_catastale();
        }

        it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.parseCodiceFiscale(
                anagrafico.getCognome(),
                anagrafico.getNome(),
                String.valueOf(dataNascita.get(GregorianCalendar.YEAR) % 100),
                String.valueOf(dataNascita.get(GregorianCalendar.MONTH)),
                String.valueOf(dataNascita.get(GregorianCalendar.DAY_OF_MONTH)),
                anagrafico.getTi_sesso(),
                cd_catastale,
                anagrafico.getCodice_fiscale());

    }

    /**
     * <B>A correzione dell'errore No. 02237A</B>
     * <p>
     * Metodo creato per il controllo del Nome e del Cognome di una Persona Fisica.
     * Il metodo è invocato durante la validazione di un Anagrafica per Creazione,
     * (metodo validaCreaModificaConBulk), e controlla, nel caso di una Persona Fisica,
     * che il Cognome ed il nome non contengano caratteri numerici.
     * Questo metodo potrebbe anche essere spostato nella Component <code>UtilitaAnagraficaComponent</code>.
     * <p>
     * Creation date: (22/08/2002 12.10.44)
     *
     * @param anagrafico <code>AnagraficoBulk</code> il bulk da controllare
     */
    private void controllaNomeCognome(AnagraficoBulk anagrafico) throws ApplicationException {
	
	/* Si potrebbe anche controllare se cognome e nome sono null, spostando, così, 
		il controllo dal valida a quì! */

        char trattino = '-';
        char apostrofo = '\'';

        if (anagrafico.isPersonaFisica()) {
            char[] cognome = anagrafico.getCognome().toCharArray();
            char[] nome = anagrafico.getNome().toCharArray();

            // Controlla il Cognome
            for (int i = 0; i < cognome.length; i++) {
                if (!Character.isLetter(cognome[i]) &&
                        !Character.isSpaceChar(cognome[i]) &&
                        cognome[i] != apostrofo &&
                        cognome[i] != trattino) {
                    throw new ApplicationException("Attenzione: Cognome non valido. Il Cognome può contenere solo caratteri alfabetici.");
                }
            }

            // Controlla il Nome
            for (int i = 0; i < nome.length; i++) {
                if (!Character.isLetter(nome[i]) &&
                        !Character.isSpaceChar(nome[i]) &&
                        nome[i] != apostrofo &&
                        nome[i] != trattino) {
                    throw new ApplicationException("Attenzione: Nome non valido. Il Nome può contenere solo caratteri alfabetici.");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        valorizzaDatiFatturazioneElettronica(userContext, bulk);
        AnagraficoBulk anagrafico = (AnagraficoBulk) super.creaConBulk(userContext, bulk);
        creaDefaultTerzo(userContext, anagrafico);
        calcolaMontantePerPagamentoEsterno(userContext, anagrafico, true);
        //try{
        //// Controlla ed, eventualmente, crea l'ANAGRAFICO_ESERCIZIO
        //if (anagrafico.getAnagrafico_esercizio() != null &&
        //anagrafico.getAnagrafico_esercizio().getFl_notaxarea() != null &&
        //anagrafico.getAnagrafico_esercizio().getFl_notaxarea().booleanValue()){

        //anagrafico.getAnagrafico_esercizio().setCd_anag(anagrafico.getCd_anag());

        //insertBulk(userContext, anagrafico.getAnagrafico_esercizio());
        //}
        //} catch (it.cnr.jada.persistency.PersistencyException e){
        //throw new ComponentException (e);
        //}

        validaCreaModificaAnagrafico_esercizio(userContext, anagrafico);

        return anagrafico;
    }

    private void valorizzaDatiFatturazioneElettronica(UserContext userContext, OggettoBulk bulk)
            throws ComponentException {
        IpaServFattBulk ipa = recuperoDatiIpa(userContext, bulk);
        if (ipa != null) {
            ((AnagraficoBulk) bulk).setCodiceAmministrazioneIpa(ipa.getCodAmm());
            ((AnagraficoBulk) bulk).setDataAvvioFattElettr(ipa.getDataAvvioSfe());
        } else {
            ((AnagraficoBulk) bulk).setCodiceAmministrazioneIpa(null);
            if (((AnagraficoBulk) bulk).getDataAvvioFattElettr() == null){
                	try {
                		((AnagraficoBulk) bulk).setDataAvvioFattElettr(Utility.createConfigurazioneCnrComponentSession().
                				getDt01(userContext, CNRUserContext.getEsercizio(userContext), null,"FATTURAZIONE_ELETTRONICA", "INIZIO_TRA_PRIVATI"));
                	} catch (RemoteException e) {
                		throw handleException(e);
                	} catch (javax.ejb.EJBException ex) {
                		throw handleException(ex);
                	}
            }
        }
    }

    private IpaServFattBulk recuperoDatiIpa(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            if (((AnagraficoBulk) bulk).getCodice_fiscale() == null) {
                return null;
            }
            IpaServFattHome ipaHome = (IpaServFattHome) getHome(userContext, IpaServFattBulk.class);
            SQLBuilder sql = ipaHome.createSQLBuilder();
            sql.addClause("AND", "cf", SQLBuilder.EQUALS, ((AnagraficoBulk) bulk).getCodice_fiscale());
            sql.addOrderBy("dt_Verifica_Cf desc");
            List<IpaServFattBulk> listaIPA = ipaHome.fetchAll(sql);
            if (!listaIPA.isEmpty())
                return listaIPA.get(0);
            return null;
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    public void eliminaBulkForSIP(UserContext userContext, String cd_terzo) throws it.cnr.jada.comp.ComponentException {
        try {
            TerzoHome terzoHome = (TerzoHome) getHome(userContext, TerzoBulk.class);
            SQLBuilder sql = terzoHome.createSQLBuilder();
            sql.addClause("AND", "cd_terzo", sql.EQUALS, cd_terzo);
            List listaTerzi = terzoHome.fetchAll(sql);
            if (listaTerzi.isEmpty())
                throw new TerzoNonPresenteSIPException();
            for (Iterator terzi = listaTerzi.iterator(); terzi.hasNext(); ) {
                TerzoBulk terzo = (TerzoBulk) terzi.next();
                AnagraficoBulk anagrafico = (AnagraficoBulk) getHome(userContext, AnagraficoBulk.class).findByPrimaryKey(new AnagraficoBulk(terzo.getCd_anag()));
                if (!anagrafico.getUtuv().equalsIgnoreCase(userContext.getUser()))
                    throw new ApplicationException("Terzo non cancellabile, in quanto aggiornato nel Sistema Contabile");
                anagrafico.setToBeDeleted();
                eliminaConBulk(userContext, anagrafico);
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    public java.util.List bulkForSIP(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            AnagraficoBulk anagrafico = (AnagraficoBulk) bulk;
            anagrafico.setNazionalita((NazioneBulk) getHome(userContext, NazioneBulk.class).findByPrimaryKey(anagrafico.getNazionalita()));
            anagrafico.setComune_fiscale((ComuneBulk) getHome(userContext, ComuneBulk.class).findByPrimaryKey(anagrafico.getComune_fiscale()));
            getHomeCache(userContext).fetchAll(userContext);
            anagrafico.setTi_italiano_estero(anagrafico.getNazionalita().getTi_nazione());
            if (anagrafico.getPartita_iva() != null || anagrafico.getCodice_fiscale() != null) {
                try {
                    SQLBuilder sql = getHome(userContext, anagrafico).createSQLBuilder();
                    sql.openParenthesis("AND");
                    sql.addClause("OR", "partita_iva", sql.EQUALS, anagrafico.getPartita_iva());
                    sql.addClause(
                            "OR",
                            "codice_fiscale",
                            sql.EQUALS,
                            anagrafico.getCodice_fiscale());
                    sql.closeParenthesis();
                    if (!anagrafico.isToBeCreated())
                        sql.addClause("AND", "cd_anag", sql.NOT_EQUALS, anagrafico.getCd_anag());
                    if (sql.executeExistsQuery(getConnection(userContext)))
                        throw new DuplicateKeyException();
                } catch (java.sql.SQLException e) {
                    throw handleException(e);
                }
            }
            if (anagrafico.getTi_entita().equalsIgnoreCase(AnagraficoBulk.GIURIDICA)
                    && NazioneBulk.ITALIA.equals(anagrafico.getTi_italiano_estero())
                    && anagrafico.getPartita_iva() != null)
                anagrafico.setCodice_fiscale(anagrafico.getPartita_iva());
            if (anagrafico.isToBeCreated())
                anagrafico = (AnagraficoBulk) creaConBulk(userContext, bulk);
            else if (anagrafico.isToBeUpdated()) {
                anagrafico = (AnagraficoBulk) modificaConBulk(userContext, bulk);
                modificaDefaultTerzoForSIP(userContext, anagrafico);
            }
            V_terzo_anagrafico_sipHome home = (V_terzo_anagrafico_sipHome) getHome(userContext, V_terzo_anagrafico_sipBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
            return home.fetchAll(sql);
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        }
    }

    protected void creaDefaultTerzo(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        try {
            TerzoBulk terzo = new TerzoBulk();

            terzo.setDenominazione_sede(anagrafico.getRagione_sociale());
            if (terzo.getDenominazione_sede() == null) {
                StringBuffer denominazione_sede = new StringBuffer();
                if (anagrafico.getCognome() != null)
                    denominazione_sede.append(anagrafico.getCognome());
                denominazione_sede.append(' ');
                if (anagrafico.getNome() != null)
                    denominazione_sede.append(anagrafico.getNome());
                terzo.setDenominazione_sede(denominazione_sede.toString().trim());
            }

            int dim = getHome(userContext, terzo).getColumnMap().getMappingForProperty("denominazione_sede").getColumnSize();
            if (terzo.getDenominazione_sede().length() > dim)
                terzo.setDenominazione_sede(terzo.getDenominazione_sede().substring(0, dim - 3) + "...");

            terzo.setTi_terzo(terzo.ENTRAMBI);
            terzo.setComune_sede(anagrafico.getComune_fiscale());
            terzo.setFrazione_sede(anagrafico.getFrazione_fiscale());
            terzo.setVia_sede(anagrafico.getVia_fiscale());
            terzo.setNumero_civico_sede(anagrafico.getNum_civico_fiscale());
            terzo.setCap_comune_sede(anagrafico.getCap_comune_fiscale());
            terzo.setCaps_comune(anagrafico.getCaps_comune());
            terzo.setFlSbloccoFatturaElettronica(false);
            terzo.setAnagrafico(anagrafico);
            terzo.setUser(Optional.ofNullable(anagrafico.getUser()).orElseGet(() -> userContext.getUser()));
            insertBulk(userContext, terzo);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    protected void modificaDefaultTerzoForSIP(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        try {
            AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
            for (Iterator terzi = anagraficoHome.findTerzi(anagrafico).iterator(); terzi.hasNext(); ) {
                TerzoBulk terzo = (TerzoBulk) terzi.next();
                terzo.setDenominazione_sede(anagrafico.getRagione_sociale());
                if (terzo.getDenominazione_sede() == null) {
                    StringBuffer denominazione_sede = new StringBuffer();
                    if (anagrafico.getCognome() != null)
                        denominazione_sede.append(anagrafico.getCognome());
                    denominazione_sede.append(' ');
                    if (anagrafico.getNome() != null)
                        denominazione_sede.append(anagrafico.getNome());
                    terzo.setDenominazione_sede(denominazione_sede.toString().trim());
                }

                int dim = getHome(userContext, terzo).getColumnMap().getMappingForProperty("denominazione_sede").getColumnSize();
                if (terzo.getDenominazione_sede().length() > dim)
                    terzo.setDenominazione_sede(terzo.getDenominazione_sede().substring(0, dim - 3) + "...");

                terzo.setTi_terzo(TerzoBulk.ENTRAMBI);
                terzo.setComune_sede(anagrafico.getComune_fiscale());
                terzo.setFrazione_sede(anagrafico.getFrazione_fiscale());
                terzo.setVia_sede(anagrafico.getVia_fiscale());
                terzo.setNumero_civico_sede(anagrafico.getNum_civico_fiscale());
                terzo.setCap_comune_sede(anagrafico.getCap_comune_fiscale());
                terzo.setCaps_comune(anagrafico.getCaps_comune());
                terzo.setAnagrafico(anagrafico);
                terzo.setUser(userContext.getUser());
                terzo.setToBeUpdated();
                updateBulk(userContext, terzo);
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Viene sottomessa la richiesta di cancellazione dell'<code>OggettoBulk</code> passato in
     * ingresso. Se viene rilevata una <code>ReferentialIntegrityException</code> si effettua
     * l'assegnazione, in cascata, della data di fine rapporto, per l'elemento e i sui "figli".
     * <p>
     * Nome: Eliminare un oggetto anagrafico;
     * Pre:  Effettuare l'eliminazione dell'oggetto anagrafico;
     * Post: Se l'anagrafica ha ancora dei riferimenti anziche effettuare una cancellazione fisica si procede a impostare
     * la data di fine rapporto per tutti gli elementi associati e l'anagrafica stessa.
     *
     * @param bulk <code>OggettoBulk</code> da eliminare.
     */

    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            AnagraficoBulk anagrafico = (AnagraficoBulk) bulk;
            if (!isAnagraficaModificabile(userContext, anagrafico))
                throw new ApplicationException("Non si hanno i diritti per modificare una struttura CNR.");

            super.makeBulkPersistent(userContext, bulk);
        } catch (it.cnr.jada.persistency.sql.ReferentialIntegrityException rie) {
	/*  Angelo 03/01/05	Se ci sono dei dettagli non imposto la data di fine
	try {
			AnagraficoBulk anagrafico = (AnagraficoBulk)bulk;
			java.sql.Timestamp dt_odierna = getHome(userContext,anagrafico).getServerDate();
			anagrafico.setCrudStatus(anagrafico.TO_BE_UPDATED);
			lockBulk(userContext,anagrafico);
			// Imposto sull'anagrafico la data di fine rapporto alla data odierna 
			anagrafico.setDt_fine_rapporto(dt_odierna);
			adeguaDt_fine_rapportoTerzi(userContext,anagrafico);
			modificaConBulk(userContext,anagrafico);
		} catch(Throwable e) {
			throw handleException(bulk,rie);
		}*/
            throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }
    }

    public AnagraficoBulk getAnagraficoEnte(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configurazione = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
            AnagraficoBulk ente = (AnagraficoBulk) getHome(userContext, AnagraficoBulk.class).findByPrimaryKey(
                    new AnagraficoBulk(
                            new Integer(
                                    configurazione.getIm01(userContext, new Integer(0), null, "COSTANTI", "CODICE_ANAG_ENTE").toString()
                            )
                    )
            );
            ente.setComune_fiscale((ComuneBulk) getHome(userContext, ComuneBulk.class).findByPrimaryKey(new ComuneBulk(ente.getPg_comune_fiscale())));
            ente.setComune_nascita((ComuneBulk) getHome(userContext, ComuneBulk.class).findByPrimaryKey(new ComuneBulk(ente.getPg_comune_nascita())));
            return ente;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public TerzoBulk getDefaultTerzo(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        try {
            BulkHome home = getHome(userContext, TerzoBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addClause("AND", "cd_anag", sql.EQUALS, anagrafico.getCd_anag());
            sql.addOrderBy("CD_TERZO");
            it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
            if (!broker.next()) return null;
            TerzoBulk terzo = (TerzoBulk) broker.fetch(TerzoBulk.class);
            broker.close();
            getHomeCache(userContext).fetchAll(userContext);
            terzo.setTerzo_speciale(isTerzoSpeciale(userContext, terzo));
            return terzo;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Genera un nuovo oggetto <code>AnagraficoBulk</code> "pulito" con delle
     * preimpostazioni dei radio button e i check button.
     * <p>
     * Nome: Impostazioni di default proposte dal programma;
     * Pre:  Il programma deve proposte come impostazioni di default le opzioni: Italiana,  Persona fisica,
     * Ditta individuale;
     * Post: Vengono proposte di default le opzioni: "Italiana", "Persona fisica", "Ditta individuale" e sulla
     * selezione di "Persona giuridica" viene proposto "Altro".
     * <p>
     * Nome: Flag IVA;
     * Pre:  Flag Soggetto IVA selezionato di default;
     * Post: Il Flag Soggetto IVA viene selezionato di default. Se li flag è selezionato rende obbligatorio
     * l'inserimento della Partita I.V.A.
     *
     * @param bulk dovrà essere sempore <code>AnagraficoBulk</code>.
     * @return un <code>OggettoBulk</code> che sarà sempre un <code>AnagraficoBulk</code>.
     */

    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        AnagraficoBulk anagrafico = (AnagraficoBulk) super.inizializzaBulkPerInserimento(userContext, bulk);

        if (anagrafico.getTi_italiano_estero() == null) {
            anagrafico.setTi_italiano_estero(NazioneBulk.ITALIA);
        }
        if (anagrafico.getTi_entita_persona_struttura() == anagrafico.ENTITA_STRUTTURA) {
            anagrafico.setTi_entita(AnagraficoBulk.STRUT_CNR);
            anagrafico.setTi_entita_giuridica(null);
            anagrafico.setTi_entita_fisica(null);
        } else {
            anagrafico.setTi_entita(AnagraficoBulk.FISICA);
            anagrafico.setTi_entita_giuridica(AnagraficoBulk.ALTRO);
            anagrafico.setTi_entita_fisica(AnagraficoBulk.ALTRO);

        }

        anagrafico.setFl_fatturazione_differita(Boolean.FALSE);
        anagrafico.setFl_occasionale(Boolean.FALSE);
        anagrafico.setFl_soggetto_iva(Boolean.FALSE);

        anagrafico.setFl_cervellone(Boolean.FALSE);
        anagrafico.setFl_non_obblig_p_iva(Boolean.FALSE);
        anagrafico.setFl_studio_associato(Boolean.FALSE);
        anagrafico.setFl_speciale(Boolean.FALSE);
        // Crea un nuovo ANGRAFICO_ESERCIZIO e lo imposta il CRUD Status a ToBeCreated
        Anagrafico_esercizioBulk anagrafico_esercizio = new Anagrafico_esercizioBulk(null, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
        anagrafico_esercizio.setIm_detrazione_personale_anag(new java.math.BigDecimal(0));
        anagrafico_esercizio.setIm_deduzione_family_area(new java.math.BigDecimal(0));
        anagrafico_esercizio.setIm_reddito_complessivo(new java.math.BigDecimal(0));
        anagrafico_esercizio.setIm_reddito_abitaz_princ(new java.math.BigDecimal(0));
        anagrafico_esercizio.setFl_no_credito_irpef(Boolean.FALSE);
        anagrafico_esercizio.setFl_no_credito_cuneo_irpef(Boolean.FALSE);
        anagrafico_esercizio.setFl_no_detr_cuneo_irpef(Boolean.FALSE);
        anagrafico_esercizio.setFl_detrazioni_altri_tipi(Boolean.FALSE);
        anagrafico_esercizio.setToBeCreated();
        anagrafico.setAnagrafico_esercizio(anagrafico_esercizio);
        anagrafico.setFl_sospensione_irpef(Boolean.FALSE);

        anagrafico.setFl_abilita_diaria_miss_est(Boolean.FALSE);

        return anagrafico;
    }

    /**
     * Carica un anagrafica con tutti i dati correlati.
     * <p>
     * Nome: Inizializzazione;
     * Pre:  Preparare l'oggetto alle modifiche;
     * Post: Si procede, oltre che alla normare procedura di inizializzazione di un oggetto bulk,
     * anche al caricamento di tutti gli elementi associati all'anagrafica in modifica.
     *
     * @param bulk dovrà essere sempore <code>AnagraficoBulk</code>.
     * @return un <code>OggettoBulk</code> che sarà sempre un <code>AnagraficoBulk</code>.
     */

    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            AnagraficoBulk anagrafico = (AnagraficoBulk) super.inizializzaBulkPerModifica(userContext, bulk);
            AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
            //anagrafico.setTerzi(new BulkList(anagraficoHome.findTerzi(anagrafico)));
            anagrafico.setRapporti(new BulkList(anagraficoHome.findRapporti(anagrafico)));
            anagrafico.setAssGruppoIva(new BulkList(anagraficoHome.findAssGruppoIva(anagrafico)));
            anagrafico.setDichiarazioni_intento(new BulkList(anagraficoHome.findDichiarazioni_intento(anagrafico)));
            anagrafico.setCarichi_familiari_anag(new BulkList(anagraficoHome.findCarichi_familiari_anag(anagrafico)));
            anagrafico.setPagamenti_esterni(new BulkList(anagraficoHome.findPagamenti_esterni(anagrafico)));
            if (anagrafico.STRUT_CNR.equals(anagrafico.getTi_entita()))
                anagrafico.setTi_entita_persona_struttura(anagrafico.ENTITA_STRUTTURA);
            else
                anagrafico.setTi_entita_persona_struttura(anagrafico.ENTITA_PERSONA);
            //??????????????
            //anagrafico.setCervello(anagrafico.getFl_cervelloni());


            //java.util.Collection details = anagraficoHome.findContatti(anagrafico);
            //for (java.util.Iterator i = details.iterator();i.hasNext();) {
            //ContattoBulk contatto = (ContattoBulk)i.next();
            //contatto.getTerzo().addToContatti(contatto);
            //}

            //details = anagraficoHome.findBanca(anagrafico);
            //for (java.util.Iterator i = details.iterator();i.hasNext();) {
            //BancaBulk banca = (BancaBulk)i.next();
            //banca.getTerzo().addToBanche(banca);
            //}
            //details = anagraficoHome.findTermini_pagamento(anagrafico);
            //for (java.util.Iterator i = details.iterator();i.hasNext();) {
            //Termini_pagamentoBulk termini_pagamento = (Termini_pagamentoBulk)i.next();
            //termini_pagamento.getTerzo().addToTermini_pagamento(termini_pagamento);
            //}

            //details = anagraficoHome.findModalita_pagamento(anagrafico);
            //for (java.util.Iterator i = details.iterator();i.hasNext();) {
            //Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk)i.next();
            //modalita_pagamento.getTerzo().addToModalita_pagamento(modalita_pagamento);
            //}

            //details = anagraficoHome.findTelefoni(anagrafico, getHomeCache(userContext).getHome(TelefonoBulk.class,"V_TELEFONO_ANAG"));
            //for (java.util.Iterator i = details.iterator();i.hasNext();) {
            //TelefonoBulk telefoni = (TelefonoBulk)i.next();
            //if(TelefonoBulk.TEL.equals(telefoni.getTi_riferimento()))   telefoni.getTerzo().addToTelefoni(telefoni);
            //else if(TelefonoBulk.EMAIL.equals(telefoni.getTi_riferimento())) telefoni.getTerzo().addToEmail(telefoni);
            //else if(TelefonoBulk.FAX.equals(telefoni.getTi_riferimento()))   telefoni.getTerzo().addToFax(telefoni);
            //}

            //for (java.util.Iterator i = anagrafico.getTerzi().iterator();i.hasNext();) {
            //TerzoBulk terzo = (TerzoBulk)i.next();
            //terzo.setRif_termini_pagamento_disponibili(anagrafico.getRif_termini_pagamento());
            //terzo.setRif_modalita_pagamento_disponibili(anagrafico.getRif_modalita_pagamento());
            //if (terzo.getCd_unita_organizzativa() != null) {
            //terzo.setUnita_organizzativa((it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)getHome(userContext,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class).findByPrimaryKey(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaKey(terzo.getCd_unita_organizzativa())));
            //if (terzo.getUnita_organizzativa() == null) {
            //terzo.setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());
            //terzo.getUnita_organizzativa().setCd_unita_organizzativa(terzo.getCd_unita_organizzativa());
            //terzo.getUnita_organizzativa().setCrudStatus(terzo.NORMAL);
            //}
            //}
            //super.initializeKeysAndOptionsInto(userContext,terzo);
            //}

            java.util.Collection details = anagraficoHome.findInquadramenti(anagrafico);
            for (java.util.Iterator i = details.iterator(); i.hasNext(); ) {
                InquadramentoBulk inquadramento = (InquadramentoBulk) i.next();
                inquadramento.setMax_dt_fin_validita_missione(findMaxFinMissione(userContext, anagrafico));
                inquadramento.getRapporto().getInquadramenti().add(inquadramento);
            }

            anagrafico.setDipendente(anagraficoHome.findRapportoDipendenteFor(anagrafico));
            anagrafico.setUtilizzata(isAnagraficaUtilizzata(userContext, anagrafico));
            anagrafico.setUtilizzata_detrazioni(isAnagraficaUtilizzataDetrazione(userContext, anagrafico));

            /* Cerca una corrispondenza nella tabella ANAGRAFICO_ESERCIZIO;
             *	se non la trova, ne crea uno nuovo e ne imposta l'esercizio con quello di
             *	scrivania.
             */
            Anagrafico_esercizioHome anag_esercHome = (Anagrafico_esercizioHome) getHome(userContext, Anagrafico_esercizioBulk.class);
            Anagrafico_esercizioBulk anagrafico_esercizio = anag_esercHome.findAnagrafico_esercizioFor(userContext, anagrafico, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            if (anagrafico_esercizio != null) {
                anagrafico_esercizio.setToBeUpdated();
            } else {
                anagrafico_esercizio = new Anagrafico_esercizioBulk(anagrafico.getCd_anag(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
                anagrafico_esercizio.setIm_detrazione_personale_anag(new java.math.BigDecimal(0));
                anagrafico_esercizio.setIm_deduzione_family_area(new java.math.BigDecimal(0));
                anagrafico_esercizio.setIm_reddito_complessivo(new java.math.BigDecimal(0));
                anagrafico_esercizio.setIm_reddito_abitaz_princ(new java.math.BigDecimal(0));
                anagrafico_esercizio.setToBeCreated();
            }
            anagrafico.setAnagrafico_esercizio(anagrafico_esercizio);
            anagrafico.setAssociatiStudio(new BulkList(anagraficoHome.findAssociatiStudio(anagrafico)));

            getHomeCache(userContext).fetchAll(userContext);

            if (!isAnagraficaModificabile(userContext, anagrafico))
                return asRO(anagrafico, "Non si hanno diritti per modificare una struttura CNR.");
            if (anagrafico.isSpeciale())
                return asRO(anagrafico, "Anagrafica non modificabile.");
            return anagrafico;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    private void inizializzaBulkPerStampa(UserContext userContext, Stampa_previdenziale_dipendentiVBulk stampa) throws it.cnr.jada.comp.ComponentException {

        stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));

        stampa.setTerzoForPrint(new TerzoBulk());

        stampa.setMatricolaForPrint(new V_prev_dipBulk());

        stampa.setMatricolaForPrintEnabled(true);

        stampa.setTerzoForPrintEnabled(true);

        try {
            String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);

            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
            it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk) uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

            stampa.setUo_scrivania(uo);

            if (!uo.isUoCds()) {
                stampa.setUoForPrint(uo);
                stampa.setIsUOForPrintEnabled(false);
            } else {
                stampa.setUoForPrint(new Unita_organizzativaBulk());
                stampa.setIsUOForPrintEnabled(true);
            }

        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        if (bulk instanceof Stampa_previdenziale_dipendentiVBulk)
            inizializzaBulkPerStampa(userContext, (Stampa_previdenziale_dipendentiVBulk) bulk);

        return bulk;
    }

    private boolean isAnagraficaModificabile(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        try {
            return
                    !anagrafico.isStrutturaCNR() ||
                            ((GestioneLoginComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(userContext, ACCESSO_MODIFICA_STRUTTURA_CNR);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Modifica Anagrafico
     * PreCondition:
     * E' stata generata la richiesta di modificare un Anagrafico.
     * PostCondition:
     * Viene consentito il salvataggio.
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>OggettoBulk</code> il Bulk da modificare
     * @return l'oggetto <code>OggettoBulk</code> modificato
     **/
    public OggettoBulk modificaConBulk(UserContext aUC, OggettoBulk bulk)
            throws ComponentException {


        AnagraficoBulk anagrafico = (AnagraficoBulk) bulk;
        if (anagrafico.getCodice_fiscale() != null) {
            try {
                AnagraficoBulk anagraficoDB = (AnagraficoBulk) getHome(aUC, AnagraficoBulk.class).findByPrimaryKey(new AnagraficoBulk(anagrafico.getCd_anag()));
                if ((anagraficoDB.getCodice_fiscale() != null && !anagraficoDB.getCodice_fiscale().equals(anagrafico.getCodice_fiscale())) || anagraficoDB.getCodice_fiscale() == null) {
                    valorizzaDatiFatturazioneElettronica(aUC, anagrafico);
                }
            } catch (PersistencyException e1) {
                // TODO Auto-generated catch block
                throw new ComponentException(e1);
            }
        }

        if (anagrafico.getCodiceAmministrazioneIpa() == null) {
            TerzoHome home = (TerzoHome) getHome(aUC, TerzoBulk.class);

            SQLBuilder sql = home.createSQLBuilder();
            sql.addClause("AND", "cd_anag", SQLBuilder.EQUALS, anagrafico.getCd_anag());
            it.cnr.jada.persistency.Broker broker;
            try {
                broker = home.createBroker(sql);
                while (broker.next()) {
                    TerzoBulk terzo = (TerzoBulk) broker.fetch(TerzoBulk.class);

                    if (terzo.getCodiceUnivocoUfficioIpa() != null)
                        throw new ApplicationException("Il codice Amministrazione dell'IPA deve essere valorizzato perchè esiste il terzo " + terzo.getCd_terzo() + " con il codice Ufficio IPA valorizzato.");
                }
            } catch (PersistencyException e) {
                // TODO Auto-generated catch block
                throw handleException(e);
            }

        }
        calcolaMontantePerPagamentoEsterno(aUC, (AnagraficoBulk) bulk);
        /* Segnalazione Err. CNR 643 - BORRIELLO	*/
        //if (!isItalianoEsteroModificabile(aUC, (AnagraficoBulk)bulk)){
        if (anagrafico.getComune_fiscale() != null &&
                anagrafico.getComune_fiscale().getTi_italiano_estero() != null &&
                (anagrafico.getComune_fiscale().getTi_italiano_estero().compareTo(anagrafico.getTi_italiano_estero()) != 0) &&
                !(anagrafico.getComune_fiscale().getTi_italiano_estero().compareTo(ComuneBulk.COMUNE_ESTERO) == 0 &&
                        (anagrafico.getTi_italiano_estero().compareTo(NazioneBulk.CEE) == 0) ||
                        anagrafico.getTi_italiano_estero().compareTo(NazioneBulk.SAN_MARINO) == 0))
            throw new ApplicationException("Attenzione: il comune fiscale non è compatibile con il tipo Italiano/Estero.");
        //}
        anagrafico = (AnagraficoBulk) super.modificaConBulk(aUC, anagrafico);

        validaCreaModificaAnagrafico_esercizio(aUC, anagrafico);
        return anagrafico;
    }

    protected Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        AnagraficoBulk anagrafico = (AnagraficoBulk) bulk;

        if (clauses == null && bulk != null)
            clauses = bulk.buildFindClauses(null);

        if (clauses != null) {
            for (java.util.Iterator list = clauses.iterator(); list.hasNext(); ) {
                SimpleFindClause cond = (SimpleFindClause) list.next();
                if ("ti_italiano_estero_anag".equals(cond.getPropertyName())) {
                    cond.setPropertyName("ti_italiano_estero");
                    if (!NazioneBulk.ITALIA.equals(cond.getValue())) {
                        if (cond.getOperator() == SQLBuilder.EQUALS) {
                            cond.setValue(NazioneBulk.ITALIA);
                            cond.setOperator(SQLBuilder.NOT_EQUALS);
                        } else if (cond.getOperator() == SQLBuilder.NOT_EQUALS) {
                            cond.setValue(NazioneBulk.ITALIA);
                            cond.setOperator(SQLBuilder.EQUALS);
                        }
                    }
                }
            }
        }

        if (anagrafico.getTi_entita_persona_struttura() != anagrafico.ENTITA_INDEFINITA && anagrafico.getTi_entita() == null) {
            FindClause clause;
            if (anagrafico.getTi_entita_persona_struttura() == anagrafico.ENTITA_PERSONA) {
                clause = CompoundFindClause.or(
                        new SimpleFindClause("ti_entita", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, anagrafico.FISICA),
                        new SimpleFindClause("ti_entita", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, anagrafico.GIURIDICA));
                clause = CompoundFindClause.or(
                        new SimpleFindClause("ti_entita", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, anagrafico.DIVERSI),
                        clause);
            } else {
                clause = new SimpleFindClause("ti_entita", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, anagrafico.STRUT_CNR);
            }
            clauses = CompoundFindClause.and(clauses, clause);
        }
        return super.select(userContext, clauses, bulk);
    }

    public SQLBuilder selectCd_ente_appByClause(UserContext userContext,
                                                AnagraficoBulk anag,
                                                AnagraficoBulk anag2,
                                                CompoundFindClause clauses)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (clauses == null) clauses = anag2.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, anag2).createSQLBuilder();
        if (clauses != null) sql.addClause(clauses);

        if (clauses != null) {
            for (java.util.Iterator list = clauses.iterator(); list.hasNext(); ) {
                SimpleFindClause cond = (SimpleFindClause) list.next();
                if ("ti_italiano_estero_anag".equals(cond.getPropertyName())) {
                    cond.setPropertyName("ti_italiano_estero");
                    if (!NazioneBulk.ITALIA.equals(cond.getValue())) {
                        if (cond.getOperator() == SQLBuilder.EQUALS) {
                            cond.setValue(NazioneBulk.ITALIA);
                            cond.setOperator(SQLBuilder.NOT_EQUALS);
                        } else if (cond.getOperator() == SQLBuilder.NOT_EQUALS) {
                            cond.setValue(NazioneBulk.ITALIA);
                            cond.setOperator(SQLBuilder.EQUALS);
                        }
                    }
                }
            }
        }

        return sql;
    }

    public SQLBuilder selectComune_fiscaleByClause(UserContext userContext,
                                                   AnagraficoBulk anag,
                                                   ComuneBulk comune,
                                                   CompoundFindClause clause)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (clause == null) clause = comune.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, comune).createSQLBuilder();
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "DT_CANC", sql.ISNULL, null);
        sql.addSQLClause("OR", "DT_CANC", sql.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
        sql.closeParenthesis();
        if (clause != null) sql.addClause(clause);

        if (NazioneBulk.EXTRA_CEE.equals(anag.getTi_italiano_estero()))
            sql.addSQLClause("AND", "TI_ITALIANO_ESTERO", sql.NOT_EQUALS, NazioneBulk.ITALIA);

        return sql;
    }

    public SQLBuilder selectAnagraficoByClause(UserContext userContext,
                                                   AssGruppoIvaAnagBulk ass,
                                                   AnagraficoBulk anag_find,
                                                   CompoundFindClause clause)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (clause == null) clause = anag_find.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, anag_find).createSQLBuilder();
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "TI_ENTITA_GIURIDICA", sql.ISNULL, null);
        sql.addSQLClause("OR", "TI_ENTITA_GIURIDICA", sql.NOT_EQUALS, AnagraficoBulk.GRUPPO_IVA);
        sql.closeParenthesis();
        if (clause != null) sql.addClause(clause);

        return sql;
    }

    public SQLBuilder selectAnagraficoGruppoIvaByClause(UserContext userContext,
                                               AssGruppoIvaAnagBulk ass,
                                               AnagraficoBulk anag_find,
                                               CompoundFindClause clause)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (clause == null) clause = anag_find.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, anag_find).createSQLBuilder();
        sql.addSQLClause("AND", "TI_ENTITA_GIURIDICA", sql.EQUALS, AnagraficoBulk.GRUPPO_IVA);
        sql.addSQLClause("AND", "DT_INI_VAL_GRUPPO_IVA", sql.ISNOTNULL, null);
        if (clause != null) sql.addClause(clause);

        return sql;
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla vista V_PREV_DIP
     * per la Stampa Previdenziale Dipendenti.
     * <p>
     * Nome: Richiesta di ricerca di una Matricola valida per la Stampa Previdenziale Dipendenti
     * Pre: E' stata generata la richiesta di ricerca di una Matricola valida per la Stampa Previdenziale Dipendenti.
     * Post: Viene restituito l'SQLBuilder per filtrare i Terzi
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param stampa      l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param matricola   l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
     * della query.
     * @param                clauses L'albero logico delle clausole da applicare alla ricerca
     **/
    public SQLBuilder selectMatricolaForPrintByClause(UserContext userContext, Stampa_previdenziale_dipendentiVBulk stampa, V_prev_dipBulk matricola, CompoundFindClause clauses) throws PersistencyException, ComponentException {

        // Recupera la sede dalla Uo di scrivania. La sede è data dal codice della Uo eliminando il punto separatore.
        //String uo = stampa.getUo_scrivania().getCd_unita_organizzativa();


        //Recupera la sede dalla Uo selezionata
        String uo = stampa.getCdUOCRForPrint();
        String sede = null;
        if (!(stampa.getCdUOCRForPrint().equalsIgnoreCase("*")))
            sede = uo.substring(0, 3) + uo.substring(4);

        V_prev_dipHome home = (V_prev_dipHome) getHome(userContext, V_prev_dipBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause(clauses);

        //Se la UO è 999.000 allora viene vista la lista di tutte le matricole altrimenti solo della Uo selezionata
        Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        //if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() ))
        if (stampa.getCdUOCRForPrint().equals(uoEnte.getCd_unita_organizzativa())) {

            sql.addOrderBy("V_PREV_DIP.MATRICOLA");
            return sql;
        } else {
            sql.addClause("AND", "sede", sql.EQUALS, sede);
            sql.addOrderBy("V_PREV_DIP.MATRICOLA");
            return sql;
        }


    }

    /**
     * Normale
     * PreCondition:
     * E' stata generata la richiesta di creare un nuovo Inquadramento.
     * PostCondition:
     * Viene restituita una query sulla tabella TIPO_RAPPORTO con le clausole specificate
     * inoltre, si cercano quei Rif. Inquadramenti che abbiano TI_DIPENDENTE_ALTRO, coerente
     * con il Tipo di Rapporto selezionato.
     */

    public SQLBuilder selectRif_inquadramentoByClause(UserContext userContext, InquadramentoBulk inquadramento, Rif_inquadramentoBulk rif_inquadramento, CompoundFindClause clause) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

        RapportoBulk rapporto = inquadramento.getRapporto();
        String dip_altro = null;
        if (rapporto != null && rapporto.getTipo_rapporto() != null && rapporto.getTipo_rapporto().getTi_dipendente_altro() != null) {
            dip_altro = rapporto.getTipo_rapporto().getTi_dipendente_altro();
        }

        SQLBuilder sql = getHome(userContext, rif_inquadramento).createSQLBuilder();
        sql.addClause(clause);

        if (dip_altro != null)
            sql.addSQLClause("AND", "ti_dipendente_altro", sql.EQUALS, dip_altro);

        return sql;
    }

    public SQLBuilder selectTerzoForPrintByClause(UserContext userContext, Stampa_previdenziale_dipendentiVBulk stampa, TerzoBulk terzo, CompoundFindClause clauses) throws PersistencyException, ComponentException {

        // Recupera la sede dalla Uo di scrivania. La sede è data dal codice della Uo eliminando il punto separatore.
        //String uo = stampa.getUo_scrivania().getCd_unita_organizzativa();


        //	Recupera la sede dalla Uo selezionata
        String uo = stampa.getCdUOCRForPrint();
        String sede = uo.substring(0, 3) + uo.substring(4);


        TerzoHome home = (TerzoHome) getHome(userContext, TerzoBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addTableToHeader("V_PREV_DIP");
        sql.addSQLJoin("TERZO.CD_TERZO", "V_PREV_DIP.CD_TERZO");
        sql.addClause(clauses);

//	Se la UO è 999.000 allora viene vista la lista di tutti i terzi altrimenti solo della Uo selezionata
        Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        if (stampa.getCdUOCRForPrint().equals(uoEnte.getCd_unita_organizzativa())) {

            return sql;
        } else {
            sql.addSQLClause("AND", "V_PREV_DIP.SEDE", sql.EQUALS, sede);
            return sql;
        }


    }

    /**
     * Normale
     * PreCondition:
     * L'utente ha richiesto la lista dei tipi rapporto da usabili nell'inserimento di un nuovo rapporto di un anagrafico
     * PostCondition:
     * Viene restituita una query sulla tabella TIPO_RAPPORTO con le clausole specificate più la clausola "TI_DIPENDENTE_ALTRO <> 'D'"
     */

    public SQLBuilder selectTipo_rapportoByClause(UserContext userContext, RapportoBulk rapporto, Tipo_rapportoBulk tipo_rapporto, CompoundFindClause clause) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = getHome(userContext, tipo_rapporto).createSQLBuilder();
        sql.addClause(clause);
        UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));
        if (!utente.isSupervisore()) {
            sql.addSQLClause("AND", "fl_visibile_a_tutti", sql.EQUALS, "Y");
            sql.addClause("AND", "ti_dipendente_altro", sql.NOT_EQUALS, tipo_rapporto.DIPENDENTE);
            if (rapporto != null && rapporto.getAnagrafico() != null && rapporto.getAnagrafico().getTi_entita() != null &&
                    rapporto.getAnagrafico().getTi_entita().equals(AnagraficoBulk.GIURIDICA)) {
                sql.addSQLClause("AND", "cd_tipo_rapporto = (select CD_TIPO_RAPPORTO_PROF from parametri_cnr where esercizio = " + ((CNRUserContext) userContext).getEsercizio() + ")");
            }
        } else {
            sql.openParenthesis("AND");
            sql.openParenthesis("AND");
            sql.addClause("AND", "ti_dipendente_altro", sql.NOT_EQUALS, tipo_rapporto.DIPENDENTE);
//		if (rapporto != null &&  rapporto.getAnagrafico() != null && rapporto.getAnagrafico().getTi_entita()!= null && 
//				rapporto.getAnagrafico().getTi_entita().equals(AnagraficoBulk.GIURIDICA)) {
//			sql.addSQLClause("AND","cd_tipo_rapporto = (select CD_TIPO_RAPPORTO_PROF from parametri_cnr where esercizio = "+((CNRUserContext)userContext).getEsercizio()+")");
//		}
            sql.closeParenthesis();
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "fl_visibile_a_tutti", sql.EQUALS, "Y");
            sql.addSQLClause("OR", "fl_visibile_a_tutti", sql.EQUALS, "N");
            sql.closeParenthesis();
            sql.closeParenthesis();
        }
        return sql;
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
     * <p>
     * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa Previdenziale Dipendenti
     * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
     * Post: Viene restituito l'SQLBuilder per filtrare le UO
     * in base al cds di scrivania
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param stampa      l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param uo          l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
     * della query.
     * @param                clauses L'albero logico delle clausole da applicare alla ricerca
     **/

/*public SQLBuilder selectUoForPrintByClause (UserContext userContext, Stampa_previdenziale_dipendentiVBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses ) throws ComponentException
{
	SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
	sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
	sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds().getCd_unita_organizzativa());
	sql.addClause( clauses );
	return sql;
}*/
    public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_previdenziale_dipendentiVBulk stampa, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws PersistencyException, ComponentException {

        Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals(uoEnte.getCd_unita_organizzativa())) {

            SQLBuilder sql = ((Unita_organizzativaHome) getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            //sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
            sql.addClause(clauses);
            return sql;
        } else {
            SQLBuilder sql = ((Unita_organizzativaHome) getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
            sql.addClause(clauses);
            return sql;
        }

    }

    /**
     * Imposta il comune fiscale relativo all'anagrafica.
     * <p>
     * Nome: Gestione comune fiscale;
     * Pre:  Ricerca del comune e acricamenti dei cap relativi;
     * Post: Viene assegnato il comune e lanciato l'aggornamento dell'elenco dei cap associati.
     *
     * @param anagrafico <code>AnagraficoBulk</code> su cui va impostato il comune fiscale.
     * @param comune     il <code>ComuneBulk</code> del comune da impostare.
     * @return AnagraficoBulk <code>AnagraficoBulk</code> con comune impostato.
     */

    public AnagraficoBulk setComune_fiscale(UserContext userContext, AnagraficoBulk anagrafico, it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune) throws it.cnr.jada.comp.ComponentException {
        anagrafico.setComune_fiscale(comune);
        anagrafico.setCaps_comune(null);
        initializeKeysAndOptionsInto(userContext, anagrafico);
        if (comune != null)
            anagrafico.setCap_comune_fiscale(comune.getCd_cap());
        else
            anagrafico.setCap_comune_fiscale(null);
        return anagrafico;
    }

    /**
     * Imposta il comune della sede di un terzo.
     * <p>
     * Nome: Gestione comune sede;
     * Pre:  Ricerca del comune e acricamenti dei cap relativi;
     * Post: Viene assegnato il comune e lanciato l'aggornamento dell'elenco dei cap associati.
     *
     * @param anagrafico <code>AnagraficoBulk</code> a cui abbartiene il terzo.
     * @param terzo      <code>TerzoBulk</code> su cui va impostato il comunedella sede.
     * @param comune     il <code>ComuneBulk</code> del comune da impostare.
     * @return AnagraficoBulk <code>AnagraficoBulk</code> completo.
     */

    public AnagraficoBulk setComune_sede(UserContext userContext, AnagraficoBulk anagrafico, TerzoBulk terzo, it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune) throws it.cnr.jada.comp.ComponentException {
        terzo.setComune_sede(comune);
        terzo.setCaps_comune(null);
        super.initializeKeysAndOptionsInto(userContext, terzo);
        if (comune != null)
            terzo.setCap_comune_sede(comune.getCd_cap());
        else
            terzo.setCap_comune_sede(null);
        return terzo.getAnagrafico();
    }

    /**
     * stampaConBulk method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        return bulk;
    }

    protected void validaCarichiFamiliari(UserContext userContext, AnagraficoBulk anagrafico) throws ApplicationException {
        for (java.util.Iterator i = anagrafico.getCarichi_familiari_anag().iterator(); i.hasNext(); ) {
            Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();

            /* Carichi Familiari se di tipo "Figlio" aggiorno la data "Fine figlio ha treanni" uguale a "Data inizio validità" + 3 */
            if (carico_familiare.FIGLIO.equals(carico_familiare.getTi_persona()) &&
                    carico_familiare.getDt_ini_validita() != null) {
                GregorianCalendar dffht = new GregorianCalendar();
                dffht.setTime(carico_familiare.getDt_nascita_figlio());
                dffht.add(dffht.YEAR, 3);
                carico_familiare.setDt_fine_figlio_ha_treanni(new java.sql.Timestamp(dffht.getTime().getTime()));
            }
        }
    }

    /*
     *	Pre: nel PAGAMENTO_ESTERNO sono stati inseriti dei record
     *	Post: viene chiamata la stored procedure di aggiornamento dei Montanti per i Pagamenti Esterni
     *
     * @param userContext <code>UserContext</code>
     * @param cd_anag <code>Integer</code>
     * @param pagamento <code>BigDecimal</code>
     */
    public void aggiornaMontantiPagEst(UserContext userContext, Integer cd_anag, BigDecimal pagamento) throws ComponentException {
        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{ call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB550.aggiornaMontantiPagEst( ?, ?, ?, ? ) }", false, this.getClass());
                cs.setObject(1, ((CNRUserContext) userContext).getEsercizio());
                cs.setObject(2, cd_anag);
                cs.setObject(3, ((CNRUserContext) userContext).getUser());
                cs.setObject(4, pagamento);
                cs.executeQuery();
            } catch (Throwable e) {
                throw handleException(e);
            } finally {
                if (cs != null) cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
    }

    private void calcolaMontantePerPagamentoEsterno(UserContext userContext, AnagraficoBulk anagrafico, boolean creazione) throws ComponentException {
        //Prima mi recupero il Tipo Rapporto Occasionale dai Parametri Ente
        String messaggio = "Esiste almeno un compenso con data di registrazione superiore alla data indicata.";
        Tipo_rapportoBulk tipo_rapporto_occa = null;
        BigDecimal imp_pag_esterno = new BigDecimal(0);
        Pagamento_esternoHome pagamento_esternoHome = (Pagamento_esternoHome) getHome(userContext, Pagamento_esternoBulk.class);
        Parametri_cnrHome home = (Parametri_cnrHome) getHome(userContext, Parametri_cnrBulk.class);
        CompensoHome compensoHome = (CompensoHome) getHome(userContext, CompensoBulk.class);
        try {
            Parametri_cnrBulk parametri_correnti = (Parametri_cnrBulk) (home.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext))));
            if (parametri_correnti == null)
                throw new ApplicationException("Non esistono i parametri CNR per l'anno " + it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            tipo_rapporto_occa = parametri_correnti.getTipo_rapporto();
        } catch (PersistencyException e) {
            throw new ApplicationException("Non esistono i parametri CNR per l'anno in corso.");
        }
        for (java.util.Iterator i = anagrafico.getPagamenti_esterni().listIterator(); i.hasNext(); ) {
            Pagamento_esternoBulk pagamento_esterno = (Pagamento_esternoBulk) i.next();
            if (pagamento_esterno != null && pagamento_esterno.getCd_tipo_rapporto() != null &&
                    (pagamento_esterno.getCrudStatus() == OggettoBulk.TO_BE_CREATED ||
                            pagamento_esterno.getCrudStatus() == OggettoBulk.TO_BE_UPDATED || creazione)) {
                if (pagamento_esterno.getCrudStatus() == OggettoBulk.TO_BE_UPDATED) {
                    try {
                        //Recupero il record sul DB e verifico i cambiamenti sui campi interessati
                        Pagamento_esternoBulk pagamento_esternoDB = (Pagamento_esternoBulk) (pagamento_esternoHome.findAndLock(pagamento_esterno));
                        //Ora verifico se aggiornare il Montante o meno

                        //Controllo se può aggiornare il record sui compensi attraverso la data
                        //in caso di risposta affermativa aggiorno il Montante
                        if ((pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                !pagamento_esternoDB.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto())) ||
                                (!pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                        pagamento_esternoDB.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto())) ||
                                (pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                        pagamento_esternoDB.getDt_pagamento().compareTo(pagamento_esterno.getDt_pagamento()) != 0 &&
                                        ((compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esternoDB.getDt_pagamento()).booleanValue() &&
                                                !compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue()) ||
                                                (!compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esternoDB.getDt_pagamento()).booleanValue() &&
                                                        compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue())) ||
                                        (compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esternoDB.getDt_pagamento()).booleanValue() &&
                                                compensoHome.findCompensoConDataInferiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esternoDB.getDt_pagamento()).booleanValue()) &&
                                                (!compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue() ||
                                                        !compensoHome.findCompensoConDataInferiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue())
                                ) ||
                                (pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                        pagamento_esternoDB.getIm_pagamento().compareTo(pagamento_esterno.getIm_pagamento()) != 0)
                                ) {
                            try {
                                if (compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue())
                                    throw new ApplicationException(messaggio);
                            } catch (PersistencyException e) {
                                throw new ApplicationException(messaggio);
                            }
                        }
                        if ((pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esternoDB.getDt_pagamento()).booleanValue())) {
                            try {
                                if (!compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue())
                                    throw new ApplicationException("Esiste almeno un compenso con data di registrazione inferiore alla data indicata.");
                            } catch (PersistencyException e) {
                                throw new ApplicationException("Esiste almeno un compenso con data di registrazione inferiore alla data indicata.");
                            }
                        }
                        //In questo caso il tipo di rapporto non era Occasionale e poi è stato aggiornato
                        if (pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                !pagamento_esternoDB.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto())
                                ) {
                            imp_pag_esterno = pagamento_esterno.getIm_pagamento();
                            aggiornaMontantiPagEst(userContext, anagrafico.getCd_anag(), imp_pag_esterno);
                        }
                        //In questo caso il tipo di rapporto era Occasionale e poi è stato aggiornato
                        if (!pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                pagamento_esternoDB.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto())
                                ) {
                            //Controllo se può aggiornare il record sui compensi attraverso la data
                            //in caso di risposta affermativa aggiorno il Montante
                            imp_pag_esterno = pagamento_esternoDB.getIm_pagamento().negate();
                            aggiornaMontantiPagEst(userContext, anagrafico.getCd_anag(), imp_pag_esterno);
                        }
                        //In questo caso il tipo di rapporto non è stato modificato Occasionale e invece
                        //è stato aggiornato l'importo
                        if (pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                pagamento_esternoDB.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto()) &&
                                pagamento_esternoDB.getIm_pagamento().compareTo(pagamento_esterno.getIm_pagamento()) != 0
                                ) {
                            //Controllo se può aggiornare il record sui compensi attraverso la data
                            //in caso di risposta affermativa aggiorno il Montante
                            if (pagamento_esternoDB.getIm_pagamento().compareTo(pagamento_esterno.getIm_pagamento()) == -1)
                                imp_pag_esterno = pagamento_esterno.getIm_pagamento().add(pagamento_esternoDB.getIm_pagamento().negate());
                            else
                                imp_pag_esterno = pagamento_esternoDB.getIm_pagamento().add(pagamento_esterno.getIm_pagamento().negate()).negate();
                            aggiornaMontantiPagEst(userContext, anagrafico.getCd_anag(), imp_pag_esterno);
                        }
                    } catch (PersistencyException e1) {
                        throw new ApplicationException("Errore nel recupero dei compensi." + e1.getMessage());
                    } catch (OutdatedResourceException e) {
                        throw new ApplicationException("Errore nel recupero dei compensi." + e.getMessage());
                    } catch (BusyResourceException e) {
                        throw new ApplicationException("Errore nel recupero dei compensi." + e.getMessage());
                    }
                }
                if (pagamento_esterno != null && pagamento_esterno.getTipo_rapporto() != null &&
                        (pagamento_esterno.getCrudStatus() == OggettoBulk.TO_BE_CREATED || creazione) &&
                        pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto())
                        && pagamento_esterno.getIm_pagamento() != null) {
                    //Controllo se può aggiornare il record sui compensi attraverso la data
                    //in caso di risposta affermativa aggiorno il Montante
                    try {
                        if (compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue())
                            throw new ApplicationException(messaggio);
                    } catch (PersistencyException e) {
                        throw new ApplicationException(messaggio);
                    }
                    imp_pag_esterno = pagamento_esterno.getIm_pagamento();
                    aggiornaMontantiPagEst(userContext, anagrafico.getCd_anag(), imp_pag_esterno);
                }
            }
        }
        for (java.util.Iterator i = anagrafico.getPagamenti_esterni().deleteIterator(); i.hasNext(); ) {
            Pagamento_esternoBulk pagamento_esterno = (Pagamento_esternoBulk) i.next();
            //		Recupero il record sul DB
            try {
                pagamento_esterno = (Pagamento_esternoBulk) (pagamento_esternoHome.findAndLock(pagamento_esterno));
            } catch (PersistencyException e1) {
                if (!(e1 instanceof it.cnr.jada.persistency.ObjectNotFoundException))
                    throw new ComponentException(e1);
            } catch (OutdatedResourceException e1) {
                throw new ApplicationException("Risorsa modificata, rieffettuare la ricerca.");
            } catch (BusyResourceException e1) {
                throw new ApplicationException("Risorsa occupata!");
            }
            if (pagamento_esterno != null && pagamento_esterno.getCd_tipo_rapporto() != null) {
                if (pagamento_esterno.getCd_tipo_rapporto().equals(tipo_rapporto_occa.getCd_tipo_rapporto())
                        && pagamento_esterno.getIm_pagamento() != null) {
                    //Controllo se può cancellare il record sui compensi attraverso la data
                    //in caso di risposta affermativa aggiorno il Montante
                    try {
                        if (compensoHome.findCompensoConDataSuperiore(((CNRUserContext) userContext).getEsercizio(), anagrafico.getCd_anag(), pagamento_esterno.getDt_pagamento()).booleanValue())
                            throw new ApplicationException(messaggio);
                    } catch (PersistencyException e) {
                        throw new ApplicationException(messaggio);
                    }
                    imp_pag_esterno = pagamento_esterno.getIm_pagamento().negate();
                    aggiornaMontantiPagEst(userContext, anagrafico.getCd_anag(), imp_pag_esterno);
                }
            }
        }
    }

    /**
     * Metodo creato per permettere l'aggionamento dei Montanti nel caso in cui
     * il tipo di rapporto sia quello definito nei Parametri Ente come
     * rapporto di tipo Occasionale
     * Creation date: (22/11/2004 16.55)
     * Author: Spasiano Marco
     *
     * @param anagrafico <code>AnagraficoBulk</code> il bulk da controllare
     */
    private void calcolaMontantePerPagamentoEsterno(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        calcolaMontantePerPagamentoEsterno(userContext, anagrafico, false);
    }

    /**
     * Metodo creato per il controllo dei relativi all'oggetto ANAGRAFICO_ESERCIZIO.
     * Il metodo è invocato durante la Creazione o la Modifica di un Anagrafico,
     * (metodo modificaConBulk), e, per ora, controlla, solo la validità del campo
     * <code>FL_NOTAXAREA</code>.
     * <p>
     * Creation date: (15/01/2003 15.55)
     * Author: Borriello Gennaro
     *
     * @param anagrafico <code>AnagraficoBulk</code> il bulk da controllare
     */
    private void validaCreaModificaAnagrafico_esercizio(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {


        if (anagrafico.getAnagrafico_esercizio() != null) {
            Anagrafico_esercizioBulk anag_eserc = anagrafico.getAnagrafico_esercizio();

            try {
                // L'ANAGRAFICO_ESERCIZIO non esiste
                if (anag_eserc.isToBeCreated()) {
                    /* Il FL_NOTAXAREA è stato impostato come TRUE oppure
                     *	è stato impostato un valore per il campo <code>Im_detrazione_personale_anag</code>
                     */
                    if ((anag_eserc.getFl_notaxarea() != null && anag_eserc.getFl_notaxarea().booleanValue())
                            || (anag_eserc.getFl_nofamilyarea() != null && anag_eserc.getFl_nofamilyarea().booleanValue())
                            || (anag_eserc.getFl_no_detrazioni_altre() != null && anag_eserc.getFl_no_detrazioni_altre().booleanValue())
                            || (anag_eserc.getFl_no_detrazioni_family() != null && anag_eserc.getFl_no_detrazioni_family().booleanValue())
                            || (anag_eserc.getIm_detrazione_personale_anag() != null && anag_eserc.getIm_detrazione_personale_anag().compareTo(new java.math.BigDecimal(0)) > 0)
                            || (anag_eserc.getIm_reddito_complessivo() != null && anag_eserc.getIm_reddito_complessivo().compareTo(new java.math.BigDecimal(0)) > 0)
                            || (anag_eserc.getIm_reddito_abitaz_princ() != null && anag_eserc.getIm_reddito_abitaz_princ().compareTo(new java.math.BigDecimal(0)) > 0)
                            || (anag_eserc.getFl_applica_detr_pers_max() != null && anag_eserc.getFl_applica_detr_pers_max().booleanValue())
                            || (anag_eserc.getIm_deduzione_family_area() != null && anag_eserc.getIm_deduzione_family_area().compareTo(new java.math.BigDecimal(0)) > 0)
                            || (anag_eserc.getFl_no_credito_irpef() != null && anag_eserc.getFl_no_credito_irpef().booleanValue())
                            || (anag_eserc.getFl_no_detr_cuneo_irpef() != null && anag_eserc.getFl_no_detr_cuneo_irpef().booleanValue())
                            || (anag_eserc.getFl_no_credito_cuneo_irpef() != null && anag_eserc.getFl_no_credito_cuneo_irpef().booleanValue())
                            || (anag_eserc.getFl_detrazioni_altri_tipi() != null && anag_eserc.getFl_detrazioni_altri_tipi().booleanValue())
                            || (anag_eserc.getFl_applica_detr_pers_max() != null && anag_eserc.getFl_applica_detr_pers_max().booleanValue())
                            || (anag_eserc.getContoCredito()!=null && anag_eserc.getContoCredito().getEsercizio()!=null && anag_eserc.getContoCredito().getCd_voce_ep()!=null)
                            || (anag_eserc.getContoDebito()!=null && anag_eserc.getContoDebito().getEsercizio()!=null && anag_eserc.getContoDebito().getCd_voce_ep()!=null)) {

                        //inizializzo i flag se non valorizzati
                        if (anag_eserc.getFl_nofamilyarea() == null) {
                            if (isGestiteDeduzioniFamily(userContext))
                                anag_eserc.setFl_nofamilyarea(new Boolean(false));
                            else
                                anag_eserc.setFl_nofamilyarea(new Boolean(true));
                        }
                        if (anag_eserc.getFl_notaxarea() == null) {
                            if (isGestiteDeduzioniIrpef(userContext))
                                anag_eserc.setFl_notaxarea(new Boolean(false));
                            else
                                anag_eserc.setFl_notaxarea(new Boolean(true));
                        }
                        if (anag_eserc.getFl_no_detrazioni_family() == null) {
                            if (isGestiteDetrazioniFamily(userContext))
                                anag_eserc.setFl_no_detrazioni_family(new Boolean(false));
                            else
                                anag_eserc.setFl_no_detrazioni_family(new Boolean(true));
                        }
                        if (anag_eserc.getFl_no_detrazioni_altre() == null) {
                            if (isGestiteDetrazioniAltre(userContext))
                                anag_eserc.setFl_no_detrazioni_altre(new Boolean(false));
                            else
                                anag_eserc.setFl_no_detrazioni_altre(new Boolean(true));
                        }
                        if (anag_eserc.getFl_no_credito_irpef() == null) {
                            // potrebbero attivarlo dopo l'inserimento di anagrafico_esercizio negli esercizi futuri
                            // quindi lo inizializziamo sempre a NO, il calcolo del Bonus viene fatto solo con gestione attiva
                            // if (isGestitoCreditoIrpef(userContext))
                            //		anag_eserc.setFl_no_credito_irpef(new Boolean(false));
                            // else
                            //		anag_eserc.setFl_no_credito_irpef(new Boolean(true));
                            anag_eserc.setFl_no_credito_irpef(new Boolean(false));
                        }
                        if (anag_eserc.getFl_no_credito_cuneo_irpef() == null) {
                            // potrebbero attivarlo dopo l'inserimento di anagrafico_esercizio negli esercizi futuri
                            // quindi lo inizializziamo sempre a NO, il calcolo del Bonus viene fatto solo con gestione attiva
                            anag_eserc.setFl_no_credito_cuneo_irpef(new Boolean(false));
                        }
                        if (anag_eserc.getFl_no_detr_cuneo_irpef() == null) {
                            // potrebbero attivarlo dopo l'inserimento di anagrafico_esercizio negli esercizi futuri
                            // quindi lo inizializziamo sempre a NO, il calcolo del Bonus viene fatto solo con gestione attiva
                            anag_eserc.setFl_no_detr_cuneo_irpef(new Boolean(false));
                        }
                        if (anag_eserc.getFl_detrazioni_altri_tipi() == null) {
                            // lo inizializziamo sempre a NO
                            anag_eserc.setFl_detrazioni_altri_tipi(new Boolean(false));
                        }
                        if (anag_eserc.getFl_applica_detr_pers_max() == null) {
                            // lo inizializziamo sempre a NO
                            anag_eserc.setFl_applica_detr_pers_max(new Boolean(false));
                        }
                        anag_eserc.setCd_anag(anagrafico.getCd_anag());
                        insertBulk(userContext, anag_eserc);
                    }
                }
                //  L'ANAGRAFICO_ESERCIZIO esiste: verifica ed aggiorna il FL_NOTAXAREA.
                else if (anag_eserc.isToBeUpdated()) {

                    Anagrafico_esercizioHome anag_esercHome = (Anagrafico_esercizioHome) getHome(userContext, Anagrafico_esercizioBulk.class);
                    Anagrafico_esercizioBulk exist_anag_eserc = anag_esercHome.findAnagrafico_esercizioFor(userContext, anagrafico, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
                    updateBulk(userContext, anag_eserc);

                    // Se sono state fatte delle modifiche, aggiorna il record
                    //if (anag_eserc.getFl_notaxarea() != null &&
                    //exist_anag_eserc.getFl_notaxarea() != null &&
                    //anag_eserc.getFl_notaxarea().booleanValue() != exist_anag_eserc.getFl_notaxarea().booleanValue()) {

                    //updateBulk(userContext, anag_eserc);
                    //}
                }
            } catch (it.cnr.jada.persistency.IntrospectionException ie) {
                throw new ComponentException(ie);
            } catch (it.cnr.jada.persistency.PersistencyException pe) {
                throw new ComponentException(pe);
            }
        }
    }

    public void validaCreaModificaConBulk(
            UserContext userContext,
            OggettoBulk bulk)
            throws ComponentException, ExCodiceFiscale {

        super.validaCreaModificaConBulk(userContext, bulk);

        AnagraficoBulk anagrafico = (AnagraficoBulk) bulk;

        if (!isAnagraficaModificabile(userContext, anagrafico))
            throw new ApplicationException("Non si hanno i diritti per modificare una struttura CNR.");

        // RIPULITURA CAMPI
        // vengono posti a null i campi non valorizzabili in base alla tipologia di anagrafica
        if (!anagrafico.isGruppoIVA()){
            anagrafico.setDtIniValGruppoIva(null);
        } else {
            if (anagrafico.getDtIniValGruppoIva() == null){
                throw new it.cnr.jada.comp.ApplicationException("La data inizio validità del gruppo IVA è obbligatoria.");
            }
            if (anagrafico.getDt_canc() == null){
                throw new it.cnr.jada.comp.ApplicationException("La data fine validità del gruppo IVA è obbligatoria.");
            }
            if (anagrafico.getDtIniValGruppoIva().after(anagrafico.getDt_canc())){
                throw new it.cnr.jada.comp.ApplicationException("La data inizio validità del gruppo IVA deve essere precedente alla data di fine validità.");
            }
        }
        if (anagrafico.isPersonaFisica() || anagrafico.isDiversi()) {
            // PERSONA FISICA or DIVERSI
            anagrafico.setTi_entita_giuridica(null);
            anagrafico.setDt_canc(null);
            if (!anagrafico.isDittaIndividuale())
                anagrafico.setRagione_sociale(null);
        } else if (anagrafico.isPersonaGiuridica()) {
            // PERSONA GIURIDICA
            anagrafico.setTi_entita_fisica(null);
            anagrafico.setCognome(null);
            if (!anagrafico.isGruppoIVA())
                anagrafico.setDt_canc(null);
            anagrafico.setDt_nascita(null);
            anagrafico.setNome(null);
            anagrafico.setComune_nascita(null);
            anagrafico.setNazionalita(null);
        } else {
            // STRUTTURA C.N.R.
            anagrafico.setTi_italiano_estero(NazioneBulk.ITALIA);
            anagrafico.setTi_entita_giuridica(null);
            anagrafico.setTi_entita_fisica(null);
            anagrafico.setFl_soggetto_iva(new Boolean(false));
            anagrafico.setCognome(null);
            anagrafico.setDt_nascita(null);
            anagrafico.setDt_canc(null);
            anagrafico.setNome(null);
            anagrafico.setComune_nascita(null);
            anagrafico.setNazionalita(null);
        }

        if (anagrafico.getComune_fiscale() != null
                && anagrafico.getComune_fiscale().getNazione() != null)
            anagrafico.setTi_italiano_estero(
                    anagrafico.getComune_fiscale().getNazione().getTi_nazione());

        if (anagrafico.isPersonaFisica()) {
            // PERSONA FISICA

            // CODICE FISCALE */
            // verifica dell'esistenza dei campi per la ferifica del codice fiscale
            if (anagrafico.getDt_nascita() == null)
                throw new it.cnr.jada.comp.ApplicationException(
                        "La data di nascita è obbligatoria.");

            if (anagrafico.getCognome() == null)
                throw new it.cnr.jada.comp.ApplicationException("Il cognome è obbligatorio.");

            if (anagrafico.getNome() == null)
                throw new it.cnr.jada.comp.ApplicationException("Il nome è obbligatorio.");

            // Controlla la validità di Cognome e Nome
            controllaNomeCognome(anagrafico);

            if (anagrafico.getTi_sesso() == null)
                throw new it.cnr.jada.comp.ApplicationException("Il sesso è obbligatorio.");

            if (anagrafico.getComune_nascita() == null
                    || anagrafico.getPg_comune_nascita() == null)
                throw new it.cnr.jada.comp.ApplicationException(
                        "Il comune di nascita è obbligatorio.");

            if (anagrafico.getComune_fiscale() == null
                    || anagrafico.getPg_comune_fiscale() == null)
                throw new it.cnr.jada.comp.ApplicationException(
                        "Il comune Fiscale è obbligatorio.");

            // Viene verificata l'esattezza del codice fiscale
            if (anagrafico.getFl_codice_fiscale_forzato() == null
                    || !anagrafico.getFl_codice_fiscale_forzato().booleanValue())
                controllaCodiceFiscale(anagrafico);

            // Upper Case del codice fiscale
            if (anagrafico.getCodice_fiscale() != null)
                anagrafico.setCodice_fiscale(anagrafico.getCodice_fiscale().toUpperCase());

            if (anagrafico.getCodice_fiscale() == null)
                throw new ApplicationException("Il codice fiscale è obbligatorio");
            // Verifica lunghezza codice fiscale Italiani - persone fisiche
            if (NazioneBulk.ITALIA.equals(anagrafico.getTi_italiano_estero()))
                if (anagrafico.getCodice_fiscale().replace(" ", "").length() != 16)
                    throw new it.cnr.jada.comp.ApplicationException(
                            "La lunghezza del codice fiscale non è valida!");
            // Carichi Familiari verifica
            validaCarichiFamiliari(userContext, anagrafico);
        }

        /* verifica dell'esistenza del comune fiscale */
        if (anagrafico.getComune_fiscale() == null
                || anagrafico.getComune_fiscale().getPg_comune() == null)
            throw new it.cnr.jada.comp.ApplicationException(
                    "Il comune fiscale è obbligatorio.");

        // PARTITA IVA e C.F. di persona guiridica
        // se richiesta verifica che non sia nulla
        if (anagrafico.getFl_soggetto_iva().booleanValue()
                && (anagrafico.getPartita_iva() == null
                || anagrafico.getPartita_iva().length() == 0))
            throw new it.cnr.jada.comp.ApplicationException(
                    "La partita IVA è obbligatoria per le anagrafiche soggette ad IVA.");

        // verifica dell'esattezza della partita iva
        try {
            if (anagrafico.getPartita_iva() != null
                    && anagrafico.getPartita_iva().length() != 0
                    && NazioneBulk.ITALIA.equals(anagrafico.getTi_italiano_estero()))
                PartitaIVAControllo.parsePartitaIVA(anagrafico.getPartita_iva());
        } catch (ExPartitaIVA ecf) {
            throw new it.cnr.jada.comp.ApplicationException("La partita IVA è errata.");
        }

        // verifica dell'esattezza del codice fiscale per anagrafiche non Persona Fisica
        if (!anagrafico.isPersonaFisica()) {
            if (anagrafico.isPersonaGiuridica() && anagrafico.getRagione_sociale() == null)
                throw new ApplicationException("La ragione sociale è obbligatoria per una persona giuridica.");

            if (anagrafico.getCodice_fiscale() != null) {
                if (NazioneBulk.ITALIA.equals(anagrafico.getTi_italiano_estero())) {
                    try {
                        // Per testare se il codice fiscale è una partita iva
                        // controllo se sono tutti numeri...
                        new Long(anagrafico.getCodice_fiscale());
                        PartitaIVAControllo.parsePartitaIVA(anagrafico.getCodice_fiscale());
                    } catch (NumberFormatException nfe) {
                        // se non sono tutti numeri è un codice fiscale!
//					if (anagrafico.getCodice_fiscale().length() != 16
//						|| !CodiceFiscaleControllo.checkCC(anagrafico.getCodice_fiscale()))
                        if (anagrafico.getCodice_fiscale().length() == 16)
                            throw new it.cnr.jada.comp.ApplicationException(
                                    "Codice fiscale inserito errato per la tipologia dell'anagrafica.");
                        else
                            throw new it.cnr.jada.comp.ApplicationException(
                                    "Codice fiscale inserito errato.");
                    } catch (ExPartitaIVA ecf) {
                        throw new it.cnr.jada.comp.ApplicationException(
                                "Codice fiscale inserito errato.");
                    }
                }
            } else
                //Se persona giuridica il codice fiscale è obbligatorio se italiano o non soggetto ad iva
                if (anagrafico.isPersonaGiuridica()
                        && (!anagrafico.getFl_soggetto_iva().booleanValue() ||
                        NazioneBulk.ITALIA.equals(anagrafico.getTi_italiano_estero()))) {
                    throw new it.cnr.jada.comp.ApplicationException(
                            "Codice fiscale obbligatorio.");

                } else if (anagrafico.isDiversi()
                        && anagrafico.getCognome() == null
                        && anagrafico.getNome() == null) {
                    throw new it.cnr.jada.comp.ApplicationException(
                            "Attenzione: per i soggetti di tipo <Diversi>, è obbligatorio specificare almeno il Cognome o il Nome.");
                }
        }

        /* <B>Rich. 661</B>
         *	Il fl_esigibilità_differita, <U>per gli enti pubblici stranieri</U>,
         *	NON PUO' essere TRUE
         *
         * Creation date: (29/01/2004)
         * Author: Borriello Gennaro
         */
        if (!NazioneBulk.ITALIA.equals(anagrafico.getTi_italiano_estero()) &&
                anagrafico.isEntePubblico() &&
                anagrafico.getFl_fatturazione_differita().booleanValue()) {

            throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione: gli Enti Pubblici stranieri non possono essere a fatturazione differita.");
        }

        // VERIFICA ESISTENZA ANAGRAFICA CON IL C.F. O P.IVA IMPUTATI

	/*  <B>A correzione dell'errore No. 02240A</B>
    
		Aggiunto controllo: la verifica viene fatta solo se AnagraficoBulk è in 
		Creazione OR è stata inserita la P.I. OR il C.F. Questo evita nel caso, ad es. di un 
		AnagraficoBulk di tipo "DIVERSI", (in cui l'utente ha la facoltà di NON inserire nè
		la P.I., nè il C.F.), di trovare risultati fuorvianti. */

        if (/*(!anagrafico.isToBeCreated()) || */
                anagrafico.getPartita_iva() != null
                        || anagrafico.getCodice_fiscale() != null) {
            try {
                AnagraficoHome anagraficoHome = (AnagraficoHome)getHome(userContext, anagrafico);
                SQLBuilder sql = anagraficoHome.createSQLBuilder();
                sql.openParenthesis("AND");
                sql.addClause("OR", "partita_iva", SQLBuilder.EQUALS, anagrafico.getPartita_iva());
                sql.addClause(
                        "OR",
                        "codice_fiscale",
                        SQLBuilder.EQUALS,
                        anagrafico.getCodice_fiscale());
                sql.closeParenthesis();
                sql.addClause("AND", "dt_fine_rapporto", SQLBuilder.ISNULL, null);
                if (!anagrafico.isToBeCreated())
                    sql.addClause("AND", "cd_anag", SQLBuilder.NOT_EQUALS, anagrafico.getCd_anag());

                 List<AnagraficoBulk> listaAnagrafica = anagraficoHome.fetchAll(sql);
/*                if (listaAnagrafica.size() > 1){
                    throw new ApplicationException("Esistono altre anagrafiche con questo codice fiscale o partita iva");
                }*/
                if (listaAnagrafica.size() > 0){
                    for (AnagraficoBulk anagraficoConDatiUguali : listaAnagrafica){
                        if ((!anagrafico.isGruppoIVA() && !anagraficoConDatiUguali.isGruppoIVA()) || (anagrafico.isGruppoIVA() && anagraficoConDatiUguali.isGruppoIVA())){
                            throw new ApplicationException("Esiste già l'anagrafica "+anagraficoConDatiUguali.getCd_anag()+" con questo codice fiscale o partita iva");
                        }
                    }
                }
            } catch (PersistencyException e) {
                throw handleException(e);
            }
        }

        if (anagrafico.isToBeUpdated() && anagrafico.getTitolo_studio() == null) {
            try {
                TerzoHome home = (TerzoHome) getHome(userContext, TerzoBulk.class);

                SQLBuilder sql = home.createSQLBuilder();
                sql.addClause("AND", "cd_anag", SQLBuilder.EQUALS, anagrafico.getCd_anag());
                it.cnr.jada.persistency.Broker broker = home.createBroker(sql);

                while (broker.next()) {
                    TerzoBulk terzo = (TerzoBulk) broker.fetch(TerzoBulk.class);

                    SQLBuilder sql2 = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
                    sql2.addClause("AND", "cd_terzo", SQLBuilder.EQUALS, terzo.getCd_terzo());
                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        throw new ApplicationException("Campo \"Titolo di Studio\" obbligatorio. Esiste almeno un incarico emesso per il terzo (" + terzo.getCd_terzo() + ") in oggetto.");
                }
            } catch (Throwable e) {
                throw handleException(e);
            }
        }
        verificaDataFineRapporto(userContext, anagrafico);


        // * RP 18/02/2010 Controllo Struttuta Partita Iva INTRA CEE

        if (NazioneBulk.CEE.equals(anagrafico.getTi_italiano_estero())) {
            // Per testare la partita iva
            try {
                if (anagrafico.getPartita_iva() != null && !verificaStrutturaPiva(userContext, anagrafico))
                    throw new ApplicationMessageFormatException(
                            "Verificare la partita Iva non corrisponde al modello della sua nazionalità. " +
                            "Modello di riferimento: '{0}'", Optional.ofNullable(anagrafico.getNazionalita())
                            .map(NazioneBulk::getStruttura_piva).orElse(""));
            } catch (ValidationException e) {
                throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
            }
        }

        /* Valida i campi del TAB <code>Dettagli</code>
         *
         * Creation date: (23/08/2002 11.10.44)
         * Author: Borriello Gennaro
         */
        validaDettagli(anagrafico);
        if (anagrafico.getCarichi_familiari_anag() != null) {
            validaTi_persona(userContext, anagrafico);
        }
        controlliGruppoIVA(userContext, anagrafico);
    }

    private void controlliGruppoIVA(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        if (!anagrafico.getAssGruppoIva().isEmpty()){
            AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
            for (Iterator di = anagrafico.getAssGruppoIva().iterator(); di.hasNext(); ) {
                AssGruppoIvaAnagBulk ass = (AssGruppoIvaAnagBulk) di.next();

                if (ass.isToBeCreated()|| ass.isToBeUpdated()) {
                    Collection coll = null;
                        try {
                            if (anagrafico.isGruppoIVA()){
                                coll = anagraficoHome.findGruppiIvaAssociati(ass.getAnagrafico());
                            } else {
                                coll = anagraficoHome.findGruppiIvaAssociati(anagrafico);
                            }
                        } catch (IntrospectionException e) {
                            throw handleException(e);
                        } catch (PersistencyException e) {
                            throw handleException(e);
                        }
                        if (!coll.isEmpty()){
                                for (Iterator d = coll.iterator(); d.hasNext(); ) {
                                    AssGruppoIvaAnagBulk assColl = (AssGruppoIvaAnagBulk) d.next();
                                    if (anagrafico.isGruppoIVA()){
                                        controlloCongruenzaDateGruppiIva(userContext, anagrafico, assColl.getAnagraficoGruppoIva());
                                    } else {
                                        controlloCongruenzaDateGruppiIva(userContext, ass.getAnagraficoGruppoIva(), assColl.getAnagraficoGruppoIva());
                                    }
                                }
                        }
                    }
                }
            if (anagrafico.isGruppoIVA()){
                for (Iterator di = anagrafico.getAssGruppoIva().iterator(); di.hasNext(); ) {
                    AssGruppoIvaAnagBulk ass = (AssGruppoIvaAnagBulk) di.next();
                    try {
                        Collection coll = anagraficoHome.findGruppiIvaAssociati(ass.getAnagrafico());
                        for (Iterator d = coll.iterator(); d.hasNext(); ) {
                            AssGruppoIvaAnagBulk assColl = (AssGruppoIvaAnagBulk) d.next();
                            if (!assColl.getAnagraficoGruppoIva().equalsByPrimaryKey(anagrafico)){
                                controlloCongruenzaDateGruppiIva(userContext, anagrafico, assColl.getAnagraficoGruppoIva());
                            }
                        }
                    } catch (IntrospectionException e) {
                        throw handleException(e);
                    } catch (PersistencyException e) {
                        throw handleException(e);
                    }
                }
            }
        }
    }

    private void controlloCongruenzaDateGruppiIva(UserContext userContext, AnagraficoBulk anagrafico, AnagraficoBulk anagraficoAssBulk) throws ApplicationException, ComponentException {
        try {
            if (!anagrafico.isToBeCreated())
                anagrafico = (AnagraficoBulk)getHome(userContext,anagrafico).findByPrimaryKey(anagrafico);
            if (!anagraficoAssBulk.isToBeCreated())
                anagraficoAssBulk = (AnagraficoBulk)getHome(userContext,anagraficoAssBulk).findByPrimaryKey(anagraficoAssBulk);

            if (anagraficoAssBulk.getDt_canc() != null && anagrafico.getDtIniValGruppoIva() != null && anagrafico.getDt_canc() != null && anagraficoAssBulk.getDtIniValGruppoIva() != null &&
                ((anagraficoAssBulk.getDt_canc().compareTo(anagrafico.getDtIniValGruppoIva()) >= 0 &&
                  anagrafico.getDt_canc().compareTo(anagraficoAssBulk.getDtIniValGruppoIva()) >= 0) ||
                 (anagraficoAssBulk.getDtIniValGruppoIva().compareTo(anagrafico.getDt_canc()) <= 0 &&
                  anagrafico.getDtIniValGruppoIva().compareTo(anagraficoAssBulk.getDt_canc()) <= 0))) {
                throw new ApplicationException("Date incongruenti per i gruppi IVA associati all'anagrafico " + anagraficoAssBulk.getCd_anag());
            }
        } catch (PersistencyException e) {
            throw handleException(e);
        } catch (ComponentException e) {
            throw handleException(e);
        }
    }

    /**
     * <B>A correzione dell'errore No. 02234A</B>
     * <p>
     * Metodo creato per il controllo dei dati inseriti nel Tab Dettagli.
     * Il metodo è invocato durante la validazione di un Anagrafica per Creazione,
     * (metodo validaCreaModificaConBulk), e, per ora, controlla, solo la validità del campo
     * <code>Aliquota Fiscale</code>
     * <p>
     * Creation date: (23/08/2002 11.10.44)
     * Author: Borriello Gennaro
     *
     * @param anagrafico <code>AnagraficoBulk</code> il bulk da controllare
     */
    protected void validaDettagli(AnagraficoBulk anagrafico) throws ApplicationException {

        java.math.BigDecimal cento = new java.math.BigDecimal(100);
        java.math.BigDecimal zero = new java.math.BigDecimal(0);

        if (anagrafico.getAliquota_fiscale() != null) {
            if (anagrafico.getAliquota_fiscale().compareTo(zero) < 0 || anagrafico.getAliquota_fiscale().compareTo(cento) > 0) {
                throw new ApplicationException("Attenzione: Aliquota Fiscale non valida.");
            }
        }
    }

    /**
     * Data fine rapporto anagrafico non valida
     * PreCondition:
     * L'utente ha richiesto la modifica della data di fine rapporto di un anagrafico ed ha impostato una data antecedente alla data odierna
     * PostCondition:
     * Viene generata una ApplicationException con il messaggio: "La data di fine rapporto dell'anagrafico deve essere superiore alla data odierna"
     * Data fine rapporto terzo non valida
     * PreCondition:
     * L'utente ha modificato la data di fine rapporto di un terzo ed ha specificato una data antecedente alla data odierna
     * PostCondition:
     * Viene generata una ApplicationException con il messaggio: "La data di fine rapporto del terzo deve essere maggiore della data odierna"
     * Anagrafico valido, richiesta cancellazione
     * PreCondition:
     * L'utente ha richiesto la cancellazione di un anagrafico valido
     * PostCondition:
     * Viene impostata la data di fine rapporto dell'anagrafico e di ogni terzo con data di fine rapporto nulla o superiore alla data odierna con la data odierna.
     * Anagrafico con data di fine rapporto, richiesta modifica data fine rapporto
     * PreCondition:
     * L'utente ha richiesto la modifica della data di fine rapporto di un anagrafico che ha già impostata la data di fine rapporto
     * PostCondition:
     * Viene salvata la data di fine rapporto dell'anagrafico e impostata la data di fine rapporto di ogni terzo con data di fine uguale alla precedente data di fine rapporto dell'anagrafico
     */
    public void verificaDataFineRapporto(UserContext userContext, AnagraficoBulk anagrafico) throws it.cnr.jada.comp.ComponentException {
        try {
            java.sql.Timestamp dt_odierna = getHome(userContext, anagrafico).getServerDate();
/*
		AnagraficoBulk anagrafico_bck = (AnagraficoBulk)getHome(userContext,anagrafico).findByPrimaryKey(anagrafico);

		// Se la data di fine rapporto è stata modificata
		if (anagrafico_bck != null) {
			if (anagrafico.getDt_fine_rapporto() == null) {
				if (anagrafico_bck.getDt_fine_rapporto() != null)
					throw new ApplicationException("Non è possibile togliere la data di fine rapporto di un anagrafico.");
			} else if (!anagrafico.getDt_fine_rapporto().equals(anagrafico_bck.getDt_fine_rapporto())) {
				if (anagrafico.getDt_fine_rapporto().before(dt_odierna))
					throw new ApplicationException("La data di fine rapporto di un anagrafico non può essere anteriore alla data odierna");
				adeguaDt_fine_rapportoTerzi(userContext,anagrafico);
			}
		}
*/
            verificaNuovoRapporto(anagrafico);
        } catch (Throwable e) {
            throw handleException(anagrafico, e);
        }
    }

    /**
     * Controlla i periodi di validità dei rapporti di tipo NON DIPENDENTE.
     * Per ogni Rapporto di tipo NON DIPENDENTE, si controlla che la Data di Inizio Validità
     * e quella di Fine Validità non siano all'interno di un periodo di validità di un Rapporto DIPENDENTE.
     * In tal caso viene lanciato un messaggio all'utente.
     */
    private void verificaNuovoRapporto(AnagraficoBulk anagrafico) throws it.cnr.jada.comp.ComponentException {

        boolean wasDipendente = false;
        BulkList rapporti_dip = new BulkList();

        // Cerca tutti i rapporti di tipo DIPENDENTE
        for (Iterator di = anagrafico.getRapporti().iterator(); di.hasNext(); ) {
            RapportoBulk rap_dip = (RapportoBulk) di.next();

            if (rap_dip.getTipo_rapporto().isDipendente()) {
                wasDipendente = true;
                rapporti_dip.add(rap_dip);
            }
        }

        // Se esiste almeno un rapporto di tipo DIPENDENTE, controlla tutti gli altri rapporti
        if (wasDipendente) {

            try {

                for (java.util.Iterator i = anagrafico.getRapporti().iterator(); i.hasNext(); ) {
                    RapportoBulk rapporto = (RapportoBulk) i.next();

                    if (!rapporto.getTipo_rapporto().isDipendente()) {

                        // Confronta il Tipo NON DIPENDENTE con tutti i rapporti DIPENDENTE, cercando
                        //	delle incongruenze nelle date di Inizio/FIne periodo.
                        for (Iterator i_dip = rapporti_dip.iterator(); i_dip.hasNext(); ) {
                            RapportoBulk rapporto_dip = (RapportoBulk) i_dip.next();

                            // Controlla la data di INIZIO validità del rapporto NON dipendente
// Aggiunta la condizione se non è dipendente perchè ci sono casi in cui è necessario creare un rapporto ASS su un dipendente attivo. Questa possibilità è data solo agli utenti con privilegio ALLTRA.
                            if (!anagrafico.isDipendente() && rapporto.getDt_ini_validita().after(rapporto_dip.getDt_ini_validita()) &&
                                    rapporto.getDt_ini_validita().before(rapporto_dip.getDt_fin_validita())) {

                                throw new ApplicationException("Attenzione: la data di INIZIO validità del rapporto "
                                        + rapporto.getTipo_rapporto().getDs_tipo_rapporto()
                                        + " è incompatibile col periodo di validità del rapporto "
                                        + rapporto_dip.getTipo_rapporto().getDs_tipo_rapporto());
                            }
                            // Controlla la data di FINE validità del rapporto NON dipendente
// Aggiunta la condizione se non è dipendente perchè ci sono casi in cui è necessario creare un rapporto ASS su un dipendente attivo. Questa possibilità è data solo agli utenti con privilegio ALLTRA.
                            else if (!anagrafico.isDipendente() && rapporto.getDt_fin_validita().after(rapporto_dip.getDt_ini_validita()) &&
                                    rapporto.getDt_fin_validita().before(rapporto_dip.getDt_fin_validita())) {

                                throw new ApplicationException("Attenzione: la data di FINE validità del rapporto "
                                        + rapporto.getTipo_rapporto().getDs_tipo_rapporto()
                                        + " è incompatibile col periodo di validità del rapporto "
                                        + rapporto_dip.getTipo_rapporto().getDs_tipo_rapporto());
                            }

                        } // Fine ciclo sui Rapporti di tipo DIPENDENTE

                    } // end if

                } // Fine ciclo sui Rapporti NON DIPENDENTI
            } catch (Throwable t) {
                throw new ComponentException(t);
            }

        } //end if
    }

    public boolean isItalianoEsteroModificabile(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        try {
            if (anagrafico.getCd_anag() != null) {
                if (isAnagraficaModificabile(userContext, anagrafico)) {
                    /**è modificabile solo se è Estero, se è Persona Fisica e se
                     non ha fatture passive nell'anno */

                    /**serve per prendere il valore di ti_italiano_estero dal db*/
                    AnagraficoHome aHome = (AnagraficoHome) getHomeCache(userContext).getHome(AnagraficoBulk.class);
                    AnagraficoBulk aKey = new AnagraficoBulk(anagrafico.getCd_anag());
                    AnagraficoBulk a = (AnagraficoBulk) aHome.findByPrimaryKey(aKey);

                    if (a.getTi_italiano_estero().equals(new String("I")) ||
                            !a.getTi_entita().equals(new String("F")))
                        return false;

                    TerzoHome home = (TerzoHome) getHome(userContext, TerzoBulk.class);
                    SQLBuilder sql = home.createSQLBuilder();
                    sql.addClause("AND", "cd_anag", sql.EQUALS, anagrafico.getCd_anag());

                    if (sql.executeCountQuery(getConnection(userContext)) > 1)
                        return false;
                    it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
                    if (!broker.next())
                        return false;
                    TerzoBulk terzo = (TerzoBulk) broker.fetch(TerzoBulk.class);
                    broker.close();

                    Fattura_passivaHome homeFp = (Fattura_passivaHome) getHome(userContext, Fattura_passivaBulk.class);
                    SQLBuilder sqlfp = homeFp.createSQLBuilder();
                    sqlfp.addClause("AND", "esercizio", sqlfp.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
                    sqlfp.addClause("AND", "cd_terzo", sqlfp.EQUALS, terzo.getCd_terzo());

                    if (sqlfp.executeCountQuery(getConnection(userContext)) > 0)
                        return false;
                    else
                        return true;
                }
                return false;
            }
            return true;

        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void checkConiugeAlreadyExistFor(UserContext userContext, AnagraficoBulk anagrafico, Carico_familiare_anagBulk carico) throws ComponentException {
        for (java.util.Iterator i = anagrafico.getCarichi_familiari_anag().iterator(); i.hasNext(); ) {
            Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();
            if (!carico.equals(carico_familiare) &&
                    !((carico.getDt_ini_validita().before(carico_familiare.getDt_ini_validita()) &&
                            carico.getDt_ini_validita().before(carico_familiare.getDt_fin_validita()) &&
                            carico.getDt_fin_validita().before(carico_familiare.getDt_ini_validita()) &&
                            carico.getDt_fin_validita().before(carico_familiare.getDt_fin_validita())) ||
                            (carico.getDt_ini_validita().after(carico_familiare.getDt_ini_validita()) &&
                                    carico.getDt_ini_validita().after(carico_familiare.getDt_fin_validita()) &&
                                    carico.getDt_fin_validita().after(carico_familiare.getDt_ini_validita()) &&
                                    carico.getDt_fin_validita().after(carico_familiare.getDt_fin_validita())))) {
                if (carico.isConiuge() && carico_familiare.isConiuge())
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare un Coniuge per questo periodo, esiste un Coniuge valido nello stesso periodo!");
                else if (carico.isConiuge() && carico_familiare.isFiglio() && carico_familiare.getFl_primo_figlio_manca_con().booleanValue())
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare un Coniuge per questo periodo, esiste già un primo figlio in assenza di Coniuge nello stesso periodo!");
                else if (carico.isFiglio() && carico.getFl_primo_figlio_manca_con().booleanValue() && carico_familiare.isConiuge())
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare primo figlio in assenza di Coniuge, esiste un Coniuge valido nello stesso periodo!");
                else if (carico.isFiglio() && carico.getFl_primo_figlio_manca_con().booleanValue() && carico_familiare.isFiglio() && carico_familiare.getFl_primo_figlio_manca_con().booleanValue())
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare primo figlio in assenza di Coniuge, esiste già un primo figlio in assenza di Coniuge nello stesso periodo!");
                else if (carico.getCodice_fiscale().equals(carico_familiare.getCodice_fiscale()))
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: è stato già inserito un carico con lo stesso Codice Fiscale!");
                else if (!carico.isConiuge() && carico.getCodice_fiscale().equals(carico_familiare.getCodice_fiscale_altro_gen()))
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: il carico ha lo stesso codice fiscale dell'altro genitore!");
                else if (carico.isFiglio() &&
                        carico.getCodice_fiscale_altro_gen() != null &&
                        carico.getCodice_fiscale_altro_gen().equals(carico_familiare.getCodice_fiscale()) && !carico_familiare.isConiuge())
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: Il Codice Fiscale dell'altro genitore è uguale a quello di un altro carico!");
            }
        }
    }

    private boolean isAnagraficaUtilizzata(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        try {

            AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);

            if (anagraficoHome.existsObbligazioni(anagrafico) ||
                    anagraficoHome.existsAccertamenti(anagrafico) ||
                    anagraficoHome.existsRigheDocumenti(anagrafico) ||
                    anagraficoHome.existsFattureAttive(anagrafico) ||
                    anagraficoHome.existsFatturePassive(anagrafico) ||
                    anagraficoHome.existsMissioni(anagrafico) ||
                    anagraficoHome.existsCompensi(anagrafico) ||
                    anagraficoHome.existsMinicarriere(anagrafico) ||
                    anagraficoHome.existsConguagli(anagrafico) ||
                    anagraficoHome.existsAnticipi(anagrafico))
                return true;
        } catch (Throwable e) {
            throw handleException(e);
        }
        return false;
    }

    private boolean isAnagraficaUtilizzataDetrazione(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        try {

            AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);

            if (!anagraficoHome.findCompensoValido(anagrafico).isEmpty())
                return true;
        } catch (Throwable e) {
            throw handleException(e);
        }
        return false;
    }

    public boolean esisteConiugeValido(UserContext userContext, AnagraficoBulk anagrafico, Carico_familiare_anagBulk carico) throws ComponentException {
        Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
        java.util.GregorianCalendar data_da = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        java.util.GregorianCalendar data_a = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        if (carico == null) {
            it.cnr.contab.anagraf00.core.bulk.AnagraficoHome home = (it.cnr.contab.anagraf00.core.bulk.AnagraficoHome) getHome(userContext, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.class);
            try {
                for (java.util.Iterator i = home.findCarichi_familiari_anag(anagrafico).iterator(); i.hasNext(); ) {
                    Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();
                    if (!carico.equals(carico_familiare)) {
                        data_da.setTime(carico_familiare.getDt_ini_validita());
                        data_a.setTime(carico_familiare.getDt_fin_validita());
                        if (carico_familiare.isConiuge() &&
                                data_da.get(java.util.GregorianCalendar.YEAR) <= esercizio &&
                                data_a.get(java.util.GregorianCalendar.YEAR) >= esercizio) {
                            return true;
                        }
                    }
                }
            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(e);
            } catch (it.cnr.jada.persistency.IntrospectionException e) {
                throw handleException(e);
            }
        } else {
            for (java.util.Iterator i = anagrafico.getCarichi_familiari_anag().iterator(); i.hasNext(); ) {
                Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();
                if (!carico.equals(carico_familiare)) {
                    data_da.setTime(carico_familiare.getDt_ini_validita());
                    data_a.setTime(carico_familiare.getDt_fin_validita());
                    if (carico_familiare.isConiuge() &&
                            data_da.get(java.util.GregorianCalendar.YEAR) <= esercizio &&
                            data_a.get(java.util.GregorianCalendar.YEAR) >= esercizio) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean esisteFiglioValido(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
        java.util.GregorianCalendar data_da = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        java.util.GregorianCalendar data_a = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        it.cnr.contab.anagraf00.core.bulk.AnagraficoHome home = (it.cnr.contab.anagraf00.core.bulk.AnagraficoHome) getHome(userContext, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.class);
        try {
            for (java.util.Iterator i = home.findCarichi_familiari_anag(anagrafico).iterator(); i.hasNext(); ) {
                Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();
                data_da.setTime(carico_familiare.getDt_ini_validita());
                data_a.setTime(carico_familiare.getDt_fin_validita());
                if (carico_familiare.isFiglio() &&
                        data_da.get(java.util.GregorianCalendar.YEAR) <= esercizio &&
                        data_a.get(java.util.GregorianCalendar.YEAR) >= esercizio) {
                    return true;
                }
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(e);
        }
        return false;
    }

    public boolean esisteAltroFamValido(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException {
        Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
        java.util.GregorianCalendar data_da = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        java.util.GregorianCalendar data_a = (java.util.GregorianCalendar) java.util.GregorianCalendar.getInstance();
        it.cnr.contab.anagraf00.core.bulk.AnagraficoHome home = (it.cnr.contab.anagraf00.core.bulk.AnagraficoHome) getHome(userContext, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.class);
        try {
            for (java.util.Iterator i = home.findCarichi_familiari_anag(anagrafico).iterator(); i.hasNext(); ) {
                Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();
                data_da.setTime(carico_familiare.getDt_ini_validita());
                data_a.setTime(carico_familiare.getDt_fin_validita());
                if (carico_familiare.isAltro() &&
                        data_da.get(java.util.GregorianCalendar.YEAR) <= esercizio &&
                        data_a.get(java.util.GregorianCalendar.YEAR) >= esercizio) {
                    return true;
                }
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(e);
        }
        return false;
    }

    public void validaTi_persona(UserContext userContext, AnagraficoBulk carico) {
        try {
            List list = carico.getCarichi_familiari_anag();
            for (int j = list.size() - 1; j >= 0; j--) {
                Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) list.get(j);
                if (carico_familiare.isFiglio() &&
                        !carico_familiare.getFl_primo_figlio_manca_con() &&
                        carico_familiare.getPrc_carico().compareTo(new java.math.BigDecimal(100)) == 0
                        //&&
                        //!esisteConiugeValido(userContext,carico_familiare.getAnagrafico(),carico_familiare)
                        &&
                        carico_familiare.getCodice_fiscale_altro_gen() == null)
                    throw new ApplicationException("Carichi Familiari: per il Figlio è necessario specificare il Codice fiscale dell'altro genitore oppure è necessario inserire il Coniuge");
            }
        } catch (javax.ejb.EJBException e) {
            throw new it.cnr.jada.DetailedRuntimeException(e);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw new it.cnr.jada.DetailedRuntimeException(ex);
        }
    }

    public boolean isGestiteDeduzioniIrpef(UserContext userContext) throws ComponentException {
        try {
            Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession", Parametri_cnrComponentSession.class)).getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
            return
                    par.getFl_deduzione_irpef();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isGestiteDeduzioniFamily(UserContext userContext) throws ComponentException {
        try {
            Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession", Parametri_cnrComponentSession.class)).getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
            return
                    par.getFl_deduzione_family();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isGestiteDetrazioniAltre(UserContext userContext) throws ComponentException {
        try {
            Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession", Parametri_cnrComponentSession.class)).getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
            return
                    par.getFl_detrazioni_altre();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isGestiteDetrazioniFamily(UserContext userContext) throws ComponentException {
        try {
            Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession", Parametri_cnrComponentSession.class)).getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
            return
                    par.getFl_detrazioni_family();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public List EstraiLista(UserContext userContext, Long prog_estrazione) throws ComponentException {

        EcfHome home = (EcfHome) getHome(userContext, EcfBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
        sql.addClause("AND", "prog_estrazione", sql.EQUALS, prog_estrazione);
        sql.addOrderBy("ESERCIZIO,PROG");
        try {
            return home.fetchAll(sql);
        } catch (PersistencyException e) {
            handleException(e);
        }
        return null;
    }

    public Long Max_prog_estrazione(UserContext userContext) throws ComponentException {
        try {
            EcfHome home = (EcfHome) getHome(userContext, EcfBulk.class);
            return home.getMaxProg_estrazione(userContext);
        } catch (PersistencyException e) {
            handleException(e);
        }
        return null;
    }

    public void Popola_ecf(UserContext userContext, Long prog_estrazione) throws ComponentException {
        try {
            EcfHome home = (EcfHome) getHome(userContext, EcfBulk.class, "ELENCO_CF", "none");
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.openParenthesis("AND");
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", " fr003001", sql.ISNOTNULL, null);
            sql.addSQLClause("AND", " cl002001", sql.ISNULL, null);
            sql.addSQLClause("AND", " cl003001", sql.ISNULL, null);
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", " fr004001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr004002", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr005001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr006001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr007001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr008001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr009001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr009002", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr010001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr011001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr012001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", " fr013001", sql.NOT_EQUALS, 0);
            sql.closeParenthesis();
            sql.closeParenthesis();
            sql.openParenthesis("OR");
            sql.addSQLClause("AND", "cl003001", sql.ISNOTNULL, null);
            sql.addSQLClause("AND", "fr002001", sql.ISNULL, null);
            sql.addSQLClause("AND", "fr003001", sql.ISNULL, null);
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "cl004001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl004002", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl005001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl006001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl007001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl008001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl008002", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl009001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl010001", sql.NOT_EQUALS, 0);
            sql.addSQLClause("OR", "cl011001", sql.NOT_EQUALS, 0);
            sql.closeParenthesis();
            sql.closeParenthesis();
            sql.closeParenthesis();
            sql.addOrderBy("ESERCIZIO,CL002001,CL003001,FR002001,FR003001,PROG");
            List lista_ecf = home.fetchAll(sql);
            for (Iterator i = lista_ecf.iterator(); i.hasNext(); ) {
                EcfBulk ecf = (EcfBulk) i.next();
                EcfBulk new_ecf = new EcfBulk();
                new_ecf.setEsercizio(ecf.getEsercizio());
                new_ecf.setProg_estrazione(prog_estrazione);

                new_ecf.setCl002001(ecf.getCl002001());
                new_ecf.setCl003001(ecf.getCl003001());
                new_ecf.setCl004001(ecf.getCl004001());
                new_ecf.setCl004002(ecf.getCl004002());
                new_ecf.setCl005001(ecf.getCl005001());
                new_ecf.setCl006001(ecf.getCl006001());
                new_ecf.setCl007001(ecf.getCl007001());
                new_ecf.setCl008001(ecf.getCl008001());
                new_ecf.setCl008002(ecf.getCl008002());
                new_ecf.setCl009001(ecf.getCl009001());
                new_ecf.setCl010001(ecf.getCl010001());
                new_ecf.setCl011001(ecf.getCl011001());

                new_ecf.setFr002001(ecf.getFr002001());
                new_ecf.setFr003001(ecf.getFr003001());
                new_ecf.setFr004001(ecf.getFr004001());
                new_ecf.setFr004002(ecf.getFr004002());
                new_ecf.setFr005001(ecf.getFr005001());
                new_ecf.setFr006001(ecf.getFr006001());
                new_ecf.setFr007001(ecf.getFr007001());
                new_ecf.setFr008001(ecf.getFr008001());
                new_ecf.setFr009001(ecf.getFr009001());
                new_ecf.setFr009002(ecf.getFr009002());
                new_ecf.setFr010001(ecf.getFr010001());
                new_ecf.setFr011001(ecf.getFr011001());
                new_ecf.setFr012001(ecf.getFr012001());
                new_ecf.setFr013001(ecf.getFr013001());

                new_ecf.setToBeCreated();
                ((CRUDComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class)).creaConBulk(userContext, new_ecf);
            }
        } catch (PersistencyException e) {
            handleException(e);
        } catch (RemoteException e) {
            handleException(e);
        } catch (EJBException e) {
            handleException(e);
        }
    }

    public java.sql.Timestamp findMaxFinMissione(UserContext context, AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, ComponentException {
        MissioneHome missioneHome = (MissioneHome) getHome(context, MissioneBulk.class);
        SQLBuilder sql = missioneHome.createSQLBuilder();
        sql.addTableToHeader("TERZO");
        sql.addSQLJoin("MISSIONE.CD_TERZO", "TERZO.CD_TERZO");
        sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
        sql.addOrderBy("DT_FINE_MISSIONE DESC");
        java.util.List result = missioneHome.fetchAll(sql);
        if (result == null || result.isEmpty())
            return null;
        else {
            MissioneBulk missione = (MissioneBulk) result.get(0);
            return missione.getDt_fine_missione();
        }
    }

    public SQLBuilder selectComune_nascitaByClause(UserContext userContext,
                                                   AnagraficoBulk anag,
                                                   ComuneBulk comune,
                                                   CompoundFindClause clause)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        if (clause == null) clause = comune.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, comune).createSQLBuilder();
        if (clause != null) sql.addClause(clause);

        return sql;
    }

    public Timestamp findMaxDataCompValida(UserContext context, AnagraficoBulk anagrafico) throws PersistencyException, ComponentException, IntrospectionException {
        CompensoHome compensoHome = (CompensoHome) getHome(context, CompensoBulk.class);
        SQLBuilder sql = compensoHome.createSQLBuilder();
        sql.setHeader("SELECT TRUNC(MAX(DT_A_COMPETENZA_COGE)) AS DT_A_COMPETENZA_COGE");
        sql.addTableToHeader("TERZO");
        sql.addSQLJoin("COMPENSO.CD_TERZO", "TERZO.CD_TERZO");
        sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
        sql.addSQLClause("AND", "COMPENSO.STATO_COFI", SQLBuilder.NOT_EQUALS, CompensoBulk.STATO_ANNULLATO);
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "COMPENSO.FL_COMPENSO_CONGUAGLIO", SQLBuilder.EQUALS, "Y");
        sql.addSQLClause("OR", "COMPENSO.FL_COMPENSO_MINICARRIERA", SQLBuilder.EQUALS, "Y");
        sql.closeParenthesis();

        Broker broker = compensoHome.createBroker(sql);
        Object value = null;
        if (broker.next()) {
            value = broker.fetchPropertyValue("dt_a_competenza_coge", compensoHome.getIntrospector().getPropertyType(compensoHome.getPersistentClass(), "dt_a_competenza_coge"));
            broker.close();
        }
        return (Timestamp) value;
    }

    public boolean verificaStrutturaPiva(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException, ValidationException {
        if (anagrafico != null && anagrafico.getPartita_iva() == null && anagrafico.getFl_non_obblig_p_iva().booleanValue())
            return true;
        if (anagrafico == null || anagrafico.getPartita_iva() == null)
            return false;
        if (anagrafico.getPartita_iva().contains(" "))
            throw new ApplicationException("Eliminare gli spazi nella partita iva.");

        NazioneBulk nazione = new NazioneBulk();
        nazione.setPg_nazione(anagrafico.getPg_nazione_fiscale());
        try {
            nazione = (NazioneBulk) getHome(userContext, nazione).findByPrimaryKey(nazione);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }

        if (nazione != null)
            anagrafico.setNazionalita(nazione);
        else {
            if (anagrafico.getComune_fiscale() != null && anagrafico.getComune_fiscale().getNazione() != null)
                anagrafico.setNazionalita(anagrafico.getComune_fiscale().getNazione());
            else if (anagrafico.getComune_fiscale() != null && anagrafico.getComune_fiscale().getPg_comune() != null) {
                try {
                    anagrafico.setComune_fiscale((ComuneBulk) getHome(userContext, anagrafico.getComune_fiscale()).findByPrimaryKey(anagrafico.getComune_fiscale()));
                    anagrafico.setNazionalita((NazioneBulk) getHome(userContext, anagrafico.getComune_fiscale().getNazione()).findByPrimaryKey(anagrafico.getComune_fiscale().getNazione()));
                } catch (it.cnr.jada.persistency.PersistencyException ex) {
                    throw handleException(ex);
                }
            }
        }
        //RP intrastat per il momento sospeso controllo per altre nazione CEE
        if (anagrafico.getNazionalita() != null && anagrafico.getNazionalita().getStruttura_piva() == null)
            throw new ApplicationException("Non è definito nessun modello di partita Iva per la nazione.");
        //return true;
        if (anagrafico.getNazionalita() != null && anagrafico.getNazionalita().getStruttura_piva() != null) {
            for (int i = 0; i < anagrafico.getNazionalita().getStrutturaPivaModelliPossibili(); i++) {
                if (anagrafico.getPartita_iva().length() == anagrafico.getNazionalita().getStrutturaPivaModello(i + 1).length()) {
                    for (int y = 0; y < anagrafico.getNazionalita().getStrutturaPivaModello(i + 1).length(); y++) {
                        if (anagrafico.getNazionalita().getStrutturaPivaModello(i + 1).charAt(y) != NazioneBulk.IBAN_TIPO_ALFANUMERICO.charAt(0)) {
                            try {
                                char data[] = {anagrafico.getPartita_iva().charAt(y)};
                                int appo = Integer.parseInt(new String(data));
                                if (anagrafico.getNazionalita().getStrutturaPivaModello(i + 1).charAt(y) == NazioneBulk.IBAN_TIPO_CARATTERE.charAt(0))
                                    throw new ValidationException("Il " + (y + 1) + "° carattere della partita Iva non deve essere un numero.");
                            } catch (ValidationException e) {
                                throw e;
                            } catch (Exception e) {
                                if (anagrafico.getNazionalita().getStrutturaPivaModello(i + 1).charAt(y) == NazioneBulk.IBAN_TIPO_NUMERICO.charAt(0))
                                    throw new ValidationException("Il " + (y + 1) + "° carattere della partita Iva deve essere un numero.");
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void checkCaricoAlreadyExistFor(UserContext userContext,
                                           AnagraficoBulk anagrafico, Carico_familiare_anagBulk carico) throws ComponentException {
        for (java.util.Iterator i = anagrafico.getCarichi_familiari_anag().iterator(); i.hasNext(); ) {
            Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();
            if (!carico.equals(carico_familiare) &&
                    !((carico.getDt_ini_validita().before(carico_familiare.getDt_ini_validita()) &&
                            carico.getDt_ini_validita().before(carico_familiare.getDt_fin_validita()) &&
                            carico.getDt_fin_validita().before(carico_familiare.getDt_ini_validita()) &&
                            carico.getDt_fin_validita().before(carico_familiare.getDt_fin_validita())) ||
                            (carico.getDt_ini_validita().after(carico_familiare.getDt_ini_validita()) &&
                                    carico.getDt_ini_validita().after(carico_familiare.getDt_fin_validita()) &&
                                    carico.getDt_fin_validita().after(carico_familiare.getDt_ini_validita()) &&
                                    carico.getDt_fin_validita().after(carico_familiare.getDt_fin_validita())))) {
                if (carico.getCodice_fiscale().compareTo(carico_familiare.getCodice_fiscale()) == 0) {
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare un carico in questo periodo con questo codice fiscale, esiste già un carico valido nello stesso periodo!");
                }
            }
        }
    }

    public void controllaUnicitaCaricoInAnnoImposta(UserContext userContext, AnagraficoBulk anagrafico, Carico_familiare_anagBulk carico) throws ComponentException {
        GregorianCalendar dataInCarico = new GregorianCalendar();
        GregorianCalendar dataFinCarico = new GregorianCalendar();
        dataInCarico.setTime(carico.getDt_ini_validita());
        dataFinCarico.setTime(carico.getDt_fin_validita());

        GregorianCalendar dataInCaricoFamiliare = new GregorianCalendar();
        GregorianCalendar dataFinCaricoFamiliare = new GregorianCalendar();

        for (java.util.Iterator i = anagrafico.getCarichi_familiari_anag().iterator(); i.hasNext(); ) {
            Carico_familiare_anagBulk carico_familiare = (Carico_familiare_anagBulk) i.next();
            dataInCaricoFamiliare.setTime(carico_familiare.getDt_ini_validita());
            dataFinCaricoFamiliare.setTime(carico_familiare.getDt_fin_validita());
            if (!carico.equals(carico_familiare) &&
                    carico.getTi_persona().equals(carico_familiare.getTi_persona()) &&
                    carico.getCodice_fiscale().equals(carico_familiare.getCodice_fiscale()) &&
                    (dataInCarico.get(GregorianCalendar.YEAR) == (dataInCaricoFamiliare.get(GregorianCalendar.YEAR)) ||
                            dataInCarico.get(GregorianCalendar.YEAR) == (dataFinCaricoFamiliare.get(GregorianCalendar.YEAR)) ||
                            dataFinCarico.get(GregorianCalendar.YEAR) == (dataInCaricoFamiliare.get(GregorianCalendar.YEAR)) ||
                            dataFinCarico.get(GregorianCalendar.YEAR) == (dataFinCaricoFamiliare.get(GregorianCalendar.YEAR)))
                    )
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: Non è possibile inserire per lo stesso carico familiare più dettagli relativi ad uno stesso anno d'imposta!");
        }
    }

    public boolean isGestitoCreditoIrpef(UserContext userContext) throws ComponentException {
        try {
            Parametri_cnrBulk par = ((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession", Parametri_cnrComponentSession.class)).getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
            return
                    par.getFl_credito_irpef();
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
    public void aggiornaDatiAce(UserContext userContext, AnagraficoBulk anagraficoBulk) throws ComponentException {
        if (!Optional.ofNullable(aceService).isPresent())
            return;
                logger.info(anagraficoBulk.getCd_anag().toString());
                anagraficoBulk = (AnagraficoBulk)inizializzaBulkPerModifica(userContext, anagraficoBulk);
                try {
            if (!anagraficoBulk.isDipendente() && !anagraficoBulk.getRapporti().isEmpty()){
                Optional<String> personaId = Optional.empty();
                try {
                    personaId = Optional.ofNullable(aceService.getPersonaId(anagraficoBulk.getCodice_fiscale()));
                } catch (FeignException _ex) {
                }
                List<RapportoBulk> rapportiValidi = new LinkedList<>();
                boolean isExDipendente = false;
                for (Object obj : anagraficoBulk.getRapporti()){
                    RapportoBulk rapportoBulk = (RapportoBulk) obj;
                    Tipo_rapportoBulk tipo_rapportoBulk = (Tipo_rapportoBulk) getHome(userContext, Tipo_rapportoBulk.class).findByPrimaryKey(rapportoBulk.getTipo_rapporto());
                    if (tipo_rapportoBulk.isDipendente()){
                        isExDipendente = true;
                    }
                    if ((rapportoBulk.getDt_ini_validita().compareTo(getCurrentDate()) > 0 ||
                            rapportoBulk.getDt_fin_validita().compareTo(getCurrentDate()) > 0) && !tipo_rapportoBulk.isDipendente()  &&
                            RapportoBulk.TIPOCONTRATTO_ACE.containsKey(tipo_rapportoBulk.getCd_tipo_rapporto())){
                        rapportiValidi.add(rapportoBulk);
                    }
                }

                if (!personaId.isPresent()) {
                    for (RapportoBulk rapportoBulk : rapportiValidi){
                        if (rapportoBulk.getCd_tipo_rapporto().equals(VIncarichiAssRicBorseStBulk.BORSA_DI_STUDIO) ||
                                rapportoBulk.getCd_tipo_rapporto().equals(VIncarichiAssRicBorseStBulk.ASSEGNI_DI_RICERCA)){
                            logger.error("Invio Dati ACE: Codice fiscale {} non trovato. Per il codice fiscale: {} non è stata trovata la persona in ACE", anagraficoBulk.getCodice_fiscale(), anagraficoBulk.getCodice_fiscale());
                            break;
                        }
                    }

                } else {
                    PersonaWebDto personaWebDto = aceService.personaById(new Integer(personaId.get()));
                    try {
                        Map<String, Object> params = new HashMap<>();
                        params.put("persona", personaId.get());
                        params.put("tipoAppartenenza", TipoAppartenenza.AFFERENZA_UO);
                        List<PersonaEntitaOrganizzativaWebDto> personeEO;
                        personeEO = aceService.personaEntitaOrganizzativaFind(params)
                                .stream().sorted(Comparator.comparing(PersonaEntitaOrganizzativaWebDto::getInizioValidita)).collect(Collectors.toList());
                        if (personeEO == null || personeEO.isEmpty()){
                            params = new HashMap<>();
                            params.put("persona", personaId.get());
                            params.put("tipoAppartenenza", TipoAppartenenza.SEDE);
                            personeEO = aceService.personaEntitaOrganizzativaFind(params)
                                    .stream().sorted(Comparator.comparing(PersonaEntitaOrganizzativaWebDto::getInizioValidita)).collect(Collectors.toList());
                        }
                        if (personeEO == null || personeEO.isEmpty()){
                            String error = "Per la persona: "  + personaId.get() +" Anagrafico: "+anagraficoBulk.getCd_anag()+" non è stata trovata l'appartenenza in ACE";
                            logger.error(error);
                        } else {
                            PersonaEntitaOrganizzativaWebDto personaEO = personeEO.stream().reduce((first, second) -> second).get();

                            if (rapportiValidi.size() > 0){
                                List<RapportoBulk> rapportiOrdinati = (List<RapportoBulk>) rapportiValidi.stream().sorted(Comparator.comparing(RapportoBulk::getDt_ini_validita)).collect(Collectors.toList());
                                LocalDate dataFinePrecedente = null;
                                RapportoBulk rapportoPrec = null;
                                List<RapportoBulk> rapportiCongruenti = new LinkedList<>();
                                for (RapportoBulk rapportoBulk : rapportiOrdinati){
                                    if (rapportoPrec == null){
                                        rapportoPrec = rapportoBulk;
                                        rapportiCongruenti.add(rapportoBulk);
                                    } else {
                                        if (rapportoPrec.getDt_fin_validita().compareTo(rapportoBulk.getDt_ini_validita()) < 0){
                                            rapportiCongruenti.add(rapportoBulk);
                                        } else {
                                            rapportiCongruenti = new LinkedList<>();
                                            rapportiCongruenti.add(rapportoBulk);
                                        }
                                        rapportoPrec = rapportoBulk;
                                    }
                                }
                                if (rapportiCongruenti.size() > 0){
                                    RapportoBulk ultimoRapportoValido = rapportiCongruenti.stream().reduce((first, second) -> second).get();
                                    TipoContratto tipoContratto = Optional.ofNullable(ultimoRapportoValido.getCd_tipo_rapporto())
                                            .filter(s -> RapportoBulk.TIPOCONTRATTO_ACE.containsKey(ultimoRapportoValido.getCd_tipo_rapporto()))
                                            .map(s -> RapportoBulk.TIPOCONTRATTO_ACE.get(ultimoRapportoValido.getCd_tipo_rapporto()))
                                            .orElse(null);
                                    if (ultimoRapportoValido.getCd_tipo_rapporto().equals("ASS") && !isExDipendente){
                                        tipoContratto = TipoContratto.ASSEGNISTA;
                                    }
                                    if (tipoContratto != null && (personaWebDto.getTipoContratto() == null || tipoContratto.compareTo(personaWebDto.getTipoContratto()) != 0)){
                                        personaWebDto.setTipoContratto(tipoContratto);
                                        personaWebDto.setDataCessazione(null);
                                        personaWebDto.setDataPrevistaCessazione(null);
                                        personaWebDto.setLivello(null);
                                        personaWebDto.setProfilo(null);
                                        aceService.updatePersona(personaWebDto);
                                    }
                                    boolean primoGiro = true;
                                    for (RapportoBulk rapportoBulk : rapportiCongruenti){
                                        if (primoGiro){
                                            LocalDate dataFineRapporto = rapportoBulk.getDt_fin_validita().toLocalDateTime().toLocalDate();
//                                            if (personaEO.getFineValidita() == null || !dataFineRapporto.isEqual(personaEO.getFineValidita())){
                                                aggiornaPersonaEO(userContext.getUser(), personaEO.getInizioValidita(), rapportoBulk.getDt_fin_validita().toLocalDateTime().toLocalDate(), personaEO);
//                                            }
                                            primoGiro = false;
                                        } else {
                                            PersonaEntitaOrganizzativaDto personaEntitaOrganizzativaDto = new PersonaEntitaOrganizzativaDto();
                                            personaEntitaOrganizzativaDto.setPersona(Integer.valueOf(personaId.get()));
                                            personaEntitaOrganizzativaDto.setTipoAppartenenza(TipoAppartenenza.AFFERENZA_UO);
                                            personaEntitaOrganizzativaDto.setEntitaOrganizzativa(personaEO.getEntitaOrganizzativa().getId());
                                            personaEntitaOrganizzativaDto.setInizioValidita(
                                                    rapportoBulk.getDt_ini_validita().toLocalDateTime().toLocalDate()
                                            );
                                            personaEntitaOrganizzativaDto.setFineValidita(
                                                    rapportoBulk.getDt_fin_validita().toLocalDateTime().toLocalDate()
                                            );
                                            logger.info(personaEntitaOrganizzativaDto.toString());
                                            final PersonaEntitaOrganizzativaWebDto personaEntitaOrganizzativaWebDto =
                                                    aceService.savePersonaEntitaOrganizzativa(personaEntitaOrganizzativaDto);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (FeignException _ex) {
                        String error = "Per la persona con id: "  + personaId.get() +" è stato riscontrato un errore durante l'aggiornamento dell'appartenenza in ACE: "+_ex.getMessage();
                        logger.error(error);
                        SendMail.sendErrorMail("Invio Dati ACE: Eccezione durante l'aggiornamento delle appartenenze per la persona con ID "  + personaId.get() , error);
                    }
                }
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
    private void aggiornaPersonaEO(String utente, LocalDate dtIniValidita, LocalDate dtFinValidita, PersonaEntitaOrganizzativaWebDto personaEOWebDto) {
        PersonaEntitaOrganizzativaDto personaEntitaOrganizzativaDto = new PersonaEntitaOrganizzativaDto();
        personaEntitaOrganizzativaDto.setId(personaEOWebDto.getId());
        personaEntitaOrganizzativaDto.setNote(personaEOWebDto.getNote());
        personaEntitaOrganizzativaDto.setPermissions(personaEOWebDto.getPermissions());
        personaEntitaOrganizzativaDto.setUtenteUva(utente);
        personaEntitaOrganizzativaDto.setProvvedimento(personaEOWebDto.getProvvedimento());
        personaEntitaOrganizzativaDto.setPersona(personaEOWebDto.getPersona().getId());
        personaEntitaOrganizzativaDto.setTipoAppartenenza(TipoAppartenenza.AFFERENZA_UO);
        personaEntitaOrganizzativaDto.setEntitaOrganizzativa(personaEOWebDto.getEntitaOrganizzativa().getId());
        personaEntitaOrganizzativaDto.setInizioValidita(dtIniValidita);
        personaEntitaOrganizzativaDto.setFineValidita(dtFinValidita);
        logger.info(personaEntitaOrganizzativaDto.toString());

        aceService.updatePersonaEntitaOrganizzativa(personaEntitaOrganizzativaDto);
    }
    public java.sql.Timestamp getCurrentDate() {
        try {
            return it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
        } catch (javax.ejb.EJBException e) {
            throw new it.cnr.jada.DetailedRuntimeException(e);
        }
    }

    public SQLBuilder selectAnagrafico_esercizio_contoCreditoByClause(UserContext userContext, AnagraficoBulk anagrafico, ContoBulk conto, CompoundFindClause clause) throws ComponentException {
        SQLBuilder sql = getHome(userContext, conto).createSQLBuilder();
        sql.addClause(clause);
        sql.addSQLClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
        return sql;
    }

    public SQLBuilder selectAnagrafico_esercizio_contoDebitoByClause(UserContext userContext, AnagraficoBulk anagrafico, ContoBulk conto, CompoundFindClause clause) throws ComponentException {
        SQLBuilder sql = getHome(userContext, conto).createSQLBuilder();
        sql.addClause(clause);
        sql.addSQLClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
        return sql;
    }
}
