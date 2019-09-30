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
* Created by Generator 1.0
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_residuo_detBulk extends Pdg_residuo_detBase {
	protected CdrBulk            centro_responsabilita;
	protected CdrBulk            centro_responsabilita_la;
	protected WorkpackageBulk linea_attivita;
	protected Elemento_voceBulk  elemento_voce;

	private it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura;
	private it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione;

	protected java.util.Collection nature;
	protected java.util.Collection funzioni;

	public final static String STATO_INIZIALE = "I";
	public final static String STATO_ANNULLATO = "A";

	public final static java.util.Dictionary STATO;
	static {
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INIZIALE,"Iniziale");
		STATO.put(STATO_ANNULLATO,"Annullato");
	}

	private Pdg_residuoBulk pdg_residuo;

	public Pdg_residuo_detBulk() {
		super();
	}
	public Pdg_residuo_detBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_dettaglio);
		setPdg_residuo(new Pdg_residuoBulk(esercizio,cd_centro_responsabilita));
		setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	}

	/**
	 * Inizializza l'Oggetto Bulk.
	 * @param bp Il Business Process in uso
	 * @param context Il contesto dell'azione
	 * @return OggettoBulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)
	{
		setStato(STATO_INIZIALE);
		return this;
	}

	public boolean isROStato() {
		if (isToBeCreated()||(getStato()!=null&&getStato().equals(STATO_ANNULLATO)))
			return true;
		else
			return false;
	}

	public Pdg_residuoBulk getPdg_residuo() {
	   return pdg_residuo;
	}
	public void setPdg_residuo(Pdg_residuoBulk bulk) {
		pdg_residuo = bulk;
	}
	public java.lang.Integer getEsercizio() {
	   Pdg_residuoBulk residuo = this.getPdg_residuo();
	   if (residuo == null)
		   return null;
	   return residuo.getEsercizio();
	}
	public java.lang.String getCd_centro_responsabilita() {
	   Pdg_residuoBulk residuo = this.getPdg_residuo();
	   if (residuo == null)
		   return null;
	   return residuo.getCd_centro_responsabilita();
	}
	public java.lang.String getCd_elemento_voce() {
	   it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	   if (elemento_voce == null)
		   return null;
	   return elemento_voce.getCd_elemento_voce();
	}
	public java.lang.String getCd_funzione() {
	   it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione = this.getFunzione();
	   if (funzione == null)
		   return null;
	   return funzione.getCd_funzione();
	}
	public java.lang.String getCd_cdr_linea() {
		CdrBulk cdr = this.getCentro_responsabilita_la();
		if (cdr == null)
			return null;
		return cdr.getCd_centro_responsabilita();
	}
	public java.lang.String getCd_linea_attivita() {
	   it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	   if (linea_attivita == null)
		   return null;
	   return linea_attivita.getCd_linea_attivita();
	}
	public java.lang.String getCd_natura() {
	   it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura = this.getNatura();
	   if (natura == null)
		   return null;
	   return natura.getCd_natura();
	}
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
	   return centro_responsabilita;
	}
	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	public it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk getFunzione() {
		return funzione;
	}
	public java.util.Collection getFunzioni() {
		return funzioni;
	}
	public WorkpackageBulk getLinea_attivita() {
		return linea_attivita;
	}
	public it.cnr.contab.config00.pdcfin.bulk.NaturaBulk getNatura() {
		return natura;
	}
	public java.util.Collection getNature() {
		return nature;
	}
	public java.util.Dictionary getStatoKeys() {
		return STATO;
	}
	public java.lang.String getTi_appartenenza() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	public java.lang.String getTi_gestione() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		this.getPdg_residuo().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	public void setEsertcizio(java.lang.Integer esercizio) {
		this.getPdg_residuo().setEsercizio(esercizio);
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}
	public void setCd_funzione(java.lang.String cd_funzione) {
		this.getFunzione().setCd_funzione(cd_funzione);
	}
	public void setCd_cdr_linea(java.lang.String cd_cdr_linea) {
		this.getLinea_attivita().setCd_centro_responsabilita(cd_cdr_linea);
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
		this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
	}
	public void setCd_natura(java.lang.String cd_natura) {
		this.getNatura().setCd_natura(cd_natura);
	}
	public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita) {
		centro_responsabilita = newCentro_responsabilita;
	}
	public void setElemento_voce(Elemento_voceBulk newElemento_voce) {
		elemento_voce = newElemento_voce;
	}
	public void setFunzione(it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk newFunzione) {
		funzione = newFunzione;
	}
	public void setFunzioni(java.util.Collection newFunzioni) {
		funzioni = newFunzioni;
	}
	public void setLinea_attivita(WorkpackageBulk newLinea_attivita) {
		linea_attivita = newLinea_attivita;
	}
	public void setNatura(it.cnr.contab.config00.pdcfin.bulk.NaturaBulk newNatura) {
		natura = newNatura;
	}
	public void setNature(java.util.Collection newNature) {
		nature = newNature;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.getElemento_voce().setTi_gestione(ti_gestione);
	}
	public CdrBulk getCentro_responsabilita_la() {
		return centro_responsabilita_la;
	}
	public void setCentro_responsabilita_la(CdrBulk bulk) {
		centro_responsabilita_la = bulk;
	}
	public String getCd_progetto() {
		if (getLinea_attivita()==null)
			return null;
		if (getLinea_attivita().getProgettopadre()==null)
			return null;
		return getLinea_attivita().getProgettopadre().getCd_progetto();
	}
	public String getCd_dipartimento() {
		if (getLinea_attivita()==null)
			return null;
		if (getLinea_attivita().getProgettopadre()==null)
			return null;
		if (getLinea_attivita().getProgettopadre().getProgettopadre()==null)
			return null;
		return getLinea_attivita().getProgettopadre().getProgettopadre().getCd_dipartimento();
	}
	public String getCd_classificazione_spese() {
		if (getElemento_voce()==null)
			return null;
		return getElemento_voce().getCod_cla_s();
	}
	public String getDs_classificazione_spese() {
		if (getElemento_voce()==null)
			return null;
		if (getElemento_voce().getClassificazione_spese()==null)
			return null;
		return getElemento_voce().getClassificazione_spese().getDescrizione();
	}
}