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
 * Date 18/06/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import java.util.Dictionary;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ass_tipologia_istat_siopeBulk extends Ass_tipologia_istat_siopeBase {
	
	private Codici_siopeBulk codici_siope = new Codici_siopeBulk();
	private Tipologie_istatBulk tipologie_istat = new Tipologie_istatBulk();
	//private Integer esercizio_scrivania;
	
	
	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;
	
	private boolean gia_associato = false;
	
	private final static java.util.Dictionary ti_gestione_siopeKeys;
	static {
		ti_gestione_siopeKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestione_siopeKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestione_siopeKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	
	
	public Ass_tipologia_istat_siopeBulk() {
		super();
	}
	public Ass_tipologia_istat_siopeBulk(java.lang.Integer pg_tipologia, java.lang.Integer esercizio_siope, java.lang.String ti_gestione_siope, java.lang.String cd_siope) {
		super(pg_tipologia, esercizio_siope, ti_gestione_siope, cd_siope);
	}
	
	public Codici_siopeBulk getCodici_siope() {
		return codici_siope;
	}
	
	public void setCodici_siope(Codici_siopeBulk newCodici_siope) {
		codici_siope = newCodici_siope;
	}

	public Tipologie_istatBulk getTipologie_istat() {
		return tipologie_istat;
	}
	
	public void setTipologie_istat(Tipologie_istatBulk newTipologie_istat) {
		tipologie_istat = newTipologie_istat;
	}
	/**
	 * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
	 * @param bp  Business Process <code>CRUDBP</code> in uso.
	 * @param context  <code>ActionContext</code> in uso.
	 * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio_siope(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		return this;
	}
	/**
	 * Metodo per inizializzare l'oggetto bulk in fase di ricerca.
	 * @param bp  Business Process <code>CRUDBP</code> in uso.
	 * @param context  <code>ActionContext</code> in uso.
	 * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
	 */
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio_siope(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		return this;
	}
	
	public boolean isROGiaAssociato() {
		if (tipologie_istat!=null ||codici_siope!=null)
	return true;
		return false;
}
	
	public Dictionary getTi_gestione_siopeKeys() {
		return ti_gestione_siopeKeys;
	}
	
	public java.lang.Integer getEsercizio_siope() {
		Codici_siopeBulk codici_siope = this.getCodici_siope();
		if (codici_siope == null)
			return null;
		return codici_siope.getEsercizio();
	}
	
	public java.lang.String getCd_siope() {
		Codici_siopeBulk codici_siope = this.getCodici_siope();
		if (codici_siope == null)
			return null;
		return codici_siope.getCd_siope();
	}
	public java.lang.String getTi_gestione_siope() {
		Codici_siopeBulk codici_siope = this.getCodici_siope();
		if (codici_siope == null)
			return null;
		return codici_siope.getTi_gestione();
	}
		
	public void setEsercizio_siope(java.lang.Integer esercizio_siope) {
		this.getCodici_siope().setEsercizio(esercizio_siope);
	} 
	public void setCd_siope(java.lang.String cd_siope) {
		this.getCodici_siope().setCd_siope(cd_siope);
	} 
	public void setTi_gestione_siope(java.lang.String ti_gestione_siope) {
		this.getCodici_siope().setTi_gestione(ti_gestione_siope);
	} 
	
	public java.lang.Integer getPg_tipologia() {
		Tipologie_istatBulk tipologia_istat = this.getTipologie_istat();
		if (tipologia_istat == null)
			return null;
		return tipologia_istat.getPg_tipologia();
	}
	public void setPg_tipologia(java.lang.Integer pg_tipologia) {
		this.getTipologie_istat().setPg_tipologia(pg_tipologia);
	}
	/*public Integer getEsercizio_scrivania() {
		return esercizio_scrivania;
	}
	public void setEsercizio_scrivania(Integer esercizio_scrivania) {
		this.esercizio_scrivania = esercizio_scrivania;
	} */
}