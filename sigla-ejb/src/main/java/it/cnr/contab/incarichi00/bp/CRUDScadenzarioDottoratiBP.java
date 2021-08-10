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

package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.bp.MinicarrieraRataCRUDController;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.ejb.MinicarrieraComponentSession;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.ScadenzarioDottoratiRataBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.12.44)
 *
 * @author: Roberto Fantino
 */
public class CRUDScadenzarioDottoratiBP
        extends it.cnr.jada.util.action.SimpleCRUDBP
        {

    private final SimpleDetailCRUDController rateCRUDController = new SimpleDetailCRUDController(
            "rateCRUDController",
            ScadenzarioDottoratiRataBulk.class,
            "scadenzarioDottoratiRate",
            this);

    public CRUDScadenzarioDottoratiBP() {
        super();
    }


    public CRUDScadenzarioDottoratiBP(String function) {
        super(function + "Tr");
    }

    public SimpleDetailCRUDController getRateCRUDController() {
        return rateCRUDController;
    }

    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
            setTab("tab","tabScadenzarioDottorati");
            super.init(config, actioncontext);
    }

}
