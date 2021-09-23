/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Accertamento_pluriennaleKey extends OggettoBulk implements KeyedPersistent {
	private String cdCds;
	private Integer esercizio;
	private Integer esercizioOriginale;
	private Long pgAccertamento;
	private Integer anno;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ACCERTAMENTO_PLURIENNALE
	 **/
	public Accertamento_pluriennaleKey() {
		super();
	}
	public Accertamento_pluriennaleKey(String cdCds, Integer esercizio, Integer esercizioOriginale, Long pgAccertamento, Integer anno) {
		super();
		this.cdCds=cdCds;
		this.esercizio=esercizio;
		this.esercizioOriginale=esercizioOriginale;
		this.pgAccertamento=pgAccertamento;
		this.anno=anno;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Accertamento_pluriennaleKey)) return false;
		Accertamento_pluriennaleKey k = (Accertamento_pluriennaleKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getEsercizioOriginale(), k.getEsercizioOriginale())) return false;
		if (!compareKey(getPgAccertamento(), k.getPgAccertamento())) return false;
		if (!compareKey(getAnno(), k.getAnno())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getEsercizioOriginale());
		i = i + calculateKeyHashCode(getPgAccertamento());
		i = i + calculateKeyHashCode(getAnno());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Cds dell'accertamento - chiave primaria]
	 **/
	public void setCdCds(String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Cds dell'accertamento - chiave primaria]
	 **/
	public String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio dell'accertamento - chiave primaria]
	 **/
	public void setEsercizio(Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio dell'accertamento - chiave primaria]
	 **/
	public Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio Originale dell'accertamento - chiave primaria]
	 **/
	public void setEsercizioOriginale(Integer esercizioOriginale)  {
		this.esercizioOriginale=esercizioOriginale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio Originale dell'accertamento - chiave primaria]
	 **/
	public Integer getEsercizioOriginale() {
		return esercizioOriginale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numero dell'accertamento - chiave primaria]
	 **/
	public void setPgAccertamento(Long pgAccertamento)  {
		this.pgAccertamento=pgAccertamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numero dell'accertamento - chiave primaria]
	 **/
	public Long getPgAccertamento() {
		return pgAccertamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Anno Accertamento Pluriennale - chiave primaria]
	 **/
	public void setAnno(Integer anno)  {
		this.anno=anno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Anno Accertamento Pluriennale - chiave primaria]
	 **/
	public Integer getAnno() {
		return anno;
	}
}