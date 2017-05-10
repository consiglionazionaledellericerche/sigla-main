/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;

public class AbilUtenteUopOperMagBulk extends AbilUtenteUopOperMagBase {
	/**
	 * [UTENTE Contiene gli utenti dell'applicazione.]
	 **/
//	private UtenteBulk utente =  new UtenteBulk();
//	/**
//	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
//	 **/
//	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
//	/**
//	 * [TIPO_OPERAZIONE_ORD Rappresenta l'anagrafica dei tipi operazione degli ordini.]
//	 **/
//	private TipoOperazioneOrdBulk tipoOperazioneOrd =  new TipoOperazioneOrdBulk();
//	/**
//	 * [MAGAZZINO Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
//	 **/
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	private AbilUtenteUopOperBulk abilUtenteUopOperBulk =  new AbilUtenteUopOperBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABIL_UTENTE_UOP_OPER_MAG
	 **/
	public AbilUtenteUopOperMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABIL_UTENTE_UOP_OPER_MAG
	 **/
	public AbilUtenteUopOperMagBulk(java.lang.String cdUtente, java.lang.String cdUnitaOperativa, java.lang.String cdTipoOperazione, java.lang.String cdCds, java.lang.String cdMagazzino) {
		super(cdUtente, cdUnitaOperativa, cdTipoOperazione, cdCds, cdMagazzino);
		setAbilUtenteUopOperBulk( new AbilUtenteUopOperBulk(cdUtente, cdUnitaOperativa, cdTipoOperazione) );
		setMagazzino( new MagazzinoBulk(cdCds, cdMagazzino) );
	}
//	/**
//	 * Created by BulkGenerator 2.0 [07/12/2009]
//	 * Restituisce il valore di: [Contiene gli utenti dell'applicazione.]
//	 **/
//	public UtenteBulk getUtente() {
//		return utente;
//	}
//	/**
//	 * Created by BulkGenerator 2.0 [07/12/2009]
//	 * Setta il valore di: [Contiene gli utenti dell'applicazione.]
//	 **/
//	public void setUtente(UtenteBulk utente)  {
//		this.utente=utente;
//	}
//	/**
//	 * Created by BulkGenerator 2.0 [07/12/2009]
//	 * Restituisce il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
//	 **/
//	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
//		return unitaOperativaOrd;
//	}
//	/**
//	 * Created by BulkGenerator 2.0 [07/12/2009]
//	 * Setta il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
//	 **/
//	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
//		this.unitaOperativaOrd=unitaOperativaOrd;
//	}
//	/**
//	 * Created by BulkGenerator 2.0 [07/12/2009]
//	 * Restituisce il valore di: [Rappresenta l'anagrafica dei tipi operazione degli ordini.]
//	 **/
//	public TipoOperazioneOrdBulk getTipoOperazioneOrd() {
//		return tipoOperazioneOrd;
//	}
//	/**
//	 * Created by BulkGenerator 2.0 [07/12/2009]
//	 * Setta il valore di: [Rappresenta l'anagrafica dei tipi operazione degli ordini.]
//	 **/
//	public void setTipoOperazioneOrd(TipoOperazioneOrdBulk tipoOperazioneOrd)  {
//		this.tipoOperazioneOrd=tipoOperazioneOrd;
//	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public MagazzinoBulk getMagazzino() {
		return magazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public void setMagazzino(MagazzinoBulk magazzino)  {
		this.magazzino=magazzino;
	}
	public AbilUtenteUopOperBulk getAbilUtenteUopOperBulk() {
		return abilUtenteUopOperBulk;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public void setAbilUtenteUopOperBulk(AbilUtenteUopOperBulk abilUtenteUopOperBulk)  {
		this.abilUtenteUopOperBulk=abilUtenteUopOperBulk;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUtente]
	 **/
	public java.lang.String getCdUtente() {
		AbilUtenteUopOperBulk utente = this.getAbilUtenteUopOperBulk();
		if (utente == null)
			return null;
		return getAbilUtenteUopOperBulk().getCdUtente();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUtente]
	 **/
	public void setCdUtente(java.lang.String cdUtente)  {
		this.getAbilUtenteUopOperBulk().setCdUtente(cdUtente);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		AbilUtenteUopOperBulk unitaOperativaOrd = this.getAbilUtenteUopOperBulk();
		if (unitaOperativaOrd == null)
			return null;
		return getAbilUtenteUopOperBulk().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getAbilUtenteUopOperBulk().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public java.lang.String getCdTipoOperazione() {
		AbilUtenteUopOperBulk tipoOperazioneOrd = this.getAbilUtenteUopOperBulk();
		if (tipoOperazioneOrd == null)
			return null;
		return getAbilUtenteUopOperBulk().getCdTipoOperazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(java.lang.String cdTipoOperazione)  {
		this.getAbilUtenteUopOperBulk().setCdTipoOperazione(cdTipoOperazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getMagazzino().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getMagazzino().setCdMagazzino(cdMagazzino);
	}
}