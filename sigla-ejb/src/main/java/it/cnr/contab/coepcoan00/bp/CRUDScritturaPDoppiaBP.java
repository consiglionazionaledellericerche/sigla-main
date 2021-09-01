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

package it.cnr.contab.coepcoan00.bp;

import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Business Process che gestisce le attività di CRUD per l'oggetto Scrittura_partitadoppiaBulk
 */

public class CRUDScritturaPDoppiaBP extends it.cnr.jada.util.action.SimpleCRUDBP {
    public static final String[] TAB_ECONOMICA = new String[]{"tabEconomica", "Economico/Patrimoniale", "/coepcoan00/tab_doc_economica.jsp"};

    private final SimpleDetailCRUDController movimentiDare = new SimpleDetailCRUDController("MovimentiDare", it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk.class, "movimentiDareColl", this);
    private final SimpleDetailCRUDController movimentiAvere = new SimpleDetailCRUDController("MovimentiAvere", it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk.class, "movimentiAvereColl", this);

    public CRUDScritturaPDoppiaBP() {
        super();
        setTab("tab", "tabScrittura");
    }

    public CRUDScritturaPDoppiaBP(String function) {
        super(function);
        setTab("tab", "tabScrittura");
    }

    /**
     * restituisce il Controller che gestisce la lista dei movimenti avere
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getMovimentiAvere() {
        return movimentiAvere;
    }

    /**
     * restituisce il Controller che gestisce la lista dei movimenti dare
     *
     * @return it.cnr.jada.util.action.SimpleDetailCRUDController
     */
    public final it.cnr.jada.util.action.SimpleDetailCRUDController getMovimentiDare() {
        return movimentiDare;
    }

    /* Stabilisce quando il bottone Elimina della Gestione Scrittura Partita Doppia deve essere
       abilitato */
    public boolean isDeleteButtonEnabled() {
        return super.isDeleteButtonEnabled() &&
                Scrittura_partita_doppiaBulk.ORIGINE_CAUSALE.equals(((Scrittura_partita_doppiaBulk) getModel()).getOrigine_scrittura());
    }

    /* Stabilisce quando il bottone Salva della Gestione Scrittura Partita Doppia deve essere
       abilitato */
    public boolean isSaveButtonEnabled() {
        return isEditable() && isInserting() &&
                Scrittura_partita_doppiaBulk.ORIGINE_CAUSALE.equals(((Scrittura_partita_doppiaBulk) getModel()).getOrigine_scrittura());
    }

    /* Metodo per riportare il fuoco sul tab iniziale */
    protected void resetTabs(ActionContext context) {
        setTab("tab", "tabScrittura");
    }
}
