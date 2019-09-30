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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaNuovoBP;
import it.cnr.contab.progettiric00.tabrif.bulk.*;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.List;
import java.util.Vector;

public class ProgettoGestBulk extends ProgettoGestBase {

	public static final String TIPO_STATO_PROPOSTA  ="P";
	public static final String TIPO_STATO_APPROVATO ="A";

	public static final String TIPO_FASE_PREVISIONE  ="P";
	public static final String TIPO_FASE_GESTIONE ="G";
	public static final String TIPO_FASE_NON_DEFINITA ="X";
	
	public static final String DURATA_PROGETTO_PLURIENNALE ="P";
	public static final String DURATA_PROGETTO_ANNUALE 	="A";

	public static final Integer LIVELLO_PROGETTO_PRIMO 	= new Integer(1);
	public static final Integer LIVELLO_PROGETTO_SECONDO  = new Integer(2);
	public static final Integer LIVELLO_PROGETTO_TERZO 	= new Integer(3);
	public static final Integer LIVELLO_PROGETTO_ALL 	= new Integer(4);

	private it.cnr.jada.bulk.BulkList	workpackage_collegati = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList	workpackage_disponibili  = new it.cnr.jada.bulk.BulkList();

		public final static Dictionary statoKeys;
		static {
		statoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoKeys.put(TIPO_STATO_PROPOSTA,"Proposta");
		statoKeys.put(TIPO_STATO_APPROVATO,"Approvato");
	};
	public final static Dictionary tipo_faseKeys;
	static {
		tipo_faseKeys = new it.cnr.jada.util.OrderedHashtable();
		tipo_faseKeys.put(TIPO_FASE_PREVISIONE,"Previsione");
		tipo_faseKeys.put(TIPO_FASE_GESTIONE,"Gestione");
	};

	public final static Dictionary tipo_faseAllKeys;
	static {
		tipo_faseAllKeys = new it.cnr.jada.util.OrderedHashtable();
		tipo_faseAllKeys.put(TIPO_FASE_PREVISIONE,"Previsione");
		tipo_faseAllKeys.put(TIPO_FASE_GESTIONE,"Gestione");
		tipo_faseAllKeys.put(TIPO_FASE_NON_DEFINITA,"Non definita");
	};
	
		public final static Dictionary durata_progettoKeys;
		static {
		durata_progettoKeys = new it.cnr.jada.util.OrderedHashtable();
		durata_progettoKeys.put(DURATA_PROGETTO_PLURIENNALE,"Pluriennale");
		durata_progettoKeys.put(DURATA_PROGETTO_ANNUALE,"Annuale");
	};
		public final static Dictionary livello_progettoKeys;
		static {
		livello_progettoKeys = new it.cnr.jada.util.OrderedHashtable();
		livello_progettoKeys.put(LIVELLO_PROGETTO_PRIMO,"Progetto");
		livello_progettoKeys.put(LIVELLO_PROGETTO_SECONDO,"Commessa");
		livello_progettoKeys.put(LIVELLO_PROGETTO_TERZO,"Modulo di Attività");
		livello_progettoKeys.put(LIVELLO_PROGETTO_ALL,"Sottogruppo");
		for(int i=4;i<100;i++)
		  livello_progettoKeys.put(new Integer(i),"Sottogruppo");
	};
	private Tipo_progettoBulk tipo;
	private Unita_organizzativaBulk unita_organizzativa;
	private TerzoBulk responsabile;
	private DivisaBulk divisa;
	protected ProgettoGestBulk progettopadre;
	private DipartimentoBulk dipartimento;
	protected java.util.Collection progetti_figli;
	private BulkList dettagli = new BulkList();
	private BulkList dettagliFinanziatori = new BulkList();
	private BulkList dettagliPartner_esterni = new BulkList();
	private BulkList speseEsercizio = new BulkList();
	private Commessa_spesaBulk spese;
	private Parametri_cdsBulk parametriCds;
	
public ProgettoGestBulk() {
	super();
}
public ProgettoGestBulk(java.lang.Integer esercizio,java.lang.Integer pg_progetto,java.lang.String tipo_fase) {
	super(esercizio,pg_progetto,tipo_fase);
}
   /**
	* Aggiunge il progetto figlio alla collezione progetti_figli
	*
	* @param progetto figlio da aggiungere
	*/
	public void addToProgetti_figli(ProgettoGestBulk figlio) {
	  if (progetti_figli == null)
		progetti_figli = new java.util.LinkedList();
	  progetti_figli.add(figlio);
	}
	public int addToDettagli(Progetto_uoBulk dett) {
		dett.setPg_progetto( getPg_progetto() );
		dettagli.add(dett);
		return dettagli.size()-1;
	}

