package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.web.rest.local.util.FattureAttiveLocal;
import it.cnr.jada.UserContext;
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
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class FattureAttiveResource implements FattureAttiveLocal {
    private transient static final Logger logger = LoggerFactory.getLogger(FattureAttiveResource.class);
    @EJB
    private Configurazione_cnrComponentSession configurazione_cnrComponentSession;
    @EJB
    private DocAmmFatturazioneElettronicaComponentSession docAmmFatturazioneElettronicaComponentSession;

    @Override
    public Response reinviaPEC(HttpServletRequest request) throws Exception {
        UserContext userContext = new WSUserContext("PEC", null, 2019, null, null, null);
        DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService =
                SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
        FatturaPassivaElettronicaService fatturaService = SpringUtil.getBean(FatturaPassivaElettronicaService.class);
        Fattura_attiva_IBulk fattura_attiva_iBulk = new Fattura_attiva_IBulk();
        fattura_attiva_iBulk.setEsercizio(2019);
        fattura_attiva_iBulk.setStatoInvioSdi(Fattura_attivaBulk.FATT_ELETT_INVIATA_SDI);
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
                        "comunicazioni.sdi@pec.cnr.it",
                        "w319f64r",
                        new ByteArrayDataSource(documentiCollegatiDocAmmService.getResource(storageObjectByPath.get()), MimeTypes.P7M.mimetype()),
                        docAmmFatturazioneElettronicaComponentSession.recuperoNomeFileXml(null, fattura_attivaBulk).concat(".p7m"));
            } else {
                logger.error("File firmato non trovato fattura {}/{}", fattura_attivaBulk.getEsercizio(), fattura_attivaBulk.getPg_fattura_attiva());
            }
        }
        return Response.ok().build();
    }
}
