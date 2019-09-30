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

package it.cnr.contab.ordmag.anag00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRORDMAG00_EJB_AbilitBeneServMagComponentSession")
public class AbilitBeneServMagComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AbilitBeneServMagComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.ordmag.anag00.comp.AbilitBeneServMagComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AbilitBeneServMagComponentSessionBean();
	}

}