	public int addToDettagliFinanziatori(Progetto_finanziatoreBulk dett) {
		dett.setPg_progetto( getPg_progetto() );
		dettagliFinanziatori.add(dett);
		return dettagliFinanziatori.size()-1;
	}

	public int addToDettagliPartner_esterni(Progetto_partner_esternoBulk dett) {
		dett.setPg_progetto( getPg_progetto() );
		dettagliPartner_esterni.add(dett);
		return dettagliPartner_esterni.size()-1;
	}

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {dettagli,dettagliFinanziatori,dettagliPartner_esterni};
	}

public java.lang.String getCd_divisa() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
	if (divisa == null)
		return null;
	return divisa.getCd_divisa();
}
public java.lang.Integer getCd_responsabile_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile = this.getResponsabile();
	if (responsabile == null)
		return null;
	return responsabile.getCd_terzo();
}
public java.lang.String getCd_tipo_progetto() {
	Tipo_progettoBulk tipo = this.getTipo();
	if (tipo == null)
		return null;
	return tipo.getCd_tipo_progetto();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.20.58)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getDettagli() {
	return dettagli;
}
public it.cnr.jada.bulk.BulkList getDettagliFinanziatori() {
	return dettagliFinanziatori;
}

/**
 * Restituisce il valore della proprietà 'rOprogetto'
 *
 * @return Il valore della proprietà 'rOprogetto'
 */
public boolean isROprogetto() {
	/* la prima condizione perchè il campo appariva
	 * non utilizzabile nelle ricerchi guidate */ 
	if (getCrudStatus() == UNDEFINED)
		return false;

	return getProgettopadre() == null ||
			getProgettopadre().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL ||
	         !getProgettopadre().isOperabile();
}

public boolean isROfind_nodo_padre() {

	if (getCrudStatus() == UNDEFINED)
		return false;

	return getProgettopadre() == null ||
	       !getProgettopadre().isOperabile();
}

/**
 * Restituisce il valore della proprietà 'rOprogetto'
 *
 * @return Il valore della proprietà 'rOprogetto'
 */
public boolean isROprogettopadre() {

	return true;
}
public boolean isROprogettoCodifica() {
	if (getParametriCds()!=null && getParametriCds().getFl_progetto_numeratore() != null) {
		if (getParametriCds().getFl_progetto_numeratore().booleanValue())
			return true;
		else
			return false;
	}
	else
		return false;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.27.32)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisa() {
	return divisa;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.28)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getResponsabile() {
	return responsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 14.29.45)
 * @return it.cnr.contabres.progettiric00.tabrif.bulk.Tipo_progettoBulk
 */
public Tipo_progettoBulk getTipo() {
	return tipo;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.24.21)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
public boolean isRODt_fine() {
	return getDt_fine() != null;
}
public Progetto_uoBulk removeFromDettagli(int index) {
	Progetto_uoBulk dett = (Progetto_uoBulk)dettagli.remove(index);
	return dett;
}
public Progetto_finanziatoreBulk removeFromDettagliFinanziatori(int index) {
	Progetto_finanziatoreBulk dett = (Progetto_finanziatoreBulk)dettagliFinanziatori.remove(index);
	return dett;
}
public Progetto_partner_esternoBulk removeFromDettagliPartner_esterni(int index) {
	Progetto_partner_esternoBulk dett = (Progetto_partner_esternoBulk)dettagliPartner_esterni.remove(index);
	return dett;
}

