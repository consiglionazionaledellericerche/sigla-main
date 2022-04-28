/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.util.action.SelectionIterator;
import it.cnr.jada.util.jsp.Button;

public class RimodulaProgettoPianoEconomicoCRUDController extends SimpleProgettoPianoEconomicoCRUDController {
	public RimodulaProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	@Override
	public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
		((Progetto_piano_economicoBulk)oggettobulk).setEsercizio_piano(((Progetto_rimodulazioneBulk)this.getParentModel()).getProgetto().getEsercizio());
		((Progetto_piano_economicoBulk)oggettobulk).setIm_entrata(BigDecimal.ZERO);
		((Progetto_piano_economicoBulk)oggettobulk).setIm_spesa_finanziato(BigDecimal.ZERO);
		((Progetto_piano_economicoBulk)oggettobulk).setIm_spesa_cofinanziato(BigDecimal.ZERO);
		((Progetto_piano_economicoBulk)oggettobulk).setImSpesaFinanziatoRimodulato(BigDecimal.ZERO);
		((Progetto_piano_economicoBulk)oggettobulk).setImSpesaCofinanziatoRimodulato(BigDecimal.ZERO);
		return super.addDetail(oggettobulk);
	};	
	
	@Override
	public boolean isShrinkable() {
		Progetto_rimodulazioneBulk obj = (Progetto_rimodulazioneBulk)this.getParentModel();
		return super.isShrinkable() && !obj.isROFieldRimodulazione();
	}
	
	@Override
	public boolean isGrowable() {
		Progetto_rimodulazioneBulk obj = (Progetto_rimodulazioneBulk)this.getParentModel();
		return super.isGrowable() && !obj.isROFieldRimodulazione();
	}
	
	@Override
	public String getRowStyle(Object obj) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)obj;
		StringBuffer style = new StringBuffer();
		if (ppe.isDetailRimodulato() || (ppe.isToBeCreated() && !ppe.isDetailDerivato()))
			style.append("font-style:italic;font-weight:bold;");
		if (ppe.isDetailRimodulatoEliminato())
			style.append("text-decoration: line-through;");
		if (Optional.ofNullable(ppe.getMessageAnomaliaDetailRimodulato()).isPresent())
			style.append("color:red!important;");
		return Optional.of(style).filter(el->el.length()>0).map(StringBuffer::toString).orElse(null);
	};
	
	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)oggettobulk;
		if (!ppe.isDetailDerivato())
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

		Button button = new Button();
		button.setImg("img/undo16.gif");
		button.setDisabledImg("img/undo16.gif");
		button.setTitle("Annulla Eliminazione");
		button.setIconClass("fa fa-fw fa-undo text-primary");
		button.setButtonClass("btn-sm btn-outline-secondary btn-title");
        button.setHref("javascript:submitForm('doUndoRemoveFromCRUD(" + getInputPrefix() + ")')");
        boolean isButtonEnable = isShrinkable();
        button.writeToolbarButton(context.getOut(), isButtonEnable, HttpActionContext.isFromBootstrap(context));
        
        super.closeButtonGROUPToolbar(context);
    }
    
    public void undoRemove(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        basicSetSelection(actioncontext);
        if (paged) {
            List list = getDetailsPage();
            BitSet bitset = selection.getSelection(getCurrentPage() * getPageSize(), getPageSize());
            if (bitset.length() == 0) {
                for (int i = 0; i < getPageSize(); i++)
                    if (bitset.get(i))
                        validateForUndoRemoveDetail(actioncontext, (OggettoBulk) list.get(i));

                for (int j = getPageSize() - 1; j > 0; j--)
                    if (bitset.get(j))
                        undoRemoveDetail((OggettoBulk) list.get(j), j);

            } else if (selection.getFocus() >= 0) {
                OggettoBulk oggettobulk1 = getDetail(selection.getFocus());
                validateForUndoRemoveDetail(actioncontext, oggettobulk1);
                undoRemoveDetail(oggettobulk1, selection.getFocus());
            }
        } else {
            List list1 = getDetails();
            if (selection.size() > 0) {
                OggettoBulk oggettobulk2;
                for (Iterator iterator1 = selection.iterator(list1); iterator1.hasNext(); validateForUndoRemoveDetail(actioncontext, oggettobulk2))
                    oggettobulk2 = (OggettoBulk) iterator1.next();

                int k;
                OggettoBulk oggettobulk3;
                for (SelectionIterator selectioniterator = selection.reverseIterator(); selectioniterator.hasNext(); undoRemoveDetail(oggettobulk3, k)) {
                    k = selectioniterator.nextIndex();
                    oggettobulk3 = (OggettoBulk) list1.get(k);
                }

            } else if (selection.getFocus() >= 0) {
                OggettoBulk oggettobulk = getDetail(selection.getFocus());
                validateForUndoRemoveDetail(actioncontext, oggettobulk);
                undoRemoveDetail(oggettobulk, selection.getFocus());
            }
        }
        getParentController().setDirty(true);
        reset(actioncontext);
    }

    public void validateForUndoRemoveDetail(ActionContext actioncontext, OggettoBulk oggettobulk)
            throws ValidationException {
    }

    public OggettoBulk undoRemoveDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)oggettobulk;
		Progetto_rimodulazioneBulk rim = (Progetto_rimodulazioneBulk)this.getParentModel();

		if (ppe.isDetailRimodulatoEliminato()) {
			//Per riattiavrlo devo essere sicuro che le date siano coerenti
			if (ppe.getEsercizio_piano().compareTo(rim.getAnnoInizioRimodulato())<0)
				throw new ApplicationRuntimeException("Per l'anno " + ppe.getEsercizio_piano() + " non è possibile riattivare dettagli di piano economico " +
						" perchè precedente alla data di inizio del progetto. Modificare la data di inizio e ripetere l'operazione!");

			if (ppe.getEsercizio_piano().compareTo(rim.getAnnoFineRimodulato())>0)
				throw new ApplicationRuntimeException("Per l'anno " + ppe.getEsercizio_piano() + " non è possibile riattivare dettagli di piano economico " +
						" perchè successivo alla data di fine/proroga del progetto. Modificare la data di fine e ripetere l'operazione!");

			ppe.setImSpesaFinanziatoRimodulato(Optional.ofNullable(ppe.getImSpesaFinanziatoRimodulatoPreDelete()).orElse(ppe.getIm_spesa_finanziato()));
			ppe.setImSpesaCofinanziatoRimodulato(Optional.ofNullable(ppe.getImSpesaCofinanziatoRimodulatoPreDelete()).orElse(ppe.getIm_spesa_cofinanziato()));
		}
		ppe.getVociBilancioAssociate().stream()
		   .filter(Ass_progetto_piaeco_voceBulk::isDetailRimodulatoEliminato)
	  	   .forEach(ppeVoce->ppeVoce.setDetailRimodulatoEliminato(Boolean.FALSE));
		return ppe;
	}
}