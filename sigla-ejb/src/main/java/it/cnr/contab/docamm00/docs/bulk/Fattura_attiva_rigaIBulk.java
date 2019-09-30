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

import it.cnr.contab.doccont00.core.bulk.Reversale_rigaIBulk;
import it.cnr.jada.bulk.BulkList;

/**
 * Insert the type's description here.
 * Creation date: (10/25/2001 11:52:17 AM)
 * @author: Roberto Peli
 */
@JsonInclude(value=Include.NON_NULL)
public class Fattura_attiva_rigaIBulk extends Fattura_attiva_rigaBulk {

	@JsonIgnore
	private Fattura_attiva_IBulk fattura_attivaIBulk;	

	private java.math.BigDecimal saldo = new java.math.BigDecimal(0);
	private java.math.BigDecimal im_totale_storni = new java.math.BigDecimal(0);
	private java.math.BigDecimal im_totale_addebiti = new java.math.BigDecimal(0);

	private java.math.BigDecimal im_riga_sdoppia;

	/*
	 * lista righe reversali associate, utilizzato per l'integrazione con i brevetti
	 */
	private BulkList<Reversale_rigaIBulk> reversaliRighe = new BulkList();
	/**
	 * Fattura_passiva_rigaIBulk constructor comment.
	 */
	public Fattura_attiva_rigaIBulk() {
		super();
	}
	public Fattura_attiva_rigaIBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva,java.lang.Long progressivo_riga) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva,progressivo_riga);
		setFattura_attivaI(new it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva));
	}
	public java.lang.String getCd_cds() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = this.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getCd_cds();
	}
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = this.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getCd_unita_organizzativa();
	}
	public java.lang.Integer getEsercizio() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = this.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getEsercizio();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/25/2001 12:03:34 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk
	 */
	public Fattura_attivaBulk getFattura_attiva() {

		return getFattura_attivaI();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/25/2001 12:03:34 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk
	 */
	@JsonIgnore
	public Fattura_attiva_IBulk getFattura_attivaI() {
		return fattura_attivaIBulk;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/17/2001 2:14:22 PM)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getIm_totale_addebiti() {
		return im_totale_addebiti;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/6/2001 3:20:41 PM)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getIm_totale_storni() {

		return im_totale_storni;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/13/2002 6:15:04 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
	 */
	public IDocumentoAmministrativoRigaBulk getOriginalDetail() {
		return null;
	}
	public java.lang.Long getPg_fattura_attiva() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = this.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getPg_fattura_attiva();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/6/2001 3:38:46 PM)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getSaldo() {

		if (getIm_iva() == null) setIm_iva(new java.math.BigDecimal(0));

		return getIm_imponibile().add(getIm_iva()).subtract(getIm_totale_storni()).add(getIm_totale_addebiti());
	}
	public boolean hasAddebiti() {

		return	getIm_totale_addebiti() != null && 
				getIm_totale_addebiti().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) > 0;
	}
	public boolean hasStorni() {

		return	getIm_totale_storni() != null && 
				getIm_totale_storni().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) > 0;
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.getFattura_attivaI().setCd_cds(cd_cds);
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getFattura_attivaI().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.getFattura_attivaI().setEsercizio(esercizio);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/2001 5:51:50 PM)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
	 */
	public void setFattura_attiva(Fattura_attivaBulk fattura_attiva) {

		setFattura_attivaI((Fattura_attiva_IBulk)fattura_attiva);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/25/2001 12:03:34 PM)
	 * @param newFattura_passivaIBulk it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk
	 */
	@JsonIgnore
	public void setFattura_attivaI(Fattura_attiva_IBulk newFattura_attivaIBulk) {
		fattura_attivaIBulk = newFattura_attivaIBulk;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/17/2001 2:14:22 PM)
	 * @param newIm_totale_addebiti java.math.BigDecimal
	 */
	public void setIm_totale_addebiti(java.math.BigDecimal newIm_totale_addebiti) {
		im_totale_addebiti = newIm_totale_addebiti;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/6/2001 3:20:41 PM)
	 * @param newIm_totale_storni java.math.BigDecimal
	 */
	public void setIm_totale_storni(java.math.BigDecimal newIm_totale_storni) {
		im_totale_storni = newIm_totale_storni;
	}
	public void setPg_fattura_attiva(java.lang.Long pg_fattura_attiva) {
		this.getFattura_attivaI().setPg_fattura_attiva(pg_fattura_attiva);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/6/2001 3:38:46 PM)
	 * @param newSaldo java.math.BigDecimal
	 */
	public void setSaldo(java.math.BigDecimal newSaldo) {
		saldo = newSaldo;
	}

	public java.math.BigDecimal getIm_riga_sdoppia() {
		return im_riga_sdoppia;
	}

	public void setIm_riga_sdoppia(java.math.BigDecimal im_riga_sdoppia) {
		this.im_riga_sdoppia = im_riga_sdoppia;
	}
	public void setReversaliRighe(BulkList<Reversale_rigaIBulk> reversaliRighe) {
		this.reversaliRighe = reversaliRighe;
	}
	public BulkList<Reversale_rigaIBulk> getReversaliRighe() {
		return reversaliRighe;
	}

}
