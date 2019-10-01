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
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class CnrIfacFatturaPassivaRigaKey extends OggettoBulk implements KeyedPersistent {
//  CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cdCds;

//  CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;

//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

//  PG_FATTURA_PASSIVA DECIMAL(10,0) NOT NULL
	private java.lang.Long pgFatturaPassiva;

//  PROGRESSIVO_RIGA DECIMAL(10,0) NOT NULL
	private java.lang.Long progressivoRiga;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CNR_IFAC_FATTURA_PASSIVA_RIGA
	 **/
	public CnrIfacFatturaPassivaRigaKey(java.lang.String cdCds,java.lang.String cdUnitaOrganizzativa,java.lang.Integer esercizio,java.lang.Long pgFatturaPassiva,java.lang.Long progressivoRiga) {
		this.cdCds = cdCds;
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
		this.esercizio = esercizio;
		this.pgFatturaPassiva = pgFatturaPassiva;
		this.progressivoRiga = progressivoRiga;
	}
	public CnrIfacFatturaPassivaRigaKey() {
		super();
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof CnrIfacFatturaPassivaRigaKey)) return false;
		CnrIfacFatturaPassivaRigaKey k = (CnrIfacFatturaPassivaRigaKey)o;
		if(!compareKey(getCdCds(),k.getCdCds())) return false;
		if(!compareKey(getCdUnitaOrganizzativa(),k.getCdUnitaOrganizzativa())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getPgFatturaPassiva(),k.getPgFatturaPassiva())) return false;
		if(!compareKey(getProgressivoRiga(),k.getProgressivoRiga())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return
				calculateKeyHashCode(getCdCds())+
				calculateKeyHashCode(getCdUnitaOrganizzativa())+
				calculateKeyHashCode(getEsercizio())+
				calculateKeyHashCode(getPgFatturaPassiva())+
				calculateKeyHashCode(getProgressivoRiga());
	}
	public java.lang.String getCdCds() {
		return cdCds;
	}
	public void setCdCds(java.lang.String cdCds) {
		this.cdCds = cdCds;
	}
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa) {
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public java.lang.Long getPgFatturaPassiva() {
		return pgFatturaPassiva;
	}
	public void setPgFatturaPassiva(java.lang.Long pgFatturaPassiva) {
		this.pgFatturaPassiva = pgFatturaPassiva;
	}
	public java.lang.Long getProgressivoRiga() {
		return progressivoRiga;
	}
	public void setProgressivoRiga(java.lang.Long progressivoRiga) {
		this.progressivoRiga = progressivoRiga;
	}
}