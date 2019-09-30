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
 * Created on Oct 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

import java.math.BigDecimal;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_pdg_variazione_riepilogoBulk extends OggettoBulk {
	// Esercizio di scrivania
	private Integer esercizio;
    // pg_variazione
	private Pdg_variazioneBulk pdg_variazioneForPrint;
	private boolean pdg_variazioneForPrintEnabled;
	
	private Pdg_variazioneBulk pg_variazione_pdg;
	private BigDecimal pg_stampa;
	
	private Unita_organizzativaBulk uoForPrint;
	private boolean uoForPrintEnabled;
	
	private CdsBulk cdsForPrint;
	private boolean cdsForPrintEnabled;	
	
	private java.sql.Date dataApprovazione_da;
	private java.sql.Date dataApprovazione_a;
	
	private boolean dataApprovazione_daEnabled;
	private boolean dataApprovazione_aEnabled;
	
	private String stato;
	private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
		
	final public static String STATO_PRP = "PRP";
	final public static String STATO_PRD = "PRD";
	final public static String STATO_APP = "APP";
	final public static String STATO_ANN = "ANN";
	final public static String STATO_RES = "RES";
	final public static String STATO_TUTTI = "*";
	
			
	static {
			ti_statoKeys.put(STATO_PRP,"Proposta Provvisoria");
			ti_statoKeys.put(STATO_PRD,"Proposta Definitiva");
			ti_statoKeys.put(STATO_APP,"Approvata");
			ti_statoKeys.put(STATO_ANN,"Annullata");
			ti_statoKeys.put(STATO_RES,"Respinta");
			ti_statoKeys.put(STATO_TUTTI,"Tutti");
	}
	
	private BulkList riepilogovariazioni = new BulkList();	
	/**
	 * 
	 */
	public Stampa_pdg_variazione_riepilogoBulk() {
		super();
	}
	
	

	/*public String getPg_variazione_pdgForPrint() {

			if (getPdg_variazioneForPrint()==null)
				return "*";
			if (getPdg_variazioneForPrint().getPg_variazione_pdg()==null)
				return "*";
			return getPdg_variazioneForPrint().getPg_variazione_pdg().toString();
		}*/

	public String getCdUoForPrint() {

			if (getUoForPrint()==null)
				return "*";
			if (getUoForPrint().getCd_unita_organizzativa()==null)
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
	
	
	/*public Pdg_variazioneBulk getPdg_variazioneForPrint() {
		return pdg_variazioneForPrint;
	}*/

	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public Unita_organizzativaBulk getUoForPrint() {
		return uoForPrint;
	}
	
	public CdsBulk getCdsForPrint() {
			return cdsForPrint;
		}
	
	public final java.util.Dictionary getTi_statoKeys() {
				return ti_statoKeys;
			}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @return boolean
	 */
	/*public boolean isPdg_variazioneForPrintEnabled() {
		return pdg_variazioneForPrintEnabled;
	}
	
	public void setPdg_variazioneForPrintEnabled(boolean b) {
		pdg_variazioneForPrintEnabled = b;
			}*/
			
	public boolean isUoForPrintEnabled() {
			return uoForPrintEnabled;
		}
	
	public void setUoForPrintEnabled(boolean b) {
			uoForPrintEnabled = b;
			}
			
	public boolean isCdsForPrintEnabled() {
				return cdsForPrintEnabled;
			}
	
	public void setCdsForPrintEnabled(boolean b) {
			cdsForPrintEnabled = b;
			}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newEsercizio java.lang.Integer
	 */
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	/*public void setPdg_variazioneForPrint(Pdg_variazioneBulk newPdg_variazioneForPrint) {
			pdg_variazioneForPrint = newPdg_variazioneForPrint;
		}*/
		
	public void setUoForPrint(Unita_organizzativaBulk newUoForPrint) {
				uoForPrint = newUoForPrint;
		}
		
	public void setCdsForPrint(CdsBulk newCdsForPrint) {
				cdsForPrint = newCdsForPrint;
		}
		
	public String getStato() {
				 return stato;
	}
	
	public void setStato(String string) {
			 stato = string;
	}
	
	
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {riepilogovariazioni};
	}
	/**
	 * @return
	 */
	
	public BulkList getRiepilogovariazioni() {
		return riepilogovariazioni;
	}

	/**
	 * @param list
	 */
	public void setRiepilogovariazioni(BulkList list) {
		riepilogovariazioni = list;
	}
	
	public int addToRiepilogovariazioni(Pdg_variazioneBulk bulk) {
		riepilogovariazioni.add(bulk);
		return riepilogovariazioni.size()-1;
	}
		
	public Pdg_variazioneBulk removeFromRiepilogovariazioni(int index) {
		Pdg_variazioneBulk bulk = (Pdg_variazioneBulk)riepilogovariazioni.remove(index);
		return bulk;
	}
	
	public Integer getCur() {
		   return new Integer(0);
	   }
	   
	public java.sql.Date getDataApprovazione_da() {
		   return dataApprovazione_da;
	   }	
		
	public java.sql.Date getDataApprovazione_a() {
		   return dataApprovazione_a;
	   }
	   
	public void setDataApprovazione_da(java.sql.Date newDataApprovazione_da) {
		dataApprovazione_da = newDataApprovazione_da;
	   }
		
	public void setDataApprovazione_a(java.sql.Date newDataApprovazione_a) {
		dataApprovazione_a = newDataApprovazione_a;
	   }   
	
    public boolean isRODataApprovazione_da() {
		return isdataApprovazione_daEnabled();
	   }
		
    public boolean isdataApprovazione_daEnabled() {
        return dataApprovazione_daEnabled;
	  }

	public void setDataApprovazione_daEnabled(boolean b) {
		dataApprovazione_daEnabled = b;
      }
      
    public boolean isRODataApprovazione_a() {
    	  return isdataApprovazione_aEnabled();
	 }
		
	public boolean isdataApprovazione_aEnabled() {
		  return dataApprovazione_daEnabled;
	 }

	public void setDataApprovazione_aEnabled(boolean b) {
		  dataApprovazione_aEnabled = b;
	 }



	public BigDecimal getPg_stampa() {
		return pg_stampa;
	}
	



	public void setPg_stampa(BigDecimal pg_stampa) {
		this.pg_stampa = pg_stampa;
	}
	
}
