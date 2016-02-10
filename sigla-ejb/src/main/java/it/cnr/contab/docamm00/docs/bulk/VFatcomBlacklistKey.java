/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 31/03/2011
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VFatcomBlacklistKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Integer mese;
	private java.lang.Integer cd_terzo;
//  BENE_SERVIZIO VARCHAR(10)
	private java.lang.String bene_servizio;
	private java.lang.String tipo;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_FATCOM_BLACKLIST
	 **/
	public VFatcomBlacklistKey() {
		super();
	}
	public VFatcomBlacklistKey(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.Integer cd_terzo,String tipo,String bene_servizio) {
		super();
		this.esercizio=esercizio;
		this.mese=mese;
		this.cd_terzo=cd_terzo;
		this.tipo=tipo;
		this.bene_servizio=bene_servizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof VFatcomBlacklistKey)) return false;
		VFatcomBlacklistKey k = (VFatcomBlacklistKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getMese(), k.getMese())) return false;
		if (!compareKey(getCd_terzo(), k.getCd_terzo())) return false;
		if (!compareKey(getTipo(), k.getTipo())) return false;
		if (!compareKey(getBene_servizio(), k.getBene_servizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getMese());
		i = i + calculateKeyHashCode(getCd_terzo());
		i = i + calculateKeyHashCode(getTipo());
		i = i + calculateKeyHashCode(getBene_servizio());
		return i;
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
	 * Restituisce il valore di: [mese]
	 **/
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mese]
	 **/
	public java.lang.Integer getMese() {
		return mese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_terzo]
	 **/
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_terzo]
	 **/
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipo]
	 **/
	public java.lang.String getTipo() {
		return tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipo]
	 **/
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [bene_servizio]
	 **/
	public java.lang.String getBene_servizio() {
		return bene_servizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [bene_servizio]
	 **/
	public void setBene_servizio(java.lang.String bene_servizio)  {
		this.bene_servizio=bene_servizio;
	}
}