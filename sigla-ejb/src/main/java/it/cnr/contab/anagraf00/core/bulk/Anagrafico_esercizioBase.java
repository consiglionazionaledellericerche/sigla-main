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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Anagrafico_esercizioBase extends Anagrafico_esercizioKey implements Keyed {
	// FL_NOTAXAREA CHAR(1) NOT NULL
	private java.lang.Boolean fl_notaxarea;

	// FL_NOFAMILYAREA CHAR(1) NOT NULL
	private java.lang.Boolean fl_nofamilyarea;

	// IM_DETRAZIONE_PERSONALE_ANAG DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_detrazione_personale_anag;

	// IM_DEDUZIONE_FAMILY_AREA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_deduzione_family_area;

	// IM_REDDITO_COMPLESSIVO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_complessivo;

	// IM_REDDITO_ABITAZ_PRINC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_abitaz_princ;
	
	private Boolean fl_no_detrazioni_altre;
	
	private Boolean fl_no_detrazioni_family;

	private Boolean fl_applica_detr_pers_max;
	
	private Boolean fl_no_credito_irpef;

	private Boolean fl_no_credito_cuneo_irpef;

	private Boolean fl_no_detr_cuneo_irpef;

	private Boolean fl_detrazioni_altri_tipi;

	private java.lang.Integer esercizio_voce_ep;

	private java.lang.String cd_voce_ep;
	
public Anagrafico_esercizioBase() {
	super();
}
public Anagrafico_esercizioBase(java.lang.Integer cd_anag,java.lang.Integer esercizio) {
	super(cd_anag,esercizio);
}
/* 
 * Getter dell'attributo fl_notaxarea
 */
public java.lang.Boolean getFl_notaxarea() {
	return fl_notaxarea;
}
/* 
 * Getter dell'attributo im_detrazione_personale_anag
 */
public java.math.BigDecimal getIm_detrazione_personale_anag() {
	return im_detrazione_personale_anag;
}
/* 
 * Setter dell'attributo fl_notaxarea
 */
public void setFl_notaxarea(java.lang.Boolean fl_notaxarea) {
	this.fl_notaxarea = fl_notaxarea;
}
/* 
 * Setter dell'attributo im_detrazione_personale_anag
 */
public void setIm_detrazione_personale_anag(java.math.BigDecimal im_detrazione_personale_anag) {
	this.im_detrazione_personale_anag = im_detrazione_personale_anag;
}
	/**
	 * @return
	 */
	public java.lang.Boolean getFl_nofamilyarea() {
		return fl_nofamilyarea;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_nofamilyarea(java.lang.Boolean boolean1) {
		fl_nofamilyarea = boolean1;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getIm_deduzione_family_area() {
		return im_deduzione_family_area;
	}

	/**
	 * @param decimal
	 */
	public void setIm_deduzione_family_area(java.math.BigDecimal decimal) {
		im_deduzione_family_area = decimal;
	}
	public Boolean getFl_no_detrazioni_altre() {
		return fl_no_detrazioni_altre;
	}
	public void setFl_no_detrazioni_altre(Boolean fl_no_detrazioni_altre) {
		this.fl_no_detrazioni_altre = fl_no_detrazioni_altre;
	}
	public Boolean getFl_no_detrazioni_family() {
		return fl_no_detrazioni_family;
	}
	public void setFl_no_detrazioni_family(Boolean fl_no_detrazioni_family) {
		this.fl_no_detrazioni_family = fl_no_detrazioni_family;
	}
	public java.math.BigDecimal getIm_reddito_complessivo() {
		return im_reddito_complessivo;
	}
	public void setIm_reddito_complessivo(
			java.math.BigDecimal im_reddito_complessivo) {
		this.im_reddito_complessivo = im_reddito_complessivo;
	}
	public Boolean getFl_applica_detr_pers_max() {
		return fl_applica_detr_pers_max;
	}
	public void setFl_applica_detr_pers_max(Boolean fl_applica_detr_pers_max) {
		this.fl_applica_detr_pers_max = fl_applica_detr_pers_max;
	}
	public java.math.BigDecimal getIm_reddito_abitaz_princ() {
		return im_reddito_abitaz_princ;
	}
	public void setIm_reddito_abitaz_princ(
			java.math.BigDecimal im_reddito_abitaz_princ) {
		this.im_reddito_abitaz_princ = im_reddito_abitaz_princ;
	}
	public Boolean getFl_no_credito_irpef() {
		return fl_no_credito_irpef;
	}
	public void setFl_no_credito_irpef(Boolean fl_no_credito_irpef) {
		this.fl_no_credito_irpef = fl_no_credito_irpef;
	}
	public Boolean getFl_detrazioni_altri_tipi() {
		return fl_detrazioni_altri_tipi;
	}
	public void setFl_detrazioni_altri_tipi(Boolean fl_detrazioni_altri_tipi) {
		this.fl_detrazioni_altri_tipi = fl_detrazioni_altri_tipi;
	}
	public Boolean getFl_no_credito_cuneo_irpef() {
		return fl_no_credito_cuneo_irpef;
	}

	public void setFl_no_credito_cuneo_irpef(Boolean fl_no_credito_cuneo_irpef) {
		this.fl_no_credito_cuneo_irpef = fl_no_credito_cuneo_irpef;
	}

	public Boolean getFl_no_detr_cuneo_irpef() {
		return fl_no_detr_cuneo_irpef;
	}

	public void setFl_no_detr_cuneo_irpef(Boolean fl_no_detr_cuneo_irpef) {
		this.fl_no_detr_cuneo_irpef = fl_no_detr_cuneo_irpef;
	}

	public Integer getEsercizio_voce_ep() {
		return esercizio_voce_ep;
	}

	public void setEsercizio_voce_ep(Integer esercizio_voce_ep) {
		this.esercizio_voce_ep = esercizio_voce_ep;
	}

	public String getCd_voce_ep() {
		return cd_voce_ep;
	}

	public void setCd_voce_ep(String cd_voce_ep) {
		this.cd_voce_ep = cd_voce_ep;
	}
}
