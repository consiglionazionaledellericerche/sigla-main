package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SelectionIterator;

public class RimodulaProgettoPianoEconomicoCRUDController extends SimpleProgettoPianoEconomicoCRUDController {
	public RimodulaProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	@Override
	public String getRowStyle(Object obj) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)obj;
		StringBuffer style = new StringBuffer();
		if (ppe.isDetailRimodulato() || ppe.isToBeCreated())
			style.append("font-style:italic;font-weight:bold;");
		if (ppe.isDetailRimodulatoEliminato())
			style.append("text-decoration: line-through;");
		if (ppe.getDispResiduaRimodulato().compareTo(BigDecimal.ZERO)<0)
			style.append("color:red;");
		return Optional.of(style).filter(el->el.length()>0).map(StringBuffer::toString).orElse(null);
	};
	
	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)oggettobulk;
		if (ppe.isToBeCreated())
			return super.removeDetail(oggettobulk, i);
		if (!ppe.isDetailRimodulatoEliminato()) {
			ppe.setImSpesaFinanziatoRimodulatoPreDelete(ppe.getImSpesaFinanziatoRimodulato());
			ppe.setImSpesaCofinanziatoRimodulatoPreDelete(ppe.getImSpesaCofinanziatoRimodulato());
			ppe.setImSpesaFinanziatoRimodulato(BigDecimal.ZERO);
			ppe.setImSpesaCofinanziatoRimodulato(BigDecimal.ZERO);
		}
		ppe.getVociBilancioAssociate().stream()
		   .filter(el->!el.isDetailRimodulatoEliminato())
		   .forEach(ppeVoce->{
			   ppeVoce.setImVarFinanziatoRimodulatoPreDelete(ppeVoce.getImVarFinanziatoRimodulato());
			   ppeVoce.setImVarCofinanziatoRimodulatoPreDelete(ppeVoce.getImVarCofinanziatoRimodulato());
			   ppeVoce.setImVarFinanziatoRimodulato(BigDecimal.ZERO);
			   ppeVoce.setImVarCofinanziatoRimodulato(BigDecimal.ZERO);
			   ppeVoce.setDetailRimodulatoEliminato(Boolean.TRUE);
		   });
		return ppe;
	};

    @Override
    public void writeHTMLToolbar(
            javax.servlet.jsp.PageContext context,
            boolean reset,
            boolean find,
            boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

        super.writeHTMLToolbar(context, reset, find, delete, false);

        String command = "javascript:submitForm('doUndoRemoveFromCRUD(" + getInputPrefix() + ")')";
        it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                    context,
                    HttpActionContext.isFromBootstrap(context) ? "fa fa-fw fa-undo text-primary" : "img/undo16.gif",
                    command,
                    true,
                    "Ripristina Selezionati",
                    "btn-sm btn-secondary btn-outline-secondary btn-title",
                    HttpActionContext.isFromBootstrap(context));
        super.closeButtonGROUPToolbar(context);
    }
    
    public void undoRemove(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        basicSetSelection(actioncontext);
        if (paged) {
            List list = getDetailsPage();
            BitSet bitset = selection.getSelection(getCurrentPage() * getPageSize(), getPageSize());
            if (bitset.length() == 0) {
                for (int j = getPageSize() - 1; j > 0; j--)
                    if (bitset.get(j))
                        undoRemoveDetail((OggettoBulk) list.get(j), j);

            } else if (selection.getFocus() >= 0) {
                OggettoBulk oggettobulk1 = getDetail(selection.getFocus());
                undoRemoveDetail(oggettobulk1, selection.getFocus());
            }
        } else {
            List list1 = getDetails();
            if (selection.size() > 0) {
                int k;
                OggettoBulk oggettobulk3;
                for (SelectionIterator selectioniterator = selection.reverseIterator(); selectioniterator.hasNext(); undoRemoveDetail(oggettobulk3, k)) {
                    k = selectioniterator.nextIndex();
                    oggettobulk3 = (OggettoBulk) list1.get(k);
                }

            } else if (selection.getFocus() >= 0) {
                OggettoBulk oggettobulk = getDetail(selection.getFocus());
                undoRemoveDetail(oggettobulk, selection.getFocus());
            }
        }
        getParentController().setDirty(true);
        reset(actioncontext);
    }

	public OggettoBulk undoRemoveDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)oggettobulk;
		if (ppe.isDetailRimodulatoEliminato()) {
			ppe.setImSpesaFinanziatoRimodulato(Optional.ofNullable(ppe.getImSpesaFinanziatoRimodulatoPreDelete()).orElse(ppe.getIm_spesa_finanziato()));
			ppe.setImSpesaCofinanziatoRimodulato(Optional.ofNullable(ppe.getImSpesaCofinanziatoRimodulatoPreDelete()).orElse(ppe.getIm_spesa_cofinanziato()));
		}
		ppe.getVociBilancioAssociate().stream()
		   .filter(Ass_progetto_piaeco_voceBulk::isDetailRimodulatoEliminato)
	  	   .forEach(ppeVoce->ppeVoce.setDetailRimodulatoEliminato(Boolean.FALSE));
		return ppe;
	}
}