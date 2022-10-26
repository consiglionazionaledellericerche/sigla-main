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

package it.cnr.contab.inventario01.ejb;
import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDDetailComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

/**
 * Remote interface for Enterprise Bean: CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession
 */
@Remote
public interface BuonoCaricoScaricoComponentSession	extends CRUDDetailComponentSession
{
		void associaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void associaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,java.util.List param2, it.cnr.jada.persistency.sql.CompoundFindClause clause) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void annullaModificaBeniAssociati(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void annullaModificaScaricoBeni(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void annullaRiportaAssFattura_Bene(it.cnr.jada.UserContext param0,OggettoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
		void inizializzaBeniDaScaricare(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void eliminaBeniAssociatiConBulk(it.cnr.jada.UserContext param0,OggettoBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,OggettoBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void eliminaBeniAssociatiConBulk(it.cnr.jada.UserContext param0,OggettoBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void eliminaBuoniAssociatiConBulk(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,OggettoBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void eliminaBuoniAssociatiConBulk(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void inizializzaBeniAssociatiPerModifica(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.bulk.OggettoBulk modificaBeneAssociatoConBulk(it.cnr.jada.UserContext param0,OggettoBulk param1,OggettoBulk param2,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		boolean isEsercizioCOEPChiuso(it.cnr.jada.UserContext userContext) throws ComponentException,java.rmi.RemoteException;
		RemoteIterator getListaBeni(UserContext userContext, OggettoBulk bulk, boolean no_accessori, SimpleBulkList beni_da_escludere, CompoundFindClause clauses)throws ComponentException,java.rmi.RemoteException;
		RemoteIterator cercaBeniScaricabili(it.cnr.jada.UserContext userContext, Buono_carico_scaricoBulk buono, boolean no_accessori, SimpleBulkList beni_da_escludere, CompoundFindClause clauses) throws ComponentException,RemoteException;
		void scaricaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void scaricaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,java.util.List param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void modificaBeniScaricati(UserContext userContext,Buono_carico_scaricoBulk buonoS, OggettoBulk[] beni,java.util.BitSet old_ass,java.util.BitSet ass) throws ComponentException, RemoteException ;
		RemoteIterator getListaBeniDaScaricare(UserContext userContext, Buono_carico_scaricoBulk bulk, boolean no_accessori,SimpleBulkList beni_da_escludere,  CompoundFindClause clauses)throws ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator selectBeniAssociatiByClause(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,OggettoBulk param2,java.lang.Class param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk caricaInventario(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator selectEditDettagliScarico(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,java.lang.Class param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void checkBeniAccessoriAlreadyExistFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void scaricaBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2,java.util.List param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void modificaBeneScaricato(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,
		it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2,OggettoBulk param3)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void annullaScaricaBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2,java.util.List param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		java.util.List getBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		SimpleBulkList selezionati(it.cnr.jada.UserContext userContext, Buono_carico_scaricoBulk buonoS) throws it.cnr.jada.comp.ComponentException,RemoteException;
		void checkNuovoBenePadreAlreadySelected(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(UserContext userContext,Buono_carico_scaricoBulk buonoS, Fattura_attiva_rigaIBulk riga_fattura, CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator selectBeniAssociatiByClause(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,OggettoBulk param2,java.lang.Class param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void selectBeniAssociatiForModifica(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void modificaBeniAssociati(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,java.util.List param2,it.cnr.jada.bulk.OggettoBulk[] param3,java.util.BitSet param4,java.util.BitSet param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void validaRiportaAssFattura_Bene(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		String getLocalTransactionID(UserContext aUC, boolean force) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException ;
		boolean verifica_associazioni(UserContext userContext,Buono_carico_scarico_dettBulk dettaglio) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		Long findMaxAssociazione(UserContext userContext,Ass_inv_bene_fatturaBulk ass) throws ComponentException,java.rmi.RemoteException;
		boolean isNonUltimo(UserContext userContext,Buono_carico_scarico_dettBulk dettaglio) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void modificaBeniScaricatiPerAssocia(UserContext userContext,Buono_carico_scaricoBulk buonoS, java.util.List righe_fattura,OggettoBulk[] beni,java.util.BitSet old_ass,java.util.BitSet ass)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(UserContext userContext,Buono_carico_scaricoBulk buonoS, Nota_di_credito_rigaBulk riga_fattura, CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,Nota_di_credito_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,Nota_di_debito_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void scaricaBeniAccessori(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(UserContext userContext,Buono_carico_scaricoBulk buonoS, Documento_generico_rigaBulk riga, CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		boolean verifica_associazioni(UserContext userContext,Buono_carico_scaricoBulk buono) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		boolean isContabilizzato(UserContext userContext,Buono_carico_scaricoBulk buono) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk findCategoria_gruppo_voceforvoce(UserContext userContext,it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elem)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void scaricaTuttiBeniDef(UserContext userContext,Buono_carico_scaricoBulk buonoS) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		Ass_inv_bene_fatturaBulk sdoppiaAssociazioneFor (UserContext userContext,Fattura_passiva_rigaBulk riga_fattura,Fattura_passiva_rigaBulk riga_fattura_new) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		boolean checkEtichettaBeneAlreadyExist(UserContext userContext,Buono_carico_scarico_dettBulk dett) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
