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
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiBulk;
import it.cnr.contab.incarichi00.bulk.AnagraficaDottoratiRateBulk;
import it.cnr.contab.incarichi00.ejb.AnagraficaDottoratiComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.12.44)
 *columnFieldProperty
 * @author: Roberto Fantino
 */
public class CRUDAnagraficaDottoratiBP
        extends it.cnr.jada.util.action.SimpleCRUDBP {

    private final AnagraficaDottoratiRataCRUDController rateCRUDController = new AnagraficaDottoratiRataCRUDController(
            "rateCRUDController",
            AnagraficaDottoratiRateBulk.class,
            "anagraficaDottoratiRate",
            this);

    public CRUDAnagraficaDottoratiBP() {
        super();
    }


    public CRUDAnagraficaDottoratiBP(String function) {
        super(function);
    }

    public AnagraficaDottoratiRataCRUDController getRateCRUDController() {
        return rateCRUDController;
    }

   protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        setTab("tab", "tabAnagraficadottorati");
        super.init(config, actioncontext);
    }


    /**
     * Ricerca i conti disponibili e imposta nel modello il primo elemento trovato
     */

    public void findListaBanche(ActionContext context) throws BusinessProcessException {

        try {
            AnagraficaDottoratiBulk anagraficaDottorati = (AnagraficaDottoratiBulk) getModel();
            if (anagraficaDottorati.getModalita_pagamento() != null) {
                AnagraficaDottoratiComponentSession component = (AnagraficaDottoratiComponentSession) createComponentSession();
                java.util.List coll = component.findListaBanche(context.getUserContext(), anagraficaDottorati);

                //	Assegno di default la prima banca tra quelle selezionate
                if (coll == null || coll.isEmpty())
                    anagraficaDottorati.setBanca(null);
                else
                    anagraficaDottorati.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk) new java.util.Vector(coll).firstElement());
            } else
                anagraficaDottorati.setBanca(null);

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
            AnagraficaDottoratiBulk anagraficaDottorati,
            TerzoBulk terzo) throws BusinessProcessException {

        try {

            AnagraficaDottoratiComponentSession component = (AnagraficaDottoratiComponentSession) createComponentSession();
            AnagraficaDottoratiBulk anagraficaDottoratiClone = component.completaTerzo(context.getUserContext(), anagraficaDottorati, terzo);

            setModel(context, anagraficaDottoratiClone);

        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }

    }
}
