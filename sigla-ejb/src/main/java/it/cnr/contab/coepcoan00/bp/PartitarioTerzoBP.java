/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.coepcoan00.bp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.coepcoan00.filter.bulk.FiltroRicercaTerzoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

public class PartitarioTerzoBP extends BulkBP {

    @Override
    protected Button[] createToolbar() {
        final Properties properties = it.cnr.jada.util.Config.getHandler().getProperties(getClass());
        return Arrays.asList(
                        new Button(properties, "CRUDToolbar.startSearch")
                ).stream().toArray(Button[]::new);
    }

    @Override
    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        super.init(config, actioncontext);
        final FiltroRicercaTerzoBulk filtroRicercaTerzoBulk = new FiltroRicercaTerzoBulk();
        final TerzoBulk terzoBulk = new TerzoBulk();
        terzoBulk.setAnagrafico(new AnagraficoBulk());
        filtroRicercaTerzoBulk.setTerzo(terzoBulk);
        setModel(actioncontext, filtroRicercaTerzoBulk);
    }

    public boolean isStartSearchButtonEnabled() {
        return Optional.ofNullable(getModel())
                .filter(FiltroRicercaTerzoBulk.class::isInstance)
                .map(FiltroRicercaTerzoBulk.class::cast)
                .flatMap(filtroRicercaTerzoBulk -> Optional.ofNullable(filtroRicercaTerzoBulk.getTerzo()))
                .filter(terzoBulk -> terzoBulk.getCrudStatus() == OggettoBulk.NORMAL)
                .isPresent();
    }
    @Override
    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
        try {
            it.cnr.jada.ejb.CRUDComponentSession cs = Utility.createCRUDComponentSession();
            if (cs == null) return null;
            return EJBCommonServices.openRemoteIterator(
                    actioncontext,
                    cs.cerca(actioncontext.getUserContext(), compoundfindclause, oggettobulk));
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }
}
