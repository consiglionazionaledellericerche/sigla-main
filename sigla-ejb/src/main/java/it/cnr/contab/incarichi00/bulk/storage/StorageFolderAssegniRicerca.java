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

package it.cnr.contab.incarichi00.bulk.storage;

import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.util.Utility;

import java.util.List;
import java.util.Optional;

@StorageType(name="F:sigla_contratti:assegni_ricerca")
public class StorageFolderAssegniRicerca extends StorageFolderContrattiModel {
	private static final long serialVersionUID = 1L;

	public StorageFolderAssegniRicerca(Incarichi_repertorioBulk incaricoRepertorio) {
    	super(incaricoRepertorio);
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@StorageProperty(name="sigla_contratti_aspect_assegni_ricerca:esercizio"))
	public Integer getEsercizio() {
		return super.getEsercizio();
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@StorageProperty(name="sigla_contratti_aspect_assegni_ricerca:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
	public Long getPg_repertorio() {
		return super.getPg_repertorio();
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:esercizio"))
    public Integer getEsercizioIncarico() {
		return super.getEsercizio();
    }

	@StoragePolicy(name="P:sigla_contratti_aspect:incarichi", property=@StorageProperty(name="sigla_contratti_aspect_incarichi:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
    public Long getPgIncarico() {
		return super.getPg_repertorio();
    }

	public String getCMISPath() {
		return SpringUtil.getBean("storeService", StoreService.class)
				.createFolderIfNotPresent(
						getCMISParentPath(),
						"Assegno di Ricerca "+this.getEsercizio().toString()+Utility.lpad(this.getPg_repertorio().toString(),10,'0'),
						null, null, this);
	}
	
	public boolean isEqualsTo(StorageObject node, List<String> listError){
		boolean isEquals = super.isEqualsTo(node, listError);
		String initTesto = "Procedura "+this.getEsercizio_procedura().toString()+"/"+this.getPg_procedura().toString()+" - "+
						   "Incarico "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString()+" - Disallineamento dato ";
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizio());
		valueCMIS=String.valueOf(node.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ASSEGNI_RICERCA_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Assegni Ricerca - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPg_repertorio());
		valueCMIS=String.valueOf(node.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_ASSEGNI_RICERCA_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_repertorio Assegni Ricerca - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		return isEquals;
	}
}
