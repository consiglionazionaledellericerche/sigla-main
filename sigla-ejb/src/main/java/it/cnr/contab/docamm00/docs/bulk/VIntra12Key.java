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
 * Date 26/05/2010
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VIntra12Key extends OggettoBulk implements KeyedPersistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INTRA12
	 **/
//  CD_TIPO_SEZIONALE VARCHAR(10) NOT NULL
	private java.lang.String cdTipoSezionale;
 
//    ESERCIZIO VARCHAR(5) NOT NULL
	private Integer esercizio;
 
//    MESE VARCHAR(2)
	private Integer mese;

//    CD_BENE_SERVIZIO VARCHAR(10)
	private java.lang.String cdBeneServizio;
 
//    FL_INTRA_UE CHAR(1) NOT NULL
	private Boolean flIntraUe;
 
//    FL_EXTRA_UE CHAR(1) NOT NULL
	private Boolean flExtraUe;
	
	public VIntra12Key() {
		super();
	}
	public VIntra12Key(java.lang.String cdTipoSezionale,java.lang.String cdBeneServizio,Integer esercizio,Integer mese,Boolean flExtra,Boolean flIntra) {
		
		super();
		this.cdTipoSezionale = cdTipoSezionale;
		this.cdBeneServizio = cdBeneServizio;
		this.esercizio = esercizio;
		this.mese = mese;
		this.flExtraUe =flExtra;
		this.flIntraUe =flIntra;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof VIntra12Key)) return false;
		VIntra12Key k = (VIntra12Key) o;
		if(!compareKey(getCdTipoSezionale(),k.getCdTipoSezionale())) return false;
		if(!compareKey(getCdBeneServizio(),k.getCdBeneServizio())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getMese(),k.getMese())) return false;
		if(!compareKey(getFlExtraUe(),k.getFlExtraUe())) return false;
		if(!compareKey(getFlIntraUe(),k.getFlIntraUe())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCdTipoSezionale())+
			calculateKeyHashCode(getCdBeneServizio())+
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getMese())+
			calculateKeyHashCode(getFlExtraUe())+
			calculateKeyHashCode(getFlIntraUe());
	}
	public java.lang.String getCdTipoSezionale() {
		return cdTipoSezionale;
	}
	public void setCdTipoSezionale(java.lang.String cdTipoSezionale) {
		this.cdTipoSezionale = cdTipoSezionale;
	}
	public Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}
	public Integer getMese() {
		return mese;
	}
	public void setMese(Integer mese) {
		this.mese = mese;
	}
	public java.lang.String getCdBeneServizio() {
		return cdBeneServizio;
	}
	public void setCdBeneServizio(java.lang.String cdBeneServizio) {
		this.cdBeneServizio = cdBeneServizio;
	}
	public Boolean getFlIntraUe() {
		return flIntraUe;
	}
	public void setFlIntraUe(Boolean flIntraUe) {
		this.flIntraUe = flIntraUe;
	}
	public Boolean getFlExtraUe() {
		return flExtraUe;
	}
	public void setFlExtraUe(Boolean flExtraUe) {
		this.flExtraUe = flExtraUe;
	}

}