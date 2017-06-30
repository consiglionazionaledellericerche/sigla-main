package it.cnr.contab.docamm00.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.dp.DigitalPreservationProperties;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@CMISType(name="F:sigla_fatture:fatture_passive")
public class CMISFolderFatturaPassiva extends OggettoBulk {
	private static final long serialVersionUID = 4110702628275029148L;

	private final DocumentoEleTestataBulk documentoEleTestata;
	private final BigInteger identificativoSdI;
	
	public CMISFolderFatturaPassiva(DocumentoEleTestataBulk documentoEleTestata, BigInteger identificativoSdI) {
    	super();
    	this.documentoEleTestata = documentoEleTestata;
    	this.identificativoSdI = identificativoSdI;
    }
	
	@CMISProperty(name="sigla_fatture:identificativoSdI")
	public Long getIdentificativoSdI() {
		return this.identificativoSdI.longValue();
	}
	
	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:codice"))
	public String getCodiceCds(){
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_cds();
	}
	
	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:codice"))
	public String getCodiceUo(){
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	
	@CMISPolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@CMISProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		return "SDI";
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi", property=@CMISProperty(name="sigla_commons_aspect:terzi_cd_terzo"))
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
	
	public CMISFolderFatturaPassiva(Fattura_passivaBulk fattura_passivaBulk, DocumentoEleTestataBulk documentoEleTestata) {
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

	@CMISProperty(name="sigla_fatture:esercizio")
    public Integer getEsercizioFattura() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getEsercizio();
    }

	@CMISProperty(name="sigla_fatture:pg_fattura", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getPgFattura() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getPg_fattura_passiva();
    }
	
	@CMISProperty(name="sigla_fatture:prot_iva", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getProtocolloIva() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProtocollo_iva();
    }

	@CMISProperty(name="sigla_fatture:prot_gen", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getProtocolloGenerale() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProtocollo_iva();
    }

	@CMISProperty(name="sigla_fatture:data_reg", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataRegistrazione(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_registrazione()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_registrazione();
	}
	
	@CMISProperty(name="sigla_fatture:descrizione")
	public String getDescrizione() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getDs_fattura_passiva();
	}
	
	@CMISProperty(name="sigla_fatture:imponibile")
	public BigDecimal getImponibile(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getIm_totale_imponibile();
	}
	
	@CMISProperty(name="sigla_fatture:iva")
	public BigDecimal getIva(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getIm_totale_iva();
	}
	
	@CMISProperty(name="sigla_fatture:importo_totale")
	public BigDecimal getImportoTotale(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getIm_totale_fattura();
	}
	
	@CMISProperty(name="sigla_fatture:divisa")
	public String getDivisa() {
		if (this.getFattura_passivaBulk()==null ||
				this.getFattura_passivaBulk().getValuta()==null)
				return null;
			return this.getFattura_passivaBulk().getValuta().getCd_divisa();
	}
	
	@CMISProperty(name="sigla_fatture:cambio")
	public BigDecimal getCambio(){
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCambio();
	}
	
	@CMISProperty(name="sigla_fatture:dt_competenza_dal", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaDal(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_da_competenza_coge()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_da_competenza_coge();
	}
	
	@CMISProperty(name="sigla_fatture:dt_competenza_al", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataCompetenzaAl(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_a_competenza_coge()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_a_competenza_coge();
	}
	
	@CMISProperty(name="sigla_fatture:data_documento", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public Timestamp getDataEmissione(){
		if (this.getFattura_passivaBulk()==null ||
			this.getFattura_passivaBulk().getDt_fattura_fornitore()==null)
			return null;
		return this.getFattura_passivaBulk().getDt_fattura_fornitore();
	}
	
	@CMISProperty(name="sigla_fatture:numero_documento")
	public String getNumeroDocumento() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getNr_fattura_fornitore();
	}

	@CMISProperty(name="sigla_fatture:prog_univoco_anno", converterBeanName="cmis.converter.longToIntegerConverter")
    public Long getProgrUnivocoAnno() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProgr_univoco();
    }
	
	@CMISProperty(name="sigla_fatture:tipo_documento")
	public String getTipoDocumento() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getTi_fattura();
	}

	@CMISProperty(name="sigla_fatture:cuu")
	public String getCodiceIpa() {
		if (this.documentoEleTestata == null)
			return null;
		return this.documentoEleTestata.getDocumentoEleTrasmissione().getCodiceDestinatario();
	}
	
	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pg", property=@CMISProperty(name="sigla_commons_aspect:terzi_pg_denominazione"))
	public String getRagioneSociale() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getRagione_sociale();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pg", property=@CMISProperty(name="sigla_commons_aspect:terzi_pg_pariva"))
	public String getPariva() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getPartita_iva();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pf", property=@CMISProperty(name="sigla_commons_aspect:terzi_pf_cognome"))
	public String getCognome() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCognome();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pf", property=@CMISProperty(name="sigla_commons_aspect:terzi_pf_nome"))
	public String getNome() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getNome();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:terzi_pf", property=@CMISProperty(name="sigla_commons_aspect:terzi_pf_codfis"))
	public String getCodfis() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCodice_fiscale();
	}

