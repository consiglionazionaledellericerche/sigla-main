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

package it.cnr.contab.doccont00.comp;

public interface IStampaSingoloContoMgr extends it.cnr.jada.comp.ICRUDMgr {
void annullaModificaSelezione(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro)
	throws it.cnr.jada.comp.ComponentException;
void associaTutti(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro,
	java.math.BigDecimal pg_stampa)
	throws it.cnr.jada.comp.ComponentException;
java.math.BigDecimal getPgStampa(
	it.cnr.jada.UserContext userContext)
	throws it.cnr.jada.comp.ComponentException;
void inizializzaSelezionePerModifica(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro)
	throws it.cnr.jada.comp.ComponentException;
java.math.BigDecimal modificaSelezione(
	it.cnr.jada.UserContext userContext, 
	it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk filtro,
	it.cnr.jada.bulk.OggettoBulk[] bulks,
	java.util.BitSet oldSelection,
	java.util.BitSet newSelection,
	java.math.BigDecimal pg_stampa,
	java.math.BigDecimal currentSequence)
	throws it.cnr.jada.comp.ComponentException;
}
