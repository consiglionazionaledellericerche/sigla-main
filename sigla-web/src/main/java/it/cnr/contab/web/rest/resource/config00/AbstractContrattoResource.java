package it.cnr.contab.web.rest.resource.config00;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.config00.contratto.bulk.*;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.model.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.*;
@Stateless
public abstract  class AbstractContrattoResource {
    private final Logger LOGGER = LoggerFactory.getLogger(ContrattoResource.class);

    @Context SecurityContext securityContext;
    @EJB CRUDComponentSession crudComponentSession;
    @EJB ContrattoComponentSession contrattoComponentSession;
    @EJB Unita_organizzativaComponentSession unita_organizzativaComponentSession;

    private void checkRowDettaglioContrattoArticolo(DettaglioContrattoDtoBulk row){
        if (!Optional.ofNullable(row.getCdBeneServizio()).isPresent())
            throw new RestException(Response.Status.BAD_REQUEST,String.format("Il codice Bene Servizio è obbligatorio per le righe del Dettaglio"));
        if (!Optional.ofNullable(row.getPrezzoUnitario()).isPresent() && row.getPrezzoUnitario().compareTo(BigDecimal.ZERO)<=0)
            throw new RestException(Response.Status.BAD_REQUEST,String.format("Il prezzo unitario è obbligatorio e deve essere maggiore di zero per le righe del Dettaglio"));
    }
    private void checkRowDettaglioContrattoCatGrp(DettaglioContrattoDtoBulk row){
        if (!Optional.ofNullable(row.getCdCategoriaGruppo()).isPresent())
            throw new RestException(Response.Status.BAD_REQUEST,String.format("La categoria/Gruppo è obbligatoria per le righe del Dettaglio"));
        if (!Optional.ofNullable(row.getPrezzoUnitario()).isPresent() && row.getPrezzoUnitario().compareTo(BigDecimal.ZERO)<=0)
            throw new RestException(Response.Status.BAD_REQUEST,String.format("Il prezzo unitario è obbligatorio e deve essere maggiore di zero per le righe del Dettaglio"));

    }
    private void checkRowDettaglioContratto(EnumTipoDettaglioContratto tipoDettaglioContratto,DettaglioContrattoDtoBulk row){
       if ( tipoDettaglioContratto.equals(EnumTipoDettaglioContratto.DETTAGLIO_CONTRATTO_CATGRP))
           checkRowDettaglioContrattoCatGrp(row);
        if ( tipoDettaglioContratto.equals(EnumTipoDettaglioContratto.DETTAGLIO_CONTRATTO_ARTICOLI))
            checkRowDettaglioContrattoArticolo(row);
    }
    private void checkRowsDettaglioContratto(ContrattoDtoBulk contrattoDtoBulk){
        Optional.ofNullable(contrattoDtoBulk.getDettaglioContratto()).
                ifPresent(stream->stream.forEach(row->{
                    checkRowDettaglioContratto(contrattoDtoBulk.getTipoDettaglioContratto(),row);
                }));
    }
    private Dettaglio_contrattoBulk getDettaglioContrattoArticolo(DettaglioContrattoDtoBulk row){
        //check Bene Servizio esiste in sigla
        Dettaglio_contrattoBulk d = new Dettaglio_contrattoBulk();
        d.setCdBeneServizio(row.getCdBeneServizio());
        d.setPrezzoUnitario(row.getPrezzoUnitario());
        d.setQuantitaMax( row.getQuantitaMax());
        d.setQuantitaMin(row.getQuantitaMin());
        return d;
    }
    private Dettaglio_contrattoBulk getDettaglioContrattoCatGrp(DettaglioContrattoDtoBulk row){
        //check Categoria Gruppo esiste in sigla
        Dettaglio_contrattoBulk d = new Dettaglio_contrattoBulk();
        d.setCdCategoriaGruppo(row.getCdCategoriaGruppo());
        d.setPrezzoUnitario(row.getPrezzoUnitario());
        return d;
    }
    private Dettaglio_contrattoBulk getDettaglioContratto(EnumTipoDettaglioContratto tipoDettaglioContratto,DettaglioContrattoDtoBulk row){
        if ( tipoDettaglioContratto.equals(EnumTipoDettaglioContratto.DETTAGLIO_CONTRATTO_CATGRP))
            return getDettaglioContrattoCatGrp(row);
        if ( tipoDettaglioContratto.equals(EnumTipoDettaglioContratto.DETTAGLIO_CONTRATTO_ARTICOLI))
            return getDettaglioContrattoArticolo(row);
        return null;

    }
    private List<Dettaglio_contrattoBulk> getListaDettagliContratto( EnumTipoDettaglioContratto tipoDettaglioContratto,List<DettaglioContrattoDtoBulk> dettagliContrattoDtoBulk){
        List<Dettaglio_contrattoBulk> dettaglio = new ArrayList<Dettaglio_contrattoBulk>();
        Optional.ofNullable(dettagliContrattoDtoBulk).
                ifPresent(stream->stream.forEach(row->{
                   dettaglio.add(getDettaglioContratto(tipoDettaglioContratto,row));
                }));
        return Collections.EMPTY_LIST;
    }

