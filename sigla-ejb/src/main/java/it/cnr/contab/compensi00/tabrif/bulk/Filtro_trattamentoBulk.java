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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.util.enumeration.TipoIVA;

/**
 * Insert the type's description here.
 * Creation date: (24/09/2002 10.08.08)
 * @author: Roberto Fantino
 */
public class Filtro_trattamentoBulk extends it.cnr.jada.bulk.OggettoBulk {
	private java.lang.String tipoAnagrafico;
	private java.sql.Timestamp dataValidita;
	private java.lang.Boolean flDefaultCongualio;
	private java.lang.Boolean flSenzaCalcoli;
	private java.lang.Boolean flDiaria;
	
	private java.lang.String cdTipoTrattamento;
	private java.lang.String cdTipoRapporto;
	private java.lang.String entePrev;
	private java.lang.String tiIstituzionaleCommerciale;
	private java.lang.Boolean flTassazioneSeparata;
	private java.lang.Boolean flAgevolazioniCervelli;
	private java.lang.Boolean flAnnoPrec;
	private java.lang.Boolean flIncarico;
    private java.lang.Boolean flBonus;   
    private java.lang.String tipoRappImpiego;
    private java.lang.Boolean flVisibileATutti;
    private java.lang.Boolean flTipoPrestazioneObbl;
	private java.lang.Boolean flAgevolazioniRientroLav;    
	private java.lang.Boolean flSoloInailEnte;
	private java.lang.Boolean flSplitPayment;


/**
 * Filtro_trattamentiBulk constructor comment.
 */
public Filtro_trattamentoBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.35.01)
 * @return java.lang.String
 */
public java.lang.String getCdTipoRapporto() {
	return cdTipoRapporto;
}

/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.34.47)
 * @return java.lang.String
 */
public java.lang.String getCdTipoTrattamento() {
	return cdTipoTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.18.14)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataValidita() {
	return dataValidita;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.18.50)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFlDefaultCongualio() {
	return flDefaultCongualio;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.29.29)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFlDiaria() {
	return flDiaria;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.29.29)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFlSenzaCalcoli() {
	return flSenzaCalcoli;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 11.24.21)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFlTassazioneSeparata() {
	return flTassazioneSeparata;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.52.56)
 * @return String
 */
public String getTiCommerciale() {

	if (TipoIVA.ISTITUZIONALE.value().equals(getTiIstituzionaleCommerciale()))
		return Tipo_trattamentoBulk.ATT_NON_COMMERCIALE;

	if (TipoIVA.COMMERCIALE.value().equals(getTiIstituzionaleCommerciale()))
		return Tipo_trattamentoBulk.ATT_COMMERCIALE;

	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.56.19)
 * @return java.lang.String
 */
public java.lang.String getTiIstituzionaleCommerciale() {
	return tiIstituzionaleCommerciale;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.17.43)
 * @return java.lang.String
 */
public java.lang.String getTipoAnagrafico() {
	return tipoAnagrafico;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.35.01)
 * @param newCdTipoRapporto java.lang.String
 */
public void setCdTipoRapporto(java.lang.String newCdTipoRapporto) {
	cdTipoRapporto = newCdTipoRapporto;
}

/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.34.47)
 * @param newCdTipoTrattamento java.lang.String
 */
public void setCdTipoTrattamento(java.lang.String newCdTipoTrattamento) {
	cdTipoTrattamento = newCdTipoTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.18.14)
 * @param newDataValidita java.sql.Timestamp
 */
public void setDataValidita(java.sql.Timestamp newDataValidita) {
	dataValidita = newDataValidita;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.18.50)
 * @param newFlDefaultCongualio java.lang.Boolean
 */
public void setFlDefaultCongualio(java.lang.Boolean newFlDefaultCongualio) {
	flDefaultCongualio = newFlDefaultCongualio;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.29.29)
 * @param newFlDiaria java.lang.Boolean
 */
