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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_mandato_reversaleBulk extends Ass_mandato_reversaleBase {
	public static final String TIPO_ORIGINE_ENTRATA = "E";
	public static final String TIPO_ORIGINE_SPESA	= "S";
	protected ReversaleBulk reversale;
	protected MandatoBulk mandato;
public Ass_mandato_reversaleBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param mandato	
 * @param reversale	
 */
public Ass_mandato_reversaleBulk( MandatoBulk mandato, ReversaleBulk reversale)
{
	setMandato( mandato );
	setReversale( reversale );
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public MandatoBulk getMandato() {
	return mandato;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public ReversaleBulk getReversale() {
	return reversale;
}
/**
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public void setMandato(MandatoBulk newMandato) {
	mandato = newMandato;
	setPg_mandato( mandato.getPg_mandato());
	setEsercizio_mandato( mandato.getEsercizio());
	setCd_cds_mandato( mandato.getCd_cds());
}
/**
 * @param newReversale it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public void setReversale(ReversaleBulk newReversale) {
	reversale = newReversale;
	setPg_reversale( reversale.getPg_reversale());
	setEsercizio_reversale( reversale.getEsercizio());
	setCd_cds_reversale( reversale.getCd_cds());
}
}
