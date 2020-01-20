/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.bulk.BulkCollection;

import java.util.Iterator;
import java.util.List;

public class AbilUtenteUopOperBulk extends AbilUtenteUopOperBase {
	/**
	 * [UTENTE Contiene gli utenti dell'applicazione.]
	 **/
	private UtenteBulk utente =  new UtenteBulk();
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	/**
	 * [TIPO_OPERAZIONE_ORD Rappresenta l'anagrafica dei tipi operazione degli ordini.]
	 **/
	private TipoOperazioneOrdBulk tipoOperazioneOrd =  new TipoOperazioneOrdBulk();


	protected it.cnr.jada.bulk.BulkList<AbilUtenteUopOperMagBulk>	utente_abil_magazzini= new it.cnr.jada.bulk.BulkList<AbilUtenteUopOperMagBulk>();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABIL_UTENTE_UOP_OPER
	 **/
	public AbilUtenteUopOperBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABIL_UTENTE_UOP_OPER
	 **/
	public AbilUtenteUopOperBulk(String cdUtente, String cdUnitaOperativa, String cdTipoOperazione) {
		super(cdUtente, cdUnitaOperativa, cdTipoOperazione);
		setUtente( new UtenteBulk(cdUtente) );
		setUnitaOperativaOrd( new UnitaOperativaOrdBulk(cdUnitaOperativa) );
		setTipoOperazioneOrd( new TipoOperazioneOrdBulk(cdTipoOperazione) );
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
	 * Restituisce il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
		this.unitaOperativaOrd=unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta l'anagrafica dei tipi operazione degli ordini.]
	 **/
	public TipoOperazioneOrdBulk getTipoOperazioneOrd() {
		return tipoOperazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'anagrafica dei tipi operazione degli ordini.]
	 **/
	public void setTipoOperazioneOrd(TipoOperazioneOrdBulk tipoOperazioneOrd)  {
		this.tipoOperazioneOrd=tipoOperazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUtente]
	 **/
	public String getCdUtente() {
		UtenteBulk utente = this.getUtente();
		if (utente == null)
			return null;
		return getUtente().getCd_utente();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUtente]
	 **/
	public void setCdUtente(String cdUtente)  {
		this.getUtente().setCd_utente(cdUtente);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public String getCdUnitaOperativa() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(String cdUnitaOperativa)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public String getCdTipoOperazione() {
		TipoOperazioneOrdBulk tipoOperazioneOrd = this.getTipoOperazioneOrd();
		if (tipoOperazioneOrd == null)
			return null;
		return getTipoOperazioneOrd().getCdTipoOperazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(String cdTipoOperazione)  {
		this.getTipoOperazioneOrd().setCdTipoOperazione(cdTipoOperazione);
	}
	public it.cnr.jada.bulk.BulkList getUtente_abil_magazzini() {
		return utente_abil_magazzini;
	}
	public void setUtente_abil_magazzini(it.cnr.jada.bulk.BulkList utente_abil_magazzini) {
		this.utente_abil_magazzini = utente_abil_magazzini;
	}
	public int addToUtente_abil_magazzini(AbilUtenteUopOperMagBulk dett) {
		//dett.setUtente(this);
		dett.setAbilUtenteUopOperBulk(this);
		getUtente_abil_magazzini().add(dett);
		return getUtente_abil_magazzini().size()-1;
	}
	public AbilUtenteUopOperMagBulk removeFromUtente_abil_magazzini(int index) {
		AbilUtenteUopOperMagBulk dett = (AbilUtenteUopOperMagBulk)getUtente_abil_magazzini().remove(index);
		return dett;
	}

	public BulkCollection[] getBulkLists() {

		// Metti solo le liste di oggetti che devono essere resi persistenti

		return new it.cnr.jada.bulk.BulkCollection[] {
				utente_abil_magazzini
		};
	}



	public AbilUtenteUopOperMagBulk removeFromUtenteAbilMgazzini(int index)
	{
		// Gestisce la selezione del bottone cancella repertorio
		AbilUtenteUopOperMagBulk abilUteMag = (AbilUtenteUopOperMagBulk)utente_abil_magazzini.remove(index);
		//utente_abil_magazzini.setToBeDeleted();
		return abilUteMag;
	}


}