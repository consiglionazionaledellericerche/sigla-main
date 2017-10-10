/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;

public class TipoOperazioneOrdBulk extends TipoOperazioneOrdBase {
    public final static String OPERAZIONE_VALIDAZIONE_RICHIESTA= "VR1";
    public final static String OPERAZIONE_RICHIESTA= "RIC";
    public final static String EVASIONE_ORDINE= "RIS";
    public final static String OPERAZIONE_ORDINE= "ORD";
    public final static String OPERAZIONE_APPROVAZIONE_ORDINE= "VO1";
    public final static String OPERAZIONE_FIRMA_ORDINE= "VO2";
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_OPERAZIONE_ORD
	 **/
	public TipoOperazioneOrdBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_OPERAZIONE_ORD
	 **/
	public TipoOperazioneOrdBulk(java.lang.String cdTipoOperazione) {
		super(cdTipoOperazione);
	}
}