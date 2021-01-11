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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
@Stateless(name="CNRDOCCONT00_EJB_SaldoComponentSession")
public class SaldoComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean implements SaldoComponentSession  {
	private it.cnr.contab.doccont00.comp.SaldoComponent componentObj;
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.doccont00.comp.SaldoComponent();
	}
	public static 
	SaldoComponentSessionBean newInstance() throws EJBException {
		return new SaldoComponentSessionBean();
	}
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk result = componentObj.aggiornaMandatiReversali(param0,param1,param2,param3,param4);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4,boolean param5) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk result = componentObj.aggiornaMandatiReversali(param0,param1,param2,param3,param4,param5);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk result = componentObj.aggiornaObbligazioniAccertamenti(param0,param1,param2,param3,param4);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4,boolean param5) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk result = componentObj.aggiornaObbligazioniAccertamenti(param0,param1,param2,param3,param4,param5);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaPagamentiIncassi(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk result = componentObj.aggiornaPagamentiIncassi(param0,param1,param2,param3,param4);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaMandati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk result = componentObj.checkDisponabilitaCassaMandati(param0,param1,param2,param3,param4);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk result = componentObj.checkDisponabilitaCassaObbligazioni(param0,param1,param2,param3,param4);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5, String param6) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaMandatiReversali(param0,param1,param2,param3,param4,param5,param6);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5, String param6, boolean param7) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaMandatiReversali(param0,param1,param2,param3,param4,param5,param6,param7);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk checkDisponabilitaCassaMandati(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, java.math.BigDecimal param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.checkDisponabilitaCassaMandati(param0,param1,param2,param3,param4);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6, String param7) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaObbligazioniAccertamenti(param0,param1,param2,param3,param4,param5,param6,param7);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaPagamentiIncassi(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5 ) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaPagamentiIncassi(param0,param1,param2,param3,param4,param5);
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
	public String checkDispObbligazioniAccertamenti(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, String param6 ) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			String result = componentObj.checkDispObbligazioniAccertamenti(param0,param1,param2,param3,param4,param5,param6);
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
	public Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6, Boolean param7) throws ComponentException{
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaVariazioneStanziamento(param0,param1,param2,param3,param4,param5,param6,param7);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaVariazioneStanziamento(param0,param1,param2,param3,param4,param5,param6);
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
	public java.math.BigDecimal getTotaleSaldoResidui(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3)  throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.math.BigDecimal result = componentObj.getTotaleSaldoResidui(param0,param1,param2,param3);
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
	public String getMessaggioSfondamentoDisponibilita(it.cnr.jada.UserContext param0, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			String result = componentObj.getMessaggioSfondamentoDisponibilita(param0,param1);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaImpegniResiduiPropri(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, BigDecimal param5) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaImpegniResiduiPropri(param0,param1,param2,param3,param4,param5);
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
	public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaAccertamentiResiduiPropri(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, BigDecimal param5) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk result = componentObj.aggiornaAccertamentiResiduiPropri(param0,param1,param2,param3,param4,param5);
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
	public void aggiornaSaldiAnniSuccessivi(it.cnr.jada.UserContext param1, String param2, String param3, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param4, Integer param5, BigDecimal param6, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk param7) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param1,componentObj);
		try {
			componentObj.aggiornaSaldiAnniSuccessivi(param1,param2,param3,param4,param5,param6,param7);
			component_invocation_succes(param1,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param1,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param1,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param1,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param1,componentObj,e);
		}
	}
	public void checkDispPianoEconomicoProgetto(it.cnr.jada.UserContext param1, it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk param2, boolean param3) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param1,componentObj);
		try {
			componentObj.checkDispPianoEconomicoProgetto(param1,param2,param3);
			component_invocation_succes(param1,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param1,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param1,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param1,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param1,componentObj,e);
		}
	}
	public void checkDispPianoEconomicoProgetto(it.cnr.jada.UserContext param1, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param2) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param1,componentObj);
		try {
			componentObj.checkDispPianoEconomicoProgetto(param1,param2);
			component_invocation_succes(param1,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param1,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param1,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param1,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param1,componentObj,e);
		}
	}
	public void checkDispPianoEconomicoProgetto(it.cnr.jada.UserContext param1, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param2) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param1,componentObj);
		try {
			componentObj.checkDispPianoEconomicoProgetto(param1,param2);
			component_invocation_succes(param1,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param1,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param1,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param1,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param1,componentObj,e);
		}
	}
	public void checkPdgPianoEconomico(it.cnr.jada.UserContext param1, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param2) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param1,componentObj);
		try {
			componentObj.checkPdgPianoEconomico(param1,param2);
			component_invocation_succes(param1,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param1,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param1,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param1,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param1,componentObj,e);
		}
	}
	public void checkPdgPianoEconomico(it.cnr.jada.UserContext param1, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param2) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param1,componentObj);
		try {
			componentObj.checkPdgPianoEconomico(param1,param2);
			component_invocation_succes(param1,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param1,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param1,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param1,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param1,componentObj,e);
		}
	}
	public java.math.BigDecimal getStanziamentoAssestatoProgetto(it.cnr.jada.UserContext param0, ProgettoBulk param1, String param2, Integer param3, Timestamp param4, String param5) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.math.BigDecimal result = componentObj.getStanziamentoAssestatoProgetto(param0,param1,param2,param3,param4,param5);
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

	public void checkBloccoImpegniNatfin(UserContext userContext, String cdr, String cdLineaAttivita, Elemento_voceBulk elementoVoceBulk, String tipoObbligazione) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(userContext,componentObj);
		try {
			componentObj.checkBloccoImpegniNatfin(userContext,cdr,cdLineaAttivita,elementoVoceBulk,tipoObbligazione);
			component_invocation_succes(userContext,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public void checkBloccoImpegniNatfin(UserContext userContext, WorkpackageBulk workpackageBulk, Elemento_voceBulk elementoVoceBulk, String tipoObbligazione) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(userContext,componentObj);
		try {
			componentObj.checkBloccoImpegniNatfin(userContext,workpackageBulk,elementoVoceBulk,tipoObbligazione);
			component_invocation_succes(userContext,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public void checkBloccoImpegniNatfin(UserContext userContext, Var_stanz_resBulk variazione) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(userContext,componentObj);
		try {
			componentObj.checkBloccoImpegniNatfin(userContext,variazione);
			component_invocation_succes(userContext,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}

	public void checkBloccoLimiteClassificazione(UserContext userContext, Pdg_variazioneBulk variazione) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(userContext,componentObj);
		try {
			componentObj.checkBloccoLimiteClassificazione(userContext,variazione);
			component_invocation_succes(userContext,componentObj);
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(userContext,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(userContext,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(userContext,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(userContext,componentObj,e);
		}
	}
}