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

package it.cnr.contab.gestiva00.comp;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;


public interface IStampaRegistriIvaMgr extends it.cnr.jada.comp.IRicercaMgr
{
/** 
  *  gestisce il richiamo delle procedure sul DB per la registrazione, la stampa,
  *  la liquidazione, la conferma dei registri e l'annullamento di un registro
  *  registro provvisorio o definitivo
  *    PreCondition:
  *      richiesta di creare un registro nuovo provvisorio o definitivo
  *    PostCondition:
  *      crea il registro
  *  stampa di un registro
  *    PreCondition:
  *      richiesta di stampa di un registro provvisorio o definitivo
  *    PostCondition:
  *      crea il prospetto di stampa
  *  liquidazione
  *	   PreCondition:  		
  *      liquidazione provvisoria o definitiva
  *    PostCondition:
  *      crea la liquidazione
  *  Si è verificato un errore.
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio e non permette l'operazione
 */

public abstract it.cnr.jada.bulk.MTUWrapper callStampeIva(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk param1) throws it.cnr.jada.comp.ComponentException;

public it.cnr.jada.bulk.BulkList findRegistriStampati(
    UserContext param0,
    Stampa_registri_ivaVBulk param1)
    throws ComponentException;

public Liquidazione_iva_annualeVBulk riepilogoLiquidazioneIVA(
    UserContext param0,
    Liquidazione_iva_annualeVBulk param1)
    throws ComponentException;

/** 
  *  Prospetto delle liquidazioni definitive
  *  richiesta la lista delle liquidazioni
  *    PreCondition:
  *      Viene richiesta la lista delle liquidazioni già stampate 
  *    PostCondition:
  *      visualizza la lista delle liquidazioni stampate
  *  si verifica un errore
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio con il relativo errore ritornato dal DB
 */

public abstract java.util.Collection selectProspetti_stampatiByClause(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;

/** 
  *  Combo dei sezionali
  *    PreCondition:
  *      Viene richiesta la lista dei sezionali relativi alla UO di appartenenza
  *    PostCondition:
  *      visualizza la lista dei sezionali
  *  Si è verificato un errore
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato un messaggio con il relativo errore ritornato dal DB
 */

public abstract java.util.Collection selectTipi_sezionaliByClause(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException;

public Liquidazione_iva_annualeVBulk tabCodIvaAcquisti(
    UserContext param0,
    Liquidazione_iva_annualeVBulk param1)
    throws ComponentException;

public Liquidazione_iva_annualeVBulk tabCodIvaVendite(
    UserContext param0,
    Liquidazione_iva_annualeVBulk param1)
    throws ComponentException;
}