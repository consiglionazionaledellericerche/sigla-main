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

package it.cnr.contab.cori00.ejb;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.cori00.comp.Liquid_coriComponent;
import it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk;
import it.cnr.contab.cori00.views.bulk.ParSelConsLiqCoriBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ParametriSelezioneMovimentiBulk;
import it.cnr.contab.ordmag.magazzino.comp.MovimentiMagComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

@Stateless(name="CNRCORI00_EJB_Liquid_coriComponentSession")
public class Liquid_coriComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Liquid_coriComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.cori00.comp.Liquid_coriComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new Liquid_coriComponentSessionBean();
}
public it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk calcolaLiquidazione(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk result = ((Liquid_coriComponent)componentObj).calcolaLiquidazione(param0,param1);
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
public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,java.lang.Class param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.util.RemoteIterator result = ((Liquid_coriComponent)componentObj).cerca(param0,param1,param2,param3,param4);
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
public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.jada.bulk.OggettoBulk param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((Liquid_coriComponent)componentObj).creaConBulk(param0,param1,param2,param3);
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
public void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,java.lang.String param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((Liquid_coriComponent)componentObj).eliminaConBulk(param0,param1,param2);
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
public void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk[] param1,it.cnr.jada.bulk.OggettoBulk param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((Liquid_coriComponent)componentObj).eliminaConBulk(param0,param1,param2,param3);
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
public it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk eseguiLiquidazione(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk result = ((Liquid_coriComponent)componentObj).eseguiLiquidazione(param0,param1,param2);
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
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.jada.bulk.OggettoBulk param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((Liquid_coriComponent)componentObj).inizializzaBulkPerInserimento(param0,param1,param2,param3);
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
public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.jada.bulk.OggettoBulk param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((Liquid_coriComponent)componentObj).inizializzaBulkPerModifica(param0,param1,param2,param3);
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
public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.jada.bulk.OggettoBulk param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((Liquid_coriComponent)componentObj).modificaConBulk(param0,param1,param2,param3);
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
public it.cnr.jada.bulk.OggettoBulk eseguiLiquidazioneMassaCori(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquidazione_massa_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((Liquid_coriComponent)componentObj).eseguiLiquidazioneMassaCori(param0,param1);
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
public it.cnr.jada.bulk.OggettoBulk cercaBatch(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquidazione_massa_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((Liquid_coriComponent)componentObj).cercaBatch(param0,param1);
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
public void callRibalta(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((Liquid_coriComponent)componentObj).callRibalta(param0,param1);
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
public it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk cambiaStato(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	
	try {
		it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk result = ((Liquid_coriComponent)componentObj).cambiaStato(param0,param1);
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
public boolean esisteRiga(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	
	try {
		boolean result = ((Liquid_coriComponent)componentObj).esisteRiga(param0,param1);
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
public java.util.List EstraiLista(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.List result = ((Liquid_coriComponent)componentObj).EstraiLista(param0,param1);
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
public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagraficoEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk result = ((Liquid_coriComponent)componentObj).getAnagraficoEnte(param0);
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
public String getContoSpecialeEnteF24(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		String result = ((Liquid_coriComponent)componentObj).getContoSpecialeEnteF24(param0);
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
public void Popola_f24(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((Liquid_coriComponent)componentObj).Popola_f24(param0,param1);
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
public void Popola_f24Tot(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((Liquid_coriComponent)componentObj).Popola_f24Tot(param0,param1);
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
public java.util.List EstraiListaTot(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.List result = ((Liquid_coriComponent)componentObj).EstraiListaTot(param0,param1);
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
public String getSedeInpsF24(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		String result = ((Liquid_coriComponent)componentObj).getSedeInpsF24(param0);
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
public Configurazione_cnrBulk getSedeInailF24(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		Configurazione_cnrBulk result = ((Liquid_coriComponent)componentObj).getSedeInailF24(param0);
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
public String getSedeInpdapF24(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		String result = ((Liquid_coriComponent)componentObj).getSedeInpdapF24(param0);
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
public String getSedeInpgiF24(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		String result = ((Liquid_coriComponent)componentObj).getSedeInpgiF24(param0);
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
public void eliminaPendenti_f24Tot(UserContext param0) throws ComponentException,
		RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((Liquid_coriComponent)componentObj).eliminaPendenti_f24Tot(param0);
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
	public RemoteIterator ricercaCori(UserContext userContext, ParSelConsLiqCoriBulk parametri) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(userContext,componentObj);
		try {
			RemoteIterator result = ((Liquid_coriComponent)componentObj).ricercaCori(userContext, parametri);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
	public ParSelConsLiqCoriBulk initializeConsultazioneCori(UserContext userContext, ParSelConsLiqCoriBulk parametri) throws PersistencyException, ComponentException , RemoteException, ApplicationException {
		pre_component_invocation(userContext,componentObj);
		try {
			ParSelConsLiqCoriBulk result = ((Liquid_coriComponent)componentObj).initializeConsultazioneCori(userContext, parametri);
			component_invocation_succes(userContext,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

}
