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

package it.cnr.contab.inventario00.bp;


/**
 * Controller utilizzato per la visualizzazione dei dettagli di un Buono di Scarico.
 * E' necessario creare una classe per poter
 * implementare metodi (per es.: getDettagliPagina), che richiamino la superclasse e che,
 * altrimenti, non sarebbe possibile
 **/

import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.jada.bulk.OggettoBulk;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class RigheInvDaFatturaCRUDController
        extends it.cnr.jada.util.action.RemoteDetailCRUDController implements it.cnr.jada.util.jsp.TableCustomizer {

    public RigheInvDaFatturaCRUDController(String name, Class modelClass, String attributeName, String componentSessionName, it.cnr.jada.util.action.FormController parent) {
        super(name, modelClass, attributeName, componentSessionName, parent);
    }

    public RigheInvDaFatturaCRUDController(String name, Class modelClass, String attributeName, String componentSessionName, it.cnr.jada.util.action.FormController parent, boolean multiSelection) {
        super(name, modelClass, attributeName, componentSessionName, parent, multiSelection);
    }

    /**
     * Restituisce il valore della proprietà 'dettagliPagina'
     *
     * @return Il valore della proprietà 'dettagliPagina'
     */
    public java.util.List getDettagliPagina() {
        return super.getDetailsPage();
    }

    /**
     * getRowStyle method comment.
     */
    public java.lang.String getRowStyle(java.lang.Object row) {
        return null;
    }
    @Override
    public String getRowCSSClass(Object obj, boolean even) {
        return null;
    };
    public OggettoBulk initializeForEdit(OggettoBulk bulk) {

        Inventario_beniBulk bene = (Inventario_beniBulk) bulk;
        bene.setValore_unitario(bene.getVariazione_meno());
        return bene;
    }

    /**
     * Abilita la riga di dettaglio se il bene è impostato come visibile, (FL_VISIBILE = 'Y').
     *	Il flag visibile è impostato come FALSE se il bene è un bene accessorio scaricato in
     *	seguito allo scarico del suo bene padre.
     *
     * @param row il <code>Object</code> bene sulla riga
     *
     * @return <code>boolean</code>
     **/
    public boolean isRowEnabled(java.lang.Object row) {

        Inventario_beniBulk bene = (Inventario_beniBulk) row;

        if (bene.getFl_visibile() != null)
            return bene.getFl_visibile().equals("Y");
        return true;
    }

    /**
     * isRowReadonly method comment.
     */
    public boolean isRowReadonly(java.lang.Object row) {
        return false;
    }

    @Override
    public String getTableClass() {
        return null;
    }

    @Override
    public void writeTfoot(JspWriter jspwriter) throws IOException {

    }
}
