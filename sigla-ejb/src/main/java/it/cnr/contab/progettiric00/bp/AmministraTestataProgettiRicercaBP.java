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

package it.cnr.contab.progettiric00.bp;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.jsp.Button;

import java.util.Optional;

public class AmministraTestataProgettiRicercaBP extends TestataProgettiRicercaBP {
    public AmministraTestataProgettiRicercaBP() {
    }

    public AmministraTestataProgettiRicercaBP(String function) {
        super(function);
    }

    @Override
    protected boolean isROProgettoPianoEconomico(ProgettoBulk progettoBulk) {
        return Boolean.FALSE;
    }

    @Override
    public void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
        super.basicEdit(actioncontext, oggettobulk, flag);
        this.setStatus(EDIT);
        amministra();
    }

    @Override
    public void update(ActionContext actioncontext) throws BusinessProcessException {
        amministra();
        super.update(actioncontext);
    }

    @Override
    public void create(ActionContext actioncontext) throws BusinessProcessException {
        amministra();
        super.create(actioncontext);
    }

    private void amministra() {
        Optional.ofNullable(getModel())
                .filter(ProgettoBulk.class::isInstance)
                .map(ProgettoBulk.class::cast)
                .ifPresent(progettoBulk -> progettoBulk.setAmministra(true));
    }
    @Override
    protected Button[] createToolbar() {
        Button[] toolbar = super.createToolbar();
        Button[] newToolbar = new Button[toolbar.length + 1];
        int i;
        for (i = 0; i < toolbar.length; i++)
            newToolbar[i] = toolbar[i];
        newToolbar[i] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.removePianoEconomico");
        newToolbar[i].setSeparator(true);

        return newToolbar;
    }

    /*
        Il bottone viene visualizzato solo se:
        1) esiste piano economico
        2) il progetto è in stato approvato
     */
    public boolean isRemovePianoEconomicoButtonHidden() {
        return Optional.ofNullable(this.getModel())
                .filter(ProgettoBulk.class::isInstance)
                .map(ProgettoBulk.class::cast)
                .map(el->el.getAllDetailsProgettoPianoEconomico().isEmpty())
                .orElse(Boolean.TRUE) ||
               Optional.ofNullable(this.getModel())
                .filter(ProgettoBulk.class::isInstance)
                .map(ProgettoBulk.class::cast)
                .flatMap(el -> Optional.ofNullable(el.getOtherField()))
                .map(el->!el.isStatoApprovato()).orElse(Boolean.TRUE);
    }
}
