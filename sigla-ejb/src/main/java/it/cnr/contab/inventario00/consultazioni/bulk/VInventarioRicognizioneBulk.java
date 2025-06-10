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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 05/03/2014
 */
package it.cnr.contab.inventario00.consultazioni.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class VInventarioRicognizioneBulk extends OggettoBulk implements Persistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INVENTARIO_RICOGNIZIONE
	 **/
	public VInventarioRicognizioneBulk() {
		super();
	}
//  DATA_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp data;
//  CATEGORIA VARCHAR(10) NOT NULL
	private java.lang.String categoria;
	
	private java.lang.String gruppo;

//  DS_CATEGORIA VARCHAR(100) NOT NULL
	private java.lang.String dsCategoriaGruppo;

//  UO VARCHAR(30) NOT NULL
	private java.lang.String uo;

//  PG_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pgInventario;

//  NR_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long nrInventario;

//  PROGRESSIVO DECIMAL(10,0) NOT NULL
	private java.lang.Long progressivo;

//  DS_BENE VARCHAR(1000) NOT NULL
	private java.lang.String dsBene;

//  DATA_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataRegistrazione;

//  ETICHETTA VARCHAR(50) NOT NULL
	private java.lang.String etichetta;

//  ESERCIZIO_CARICO_BENE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioCaricoBene;

//  UBICAZIONE VARCHAR(30) NOT NULL
	private java.lang.String ubicazione;

//  DS_UBICAZIONE VARCHAR(300) NOT NULL
	private java.lang.String dsUbicazione;

//  TOTALMENTE_SCARICATO CHAR(1) NOT NULL
	private Boolean totalmenteScaricato;

//  VALORE DECIMAL(22,0)
	private java.math.BigDecimal valore;

	private java.lang.String stato;
	
	private java.lang.Long assegnatario;
	
	private java.lang.String dsAssegnatario;

	private java.sql.Timestamp dataScaricoDef;
	
	// Categoria
	private Categoria_gruppo_inventBulk categoriaForPrint;
	
	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;

	private boolean uOForPrintEnabled;

	public it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk getCategoriaForPrint() {
		return categoriaForPrint;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (21/07/2004 17.02.54)
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
		return uoForPrint;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/12/2002 10.47.40)
	 * @param newCdUOEmittente java.lang.String
	 */
	public boolean isROUoForPrint() {
		return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (21/07/2004 17.02.54)
	 * @return boolean
	 */
	public boolean isUOForPrintEnabled() {
		return uOForPrintEnabled;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (21/07/2004 17.02.54)
	 * @param newCategoriaForPrint it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
	 */
	public void setCategoriaForPrint(it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk newCategoriaForPrint) {
		categoriaForPrint = newCategoriaForPrint;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (21/07/2004 17.02.54)
	 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
		uoForPrint = newUoForPrint;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (21/07/2004 17.02.54)
	 * @param newUOForPrintEnabled boolean
	 */
	public void setUOForPrintEnabled(boolean newUOForPrintEnabled) {
		uOForPrintEnabled = newUOForPrintEnabled;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [categoria]
	 **/
	public java.lang.String getCategoria() {
		return categoria;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [categoria]
	 **/
	public void setCategoria(java.lang.String categoria)  {
		this.categoria=categoria;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [uo]
	 **/
	public java.lang.String getUo() {
		return uo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [uo]
	 **/
	public void setUo(java.lang.String uo)  {
		this.uo=uo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgInventario]
	 **/
	public java.lang.Long getPgInventario() {
		return pgInventario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgInventario]
	 **/
	public void setPgInventario(java.lang.Long pgInventario)  {
		this.pgInventario=pgInventario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrInventario]
	 **/
	public java.lang.Long getNrInventario() {
		return nrInventario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrInventario]
	 **/
	public void setNrInventario(java.lang.Long nrInventario)  {
		this.nrInventario=nrInventario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivo]
	 **/
	public java.lang.Long getProgressivo() {
		return progressivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivo]
	 **/
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsBene]
	 **/
	public java.lang.String getDsBene() {
		return dsBene;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsBene]
	 **/
	public void setDsBene(java.lang.String dsBene)  {
		this.dsBene=dsBene;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataRegistrazione]
	 **/
	public java.sql.Timestamp getDataRegistrazione() {
		return dataRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataRegistrazione]
	 **/
	public void setDataRegistrazione(java.sql.Timestamp dataRegistrazione)  {
		this.dataRegistrazione=dataRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [etichetta]
	 **/
	public java.lang.String getEtichetta() {
		return etichetta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [etichetta]
	 **/
	public void setEtichetta(java.lang.String etichetta)  {
		this.etichetta=etichetta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioCaricoBene]
	 **/
	public java.lang.Integer getEsercizioCaricoBene() {
		return esercizioCaricoBene;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioCaricoBene]
	 **/
	public void setEsercizioCaricoBene(java.lang.Integer esercizioCaricoBene)  {
		this.esercizioCaricoBene=esercizioCaricoBene;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ubicazione]
	 **/
	public java.lang.String getUbicazione() {
		return ubicazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ubicazione]
	 **/
	public void setUbicazione(java.lang.String ubicazione)  {
		this.ubicazione=ubicazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsUbicazione]
	 **/
	public java.lang.String getDsUbicazione() {
		return dsUbicazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsUbicazione]
	 **/
	public void setDsUbicazione(java.lang.String dsUbicazione)  {
		this.dsUbicazione=dsUbicazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totalmenteScaricato]
	 **/
	public Boolean getTotalmenteScaricato() {
		return totalmenteScaricato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totalmenteScaricato]
	 **/
	public void setTotalmenteScaricato(Boolean totalmenteScaricato)  {
		this.totalmenteScaricato=totalmenteScaricato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valore]
	 **/
	public java.math.BigDecimal getValore() {
		return valore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valore]
	 **/
	public void setValore(java.math.BigDecimal valore)  {
		this.valore=valore;
	}
	public java.sql.Timestamp getData() {
		return data;
	}
	public void setData(java.sql.Timestamp data) {
		this.data = data;
	}
	public java.lang.Long getAssegnatario() {
		return assegnatario;
	}
	public void setAssegnatario(java.lang.Long assegnatario) {
		this.assegnatario = assegnatario;
	}
	public java.lang.String getDsAssegnatario() {
		return dsAssegnatario;
	}
	public void setDsAssegnatario(java.lang.String dsAssegnatario) {
		this.dsAssegnatario = dsAssegnatario;
	}
	public java.lang.String getGruppo() {
		return gruppo;
	}
	public void setGruppo(java.lang.String gruppo) {
		this.gruppo = gruppo;
	}
	public java.lang.String getDsCategoriaGruppo() {
		return dsCategoriaGruppo;
	}
	public void setDsCategoriaGruppo(java.lang.String dsCategoriaGruppo) {
		this.dsCategoriaGruppo = dsCategoriaGruppo;
	}
	public java.sql.Timestamp getDataScaricoDef() {
		return dataScaricoDef;
	}
	public void setDataScaricoDef(java.sql.Timestamp dataScaricoDef) {
		this.dataScaricoDef = dataScaricoDef;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public final java.util.Dictionary getStatoKeys() {
		return Inventario_beniBulk.statoKeys;
	}
}