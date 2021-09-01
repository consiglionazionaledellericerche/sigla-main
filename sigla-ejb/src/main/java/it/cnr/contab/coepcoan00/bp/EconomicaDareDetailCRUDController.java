/*
 * Copyright (C) 2021  Consiglio Nazionale delle Ricerche
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

import it.cnr.jada.util.action.FormController;

public class EconomicaDareDetailCRUDController extends EconomicaDetailCRUDController{
    public EconomicaDareDetailCRUDController(FormController formcontroller) {
        this(
                "Movimenti Dare",
                it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk.class,
                "scrittura_partita_doppia.movimentiDareColl",
                formcontroller
        );
    }

    public EconomicaDareDetailCRUDController(String s, Class class1, String s1, FormController formcontroller) {
        super(s, class1, s1, formcontroller);
    }

    public EconomicaDareDetailCRUDController(String s, Class class1, String s1, FormController formcontroller, boolean flag) {
        super(s, class1, s1, formcontroller, flag);
    }

    @Override
    protected String getBorderClass() {
        return "border-danger";
    }

    @Override
    protected String getTextClass() {
        return "text-danger";
    }

}
