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
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

public class TransactionalSaldoComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements SaldoComponentSession {
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk)invoke("aggiornaMandatiReversali",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4,boolean param5) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk)invoke("aggiornaMandatiReversali",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			new Boolean(param5) });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk)invoke("aggiornaObbligazioniAccertamenti",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4,boolean param5) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk)invoke("aggiornaObbligazioniAccertamenti",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			new Boolean(param5) });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaPagamentiIncassi(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk)invoke("aggiornaPagamentiIncassi",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaMandati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk)invoke("checkDisponabilitaCassaMandati",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk)invoke("checkDisponabilitaCassaObbligazioni",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5, String param6) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaMandatiReversali",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5, String param6, boolean param7) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaMandatiReversali",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6,
			new Boolean(param7)});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk checkDisponabilitaCassaMandati(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, java.math.BigDecimal param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("checkDisponabilitaCassaMandati",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6,String param7) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaObbligazioniAccertamenti",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6,
			param7});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaPagamentiIncassi(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, java.math.BigDecimal param5 ) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaPagamentiIncassi",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public String checkDispObbligazioniAccertamenti(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, String param6 ) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (String)invoke("checkDispObbligazioniAccertamenti",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6, Boolean param7) throws RemoteException,ComponentException{
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaVariazioneStanziamento",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6,
			param7});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, String param5, java.math.BigDecimal param6) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaVariazioneStanziamento",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.math.BigDecimal getTotaleSaldoResidui(it.cnr.jada.UserContext param0, String param1, String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3)  throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.math.BigDecimal)invoke("getTotaleSaldoResidui",new Object[] {
			param0,
			param1,
			param2,
			param3});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public String getMessaggioSfondamentoDisponibilita(it.cnr.jada.UserContext param0, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (String)invoke("getMessaggioSfondamentoDisponibilita",new Object[] {
			param0,
			param1});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaImpegniResiduiPropri(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, BigDecimal param5) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaImpegniResiduiPropri",new Object[] {
			param0,
			param1,
			param3,
			param4,
			param5});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk aggiornaAccertamentiResiduiPropri(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param3, Integer param4, BigDecimal param5) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk)invoke("aggiornaAccertamentiResiduiPropri",new Object[] {
			param0,
			param1,
			param3,
			param4,
			param5});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public void aggiornaSaldiAnniSuccessivi(it.cnr.jada.UserContext param1, String param2, String param3, it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk param4, Integer param5, BigDecimal param6, it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk param7) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("aggiornaSaldiAnniSuccessivi",new Object[] {
			param1,
			param3,
			param4,
			param5,
			param6,
			param7});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public void checkDispPianoEconomicoProgetto(it.cnr.jada.UserContext param1, it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk param2, boolean param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("checkDispPianoEconomicoProgetto",new Object[] {
			param1,
			param2,
			param3});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public void checkDispPianoEconomicoProgetto(it.cnr.jada.UserContext param1, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("checkDispPianoEconomicoProgetto",new Object[] {
			param1,
			param2});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public void checkDispPianoEconomicoProgetto(it.cnr.jada.UserContext param1, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("checkDispPianoEconomicoProgetto",new Object[] {
				param1,
				param2});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public void checkPdgPianoEconomico(it.cnr.jada.UserContext param1, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("checkPdgPianoEconomico",new Object[] {
			param1,
			param2});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public void checkPdgPianoEconomico(it.cnr.jada.UserContext param1, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("checkPdgPianoEconomico",new Object[] {
			param1,
			param2});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.math.BigDecimal getStanziamentoAssestatoProgetto(it.cnr.jada.UserContext param0, ProgettoBulk param1, String param2, Integer param3, Timestamp param4, String param5) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.math.BigDecimal)invoke("getStanziamentoAssestatoProgetto",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
	public void checkBloccoImpegniNatfin(UserContext userContext, String cdr, String cdLineaAttivita, Elemento_voceBulk elementoVoceBulk, String tipoObbligazione) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("checkBloccoDisponibilitaResidue",new Object[] {
					userContext,
					cdr,
					cdLineaAttivita,
					elementoVoceBulk});
		} catch(java.rmi.RemoteException e) {
			throw e;
		} catch(java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch(it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}
	public void checkBloccoImpegniNatfin(UserContext userContext, WorkpackageBulk workpackageBulk, Elemento_voceBulk elementoVoceBulk, String tipoObbligazione) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("checkBloccoDisponibilitaResidue",new Object[] {
					userContext,
					workpackageBulk,
					elementoVoceBulk});
		} catch(java.rmi.RemoteException e) {
			throw e;
		} catch(java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch(it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}
	public void checkBloccoImpegniNatfin(UserContext userContext, Var_stanz_resBulk variazione) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("checkBloccoDisponibilitaResidue",new Object[] {
					userContext,
					variazione});
		} catch(java.rmi.RemoteException e) {
			throw e;
		} catch(java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch(it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}
	public void checkBloccoLimiteClassificazione(UserContext userContext, Pdg_variazioneBulk variazione) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			invoke("checkBloccoLimiteClassificazione",new Object[] {
					userContext,
					variazione});
		} catch(java.rmi.RemoteException e) {
			throw e;
		} catch(java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch(it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}
}
