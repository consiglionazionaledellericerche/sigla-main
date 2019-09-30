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
* Date 30/08/2005
*/
package it.cnr.contab.config00.pdcep.cla.bulk;
import it.cnr.jada.persistency.Keyed;
public class Parametri_livelli_epBase extends Parametri_livelli_epKey implements Keyed {
//    LIVELLI_ECO NUMBER(2) NOT NULL
	private java.lang.Integer livelli_eco;
 
//    LUNG_LIVELLO1E VARCHAR(1)
	private java.lang.Integer lung_livello1e;
 
//    DS_LIVELLO1E VARCHAR(20)
	private java.lang.String ds_livello1e;
 
//    LUNG_LIVELLO2E VARCHAR(1)
	private java.lang.Integer lung_livello2e;
 
//    DS_LIVELLO2E VARCHAR(20)
	private java.lang.String ds_livello2e;
 
//    LUNG_LIVELLO3E VARCHAR(1)
	private java.lang.Integer lung_livello3e;
 
//    DS_LIVELLO3E VARCHAR(20)
	private java.lang.String ds_livello3e;
 
//    LUNG_LIVELLO4E VARCHAR(1)
	private java.lang.Integer lung_livello4e;
 
//    DS_LIVELLO4E VARCHAR(20)
	private java.lang.String ds_livello4e;
 
//    LUNG_LIVELLO5E VARCHAR(1)
	private java.lang.Integer lung_livello5e;
 
//    DS_LIVELLO5E VARCHAR(20)
	private java.lang.String ds_livello5e;
 
//    LUNG_LIVELLO6E VARCHAR(1)
	private java.lang.Integer lung_livello6e;
 
//    DS_LIVELLO6E VARCHAR(20)
	private java.lang.String ds_livello6e;
 
//    LUNG_LIVELLO7E VARCHAR(1)
	private java.lang.Integer lung_livello7e;
 
//    DS_LIVELLO7E VARCHAR(20)
	private java.lang.String ds_livello7e;

//    LUNG_LIVELLO8E VARCHAR(1)
	private java.lang.Integer lung_livello8e;
 
//    DS_LIVELLO8E VARCHAR(20)
	private java.lang.String ds_livello8e;
 
//    LIVELLI_PAT NUMBER(2) NOT NULL
	private java.lang.Integer livelli_pat;
 
//    LUNG_LIVELLO1p VARCHAR(1)
	private java.lang.Integer lung_livello1p;
 
//    DS_LIVELLO1p VARCHAR(20)
	private java.lang.String ds_livello1p;
 
//    LUNG_LIVELLO2p VARCHAR(1)
	private java.lang.Integer lung_livello2p;
 
//    DS_LIVELLO2p VARCHAR(20)
	private java.lang.String ds_livello2p;
 
//    LUNG_LIVELLO3p VARCHAR(1)
	private java.lang.Integer lung_livello3p;
 
//    DS_LIVELLO3p VARCHAR(20)
	private java.lang.String ds_livello3p;
 
//    LUNG_LIVELLO4p VARCHAR(1)
	private java.lang.Integer lung_livello4p;
 
//    DS_LIVELLO4p VARCHAR(20)
	private java.lang.String ds_livello4p;
 
//    LUNG_LIVELLO5p VARCHAR(1)
	private java.lang.Integer lung_livello5p;
 
//    DS_LIVELLO5p VARCHAR(20)
	private java.lang.String ds_livello5p;
 
//    LUNG_LIVELLO6p VARCHAR(1)
	private java.lang.Integer lung_livello6p;
 
//    DS_LIVELLO6p VARCHAR(20)
	private java.lang.String ds_livello6p;
 
//    LUNG_LIVELLO7p VARCHAR(1)
	private java.lang.Integer lung_livello7p;
 
//    DS_LIVELLO7p VARCHAR(20)
	private java.lang.String ds_livello7p; 
 
//    LUNG_LIVELLO8P VARCHAR(1)
	private java.lang.Integer lung_livello8p;
 
//    DS_LIVELLO8P VARCHAR(20)
	private java.lang.String ds_livello8p;
	
