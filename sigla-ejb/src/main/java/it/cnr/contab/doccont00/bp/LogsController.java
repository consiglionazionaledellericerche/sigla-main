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

package it.cnr.contab.doccont00.bp;

import it.cnr.jada.action.HttpActionContext;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * Controller creato per la visualizzazione dei Logs nel pannelo Carica File Cassiere.
 * Reimplementati i metodi relativi alla creazione e disegno della barra dei pulsanti.
 * Creation date: (24/04/2003 12.12.37)
 *
 * @author: Gennaro Borriello
 */
public class LogsController extends it.cnr.jada.util.action.SimpleDetailCRUDController {

    // Collezione dei pulsanti
    private it.cnr.jada.util.jsp.Button[] crudToolbar;

    /**
     * LogsController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     */
    public LogsController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, listPropertyName, parent);
    }

    /**
     * LogsController constructor comment.
     *
     * @param name             java.lang.String
     * @param modelClass       java.lang.Class
     * @param listPropertyName java.lang.String
     * @param parent           it.cnr.jada.util.action.FormController
     * @param multiSelection   boolean
     */
    public LogsController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent, boolean multiSelection) {
        super(name, modelClass, listPropertyName, parent, multiSelection);
    }

    /**
     * Reimplementato per disegnare, insieme agli altri pulsanti, il pulsante per visualizzare
     * i logs relativi al File selezionato.
     */
    protected it.cnr.jada.util.jsp.Button[] createCRUDToolbar() {
        it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[7];
        toolbar[0] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.add");
        toolbar[1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.filter");
        toolbar[2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.remove");
        toolbar[3] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.removeAll");
        toolbar[4] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.removeFilter");
        toolbar[5] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.logs");
        toolbar[6] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.logscarti");

        crudToolbar = toolbar;
        return toolbar;
    }

    /**
     * Disegna una toolbar HTML con i bottoni per effettuare le operazioni
     * di inserimento, cancellazione e ricerca dei dettagli
     */
    @Override
    public void writeHTMLToolbar(PageContext context, boolean add, boolean filter, boolean delete, boolean closedToolbar) throws IOException, ServletException {
        super.writeHTMLToolbar(context, add, filter, delete, false);

        if (getDetails() != null) {
            it.cnr.jada.util.jsp.Button button;
            it.cnr.jada.util.jsp.Button button1;
            button = crudToolbar[5];
            button1 = crudToolbar[6];
            button.writeToolbarButton(context.getOut(), isShrinkable(), HttpActionContext.isFromBootstrap(context));
            button1.writeToolbarButton(context.getOut(), isShrinkable(), HttpActionContext.isFromBootstrap(context));
        }
        super.closeButtonGROUPToolbar(context);
    }
}
