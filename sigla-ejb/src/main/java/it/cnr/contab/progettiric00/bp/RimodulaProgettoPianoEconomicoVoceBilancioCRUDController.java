package it.cnr.contab.progettiric00.bp;

import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

public class RimodulaProgettoPianoEconomicoVoceBilancioCRUDController extends RimodulaProgettoCRUDController {
	public RimodulaProgettoPianoEconomicoVoceBilancioCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
		assVoce.setDetailRimodulatoEliminato(Boolean.TRUE);
		return oggettobulk;
	};
	
	@Override
	public OggettoBulk undoRemoveDetail(OggettoBulk oggettobulk, int i) {
		Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
		assVoce.setDetailRimodulatoEliminato(Boolean.FALSE);
		return oggettobulk;
	}

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
		return Optional.of(style).filter(el->el.length()>0).map(StringBuffer::toString).orElse(null);
	};	
}