	@CMISProperty(name="sigla_fatture:cod_amministrazione")
	public String getCodiceAmministrazione() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodAmm();
	}

	@CMISProperty(name="sigla_fatture:cod_registro")
	public String getCodiceRegistro() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		if (dpProperties == null)
			return null;
		return dpProperties.getDigitalPreservationCodRegFP();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:cds_origine", property=@CMISProperty(name="sigla_commons_aspect:cds_origine_codice"))
	public String getCdsOrigine() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCd_cds_origine();
	}

	@CMISPolicy(name="P:sigla_commons_aspect:uo_origine", property=@CMISProperty(name="sigla_commons_aspect:uo_origine_codice"))
	public String getUoOrigine() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getCd_uo_origine();
	}

	@CMISProperty(name="sigla_fatture:progressivo_sdi")
	public Long getProgressivoSdi() {
		if (this.getFattura_passivaBulk()==null)
			return null;
		return this.getFattura_passivaBulk().getProgressivo();
	}
	
	public Boolean isProgressivoTrasmissioneZero(){
		if (getProgressivoSdi() == null){
			return false;
		}
		return getProgressivoSdi().equals(new Long ("0"));
	}

	public Boolean isProgressivoTrasmissioneDiversoDaZero(){
		if (getProgressivoSdi() == null){
			return false;
		}
		return !getProgressivoSdi().equals(new Long ("0"));
	}

	public void updateMetadataPropertiesCMIS() throws ApplicationException{
		StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
		StringBuffer query = new StringBuffer("select fat.cmis:objectId, fat.sigla_fatture:progressivo_sdi from sigla_fatture:fatture_passive fat ");
		query.append(" where fat.sigla_fatture:identificativoSdI = ").append(identificativoSdI);
		List<StorageObject> resultsFolder = storeService.search(query.toString());
		if (resultsFolder.size() == 0)
			throw new ApplicationException("Non esiste sul documentale una fattura con identificativo SDI "+identificativoSdI);
		else {
            StorageObject trasmissioneFolder = null;
			for (StorageObject storageObject : resultsFolder) {
                trasmissioneFolder = storageObject;
				BigInteger prog = storageObject.<BigInteger>getPropertyValue("sigla_fatture:progressivo_sdi");
				if (prog == null || prog.equals(new BigInteger("0"))){
					if (isProgressivoTrasmissioneZero()){
						storeService.updateMetadataFromBulk(storageObject, this);
						break;
					}
				}
			}
			if (isProgressivoTrasmissioneDiversoDaZero() && trasmissioneFolder != null){
                String objectKey = storeService.createFolderIfNotPresent(trasmissioneFolder.getPath(),
                        identificativoSdI + " - "+getProgressivoSdi(), null, null, this);
				if (objectKey != null){
                    storeService.setInheritedPermission(storeService.getStorageObjectBykey(objectKey), Boolean.FALSE);
				}
			}
		}
	}

	public Fattura_passivaBulk getFattura_passivaBulk() {
		return fattura_passivaBulk;
	}

	public void setFattura_passivaBulk(Fattura_passivaBulk fattura_passivaBulk) {
		this.fattura_passivaBulk = fattura_passivaBulk;
	}

}