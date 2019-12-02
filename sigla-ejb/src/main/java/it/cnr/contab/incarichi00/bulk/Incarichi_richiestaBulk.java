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
 * Date 23/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tematica_attivitaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
public class Incarichi_richiestaBulk extends Incarichi_richiestaBase {
	private Unita_organizzativaBulk unita_organizzativa = new Unita_organizzativaBulk();
	private Tematica_attivitaBulk tematica_attivita;
	private CdsBulk cds = new CdsBulk();
	private String indirizzo_unita_organizzativa;
	
	public static final String STATO_PROVVISORIO  = "P";
	public static final String STATO_DEFINITIVO   = "D";
	public static final String STATO_ANNULLATO    = "A";
	public static final String STATO_CHIUSO    = "C";
	public static final String STATO_CANCELLATO    = "X";
	
	public final static Dictionary ti_statoKeys;
	static {
		ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_statoKeys.put(STATO_PROVVISORIO,"Provvisoria");
		ti_statoKeys.put(STATO_DEFINITIVO, "Definitiva");
		ti_statoKeys.put(STATO_ANNULLATO,  "Annullata");
		ti_statoKeys.put(STATO_CANCELLATO, "Cancellata");
		ti_statoKeys.put(STATO_CHIUSO, "Chiusa");
	};

	public static final String PERSONALE_INTERNO_TROVATO            = "SI";
	public static final String PERSONALE_INTERNO_TROVATO_NON_ADATTO = "NA";
	public static final String PERSONALE_INTERNO_NON_TROVATO        = "NO";
	
	public final static Dictionary personale_internoKeys;
	static {
		personale_internoKeys = new it.cnr.jada.util.OrderedHashtable();
		personale_internoKeys.put(PERSONALE_INTERNO_TROVATO,"Conforme alla richiesta");
		personale_internoKeys.put(PERSONALE_INTERNO_TROVATO_NON_ADATTO, "Non conforme alla richiesta");
		personale_internoKeys.put(PERSONALE_INTERNO_NON_TROVATO,  "Non pervenute candidature");
	};

	private BulkList incarichi_proceduraColl;
	private it.cnr.jada.util.OrderedHashtable nrRisorseTrovateList = new it.cnr.jada.util.OrderedHashtable();
	
