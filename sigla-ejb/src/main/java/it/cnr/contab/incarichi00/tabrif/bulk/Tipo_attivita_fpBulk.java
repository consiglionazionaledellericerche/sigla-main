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

public class Tipo_attivita_fpBulk extends Tipo_attivita_fpBase {
	public Tipo_attivita_fpBulk() {
		super();
	}
	public Tipo_attivita_fpBulk(java.lang.String cd_tipo_attivita) {
		super(cd_tipo_attivita);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_cancellato(Boolean.FALSE);
		return this;
	}

	private Tipo_attivita_fpBulk tipo_attivita_fp_padre;
	
	public Tipo_attivita_fpBulk getTipo_attivita_fp_padre() {
		return tipo_attivita_fp_padre;
	}
	public void setTipo_attivita_fp_padre(Tipo_attivita_fpBulk tipo_attivita_fp_padre) {
		this.tipo_attivita_fp_padre = tipo_attivita_fp_padre;
	}
	@Override
	public String getCd_tipo_attivita_padre() {
		if (this.getTipo_attivita_fp_padre() == null)
			return null;
		return this.getTipo_attivita_fp_padre().getCd_tipo_attivita();
	}
	public void setCd_tipo_attivita_padre(java.lang.String cd_tipo_attivita_padre) {
		if (this.getTipo_attivita_fp_padre() != null)
			this.getTipo_attivita_fp_padre().setCd_tipo_attivita_padre(cd_tipo_attivita_padre);
	}
}