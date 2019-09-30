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

package it.cnr.contab.util00.bulk.storage;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

import it.cnr.contab.util00.bulk.storage.enumeration.AllegatoGenericoType;

public class AllegatoGenericoTypeBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;
	private String objectType;
	
    public final static Map<String,String> ti_allegatoKeys = Arrays.asList(AllegatoGenericoType.values())
            .stream()
            .collect(Collectors.toMap(
            		AllegatoGenericoType::value,
            		AllegatoGenericoType::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
    
	public AllegatoGenericoTypeBulk() {
		super();
	}

	public static AllegatoGenericoTypeBulk construct(String storageKey){
		return new AllegatoGenericoTypeBulk(storageKey);
	}
	
	public AllegatoGenericoTypeBulk(String storageKey) {
		super(storageKey);
	}

	public String getObjectType() {
		return objectType;
	}
	
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
}
