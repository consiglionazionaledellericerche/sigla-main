package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Dictionary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Tipo_sezionaleBulk extends Tipo_sezionaleBase {

	protected Attivita_commercialeBulk attivita_commerciale;
	protected Tipo_sezionaleBulk tipo_sezionale;
	protected Tipo_sezionaleBulk tipo_sezionale_vendita;
	protected final static java.lang.Boolean TRUE=new java.lang.Boolean (true) ;
	protected final static java.lang.Boolean FALSE=new java.lang.Boolean (false) ;

	public final static java.util.Dictionary ACQUISTI_VENDITE;
	public final static java.util.Dictionary ISTITUZIONALE_COMMERCIALE;
	public final static Dictionary BENI_SERVIZI;

	public final static String ACQUISTI = "A";
	public final static String VENDITE  = "V";

	public final static String ISTITUZIONALE = "I";
	public final static String COMMERCIALE  = "C";

	public final static String BENE_SERVIZIO = Bene_servizioBulk.BENE_SERVIZIO;
	public final static String SERVIZIO = Bene_servizioBulk.SERVIZIO;
	public final static String BENE = Bene_servizioBulk.BENE;

	static{
		ACQUISTI_VENDITE = new it.cnr.jada.util.OrderedHashtable();
		ACQUISTI_VENDITE.put(ACQUISTI,"Acquisti");
		ACQUISTI_VENDITE.put(VENDITE,"Vendite");

		ISTITUZIONALE_COMMERCIALE = new it.cnr.jada.util.OrderedHashtable();
		ISTITUZIONALE_COMMERCIALE.put(ISTITUZIONALE,"Istituzionale");
		ISTITUZIONALE_COMMERCIALE.put(COMMERCIALE,"Commerciale");

		BENI_SERVIZI = new it.cnr.jada.util.OrderedHashtable();
		BENI_SERVIZI.put(BENE,"Bene");
		BENI_SERVIZI.put(SERVIZIO,"Servizio");
		BENI_SERVIZI.put(BENE_SERVIZIO,"Entrambi");
	}
	public Tipo_sezionaleBulk() {
		super();
	}
	public Tipo_sezionaleBulk(java.lang.String cd_tipo_sezionale) {
		super(cd_tipo_sezionale);
	}
	/**
	 * return Attivita_commercialeBulk
	 */
	public Attivita_commercialeBulk getAttivita_commerciale() {
		return attivita_commerciale;
	}
	public String getCd_ds_tipo_sezionale() {

		String result = "";
		result += ((getCd_tipo_sezionale() == null) ? "n.n." : getCd_tipo_sezionale()) + " - ";
		result += ((getDs_tipo_sezionale() == null) ? "n.n." : getDs_tipo_sezionale());
		return result;
	}
	public java.lang.String getSezionale_ven_liquidazione() {
		it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk tipo_sezionale_vendita = this.getTipo_sezionale_vendita();
		if (tipo_sezionale_vendita == null)
			return null;
		return tipo_sezionale_vendita.getCd_tipo_sezionale();
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi acquisti/vendite
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_acquisti_venditeKeys() {
		return ACQUISTI_VENDITE;
	}
	public Dictionary getTi_bene_servizioKeys() {

		return BENI_SERVIZI;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi istituzionale/commerciale
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_istituz_commercKeys() {
		return ISTITUZIONALE_COMMERCIALE;
	}
	/**
	 * @return Tipo_sezionaleBulk
	 */
	public Tipo_sezionaleBulk getTipo_sezionale() {
		return tipo_sezionale;
	}
	/**
	 * @return Tipo_sezionaleBulk
	 */
	public Tipo_sezionaleBulk getTipo_sezionale_vendita() {
		return tipo_sezionale_vendita;
	}
	/**
	 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
	 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
	 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e pu� contenere
	 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
	 */
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		tipo_sezionale = new Tipo_sezionaleBulk(); 
		tipo_sezionale_vendita = new Tipo_sezionaleBulk();	
		attivita_commerciale = new Attivita_commercialeBulk();
		setTi_acquisti_vendite(ACQUISTI);
		setTi_istituz_commerc(ISTITUZIONALE);

		if (getFl_autofattura()==null)
			setFl_autofattura(FALSE);
		if (getFl_extra_ue()==null)
			setFl_extra_ue(FALSE);
		if (getFl_intra_ue()==null)
			setFl_intra_ue(FALSE);
		if (getFl_ordinario()==null)
			setFl_ordinario(FALSE);
		if (getFl_san_marino_con_iva()==null)
			setFl_san_marino_con_iva(FALSE);
		if (getFl_san_marino_senza_iva()==null)
			setFl_san_marino_senza_iva(FALSE);
		if(getFl_servizi_non_residenti()==null)
			setFl_servizi_non_residenti(FALSE);
		return this;
	}

	/**
	 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
	 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
	 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e pu� contenere
	 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		Tipo_sezionaleBulk tSez = (Tipo_sezionaleBulk)super.initializeForInsert(bp, context);

		setTi_bene_servizio(BENE_SERVIZIO);

		return tSez;
	}

	/**
	 * Restituisce <code>true</code> se il sezionale � di tipo Acquisti
	 *
	 * @return boolean
	 */

	public boolean isAcquisti() {
		return ACQUISTI.equals(getTi_acquisti_vendite());
	}
	/**
	 * Restituisce <code>true</code> se il sezionale � di tipo Acquisti
	 *
	 * @return boolean
	 */

	public boolean isAutofattura() {
		return getFl_autofattura() != null && getFl_autofattura().booleanValue();
	}
	/**
	 * Restituisce <code>true</code> se il sezionale � di tipo Istituzionale
	 *
	 * @return boolean
	 */

	public boolean isIstituzionale() {
		return ISTITUZIONALE.equals(getTi_istituz_commerc());
	}
	public boolean isROTi_bene_servizio() {

		return !isAcquisti() && !isAutofattura();
	}
	public void setAttivita_commerciale(Attivita_commercialeBulk newAttCommerciale) {
		attivita_commerciale = newAttCommerciale;
		if (newAttCommerciale!=null)
			setCd_attivita_commerciale(attivita_commerciale.getCd_attivita_commerciale());
	}
	public void setSezionale_ven_liquidazione(java.lang.String sezionale_ven_liquidazione) {
		if (this.getTipo_sezionale_vendita()!=null)
			this.getTipo_sezionale_vendita().setCd_tipo_sezionale(sezionale_ven_liquidazione);
	}
	/**
	 * @return void
	 */
	public void setTipo_sezionale(Tipo_sezionaleBulk newTipo_sezionale) {
		tipo_sezionale=newTipo_sezionale;
		//sezionale_ven_liquidazione = newTipo_sezionale.cd_tipo_sezionale;
	}
	/**
	 * @return void
	 */
	public void setTipo_sezionale_vendita(Tipo_sezionaleBulk newTipo_sezionale_vendita) {
		tipo_sezionale_vendita=newTipo_sezionale_vendita;
		//sezionale_ven_liquidazione = newTipo_sezionale.cd_tipo_sezionale;
	}
	public void validate() throws ValidationException {
		super.validate();
		if (getFl_intra_ue()==null)
			setFl_intra_ue(FALSE);
		if (getFl_extra_ue()==null)
			setFl_extra_ue(FALSE);
		if (getFl_san_marino_con_iva()==null)
			setFl_san_marino_con_iva(FALSE);
		if (getFl_san_marino_senza_iva()==null)
			setFl_san_marino_senza_iva(FALSE);
		if (getFl_ordinario()==null)
			setFl_ordinario(FALSE);
		if (getFl_autofattura()==null)
			setFl_autofattura(FALSE);

		if (getTi_acquisti_vendite().equals(VENDITE) && getTi_istituz_commerc().equals(ISTITUZIONALE))
			throw new ValidationException("Non � possibile creare un sezionale di tipo vendite non commerciale");
		if (getFl_intra_ue().equals(FALSE) &&
				getFl_extra_ue().equals(FALSE) &&
				getFl_san_marino_con_iva().equals(FALSE) &&
				getFl_san_marino_senza_iva().equals(FALSE) &&
				getFl_ordinario().equals(FALSE) &&
				getFl_autofattura().equals(FALSE))
			throw new ValidationException("Attenzione non � stato selezionato nessun tipo di registro");
		/*
	if (tipo_sezionale_vendita.sezionale_ven_liquidazione==null)
	{
		//sezionale_ven_liquidazione = cd_tipo_sezionale;
		tipo_sezionale_vendita=tipo_sezionale;
		tipo_sezionale_vendita.cd_tipo_sezionale = this.cd_tipo_sezionale;
	}
		 */

		if (!isAcquisti()) {
			if (!BENE_SERVIZIO.equalsIgnoreCase(getTi_bene_servizio()) && !isAutofattura())
				throw new ValidationException("Non � possibile definire un tipo bene/servizio per tipi sezionali di vendita senza specificare \"autofattura\".");	
			setSezionale_ven_liquidazione(null);
		} else if (getFl_autofattura().booleanValue())
			throw new ValidationException("Non � possibile impostare un tipo sezionale acquisti come autofattura.");	

		if (isIstituzionale()) {
			setSezionale_ven_liquidazione(null);	
			setCd_attivita_commerciale(null);
		} else {
			if (getCd_attivita_commerciale()==null)
				throw new ValidationException("Inserire una attivit� commerciale");
			if (getSezionale_ven_liquidazione()==null && getTi_acquisti_vendite().equals(ACQUISTI))
				throw new ValidationException("Inserire un sezionale vendite");
		}
	}
}
