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

package it.cnr.contab.doccont00.bp;

import java.math.BigDecimal;
import java.util.Optional;

import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;

public class CRUDAccertamentoResiduoAmministraBP extends
		CRUDAccertamentoResiduoBP {
	
	public CRUDAccertamentoResiduoAmministraBP() {
		super();
	}

	public CRUDAccertamentoResiduoAmministraBP(String function) {
		super(function);
	}

	@Override
	public boolean isStatoVisibile() {
		return true;
	}
	
	@Override
	public boolean isROStato() {
		return false;
	}
    
    @Override
    public void update(ActionContext actioncontext) throws BusinessProcessException {
    	Optional.ofNullable(this.getModel()).map(AccertamentoResiduoBulk.class::cast).ifPresent(acc->{
    		if (acc.getStato()!=null && !acc.isStatoParzialmenteInesigibile() && !acc.isStatoInesigibile() && !acc.isStatoDubbio() && !acc.isStatoGiudizialmenteControverso()) {
    			acc.getPdgVincoliColl().stream().forEach(e->{
    				e.setFl_attivo(Boolean.FALSE);
    				e.setToBeUpdated();
    			});
    			acc.getAccertamentoVincoliPerentiColl().stream().forEach(e->{
    				e.setIm_vincolo(BigDecimal.ZERO);
    				e.setToBeUpdated();
    			});
    		}
    	});
    	super.update(actioncontext);
    }
}
