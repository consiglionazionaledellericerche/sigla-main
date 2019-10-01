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
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.jada.persistency.Keyed;
public class Geco_modulo_rstlBase extends Geco_modulo_rstlKey implements Keyed {
//    cod_mod VARCHAR(30)
	private java.lang.String cod_mod;
 
//    id_comm DECIMAL(10,0) NOT NULL
	private java.lang.Long id_comm;
 
//    tipo_mod DECIMAL(10,0)
	private java.lang.Long tipo_mod;
 
//    descr_mod VARCHAR(255) NOT NULL
	private java.lang.String descr_mod;
 
//    cds_prop CHAR(3)
	private java.lang.String cds_prop;
 
//    cds_esec CHAR(3)
	private java.lang.String cds_esec;
 
//    sede_princ_cdsuo CHAR(7)
	private java.lang.String sede_princ_cdsuo;
 
//    cod_tip DECIMAL(10,0)
	private java.lang.Long cod_tip;
 
//    cod_3rzo_gest VARCHAR(255)
	private java.lang.String cod_3rzo_gest;
 
//    matricola_gest DECIMAL(10,0)
	private java.lang.Long matricola_gest;
 
//    cognome_gest VARCHAR(255)
	private java.lang.String cognome_gest;
 
//    nome_gest VARCHAR(255)
	private java.lang.String nome_gest;
 
//    telefono_gest VARCHAR(50)
	private java.lang.String telefono_gest;
 
//    email_gest VARCHAR(255)
	private java.lang.String email_gest;
 
//    parola_chiave_1 VARCHAR(255)
	private java.lang.String parola_chiave_1;
 
//    parola_chiave_2 VARCHAR(255)
	private java.lang.String parola_chiave_2;
 
//    parola_chiave_3 VARCHAR(255)
	private java.lang.String parola_chiave_3;
 
//    descrittori_1 DECIMAL(10,0)
	private java.lang.Long descrittori_1;
 
//    descrittori_2 DECIMAL(10,0)
	private java.lang.Long descrittori_2;
 
//    descrittori_3 DECIMAL(10,0)
	private java.lang.Long descrittori_3;
 
//    esercizio_proposta DECIMAL(10,0)
	private java.lang.Long esercizio_proposta;
 
//    sercizio_inizio_attivita DECIMAL(10,0)
	private java.lang.Long esercizio_inizio_attivita;
 
//    data_inizio_attivita TIMESTAMP(7)
	private java.sql.Timestamp data_inizio_attivita;
 
//    stato DECIMAL(10,0)
	private java.lang.Long stato;
 
//    stato_att_scie DECIMAL(10,0)
	private java.lang.Long stato_att_scie;
 
//    stato_att_contab DECIMAL(10,0)
	private java.lang.Long stato_att_contab;
 
//    data_fine_attivita_scie TIMESTAMP(7)
	private java.sql.Timestamp data_fine_attivita_scie;
 
//    data_fine_attivita_contab TIMESTAMP(7)
	private java.sql.Timestamp data_fine_attivita_contab;
 
//    esercizio_chiusura DECIMAL(10,0)
	private java.lang.Long esercizio_chiusura;
 
//    codicefiscale_gest CHAR(16)
	private java.lang.String codicefiscale_gest;
 
