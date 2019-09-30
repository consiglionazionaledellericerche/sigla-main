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
 * Date 11/12/2015
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Dictionary;
public class UtenteFirmaDettaglioBulk extends UtenteFirmaDettaglioBase {

	private static final long serialVersionUID = 1L;
	/**
	 * [UTENTE Contiene gli utenti dell'applicazione.]
	 **/
	private UtenteBulk utente =  new UtenteBulk();
	/**
	 * [UTENTE Contiene gli utenti dell'applicazione.]
	 **/
	private UtenteBulk delegato =  new UtenteBulk();

	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();

	public static final String TITOLO_FIRMA_DIRETTORE = "DIR", 
			TITOLO_FIRMA_SEGRETARIO_AMMINISTRATIVO = "SEG",
			TITOLO_FIRMA_DELEGATO = "DEL";

	public final static Dictionary<String, String> titoloKeys;
	static 
	{
		titoloKeys = new it.cnr.jada.util.OrderedHashtable();
		titoloKeys.put(TITOLO_FIRMA_DIRETTORE, "Direttore");
		titoloKeys.put(TITOLO_FIRMA_SEGRETARIO_AMMINISTRATIVO, "Segretario Amministrativo");
		titoloKeys.put(TITOLO_FIRMA_DELEGATO, "Delegato");
	};
	public final static Dictionary<String, String> abilitatoKeys;
	static 
	{
		abilitatoKeys = new it.cnr.jada.util.OrderedHashtable();
		abilitatoKeys.put(AbilitatoFirma.DOCCONT.name(), "Documenti Contabili");
		abilitatoKeys.put(AbilitatoFirma.DOC_1210.name(), "Documenti 1210");
		abilitatoKeys.put(AbilitatoFirma.VAR.name(), "Variazioni");
		abilitatoKeys.put(AbilitatoFirma.FAT.name(), "Fatture");
		abilitatoKeys.put(AbilitatoFirma.DIST.name(), "Distinte");
	};
	
	public static Dictionary<String, String> getTitolokeys() {
		return titoloKeys;
	}
	
	public static Dictionary<String, String> getAbilitatokeys() {
		return abilitatoKeys;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UTENTE_FIRMA_DETTAGLIO
	 **/
	public UtenteFirmaDettaglioBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UTENTE_FIRMA_DETTAGLIO
	 **/
	public UtenteFirmaDettaglioBulk(java.lang.String cdUtente, java.lang.String cdUnitaOrganizzativa) {
		super(cdUtente, cdUnitaOrganizzativa);
		setUtente( new UtenteBulk(cdUtente) );
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdUnitaOrganizzativa) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Contiene gli utenti dell'applicazione.]
	 **/
	public UtenteBulk getUtente() {
		return utente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Contiene gli utenti dell'applicazione.]
	 **/
	public void setUtente(UtenteBulk utente)  {
		this.utente=utente;
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
	 * Restituisce il valore di: [null]
	 **/
	public java.lang.String getCdUtente() {
		UtenteBulk utente = this.getUtente();
		if (utente == null)
			return null;
		return getUtente().getCd_utente();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setCdUtente(java.lang.String cdUtente)  {
		this.getUtente().setCd_utente(cdUtente);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public java.lang.String getDelegatoDa() {
		UtenteBulk utente = this.getDelegato();
		if (utente == null)
			return null;
		return getDelegato().getCd_utente();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setDelegatoDa(java.lang.String delegatoDa)  {
		this.getDelegato().setCd_utente(delegatoDa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}
	public UtenteBulk getDelegato() {
		return delegato;
	}
	public void setDelegato(UtenteBulk delegato) {
		this.delegato = delegato;
	}
	
	public String getDisplayFunzioniAbilitate() {
		String result = "";
		if (getFunzioniAbilitate() == null || getFunzioniAbilitate().isEmpty())
			return null;
		for (String key : getFunzioniAbilitate()) {
			result = result.concat(abilitatoKeys.get(key)).concat("<br>");
		}
		return result;
	}
	
	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (getTitoloFirma() != null && getTitoloFirma().equalsIgnoreCase(TITOLO_FIRMA_DELEGATO) && getDelegatoDa() == null){
			throw new ValidationException("Valorizzare il delegato!");
		}
	}
}