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

import it.cnr.contab.config00.comp.Configurazione_cnrComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.AdminUserContext;
import it.cnr.jada.comp.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.UUID;

@Stateless(name = "CNRCONFIG00_EJB_Configurazione_cnrComponentSession")
public class Configurazione_cnrComponentSessionBean extends it.cnr.jada.ejb.GenericComponentSessionBean implements Configurazione_cnrComponentSession {
    private Configurazione_cnrComponent componentObj;
    private transient final static Logger logger = LoggerFactory.getLogger(Configurazione_cnrComponentSessionBean.class);

    public static Configurazione_cnrComponentSessionBean newInstance() throws EJBException {
        return new Configurazione_cnrComponentSessionBean();
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    @Remove
    public void ejbRemove() throws EJBException {
        try {
            shutdowHook();
        } catch (ComponentException e) {
            logger.error("ERROR while shutdow hook", e);
        }
        componentObj.release();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new Configurazione_cnrComponent();
    }

    public void shutdowHook() throws ComponentException, EJBException{
        UserContext param0 = new AdminUserContext(UUID.randomUUID().toString());
        pre_component_invocation(param0, componentObj);
        try {
            componentObj.shutdowHook(param0);
            component_invocation_succes(param0, componentObj);
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public it.cnr.contab.config00.bulk.Configurazione_cnrBulk getConfigurazione(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.config00.bulk.Configurazione_cnrBulk result = componentObj.getConfigurazione(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.sql.Timestamp getDt01(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.sql.Timestamp result = componentObj.getDt01(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.sql.Timestamp getDt01(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.sql.Timestamp result = componentObj.getDt01(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.sql.Timestamp getDt02(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.sql.Timestamp result = componentObj.getDt02(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.sql.Timestamp getDt02(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.sql.Timestamp result = componentObj.getDt02(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.math.BigDecimal getIm01(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.math.BigDecimal result = componentObj.getIm01(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.math.BigDecimal getIm01(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.math.BigDecimal result = componentObj.getIm01(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.math.BigDecimal getIm02(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.math.BigDecimal result = componentObj.getIm02(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.math.BigDecimal getIm02(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.math.BigDecimal result = componentObj.getIm02(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal01(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal01(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal01(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal01(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal02(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal02(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal02(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal02(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal03(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal03(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal03(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal03(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal04(UserContext param0, Integer param1, String param2, String param3, String param4) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal04(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getVal04(UserContext param0, String param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getVal04(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public Boolean isAttivoOrdini(UserContext param0) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            Boolean result = componentObj.isAttivoOrdini(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getCdrPersonale(UserContext param0, Integer param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getCdrPersonale(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getUoRagioneria(UserContext param0, Integer param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getUoRagioneria(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public String getUoDistintaTuttaSac(UserContext param0, Integer param1) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            String result = componentObj.getUoDistintaTuttaSac(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public Boolean isUOSpecialeDistintaTuttaSAC(UserContext param0, Integer param1, String param2) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            Boolean result = componentObj.isUOSpecialeDistintaTuttaSAC(param0,param1,param2);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }
    public String getCdsSAC(UserContext userContext, Integer esercizio) throws ComponentException, EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            String result = componentObj.getCdsSAC(userContext, esercizio);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    public Boolean isEconomicaPatrimonialeAttivaImputazioneManuale(UserContext param0) throws ComponentException, EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            Boolean result = componentObj.isEconomicaPatrimonialeAttivaImputazioneManuale(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    @Override
    public Boolean getGestioneImpegnoChiusuraForzataCompetenza(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.getGestioneImpegnoChiusuraForzataCompetenza(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    @Override
    public Boolean getGestioneImpegnoChiusuraForzataResiduo(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.getGestioneImpegnoChiusuraForzataResiduo(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    @Override
    public Boolean isAttivaEconomica(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.isAttivaEconomica(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    @Override
    public Boolean isAttivaEconomicaPura(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.isAttivaEconomicaPura(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    @Override
    public Boolean isAttivaEconomicaParallela(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.isAttivaEconomicaParallela(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    @Override
    public Boolean isBloccoScrittureProposte(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.isBloccoScrittureProposte(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }
    @Override
    public Boolean isAssPrgAnagraficoAttiva(UserContext param0) throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            Boolean result = componentObj.isAssPrgAnagraficoAttiva(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    @Override
    public Boolean isImpegnoPluriennaleAttivo(UserContext param0) throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            Boolean result = componentObj.isImpegnoPluriennaleAttivo(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    @Override
    public Boolean isAccertamentoPluriennaleAttivo(UserContext param0) throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            Boolean result = componentObj.isAccertamentoPluriennaleAttivo(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    @Override
    public Boolean isAttachRestContrStoredFromSigla(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.isAttachRestContrStoredFromSigla(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    @Override
    public Boolean isVariazioneAutomaticaSpesa(UserContext userContext) throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            Boolean result = componentObj.isVariazioneAutomaticaSpesa(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    public Integer getCdTerzoDiversiStipendi(UserContext userContext) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            Integer result = componentObj.getCdTerzoDiversiStipendi(userContext);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

    public String getContoCorrenteEnte(UserContext userContext, Integer esercizio) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            String result = componentObj.getContoCorrenteEnte(userContext, esercizio);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }
    public Timestamp getDataFineValiditaCaricoFamiliare(UserContext userContext, String tiPersona) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            Timestamp result = componentObj.getDataFineValiditaCaricoFamiliare(userContext, tiPersona);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }

}