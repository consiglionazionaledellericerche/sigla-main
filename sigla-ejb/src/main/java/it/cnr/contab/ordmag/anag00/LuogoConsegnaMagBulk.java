/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
ù * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.OggettoBulk;
public class LuogoConsegnaMagBulk extends LuogoConsegnaMagBase {
	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	/**
	 * [COMUNE Codifica dei comuni italiani e delle città estere.E' definito un dialogo utente per popolare le città estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	private ComuneBulk comuneItaliano =  new ComuneBulk();
	/**
	 * [NAZIONE Codifica delle nazioni. Non è stato definito un dialogo utente sulla tabella ma si prevede il recupero in sede di migrazione]
	 **/
	private NazioneBulk nazione =  new NazioneBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LUOGO_CONSEGNA_MAG
	 **/
	public LuogoConsegnaMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LUOGO_CONSEGNA_MAG
	 **/
	public LuogoConsegnaMagBulk(String cdCds, String cdLuogoConsegna) {
		super(cdCds, cdLuogoConsegna);
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdCds) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa)  {
		this.unitaOrganizzativa=unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codifica dei comuni italiani e delle città estere.E' definito un dialogo utente per popolare le città estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public ComuneBulk getComuneItaliano() {
		return comuneItaliano;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codifica dei comuni italiani e delle città estere.E' definito un dialogo utente per popolare le città estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public void setComuneItaliano(ComuneBulk comuneItaliano)  {
		this.comuneItaliano=comuneItaliano;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codifica delle nazioni. Non è stato definito un dialogo utente sulla tabella ma si prevede il recupero in sede di migrazione]
	 **/
	public NazioneBulk getNazione() {
		return nazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codifica delle nazioni. Non è stato definito un dialogo utente sulla tabella ma si prevede il recupero in sede di migrazione]
	 **/
	public void setNazione(NazioneBulk nazione)  {
		this.nazione=nazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public String getCdCds() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgComune]
	 **/
	public Long getPgComune() {
		ComuneBulk comune = this.getComuneItaliano();
		if (comune == null)
			return null;
		return getComuneItaliano().getPg_comune();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgComune]
	 **/
	public void setPgComune(Long pgComune)  {
		this.getComuneItaliano().setPg_comune(pgComune);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgNazione]
	 **/
	public Long getPgNazione() {
		NazioneBulk nazione = this.getNazione();
		if (nazione == null)
			return null;
		return getNazione().getPg_nazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgNazione]
	 **/
	public void setPgNazione(Long pgNazione)  {
		this.getNazione().setPg_nazione(pgNazione);
	}
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
		return super.initialize(bp,context);
	}
}