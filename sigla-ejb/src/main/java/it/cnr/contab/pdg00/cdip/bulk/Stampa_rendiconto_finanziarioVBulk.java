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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;

public class Stampa_rendiconto_finanziarioVBulk extends OggettoBulk {
	private Integer esercizio;
    private DipartimentoBulk dipartimento;
    private CdsBulk cds;
    private String tipo_stampa;
    private Boolean ufficiale;
    
    public static final String GESTIONALE = "GES";
    public static final String DECISIONALE = "DEC";

	public final static java.util.Dictionary rendicontoKeys;
	static{
		rendicontoKeys = new it.cnr.jada.util.OrderedHashtable();				
		rendicontoKeys.put(GESTIONALE,  "Gestionale");
		rendicontoKeys.put(DECISIONALE,  "Decisionale");
	};
    // Check per i raggruppamenti
	private Boolean ragrr_dipartimento;
	private Boolean ragrr_cds;
	private Boolean ragrr_titolo;
	private Boolean ragrr_categoria;
	private Boolean ragrr_capitolo;

	//Tipo di stampa
	private String ti_etr_spe;
	
	public final static String TIPO_ENTRATA = "E";
	public final static String TIPO_SPESA = "S";

	public final static java.util.Dictionary ti_etr_speKeys;
	
	static {		
		ti_etr_speKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_etr_speKeys.put(TIPO_ENTRATA,"Entrata");
		ti_etr_speKeys.put(TIPO_SPESA,"Spesa");
	};	
	
	public Stampa_rendiconto_finanziarioVBulk() {
		super();
	}
	public boolean isDecisionale(){
		return getTipo_stampa().equalsIgnoreCase(DECISIONALE); 
	}
	public String getCdCDSForPrint() {
		if (getCds()==null)
			return "*";
		if (getCds().getCd_unita_organizzativa()==null)
			return "*";
		return getCds().getCd_unita_organizzativa();
	}
	public String getCdDipartimentoForPrint() {
		if (getDipartimento()==null)
			return "*";
		if (getDipartimento().getCd_dipartimento()==null)
			return "*";
		return getDipartimento().getCd_dipartimento();
	}

	public CdsBulk getCds() {
		return cds;
	}

	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}

	public DipartimentoBulk getDipartimento() {
		return dipartimento;
	}

	public void setDipartimento(DipartimentoBulk dipartimento) {
		this.dipartimento = dipartimento;
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	public static java.util.Dictionary getRendicontoKeys() {
		return rendicontoKeys;
	}
	public Boolean getRagrr_capitolo() {
		return ragrr_capitolo;
	}
	public void setRagrr_capitolo(Boolean ragrr_capitolo) {
		this.ragrr_capitolo = ragrr_capitolo;
	}
	public Boolean getRagrr_categoria() {
		return ragrr_categoria;
	}
	public void setRagrr_categoria(Boolean ragrr_categoria) {
		this.ragrr_categoria = ragrr_categoria;
	}
	public Boolean getRagrr_cds() {
		return ragrr_cds;
	}
	public void setRagrr_cds(Boolean ragrr_cds) {
		this.ragrr_cds = ragrr_cds;
	}
	public Boolean getRagrr_dipartimento() {
		return ragrr_dipartimento;
	}
	public void setRagrr_dipartimento(Boolean ragrr_dipartimento) {
		this.ragrr_dipartimento = ragrr_dipartimento;
	}
	public Boolean getRagrr_titolo() {
		return ragrr_titolo;
	}
	public void setRagrr_titolo(Boolean ragrr_titolo) {
		this.ragrr_titolo = ragrr_titolo;
	}
	/**
	 * Inizializza gli attributi di ragruppamento
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk Stampa_pdg_etr_speVBulk con i falg inizializzati
	 */
	public void inizializzaRagruppamenti() {
		setRagrr_dipartimento(new Boolean(true));
		setRagrr_cds(new Boolean(true));
		setRagrr_titolo(new Boolean(true));
		setRagrr_categoria(new Boolean(true));
		setRagrr_capitolo(new Boolean(true));
	}
	public void selezionaRagruppamenti(){
		setRagrr_dipartimento(new Boolean(!getRagrr_dipartimento().booleanValue()));
		setRagrr_cds(new Boolean(!getRagrr_cds().booleanValue()));
		setRagrr_titolo(new Boolean(!getRagrr_titolo().booleanValue()));
		setRagrr_categoria(new Boolean(!getRagrr_categoria().booleanValue()));
		setRagrr_capitolo(new Boolean(!getRagrr_capitolo().booleanValue()));
	}
	public String getTi_etr_spe() {
		return ti_etr_spe;
	}
	public void setTi_etr_spe(String ti_etr_spe) {
		this.ti_etr_spe = ti_etr_spe;
	}
	public String getTipo_stampa() {
		return tipo_stampa;
	}
	public void setTipo_stampa(String tipo_stampa) {
		this.tipo_stampa = tipo_stampa;
	}
	public Boolean getUfficiale() {
		return ufficiale;
	}
	public void setUfficiale(Boolean ufficiale) {
		this.ufficiale = ufficiale;
	}
	@Override
	public void validate() throws ValidationException {
		if (!getRagrr_dipartimento() && !getRagrr_cds() && 
				!getRagrr_titolo() && !getRagrr_categoria() && !getRagrr_capitolo())
			throw new ValidationException("Selezionare almeno un ragruppamento!");
		super.validate();
	}
}