	public Geco_modulo_rstlBase() {
		super();
	}
	public Geco_modulo_rstlBase(java.lang.Long id_mod, java.lang.Long esercizio, java.lang.String fase) {
		super(id_mod, esercizio, fase);
	}
	public java.lang.String getCod_mod() {
		return cod_mod;
	}
	public void setCod_mod(java.lang.String cod_mod)  {
		this.cod_mod=cod_mod;
	}
	public java.lang.Long getId_comm() {
		return id_comm;
	}
	public void setId_comm(java.lang.Long id_comm)  {
		this.id_comm=id_comm;
	}
	public java.lang.Long getTipo_mod() {
		return tipo_mod;
	}
	public void setTipo_mod(java.lang.Long tipo_mod)  {
		this.tipo_mod=tipo_mod;
	}
	public java.lang.String getDescr_mod() {
		return descr_mod;
	}
	public void setDescr_mod(java.lang.String descr_mod)  {
		this.descr_mod=descr_mod;
	}
	public java.lang.String getCds_prop() {
		return cds_prop;
	}
	public void setCds_prop(java.lang.String cds_prop)  {
		this.cds_prop=cds_prop;
	}
	public java.lang.String getCds_esec() {
		return cds_esec;
	}
	public void setCds_esec(java.lang.String cds_esec)  {
		this.cds_esec=cds_esec;
	}
	public java.lang.String getSede_princ_cdsuo() {
		return sede_princ_cdsuo;
	}
	public void setSede_princ_cdsuo(java.lang.String sede_princ_cdsuo)  {
		this.sede_princ_cdsuo=sede_princ_cdsuo;
	}
	public java.lang.Long getCod_tip() {
		return cod_tip;
	}
	public void setCod_tip(java.lang.Long cod_tip)  {
		this.cod_tip=cod_tip;
	}
	public java.lang.String getCod_3rzo_gest() {
		return cod_3rzo_gest;
	}
	public void setCod_3rzo_gest(java.lang.String cod_3rzo_gest)  {
		this.cod_3rzo_gest=cod_3rzo_gest;
	}
	public java.lang.Long getMatricola_gest() {
		return matricola_gest;
	}
	public void setMatricola_gest(java.lang.Long matricola_gest)  {
		this.matricola_gest=matricola_gest;
	}
	public java.lang.String getCognome_gest() {
		return cognome_gest;
	}
	public void setCognome_gest(java.lang.String cognome_gest)  {
		this.cognome_gest=cognome_gest;
	}
	public java.lang.String getNome_gest() {
		return nome_gest;
	}
	public void setNome_gest(java.lang.String nome_gest)  {
		this.nome_gest=nome_gest;
	}
	public java.lang.String getTelefono_gest() {
		return telefono_gest;
	}
	public void setTelefono_gest(java.lang.String telefono_gest)  {
		this.telefono_gest=telefono_gest;
	}
	public java.lang.String getEmail_gest() {
		return email_gest;
	}
	public void setEmail_gest(java.lang.String email_gest)  {
		this.email_gest=email_gest;
	}
	public java.lang.String getParola_chiave_1() {
		return parola_chiave_1;
	}
	public void setParola_chiave_1(java.lang.String parola_chiave_1)  {
		this.parola_chiave_1=parola_chiave_1;
	}
	public java.lang.String getParola_chiave_2() {
		return parola_chiave_2;
	}
	public void setParola_chiave_2(java.lang.String parola_chiave_2)  {
		this.parola_chiave_2=parola_chiave_2;
	}
	public java.lang.String getParola_chiave_3() {
		return parola_chiave_3;
	}
	public void setParola_chiave_3(java.lang.String parola_chiave_3)  {
		this.parola_chiave_3=parola_chiave_3;
	}
	public java.lang.Long getDescrittori_1() {
		return descrittori_1;
	}
	public void setDescrittori_1(java.lang.Long descrittori_1)  {
		this.descrittori_1=descrittori_1;
	}
	public java.lang.Long getDescrittori_2() {
		return descrittori_2;
	}
	public void setDescrittori_2(java.lang.Long descrittori_2)  {
		this.descrittori_2=descrittori_2;
	}
	public java.lang.Long getDescrittori_3() {
		return descrittori_3;
	}
	public void setDescrittori_3(java.lang.Long descrittori_3)  {
		this.descrittori_3=descrittori_3;
	}
	public java.lang.Long getEsercizio_proposta() {
		return esercizio_proposta;
	}
	public void setEsercizio_proposta(java.lang.Long esercizio_proposta)  {
		this.esercizio_proposta=esercizio_proposta;
	}
	public java.lang.Long getEsercizio_inizio_attivita() {
		return esercizio_inizio_attivita;
	}
	public void setEsercizio_inizio_attivita(java.lang.Long sercizio_inizio_attivita)  {
		this.esercizio_inizio_attivita=sercizio_inizio_attivita;
	}
	public java.sql.Timestamp getData_inizio_attivita() {
		return data_inizio_attivita;
	}
	public void setData_inizio_attivita(java.sql.Timestamp data_inizio_attivita)  {
		this.data_inizio_attivita=data_inizio_attivita;
	}
	public java.lang.Long getStato() {
		return stato;
	}
	public void setStato(java.lang.Long stato)  {
		this.stato=stato;
	}
	public java.lang.Long getStato_att_scie() {
		return stato_att_scie;
	}
	public void setStato_att_scie(java.lang.Long stato_att_scie)  {
		this.stato_att_scie=stato_att_scie;
	}
	public java.lang.Long getStato_att_contab() {
		return stato_att_contab;
	}
	public void setStato_att_contab(java.lang.Long stato_att_contab)  {
		this.stato_att_contab=stato_att_contab;
	}
	public java.sql.Timestamp getData_fine_attivita_scie() {
		return data_fine_attivita_scie;
	}
	public void setData_fine_attivita_scie(java.sql.Timestamp data_fine_attivita_scie)  {
		this.data_fine_attivita_scie=data_fine_attivita_scie;
	}
	public java.sql.Timestamp getData_fine_attivita_contab() {
		return data_fine_attivita_contab;
	}
	public void setData_fine_attivita_contab(java.sql.Timestamp data_fine_attivita_contab)  {
		this.data_fine_attivita_contab=data_fine_attivita_contab;
	}
	public java.lang.Long getEsercizio_chiusura() {
		return esercizio_chiusura;
	}
	public void setEsercizio_chiusura(java.lang.Long esercizio_chiusura)  {
		this.esercizio_chiusura=esercizio_chiusura;
	}
	public java.lang.String getCodicefiscale_gest() {
		return codicefiscale_gest;
	}
	public void setCodicefiscale_gest(java.lang.String codicefiscale_gest)  {
		this.codicefiscale_gest=codicefiscale_gest;
	}
}