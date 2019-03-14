package it.cnr.contab.pdg00.bp;

import it.cnr.jada.util.jsp.Button;

/**
 * Insert the type's description here.
 * Creation date: (25/03/2004 10.12.49)
 *
 * @author: Gennaro Borriello
 */
public class StampaRendFinanziarioCNRperCdsBP extends StampaRendFinanziarioCNRBP {
    /**
     * StampaRendFinanziarioCNRperCdsBP constructor comment.
     */
    public StampaRendFinanziarioCNRperCdsBP() {
        super();
    }

    /**
     * StampaRendFinanziarioCNRperCdsBP constructor comment.
     *
     * @param function java.lang.String
     */
    public StampaRendFinanziarioCNRperCdsBP(String function) {
        super(function);
    }

    /**
     * Inizializza la schermata presentando in primo piano il Tab dell'esercizio dell'anno corrente
     *
     * @param context {@link it.cnr.jada.action.ActionContext } in uso.
     * @throws it.cnr.jada.action.BusinessProcessException
     */
    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        super.init(config, context);
        it.cnr.contab.pdg00.bulk.StampaRendFinCNRVBulk model = (it.cnr.contab.pdg00.bulk.StampaRendFinCNRVBulk) getModel();
        model.setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk());
        setModel(context, model);
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = new Button[1];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.close");
        return toolbar;
    }
}
