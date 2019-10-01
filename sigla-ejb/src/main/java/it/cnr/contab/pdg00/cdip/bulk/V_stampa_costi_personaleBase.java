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
* Creted by Generator 1.0
* Date 04/04/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_stampa_costi_personaleBase extends OggettoBulk implements Persistent {
//    TIPO VARCHAR(26)
	private java.lang.String tipo;
 
//    ESERCIZIO DECIMAL(22,0)
	private java.lang.Integer esercizio;
 
//    MESE DECIMAL(22,0)
	private java.lang.Integer mese;
 
//    UO VARCHAR(30)
	private java.lang.String uo;
 
//    CDR VARCHAR(30)
	private java.lang.String cdr;
 
//    ALTRA_UO VARCHAR(30)
	private java.lang.String altra_uo;

//	CD_PROGETTO VARCHAR(30)
    private java.lang.String cd_progetto;
 
//	DS_PROGETTO VARCHAR(400)
    private java.lang.String ds_progetto;

//	CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;

//	DS_DIPARTIMENTO VARCHAR(300)
	private java.lang.String ds_dipartimento;

//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(400)
	private java.lang.String ds_commessa;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(400)
	private java.lang.String ds_modulo;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//    DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String ds_linea_attivita;
 
//    ID_MATRICOLA VARCHAR(10)
	private java.lang.String id_matricola;
 
//    NOMINATIVO VARCHAR(60)
	private java.lang.String nominativo;

//	DS_PROFILO VARCHAR(60)
    private java.lang.String ds_profilo;
 
//    PERCENT_A1 DECIMAL(22,2)
	private java.math.BigDecimal percent_a1;
 
//    PERCENT_A2 DECIMAL(22,2)
	private java.math.BigDecimal percent_a2;
 
//    PERCENT_A3 DECIMAL(22,2)
	private java.math.BigDecimal percent_a3;

	//  STIPENDIO_A1 DECIMAL(22,2)
	private java.math.BigDecimal stipendio_a1;
 
//    STIPENDIO DECIMAL(22,2)
	private java.math.BigDecimal stipendio;
 
//  CDS VARCHAR(30)
	private java.lang.String cds;
	
//  TI_PREV_CONS VARCHAR(10)
	private java.lang.String ti_prev_cons;
	
//  STIPENDIO DECIMAL(22,2)
	private java.math.BigDecimal costo_mensile;

 
	public V_stampa_costi_personaleBase() {
		super();
	}
	public java.lang.String getTipo () {
		return tipo;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getMese () {
		return mese;
	}
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	public java.lang.String getUo () {
		return uo;
	}
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	public java.lang.String getCdr () {
		return cdr;
	}
	public void setCdr(java.lang.String cdr)  {
		this.cdr=cdr;
	}
	public java.lang.String getAltra_uo () {
		return altra_uo;
	}
	public void setAltra_uo(java.lang.String altra_uo)  {
		this.altra_uo=altra_uo;
	}
	public java.lang.String getCd_commessa () {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa)  {
		this.cd_commessa=cd_commessa;
	}
	public java.lang.String getDs_commessa () {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.String getCd_modulo () {
		return cd_modulo;
	}
	public void setCd_modulo(java.lang.String cd_modulo)  {
		this.cd_modulo=cd_modulo;
	}
	public java.lang.String getDs_modulo () {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo)  {
		this.ds_modulo=ds_modulo;
	}
	public java.lang.String getCd_linea_attivita () {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getDs_linea_attivita () {
		return ds_linea_attivita;
	}
	public void setDs_linea_attivita(java.lang.String ds_linea_attivita)  {
		this.ds_linea_attivita=ds_linea_attivita;
	}
	public java.lang.String getId_matricola () {
		return id_matricola;
	}
	public void setId_matricola(java.lang.String id_matricola)  {
		this.id_matricola=id_matricola;
	}
	public java.lang.String getNominativo () {
		return nominativo;
	}
	public void setNominativo(java.lang.String nominativo)  {
		this.nominativo=nominativo;
	}
	public java.lang.String getCds () {
		return cds;
	}
	public void setCds(java.lang.String cds)  {
		this.cds=cds;
	}
/**
 * @return
 */
public java.lang.String getCd_progetto() {
	return cd_progetto;
}

/**
 * @return
 */
public java.lang.String getDs_profilo() {
	return ds_profilo;
}

/**
 * @return
 */
public java.lang.String getDs_progetto() {
	return ds_progetto;
}

/**
 * @param string
 */
public void setCd_progetto(java.lang.String string) {
	cd_progetto = string;
}

/**
 * @param string
 */
public void setDs_profilo(java.lang.String string) {
	ds_profilo = string;
}

/**
 * @param string
 */
public void setDs_progetto(java.lang.String string) {
	ds_progetto = string;
}

/**
 * @return
 */
public java.lang.String getCd_dipartimento() {
	return cd_dipartimento;
}

/**
 * @param string
 */
public void setCd_dipartimento(java.lang.String string) {
	cd_dipartimento = string;
}

/**
 * @return
 */
public java.math.BigDecimal getPercent_a1() {
	return percent_a1;
}

/**
 * @return
 */
public java.math.BigDecimal getPercent_a2() {
	return percent_a2;
}

/**
 * @return
 */
public java.math.BigDecimal getPercent_a3() {
	return percent_a3;
}

/**
 * @return
 */
public java.math.BigDecimal getStipendio_a1() {
	return stipendio_a1;
}
public java.math.BigDecimal getStipendio() {
	return stipendio;
}

/**
 * @param decimal
 */
public void setPercent_a1(java.math.BigDecimal decimal) {
	percent_a1 = decimal;
}

/**
 * @param decimal
 */
public void setPercent_a2(java.math.BigDecimal decimal) {
	percent_a2 = decimal;
}

/**
 * @param decimal
 */
public void setPercent_a3(java.math.BigDecimal decimal) {
	percent_a3 = decimal;
}

/**
 * @param decimal
 */
public void setStipendio_a1(java.math.BigDecimal decimal) {
	stipendio_a1 = decimal;
}
public void setStipendio(java.math.BigDecimal decimal) {
	stipendio = decimal;
}
public java.lang.String getTi_prev_cons() {
	return ti_prev_cons;
}
public void setTi_prev_cons(java.lang.String ti_prev_cons) {
	this.ti_prev_cons = ti_prev_cons;
}
public java.math.BigDecimal getCosto_mensile() {
	return costo_mensile;
}
public void setCosto_mensile(java.math.BigDecimal costo_mensile) {
	this.costo_mensile = costo_mensile;
}
public java.lang.String getDs_dipartimento() {
	return ds_dipartimento;
}

public void setDs_dipartimento(java.lang.String ds_dipartimento) {
	this.ds_dipartimento = ds_dipartimento;
}
}