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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Tipo_prestazioneBulk extends Tipo_prestazioneBase {
	final public static String PREVISTA_DA_NORME_DI_LEGGE = "LEGGE";
	final public static String DI_NATURA_DISCREZIONALE = "DISCR";

	public Tipo_prestazioneBulk() {
		super();
	}
	public Tipo_prestazioneBulk(java.lang.String cd_tipo_prestazione) {
		super(cd_tipo_prestazione);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_cancellato(Boolean.FALSE);
		return this;
	}
    public boolean isPrevistaDaNormeDiLegge() {
    	return this.getTipo_classificazione()!=null && 
			   this.getTipo_classificazione().equals(PREVISTA_DA_NORME_DI_LEGGE);
    }
    public boolean isDiNaturaDiscrezionale() {
    	return this.getTipo_classificazione()!=null && 
 			   this.getTipo_classificazione().equals(DI_NATURA_DISCREZIONALE);
    }
	
	
}