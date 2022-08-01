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

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.util.EuroFormat;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.jsp.TableCustomizer;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (10/09/2017 11:32:54 AM)
 *
 * @author: Marco Spasiano
 */
public class OrdiniCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController implements TableCustomizer {
    private boolean rettificheCollapse = true;

    public boolean isRettificheCollapse() {
        return rettificheCollapse;
    }

    public void setRettificheCollapse(boolean rettificheCollapse) {
        this.rettificheCollapse = rettificheCollapse;
    }
    /**
     * OrdiniCRUDController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     */
    public OrdiniCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }


    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isGrowable() {
        return false;
    }

    /**
     * Restituisce true se è possibile aggiungere nuovi elementi
     */
    public boolean isShrinkable() {
        return	super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching();
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

    @Override
    public void writeTfoot(JspWriter jspWriter) throws IOException {
        final EuroFormat euroFormat = new EuroFormat();
        final long numberOfColspan = Collections.list(BulkInfo.getBulkInfo(this.getModelClass())
                .getColumnFieldProperties()).stream().count() - 2;

        final Optional<Fattura_passivaBulk> fattura_passiva = Optional.ofNullable(getParentController())
                .filter(CRUDFatturaPassivaBP.class::isInstance)
                .map(CRUDFatturaPassivaBP.class::cast)
                .map(crudFatturaPassivaBP -> crudFatturaPassivaBP.getModel()
                )
                .filter(Fattura_passivaBulk.class::isInstance)
                .map(Fattura_passivaBulk.class::cast);
        final List<FatturaOrdineBulk> fatturaOrdineBulks = getDetails();
        if (!fatturaOrdineBulks.isEmpty()) {
            final BigDecimal totaleImponibile = BigDecimal.valueOf(fatturaOrdineBulks.stream()
                    .mapToDouble(value -> value.getImImponibile().doubleValue())
                    .sum());
            final BigDecimal totaleIva = BigDecimal.valueOf(fatturaOrdineBulks.stream()
                    .mapToDouble(value -> value.getImIva().doubleValue())
                    .sum());
            final BigDecimal totaleImponibilePerNotaCredito = BigDecimal.valueOf(fatturaOrdineBulks.stream()
                    .mapToDouble(value -> Utility.nvl(value.getImponibileErrato(), value.getImponibilePerRigaFattura()).doubleValue())
                    .sum());
            final BigDecimal totaleIvaPerNotaCredito = BigDecimal.valueOf(fatturaOrdineBulks.stream()
                    .mapToDouble(value -> value.getIvaPerRigaFattura().doubleValue())
                    .sum());
            final BigDecimal differenzaImponibile = fattura_passiva.get()
                    .getTotaleImponibileFatturaElettronica().subtract(totaleImponibilePerNotaCredito);
            final BigDecimal differenzaIva = fattura_passiva.get()
                    .getTotaleIvaFatturaElettronica().subtract(totaleIvaPerNotaCredito);

            jspWriter.println("<tfoot class=\"bg-info\">");
            jspWriter.println("<tr>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\"  colspan=\"" + numberOfColspan + "\" align=\"right\">");
            jspWriter.println("<span>Totali Ordine:</span>");
            jspWriter.println("</td>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
            jspWriter.print(euroFormat.format(totaleImponibile));
            jspWriter.println("</td>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
            jspWriter.print(euroFormat.format(totaleIva));
            jspWriter.println("</td>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
            jspWriter.print(euroFormat.format(totaleImponibile.add(totaleIva)));
            jspWriter.println("</td>");
            jspWriter.println("</tr>");

            if (totaleImponibile.compareTo(totaleImponibilePerNotaCredito) != 0 || totaleIva.compareTo(totaleIvaPerNotaCredito) != 0){
                jspWriter.println("<tr>");
                jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\"  colspan=\"" + numberOfColspan + "\" align=\"right\">");
                jspWriter.println("<span>Totali Ordine Per Nota Credito:</span>");
                jspWriter.println("</td>");
                jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
                jspWriter.print(euroFormat.format(totaleImponibilePerNotaCredito));
                jspWriter.println("</td>");
                jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
                jspWriter.print(euroFormat.format(totaleIvaPerNotaCredito));
                jspWriter.println("</td>");
                jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
                jspWriter.print(euroFormat.format(totaleImponibilePerNotaCredito.add(totaleIvaPerNotaCredito)));
                jspWriter.println("</td>");
                jspWriter.println("</tr>");
            }

            jspWriter.println("<tr>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\"  colspan=\"" + numberOfColspan + "\" align=\"right\">");
            jspWriter.println("<span>Importi Fattura:</span>");
            jspWriter.println("</td>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
            jspWriter.print(euroFormat.format(fattura_passiva.get()
                    .getTotaleImponibileFatturaElettronica()));
            jspWriter.println("</td>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
            jspWriter.print(euroFormat.format(fattura_passiva.get()
                    .getTotaleIvaFatturaElettronica()));
            jspWriter.println("</td>");
            jspWriter.println("<td class=\"TableHeader text-white font-weight-bold\" align=\"right\">");
            jspWriter.print(euroFormat.format(fattura_passiva.get()
                    .getTotaleImponibileFatturaElettronica().add(fattura_passiva.get()
                            .getTotaleIvaFatturaElettronica())));
            jspWriter.println("</td>");
            jspWriter.println("</tr>");
            if (differenzaImponibile.compareTo(BigDecimal.ZERO) != 0 || differenzaIva.compareTo(BigDecimal.ZERO) != 0){
                jspWriter.println("<tr>");
                jspWriter.println("<td class=\"TableHeader text-white bg-danger font-weight-bold\" colspan=\"" + numberOfColspan +"\" align=\"right\">");
                jspWriter.println("<span>Differenze:</span>");
                jspWriter.println("</td>");
                jspWriter.println("<td class=\"TableHeader text-white bg-danger font-weight-bold\" align=\"right\">");
                jspWriter.print(euroFormat.format(differenzaImponibile));
                jspWriter.println("</td>");
                jspWriter.println("<td class=\"TableHeader text-white bg-danger font-weight-bold\" align=\"right\">");
                jspWriter.print(euroFormat.format(differenzaIva));
                jspWriter.println("</td>");
                jspWriter.println("<td class=\"TableHeader text-white bg-danger font-weight-bold\" align=\"right\">");
                jspWriter.print(euroFormat.format(differenzaImponibile.add(differenzaIva)));
                jspWriter.println("</td>");
                jspWriter.println("</tr>");
            }
            jspWriter.println("</tfoot>");
        }
    }


    @Override
    public void writeHTMLToolbar(
            javax.servlet.jsp.PageContext context,
            boolean reset,
            boolean find,
            boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

        super.writeHTMLToolbar(context, reset, find, delete, false);
        boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);
        String command = null;
        if (getParentController() != null)
            command = "javascript:submitForm('doSelezionaOrdini')";
        it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
                context,
                isFromBootstrap ? "fa fa-fw fa-bolt" : "img/history16.gif",
                !(isInputReadonly() ||  ((CRUDFatturaPassivaBP) getParentController()).isSearching()) ? command : null,
                true,
                "Seleziona Ordini",
                "btn-sm btn-outline-primary btn-title",
                isFromBootstrap);
        super.closeButtonGROUPToolbar(context);
    }
}
