/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class AssUnitaOperativaOrdKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUnitaOperativa;
	private java.lang.String cdUnitaOperativaRif;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_UNITA_OPERATIVA_ORD
	 **/
	public AssUnitaOperativaOrdKey() {
		super();
	}
	public AssUnitaOperativaOrdKey(java.lang.String cdUnitaOperativa, java.lang.String cdUnitaOperativaRif) {
		super();
		this.cdUnitaOperativa=cdUnitaOperativa;
		this.cdUnitaOperativaRif=cdUnitaOperativaRif;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof AssUnitaOperativaOrdKey)) return false;
		AssUnitaOperativaOrdKey k = (AssUnitaOperativaOrdKey) o;
		if (!compareKey(getCdUnitaOperativa(), k.getCdUnitaOperativa())) return false;
		if (!compareKey(getCdUnitaOperativaRif(), k.getCdUnitaOperativaRif())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUnitaOperativa());
		i = i + calculateKeyHashCode(getCdUnitaOperativaRif());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativaRif]
	 **/
	public void setCdUnitaOperativaRif(java.lang.String cdUnitaOperativaRif)  {
		this.cdUnitaOperativaRif=cdUnitaOperativaRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativaRif]
	 **/
	public java.lang.String getCdUnitaOperativaRif() {
		return cdUnitaOperativaRif;
	}
}