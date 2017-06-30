package it.cnr.contab.docamm00.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.dp.DigitalPreservationProperties;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@CMISType(name="F:sigla_fatture:fatture_attive")
public class CMISFolderFatturaAttiva extends OggettoBulk {
	private static final long serialVersionUID = 4110702628275029148L;

	private Fattura_attivaBulk fattura_attivaBulk;
	private DigitalPreservationProperties dpProperties;
	
	public CMISFolderFatturaAttiva(Fattura_attivaBulk fattura_attivaBulk) {
    	super();
    	setFattura_attivaBulk(fattura_attivaBulk);
    	loadProperties();
	}
	
	public void loadProperties(){
		if (dpProperties == null)
			dpProperties = SpringUtil.getBean("digitalPreservationProperties",DigitalPreservationProperties.class);
	}

	@CMISProperty(name="sigla_fatture:esercizio")
    public Integer getEsercizioFattura() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getEsercizio();
    }

	@CMISProperty(name="sigla_fatture:pg_fattura", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getPgFattura() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getPg_fattura_attiva();
    }
	
	@CMISProperty(name="sigla_fatture:prot_iva", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getProtocolloIva() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getProtocollo_iva();
    }

	@CMISProperty(name="sigla_fatture:prot_gen", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getProtocolloGenerale() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getProtocollo_iva();
    }

	@CMISProperty(name="sigla_fatture:data_reg", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataRegistrazione(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_registrazione()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_registrazione();
	}
	
	@CMISProperty(name="sigla_fatture:descrizione")
	public String getDescrizione() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getDs_fattura_attiva();
	}
	
	@CMISProperty(name="sigla_fatture:imponibile")
	public BigDecimal getImponibile(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getIm_totale_imponibile();
	}
	
	@CMISProperty(name="sigla_fatture:iva")
	public BigDecimal getIva(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getIm_totale_iva();
	}
	
	@CMISProperty(name="sigla_fatture:importo_totale")
	public BigDecimal getImportoTotale(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getIm_totale_fattura();
	}
	
	@CMISProperty(name="sigla_fatture:divisa")
	public String getDivisa() {
		if (this.getFattura_attivaBulk()==null ||
				this.getFattura_attivaBulk().getValuta()==null)
				return null;
			return this.getFattura_attivaBulk().getValuta().getCd_divisa();
	}
	
	@CMISProperty(name="sigla_fatture:cambio")
	public BigDecimal getCambio(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCambio();
	}
	
	@CMISProperty(name="sigla_fatture:dt_competenza_dal", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaDal(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_da_competenza_coge()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_da_competenza_coge();
	}
	
	@CMISProperty(name="sigla_fatture:dt_competenza_al", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaAl(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_a_competenza_coge()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_a_competenza_coge();
	}
	
	@CMISProperty(name="sigla_fatture:data_emissione", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataEmissione(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_emissione()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_emissione();
	}
	
	@CMISProperty(name="sigla_fatture:codice_ipa")
	public String getCodiceIpa() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCodiceUnivocoUfficioIpa();
	}
	
	@CMISProperty(name="sigla_fatture:codice_invio_sdi")
	public String getCodiceInvioSdi() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCodiceInvioSdi();
	}
	
	@CMISProperty(name="sigla_fatture:stato_invio_sdi")
	public String getStatoInvioSdi() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return (String)this.getFattura_attivaBulk().recuperoStatoInvioSdiKeys();
	}
		
	@CMISProperty(name="sigla_fatture:note_invio_sdi")
	public String getNoteInvioSdi() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getNoteInvioSdi();
	}
		
	@CMISProperty(name="sigla_fatture:data_consegna_sdi", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataConsegnaSdi(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDtConsegnaSdi()==null)
			return null;
		return this.getFattura_attivaBulk().getDtConsegnaSdi();
	}
	
	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:codice"))
	public String getCodiceCds(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getCd_cds()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_cds();
	}
	
	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:codice"))
	public String getCodiceUo(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getCd_unita_organizzativa()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_unita_organizzativa();
	}
	
	@CMISPolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@CMISProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getUtuv();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi", property=@CMISProperty(name="sigla_commons_aspect:terzi_cd_terzo"))
	public String getCodiceTerzo() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_terzo().toString();
	}

	@CMISProperty(name="sigla_fatture:modalita_incasso")
	public String getModalitaIncasso() {
		if (this.getFattura_attivaBulk()==null||this.getFattura_attivaBulk().getModalita_pagamento_uo() ==null)
			return null;
		return this.getFattura_attivaBulk().getModalita_pagamento_uo().getCd_modalita_pag();
	}

	@CMISProperty(name="sigla_fatture:prog_univoco_anno", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getProgrUnivocoAnno() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getProgrUnivocoAnno();
    }
	
	@CMISProperty(name="sigla_fatture:tipo_documento")
	public String getTipoDocumento() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getTi_fattura();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pg", property=@CMISProperty(name="sigla_commons_aspect:terzi_pg_denominazione"))
	public String getRagioneSociale() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getRagione_sociale();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pg", property=@CMISProperty(name="sigla_commons_aspect:terzi_pg_pariva"))
	public String getPariva() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getPartita_iva();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pf", property=@CMISProperty(name="sigla_commons_aspect:terzi_pf_cognome"))
	public String getCognome() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCognome();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pf", property=@CMISProperty(name="sigla_commons_aspect:terzi_pf_nome"))
	public String getNome() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getNome();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pf", property=@CMISProperty(name="sigla_commons_aspect:terzi_pf_codfis"))
	public String getCodfis() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCodice_fiscale();
	}

	@CMISProperty(name="sigla_fatture:cod_amministrazione")
	public String getCodiceAmministrazione() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodAmm();
	}

	@CMISProperty(name="sigla_fatture:cod_registro")
	public String getCodiceRegistro() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodRegFA();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:cds_origine", property=@CMISProperty(name="sigla_commons_aspect:cds_origine_codice"))
	public String getCdsOrigine() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_cds_origine();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:uo_origine", property=@CMISProperty(name="sigla_commons_aspect:uo_origine_codice"))
	public String getUoOrigine() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_uo_origine();
	}


	public String getCMISPrincipalPath() {
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
                this.getFattura_attivaBulk().getCd_uo_origine(),
                "Fatture Attive"
        ).stream().collect(
                Collectors.joining(StoreService.BACKSLASH)
        );
	}

	public String getCMISPath(){
        return Arrays.asList(
                getCMISPrincipalPath(),
                Optional.ofNullable(getEsercizioFattura())
                        .map(esercizio -> String.valueOf(esercizio))
                        .orElse("0"),
                "Fattura " + this.getEsercizioFattura().toString() + Utility.lpad(this.getPgFattura().toString(),10,'0')
        ).stream().collect(
                Collectors.joining(StoreService.BACKSLASH)
        );
	}
	
	public Fattura_attivaBulk getFattura_attivaBulk() {
		return fattura_attivaBulk;
	}

	public void setFattura_attivaBulk(Fattura_attivaBulk fattura_attivaBulk) {
		this.fattura_attivaBulk = fattura_attivaBulk;
	}
}