package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

public class CRUDScadenzeController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
    public CRUDScadenzeController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    /**
     * Metodo con cui si verifica la validità di alcuni campi, mediante un
     * controllo sintattico o contestuale.
     *
     * @param context  Il contesto dell'azione
     * @param scadenza La scadenza dell'accertamento
     */
    public void validateForDelete(ActionContext context, Accertamento_scadenzarioBulk scadenza) throws ValidationException {
        if (scadenza.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0)) != 0)
            throw new ValidationException("Impossibile cancellare una scadenza che e' stata associata a documenti amministrativi");

    }

    /**
     * Metodo con cui si verifica la validità di alcuni campi, mediante un
     * controllo sintattico o contestuale.
     *
     * @param context  Il contesto dell'azione
     * @param scadenza La scadenza dell'obbligazione
     */
    public void validateForDelete(ActionContext context, Obbligazione_scadenzarioBulk scadenza) throws ValidationException {
        if (scadenza.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0)) != 0)
            throw new ValidationException("Impossibile cancellare una scadenza che e' stata associata a documenti amministrativi");

    }

    /**
     * Metodo con cui si verifica la validità di alcuni campi, mediante un
     * controllo sintattico o contestuale.
     *
     * @param context  Il contesto dell'azione
     * @param scadenza La scadenza dell'oggetto bulk in uso
     */
    public void validateForDelete(ActionContext context, OggettoBulk scadenza) throws ValidationException {
        if (scadenza instanceof Obbligazione_scadenzarioBulk)
            validateForDelete(context, (Obbligazione_scadenzarioBulk) scadenza);
        else if (scadenza instanceof Accertamento_scadenzarioBulk)
            validateForDelete(context, (Accertamento_scadenzarioBulk) scadenza);
    }

    /**
     * Metodo per aggiungere alla toolbar del Controller un tasto necessario per accorpare
     * le righe di scadenza dell'obbligazione.
     *
     * @param context  Il contesto dell'azione
     * @param scadenza La scadenza dell'oggetto bulk in uso
     */
    @Override
    public void writeHTMLToolbar(
            javax.servlet.jsp.PageContext context,
            boolean reset,
            boolean find,
            boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

        super.writeHTMLToolbar(context, reset, find, delete, false);

        if (getParentController() instanceof CRUDObbligazioneBP) {
            String command = "javascript:submitForm('doRaggruppaScadenze')";
            it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                    context,
                    HttpActionContext.isFromBootstrap(context) ? "fa fa-fw fa-object-group text-primary" : "img/properties16.gif",
                    !(isInputReadonly() || getDetails().isEmpty() || ((CRUDObbligazioneBP) getParentController()).isSearching()) ? command : null,
                    true,
                    "Raggruppa",
                    "btn-sm btn-secondary btn-outline-secondary btn-title",
                    HttpActionContext.isFromBootstrap(context));
        }
        super.closeButtonGROUPToolbar(context);
    }
}
