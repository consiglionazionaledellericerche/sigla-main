/*
 * Created on Jan 19, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario01.bp;


import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

import java.rmi.RemoteException;
import java.util.Optional;

/**
 * @author rpucciarelli
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class CRUDCaricoScaricoInventarioBP extends SimpleCRUDBP {
    public static final String CARICO = "CARICO";
    public static final String SCARICO = "SCARICO";
    protected boolean by_fattura = false;
    protected boolean by_documento = false;
    private String Tipo;
    private boolean first = true;
    private boolean isAmministratore = false;
    private boolean isVisualizzazione = false;

    public CRUDCaricoScaricoInventarioBP() {
        super();
    }

    public CRUDCaricoScaricoInventarioBP(String function) {
        super(function);
    }

    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
        if (this instanceof CRUDCaricoInventarioBP)
            setTipo(CARICO);
        else
            setTipo(SCARICO);
        setSearchResultColumnSet(getTipo());
        setFreeSearchSet(getTipo());
        try {
            BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession) createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession", BuonoCaricoScaricoComponentSession.class);
            setVisualizzazione(session.isEsercizioCOEPChiuso(context.getUserContext()));
            setAmministratore(UtenteBulk.isAmministratoreInventario(context.getUserContext()));
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
        super.init(config, context);

        initVariabili(context, getTipo());
    }

    public void initVariabili(it.cnr.jada.action.ActionContext context, String Tipo) {

        if (this instanceof CRUDCaricoInventarioBP)
            setTipo(CARICO);
        else
            setTipo(SCARICO);
        setSearchResultColumnSet(getTipo());
        setFreeSearchSet(getTipo());
    }

    public OggettoBulk initializeModelForEdit(ActionContext context, OggettoBulk bulk) throws BusinessProcessException {
        try {
            BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession) createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession", BuonoCaricoScaricoComponentSession.class);
            setAmministratore(UtenteBulk.isAmministratoreInventario(context.getUserContext()));
        } catch (ComponentException e1) {
            throw handleException(e1);
        } catch (RemoteException e1) {
            throw handleException(e1);
        }
        bulk = super.initializeModelForEdit(context, bulk);
        return bulk;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String string) {
        Tipo = string;
    }

    public boolean isBy_fattura() {
        return by_fattura;
    }

    public void setBy_fattura(boolean b) {
        by_fattura = b;
        setFirst(true);
        Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) getModel();
        buonoCS.setByFattura(new Boolean(b));

    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean b) {
        first = b;
    }

    public boolean isAmministratore() {
        return isAmministratore;
    }

    public void setAmministratore(boolean b) {
        isAmministratore = b;
    }

    public boolean isVisualizzazione() {
        return isVisualizzazione;
    }

    public void setVisualizzazione(boolean b) {
        isVisualizzazione = b;
    }

    public boolean isEditable() {
        return !isVisualizzazione() && super.isEditable();
    }

    public boolean isAssociata(UserContext uc, Buono_carico_scarico_dettBulk dett) throws BusinessProcessException, ComponentException, RemoteException {
        if (dett == null || dett.getBene() == null || dett.getBene().getProgressivo() == null)
            return false;
        else {
            BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession) createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession", BuonoCaricoScaricoComponentSession.class);
            return session.verifica_associazioni(uc, dett);
        }

    }

    public boolean isAssociataTestata(UserContext uc, Buono_carico_scaricoBulk buono) throws BusinessProcessException, ComponentException, RemoteException {
        if (buono == null)
            return false;
        else {
            BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession) createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession", BuonoCaricoScaricoComponentSession.class);
            return session.verifica_associazioni(uc, buono);
        }

    }

    public boolean isNonUltimo(UserContext uc, Buono_carico_scarico_dettBulk dett) throws BusinessProcessException, ComponentException, RemoteException {

        if (dett == null || dett.getBene() == null || dett.getBene().getProgressivo() == null)
            return false;
        else {
            BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession) createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession", BuonoCaricoScaricoComponentSession.class);
            return session.isNonUltimo(uc, dett);
        }
    }

    public boolean isBy_documento() {
        return by_documento;
    }

    public void setBy_documento(boolean by_doc) {
        by_documento = by_doc;
        setFirst(true);
        Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk) getModel();
        buonoCS.setByDocumento(new Boolean(by_doc));
    }

    public boolean isContabilizzato(UserContext uc, Buono_carico_scaricoBulk buono) throws BusinessProcessException, ComponentException, RemoteException {
        if (buono == null)
            return false;
        else {
            BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession) createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession", BuonoCaricoScaricoComponentSession.class);
            return session.isContabilizzato(uc, buono);
        }
    }

    @Override
    public boolean isPrintButtonHidden() {
        return !Optional.ofNullable(getModel())
                .filter(Buono_carico_scaricoBulk.class::isInstance)
                .map(Buono_carico_scaricoBulk.class::cast)
                .flatMap(buono_carico_scaricoBulk -> Optional.ofNullable(buono_carico_scaricoBulk.getPg_buono_c_s()))
                .isPresent();
    }

    @Override
    protected void initializePrintBP(ActionContext actioncontext, AbstractPrintBP abstractprintbp) {
        OfflineReportPrintBP printbp = (OfflineReportPrintBP) abstractprintbp;
        printbp.setReportName("/cnrdocamm/docamm/buono_carico_scarico.jasper");
        final Buono_carico_scaricoBulk buono_carico_scaricoBulk = Optional.ofNullable(getModel())
                .filter(Buono_carico_scaricoBulk.class::isInstance)
                .map(Buono_carico_scaricoBulk.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Modello vuoto!"));

        Print_spooler_paramBulk param;

        param = new Print_spooler_paramBulk();
        param.setNomeParam("esercizio");
        param.setValoreParam(
                Optional.ofNullable(buono_carico_scaricoBulk.getEsercizio())
                    .map(integer -> String.valueOf(integer))
                    .orElse(null)
        );
        param.setParamType(Integer.class.getCanonicalName());
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("pg_inventario");
        param.setValoreParam(
                Optional.ofNullable(buono_carico_scaricoBulk.getPg_inventario())
                        .map(integer -> String.valueOf(integer))
                        .orElse(null)
        );
        param.setParamType(Integer.class.getCanonicalName());
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("ti_documento");
        param.setValoreParam(
                Optional.ofNullable(buono_carico_scaricoBulk.getTi_documento())
                        .orElse(null)
        );
        param.setParamType(String.class.getCanonicalName());
        printbp.addToPrintSpoolerParam(param);

        param = new Print_spooler_paramBulk();
        param.setNomeParam("pg_buono_c_s");
        param.setValoreParam(
                Optional.ofNullable(buono_carico_scaricoBulk.getPg_buono_c_s())
                        .map(integer -> String.valueOf(integer))
                        .orElse(null)
        );
        param.setParamType(Integer.class.getCanonicalName());
        printbp.addToPrintSpoolerParam(param);

    }
}
