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
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.bulk;

import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_limiteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;

public class Repertorio_limitiBulk extends Repertorio_limitiBase {
	private Tipo_limiteBulk tipo_limite;
	private BulkList incarichi_x_cdsColl = new BulkList();
	private BulkList incarichi_x_terzoColl = new BulkList();
	private BulkList incarichi_scadutiColl = new BulkList();
	private BulkList incarichi_validiColl = new BulkList();
	
	public Repertorio_limitiBulk() {
		super();
	}
	public Repertorio_limitiBulk(java.lang.Integer esercizio, java.lang.String cd_tipo_limite) {
		super(esercizio, cd_tipo_limite);
		setTipo_limite(new Tipo_limiteBulk(cd_tipo_limite));
	}
	public Tipo_limiteBulk getTipo_limite() {
		return tipo_limite;
	}
	public void setTipo_limite(Tipo_limiteBulk tipo_limite) {
		this.tipo_limite = tipo_limite;
	}
	public java.lang.String getCd_tipo_limite() {
		if (getTipo_limite()==null) return null;
		return getTipo_limite().getCd_tipo_limite();
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite) {
		if (getTipo_limite()!=null) 
			getTipo_limite().setCd_tipo_limite(cd_tipo_limite);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_raggiunto_limite(Boolean.FALSE);
		setImporto_limite(Utility.ZERO);
		setImporto_utilizzato(Utility.ZERO);
		return this;
	}
	public boolean isUtilizzato(){
		return getImporto_utilizzato().compareTo(Utility.ZERO)!=0;
	}
	/**
	 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
	 * controllo sintattico o contestuale.
	 */
	public void validate() throws ValidationException {
		super.validate();

		if ( getImporto_limite().compareTo(getImporto_utilizzato())==-1 )
			throw new ValidationException("Non è possibile inserire un importo inferiore a quello utilizzato.");		
	}
	public BulkList getIncarichi_x_cdsColl() {
		return incarichi_x_cdsColl;
	}
	public void setIncarichi_x_cdsColl(BulkList incarichi_x_cdsColl) {
		this.incarichi_x_cdsColl = incarichi_x_cdsColl;
	}
	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				 incarichi_x_cdsColl,
				 incarichi_x_terzoColl,
				 incarichi_scadutiColl,
				 incarichi_validiColl  };
	}
	public java.math.BigDecimal getTot_incarichi_x_cds(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_x_cdsColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_cdsBulk)i.next()).getIm_incarichi());
		return totale;
	}
	public java.math.BigDecimal getTot_prc_x_cds(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_x_cdsColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_cdsBulk)i.next()).getPrc_utilizzato());
		return totale;
	}
	public BulkList getIncarichi_x_terzoColl() {
		return incarichi_x_terzoColl;
	}
	public void setIncarichi_x_terzoColl(BulkList incarichi_x_terzoColl) {
		this.incarichi_x_terzoColl = incarichi_x_terzoColl;
	}
	public BulkList getIncarichi_scadutiColl() {
		return incarichi_scadutiColl;
	}
	public void setIncarichi_scadutiColl(BulkList incarichi_scadutiColl) {
		this.incarichi_scadutiColl = incarichi_scadutiColl;
	}
	public BulkList getIncarichi_validiColl() {
		return incarichi_validiColl;
	}
	public void setIncarichi_validiColl(BulkList incarichi_validiColl) {
		this.incarichi_validiColl = incarichi_validiColl;
	}
	public java.math.BigDecimal getTot_incarichi_assegnati(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_x_terzoColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_terzoBulk)i.next()).getIm_incarichi());
		return totale;
	}
	public java.math.BigDecimal getTot_incarichi_validi(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_validiColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_da_assegnareBulk)i.next()).getIm_incarichi());
		return totale;
	}
	public java.math.BigDecimal getTot_incarichi_scaduti(){
		java.math.BigDecimal totale = new BigDecimal(0);
		for (Iterator i = this.getIncarichi_scadutiColl().iterator();i.hasNext();)
			totale = totale.add(((V_incarichi_da_assegnareBulk)i.next()).getIm_incarichi());
		return totale;
	}
}