	public Parametri_livelli_epBase() {
		super();
	}
	public Parametri_livelli_epBase(java.lang.Integer esercizio) {
		super(esercizio);
	}
	public java.lang.Integer getLivelli_eco () {
		return livelli_eco;
	}
	public void setLivelli_eco(java.lang.Integer livelli_eco)  {
		this.livelli_eco=livelli_eco;
	}
	public java.lang.Integer getLung_livello1e () {
		return lung_livello1e;
	}
	public void setLung_livello1e(java.lang.Integer lung_livello1e)  {
		this.lung_livello1e=lung_livello1e;
	}
	public java.lang.String getDs_livello1e () {
		return ds_livello1e;
	}
	public void setDs_livello1e(java.lang.String ds_livello1e)  {
		this.ds_livello1e=ds_livello1e;
	}
	public java.lang.Integer getLung_livello2e () {
		return lung_livello2e;
	}
	public void setLung_livello2e(java.lang.Integer lung_livello2e)  {
		this.lung_livello2e=lung_livello2e;
	}
	public java.lang.String getDs_livello2e () {
		return ds_livello2e;
	}
	public void setDs_livello2e(java.lang.String ds_livello2e)  {
		this.ds_livello2e=ds_livello2e;
	}
	public java.lang.Integer getLung_livello3e () {
		return lung_livello3e;
	}
	public void setLung_livello3e(java.lang.Integer lung_livello3e)  {
		this.lung_livello3e=lung_livello3e;
	}
	public java.lang.String getDs_livello3e () {
		return ds_livello3e;
	}
	public void setDs_livello3e(java.lang.String ds_livello3e)  {
		this.ds_livello3e=ds_livello3e;
	}
	public java.lang.Integer getLung_livello4e () {
		return lung_livello4e;
	}
	public void setLung_livello4e(java.lang.Integer lung_livello4e)  {
		this.lung_livello4e=lung_livello4e;
	}
	public java.lang.String getDs_livello4e () {
		return ds_livello4e;
	}
	public void setDs_livello4e(java.lang.String ds_livello4e)  {
		this.ds_livello4e=ds_livello4e;
	}
	public java.lang.Integer getLung_livello5e () {
		return lung_livello5e;
	}
	public void setLung_livello5e(java.lang.Integer lung_livello5e)  {
		this.lung_livello5e=lung_livello5e;
	}
	public java.lang.String getDs_livello5e () {
		return ds_livello5e;
	}
	public void setDs_livello5e(java.lang.String ds_livello5e)  {
		this.ds_livello5e=ds_livello5e;
	}
	public java.lang.Integer getLung_livello6e () {
		return lung_livello6e;
	}
	public void setLung_livello6e(java.lang.Integer lung_livello6e)  {
		this.lung_livello6e=lung_livello6e;
	}
	public java.lang.String getDs_livello6e () {
		return ds_livello6e;
	}
	public void setDs_livello6e(java.lang.String ds_livello6e)  {
		this.ds_livello6e=ds_livello6e;
	}
	public java.lang.Integer getLung_livello7e () {
		return lung_livello7e;
	}
	public void setLung_livello7e(java.lang.Integer lung_livello7e)  {
		this.lung_livello7e=lung_livello7e;
	}
	public java.lang.String getDs_livello7e () {
		return ds_livello7e;
	}
	public void setDs_livello7e(java.lang.String ds_livello7e)  {
		this.ds_livello7e=ds_livello7e;
	}
	
