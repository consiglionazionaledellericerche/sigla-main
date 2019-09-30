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

import java.rmi.RemoteException;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;

public class ProgettoPianoEconomicoCRUDController extends SimpleProgettoPianoEconomicoCRUDController {

	public ProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException
	{
		if (!detail.isToBeCreated()) {
			Optional<Progetto_piano_economicoBulk> opt = Optional.ofNullable(detail).filter(Progetto_piano_economicoBulk.class::isInstance)
																 .map(Progetto_piano_economicoBulk.class::cast);
			if (opt.filter(el->el.isROProgettoPianoEconomico()).isPresent())
				throw new ValidationException("Eliminazione non possibile! Il progetto per l'anno di riferimento "
								+ opt.get().getEsercizio_piano() + " risulta essere stato confermato in PdgP!");
			try {
				Utility.createProgettoRicercaComponentSession().validaCancellazionePianoEconomicoAssociato(
					context.getUserContext(),
					(ProgettoBulk)getParentModel(),
					detail);
			} catch (ComponentException e) {
				throw new ValidationException(e.getMessage());
			} catch (RemoteException e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}
}
