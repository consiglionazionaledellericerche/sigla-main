/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 19/08/2021
 */
package it.cnr.contab.config00.contratto.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.util.Utility;

import java.math.BigDecimal;

public class Dettaglio_contrattoBulk extends Dettaglio_contrattoBase {
	/**
	 * [CONTRATTO ]
	 **/
	private ContrattoBulk contratto =  new ContrattoBulk();
	/**
	 * [BENE_SERVIZIO ]
	 **/
	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	/**
	 * [UNITA_MISURA ]
	 **/
	private UnitaMisuraBulk unitaMisura =  new UnitaMisuraBulk();
	/**
	 * [CATEGORIA_GRUPPO_INVENT ]
	 **/
	private Categoria_gruppo_inventBulk categoriaGruppoInvent =  new Categoria_gruppo_inventBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DETTAGLIO_CONTRATTO
	 **/
	public Dettaglio_contrattoBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DETTAGLIO_CONTRATTO
	 **/
	public Dettaglio_contrattoBulk(Long id) {
		super(id);
	}
	public ContrattoBulk getContratto() {
		return contratto;
	}
	public void setContratto(ContrattoBulk contratto)  {
		this.contratto=contratto;
	}
	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	public void setBeneServizio(Bene_servizioBulk beneServizio)  {
		this.beneServizio=beneServizio;
	}
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura)  {
		this.unitaMisura=unitaMisura;
	}
	public Categoria_gruppo_inventBulk getCategoriaGruppoInvent() {
		return categoriaGruppoInvent;
	}
	public void setCategoriaGruppoInvent(Categoria_gruppo_inventBulk categoriaGruppoInvent)  {
		this.categoriaGruppoInvent=categoriaGruppoInvent;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Progressivo contratto (cronologico all'interno dell'esercizio)]
	 **/
	public Long getPgContratto() {
		ContrattoBulk contratto = this.getContratto();
		if (contratto == null)
			return null;
		return getContratto().getPg_contratto();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Progressivo contratto (cronologico all'interno dell'esercizio)]
	 **/
	public void setPgContratto(Long pgContratto)  {
		this.getContratto().setPg_contratto(pgContratto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio del Contratto.]
	 **/
	public Integer getEsercizioContratto() {
		ContrattoBulk contratto = this.getContratto();
		if (contratto == null)
			return null;
		return getContratto().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio del Contratto.]
	 **/
	public void setEsercizioContratto(Integer esercizioContratto)  {
		this.getContratto().setEsercizio(esercizioContratto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Stato del Contratto]
	 **/
	public String getStatoContratto() {
		ContrattoBulk contratto = this.getContratto();
		if (contratto == null)
			return null;
		return getContratto().getStato();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Stato del Contratto]
	 **/
	public void setStatoContratto(String statoContratto)  {
		this.getContratto().setStato(statoContratto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice identificativo del bene o servizio]
	 **/
	public String getCdBeneServizio() {
		Bene_servizioBulk beneServizio = this.getBeneServizio();
		if (beneServizio == null)
			return null;
		return getBeneServizio().getCd_bene_servizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice identificativo del bene o servizio]
	 **/
	public void setCdBeneServizio(String cdBeneServizio)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice dell'unità di misura di ordine]
	 **/
	public String getCdUnitaMisura() {
		UnitaMisuraBulk unitaMisura = this.getUnitaMisura();
		if (unitaMisura == null)
			return null;
		return getUnitaMisura().getCdUnitaMisura();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice dell'unità di misura di ordine]
	 **/
	public void setCdUnitaMisura(String cdUnitaMisura)  {
		this.getUnitaMisura().setCdUnitaMisura(cdUnitaMisura);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice Categoria]
	 **/
	public String getCdCategoriaGruppo() {
		Categoria_gruppo_inventBulk categoriaGruppoInvent = this.getCategoriaGruppoInvent();
		if (categoriaGruppoInvent == null)
			return null;
		return getCategoriaGruppoInvent().getCd_categoria_gruppo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice Categoria]
	 **/
	public void setCdCategoriaGruppo(String cdCategoriaGruppo)  {
		this.getCategoriaGruppoInvent().setCd_categoria_gruppo(cdCategoriaGruppo);
	}
	public Boolean isROCoefConv(){
		if (getUnitaMisura() != null && getUnitaMisura().getCdUnitaMisura() != null &&
				getBeneServizio() != null && getBeneServizio().getUnitaMisura() != null && getBeneServizio().getCdUnitaMisura() != null &&
				!getUnitaMisura().getCdUnitaMisura().equals(getBeneServizio().getCdUnitaMisura())){
			return false;
		}
		return true;
	}
	public boolean isNonCancellabile() {
		if ( ContrattoBulk.DETTAGLIO_CONTRATTO_ARTICOLI.equals(this.getContratto().getTipo_dettaglio_contratto()))
			return Utility.nvl(this.getQuantitaOrdinata()).compareTo(BigDecimal.ZERO)>0;
		if ( ContrattoBulk.DETTAGLIO_CONTRATTO_CATGRP.equals(this.getContratto().getTipo_dettaglio_contratto()))
			return Utility.nvl(this.getImportoOrdinato()).compareTo(BigDecimal.ZERO)>0;
		return Boolean.FALSE;
	}
}