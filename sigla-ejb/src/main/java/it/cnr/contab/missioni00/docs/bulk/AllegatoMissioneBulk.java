package it.cnr.contab.missioni00.docs.bulk;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.missioni00.service.MissioniCMISService;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoMissioneBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	public static final String FLUSSO_ORDINE = "FLUSSO_ORDINE";
	public static final String FLUSSO_RIMBORSO = "FLUSSO_RIMBORSO";

	public static OrderedHashtable aspectNamesKeys = new OrderedHashtable();

	static {
		aspectNamesKeys.put("P:missioni_ordine_attachment:allegati","Allegato all'Ordine di Missione");
		aspectNamesKeys.put("P:missioni_ordine_attachment:allegati_anticipo","Allegato Anticipo");
		aspectNamesKeys.put("P:missioni_ordine_attachment:uso_auto_propria","Richiesta Auto Propria");
		aspectNamesKeys.put("P:missioni_ordine_attachment:richiesta_anticipo","Richiesta Anticipo");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:allegati","Allegato al Rimborso Missione");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:scontrini","Giustificativo");
		aspectNamesKeys.put("P:missioni_ordine_attachment:ordine","Ordine Di Missione");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:rimborso","Rimborso Missione");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:rimborso","Rimborso Missione");
		aspectNamesKeys.put(FLUSSO_ORDINE,"Flusso Ordine di Missione");
		aspectNamesKeys.put(FLUSSO_RIMBORSO,"Flusso Rimborso Missione");
		aspectNamesKeys.put(MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA,"Allegati vari alla Missione SIGLA");
	}
	private String aspectName;
	
	public AllegatoMissioneBulk() {
		super();
		setAspectName(MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA);
	}

	public AllegatoMissioneBulk(Document node) {
		super(node);
		setAspectName(MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA);
	}
	
	public String getAspectName() {
		return aspectName;
	}
	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}
	@CMISProperty(name=PropertyIds.SECONDARY_OBJECT_TYPE_IDS)
	public List<String> getAspect() {
		 List<String> results = new ArrayList<String>();
		 results.add("P:cm:titled");
		 results.add(getAspectName());
		 return results;
	}

	public static OrderedHashtable getAspectNamesKeys() {
		return aspectNamesKeys;
	}

	@Override
	public void validate() throws ValidationException {
		if (getAspectName() == null) {
			throw new ValidationException("Attenzione: selezionare la tipologia di File!");
		}		
		super.validate();
	}
	public boolean isAllegatoEsistente()
	{
		if(this.isToBeCreated())
			return false;

		return true;
	}
}
