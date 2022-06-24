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

    java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.Boolean isAttivoOrdini(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getCdrPersonale(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getUoRagioneria(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getUoDistintaTuttaSac(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.Boolean isUOSpecialeDistintaTuttaSAC(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    String getCdsSAC(UserContext userContext, Integer esercizio) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    Boolean getGestioneImpegnoChiusuraForzataCompetenza(UserContext userContext) throws ComponentException, java.rmi.RemoteException;

    Boolean getGestioneImpegnoChiusuraForzataResiduo(UserContext userContext) throws ComponentException, java.rmi.RemoteException;

    Boolean isAttivaEconomica(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isAttivaEconomicaPura(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isAttivaEconomicaParallela(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isBloccoScrittureProposte(UserContext userContext) throws ComponentException, RemoteException;

    Boolean isVariazioneAutomaticaSpesa(UserContext userContext) throws ComponentException, RemoteException;

    java.lang.Integer getCdTerzoDiversiStipendi(UserContext userContext) throws ComponentException, RemoteException;

    String getContoCorrenteEnte(UserContext userContext, Integer esercizio) throws ComponentException, java.rmi.RemoteException;
    Timestamp getDataFineValiditaCaricoFamiliare(UserContext userContext, String tiPersona) throws ComponentException, RemoteException;

}