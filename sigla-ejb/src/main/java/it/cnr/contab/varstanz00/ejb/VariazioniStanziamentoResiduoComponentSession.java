package it.cnr.contab.varstanz00.ejb;

import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;

/**
 * Remote interface for Enterprise Bean: CNRVARSTANZ00_EJB_VariazioniStanziamentoResiduoComponentSession
 */
@Remote
public interface VariazioniStanziamentoResiduoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.jada.bulk.OggettoBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk approva(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk controllaApprova(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk respingi(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk statoPrecedente(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getVoce_FdaEV(it.cnr.jada.UserContext param0,Integer param1,String param2,String param3, String param4 , String param5 , String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.math.BigDecimal calcolaDisponibilita_stanz_res(it.cnr.jada.UserContext param0,it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isCdsAbilitatoAdApprovare(it.cnr.jada.UserContext param0,String param1, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void aggiungiDettaglioVariazione(it.cnr.jada.UserContext param0, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param1, it.cnr.contab.prevent00.bulk.V_assestato_residuoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk generaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk esitaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void validaOrigineFontiPerAnnoResiduo(it.cnr.jada.UserContext usercontext, Integer annoResiduo, String origineFonti) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.contab.progettiric00.core.bulk.ProgettoBulk getProgettoLineaAttivita(it.cnr.jada.UserContext usercontext, it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk varRiga) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isVariazioneFromLiquidazioneIvaDaModificare(UserContext userContext, Var_stanz_resBulk variazione) throws ComponentException,java.rmi.RemoteException;
	Var_stanz_res_rigaBulk recuperoRigaLiquidazioneIva(UserContext userContext, Var_stanz_resBulk bulk) throws ComponentException,java.rmi.RemoteException;
}
