/*
ù * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.OggettoBulk;
public class LuogoConsegnaMagBulk extends LuogoConsegnaMagBase {
	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	/**
	 * [COMUNE Codifica dei comuni italiani e delle città estere.E' definito un dialogo utente per popolare le città estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	private ComuneBulk comuneItaliano =  new ComuneBulk();
	/**
	 * [NAZIONE Codifica delle nazioni. Non è stato definito un dialogo utente sulla tabella ma si prevede il recupero in sede di migrazione]
	 **/
	private NazioneBulk nazione =  new NazioneBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LUOGO_CONSEGNA_MAG
	 **/
	public LuogoConsegnaMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LUOGO_CONSEGNA_MAG
	 **/
	public LuogoConsegnaMagBulk(java.lang.String cdCds, java.lang.String cdLuogoConsegna) {
		super(cdCds, cdLuogoConsegna);
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdCds) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa)  {
		this.unitaOrganizzativa=unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codifica dei comuni italiani e delle città estere.E' definito un dialogo utente per popolare le città estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public ComuneBulk getComuneItaliano() {
		return comuneItaliano;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codifica dei comuni italiani e delle città estere.E' definito un dialogo utente per popolare le città estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public void setComuneItaliano(ComuneBulk comuneItaliano)  {
		this.comuneItaliano=comuneItaliano;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codifica delle nazioni. Non è stato definito un dialogo utente sulla tabella ma si prevede il recupero in sede di migrazione]
	 **/
	public NazioneBulk getNazione() {
		return nazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codifica delle nazioni. Non è stato definito un dialogo utente sulla tabella ma si prevede il recupero in sede di migrazione]
	 **/
	public void setNazione(NazioneBulk nazione)  {
		this.nazione=nazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgComune]
	 **/
	public java.lang.Long getPgComune() {
		ComuneBulk comune = this.getComuneItaliano();
		if (comune == null)
			return null;
		return getComuneItaliano().getPg_comune();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgComune]
	 **/
	public void setPgComune(java.lang.Long pgComune)  {
		this.getComuneItaliano().setPg_comune(pgComune);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgNazione]
	 **/
	public java.lang.Long getPgNazione() {
		NazioneBulk nazione = this.getNazione();
		if (nazione == null)
			return null;
		return getNazione().getPg_nazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgNazione]
	 **/
	public void setPgNazione(java.lang.Long pgNazione)  {
		this.getNazione().setPg_nazione(pgNazione);
	}
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
		return super.initialize(bp,context);
	}
}