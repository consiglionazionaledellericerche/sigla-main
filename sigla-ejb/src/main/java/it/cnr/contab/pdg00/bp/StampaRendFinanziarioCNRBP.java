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

package it.cnr.contab.pdg00.bp;

import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.util.jsp.Button;

/**
 * Insert the type's description here.
 * Creation date: (23/03/2004 16.25.52)
 *
 * @author: Gennaro Borriello
 */
public class StampaRendFinanziarioCNRBP extends ParametricPrintBP {
    /**
     * StampaRendFinanziarioCNRBP constructor comment.
     */
    public StampaRendFinanziarioCNRBP() {
        super();
    }

    /**
     * StampaRendFinanziarioCNRBP constructor comment.
     *
     * @param function java.lang.String
     */
    public StampaRendFinanziarioCNRBP(String function) {
        super(function);
    }

    public String getPrintbp() {
        return "OfflineReportPrintBP";
    }

    /**
     * Inizializza la schermata presentando in primo piano il Tab dell'esercizio dell'anno corrente
     *
     * @param context {@link it.cnr.jada.action.ActionContext } in uso.
     * @throws it.cnr.jada.action.BusinessProcessException
     */
    protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        super.init(config, context);
        it.cnr.contab.pdg00.bulk.StampaRendFinCNRVBulk model = (it.cnr.contab.pdg00.bulk.StampaRendFinCNRVBulk) getModel();
        model.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
        setModel(context, model);
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = new Button[1];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.close");
        return toolbar;
    }
}