    public void validateContratto(ContrattoDtoBulk contrattoBulk,CNRUserContext userContext) throws RemoteException, ComponentException {
        //Check valore tipoDettaglioContratto
        if (Optional.ofNullable(contrattoBulk.getTipo_dettaglio_contratto()).isPresent()){
            if ( Utility.createConfigurazioneCnrComponentSession().isAttivoOrdini(userContext))
                throw new RestException(Response.Status.BAD_REQUEST,String.format("Il dettaglio del contratto non è previsto su questa installazione SIGLA"));

            /*
            if ( !contrattoBulk.getTipo_dettaglio_contratto().equals(ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI) &&
                    contrattoBulk.getTipo_dettaglio_contratto().equals(ContrattoBulk.DETTAGLIO_CONTRATTO_CATGRP))
            throw new RestException(Response.Status.BAD_REQUEST, String.format("Per Il Tipo Dettaglio Contratto sono previsti i seguenti valori:{ vuoto,"
                    + ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI)+","+ContrattoBulk.DETTAGLIO_CONTRATTO_CATGRP +"}");
            if ( contrattoBulk.getTipo_dettaglio_contratto().equals(ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI)){
                //if (CollectionUtils.isEmpty(contrattoBulk.getDettaglioContratto()))

            }
             */
            if ( Optional.ofNullable(contrattoBulk.getTipoDettaglioContratto()).isPresent()){
                checkRowsDettaglioContratto(contrattoBulk);
            }
        }
    }

    protected ContrattoBulk innerCreaContrattoBulk( CNRUserContext userContext ,ContrattoBulk contratto) throws ComponentException, RemoteException {
        return (ContrattoBulk) contrattoComponentSession.creaContrattoDaFlussoAcquisti(userContext, contratto,false);
    }

    protected ContrattoBulk creaContrattoBulk(CNRUserContext userContext ,ContrattoBulk contrattoBulkSigla) throws ComponentException, RemoteException {
        final ContrattoBulk contratto = (ContrattoBulk) contrattoComponentSession.inizializzaBulkPerInserimento(
                userContext,
                contrattoBulkSigla);

        contratto.setToBeCreated();
        return innerCreaContrattoBulk( userContext,contratto);

    }

    public Response insertContratto(@Context HttpServletRequest request, ContrattoDtoBulk contrattoBulk) throws Exception {

        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        Optional.ofNullable(contrattoBulk.getEsercizio()).filter(x -> userContext.getEsercizio().equals(x)).
                orElseThrow(() -> new RestException(Response.Status.BAD_REQUEST, "Esercizio del contesto diverso da quello del Contratto"));
        Optional.ofNullable(contrattoBulk.getEsercizio()).orElse(getYearFromToday());
        validateContratto(contrattoBulk,userContext);
        ContrattoBulk contrattoBulkSigla = creaContrattoSigla(contrattoBulk, userContext);
        contrattoBulkSigla.setStato(ContrattoBulk.STATO_PROVVISORIO);
        if (contrattoBulkSigla.getCd_unita_organizzativa() != null){
            if (contrattoBulkSigla.getCd_unita_organizzativa().length() == 6){
                contrattoBulkSigla.setCd_unita_organizzativa(contrattoBulkSigla.getCd_unita_organizzativa().substring(0, 3)+"."+contrattoBulkSigla.getCd_unita_organizzativa().substring(3));
            } else {
                throw new RestException(Response.Status.BAD_REQUEST, String.format("L'Unita Organizzativa indicata %s non è conforme con il formato atteso", contrattoBulkSigla.getCd_unita_organizzativa()));
            }
        } else {
            throw new RestException(Response.Status.BAD_REQUEST, "Unita Organizzativa non indicata");
        }

        contrattoBulkSigla.setNatura_contabile(ContrattoBulk.NATURA_CONTABILE_PASSIVO);
        contrattoBulkSigla.setAtto(new Tipo_atto_amministrativoBulk());
        contrattoBulkSigla.setCd_tipo_atto("DET");

/*
        final ContrattoBulk contratto = (ContrattoBulk) contrattoComponentSession.inizializzaBulkPerInserimento(
                userContext,
                contrattoBulkSigla);

        contratto.setToBeCreated();

 */
        ContrattoBulk contrattoCreated = creaContrattoBulk( userContext,contrattoBulkSigla);
        contrattoBulk.setPg_contratto(contrattoCreated.getPg_contratto());
        return Response.status(Response.Status.CREATED).entity(contrattoBulk).build();
    }


