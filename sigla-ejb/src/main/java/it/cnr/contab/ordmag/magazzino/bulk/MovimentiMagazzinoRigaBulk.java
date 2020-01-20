/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.util.Optional;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public abstract class MovimentiMagazzinoRigaBulk extends OggettoBulk implements KeyedPersistent{
	private static final long serialVersionUID = 1L;

	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	private UnitaMisuraBulk unitaMisura = new UnitaMisuraBulk();	
	private MovimentiMagazzinoBulk movimentiMagazzinoBulk;
	private java.math.BigDecimal coefConv;
	private java.math.BigDecimal quantita;
	private String anomalia;

	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	
	public void setBeneServizio(Bene_servizioBulk beneServizio) {
		this.beneServizio = beneServizio;
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
	public String getAnomalia() {
		return anomalia;
	}
	
	public void setAnomalia(String anomalia) {
		this.anomalia = anomalia;
	}

	public MovimentiMagazzinoBulk getMovimentiMagazzinoBulk() {
		return movimentiMagazzinoBulk;
	}

	public void setMovimentiMagazzinoBulk(MovimentiMagazzinoBulk movimentiMagazzinoBulk) {
		this.movimentiMagazzinoBulk = movimentiMagazzinoBulk;
	}

	public java.math.BigDecimal getQuantita() {
		return quantita;
	}

	public void setQuantita(java.math.BigDecimal quantita) {
		this.quantita = quantita;
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

}