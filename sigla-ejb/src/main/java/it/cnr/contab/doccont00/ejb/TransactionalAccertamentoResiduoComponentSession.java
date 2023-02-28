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

package it.cnr.contab.doccont00.ejb;

import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;
import java.util.Vector;

public class TransactionalAccertamentoResiduoComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements AccertamentoResiduoComponentSession {
    public void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1, java.util.Map param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("aggiornaCogeCoanInDifferita", new Object[]{
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

    public void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1, java.util.Map param2, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("aggiornaSaldiInDifferita", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public it.cnr.contab.doccont00.core.bulk.AccertamentoBulk aggiornaScadenzarioSuccessivoAccertamento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk) invoke("aggiornaScadenzarioSuccessivoAccertamento", new Object[]{
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

    public it.cnr.contab.doccont00.core.bulk.AccertamentoBulk annullaAccertamento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk) invoke("annullaAccertamento", new Object[]{
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

    public void callRiportaAvanti(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("callRiportaAvanti", new Object[]{
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

    public void callRiportaIndietro(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("callRiportaIndietro", new Object[]{
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

    public it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk cancellaDettagliScadenze(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1, it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk) invoke("cancellaDettagliScadenze", new Object[]{
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

    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
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

    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2, it.cnr.jada.bulk.OggettoBulk param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
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

    public it.cnr.contab.doccont00.core.bulk.AccertamentoBulk creaAccertamentoDiSistema(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoAccreditamento_rigaBulk param1, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk) invoke("creaAccertamentoDiSistema", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk[] creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("creaConBulk", new Object[]{
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

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
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

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
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

    public it.cnr.contab.doccont00.core.bulk.AccertamentoBulk generaDettagliScadenzaAccertamento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1, it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk) invoke("generaDettagliScadenzaAccertamento", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerInserimento", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerModifica", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk[] inizializzaBulkPerModifica(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("inizializzaBulkPerModifica", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerRicerca", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerRicercaLibera", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerStampa", new Object[]{
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

    public java.util.Vector listaCodiciNaturaPerCapitolo(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.util.Vector) invoke("listaCodiciNaturaPerCapitolo", new Object[]{
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

    public java.util.Vector listaLineeAttivitaPerCapitolo(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.util.Vector) invoke("listaLineeAttivitaPerCapitolo", new Object[]{
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

    public void lockScadenza(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("lockScadenza", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk[] modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("modificaConBulk", new Object[]{
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

    public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, java.math.BigDecimal param2, boolean param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk) invoke("modificaScadenzaInAutomatico", new Object[]{
                    param0,
                    param1,
                    param2,
					Boolean.valueOf(param3)});
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

    public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, java.math.BigDecimal param2, boolean param3, Boolean aggiornaCalcoloAutomatico) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk) invoke("modificaScadenzaInAutomatico", new Object[]{
                    param0,
                    param1,
                    param2,
					Boolean.valueOf(param3),
                    aggiornaCalcoloAutomatico});
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

    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("stampaConBulk", new Object[]{
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

    public void verificaAccertamento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("verificaAccertamento", new Object[]{
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

    public it.cnr.contab.doccont00.core.bulk.AccertamentoBulk verificaScadenzarioAccertamento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk) invoke("verificaScadenzarioAccertamento", new Object[]{
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

    public it.cnr.contab.doccont00.core.bulk.AccertamentoBulk verificaScadenzarioAccertamento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1, boolean param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk) invoke("verificaScadenzarioAccertamento", new Object[]{
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

    public Vector listaCdrPerCapitoli(UserContext param0, AccertamentoBulk param1) throws ComponentException, RemoteException {
        try {
            return (Vector) invoke("listaCdrPerCapitoli", new Object[]{
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

    public void validaContratto(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1, it.cnr.contab.config00.contratto.bulk.ContrattoBulk param2, it.cnr.jada.persistency.sql.CompoundFindClause param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("validaContratto", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, java.math.BigDecimal param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk) invoke("sdoppiaScadenzaInAutomatico", new Object[]{
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

    public String controllaDettagliScadenzaAccertamento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1, it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (String) invoke("controllaDettagliScadenzaAccertamento", new Object[]{
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

    public void cancellaAccertamentoModTemporanea(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException {
        try {
            invoke("cancellaAccertamentoModTemporanea", new Object[]{
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

    public PrimaryKeyHashtable getOldRipartizioneCdrVoceLinea(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (PrimaryKeyHashtable) invoke("getOldRipartizioneCdrVoceLinea", new Object[]{
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

    @Override
    public AccertamentoBulk aggiornaAccertamentiTemporanei(UserContext param0, AccertamentoBulk param1) throws ComponentException, RemoteException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk) invoke("aggiornaAccertamentiTemporanei", new Object[]{
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
}
