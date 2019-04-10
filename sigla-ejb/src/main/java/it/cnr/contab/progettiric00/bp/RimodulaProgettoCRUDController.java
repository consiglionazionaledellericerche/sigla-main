package it.cnr.contab.progettiric00.bp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.jsp.JspWriter;

import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SelectionIterator;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.TableCustomizer;

public class RimodulaProgettoCRUDController extends SimpleDetailCRUDController implements TableCustomizer {
	public RimodulaProgettoCRUDController(String s, Class class1, String s1, FormController formcontroller,
			boolean flag) {
		super(s, class1, s1, formcontroller, flag);
	}

	public RimodulaProgettoCRUDController(String s, Class class1, String s1, FormController formcontroller) {
		super(s, class1, s1, formcontroller);
	}

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
                    HttpActionContext.isFromBootstrap(context) ? "fa fa-fw fa-object-group text-primary" : "img/properties16.gif",
                    command,
                    true,
                    "Annulla",
                    "btn-sm btn-secondary btn-outline-secondary btn-title",
                    HttpActionContext.isFromBootstrap(context));
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
                validateForDelete(actioncontext, oggettobulk1);
                removeDetail(oggettobulk1, selection.getFocus());
            }
        } else {
            List list1 = getDetails();
            if (selection.size() > 0) {
                OggettoBulk oggettobulk2;
                for (Iterator iterator1 = selection.iterator(list1); iterator1.hasNext(); validateForDelete(actioncontext, oggettobulk2))
                    oggettobulk2 = (OggettoBulk) iterator1.next();

                int k;
                OggettoBulk oggettobulk3;
                for (SelectionIterator selectioniterator = selection.reverseIterator(); selectioniterator.hasNext(); removeDetail(oggettobulk3, k)) {
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

    public OggettoBulk undoRemoveDetail(OggettoBulk oggettobulk, int i){
    	return removeDetail(i);
    }

	@Override
	public String getRowStyle(Object obj) {
		return null;
	};		

    @Override
	public boolean isRowEnabled(Object obj) {
		return true;
	}

	@Override
	public boolean isRowReadonly(Object obj) {
		return false;
	}

	@Override
	public String getTableClass() {
		return null;
	}

	@Override
	public void writeTfoot(JspWriter jspwriter) throws IOException {
	}
}
