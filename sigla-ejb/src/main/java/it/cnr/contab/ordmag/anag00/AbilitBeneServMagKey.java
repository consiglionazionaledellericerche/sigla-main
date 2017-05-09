/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class AbilitBeneServMagKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.String cdMagazzino;
	private java.lang.String cdCategoriaGruppo;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABILIT_BENE_SERV_MAG
	 **/
	public AbilitBeneServMagKey() {
		super();
	}
	public AbilitBeneServMagKey(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.String cdCategoriaGruppo) {
		super();
		this.cdCds=cdCds;
		this.cdMagazzino=cdMagazzino;
		this.cdCategoriaGruppo=cdCategoriaGruppo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof AbilitBeneServMagKey)) return false;
		AbilitBeneServMagKey k = (AbilitBeneServMagKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdMagazzino(), k.getCdMagazzino())) return false;
		if (!compareKey(getCdCategoriaGruppo(), k.getCdCategoriaGruppo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdMagazzino());
		i = i + calculateKeyHashCode(getCdCategoriaGruppo());
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
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.cdMagazzino=cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		return cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCategoriaGruppo]
	 **/
	public void setCdCategoriaGruppo(java.lang.String cdCategoriaGruppo)  {
		this.cdCategoriaGruppo=cdCategoriaGruppo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCategoriaGruppo]
	 **/
	public java.lang.String getCdCategoriaGruppo() {
		return cdCategoriaGruppo;
	}
}