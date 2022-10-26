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

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaHome;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.comp.BuonoCaricoScaricoComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.comp.ComponentException;

/**
 * Bean implementation class for Enterprise Bean: CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession
 */
@Stateless(name="CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession")
public class BuonoCaricoScaricoComponentSessionBean extends it.cnr.jada.ejb.CRUDDetailComponentSessionBean implements BuonoCaricoScaricoComponentSession {
	
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.inventario01.comp.BuonoCaricoScaricoComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new BuonoCaricoScaricoComponentSessionBean();
	}
	
	public void eliminaBeniAssociatiConBulk(it.cnr.jada.UserContext param0,OggettoBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,OggettoBulk param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).eliminaBeniAssociatiConBulk(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void eliminaBeniAssociatiConBulk(it.cnr.jada.UserContext param0,OggettoBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).eliminaBeniAssociatiConBulk(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void eliminaBuoniAssociatiConBulk(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).eliminaBuoniAssociatiConBulk(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void eliminaBuoniAssociatiConBulk(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,OggettoBulk param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).eliminaBuoniAssociatiConBulk(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}

	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0,param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0, param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	
	public boolean isEsercizioCOEPChiuso(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, EJBException{
			pre_component_invocation(param0,componentObj);
			try {
				boolean result = ((BuonoCaricoScaricoComponent)componentObj).isEsercizioCOEPChiuso(param0);
				component_invocation_succes(param0,componentObj);
				return result;
			} catch(it.cnr.jada.comp.NoRollbackException e) {
				component_invocation_succes(param0,componentObj);
				throw e;
			} catch(it.cnr.jada.comp.ComponentException e) {
				component_invocation_failure(param0,componentObj);
				throw e;
			} catch(RuntimeException e) {
				throw uncaughtRuntimeException(param0,componentObj,e);
			} catch(Error e) {
				throw uncaughtError(param0,componentObj,e);
			}
		}	
	public it.cnr.jada.util.RemoteIterator getListaBeni(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,boolean param2,it.cnr.jada.bulk.SimpleBulkList param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).getListaBeni(param0,param1,param2,param3,param4);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator getListaBeniDaScaricare(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,boolean param2,SimpleBulkList param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,EJBException {
			pre_component_invocation(param0,componentObj);
			try {
				it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).getListaBeniDaScaricare(param0,param1,param2,param3,param4);
				component_invocation_succes(param0,componentObj);
				return result;
			} catch(it.cnr.jada.comp.NoRollbackException e) {
				component_invocation_succes(param0,componentObj);
				throw e;
			} catch(it.cnr.jada.comp.ComponentException e) {
				component_invocation_failure(param0,componentObj);
				throw e;
			} catch(RuntimeException e) {
				throw uncaughtRuntimeException(param0,componentObj,e);
			} catch(Error e) {
				throw uncaughtError(param0,componentObj,e);
			}
		}
	public it.cnr.jada.util.RemoteIterator cercaBeniScaricabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,boolean param2,it.cnr.jada.bulk.SimpleBulkList param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniScaricabili(param0,param1,param2,param3,param4);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
		
	}
	public void scaricaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).scaricaTuttiBeni(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void scaricaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,java.util.List param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).scaricaTuttiBeni(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void modificaBeniScaricatiPerAssocia(UserContext param0,Buono_carico_scaricoBulk param1, java.util.List param2,OggettoBulk[] param3,java.util.BitSet param4,java.util.BitSet param5) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).modificaBeniScaricatiPerAssocia(param0,param1,param2,param3,param4,param5);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void modificaBeniScaricati(UserContext param0,Buono_carico_scaricoBulk param1, OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).modificaBeniScaricati(param0,param1,param2,param3,param4);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator selectBeniAssociatiByClause(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,OggettoBulk param2,java.lang.Class param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).selectBeniAssociatiByClause(param0,param1,param2,param3,param4);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	
	
	public it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk caricaInventario(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk result = ((BuonoCaricoScaricoComponent)componentObj).caricaInventario(param0);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.IntrospectionException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator selectEditDettagliScarico(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,java.lang.Class param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).selectEditDettagliScarico(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void checkBeniAccessoriAlreadyExistFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).checkBeniAccessoriAlreadyExistFor(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void checkNuovoBenePadreAlreadySelected(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).checkNuovoBenePadreAlreadySelected(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void scaricaBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2,java.util.List param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).scaricaBeniAccessoriFor(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void modificaBeneScaricato(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,
			it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2,OggettoBulk param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).modificaBeneScaricato(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	
	public void annullaScaricaBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2,java.util.List param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).annullaScaricaBeniAccessoriFor(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
	public java.util.List getBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List result = ((BuonoCaricoScaricoComponent)componentObj).getBeniAccessoriFor(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	
	public void annullaModificaScaricoBeni(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).annullaModificaScaricoBeni(param0);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void annullaModificaBeniAssociati(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).annullaModificaBeniAssociati(param0);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void inizializzaBeniDaScaricare(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).inizializzaBeniDaScaricare(param0);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	
	public SimpleBulkList selezionati(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			SimpleBulkList result = ((BuonoCaricoScaricoComponent)componentObj).selezionati(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void inizializzaBeniAssociatiPerModifica(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).inizializzaBeniAssociatiPerModifica(param0);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
	public void associaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).associaTuttiBeni(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void associaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,java.util.List param2, it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).associaTuttiBeni(param0,param1,param2, param3);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator selectBeniAssociatiByClause(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,OggettoBulk param2,java.lang.Class param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws  it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).selectBeniAssociatiByClause(param0,param1,param2,param3,param4);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void selectBeniAssociatiForModifica(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException, EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).selectBeniAssociatiForModifica(param0,param1,param2);
	 		component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
	public void modificaBeniAssociati(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1,java.util.List param2,it.cnr.jada.bulk.OggettoBulk[] param3,java.util.BitSet param4,java.util.BitSet param5) throws  it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).modificaBeniAssociati(param0,param1,param2,param3,param4,param5);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
	public void annullaRiportaAssFattura_Bene(it.cnr.jada.UserContext param0,OggettoBulk param1,java.util.List param2) throws  it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).annullaRiportaAssFattura_Bene(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.IntrospectionException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void annullaRiportaAssFattura_Bene(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,java.util.List param2) throws  it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).annullaRiportaAssFattura_Bene(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.IntrospectionException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void validaRiportaAssFattura_Bene(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1) throws  it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).validaRiportaAssFattura_Bene(param0,param1);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public java.lang.String getLocalTransactionID(it.cnr.jada.UserContext param0,boolean param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.lang.String result = ((BuonoCaricoScaricoComponent)componentObj).getLocalTransactionID(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.IntrospectionException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public boolean verifica_associazioni(it.cnr.jada.UserContext param0,Buono_carico_scarico_dettBulk param1) throws it.cnr.jada.comp.ComponentException, EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((BuonoCaricoScaricoComponent)componentObj).verifica_associazioni(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public Long findMaxAssociazione(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1) throws it.cnr.jada.comp.ComponentException, EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			Long result = ((BuonoCaricoScaricoComponent)componentObj).findMaxAssociazione(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
	public boolean isNonUltimo(it.cnr.jada.UserContext param0,Buono_carico_scarico_dettBulk param1) throws it.cnr.jada.comp.ComponentException, EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((BuonoCaricoScaricoComponent)componentObj).isNonUltimo(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,Nota_di_credito_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0,param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.bulk.OggettoBulk modificaBeneAssociatoConBulk(it.cnr.jada.UserContext param0,OggettoBulk param1,OggettoBulk param2,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param3) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((BuonoCaricoScaricoComponent)componentObj).modificaBeneAssociatoConBulk(param0,param1,param2,param3);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,Nota_di_credito_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0,param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,Nota_di_debito_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0,param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,Fattura_attiva_rigaIBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0,param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
		public void scaricaBeniAccessori(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param2) throws it.cnr.jada.comp.ComponentException,EJBException {
			pre_component_invocation(param0,componentObj);
			try {
				((BuonoCaricoScaricoComponent)componentObj).scaricaBeniAccessori(param0,param1,param2);
				component_invocation_succes(param0,componentObj);
			} catch(it.cnr.jada.comp.NoRollbackException e) {
				component_invocation_succes(param0,componentObj);
				throw e;
			} catch(it.cnr.jada.comp.ComponentException e) {
				component_invocation_failure(param0,componentObj);
				throw e;
			} catch(RuntimeException e) {
				throw uncaughtRuntimeException(param0,componentObj,e);
			} catch(Error e) {
				throw uncaughtError(param0,componentObj,e);
			}
		}
	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,Ass_inv_bene_fatturaBulk param1,Documento_generico_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0,param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
	public it.cnr.jada.util.RemoteIterator cercaBeniAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk param1,Documento_generico_rigaBulk param2,it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException,EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((BuonoCaricoScaricoComponent)componentObj).cercaBeniAssociabili(param0,param1,param2,clauses);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}	
	public boolean verifica_associazioni(it.cnr.jada.UserContext param0,Buono_carico_scaricoBulk param1) throws it.cnr.jada.comp.ComponentException, EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((BuonoCaricoScaricoComponent)componentObj).verifica_associazioni(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public boolean isContabilizzato(it.cnr.jada.UserContext param0,Buono_carico_scaricoBulk param1) throws it.cnr.jada.comp.ComponentException, EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((BuonoCaricoScaricoComponent)componentObj).isContabilizzato(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk findCategoria_gruppo_voceforvoce(UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk param1) throws it.cnr.jada.comp.ComponentException, EJBException{
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk result = ((BuonoCaricoScaricoComponent)componentObj).findCategoria_gruppo_voceforvoce(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public void scaricaTuttiBeniDef(UserContext param0,
			Buono_carico_scaricoBulk buonoS) throws ComponentException,
			RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			((BuonoCaricoScaricoComponent)componentObj).scaricaTuttiBeniDef(param0,buonoS);
			component_invocation_succes(param0,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public Ass_inv_bene_fatturaBulk sdoppiaAssociazioneFor(
			UserContext param0, Fattura_passiva_rigaBulk param1, Fattura_passiva_rigaBulk param2)
			throws ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			Ass_inv_bene_fatturaBulk result= ((BuonoCaricoScaricoComponent)componentObj).sdoppiaAssociazioneFor(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}


	public boolean checkEtichettaBeneAlreadyExist(UserContext param0, Buono_carico_scarico_dettBulk param1) throws ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((BuonoCaricoScaricoComponent)componentObj).checkEtichettaBeneAlreadyExist(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}

}
