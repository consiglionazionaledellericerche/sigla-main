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

import java.util.Dictionary;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.bulk.BulkInfo;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TestataProgettiRicercaNuovoBP extends TestataProgettiRicercaBP implements IProgettoBP{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for TestataProgettiRicercaNuovoBP.
	 */
	public TestataProgettiRicercaNuovoBP() {
		super();
	}

	/**
	 * Constructor for TestataProgettiRicercaNuovoBP.
	 * @param function
	 */
	public TestataProgettiRicercaNuovoBP(String function) {
		super(function);
	}
	
	public BulkInfo getBulkInfo()
	{
		BulkInfo infoBulk = super.getBulkInfo();
		if (isFlNuovoPdg())
			infoBulk.setShortDescription(ProgettoBulk.LABEL_AREA_PROGETTUALE);
		else	
			infoBulk.setShortDescription("Progetti");
		return infoBulk;
	}	
	/*
	 * Utilizzato per la gestione del titolo
	 * Sovrascrive il metodo si CRUDBP
	 * */
	public String getFormTitle()
	{
		StringBuffer stringbuffer = new StringBuffer();
		if (isFlNuovoPdg())
			stringbuffer = stringbuffer.append(ProgettoBulk.LABEL_AREA_PROGETTUALE);
		else
			stringbuffer = stringbuffer.append("Progetti di Ricerca");
		
		stringbuffer.append(" - ");
		switch(getStatus())
		{
		case 1: // '\001'
			stringbuffer.append("Inserimento");
			break;

		case 2: // '\002'
			stringbuffer.append("Modifica");
			break;

		case 0: // '\0'
			stringbuffer.append("Ricerca");
			break;

		case 5: // '\005'
			stringbuffer.append("Visualizza");
			break;
		}
		return stringbuffer.toString();
	}

	public String getSearchResultColumnSet() {
		return "filtro_ricerca_aree_short";
	}	
	
	@Override
	public String getFreeSearchSet() {
		return "filtro_ricerca_aree_short";
	}

	public int getLivelloProgetto() {
		return ProgettoBulk.LIVELLO_PROGETTO_PRIMO.intValue();
	}

	@Override
	public Dictionary getSearchResultColumns() {
		if (this.isFlNuovoPdg())
			return getModel().getBulkInfo().getColumnFieldPropertyDictionary("filtro_ricerca_aree_short");
		return super.getSearchResultColumns();
	}
	public String getLabelCd_progetto() {
		if (this.isFlNuovoPdg())
			return ProgettoBulk.LABEL_AREA_PROGETTUALE;
		return ProgettoBulk.LABEL_PROGETTO;
	}
}
