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
 * Date 06/06/2008
 */
package it.cnr.contab.anagraf00.core.bulk;

import java.sql.Timestamp;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_terzo_sipBulk extends OggettoBulk implements Persistent{
public V_terzo_sipBulk() {
	super();
}
//CD_TERZO DECIMAL(8,0)
private java.lang.Integer cd_terzo;

//CD_ANAG DECIMAL(8,0)
private java.lang.Integer cd_anag;

//DENOMINAZIONE_SEDE VARCHAR(200)
private java.lang.String denominazione_sede;

//TI_ENTITA CHAR(1)
private java.lang.String ti_entita;

//CODICE_FISCALE VARCHAR(20)
private java.lang.String codice_fiscale;

//PARTITA_IVA VARCHAR(20)
private java.lang.String partita_iva;

//NOME VARCHAR(50)
private java.lang.String nome;

//COGNOME VARCHAR(50)
private java.lang.String cognome;

//MATRICOLA DECIMAL(22,0)
private java.lang.Long matricola;

//QUALIFICA VARCHAR(10)
private java.lang.String qualifica;

//livello_1 VARCHAR(3)
private java.lang.String livello_1;

//QUALIFICA VARCHAR(100)
private java.lang.String desc_qualifica;

//TIPO_RAPPORTO VARCHAR(2)
private java.lang.String tipo_rapporto;

//UO VARCHAR(19)
private java.lang.String uo;

//ANNO_RIF DECIMAL(22,0)
private java.lang.Long anno_rif;

//MESE_RIF DECIMAL(22,0)
private java.lang.Long mese_rif;

private Timestamp data_cessazione;

private java.lang.Long fascia;

//PERCENTUALE PART-TIME DECIMAL(5,2)
private java.math.BigDecimal perc_part_time;

private java.math.BigDecimal costo;

public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
public void setCd_terzo(java.lang.Integer cd_terzo)  {
	this.cd_terzo=cd_terzo;
}
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
public void setCd_anag(java.lang.Integer cd_anag)  {
	this.cd_anag=cd_anag;
}
public java.lang.String getDenominazione_sede() {
	return denominazione_sede;
}
public void setDenominazione_sede(java.lang.String denominazione_sede)  {
	this.denominazione_sede=denominazione_sede;
}
public java.lang.String getTi_entita() {
	return ti_entita;
}
public void setTi_entita(java.lang.String ti_entita)  {
	this.ti_entita=ti_entita;
}
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
public void setCodice_fiscale(java.lang.String codice_fiscale)  {
	this.codice_fiscale=codice_fiscale;
}
public java.lang.String getPartita_iva() {
	return partita_iva;
}
public void setPartita_iva(java.lang.String partita_iva)  {
	this.partita_iva=partita_iva;
}
public java.lang.String getNome() {
	return nome;
}
public void setNome(java.lang.String nome)  {
	this.nome=nome;
}
public java.lang.String getCognome() {
	return cognome;
}
public void setCognome(java.lang.String cognome)  {
	this.cognome=cognome;
}
public java.lang.Long getMatricola() {
	return matricola;
}
public void setMatricola(java.lang.Long matricola)  {
	this.matricola=matricola;
}
public java.lang.String getQualifica() {
	return qualifica;
}
public void setQualifica(java.lang.String qualifica)  {
	this.qualifica=qualifica;
}
public java.lang.String getTipo_rapporto() {
	return tipo_rapporto;
}
public void setTipo_rapporto(java.lang.String tipo_rapporto)  {
	this.tipo_rapporto=tipo_rapporto;
}
public java.lang.String getUo() {
	return uo;
}
public void setUo(java.lang.String uo)  {
	this.uo=uo;
}
public java.lang.Long getAnno_rif() {
	return anno_rif;
}
public void setAnno_rif(java.lang.Long anno_rif)  {
	this.anno_rif=anno_rif;
}
public java.lang.Long getMese_rif() {
	return mese_rif;
}
public void setMese_rif(java.lang.Long mese_rif)  {
	this.mese_rif=mese_rif;
}
public Timestamp getData_cessazione() {
	return data_cessazione;
}
public void setData_cessazione(Timestamp data_cessazione) {
	this.data_cessazione = data_cessazione;
}
public java.lang.String getDesc_qualifica() {
	return desc_qualifica;
}
public void setDesc_qualifica(java.lang.String desc_qualifica) {
	this.desc_qualifica = desc_qualifica;
}
public java.lang.String getLivello_1() {
	return livello_1;
}
public void setLivello_1(java.lang.String livello_1) {
	this.livello_1 = livello_1;
}
public java.lang.Long getFascia() {
	return fascia;
}
public void setFascia(java.lang.Long fascia) {
	this.fascia = fascia;
}
public java.math.BigDecimal getPerc_part_time() {
	return perc_part_time;
}
public void setPerc_part_time(java.math.BigDecimal perc_part_time) {
	this.perc_part_time = perc_part_time;
}
public java.math.BigDecimal getCosto() {
	return costo;
}
public void setCosto(java.math.BigDecimal costo) {
	this.costo = costo;
}
}