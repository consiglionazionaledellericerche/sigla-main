/*
* Created by Generator 1.0
* Date 29/09/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.persistency.Keyed;
public class Pdg_modulo_speseBase extends Pdg_modulo_speseKey implements Keyed {
//    IM_SPESE_GEST_DECENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_int;
 
//    IM_SPESE_GEST_DECENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_decentrata_est;
 
//    IM_SPESE_GEST_ACCENTRATA_INT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_int;
 
//    IM_SPESE_GEST_ACCENTRATA_EST DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_gest_accentrata_est;
 
//    IM_SPESE_A2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_a2;
 
//    IM_SPESE_A3 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_spese_a3;
	
	private String cd_cofog;
 
	public Pdg_modulo_speseBase() {
		super();
	}
	public Pdg_modulo_speseBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer id_classificazione, java.lang.String cd_cds_area,Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, id_classificazione, cd_cds_area,pg_dettaglio);
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_int () {
		return im_spese_gest_decentrata_int;
	}
	public void setIm_spese_gest_decentrata_int(java.math.BigDecimal im_spese_gest_decentrata_int)  {
		this.im_spese_gest_decentrata_int=im_spese_gest_decentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_decentrata_est () {
		return im_spese_gest_decentrata_est;
	}
	public void setIm_spese_gest_decentrata_est(java.math.BigDecimal im_spese_gest_decentrata_est)  {
		this.im_spese_gest_decentrata_est=im_spese_gest_decentrata_est;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_int () {
		return im_spese_gest_accentrata_int;
	}
	public void setIm_spese_gest_accentrata_int(java.math.BigDecimal im_spese_gest_accentrata_int)  {
		this.im_spese_gest_accentrata_int=im_spese_gest_accentrata_int;
	}
	public java.math.BigDecimal getIm_spese_gest_accentrata_est () {
		return im_spese_gest_accentrata_est;
	}
	public void setIm_spese_gest_accentrata_est(java.math.BigDecimal im_spese_gest_accentrata_est)  {
		this.im_spese_gest_accentrata_est=im_spese_gest_accentrata_est;
	}
	public java.math.BigDecimal getIm_spese_a2 () {
		return im_spese_a2;
	}
	public void setIm_spese_a2(java.math.BigDecimal im_spese_a2)  {
		this.im_spese_a2=im_spese_a2;
	}
	public java.math.BigDecimal getIm_spese_a3 () {
		return im_spese_a3;
	}
	public void setIm_spese_a3(java.math.BigDecimal im_spese_a3)  {
		this.im_spese_a3=im_spese_a3;
	}
	public String getCd_cofog() {
		return cd_cofog;
	}
	public void setCd_cofog(String cd_cofog) {
		this.cd_cofog = cd_cofog;
	}
	
}