/**
 * 
 */
package it.cnr.contab.preventvar00.bulk;

/**
 * @author mspasiano
 *
 */
public class Var_bilancioResiduiBulk extends Var_bilancioBulk {
	protected final static java.util.Dictionary <String,String> TIPI_VARIAZIONE;
	static {
		TIPI_VARIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_VARIAZIONE.put(STORNO_S,"Storno tra spese");
		TIPI_VARIAZIONE.put(VAR_ECO,"Economie");
		TIPI_VARIAZIONE.put(VAR_MSP,"Maggiori spese");		
	}

	/**
	 * 
	 */
	public Var_bilancioResiduiBulk() {
		super();
	}

	/**
	 * @param cd_cds
	 * @param esercizio
	 * @param pg_variazione
	 * @param ti_appartenenza
	 */
	public Var_bilancioResiduiBulk(String cd_cds, Integer esercizio,
			Long pg_variazione, String ti_appartenenza) {
		super(cd_cds, esercizio, pg_variazione, ti_appartenenza);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'tipiVariazioneKeys'
	 *
	 * @return Il valore della proprietà 'tipiVariazioneKeys'
	 */
	public java.util.Dictionary getTipiVariazioneKeys(){
		return TIPI_VARIAZIONE;
	}
	protected String getCompetenzaResiduo() {
		return "R";
	}
}
