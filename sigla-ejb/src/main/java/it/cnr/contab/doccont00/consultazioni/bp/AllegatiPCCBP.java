package it.cnr.contab.doccont00.consultazioni.bp;

import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentIBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.OrderConstants;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AllegatiPCCBP extends AllegatiCRUDBP<AllegatoGenericoBulk, AllegatoParentIBulk> {
    public static final String COMUNICAZIONI_PCC = "Comunicazioni PCC";
    private Integer esercizio;

    public AllegatiPCCBP() {
    }

    public AllegatiPCCBP(String s) {
        super(s);
    }

    @Override
    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        esercizio = CNRUserContext.getEsercizio(actioncontext.getUserContext());
        setStatus(EDIT);
        final AllegatoParentIBulk allegatoParentIBulk = new AllegatoParentIBulk();
        allegatoParentIBulk.setCrudStatus(OggettoBulk.NORMAL);
        setModel(actioncontext, initializeModelForEditAllegati(actioncontext, allegatoParentIBulk));
        getCrudArchivioAllegati().setOrderBy(actioncontext, "lastModificationDate", OrderConstants.ORDER_DESC);
    }

    @Override
    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        Optional.ofNullable(getModel())
                .filter(AllegatoParentIBulk.class::isInstance)
                .map(AllegatoParentIBulk.class::cast)
                .ifPresent(allegatoParentIBulk -> allegatoParentIBulk.getArchivioAllegati().clear());
        return initializeModelForEditAllegati(actioncontext, oggettobulk);
    }

    @Override
    public boolean isSaveButtonEnabled() {
        return super.isSaveButtonEnabled();
    }

    @Override
    public boolean isSearchButtonHidden() {
        return true;
    }

    @Override
    public boolean isFreeSearchButtonHidden() {
        return true;
    }

    @Override
    public boolean isNewButtonHidden() {
        return true;
    }

    @Override
    public boolean isDeleteButtonHidden() {
        return true;
    }

    @Override
    protected String getStorePath(AllegatoParentIBulk allegatoParentIBulk, boolean create) throws BusinessProcessException {
        return AllegatoParentIBulk.getStorePath(COMUNICAZIONI_PCC, esercizio);
    }

    @Override
    protected Class<AllegatoGenericoBulk> getAllegatoClass() {
        return AllegatoGenericoBulk.class;
    }

    @Override
    public String getAllegatiFormName() {
        return "lastModificationDate";
    }

    @Override
    protected void completeUpdateAllegato(UserContext userContext, AllegatoGenericoBulk allegato) throws ApplicationException {
        super.completeUpdateAllegato(userContext, allegato);
        FatturaElettronicaPassivaComponentSession component =
                (FatturaElettronicaPassivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession");
        try {
            Optional.ofNullable(allegato.getFile())
                .ifPresent(file -> {
                    try {
                        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
                        XSSFSheet s = wb.getSheet(wb.getSheetName(0));
                        Map<String, String> results = new HashMap<String, String>();
                        for(int i=8; i <= s.getLastRowNum(); i++){
                            final XSSFRow row = s.getRow(i);
                            final Optional<String> identifictivoSDI = Optional.ofNullable(row).map(cells -> cells.getCell(1)) .map(XSSFCell::getRawValue);
                            if (identifictivoSDI.isPresent()) {
                                final Optional<String> esito = Optional.ofNullable(row.getCell(20)).map(XSSFCell::getStringCellValue);
                                if (esito.filter(s1 -> s1.length() > 0).isPresent()) {
                                    results.put(identifictivoSDI.get(), esito.get());
                                }
                            }
                        }
                        component.aggiornaEsitoPCC(userContext, results);
                    } catch (IOException | ComponentException e) {
                        throw new ApplicationRuntimeException(e);
                    }
                });
        } catch (Exception _ex) {
            throw new ApplicationMessageFormatException("Il file non è stato elaborato per il seguente errore: {0}", _ex.getMessage());
        }
    }
}
