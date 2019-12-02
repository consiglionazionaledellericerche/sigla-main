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

package it.cnr.contab.pdg00.ejb;

import javax.ejb.Remote;

@Remote
public interface CostiDipendenteComponentSession extends it.cnr.jada.ejb.RicercaComponentSession, it.cnr.jada.ejb.PrintComponentSession {
void annullaScritturaAnalitica(it.cnr.jada.UserContext param0,int param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk caricaCosti_dipendente(it.cnr.jada.UserContext param0,int param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk caricaCosti_dipendente(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1,int param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk caricaCosto_dipendente(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.Costi_dipendenteVBulk param1,it.cnr.contab.pdg00.cdip.bulk.V_cdp_matricolaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void contabilizzaFlussoStipendialeMensile(it.cnr.jada.UserContext param0,int param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk copiaRipartizione(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.Costi_dipendenteVBulk param1,it.cnr.contab.pdg00.cdip.bulk.V_cdp_matricolaBulk param2,it.cnr.contab.pdg00.cdip.bulk.V_cdp_matricolaBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void generaScritturaAnalitica(it.cnr.jada.UserContext param0,int param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator listaCdp_analitica(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator listaCdr(it.cnr.jada.UserContext param0,String param1,int param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator listaLinea_attivitaPerCdr(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1,int param2, String param3, boolean param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List listaLinea_attivitaPerRipartizioneResidui(it.cnr.jada.UserContext param0,String param1,String param2,int param3, String param4, boolean param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator listaStipendi_cofi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator listaUnita_organizzativa(it.cnr.jada.UserContext param0,String param1,int param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void ripartizioneResidui(it.cnr.jada.UserContext param0,String param1,String param2,int param3,java.util.Collection param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk salvaCosti_dipendente(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.Costi_dipendenteVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isPdgPrevisionaleEnabled(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteRipartiti(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteRipartiti(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteRipartiti(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1,it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk param2) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteRipartiti(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteCaricati(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteCaricati(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isSpeseFromScaricoDipendente(it.cnr.jada.UserContext param0, it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isModuloUtilizzato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1,it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk param2) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteDefinitivi(it.cnr.jada.UserContext param0, int param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteDefinitivi(it.cnr.jada.UserContext param0, int param1, it.cnr.contab.config00.sto.bulk.CdrBulk param2) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteDefinitivi(it.cnr.jada.UserContext param0, int param1, String param2) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean isCostiDipendenteDefinitivi(it.cnr.jada.UserContext param0, int param1, it.cnr.contab.config00.sto.bulk.CdrBulk param2,it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk param3) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk salvaDefinitivoCosti_dipendente(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.Costi_dipendenteVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk annullaDefinitivoCosti_dipendente(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.Costi_dipendenteVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.cdip.bulk.V_cdp_matricolaBulk generaDaUltimaRipartizione(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.cdip.bulk.V_cdp_matricolaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void lockMatricola(it.cnr.jada.UserContext param0,String param1,int param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException, it.cnr.jada.bulk.BusyResourceException;
boolean isEsercizioChiuso(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
}