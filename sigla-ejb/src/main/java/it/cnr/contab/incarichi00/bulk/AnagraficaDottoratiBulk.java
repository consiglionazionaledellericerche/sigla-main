/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.AbstractList;
import java.util.Dictionary;
import java.util.Optional;

public class AnagraficaDottoratiBulk extends AnagraficaDottoratiBase {
	public static final String STATO_NON_ASS_COMPENSO = "N";
	private CdsBulk cds = new CdsBulk();
	private Unita_organizzativaBulk uo;
	private AnagraficaDottoratiBulk anagraficaDottorati_origine = null;

	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = null;
	private java.util.Collection modalita;
	/**
	 * [TERZO ]
	 **/
	private TerzoBulk terzo =  new TerzoBulk();
	/**
	 * [PHDTIPO_DOTTORATI ]
	 **/
	private Phdtipo_dottoratiBulk phdtipoDottorati =  new Phdtipo_dottoratiBulk();
	/**
	 * [CICLO_DOTTORATI ]
	 **/
	private Ciclo_dottoratiBulk cicloDottorati =  new Ciclo_dottoratiBulk();

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ANAGRAFICA_DOTTORATI
	 **/
	public static String TIPO_COFINANZIAMENTO_EFFETTUATO_SI = "S";
	public static String TIPO_COFINANZIAMENTO_EFFETTUATO_NO = "N";
	public static String TIPO_COFINANZIAMENTO_EFFETTUATO_NON_PRESENTE = "X";

	public static Dictionary<String, String> tipoCofinanziamentoEffettuato = new it.cnr.jada.util.OrderedHashtable();
	static {
		tipoCofinanziamentoEffettuato.put(TIPO_COFINANZIAMENTO_EFFETTUATO_SI, "Si");
		tipoCofinanziamentoEffettuato.put(TIPO_COFINANZIAMENTO_EFFETTUATO_NO, "No");
		tipoCofinanziamentoEffettuato.put(TIPO_COFINANZIAMENTO_EFFETTUATO_NON_PRESENTE, "Non presente");
	}

