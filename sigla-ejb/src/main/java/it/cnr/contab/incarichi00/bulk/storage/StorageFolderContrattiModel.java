package it.cnr.contab.incarichi00.bulk.storage;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.jada.bulk.OggettoBulk;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;

@StorageType(name="F:sigla_contratti:model")
public class StorageFolderContrattiModel extends OggettoBulk {
	private static final long serialVersionUID = -73464472624869795L;

	private Incarichi_repertorioBulk incaricoRepertorio;

	public StorageFolderContrattiModel(Incarichi_repertorioBulk incaricoRepertorio) {
    	super();
    	setIncaricoRepertorio(incaricoRepertorio);
    }

	private void setIncaricoRepertorio(Incarichi_repertorioBulk incaricoRepertorio) {
		this.incaricoRepertorio = incaricoRepertorio;
	}
	
	public Incarichi_repertorioBulk getIncaricoRepertorio() {
		return incaricoRepertorio;
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:esercizio"))
	public Integer getEsercizio_procedura() {
		if (this.getIncaricoRepertorio()==null || 
			this.getIncaricoRepertorio().getIncarichi_procedura() == null)
			return null;
		return this.getIncaricoRepertorio().getIncarichi_procedura().getEsercizio();
	}
	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
	public Long getPg_procedura() {
		if (this.getIncaricoRepertorio()==null || 
			this.getIncaricoRepertorio().getIncarichi_procedura() == null)
			return null;
		return this.getIncaricoRepertorio().getIncarichi_procedura().getPg_procedura();
	}

	@StorageProperty(name="sigla_contratti:importo_contratto")
	public BigDecimal getImporto_lordo() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getImporto_lordo();
	}
	
	@StorageProperty(name="sigla_contratti:data_stipula", converterBeanName="storage.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDt_stipula() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getDt_stipula();
	}
	
	@StorageProperty(name="sigla_contratti:data_inizio", converterBeanName="storage.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDt_inizio_validita() {
		if (this.getIncaricoRepertorio()==null)
			return null;
		return this.getIncaricoRepertorio().getDt_inizio_validita();
	}

	@StorageProperty(name="sigla_contratti:nr_provvedimento_nomina")
	public String getNr_provv() {
		if (this.getIncaricoRepertorio()==null || this.getIncaricoRepertorio().getNr_provv()==null)
			return null;
		return this.getIncaricoRepertorio().getNr_provv().toString();
	}
	
	@StorageProperty(name="sigla_contratti:dt_provvedimento_nomina", converterBeanName="storage.converter.timestampToCalendarConverter")
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

	public String getCMISParentPath() {
		if (this.getIncaricoRepertorio()==null || 
			this.getIncaricoRepertorio().getIncarichi_procedura() == null)
			return null;
		return this.getIncaricoRepertorio().getIncarichi_procedura().getCMISFolder().getCMISPath();
	}

	public String getCMISPath(){
		return null;
	}

	public boolean isEqualsTo(StorageObject storageObject, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizio_procedura().toString()+"/"+this.getPg_procedura().toString()+" - "+
						   "Incarico "+this.getEsercizio().toString()+"/"+this.getPg_repertorio().toString()+" - Disallineamento dato ";
		boolean isEquals = true;
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizio_procedura());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio Procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPg_procedura());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getImporto_lordo());
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti:importo_contratto"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Importo - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getNr_provv());
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti:nr_provvedimento_nomina"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Numero Provvedimento Nomina - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		GregorianCalendar gcDB = (GregorianCalendar)GregorianCalendar.getInstance(); 
		if (this.getDt_provv()!=null) gcDB.setTime(this.getDt_provv());
		valueDB=Integer.toString(gcDB.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.YEAR));
		GregorianCalendar gcCMIS = (GregorianCalendar)storageObject.getPropertyValue("sigla_contratti:dt_provvedimento_nomina");
		if (gcCMIS==null) gcCMIS = (GregorianCalendar)GregorianCalendar.getInstance(); 
		valueCMIS=Integer.toString(gcCMIS.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.YEAR));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Data Provvedimento Nomina - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		
		gcDB = (GregorianCalendar)GregorianCalendar.getInstance(); 
		if (this.getDt_stipula()!=null) gcDB.setTime(this.getDt_stipula());
		valueDB=Integer.toString(gcDB.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.YEAR));
		gcCMIS = (GregorianCalendar)storageObject.getPropertyValue("sigla_contratti:data_stipula");
		if (gcCMIS==null) gcCMIS = (GregorianCalendar)GregorianCalendar.getInstance(); 
		valueCMIS=Integer.toString(gcCMIS.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.YEAR));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Data Stipula - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		gcDB = (GregorianCalendar)GregorianCalendar.getInstance(); 
		if (this.getDt_inizio_validita()!=null) gcDB.setTime(this.getDt_inizio_validita());
		valueDB=Integer.toString(gcDB.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.MONTH)) + "/" + 
				Integer.toString(gcDB.get(GregorianCalendar.YEAR));
		gcCMIS = (GregorianCalendar)storageObject.getPropertyValue("sigla_contratti:data_inizio");
		if (gcCMIS==null) gcCMIS = (GregorianCalendar)GregorianCalendar.getInstance(); 
		valueCMIS=Integer.toString(gcCMIS.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.MONTH)) + "/" + 
				  Integer.toString(gcCMIS.get(GregorianCalendar.YEAR));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Data Inizio Validità - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		
		return isEquals;
	}
}
