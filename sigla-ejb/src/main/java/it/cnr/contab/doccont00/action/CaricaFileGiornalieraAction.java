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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.bp.CaricaFileGiornalieraBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.upload.UploadedFile;

/**
 * Insert the type's description here.
 * Creation date: (10/04/2003 12.04.09)
 *
 * @author: Gennaro Borriello
 */
public class CaricaFileGiornalieraAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
    /**
     * CaricaFileGiornalieraAction constructor comment.
     */
    public CaricaFileGiornalieraAction() {
        super();
    }

    public Forward doBringBack(ActionContext context) {
        return context.findDefaultForward();
    }

    /**
     * Richiamato per la procedura di upload del nuovo File Cassiere.
     *
     * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
     * @return forward <code>Forward</code>
     **/
    public Forward doCaricaFile(ActionContext context) {

        it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext) context;

        UploadedFile file = httpContext.getMultipartParameter("fileGiornaliera");
        try {
            if (file == null || file.getName().equals(""))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da caricare.");

            CaricaFileGiornalieraBP bp = (CaricaFileGiornalieraBP) httpContext.getBusinessProcess();
            bp.caricaFile(context, file.getFile());
            bp.setMessage("Operazione Completata.");
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}