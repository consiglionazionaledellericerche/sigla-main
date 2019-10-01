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

package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface StampaSingoloContoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
void annullaModificaSelezione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void associaTutti(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk param1,java.math.BigDecimal param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal getPgStampa(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void inizializzaSelezionePerModifica(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal modificaSelezione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4,java.math.BigDecimal param5,java.math.BigDecimal param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
