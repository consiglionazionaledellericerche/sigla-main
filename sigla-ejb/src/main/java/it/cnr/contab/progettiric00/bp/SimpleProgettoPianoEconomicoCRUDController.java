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
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.jsp.JspWriter;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.jsp.TableCustomizer;

public class SimpleProgettoPianoEconomicoCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController implements TableCustomizer {

	public SimpleProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	@Override
	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk pianoEco = (Progetto_piano_economicoBulk)oggettobulk;
		BulkList<Ass_progetto_piaeco_voceBulk> vociToBeDelete = new BulkList<Ass_progetto_piaeco_voceBulk>(pianoEco.getVociBilancioAssociate());
		Optional.ofNullable(vociToBeDelete).map(el->el.stream()).orElse(Stream.empty())
		.forEach(e->{
			e.setToBeDeleted();
			pianoEco.removeFromVociBilancioAssociate(pianoEco.getVociBilancioAssociate().indexOf(e));
		});
		return super.removeDetail(oggettobulk, i);
	}
	
	@Override
	public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
		Progetto_piano_economicoBulk pianoEco = (Progetto_piano_economicoBulk)oggettobulk;
		pianoEco.setIm_entrata(BigDecimal.ZERO);
		pianoEco.setFl_ctrl_disp(Boolean.TRUE);
		return super.addDetail(oggettobulk);
	}

	@Override
	public String getRowStyle(Object obj) {
		return null;
	}

	@Override
	public String getRowCSSClass(Object obj, boolean even) {
		return null;
	}

	;

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
