/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/08/2021
 */
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Progetto_anagraficoKey extends OggettoBulk implements KeyedPersistent {
	private Long idPrgAnagrafico;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PROGETTO_ANAGRAFICO
	 **/
	public Progetto_anagraficoKey() {
		super();
	}
	public Progetto_anagraficoKey(Long idPrgAnagrafico) {
		super();
		this.idPrgAnagrafico=idPrgAnagrafico;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Progetto_anagraficoKey)) return false;
		Progetto_anagraficoKey k = (Progetto_anagraficoKey) o;
		if (!compareKey(getIdPrgAnagrafico(), k.getIdPrgAnagrafico())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getIdPrgAnagrafico());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Chiave Primaria.]
	 **/
	public void setIdPrgAnagrafico(Long idPrgAnagrafico)  {
		this.idPrgAnagrafico=idPrgAnagrafico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Chiave Primaria.]
	 **/
	public Long getIdPrgAnagrafico() {
		return idPrgAnagrafico;
	}
}