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

package it.cnr.contab.docamm00.bp;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.util.ObjectReplacer;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * <!-- @TODO: da completare -->
 */

public class CRUDValutaStranieraBP
    extends it.cnr.jada.util.action.SimpleCRUDBP {

    private boolean isDeleting = false;

public CRUDValutaStranieraBP(String function) {

	super(function);
}

public void edit(
    it.cnr.jada.action.ActionContext context,
    it.cnr.jada.bulk.OggettoBulk bulk)
    throws it.cnr.jada.action.BusinessProcessException {

    super.edit(context, bulk);


	if (bulk != null) {
            String cd_euro = null;
            try {
                cd_euro =
                    (
                        (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)
                        super.createComponentSession(
                                "CNRCONFIG00_EJB_Configurazione_cnrComponentSession",
                                it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class))
                            .getVal01(
                        context.getUserContext(),
                        new Integer(0),
                        "*",
                        "CD_DIVISA",
                        "EURO");
            } catch (it.cnr.jada.comp.ComponentException e) {
                throw handleException(e);
            } catch (java.rmi.RemoteException e) {
                throw handleException(e);
            }
            if (((DivisaBulk)bulk).getCd_divisa().equals(cd_euro)){
           	    setStatus(it.cnr.jada.util.action.FormController.VIEW);
           	    setMessage("Attenzione. Non è possibile modificare la valuta di default!");
            }
         }

}
}