package it.cnr.contab.doccont00.ejb;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
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
	public String getMessaggioSfondamentoPianoEconomico(it.cnr.jada.UserContext param1, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param2) throws ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param1,componentObj);
		try {
			java.lang.String result = componentObj.getMessaggioSfondamentoPianoEconomico(param1,param2);
			component_invocation_succes(param1,componentObj);
			return result;
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
	public java.math.BigDecimal getStanziamentoAssestatoProgetto(it.cnr.jada.UserContext param0, ProgettoBulk param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.math.BigDecimal result = componentObj.getStanziamentoAssestatoProgetto(param0,param1,param2,param3);
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