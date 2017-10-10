package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (10/09/2017 11:32:54 AM)
 *
 * @author: Marco Spasiano
 */
public class OrdiniCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
    /**
     * OrdiniCRUDController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     */
    public OrdiniCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    public java.util.List getDetails() {
        final Optional<Fattura_passiva_rigaBulk> fattura_passiva_rigaBulk = Optional.ofNullable(getParentController())
                .filter(CRUDFatturaPassivaBP.class::isInstance)
                .map(CRUDFatturaPassivaBP.class::cast)
                .filter(crudFatturaPassivaBP -> crudFatturaPassivaBP.getDettaglio().getSelection().getFocus() != -1)
                .map(crudFatturaPassivaBP -> crudFatturaPassivaBP.getDettaglio().getDetails().get(
                        crudFatturaPassivaBP.getDettaglio().getSelection().getFocus())
                )
                .filter(Fattura_passiva_rigaBulk.class::isInstance)
                .map(Fattura_passiva_rigaBulk.class::cast);
        if (fattura_passiva_rigaBulk.isPresent()) {
            return Optional.ofNullable(getParentModel())
                    .filter(Fattura_passivaBulk.class::isInstance)
                    .map(Fattura_passivaBulk.class::cast)
                    .map(fattura_passivaBulk -> fattura_passivaBulk.getFatturaRigaOrdiniHash())
                    .map(fatturaRigaOrdiniTable -> fatturaRigaOrdiniTable.get(fattura_passiva_rigaBulk.get()))
                    .orElseGet(() -> Collections.emptyList());
        }
        return Collections.emptyList();
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isGrowable() {
        return false;
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isShrinkable() {
        return	super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching();
    }

}