	public AnagraficaDottoratiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ANAGRAFICA_DOTTORATI
	 **/
	public AnagraficaDottoratiBulk(Long id) {
		super(id);
	}
	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}
	public Phdtipo_dottoratiBulk getPhdtipoDottorati() {
		return phdtipoDottorati;
	}
	public void setPhdtipoDottorati(Phdtipo_dottoratiBulk phdtipoDottorati)  {
		this.phdtipoDottorati=phdtipoDottorati;
	}
	public Ciclo_dottoratiBulk getCicloDottorati() {
		return cicloDottorati;
	}
	public void setCicloDottorati(Ciclo_dottoratiBulk cicloDottorati)  {
		this.cicloDottorati=cicloDottorati;
	}

	public AnagraficaDottoratiBulk getAnagraficaDottorati() {
		return anagraficaDottorati_origine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 *
	 * @return*/
	public Integer getCdTerzo() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del tipo dottorati.]
	 **/
	public Long getIdPhdtipoDottorati() {
		Phdtipo_dottoratiBulk phdtipoDottorati = this.getPhdtipoDottorati();
		if (phdtipoDottorati == null)
			return null;
		return getPhdtipoDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del tipo dottorati.]
	 **/
	public void setIdPhdtipoDottorati(Long idPhdtipoDottorati)  {
		this.getPhdtipoDottorati().setId(idPhdtipoDottorati);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del ciclo dottorati.]
	 **/
	public Long getIdCicloDottorati() {
		Ciclo_dottoratiBulk cicloDottorati = this.getCicloDottorati();
		if (cicloDottorati == null)
			return null;
		return getCicloDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del ciclo dottorati.]
	 **/
	public void setIdCicloDottorati(Long idCicloDottorati)  {
		this.getCicloDottorati().setId(idCicloDottorati);
	}

	public static Dictionary<String, String> getTipoCofinanziamentoEffettuato() {
		return tipoCofinanziamentoEffettuato;
	}


	/**
	 * Banca
	 */
	private it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = null;
	private V_terzo_per_compensoBulk percipiente = null;

	public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
		return banca;
	}

	public java.lang.Long getPg_banca() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
		if (banca == null)
			return null;
		return banca.getPg_banca();
	}

	public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
		banca = newBanca;
	}

	public void setPg_banca(java.lang.Long pg_banca) {
		this.getBanca().setPg_banca(pg_banca);
	}

	/**
 	* Fine Banca
 	*/
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = null;
	private java.util.Collection termini;

	public java.lang.String getCd_termini_pag() {
		it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = this.getTermini_pagamento();
		if (termini_pagamento == null)
			return null;
		return termini_pagamento.getCd_termini_pag();
	}

	public java.util.Collection getTermini() {
		return termini;
	}

	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento() {
		return termini_pagamento;
	}

	public void setCd_termini_pag(java.lang.String cd_termini_pag) {
		this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
	}

	public void setTermini(java.util.Collection newTermini) {
		termini = newTermini;
	}

	public void setTermini_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento) {
		termini_pagamento = newTermini_pagamento;
	}

	public java.lang.String getCd_modalita_pag() {
		it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
		if (modalita_pagamento == null)
			return null;
		return modalita_pagamento.getCd_modalita_pag();
	}

	public java.util.Collection getModalita() {
		return modalita;
	}


	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
		return modalita_pagamento;
	}

	public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
		this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
	}

	public void setModalita_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento) {
		modalita_pagamento = newModalita_pagamento;
	}

	public void setModalita(java.util.Collection newModalita) {
		modalita = newModalita;
	}

	public CdsBulk getCds() { return cds; }
	public void setCds(CdsBulk cds) { this.cds=cds; }

	public Unita_organizzativaBulk getUo() { return uo; }
	public void setUo(Unita_organizzativaBulk uo) { this.uo=uo; }
	private BulkList<AnagraficaDottoratiRateBulk> anagraficaDottoratiRate = new BulkList();


	public BulkList<AnagraficaDottoratiRateBulk> getAnagraficaDottoratiRate() {
		return anagraficaDottoratiRate;
	}

	public void setAnagraficaDottoratiRate(BulkList<AnagraficaDottoratiRateBulk> anagraficaDottoratiRate) {
		this.anagraficaDottoratiRate = anagraficaDottoratiRate;
	}

	@Override
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {anagraficaDottoratiRate};
	}

	public int addToAnagraficaDottoratiRate(AnagraficaDottoratiRateBulk anagraficaDottoratiRateBulk) {
		anagraficaDottoratiRateBulk.setAnagraficaDottorati(this);
		getAnagraficaDottoratiRate().add(anagraficaDottoratiRateBulk);
		return getAnagraficaDottoratiRate().size()-1;
	}

	public AnagraficaDottoratiRateBulk removeFromAnagraficaDottoratiRate(int index) {
		return Optional.ofNullable(getAnagraficaDottoratiRate())
				.map(list -> list.remove(index))
				.orElse(null);
	}

	/**
	 *
	 * @return CdCds
	 */
	public String getCdCds(){
		CdsBulk cds = this.getCds();
		if (cds == null)
			return null;
		//return getCds().getCd_unita_padre();
		return getCds().getCd_ds_cds();
	}
	/**
	 * Setta il valore di: [CdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getCds().setCd_unita_padre(cdCds);
	}

	/**
	 *
	 * @return CdUnitaOrganizzativa
	 */
	public String getCdUnitaOrganizzativa(){
		Unita_organizzativaBulk uo = this.getUo();
		if (uo == null)
			return null;
		return getUo().getCd_unita_organizzativa();
	}
	/**
	 * Setta il valore di: [CdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa)  {
		this.getUo().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}

	/**
	 * ROpercipiente
	 */
	public V_terzo_per_compensoBulk getPercipiente() {
		return percipiente;
	}

	/**
	 * Restituisce un boolean 'true' nel caso in cui NON posso ricercare banche
	 */

	public boolean isAbledToInsertBank() {

		return !(getTerzo()!= null &&
				getModalita_pagamento() != null &&
				!isROPercipiente());
	}

	/**
	 * Restituisce un boolean 'true' se i campi anagrafici relativi al percipiente
	 * selezionato non sono modificabili
	 */

	public boolean isROFl_tassazione_separata() {

		return	isROPercipiente() ||
				(getAnagraficaDottoratiRate() != null && !getAnagraficaDottoratiRate().isEmpty());
	}

	/**
	 * Restituisce un boolean 'true' se il percipiente non è modificabile
	 */

	public boolean isROPercipiente() {

		return (getAnagraficaDottorati() != null &&
				(getAnagraficaDottorati().getCrudStatus() == OggettoBulk.NORMAL ||
						getAnagraficaDottorati().getCrudStatus() == OggettoBulk.TO_BE_UPDATED)) ||
				(getStatoAssCompenso() != null &&
						!STATO_NON_ASS_COMPENSO.equalsIgnoreCase(getStatoAssCompenso()) &&
						getCrudStatus() != OggettoBulk.UNDEFINED) ||
				!(getStato() == null ||
						getPercipiente() == null);
	}
	/**
	 * Restituisce un boolean 'true' se i campi anagrafici relativi al percipiente
	 * selezionato non sono modificabili
	 */

	public boolean isROPercipienteAnag() {

		return	getPercipiente() == null ||
				getPercipiente().getCrudStatus() == OggettoBulk.NORMAL ||
				isROPercipiente();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/4/2001 2:42:26 PM)
	 * @return boolean
	 */
	public boolean isROTi_istituz_commerc() {
		return isROPercipiente();
	}

	public boolean isROTerzo() {
		return Optional.ofNullable(getTerzo())
				.filter(terzoBulk -> Optional.ofNullable(terzoBulk.getCd_terzo()).isPresent())
				.isPresent();
	}
}