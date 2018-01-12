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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class ScaricoMagazzinoRigaBulk extends OggettoBulk implements KeyedPersistent{
	private static final long serialVersionUID = 1L;

	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	private UnitaOperativaOrdBulk unitaOperativaRicevente = new UnitaOperativaOrdBulk();
	private UnitaMisuraBulk unitaMisura = new UnitaMisuraBulk();	
	private ScaricoMagazzinoBulk scaricoMagazzino;
	private java.math.BigDecimal coefConv;
	private java.math.BigDecimal qtScarico;
	private List<ScaricoMagazzinoRigaLottoBulk> scaricoMagazzinoRigaLottoColl = new BulkList<ScaricoMagazzinoRigaLottoBulk>();
	private String anomalia;

	public ScaricoMagazzinoRigaBulk() {
		super();
	}

	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	
	public void setBeneServizio(Bene_servizioBulk beneServizio) {
		this.beneServizio = beneServizio;
	}
	
	public UnitaOperativaOrdBulk getUnitaOperativaRicevente() {
		return unitaOperativaRicevente;
	}
	
	public void setUnitaOperativaRicevente(UnitaOperativaOrdBulk unitaOperativaRicevente) {
		this.unitaOperativaRicevente = unitaOperativaRicevente;
	}
	
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura) {
		this.unitaMisura = unitaMisura;
	}
	
	public java.math.BigDecimal getCoefConv() {
		return coefConv;
	}
	
	public void setCoefConv(java.math.BigDecimal coefConv) {
		this.coefConv = coefConv;
	}

	public java.math.BigDecimal getQtScarico() {
		return qtScarico;
	}
	
	public void setQtScarico(java.math.BigDecimal qtScarico) {
		this.qtScarico = qtScarico;
	}
	
	public List<ScaricoMagazzinoRigaLottoBulk> getScaricoMagazzinoRigaLottoColl() {
		return scaricoMagazzinoRigaLottoColl;
	}
	
	public void setScaricoMagazzinoRigaLottoColl(List<ScaricoMagazzinoRigaLottoBulk> scaricoMagazzinoRigaLottoColl) {
		this.scaricoMagazzinoRigaLottoColl = scaricoMagazzinoRigaLottoColl;
	}
	
	public ScaricoMagazzinoBulk getScaricoMagazzino() {
		return scaricoMagazzino;
	}
	
	public void setScaricoMagazzino(ScaricoMagazzinoBulk scaricoMagazzino) {
		this.scaricoMagazzino = scaricoMagazzino;
	}
	
	public String getAnomalia() {
		return anomalia;
	}
	
	public void setAnomalia(String anomalia) {
		this.anomalia = anomalia;
	}

	public java.math.BigDecimal getQtScaricoConvertita() {
		return Utility.round5Decimali(Optional.ofNullable(this.getQtScarico()).orElse(BigDecimal.ZERO)
				.multiply(Optional.ofNullable(this.getCoefConv()).orElse(BigDecimal.ZERO)));
	}
	
	public java.math.BigDecimal getTotGiacenzaLotti() {
		return Optional.ofNullable(this.getScaricoMagazzinoRigaLottoColl())
				.flatMap(e->e.stream().map(ScaricoMagazzinoRigaLottoBulk::getLottoMagazzino)
									  .map(LottoMagBulk::getGiacenza)
									  .reduce((x, y)->x.add(y)))
				.orElse(BigDecimal.ZERO);
	}

	public java.math.BigDecimal getTotQtScaricoLotti() {
		return Optional.ofNullable(this.getScaricoMagazzinoRigaLottoColl())
				.flatMap(e->e.stream().map(ScaricoMagazzinoRigaLottoBulk::getQtScarico)
									  .reduce((x, y)->x.add(y)))
				.orElse(BigDecimal.ZERO);
	}
	
	public java.math.BigDecimal getTotQtScaricoLottiConvertita() {
		return Optional.ofNullable(this.getScaricoMagazzinoRigaLottoColl())
				.flatMap(e->e.stream().map(ScaricoMagazzinoRigaLottoBulk::getQtScaricoConvertita)
									  .reduce((x, y)->x.add(y)))
				.orElse(BigDecimal.ZERO);
	}

	public boolean isImputazioneScaricoSuLottiEnable() {
		return Optional.ofNullable(this.getQtScarico()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==0;
	}

	public boolean isImputazioneScaricoSuBeneEnable() {
		return getTotQtScaricoLotti().compareTo(BigDecimal.ZERO)==0;
	}
	
	public boolean isROCoefConv(){
		return Optional.ofNullable(this.getBeneServizio()).flatMap(bs->{
					return Optional.ofNullable(this.getBeneServizio().getUnitaMisura()).map(umBene->{
						return Optional.ofNullable(this.getUnitaMisura())
									.filter(e->umBene.equalsByPrimaryKey(unitaMisura))
									.isPresent();
					});
				})
				.orElse(Boolean.FALSE);
	}
}