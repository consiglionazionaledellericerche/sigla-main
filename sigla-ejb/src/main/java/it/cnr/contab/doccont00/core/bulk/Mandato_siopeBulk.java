/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;

import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.persistency.sql.CompoundFindClause;

public abstract class Mandato_siopeBulk extends Mandato_siopeBase {

	Codici_siopeBulk  codice_siope  = new Codici_siopeBulk();
	
	protected BulkList mandatoSiopeCupColl = new BulkList();
	public Mandato_siopeBulk() {
		super();
	}

	public Mandato_siopeBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_mandato, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione, java.lang.Long pg_obbligazione_scadenzario, java.lang.String cd_cds_doc_amm, java.lang.String cd_uo_doc_amm, java.lang.Integer esercizio_doc_amm, java.lang.String cd_tipo_documento_amm, java.lang.Long pg_doc_amm, java.lang.Integer esercizio_siope, java.lang.String ti_gestione, java.lang.String cd_siope) {
		super(cd_cds, esercizio, pg_mandato, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario, cd_cds_doc_amm, cd_uo_doc_amm, esercizio_doc_amm, cd_tipo_documento_amm, pg_doc_amm, esercizio_siope, ti_gestione, cd_siope);
		setMandato_riga(new Mandato_rigaIBulk(cd_cds, cd_cds_doc_amm, cd_tipo_documento_amm, cd_uo_doc_amm, esercizio, esercizio_doc_amm, esercizio_obbligazione, pg_doc_amm, pg_mandato, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario));
		setCodice_siope(new Codici_siopeBulk(esercizio_siope, ti_gestione, cd_siope));
	}

	public abstract Mandato_rigaBulk getMandato_riga();
	
	public abstract void setMandato_riga(Mandato_rigaBulk mandato_riga);

	public Codici_siopeBulk getCodice_siope() {
		return codice_siope;
	}
	
	public void setCodice_siope(Codici_siopeBulk codice_siope) {
		this.codice_siope = codice_siope;
	}

	public java.lang.String getCd_cds() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds();
	}

	public void setCd_cds(String cd_cds) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_cds(cd_cds);
	}

	public java.lang.Integer getEsercizio() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio(esercizio);
	}

	public Long getPg_mandato() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_mandato();
	}
	public void setPg_mandato(Long pg_mandato) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_mandato(pg_mandato);
	}

	public Integer getEsercizio_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_obbligazione();
	}
	public void setEsercizio_obbligazione(Integer esercizio_obbligazione) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio_obbligazione(esercizio_obbligazione);
	}
	public Integer getEsercizio_ori_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_ori_obbligazione();
	}
	public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio_ori_obbligazione(esercizio_ori_obbligazione);
	}

	public Long getPg_obbligazione() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_obbligazione();
	}
	public void setPg_obbligazione(Long pg_obbligazione) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_obbligazione(pg_obbligazione);
	}

	public Long getPg_obbligazione_scadenzario() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_obbligazione_scadenzario();
	}
	public void setPg_obbligazione_scadenzario(Long pg_obbligazione_scadenzario) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
	}

	public String getCd_cds_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_cds_doc_amm();
	}
	public void setCd_cds_doc_amm(String cd_cds_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_cds_doc_amm(cd_cds_doc_amm);
	}

	public String getCd_uo_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_uo_doc_amm();
	}
	public void setCd_uo_doc_amm(String cd_uo_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_uo_doc_amm(cd_uo_doc_amm);
	}

	public Integer getEsercizio_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getEsercizio_doc_amm();
	}
	public void setEsercizio_doc_amm(Integer esercizio_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setEsercizio_doc_amm(esercizio_doc_amm);
	}

	public String getCd_tipo_documento_amm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getCd_tipo_documento_amm();
	}
	public void setCd_tipo_documento_amm(String cd_tipo_documento_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setCd_tipo_documento_amm(cd_tipo_documento_amm);
	}

	public Long getPg_doc_amm() {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga == null)
			return null;
		return riga.getPg_doc_amm();
	}
	
	public void setPg_doc_amm(Long pg_doc_amm) {
		it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk riga = this.getMandato_riga();
		if (riga != null) this.getMandato_riga().setPg_doc_amm(pg_doc_amm);
	}

	public Integer getEsercizio_siope() {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope == null)
			return null;
		return codice_siope.getEsercizio();
	}

	public void setEsercizio_siope(Integer esercizio_siope) {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope != null) this.getCodice_siope().setEsercizio(esercizio_siope);
	}

	public String getTi_gestione() {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope == null)
			return null;
		return codice_siope.getTi_gestione();
	}

	public void setTi_gestione(String ti_gestione) {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope != null) this.getCodice_siope().setTi_gestione(ti_gestione);
	}

	public String getCd_siope() {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope == null)
			return null;
		return codice_siope.getCd_siope();
	}
	
	public void setCd_siope(String cd_siope) {
		it.cnr.contab.config00.bulk.Codici_siopeBulk codice_siope = this.getCodice_siope();
		if (codice_siope != null) this.getCodice_siope().setCd_siope(cd_siope);
	}

	public BulkList<MandatoSiopeCupBulk> getMandatoSiopeCupColl() {
		return mandatoSiopeCupColl;
	}

	public void setMandatoSiopeCupColl(BulkList mandatoSiopeCupColl) {
		this.mandatoSiopeCupColl = mandatoSiopeCupColl;
	}
	/**
	 * Aggiunge un nuovo dettaglio (MandatoCupBulk) alla lista di dettagli definiti per il mandato
	 * inizializzandone alcuni campi
	 * @param mr dettaglio da aggiungere alla lista
	 * @return int
	 */
	public int addToMandatoSiopeCupColl( MandatoSiopeCupBulk mandato_siope_cup ) 
	{
		mandatoSiopeCupColl.add(mandato_siope_cup);
		mandato_siope_cup.setMandatoSiope(this); 
		if (mandato_siope_cup.getImporto()==null && mandatoSiopeCupColl.size()==1) 
			mandato_siope_cup.setImporto(this.getImporto());
		else
			mandato_siope_cup.setImporto(BigDecimal.ZERO);
		return mandatoSiopeCupColl.size()-1;
	}
	/**
	 * Metodo per l'eliminazione di un elemento <code>MandatoCupBulk</code> dalla <code>Collection</code>
	 * delle righe del mandato.
	 * @param index L'indice per scorrere la collezione dei codici cup legati alla riga del mandato.
	 * @return MandatoCupBulk La riga da rimuovere.
	 */
	public MandatoSiopeCupBulk removeFromMandatoSiopeCupColl(int index) 
	{
		MandatoSiopeCupBulk mandato_siope_cup = (MandatoSiopeCupBulk)mandatoSiopeCupColl.remove(index);
		
		return mandato_siope_cup;
	}
	public BigDecimal getIm_associato_cup(){
		BigDecimal totale = Utility.ZERO;
		for (Iterator i = getMandatoSiopeCupColl().iterator(); i.hasNext();) totale = totale.add(((MandatoSiopeCupBulk)i.next()).getImporto());
		return Utility.nvl(totale);
	}
	public BigDecimal getIm_da_associare_cup(){
		return Utility.nvl(getImporto()).subtract(Utility.nvl(getIm_associato_cup()));
	}
	/*
	 * Ritorna l'informazione circa la totale o parziale associazione della
	 * riga a codici CUP.
	 * 
	 * return
	 * 	"T" = SIOPE_TOTALMENTE_ASSOCIATO
	 *  "P" = SIOPE_PARZIALMENTE_ASSOCIATO
	 *  "N" = SIOPE_NON_ASSOCIATO
	 * 		
	 */
	public String getTipoAssociazioneCup() {
		BigDecimal totCup = getIm_associato_cup();
		if (getIm_da_associare_cup().compareTo(Utility.ZERO)==0) return Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO;
		if (totCup.compareTo(Utility.ZERO)==0) return Mandato_rigaBulk.SIOPE_NON_ASSOCIATO;
		return Mandato_rigaBulk.SIOPE_PARZIALMENTE_ASSOCIATO;
	}
	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { mandatoSiopeCupColl};

	}
}