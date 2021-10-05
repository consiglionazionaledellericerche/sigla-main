/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.bulk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.ordmag.ordini.service.OrdineAcqCMISService;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

import javax.swing.text.html.Option;

public class AllegatoOrdineBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	private OrdineAcqBulk ordine;

	public void setOrdine(OrdineAcqBulk ordine) {
		this.ordine = ordine;
	}

	public static OrderedHashtable aspectNamesKeys = new OrderedHashtable();

	static {
		aspectNamesKeys.put(OrdineAcqCMISService.ASPECT_ALLEGATI_ORDINI,"Allegati all'Ordine");
		aspectNamesKeys.put(OrdineAcqCMISService.ASPECT_STAMPA_ORDINI,"Stampa Ordine");
	}
	private String aspectName;
	
	public AllegatoOrdineBulk() {
		super();
		setAspectName(OrdineAcqCMISService.ASPECT_ALLEGATI_ORDINI);
	}

	public AllegatoOrdineBulk(String storageKey) {
		super(storageKey);
		setAspectName(OrdineAcqCMISService.ASPECT_ALLEGATI_ORDINI);
	}
	
	public String getAspectName() {
		return aspectName;
	}
	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}


	@StoragePolicy(name="P:ordine_acq:stato", property=@StorageProperty(name="ordine_acq:stato"))
	public String getStato(){
		if ( Optional.ofNullable(ordine).map(ordine->ordine.getStato()).filter(s->!(s.isEmpty())).isPresent())
			return OrdineAcqBulk.STATO.get(ordine.getStato()).toString();
		return null;
	}

	@StorageProperty(name="cmis:secondaryObjectTypeIds")
	public List<String> getAspect() {
		 List<String> results = new ArrayList<String>();
		 //results.add("P:cm:titled");
		/*
		Optional.ofNullable(ordine).map(ordine->ordine.getStato()).filter(s->!(s.isEmpty())).ifPresent(stato->{
			results.add( "ciao");
		});
		*/
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