    private List<AllegatoContrattoFlussoDocumentBulk> getAllegatiContrattoFlusso(ContrattoBulk contrattoBulk,List<AttachmentContratto> l){
        List<AllegatoContrattoFlussoDocumentBulk> attachments = new ArrayList<AllegatoContrattoFlussoDocumentBulk>();
        Optional.ofNullable(l).
                ifPresent(stream->stream.forEach(a->{
                    AllegatoContrattoFlussoDocumentBulk attachment = new AllegatoContrattoFlussoDocumentBulk();
                    attachment.setType(a.getTypeAttachment().matadata);
                    attachment.setBytes(Base64.getDecoder().decode(a.getBytes()));
                    attachment.setNome(a.getNomeFile());
                    attachment.setTitolo(a.getNomeFile());
                    attachment.setContentType(a.getMimeTypes().mimetype());
                    attachments.add(attachment);
                    attachment.setContrattoBulk(contrattoBulk);
                    attachment.setCrudStatus(OggettoBulk.TO_BE_CREATED);
                    }));

        return attachments;
    }

    private ContrattoBulk creaContrattoSigla(ContrattoDtoBulk contrattoBulk, CNRUserContext userContext) throws PersistencyException, ValidationException, ComponentException, RemoteException {
        ContrattoBulk contrattoBulkSigla = new ContrattoBulk();
        if (Optional.ofNullable(contrattoBulk.getCd_unita_organizzativa()).isPresent()){
            contrattoBulkSigla.setUnita_organizzativa(new Unita_organizzativaBulk());
            contrattoBulkSigla.setCd_unita_organizzativa(contrattoBulk.getCd_unita_organizzativa());
        }
        contrattoBulkSigla.setEsercizio(contrattoBulk.getEsercizio());
        contrattoBulkSigla.setPg_contratto(contrattoBulk.getPg_contratto());
        contrattoBulkSigla.setStato(contrattoBulk.getStato());
        contrattoBulkSigla.setIm_contratto_attivo(contrattoBulk.getIm_contratto_attivo());
        if (Optional.ofNullable(contrattoBulk.getCd_organo()).isPresent()){
            contrattoBulkSigla.setOrgano(new OrganoBulk());
            contrattoBulkSigla.setCd_organo(contrattoBulk.getCd_organo());
        }
        if (Optional.ofNullable(contrattoBulk.getCd_proc_amm()).isPresent()){
            contrattoBulkSigla.setProcedura_amministrativa(new Procedure_amministrativeBulk());
            contrattoBulkSigla.setCd_proc_amm(contrattoBulk.getCd_proc_amm());
        }
        contrattoBulkSigla.setCd_protocollo(contrattoBulk.getCd_protocollo());
        contrattoBulkSigla.setCd_protocollo_generale(contrattoBulk.getCd_protocollo_generale());
        if (Optional.ofNullable(contrattoBulk.getCd_terzo_firmatario()).isPresent()){
            contrattoBulkSigla.setFirmatario(new V_persona_fisicaBulk());
            contrattoBulkSigla.setCd_terzo_firmatario(contrattoBulk.getCd_terzo_firmatario());
        }
        if (Optional.ofNullable(contrattoBulk.getCd_terzo_resp()).isPresent()){
            contrattoBulkSigla.setResponsabile(new V_persona_fisicaBulk());
            contrattoBulkSigla.setCd_terzo_resp(contrattoBulk.getCd_terzo_resp());
        }
        if (Optional.ofNullable(contrattoBulk.getCd_tipo_atto()).isPresent()){
            contrattoBulkSigla.setAtto(new Tipo_atto_amministrativoBulk());
            contrattoBulkSigla.setCd_tipo_atto(contrattoBulk.getCd_tipo_atto());
        }
        contrattoBulkSigla.setCdCigExt(contrattoBulk.getCdCigExt());
        if (Optional.ofNullable(contrattoBulk.getCd_tipo_contratto()).isPresent()){
            contrattoBulkSigla.setTipo_contratto(new Tipo_contrattoBulk());
            contrattoBulkSigla.setCd_tipo_contratto(contrattoBulk.getCd_tipo_contratto());
        }
        contrattoBulkSigla.setCdCigFatturaAttiva(contrattoBulk.getCdCigFatturaAttiva());
        contrattoBulkSigla.setCodfisPivaAggiudicatarioExt(contrattoBulk.getCodfisPivaAggiudicatarioExt());
        contrattoBulkSigla.setCodfisPivaFirmatarioExt(contrattoBulk.getCodfisPivaFirmatarioExt());
        contrattoBulkSigla.setCodfisPivaRupExt(contrattoBulk.getCodfisPivaRupExt());
        contrattoBulkSigla.setCodiceFlussoAcquisti(contrattoBulk.getCodiceFlussoAcquisti());
        contrattoBulkSigla.setDs_atto(contrattoBulk.getDs_atto());
        contrattoBulkSigla.setDs_organo_non_definito(contrattoBulk.getDs_organo_non_definito());
        contrattoBulkSigla.setDt_fine_validita(contrattoBulk.getDt_fine_validita());
        contrattoBulkSigla.setDt_inizio_validita(contrattoBulk.getDt_inizio_validita());
        contrattoBulkSigla.setDt_proroga(contrattoBulk.getDt_proroga());
        contrattoBulkSigla.setDt_registrazione(contrattoBulk.getDt_registrazione());
        contrattoBulkSigla.setDt_stipula(contrattoBulk.getDt_stipula());
        contrattoBulkSigla.setEsercizio_protocollo(contrattoBulk.getEsercizio_protocollo());

        if (Optional.ofNullable(contrattoBulk.getFig_giur_est()).isPresent()){
            contrattoBulkSigla.setFigura_giuridica_esterna(new TerzoBulk());
            contrattoBulkSigla.setFig_giur_est(contrattoBulk.getFig_giur_est());
        }
        if (Optional.ofNullable(contrattoBulk.getFig_giur_int()).isPresent()){
            contrattoBulkSigla.setFigura_giuridica_interna(new TerzoBulk());
            contrattoBulkSigla.setFig_giur_int(contrattoBulk.getFig_giur_int());
        }
        contrattoBulkSigla.setFl_art82(Boolean.FALSE);
        Optional.ofNullable(contrattoBulk.getFl_art82()).ifPresent(fl->{
            contrattoBulkSigla.setFl_art82(fl);
        });

        contrattoBulkSigla.setFl_mepa(contrattoBulk.getFl_mepa());
        contrattoBulkSigla.setFl_pubblica_contratto(contrattoBulk.getFl_pubblica_contratto());
        contrattoBulkSigla.setIm_contratto_passivo(contrattoBulk.getIm_contratto_passivo());
        contrattoBulkSigla.setIm_contratto_passivo_netto(contrattoBulk.getIm_contratto_passivo_netto());
        contrattoBulkSigla.setNatura_contabile(contrattoBulk.getNatura_contabile());
        contrattoBulkSigla.setOggetto(contrattoBulk.getOggetto());
        if (Optional.ofNullable(contrattoBulk.getPg_progetto()).isPresent()){
            contrattoBulkSigla.setProgetto(new ProgettoBulk());
            contrattoBulkSigla.setPg_progetto(contrattoBulk.getPg_progetto());
        }
        contrattoBulkSigla.setResp_esterno(contrattoBulk.getResp_esterno());
        contrattoBulkSigla.setStato_padre(contrattoBulk.getStato_padre());
        gestioneCupSuContrattoDaFlows(userContext, contrattoBulkSigla, contrattoBulk.getCdCupExt());
        if (contrattoBulk.getListaDitteInvitateExt() != null && !contrattoBulk.getListaDitteInvitateExt().isEmpty()){
            for (DittaInvitataExt ditta : contrattoBulk.getListaDitteInvitateExt()){
                Ass_contratto_ditteBulk dittaContr = new Ass_contratto_ditteBulk();
                if (ditta.getDittaExtraUE() != null && ditta.getDittaExtraUE().equals("NO")){
                    dittaContr.setCodice_fiscale(ditta.getpIvaCodiceFiscaleDittaInvitata());
                } else {
                    dittaContr.setId_fiscale(ditta.getpIvaCodiceFiscaleDittaInvitata());
                }
                dittaContr.setTipologia(Ass_contratto_ditteBulk.LISTA_INVITATE);
                dittaContr.setUser(contrattoBulkSigla.getUser());
                dittaContr.setDenominazione(ditta.getRagioneSocialeDittaInvitata());
                contrattoBulkSigla.addToDitteInvitate(dittaContr);
            }
        }
        if (contrattoBulk.getListaUoAbilitateExt() != null && !contrattoBulk.getListaUoAbilitateExt().isEmpty()){
            for (UoAbilitataExt uoExt : contrattoBulk.getListaUoAbilitateExt()){
                Ass_contratto_uoBulk uo = new Ass_contratto_uoBulk();
                if (uoExt.getUo() != null){
                    if (uoExt.getUo().length() == 6){
                        uo.setUnita_organizzativa(new Unita_organizzativaBulk());
                        uo.setCd_unita_organizzativa(uoExt.getUo().substring(0, 3)+"."+uoExt.getUo().substring(3));
                        uo.setContratto(contrattoBulkSigla);
                        uo.setEsercizio(contrattoBulkSigla.getEsercizio());
                        uo.setStato_contratto(contrattoBulkSigla.getStato());
                        contrattoBulkSigla.addToAssociazioneUO(uo);
                    } else {
                        throw new RestException(Response.Status.BAD_REQUEST, String.format("L'Unita Organizzativa indicata %s non è conforme con il formato atteso", uoExt.getUo()));
                    }
                } else {
                    throw new RestException(Response.Status.BAD_REQUEST, "Unita Organizzativa non indicata");
                }
            }
        }
        if ( Optional.ofNullable(contrattoBulk.getTipoDettaglioContratto()).isPresent()){
            contrattoBulkSigla.setTipo_dettaglio_contratto( contrattoBulk.getTipoDettaglioContratto().name());
            contrattoBulkSigla.setDettaglio_contratto(new BulkList<Dettaglio_contrattoBulk>(getListaDettagliContratto(contrattoBulk.getTipoDettaglioContratto(),contrattoBulk.getDettaglioContratto())));
        }
        contrattoBulkSigla.setArchivioAllegatiFlusso(new BulkList<AllegatoContrattoFlussoDocumentBulk>(getAllegatiContrattoFlusso(contrattoBulkSigla,contrattoBulk.getAttachments())));
        return contrattoBulkSigla;
    }

