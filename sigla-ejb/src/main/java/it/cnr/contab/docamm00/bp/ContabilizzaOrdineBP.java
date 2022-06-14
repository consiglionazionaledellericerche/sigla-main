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

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.RicercaComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class ContabilizzaOrdineBP extends SelezionatoreListaBP implements SearchProvider {

    private Fattura_passivaBulk fattura_passivaBulk;

    public ContabilizzaOrdineBP(String s) {
        super(s);
        setBulkInfo(BulkInfo.getBulkInfo(EvasioneOrdineRigaBulk.class));
        setColumns(getBulkInfo().getColumnFieldPropertyDictionary("fattura_passiva"));
    }

    public void setFattura_passivaBulk(Fattura_passivaBulk fattura_passivaBulk) {
        this.fattura_passivaBulk = fattura_passivaBulk;
    }

    @Override
    public RemoteIterator search(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException {
        try {
            return EJBCommonServices.openRemoteIterator(
                    actioncontext,
                    ((RicercaComponentSession)createComponentSession("CNRDOCAMM00_EJB_FatturaPassivaComponentSession"))
                            .cerca(
                                actioncontext.getUserContext(), compoundfindclause,
                                oggettobulk, fattura_passivaBulk, "ricercaOrdini"
                            )
            );
        } catch (Exception exception) {
            throw new BusinessProcessException(exception);
        }
    }
}
