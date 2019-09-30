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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 10/09/2007
 */
package it.cnr.contab.incarichi00.bulk;

import java.util.Iterator;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Incarichi_repertorio_annoBulk extends Incarichi_repertorio_annoBase {
	private Incarichi_repertorioBulk incarichi_repertorio = new Incarichi_repertorioBulk();

	private BulkList compensiColl = new BulkList();

	private it.cnr.jada.util.OrderedHashtable anniList = new it.cnr.jada.util.OrderedHashtable();

	public Incarichi_repertorio_annoBulk() {
		super();
	}
	public Incarichi_repertorio_annoBulk(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Integer esercizio_limite) {
		super(esercizio, pg_repertorio, esercizio_limite);
		setIncarichi_repertorio(new Incarichi_repertorioBulk(esercizio,pg_repertorio));
	}

	public Incarichi_repertorioBulk getIncarichi_repertorio() {
		return incarichi_repertorio;
	}
	public void setIncarichi_repertorio(
			Incarichi_repertorioBulk incarichi_repertorio) {
		this.incarichi_repertorio = incarichi_repertorio;
	}
	public Integer getEsercizio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setEsercizio(esercizio);
	}	
	public Long getPg_repertorio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getPg_repertorio();
	}
	public void setPg_repertorio(Long pg_repertorio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setPg_repertorio(pg_repertorio);
	}	
	public java.math.BigDecimal getImporto_utilizzato() {
		java.math.BigDecimal totale = new java.math.BigDecimal(0);
		for (Iterator i=this.getCompensiColl().iterator();i.hasNext();)
			totale = totale.add( ((CompensoBulk)i.next()).getIm_totale_compenso());
		return totale;
	}
	public it.cnr.jada.util.OrderedHashtable getAnniList() {
		return anniList;
	}
	public void setAnniList(it.cnr.jada.util.OrderedHashtable hashtable) {
		anniList = hashtable;
	}
	public void caricaAnniList(ActionContext actioncontext) { 
		for (int i=CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue()+5;i>=2005;i--)
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
	public boolean isROEsercizio_limite() {
		return this == null || this.isNotNew();
	}
	public boolean isROImporto_iniziale() {
		return this == null || getIncarichi_repertorio() == null ||
			   getIncarichi_repertorio().getIncarichi_procedura() == null ||
		       getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1;
	}
	public boolean isROImporto_complessivo() {
		return this == null || getIncarichi_repertorio() == null ||
			   getIncarichi_repertorio().getIncarichi_procedura() == null ||
	           getIncarichi_repertorio().getIncarichi_procedura().isProceduraScaduta() ||
	           getIncarichi_repertorio().isIncaricoChiuso() ||			   
	           getIncarichi_repertorio().isIncaricoAnnullato() ||	           
	           getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)!=1;
	}
	public BulkList getCompensiColl() {
		return compensiColl;
	}
	public void setCompensiColl(BulkList compensiColl) {
		this.compensiColl = compensiColl;
	}
}
