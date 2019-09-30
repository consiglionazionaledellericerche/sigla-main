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

package it.cnr.contab.fondecon00.core.bulk;

import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;

public class Filtro_ricerca_obbligazioniVBulk extends it.cnr.jada.bulk.OggettoBulk {

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = null;
	private java.sql.Timestamp data_scadenziario;
	private java.math.BigDecimal im_importo;
	private java.lang.Integer esercizio_ori_obbligazione;
	private java.lang.Long nr_obbligazione;
	private java.lang.Long nr_scadenza;
	private java.lang.Boolean fl_data_scadenziario;
	private java.lang.Boolean fl_fornitore;
	private java.lang.Boolean fl_importo;
	private java.lang.Boolean fl_nr_obbligazione;
	private java.lang.String cd_unita_organizzativa;
	private java.lang.Boolean fl_associate = null;
	private Fondo_economaleBulk fondo = null;
	private java.lang.String tipo_obbligazione;
	public java.lang.String getTipo_obbligazione() {
		return tipo_obbligazione;
	}

	public void setTipo_obbligazione(java.lang.String tipo_obbligazione) {
		this.tipo_obbligazione = tipo_obbligazione;
	}

	public final static Dictionary competenzaResiduoKeys = ObbligazioneBulk.competenzaResiduoKeys;
	/**
	 * Filtro_ricerca_obbligazioniVBulk constructor comment.
	 */
	public Filtro_ricerca_obbligazioniVBulk() {
		super();
	}

	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

public java.sql.Timestamp getCurrentDate() {

	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}	
}

	public java.sql.Timestamp getData_scadenziario() {
		return data_scadenziario;
	}

/**
 * Insert the method's description here.
 * Creation date: (6/19/2002 5:45:02 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_associate() {
	return fl_associate;
}
	public java.lang.Boolean getFl_data_scadenziario() {
		return fl_data_scadenziario;
	}

	public java.lang.Boolean getFl_fornitore() {
		return fl_fornitore;
	}

	public java.lang.Boolean getFl_importo() {
		return fl_importo;
	}

	public java.lang.Boolean getFl_nr_obbligazione() {
		return fl_nr_obbligazione;
	}

/**
 * Insert the method's description here.
 * Creation date: (9/26/2002 5:27:42 PM)
 * @return it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public Fondo_economaleBulk getFondo() {
	return fondo;
}
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getFornitore() {
		return fornitore;
	}

	public java.math.BigDecimal getIm_importo() {
		return im_importo;
	}

	public java.lang.Integer getEsercizio_ori_obbligazione() {
		return esercizio_ori_obbligazione;
	}

	public java.lang.Long getNr_obbligazione() {
		return nr_obbligazione;
	}

/**
 * Insert the method's description here.
 * Creation date: (5/31/2002 12:42:46 PM)
 * @return java.lang.Long
 */
public java.lang.Long getNr_scadenza() {
	return nr_scadenza;
}
	public Filtro_ricerca_obbligazioniVBulk initializeForSearch(
												it.cnr.jada.util.action.BulkBP bp,
												it.cnr.jada.action.ActionContext context)
	{

		setFl_data_scadenziario(Boolean.TRUE);
		setFl_fornitore(Boolean.TRUE);
		setFl_importo(Boolean.FALSE);
		setFl_nr_obbligazione(Boolean.FALSE);
		setFl_associate(Boolean.FALSE);
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
		fornitore.setAnagrafico(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
		setFornitore(fornitore);
		setUser(context.getUserInfo().getUserid());
		setCd_unita_organizzativa(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
		setData_scadenziario(getCurrentDate());
		setIm_importo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		setEsercizio_ori_obbligazione(null);
		setNr_obbligazione(null);
		
		return this;
	}

	public boolean isROdatascadenza() {
		return !getFl_data_scadenziario().booleanValue();
	}

	public boolean isROfornitoretool() {
		return !getFl_fornitore().booleanValue();
	}

	public boolean isROimporto() {
		return !getFl_importo().booleanValue();
	}

	public boolean isROnrobbligazione() {
		return !getFl_nr_obbligazione().booleanValue() ;
	}

	public void setCd_unita_organizzativa(java.lang.String newCd_unita_organizzativa) {
		cd_unita_organizzativa = newCd_unita_organizzativa;
	}

	public void setData_scadenziario(java.sql.Timestamp newDataScadenziario) {
		data_scadenziario = newDataScadenziario;
	}

/**
 * Insert the method's description here.
 * Creation date: (6/19/2002 5:45:02 PM)
 * @param newFl_associate java.lang.Boolean
 */
public void setFl_associate(java.lang.Boolean newFl_associate) {
	fl_associate = newFl_associate;
}
	public void setFl_data_scadenziario(java.lang.Boolean newFl_data_scadenziario) {
		fl_data_scadenziario = newFl_data_scadenziario;
	}

	public void setFl_fornitore(java.lang.Boolean newFl_fornitore) {
		fl_fornitore = newFl_fornitore;
	}

	public void setFl_importo(java.lang.Boolean newFl_importo) {
		fl_importo = newFl_importo;
	}

	public void setFl_nr_obbligazione(java.lang.Boolean newFl_nr_obbligazione) {
		fl_nr_obbligazione = newFl_nr_obbligazione;
	}

/**
 * Insert the method's description here.
 * Creation date: (9/26/2002 5:27:42 PM)
 * @param newFondo it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public void setFondo(Fondo_economaleBulk newFondo) {
	fondo = newFondo;
}
	public void setFornitore(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newFornitore) {
		fornitore = newFornitore;
	}

	public void setIm_importo(java.math.BigDecimal newIm_importo) {
		im_importo = newIm_importo;
	}

	public void setEsercizio_ori_obbligazione(java.lang.Integer newEsercizio_ori_obbligazione) {
		esercizio_ori_obbligazione = newEsercizio_ori_obbligazione;
	}

	public void setNr_obbligazione(java.lang.Long newNr_obbligazione) {
		nr_obbligazione = newNr_obbligazione;
	}

/**
 * Insert the method's description here.
 * Creation date: (5/31/2002 12:42:46 PM)
 * @param newNr_scadenza java.lang.Long
 */
public void setNr_scadenza(java.lang.Long newNr_scadenza) {
	nr_scadenza = newNr_scadenza;
}
}
