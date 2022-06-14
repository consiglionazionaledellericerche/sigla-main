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

package it.cnr.contab.config00.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

@Remote
public interface Configurazione_cnrComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
    /**
     *
     * @param userContext
     * @param esercizio
     * @param unita_funzionale
     * @param chiave_primaria
     * @param chiave_secondaria
     * @return Ritorna la configurazione in base ai parametri richiesti,
     * @throws it.cnr.jada.comp.ComponentException
     * @throws java.rmi.RemoteException
     */
    it.cnr.contab.config00.bulk.Configurazione_cnrBulk getConfigurazione(it.cnr.jada.UserContext userContext, java.lang.Integer esercizio, java.lang.String unita_funzionale, java.lang.String chiave_primaria, java.lang.String chiave_secondaria) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt01(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    java.sql.Timestamp getDt01(UserContext param0, String param1) throws ComponentException, RemoteException;

    java.sql.Timestamp getDt02(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    java.sql.Timestamp getDt02(UserContext param0, String param1) throws ComponentException, RemoteException;

    java.math.BigDecimal getIm01(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    java.math.BigDecimal getIm01(UserContext param0, String param1) throws ComponentException, RemoteException;

    java.math.BigDecimal getIm02(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    java.math.BigDecimal getIm02(UserContext param0, String param1) throws ComponentException, RemoteException;

    String getVal01(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    String getVal01(UserContext param0, String param1) throws ComponentException, RemoteException;

    String getVal02(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    String getVal02(UserContext param0, String param1) throws ComponentException, RemoteException;

    String getVal03(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    String getVal03(UserContext param0, String param1) throws ComponentException, RemoteException;

    String getVal04(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, RemoteException;

    String getVal04(UserContext param0, String param1) throws ComponentException, RemoteException;

    Boolean isAttivoOrdini(UserContext param0) throws ComponentException, RemoteException;

    String getCdrPersonale(UserContext param0, Integer param1) throws ComponentException, RemoteException;

    String getUoRagioneria(UserContext param0, Integer param1) throws ComponentException, RemoteException;

    String getUoDistintaTuttaSac(UserContext param0, Integer param1) throws ComponentException, RemoteException;

    Boolean isUOSpecialeDistintaTuttaSAC(UserContext param0, Integer param1, String param2) throws ComponentException, RemoteException;

    String getCdsSAC(UserContext userContext, Integer esercizio) throws ComponentException, RemoteException;

    Boolean isEconomicaPatrimonialeAttivaImputazioneManuale(UserContext userContext) throws ComponentException, RemoteException;

    Boolean getGestioneImpegnoChiusuraForzataCompetenza(UserContext userContext) throws ComponentException, java.rmi.RemoteException;

    Boolean getGestioneImpegnoChiusuraForzataResiduo(UserContext userContext) throws ComponentException, java.rmi.RemoteException;

    Boolean isAttivaEconomica(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isAttivaEconomicaPura(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isAttivaEconomicaParallela(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isBloccoScrittureProposte(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isAssPrgAnagraficoAttiva(UserContext param0) throws ComponentException, RemoteException;

    Boolean isImpegnoPluriennaleAttivo(UserContext param0) throws ComponentException, RemoteException;

    Boolean isAccertamentoPluriennaleAttivo(UserContext param0) throws ComponentException, RemoteException;

    Boolean isVariazioneAutomaticaSpesa(UserContext userContext) throws ComponentException, RemoteException;

    java.lang.Integer getCdTerzoDiversiStipendi(UserContext userContext) throws ComponentException, RemoteException;

    String getContoCorrenteEnte(UserContext userContext, Integer esercizio) throws ComponentException, java.rmi.RemoteException;
    Timestamp getDataFineValiditaCaricoFamiliare(UserContext userContext, String tiPersona) throws ComponentException, RemoteException;

}