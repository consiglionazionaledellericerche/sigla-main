package it.cnr.contab.utenze00.bp;

/**
 * Business Process che gestisce l'attività di Gestione Ruolo: in particolare gestisce
 * i due dettagli relativi agli Accessi gia' assegnati al Ruolo e agli Accessi ancora disponibili
 */


import it.cnr.contab.utente00.ejb.RuoloComponentSession;
import it.cnr.contab.utenze00.bulk.AccessoBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.RuoloBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.rmi.RemoteException;

public class CRUDRuoloBP extends SimpleCRUDBP {
    private CompoundFindClause compoundfindclauseAccessiDisponibili = null;
    private final SimpleDetailCRUDController crudAccessi_disponibili = new SimpleDetailCRUDController("Accessi_disponibili", AccessoBulk.class, "accessi_disponibili", this){
        public void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause) {
            compoundfindclauseAccessiDisponibili = compoundfindclause;
            CRUDRuoloBP bp = (CRUDRuoloBP) actioncontext.getBusinessProcess();
            RuoloBulk ruolo = (RuoloBulk) bp.getModel();
            ruolo.resetAccessi();
            try {
                bp.setModel(actioncontext, ((RuoloComponentSession) createComponentSession()).
                        cercaAccessiDisponibili(actioncontext.getUserContext(), ruolo, compoundfindclause));

            } catch (BusinessProcessException e) {
                handleException(e);
            } catch (ComponentException e) {
                handleException(e);
            } catch (RemoteException e) {
                handleException(e);
            }
            super.setFilter(actioncontext, compoundfindclause);
        };

        public boolean isFiltered() {
            return compoundfindclauseAccessiDisponibili != null;
        };
    };
    private final SimpleDetailCRUDController crudAccessi = new SimpleDetailCRUDController("Accessi", AccessoBulk.class, "accessi", this);

    public CRUDRuoloBP() throws BusinessProcessException {
        super();

    }

    public CRUDRuoloBP(String function) throws BusinessProcessException {
        super(function);

    }

    @Override
    public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        compoundfindclauseAccessiDisponibili = null;
        return super.initializeModelForInsert(actioncontext, oggettobulk);
    }

    @Override
    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        compoundfindclauseAccessiDisponibili = null;
        return super.initializeModelForEdit(actioncontext, oggettobulk);
    }

    @Override
    public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        compoundfindclauseAccessiDisponibili = null;
        return super.initializeModelForSearch(actioncontext, oggettobulk);
    }

    public void edit(it.cnr.jada.action.ActionContext context, OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {
        super.edit(context, bulk);
        RuoloBulk ruolo = (RuoloBulk) getModel();
        CNRUserInfo userInfo = (CNRUserInfo) context.getUserInfo();
        if (!"*".equals(userInfo.getUtente().getCd_cds_configuratore()) &&
                ((RuoloBulk) getModel()).getCds() == null)
            setStatus(VIEW);
    }

    /**
     * Restituisce il Controller che gestisce il dettaglio degli Accessi già assegnati ad un Ruolo
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudAccessi() {
        return crudAccessi;
    }

    /**
     * Restituisce il Controller che gestisce il dettaglio degli Accessi ancora disponibile per un Ruolo
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudAccessi_disponibili() {
        return crudAccessi_disponibili;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'cdsFieldEnabled'
     *
     * @return Il valore della proprietà 'cdsFieldEnabled'
     */
    public boolean isCdsFieldEnabled() {
        return isInserting() && isEditable() && ((RuoloBulk) getModel()).getGestore() != null &&
                "*".equals(((RuoloBulk) getModel()).getGestore().getCd_cds_configuratore());
    }
}