public void setCd_divisa(java.lang.String cd_divisa) {
	this.getDivisa().setCd_divisa(cd_divisa);
}
public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
	this.getResponsabile().setCd_terzo(cd_responsabile_terzo);
}
public void setCd_tipo_progetto(java.lang.String cd_tipo_fondo) {
	this.getTipo().setCd_tipo_progetto(cd_tipo_fondo);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.20.58)
 * @param newDettagli it.cnr.jada.bulk.BulkList
 */
public void setDettagli(it.cnr.jada.bulk.BulkList newDettagli) {
	dettagli = newDettagli;
}
public void setDettagliFinanziatori(it.cnr.jada.bulk.BulkList newDettagliFinanziatori) {
	dettagliFinanziatori = newDettagliFinanziatori;
}

/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.27.32)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newDivisa) {
	divisa = newDivisa;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.25.28)
 * @param newResponsabile it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setResponsabile(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newResponsabile) {
	responsabile = newResponsabile;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 14.29.45)
 * @param newTipo it.cnr.contabres.progettiric00.tabrif.bulk.Tipo_progettoBulk
 */
public void setTipo(Tipo_progettoBulk newTipo) {
	tipo = newTipo;
}
/**
 * Insert the method's description here.
 * Creation date: (17/12/2001 15.24.21)
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
	/**
	 * Returns the progettopadre.
	 * @return ProgettoGestBulk
	 */
	public ProgettoGestBulk getProgettopadre() {
		return progettopadre;
	}

	/**
	 * Sets the progettopadre.
	 * @param progettopadre The progettopadre to set
	 */
	public void setProgettopadre(ProgettoGestBulk progettopadre) {
		this.progettopadre = progettopadre;
	}


	public java.lang.Integer getPg_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getPg_progetto();
	}
	public Integer getEsercizio_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getEsercizio();
	}
	public String getTipo_fase_progetto_padre() {
		it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			return null;
		return progettopadre.getTipo_fase();
	}
	/**
	 * Sets the pg_progetto_padre.
	 * @param pg_progetto_padre The pg_progetto_padre to set
	 */
	public void setPg_progetto_padre(java.lang.Integer progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new ProgettoGestBulk());
		this.getProgettopadre().setPg_progetto(progetto_padre);
	}
    public void setEsercizio_progetto_padre(Integer esercizio_progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new ProgettoGestBulk());
		this.getProgettopadre().setEsercizio(esercizio_progetto_padre);
    }
    public void setTipo_fase_progetto_padre(String tipo_fase_progetto_padre) {
		it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk progettopadre = this.getProgettopadre();
		if (progettopadre == null)
			setProgettopadre(new ProgettoGestBulk());
		this.getProgettopadre().setTipo_fase(tipo_fase_progetto_padre);
    }
	public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setProgettopadre(new ProgettoGestBulk());
		setDipartimento(new DipartimentoBulk());
		setTipo_fase(ProgettoGestBulk.TIPO_FASE_PREVISIONE);
		return super.initializeForFreeSearch(bp,context);
	}
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setTipo_fase(ProgettoGestBulk.TIPO_FASE_PREVISIONE);
		return super.initializeForSearch(bp,context);
	}
	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		if((bp.getStatus() == bp.INSERT || bp.getStatus() == bp.EDIT)&&(bp instanceof TestataProgettiRicercaBP || bp instanceof TestataProgettiRicercaNuovoBP)){
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		setUnita_organizzativa(unita_organizzativa);
		setStato(ProgettoGestBulk.TIPO_STATO_PROPOSTA);
	  }
	  return this;
	}
	/**
	 * Returns the progetti_figli.
	 * @return java.util.Collection
	 */
	public java.util.Collection getProgetti_figli() {
		return progetti_figli;
	}

	/**
	 * Sets the progetti_figli.
	 * @param progetti_figli The progetti_figli to set
	 */
	public void setProgetti_figli(java.util.Collection progetti_figli) {
		this.progetti_figli = progetti_figli;
	}
	/**
	 * Returns the dettagliPartner_esterni.
	 * @return BulkList
	 */
	public BulkList getDettagliPartner_esterni() {
		return dettagliPartner_esterni;
	}

	/**
	 * Sets the dettagliPartner_esterni.
	 * @param dettagliPartner_esterni The dettagliPartner_esterni to set
	 */
	public void setDettagliPartner_esterni(BulkList dettagliPartner_esterni) {
		this.dettagliPartner_esterni = dettagliPartner_esterni;
	}

	/**
	 * @return
	 */
	public Commessa_spesaBulk getSpese() {
		return spese;
	}

	/**
	 * @param bulk
	 */
	public void setSpese(Commessa_spesaBulk bulk) {
		spese = bulk;
	}

	public java.math.BigDecimal getGenerale_affitto() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getGenerale_affitto();
	}

	public void setGenerale_affitto(java.math.BigDecimal generale_affitto) {
		this.getSpese().setGenerale_affitto(generale_affitto);
	}

	/**
	 * @return
	 */
	public BulkList getSpeseEsercizio() {
		return speseEsercizio;
	}

	/**
	 * @param list
	 */
	public void setSpeseEsercizio(BulkList list) {
		speseEsercizio = list;
		if (speseEsercizio!= null && speseEsercizio.size()>0)
			this.setSpese((Commessa_spesaBulk) list.get(0));
		else
			this.setSpese(null);
	}

	/**
	 * @return
	 */
	public BigDecimal getAcc_tfr() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAcc_tfr();
	}

	/**
	 * @return
	 */
	public BigDecimal getAmm_altri_beni() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAmm_altri_beni();
	}

	/**
	 * @return
	 */
	public BigDecimal getAmm_immobili() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAmm_immobili();
	}

	/**
	 * @return
	 */
	public BigDecimal getAmm_tecnico() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getAmm_tecnico();
	}

	/**
	 * @return
	 */
	public BigDecimal getCc_brev_pi() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getCc_brev_pi();
	}

	/**
	 * @return
	 */
	public BigDecimal getEdilizia() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getEdilizia();
	}

	/**
	 * @return
	 */
	public BigDecimal getGestione_nave() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getGestione_nave();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_fo() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_fo();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_min() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_min();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_privati() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_privati();
	}

	/**
	 * @return
	 */
	public BigDecimal getRes_ue_int() {
		Commessa_spesaBulk spese = this.getSpese();
		if (spese == null)
			return null;
		return spese.getRes_ue_int();
	}

	/**
	 * @param decimal
	 */
	public void setAcc_tfr(BigDecimal acc_tfr) {
		this.getSpese().setAcc_tfr(acc_tfr);
	}

	/**
	 * @param decimal
	 */
	public void setAmm_altri_beni(BigDecimal amm_altri_beni) {
		this.getSpese().setAmm_altri_beni(amm_altri_beni);
	}

	/**
	 * @param decimal
	 */
	public void setAmm_immobili(BigDecimal amm_immobili) {
		this.getSpese().setAmm_immobili(amm_immobili);
	}

	/**
	 * @param decimal
	 */
	public void setAmm_tecnico(BigDecimal amm_tecnico) {
		this.getSpese().setAmm_tecnico(amm_tecnico);
	}

	/**
	 * @param decimal
	 */
	public void setCc_brev_pi(BigDecimal cc_brev_pi) {
		this.getSpese().setCc_brev_pi(cc_brev_pi);
	}

	/**
	 * @param decimal
	 */
	public void setEdilizia(BigDecimal edilizia) {
		this.getSpese().setEdilizia(edilizia);
	}

	/**
	 * @param decimal
	 */
	public void setGestione_nave(BigDecimal gestione_nave) {
		this.getSpese().setGestione_nave(gestione_nave);
	}

	/**
	 * @param decimal
	 */
	public void setRes_fo(BigDecimal res_fo) {
		this.getSpese().setRes_fo(res_fo);
	}

	/**
	 * @param decimal
	 */
	public void setRes_min(BigDecimal res_min) {
		this.getSpese().setRes_min(res_min);
	}

	/**
	 * @param decimal
	 */
	public void setRes_privati(BigDecimal res_privati) {
		this.getSpese().setRes_privati(res_privati);
	}

	/**
	 * @param decimal
	 */
	public void setRes_ue_int(BigDecimal res_ue_int) {
		this.getSpese().setRes_ue_int(res_ue_int);
	}

	public BigDecimal getTotaleSpese() {
		if (getSpese()!=null) {
			BigDecimal tot = new BigDecimal("0");
			if (this.getGenerale_affitto()!=null)
				tot = tot.add(this.getGenerale_affitto());
			if (this.getGestione_nave()!=null)
				tot = tot.add(this.getGestione_nave());
			if (this.getCc_brev_pi()!=null)
				tot = tot.add(this.getCc_brev_pi());
			if (this.getEdilizia()!=null)
				tot = tot.add(this.getEdilizia());
			return(tot);
		}
		else
			return(null);
	}

	public BigDecimal getTotaleCostiFigurativi() {
		if (getSpese()!=null) {
			BigDecimal tot = new BigDecimal("0");
			if (this.getAmm_immobili()!=null)
				tot = tot.add(this.getAmm_immobili());
			if (this.getAcc_tfr()!=null)
				tot = tot.add(this.getAcc_tfr());
			if (this.getAmm_tecnico()!=null)
				tot = tot.add(this.getAmm_tecnico());
			if (this.getAmm_altri_beni()!=null)
				tot = tot.add(this.getAmm_altri_beni());
			return(tot);
		}
		else
			return(null);
	}

	public BigDecimal getTotaleRisorsePresunte() {
		if (getSpese()!=null) {
			BigDecimal tot = new BigDecimal("0");
			if (this.getRes_fo()!=null)
				tot = tot.add(this.getRes_fo());
			if (this.getRes_min()!=null)
				tot = tot.add(this.getRes_min());
			if (this.getRes_privati()!=null)
				tot = tot.add(this.getRes_privati());
			if (this.getRes_ue_int()!=null)
				tot = tot.add(this.getRes_ue_int());
			return(tot);
		}
		else
			return(null);
	}
	/**
	 * @return
	 */
	public DipartimentoBulk getDipartimento() {
		return dipartimento;
	}

	/**
	 * @param bulk
	 */
	public void setDipartimento(DipartimentoBulk bulk) {
		dipartimento = bulk;
	}
	
	public java.lang.String getCd_dipartimento() {
		DipartimentoBulk dipartimento = this.getDipartimento();
		if (dipartimento == null)
			return null;
		return dipartimento.getCd_dipartimento();
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento) {
		this.getDipartimento().setCd_dipartimento(cd_dipartimento);
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo dipartimento
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isRODipartimento() {
		/* la prima condizione perchè il campo appariva
		 * non utilizzabile nelle ricerchi guidate */ 
		if (getCrudStatus() == UNDEFINED)
			return false;
		return dipartimento == null || dipartimento.getCrudStatus() == NORMAL || dipartimento.getCrudStatus() == TO_BE_UPDATED;
	}	

	/**
	 * @return
	 */
	public Parametri_cdsBulk getParametriCds() {
		return parametriCds;
	}

	/**
	 * @param bulk
	 */
	public void setParametriCds(Parametri_cdsBulk bulk) {
		parametriCds = bulk;
	}
	public boolean isProgetto(){
		return getLivello().equals(ProgettoGestBulk.LIVELLO_PROGETTO_PRIMO);
	}
	public boolean isCommessa(){
		return getLivello().equals(ProgettoGestBulk.LIVELLO_PROGETTO_SECONDO);
	}
	public boolean isModulo(){
		return getLivello().equals(ProgettoGestBulk.LIVELLO_PROGETTO_TERZO);
	}
}