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

public class TransactionalConfigurazione_cnrComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements Configurazione_cnrComponentSession {
    public it.cnr.contab.config00.bulk.Configurazione_cnrBulk getConfigurazione(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (it.cnr.contab.config00.bulk.Configurazione_cnrBulk) invoke("getConfigurazione", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt01(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt01", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt01(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt01", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt02(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt02", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.sql.Timestamp getDt02(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt02", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm01(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm01", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm01(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm01", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm02(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm02", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.math.BigDecimal getIm02(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm02", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal01(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal01", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal01(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal01", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal02(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal02", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal02(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal02", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal03(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal03", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal03(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal03", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal04(UserContext param0, Integer param1, String param2, String param3, String param4) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal04", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getVal04(UserContext param0, String param1) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getVal04", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public Boolean isAttivoOrdini(UserContext param0) throws RemoteException, ComponentException {
        try {
            return (Boolean) invoke("isAttivoOrdini", new Object[]{
                    param0});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getCdrPersonale(UserContext param0, Integer param1) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getCdrPersonale", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getUoRagioneria(UserContext param0, Integer param1) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getUoRagioneria", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public String getUoDistintaTuttaSac(UserContext param0, Integer param1) throws RemoteException, ComponentException {
        try {
            return (String) invoke("getUoDistintaTuttaSac", new Object[]{
                    param0,
                    param1});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public Boolean isUOSpecialeDistintaTuttaSAC(UserContext param0, Integer param1, String param2) throws RemoteException, ComponentException {
        try {
            return (Boolean) invoke("isUOSpecialeDistintaTuttaSAC", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }
    public String getCdsSAC(UserContext userContext, Integer esercizio)  throws RemoteException, ComponentException {
        try {
            return (String) invoke("getCdsSAC", new Object[]{
                    userContext,
                    esercizio});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }
    public Boolean isEconomicaPatrimonialeAttivaImputazioneManuale(UserContext param0) throws RemoteException, ComponentException {
        try {
            return (Boolean) invoke("isEconomicaPatrimonialeAttivaImputazioneManuale", new Object[]{
                    param0});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean getGestioneImpegnoChiusuraForzataCompetenza(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("getGestioneImpegnoChiusuraForzataCompetenza", new Object[]{
                    userContext});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean getGestioneImpegnoChiusuraForzataResiduo(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("getGestioneImpegnoChiusuraForzataResiduo", new Object[]{
                    userContext});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
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
            return (Boolean) invoke("isAttivaEconomicaParallela", new Object[]{
                    userContext});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isBloccoScrittureProposte(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("isBloccoScrittureProposte", new Object[]{
                    userContext});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }
    @Override
    public Boolean isAssPrgAnagraficoAttiva(UserContext param0) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("isAssPrgAnagraficoAttiva", new Object[]{
                    param0});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isImpegnoPluriennaleAttivo(UserContext param0) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("isImpegnoPluriennaleAttivo", new Object[]{
                    param0});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Boolean isAccertamentoPluriennaleAttivo(UserContext param0) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("isAccertamentoPluriennaleAttivo", new Object[]{
                    param0});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
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


}

    @Override
    public Boolean isAttachRestContrStoredFromSigla(UserContext userContext) throws ComponentException, RemoteException {
        try {
            return (Boolean) invoke("isAttachRestContrStoredFromSigla", new Object[]{
                    userContext});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

}