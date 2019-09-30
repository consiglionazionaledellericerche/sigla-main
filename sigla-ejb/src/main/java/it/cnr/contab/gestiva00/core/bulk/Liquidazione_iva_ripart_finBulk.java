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

package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Liquidazione_iva_ripart_finBulk extends Liquidazione_iva_ripart_finBase {
	private static final long serialVersionUID = 1L;

	private it.cnr.jada.util.OrderedHashtable anniList = new it.cnr.jada.util.OrderedHashtable();

	private WorkpackageBulk linea_di_attivita;

	public Liquidazione_iva_ripart_finBulk() {
		super();
	}
	
	public Liquidazione_iva_ripart_finBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.lang.String tipo_liquidazione,java.sql.Timestamp dt_inizio,java.sql.Timestamp dt_fine,Long pg_dettaglio) {
		super(cd_cds,esercizio,cd_unita_organizzativa,tipo_liquidazione,dt_inizio,dt_fine,pg_dettaglio);
	}

	public WorkpackageBulk getLinea_di_attivita() {
		return linea_di_attivita;
	}
	
	public void setLinea_di_attivita(WorkpackageBulk linea_di_attivita) {
		this.linea_di_attivita = linea_di_attivita;
	}
	
	public String getCd_cdr() {
		return getLinea_di_attivita().getCd_centro_responsabilita();
	}
	
	public String getCd_linea_attivita() {
		return getLinea_di_attivita().getCd_linea_attivita();
	}

	public void setCd_cdr(String cd_cdr) {
		  getLinea_di_attivita().setCd_centro_responsabilita(cd_cdr);
	}
	
	public void setCd_linea_attivita(String cd_linea_attivita) {
		getLinea_di_attivita().setCd_linea_attivita(cd_linea_attivita);
	}

	public it.cnr.jada.util.OrderedHashtable getAnniList() {
		return anniList;
	}

	public void setAnniList(it.cnr.jada.util.OrderedHashtable hashtable) {
		anniList = hashtable;
	}

	public void caricaAnniList(ActionContext actioncontext) { 
		for (int i=CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue()-3;i<=CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue();i++)
			getAnniList().put(new Integer(i), new Integer(i));
	}

	public OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		super.initialize(crudbp, actioncontext);
		caricaAnniList(actioncontext);
		return this; 
	}
	public OggettoBulk initializeForEdit(CRUDBP crudbp, ActionContext actioncontext) {
		caricaAnniList(actioncontext);
		return super.initializeForEdit(crudbp, actioncontext);
	}
}