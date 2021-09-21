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

package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.config00.file.bulk.Gruppo_fileBulk;
import it.cnr.contab.config00.file.bulk.Gruppo_fileHome;
import it.cnr.contab.config00.file.bulk.Tipo_fileBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.incarichi00.bulk.*;
import it.cnr.contab.incarichi00.ejb.IncarichiEstrazioneFpComponentSession;
import it.cnr.contab.incarichi00.service.ContrattiService;
import it.cnr.contab.incarichi00.storage.StorageContrattiAspect;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.spring.storage.StorageObject;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class IncarichiRepertorioComponent extends CRUDComponent {
    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            oggettobulk = super.inizializzaBulkPerInserimento(usercontext, oggettobulk);

            if (oggettobulk instanceof Incarichi_repertorioBulk) {

                Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) oggettobulk;

                incarico.setEsercizio(CNRUserContext.getEsercizio(usercontext));
                incarico.setStato(Incarichi_repertorioBulk.STATO_PROVVISORIO);
                incarico.setFl_pubblica_contratto(Boolean.FALSE);

                if (incarico.getIncarichi_procedura() == null || incarico.getIncarichi_procedura().getPg_procedura() == null) {
                    incarico.setCds((CdsBulk) getHome(usercontext, CdsBulk.class).findByPrimaryKey(new CdsBulk(CNRUserContext.getCd_cds(usercontext))));
                    incarico.setUnita_organizzativa((Unita_organizzativaBulk) getHome(usercontext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext))));
                }

                TerzoBulk terzo = Utility.createTerzoComponentSession().cercaTerzoPerUnitaOrganizzativa(usercontext, incarico.getUnita_organizzativa());
                String indirizzo = "";

                if (terzo != null) {
                    if (terzo.getVia_sede() != null)
                        indirizzo = indirizzo + terzo.getVia_sede();
                    if (terzo.getNumero_civico_sede() != null)
                        indirizzo = indirizzo + ", " + terzo.getNumero_civico_sede();

//					incarico.setIndirizzo_unita_organizzativa(indirizzo);
                }

