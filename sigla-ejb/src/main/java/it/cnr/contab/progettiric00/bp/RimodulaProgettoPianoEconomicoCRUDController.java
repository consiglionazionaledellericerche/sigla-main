package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.jada.bulk.OggettoBulk;

public class RimodulaProgettoPianoEconomicoCRUDController extends RimodulaProgettoCRUDController {
	public RimodulaProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	@Override
	public String getRowStyle(Object obj) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)obj;
		StringBuffer style = new StringBuffer();
		if (ppe.isDetailRimodulato())
			style.append("font-style:italic;font-weight:bold;");
		if (ppe.isDetailRimodulatoEliminato())
			style.append("text-decoration: line-through;");
		if (ppe.getDispResiduaRimodulato().compareTo(BigDecimal.ZERO)<0)
			style.append("color:red;");
		return Optional.of(style).filter(el->el.length()>0).map(StringBuffer::toString).orElse(null);
	};
	
	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)oggettobulk;
		ppe.setImSpesaFinanziatoRimodulatoPreDelete(ppe.getImSpesaFinanziatoRimodulato());
		ppe.setImSpesaCofinanziatoRimodulatoPreDelete(ppe.getImSpesaCofinanziatoRimodulato());
		ppe.setImSpesaFinanziatoRimodulato(BigDecimal.ZERO);
		ppe.setImSpesaCofinanziatoRimodulato(BigDecimal.ZERO);
		ppe.getVociBilancioAssociate().stream()
			.forEach(ppeVoce->ppeVoce.setDetailRimodulatoEliminato(Boolean.TRUE));
		return ppe;
	};

	@Override
	public OggettoBulk undoRemoveDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)oggettobulk;
		ppe.setImSpesaFinanziatoRimodulato(ppe.getImSpesaFinanziatoRimodulatoPreDelete());
		ppe.setImSpesaCofinanziatoRimodulato(ppe.getImSpesaCofinanziatoRimodulatoPreDelete());
		ppe.getVociBilancioAssociate().stream()
			.forEach(ppeVoce->ppeVoce.setDetailRimodulatoEliminato(Boolean.FALSE));
		return ppe;
	}
}