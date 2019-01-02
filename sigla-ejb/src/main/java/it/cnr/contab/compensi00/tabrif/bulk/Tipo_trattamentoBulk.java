package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.util.TipoDebitoSIOPE;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Tipo_trattamentoBulk extends Tipo_trattamentoBase {

	public final static java.lang.String ATT_COMMERCIALE = "Y";
	public final static java.lang.String ATT_NON_COMMERCIALE = "N";
	public final static java.lang.String TIPO_DEBITO_NON_COMMERCIALE = "N";
	public final static java.lang.String TIPO_DEBITO_COMMERCIALE = "C";

	private final static java.util.Dictionary TIPO_COMMERCIALE;

	static {
		TIPO_COMMERCIALE = new OrderedHashtable();
		TIPO_COMMERCIALE.put(ATT_COMMERCIALE, "Attività commerciali");
		TIPO_COMMERCIALE.put(ATT_NON_COMMERCIALE, "Attività NON commerciali");
	}
	
	public final static Map<String,String> tipoDebitoSIOPEKeys = Arrays.asList(TipoDebitoSIOPE.values())
			.stream()
			.filter(tipoDebitoSIOPE -> !tipoDebitoSIOPE.equals(TipoDebitoSIOPE.IVA))
			.collect(Collectors.toMap(
					TipoDebitoSIOPE::value,
					TipoDebitoSIOPE::label,
					(oldValue, newValue) -> oldValue,
					Hashtable::new
			));
	
	private java.util.List intervalli;
	public Tipo_trattamentoBulk() {
		super();
	}
	public Tipo_trattamentoBulk(java.lang.String cd_trattamento,java.sql.Timestamp dt_inizio_validita) {
		super(cd_trattamento,dt_inizio_validita);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/03/2002 10.38.48)
	 * @return java.sql.Timestamp
	 * @param newDate java.sql.Timestamp
	 */
	public java.sql.Timestamp getDataFineValidita() {

		if ( (getDt_fin_validita()!=null) && (getDt_fin_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
			return null;

		return getDt_fin_validita();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (04/06/2002 14.35.49)
	 * @return java.util.List
	 */
	public java.util.List getIntervalli() {
		return intervalli;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/03/2002 15.44.26)
	 * @return java.util.Dictionary
	 */
	public java.util.Dictionary getTipoAnagraficoKeys() {
		return Tipo_rapportoBulk.DIPENDENTE_ALTRO;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/03/2002 15.53.16)
	 * @return java.util.Dictionary
	 */
	public java.util.Dictionary getTipoCommercialeKeys() {
		return TIPO_COMMERCIALE;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (18/01/2002 14.52.26)
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForInsert(bp,context);
		resetFlags();
		setTi_anagrafico(Tipo_rapportoBulk.DIPENDENTE);
		setTi_commerciale(ATT_NON_COMMERCIALE);

		return this;
	}
	private void resetFlags() {

		this.setFl_detrazioni_dipendente(Boolean.FALSE);
		this.setFl_detrazioni_familiari(Boolean.FALSE);
		this.setFl_diaria(Boolean.FALSE);
		this.setFl_irpef_annualizzata(Boolean.FALSE);
		this.setFl_registra_fattura(Boolean.FALSE);
		this.setFl_senza_calcoli(Boolean.FALSE);
		this.setFl_tassazione_separata(Boolean.FALSE);
		this.setFl_soggetto_conguaglio(Boolean.FALSE);
		this.setFl_default_conguaglio(Boolean.FALSE);
		this.setFl_agevolazioni_cervelli(Boolean.FALSE);
		this.setFl_anno_prec(Boolean.FALSE);
		this.setFl_detrazioni_altre(Boolean.FALSE);
		this.setFl_incarico(Boolean.FALSE);
		this.setFl_tipo_prestazione_obbl(Boolean.FALSE);
		this.setFl_agevolazioni_rientro_lav(Boolean.FALSE);	
		this.setFl_solo_inail_ente(Boolean.FALSE);
		this.setFl_split_payment(Boolean.FALSE);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (08/03/2002 10.39.11)
	 * @param newDate java.sql.Timestamp
	 */
	public void setDataFineValidita(java.sql.Timestamp newDate) {

		this.setDt_fin_validita(newDate);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (04/06/2002 14.35.49)
	 * @param newIntervalli java.util.List
	 */
	public void setIntervalli(java.util.List newIntervalli) {
		intervalli = newIntervalli;
	}
}
