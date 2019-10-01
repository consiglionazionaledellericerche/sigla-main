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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.missioni00.service.MissioniCMISService;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

import java.util.ArrayList;
import java.util.List;

public class AllegatoMissioneBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	public static final String FLUSSO_ORDINE = "FLUSSO_ORDINE";
	public static final String FLUSSO_RIMBORSO = "FLUSSO_RIMBORSO";

	public static final String FLUSSO_ORDINE_LABEL = "Flusso Ordine di Missione";
	public static final String FLUSSO_RIMBORSO_LABEL = "Flusso Rimborso Missione";

	public static OrderedHashtable aspectNamesKeys = new OrderedHashtable();

	static {
		aspectNamesKeys.put("P:missioni_ordine_attachment:allegati","Allegato all'Ordine di Missione");
		aspectNamesKeys.put("P:missioni_ordine_attachment:allegati_anticipo","Allegato all'Anticipo");
		aspectNamesKeys.put("P:missioni_ordine_attachment:uso_auto_propria","Richiesta Auto Propria");
		aspectNamesKeys.put("P:missioni_ordine_attachment:richiesta_anticipo","Richiesta Anticipo");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:allegati","Allegato al Rimborso Missione");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:scontrini","Giustificativo");
		aspectNamesKeys.put("P:missioni_ordine_attachment:ordine","Ordine Di Missione");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:rimborso","Rimborso Missione");
		aspectNamesKeys.put("P:missioni_rimborso_attachment:allegati_annullamento","Allegato Annullamento Rimborso Missione");
		aspectNamesKeys.put(FLUSSO_ORDINE,FLUSSO_ORDINE_LABEL);
		aspectNamesKeys.put(FLUSSO_RIMBORSO,FLUSSO_RIMBORSO_LABEL);
		aspectNamesKeys.put(MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA,"Allegati vari alla Missione SIGLA");
	}
	private String aspectName;

	public AllegatoMissioneBulk() {
		super();
		setAspectName(MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA);
	}

	public AllegatoMissioneBulk(StorageObject storageObject) {
		super(storageObject.getKey());
		setAspectName(MissioniCMISService.ASPECT_ALLEGATI_MISSIONE_SIGLA);
	}

	public String getAspectName() {
		return aspectName;
	}
	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}
	@StorageProperty(name="cmis:secondaryObjectTypeIds")
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