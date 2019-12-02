/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

import javax.xml.bind.JAXBElement;

@SuppressWarnings({ "unchecked", "serial" })
public class Incarichi_archivio_xml_fpBulk extends Incarichi_archivio_xml_fpBase {
	@SuppressWarnings("rawtypes")
	public final static Dictionary ti_dataCalcoloKeys = new it.cnr.jada.util.OrderedHashtable();
	@SuppressWarnings("rawtypes")
	public final static Dictionary ti_pagamentiKeys = new it.cnr.jada.util.OrderedHashtable();
	@SuppressWarnings("rawtypes")
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
	
	private java.lang.Integer esercizio_inizio;

	private java.lang.Integer semestre_inizio;

	private java.lang.String dt_calcolo;
	
	private boolean fl_crea_file_modifiche = false;

	private boolean fl_crea_file_per_tipologia = false;

	private boolean fl_crea_file_perla = true;

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
	
	private EsitoComunicazione esitoComunicazione, esitoComunicazioneConErrori;
	
	private String pathFileZip;
	
	private Map<String,Comunicazione> mapFileNuovoIncaricoXML, mapFileModificaIncaricoXML, mapFileCancellaIncaricoXML;

	private Map<String,EsitoComunicazione> mapFileAnomalieNuovoIncaricoXML, mapFileAnomalieModificaIncaricoXML, mapFileAnomalieCancellaIncaricoXML;

	private JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> comunicazioneNuovoIncaricoPerla, esitoComunicazioneNuovoIncaricoPerla;

	private JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> comunicazioneModificaIncaricoPerla, esitoComunicazioneModificaIncaricoPerla;

	private JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> comunicazioneCancellaIncaricoPerla, esitoComunicazioneCancellaIncaricoPerla;

	private Map<String,JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>> mapFileNuovoIncaricoXMLPerla, mapFileAnomalieNuovoIncaricoXMLPerla;

	private Map<String,JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>> mapFileModificaIncaricoXMLPerla, mapFileAnomalieModificaIncaricoXMLPerla;

	private Map<String,JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>> mapFileCancellaIncaricoXMLPerla, mapFileAnomalieCancellaIncaricoXMLPerla;

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

	public java.lang.Integer getEsercizio_inizio() {
		return esercizio_inizio;
	}

	public void setEsercizio_inizio(java.lang.Integer esercizio_inizio) {
		this.esercizio_inizio = esercizio_inizio;
	}

	public java.lang.Integer getSemestre_inizio() {
		return semestre_inizio;
	}

	public void setSemestre_inizio(java.lang.Integer semestre_inizio) {
		this.semestre_inizio = semestre_inizio;
	}

	public java.lang.String getDt_calcolo() {
		return dt_calcolo;
	}

	public void setDt_calcolo(java.lang.String dtCalcolo) {
		dt_calcolo = dtCalcolo;
	}

	public boolean isFl_crea_file_modifiche() {
		return fl_crea_file_modifiche;
	}

	public void setFl_crea_file_modifiche(boolean flCreaFileModifiche) {
		fl_crea_file_modifiche = flCreaFileModifiche;
	}


	public boolean isFl_crea_file_per_tipologia() {
		return fl_crea_file_per_tipologia;
	}

	public void setFl_crea_file_per_tipologia(boolean flCreaFilePerTipologia) {
		fl_crea_file_per_tipologia = flCreaFilePerTipologia;
	}

	public boolean isFl_crea_file_perla() {
		return fl_crea_file_perla;
	}

