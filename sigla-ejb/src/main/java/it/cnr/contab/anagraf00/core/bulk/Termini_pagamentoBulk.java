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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Termini_pagamento
 */

public class Termini_pagamentoBulk extends Termini_pagamentoBase {

	protected TerzoBulk terzo;
	protected it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk rif_termini_pagamento;
	public Termini_pagamentoBulk() {
		super();
	}
public Termini_pagamentoBulk(java.lang.String cd_termini_pag,java.lang.Integer cd_terzo) {
	super(cd_termini_pag,cd_terzo);
	setTerzo(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk(cd_terzo));
	setRif_termini_pagamento(new it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk(cd_termini_pag));
}
public java.lang.String getCd_termini_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk rif_termini_pagamento = this.getRif_termini_pagamento();
	if (rif_termini_pagamento == null)
		return null;
	return rif_termini_pagamento.getCd_termini_pag();
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
}
	/**
	 * Restituisce il <code>Rif_termini_pagamentoBulk</code> a cui è associato l'oggetto.
	 *
	 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
	 *
	 * @see setRif_termini_pagamento
	 */

	public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getRif_termini_pagamento() {
		return rif_termini_pagamento;
	}
	/**
	 * Restituisce il <code>TerzoBulk</code> a cui è associato l'oggetto.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 *
	 * @see setTerzo
	 */

	public TerzoBulk getTerzo() {
		return terzo;
	}
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.getRif_termini_pagamento().setCd_termini_pag(cd_termini_pag);
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getTerzo().setCd_terzo(cd_terzo);
}
	/**
	 * Imposta il <code>Rif_termini_pagamentoBulk</code> a cui è associato l'oggetto.
	 *
	 * @param newRif_termini_pagamento il tipo termini pagamento
	 *
	 * @see getRif_termini_pagamento
	 */

	public void setRif_termini_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newRif_termini_pagamento) {
		rif_termini_pagamento = newRif_termini_pagamento;
	}
	/**
	 * Imposta il <code>TerzoBulk</code> a cui è associato l'oggetto.
	 *
	 * @param newTerzo Il terzo da associare.
	 *
	 * @see getTerzo
	 */

	public void setTerzo(TerzoBulk newTerzo) {
		terzo = newTerzo;
	}
}
