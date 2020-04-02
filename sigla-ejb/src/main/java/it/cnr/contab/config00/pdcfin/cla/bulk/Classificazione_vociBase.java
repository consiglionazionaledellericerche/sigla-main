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
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

public class Classificazione_vociBase extends Classificazione_vociKey implements Keyed {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    DS_CLASSIFICAZIONE VARCHAR(250) NOT NULL
	private java.lang.String ds_classificazione;
 
//    CD_LIVELLO1 VARCHAR(4) NOT NULL
	private java.lang.String cd_livello1;
 
//    CD_LIVELLO2 VARCHAR(4)
	private java.lang.String cd_livello2;
 
//    CD_LIVELLO3 VARCHAR(4)
	private java.lang.String cd_livello3;
 
//    CD_LIVELLO4 VARCHAR(4)
	private java.lang.String cd_livello4;
 
//    CD_LIVELLO5 VARCHAR(4)
	private java.lang.String cd_livello5;
 
//    CD_LIVELLO6 VARCHAR(4)
	private java.lang.String cd_livello6;
 
//    CD_LIVELLO7 VARCHAR(4)
	private java.lang.String cd_livello7;
 
//    ID_CLASS_PADRE DECIMAL(7,0)
	private java.lang.Integer id_class_padre;
 
//	  FL_MASTRINO CHAR(1)
	private java.lang.Boolean fl_mastrino;

//	  FL_CLASS_SAC CHAR(1)
    private java.lang.Boolean fl_class_sac;

//	  FL_SOLO_GESTIONE CHAR(1)
    private java.lang.Boolean fl_solo_gestione;

//	  FL_PIANO_RIPARTO CHAR(1)
    private java.lang.Boolean fl_piano_riparto;

//	  FL_ACCENTRATO CHAR(1)
    private java.lang.Boolean fl_accentrato;

//    FL_SOLO_GESTIONE CHAR(1)
    private java.lang.Boolean fl_decentrato;

//    FL_ESTERNA_DA_QUADRARE_SAC CHAR(1)
    private java.lang.Boolean fl_esterna_da_quadrare_sac;

//	  CDR_ACCENTRATORE VARCHAR(30)
    private java.lang.String cdr_accentratore;

//	  TI_CLASSIFICAZIONE CHAR(4)
    private java.lang.String ti_classificazione;
    
    private java.lang.Boolean fl_prev_obb_anno_suc;

	// IM_LIMITE_ASSESTATO DECIMAL(15,2) NULL
	private java.math.BigDecimal im_limite_assestato;

	public Classificazione_vociBase() {
		super();
	}
	public Classificazione_vociBase(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getTi_gestione () {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getDs_classificazione () {
		return ds_classificazione;
	}
	public void setDs_classificazione(java.lang.String ds_classificazione)  {
		this.ds_classificazione=ds_classificazione;
	}
	public java.lang.String getCd_livello1 () {
		return cd_livello1;
	}
	public void setCd_livello1(java.lang.String cd_livello1)  {
		this.cd_livello1=cd_livello1;
	}
	public java.lang.String getCd_livello2 () {
		return cd_livello2;
	}
	public void setCd_livello2(java.lang.String cd_livello2)  {
		this.cd_livello2=cd_livello2;
	}
	public java.lang.String getCd_livello3 () {
		return cd_livello3;
	}
	public void setCd_livello3(java.lang.String cd_livello3)  {
		this.cd_livello3=cd_livello3;
	}
	public java.lang.String getCd_livello4 () {
		return cd_livello4;
	}
	public void setCd_livello4(java.lang.String cd_livello4)  {
		this.cd_livello4=cd_livello4;
	}
	public java.lang.String getCd_livello5 () {
		return cd_livello5;
	}
	public void setCd_livello5(java.lang.String cd_livello5)  {
		this.cd_livello5=cd_livello5;
	}
	public java.lang.String getCd_livello6 () {
		return cd_livello6;
	}
	public void setCd_livello6(java.lang.String cd_livello6)  {
		this.cd_livello6=cd_livello6;
	}
	public java.lang.String getCd_livello7 () {
		return cd_livello7;
	}
	public void setCd_livello7(java.lang.String cd_livello7)  {
		this.cd_livello7=cd_livello7;
	}
	public java.lang.Integer getId_class_padre () {
		return id_class_padre;
	}
	public void setId_class_padre(java.lang.Integer id_class_padre)  {
		this.id_class_padre=id_class_padre;
	}
	public java.lang.Boolean getFl_mastrino() {
		return fl_mastrino;
	}
	public void setFl_mastrino(java.lang.Boolean boolean1) {
		fl_mastrino = boolean1;
	}
	public java.lang.Boolean getFl_class_sac() {
		return fl_class_sac;
	}
	public void setFl_class_sac(java.lang.Boolean boolean1) {
		fl_class_sac = boolean1;
	}
	public java.lang.Boolean getFl_solo_gestione() {
		return fl_solo_gestione;
	}
	public void setFl_solo_gestione(java.lang.Boolean boolean1) {
		fl_solo_gestione = boolean1;
	}
	public java.lang.Boolean getFl_piano_riparto() {
		return fl_piano_riparto;
	}
	public void setFl_piano_riparto(java.lang.Boolean boolean1) {
		fl_piano_riparto = boolean1;
	}
	public java.lang.Boolean getFl_accentrato() {
		return fl_accentrato;
	}
	public void setFl_accentrato(java.lang.Boolean boolean1) {
		fl_accentrato = boolean1;
	}
	public java.lang.Boolean getFl_decentrato() {
		return fl_decentrato;
	}
	public void setFl_decentrato(java.lang.Boolean boolean1) {
		fl_decentrato = boolean1;
	}
	public java.lang.Boolean getFl_esterna_da_quadrare_sac() {
		return fl_esterna_da_quadrare_sac;
	}
	public void setFl_esterna_da_quadrare_sac(java.lang.Boolean boolean1) {
		fl_esterna_da_quadrare_sac = boolean1;
	}
	public java.lang.String getCdr_accentratore() {
		return cdr_accentratore;
	}
	public void setCdr_accentratore(java.lang.String string) {
		cdr_accentratore = string;
	}
	public java.lang.String getTi_classificazione() {
		return ti_classificazione;
	}
	public void setTi_classificazione(java.lang.String ti_classificazione) {
		this.ti_classificazione = ti_classificazione;
	}
	public java.lang.Boolean getFl_prev_obb_anno_suc() {
		return fl_prev_obb_anno_suc;
	}
	public void setFl_prev_obb_anno_suc(java.lang.Boolean fl_prev_obb_anno_suc) {
		this.fl_prev_obb_anno_suc = fl_prev_obb_anno_suc;
	}
	public BigDecimal getIm_limite_assestato() {
		return im_limite_assestato;
	}
	public void setIm_limite_assestato(BigDecimal im_limite_assestato) {
		this.im_limite_assestato = im_limite_assestato;
	}
}