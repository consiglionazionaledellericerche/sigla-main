package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.web.rest.local.util.PECFattureAttiveLocal;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PECFattureAttiveResource implements PECFattureAttiveLocal {
    private transient static final Logger logger = LoggerFactory.getLogger(PECFattureAttiveResource.class);
    @EJB
    private Configurazione_cnrComponentSession configurazione_cnrComponentSession;
    @EJB
    private DocAmmFatturazioneElettronicaComponentSession docAmmFatturazioneElettronicaComponentSession;
    @Context
    SecurityContext securityContext;
    @Override
    public Response reinviaPEC(HttpServletRequest request, Integer esercizio, Long pgFatturaAttiva) throws Exception {
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService =
                SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
        FatturaPassivaElettronicaService fatturaService = SpringUtil.getBean(FatturaPassivaElettronicaService.class);
        Configurazione_cnrBulk config = docAmmFatturazioneElettronicaComponentSession.getAuthenticatorPecSdi(userContext);
        logger.info("Recuperata Autenticazione PEC");
        String pwd = null;
        try {
            pwd = StringEncrypter.decrypt(config.getVal01(), config.getVal02());
        } catch (StringEncrypter.EncryptionException e1) {
            return Response.serverError().entity("Cannot decrypt password").build();
        }
        final String password = pwd;
        logger.info("Decrypt password");

        Fattura_attiva_IBulk fattura_attiva_iBulk = new Fattura_attiva_IBulk();
        fattura_attiva_iBulk.setEsercizio(esercizio);
        fattura_attiva_iBulk.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_INVIATA_SDI);
        fattura_attiva_iBulk.setPg_fattura_attiva(pgFatturaAttiva);
        List<Fattura_attivaBulk> fatture =
                docAmmFatturazioneElettronicaComponentSession.find(userContext, Fattura_attiva_IBulk.class, "find", userContext, fattura_attiva_iBulk);

        for (Fattura_attivaBulk fattura_attivaBulk : fatture) {
            final StorageObject fileXmlFatturaAttiva = documentiCollegatiDocAmmService.getFileXmlFatturaAttiva(fattura_attivaBulk);
            String nomeFile = fileXmlFatturaAttiva.getPropertyValue(StoragePropertyNames.NAME.value());
            String nomeFileP7m = nomeFile + ".p7m";
            final Optional<StorageObject> storageObjectByPath = Optional.ofNullable(
                    documentiCollegatiDocAmmService.getStorageObjectByPath(
                            documentiCollegatiDocAmmService.recuperoFolderFatturaByPath(fattura_attivaBulk).getPath()
                                    .concat(StorageService.SUFFIX).concat(nomeFileP7m)));
            if (storageObjectByPath.isPresent()) {
                fatturaService.inviaFatturaElettronica(
                        config.getVal01(),
                        password,
                        new ByteArrayDataSource(documentiCollegatiDocAmmService.getResource(storageObjectByPath.get()), MimeTypes.P7M.mimetype()),
                        docAmmFatturazioneElettronicaComponentSession.recuperoNomeFileXml(null, fattura_attivaBulk).concat(".p7m"));
            } else {
                logger.error("File firmato non trovato fattura {}/{}", fattura_attivaBulk.getEsercizio(), fattura_attivaBulk.getPg_fattura_attiva());
                return Response.serverError().entity("File firmato non trovato").build();
            }
        }
        return Response.ok().build();
    }
}
