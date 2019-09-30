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
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.util.Iterator;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Incarichi_procedura_annoBulk extends Incarichi_procedura_annoBase {
	private Incarichi_proceduraBulk incarichi_procedura = new Incarichi_proceduraBulk();

	private BulkList compensiColl = new BulkList();

	private it.cnr.jada.util.OrderedHashtable anniList = new it.cnr.jada.util.OrderedHashtable();

	public Incarichi_procedura_annoBulk() {
		super();
	}
	public Incarichi_procedura_annoBulk(java.lang.Integer esercizio, java.lang.Long pg_procedura, java.lang.Integer esercizio_limite) {
		super(esercizio, pg_procedura, esercizio_limite);
		setIncarichi_procedura(new Incarichi_proceduraBulk(esercizio,pg_procedura));
	}

	public Incarichi_proceduraBulk getIncarichi_procedura() {
		return incarichi_procedura;
	}
	public void setIncarichi_procedura(
			Incarichi_proceduraBulk incarichi_procedura) {
		this.incarichi_procedura = incarichi_procedura;
	}
	public Integer getEsercizio() {
		if (this.getIncarichi_procedura() == null)
			return null;
		return this.getIncarichi_procedura().getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		if (this.getIncarichi_procedura() != null)
			this.getIncarichi_procedura().setEsercizio(esercizio);
	}	
	public Long getPg_procedura() {
		if (this.getIncarichi_procedura() == null)
			return null;
		return this.getIncarichi_procedura().getPg_procedura();
	}
	public void setPg_procedura(Long pg_procedura) {
		if (this.getIncarichi_procedura() != null)
			this.getIncarichi_procedura().setPg_procedura(pg_procedura);
	}	
	public java.math.BigDecimal getImporto_utilizzato() {
		java.math.BigDecimal totale = new java.math.BigDecimal(0);
		for (Iterator i=this.getCompensiColl().iterator();i.hasNext();)
			totale = totale.add( ((CompensoBulk)i.next()).getIm_totale_compenso());
		return totale;
	}
	public java.math.BigDecimal getImporto_ripartito() {
		java.math.BigDecimal totale = new java.math.BigDecimal(0);
		Incarichi_proceduraBulk procedura = this.getIncarichi_procedura();
		if (procedura != null && procedura.getIncarichi_repertorioValidiColl()!=null) { 
			for (Iterator i=procedura.getIncarichi_repertorioValidiColl().iterator();i.hasNext();) {
				Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)i.next();
				for (Iterator y=incarico.getIncarichi_repertorio_annoColl().iterator();y.hasNext();){
					Incarichi_repertorio_annoBulk incarico_anno = (Incarichi_repertorio_annoBulk)y.next();
					if (incarico_anno.getEsercizio_limite().equals(this.getEsercizio_limite()))
						totale = totale.add( incarico_anno.getImporto_complessivo());
				}
			}
		}
		return totale;
	}
	public it.cnr.jada.util.OrderedHashtable getAnniList() {
		return anniList;
	}
	public void setAnniList(it.cnr.jada.util.OrderedHashtable hashtable) {
		anniList = hashtable;
	}
	public void caricaAnniList(ActionContext actioncontext) {
		caricaAnniList(actioncontext.getUserContext());
	}
	public void caricaAnniList(UserContext usercontext) { 
		for (int i=CNRUserContext.getEsercizio(usercontext).intValue()+5;i>=2005;i--)
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
		return this == null || getIncarichi_procedura() == null ||
		       getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1;
	}
	public boolean isROImporto_complessivo() {
		return this == null || getIncarichi_procedura() == null ||
			   getIncarichi_procedura().isProceduraScaduta() ||	
			   getIncarichi_procedura().isProceduraChiusa() ||			   
	           getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)!=1 ||
	           (getIncarichi_procedura().getNr_contratti().compareTo(1)==1&&
       		    !getIncarichi_procedura().getIncarichi_repertorioValidiColl().isEmpty());
	}
	public BulkList getCompensiColl() {
		return compensiColl;
	}
	public void setCompensiColl(BulkList compensiColl) {
		this.compensiColl = compensiColl;
	}
}