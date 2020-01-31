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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkList;

public class CaricoMagazzinoRigaBulk extends MovimentiMagazzinoRigaBulk{
	private static final long serialVersionUID = 1L;

	private TerzoBulk terzo = new TerzoBulk();
	private String numeroBolla;
	private Timestamp dataBolla;
	private String lottoFornitore;
	private Timestamp dtScadenza;
	private BigDecimal prezzoUnitario;
	private Voce_ivaBulk voceIva;
	private List<LottoMagBulk> lottoColl = new BulkList<LottoMagBulk>();

	public CaricoMagazzinoRigaBulk() {
		super();
	}
	
	public BigDecimal getTotGiacenzaLotti() {
		return Optional.ofNullable(this.getLottoColl())
				.flatMap(e->e.stream().map(LottoMagBulk::getGiacenza)
									  .reduce((x, y)->x.add(y)))
				.orElse(BigDecimal.ZERO);
	}


	
	public BigDecimal getQtCaricoConvertita() {
		return Utility.round5Decimali(Optional.ofNullable(this.getQuantita()).orElse(BigDecimal.ZERO)
				.multiply(Optional.ofNullable(this.getCoefConv()).orElse(BigDecimal.ZERO)));
	}
	
	public boolean isROCoefConv(){
		return !Optional.ofNullable(this.getUnitaMisura())
				.map(UnitaMisuraBulk::getCdUnitaMisura)
				.filter(cdUM->!Optional.ofNullable(this.getBeneServizio())
								.map(Bene_servizioBulk::getUnitaMisura)
								.filter(umBene->umBene.getCdUnitaMisura().equals(cdUM))
								.isPresent()
				)
				.isPresent();
	}

	public TerzoBulk getTerzo() {
		return terzo;
	}

	public void setTerzo(TerzoBulk terzo) {
		this.terzo = terzo;
	}

	public String getNumeroBolla() {
		return numeroBolla;
	}

	public void setNumeroBolla(String numeroBolla) {
		this.numeroBolla = numeroBolla;
	}

	public Timestamp getDataBolla() {
		return dataBolla;
	}

	public void setDataBolla(Timestamp dataBolla) {
		this.dataBolla = dataBolla;
	}

	public String getLottoFornitore() {
		return lottoFornitore;
	}

	public void setLottoFornitore(String lottoFornitore) {
		this.lottoFornitore = lottoFornitore;
	}

	public BigDecimal getPrezzoUnitario() {
		return prezzoUnitario;
	}

	public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}

	public List<LottoMagBulk> getLottoColl() {
		return lottoColl;
	}

	public void setLottoColl(List<LottoMagBulk> lottoColl) {
		this.lottoColl = lottoColl;
	}

	public Voce_ivaBulk getVoceIva() {
		return voceIva;
	}

	public void setVoceIva(Voce_ivaBulk voceIva) {
		this.voceIva = voceIva;
	}

	public Timestamp getDtScadenza() {
		return dtScadenza;
	}

	public void setDtScadenza(Timestamp dtScadenza) {
		this.dtScadenza = dtScadenza;
	}
}