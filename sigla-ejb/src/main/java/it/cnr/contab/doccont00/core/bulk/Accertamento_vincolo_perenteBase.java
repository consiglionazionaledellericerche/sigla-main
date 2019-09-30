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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Accertamento_vincolo_perenteBase extends Accertamento_vincolo_perenteKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// IM_VINCOLO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_vincolo;
 
	public Accertamento_vincolo_perenteBase() {
		super();
	}
	
	public Accertamento_vincolo_perenteBase(java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String cd_cds_accertamento,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento) {
		super(esercizio, pg_variazione, cd_cds_accertamento, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento);
	}

	public java.math.BigDecimal getIm_vincolo() {
		return im_vincolo;
	}
	
	public void setIm_vincolo(java.math.BigDecimal im_vincolo) {
		this.im_vincolo = im_vincolo;
	}
}