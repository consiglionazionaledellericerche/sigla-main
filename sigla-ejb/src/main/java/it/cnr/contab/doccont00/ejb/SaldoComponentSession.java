package it.cnr.contab.doccont00.ejb;

import java.math.BigDecimal;

import javax.ejb.Remote;

import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
@Remote
public interface SaldoComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4,boolean param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4,boolean param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaPagamentiIncassi(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaMandati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5, String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5, String param6, boolean param7  ) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk checkDisponabilitaCassaMandati(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, java.math.BigDecimal param4)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6, String param7)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaPagamentiIncassi(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5 )  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String checkDispObbligazioniAccertamenti(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, String param6 )  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal getTotaleSaldoResidui(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getMessaggioSfondamentoDisponibilita(it.cnr.jada.UserContext param0, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaImpegniResiduiPropri(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, BigDecimal param5) throws ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaAccertamentiResiduiPropri(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, BigDecimal param5) throws ComponentException,java.rmi.RemoteException;
void aggiornaSaldiAnniSuccessivi(it.cnr.jada.UserContext param1, String param2, String param3, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param4, Integer param5, BigDecimal param6, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk param7) throws ComponentException,java.rmi.RemoteException;
Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, String tipo_residuo, BigDecimal importo, Boolean sottraiImportoDaVariazioneEsistente) throws ComponentException,java.rmi.RemoteException;
void checkDispPianoEconomicoProgetto(UserContext userContext, Pdg_modulo_costiBulk moduloCosti, boolean isModified) throws ComponentException,java.rmi.RemoteException;
void checkDispPianoEconomicoProgetto(UserContext userContext, Pdg_variazioneBulk pdgVariazione) throws ComponentException,java.rmi.RemoteException;
String getMessaggioSfondamentoPianoEconomico(UserContext userContext, Pdg_variazioneBulk pdgVariazione) throws ComponentException,java.rmi.RemoteException;
}