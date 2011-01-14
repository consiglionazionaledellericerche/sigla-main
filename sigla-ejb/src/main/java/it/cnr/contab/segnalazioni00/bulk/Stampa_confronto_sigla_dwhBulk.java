/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 11/01/2010
 */
package it.cnr.contab.segnalazioni00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
public class Stampa_confronto_sigla_dwhBulk extends OggettoBulk {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONFRONTO_SIGLA_DWH
	 **/
	
	private ConfrontoSiglaDwhBulk data;
	private java.util.Collection date;
	
	
	public Stampa_confronto_sigla_dwhBulk() {
		super();
	}
	
	/*public void validate() throws ValidationException{

		if (getData()==null || getData().getDt_elaborazione()==null)
			throw new ValidationException("Il campo Data Elaborazione e' obbligatorio");
	}*/
	
	public java.sql.Timestamp getDt_elaborazioneForPrint() {
		ConfrontoSiglaDwhBulk confSiglaDwh = this.getData();
		if (confSiglaDwh == null)
			return null;
		return confSiglaDwh.getDt_elaborazione();
	}

	public ConfrontoSiglaDwhBulk getData() {
		return data;
	}

	public void setData(ConfrontoSiglaDwhBulk data) {
		this.data = data;
	}

	public java.util.Collection getDate() {
		return date;
	}

	public void setDate(java.util.Collection date) {
		this.date = date;
	}
}