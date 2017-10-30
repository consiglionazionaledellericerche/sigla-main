/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class TipoOperazioneOrdKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdTipoOperazione;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_OPERAZIONE_ORD
	 **/
	public TipoOperazioneOrdKey() {
		super();
	}
	public TipoOperazioneOrdKey(java.lang.String cdTipoOperazione) {
		super();
		this.cdTipoOperazione=cdTipoOperazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof TipoOperazioneOrdKey)) return false;
		TipoOperazioneOrdKey k = (TipoOperazioneOrdKey) o;
		if (!compareKey(getCdTipoOperazione(), k.getCdTipoOperazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdTipoOperazione());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(java.lang.String cdTipoOperazione)  {
		this.cdTipoOperazione=cdTipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public java.lang.String getCdTipoOperazione() {
		return cdTipoOperazione;
	}
}