	public Incarichi_richiestaBulk() {
		super();
	}
	public Incarichi_richiestaBulk(java.lang.Integer esercizio, java.lang.Long pg_richiesta) {
		super(esercizio, pg_richiesta);
	}
    public boolean isRichiestaProvvisoria() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_PROVVISORIO);
    }
    public boolean isRichiestaDefinitiva() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_DEFINITIVO);
    }
    public boolean isRichiestaAnnullata() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_ANNULLATO);
    }
    public boolean isRichiestaChiusa() {
       	return this.getStato()!=null&&this.getStato().equals(STATO_CHIUSO);
    }
    public boolean isRichiestaCancellata() {
    	return this.getStato()!=null&&this.getStato().equals(STATO_CANCELLATO);
    }
    public boolean isPubblicazioneFinita() {
    	return isRichiestaDefinitiva()&&
    		   getData_fine_pubblicazione().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
    }
    public boolean isPubblicazioneInCorso() {
    	return isRichiestaDefinitiva()  &&
    		   !getData_pubblicazione().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()) &&
    		   !isPubblicazioneFinita();
    }
    public boolean isRichiestaScaduta() {
    	return isRichiestaDefinitiva() &&
    	       isPubblicazioneFinita() &&
    	       getData_scadenza().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
    }
    public boolean isRichiestaInScadenza() {
    	return (isRichiestaDefinitiva() || isRichiestaChiusa()) &&
    	       !isPubblicazioneInCorso() &&
    	       !isRichiestaScaduta();
    }
    public CdsBulk getCds() {
		return cds;
	}
	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}
	public String getCd_cds() {
		if (getCds()!=null)
			return getCds().getCd_unita_organizzativa();
		return null;
	}
	public void setCd_cds(String cd_cds) {
		if (getCds()!=null)
			getCds().setCd_unita_organizzativa(cd_cds);
	}
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}
	public void setUnita_organizzativa(Unita_organizzativaBulk unita_organizzativa) {
		this.unita_organizzativa = unita_organizzativa;
	}
	public String getCd_unita_organizzativa() {
		if (getUnita_organizzativa()!=null)
			return getUnita_organizzativa().getCd_unita_organizzativa();
		return null;
	}
	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		if (getUnita_organizzativa()!=null)
			getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
    public String getIdRichiestaText() {
    	if (getEsercizio()!=null && getPg_richiesta()!=null)
    		return getEsercizio()+"/"+getPg_richiesta();
    	return null;
    }
	public String getIndirizzo_unita_organizzativa() {
		return indirizzo_unita_organizzativa;
	}
	public void setIndirizzo_unita_organizzativa(
			String indirizzo_unita_organizzativa) {
		this.indirizzo_unita_organizzativa = indirizzo_unita_organizzativa;
	}
	public boolean isROPersonaleInterno(){
		return false;
	}
	public String getStatoText(){
		if (this.isRichiestaProvvisoria())
			return ti_statoKeys.get(STATO_PROVVISORIO).toString();
		if (this.isRichiestaAnnullata())
			return ti_statoKeys.get(STATO_ANNULLATO).toString();
		if (this.isRichiestaCancellata())
			return ti_statoKeys.get(STATO_CANCELLATO).toString();
		if (this.isRichiestaChiusa())
			return "Chiusa";
		if (this.isPubblicazioneInCorso())
			return "Pubblicata";
		if (this.isRichiestaInScadenza())
			return "In scadenza";
		if (this.isRichiestaScaduta())
			return "Scaduta";
		return null;
	}
	public boolean isROCds() {
		return getUnita_organizzativa()!=null && 
		       getUnita_organizzativa().getCd_unita_organizzativa()!=null;
	}
	public Tematica_attivitaBulk getTematica_attivita() {
		return tematica_attivita;
	}
	public void setTematica_attivita(Tematica_attivitaBulk tematica_attivita) {
		this.tematica_attivita = tematica_attivita;
	}
	public String getCd_tematica_attivita() {
		if (this.getTematica_attivita() == null)
			return null;
		return this.getTematica_attivita().getCd_tematica_attivita();
	}
	public BulkList getIncarichi_proceduraColl() {
		return incarichi_proceduraColl;
	}
	public void setIncarichi_proceduraColl(
			BulkList incarichi_proceduraColl) {
		this.incarichi_proceduraColl = incarichi_proceduraColl;
	}
	public it.cnr.jada.util.OrderedHashtable getNrRisorseTrovateList() {
		return nrRisorseTrovateList;
	}
	public void setNrRisorseTrovateList(it.cnr.jada.util.OrderedHashtable hashtable) {
		nrRisorseTrovateList = hashtable;
	}
	public void caricaNrRisorseTrovateList(ActionContext actioncontext) {
		if (getNr_risorse_da_trovare()!=null && getNr_risorse_da_trovare()>0 )		
			for (int i=getNr_risorse_da_trovare().intValue();i>=0;i--)
				getNrRisorseTrovateList().put(i, i);
	}
	public Integer getNrContrattiAttivati() { 
		Integer totale = 0;
		if (getIncarichi_proceduraColl()==null) return totale;
		for (Iterator i=getIncarichi_proceduraColl().iterator();i.hasNext();)
			totale = totale + ((Incarichi_proceduraBulk)i.next()).getNr_contratti().intValue();
		return totale;
	}
	public Integer getNrRisorseTrovate() { 
		return getNr_risorse_trovate_si();
	}
	public Integer getNrRisorseNonTrovate() { 
		return getNr_risorse_da_trovare() - getNrRisorseTrovate();
	}
	public void validate() throws ValidationException {
		if (getNr_risorse_da_trovare()!=null && getNr_risorse_da_trovare().compareTo(new Integer(0))!=1) 
			throw new ValidationException("Il numero delle risorse richieste deve essere positivo.");
		if (getNr_risorse_trovate_si() + 
			getNr_risorse_trovate_no() + 
			getNr_risorse_trovate_na() > getNr_risorse_da_trovare())
			throw new ValidationException("L'esito della ricerca non può comportare un numero di risorse superiore a quello cercate.");
		
		if (getNrRisorseNonTrovate()!=null && getNrContrattiAttivati()!=0 &&
			getNrRisorseNonTrovate().compareTo(getNrContrattiAttivati())==1)
			throw new ValidationException("Il numero delle risorse non trovate non può essere inferiore ai contratti che risultano già attivati ("+getNrContrattiAttivati()+").");

		if (getEmail_risposte()!=null)
		try {
			StringTokenizer st = new StringTokenizer(getEmail_risposte(),",");
		     while (st.hasMoreTokens()) {
				new InternetAddress(st.nextToken()).validate();
		     }
		} catch (AddressException e) {
			throw new ValidationException("Indirizzo E-Mail non valido!");
		}
		super.validate();
	}
}
