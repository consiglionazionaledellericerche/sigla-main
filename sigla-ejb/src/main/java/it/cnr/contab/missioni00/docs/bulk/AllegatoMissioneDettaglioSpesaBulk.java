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

import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;

import java.util.ArrayList;
import java.util.List;

@StorageType(name="cmis:document")
public class AllegatoMissioneDettaglioSpesaBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	private String aspectName;
	
	private String nodeRefDettaglio;
	
	private Boolean isDetailAdded = false;
	
	public AllegatoMissioneDettaglioSpesaBulk() {
		super();
	}

	public AllegatoMissioneDettaglioSpesaBulk(StorageObject storageObject) {
		super(storageObject.getKey());
		setNodeRefDettaglio(storageObject.getKey());
	}

	@StorageProperty(name="cmis:secondaryObjectTypeIds")
	public List<String> getAspect() {
		List<String> results = new ArrayList<String>();
		results.add("P:cm:titled");
		if (getAspectName() != null){
			results.add(getAspectName());
		}
		return results;
	}

	public boolean isAllegatoEsistente()
	{
		return !getIsDetailAdded();
	}

	public String getAspectName() {
		return aspectName;
	}

	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}

	public Boolean getIsDetailAdded() {
		return isDetailAdded;
	}

	public void setIsDetailAdded(Boolean isDetailAdded) {
		this.isDetailAdded = isDetailAdded;
	}

	public String getNodeRefDettaglio() {
		return nodeRefDettaglio;
	}

	public void setNodeRefDettaglio(String nodeRefDettaglio) {
		this.nodeRefDettaglio = nodeRefDettaglio;
	}
}
