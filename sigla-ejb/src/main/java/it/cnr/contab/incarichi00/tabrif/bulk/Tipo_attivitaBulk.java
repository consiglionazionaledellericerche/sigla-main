/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Tipo_attivitaBulk extends Tipo_attivitaBase {
    public final static java.util.Dictionary <String,String> TIPI_ASSOCIAZIONE;
    public final static String ASS_INCARICHI = "INC";
    public final static String ASS_BORSE_STUDIO = "BST";
    public final static String ASS_ASSEGNI_RICERCA = "ASR";
	
	static {
		TIPI_ASSOCIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_ASSOCIAZIONE.put(ASS_INCARICHI,"Incarichi");
		TIPI_ASSOCIAZIONE.put(ASS_BORSE_STUDIO,"Borse di Studio");
		TIPI_ASSOCIAZIONE.put(ASS_ASSEGNI_RICERCA,"Assegni di Ricerca");
	}

	public Tipo_attivitaBulk() {
		super();
	}
	public Tipo_attivitaBulk(java.lang.String cd_tipo_attivita) {
		super(cd_tipo_attivita);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_cancellato(Boolean.FALSE);
		return this;
	}
	public java.util.Dictionary getTipiAssociazioneKeys(){
		return TIPI_ASSOCIAZIONE;
	}
}