	public java.lang.Integer getLung_livello8e () {
		return lung_livello8e;
	}
	public void setLung_livello8e(java.lang.Integer lung_livello8e)  {
		this.lung_livello8e=lung_livello8e;
	}
	public java.lang.String getDs_livello8e () {
		return ds_livello8e;
	}
	public void setDs_livello8e(java.lang.String ds_livello8e)  {
		this.ds_livello8e=ds_livello8e;
	}
	public java.lang.Integer getLivelli_pat () {
		return livelli_pat;
	}
	public void setLivelli_pat(java.lang.Integer livelli_pat)  {
		this.livelli_pat=livelli_pat;
	}
	public java.lang.Integer getLung_livello1p () {
		return lung_livello1p;
	}
	public void setLung_livello1p(java.lang.Integer lung_livello1p)  {
		this.lung_livello1p=lung_livello1p;
	}
	public java.lang.String getDs_livello1p () {
		return ds_livello1p;
	}
	public void setDs_livello1p(java.lang.String ds_livello1p)  {
		this.ds_livello1p=ds_livello1p;
	}
	public java.lang.Integer getLung_livello2p () {
		return lung_livello2p;
	}
	public void setLung_livello2p(java.lang.Integer lung_livello2p)  {
		this.lung_livello2p=lung_livello2p;
	}
	public java.lang.String getDs_livello2p () {
		return ds_livello2p;
	}
	public void setDs_livello2p(java.lang.String ds_livello2p)  {
		this.ds_livello2p=ds_livello2p;
	}
	public java.lang.Integer getLung_livello3p () {
		return lung_livello3p;
	}
	public void setLung_livello3p(java.lang.Integer lung_livello3p)  {
		this.lung_livello3p=lung_livello3p;
	}
	public java.lang.String getDs_livello3p () {
		return ds_livello3p;
	}
	public void setDs_livello3p(java.lang.String ds_livello3p)  {
		this.ds_livello3p=ds_livello3p;
	}
	public java.lang.Integer getLung_livello4p () {
		return lung_livello4p;
	}
	public void setLung_livello4p(java.lang.Integer lung_livello4p)  {
		this.lung_livello4p=lung_livello4p;
	}
	public java.lang.String getDs_livello4p () {
		return ds_livello4p;
	}
	public void setDs_livello4p(java.lang.String ds_livello4p)  {
		this.ds_livello4p=ds_livello4p;
	}
	public java.lang.Integer getLung_livello5p () {
		return lung_livello5p;
	}
	public void setLung_livello5p(java.lang.Integer lung_livello5p)  {
		this.lung_livello5p=lung_livello5p;
	}
	public java.lang.String getDs_livello5p () {
		return ds_livello5p;
	}
	public void setDs_livello5p(java.lang.String ds_livello5p)  {
		this.ds_livello5p=ds_livello5p;
	}
	public java.lang.Integer getLung_livello6p () {
		return lung_livello6p;
	}
	public void setLung_livello6p(java.lang.Integer lung_livello6p)  {
		this.lung_livello6p=lung_livello6p;
	}
	public java.lang.String getDs_livello6p () {
		return ds_livello6p;
	}
	public void setDs_livello6p(java.lang.String ds_livello6p)  {
		this.ds_livello6p=ds_livello6p;
	}
	public java.lang.Integer getLung_livello7p () {
		return lung_livello7p;
	}
	public void setLung_livello7p(java.lang.Integer lung_livello7p)  {
		this.lung_livello7p=lung_livello7p;
	}
	public java.lang.String getDs_livello7p () {
		return ds_livello7p;
	}
	public void setDs_livello7p(java.lang.String ds_livello7p)  {
		this.ds_livello7p=ds_livello7p;
	}
	public java.lang.Integer getLung_livello8p () {
		return lung_livello8p;
	}
	public void setLung_livello8p(java.lang.Integer lung_livello8p)  {
		this.lung_livello8p=lung_livello8p;
	}
	public java.lang.String getDs_livello8p () {
		return ds_livello8p;
	}
	public void setDs_livello8p(java.lang.String ds_livello8p)  {
		this.ds_livello8p=ds_livello8p;
	}
}