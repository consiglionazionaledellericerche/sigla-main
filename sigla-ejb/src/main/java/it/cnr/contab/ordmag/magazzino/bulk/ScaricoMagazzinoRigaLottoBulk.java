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