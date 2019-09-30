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

package it.cnr.contab.config00.bulk;

import java.util.Dictionary;

import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Creation date: (24/02/2005)
 * @author Tilde
 * @version 1.0
 */

public class FirmeBulk extends FirmeBase {

	public FirmeBulk() {
		super();
	}
	public FirmeBulk(java.lang.Integer esercizio, String tipo) {
		super(esercizio,tipo);
	}
	
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		super.initializeForInsert(bp,context);
		setEsercizio(CNRUserInfo.getEsercizio(context));
	  return this;
	}

	public static final String DB_CERTIFICATI  ="CRT";
	
	public final static Dictionary tipoDBKeys;
	static {
	tipoDBKeys = new it.cnr.jada.util.OrderedHashtable();
	tipoDBKeys.put(DB_CERTIFICATI,"Stampe Certificazioni");
	};

}
