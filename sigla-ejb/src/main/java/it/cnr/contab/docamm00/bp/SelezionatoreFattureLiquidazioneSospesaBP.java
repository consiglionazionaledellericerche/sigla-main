package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;

public class SelezionatoreFattureLiquidazioneSospesaBP extends SelezionatoreListaBP  implements SearchProvider {

    @Override
    protected void init(Config config, ActionContext context)
            throws BusinessProcessException {
        try {
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fattura_passiva_IBulk.class));
            setMultiSelection(true);
            Fattura_passiva_IBulk model = (Fattura_passiva_IBulk) getBulkInfo().getBulkClass().newInstance();
            model.setStato_liquidazione(IDocumentoAmministrativoBulk.SOSP);
            setModel(context, model);
            setColumns(getBulkInfo().getColumnFieldPropertyDictionary("liquidazione"));
            super.init(config, context);
            openIterator(context);
        } catch (InstantiationException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        }
    }
    public void openIterator(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            setIterator(actioncontext, search(
                    actioncontext,
                    Optional.ofNullable(getCondizioneCorrente())
                            .map(CondizioneComplessaBulk::creaFindClause)
                            .filter(CompoundFindClause.class::isInstance)
                            .map(CompoundFindClause.class::cast)
                            .orElseGet(() -> new CompoundFindClause()),
                    getModel())
            );
        } catch (RemoteException e) {
            throw new BusinessProcessException(e);
        }
    }

    @Override
    public Button[] createToolbar() {
        final Properties properties = it.cnr.jada.util.Config.getHandler().getProperties(getClass());
        return Stream.concat(Arrays.asList(super.createToolbar()).stream(),
                Arrays.asList(
                        new Button(properties, "Toolbar.provvedimento")
                ).stream()).toArray(Button[]::new);
    }

    protected FatturaPassivaComponentSession getComponentSession() {
        return (FatturaPassivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession");
    }
    @Override
    public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
        Fattura_passiva_IBulk fattura = (Fattura_passiva_IBulk) oggettobulk;
        try {
            return getComponentSession().cerca(
                    actioncontext.getUserContext(),
                    compoundfindclause,
                    fattura,
                    "selectFattureNonPagate");
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }
}
