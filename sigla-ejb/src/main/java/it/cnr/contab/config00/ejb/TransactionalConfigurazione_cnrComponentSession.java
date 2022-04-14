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

import java.rmi.RemoteException;
import java.sql.Timestamp;

public class TransactionalConfigurazione_cnrComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements Configurazione_cnrComponentSession {
    public it.cnr.contab.config00.bulk.Configurazione_cnrBulk getConfigurazione(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.config00.bulk.Configurazione_cnrBulk) invoke("getConfigurazione", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt01", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt01", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt02", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt02", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm01", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm01", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm02", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm02", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal01", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal01", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal02", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal02", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal03", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal03", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal04", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal04", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.Boolean isAttivoOrdini(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.Boolean) invoke("isAttivoOrdini", new Object[]{
                    param0});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getCdrPersonale(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getCdrPersonale", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getUoRagioneria(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getUoRagioneria", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getUoDistintaTuttaSac(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getUoDistintaTuttaSac", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.Boolean isUOSpecialeDistintaTuttaSAC(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.Boolean) invoke("isUOSpecialeDistintaTuttaSAC", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
    public String getCdsSAC(UserContext userContext, Integer esercizio)  throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (String) invoke("getCdsSAC", new Object[]{
                    userContext,
                    esercizio});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
    public java.lang.Boolean isEconomicaPatrimonialeAttivaImputazioneManuale(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.Boolean) invoke("isEconomicaPatrimonialeAttivaImputazioneManuale", new Object[]{
                    param0});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean getGestioneImpegnoChiusuraForzataCompetenza(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (java.lang.Boolean) invoke("getGestioneImpegnoChiusuraForzataCompetenza", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean getGestioneImpegnoChiusuraForzataResiduo(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (java.lang.Boolean) invoke("getGestioneImpegnoChiusuraForzataResiduo", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isAttivaEconomica(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (java.lang.Boolean) invoke("isAttivaEconomica", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isAttivaEconomicaPura(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (java.lang.Boolean) invoke("isAttivaEconomicaPura", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isAttivaEconomicaParallela(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (java.lang.Boolean) invoke("isAttivaEconomicaParallela", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isBloccoScrittureProposte(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (java.lang.Boolean) invoke("isBloccoScrittureProposte", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isVariazioneAutomaticaSpesa(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (java.lang.Boolean) invoke("isVariazioneAutomaticaSpesa", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
    public Integer getCdTerzoDiversiStipendi(UserContext userContext)  throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (Integer) invoke("getCdTerzoDiversiStipendi", new Object[]{
                    userContext});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
    public Timestamp getDataFineValiditaCaricoFamiliare(UserContext userContext, String tiPersona)  throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (Timestamp) invoke("getDataFineValiditaCaricoFamiliare", new Object[]{
                    userContext, tiPersona});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getContoCorrenteEnte(UserContext userContext, Integer esercizio)  throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (String) invoke("getContoCorrenteEnte", new Object[]{
                    userContext,
                    esercizio});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
}
