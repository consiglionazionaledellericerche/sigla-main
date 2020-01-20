/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class MovimentiMagKey extends OggettoBulk implements KeyedPersistent {
	private Long pgMovimento;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_MAG
	 **/
	public MovimentiMagKey() {
		super();
	}
	public MovimentiMagKey(Long pgMovimento) {
		super();
		this.pgMovimento=pgMovimento;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof MovimentiMagKey)) return false;
		MovimentiMagKey k = (MovimentiMagKey) o;
		if (!compareKey(getPgMovimento(), k.getPgMovimento())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPgMovimento());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgMovimento]
	 **/
	public void setPgMovimento(Long pgMovimento)  {
		this.pgMovimento=pgMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMovimento]
	 **/
	public Long getPgMovimento() {
		return pgMovimento;
	}
}