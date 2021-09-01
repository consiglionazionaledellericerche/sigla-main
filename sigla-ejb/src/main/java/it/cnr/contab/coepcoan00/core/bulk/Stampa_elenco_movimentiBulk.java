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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
//import it.cnr.contab.config00.pdcep.bulk.Voce_epBulk;
//import it.cnr.contab.utenze00.bp.CNRUserContext;

/**
 * Insert the type's description here.
 * Creation date: (08/08/2005 16.02.55)
 * @author: Marco Spasiano
 */
public class Stampa_elenco_movimentiBulk extends it.cnr.jada.bulk.OggettoBulk {
	
	//	Esercizio di scrivania
	private Integer esercizio;

	private String tipologia;
	
	
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;
	private boolean cdsForPrintEnabled;	
	private boolean uoForPrintEnabled;
	
	private static final java.util.Dictionary ti_tipologiaKeys = new it.cnr.jada.util.OrderedHashtable();
	private static final java.util.Dictionary ti_attivaKeys = new it.cnr.jada.util.OrderedHashtable();
	
	private String attiva;
	private TerzoBulk terzoForPrint;
	private ContoBulk contoForPrint; 
	private boolean terzoForPrintEnabled;	
	private boolean contoForPrintEnabled;
	
	private Boolean ragr_causale;
	private Boolean ragr_chiusura;
	private Boolean ragr_doc_amm;
	private Boolean ragr_doc_cont;
	private Boolean ragr_liquid_iva;
	private Boolean ragr_mig_beni;
	private Boolean ragr_stipendi;
	
	final public static String TIPO_TUTTO = "*";

