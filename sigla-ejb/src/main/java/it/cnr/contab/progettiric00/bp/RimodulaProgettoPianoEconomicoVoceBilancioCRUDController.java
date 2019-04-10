package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SelectionIterator;
import it.cnr.jada.util.jsp.Button;

public class RimodulaProgettoPianoEconomicoVoceBilancioCRUDController extends SimpleProgettoPianoEconomicoVoceBilancioCRUDController {
	public RimodulaProgettoPianoEconomicoVoceBilancioCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
		if (assVoce.isDetailRimodulatoAggiunto())
			return super.removeDetail(oggettobulk, i);

		if (!assVoce.isDetailRimodulatoEliminato()) {
			assVoce.setImVarFinanziatoRimodulatoPreDelete(assVoce.getImVarFinanziatoRimodulato());
			assVoce.setImVarCofinanziatoRimodulatoPreDelete(assVoce.getImVarCofinanziatoRimodulato());
			assVoce.setImVarFinanziatoRimodulato(BigDecimal.ZERO);
			assVoce.setImVarCofinanziatoRimodulato(BigDecimal.ZERO);
			assVoce.setDetailRimodulatoEliminato(Boolean.TRUE);
		}

		return oggettobulk;
	};
	
	public void add(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
		assVoce.setDetailRimodulatoAggiunto(Boolean.TRUE);
		super.add(actioncontext, oggettobulk);
	};

    @Override
	public String getRowStyle(Object obj) {
		Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)obj;
		StringBuffer style = new StringBuffer();
		if (assVoce.isDetailRimodulato())
			style.append("font-style:italic;font-weight:bold;");
		if (assVoce.isDetailRimodulatoEliminato())
			style.append("text-decoration: line-through;");
		if (assVoce.getSaldoSpesa().getAssestatoFinanziamento()
					.subtract(assVoce.getSaldoSpesa().getImpaccFin())
					.subtract(assVoce.getSaldoSpesa().getSaldoTrasferimentoFinanziamento())
					.add(assVoce.getImVarFinanziatoRimodulato())
					.compareTo(BigDecimal.ZERO)<0 ||
			assVoce.getSaldoSpesa().getAssestatoCofinanziamento()
					.subtract(assVoce.getSaldoSpesa().getImpaccCofin())
					.subtract(assVoce.getSaldoSpesa().getSaldoTrasferimentoCofinanziamento())
					.add(assVoce.getImVarFinanziatoRimodulato())
					.compareTo(BigDecimal.ZERO)<0)
			style.append("color:red;");
		return Optional.of(style).filter(el->el.length()>0).map(StringBuffer::toString).orElse(null);
	};	
	
    @Override
    public void writeHTMLToolbar(
            javax.servlet.jsp.PageContext context,
            boolean reset,
            boolean find,
            boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {
    	Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)getParentController().getModel();
    	boolean isPpeEliminato = Optional.ofNullable(ppe).map(Progetto_piano_economicoBulk::isDetailRimodulatoEliminato).orElse(Boolean.FALSE);
    	if (isPpeEliminato)
    		super.writeHTMLToolbar(context, !isPpeEliminato&&reset, !isPpeEliminato&&find, !isPpeEliminato&&delete, false, true);
    	else {
    		super.writeHTMLToolbar(context, reset, find, delete, false);

    		Button button = new Button();
    		button.setImg("img/undo16.gif");
    		button.setDisabledImg("img/undo16.gif");
    		button.setTitle("Ripristina Selezionati");
    		button.setIconClass("fa fa-fw fa-undo text-primary");
    		button.setButtonClass("btn-sm btn-secondary btn-outline-secondary btn-title");
            button.setHref("javascript:submitForm('doUndoRemoveFromCRUD(" + getInputPrefix() + ")')");
            boolean isButtonEnable = Optional.ofNullable(getParentController().getModel())
											  .filter(Progetto_piano_economicoBulk.class::isInstance)
											  .map(Progetto_piano_economicoBulk.class::cast)
											  .flatMap(el->Optional.ofNullable(el.getVoce_piano_economico()))
											  .map(Voce_piano_economico_prgBulk::getFl_add_vocibil)
											  .orElse(Boolean.FALSE);
            button.writeToolbarButton(context.getOut(), isButtonEnable, HttpActionContext.isFromBootstrap(context));
            
            super.closeButtonGROUPToolbar(context);
    	}
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
		Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
		assVoce.setImVarFinanziatoRimodulato(Optional.ofNullable(assVoce.getImVarFinanziatoRimodulatoPreDelete()).orElse(BigDecimal.ZERO));
		assVoce.setImVarCofinanziatoRimodulato(Optional.ofNullable(assVoce.getImVarCofinanziatoRimodulatoPreDelete()).orElse(BigDecimal.ZERO));
		assVoce.setDetailRimodulatoEliminato(Boolean.FALSE);
		return oggettobulk;
	}
}
