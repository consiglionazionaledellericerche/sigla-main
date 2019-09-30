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
package it.cnr.contab.config00.pdcep.cla.bulk;
import it.cnr.jada.persistency.Keyed;
public class Classificazione_voci_epBase extends Classificazione_voci_epKey implements Keyed {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    TI_GESTIONE CHAR(3) NOT NULL
	private java.lang.String tipo;
 
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
	
//    CD_LIVELLO8 VARCHAR(4)
	private java.lang.String cd_livello8;
 
//    ID_CLASS_PADRE DECIMAL(7,0)
	private java.lang.Integer id_class_padre;
 
//	  FL_MASTRINO CHAR(1)
	private java.lang.Boolean fl_mastrino;

  public Classificazione_voci_epBase() {
		super();
	}
	public Classificazione_voci_epBase(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getTipo () {
		return tipo;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
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
	public java.lang.String getCd_livello8 () {
		return cd_livello8;
	}
	public void setCd_livello8(java.lang.String cd_livello8)  {
		this.cd_livello8=cd_livello8;
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
}