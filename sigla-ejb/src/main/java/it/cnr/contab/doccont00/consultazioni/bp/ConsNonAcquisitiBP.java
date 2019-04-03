package it.cnr.contab.doccont00.consultazioni.bp;

import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_stato_invio_mandatiBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_stato_invio_mandatiHome;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_stato_invio_reversaliBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConsNonAcquisitiBP extends SelezionatoreListaBP implements SearchProvider {

    public ConsNonAcquisitiBP() {
    }

    @Override
    protected void init(Config config, ActionContext context)
            throws BusinessProcessException {
        try {
            setMultiSelection(true);
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Class.forName(config.getInitParameter("bulkClassName"))));
            setColumns(getBulkInfo().getColumnFieldPropertyDictionary(config.getInitParameter("searchResultColumnSet")));
            OggettoBulk model = (OggettoBulk) getBulkInfo().getBulkClass().newInstance();
            setModel(context, model);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw handleException(e);
        }
        super.init(config, context);
        openIterator(context);
    }

    public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException, BusinessProcessException {
        return (it.cnr.jada.ejb.CRUDComponentSession) createComponentSession("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class);
    }


    public void openIterator(ActionContext actioncontext) throws BusinessProcessException {
        try {
            setIterator(actioncontext,
                    search(actioncontext, null, getModel()));
        } catch (RemoteException e) {
            throw new BusinessProcessException(e);
        }
    }

    @Override
    public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
        final CompoundFindClause compoundFindClause = Optional.ofNullable(getCondizioneCorrente())
                .map(CondizioneComplessaBulk::creaFindClause)
                .filter(CompoundFindClause.class::isInstance)
                .map(CompoundFindClause.class::cast)
                .orElseGet(() -> new CompoundFindClause());
        try {
            return createComponentSession().cerca(actioncontext.getUserContext(),
                    compoundFindClause,
                    getModel(), "selectByClauseForNonAcqisiti");
        } catch (RemoteException | ComponentException e) {
            throw new BusinessProcessException(e);
        }
    }

    @Override
    public List<Button> createToolbarList() {
        final List<Button> toolbarList = super.createToolbarList();
        final Button button = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        button.setTitle(
                Optional.ofNullable(getModel())
                    .filter(V_cons_stato_invio_mandatiBulk.class::isInstance)
                    .map(oggettoBulk -> "Annulla Mandati")
                    .orElse("Annulla Reversali")
        );
        toolbarList.add(button);
        return toolbarList;
    }

    public void annulla(ActionContext actioncontext) throws BusinessProcessException {
        final List<?> selectedElements = getSelectedElements(actioncontext);
        try {
            if (selectedElements.isEmpty())
                throw new ApplicationException("Nessun elemento selezionato!");
            boolean isMandati =  Optional.ofNullable(getModel())
                    .filter(V_cons_stato_invio_mandatiBulk.class::isInstance)
                    .isPresent();
            if (isMandati) {
                final MandatoComponentSession mandatoComponentSession = Utility.createMandatoComponentSession();
                final List<MandatoBulk> mandatiDaAnnullare = selectedElements.stream()
                        .filter(V_cons_stato_invio_mandatiBulk.class::isInstance)
                        .map(V_cons_stato_invio_mandatiBulk.class::cast)
                        .map(v_cons_stato_invio_mandatiBulk -> {
                            return new MandatoIBulk(
                                    v_cons_stato_invio_mandatiBulk.getCd_cds(),
                                    v_cons_stato_invio_mandatiBulk.getEsercizio().intValue(),
                                    v_cons_stato_invio_mandatiBulk.getPg_mandato()
                            );
                        })
                        .map(mandatoIBulk -> {
                            try {
                                return mandatoComponentSession.inizializzaBulkPerModifica(
                                            actioncontext.getUserContext(),
                                            mandatoIBulk);
                            } catch (ComponentException | RemoteException e) {
                                return null;
                            }
                        })
                        .filter(persistent -> Optional.ofNullable(persistent).isPresent())
                        .filter(MandatoBulk.class::isInstance)
                        .map(MandatoBulk.class::cast)
                        .collect(Collectors.toList());
                for(MandatoBulk mandato : mandatiDaAnnullare) {
                    mandatoComponentSession.annullaMandato(actioncontext.getUserContext(), mandato);
                }
            } else {
                final ReversaleComponentSession reversaleComponentSession = Utility.createReversaleComponentSession();
                final List<ReversaleBulk> reversaliDaAnnullare = selectedElements.stream()
                        .filter(V_cons_stato_invio_reversaliBulk.class::isInstance)
                        .map(V_cons_stato_invio_reversaliBulk.class::cast)
                        .map(V_cons_stato_invio_reversaliBulk -> {
                            return new ReversaleIBulk(
                                    V_cons_stato_invio_reversaliBulk.getCd_cds(),
                                    V_cons_stato_invio_reversaliBulk.getEsercizio().intValue(),
                                    V_cons_stato_invio_reversaliBulk.getPg_reversale()
                            );
                        })
                        .map(reversaleIBulk -> {
                            try {
                                return reversaleComponentSession.findByPrimaryKey(actioncontext.getUserContext(), reversaleIBulk);
                            } catch (ComponentException | RemoteException e) {
                                return null;
                            }
                        })
                        .filter(persistent -> Optional.ofNullable(persistent).isPresent())
                        .filter(ReversaleBulk.class::isInstance)
                        .map(ReversaleBulk.class::cast)
                        .collect(Collectors.toList());
                for(ReversaleBulk reversale : reversaliDaAnnullare) {
                    reversaleComponentSession.annullaReversale(actioncontext.getUserContext(), reversale);
                }
            }
        } catch (ApplicationException e) {
            setMessage(e.getMessage());
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }
}
