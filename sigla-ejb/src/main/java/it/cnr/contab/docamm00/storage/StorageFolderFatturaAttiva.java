package it.cnr.contab.docamm00.storage;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.dp.DigitalPreservationProperties;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.util.Utility;

@StorageType(name="F:sigla_fatture:fatture_attive")
public class StorageFolderFatturaAttiva extends StorageFolderFattura {
	private static final long serialVersionUID = 4110702628275029148L;

	private Fattura_attivaBulk fattura_attivaBulk;
	private DigitalPreservationProperties dpProperties;
	
	public StorageFolderFatturaAttiva(Fattura_attivaBulk fattura_attivaBulk) {
    	super();
    	setFattura_attivaBulk(fattura_attivaBulk);
    	loadProperties();
	}
	
	public void loadProperties(){
		if (dpProperties == null)
			dpProperties = SpringUtil.getBean("digitalPreservationProperties",DigitalPreservationProperties.class);
	}

	@StorageProperty(name="sigla_fatture:esercizio")
    public Integer getEsercizioFattura() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getEsercizio();
    }

	@StorageProperty(name="sigla_fatture:pg_fattura", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getPgFattura() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getPg_fattura_attiva();
    }
	
	@StorageProperty(name="sigla_fatture:prot_iva", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProtocolloIva() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getProtocollo_iva();
    }

	@StorageProperty(name="sigla_fatture:prot_gen", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProtocolloGenerale() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getProtocollo_iva();
    }

	@StorageProperty(name="sigla_fatture:data_reg", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataRegistrazione(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_registrazione()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_registrazione();
	}
	
	@StorageProperty(name="sigla_fatture:descrizione")
	public String getDescrizione() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getDs_fattura_attiva();
	}
	
	@StorageProperty(name="sigla_fatture:imponibile")
	public BigDecimal getImponibile(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getIm_totale_imponibile();
	}
	
	@StorageProperty(name="sigla_fatture:iva")
	public BigDecimal getIva(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getIm_totale_iva();
	}
	
	@StorageProperty(name="sigla_fatture:importo_totale")
	public BigDecimal getImportoTotale(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getIm_totale_fattura();
	}
	
	@StorageProperty(name="sigla_fatture:divisa")
	public String getDivisa() {
		if (this.getFattura_attivaBulk()==null ||
				this.getFattura_attivaBulk().getValuta()==null)
				return null;
			return this.getFattura_attivaBulk().getValuta().getCd_divisa();
	}
	
	@StorageProperty(name="sigla_fatture:cambio")
	public BigDecimal getCambio(){
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCambio();
	}
	
	@StorageProperty(name="sigla_fatture:dt_competenza_dal", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaDal(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_da_competenza_coge()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_da_competenza_coge();
	}
	
	@StorageProperty(name="sigla_fatture:dt_competenza_al", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaAl(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_a_competenza_coge()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_a_competenza_coge();
	}
	
	@StorageProperty(name="sigla_fatture:data_emissione", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataEmissione(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDt_emissione()==null)
			return null;
		return this.getFattura_attivaBulk().getDt_emissione();
	}
	
	@StorageProperty(name="sigla_fatture:codice_ipa")
	public String getCodiceIpa() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		if (this.getFattura_attivaBulk().getCodiceUnivocoUfficioIpa() != null){
			return this.getFattura_attivaBulk().getCodiceUnivocoUfficioIpa();
		}
		return this.getFattura_attivaBulk().getCodiceDestinatarioFatt();
	}
	
	@StorageProperty(name="sigla_fatture:codice_invio_sdi")
	public String getCodiceInvioSdi() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCodiceInvioSdi();
	}
	
	@StorageProperty(name="sigla_fatture:stato_invio_sdi")
	public String getStatoInvioSdi() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return (String)this.getFattura_attivaBulk().recuperoStatoInvioSdiKeys();
	}
		
	@StorageProperty(name="sigla_fatture:note_invio_sdi")
	public String getNoteInvioSdi() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getNoteInvioSdi();
	}
		
	@StorageProperty(name="sigla_fatture:data_consegna_sdi", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataConsegnaSdi(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getDtConsegnaSdi()==null)
			return null;
		return this.getFattura_attivaBulk().getDtConsegnaSdi();
	}
	
	@StoragePolicy(name="P:strorg:cds", property=@StorageProperty(name="strorgcds:codice"))
	public String getCodiceCds(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getCd_cds()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_cds();
	}
	
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:codice"))
	public String getCodiceUo(){
		if (this.getFattura_attivaBulk()==null ||
			this.getFattura_attivaBulk().getCd_unita_organizzativa()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_unita_organizzativa();
	}
	
	@StoragePolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@StorageProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getUtuv();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi", property=@StorageProperty(name="sigla_commons_aspect:terzi_cd_terzo"))
	public String getCodiceTerzo() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_terzo().toString();
	}

	@StorageProperty(name="sigla_fatture:modalita_incasso")
	public String getModalitaIncasso() {
		if (this.getFattura_attivaBulk()==null||this.getFattura_attivaBulk().getModalita_pagamento_uo() ==null)
			return null;
		return this.getFattura_attivaBulk().getModalita_pagamento_uo().getCd_modalita_pag();
	}

	@StorageProperty(name="sigla_fatture:prog_univoco_anno", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProgrUnivocoAnno() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getProgrUnivocoAnno();
    }
	
	@StorageProperty(name="sigla_fatture:tipo_documento")
	public String getTipoDocumento() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getTi_fattura();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pg", property=@StorageProperty(name="sigla_commons_aspect:terzi_pg_denominazione"))
	public String getRagioneSociale() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getRagione_sociale();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pg", property=@StorageProperty(name="sigla_commons_aspect:terzi_pg_pariva"))
	public String getPariva() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getPartita_iva();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_cognome"))
	public String getCognome() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCognome();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_nome"))
	public String getNome() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getNome();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_codfis"))
	public String getCodfis() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCodice_fiscale();
	}

	@StorageProperty(name="sigla_fatture:cod_amministrazione")
	public String getCodiceAmministrazione() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodAmm();
	}

	@StorageProperty(name="sigla_fatture:cod_registro")
	public String getCodiceRegistro() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodRegFA();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:cds_origine", property=@StorageProperty(name="sigla_commons_aspect:cds_origine_codice"))
	public String getCdsOrigine() {
		if (this.getFattura_attivaBulk()==null)
			return null;
		return this.getFattura_attivaBulk().getCd_cds_origine();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:uo_origine", property=@StorageProperty(name="sigla_commons_aspect:uo_origine_codice"))
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
                Collectors.joining(StorageService.SUFFIX)
        );
	}

	public String getCMISPath(){
		return SpringUtil.getBean("storeService", StoreService.class)
				.createFolderIfNotPresent(
						getPathFolderFatturaAttiva(),
						getLastFolderFatturaAttiva(),
						null, null, this);
	}

	public String getPathFolderFatturaAttiva() {
		return getCMISPrincipalPath().concat(StorageService.SUFFIX).concat(
				Optional.ofNullable(getEsercizioFattura())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0")
		);
	}

	private String getLastFolderFatturaAttiva() {
		final String folderName = "Fattura " + this.getEsercizioFattura().toString() +
				Utility.lpad(this.getPgFattura().toString(),10,'0');
		return folderName;
	}
	
	public String getCMISPathForSearch(){
        return Arrays.asList(
        		getPathFolderFatturaAttiva(),
        		getLastFolderFatturaAttiva()
        ).stream().collect(
                Collectors.joining(StorageService.SUFFIX)
        );
	}

	public Fattura_attivaBulk getFattura_attivaBulk() {
		return fattura_attivaBulk;
	}

	public void setFattura_attivaBulk(Fattura_attivaBulk fattura_attivaBulk) {
		this.fattura_attivaBulk = fattura_attivaBulk;
	}
}