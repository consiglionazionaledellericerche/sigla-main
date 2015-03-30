package it.cnr.contab.docamm00.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;

import java.math.BigDecimal;
import java.sql.Timestamp;

@CMISType(name="F:sigla_fatture:fatture_attive")
public class CMISFolderFatturaAttiva extends OggettoBulk {
	private static final long serialVersionUID = 4110702628275029148L;

	private Fattura_attivaBulk fattura_attivaBulk;

	public CMISFolderFatturaAttiva(Fattura_attivaBulk fattura_attivaBulk) {
    	super();
    	setFattura_attivaBulk(fattura_attivaBulk);
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

	public CMISPath getCMISPrincipalPath(SiglaCMISService cmisService) throws ApplicationException{
		CMISPath cmisPath = SpringUtil.getBean("cmisPathFatture",CMISPath.class);
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, this.getFattura_attivaBulk().getCd_uo_origine(), getFattura_attivaBulk().getCd_uo_origine(), getFattura_attivaBulk().getCd_uo_origine());
		cmisPath = cmisService.createFolderIfNotPresent(cmisPath, "Fatture Attive", "Fatture Attive", "Fatture Attive");
		return cmisPath;
	}

	public CMISPath getCMISPath(SiglaCMISService cmisService) throws ApplicationException{
		CMISPath cmisPath = this.getCMISPrincipalPath(cmisService);
		if (cmisPath!=null) {
			cmisPath = cmisService.createFolderIfNotPresent(cmisPath, this.getEsercizioFattura().toString(), "Esercizio "+this.getEsercizioFattura().toString(), "Esercizio "+this.getEsercizioFattura().toString());
			cmisPath = cmisService.createFolderIfNotPresent(cmisPath, "Fattura "+this.getEsercizioFattura().toString()+Utility.lpad(this.getPgFattura().toString(),10,'0'), "Fattura "+this.getEsercizioFattura().toString()+"/"+this.getPgFattura().toString(), "Fattura "+this.getEsercizioFattura().toString()+"/"+this.getPgFattura().toString(), this);
			cmisService.setInheritedPermission(cmisPath, Boolean.FALSE);
		}
		return cmisPath;
	}
	
	public Fattura_attivaBulk getFattura_attivaBulk() {
		return fattura_attivaBulk;
	}

	public void setFattura_attivaBulk(Fattura_attivaBulk fattura_attivaBulk) {
		this.fattura_attivaBulk = fattura_attivaBulk;
	}
}