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

package it.cnr.contab.pagopa.bulk;

import java.util.Optional;

public class GestionePagopaBulk extends GestionePagopaBase  {
	private static final long serialVersionUID = 1L;

	private TipoPendenzaPagopaBulk tipoPendenzaPagopa;
	public GestionePagopaBulk() {
		super();
	}

	public GestionePagopaBulk(Integer id) {
		super(id);
	}


	public TipoPendenzaPagopaBulk getTipoPendenzaPagopa() {
		return tipoPendenzaPagopa;
	}

	public void setTipoPendenzaPagopa(TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk) {
		this.tipoPendenzaPagopa = tipoPendenzaPagopaBulk;
	}

	@Override
	public Integer getIdTipoPendenzaPagopa() {
		return Optional.ofNullable(getTipoPendenzaPagopa())
					.map(TipoPendenzaPagopaBulk::getId)
					.orElse(null);
	}
	
	@Override
	public void setIdTipoPendenzaPagopa(Integer idPendenzaPagopaBulk) {
		Optional.ofNullable(getTipoPendenzaPagopa()).ifPresent(el->el.setId(idPendenzaPagopaBulk));
	}
	
}