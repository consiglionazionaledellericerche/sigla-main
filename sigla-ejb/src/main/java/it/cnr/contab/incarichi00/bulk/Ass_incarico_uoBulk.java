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
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
public class Ass_incarico_uoBulk extends Ass_incarico_uoBase {
	private Incarichi_repertorioBulk incaricoRepertorio;
	private Unita_organizzativaBulk unita_organizzativa; 
	
	public Ass_incarico_uoBulk() {
		super();
	}
	public Ass_incarico_uoBulk(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.String cd_unita_organizzativa) {
		super(esercizio, pg_repertorio, cd_unita_organizzativa);
		setIncaricoRepertorio(new Incarichi_repertorioBulk(esercizio,pg_repertorio));
		setUnita_organizzativa(new Unita_organizzativaBulk(cd_unita_organizzativa));
	}

	public Incarichi_repertorioBulk getIncaricoRepertorio() {
		return incaricoRepertorio;
	}

	public void setIncaricoRepertorio(Incarichi_repertorioBulk bulk) {
		incaricoRepertorio = bulk;
	}

	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

	public void setUnita_organizzativa(Unita_organizzativaBulk bulk) {
		unita_organizzativa = bulk;
	}

	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getIncaricoRepertorio().setEsercizio(esercizio);
	}

	public java.lang.Integer getEsercizio () {
		return getIncaricoRepertorio().getEsercizio();
	}

	public void setPg_repertorio(java.lang.Long pg_repertorio)  {
		this.getIncaricoRepertorio().setPg_repertorio(pg_repertorio);
	}

	public java.lang.Long getPg_repertorio () {
		return getIncaricoRepertorio().getPg_repertorio();
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}

	public java.lang.String getCd_unita_organizzativa () {
		return getUnita_organizzativa().getCd_unita_organizzativa();
	}
}