	public void setFl_crea_file_perla(boolean fl_crea_file_perla) {
		this.fl_crea_file_perla = fl_crea_file_perla;
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
		       (getMapFileAnomalieCancellaIncaricoXML()!=null && !getMapFileAnomalieCancellaIncaricoXML().isEmpty()) ||
		       (getMapFileNuovoIncaricoXMLPerla()!=null && !getMapFileNuovoIncaricoXMLPerla().isEmpty()) ||
		       (getMapFileModificaIncaricoXMLPerla()!=null && !getMapFileModificaIncaricoXMLPerla().isEmpty()) ||
		       (getMapFileCancellaIncaricoXMLPerla()!=null && !getMapFileCancellaIncaricoXMLPerla().isEmpty()) ||
		       (getMapFileAnomalieNuovoIncaricoXMLPerla()!=null && !getMapFileAnomalieNuovoIncaricoXMLPerla().isEmpty()) ||
		       (getMapFileAnomalieModificaIncaricoXMLPerla()!=null && !getMapFileAnomalieModificaIncaricoXMLPerla().isEmpty()) ||
		       (getMapFileAnomalieCancellaIncaricoXMLPerla()!=null && !getMapFileAnomalieCancellaIncaricoXMLPerla().isEmpty());		       
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

	public JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> getComunicazioneNuovoIncaricoPerla() {
		return comunicazioneNuovoIncaricoPerla;
	}

	public void setComunicazioneNuovoIncaricoPerla(
			JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> comunicazioneNuovoIncaricoPerla) {
		this.comunicazioneNuovoIncaricoPerla = comunicazioneNuovoIncaricoPerla;
	}

	public JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> getComunicazioneModificaIncaricoPerla() {
		return comunicazioneModificaIncaricoPerla;
	}

	public void setComunicazioneModificaIncaricoPerla(
			JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> comunicazioneModificaIncaricoPerla) {
		this.comunicazioneModificaIncaricoPerla = comunicazioneModificaIncaricoPerla;
	}

	public JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> getComunicazioneCancellaIncaricoPerla() {
		return comunicazioneCancellaIncaricoPerla;
	}

	public void setComunicazioneCancellaIncaricoPerla(
			JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> comunicazioneCancellaIncaricoPerla) {
		this.comunicazioneCancellaIncaricoPerla = comunicazioneCancellaIncaricoPerla;
	}

	public JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> getEsitoComunicazioneNuovoIncaricoPerla() {
		return esitoComunicazioneNuovoIncaricoPerla;
	}

	public void setEsitoComunicazioneNuovoIncaricoPerla(
			JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> esitoComunicazioneNuovoIncaricoPerla) {
		this.esitoComunicazioneNuovoIncaricoPerla = esitoComunicazioneNuovoIncaricoPerla;
	}

	public JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> getEsitoComunicazioneModificaIncaricoPerla() {
		return esitoComunicazioneModificaIncaricoPerla;
	}

	public void setEsitoComunicazioneModificaIncaricoPerla(
			JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> esitoComunicazioneModificaIncaricoPerla) {
		this.esitoComunicazioneModificaIncaricoPerla = esitoComunicazioneModificaIncaricoPerla;
	}

	public JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> getEsitoComunicazioneCancellaIncaricoPerla() {
		return esitoComunicazioneCancellaIncaricoPerla;
	}

	public void setEsitoComunicazioneCancellaIncaricoPerla(
			JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> esitoComunicazioneCancellaIncaricoPerla) {
		this.esitoComunicazioneCancellaIncaricoPerla = esitoComunicazioneCancellaIncaricoPerla;
	}

	public Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>> getMapFileNuovoIncaricoXMLPerla() {
		return mapFileNuovoIncaricoXMLPerla;
	}

	public void setMapFileNuovoIncaricoXMLPerla(
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>> mapFileNuovoIncaricoXMLPerla) {
		this.mapFileNuovoIncaricoXMLPerla = mapFileNuovoIncaricoXMLPerla;
	}

	public Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>> getMapFileModificaIncaricoXMLPerla() {
		return mapFileModificaIncaricoXMLPerla;
	}

	public void setMapFileModificaIncaricoXMLPerla(
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>> mapFileModificaIncaricoXMLPerla) {
		this.mapFileModificaIncaricoXMLPerla = mapFileModificaIncaricoXMLPerla;
	}

	public Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>> getMapFileCancellaIncaricoXMLPerla() {
		return mapFileCancellaIncaricoXMLPerla;
	}

	public void setMapFileCancellaIncaricoXMLPerla(
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>> mapFileCancellaIncaricoXMLPerla) {
		this.mapFileCancellaIncaricoXMLPerla = mapFileCancellaIncaricoXMLPerla;
	}

	public Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>> getMapFileAnomalieNuovoIncaricoXMLPerla() {
		return mapFileAnomalieNuovoIncaricoXMLPerla;
	}

	public void setMapFileAnomalieNuovoIncaricoXMLPerla(
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>> mapFileAnomalieNuovoIncaricoXMLPerla) {
		this.mapFileAnomalieNuovoIncaricoXMLPerla = mapFileAnomalieNuovoIncaricoXMLPerla;
	}

	public Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>> getMapFileAnomalieModificaIncaricoXMLPerla() {
		return mapFileAnomalieModificaIncaricoXMLPerla;
	}

	public void setMapFileAnomalieModificaIncaricoXMLPerla(
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>> mapFileAnomalieModificaIncaricoXMLPerla) {
		this.mapFileAnomalieModificaIncaricoXMLPerla = mapFileAnomalieModificaIncaricoXMLPerla;
	}

	public Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>> getMapFileAnomalieCancellaIncaricoXMLPerla() {
		return mapFileAnomalieCancellaIncaricoXMLPerla;
	}

	public void setMapFileAnomalieCancellaIncaricoXMLPerla(
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>> mapFileAnomalieCancellaIncaricoXMLPerla) {
		this.mapFileAnomalieCancellaIncaricoXMLPerla = mapFileAnomalieCancellaIncaricoXMLPerla;
	}

	public String getPathFileZip() {
		return pathFileZip;
	}

	public void setPathFileZip(String pathFileZip) {
		this.pathFileZip = pathFileZip;
	}

	public List<String> getElencoPathFileNuovoIncaricoXML() {
		if (isFl_crea_file_perla()){
			if (getMapFileNuovoIncaricoXMLPerla()!=null)
				return new ArrayList<String>(getMapFileNuovoIncaricoXMLPerla().keySet());
		} else {
			if (getMapFileNuovoIncaricoXML()!=null)
				return new ArrayList<String>(getMapFileNuovoIncaricoXML().keySet());
		}
		return null;
	}

	public List<String> getElencoPathFileModificaIncaricoXML() {
		if (isFl_crea_file_perla()){
			if (getMapFileModificaIncaricoXMLPerla()!=null)
				return new ArrayList<String>(getMapFileModificaIncaricoXMLPerla().keySet());
		} else {
			if (getMapFileModificaIncaricoXML()!=null)
				return new ArrayList<String>(getMapFileModificaIncaricoXML().keySet());
		}
		return null;
	}

	public List<String> getElencoPathFileCancellaIncaricoXML() {
		if (isFl_crea_file_perla()){
			if (getMapFileCancellaIncaricoXMLPerla()!=null)
				return new ArrayList<String>(getMapFileCancellaIncaricoXMLPerla().keySet());
		} else {
			if (getMapFileCancellaIncaricoXML()!=null)
				return new ArrayList<String>(getMapFileCancellaIncaricoXML().keySet());
		}
		return null;
	}

	public List<String> getElencoPathFileAnomalieNuovoIncaricoXML() {
		if (isFl_crea_file_perla()){
			if (getMapFileAnomalieNuovoIncaricoXMLPerla()!=null)
				return new ArrayList<String>(getMapFileAnomalieNuovoIncaricoXMLPerla().keySet());
		} else {
			if (getMapFileAnomalieNuovoIncaricoXML()!=null)
				return new ArrayList<String>(getMapFileAnomalieNuovoIncaricoXML().keySet());
		}
		return null;
	}

	public List<String> getElencoPathFileAnomalieModificaIncaricoXML() {
		if (isFl_crea_file_perla()){
			if (getMapFileAnomalieModificaIncaricoXMLPerla()!=null)
				return new ArrayList<String>(getMapFileAnomalieModificaIncaricoXMLPerla().keySet());
		} else {
			if (getMapFileAnomalieModificaIncaricoXML()!=null)
				return new ArrayList<String>(getMapFileAnomalieModificaIncaricoXML().keySet());
		}
		return null;
	}

	public List<String> getElencoPathFileAnomalieCancellaIncaricoXML() {
		if (isFl_crea_file_perla()){
			if (getMapFileAnomalieCancellaIncaricoXMLPerla()!=null)
				return new ArrayList<String>(getMapFileAnomalieCancellaIncaricoXMLPerla().keySet());
		} else {
			if (getMapFileAnomalieCancellaIncaricoXML()!=null)
				return new ArrayList<String>(getMapFileAnomalieCancellaIncaricoXML().keySet());
		}
		return null;
	}

	public Integer getCountInsIncarichiComunicati() {
		if (isFl_crea_file_perla()){
			if (getComunicazioneNuovoIncaricoPerla()==null || getComunicazioneNuovoIncaricoPerla().getValue()==null || 
		     	getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi()==null ||
		     	getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi().getNuoviIncarichi()==null ||
		     	getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi().getNuoviIncarichi().getConsulente()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi().getNuoviIncarichi().getConsulente().isEmpty())
				return 0;
			return getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi().getNuoviIncarichi().getConsulente().size();
		} else {
			if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getNuovoIncarico()==null ||
				getComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
				return 0;
			return getComunicazione().getConsulenti().getNuovoIncarico().size();
		}
	}

	public Integer getCountUpdIncarichiComunicati() {
		if (isFl_crea_file_perla()){
			if (getComunicazioneModificaIncaricoPerla()==null || getComunicazioneModificaIncaricoPerla().getValue()==null || 
		     	getComunicazioneModificaIncaricoPerla().getValue().getVariazioneIncarichi()==null ||
		     	getComunicazioneModificaIncaricoPerla().getValue().getVariazioneIncarichi().getModificaIncarichi()==null ||
		     	getComunicazioneModificaIncaricoPerla().getValue().getVariazioneIncarichi().getModificaIncarichi().getConsulente()==null ||
				getComunicazioneModificaIncaricoPerla().getValue().getVariazioneIncarichi().getModificaIncarichi().getConsulente().isEmpty())
				return 0;
			return getComunicazioneModificaIncaricoPerla().getValue().getVariazioneIncarichi().getModificaIncarichi().getConsulente().size();
		} else {
			if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getModificaIncarico()==null ||
				getComunicazione().getConsulenti().getModificaIncarico().isEmpty())
				return 0;
			return getComunicazione().getConsulenti().getModificaIncarico().size();
		}
	}

