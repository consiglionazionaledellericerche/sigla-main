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

/*
 * Created on Mar 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.bp;

import it.cnr.jada.bulk.BulkInfo;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ModuloAttivitaBP extends TestataProgettiRicercaBP {
	public ModuloAttivitaBP() {
		super();
	}
	/**
	 * TestataProgettiRicercaBP constructor comment.
	 * @param function java.lang.String
	 */
	public ModuloAttivitaBP(String function) {
		super(function);
	}

	public BulkInfo getBulkInfo()
	{
		BulkInfo infoBulk = super.getBulkInfo();
		infoBulk.setShortDescription("Moduli di Attività");
		return infoBulk;
	}

	public String getFormTitle()
	{
		StringBuffer stringbuffer = new StringBuffer("Moduli di Attività");
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

		return "filtro_ricerca_moduli";
	}

	public int getLivelloProgetto() {
		return 3;
	}
}
