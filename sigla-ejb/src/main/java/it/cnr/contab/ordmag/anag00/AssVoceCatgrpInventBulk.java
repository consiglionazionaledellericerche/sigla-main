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
package it.cnr.contab.ordmag.anag00;
import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

public class AssVoceCatgrpInventBulk extends AssVoceCatgrpInventBase {
	
	private Elemento_voceBulk elemento_voce = new Elemento_voceBulk();
	private Categoria_gruppo_inventBulk categoria_gruppo_invent = new Categoria_gruppo_inventBulk();
	
	public final static String TI_GESTIONE_SPESE   = "S" ;
	
	public final static String APPARTENENZA_CNR = "C" ;
	public final static String APPARTENENZA_CDS = "D" ;

	private final static java.util.Dictionary ti_appartenenzaKeys;
	static {
		ti_appartenenzaKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_appartenenzaKeys.put(APPARTENENZA_CNR, "C - CNR");
		ti_appartenenzaKeys.put(APPARTENENZA_CDS, "D - CDS");
	}	
	
	public AssVoceCatgrpInventBulk() {
		super();
	}
	public AssVoceCatgrpInventBulk(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce, java.lang.String cd_categoria_gruppo) {
		super(esercizio, ti_appartenenza, ti_gestione, cd_elemento_voce, cd_categoria_gruppo);
	}
		
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	public Categoria_gruppo_inventBulk getCategoria_gruppo_invent() {
		return categoria_gruppo_invent;
	}
	public void setElemento_voce(Elemento_voceBulk newElemento_voce) {
		elemento_voce = newElemento_voce;
	}
	public void setCategoria_gruppo_invent(Categoria_gruppo_inventBulk newCategoria_gruppo_invent) {
		categoria_gruppo_invent = newCategoria_gruppo_invent;
	}

	/**
	 * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
	 * @param bp  Business Process <code>CRUDBP</code> in uso.
	 * @param context  <code>ActionContext</code> in uso.
	 * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setTi_gestione(TI_GESTIONE_SPESE);
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
		setTi_gestione(TI_GESTIONE_SPESE);
		return this;
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
	
	public java.lang.String getCd_categoria_gruppo() {
		Categoria_gruppo_inventBulk categoria_gruppo_inventBulk = this.getCategoria_gruppo_invent();
		if (categoria_gruppo_inventBulk == null)
			return null;
		return categoria_gruppo_inventBulk.getCd_categoria_gruppo();
	}
	
	public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
		this.getCategoria_gruppo_invent().setCd_categoria_gruppo(cd_categoria_gruppo);
	} 
	
	public void validate() throws ValidationException {
		super.validate();
		if ( elemento_voce == null || elemento_voce.getCd_elemento_voce() == null ||  elemento_voce.getCd_elemento_voce().equals("") )
			throw new ValidationException( "Il codice del Elemento Voce è obbligatorio." );
		if ( elemento_voce.getTi_appartenenza() == null || elemento_voce.getTi_appartenenza().equals(""))
			throw new ValidationException( "Il campo appartenenza è obbligatorio." );
		if ( elemento_voce.getTi_gestione() == null || elemento_voce.getTi_gestione().equals(""))
			throw new ValidationException( "Il campo gestione è obbligatorio." );
		if ( categoria_gruppo_invent.getCd_categoria_gruppo() == null || categoria_gruppo_invent.getCd_categoria_gruppo().equals(""))
			throw new ValidationException( "Il campo categoria/gruppo è obbligatorio." );
		if ( getOrdine() == null || getOrdine().equals(""))
			throw new ValidationException( "Il campo Ordine è obbligatorio." );
	}
}