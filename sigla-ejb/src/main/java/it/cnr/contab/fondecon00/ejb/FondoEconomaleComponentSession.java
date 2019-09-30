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

package it.cnr.contab.fondecon00.ejb;

import javax.ejb.Remote;

@Remote
public interface FondoEconomaleComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
void associaTutteSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void associazione(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal calcolaTotaleSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaFondi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaMandatiPerIntegrazioni(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaObb_scad(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaSospesiDiChiusuraFondo(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_speseVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaSpeseAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaSpeseDelFondo(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaSpeseReintegrabili(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_speseVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk chiudeFondo(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk chiudeSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void dissociaTutteSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk[] modificaSpe_associate(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk[] param1,boolean[] param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk reintegraSpese(it.cnr.jada.UserContext param0,it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
