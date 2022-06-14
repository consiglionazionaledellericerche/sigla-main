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
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;
import java.util.Optional;

public class PendenzaPagopaBulk extends PendenzaPagopaBase {
	private static final long serialVersionUID = 1L;

	public static final String TIPO_POSIZIONE_CREDITORIA = "C";
	public static final String TIPO_POSIZIONE_DEBITORIA = "D";
	public static final String STATO_APERTA = "APE";
	public static final String STATO_CHIUSO = "CHI";
	public static final String STATO_INCASSATA = "INC";
	public static final String STATO_IN_PAGAMENTO = "INP";
	public static final String STATO_RISCOSSA = "RIS";
	public static final String STATO_ANNULLATO = "ANN";
	private Elemento_voceBulk elemento_voce;
	private Unita_organizzativaBulk unitaOrganizzativa;
	protected TerzoBulk terzo;
	private TipoPendenzaPagopaBulk tipoPendenzaPagopa;
	private final static Dictionary statoKeys;
	static {
		statoKeys = new OrderedHashtable();
		statoKeys.put(STATO_APERTA,"Aperta");
		statoKeys.put(STATO_INCASSATA,"Incassata");
		statoKeys.put(STATO_RISCOSSA,"Riscossa");
		statoKeys.put(STATO_IN_PAGAMENTO,"In Pagamento");
		statoKeys.put(STATO_ANNULLATO,"Annullata");
		statoKeys.put(STATO_CHIUSO,"Chiusa");
	};

	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}

	public void setElemento_voce(Elemento_voceBulk elemento_voce) {
		this.elemento_voce = elemento_voce;
	}

	public final Dictionary getStatoKeys() {
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
	public String getCd_elemento_voce() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getCd_elemento_voce();
	}
	public String getTi_appartenenza() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_appartenenza();
	}
	public String getTi_gestione() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getTi_gestione();
	}

	public Integer getEsercizioVoce() {
		Elemento_voceBulk elemento_voce = this.getElemento_voce();
		if (elemento_voce == null)
			return null;
		return elemento_voce.getEsercizio();
	}
	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}
	public void setTi_appartenenza(String ti_appartenenza) {
		this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	public void setTi_gestione(String ti_gestione) {
		this.getElemento_voce().setTi_gestione(ti_gestione);
	}
	public void setEsercizioVoce(Integer esercizioVoce) {
		this.getElemento_voce().setEsercizio(esercizioVoce);
	}
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
		setStato(STATO_APERTA);
		setUnitaOrganizzativa(new Unita_organizzativaBulk());
		setCdUnitaOrganizzativa(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
		return super.initializeForInsert(bp,context);
	}

	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (!Tipo_unita_organizzativaHome.TIPO_UO_ENTE.equalsIgnoreCase(uo.getCd_tipo_unita())){
			setUnitaOrganizzativa(new Unita_organizzativaBulk());
			setCdUnitaOrganizzativa(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
		}

		return super.initializeForSearch(bp,context);
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
	public void setCd_terzo(Integer cd_terzo) {
		Optional.ofNullable(getTerzo()).ifPresent(el->el.setCd_terzo(cd_terzo));
	}
	public Integer getCd_terzo() {
		return Optional.ofNullable(getTerzo())
				.map(TerzoBulk::getCd_terzo)
				.orElse(null);
	}
	public Boolean isPendenzaNonModificabile(){
		if (getStato() != null && (getStato().equals(STATO_IN_PAGAMENTO) || getStato().equals(STATO_RISCOSSA) || getStato().equals(STATO_CHIUSO) || getStato().equals(STATO_INCASSATA))){
			return true;
		}
		return false;
	}

	public Boolean isPendenzaAnnullata(){
		if (getStato() != null && (getStato().equals(STATO_ANNULLATO))){
			return true;
		}
		return false;
	}

	public Boolean isPendenzaRiscossa(){
		if (getStato() != null && (getStato().equals(STATO_RISCOSSA))){
			return true;
		}
		return false;
	}

	public Boolean isPendenzaIncassata(){
		if (getStato() != null && (getStato().equals(STATO_INCASSATA))){
			return true;
		}
		return false;
	}

	public Boolean isPendenzaAperta(){
		if (getStato() != null && (getStato().equals(STATO_APERTA))){
			return true;
		}
		return false;
	}

	public Dictionary getStatoKeysForSearch() {

		OrderedHashtable d = (OrderedHashtable)getStatoKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}
	public Dictionary getStatoKeysForUpdate() {
		Dictionary stato = new OrderedHashtable();
		if (isPendenzaAperta()){
			stato.put(STATO_APERTA,"Aperta");
			stato.put(STATO_ANNULLATO,"Annullata");
		} else {
			stato.put(STATO_APERTA,"Aperta");
			stato.put(STATO_INCASSATA,"Incassata");
			stato.put(STATO_RISCOSSA,"Riscossa");
			stato.put(STATO_IN_PAGAMENTO,"In Pagamento");
			stato.put(STATO_ANNULLATO,"Annullata");
			stato.put(STATO_CHIUSO,"Chiusa");
		}
		return stato;
	}
}