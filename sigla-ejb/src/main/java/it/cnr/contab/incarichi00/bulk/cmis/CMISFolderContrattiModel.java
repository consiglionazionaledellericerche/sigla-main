package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.jada.bulk.OggettoBulk;

import java.math.BigDecimal;

@CMISType(name="F:sigla_contratti:model")
public class CMISFolderContrattiModel extends OggettoBulk {
	private static final long serialVersionUID = -73464472624869795L;

	private Incarichi_repertorioBulk incaricoRepertorio;

	public CMISFolderContrattiModel(Incarichi_repertorioBulk incaricoRepertorio) {
    	super();
    	setIncaricoRepertorio(incaricoRepertorio);
    }

	private void setIncaricoRepertorio(Incarichi_repertorioBulk incaricoRepertorio) {
		this.incaricoRepertorio = incaricoRepertorio;
	}
	
	public Incarichi_repertorioBulk getIncaricoRepertorio() {
		return incaricoRepertorio;
	}
	
	@CMISPolicy(name="P:sigla_contratti_aspect:procedura", property=@CMISProperty(name="sigla_contratti_aspect_procedura:esercizio"))
	public Integer getEsercizio_procedura() {
		if (this.getIncaricoRepertorio()==null || 
			this.getIncaricoRepertorio().getIncarichi_procedura() == null)
			return null;
		return this.getIncaricoRepertorio().getIncarichi_procedura().getEsercizio();
	}
	@CMISPolicy(name="P:sigla_contratti_aspect:procedura", property=@CMISProperty(name="sigla_contratti_aspect_procedura:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
	public Long getPg_procedura() {
		if (this.getIncaricoRepertorio()==null || 
			this.getIncaricoRepertorio().getIncarichi_procedura() == null)
			return null;
		return this.getIncaricoRepertorio().getIncarichi_procedura().getPg_procedura();
	}

	@CMISProperty(name="sigla_contratti:importo_contratto")
	public BigDecimal getImporto_lordo() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getImporto_lordo();
	}
	
	@CMISProperty(name="sigla_contratti:data_stipula", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDt_stipula() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getDt_stipula();
	}
	
	@CMISProperty(name="sigla_contratti:data_inizio", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDt_inizio_validita() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getDt_inizio_validita();
	}

	@CMISProperty(name="sigla_contratti:nr_provvedimento_nomina")
	public String getNr_provv() {
		if (this.getIncaricoRepertorio()==null || this.getIncaricoRepertorio().getNr_provv()==null)
			return null;
		return this.getIncaricoRepertorio().getNr_provv().toString();
	}
	
	@CMISProperty(name="sigla_contratti:dt_provvedimento_nomina", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDt_provv() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getDt_provv();
	}

	public Integer getEsercizio() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getEsercizio();
	}
	
	public Long getPg_repertorio() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getPg_repertorio();
	}

	public CMISPath getCMISParentPath(CMISService cmisService){
		if (this.getIncaricoRepertorio()==null || 
			this.getIncaricoRepertorio().getIncarichi_procedura() == null)
			return null;
		CMISPath cmisPath = this.getIncaricoRepertorio().getIncarichi_procedura().getCMISFolder().getCMISPath(cmisService);
		return cmisPath;
	}

	public CMISPath getCMISPath(CMISService cmisService){
		return null;
	}
}
