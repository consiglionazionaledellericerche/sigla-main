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

package it.cnr.contab.pagopa.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Dictionary;
import java.util.Optional;

public class PendenzaPagopaBulk extends PendenzaPagopaBase {
	private static final long serialVersionUID = 1L;

	public static final String TIPO_POSIZIONE_CREDITORIA = "C";
	public static final String TIPO_POSIZIONE_DEBITORIA = "D";
	public static final String STATO_VALIDO = "VAL";
	public static final String STATO_CHIUSO = "CHI";
	public static final String STATO_ANNULLATO = "ANN";
	private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce;
	private Unita_organizzativaBulk unitaOrganizzativa;
	protected TerzoBulk terzo;
	private TipoPendenzaPagopaBulk tipoPendenzaPagopa;
	private final static Dictionary statoKeys;
	static {
		statoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoKeys.put(STATO_VALIDO,"Valido");
		statoKeys.put(STATO_ANNULLATO,"Annullato");
		statoKeys.put(STATO_CHIUSO,"Chiuso");
	};

	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}

	public void setElemento_voce(Elemento_voceBulk elemento_voce) {
		this.elemento_voce = elemento_voce;
	}

	public final java.util.Dictionary getStatoKeys() {
		return statoKeys;
	}

	public PendenzaPagopaBulk() {
		super();
	}

	public PendenzaPagopaBulk(Long id) {
		super(id);
	}

	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}
	public java.lang.String getCd_elemento_voce() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
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

	public java.lang.Integer getEsercizioVoce() {
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getEsercizio();
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
	public void setEsercizioVoce(java.lang.Integer esercizioVoce) {
		this.getElemento_voce().setEsercizio(esercizioVoce);
	}
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
		setStato(STATO_VALIDO);
		setUnitaOrganizzativa(new Unita_organizzativaBulk());
		setCdUnitaOrganizzativa(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
		return super.initializeForInsert(bp,context);
	}

	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
		setUnitaOrganizzativa(new Unita_organizzativaBulk());
		setCdUnitaOrganizzativa(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
		return super.initializeForInsert(bp,context);
	}

	@Override
	public String getCdUnitaOrganizzativa() {
		return Optional.ofNullable(getUnitaOrganizzativa())
				.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
				.orElse(null);
	}
	
	@Override
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa) {
		Optional.ofNullable(getUnitaOrganizzativa()).ifPresent(el->el.setCd_unita_organizzativa(cdUnitaOrganizzativa));
	}
	
	public TipoPendenzaPagopaBulk getTipoPendenzaPagopa() {
		return tipoPendenzaPagopa;
	}

	public void setTipoPendenzaPagopa(TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk) {
		this.tipoPendenzaPagopa = tipoPendenzaPagopaBulk;
	}

	public TerzoBulk getTerzo() {
		return terzo;
	}

	public void setTerzo(TerzoBulk terzo) {
		this.terzo = terzo;
	}

	@Override
	public Integer getIdTipoPendenzaPagopa() {
		return Optional.ofNullable(getTipoPendenzaPagopa())
					.map(TipoPendenzaPagopaBulk::getId)
					.orElse(null);
	}
	
	@Override
	public void setIdTipoPendenzaPagopa(Integer idTipoPendenzaPagopa) {
		Optional.ofNullable(getTipoPendenzaPagopa()).ifPresent(el->el.setId(idTipoPendenzaPagopa));
	}
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		Optional.ofNullable(getTerzo()).ifPresent(el->el.setCd_terzo(cd_terzo));
	}
	public java.lang.Integer getCd_terzo() {
		return Optional.ofNullable(getTerzo())
				.map(TerzoBulk::getCd_terzo)
				.orElse(null);
	}
	public Boolean isPendenzaNonModificabile(){
		if (getStato() != null && (getStato().equals(STATO_ANNULLATO) || getStato().equals(STATO_CHIUSO))){
			return true;
		}
		return false;
	}

}