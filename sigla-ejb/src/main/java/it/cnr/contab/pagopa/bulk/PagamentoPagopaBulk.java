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

import it.cnr.jada.bulk.OggettoBulk;

import java.util.Optional;

public class PagamentoPagopaBulk extends PagamentoPagopaBase {
	private static final long serialVersionUID = 1L;

	private PendenzaPagopaBulk pendenzaPagopa;

	public PagamentoPagopaBulk() {
		super();
	}

	public PagamentoPagopaBulk(Long id) {
		super(id);
	}
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
		return super.initializeForInsert(bp,context);
	}

	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
		return super.initializeForInsert(bp,context);
	}

	@Override
	public Long getIdPendenzaPagopa() {
		return Optional.ofNullable(getPendenzaPagopa())
					.map(PendenzaPagopaBulk::getId)
					.orElse(null);
	}
	
	@Override
	public void setIdPendenzaPagopa(Long idPendenzaPagopa) {
		Optional.ofNullable(getPendenzaPagopa()).ifPresent(el->el.setId(idPendenzaPagopa));
	}

	public PendenzaPagopaBulk getPendenzaPagopa() {
		return pendenzaPagopa;
	}

	public void setPendenzaPagopa(PendenzaPagopaBulk pendenzaPagopa) {
		this.pendenzaPagopa = pendenzaPagopa;
	}
}