//				incarico.setTipiTrattamento(findTipiTrattamento(usercontext, incarico));
//				loadTipoTrattamento(usercontext, incarico);

                getHomeCache(usercontext).fetchAll(usercontext);
            }
            return oggettobulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            oggettobulk = super.inizializzaBulkPerModifica(usercontext, oggettobulk);
            if (oggettobulk instanceof Incarichi_repertorioBulk) {
                Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) oggettobulk;

                Incarichi_repertorioHome incHome = (Incarichi_repertorioHome) getHome(usercontext, Incarichi_repertorioBulk.class);
                incarico.setIncarichi_repertorio_annoColl(new BulkList(incHome.findIncarichi_repertorio_annoList(usercontext, incarico)));
                incarico.setArchivioAllegati(new BulkList(incHome.findArchivioAllegati(incarico)));
                incarico.setIncarichi_repertorio_varColl(new BulkList(incHome.findIncarichi_repertorio_varList(usercontext, incarico)));
                incarico.setIncarichi_repertorio_rappColl(new BulkList(incHome.findIncarichi_repertorio_rappList(usercontext, incarico)));
                incarico.setIncarichi_repertorio_rapp_detColl(new BulkList(incHome.findIncarichi_repertorio_rapp_detList(usercontext, incarico)));

                if (incarico != null && incarico.getPg_repertorio() != null) {
                    incarico.setAssociazioneUO(new it.cnr.jada.bulk.BulkList(incHome.findAssociazioneUO(incarico)));
                    if (UtenteBulk.isSuperUtenteFunzioniIncarichi(usercontext))
                        incarico.setAssociazioneUODisponibili(new BulkList(incHome.findAssociazioneUODisponibili(incarico)));
                }

                completaIncarico(usercontext, incarico);

                CompensoHome cHome = (CompensoHome) getHome(usercontext, CompensoBulk.class);

                for (Iterator i = incarico.getIncarichi_repertorio_annoColl().iterator(); i.hasNext(); ) {
                    Incarichi_repertorio_annoBulk increpanno = (Incarichi_repertorio_annoBulk) i.next();
                    increpanno.setCompensiColl(new BulkList(cHome.findCompensoIncaricoList(usercontext, increpanno)));
                }

                getHomeCache(usercontext).fetchAll(usercontext);
            }
            return oggettobulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            oggettobulk = super.inizializzaBulkPerRicerca(usercontext, oggettobulk);
            if (oggettobulk instanceof Incarichi_repertorioBulk) {
                Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) oggettobulk;
                incarico.setEsercizio(CNRUserContext.getEsercizio(usercontext));
            }
            return oggettobulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    protected Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
        boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0;
        boolean isUoSac = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC) == 0;

        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
        //sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
        if (!isUoEnte)
            sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
        if (isUoSac)
            sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
        sql.addOrderBy("pg_repertorio");
        return sql;
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

    /**
     * Viene richiesta la lista dei Tipi di rapporto associati ad un Terzo (Contraente)
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Terzo NON selezionato
     * Pre: Non è stato selezionato un Terzo per l'incarico
     * Post: Non vengono caricati i Tipi di rapporto
     * <p>
     * Nome: Terzo selezionato
     * Pre: E' stato selezionato un Terzo valido per l'incarico
     * Post: Viene restituita la lista dei Tipi di rapporto associati al Terzo
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    incarico l'OggettoBulk da completare
     * @return La lista dei Tipi di rapporto associati al terzo
     **/
    public java.util.Collection findTipiRapporto(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException {
        try {
            if (incarico.getTerzo() == null ||
                    incarico.getIncarichi_procedura() == null ||
                    incarico.getIncarichi_procedura().getTipo_incarico() == null ||
                    incarico.getIncarichi_procedura().getTipo_incarico().getCd_tipo_rapporto() == null)
                return null;

            it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome home = (it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome) getHome(userContext, it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk.class);
            List<OggettoBulk> list = new BulkList();
            Tipo_rapportoBulk tipoRapp = (Tipo_rapportoBulk) home.findTipoRapporto(incarico.getV_terzo(), incarico.getIncarichi_procedura().getTipo_incarico().getCd_tipo_rapporto(), incarico.getDt_inizio_validita(), incarico.getDt_fine_validita());
            if (tipoRapp != null)
                list.add(tipoRapp);
            return list;

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(incarico, ex);
        }
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectUnita_organizzativaByClause(UserContext userContext, Incarichi_repertorioBulk incarico, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
        sqlStruttura.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        sqlStruttura.addClause("AND", "cd_cds", SQLBuilder.EQUALS, incarico.getCd_cds());
        sqlStruttura.addClause("AND", "cd_tipo_livello", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_UO);
        sqlStruttura.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

        SQLBuilder sql = getHome(userContext, uo.getClass()).createSQLBuilder();
        sql.addSQLExistsClause("AND", sqlStruttura);
        sql.addClause(clauses);
        return sql;
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectV_terzoByClause(UserContext userContext, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk contraente, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            V_terzo_per_compensoHome home = (V_terzo_per_compensoHome) getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
            return home.selectVTerzo(Tipo_rapportoBulk.ALTRO, incarico.getV_terzo().getCd_terzo(), incarico.getDt_stipula(), incarico.getDt_stipula(), clauses);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Completamento dell'OggettoBulk <compenso> aggiornando i campi
     * relativi al terzo (nome, cognome, ragSoc, cf, pIva, comune, prov, reg)
     * e caricando da db i seguenti oggetti complessi
     * - modalita pagamento
     * - termini di pagamento
     * - tipo di rapporto
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Completamento del compenso
     * Pre: Viene richiesto il completamento del compenso
     * Post: Viene completato il compenso con i seguenti dati relativi al terzo:
     * - nome, cognome, ragSoc, cf, pIva, comune, prov, reg
     * - modalita pagamento
     * - termini di pagamento
     * - tipo di rapporto
     *
     * @return l'OggettoBulk completo
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    incarico l'OggettoBulk da completare
     **/
    private Incarichi_repertorioBulk completaTerzo(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException {

        if (incarico != null) {

            V_terzo_per_compensoBulk vTerzo = incarico.getV_terzo();
            incarico.setCd_terzo(vTerzo.getCd_terzo());

            incarico.setTipiRapporto(findTipiRapporto(userContext, incarico));
        }
        return incarico;
    }

    /**
     * Completamento dell'OggettoBulk <incarico> aggiornando i campi
     * relativi al terzo selezionato <contraente>
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Completamento dell'incarico
     * Pre: Viene richiesto il completamento dell'incarico con le informazioni del contratto
     * da attivare
     * Post: Viene restituito il compenso completo di tutti i dati
     * relativi al terzo selezionato
     *
     * @return l'OggettoBulk completo
     * <p>
     * Metodo privato chiamato:
     * completaTerzo(UserContext userContext, Incarichi_repertorioBulk incarico);
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    terzo l'OggettoBulk da completare
     **/
    public Incarichi_repertorioBulk completaTerzo(UserContext userContext, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk terzo) throws ComponentException {

        if (incarico != null) {
            incarico.setV_terzo(terzo);
            incarico.setCd_terzo(terzo.getCd_terzo());
            incarico.setTipiRapporto(findTipiRapporto(userContext, incarico));
        }
        return incarico;

    }

    /**
     * Viene caricato da db il TERZO associato all'incarico
     * valido in Data Registrazione
     * e con tipi rapporto validi in Data Competenza Coge
     **/
    private V_terzo_per_compensoBulk loadVTerzo(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException {

        try {

            V_terzo_per_compensoHome home = (V_terzo_per_compensoHome) getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
            return home.loadVTerzo(userContext, Tipo_rapportoBulk.ALTRO, incarico.getCd_terzo(), incarico.getDt_inizio_validita(), incarico.getDt_inizio_validita());

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Viene richiesto il completamento dell'OggettoBulk <compenso>
     * caricando da db i seguenti oggetti complessi
     * - terzo
     * - tipo trattamento
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Completamento dell'incarico
     * Pre: Viene richiesto il completamento dell'incarico
     * Post: Viene completato il compenso con i seguenti dati:
     * - terzo
     * - tipo trattamento
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    incarico l'OggettoBulk da completare
     * <p>
     * Metodi privati chiamati:
     * completaTerzo(UserContext userContext, Incarichi_repertorioBulk incarico);
     * loadTipoTrattamento(UserContext userContext, Incarichi_repertorioBulk incarico);
     **/
    private void completaIncarico(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException {
        try {
            if (incarico.getCd_terzo() != null) {
                incarico.setV_terzo(loadVTerzo(userContext, incarico));
                completaTerzo(userContext, incarico);
            }
            getHomeCache(userContext).fetchAll(userContext);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    private void validaIncaricoRepertorio(UserContext aUC, Incarichi_repertorioBulk incarico) throws ComponentException {
        int contaContratti = 0;
        for (Iterator i = incarico.getArchivioAllegati().iterator(); i.hasNext(); ) {
            Incarichi_repertorio_archivioBulk archivio = (Incarichi_repertorio_archivioBulk) i.next();
            if (archivio.isContratto())
                contaContratti = contaContratti + 1;
        }

        if (contaContratti > 1)
            throw handleException(new ApplicationException("All'incarico può essere allegato al massimo un \"" + Incarichi_repertorio_archivioBulk.tipo_archivioKeys.get(Incarichi_repertorio_archivioBulk.TIPO_CONTRATTO).toString() + "\"!"));

        if (incarico.getIm_complessivo_ripartito().compareTo(incarico.getImporto_complessivo()) != 0)
            throw handleException(new ApplicationException("Il totale ripartito per anno non coincide con la spesa complessiva indicata in testata!"));

        if (incarico.getTerzo() != null && incarico.getTerzo().getCd_terzo() != null)
            if (incarico.getDt_inizio_validita() != null && incarico.getDt_fine_validita() != null) {
                if (incarico.getTipo_rapporto() == null)
                    throw handleException(new ApplicationException("Completare le informazioni relative al tipo rapporto da applicare al terzo selezionato (cod: " + incarico.getTerzo().getCd_terzo() + ")!"));
                if (incarico.getTi_istituz_commerc() == null)
                    throw handleException(new ApplicationException("Completare le informazioni relative al tipo compenso da applicare al terzo selezionato (cod: " + incarico.getTerzo().getCd_terzo() + ")!"));
            }
    }

    public BigDecimal calcolaUtilizzato(UserContext userContext, Incarichi_repertorio_annoBulk incarico_anno) throws ComponentException {
        BigDecimal utilizzato = new BigDecimal(0);
        CompensoHome cHome = (CompensoHome) getHome(userContext, CompensoBulk.class);
        Iterator listacomp_incarico;
        try {
            listacomp_incarico = cHome.findCompensoIncaricoList(userContext, incarico_anno).iterator();

            for (Iterator x = listacomp_incarico; x.hasNext(); ) {
                CompensoBulk dett = (CompensoBulk) x.next();
                getHomeCache(userContext).fetchAll(userContext);
                utilizzato = utilizzato.add(dett.getIm_totale_compenso());
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(e);
        }
        return utilizzato;
    }

    private void archiviaAllegati(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException {
        Incarichi_repertorio_archivioHome archiveHome = (Incarichi_repertorio_archivioHome) getHome(userContext, Incarichi_repertorio_archivioBulk.class);
        List listFileAllegabili = null;
        if (incarico.getIncarichi_procedura().getProcedura_amministrativa() != null && incarico.getIncarichi_procedura().getProcedura_amministrativa().getCd_gruppo_file() != null) {
            try {
                listFileAllegabili = ((Gruppo_fileHome) getHome(userContext, Gruppo_fileBulk.class)).findTipo_file_associati(incarico.getIncarichi_procedura().getProcedura_amministrativa().getGruppo_file());
            } catch (PersistencyException e) {
                throw new ComponentException(e);
            } catch (IntrospectionException e) {
                throw new ComponentException(e);
            }
        }

        for (Iterator i = incarico.getArchivioAllegati().iterator(); i.hasNext(); ) {
            Incarichi_repertorio_archivioBulk allegato = (Incarichi_repertorio_archivioBulk) i.next();
            if (!(allegato.getFile() == null || allegato.getFile().getName().equals(""))) {
                String nomeFile = allegato.getFile().getName();
                String estensioneFile = nomeFile.substring(nomeFile.lastIndexOf(".") + 1);
                if (listFileAllegabili != null && !listFileAllegabili.isEmpty()) {
                    String estensioniValide = null;

                    boolean valido = false;
                    for (Iterator j = listFileAllegabili.iterator(); j.hasNext(); ) {
                        Tipo_fileBulk tipo_file = (Tipo_fileBulk) j.next();
                        if (estensioneFile.equalsIgnoreCase(tipo_file.getEstensione_file())) {
                            valido = true;
                            break;
                        }
                        estensioniValide = estensioniValide == null ? "\"" + tipo_file.getEstensione_file() + "\"" : estensioniValide.concat(", \"" + tipo_file.getEstensione_file() + "\"");
                    }
                    if (!valido)
                        throw new ApplicationException("File non valido!\nI formati dei file consentiti sono " + estensioniValide + ".");
                }
                if (!estensioneFile.equalsIgnoreCase("pdf")) {
                    if (allegato.isCurriculumVincitore())
                        throw new ApplicationException("File non valido!\nIl formato del file consentito per il Curriculum Vitae è il pdf.");
                    if (allegato.isConflittoInteressi())
                        throw new ApplicationException("File non valido!\nIl formato del file consentito per l'Attestazione Insussistenza Conflitto Interessi è il pdf.");
                    if (allegato.isAggiornamentoCurriculumVincitore())
                        throw new ApplicationException("File non valido!\nIl formato del file consentito per l'aggiornamento del Curriculum Vitae è il pdf.");
                }
                allegato.setToBeUpdated();
            }
        }
    }

    public void eliminaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        try {
            Incarichi_repertorioBulk incarico;
            if (bulk instanceof Incarichi_repertorioBulk) {
                incarico = (Incarichi_repertorioBulk) getHome(aUC, Incarichi_repertorioBulk.class).findByPrimaryKey(bulk);
                if (incarico == null)
                    throw new ApplicationException("L'incarico e' stato cancellato");

                incarico = (Incarichi_repertorioBulk) inizializzaBulkPerModifica(aUC, incarico);

                if (incarico.getStato().equals(Incarichi_repertorioBulk.STATO_PROVVISORIO)) {
                    incarico.setToBeDeleted();

                    for (Iterator i = incarico.getIncarichi_repertorio_annoColl().iterator(); i.hasNext(); )
                        ((Incarichi_repertorio_annoBulk) i.next()).setToBeDeleted();

                    for (Iterator i = incarico.getArchivioAllegati().iterator(); i.hasNext(); )
                        ((Incarichi_repertorio_archivioBulk) i.next()).setToBeDeleted();

                    makeBulkPersistent(aUC, incarico);
                } else if (incarico.getStato().equals(Incarichi_repertorioBulk.STATO_DEFINITIVO)) {
                    if (incarico.getImporto_utilizzato().compareTo(Utility.ZERO) == 1)
                        incarico = (Incarichi_repertorioBulk) chiudiIncaricoPubblicato(aUC, incarico);
                    else
                        incarico = (Incarichi_repertorioBulk) stornaIncaricoPubblicato(aUC, incarico);
                } else
                    throw handleException(new it.cnr.jada.comp.ApplicationException("Lo stato dell'incarico non ne consente la cancellazione/storno"));
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(bulk, e);
        }
    }

    public OggettoBulk stornaIncaricoPubblicato(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        try {
            if (bulk instanceof Incarichi_repertorioBulk) {
                Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) getHome(aUC, Incarichi_repertorioBulk.class).findByPrimaryKey(bulk);
                if (incarico == null)
                    throw new ApplicationException("L'incarico e' stato cancellato");

                incarico = (Incarichi_repertorioBulk) inizializzaBulkPerModifica(aUC, incarico);
		
				/* Qui bisogna verificare se gli importi associati all'incarico non siano già stati, anche in parte, associati ad impegni/compensi
			  	if (esisteimpegno o incarico)
		          	throw new ApplicationException("L'incarico risulta già utilizzato su impegni/compensi. Impossibile eliminarlo.");
				 */
				/* Qui bisogna ritornare le dipsonibilità sulla tabella degli 
		      	for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
			        if ( ((Obbligazione_scadenzarioBulk)i.next()).getIm_associato_doc_amm().compareTo( new BigDecimal(0)) > 0 )
		            throw new ApplicationException("Impossibile stornare impegni collegati a spese del fondo economale o a documenti amministrativi");
				*/
                incarico.setStato(Incarichi_repertorioBulk.STATO_ANNULLATO);
                incarico.setDt_cancellazione(DateServices.getDt_valida(aUC));
                incarico.setToBeUpdated();

//				aggiornaImportiRepertorioIncarico(aUC, incarico, CANCELLAZIONE);

                makeBulkPersistent(aUC, incarico);

                return incarico;
            }
            return bulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectIncarichi_proceduraByClause(UserContext userContext, Incarichi_repertorioBulk incarico, Incarichi_proceduraBulk procedura, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException {
        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(userContext, Incarichi_proceduraBulk.class).createSQLBuilder();

        sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, incarico.getCd_cds());
        sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, incarico.getCd_unita_organizzativa());
        //Escludo quelle scadute
        sql.addClause(FindClause.AND, "data_scadenza", SQLBuilder.GREATER_EQUALS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
/*

		it.cnr.jada.persistency.sql.SQLBuilder sqlExist = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
		sqlExist.addSQLJoin("INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA","INCARICHI_PROCEDURA.ESERCIZIO");
		sqlExist.addSQLJoin("INCARICHI_REPERTORIO.PG_PROCEDURA","INCARICHI_PROCEDURA.PG_PROCEDURA");
		
		sqlExist.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_repertorioBulk.STATO_ANNULLATO);
		sqlExist.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_repertorioBulk.STATO_RESPINTO);
		
		//Escludo quelle scadute
		sqlExist.openParenthesis(FindClause.AND_NOT);
		sqlExist.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS,Incarichi_repertorioBulk.STATO_PUBBLICATA);
		sqlExist.addClause(FindClause.AND, "dt_scadenza", SQLBuilder.LESS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sqlExist.closeParenthesis();

		if (incarico.getEsercizio() != null && incarico.getPg_repertorio() != null) {
			sqlExist.openParenthesis(FindClause.AND);
			sqlExist.addClause(FindClause.OR,"esercizio",SQLBuilder.NOT_EQUALS, incarico.getEsercizio());
			sqlExist.addClause(FindClause.OR,"pg_repertorio",SQLBuilder.NOT_EQUALS, incarico.getPg_repertorio());
			sqlExist.closeParenthesis();
		}

//		sql.executeCountQuery(connection)(connection)
		
		sql.addSQLNotExistsClause(FindClause.AND, sqlExist);
*/
        sql.addClause(clauses);
        return sql;
    }

    public OggettoBulk salvaDefinitivo(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            if (oggettobulk instanceof Incarichi_repertorioBulk) {
                Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) oggettobulk;

                boolean isDaInviareCorteConti = incarico.isIncaricoProvvisorio() && incarico.getFl_inviato_corte_conti() &&
                        incarico.getEsito_corte_conti() == null;

                if (incarico.getNr_provv() == null || incarico.getDt_provv() == null) {
                    if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                        throw new ApplicationException("Inserire numero e data protocollo del provvedimento di nomina del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\".");
                    else
                        throw new ApplicationException("Inserire numero e data protocollo del provvedimento di nomina del terzo.");
                }

                if (incarico.getDt_stipula() == null)
                    throw handleException(new ApplicationException("Inserire la data di stipula del contratto!"));

                if (incarico.getFl_inviato_corte_conti()) {
                    if (incarico.getTerzo() == null || incarico.getTerzo().getCd_terzo() == null)
                        throw handleException(new ApplicationException("Completare le informazioni relative al terzo!"));
                }
                if (!isDaInviareCorteConti) {
                    if (incarico.isIncaricoInviatoCorteConti() &&
                            (incarico.getEsito_corte_conti() == null || !incarico.getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_LEGITTIMO)))
                        throw handleException(new ApplicationException("E' possibile rendere definitivi solo i contratti con esito \"LEGITTIMO\" del controllo della Corte dei Conti!"));
                    if (incarico.getFl_inviato_corte_conti() && incarico.getDt_invio_corte_conti() == null)
                        throw handleException(new ApplicationException("Inserire la data di ricezione degli atti inviati alla Corte dei Conti!"));
                    if (incarico.getDt_inizio_validita() == null || incarico.getDt_fine_validita() == null)
                        throw handleException(new ApplicationException("Completare le informazioni relative alla data di inizio e fine validit\340 del contratto!"));
                    if (incarico.getTerzo() == null || incarico.getTerzo().getCd_terzo() == null ||
                            incarico.getTipo_trattamento() == null || incarico.getTipo_rapporto() == null ||
                            incarico.getTi_istituz_commerc() == null)
                        throw handleException(new ApplicationException("Completare le informazioni relative al terzo e al trattamento da applicare!"));
                }

                Incarichi_proceduraHome proceduraHome = (Incarichi_proceduraHome) getHome(usercontext, Incarichi_proceduraBulk.class);
                Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) proceduraHome.findByPrimaryKey(incarico.getIncarichi_procedura());

                getHomeCache(usercontext).fetchAll(usercontext);

                Incarichi_parametriBulk parametri = Utility.createIncarichiProceduraComponentSession().getIncarichiParametri(usercontext, procedura);

                if (incarico.getDt_stipula() == null)
                    throw handleException(new ApplicationException("Non \350 possibile effettuare l'operazione perchè non risulta inserita la data di stipula del contratto."));

                if (!UtenteBulk.isSuperUtenteFunzioniIncarichi(usercontext)) {
                    if (parametri == null || parametri.getLimite_dt_stipula() == null) {
                        if (!incarico.getFl_inviato_corte_conti() && DateUtils.daysBetweenDates(incarico.getDt_stipula(), EJBCommonServices.getServerDate()) > 5)
                            throw handleException(new ApplicationException("Non \350 possibile effettuare l'operazione perch\350 dalla data di stipula del contratto risultano trascorsi piu' di 5 giorni."));
                        else if (incarico.getFl_inviato_corte_conti() && incarico.getDt_inizio_validita() != null && DateUtils.daysBetweenDates(incarico.getDt_inizio_validita(), EJBCommonServices.getServerDate()) > 5)
                            throw handleException(new ApplicationException("Non \350 possibile effettuare l'operazione perch\350 dalla data di inizio validit\340 del contratto risultano trascorsi piu' di 5 giorni."));
                    } else if (parametri != null && parametri.getLimite_dt_stipula() != null && parametri.getLimite_dt_stipula().equals("Y")) {
                        Integer limite = new Integer(0);
                        if (parametri.getGiorni_limite_dt_stipula() != null)
                            limite = parametri.getGiorni_limite_dt_stipula();
                        if (!incarico.getFl_inviato_corte_conti() && DateUtils.daysBetweenDates(incarico.getDt_stipula(), EJBCommonServices.getServerDate()) > limite.intValue())
                            throw handleException(new ApplicationException("Non \350 possibile effettuare l'operazione perch\350 dalla data di stipula del contratto risultano trascorsi piu' di " + limite.toString() + " giorni."));
                        else if (incarico.getFl_inviato_corte_conti() && incarico.getDt_inizio_validita() != null && DateUtils.daysBetweenDates(incarico.getDt_inizio_validita(), EJBCommonServices.getServerDate()) > limite.intValue())
                            throw handleException(new ApplicationException("Non \350 possibile effettuare l'operazione perch\350 dalla data di inizio validit\340 del contratto risultano trascorsi piu' di " + limite.toString() + " giorni."));
                    }
                }

                if (incarico.getContratto() == null) {
                    if (parametri == null || parametri.getAllega_contratto() == null || parametri.getAllega_contratto().equals("Y")) {
                        if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                            //Istanzio la classe per riempire tipo_archivioKeys
                            new Incarichi_procedura_archivioBulk();
                        }

                        if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONTRATTO).toString() + "\".");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONTRATTO).toString() + "\".");
                    }
                }

                if (incarico.getCurriculumVincitore() == null) {
                    if (parametri != null && parametri.getAllega_curriculum_vitae() != null && parametri.getAllega_curriculum_vitae().equals("Y")) {
                        if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                            //Istanzio la classe per riempire tipo_archivioKeys
                            new Incarichi_procedura_archivioBulk();
                        }

                        if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CURRICULUM_VINCITORE).toString() + "\".");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CURRICULUM_VINCITORE).toString() + "\".");
                    }
                }

                if (incarico.getConflittoInteressi() == null) {
                    if (parametri != null && parametri.getAllega_conflitto_interesse() != null && parametri.getAllega_conflitto_interesse().equals("Y")) {
                        if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                            //Istanzio la classe per riempire tipo_archivioKeys
                            new Incarichi_procedura_archivioBulk();
                        }

                        if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONFLITTO_INTERESSI).toString() + "\".");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONFLITTO_INTERESSI).toString() + "\".");
                    }
                }

                if (incarico.getAttestazioneDirettore() == null) {
                    if (parametri != null && parametri.getAllega_attestazione_direttore() != null && parametri.getAllega_attestazione_direttore().equals("Y")) {
                        if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                            //Istanzio la classe per riempire tipo_archivioKeys
                            new Incarichi_procedura_archivioBulk();
                        }

                        if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTESTAZIONE_DIRETTORE).toString() + "\".");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTESTAZIONE_DIRETTORE).toString() + "\".");
                    }
                }

                if (incarico.getDecretoDiNomina() == null) {
                    if (parametri != null && parametri.getAllega_decreto_nomina() != null && parametri.getAllega_decreto_nomina().equals("Y")) {
                        if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                            //Istanzio la classe per riempire tipo_archivioKeys
                            new Incarichi_procedura_archivioBulk();
                        }

                        if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECRETO_DI_NOMINA).toString() + "\".");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECRETO_DI_NOMINA).toString() + "\".");
                    }
                }

                if (incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti() != null && incarico.getAttoEsitoControllo() == null) {
                    if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                        //Istanzio la classe per riempire tipo_archivioKeys
                        new Incarichi_procedura_archivioBulk();
                    }

                    if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                        throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString() + "\".");
                    else
                        throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString() + "\".");
                }

                procedura.setArchivioAllegati(new BulkList(proceduraHome.findArchivioAllegati(procedura)));

                if (procedura.getDecisioneAContrattare() == null) {
                    if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                        //Istanzio la classe per riempire tipo_archivioKeys
                        new Incarichi_procedura_archivioBulk();
                    }
                    if (parametri == null || parametri.getAllega_decisione_ctr() == null || parametri.getAllega_decisione_ctr().equals("Y"))
                        throw new it.cnr.jada.comp.ApplicationException("Allegare alla \"Procedura di conferimento incarico\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECISIONE_A_CONTRATTARE).toString() + "\".");
                }

                if (procedura.isDichiarazioneContraenteRequired()) {
                    //Controllo che sia stata inserita la dichiarazione del contraente....almeno quella del primo anno di validità dell'incarico
                    boolean existRapportoAnnoStipula = false;
                    GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
                    data_da.setTime(incarico.getDt_stipula());
                    if (incarico.getIncarichi_repertorio_rappColl() != null && !incarico.getIncarichi_repertorio_rappColl().isEmpty()) {
                        for (Iterator i = incarico.getIncarichi_repertorio_rappColl().iterator(); i.hasNext(); ) {
                            Incarichi_repertorio_rappBulk rapporto = (Incarichi_repertorio_rappBulk) i.next();
                            if (!rapporto.isAnnullato() && rapporto.getAnno_competenza().equals(data_da.get(java.util.Calendar.YEAR)))
                                existRapportoAnnoStipula = true;
                        }
                    }
                    if (!existRapportoAnnoStipula) {
                        if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                            throw new it.cnr.jada.comp.ApplicationException("Inserire la dichiarazione dell'anno " + data_da.get(java.util.Calendar.YEAR) + ", anno di stipula del contratto, resa dal terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" sui rapporti stipulati con altri enti.");
                        else
                            throw new it.cnr.jada.comp.ApplicationException("Inserire la dichiarazione dell'anno " + data_da.get(java.util.Calendar.YEAR) + ", anno di stipula del contratto, resa dal terzo sui rapporti stipulati con altri enti.");
                    }
                }

                if (procedura.getTipo_attivita_fp() == null && procedura.isProceduraForIncarichi())
                    throw new it.cnr.jada.comp.ApplicationException("Indicare, nella sezione 'Dati Perla', l'attività economica dell'incarico necessaria per la comunicazione dei dati al sistema informativo PERLA del Ministero della Funzione Pubblica.");
                if (procedura.getFl_applicazione_norma() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Indicare, nella sezione '" +
                            (procedura.isProceduraForIncarichi() ? "Dati Perla" : "Altri Dati") + "', se l'incarico è stato conferito in applicazione di una specifica norma.");
                if (procedura.isApplicazioneNormaAttiva()) {
                    if (procedura.getCd_tipo_norma_perla() == null)
                        throw new it.cnr.jada.comp.ApplicationException("Indicare, nella sezione '" +
                                (procedura.isProceduraForIncarichi() ? "Dati Perla" : "Altri Dati") + "', il tipo di norma in base al quale è stato conferito l'incarico.");
                    else if (procedura.getCd_tipo_norma_perla().equals("999") && procedura.getDs_libera_norma_perla() == null)
                        throw new it.cnr.jada.comp.ApplicationException("Indicare, nella sezione '" +
                                (procedura.isProceduraForIncarichi() ? "Dati Perla" : "Altri Dati") + "', una breve descrizione della norma in base al quale è stato conferito l'incarico.");
                }
                if (procedura.getProcedura_amministrativa_beneficiario() == null || procedura.getProcedura_amministrativa_beneficiario().getCd_proc_amm() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Indicare, nella sezione '" +
                            (procedura.isProceduraForIncarichi() ? "Dati Perla" : "Altri Dati") + "', la modalità di individuazione del beneficiario.");

                incarico.setStato(isDaInviareCorteConti ? Incarichi_repertorioBulk.STATO_INVIATO : Incarichi_repertorioBulk.STATO_DEFINITIVO);
                incarico.setToBeUpdated();
                updateBulk(usercontext, incarico);

                salvaDefinitivoCMIS(usercontext, incarico);

                procedura.setIncarichi_repertorioColl(new BulkList(proceduraHome.findIncarichi_repertorioList(usercontext, procedura)));
                Integer contadef = 0, containv = 0;
                for (Iterator i = procedura.getIncarichi_repertorioColl().iterator(); i.hasNext(); ) {
                    Incarichi_repertorioBulk incaricoColl = (Incarichi_repertorioBulk) i.next();
                    if (incaricoColl.isIncaricoDefinitivo() || incaricoColl.isIncaricoChiuso())
                        contadef = contadef + 1;
                    if (incaricoColl.isIncaricoInviatoCorteConti())
                        containv = containv + 1;
                }
                if (procedura.getNr_contratti().equals(contadef)) {
                    if (!procedura.isProceduraDefinitiva()) {
                        procedura.setStato(Incarichi_proceduraBulk.STATO_DEFINITIVO);
                        procedura.setToBeUpdated();
                        updateBulk(usercontext, procedura);
                        Utility.createIncarichiProceduraComponentSession().salvaDefinitivoCMIS(usercontext, procedura);
                    }
                } else if (procedura.getNr_contratti().equals(contadef + containv)) {
                    if (!procedura.isProceduraInviataCorteConti()) {
                        procedura.setStato(Incarichi_proceduraBulk.STATO_INVIATO);
                        procedura.setToBeUpdated();
                        updateBulk(usercontext, procedura);
                    }
                }
                this.comunicaPerla(usercontext, incarico);
            } else {
                updateBulk(usercontext, oggettobulk);
            }
            return oggettobulk;
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
//		if (oggettobulk instanceof Incarichi_repertorioBulk)
//			aggiornaImportiRepertorioIncarico(usercontext, (Incarichi_repertorioBulk)oggettobulk, INSERIMENTO);
        oggettobulk = super.eseguiCreaConBulk(usercontext, oggettobulk);
        if (oggettobulk instanceof Incarichi_repertorioBulk)
            archiviaAllegati(usercontext, (Incarichi_repertorioBulk) oggettobulk);
        return oggettobulk;
    }

    protected OggettoBulk eseguiModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
        oggettobulk = super.eseguiModificaConBulk(usercontext, oggettobulk);
        if (oggettobulk instanceof Incarichi_repertorioBulk)
            archiviaAllegati(usercontext, (Incarichi_repertorioBulk) oggettobulk);
        return oggettobulk;
    }

    protected void validaCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        super.validaCreaConBulk(usercontext, oggettobulk);
        if (oggettobulk instanceof Incarichi_repertorioBulk) {
            Incarichi_repertorioBulk repertorio = (Incarichi_repertorioBulk) oggettobulk;
            if (repertorio.getIncarichi_procedura().getFl_meramente_occasionale().booleanValue())
                repertorio.setFl_pubblica_contratto(Boolean.FALSE);
            else
                repertorio.setFl_pubblica_contratto(repertorio.getIncarichi_procedura().getTipo_attivita().getFl_pubblica_contratto());
        }
    }

    protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        super.validaCreaModificaConBulk(usercontext, oggettobulk);
        if (oggettobulk instanceof Incarichi_repertorioBulk)
            validaIncaricoRepertorio(usercontext, (Incarichi_repertorioBulk) oggettobulk);
    }

    public OggettoBulk annullaDefinitivo(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        try {
            if (oggettobulk instanceof Incarichi_repertorioBulk) {
                Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) oggettobulk;

                CompensoHome compensoHome = (CompensoHome) getHome(usercontext, CompensoBulk.class);
                if (!compensoHome.findCompensoIncaricoList(usercontext, incarico).isEmpty())
                    throw handleException(new ApplicationException("Operazione non possibile. L'incarico risulta essere già collegato a compensi!"));

                MinicarrieraHome minicarrieraHome = (MinicarrieraHome) getHome(usercontext, MinicarrieraBulk.class);
                if (!minicarrieraHome.findMinicarrieraIncaricoList(usercontext, incarico).isEmpty())
                    throw handleException(new ApplicationException("Operazione non possibile. L'incarico risulta essere già collegato a minicarriere!"));

                if (incarico.getIncarichi_procedura().getDt_scadenza() != null &&
                        incarico.getIncarichi_procedura().getDt_scadenza().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
                    throw handleException(new ApplicationException("Operazione non possibile. L'incarico risulterebbe essere scaduto e non sarebbe possibile modificare il contratto allegato!"));

                incarico.setStato(Incarichi_repertorioBulk.STATO_PROVVISORIO);
                incarico.setToBeUpdated();
                updateBulk(usercontext, incarico);

                annullaDefinitivoCMIS(usercontext, incarico);

                Incarichi_proceduraHome proceduraHome = (Incarichi_proceduraHome) getHome(usercontext, Incarichi_proceduraBulk.class);
                Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) proceduraHome.findByPrimaryKey(incarico.getIncarichi_procedura());
                if (procedura.isProceduraDefinitiva()) {
                    if (procedura.getDt_pubblicazione() == null)
                        procedura.setStato(Incarichi_proceduraBulk.STATO_PROVVISORIO);
                    else
                        procedura.setStato(Incarichi_proceduraBulk.STATO_PUBBLICATA);
                    procedura.setToBeUpdated();
                    updateBulk(usercontext, procedura);

                    Utility.createIncarichiProceduraComponentSession().annullaDefinitivoCMIS(usercontext, procedura);
                }
            } else {
                updateBulk(usercontext, oggettobulk);
            }
            return oggettobulk;
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    public OggettoBulk chiudiIncaricoPubblicato(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        try {
            if (bulk instanceof Incarichi_repertorioBulk) {
                Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) bulk;

                if (incarico.isIncaricoProvvisorio() && incarico.getEsito_corte_conti() != null &&
                        incarico.getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO)) {
                    incarico.setEsito_corte_conti(null);
                    salvaDefinitivo(aUC, bulk);
                    incarico.setEsito_corte_conti(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO);
                }

                boolean isEsitoControlloNegativo = incarico.isIncaricoInviatoCorteConti() && incarico.getEsito_corte_conti() != null &&
                        incarico.getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO);

                if (isEsitoControlloNegativo && incarico.getAttoEsitoControllo() == null) {
                    if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
                        //Istanzio la classe per riempire tipo_archivioKeys
                        new Incarichi_procedura_archivioBulk();
                    }

                    if (incarico.getV_terzo() != null && incarico.getV_terzo().getCognome() != null && incarico.getV_terzo().getNome() != null)
                        throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \"" + incarico.getV_terzo().getCognome() + " " + incarico.getV_terzo().getNome() + "\" un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString() + "\".");
                    else
                        throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \"" + Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString() + "\".");
                }

                BigDecimal totVariazione = BigDecimal.ZERO;
                for (Iterator i = incarico.getIncarichi_repertorio_annoColl().iterator(); i.hasNext(); ) {
                    Incarichi_repertorio_annoBulk dett = (Incarichi_repertorio_annoBulk) i.next();

                    if (dett.getImporto_complessivo().compareTo(dett.getImporto_utilizzato()) == 1) {
                        totVariazione = totVariazione.add(dett.getImporto_complessivo().subtract(dett.getImporto_utilizzato()).negate());
                        dett.setImporto_complessivo(dett.getImporto_utilizzato());
                        dett.setToBeUpdated();
                    }
                }
                if (totVariazione.compareTo(BigDecimal.ZERO) != 0) {
                    Incarichi_repertorio_varBulk variazione = new Incarichi_repertorio_varBulk();
                    incarico.addToIncarichi_repertorio_varColl(variazione);
                    variazione.setDt_variazione(DateServices.getDt_valida(aUC));
                    if (isEsitoControlloNegativo)
                        variazione.setDs_variazione("Variazione automatica generata in fase di annullamento incarico per esito negativo del controllo di legittima' da parte della Corte dei Conti.");
                    else
                        variazione.setDs_variazione("Variazione automatica generata in fase di chiusura incarico.");
                    variazione.setTipo_variazione(Incarichi_repertorio_varBulk.TIPO_VARIAZIONE_GENERICA);
                    variazione.setImporto_complessivo(totVariazione);
                    variazione.setStato(Incarichi_repertorio_varBulk.STATO_VALIDO);
                    variazione.setToBeCreated();
                }

                incarico.setStato(Incarichi_repertorioBulk.STATO_CHIUSO);
                incarico.setDt_cancellazione(DateServices.getDt_valida(aUC));
                incarico.setToBeUpdated();

                makeBulkPersistent(aUC, incarico);

                return incarico;
            }
            return bulk;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public boolean hasVariazioneIntegrazioneIncaricoProvvisoria(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        try {
            if (bulk instanceof Incarichi_repertorioBulk) {
                Incarichi_repertorioHome incHome = (Incarichi_repertorioHome) getHome(aUC, Incarichi_repertorioBulk.class);
                for (Iterator<Incarichi_repertorio_varBulk> i = incHome.findIncarichi_repertorio_varList(aUC, (Incarichi_repertorioBulk) bulk).iterator(); i.hasNext(); ) {
                    Incarichi_repertorio_varBulk incVar = i.next();
                    if (incVar.isVariazioneIntegrazioneIncaricoTransitorio() && incVar.isProvvisorio())
                        return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public void controllaCancellazioneAssociazioneUo(UserContext userContext, Ass_incarico_uoBulk ass_incarico_uo) throws ComponentException {
        Ass_incarico_uoHome home = (Ass_incarico_uoHome) getHome(userContext, Ass_incarico_uoBulk.class);
        try {
            if (home.existsDocContForAssIncaricoUo(ass_incarico_uo)) {
                throw new ApplicationException("Impossibile eliminare l'Unità organizzativa " + ass_incarico_uo.getCd_unita_organizzativa() +
                        " poichè esistono documenti contabili associati.");
            }
        } catch (IntrospectionException e) {
            throw new ComponentException(e);
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (SQLException e) {
            throw new ComponentException(e);
        }
    }

    public void salvaDefinitivoCMIS(UserContext userContext, Incarichi_repertorioBulk incarico_repertorio) throws ComponentException {
        List<StorageObject> nodeAddAspect = new ArrayList<StorageObject>();
        List<StorageObject> nodeAddConsumer = new ArrayList<StorageObject>();
        ContrattiService contrattiService = SpringUtil.getBean("contrattiService", ContrattiService.class);
        try {
            StorageObject nodeIncarico = contrattiService.getStorageObjectByPath(incarico_repertorio.getCMISFolder().getCMISPath());
            if (nodeIncarico != null && !contrattiService.hasAspect(nodeIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
                contrattiService.addAspect(nodeIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
                contrattiService.addConsumerToEveryone(nodeIncarico);
                nodeAddAspect.add(nodeIncarico);
                nodeAddConsumer.add(nodeIncarico);

                StorageObject nodeProcedura = contrattiService.getStorageObjectByPath(incarico_repertorio.getIncarichi_procedura().getCMISFolder().getCMISPath());
                if (nodeProcedura != null)
                    contrattiService.addConsumerToEveryone(nodeProcedura);
            }

            BulkList listArchiviFile = new BulkList();
            listArchiviFile.addAll(incarico_repertorio.getArchivioAllegati());
            listArchiviFile.addAll(incarico_repertorio.getIncarichi_repertorio_varColl());
            listArchiviFile.addAll(incarico_repertorio.getIncarichi_repertorio_rappColl());

            for (Iterator i = listArchiviFile.iterator(); i.hasNext(); ) {
                Incarichi_archivioBulk allegato = (Incarichi_archivioBulk) i.next();
                if (allegato.getCms_node_ref() != null) {
                    StorageObject nodeAllegato = contrattiService.getStorageObjectBykey(allegato.getCms_node_ref());
                    if (nodeAllegato != null && !contrattiService.hasAspect(nodeAllegato, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value()) &&
                            !contrattiService.hasAspect(nodeAllegato, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
                        contrattiService.addAspect(nodeAllegato, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
                        nodeAddAspect.add(nodeAllegato);
                    }
					if (nodeAllegato!=null) {
						if (incarico_repertorio.isIncaricoAnnullato() || incarico_repertorio.isIncaricoProvvisorio() || allegato.isAnnullato() ||
								!(allegato.isBando() || allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(nodeAllegato, false);
						else if (allegato.isBando())
							contrattiService.setInheritedPermission(nodeAllegato, true);
						else if (incarico_repertorio.isIncaricoDefinitivo() && (allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(nodeAllegato, true);
						else
							contrattiService.setInheritedPermission(nodeAllegato, false);
					}                    
                }
            }
        } catch (Exception e) {
            //Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
            for (StorageObject node : nodeAddAspect)
                contrattiService.removeAspect(node, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
            for (StorageObject node : nodeAddConsumer)
                contrattiService.removeConsumerToEveryone(node);
            throw new ApplicationException(e.getMessage());
        }
    }

    public void annullaDefinitivoCMIS(UserContext userContext, Incarichi_repertorioBulk incarico_repertorio) throws ComponentException {
        List<StorageObject> nodeRemoveAspect = new ArrayList<StorageObject>();
        List<StorageObject> nodeRemoveConsumer = new ArrayList<StorageObject>();
        ContrattiService contrattiService = SpringUtil.getBean("contrattiService", ContrattiService.class);
        try {
            StorageObject nodeIncarico = contrattiService.getStorageObjectByPath(incarico_repertorio.getCMISFolder().getCMISPath());
            if (nodeIncarico != null && contrattiService.hasAspect(nodeIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
                contrattiService.removeAspect(nodeIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
                contrattiService.removeConsumerToEveryone(nodeIncarico);
                nodeRemoveAspect.add(nodeIncarico);
                nodeRemoveConsumer.add(nodeIncarico);
            }

            BulkList listArchiviFile = new BulkList();
            listArchiviFile.addAll(incarico_repertorio.getArchivioAllegati());
            listArchiviFile.addAll(incarico_repertorio.getIncarichi_repertorio_varColl());
            listArchiviFile.addAll(incarico_repertorio.getIncarichi_repertorio_rappColl());

            for (Iterator i = listArchiviFile.iterator(); i.hasNext(); ) {
                Incarichi_archivioBulk allegato = (Incarichi_archivioBulk) i.next();
                if (allegato.getCms_node_ref() != null) {
                    StorageObject nodeAllegato = contrattiService.getStorageObjectBykey(allegato.getCms_node_ref());
                    if (nodeAllegato != null && contrattiService.hasAspect(nodeAllegato, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
                        contrattiService.removeAspect(nodeAllegato, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
                        nodeRemoveAspect.add(nodeIncarico);
                    }
					if (nodeAllegato!=null) {
						if (incarico_repertorio.isIncaricoAnnullato() || incarico_repertorio.isIncaricoProvvisorio() || allegato.isAnnullato() ||
								!(allegato.isBando() || allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(nodeAllegato, false);
						else if (allegato.isBando())
							contrattiService.setInheritedPermission(nodeAllegato, true);
						else if (incarico_repertorio.isIncaricoDefinitivo() && (allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(nodeAllegato, true);
						else
							contrattiService.setInheritedPermission(nodeAllegato, false);
					}                    
                }
            }
        } catch (Exception e) {
            //Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
            for (StorageObject node : nodeRemoveAspect)
                contrattiService.addAspect(node, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
            for (StorageObject node : nodeRemoveConsumer)
                contrattiService.addConsumerToEveryone(node);
            throw new ApplicationException(e.getMessage());
        }
    }

    public void aggiornaDatiPerla(UserContext aUC, Incarichi_repertorioBulk bulk, Long idPerla, String anomaliaPerla) throws ComponentException {
        try {
            Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) getHome(aUC, Incarichi_repertorioBulk.class).findByPrimaryKey(bulk);
            if (incarico == null)
                throw new ApplicationException("L'incarico e' stato cancellato");
            if (Optional.ofNullable(idPerla).isPresent())
                incarico.setIdPerla(idPerla.intValue());
            incarico.setAnomalia_perla(anomaliaPerla);
            incarico.setToBeUpdated();
            updateBulk(aUC, incarico);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public void comunicaPerla(UserContext userContext, Incarichi_repertorioBulk incarico) throws ComponentException {
        try {
            IncarichiEstrazioneFpComponentSession comp = Utility.createIncarichiEstrazioneFpComponentSession();
            try {
                comp.comunicaPerla2018(userContext, incarico);
            } catch (Exception e) {
                this.aggiornaDatiPerla(userContext, incarico, null, e.getMessage());
            }
        } catch (Exception e){
            throw handleException(e);
        }
    }
}
