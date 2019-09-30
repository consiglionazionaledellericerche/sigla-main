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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import java.math.BigDecimal;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
public class LimiteSpesaBulk extends LimiteSpesaBase {
	/**
	 * [ELEMENTO_VOCE Contiene l'anagrafica dei capitoli.
Tale anagrafica viene utilizzata sia per il PDC Finanziario CNR che per quello CDS.
In parte questa tabella risulta precaricata con dati cablati.

Per parte spese CDS:
Parte I e Titolo cablati
Il Capitolo viene aggiunto dall"utente

Per parte entrate CDS:
Titolo cablato.
Il Capitolo viene aggiunto dall"utente

Parte spese CNR:
Parte I e Titolo cablati
Capitolo definito dall"utente per la categoria 2 (SAC)

Parte entrate CNR:
Titolo cablato
Categoria definita dall"utente collegata a Titolo
Capitolo definito dall"utente collegato a Categoria

]
	 **/
	private Elemento_voceBulk elementoVoce =  new Elemento_voceBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA
	 **/
	public LimiteSpesaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA
	 **/
	public LimiteSpesaBulk(java.lang.Integer esercizio, java.lang.Integer esercizio_voce, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, esercizio_voce, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
		setElementoVoce( new Elemento_voceBulk(cdElementoVoce,esercizio_voce,tiAppartenenza,tiGestione) );
	}
	public static final java.util.Dictionary fonteKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String FONTE_INTERNA = "FIN";
	final public static String FONTE_ESTERNA = "FES";
	final public static String FONTE_INTERNA_E_ESTERNA = "*";
	
	static {
		fonteKeys.put(FONTE_INTERNA,"Interna");
		fonteKeys.put(FONTE_ESTERNA,"Esterna");
		fonteKeys.put(FONTE_INTERNA_E_ESTERNA,"Entrambi");
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Contiene l'anagrafica dei capitoli.
Tale anagrafica viene utilizzata sia per il PDC Finanziario CNR che per quello CDS.
In parte questa tabella risulta precaricata con dati cablati.

Per parte spese CDS:
Parte I e Titolo cablati
Il Capitolo viene aggiunto dall"utente

Per parte entrate CDS:
Titolo cablato.
Il Capitolo viene aggiunto dall"utente

Parte spese CNR:
Parte I e Titolo cablati
Capitolo definito dall"utente per la categoria 2 (SAC)

Parte entrate CNR:
Titolo cablato
Categoria definita dall"utente collegata a Titolo
Capitolo definito dall"utente collegato a Categoria

]
	 **/
	public Elemento_voceBulk getElementoVoce() {
		return elementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Contiene l'anagrafica dei capitoli.
Tale anagrafica viene utilizzata sia per il PDC Finanziario CNR che per quello CDS.
In parte questa tabella risulta precaricata con dati cablati.

Per parte spese CDS:
Parte I e Titolo cablati
Il Capitolo viene aggiunto dall"utente

Per parte entrate CDS:
Titolo cablato.
Il Capitolo viene aggiunto dall"utente

Parte spese CNR:
Parte I e Titolo cablati
Capitolo definito dall"utente per la categoria 2 (SAC)

Parte entrate CNR:
Titolo cablato
Categoria definita dall"utente collegata a Titolo
Capitolo definito dall"utente collegato a Categoria

]
	 **/
	public void setElementoVoce(Elemento_voceBulk elementoVoce)  {
		this.elementoVoce=elementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	@Override
	public java.lang.Integer getEsercizio_voce() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	@Override
	public void setEsercizio_voce(java.lang.Integer esercizio_voce)  {
		this.getElementoVoce().setEsercizio(esercizio_voce);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiAppartenenza]
	 **/
	public java.lang.String getTi_appartenenza() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getTi_appartenenza();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiAppartenenza]
	 **/
	public void setTi_appartenenza(java.lang.String tiAppartenenza)  {
		this.getElementoVoce().setTi_appartenenza(tiAppartenenza);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiGestione]
	 **/
	public java.lang.String getTi_gestione() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getTi_gestione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiGestione]
	 **/
	public void setTi_gestione(java.lang.String tiGestione)  {
		this.getElementoVoce().setTi_gestione(tiGestione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdElementoVoce]
	 **/
	public java.lang.String getCd_elemento_voce() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getCd_elemento_voce();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdElementoVoce]
	 **/
	public void setCd_elemento_voce(java.lang.String cdElementoVoce)  {
		this.getElementoVoce().setCd_elemento_voce(cdElementoVoce);
	}
	
	private SimpleBulkList dettagli= new BulkList();
	public BulkCollection[] getBulkLists() {
		// Metti solo le liste di oggetti che devono essere resi persistenti
		 return new it.cnr.jada.bulk.BulkCollection[] { this.getDettagli() };
	}
	
	public SimpleBulkList getDettagli() {
		return dettagli;
	}
	public void setDettagli(SimpleBulkList dettagli) {
		this.dettagli = dettagli;
	}
	public int addToDettagli (LimiteSpesaDetBulk nuovo)
	{
		dettagli.add(nuovo);
		nuovo.setCds(new CdsBulk());
		nuovo.setLimiteSpesa(this);
		nuovo.setImpegni_assunti(BigDecimal.ZERO);
		nuovo.setImporto_limite(BigDecimal.ZERO);
		return dettagli.size()-1;
	}
	
	public LimiteSpesaDetBulk removeFromDettagli( int indiceDiLinea ) {
		LimiteSpesaDetBulk element = (LimiteSpesaDetBulk)dettagli.get(indiceDiLinea);
		element = (LimiteSpesaDetBulk)dettagli.remove(indiceDiLinea);
		return element;
	}
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp,
			ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeForInsert(crudbp, actioncontext);
	}
	@Override
		public OggettoBulk initializeForSearch(CRUDBP crudbp,
				ActionContext actioncontext) {
			setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			return super.initializeForSearch(crudbp, actioncontext);
		}
	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (getCd_elemento_voce()==null)
			throw new ValidationException("Il campo voce è obbligatorio.");
		if (getFonte()==null)
			throw new ValidationException("Il campo fonte è obbligatorio.");
		if (getImporto_limite()==null)
			throw new ValidationException("Il campo importo limite è obbligatorio.");
		
	}
}