public void setFlDiaria(java.lang.Boolean newFlDiaria) {
	flDiaria = newFlDiaria;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.29.29)
 * @param newFlSenzaCalcoli java.lang.Boolean
 */
public void setFlSenzaCalcoli(java.lang.Boolean newFlSenzaCalcoli) {
	flSenzaCalcoli = newFlSenzaCalcoli;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 11.24.21)
 * @param newFlTassazioneSeparata java.lang.Boolean
 */
public void setFlTassazioneSeparata(java.lang.Boolean newFlTassazioneSeparata) {
	flTassazioneSeparata = newFlTassazioneSeparata;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.56.19)
 * @param newTiIstituzionaleCommerciale java.lang.String
 */
public void setTiIstituzionaleCommerciale(java.lang.String newTiIstituzionaleCommerciale) {
	tiIstituzionaleCommerciale = newTiIstituzionaleCommerciale;
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 10.17.43)
 * @param newTipoAnagrafico java.lang.String
 */
public void setTipoAnagrafico(java.lang.String newTipoAnagrafico) {
	tipoAnagrafico = newTipoAnagrafico;
}
	/**
	 * @return
	 */
	public java.lang.String getEntePrev() {
		return entePrev;
	}

	/**
	 * @param string
	 */
	public void setEntePrev(java.lang.String string) {
		entePrev = string;
	}
	public java.lang.Boolean getFlAgevolazioniCervelli() {
		return flAgevolazioniCervelli;
	}
	public void setFlAgevolazioniCervelli(java.lang.Boolean flAgevolazioniCervelli) {
		this.flAgevolazioniCervelli = flAgevolazioniCervelli;
	}
	public java.lang.Boolean getFlAnnoPrec() {
		return flAnnoPrec;
	}
	public void setFlAnnoPrec(java.lang.Boolean flAnnoPrec) {
		this.flAnnoPrec = flAnnoPrec;
	}
	public java.lang.Boolean getFlIncarico() {
		return flIncarico;
	}
	public void setFlIncarico(java.lang.Boolean flIncarico) {
		this.flIncarico = flIncarico;
	}
	public java.lang.Boolean getFlBonus() {
		return flBonus;	
	}	
	public void setFlBonus(java.lang.Boolean flBonus) {	
		this.flBonus = flBonus;	
	}
	public java.lang.String getTipoRappImpiego() {		
		   return tipoRappImpiego;	
    }	
	   
	public void setTipoRappImpiego(java.lang.String tipoRappImp) {		
		   this.tipoRappImpiego=tipoRappImp;	
	}
	public java.lang.Boolean getFlVisibileATutti() {
		return flVisibileATutti;
	}
	public void setFlVisibileATutti(java.lang.Boolean flVisibileATutti) {
		this.flVisibileATutti = flVisibileATutti;
	}
	public java.lang.Boolean getFlTipoPrestazioneObbl() {
		return flTipoPrestazioneObbl;
	}
	public void setFlTipoPrestazioneObbl(java.lang.Boolean flTipoPrestazioneObbl) {
		this.flTipoPrestazioneObbl = flTipoPrestazioneObbl;
	}
	public java.lang.Boolean getFlAgevolazioniRientroLav() {
		return flAgevolazioniRientroLav;
	}
	public void setFlAgevolazioniRientroLav(java.lang.Boolean flAgevolazioniRientroLav) {
		this.flAgevolazioniRientroLav = flAgevolazioniRientroLav;
	}
	public java.lang.Boolean getFlSoloInailEnte() {
		return flSoloInailEnte;
	}
	public void setFlSoloInailEnte(java.lang.Boolean flSoloInailEnte) {
		this.flSoloInailEnte = flSoloInailEnte;
	}
	public java.lang.Boolean getFlSplitPayment() {
		return flSplitPayment;
	}
	public void setFlSplitPayment(java.lang.Boolean flSplitPayment) {
		this.flSplitPayment = flSplitPayment;
	}	
}