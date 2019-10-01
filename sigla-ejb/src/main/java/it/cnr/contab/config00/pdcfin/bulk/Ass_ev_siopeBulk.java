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
 * Date 07/05/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import java.util.Dictionary;
import java.util.Hashtable;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

public class Ass_ev_siopeBulk extends Ass_ev_siopeBase {
	
	private Elemento_voceBulk elemento_voce = new Elemento_voceBulk();
	private Elemento_voceHome elemento_home;
	private Codici_siopeBulk codici_siope = new Codici_siopeBulk();
	
	private boolean gia_associato = false;
	
	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;
	
	private final static java.util.Dictionary ti_gestioneKeys;
	static {
		ti_gestioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestioneKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestioneKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	
	
	private final static java.util.Dictionary ti_gestione_siopeKeys;
	static {
		ti_gestione_siopeKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestione_siopeKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestione_siopeKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	
	
	public final static String APPARTENENZA_CNR = "C" ;
	public final static String APPARTENENZA_CDS = "D" ;

	private final static java.util.Dictionary ti_appartenenzaKeys;
	static {
		ti_appartenenzaKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_appartenenzaKeys.put(APPARTENENZA_CNR, "C - CNR");
		ti_appartenenzaKeys.put(APPARTENENZA_CDS, "D - CDS");
	}	
	
	public Ass_ev_siopeBulk() {
		super();
	}
	public Ass_ev_siopeBulk(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce, java.lang.String cd_siope) {
		super(esercizio, ti_appartenenza, ti_gestione, cd_elemento_voce, cd_siope);
	}
		
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	public Codici_siopeBulk getCodici_siope() {
		return codici_siope;
	}
	public void setElemento_voce(Elemento_voceBulk newElemento_voce) {
		elemento_voce = newElemento_voce;
	}
	public void setCodici_siope(Codici_siopeBulk newCodici_siope) {
		codici_siope = newCodici_siope;
	}

	/**
	 * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
	 * @param bp  Business Process <code>CRUDBP</code> in uso.
	 * @param context  <code>ActionContext</code> in uso.
	 * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		return this;
	}
	/**
	 * Metodo per inizializzare l'oggetto bulk in fase di ricerca.
	 * @param bp  Business Process <code>CRUDBP</code> in uso.
	 * @param context  <code>ActionContext</code> in uso.
	 * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
	 */
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		return this;
	}
	
	public boolean isROGiaAssociato() {
			if (elemento_voce!=null ||codici_siope!=null)
		return true;
			return false;
	}
	
	public java.lang.Integer getEsercizio() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getEsercizio();
	}
	public java.lang.String getCd_elemento_voce() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
	}
	public java.lang.String getTi_appartenenza() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	public java.lang.String getTi_gestione() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}
	
	public void setEsercizio(java.lang.Integer esercizio) {
		this.getElemento_voce().setEsercizio(esercizio);
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.getElemento_voce().setTi_gestione(ti_gestione);
	}
	
	public Dictionary getTi_appartenenzaKeys() {
		return ti_appartenenzaKeys;
	}
	public Dictionary getTi_gestioneKeys() {
		return ti_gestioneKeys;
	}
	public Dictionary getTi_gestione_siopeKeys() {
		return ti_gestione_siopeKeys;
	}
	
	public java.lang.Integer getEsercizio_siope() {
		Codici_siopeBulk codici_siope = this.getCodici_siope();
		if (codici_siope == null)
			return null;
		return codici_siope.getEsercizio();
	}
	public java.lang.String getCd_siope() {
		Codici_siopeBulk codici_siope = this.getCodici_siope();
		if (codici_siope == null)
			return null;
		return codici_siope.getCd_siope();
	}
	public java.lang.String getTi_gestione_siope() {
		Codici_siopeBulk codici_siope = this.getCodici_siope();
		if (codici_siope == null)
			return null;
		return codici_siope.getTi_gestione();
	}
	
	public void setEsercizio_siope(java.lang.Integer esercizio_siope) {
		this.getCodici_siope().setEsercizio(esercizio_siope);
	} 
	public void setCd_siope(java.lang.String cd_siope) {
		this.getCodici_siope().setCd_siope(cd_siope);
	} 
	public void setTi_gestione_siope(java.lang.String ti_gestione_siope) {
		this.getCodici_siope().setTi_gestione(ti_gestione_siope);
	} 
	
	public void validate() throws ValidationException {
		super.validate();
		if ( elemento_voce == null || elemento_voce.getCd_elemento_voce() == null ||  elemento_voce.getCd_elemento_voce().equals("") )
			throw new ValidationException( "Il codice del Elemento Voce è obbligatorio." );
		if ( elemento_voce.getTi_appartenenza() == null || elemento_voce.getTi_appartenenza().equals(""))
			throw new ValidationException( "Il campo appartenenza è obbligatorio." );
		if ( elemento_voce.getTi_gestione() == null || elemento_voce.getTi_gestione().equals(""))
			throw new ValidationException( "Il campo gestione è obbligatorio." );
		if ( codici_siope.getCd_siope() == null || codici_siope.getCd_siope().equals(""))
			throw new ValidationException( "Il campo codice siope è obbligatorio." );
	}
}