	static {
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			ti_tipologiaKeys.put(tipoIVA.value(), tipoIVA.label());
		}
		ti_tipologiaKeys.put(TIPO_TUTTO,"Tutto");
	}

	final public static String ATTIVA_SI = "Y";
	final public static String ATTIVA_NO = "N";
	final public static String ATTIVA_TUTTO = "*";
	
			
	static {
			ti_attivaKeys.put(ATTIVA_SI,"Si");
			ti_attivaKeys.put(ATTIVA_NO,"No");
			ti_attivaKeys.put(ATTIVA_TUTTO,"Tutte");
	}
			
	/**
	 * Stampa_elenco_movimentiBulk constructor comment.
	 */
	public Stampa_elenco_movimentiBulk() {
		super();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2005 12:34:48)
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getti_tipologiaKeys() {
		return ti_tipologiaKeys;
	}
	
	public final java.util.Dictionary getti_attivaKeys() {
			return ti_attivaKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/01/2003 16.50.12)
	 * @return java.sql.Timestamp
	 */
	public String getCdUoForPrint() {
		if (getUoForPrint()==null)
		  return "*";
		if(getUoForPrint().getCd_unita_organizzativa() == null)
		  return "*";	
		return getUoForPrint().getCd_unita_organizzativa();
	}
	public String getCdCdsForPrint() {
		if (getCdsForPrint()==null)
		  return "*";
		if (getCdsForPrint().getCd_unita_organizzativa()==null)
		  return "*";
		return getCdsForPrint().getCd_unita_organizzativa();
	}
	
	public String getCdTerzoForPrint() {
		if (getTerzoForPrint()==null)
		  return "*";
		if (getTerzoForPrint().getCd_terzo()==null)
		  return "*";
		return getTerzoForPrint().getCd_terzo().toString();
	}
	
	
	/**
	 * Inizializza gli attributi di ragruppamento
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk Stampa_elenco_movimentiBulk con i flag inizializzati
	 */
	
	public void inizializzaRagruppamenti() {
		setRagr_causale(new Boolean(false));
		setRagr_chiusura(new Boolean(false));
		setRagr_doc_amm(new Boolean(true));
		setRagr_doc_cont(new Boolean(false));
		setRagr_liquid_iva(new Boolean(false));
		setRagr_mig_beni(new Boolean(false));
		setRagr_stipendi(new Boolean(false));
	}
	public void selezionaRagruppamenti(){
		setRagr_causale(new Boolean(!getRagr_causale().booleanValue()));
		setRagr_chiusura(new Boolean(!getRagr_chiusura().booleanValue()));
		setRagr_doc_amm(new Boolean(!getRagr_doc_amm().booleanValue()));
		setRagr_doc_cont(new Boolean(!getRagr_doc_cont().booleanValue()));
		setRagr_liquid_iva(new Boolean(!getRagr_liquid_iva().booleanValue()));
		setRagr_mig_beni(new Boolean(!getRagr_mig_beni().booleanValue()));
		setRagr_stipendi(new Boolean(!getRagr_stipendi().booleanValue()));
		}
	
		/**
		 * @return
		 */
		public Boolean getRagr_causale() {
			return ragr_causale;
		}

		public Boolean getRagr_chiusura() {
			return ragr_chiusura;
		} 
			
		public Boolean getRagr_doc_amm() {
			return ragr_doc_amm;
		}
		
		public Boolean getRagr_doc_cont() {
			return ragr_doc_cont;
		}
		
		public Boolean getRagr_liquid_iva() {
			return ragr_liquid_iva;
		}
		
		public Boolean getRagr_mig_beni() {
			return ragr_mig_beni;
		}
		
		public Boolean getRagr_stipendi() {
			return ragr_stipendi;
		}
		/**
		 * @param boolean1
		 */
		public void setRagr_causale(Boolean boolean1) {
			ragr_causale = boolean1;
		}
		
		public void setRagr_chiusura(Boolean boolean1) {
			ragr_chiusura = boolean1;
		}
		
		public void setRagr_doc_amm(Boolean boolean1) {
			ragr_doc_amm = boolean1;
		}
		
		public void setRagr_doc_cont(Boolean boolean1) {
			ragr_doc_cont = boolean1;
		}
		
		public void setRagr_liquid_iva(Boolean boolean1) {
			ragr_liquid_iva = boolean1;
		}
		
		public void setRagr_mig_beni(Boolean boolean1) {
			ragr_mig_beni = boolean1;
		}
		
		public void setRagr_stipendi(Boolean boolean1) {
			ragr_stipendi = boolean1;
		}
	    /**
		 * Insert the method's description here.
		 * Creation date: (14/05/2003 9.28.52)
		 * @return java.lang.Integer
		 */
		public java.lang.Integer getEsercizio() {
			return esercizio;
		}
		
		
		/**
		 * Insert the method's description here.
		 * Creation date: (23/01/2003 12.00.24)
		 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
		 */
		public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
			return uoForPrint;
		}
		/**
		 * Insert the method's description here.
		 * Creation date: (20/12/2002 10.47.40)
		 * @param newCdUOEmittente java.lang.String
		 */
		public boolean isROUoForPrint() {
			return !isUoForPrintEnabled();
		}
		public boolean isROCdsForPrint() {
			return !isCdsForPrintEnabled();
		}
		public boolean isROContoForPrint() {
			return getContoForPrint()==null || getContoForPrint().getCrudStatus()==NORMAL;
		}
	
	
			
		/**
		 * Insert the method's description here.
		 * Creation date: (19/05/2003 15.45.26)
		 * @param newCdsForPrint java.lang.String
		 */
		public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk newCdsForPrint) {
			cdsForPrint = newCdsForPrint;
		}
		/**
		 * Insert the method's description here.
		 * Creation date: (14/05/2003 9.28.52)
		 * @param newEsercizio java.lang.Integer
		 */
		
			
		public void setEsercizio(java.lang.Integer newEsercizio) {
			esercizio = newEsercizio;
		}
		
		
		/**
		 * Insert the method's description here.
		 * Creation date: (23/01/2003 12.00.24)
		 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
		 */
		public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unitaOrganizzativa) {
			this.uoForPrint = unitaOrganizzativa;
		}
		/**
		 * @return
		 */
		public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
			return cdsForPrint;
		}
		
	/**
	 * @return
	 */
	public String gettipologia() {
		return tipologia;
	}
	
	public String getattiva() {
			return attiva;
	}
	
	/**
	 * @param string
	 */
	public void settipologia(String string) {
		tipologia = string;
	}
	
	public void setattiva(String string) {
			attiva = string;
	}
	

	/**
	 * @return
	 */
	public TerzoBulk getTerzoForPrint() {
		return terzoForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setTerzoForPrint(TerzoBulk bulk) {
		terzoForPrint = bulk;
	}

	/**
	 * @return
	 */
	public ContoBulk getContoForPrint() {
		return contoForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setContoForPrint(ContoBulk bulk) {
		contoForPrint = bulk;
	}

	/**
	 * @return
	 */
	public boolean isCdsForPrintEnabled() {
		return cdsForPrintEnabled;
	}

	/**
	 * @param b
	 */
	public void setCdsForPrintEnabled(boolean b) {
		cdsForPrintEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean isUoForPrintEnabled() {
		return uoForPrintEnabled;
	}

	/**
	 * @param b
	 */
	public void setUoForPrintEnabled(boolean b) {
		uoForPrintEnabled = b;
	}

}
