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

package it.cnr.contab.docamm00.docs.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (1/21/2002 4:48:58 PM)
 * @author: Roberto Peli
 */
@JsonInclude(value=Include.NON_NULL)
public class Fattura_attiva_IBulk extends Fattura_attivaBulk {
	@JsonIgnore
	private Nota_di_credito_attivaBulk notaCreditoAutomaticaGenerata = null;
	@JsonIgnore
	private java.util.HashMap storniHashMap = new java.util.HashMap();
	@JsonIgnore
	private java.util.HashMap addebitiHashMap = new java.util.HashMap();
	/**
	 * Fattura_attiva_IBulk constructor comment.
	 */
	public Fattura_attiva_IBulk() {
		super();
	}
	public Fattura_attiva_IBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/21/2002 4:49:43 PM)
	 * @return java.util.HashMap
	 */
	public java.util.HashMap getAddebitiHashMap() {
		return addebitiHashMap;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/11/2002 3:13:39 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public java.lang.Class getChildClass() {
		return Fattura_attiva_rigaIBulk.class;
	}
	public String getManagerName() {
		return "CRUDFatturaAttivaBP";
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/22/2002 4:59:44 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
	 */
	public java.lang.String getManagerOptions() {

		// NON CANCELLARE QUESTO COMMENTO: E' DA ABILITARE AL POSTO DEL RESTO NEL CASO DI APERTURA
		// IN MODIFICA
		//return "MTh";

		return "VTh";
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/21/2002 4:49:43 PM)
	 * @return java.util.HashMap
	 */
	public java.util.HashMap getStorniHashMap() {
		return storniHashMap;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/22/2001 11:59:14 AM)
	 * @return boolean
	 */
	public boolean hasAddebiti() {

		if (addebitiHashMap != null) {
			for (java.util.Iterator i = addebitiHashMap.keySet().iterator(); i.hasNext();) {
				Fattura_attiva_rigaIBulk fpa = (Fattura_attiva_rigaIBulk)i.next();
				java.util.Vector righeNDD = (java.util.Vector)addebitiHashMap.get(fpa);
				if (righeNDD != null) {
					for (java.util.Iterator it = righeNDD.iterator(); it.hasNext();) {
						Nota_di_debito_attiva_rigaBulk nddrow = (Nota_di_debito_attiva_rigaBulk)it.next();
						if (!nddrow.isAnnullato()) return true;
					}
				}
			}
		}
		return  false;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/22/2001 11:59:14 AM)
	 * @return boolean
	 */
	public boolean hasStorni() {

		if (storniHashMap != null) {
			for (java.util.Iterator i = storniHashMap.keySet().iterator(); i.hasNext();) {
				Fattura_attiva_rigaIBulk fpa = (Fattura_attiva_rigaIBulk)i.next();
				java.util.Vector righeNDC = (java.util.Vector)storniHashMap.get(fpa);
				if (righeNDC != null) {
					for (java.util.Iterator it = righeNDC.iterator(); it.hasNext();) {
						Nota_di_credito_attiva_rigaBulk ndcrow = (Nota_di_credito_attiva_rigaBulk)it.next();
						if (!ndcrow.isAnnullato()) return true;
					}
				}
			}
		}	
		return false;
	}
	public OggettoBulk initialize(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initialize(bp, context);

		setTi_fattura(TIPO_FATTURA_ATTIVA);

		return this;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/21/2002 4:49:43 PM)
	 * @param newAddebitiHashMap java.util.HashMap
	 */
	public void setAddebitiHashMap(java.util.HashMap newAddebitiHashMap) {
		addebitiHashMap = newAddebitiHashMap;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/21/2002 4:49:43 PM)
	 * @param newStorniHashMap java.util.HashMap
	 */
	public void setStorniHashMap(java.util.HashMap newStorniHashMap) {
		storniHashMap = newStorniHashMap;
	}
	public Nota_di_credito_attivaBulk getNotaCreditoAutomaticaGenerata() {
		return notaCreditoAutomaticaGenerata;
	}
	public void setNotaCreditoAutomaticaGenerata(
			Nota_di_credito_attivaBulk notaCreditoAutomaticaGenerata) {
		this.notaCreditoAutomaticaGenerata = notaCreditoAutomaticaGenerata;
	}
}
