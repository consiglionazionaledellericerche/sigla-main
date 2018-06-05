package it.cnr.contab.docamm00.storage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.dp.DigitalPreservationProperties;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.util.Utility;
import it.cnr.jada.comp.ApplicationException;

@StorageType(name="F:sigla_fatture:fatture_passive")
public class StorageFolderFatturaPassiva extends StorageFolderFattura {
	private transient final static Logger logger = LoggerFactory.getLogger(StorageFolderFatturaPassiva.class);
	private static final long serialVersionUID = 4110702628275029148L;

	private final DocumentoEleTestataBulk documentoEleTestata;
	private final BigInteger identificativoSdI;
	
	public StorageFolderFatturaPassiva(DocumentoEleTestataBulk documentoEleTestata, BigInteger identificativoSdI) {
    	super();
    	this.documentoEleTestata = documentoEleTestata;
    	this.identificativoSdI = identificativoSdI;
    }
	
	@StorageProperty(name="sigla_fatture:identificativoSdI")
	public Long getIdentificativoSdI() {
		return this.identificativoSdI.longValue();
	}
	
	@StoragePolicy(name="P:strorg:cds", property=@StorageProperty(name="strorgcds:codice"))
	public String getCodiceCds(){
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_cds();
	}
	
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:codice"))
	public String getCodiceUo(){
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	
	@StoragePolicy(name="P:sigla_commons_aspect:terzi", property=@StorageProperty(name="sigla_commons_aspect:terzi_cd_terzo"))
	public String getCodiceTerzo() {
		if (this.getFattura_passivaBulk() != null && this.getFattura_passivaBulk().getCd_terzo() != null){
			return this.getFattura_passivaBulk().getCd_terzo().toString();
		} else if (this.documentoEleTestata != null && this.documentoEleTestata.getDocumentoEleTrasmissione() != null && this.documentoEleTestata.getDocumentoEleTrasmissione().getPrestatoreCdTerzo() != null){
			return this.documentoEleTestata.getDocumentoEleTrasmissione().getPrestatoreCdTerzo().toString();
		}
		return null;
	}
	private Fattura_passivaBulk fattura_passivaBulk;
	private DigitalPreservationProperties dpProperties;
	
	public StorageFolderFatturaPassiva(Fattura_passivaBulk fattura_passivaBulk, DocumentoEleTestataBulk documentoEleTestata) {
    	super();
    	this.documentoEleTestata = documentoEleTestata;
    	this.identificativoSdI = BigInteger.valueOf(documentoEleTestata.getIdentificativoSdi());
    	setFattura_passivaBulk(fattura_passivaBulk);
    	loadProperties();
	}
	
	public void loadProperties(){
		if (dpProperties == null)
			dpProperties = SpringUtil.getBean("digitalPreservationProperties",DigitalPreservationProperties.class);
	}

	@StorageProperty(name="sigla_fatture:esercizio")
    public Integer getEsercizioFattura() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getEsercizio();
    }

	@StorageProperty(name="sigla_fatture:pg_fattura", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getPgFattura() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getPg_fattura_passiva();
    }
	
	@StorageProperty(name="sigla_fatture:prot_iva", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProtocolloIva() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProtocollo_iva();
    }

	@StorageProperty(name="sigla_fatture:prot_gen", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProtocolloGenerale() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProtocollo_iva();
    }

	@StorageProperty(name="sigla_fatture:data_reg", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataRegistrazione(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_registrazione()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_registrazione();
	}
	
	@StorageProperty(name="sigla_fatture:descrizione")
	public String getDescrizione() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getDs_fattura_passiva();
	}
	
	@StorageProperty(name="sigla_fatture:imponibile")
	public BigDecimal getImponibile(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getIm_totale_imponibile();
	}
	
	@StorageProperty(name="sigla_fatture:iva")
	public BigDecimal getIva(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getIm_totale_iva();
	}
	
	@StorageProperty(name="sigla_fatture:importo_totale")
	public BigDecimal getImportoTotale(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getIm_totale_fattura();
	}
	
