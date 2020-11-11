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

package it.cnr.contab.progettiric00.consultazioni.bulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.math.BigDecimal;
import java.util.Collections;

public class ConsProgettiEcoVociGaeBulk extends OggettoBulk implements Persistent {
	public final static java.util.Dictionary <String,String> TIPOLOGIA_STAMPA_KEYS;
	private java.lang.String tipoStampa;
	public final static String SINTETICA = "SINTETICA";
	public final static String DETTAGLIATA = "DETTAGLIATA";

	public String getTipoStampa() {
		return tipoStampa;
	}

	public void setTipoStampa(String tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	static {
		TIPOLOGIA_STAMPA_KEYS = new it.cnr.jada.util.OrderedHashtable();
		TIPOLOGIA_STAMPA_KEYS.put(SINTETICA,"Sintetica");
		TIPOLOGIA_STAMPA_KEYS.put(DETTAGLIATA,"Dettagliata per Voce e GAE");
	}


	public ConsProgettiEcoVociGaeBulk() {
		super();
	}

	public static java.util.Dictionary getTipologiaStampaKeys() {
		return TIPOLOGIA_STAMPA_KEYS;
	}

	public String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	public void setCd_linea_attivita(String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}

	public String getCd_voce_piano() {
		return cd_voce_piano;
	}

	public void setCd_voce_piano(String cd_voce_piano) {
		this.cd_voce_piano = cd_voce_piano;
	}

	public String getCd_elemento_voce() {
		return cd_elemento_voce;
	}

	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}

	public String getDs_elemento_voce() {
		return ds_elemento_voce;
	}

	public void setDs_elemento_voce(String ds_elemento_voce) {
		this.ds_elemento_voce = ds_elemento_voce;
	}

	public BigDecimal getIm_spesa_finanziato() {
		return im_spesa_finanziato;
	}

	public void setIm_spesa_finanziato(BigDecimal im_spesa_finanziato) {
		this.im_spesa_finanziato = im_spesa_finanziato;
	}

	public BigDecimal getIm_spesa_cofinanziato() {
		return im_spesa_cofinanziato;
	}

	public void setIm_spesa_cofinanziato(BigDecimal im_spesa_cofinanziato) {
		this.im_spesa_cofinanziato = im_spesa_cofinanziato;
	}

	public BigDecimal getManris_fin() {
		return manris_fin;
	}

	public void setManris_fin(BigDecimal manris_fin) {
		this.manris_fin = manris_fin;
	}

	public BigDecimal getManris_cofin() {
		return manris_cofin;
	}

	public void setManris_cofin(BigDecimal manris_cofin) {
		this.manris_cofin = manris_cofin;
	}

	//	ESERCIZIO DECIMAL(4,0)
	private Integer esercizio_piano;

	private String  cd_centro_responsabilita;

	protected it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk findProgettoForPrint;

	private String  ds_cdr;

	private Integer pg_progetto;

	private String  cd_progetto;

	private String  cd_linea_attivita;

	private String  cd_voce_piano;

	private String  cd_elemento_voce;

	private String  ds_elemento_voce;

	private String  ds_progetto;

	private java.math.BigDecimal im_spesa_finanziato;
		
	private java.math.BigDecimal im_spesa_cofinanziato;

	private java.math.BigDecimal assestato_fin;

	private java.math.BigDecimal assestato_cofin;

	private java.math.BigDecimal impacc_fin;
	
	private java.math.BigDecimal impacc_cofin;

	private java.math.BigDecimal manris_fin;

	private java.math.BigDecimal manris_cofin;

	public String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}

	public Integer getEsercizio_piano() {
		return esercizio_piano;
	}

	public void setEsercizio_piano(Integer esercizio_piano) {
		this.esercizio_piano = esercizio_piano;
	}

	public String getDs_cdr() {
		return ds_cdr;
	}

	public void setDs_cdr(String ds_cdr) {
		this.ds_cdr = ds_cdr;
	}

	public Integer getPg_progetto() {
		return pg_progetto;
	}

	public void setPg_progetto(Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}

	public String getCd_progetto() {
		return cd_progetto;
	}

	public void setCd_progetto(String cd_progetto) {
		this.cd_progetto = cd_progetto;
	}

	public String getDs_progetto() {
		return ds_progetto;
	}

	public void setDs_progetto(String ds_progetto) {
		this.ds_progetto = ds_progetto;
	}

	public BigDecimal getAssestato_fin() {
		return assestato_fin;
	}

	public void setAssestato_fin(BigDecimal assestato_fin) {
		this.assestato_fin = assestato_fin;
	}

	public BigDecimal getAssestato_cofin() {
		return assestato_cofin;
	}

	public void setAssestato_cofin(BigDecimal assestato_cofin) {
		this.assestato_cofin = assestato_cofin;
	}

	public BigDecimal getImpacc_fin() {
		return impacc_fin;
	}

	public void setImpacc_fin(BigDecimal impacc_fin) {
		this.impacc_fin = impacc_fin;
	}

	public BigDecimal getImpacc_cofin() {
		return impacc_cofin;
	}

	public void setImpacc_cofin(BigDecimal impacc_cofin) {
		this.impacc_cofin = impacc_cofin;
	}

	public Progetto_sipBulk getFindProgettoForPrint() {
		return findProgettoForPrint;
	}

	public void setFindProgettoForPrint(Progetto_sipBulk findProgetto) {
		this.findProgettoForPrint = findProgetto;
		if (getFindProgettoForPrint() != null){
			setPg_progetto(getFindProgettoForPrint().getPg_progetto());
		}
	}

}