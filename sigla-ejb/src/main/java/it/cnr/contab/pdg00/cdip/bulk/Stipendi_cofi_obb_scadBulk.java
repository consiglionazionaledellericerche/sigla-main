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
 * Date 28/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_contributo_ritenutaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Stipendi_cofi_obb_scadBulk extends Stipendi_cofi_obb_scadBase {
	public Stipendi_cofi_obb_scadBulk() {
		super();
	}
	public Stipendi_cofi_obb_scadBulk(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.String cd_cds_obbligazione, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione) {
		super(esercizio, mese, cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione);
	}
	private static final java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final int GENNAIO = 1;
	public static final int FEBBRAIO = 2;
	public static final int MARZO = 3;
	public static final int APRILE = 4;
	public static final int MAGGIO = 5;
	public static final int GIUGNO = 6;
	public static final int LUGLIO = 7;
	public static final int AGOSTO = 8;
	public static final int SETTEMBRE = 9;
	public static final int OTTOBRE = 10;
	public static final int NOVEMBRE = 11;
	public static final int TREDICESIMA = 12;
	public static final int DICEMBRE = 13;

	static {
		meseKeys.put(new Integer(GENNAIO),"Gennaio");
		meseKeys.put(new Integer(FEBBRAIO),"Febbraio");
		meseKeys.put(new Integer(MARZO),"Marzo");
		meseKeys.put(new Integer(APRILE),"Aprile");
		meseKeys.put(new Integer(MAGGIO),"Maggio");
		meseKeys.put(new Integer(GIUGNO),"Giugno");
		meseKeys.put(new Integer(LUGLIO),"Luglio");
		meseKeys.put(new Integer(AGOSTO),"Agosto");
		meseKeys.put(new Integer(SETTEMBRE),"Settembre");
		meseKeys.put(new Integer(OTTOBRE),"Ottobre");
		meseKeys.put(new Integer(NOVEMBRE),"Novembre");
		meseKeys.put(new Integer(TREDICESIMA),"Tredicesima");
		meseKeys.put(new Integer(DICEMBRE),"Dicembre");
	}
	public final java.util.Dictionary getMeseKeys() {
		return meseKeys;
	}
	private Stipendi_cofiBulk stipendi_cofi = new Stipendi_cofiBulk();
    private java.util.Collection tipoStipendi_cofi;

    private Stipendi_cofi_obbBulk stipendi_cofi_obb = new Stipendi_cofi_obbBulk();    
   
	public java.lang.Integer getEsercizio() {
		if (getStipendi_cofi() != null)
		  return getStipendi_cofi().getEsercizio();
		return null;  
	}

	public void setEsercizio(java.lang.Integer esercizio)
	{		
		this.getStipendi_cofi().setEsercizio(esercizio);
	}
	
	public java.lang.Integer getMese() {
		if (getStipendi_cofi() != null)
		  return getStipendi_cofi().getMese();
		return null;    
	}

	public void setMese(java.lang.Integer mese)
	{		
		this.getStipendi_cofi().setMese(mese);
	}
    /**
     * @return
     */
    public Stipendi_cofiBulk getStipendi_cofi() {
    	return stipendi_cofi;
    }
    /**
     * @param bulk
     */
    public void setStipendi_cofi(Stipendi_cofiBulk bulk) {
    	stipendi_cofi = bulk;
    }
    /**
     * @return
     */
    public java.util.Collection getTipoStipendi_cofi() {
    	return tipoStipendi_cofi;
    }

    /**
     * @param collection
     */
    public void setTipoStipendi_cofi(java.util.Collection collection) {
    	tipoStipendi_cofi = collection;
    }
    
	public java.lang.String getCd_cds_obbligazione() {
		return getStipendi_cofi_obb().getCd_cds_obbligazione();
	}
	public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
		this.getStipendi_cofi_obb().setCd_cds_obbligazione(cd_cds_obbligazione);
	}
	public java.lang.Integer getEsercizio_obbligazione() {
		return getStipendi_cofi_obb().getEsercizio_obbligazione();
	}
	public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
		this.getStipendi_cofi_obb().setEsercizio_obbligazione(esercizio_obbligazione);
	}
	public java.lang.Integer getEsercizio_ori_obbligazione() {
		return getStipendi_cofi_obb().getEsercizio_ori_obbligazione();
	}
	public void setEsercizio_ori_obbligazione(
			java.lang.Integer esercizio_ori_obbligazione) {
		this.getStipendi_cofi_obb().setEsercizio_ori_obbligazione(esercizio_ori_obbligazione);
	}
	public java.lang.Long getPg_obbligazione() {
		return getStipendi_cofi_obb().getPg_obbligazione();
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
		this.getStipendi_cofi_obb().setPg_obbligazione(pg_obbligazione);
	}
	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){

		super.initialize(bp, context);
		setEsercizio( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio() );	
		return this;
	}
	public Stipendi_cofi_obbBulk getStipendi_cofi_obb() {
		return stipendi_cofi_obb;
	}
	public void setStipendi_cofi_obb(Stipendi_cofi_obbBulk stipendi_cofi_obb) {
		this.stipendi_cofi_obb = stipendi_cofi_obb;
	}
}