	@StorageProperty(name="sigla_fatture:divisa")
	public String getDivisa() {
		if (this.getFattura_passivaBulk()==null ||
				this.getFattura_passivaBulk().getValuta()==null)
				return null;
			return this.getFattura_passivaBulk().getValuta().getCd_divisa();
	}
	
	@StorageProperty(name="sigla_fatture:cambio")
	public BigDecimal getCambio(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCambio();
	}
	
	@StorageProperty(name="sigla_fatture:dt_competenza_dal", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaDal(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_da_competenza_coge()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_da_competenza_coge();
	}
	
	@StorageProperty(name="sigla_fatture:dt_competenza_al", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaAl(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_a_competenza_coge()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_a_competenza_coge();
	}
	
	@StorageProperty(name="sigla_fatture:data_documento", converterBeanName="storage.converter.timestampToCalendarConverter")
	public Timestamp getDataEmissione(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_fattura_fornitore()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_fattura_fornitore();
	}
	
	@StorageProperty(name="sigla_fatture:numero_documento")
	public String getNumeroDocumento() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getNr_fattura_fornitore();
	}

	@StorageProperty(name="sigla_fatture:prog_univoco_anno", converterBeanName="storage.converter.longToIntegerConverter")
    public Long getProgrUnivocoAnno() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProgr_univoco();
    }
	
	@StorageProperty(name="sigla_fatture:tipo_documento")
	public String getTipoDocumento() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getTi_fattura();
	}

	@StorageProperty(name="sigla_fatture:cuu")
	public String getCodiceIpa() {
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getCodiceDestinatario();
	}
	
	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pg", property=@StorageProperty(name="sigla_commons_aspect:terzi_pg_denominazione"))
	public String getRagioneSociale() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getRagione_sociale();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pg", property=@StorageProperty(name="sigla_commons_aspect:terzi_pg_pariva"))
	public String getPariva() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getPartita_iva();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_cognome"))
	public String getCognome() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCognome();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_nome"))
	public String getNome() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getNome();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:terzi_pf", property=@StorageProperty(name="sigla_commons_aspect:terzi_pf_codfis"))
	public String getCodfis() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCodice_fiscale();
	}

	@StorageProperty(name="sigla_fatture:cod_amministrazione")
	public String getCodiceAmministrazione() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodAmm();
	}

	@StorageProperty(name="sigla_fatture:cod_registro")
	public String getCodiceRegistro() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodRegFP();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:cds_origine", property=@StorageProperty(name="sigla_commons_aspect:cds_origine_codice"))
	public String getCdsOrigine() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCd_cds_origine();
	}

	@StoragePolicy(name="P:sigla_commons_aspect:uo_origine", property=@StorageProperty(name="sigla_commons_aspect:uo_origine_codice"))
	public String getUoOrigine() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCd_uo_origine();
	}

	@StorageProperty(name="sigla_fatture:progressivo_sdi")
	public Long getProgressivoSdi() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProgressivo();
	}

	public void updateMetadataPropertiesCMIS() throws ApplicationException{
        StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
        StorageObject trasmissioneFolder = storeService.getStorageObjectBykey(documentoEleTestata.getDocumentoEleTrasmissione().getCmisNodeRef());
        Optional.ofNullable(trasmissioneFolder)
                .ifPresent(storageObject -> {
                    if (!Optional.ofNullable(storageObject.<BigInteger>getPropertyValue("sigla_fatture:progressivo_sdi"))
                            .filter(progressivo -> !progressivo.equals(BigInteger.ZERO))
                            .isPresent()) {
                        storeService.updateMetadataFromBulk(trasmissioneFolder, this);
                        Optional.ofNullable(storeService.createFolderIfNotPresent(trasmissioneFolder.getPath(),
                                identificativoSdI + " - "+getProgressivoSdi(), null, null, this))
                                .ifPresent(objectKey -> {
                                    storeService.setInheritedPermission(storeService.getStorageObjectByPath(objectKey), Boolean.FALSE);
                                });
                    }
                });
	}

	public Fattura_passivaBulk getFattura_passivaBulk() {
		return fattura_passivaBulk;
	}

	public void setFattura_passivaBulk(Fattura_passivaBulk fattura_passivaBulk) {
		this.fattura_passivaBulk = fattura_passivaBulk;
	}

}