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
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
public class Blt_accordiBulk extends Blt_accordiBase {

	private Elemento_voceBulk elemento_voce;

	private TerzoBulk responsabileIta;

	private NazioneBulk nazioneStr;

	private DivisaBulk divisaIta;

	public TerzoBulk terzoEnte;

	private BulkList blt_progettiColl = new BulkList();

	public Blt_accordiBulk() {
		super();
	}
	public Blt_accordiBulk(java.lang.String cd_accordo) {
		super(cd_accordo);
	}

	public Elemento_voceBulk getElemento_voce() {
		return elemento_voce;
	}
	
	public void setElemento_voce(Elemento_voceBulk elemento_voce) {
		this.elemento_voce = elemento_voce;
	}
	
	@Override
	public Integer getEsercizio() {
		if (this.getElemento_voce()!=null)
			return this.getElemento_voce().getEsercizio();
		return null;
	}
	@Override
	public void setEsercizio(Integer esercizio) {
		if (this.getElemento_voce()!=null)
			this.getElemento_voce().setEsercizio(esercizio);
	}
	@Override
	public String getTi_appartenenza() {
		if (this.getElemento_voce()!=null)
			return this.getElemento_voce().getTi_appartenenza();
		return null;
	}
	@Override
	public void setTi_appartenenza(String ti_appartenenza) {
		if (this.getElemento_voce()!=null)
			this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
	}
	@Override
	public String getTi_gestione() {
		if (this.getElemento_voce()!=null)
			return this.getElemento_voce().getTi_gestione();
		return null;
	}
	@Override
	public void setTi_gestione(String ti_gestione) {
		if (this.getElemento_voce()!=null)
			this.getElemento_voce().setTi_gestione(ti_gestione);
	}
	@Override
	public String getCd_elemento_voce() {
		if (this.getElemento_voce()!=null)
			return this.getElemento_voce().getCd_elemento_voce();
		return null;
	}
	@Override
	public void setCd_elemento_voce(String cd_elemento_voce) {
		if (this.getElemento_voce()!=null)
			this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
	}
	public TerzoBulk getResponsabileIta() {
		return responsabileIta;
	}
	public void setResponsabileIta(TerzoBulk responsabileIta) {
		this.responsabileIta = responsabileIta;
	}
	@Override
	public Integer getCd_respons_ita() {
		if (this.getResponsabileIta()!=null)
			return this.getResponsabileIta().getCd_terzo();
		return null;
	}
	@Override
	public void setCd_respons_ita(Integer cd_respons_ita) {
		if (this.getResponsabileIta()!=null)
			this.getResponsabileIta().setCd_terzo(cd_respons_ita);
	}
	public NazioneBulk getNazioneStr() {
		return nazioneStr;
	}
	public void setNazioneStr(NazioneBulk nazioneStr) {
		this.nazioneStr = nazioneStr;
	}
	@Override
	public Long getPg_nazione_str() {
		if (this.getNazioneStr()!=null)
			return this.getNazioneStr().getPg_nazione();
		return null;
	}
	@Override
	public void setPg_nazione_str(Long pg_nazione_str) {
		if (this.getNazioneStr()!=null)
			this.getNazioneStr().setPg_nazione(pg_nazione_str);
	}
	
	public String getDs_accordo() {
		return getAcronimo_ente_str()!=null?"C.N.R. - "+getAcronimo_ente_str():"";
	}
	
	public BulkList getBlt_progettiColl() {
		return blt_progettiColl;
	}
	public void setBlt_progettiColl(BulkList blt_progettiColl) {
		this.blt_progettiColl = blt_progettiColl;
	}
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				getBlt_progettiColl() };
	}
	public int addToBlt_progettiColl(Blt_progettiBulk dett) {
		dett.setBltAccordo(this);
		dett.setFl_associato_respons_ita(Boolean.FALSE);
		getBlt_progettiColl().add(dett);
		return getBlt_progettiColl().size()-1;
	}	
	public Blt_progettiBulk removeFromBlt_progettiColl(int index) {
		Blt_progettiBulk dett = (Blt_progettiBulk)getBlt_progettiColl().remove(index);
		return dett;
	}
	public DivisaBulk getDivisaIta() {
		return divisaIta;
	}
	public void setDivisaIta(DivisaBulk divisaIta) {
		this.divisaIta = divisaIta;
	}
	@Override
	public String getCd_divisa_ita() {
		if (this.getDivisaIta()!=null)
			return this.getDivisaIta().getCd_divisa();
		return null;
	}
	@Override
	public void setCd_divisa_ita(String cd_divisa_ita) {
		this.getDivisaIta().setCd_divisa(cd_divisa_ita);
	}
	
	public TerzoBulk getTerzoEnte() {
		return terzoEnte;
	}
	public void setTerzoEnte(TerzoBulk terzoEnte) {
		this.terzoEnte = terzoEnte;
	}
	@Override
	public Integer getCd_terzo_ente() {
		if (this.getTerzoEnte()!=null)
			return this.getTerzoEnte().getCd_terzo();
		return null;
	}
	@Override
	public void setCd_terzo_ente(Integer cd_terzo_ente) {
		this.getTerzoEnte().setCd_terzo(cd_terzo_ente);
	}
}