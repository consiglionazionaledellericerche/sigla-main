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

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;

import java.util.Optional;

public class GestionePagopaBulk extends GestionePagopaBase  {
	private static final long serialVersionUID = 1L;

	private TipoScadenzaPagopaBulk tipoScadenzaPagopa;
	public GestionePagopaBulk() {
		super();
	}

	public GestionePagopaBulk(Integer id) {
		super(id);
	}


	public TipoScadenzaPagopaBulk getTipoScadenzaPagopa() {
		return tipoScadenzaPagopa;
	}

	public void setTipoScadenzaPagopa(TipoScadenzaPagopaBulk tipoScadenzaPagopaBulk) {
		this.tipoScadenzaPagopa = tipoScadenzaPagopaBulk;
	}

	@Override
	public Integer getIdTipoScadenzaPagopa() {
		return Optional.ofNullable(getTipoScadenzaPagopa())
					.map(TipoScadenzaPagopaBulk::getId)
					.orElse(null);
	}
	
	@Override
	public void setIdTipoScadenzaPagopa(Integer idScadenzaPagopaBulk) {
		Optional.ofNullable(getTipoScadenzaPagopa()).ifPresent(el->el.setId(idScadenzaPagopaBulk));
	}
	
}