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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.docs.bulk.Stampa_vpg_doc_genericoBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (20/03/2003 11.12.29)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_doc_genericoAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * Stampa_vpg_doc_genericoAction constructor comment.
 */
public Stampa_vpg_doc_genericoAction() {
	super();
}
/**
 * Gestisce il comando di aggiunta di un nuovo dettaglio su un CRUDController
 * figlio del ricevente
 */

public Forward doOnTipoDocumentoChange(ActionContext context) {

    try {
        fillModel(context);
        
        ParametricPrintBP bp= (ParametricPrintBP) context.getBusinessProcess();
        Stampa_vpg_doc_genericoBulk stampa = (Stampa_vpg_doc_genericoBulk) bp.getModel();

        if (!Optional.ofNullable(stampa).flatMap(el->Optional.ofNullable(el.getTipo_documento())).flatMap(el->Optional.ofNullable(el.getCd_tipo_documento_amm()))
                .map(el->el.equals(Stampa_vpg_doc_genericoBulk.GENERICO_E)).orElse(Boolean.FALSE)) {
            stampa.setTipoDocumentoGenerico(null);
            stampa.setIdTipoDocumentoGenerico(null);
        }

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public Forward doOnTipoDocumentoGenericoChange(ActionContext context) {

    try {
        fillModel(context);
        
        ParametricPrintBP bp= (ParametricPrintBP) context.getBusinessProcess();
        Stampa_vpg_doc_genericoBulk stampa = (Stampa_vpg_doc_genericoBulk) bp.getModel();

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
