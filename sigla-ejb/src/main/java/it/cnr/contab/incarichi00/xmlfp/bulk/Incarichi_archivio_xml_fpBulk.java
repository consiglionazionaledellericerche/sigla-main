/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;

import it.cnr.contab.incarichi00.xmlfp.Comunicazione;
import it.cnr.contab.incarichi00.xmlfp.Esito;
import it.cnr.contab.incarichi00.xmlfp.EsitoComunicazione;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Incarichi_archivio_xml_fpBulk extends Incarichi_archivio_xml_fpBase {
	public final static Dictionary ti_dataCalcoloKeys = new it.cnr.jada.util.OrderedHashtable();
	public final static Dictionary ti_pagamentiKeys = new it.cnr.jada.util.OrderedHashtable();
	public final static Dictionary ti_semestriKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String DATA_STIPULA = "S";
	final public static String DATA_INIZIO_ATTIVITA = "I";

	static {
		ti_dataCalcoloKeys.put(DATA_STIPULA,"Data Stipula");
		ti_dataCalcoloKeys.put(DATA_INIZIO_ATTIVITA,"Data Inizio Incarico");
	}

	final public static String PAGAMENTI_INCLUDI = "I";
	final public static String PAGAMENTI_ESCLUDI = "E";
	final public static String PAGAMENTI_ONLY = "O";

	static {
		ti_pagamentiKeys.put(PAGAMENTI_INCLUDI,"Includi");
		ti_pagamentiKeys.put(PAGAMENTI_ESCLUDI,"Escludi");
	}

	final public static Integer PRIMO_SEMESTRE = 1;
	final public static Integer SECONDO_SEMESTRE = 2;

	static {
		ti_semestriKeys.put(PRIMO_SEMESTRE,"Primo Semestre");
		ti_semestriKeys.put(SECONDO_SEMESTRE,"Secondo Semestre");
	}

	private java.lang.Integer esercizio;

	private java.lang.Integer semestre;
	
	private java.lang.String dt_calcolo;
	
	private boolean fl_crea_file_per_tipologia = false;

	private boolean fl_crea_file_da_file = false;

	private java.lang.Integer num_max_file_record = 1000;

	private boolean fl_visualizza_file_xml = false;

	private java.lang.String tipo_estrazione_pagamenti;

	private java.lang.String blob_inv;

	private java.lang.String blob_ric;
	
	private java.lang.String blob_ric_err;

	private File file_inv;
	
	private File file_ric;

	private File file_ric_err;

	private Comunicazione comunicazione;
	
	private EsitoComunicazione esitoComunicazione;
	
	private EsitoComunicazione esitoComunicazioneConErrori;

	private String pathFileZip;
	
	private Map<String,Comunicazione> mapFileNuovoIncaricoXML, mapFileModificaIncaricoXML, mapFileCancellaIncaricoXML;

	private Map<String,EsitoComunicazione> mapFileAnomalieNuovoIncaricoXML, mapFileAnomalieModificaIncaricoXML, mapFileAnomalieCancellaIncaricoXML;

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.Integer getSemestre() {
		return semestre;
	}

	public void setSemestre(java.lang.Integer semestre) {
		this.semestre = semestre;
	}

	public java.lang.String getDt_calcolo() {
		return dt_calcolo;
	}

	public void setDt_calcolo(java.lang.String dtCalcolo) {
		dt_calcolo = dtCalcolo;
	}

	public boolean isFl_crea_file_per_tipologia() {
		return fl_crea_file_per_tipologia;
	}

	public void setFl_crea_file_per_tipologia(boolean flCreaFilePerTipologia) {
		fl_crea_file_per_tipologia = flCreaFilePerTipologia;
	}

	public java.lang.Integer getNum_max_file_record() {
		return num_max_file_record;
	}

	public void setNum_max_file_record(java.lang.Integer numMaxFileRecord) {
		num_max_file_record = numMaxFileRecord;
	}

	public java.lang.String getBlob_inv() {
		return blob_inv;
	}

	public boolean isROSelezione() {
		return (getMapFileNuovoIncaricoXML()!=null && !getMapFileNuovoIncaricoXML().isEmpty()) ||
		       (getMapFileModificaIncaricoXML()!=null && !getMapFileModificaIncaricoXML().isEmpty()) ||
		       (getMapFileCancellaIncaricoXML()!=null && !getMapFileCancellaIncaricoXML().isEmpty()) ||
		       (getMapFileAnomalieNuovoIncaricoXML()!=null && !getMapFileAnomalieNuovoIncaricoXML().isEmpty()) ||
		       (getMapFileAnomalieModificaIncaricoXML()!=null && !getMapFileAnomalieModificaIncaricoXML().isEmpty()) ||
		       (getMapFileAnomalieCancellaIncaricoXML()!=null && !getMapFileAnomalieCancellaIncaricoXML().isEmpty());
	}
	
	public void setBlob_inv(java.lang.String blobInv) {
		blob_inv = blobInv;
	}

	public java.lang.String getBlob_ric() {
		return blob_ric;
	}

	public void setBlob_ric(java.lang.String blobRic) {
		blob_ric = blobRic;
	}

	public java.lang.String getBlob_ric_err() {
		return blob_ric_err;
	}

	public void setBlob_ric_err(java.lang.String blobRicErr) {
		blob_ric_err = blobRicErr;
	}

	public File getFile_inv() {
		return file_inv;
	}

	public void setFile_inv(File fileInv) {
		file_inv = fileInv;
	}

	public File getFile_ric() {
		return file_ric;
	}

	public void setFile_ric(File fileRic) {
		file_ric = fileRic;
	}

	public File getFile_ric_err() {
		return file_ric_err;
	}

	public void setFile_ric_err(File fileRic_err) {
		file_ric_err = fileRic_err;
	}

	public Comunicazione getComunicazione() {
		return comunicazione;
	}

	public void setComunicazione(Comunicazione comunicazione) {
		this.comunicazione = comunicazione;
	}

	public EsitoComunicazione getEsitoComunicazione() {
		return esitoComunicazione;
	}

	public void setEsitoComunicazione(EsitoComunicazione esitoComunicazione) {
		this.esitoComunicazione = esitoComunicazione;
	}

	public EsitoComunicazione getEsitoComunicazioneConErrori() {
		return esitoComunicazioneConErrori;
	}

	public void setEsitoComunicazioneConErrori(EsitoComunicazione esitoComunicazioneConErrori) {
		this.esitoComunicazioneConErrori = esitoComunicazioneConErrori;
	}

	public Map<String, Comunicazione> getMapFileNuovoIncaricoXML() {
		return mapFileNuovoIncaricoXML;
	}

	public void setMapFileNuovoIncaricoXML(Map<String, Comunicazione> mapFileNuovoIncaricoXML) {
		this.mapFileNuovoIncaricoXML = mapFileNuovoIncaricoXML;
	}

	public Map<String, Comunicazione> getMapFileModificaIncaricoXML() {
		return mapFileModificaIncaricoXML;
	}

	public void setMapFileModificaIncaricoXML(
			Map<String, Comunicazione> mapFileModificaIncaricoXML) {
		this.mapFileModificaIncaricoXML = mapFileModificaIncaricoXML;
	}

	public Map<String, Comunicazione> getMapFileCancellaIncaricoXML() {
		return mapFileCancellaIncaricoXML;
	}

	public void setMapFileCancellaIncaricoXML(
			Map<String, Comunicazione> mapFileCancellaIncaricoXML) {
		this.mapFileCancellaIncaricoXML = mapFileCancellaIncaricoXML;
	}

	public Map<String, EsitoComunicazione> getMapFileAnomalieNuovoIncaricoXML() {
		return mapFileAnomalieNuovoIncaricoXML;
	}

	public void setMapFileAnomalieNuovoIncaricoXML(
			Map<String, EsitoComunicazione> mapFileAnomalieNuovoIncaricoXML) {
		this.mapFileAnomalieNuovoIncaricoXML = mapFileAnomalieNuovoIncaricoXML;
	}

	public Map<String, EsitoComunicazione> getMapFileAnomalieModificaIncaricoXML() {
		return mapFileAnomalieModificaIncaricoXML;
	}

	public void setMapFileAnomalieModificaIncaricoXML(
			Map<String, EsitoComunicazione> mapFileAnomalieModificaIncaricoXML) {
		this.mapFileAnomalieModificaIncaricoXML = mapFileAnomalieModificaIncaricoXML;
	}

	public Map<String, EsitoComunicazione> getMapFileAnomalieCancellaIncaricoXML() {
		return mapFileAnomalieCancellaIncaricoXML;
	}

	public void setMapFileAnomalieCancellaIncaricoXML(
			Map<String, EsitoComunicazione> mapFileAnomalieCancellaIncaricoXML) {
		this.mapFileAnomalieCancellaIncaricoXML = mapFileAnomalieCancellaIncaricoXML;
	}

	public String getPathFileZip() {
		return pathFileZip;
	}

	public void setPathFileZip(String pathFileZip) {
		this.pathFileZip = pathFileZip;
	}

	public List<String> getElencoPathFileNuovoIncaricoXML() {
		if (getMapFileNuovoIncaricoXML()!=null)
			return new ArrayList<String>(getMapFileNuovoIncaricoXML().keySet());
		return null;
	}

	public List<String> getElencoPathFileModificaIncaricoXML() {
		if (getMapFileModificaIncaricoXML()!=null)
			return new ArrayList<String>(getMapFileModificaIncaricoXML().keySet());
		return null;
	}

	public List<String> getElencoPathFileCancellaIncaricoXML() {
		if (getMapFileCancellaIncaricoXML()!=null)
			return new ArrayList<String>(getMapFileCancellaIncaricoXML().keySet());
		return null;
	}

	public List<String> getElencoPathFileAnomalieNuovoIncaricoXML() {
		if (getMapFileAnomalieNuovoIncaricoXML()!=null)
			return new ArrayList<String>(getMapFileAnomalieNuovoIncaricoXML().keySet());
		return null;
	}

	public List<String> getElencoPathFileAnomalieModificaIncaricoXML() {
		if (getMapFileAnomalieModificaIncaricoXML()!=null)
			return new ArrayList<String>(getMapFileAnomalieModificaIncaricoXML().keySet());
		return null;
	}

	public List<String> getElencoPathFileAnomalieCancellaIncaricoXML() {
		if (getMapFileAnomalieCancellaIncaricoXML()!=null)
			return new ArrayList<String>(getMapFileAnomalieCancellaIncaricoXML().keySet());
		return null;
	}

	public Integer getCountInsIncarichiComunicati() {
		if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getNuovoIncarico()==null ||
			getComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
			return 0;
		return getComunicazione().getConsulenti().getNuovoIncarico().size();
	}

	public Integer getCountUpdIncarichiComunicati() {
		if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getModificaIncarico()==null ||
			getComunicazione().getConsulenti().getModificaIncarico().isEmpty())
			return 0;
		return getComunicazione().getConsulenti().getModificaIncarico().size();
	}

	public Integer getCountDelIncarichiComunicati() {
		if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getCancellaIncarico()==null ||
			getComunicazione().getConsulenti().getCancellaIncarico().isEmpty())
			return 0;
		return getComunicazione().getConsulenti().getCancellaIncarico().size();
	}
	
	public Integer getCountInsIncarichiOkRicevuti() {
		if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getNuovoIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
			return 0;
		if (getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK))
			return getEsitoComunicazione().getConsulenti().getNuovoIncarico().size();
		int countOk=0;
		for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
			if (type.getEsito()==null || type.getEsito().equals(Esito.OK)) countOk++;
		}
		return countOk;
	}

	public Integer getCountUpdIncarichiOkRicevuti() {
		if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getModificaIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getModificaIncarico().isEmpty())
			return 0;
		if (getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK))
			return getEsitoComunicazione().getConsulenti().getModificaIncarico().size();
		int countOk=0;
		for (Iterator<EsitoComunicazione.Consulenti.ModificaIncarico> iterator = getEsitoComunicazione().getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.ModificaIncarico type = iterator.next();
			if (type.getEsito()==null || type.getEsito().equals(Esito.OK)) countOk++;
		}
		return countOk;
	}

	public Integer getCountDelIncarichiOkRicevuti() {
		if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getCancellaIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getCancellaIncarico().isEmpty())
			return 0;
		if (getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK))
			return getEsitoComunicazione().getConsulenti().getCancellaIncarico().size();
		int countOk=0;
		for (Iterator<EsitoComunicazione.Consulenti.CancellaIncarico> iterator = getEsitoComunicazione().getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.CancellaIncarico type = iterator.next();
			if (type.getEsito()==null || type.getEsito().equals(Esito.OK)) countOk++;
		}
		return countOk;
	}

	public Integer getCountInsIncarichiErrRicevuti() {
		if (getEsitoComunicazione()==null || 
			(getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK)) ||
			getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getNuovoIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
			return 0;
		int countErr=0;
		for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
			if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) countErr++;
		}
		return countErr;
	}

	public Integer getCountUpdIncarichiErrRicevuti() {
		if (getEsitoComunicazione()==null || 
			(getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK)) ||
			getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getModificaIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getModificaIncarico().isEmpty())
			return 0;
		int countErr=0;
		for (Iterator<EsitoComunicazione.Consulenti.ModificaIncarico> iterator = getEsitoComunicazione().getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.ModificaIncarico type = iterator.next();
			if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) countErr++;
		}
		return countErr;
	}

	public Integer getCountDelIncarichiErrRicevuti() {
		if (getEsitoComunicazione()==null || 
			(getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK)) ||
			getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getCancellaIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getCancellaIncarico().isEmpty())
			return 0;
		int countErr=0;
		for (Iterator<EsitoComunicazione.Consulenti.CancellaIncarico> iterator = getEsitoComunicazione().getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.CancellaIncarico type = iterator.next();
			if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) countErr++;
		}
		return countErr;
	}

	public Integer getCountInsIncarichiOkEstratti() {
		if (getMapFileNuovoIncaricoXML()==null || getMapFileNuovoIncaricoXML().isEmpty())
			return 0;
		int countOk=0;
		for (Iterator<String> iterator = getMapFileNuovoIncaricoXML().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Comunicazione value = getMapFileNuovoIncaricoXML().get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getNuovoIncarico()!=null)
				countOk = countOk + value.getConsulenti().getNuovoIncarico().size();
		}
		return countOk;
	}

	public Integer getCountUpdIncarichiOkEstratti() {
		Map<String, Comunicazione> map = null;
		if (isFl_crea_file_per_tipologia())
			map = getMapFileModificaIncaricoXML();
		else
			map = getMapFileNuovoIncaricoXML();

		if (map==null || map.isEmpty())
			return 0;
		int countOk=0;
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Comunicazione value = map.get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getModificaIncarico()!=null)
				countOk = countOk + value.getConsulenti().getModificaIncarico().size();
		}
		return countOk;
	}

	public Integer getCountDelIncarichiOkEstratti() {
		Map<String, Comunicazione> map = null;
		if (isFl_crea_file_per_tipologia())
			map = getMapFileCancellaIncaricoXML();
		else
			map = getMapFileNuovoIncaricoXML();

		if (map==null || map.isEmpty())
			return 0;
		int countOk=0;
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Comunicazione value = map.get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getCancellaIncarico()!=null)
				countOk = countOk + value.getConsulenti().getCancellaIncarico().size();
		}
		return countOk;
	}

	public Integer getCountInsIncarichiErrEstratti() {
		if (getMapFileAnomalieNuovoIncaricoXML()==null || getMapFileAnomalieNuovoIncaricoXML().isEmpty())
			return 0;
		int countOk=0;
		for (Iterator<String> iterator = getMapFileAnomalieNuovoIncaricoXML().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			EsitoComunicazione value = getMapFileAnomalieNuovoIncaricoXML().get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getNuovoIncarico()!=null)
				countOk = countOk + value.getConsulenti().getNuovoIncarico().size();
		}
		return countOk;
	}

	public Integer getCountUpdIncarichiErrEstratti() {
		if (getMapFileAnomalieModificaIncaricoXML()==null || getMapFileAnomalieModificaIncaricoXML().isEmpty())
			return 0;
		int countOk=0;
		for (Iterator<String> iterator = getMapFileAnomalieModificaIncaricoXML().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			EsitoComunicazione value = getMapFileAnomalieModificaIncaricoXML().get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getModificaIncarico()!=null)
				countOk = countOk + value.getConsulenti().getModificaIncarico().size();
		}
		return countOk;
	}

	public Integer getCountDelIncarichiErrEstratti() {
		if (getMapFileAnomalieCancellaIncaricoXML()==null || getMapFileAnomalieCancellaIncaricoXML().isEmpty())
			return 0;
		int countOk=0;
		for (Iterator<String> iterator = getMapFileAnomalieCancellaIncaricoXML().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			EsitoComunicazione value = getMapFileAnomalieCancellaIncaricoXML().get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getCancellaIncarico()!=null)
				countOk = countOk + value.getConsulenti().getCancellaIncarico().size();
		}
		return countOk;
	}

	public BigDecimal getTotImportoNuoviIncarichiComunicato() {
		if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getNuovoIncarico()==null ||
			getComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
			return BigDecimal.ZERO;
		BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<Comunicazione.Consulenti.NuovoIncarico> iterator = getComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
			Comunicazione.Consulenti.NuovoIncarico type = iterator.next();
			totale = totale.add(type.getImportoPrevisto());
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiOkRicevuti() {
		if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getNuovoIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
			return BigDecimal.ZERO;
		BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
			if (type.getEsito()==null || type.getEsito().equals(Esito.OK))
				totale = totale.add(type.getImportoPrevisto());
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiErrRicevuti() {
		if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getNuovoIncarico()==null ||
			getEsitoComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
			return BigDecimal.ZERO;
		BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
			EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
			if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) 
				totale = totale.add(type.getImportoPrevisto());
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiOkEstratti() {
		if (getMapFileNuovoIncaricoXML()==null || getMapFileNuovoIncaricoXML().isEmpty())
			return BigDecimal.ZERO;
		BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<String> iterator = getMapFileNuovoIncaricoXML().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Comunicazione value = getMapFileNuovoIncaricoXML().get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getNuovoIncarico()!=null) {
				for (Iterator<Comunicazione.Consulenti.NuovoIncarico> iterator2 = value.getConsulenti().getNuovoIncarico().iterator(); iterator2.hasNext();) {
					Comunicazione.Consulenti.NuovoIncarico type = iterator2.next();
					totale = totale.add(type.getImportoPrevisto());
				}
			}
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiErrEstratti() {
		if (getMapFileAnomalieNuovoIncaricoXML()==null || getMapFileAnomalieNuovoIncaricoXML().isEmpty())
			return BigDecimal.ZERO;
		BigDecimal totale = BigDecimal.ZERO;
		for (Iterator<String> iterator = getMapFileAnomalieNuovoIncaricoXML().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			EsitoComunicazione value = getMapFileAnomalieNuovoIncaricoXML().get(key);
			if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getNuovoIncarico()!=null) {
				for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator2 = value.getConsulenti().getNuovoIncarico().iterator(); iterator2.hasNext();) {
					EsitoComunicazione.Consulenti.NuovoIncarico type = iterator2.next();
					totale = totale.add(type.getImportoPrevisto());
				}
			}
		}
		return totale;
	}

	public boolean isFl_visualizza_file_xml() {
		return fl_visualizza_file_xml;
	}

	public void setFl_visualizza_file_xml(boolean flVisualizzaFileXml) {
		fl_visualizza_file_xml = flVisualizzaFileXml;
	}

	public boolean isFl_crea_file_da_file() {
		return fl_crea_file_da_file;
	}

	public void setFl_crea_file_da_file(boolean flCreaFileDaFile) {
		fl_crea_file_da_file = flCreaFileDaFile;
	}

	public java.lang.String getTipo_estrazione_pagamenti() {
		return tipo_estrazione_pagamenti;
	}

	public void setTipo_estrazione_pagamenti(java.lang.String tipoEstrazionePagamenti) {
		tipo_estrazione_pagamenti = tipoEstrazionePagamenti;
	}
}
