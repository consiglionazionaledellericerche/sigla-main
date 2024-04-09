package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.OrderConstants;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

public class SelezionatoreFattureElettronicheLiquidazioneBP extends SelezionatoreListaBP  implements SearchProvider {

    @Override
    protected void init(Config config, ActionContext context)
            throws BusinessProcessException {
        try {
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(DocumentoEleTestataBulk.class));
            setMultiSelection(true);
            DocumentoEleTestataBulk model = (DocumentoEleTestataBulk) getBulkInfo().getBulkClass().newInstance();
            setModel(context, model);
            super.init(config, context);
            openIterator(context);
            setOrderBy(context,"documentoEleTrasmissione.dataRicezione", OrderConstants.ORDER_DESC);
            reset(context);
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

    protected FatturaElettronicaPassivaComponentSession getComponentSession() {
        return (FatturaElettronicaPassivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession");
    }
    @Override
    public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
        DocumentoEleTestataBulk documentoEleTestataBulk = (DocumentoEleTestataBulk) oggettobulk;
        try {
            return getComponentSession().cerca(
                    actioncontext.getUserContext(),
                    compoundfindclause,
                    documentoEleTestataBulk,
                    "selectFattureAggiornateComplete");
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }
}
