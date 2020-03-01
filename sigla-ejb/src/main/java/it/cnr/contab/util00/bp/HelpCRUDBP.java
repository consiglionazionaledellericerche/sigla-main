/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.util00.bp;

import it.cnr.contab.utenze00.ejb.AssBpAccessoComponentSession;
import it.cnr.contab.util00.bulk.HelpBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class HelpCRUDBP extends SimpleCRUDBP {
    public HelpCRUDBP() {
    }

    public HelpCRUDBP(String s) {
        super(s);
    }

    @Override
    public void validate(ActionContext actioncontext) throws ValidationException {
        super.validate(actioncontext);
        Optional.ofNullable(getModel())
                .filter(HelpBulk.class::isInstance)
                .map(HelpBulk.class::cast)
                .filter(helpBulk -> !(!Optional.ofNullable(helpBulk.getBpName()).isPresent() && !Optional.ofNullable(helpBulk.getPage()).isPresent()))
                .orElseThrow(() -> {
                    return new ValidationException(
                                    "Valorizzare almeno uno tra '".concat(getBulkInfo().getFieldProperty("bpName").getLabel()).concat(
                                            "' e '").concat(getBulkInfo().getFieldProperty("page").getLabel()).concat("'!"));
                    }
                );
    }

    @Override
    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        if (HelpBulk.bpKeys.isEmpty()) {
            final Map<String, String> map = Optional.ofNullable(createComponentSession("CNRUTENZE00_EJB_AssBpAccessoComponentSession", AssBpAccessoComponentSession.class))
                    .filter(AssBpAccessoComponentSession.class::isInstance)
                    .map(AssBpAccessoComponentSession.class::cast)
                    .map(assBpAccessoComponentSession -> {
                        try {
                            return assBpAccessoComponentSession.findDescrizioneBP(actioncontext.getUserContext());
                        } catch (ComponentException | RemoteException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    })
                    .orElse(Collections.emptyMap());

            actioncontext
                    .getActionMapping()
                    .getMappings()
                    .getBusinessProcesses()
                    .keySet()
                    .stream()
                    .sorted((o1, o2) -> {
                        return Optional.ofNullable(map.get(o1)).orElse(o1).toUpperCase().compareTo(
                                Optional.ofNullable(map.get(o2)).orElse(o2).toUpperCase()
                        );
                    })
                    .forEach(s -> {
                        HelpBulk.bpKeys.put(s,Optional.ofNullable(map.get(s)).map(s1 -> s1.concat(" - ").concat(s)).orElse(s));
                    });
        }
    }
}
