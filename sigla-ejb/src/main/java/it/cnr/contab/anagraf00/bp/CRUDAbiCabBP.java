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

package it.cnr.contab.anagraf00.bp;

import it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.jsp.Button;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 11.01.35)
 *
 * @author: CNRADM
 */
public class CRUDAbiCabBP extends it.cnr.jada.util.action.SimpleCRUDBP {
    /**
     * CRUDAbiCabBP constructor comment.
     */
    public CRUDAbiCabBP() {
        super();
    }

    /**
     * CRUDAbiCabBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDAbiCabBP(String function) {
        super(function);
    }

    public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {

        super.basicEdit(context, bulk, doInitializeForEdit);

        if (getStatus() != VIEW) {
            AbicabBulk abiCab = (AbicabBulk) getModel();
            if (abiCab != null && abiCab.isCancellatoLogicamente()) {
                setStatus(VIEW);
            }
        }
    }

    public void findCaps(ActionContext context) throws BusinessProcessException {

        try {
            it.cnr.contab.anagraf00.ejb.AbiCabComponentSession session = (it.cnr.contab.anagraf00.ejb.AbiCabComponentSession) createComponentSession();
            AbicabBulk abicab = session.findCaps(context.getUserContext(), (AbicabBulk) getModel());
            setModel(context, abicab);

        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        } catch (ComponentException ex) {
            throw handleException(ex);
        }
    }

    public boolean isCancellatoLogicamente(ActionContext context) throws BusinessProcessException {

        try {
            it.cnr.contab.anagraf00.ejb.AbiCabComponentSession session = (it.cnr.contab.anagraf00.ejb.AbiCabComponentSession) createComponentSession();
            return session.isCancellatoLogicamente(context.getUserContext(), (AbicabBulk) getModel());

        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        } catch (ComponentException ex) {
            throw handleException(ex);
        }
    }

    public boolean isCancellatoLogicamente() throws BusinessProcessException {
        return Optional.ofNullable(getModel())
                .filter(AbicabBulk.class::isInstance)
                .map(AbicabBulk.class::cast)
                .map(AbicabBulk::getFl_cancellato)
                .orElse(Boolean.FALSE);
    }

	@Override
	protected Button[] createToolbar() {
		final Properties properties = Config.getHandler().getProperties(getClass());
		return Stream.concat(Arrays.asList(super.createToolbar()).stream(),
				Arrays.asList(
						new Button(properties, "CRUDToolbar.ripristina")
				).stream()).toArray(Button[]::new);
	}
}
