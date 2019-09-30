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
public class Geco_attivitaBase extends Geco_attivitaKey implements Keyed {
	private static final long serialVersionUID = 1L;

//    cod_att VARCHAR(30)
	private java.lang.String cod_att;
 
//    id_prog DECIMAL(10,0) NOT NULL
	private java.lang.Long id_prog;
 
//    tipo_att DECIMAL(10,0) NOT NULL
	private java.lang.Long tipo_att;
 
//    denom_att VARCHAR(255) NOT NULL
	private java.lang.String denom_att;
 
//    cds_prop CHAR(3)
	private java.lang.String cds_prop;
 
//    cds_esec CHAR(3)
	private java.lang.String cds_esec;
 
//    sede_princ_cdsuo CHAR(7)
	private java.lang.String sede_princ_cdsuo;
 
//    cod_tip DECIMAL(10,0)
	//private java.lang.Long cod_tip;
 
//    cod_3rzo_refe VARCHAR(255)
	private java.lang.String cod_3rzo_refe;
 
//    matricola_refe DECIMAL(10,0)
	private java.lang.Long matricola_refe;
 
//    cognome_refe VARCHAR(255)
	private java.lang.String cognome_refe;
 
//    nome_refe VARCHAR(255)
	private java.lang.String nome_refe;
 
//    telefono_refe VARCHAR(50)
	private java.lang.String telefono_refe;
 
//    email_refe VARCHAR(255)
	private java.lang.String email_refe;
 
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
 
//    esercizio_inizio_attivita DECIMAL(10,0)
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
 
//    esercizio_chiusura DECIMAL(10,0)
	private java.lang.Long esercizio_chiusura;
 
//    codicefiscale_refe CHAR(16)
	private java.lang.String codicefiscale_refe;
 
//    esito_negoz DECIMAL(5,0)
	private java.lang.Integer esito_negoz;
 
//    uid_refe VARCHAR(255)
	private java.lang.String uid_refe;

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
 
	public Geco_attivitaBase() {
		super();
	}
	public Geco_attivitaBase(java.lang.Long id_att, java.lang.Long esercizio, java.lang.String fase) {
		super(id_att, esercizio, fase);
	}
	public java.lang.String getCod_att() {
		return cod_att;
	}
	public void setCod_att(java.lang.String cod_att)  {
		this.cod_att=cod_att;
	}
	public java.lang.Long getId_prog() {
		return id_prog;
	}
	public void setId_prog(java.lang.Long id_prog)  {
		this.id_prog=id_prog;
	}
	public java.lang.Long getTipo_att() {
		return tipo_att;
	}
	public void setTipo_att(java.lang.Long tipo_att)  {
		this.tipo_att=tipo_att;
	}
	public java.lang.String getDenom_att() {
		return denom_att;
	}
	public void setDenom_att(java.lang.String denom_att)  {
		this.denom_att=denom_att;
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
	
	public java.lang.String getCod_3rzo_refe() {
		return cod_3rzo_refe;
	}
	public void setCod_3rzo_refe(java.lang.String cod_3rzo_refe)  {
		this.cod_3rzo_refe=cod_3rzo_refe;
	}
	public java.lang.Long getMatricola_refe() {
		return matricola_refe;
	}
	public void setMatricola_refe(java.lang.Long matricola_refe)  {
		this.matricola_refe=matricola_refe;
	}
	public java.lang.String getCognome_refe() {
		return cognome_refe;
	}
	public void setCognome_refe(java.lang.String cognome_refe)  {
		this.cognome_refe=cognome_refe;
	}
	public java.lang.String getNome_refe() {
		return nome_refe;
	}
	public void setNome_refe(java.lang.String nome_refe)  {
		this.nome_refe=nome_refe;
	}
	public java.lang.String getTelefono_refe() {
		return telefono_refe;
	}
	public void setTelefono_refe(java.lang.String telefono_refe)  {
		this.telefono_refe=telefono_refe;
	}
	public java.lang.String getEmail_refe() {
		return email_refe;
	}
	public void setEmail_refe(java.lang.String email_refe)  {
		this.email_refe=email_refe;
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
	public void setEsercizio_inizio_attivita(java.lang.Long esercizio_inizio_attivita)  {
		this.esercizio_inizio_attivita=esercizio_inizio_attivita;
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
	public java.lang.Long getEsercizio_chiusura() {
		return esercizio_chiusura;
	}
	public void setEsercizio_chiusura(java.lang.Long esercizio_chiusura)  {
		this.esercizio_chiusura=esercizio_chiusura;
	}
	public java.lang.String getCodicefiscale_refe() {
		return codicefiscale_refe;
	}
	public void setCodicefiscale_refe(java.lang.String codicefiscale_refe)  {
		this.codicefiscale_refe=codicefiscale_refe;
	}
	public java.lang.Integer getEsito_negoz() {
		return esito_negoz;
	}
	public void setEsito_negoz(java.lang.Integer esito_negoz)  {
		this.esito_negoz=esito_negoz;
	}
	public java.lang.String getUid_refe() {
		return uid_refe;
	}
	public void setUid_refe(java.lang.String uid_refe) {
		this.uid_refe = uid_refe;
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