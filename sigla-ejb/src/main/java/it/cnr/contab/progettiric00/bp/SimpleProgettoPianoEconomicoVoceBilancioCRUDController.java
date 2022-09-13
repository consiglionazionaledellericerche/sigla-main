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

import java.io.IOException;
import java.util.Optional;

import javax.servlet.jsp.JspWriter;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.jsp.TableCustomizer;

public class SimpleProgettoPianoEconomicoVoceBilancioCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController implements TableCustomizer {
	public SimpleProgettoPianoEconomicoVoceBilancioCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException
	{
		Progetto_piano_economicoBulk pianoEco = (Progetto_piano_economicoBulk)getParentModel();
		Ass_progetto_piaeco_voceBulk assVocePiaeco = (Ass_progetto_piaeco_voceBulk)detail;
		if (Optional.ofNullable(pianoEco.getVoce_piano_economico()).map(Voce_piano_economico_prgBulk::getFl_link_vocibil_associate).orElse(Boolean.FALSE)) {
			if (Optional.ofNullable(assVocePiaeco.getElemento_voce()).flatMap(el->Optional.ofNullable(el.getCd_voce_piano())).isPresent())
				throw new ValidationException("Scollegamento non possibile! La voce di bilancio "+assVocePiaeco.getElemento_voce().getCd_elemento_voce()+" del "+assVocePiaeco.getElemento_voce().getEsercizio()+" deve essere collegata obbligatoriamente alla voce del piano economico!");
		}
	}
	
    @Override
    public boolean isGrowable() {
        return super.isGrowable()
                && Optional.ofNullable(getParentController().getModel())
                			.filter(Progetto_piano_economicoBulk.class::isInstance)
                			.map(Progetto_piano_economicoBulk.class::cast)
                			.flatMap(el->Optional.ofNullable(el.getVoce_piano_economico()))
                			.map(Voce_piano_economico_prgBulk::getFl_add_vocibil)
                			.orElse(Boolean.FALSE);
    }

    @Override
    public boolean isShrinkable() {
        return super.isShrinkable()
                && Optional.ofNullable(getParentController().getModel())
			    			.filter(Progetto_piano_economicoBulk.class::isInstance)
			    			.map(Progetto_piano_economicoBulk.class::cast)
			    			.flatMap(el->Optional.ofNullable(el.getVoce_piano_economico()))
			    			.map(Voce_piano_economico_prgBulk::getFl_add_vocibil)
			    			.orElse(Boolean.FALSE);
    }

	@Override
	public String getRowStyle(Object obj) {
		return null;
	}

	@Override
	public String getRowCSSClass(Object obj, boolean even) {
		return null;
	};

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
		return null;
	}

	@Override
	public void writeTfoot(JspWriter jspwriter) throws IOException {
	}
}