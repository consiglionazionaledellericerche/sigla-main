/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkList;

public class ScaricoMagazzinoRigaBulk extends MovimentiMagazzinoRigaBulk{
	private static final long serialVersionUID = 1L;

	private UnitaOperativaOrdBulk unitaOperativaRicevente = new UnitaOperativaOrdBulk();
	private List<ScaricoMagazzinoRigaLottoBulk> scaricoMagazzinoRigaLottoColl = new BulkList<ScaricoMagazzinoRigaLottoBulk>();

	public ScaricoMagazzinoRigaBulk() {
		super();
	}

	public UnitaOperativaOrdBulk getUnitaOperativaRicevente() {
		return unitaOperativaRicevente;
	}
	
	public void setUnitaOperativaRicevente(UnitaOperativaOrdBulk unitaOperativaRicevente) {
		this.unitaOperativaRicevente = unitaOperativaRicevente;
	}
	
	public List<ScaricoMagazzinoRigaLottoBulk> getScaricoMagazzinoRigaLottoColl() {
		return scaricoMagazzinoRigaLottoColl;
	}
	
	public void setScaricoMagazzinoRigaLottoColl(List<ScaricoMagazzinoRigaLottoBulk> scaricoMagazzinoRigaLottoColl) {
		this.scaricoMagazzinoRigaLottoColl = scaricoMagazzinoRigaLottoColl;
	}
	
	
	public BigDecimal getQtScaricoConvertita() {
		return Utility.round5Decimali(Optional.ofNullable(this.getQuantita()).orElse(BigDecimal.ZERO)
				.multiply(Optional.ofNullable(this.getCoefConv()).orElse(BigDecimal.ZERO)));
	}
	
	public BigDecimal getTotGiacenzaLotti() {
		return Optional.ofNullable(this.getScaricoMagazzinoRigaLottoColl())
				.flatMap(e->e.stream().map(ScaricoMagazzinoRigaLottoBulk::getLottoMagazzino)
									  .map(LottoMagBulk::getGiacenza)
									  .reduce((x, y)->x.add(y)))
				.orElse(BigDecimal.ZERO);
	}

	public BigDecimal getTotQtScaricoLotti() {
		return Optional.ofNullable(this.getScaricoMagazzinoRigaLottoColl())
				.flatMap(e->e.stream().map(ScaricoMagazzinoRigaLottoBulk::getQtScarico)
									  .reduce((x, y)->x.add(y)))
				.orElse(BigDecimal.ZERO);
	}
	
	public BigDecimal getTotQtScaricoLottiConvertita() {
		return Optional.ofNullable(this.getScaricoMagazzinoRigaLottoColl())
				.flatMap(e->e.stream().map(ScaricoMagazzinoRigaLottoBulk::getQtScaricoConvertita)
									  .reduce((x, y)->x.add(y)))
				.orElse(BigDecimal.ZERO);
	}

	public boolean isImputazioneScaricoSuLottiEnable() {
		return Optional.ofNullable(this.getQuantita()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==0;
	}

	public boolean isImputazioneScaricoSuBeneEnable() {
		return getTotQtScaricoLotti().compareTo(BigDecimal.ZERO)==0;
	}
	
}