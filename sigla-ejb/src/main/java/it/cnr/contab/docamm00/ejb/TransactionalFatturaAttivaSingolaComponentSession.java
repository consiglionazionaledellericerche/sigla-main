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

package it.cnr.contab.docamm00.ejb;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

public class TransactionalFatturaAttivaSingolaComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements FatturaAttivaSingolaComponentSession {
public it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk addebitaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1,java.util.List param2,java.util.Hashtable param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk)invoke("addebitaDettagli",new Object[] {
			param0,
			param1,
			param2,
			param3 });
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
public void preparaProtocollazioneEProtocolla(UserContext userContext, Long pgProtocollazione, Integer offSet, Long pgStampa, java.sql.Timestamp dataStampa,Fattura_attivaBulk fattura) throws PersistencyException, ComponentException, java.rmi.RemoteException{
	try {
		invoke("preparaProtocollazioneEProtocolla",new Object[] {
			userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura });
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
public void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,java.lang.String param3,java.lang.Integer param4,java.lang.Long param5,java.lang.String param6) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("aggiornaStatoDocumentiAmministrativi",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6 });
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
public void annullaSelezionePerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("annullaSelezionePerStampa",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk)invoke("calcoloConsuntivi",new Object[] {
			param0,
			param1 });
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
public java.lang.Long callGetPgPerProtocolloIVA(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.Long)invoke("callGetPgPerProtocolloIVA",new Object[] {
			param0 });
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
public java.lang.Long callGetPgPerStampa(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.Long)invoke("callGetPgPerStampa",new Object[] {
			param0 });
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
public void cancellaDatiPerProtocollazioneIva(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.lang.Long param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("cancellaDatiPerProtocollazioneIva",new Object[] {
			param0,
			param1,
			param2 });
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
public void cancellaDatiPerStampaIva(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.lang.Long param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("cancellaDatiPerStampaIva",new Object[] {
			param0,
			param1,
			param2 });
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
public it.cnr.jada.util.RemoteIterator cercaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_accertamentiVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaAccertamenti",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk cercaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk)invoke("cercaCambio",new Object[] {
			param0,
			param1 });
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
public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaDettagliFatturaPerNdC",new Object[] {
			param0,
			param1 });
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
public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaDettagliFatturaPerNdD",new Object[] {
			param0,
			param1 });
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
public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaFatturaPerNdC",new Object[] {
			param0,
			compoundfindclause,
			param1 });
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
public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaFatturaPerNdD",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk completaCliente(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk)invoke("completaCliente",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk)invoke("completaTerzo",new Object[] {
			param0,
			param1,
			param2 });
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
public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk)invoke("contabilizzaDettagliSelezionati",new Object[] {
			param0,
			param1,
			param2,
			param3 });
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
public void controllaQuadraturaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("controllaQuadraturaAccertamenti",new Object[] {
			param0,
			param1 });
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
public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("creaConBulk",new Object[] {
			param0,
			param1,
			param2 });
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
public void eliminaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("eliminaRiga",new Object[] {
			param0,
			param1 });
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
public boolean esistonoDatiPerProtocollazioneIva(it.cnr.jada.UserContext param0,java.lang.Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException {
	try {
		return ((Boolean)invoke("esistonoDatiPerProtocollazioneIva",new Object[] {
			param0,
			param1 })).booleanValue();
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public boolean esistonoDatiPerStampaIva(it.cnr.jada.UserContext param0,java.lang.Long param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException {
	try {
		return ((Boolean)invoke("esistonoDatiPerStampaIva",new Object[] {
			param0,
			param1 })).booleanValue();
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.Vector)invoke("estraeSezionali",new Object[] {
			param0,
			param1 });
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
public java.util.Vector estraeSezionaliPerRistampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.util.Vector param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.Vector)invoke("estraeSezionaliPerRistampa",new Object[] {
			param0,
			param1,
			param2 });
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
public it.cnr.jada.util.RemoteIterator findAccertamentiFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.math.BigDecimal param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("findAccertamentiFor",new Object[] {
			param0,
			param1,
			param2 });
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
public java.util.List findDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	try {
		return (java.util.List)invoke("findDettagli",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.IntrospectionException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.util.List recuperoFattureElettronicheSenzaNotificaConsegna(UserContext param0, Unita_organizzativaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	try {
		return (java.util.List)invoke("recuperoFattureElettronicheSenzaNotificaConsegna",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.IntrospectionException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}

public Fattura_attivaBulk aggiornaFatturaScartoSDI(UserContext param0, Fattura_attivaBulk param1, String param2, String param3) throws PersistencyException, ComponentException,java.rmi.RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("aggiornaFatturaScartoSDI",new Object[] {
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
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public Fattura_attivaBulk aggiornaFatturaEsitoAccettatoSDI(UserContext param0, Fattura_attivaBulk param1) throws PersistencyException, ComponentException,java.rmi.RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("aggiornaFatturaEsitoAccettatoSDI",new Object[] {
				param0,
				param1});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public Fattura_attivaBulk aggiornaFatturaDecorrenzaTerminiSDI(UserContext param0, Fattura_attivaBulk param1, String param2) throws PersistencyException, ComponentException,java.rmi.RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("aggiornaFatturaDecorrenzaTerminiSDI",new Object[] {
				param0,
				param1,
				param2});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public Fattura_attivaBulk aggiornaFatturaRifiutataDestinatarioSDI(UserContext param0, Fattura_attivaBulk param1, String param2) throws PersistencyException, ComponentException,java.rmi.RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("aggiornaFatturaRifiutataDestinatarioSDI",new Object[] {
				param0,
				param1,
				param2});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public Fattura_attivaBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext param0, Fattura_attivaBulk param1, String param2, String param3) throws PersistencyException, ComponentException,java.rmi.RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("aggiornaFatturaMancataConsegnaInvioSDI",new Object[] {
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
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public Fattura_attivaBulk aggiornaFatturaRicevutaConsegnaInvioSDI(UserContext param0, Fattura_attivaBulk param1, String param2, XMLGregorianCalendar param3) throws PersistencyException, ComponentException,java.rmi.RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("aggiornaFatturaRicevutaConsegnaInvioSDI",new Object[] {
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
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public Fattura_attivaBulk recuperoFatturaElettronicaDaNomeFile(UserContext param0, String param1) throws PersistencyException, ComponentException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("recuperoFatturaElettronicaDaNomeFile",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.IntrospectionException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException {
	try {
		return (java.util.Collection)invoke("findListabanche",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.util.Vector findListabancheuo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException {
	try {
		return (java.util.Vector)invoke("findListabancheuo",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("findNotaDiCreditoFor",new Object[] {
			param0,
			param1 });
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
public it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("findNotaDiDebitoFor",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk findTariffario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	try {
		return (it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk)invoke("findTariffario",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.IntrospectionException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public boolean hasFatturaAttivaARowNotInventoried(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("hasFatturaAttivaARowNotInventoried",new Object[] {
			param0,
			param1 })).booleanValue();
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
public void inizializzaSelezionePerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("inizializzaSelezionePerStampa",new Object[] {
			param0,
			param1 });
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
public void inserisciRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("inserisciRiga",new Object[] {
			param0,
			param1 });
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
public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("modificaConBulk",new Object[] {
			param0,
			param1,
			param2 });
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
public java.lang.Integer modificaSelezionePerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4,java.lang.Long param5,java.lang.Integer param6,java.lang.Long param7,java.sql.Timestamp param8) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.Integer)invoke("modificaSelezionePerStampa",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5,
			param6,
			param7,
			param8 });
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
public void protocolla(it.cnr.jada.UserContext param0,java.sql.Timestamp param1,java.lang.Long param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("protocolla",new Object[] {
			param0,
			param1,
			param2 });
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
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk)invoke("riportaAvanti",new Object[] {
			param0,
			param1,
			param2 });
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
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk)invoke("riportaIndietro",new Object[] {
			param0,
			param1 });
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
public void rollbackToSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("rollbackToSavePoint",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk selezionaTuttiPerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk)invoke("selezionaTuttiPerStampa",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk selezionaTuttiPerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk)invoke("selezionaTuttiPerStampa",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk setContoEnteIn(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.util.List param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk)invoke("setContoEnteIn",new Object[] {
			param0,
			param1,
			param2 });
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
public void setSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("setSavePoint",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk stornaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1,java.util.List param2,java.util.Hashtable param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk)invoke("stornaDettagli",new Object[] {
			param0,
			param1,
			param2,
			param3 });
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
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk)invoke("update",new Object[] {
			param0,
			param1 });
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
public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk)invoke("updateImportoAssociatoDocAmm",new Object[] {
			param0,
			param1 });
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
public void validaFattura(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("validaFattura",new Object[] {
			param0,
			param1 });
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
public void validaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("validaRiga",new Object[] {
			param0,
			param1 });
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
public void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("verificaEsistenzaEdAperturaInventario",new Object[] {
			param0,
			param1 });
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
public boolean verificaStatoEsercizio(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("verificaStatoEsercizio",new Object[] {
			param0,
			param1 })).booleanValue();
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

//aggiunto per testare l'esercizio della data competenza da/a
public boolean isEsercizioChiusoPerDataCompetenza(it.cnr.jada.UserContext param0,Integer param1,java.lang.String param2) throws ComponentException, PersistencyException, RemoteException{
	try {
		return ((Boolean)invoke("isEsercizioChiusoPerDataCompetenza",new Object[] {
			param0,
			param1, 
			param2})).booleanValue();
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
public Boolean ha_beniColl(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("ha_beniColl",new Object[] {
			param0,
			param1 }));
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
public void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk param1,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		invoke("rimuoviDaAssociazioniInventario",new Object[] {
			param0,
			param1,
			param2 });
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
public it.cnr.jada.util.RemoteIterator selectBeniFor(it.cnr.jada.UserContext param0,Fattura_attivaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("selectBeniFor",new Object[] {
			param0,
			param1 });
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


public it.cnr.jada.bulk.OggettoBulk rebuildDocumento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("rebuildDocumento",new Object[] {
			param0,
			param1 });
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
public it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaObbligazioni",new Object[] {
			param0,
			param1 });
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
public byte[] lanciaStampa(it.cnr.jada.UserContext userContext,Long pg_stampa)
		throws PersistencyException, ComponentException, RemoteException {
	try {
		return ( byte[])invoke("lanciaStampa",new Object[] {
				userContext,
				pg_stampa
				});
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
public it.cnr.jada.bulk.OggettoBulk completaOggetto(it.cnr.jada.UserContext auc, it.cnr.jada.bulk.OggettoBulk oggetto)
		throws PersistencyException, ComponentException, RemoteException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("completaOggetto",new Object[] {
				auc,
				oggetto
				});
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

public void gestioneAllegatiPerFatturazioneElettronica(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk testata)
		throws ComponentException, RemoteException {
	try {
		invoke("gestioneAllegatiPerFatturazioneElettronica",new Object[] {
				userContext,
				testata
				});
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

public boolean VerificaDuplicati(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk testata)
		throws ComponentException, RemoteException {
	try {
		return ((Boolean)invoke("VerificaDuplicati",new Object[] {
				userContext,
				testata
				})).booleanValue();
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
public java.util.List findListaBancheWS(it.cnr.jada.UserContext userContext, String terzo,
		String modalita, String query, String dominio, String tipoRicerca)
		throws ComponentException, RemoteException {
	try {
		return (java.util.List)invoke("findListaBancheWS",new Object[] {
				userContext,
				terzo,
				modalita,
				query,
				dominio,
				tipoRicerca});
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
public java.util.List findListaModalitaPagamentoWS(it.cnr.jada.UserContext userContext, String terzo,
		String query, String dominio, String tipoRicerca)
		throws ComponentException, RemoteException {
	try {
		return (java.util.List)invoke("findListaModalitaPagamentoWS",new Object[] {
				userContext,
				terzo,
				query,
				dominio,
				tipoRicerca});
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
public java.util.List findListaRigheperNCWS(it.cnr.jada.UserContext param0,String param1,String param2,String param3,String param4,String param5,
		String param6,String param7)throws ComponentException, RemoteException {
	try {
		return (java.util.List)invoke("findListaRigheperNCWS",new Object[] {
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
public java.util.List recuperoScadVoce(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk)
		throws ComponentException, RemoteException {
	try {
		return (java.util.List)invoke("recuperoScadVoce",new Object[] {
				userContext,
				bulk});
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
public Fattura_attivaBulk ricercaFattura(it.cnr.jada.UserContext userContext,
		Long esercizio, String cd_cds, String cd_unita_organizzativa,
		Long pg_fattura) throws ComponentException, RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("ricercaFattura",new Object[] {
				userContext,
				esercizio,
				cd_cds,
				cd_unita_organizzativa,
				pg_fattura});
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
public Fattura_attivaBulk ricercaFatturaTrovato(it.cnr.jada.UserContext userContext,
		Long esercizio, String cd_cds, String cd_unita_organizzativa,
		Long pg_fattura) throws ComponentException, RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("ricercaFatturaTrovato",new Object[] {
				userContext,
				esercizio,
				cd_cds,
				cd_unita_organizzativa,
				pg_fattura});
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
public Fattura_attivaBulk ricercaFatturaByKey(it.cnr.jada.UserContext userContext,
		Long esercizio, String cd_cds, String cd_unita_organizzativa,
		Long pg_fattura) throws ComponentException, RemoteException {
	try {
		return (Fattura_attivaBulk)invoke("ricercaFatturaByKey",new Object[] {
				userContext,
				esercizio,
				cd_cds,
				cd_unita_organizzativa,
				pg_fattura});
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
public java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk> ricercaFattureTrovato(it.cnr.jada.UserContext userContext,
		Long trovato) throws ComponentException, RemoteException {
	try {
		return (java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk>)invoke("ricercaFattureTrovato",new Object[] {
				userContext,
				trovato});
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
public java.util.List findManRevRigaCollegati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	try {
		return (java.util.List)invoke("findManRevRigaCollegati",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.IntrospectionException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
public java.util.List findListaBeneServizioWS(it.cnr.jada.UserContext userContext, 
		String query, String tipo,String dominio, String tipoRicerca)
		throws ComponentException, RemoteException {
	try {
		return (java.util.List)invoke("findListaBeneServizioWS",new Object[] {
				userContext,
				query,
				tipo,
				dominio,
				tipoRicerca});
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
public List findListaCodiciCpaWS(UserContext userContext, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (java.util.List)invoke("findListaCodiciCpaWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
public List findListaModalitaErogazioneWS(UserContext userContext,
		String query, String dominio, String tipoRicerca)
		throws ComponentException, RemoteException {
	try {
		return (java.util.List)invoke("findListaModalitaErogazioneWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
public List findListaModalitaIncassoWS(UserContext userContext, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (java.util.List)invoke("findListaModalitaIncassoWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
public List findListaNazioneWS(UserContext userContext, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (java.util.List)invoke("findListaNazioneWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
public List findListaNomenclaturaCombinataWS(UserContext userContext, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (java.util.List)invoke("findListaNomenclaturaCombinataWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
public List findListaNaturaTransazioneWS(UserContext userContext, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (java.util.List)invoke("findListaNaturaTransazioneWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
public List findListaModalitaTrasportoWS(UserContext userContext, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (java.util.List)invoke("findListaModalitaTrasportoWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
public List findListaCondizioneConsegnaWS(UserContext userContext, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (java.util.List)invoke("findListaCondizioneConsegnaWS",new Object[] {
				userContext,
				query,
				dominio,
				tipoRicerca});
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
	public List findListaProvinciaWS(UserContext userContext, String query,
			String dominio, String tipoRicerca) throws ComponentException,
			RemoteException {
		try {
			return (java.util.List)invoke("findListaProvinciaWS",new Object[] {
					userContext,
					query,
					dominio,
					tipoRicerca});
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
	public java.lang.Long inserisciDatiPerStampaIva(it.cnr.jada.UserContext userContext, Long esercizio,
			String cd_cds, String cd_unita_organizzativa, Long pg_fattura)
			throws PersistencyException, ComponentException, RemoteException {
		try {
			return (Long)invoke("inserisciDatiPerStampaIva",new Object[] {
					userContext,
					esercizio,
					cd_cds,
					cd_unita_organizzativa,
					pg_fattura
					});
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
	public Nota_di_credito_attivaBulk generaNotaCreditoAutomatica(it.cnr.jada.UserContext userContext, Fattura_attiva_IBulk fa, Integer esercizio) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		try {
			return (Nota_di_credito_attivaBulk)invoke("generaNotaCreditoAutomatica",new Object[] {
					userContext,
					fa,
					esercizio});
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
	public Fattura_attivaBulk ricercaFatturaDaCodiceSDI(UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
		try {
			return (Fattura_attivaBulk)invoke("ricercaFatturaDaCodiceSDI",new Object[] {
					userContext,
					codiceInvioSdi});
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
	public Fattura_attiva_IBulk ricercaFatturaSDI(UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
		try {
			return (Fattura_attiva_IBulk)invoke("ricercaFatturaSDI",new Object[] {
					userContext,
					codiceInvioSdi});
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
	public Fattura_attivaBulk aggiornaFatturaConsegnaSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, Date dataConsegnaSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		try {
			return (Fattura_attivaBulk)invoke("aggiornaFatturaConsegnaSDI",new Object[] {
					userContext,
					fatturaAttiva,
					dataConsegnaSdi});
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
	public Fattura_attivaBulk aggiornaFatturaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		try {
			return (Fattura_attivaBulk)invoke("aggiornaFatturaInvioSDI",new Object[] {
					userContext,
					fatturaAttiva});
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
	public Fattura_attivaBulk aggiornaFatturaPredispostaAllaFirma(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		try {
			return (Fattura_attivaBulk)invoke("aggiornaFatturaPredispostaAllaFirma",new Object[] {
					userContext,
					fatturaAttiva});
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
	public Fattura_attiva_IBulk aggiornaDatiFatturaSDI(UserContext userContext, String codiceInvioSdi, String statoInvioSdi, String noteInvioSdi, XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
		try {
			return (Fattura_attiva_IBulk)invoke("aggiornaDatiFatturaSDI",new Object[] {
					userContext,
					codiceInvioSdi,
					statoInvioSdi,
					dataConsegnaSdi,
					stornaFattura});
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
	public Fattura_attiva_IBulk aggiornaDatiFatturaSDI(UserContext userContext, Fattura_attiva_IBulk fatturaAttiva, String statoInvioSdi, String noteInvioSdi, XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
		try {
			return (Fattura_attiva_IBulk)invoke("aggiornaDatiFatturaSDI",new Object[] {
					userContext,
					fatturaAttiva,
					statoInvioSdi,
					dataConsegnaSdi,
					stornaFattura});
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
	public boolean isAttivoSplitPayment(UserContext param0,
			Timestamp param1) throws PersistencyException,
			ComponentException, RemoteException {
		try {
			return ((Boolean)invoke("isAttivoSplitPayment",new Object[] {
				param0,
				param1})).booleanValue();
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
	
	public Fattura_attivaBulk aggiornaFatturaTrasmissioneNonRecapitataSDI(UserContext param0, Fattura_attivaBulk param1, String param2, String param3) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		try {
			return (Fattura_attivaBulk)invoke("aggiornaFatturaTrasmissioneNonRecapitataSDI",new Object[] {
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
			} catch(it.cnr.jada.persistency.PersistencyException ex) {
				throw ex;
			} catch(Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception",ex);
			}
		}
	}

	public BigDecimal getImportoBolloVirtuale(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException,java.rmi.RemoteException {
		try {
			return (BigDecimal)invoke("getImportoBolloVirtuale",new Object[] {
					userContext,
					fattura});
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

	public void controlliGestioneBolloVirtuale(UserContext param0, Fattura_attivaBulk fatturaAttiva, BulkList dettaglio) throws ComponentException,java.rmi.RemoteException,javax.ejb.EJBException  {
		try {
			invoke("controlliGestioneBolloVirtuale",new Object[] {param0,fatturaAttiva, dettaglio});
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

	public String recuperoEmailUtente(UserContext param0, Fattura_attivaBulk fatturaAttiva) throws ComponentException,java.rmi.RemoteException,javax.ejb.EJBException  {
		try {
			return (String)invoke("recuperoEmailUtente",new Object[] {param0,fatturaAttiva});
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

	public void gestioneAvvisoInvioMailFattureAttive(UserContext param0) throws ComponentException,java.rmi.RemoteException,javax.ejb.EJBException  {
		try {
			invoke("gestioneAvvisoInvioMailFattureAttive",new Object[] {param0});
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
