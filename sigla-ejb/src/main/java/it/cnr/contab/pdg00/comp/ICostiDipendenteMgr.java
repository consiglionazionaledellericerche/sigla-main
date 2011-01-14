package it.cnr.contab.pdg00.comp;

import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;

public interface ICostiDipendenteMgr extends IRicercaMgr{
void annullaScritturaAnalitica(UserContext userContext,int mese) throws ComponentException;
OggettoBulk caricaCosti_dipendente(UserContext userContext,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo,int mese) throws it.cnr.jada.comp.ComponentException;
OggettoBulk caricaCosto_dipendente(UserContext userContext,Costi_dipendenteVBulk costi_dipendente,V_cdp_matricolaBulk cdp) throws it.cnr.jada.comp.ComponentException;
void contabilizzaFlussoStipendialeMensile(UserContext userContext,int mese) throws ComponentException;
it.cnr.jada.bulk.OggettoBulk copiaRipartizione(it.cnr.jada.UserContext userContext,Costi_dipendenteVBulk cdp, V_cdp_matricolaBulk matricola_src,V_cdp_matricolaBulk matricola_dest) throws it.cnr.jada.comp.ComponentException;
void generaScritturaAnalitica(UserContext userContext,int mese) throws ComponentException;
it.cnr.jada.util.RemoteIterator listaCdp_analitica(UserContext userContext) throws ComponentException;
it.cnr.jada.util.RemoteIterator listaCdr(UserContext userContext,String cd_unita_organizzativa,int mese) throws ComponentException;
it.cnr.jada.util.RemoteIterator listaLinea_attivitaPerCdr(UserContext userContext,CdrBulk cdr,int mese, String tipo_rapporto) throws ComponentException;
java.util.List listaLinea_attivitaPerRipartizioneResidui(UserContext userContext,String id_matricola,String cd_unita_organizzativa,int mese, String tipo_rapporto) throws ComponentException;
it.cnr.jada.util.RemoteIterator listaStipendi_cofi(UserContext userContext) throws ComponentException;
it.cnr.jada.util.RemoteIterator listaUnita_organizzativa(UserContext userContext,String cd_unita_organizzativa,int mese) throws ComponentException;
void ripartizioneResidui(it.cnr.jada.UserContext userContext, java.lang.String id_matricola,String cd_unita_organizzativa,int mese,java.util.Collection linee_attivita) throws it.cnr.jada.comp.ComponentException;
OggettoBulk salvaCosti_dipendente(UserContext userContext,Costi_dipendenteVBulk costi_dipendente) throws ComponentException;
}
