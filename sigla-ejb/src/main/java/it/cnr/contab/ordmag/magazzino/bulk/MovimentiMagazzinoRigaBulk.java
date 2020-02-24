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