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
 * Date 31/03/2011
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.persistency.Keyed;
public class VFatcomBlacklistBase extends VFatcomBlacklistKey implements Keyed {
 
//    IMPONIBILE DECIMAL(15,2)
	private java.math.BigDecimal imponibile;
 
//    IVA DECIMAL(15,2)
	private java.math.BigDecimal iva;
 
//    IMP_ESENTE DECIMAL(15,2)
	private java.math.BigDecimal imp_esente;
 
//    IMP_NON_IMP DECIMAL(15,2)
	private java.math.BigDecimal imp_non_imp;
 
//    IMP_NON_SOGGETTO DECIMAL(15,2)
	private java.math.BigDecimal imp_non_soggetto;
 
//    DS_NAZIONE VARCHAR(100)
	private java.lang.String ds_nazione;
 
//    CD_NAZIONE VARCHAR(10)
	private java.lang.String cd_nazione;
  
//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;
 
//    ID_FISCALE_ESTERO VARCHAR(20)
	private java.lang.String id_fiscale_estero;
 
//    PERSONA VARCHAR(10)
	private java.lang.String persona;
 
//    NOME VARCHAR(50)
	private java.lang.String nome;
 
//    COGNOME VARCHAR(50)
	private java.lang.String cognome;
 
//    RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragione_sociale;
 
//    COMUNE_NASCITA VARCHAR(100)
	private java.lang.String comune_nascita;
 
//    DT_NASCITA TIMESTAMP(7)
	private java.sql.Timestamp dt_nascita;
 
//    CD_PROVINCIA VARCHAR(10)
	private java.lang.String cd_provincia;
 
//    ITALIANO_ESTERO CHAR(1)
	private java.lang.String italiano_estero;
 
//    STATO_NASCITA VARCHAR(100)
	private java.lang.String stato_nascita;
 
//    INDIRIZZO_SEDE VARCHAR(120)
	private java.lang.String indirizzo_sede;
 
//    COMUNE_SEDE VARCHAR(100)
	private java.lang.String comune_sede;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_FATCOM_BLACKLIST
	 **/
	public VFatcomBlacklistBase() {
		super();
	}
	public VFatcomBlacklistBase(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.Integer cd_terzo,String tipo,String bene_servizio) {
		super(esercizio, mese, cd_terzo,tipo,bene_servizio);
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imponibile]
	 **/
	public java.math.BigDecimal getImponibile() {
		return imponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imponibile]
	 **/
	public void setImponibile(java.math.BigDecimal imponibile)  {
		this.imponibile=imponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [iva]
	 **/
	public java.math.BigDecimal getIva() {
		return iva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [iva]
	 **/
	public void setIva(java.math.BigDecimal iva)  {
		this.iva=iva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imp_esente]
	 **/
	public java.math.BigDecimal getImp_esente() {
		return imp_esente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imp_esente]
	 **/
	public void setImp_esente(java.math.BigDecimal imp_esente)  {
		this.imp_esente=imp_esente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imp_non_imp]
	 **/
	public java.math.BigDecimal getImp_non_imp() {
		return imp_non_imp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imp_non_imp]
	 **/
	public void setImp_non_imp(java.math.BigDecimal imp_non_imp)  {
		this.imp_non_imp=imp_non_imp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imp_non_soggetto]
	 **/
	public java.math.BigDecimal getImp_non_soggetto() {
		return imp_non_soggetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imp_non_soggetto]
	 **/
	public void setImp_non_soggetto(java.math.BigDecimal imp_non_soggetto)  {
		this.imp_non_soggetto=imp_non_soggetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ds_nazione]
	 **/
	public java.lang.String getDs_nazione() {
		return ds_nazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ds_nazione]
	 **/
	public void setDs_nazione(java.lang.String ds_nazione)  {
		this.ds_nazione=ds_nazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_nazione]
	 **/
	public java.lang.String getCd_nazione() {
		return cd_nazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_nazione]
	 **/
	public void setCd_nazione(java.lang.String cd_nazione)  {
		this.cd_nazione=cd_nazione;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codice_fiscale]
	 **/
	public java.lang.String getCodice_fiscale() {
		return codice_fiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codice_fiscale]
	 **/
	public void setCodice_fiscale(java.lang.String codice_fiscale)  {
		this.codice_fiscale=codice_fiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [id_fiscale_estero]
	 **/
	public java.lang.String getId_fiscale_estero() {
		return id_fiscale_estero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [id_fiscale_estero]
	 **/
	public void setId_fiscale_estero(java.lang.String id_fiscale_estero)  {
		this.id_fiscale_estero=id_fiscale_estero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [persona]
	 **/
	public java.lang.String getPersona() {
		return persona;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [persona]
	 **/
	public void setPersona(java.lang.String persona)  {
		this.persona=persona;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nome]
	 **/
	public java.lang.String getNome() {
		return nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nome]
	 **/
	public void setNome(java.lang.String nome)  {
		this.nome=nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cognome]
	 **/
	public java.lang.String getCognome() {
		return cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognome]
	 **/
	public void setCognome(java.lang.String cognome)  {
		this.cognome=cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ragione_sociale]
	 **/
	public java.lang.String getRagione_sociale() {
		return ragione_sociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ragione_sociale]
	 **/
	public void setRagione_sociale(java.lang.String ragione_sociale)  {
		this.ragione_sociale=ragione_sociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [comune_nascita]
	 **/
	public java.lang.String getComune_nascita() {
		return comune_nascita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [comune_nascita]
	 **/
	public void setComune_nascita(java.lang.String comune_nascita)  {
		this.comune_nascita=comune_nascita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dt_nascita]
	 **/
	public java.sql.Timestamp getDt_nascita() {
		return dt_nascita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dt_nascita]
	 **/
	public void setDt_nascita(java.sql.Timestamp dt_nascita)  {
		this.dt_nascita=dt_nascita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_provincia]
	 **/
	public java.lang.String getCd_provincia() {
		return cd_provincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_provincia]
	 **/
	public void setCd_provincia(java.lang.String cd_provincia)  {
		this.cd_provincia=cd_provincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [italiano_estero]
	 **/
	public java.lang.String getItaliano_estero() {
		return italiano_estero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [italiano_estero]
	 **/
	public void setItaliano_estero(java.lang.String italiano_estero)  {
		this.italiano_estero=italiano_estero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato_nascita]
	 **/
	public java.lang.String getStato_nascita() {
		return stato_nascita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato_nascita]
	 **/
	public void setStato_nascita(java.lang.String stato_nascita)  {
		this.stato_nascita=stato_nascita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indirizzo_sede]
	 **/
	public java.lang.String getIndirizzo_sede() {
		return indirizzo_sede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indirizzo_sede]
	 **/
	public void setIndirizzo_sede(java.lang.String indirizzo_sede)  {
		this.indirizzo_sede=indirizzo_sede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [comune_sede]
	 **/
	public java.lang.String getComune_sede() {
		return comune_sede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [comune_sede]
	 **/
	public void setComune_sede(java.lang.String comune_sede)  {
		this.comune_sede=comune_sede;
	}
}