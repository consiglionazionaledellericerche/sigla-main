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

import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.contab.util.EuroFormat;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.action.CollapsableDetailCRUDController;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.jsp.TableCustomizer;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class EconomicaDetailCRUDController extends CollapsableDetailCRUDController implements TableCustomizer {

    public EconomicaDetailCRUDController(String s, Class class1, String s1, FormController formcontroller) {
        super(s, class1, s1, formcontroller);
        this.setCollapsed(Boolean.FALSE);
    }

    public EconomicaDetailCRUDController(String s, Class class1, String s1, FormController formcontroller, boolean flag) {
        super(s, class1, s1, formcontroller, flag);
    }

    @Override
    public boolean isInputReadonly() {
        return Boolean.TRUE;
    }


    @Override
    public String getRowStyle(Object obj) {
        return null;
    }

    @Override
    public boolean isRowEnabled(Object obj) {
        return true;
    }

    @Override
    public boolean isRowReadonly(Object obj) {
        return false;
    }

    @Override
    public String getTableClass() {
        return " table-fixed-header";
    }

    public void writeTfoot(JspWriter jspWriter) throws IOException {
        final EuroFormat euroFormat = new EuroFormat();
        final long numberOfColspan = Collections.list(BulkInfo.getBulkInfo(this.getModelClass())
                .getColumnFieldProperties("scrittura")).stream().count();
        final List<Movimento_cogeBulk> movimento_cogeBulks = getDetails();
        if (Optional.ofNullable(movimento_cogeBulks).map(movimento_cogeBulks1 -> !movimento_cogeBulks.isEmpty()).orElse(Boolean.FALSE) ) {
            final BigDecimal totalMovimento = BigDecimal.valueOf(movimento_cogeBulks.stream()
                    .mapToDouble(value -> value.getIm_movimento().doubleValue())
                    .sum());

            jspWriter.println("<tfoot class=\"bg-info\">");
            jspWriter.println("<tr>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\"  colspan=\"" + numberOfColspan + "\" align=\"right\">");
            jspWriter.println("<span>Totale:</span>");
            jspWriter.println("</td>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
            jspWriter.print(euroFormat.format(totalMovimento));
            jspWriter.println("</td>");
            jspWriter.println("</tr>");
            jspWriter.println("</tfoot>");
        }
    }
}
