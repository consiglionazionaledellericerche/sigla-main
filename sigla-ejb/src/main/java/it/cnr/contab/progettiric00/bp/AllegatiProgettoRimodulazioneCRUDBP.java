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

import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.AllegatoProgettoRimodulazioneBulk;
import it.cnr.contab.util00.bp.AllegatiTypeCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;

public abstract class AllegatiProgettoRimodulazioneCRUDBP<T extends AllegatoGenericoTypeBulk, K extends AllegatoParentBulk> extends AllegatiTypeCRUDBP<T,K> {
    private static final long serialVersionUID = 1L;

    public AllegatiProgettoRimodulazioneCRUDBP() {
        super();
    }

    public AllegatiProgettoRimodulazioneCRUDBP(String s) {
        super(s);
    }

    @Override
    protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
    	if (Optional.ofNullable(allegato).map(AllegatoProgettoRimodulazioneBulk.class::isInstance).orElse(Boolean.FALSE))
    		return !allegato.getDaNonEliminare() && super.isPossibileCancellazione(allegato);
   		return super.isPossibileCancellazione(allegato);
    }
    
    @Override
    protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
    	if (Optional.ofNullable(allegato).map(AllegatoProgettoRimodulazioneBulk.class::isInstance).orElse(Boolean.FALSE))
    		return !allegato.getDaNonEliminare() && super.isPossibileModifica(allegato);
   		return super.isPossibileModifica(allegato);
    }
}
