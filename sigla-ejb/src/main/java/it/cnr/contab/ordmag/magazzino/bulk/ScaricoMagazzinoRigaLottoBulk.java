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
import java.util.Optional;

import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class ScaricoMagazzinoRigaLottoBulk extends OggettoBulk implements KeyedPersistent{
	private static final long serialVersionUID = 1L;

	private LottoMagBulk lottoMagazzino;
	private ScaricoMagazzinoRigaBulk scaricoMagazzinoRiga;

	//variabile utilizzata in fase di scarico manuale da magazzino
	private BigDecimal qtScarico = BigDecimal.ZERO;

	public LottoMagBulk getLottoMagazzino() {
		return lottoMagazzino;
	}
	
	public void setLottoMagazzino(LottoMagBulk lottoMagazzino) {
		this.lottoMagazzino = lottoMagazzino;
	}
	
	public ScaricoMagazzinoRigaBulk getScaricoMagazzinoRiga() {
		return scaricoMagazzinoRiga;
	}
	
	public void setScaricoMagazzinoRiga(ScaricoMagazzinoRigaBulk scaricoMagazzinoRiga) {
		this.scaricoMagazzinoRiga = scaricoMagazzinoRiga;
	}
	
	/*
	 * 	Ritorna la quantita da scaricare selezionata nella mappa di scarico manuale da magazzino
	 */
	public BigDecimal getQtScarico() {
		return qtScarico;
	}
	
	public void setQtScarico(BigDecimal qtScarico) {
		this.qtScarico = qtScarico;
	}

	public BigDecimal getQtScaricoConvertita() {
		return Utility.round5Decimali(Optional.ofNullable(this.getQtScarico()).orElse(BigDecimal.ZERO)
				.multiply(Optional.ofNullable(this.getScaricoMagazzinoRiga().getCoefConv()).orElse(BigDecimal.ZERO)));
	}
}