    private Date getDateTodayWithoutTime(){
        Calendar cal = getTodayWithoutTime();
        return cal.getTime();
    }
    private Calendar getTodayWithoutTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private Integer getYearFromToday() {
        Calendar cal = getTodayWithoutTime();
        return cal.get(Calendar.YEAR);
    }
    private void gestioneCupSuContrattoDaFlows(UserContext userContext, ContrattoBulk contratto, String cupExt)
            throws PersistencyException, ComponentException, RemoteException, EJBException {
        if (cupExt != null){

            CupBulk cup = new CupBulk();
            cup.setCdCup(cupExt);
            cup.setDescrizione(contratto.getOggetto());
            CupBulk cupDb = (CupBulk)crudComponentSession.findByPrimaryKey(userContext, cup);
            if (cupDb != null){
                contratto.setCup(cupDb);
            } else {
                cup.setUser(contratto.getUser());
                cup.setToBeCreated();
                contratto.setCup(cup);
            }
        }
    }

    public Response recuperoDatiContratto(@Context HttpServletRequest request, @QueryParam("uo") String uo,
                                          @QueryParam("cdTerzo") Integer cdTerzo) throws Exception {
        LOGGER.debug("REST request per recupero Dati Contratto.");
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        final ContrattoDatiSintesiBulk contrattoDatiSintesiBulk = new ContrattoDatiSintesiBulk();
        Optional.ofNullable(uo).orElseThrow(() -> new RestException(Response.Status.BAD_REQUEST, "Errore, uo di selezione obbligatoria."));
        Optional.ofNullable(cdTerzo).orElseThrow(() -> new RestException(Response.Status.BAD_REQUEST, "Errore, codice terzo di selezione obbligatoria."));

        try {
            List<ContrattoDatiSintesiBulk> dati =
                    crudComponentSession.find(userContext, ContrattoDatiSintesiBulk.class, "recuperoDati", userContext, contrattoDatiSintesiBulk, ContrattoBulk.NATURA_CONTABILE_PASSIVO, cdTerzo, uo);

            LOGGER.debug("Fine REST per recupero Dati Contratto.");
            return Response.ok(Optional.ofNullable(dati).orElse(Collections.emptyList())).build();
        } catch (Exception _ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("ERROR", _ex)).build();
        }
    }
}
