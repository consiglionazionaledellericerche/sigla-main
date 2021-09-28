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

package it.cnr.contab.incarichi00.ejb;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.incarichi00.bulk.ScadenzarioDottoratiBulk;
import it.cnr.contab.incarichi00.comp.ScadenzarioDottoratiComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.List;

@Stateless(name="CNRINCARICHI00_EJB_ScadenzarioDottoratiComponentSession")
public class ScadenzarioDottoratiComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ScadenzarioDottoratiComponentSession {
@PostConstruct
	public void ejbCreate() {
		componentObj = new ScadenzarioDottoratiComponent();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ScadenzarioDottoratiComponentSessionBean();
	}

	@Override
	public ScadenzarioDottoratiBulk completaTerzo(UserContext param0, ScadenzarioDottoratiBulk param1, TerzoBulk param2) throws ComponentException, RemoteException {
		return null;
	}

	@Override
	public List findListaBanche(UserContext param0, ScadenzarioDottoratiBulk param1) throws ComponentException, RemoteException {
		return null;
	}

}
