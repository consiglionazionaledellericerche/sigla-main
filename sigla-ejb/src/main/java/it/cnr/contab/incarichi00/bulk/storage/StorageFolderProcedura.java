package it.cnr.contab.incarichi00.bulk.storage;

import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.si.spring.storage.converter.Converter;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@StorageType(name="F:sigla_contratti:procedura")
public class StorageFolderProcedura extends OggettoBulk {
	private static final long serialVersionUID = 4110702628275029148L;

	private Incarichi_proceduraBulk incaricoProcedura;

	public StorageFolderProcedura(Incarichi_proceduraBulk incaricoProcedura) {
    	super();
    	setIncaricoProcedura(incaricoProcedura);
    }

	private void setIncaricoProcedura(Incarichi_proceduraBulk incaricoProcedura) {
		this.incaricoProcedura = incaricoProcedura;
	}
	
	public Incarichi_proceduraBulk getIncaricoProcedura() {
		return incaricoProcedura;
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:esercizio"))
	public Integer getEsercizio() {
		if (this.getIncaricoProcedura()==null)
			return null;
		return this.getIncaricoProcedura().getEsercizio();
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:procedura", property=@StorageProperty(name="sigla_contratti_aspect_procedura:progressivo", converterBeanName="storage.converter.longToIntegerConverter"))
	public Long getPg_procedura() {
		if (this.getIncaricoProcedura()==null)
			return null;
		return this.getIncaricoProcedura().getPg_procedura();
	}
	
	@StorageProperty(name="sigla_contratti:oggetto")
	public String getOggetto() {
		if (this.getIncaricoProcedura()==null)
			return null;
		return this.getIncaricoProcedura().getOggetto();
	}
	
	@StorageProperty(name="sigla_contratti:procedura_amministrativa")
	public String getDescrizioneProceduraAmministrativa(){
		if (this.getIncaricoProcedura()==null ||
			this.getIncaricoProcedura().getProcedura_amministrativa()==null)
			return null;
		return this.getIncaricoProcedura().getProcedura_amministrativa().getDs_proc_amm();
	}
	
	@StoragePolicy(name="P:sigla_contratti_aspect:tipo_norma", property=@StorageProperty(name="sigla_contratti_aspect_tipo_norma:descrizione"))
	public String getTipoNormaDescrizione() {
		if (this.getIncaricoProcedura()==null || this.getIncaricoProcedura().getTipo_norma_perla()==null)
			return null;
		if (this.getIncaricoProcedura().getCd_tipo_norma_perla()!=null && this.getIncaricoProcedura().getCd_tipo_norma_perla().equals("999")) 
			return (this.getIncaricoProcedura().getDs_libera_norma_perla());
		return this.getIncaricoProcedura().getTipo_norma_perla().getDs_tipo_norma();
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:tipo_norma", property=@StorageProperty(name="sigla_contratti_aspect_tipo_norma:numero"))
	public String getTipoNormaNumero() {
		if (this.getIncaricoProcedura()==null || this.getIncaricoProcedura().getTipo_norma_perla()==null)
			return null;
		if (this.getIncaricoProcedura().getCd_tipo_norma_perla()!=null && this.getIncaricoProcedura().getCd_tipo_norma_perla().equals("999")) 
			return null;
		return this.getIncaricoProcedura().getTipo_norma_perla().getNumero_tipo_norma();
	}

	@SuppressWarnings("unchecked")
	@StoragePolicy(name="P:sigla_contratti_aspect:tipo_norma", property=@StorageProperty(name="sigla_contratti_aspect_tipo_norma:data"))
	public GregorianCalendar getTipoNormaData() {
		if (this.getIncaricoProcedura()==null || this.getIncaricoProcedura().getTipo_norma_perla()==null)
			return null;
		if (this.getIncaricoProcedura().getCd_tipo_norma_perla()!=null && this.getIncaricoProcedura().getCd_tipo_norma_perla().equals("999")) 
			return null;
		return (GregorianCalendar)SpringUtil.getBean("storage.converter.timestampToCalendarConverter", Converter.class).convert(this.getIncaricoProcedura().getTipo_norma_perla().getDt_tipo_norma());
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:tipo_norma", property=@StorageProperty(name="sigla_contratti_aspect_tipo_norma:articolo"))
	public String getTipoNormaArticolo() {
		if (this.getIncaricoProcedura()==null || this.getIncaricoProcedura().getTipo_norma_perla()==null)
			return null;
		if (this.getIncaricoProcedura().getCd_tipo_norma_perla()!=null && this.getIncaricoProcedura().getCd_tipo_norma_perla().equals("999")) 
			return null;
		return this.getIncaricoProcedura().getTipo_norma_perla().getArticolo_tipo_norma();
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:tipo_norma", property=@StorageProperty(name="sigla_contratti_aspect_tipo_norma:comma"))
	public String getTipoNormaComma() {
		if (this.getIncaricoProcedura()==null || this.getIncaricoProcedura().getTipo_norma_perla()==null)
			return null;
		if (this.getIncaricoProcedura().getCd_tipo_norma_perla()!=null && this.getIncaricoProcedura().getCd_tipo_norma_perla().equals("999")) 
			return null;
		return this.getIncaricoProcedura().getTipo_norma_perla().getComma_tipo_norma();
	}

	public String getCMISPrincipalPath() {
		String path = "";
		if (this.getIncaricoProcedura().isProceduraForIncarichi())
			path = "Incarichi di Collaborazione";
		else if (this.getIncaricoProcedura().isProceduraForBorseStudio())
			path = "Borse di Studio";
		else if (this.getIncaricoProcedura().isProceduraForAssegniRicerca())
			path = "Assegni di Ricerca";
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
                this.getIncaricoProcedura().getCd_unita_organizzativa(),
                path
        ).stream().collect(
                Collectors.joining(StorageService.SUFFIX)
        );
	}

	public String getCMISPath(){
        return SpringUtil.getBean("storeService", StoreService.class)
				.createFolderIfNotPresent(
                        Arrays.asList(
                                getCMISPrincipalPath(),
                                Optional.ofNullable(this.getEsercizio())
                                        .map(esercizio -> String.valueOf(esercizio))
                                        .orElse("0")
                        ).stream().collect(
                                Collectors.joining(StorageService.SUFFIX)
                        ),
						"Procedura "+this.getEsercizio().toString()+Utility.lpad(this.getPg_procedura().toString(),10,'0'),
						null, null, this);
	}
	
	public boolean isEqualsTo(StorageObject storageObject, List<String> listError){
		String initTesto = "Procedura "+this.getEsercizio().toString()+"/"+this.getPg_procedura().toString()+" - Disallineamento dato ";
		boolean isEquals = true;
		String valueDB=null, valueCMIS=null; 

		valueDB=String.valueOf(this.getEsercizio());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Esercizio - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getPg_procedura());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Pg_procedura - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getOggetto()).replaceAll("  ", " ").replaceAll("\r\n", "\n");
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti:oggetto"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Oggetto - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getDescrizioneProceduraAmministrativa()).replaceAll("  ", " ").replaceAll("\r\n", "\n");
		valueCMIS=String.valueOf(storageObject.getPropertyValue("sigla_contratti:procedura_amministrativa"));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Procedura Amministrativa - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTipoNormaDescrizione()).replaceAll("  ", " ").replaceAll("\r\n", "\n");
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_TIPO_NORMA_DESCRIZIONE.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Descrizione Tipo Norma - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		
		valueDB=String.valueOf(this.getTipoNormaNumero());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_TIPO_NORMA_NUMERO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Numero Tipo Norma - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTipoNormaArticolo());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_TIPO_NORMA_ARTICOLO.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Articolo Tipo Norma - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=String.valueOf(this.getTipoNormaComma());
		valueCMIS=String.valueOf(storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_TIPO_NORMA_COMMA.value()));
		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Comma Tipo Norma - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}

		valueDB=this.getTipoNormaData()==null?"":Integer.toString(this.getTipoNormaData().get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
												 Integer.toString(this.getTipoNormaData().get(GregorianCalendar.MONTH)) + "/" + 
												 Integer.toString(this.getTipoNormaData().get(GregorianCalendar.YEAR));
		GregorianCalendar gcCMIS = (GregorianCalendar)storageObject.getPropertyValue(StorageContrattiProperty.SIGLA_CONTRATTI_TIPO_NORMA_DATA.value());
		valueCMIS=gcCMIS==null?"":Integer.toString(gcCMIS.get(GregorianCalendar.DAY_OF_MONTH)) + "/" + 
				  				  Integer.toString(gcCMIS.get(GregorianCalendar.MONTH)) + "/" + 
				  				  Integer.toString(gcCMIS.get(GregorianCalendar.YEAR));

		if (!valueCMIS.equals(valueDB)) {
			listError.add(initTesto+" - Data Tipo Norma - DB:"+valueDB+" - CMIS:"+valueCMIS);
			isEquals = false;
		}
		return isEquals;
	}
}