	public Integer getCountDelIncarichiComunicati() {
		if (getFl_perla()){
			if (getComunicazioneCancellaIncaricoPerla()==null || getComunicazioneCancellaIncaricoPerla().getValue()==null || 
		     	getComunicazioneCancellaIncaricoPerla().getValue().getCancellazioneIncarichi()==null ||
		     	getComunicazioneCancellaIncaricoPerla().getValue().getCancellazioneIncarichi().getIncarichi()==null ||
		     	getComunicazioneCancellaIncaricoPerla().getValue().getCancellazioneIncarichi().getIncarichi().getConsulente()==null ||
				getComunicazioneCancellaIncaricoPerla().getValue().getCancellazioneIncarichi().getIncarichi().getConsulente().isEmpty())
				return 0;
			return getComunicazioneCancellaIncaricoPerla().getValue().getCancellazioneIncarichi().getIncarichi().getConsulente().size();
		} else {
			if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getCancellaIncarico()==null ||
				getComunicazione().getConsulenti().getCancellaIncarico().isEmpty())
				return 0;
			return getComunicazione().getConsulenti().getCancellaIncarico().size();
		}
	}
	
	public Integer getCountInsIncarichiOkRicevuti() {
		int countOk=0;
		if (getFl_perla()){
			if (getEsitoComunicazioneNuovoIncaricoPerla()==null || getEsitoComunicazioneNuovoIncaricoPerla().getValue()==null || 
		     	getEsitoComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi()==null ||
				getEsitoComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente()==null ||				 
				getEsitoComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().isEmpty())
				return 0;
			if (getEsitoComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoFile()!=null && 
				getEsitoComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.OK))
				return getEsitoComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().size();
			for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType> iterator = getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType type = iterator.next();
				if (type.getEsito()==null || type.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.OK)) countOk++;
			}
		} else {
			if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getNuovoIncarico()==null ||
				getEsitoComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
				return 0;
			if (getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK))
				return getEsitoComunicazione().getConsulenti().getNuovoIncarico().size();
			for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
				if (type.getEsito()==null || type.getEsito().equals(Esito.OK)) countOk++;
			}
		}
		return countOk;
	}

	public Integer getCountUpdIncarichiOkRicevuti() {
		int countOk=0;
		if (getFl_perla()){
			if (getEsitoComunicazioneModificaIncaricoPerla()==null || getEsitoComunicazioneModificaIncaricoPerla().getValue()==null || 
				getEsitoComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi()==null ||
				getEsitoComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente()==null ||				 
				getEsitoComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().isEmpty())
				return 0;
			if (getEsitoComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoFile()!=null && 
				getEsitoComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.OK))
				return getEsitoComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().size();
			for (Iterator<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType> iterator = getEsitoComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().iterator(); iterator.hasNext();) {
				it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType type = iterator.next();
				if (type.getEsito()==null || type.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.OK)) countOk++;
			}
		} else {
			if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getModificaIncarico()==null ||
				getEsitoComunicazione().getConsulenti().getModificaIncarico().isEmpty())
				return 0;
			if (getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK))
				return getEsitoComunicazione().getConsulenti().getModificaIncarico().size();
			for (Iterator<EsitoComunicazione.Consulenti.ModificaIncarico> iterator = getEsitoComunicazione().getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.ModificaIncarico type = iterator.next();
				if (type.getEsito()==null || type.getEsito().equals(Esito.OK)) countOk++;
			}
		}
		return countOk;
	}

	public Integer getCountDelIncarichiOkRicevuti() {
		int countOk=0;
		if (getFl_perla()){
			if (getComunicazioneCancellaIncaricoPerla()==null || getComunicazioneCancellaIncaricoPerla().getValue()==null || 
		     	getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi()==null ||
				getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente()==null ||				 
				getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente().isEmpty())
				return 0;
			if (getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoFile()!=null && 
				getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoType.OK))
				return getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente().size();
			for (Iterator<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoIncaricoType> iterator = getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente().iterator(); iterator.hasNext();) {
				it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoIncaricoType type = iterator.next();
				if (type.getEsito()==null || type.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoType.OK)) countOk++;
			}
		} else {
			if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getCancellaIncarico()==null ||
				getEsitoComunicazione().getConsulenti().getCancellaIncarico().isEmpty())
				return 0;
			if (getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK))
				return getEsitoComunicazione().getConsulenti().getCancellaIncarico().size();
			for (Iterator<EsitoComunicazione.Consulenti.CancellaIncarico> iterator = getEsitoComunicazione().getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.CancellaIncarico type = iterator.next();
				if (type.getEsito()==null || type.getEsito().equals(Esito.OK)) countOk++;
			}
		}
		return countOk;
	}

	public Integer getCountInsIncarichiErrRicevuti() {
		int countErr=0;
		if (getFl_perla()){
			if (getComunicazioneNuovoIncaricoPerla()==null || getComunicazioneNuovoIncaricoPerla().getValue()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi()==null ||
				(getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoFile()!=null && 
				 getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.OK)) ||
		     	getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente()==null ||				 
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().isEmpty())
					return 0;
				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType> iterator = getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
					it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType type = iterator.next();
					if (type.getEsito()!=null && type.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoType.KO)) countErr++;
				}
		} else {
			if (getEsitoComunicazione()==null || 
				(getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK)) ||
				getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getNuovoIncarico()==null ||
				getEsitoComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
				return 0;
			for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
				if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) countErr++;
			}
		}
		return countErr;
	}

	public Integer getCountUpdIncarichiErrRicevuti() {
		int countErr=0;
		if (getFl_perla()){
			if (getComunicazioneModificaIncaricoPerla()==null || getComunicazioneModificaIncaricoPerla().getValue()==null ||
				getComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi()==null ||
				(getComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoFile()!=null && 
				 getComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.OK)) ||
		     	getComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi()==null ||
				getComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente()==null ||				 
				getComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().isEmpty())
					return 0;
				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType> iterator = getComunicazioneModificaIncaricoPerla().getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().iterator(); iterator.hasNext();) {
					it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoConsulenteType type = iterator.next();
					if (type.getEsito()!=null && type.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.EsitoType.KO)) countErr++;
				}
		} else {
			if (getEsitoComunicazione()==null || 
				(getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK)) ||
				getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getModificaIncarico()==null ||
				getEsitoComunicazione().getConsulenti().getModificaIncarico().isEmpty())
				return 0;
			for (Iterator<EsitoComunicazione.Consulenti.ModificaIncarico> iterator = getEsitoComunicazione().getConsulenti().getModificaIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.ModificaIncarico type = iterator.next();
				if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) countErr++;
			}
		}
		return countErr;
	}

	public Integer getCountDelIncarichiErrRicevuti() {
		int countErr=0;
		if (getFl_perla()){
			if (getComunicazioneCancellaIncaricoPerla()==null || getComunicazioneCancellaIncaricoPerla().getValue()==null ||
				getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi()==null ||
				(getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoFile()!=null && 
				 getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoFile().equals(it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoType.OK)) ||
		     	getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi()==null ||
				getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente()==null ||				 
				getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente().isEmpty())
					return 0;
				for (Iterator<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoIncaricoType> iterator = getComunicazioneCancellaIncaricoPerla().getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente().iterator(); iterator.hasNext();) {
					it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoIncaricoType type = iterator.next();
					if (type.getEsito()!=null && type.getEsito().equals(it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.EsitoType.KO)) countErr++;
				}
		} else {
			if (getEsitoComunicazione()==null || 
				(getEsitoComunicazione().getEsito()!=null && getEsitoComunicazione().getEsito().equals(Esito.OK)) ||
				getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getCancellaIncarico()==null ||
				getEsitoComunicazione().getConsulenti().getCancellaIncarico().isEmpty())
				return 0;
			for (Iterator<EsitoComunicazione.Consulenti.CancellaIncarico> iterator = getEsitoComunicazione().getConsulenti().getCancellaIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.CancellaIncarico type = iterator.next();
				if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) countErr++;
			}
		}			
		return countErr;
	}

	public Integer getCountInsIncarichiOkEstratti() {
		int countOk=0;
		if (isFl_crea_file_perla()){
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>> mapPerla = getMapFileNuovoIncaricoXMLPerla();
			if (mapPerla==null || mapPerla.isEmpty())
				return 0;
			for (Iterator<String> iterator = mapPerla.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> value = mapPerla.get(key);
				if (value!=null && value.getValue()!=null && value.getValue().getInserimentoIncarichi()!=null &&
					value.getValue().getInserimentoIncarichi().getNuoviIncarichi()!=null && 
					value.getValue().getInserimentoIncarichi().getNuoviIncarichi().getConsulente()!=null)
					countOk = countOk + value.getValue().getInserimentoIncarichi().getNuoviIncarichi().getConsulente().size();
			}
		} else {
			if (getMapFileNuovoIncaricoXML()==null || getMapFileNuovoIncaricoXML().isEmpty())
				return 0;
			for (Iterator<String> iterator = getMapFileNuovoIncaricoXML().keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Comunicazione value = getMapFileNuovoIncaricoXML().get(key);
				if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getNuovoIncarico()!=null)
					countOk = countOk + value.getConsulenti().getNuovoIncarico().size();
			}
		}
		return countOk;
	}

	public Integer getCountUpdIncarichiOkEstratti() {
		int countOk=0;
		if (isFl_crea_file_perla()){
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>> mapPerla = getMapFileModificaIncaricoXMLPerla();
			if (mapPerla==null || mapPerla.isEmpty())
				return 0;
			for (Iterator<String> iterator = mapPerla.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> value = mapPerla.get(key);
				if (value!=null && value.getValue()!=null && value.getValue().getVariazioneIncarichi()!=null &&
					value.getValue().getVariazioneIncarichi().getModificaIncarichi()!=null && 
					value.getValue().getVariazioneIncarichi().getModificaIncarichi().getConsulente()!=null)
					countOk = countOk + value.getValue().getVariazioneIncarichi().getModificaIncarichi().getConsulente().size();
			}
		} else {
			Map<String, Comunicazione> map = null;
			if (isFl_crea_file_per_tipologia())
				map = getMapFileModificaIncaricoXML();
			else
				map = getMapFileNuovoIncaricoXML();
	
			if (map==null || map.isEmpty())
				return 0;
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Comunicazione value = map.get(key);
				if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getModificaIncarico()!=null)
					countOk = countOk + value.getConsulenti().getModificaIncarico().size();
			}
		}
		return countOk;
	}

	public Integer getCountDelIncarichiOkEstratti() {
		int countOk=0;
		if (isFl_crea_file_perla()){
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>> mapPerla = getMapFileCancellaIncaricoXMLPerla();
			if (mapPerla==null || mapPerla.isEmpty())
				return 0;
			for (Iterator<String> iterator = mapPerla.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> value = mapPerla.get(key);
				if (value!=null && value.getValue()!=null && value.getValue().getCancellazioneIncarichi()!=null &&
					value.getValue().getCancellazioneIncarichi().getIncarichi()!=null && value.getValue().getCancellazioneIncarichi().getIncarichi().getConsulente()!=null)
					countOk = countOk + value.getValue().getCancellazioneIncarichi().getIncarichi().getConsulente().size();
			}
		} else {
			Map<String, Comunicazione> map = null;
			if (isFl_crea_file_per_tipologia())
				map = getMapFileCancellaIncaricoXML();
			else
				map = getMapFileNuovoIncaricoXML();
	
			if (map==null || map.isEmpty())
				return 0;
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Comunicazione value = map.get(key);
				if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getCancellaIncarico()!=null)
					countOk = countOk + value.getConsulenti().getCancellaIncarico().size();
			}
		}
		return countOk;
	}

	public Integer getCountInsIncarichiErrEstratti() {
		int countOk=0;
		if (getFl_perla()){
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType>> mapPerla = getMapFileAnomalieNuovoIncaricoXMLPerla();
			if (mapPerla==null || mapPerla.isEmpty())
				return 0;
			for (Iterator<String> iterator = mapPerla.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType> value = mapPerla.get(key);
				if (value!=null && value.getValue()!=null && value.getValue().getEsitoInserimentoIncarichi()!=null &&
					value.getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi()!=null && value.getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente()!=null)
					countOk = countOk + value.getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().size();
			}
		} else {
			if (getMapFileAnomalieNuovoIncaricoXML()==null || getMapFileAnomalieNuovoIncaricoXML().isEmpty())
				return 0;
			for (Iterator<String> iterator = getMapFileAnomalieNuovoIncaricoXML().keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				EsitoComunicazione value = getMapFileAnomalieNuovoIncaricoXML().get(key);
				if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getNuovoIncarico()!=null)
					countOk = countOk + value.getConsulenti().getNuovoIncarico().size();
			}
		}
		return countOk;
	}

	public Integer getCountUpdIncarichiErrEstratti() {
		int countOk=0;
		if (getFl_perla()){
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType>> mapPerla = getMapFileAnomalieModificaIncaricoXMLPerla();
			if (mapPerla==null || mapPerla.isEmpty())
				return 0;
			for (Iterator<String> iterator = mapPerla.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_variazioneincarichi.ComunicazioneType> value = mapPerla.get(key);
				if (value!=null && value.getValue()!=null && value.getValue().getEsitoVariazioneIncarichi()!=null &&
					value.getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi()!=null && 
					value.getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente()!=null)
					countOk = countOk + value.getValue().getEsitoVariazioneIncarichi().getEsitoModificaIncarichi().getConsulente().size();
			}
		} else {
			if (getMapFileAnomalieModificaIncaricoXML()==null || getMapFileAnomalieModificaIncaricoXML().isEmpty())
				return 0;
			for (Iterator<String> iterator = getMapFileAnomalieModificaIncaricoXML().keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				EsitoComunicazione value = getMapFileAnomalieModificaIncaricoXML().get(key);
				if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getModificaIncarico()!=null)
					countOk = countOk + value.getConsulenti().getModificaIncarico().size();
			}
		}
		return countOk;
	}

	public Integer getCountDelIncarichiErrEstratti() {
		int countOk=0;
		if (getFl_perla()){
			Map<String, JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType>> mapPerla = getMapFileAnomalieCancellaIncaricoXMLPerla();
			if (mapPerla==null || mapPerla.isEmpty())
				return 0;
			for (Iterator<String> iterator = mapPerla.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				JAXBElement<it.perla.accenture.com.anagrafeprestazioni_cancellazioneincarichi.ComunicazioneType> value = mapPerla.get(key);
				if (value!=null && value.getValue()!=null && value.getValue().getEsitoCancellazioneIncarichi()!=null &&
					value.getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi()!=null && 
					value.getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente()!=null)
					countOk = countOk + value.getValue().getEsitoCancellazioneIncarichi().getEsitoIncarichi().getConsulente().size();
			}
		} else {
			if (getMapFileAnomalieCancellaIncaricoXML()==null || getMapFileAnomalieCancellaIncaricoXML().isEmpty())
				return 0;
			for (Iterator<String> iterator = getMapFileAnomalieCancellaIncaricoXML().keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				EsitoComunicazione value = getMapFileAnomalieCancellaIncaricoXML().get(key);
				if (value!=null && value.getConsulenti()!=null && value.getConsulenti().getCancellaIncarico()!=null)
					countOk = countOk + value.getConsulenti().getCancellaIncarico().size();
			}
		}
		return countOk;
	}

	public BigDecimal getTotImportoNuoviIncarichiComunicato() {
		BigDecimal totale = BigDecimal.ZERO;
		if (getFl_perla()){
			if (getComunicazioneNuovoIncaricoPerla()==null || getComunicazioneNuovoIncaricoPerla().getValue()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi().getNuoviIncarichi()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi().getNuoviIncarichi().getConsulente().isEmpty())
				return BigDecimal.ZERO;
			for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType> iterator = getComunicazioneNuovoIncaricoPerla().getValue().getInserimentoIncarichi().getNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType type = iterator.next();
				if (type.getIncarico()!=null && type.getIncarico().getImporto()!=null)
					totale = totale.add(type.getIncarico().getImporto());
			}
		} else {
			if (getComunicazione()==null || getComunicazione().getConsulenti()==null || getComunicazione().getConsulenti().getNuovoIncarico()==null ||
				getComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
				return BigDecimal.ZERO;
			for (Iterator<Comunicazione.Consulenti.NuovoIncarico> iterator = getComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
				Comunicazione.Consulenti.NuovoIncarico type = iterator.next();
				totale = totale.add(type.getImportoPrevisto());
			}
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiOkRicevuti() {
		BigDecimal totale = BigDecimal.ZERO;
		if (getFl_perla()){
			if (getComunicazioneNuovoIncaricoPerla()==null || getComunicazioneNuovoIncaricoPerla().getValue()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().isEmpty())
				return BigDecimal.ZERO;
			for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType> iterator = getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType type = iterator.next();
				totale = totale.add(BigDecimal.ZERO);
			}
		} else {
			for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
				if (type.getEsito()==null || type.getEsito().equals(Esito.OK))
					totale = totale.add(type.getImportoPrevisto());
			}
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiErrRicevuti() {
		BigDecimal totale = BigDecimal.ZERO;
		if (getFl_perla()){
			if (getComunicazioneNuovoIncaricoPerla()==null || getComunicazioneNuovoIncaricoPerla().getValue()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi()==null ||
				getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().isEmpty())
				return BigDecimal.ZERO;
			for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType> iterator = getComunicazioneNuovoIncaricoPerla().getValue().getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().iterator(); iterator.hasNext();) {
				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType type = iterator.next();
				totale = totale.add(BigDecimal.ZERO);
			}
		} else {
			if (getEsitoComunicazione()==null || getEsitoComunicazione().getConsulenti()==null || getEsitoComunicazione().getConsulenti().getNuovoIncarico()==null ||
				getEsitoComunicazione().getConsulenti().getNuovoIncarico().isEmpty())
				return BigDecimal.ZERO;
			for (Iterator<EsitoComunicazione.Consulenti.NuovoIncarico> iterator = getEsitoComunicazione().getConsulenti().getNuovoIncarico().iterator(); iterator.hasNext();) {
				EsitoComunicazione.Consulenti.NuovoIncarico type = iterator.next();
				if (type.getEsito()!=null && type.getEsito().equals(Esito.ERRATO)) 
					totale = totale.add(type.getImportoPrevisto());
			}
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiOkEstratti() {
		BigDecimal totale = BigDecimal.ZERO;
		if (isFl_crea_file_perla()){
			if (getMapFileNuovoIncaricoXMLPerla()==null || getMapFileNuovoIncaricoXMLPerla().isEmpty())
				return BigDecimal.ZERO;
			for (Iterator<String> iterator = getMapFileNuovoIncaricoXMLPerla().keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType value = getMapFileNuovoIncaricoXMLPerla().get(key).getValue();
				if (value!=null && value.getInserimentoIncarichi()!=null && value.getInserimentoIncarichi().getNuoviIncarichi()!=null && 
					value.getInserimentoIncarichi().getNuoviIncarichi().getConsulente()!=null) {
					for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType> iterator2 = value.getInserimentoIncarichi().getNuoviIncarichi().getConsulente().iterator(); iterator2.hasNext();) {
						it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ConsulenteType type = iterator2.next();
						if (type.getIncarico()!=null)
							totale = totale.add(type.getIncarico().getImporto());
					}
				}
			}
		} else {
			if (getMapFileNuovoIncaricoXML()==null || getMapFileNuovoIncaricoXML().isEmpty())
				return BigDecimal.ZERO;
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
		}
		return totale;
	}

	public BigDecimal getTotImportoNuoviIncarichiErrEstratti() {
		BigDecimal totale = BigDecimal.ZERO;
		if (getFl_perla()){
			if (getMapFileAnomalieNuovoIncaricoXMLPerla()==null || getMapFileAnomalieNuovoIncaricoXMLPerla().isEmpty())
				return BigDecimal.ZERO;
			for (Iterator<String> iterator = getMapFileAnomalieNuovoIncaricoXMLPerla().keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.ComunicazioneType value = getMapFileAnomalieNuovoIncaricoXMLPerla().get(key).getValue();
				if (value!=null && value.getEsitoInserimentoIncarichi()!=null && value.getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi()!=null && 
					value.getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente()!=null) {
					for (Iterator<it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType> iterator2 = value.getEsitoInserimentoIncarichi().getEsitoNuoviIncarichi().getConsulente().iterator(); iterator2.hasNext();) {
						it.perla.accenture.com.anagrafeprestazioni_inserimentoincarichi.EsitoConsulenteType type = iterator2.next();
						totale = totale.add(BigDecimal.ZERO);
					}
				}
			}
		} else {
			if (getMapFileAnomalieNuovoIncaricoXML()==null || getMapFileAnomalieNuovoIncaricoXML().isEmpty())
				return BigDecimal.ZERO;
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
