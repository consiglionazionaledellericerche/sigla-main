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
 * Date 14/06/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class FatturaPassivaIntraSKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdUnitaOrganizzativa;
	private java.lang.Integer esercizio;
	private java.lang.Long pgFatturaPassiva;
	private java.lang.Long pgRigaIntra;
	private java.lang.Long pgStorico;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_PASSIVA_INTRA_S
	 **/
	public FatturaPassivaIntraSKey() {
		super();
	}
	public FatturaPassivaIntraSKey(java.lang.String cdCds, java.lang.String cdUnitaOrganizzativa, java.lang.Integer esercizio, java.lang.Long pgFatturaPassiva, java.lang.Long pgRigaIntra, java.lang.Long pgStorico) {
		super();
		this.cdCds=cdCds;
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
		this.esercizio=esercizio;
		this.pgFatturaPassiva=pgFatturaPassiva;
		this.pgRigaIntra=pgRigaIntra;
		this.pgStorico=pgStorico;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof FatturaPassivaIntraSKey)) return false;
		FatturaPassivaIntraSKey k = (FatturaPassivaIntraSKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdUnitaOrganizzativa(), k.getCdUnitaOrganizzativa())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPgFatturaPassiva(), k.getPgFatturaPassiva())) return false;
		if (!compareKey(getPgRigaIntra(), k.getPgRigaIntra())) return false;
		if (!compareKey(getPgStorico(), k.getPgStorico())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdUnitaOrganizzativa());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPgFatturaPassiva());
		i = i + calculateKeyHashCode(getPgRigaIntra());
		i = i + calculateKeyHashCode(getPgStorico());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgFatturaPassiva]
	 **/
	public void setPgFatturaPassiva(java.lang.Long pgFatturaPassiva)  {
		this.pgFatturaPassiva=pgFatturaPassiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgFatturaPassiva]
	 **/
	public java.lang.Long getPgFatturaPassiva() {
		return pgFatturaPassiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgRigaIntra]
	 **/
	public void setPgRigaIntra(java.lang.Long pgRigaIntra)  {
		this.pgRigaIntra=pgRigaIntra;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgRigaIntra]
	 **/
	public java.lang.Long getPgRigaIntra() {
		return pgRigaIntra;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgStorico]
	 **/
	public void setPgStorico(java.lang.Long pgStorico)  {
		this.pgStorico=pgStorico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgStorico]
	 **/
	public java.lang.Long getPgStorico() {
		return pgStorico;
	}
}