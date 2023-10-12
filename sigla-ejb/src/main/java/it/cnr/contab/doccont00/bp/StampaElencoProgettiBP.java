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

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.EJBException;
import java.rmi.RemoteException;

public class StampaElencoProgettiBP extends ParametricPrintBP {
    private Parametri_cnrBulk parametriCnr;

    public StampaElencoProgettiBP() {
        super();
    }

    public StampaElencoProgettiBP(String function) {
        super(function);
    }

    public Parametri_cnrBulk getParametriCnr() {
        return parametriCnr;
    }

    public void setParametriCnr(Parametri_cnrBulk parametriCnr) {
        this.parametriCnr = parametriCnr;
    }

    @Override
    protected void initialize(ActionContext context)
            throws BusinessProcessException {
        try {
            setParametriCnr(Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())));
            super.initialize(context);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        } catch (EJBException e) {
            throw handleException(e);
        }
    }

    public String getLabelFindProgettoForPrint() {
        if (this.getParametriCnr().getFl_nuovo_pdg())
            return ProgettoBulk.LABEL_AREA_PROGETTUALE;
        else
            return ProgettoBulk.LABEL_PROGETTO;
    }

    public String getLabelFindCommessaForPrint() {
        if (this.getParametriCnr().getFl_nuovo_pdg())
            return ProgettoBulk.LABEL_PROGETTO;
        else
            return ProgettoBulk.LABEL_COMMESSA;
    }

}
