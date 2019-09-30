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
public class Geco_area_progBase extends Geco_area_progKey implements Keyed {
	private static final long serialVersionUID = 1L;

//    cod_area VARCHAR(15) NOT NULL
	private java.lang.String cod_area;
 
//    id_dip DECIMAL(10,0)
	private java.lang.Long id_dip;
 
//    tipo_area DECIMAL(10,0) NOT NULL
	private java.lang.Long tipo_area;
 
//    denom_area VARCHAR(255) NOT NULL
	private java.lang.String denom_area;
 
//    data_istituzione_area TIMESTAMP(7)
	private java.sql.Timestamp data_istituzione_area;
 
//    cod_3rzo_resp VARCHAR(255)
	private java.lang.String cod_3rzo_resp;
 
//    matricola_resp DECIMAL(10,0)
	private java.lang.Long matricola_resp;
 
//    codicefiscale_resp CHAR(16)
	private java.lang.String codicefiscale_resp;
 
//    cognome_resp VARCHAR(255)
	private java.lang.String cognome_resp;
 
//    nome_resp VARCHAR(255)
	private java.lang.String nome_resp;
 
//    telefono_resp VARCHAR(50)
	private java.lang.String telefono_resp;
 
//    email_resp VARCHAR(255)
	private java.lang.String email_resp;
 
//    parola_chiave_1 VARCHAR(255)
	private java.lang.String parola_chiave_1;
 
//    parola_chiave_2 VARCHAR(255)
	private java.lang.String parola_chiave_2;
 
//    parola_chiave_3 VARCHAR(255)
	private java.lang.String parola_chiave_3;
 
//    ordine_area DECIMAL(10,0)
	private java.lang.Long ordine_area;

//    esercizio_inizio_attivita DECIMAL(10,0)
	private java.lang.Long esercizio_inizio_attivita;
 
//    data_inizio_attivita TIMESTAMP(7)
	private java.sql.Timestamp data_inizio_attivita;
 
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
 
//    ordine_pro DECIMAL(10,0) NOT NULL
	private java.lang.Long ordine_pro;
 
//    utente_creatore VARCHAR(255)
	private java.lang.String utente_creatore;
 
//    data_creazione TIMESTAMP(7)
	private java.sql.Timestamp data_creazione;
 
//    ip_creazione VARCHAR(255)
	private java.lang.String ip_creazione;
 
//    utente_ultima_modifica VARCHAR(255)
	private java.lang.String utente_ultima_modifica;
 
//    data_ultima_modifica TIMESTAMP(7)
	private java.sql.Timestamp data_ultima_modifica;
 
//    ip_ultima_modifica VARCHAR(32)
	private java.lang.String ip_ultima_modifica;
 
	public Geco_area_progBase() {
		super();
	}
	public Geco_area_progBase(java.lang.Long id_area, java.lang.Long esercizio, java.lang.String fase) {
		super(id_area, esercizio, fase);
	}
	public java.lang.String getCod_area() {
		return cod_area;
	}
	public void setCod_area(java.lang.String cod_area)  {
		this.cod_area=cod_area;
	}
	public java.lang.Long getId_dip() {
		return id_dip;
	}
	public void setId_dip(java.lang.Long id_dip)  {
		this.id_dip=id_dip;
	}
	public java.lang.Long getTipo_area() {
		return tipo_area;
	}
	public void setTipo_area(java.lang.Long tipo_area)  {
		this.tipo_area=tipo_area;
	}
	public java.lang.String getDenom_area() {
		return denom_area;
	}
	public void setDenom_area(java.lang.String denom_area)  {
		this.denom_area=denom_area;
	}
	public java.sql.Timestamp getData_istituzione_area() {
		return data_istituzione_area;
	}
	public void setData_istituzione_area(java.sql.Timestamp data_istituzione_area)  {
		this.data_istituzione_area=data_istituzione_area;
	}
	public java.lang.String getCod_3rzo_resp() {
		return cod_3rzo_resp;
	}
	public void setCod_3rzo_resp(java.lang.String cod_3rzo_resp)  {
		this.cod_3rzo_resp=cod_3rzo_resp;
	}
	public java.lang.Long getMatricola_resp() {
		return matricola_resp;
	}
	public void setMatricola_resp(java.lang.Long matricola_resp)  {
		this.matricola_resp=matricola_resp;
	}
	public java.lang.String getCodicefiscale_resp() {
		return codicefiscale_resp;
	}
	public void setCodicefiscale_resp(java.lang.String codicefiscale_resp)  {
		this.codicefiscale_resp=codicefiscale_resp;
	}
	public java.lang.String getCognome_resp() {
		return cognome_resp;
	}
	public void setCognome_resp(java.lang.String cognome_resp)  {
		this.cognome_resp=cognome_resp;
	}
	public java.lang.String getNome_resp() {
		return nome_resp;
	}
	public void setNome_resp(java.lang.String nome_resp)  {
		this.nome_resp=nome_resp;
	}
	public java.lang.String getTelefono_resp() {
		return telefono_resp;
	}
	public void setTelefono_resp(java.lang.String telefono_resp)  {
		this.telefono_resp=telefono_resp;
	}
	public java.lang.String getEmail_resp() {
		return email_resp;
	}
	public void setEmail_resp(java.lang.String email_resp)  {
		this.email_resp=email_resp;
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
	public java.lang.Long getOrdine_area() {
		return ordine_area;
	}
	public void setOrdine_area(java.lang.Long ordine_area) {
		this.ordine_area = ordine_area;
	}
	public java.lang.Long getEsercizio_inizio_attivita() {
		return esercizio_inizio_attivita;
	}
	public void setEsercizio_inizio_attivita(java.lang.Long esercizio_inizio_attivita)  {
		this.esercizio_inizio_attivita=esercizio_inizio_attivita;
	}
	public java.sql.Timestamp getData_inizio_attivita() {
		return data_inizio_attivita;
	}
	public void setData_inizio_attivita(java.sql.Timestamp data_inizio_attivita)  {
		this.data_inizio_attivita=data_inizio_attivita;
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
	public java.lang.Long getOrdine_pro() {
		return ordine_pro;
	}
	public void setOrdine_pro(java.lang.Long ordine_pro)  {
		this.ordine_pro=ordine_pro;
	}
	public java.lang.String getUtente_creatore() {
		return utente_creatore;
	}
	public void setUtente_creatore(java.lang.String utente_creatore)  {
		this.utente_creatore=utente_creatore;
	}
	public java.sql.Timestamp getData_creazione() {
		return data_creazione;
	}
	public void setData_creazione(java.sql.Timestamp data_creazione)  {
		this.data_creazione=data_creazione;
	}
	public java.lang.String getIp_creazione() {
		return ip_creazione;
	}
	public void setIp_creazione(java.lang.String ip_creazione)  {
		this.ip_creazione=ip_creazione;
	}
	public java.lang.String getUtente_ultima_modifica() {
		return utente_ultima_modifica;
	}
	public void setUtente_ultima_modifica(java.lang.String utente_ultima_modifica)  {
		this.utente_ultima_modifica=utente_ultima_modifica;
	}
	public java.sql.Timestamp getData_ultima_modifica() {
		return data_ultima_modifica;
	}
	public void setData_ultima_modifica(java.sql.Timestamp data_ultima_modifica)  {
		this.data_ultima_modifica=data_ultima_modifica;
	}
	public java.lang.String getIp_ultima_modifica() {
		return ip_ultima_modifica;
	}
	public void setIp_ultima_modifica(java.lang.String ip_ultima_modifica)  {
		this.ip_ultima_modifica=ip_ultima_modifica;
	}
}