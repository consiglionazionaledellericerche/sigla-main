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

import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.bulk.FieldValidationMap;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.CollapsableDetailCRUDController;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface IDocAmmEconomicaBP {

    String getTab(String tabName);

    FormController getController();

    CollapsableDetailCRUDController getMovimentiDare();

    CollapsableDetailCRUDController getMovimentiAvere();

    int getStatus();

    FieldValidationMap getFieldValidationMap();

    BusinessProcess getParentRoot();

    OggettoBulk getModel();

    void setDirty(boolean dirty);

    void setMessage(int status, String message);

    boolean isButtonGeneraScritturaVisible();

    public static Button[] addPartitario(Button[] buttons, boolean attivaEconomicaParallela, boolean isEditing, OggettoBulk model) {
        if (attivaEconomicaParallela) {
            return Stream.concat(Arrays.asList(buttons).stream(),
                    Arrays.asList(
                            new Button(Config.getHandler().getProperties(IDocAmmEconomicaBP.class), "CRUDToolbar.partitario")
                    ).stream()).toArray(Button[]::new);
        }
        return buttons;
    }
}
