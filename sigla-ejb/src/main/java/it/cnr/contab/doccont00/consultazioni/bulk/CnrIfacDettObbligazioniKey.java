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
 * Date 14/03/2016
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class CnrIfacDettObbligazioniKey extends OggettoBulk implements KeyedPersistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CNR_IFAC_DETT_OBBLIGAZIONI
	 **/
//  CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cdCds;

//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

//  PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pgObbligazione;

//  PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pgObbligazioneScadenzario;

//  TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String tiAppartenenza;

//  TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String tiGestione;

//  CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cdVoce;

	// ESERCIZIO_ORIGINALE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioOriginale;

//  CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cdCentroResponsabilita;

//  CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cdLineaAttivita;

//  PG_DOCAMM DECIMAL(10,0)
	private java.lang.Long pgDocamm;

	public CnrIfacDettObbligazioniKey(java.lang.String cdCds,java.lang.String cdCentroResponsabilita,java.lang.String cdLineaAttivita,java.lang.String cdVoce,java.lang.Integer esercizio,java.lang.Integer esercizioOriginale,java.lang.Long pgObbligazione,java.lang.Long pgObbligazioneScadenzario,java.lang.String tiAppartenenza,java.lang.String tiGestione,java.lang.Long pgDocamm) {
		super();
		this.cdCds = cdCds;
		this.cdCentroResponsabilita = cdCentroResponsabilita;
		this.cdLineaAttivita = cdLineaAttivita;
		this.cdVoce = cdVoce;
		this.esercizio = esercizio;
		this.esercizioOriginale = esercizioOriginale;
		this.pgObbligazione = pgObbligazione;
		this.pgObbligazioneScadenzario = pgObbligazioneScadenzario;
		this.tiAppartenenza = tiAppartenenza;
		this.tiGestione = tiGestione;
		this.pgDocamm = pgDocamm;
	}
	public CnrIfacDettObbligazioniKey() {
		super();
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof CnrIfacDettObbligazioniKey)) return false;
		CnrIfacDettObbligazioniKey k = (CnrIfacDettObbligazioniKey)o;
		if(!compareKey(getCdCds(),k.getCdCds())) return false;
		if(!compareKey(getCdCentroResponsabilita(),k.getCdCentroResponsabilita())) return false;
		if(!compareKey(getCdLineaAttivita(),k.getCdLineaAttivita())) return false;
		if(!compareKey(getCdVoce(),k.getCdVoce())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getEsercizioOriginale(),k.getEsercizioOriginale())) return false;
		if(!compareKey(getPgObbligazione(),k.getPgObbligazione())) return false;
		if(!compareKey(getPgObbligazioneScadenzario(),k.getPgObbligazioneScadenzario())) return false;
		if(!compareKey(getTiAppartenenza(),k.getTiAppartenenza())) return false;
		if(!compareKey(getTiGestione(),k.getTiGestione())) return false;
		if(!compareKey(getPgDocamm(),k.getPgDocamm())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return
				calculateKeyHashCode(getCdCds())+
				calculateKeyHashCode(getCdCentroResponsabilita())+
				calculateKeyHashCode(getCdLineaAttivita())+
				calculateKeyHashCode(getCdVoce())+
				calculateKeyHashCode(getEsercizio())+
				calculateKeyHashCode(getEsercizioOriginale())+
				calculateKeyHashCode(getPgObbligazione())+
				calculateKeyHashCode(getPgObbligazioneScadenzario())+
				calculateKeyHashCode(getTiAppartenenza())+
				calculateKeyHashCode(getTiGestione())+
				calculateKeyHashCode(getPgDocamm());
	}
	public java.lang.String getCdCds() {
		return cdCds;
	}
	public void setCdCds(java.lang.String cdCds) {
		this.cdCds = cdCds;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	public void setPgObbligazione(java.lang.Long pgObbligazione) {
		this.pgObbligazione = pgObbligazione;
	}
	public java.lang.Long getPgObbligazioneScadenzario() {
		return pgObbligazioneScadenzario;
	}
	public void setPgObbligazioneScadenzario(
			java.lang.Long pgObbligazioneScadenzario) {
		this.pgObbligazioneScadenzario = pgObbligazioneScadenzario;
	}
	public java.lang.String getTiAppartenenza() {
		return tiAppartenenza;
	}
	public void setTiAppartenenza(java.lang.String tiAppartenenza) {
		this.tiAppartenenza = tiAppartenenza;
	}
	public java.lang.String getTiGestione() {
		return tiGestione;
	}
	public void setTiGestione(java.lang.String tiGestione) {
		this.tiGestione = tiGestione;
	}
	public java.lang.String getCdVoce() {
		return cdVoce;
	}
	public void setCdVoce(java.lang.String cdVoce) {
		this.cdVoce = cdVoce;
	}
	public java.lang.Integer getEsercizioOriginale() {
		return esercizioOriginale;
	}
	public void setEsercizioOriginale(java.lang.Integer esercizioOriginale) {
		this.esercizioOriginale = esercizioOriginale;
	}
	public java.lang.String getCdCentroResponsabilita() {
		return cdCentroResponsabilita;
	}
	public void setCdCentroResponsabilita(java.lang.String cdCentroResponsabilita) {
		this.cdCentroResponsabilita = cdCentroResponsabilita;
	}
	public java.lang.String getCdLineaAttivita() {
		return cdLineaAttivita;
	}
	public void setCdLineaAttivita(java.lang.String cdLineaAttivita) {
		this.cdLineaAttivita = cdLineaAttivita;
	}
	public java.lang.Long getPgDocamm() {
		return pgDocamm;
	}
	public void setPgDocamm(java.lang.Long pgDocamm) {
		this.pgDocamm = pgDocamm;
	}
}