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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
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
import it.cnr.contab.incarichi00.bulk.ScadenzarioDottoratiBulk;
import it.cnr.contab.incarichi00.bulk.ScadenzarioDottoratiRataBulk;
import it.cnr.contab.incarichi00.ejb.ScadenzarioDottoratiComponentSession;
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
            setTab("tab","tabScadenzariodottorati");
            super.init(config, actioncontext);
    }


     /**
     * Ricerca i conti disponibili e imposta nel modello il primo elemento trovato
     */

     public void findListaBanche(ActionContext context) throws BusinessProcessException {

     try {
                    ScadenzarioDottoratiBulk scadenzarioDottorati = (ScadenzarioDottoratiBulk) getModel();
                    if (scadenzarioDottorati.getModalita_pagamento() != null) {
                        ScadenzarioDottoratiComponentSession component = (ScadenzarioDottoratiComponentSession) createComponentSession();
                        java.util.List coll = component.findListaBanche(context.getUserContext(), scadenzarioDottorati);

                        //	Assegno di default la prima banca tra quelle selezionate
                        if (coll == null || coll.isEmpty())
                            scadenzarioDottorati.setBanca(null);
                        else
                            scadenzarioDottorati.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk) new java.util.Vector(coll).firstElement());
                    } else
                        scadenzarioDottorati.setBanca(null);

                } catch (it.cnr.jada.comp.ComponentException ex) {
                    throw handleException(ex);
                } catch (java.rmi.RemoteException ex) {
                    throw handleException(ex);
                }
            }

            /**
             * Chiama il metodo sulla component 'completa percipiente' e mette il controller
             * in stato edit sul modello clone
             */

            public void completaTerzo(
                    ActionContext context,
                    ScadenzarioDottoratiBulk scadenzarioDottorati,
                    TerzoBulk terzo) throws BusinessProcessException {

                try {

                    ScadenzarioDottoratiComponentSession component = (ScadenzarioDottoratiComponentSession) createComponentSession();
                    ScadenzarioDottoratiBulk scadenzarioDottoratiClone = component.completaTerzo(context.getUserContext(), scadenzarioDottorati, terzo);

                    setModel(context, scadenzarioDottoratiClone);

                } catch (it.cnr.jada.comp.ComponentException ex) {
                    throw handleException(ex);
                } catch (java.rmi.RemoteException ex) {
                    throw handleException(ex);
                }

            }
}
