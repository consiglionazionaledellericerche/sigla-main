/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
public class Tipo_norma_perlaBulk extends Tipo_norma_perlaBase {
    public final static java.util.Dictionary <String,String> TIPI_ASSOCIAZIONE;
    public final static String ASS_INCARICHI = "INC";
    public final static String ASS_CONTRATTI = "CTR";
	
	static {
		TIPI_ASSOCIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_ASSOCIAZIONE.put(ASS_INCARICHI,"Incarichi");
		TIPI_ASSOCIAZIONE.put(ASS_CONTRATTI,"Borse di Studio");
	}

	public Tipo_norma_perlaBulk() {
		super();
	}
	public Tipo_norma_perlaBulk(java.lang.String cd_tipo_norma_perla) {
		super(cd_tipo_norma_perla);
	}
	public java.util.Dictionary getTipiAssociazioneKeys(){
		return TIPI_ASSOCIAZIONE;
	}
}