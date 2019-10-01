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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class FunzioneBulk extends FunzioneBase {
public FunzioneBulk() {
	super();
}
public FunzioneBulk(java.lang.String cd_funzione) {
	super(cd_funzione);
}
	public boolean equals(Object obj) {
	 if(obj instanceof FunzioneBulk) {
	  FunzioneBulk aFB = (FunzioneBulk)obj;
	  if(( getCd_funzione() == null ) && ( aFB.getCd_funzione() == null)) return true;
	  if(   ((getCd_funzione() != null) && (aFB.getCd_funzione() == null))
		 || ((getCd_funzione() == null) && (aFB.getCd_funzione() != null))
		) return false;
	  
	  return ((FunzioneBulk)obj).getCd_funzione().equals( getCd_funzione() );
	 }
	 return super.equals(obj);
	}
/**
 * Restituisce il valore della proprietà 'cd_ds_funzione'
 *
 * @return Il valore della proprietà 'cd_ds_funzione'
 */
public String getCd_ds_funzione() {
	return getCd_funzione() + " - " + getDs_funzione();
}
}
