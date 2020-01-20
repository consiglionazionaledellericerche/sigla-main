/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;

public class AbilUtenteUopOperMagBulk extends AbilUtenteUopOperMagBase {
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
	public AbilUtenteUopOperMagBulk(String cdUtente, String cdUnitaOperativa, String cdTipoOperazione, String cdCds, String cdMagazzino) {
		super(cdUtente, cdUnitaOperativa, cdTipoOperazione, cdCds, cdMagazzino);
		setAbilUtenteUopOperBulk( new AbilUtenteUopOperBulk(cdUtente, cdUnitaOperativa, cdTipoOperazione) );
		setMagazzino( new MagazzinoBulk(cdCds, cdMagazzino) );
	}
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
	public String getCdUtente() {
		AbilUtenteUopOperBulk utente = this.getAbilUtenteUopOperBulk();
		if (utente == null)
			return null;
		return getAbilUtenteUopOperBulk().getCdUtente();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUtente]
	 **/
	public void setCdUtente(String cdUtente)  {
		this.getAbilUtenteUopOperBulk().setCdUtente(cdUtente);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public String getCdUnitaOperativa() {
		AbilUtenteUopOperBulk unitaOperativaOrd = this.getAbilUtenteUopOperBulk();
		if (unitaOperativaOrd == null)
			return null;
		return getAbilUtenteUopOperBulk().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(String cdUnitaOperativa)  {
		this.getAbilUtenteUopOperBulk().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public String getCdTipoOperazione() {
		AbilUtenteUopOperBulk tipoOperazioneOrd = this.getAbilUtenteUopOperBulk();
		if (tipoOperazioneOrd == null)
			return null;
		return getAbilUtenteUopOperBulk().getCdTipoOperazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(String cdTipoOperazione)  {
		this.getAbilUtenteUopOperBulk().setCdTipoOperazione(cdTipoOperazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public String getCdCds() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getMagazzino().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public String getCdMagazzino() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(String cdMagazzino)  {
		this.getMagazzino().setCdMagazzino(cdMagazzino);
	}
}