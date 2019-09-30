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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/02/2011
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class VLimiteSpesaDetBulk extends VLimiteSpesaDetBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_LIMITE_SPESA_DET
	 **/
	public static final java.util.Dictionary fonteKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String FONTE_INTERNA = "FIN";
	final public static String FONTE_ESTERNA = "FES";
	
	static {
		fonteKeys.put(FONTE_INTERNA,"Interna");
		fonteKeys.put(FONTE_ESTERNA,"Esterna");
	}
	public VLimiteSpesaDetBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_LIMITE_SPESA_DET
	 **/
	public VLimiteSpesaDetBulk(java.lang.Integer esercizio, java.lang.String cdCds, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, cdCds, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
	}
}