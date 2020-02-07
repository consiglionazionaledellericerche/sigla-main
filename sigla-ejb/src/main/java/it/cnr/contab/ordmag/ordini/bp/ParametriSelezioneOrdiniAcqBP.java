package it.cnr.contab.ordmag.ordini.bp;

import it.cnr.contab.ordmag.ordini.bulk.ParametriSelezioneOrdiniAcqBulk;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;

public class ParametriSelezioneOrdiniAcqBP extends BulkBP {

    public static final String VIS_ORDINI_RIGA_CONS = "visOrdiniConsegna";
    public static final String EVA_FORZATA_ORDINI = "evaForzataOrdini";

    private String tipoSelezione;

    public String getTipoSelezione() {
        return tipoSelezione;
    }

    public void setTipoSelezione(String tipoSelezione) {
        this.tipoSelezione = tipoSelezione;
    }

    public ParametriSelezioneOrdiniAcqBP() {
            this("");
    }

    public ParametriSelezioneOrdiniAcqBP(String s) {
        super(s);
    }

    public ParametriSelezioneOrdiniAcqBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

        try {
            return createNewBulk(context).initializeForSearch(this,context);
        } catch(Exception e) {
            throw handleException(e);
        }
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext context) throws BusinessProcessException {
        super.init(config,context);
        try {
            ParametriSelezioneOrdiniAcqBulk bulk = createEmptyModelForSearch(context);
            bulk = (ParametriSelezioneOrdiniAcqBulk)((OrdineAcqComponentSession)createComponentSession(context)).initializeAbilitazioneOrdiniAcq(context.getUserContext(), bulk);
            setModel(context,bulk);
            setDirty(false);
            resetChildren(context);
        } catch(Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    /**
     * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
     */
    public ParametriSelezioneOrdiniAcqBulk createNewBulk(ActionContext context) throws BusinessProcessException {
        try {
            ParametriSelezioneOrdiniAcqBulk bulk = new ParametriSelezioneOrdiniAcqBulk();
            bulk.setUser(context.getUserInfo().getUserid());
            return bulk;
        } catch(Exception e) {
            throw handleException(e);
        }
    }

    public it.cnr.jada.ejb.CRUDComponentSession createComponentSession(ActionContext actionContext) throws BusinessProcessException {

        return (OrdineAcqComponentSession) createComponentSession(
                "CNRORDMAG00_EJB_OrdineAcqComponentSession",
                OrdineAcqComponentSession.class);

    }

    public RemoteIterator ricercaOrdiniAcq(ActionContext actioncontext) throws BusinessProcessException {
        try {
            OrdineAcqComponentSession cs = (OrdineAcqComponentSession)createComponentSession(actioncontext);
            if (cs == null) return null;
            ParametriSelezioneOrdiniAcqBulk parametriSelezioneOrdiniAcqBulk = (ParametriSelezioneOrdiniAcqBulk)getModel();
            if (parametriSelezioneOrdiniAcqBulk.isIndicatoAlmenoUnCriterioDiSelezione()){
                return cs.ricercaOrdiniAcqCons(actioncontext.getUserContext(), parametriSelezioneOrdiniAcqBulk);
            }
            throw new ApplicationException("E' necessario indicare almeno un criterio di selezione");
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }

    }

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
        return toolbar;
    }

    @Override
    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
        return null;
    }
}
