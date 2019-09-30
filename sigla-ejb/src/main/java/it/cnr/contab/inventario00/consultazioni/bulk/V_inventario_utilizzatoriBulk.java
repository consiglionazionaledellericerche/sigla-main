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
 * Date 26/11/2007
 */
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_inventario_utilizzatoriBulk extends OggettoBulk implements Persistent {
	
	public V_inventario_utilizzatoriBulk() {
		super();
	}
//  PG_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_inventario;
 
//    NR_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long nr_inventario;
 
//    PROGRESSIVO DECIMAL(10,0) NOT NULL
	private java.lang.Long progressivo;
 
//    DS_BENE VARCHAR(100) NOT NULL
	private java.lang.String ds_bene;
 
//    CD_CATEGORIA_GRUPPO VARCHAR(10) NOT NULL
	private java.lang.String cd_categoria_gruppo;
 
//    FL_AMMORTAMENTO CHAR(1)
	private java.lang.Boolean fl_ammortamento;
 
//    DS_CONDIZIONE_BENE VARCHAR(100) NOT NULL
	private java.lang.String ds_condizione_bene;
 
//    TIPOLOGIA VARCHAR(11)
	private java.lang.String tipologia;
 
//    VALORE DECIMAL(22,2)
	private java.math.BigDecimal valore;
 
//    FONDO DECIMAL(20,6)
	private java.math.BigDecimal fondo;
 
//    IMPONIBILE DECIMAL(20,6)
	private java.math.BigDecimal imponibile;
 
//    STATO VARCHAR(20)
	private java.lang.String stato;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    CD_UBICAZIONE VARCHAR(30) NOT NULL
	private java.lang.String cd_ubicazione;
 
//    DS_UBICAZIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_ubicazione;
 
//    ASSEGNATARIO VARCHAR(101)
	private java.lang.String assegnatario;
 
//    ETICHETTA VARCHAR(50) NOT NULL
	private java.lang.String etichetta;
 
//    ID_BENE_ORIGINE VARCHAR(30)
	private java.lang.String id_bene_origine;
 
//    CD_BARRE DECIMAL(6,0)
	private java.lang.Integer cd_barre;
 
//    TARGA VARCHAR(8)
	private java.lang.String targa;
 
//    CD_UTILIZZATORE_CDR VARCHAR(30) NOT NULL
	private java.lang.String cd_utilizzatore_cdr;
 
//    DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr;
 
//    CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;
 
//    DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String ds_linea_attivita;
 
	public java.lang.Long getPg_inventario() {
		return pg_inventario;
	}
	public void setPg_inventario(java.lang.Long pg_inventario)  {
		this.pg_inventario=pg_inventario;
	}
	public java.lang.Long getNr_inventario() {
		return nr_inventario;
	}
	public void setNr_inventario(java.lang.Long nr_inventario)  {
		this.nr_inventario=nr_inventario;
	}
	public java.lang.Long getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	public java.lang.String getDs_bene() {
		return ds_bene;
	}
	public void setDs_bene(java.lang.String ds_bene)  {
		this.ds_bene=ds_bene;
	}
	public java.lang.String getCd_categoria_gruppo() {
		return cd_categoria_gruppo;
	}
	public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo)  {
		this.cd_categoria_gruppo=cd_categoria_gruppo;
	}
	public java.lang.Boolean getFl_ammortamento() {
		return fl_ammortamento;
	}
	public void setFl_ammortamento(java.lang.Boolean fl_ammortamento)  {
		this.fl_ammortamento=fl_ammortamento;
	}
	public java.lang.String getDs_condizione_bene() {
		return ds_condizione_bene;
	}
	public void setDs_condizione_bene(java.lang.String ds_condizione_bene)  {
		this.ds_condizione_bene=ds_condizione_bene;
	}
	public java.lang.String getTipologia() {
		return tipologia;
	}
	public void setTipologia(java.lang.String tipologia)  {
		this.tipologia=tipologia;
	}
	public java.math.BigDecimal getValore() {
		return valore;
	}
	public void setValore(java.math.BigDecimal valore)  {
		this.valore=valore;
	}
	public java.math.BigDecimal getFondo() {
		return fondo;
	}
	public void setFondo(java.math.BigDecimal fondo)  {
		this.fondo=fondo;
	}
	public java.math.BigDecimal getImponibile() {
		return imponibile;
	}
	public void setImponibile(java.math.BigDecimal imponibile)  {
		this.imponibile=imponibile;
	}
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_ubicazione() {
		return cd_ubicazione;
	}
	public void setCd_ubicazione(java.lang.String cd_ubicazione)  {
		this.cd_ubicazione=cd_ubicazione;
	}
	public java.lang.String getDs_ubicazione() {
		return ds_ubicazione;
	}
	public void setDs_ubicazione(java.lang.String ds_ubicazione)  {
		this.ds_ubicazione=ds_ubicazione;
	}
	public java.lang.String getAssegnatario() {
		return assegnatario;
	}
	public void setAssegnatario(java.lang.String assegnatario)  {
		this.assegnatario=assegnatario;
	}
	public java.lang.String getEtichetta() {
		return etichetta;
	}
	public void setEtichetta(java.lang.String etichetta)  {
		this.etichetta=etichetta;
	}
	public java.lang.String getId_bene_origine() {
		return id_bene_origine;
	}
	public void setId_bene_origine(java.lang.String id_bene_origine)  {
		this.id_bene_origine=id_bene_origine;
	}
	public java.lang.Integer getCd_barre() {
		return cd_barre;
	}
	public void setCd_barre(java.lang.Integer cd_barre)  {
		this.cd_barre=cd_barre;
	}
	public java.lang.String getTarga() {
		return targa;
	}
	public void setTarga(java.lang.String targa)  {
		this.targa=targa;
	}
	public java.lang.String getCd_utilizzatore_cdr() {
		return cd_utilizzatore_cdr;
	}
	public void setCd_utilizzatore_cdr(java.lang.String cd_utilizzatore_cdr)  {
		this.cd_utilizzatore_cdr=cd_utilizzatore_cdr;
	}
	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getDs_linea_attivita() {
		return ds_linea_attivita;
	}
	public void setDs_linea_attivita(java.lang.String ds_linea_attivita)  {
		this.ds_linea_attivita=ds_linea_attivita;
	}
}