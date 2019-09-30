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

import java.util.*;

import it.cnr.jada.bulk.*;

public class Reversale_rigaIBulk extends Reversale_rigaBulk {
	protected ReversaleIBulk reversaleI;
	protected boolean flCancellazione = false;	
public Reversale_rigaIBulk() {
	super();
}
public Reversale_rigaIBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario,java.lang.Long pg_reversale) {
	super(cd_cds,esercizio,esercizio_accertamento,esercizio_ori_accertamento,pg_accertamento,pg_accertamento_scadenzario,pg_reversale);
	setReversaleI(new it.cnr.contab.doccont00.core.bulk.ReversaleIBulk(cd_cds,esercizio,pg_reversale));
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public ReversaleBulk getReversale() 
{
	return reversaleI;
}	
/**
 * @return it.cnr.contab.doccont00.core.bulk.ReversaleIBulk
 */
public ReversaleIBulk getReversaleI() {
	return reversaleI;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'flCancellazione'
 *
 * @return Il valore della proprietà 'flCancellazione'
 */
public boolean isFlCancellazione() {
	return flCancellazione;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'flCancellazione'
 *
 * @param newFlCancellazione	Il valore da assegnare a 'flCancellazione'
 */
public void setFlCancellazione(boolean newFlCancellazione) {
	flCancellazione = newFlCancellazione;
}
/**
 * @param newReversale it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public void setReversale(ReversaleBulk newReversale)
{
	setReversaleI( (ReversaleIBulk) newReversale);
}	
/**
 * @param newReversaleI it.cnr.contab.doccont00.core.bulk.ReversaleIBulk
 */
public void setReversaleI(ReversaleIBulk newReversaleI) {
	reversaleI = newReversaleI;
}
}
