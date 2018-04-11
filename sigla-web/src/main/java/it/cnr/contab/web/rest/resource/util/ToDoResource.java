package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.Albero_mainBulk;
import it.cnr.contab.utenze00.bulk.Albero_mainKey;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.util.ToDoLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class ToDoResource implements ToDoLocal {
    public static final String FIRMA_DIGITALE_PDG_VARIAZIONI_BP = "FirmaDigitalePdgVariazioniBP";
    public static final String FIRMA_DIGITALE_MANDATI_BP = "FirmaDigitaleMandatiBP";
    private final Logger LOGGER = LoggerFactory.getLogger(ToDoResource.class);
    @Context
    SecurityContext securityContext;

    @EJB
    PdGVariazioniComponentSession pdGVariazioniComponentSession;
    @EJB
    GestioneLoginComponentSession gestioneLoginComponentSession;
    @EJB
    CRUDComponentSession crudComponentSession;
    @EJB
    UtenteComponentSession utenteComponentSession;

    public Response map(@Context HttpServletRequest request) {
        UserContext userContext = Optional.ofNullable(securityContext.getUserPrincipal())
                .filter(UserContext.class::isInstance)
                .map(UserContext.class::cast)
                .orElseGet(() -> {
                    return Optional.ofNullable(request.getSession(false))
                            .flatMap(httpSession -> Optional.ofNullable(httpSession.getAttribute(HttpActionContext.USER_CONTEXT)))
                            .filter(UserContext.class::isInstance)
                            .map(UserContext.class::cast)
                            .orElse(null);
                });
        if (Optional.ofNullable(userContext).isPresent()) {
            UtenteBulk utente = new UtenteBulk();
            utente.setCd_utente(userContext.getUser());
            utente.setFl_attiva_blocco(Boolean.FALSE);

            List<ToDoDetail> result = new ArrayList<ToDoDetail>();
            try {
                if (gestioneLoginComponentSession.isBPEnableForUser(userContext, utente,
                        CNRUserContext.getCd_unita_organizzativa(userContext), FIRMA_DIGITALE_PDG_VARIAZIONI_BP)) {
                    BulkLoaderIterator remoteIterator =
                            Optional.ofNullable(pdGVariazioniComponentSession.cercaVariazioniForDocumentale(userContext, null, new Pdg_variazioneBulk(),
                                    ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED, Boolean.TRUE))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIterator)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    if (iterator.countElements() > 0) {
                                        result.add(new ToDoDetail(
                                                getCdNodo(userContext, FIRMA_DIGITALE_PDG_VARIAZIONI_BP, "PRVFIRMAVARIAZIONE"),
                                                "fa fa-fw fa-cubes text-primary",
                                                "Varizioni al PdG",
                                                "Sono presenti " + iterator.countElements() + " variazioni in attesa di firma digitale."
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                }
                if (gestioneLoginComponentSession.isBPEnableForUser(userContext, utente,
                        CNRUserContext.getCd_unita_organizzativa(userContext), FIRMA_DIGITALE_MANDATI_BP)) {
                    V_mandato_reversaleBulk v_mandato_reversaleBulk = new V_mandato_reversaleBulk();
                    v_mandato_reversaleBulk.setStato_trasmissione("N");
                    BulkLoaderIterator remoteIterator =
                            Optional.ofNullable(crudComponentSession.cerca(
                                    userContext,
                                    new CompoundFindClause(),
                                    v_mandato_reversaleBulk,
                                    "selectByClauseForFirmaMandati"))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIterator)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    if (iterator.countElements() > 0) {
                                        result.add(new ToDoDetail(
                                                getCdNodo(userContext, FIRMA_DIGITALE_MANDATI_BP, "DOCINTCASFIRMAMANRE"),
                                                "fa fa-fw fa-shopping-basket text-info",
                                                "Mandati/Reversali",
                                                "Sono presenti " + iterator.countElements() + " Mandati/Reversali in attesa di predisposizione."
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                    if (Optional.ofNullable(utenteComponentSession.isUtenteAbilitatoFirma(userContext, AbilitatoFirma.DOCCONT)).isPresent()) {
                        v_mandato_reversaleBulk.setStato_trasmissione("F");
                        BulkLoaderIterator remoteIteratorDaFirmare =
                                Optional.ofNullable(crudComponentSession.cerca(
                                        userContext,
                                        new CompoundFindClause(),
                                        v_mandato_reversaleBulk,
                                        "selectByClauseForFirmaMandati"))
                                        .filter(BulkLoaderIterator.class::isInstance)
                                        .map(BulkLoaderIterator.class::cast)
                                        .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                        Optional.ofNullable(remoteIteratorDaFirmare)
                                .ifPresent(iterator -> {
                                    try {
                                        iterator.open(userContext);
                                        if (iterator.countElements() > 0) {
                                            result.add(new ToDoDetail(
                                                    getCdNodo(userContext, FIRMA_DIGITALE_MANDATI_BP, "DOCINTCASFIRMAMANRE"),
                                                    "fa fa-fw fa-euro-sign text-info",
                                                    "Mandati/Reversali",
                                                    "Sono presenti " + iterator.countElements() + " Mandati/Reversali da firmare."
                                            ));
                                        }
                                    } catch (ComponentException | RemoteException e) {
                                        throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                    } finally {
                                        iterator.ejbRemove();
                                    }
                                });
                    }
                }

            } catch (ComponentException | RemoteException e) {
                LOGGER.error("ERROR in todo method", e);
                throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
            }
            return Response.ok(result).build();
        }
        return Response.ok(Collections.emptyMap()).build();
    }

    private String getCdNodo(UserContext userContext, String businessProcess, String cdAccesso) throws RemoteException, ComponentException {
        final List<Albero_mainBulk> nodi = crudComponentSession.find(
                userContext, Albero_mainBulk.class,
                "findNodo", userContext, businessProcess, cdAccesso);
        return nodi
                .stream()
                .findAny()
                .map(Albero_mainKey::getCd_nodo)
                .orElse(null);
    }

    public class ToDoDetail {
        private final String cdNodo;
        private final String faClass;
        private final String headerLabel;
        private final String label;

        public ToDoDetail(String cdNodo, String faClass, String headerLabel, String label) {
            this.cdNodo = cdNodo;
            this.faClass = faClass;
            this.headerLabel = headerLabel;
            this.label = label;
        }

        public String getCdNodo() {
            return cdNodo;
        }

        public String getFaClass() {
            return faClass;
        }

        public String getHeaderLabel() {
            return headerLabel;
        }

        public String getLabel() {
            return label;
        }
    }
}