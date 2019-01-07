package it.cnr.contab.config00.ejb;

import it.cnr.contab.config00.bulk.ServizioPecBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;

@Remote
public interface Parametri_enteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
it.cnr.contab.config00.bulk.Parametri_enteBulk getParametriEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
int getGiorniRimanenti(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
ServizioPecBulk getServizioPec(it.cnr.jada.UserContext param0, String param1) throws ComponentException;
CdsBulk getCds(UserContext param0, String param1) throws ComponentException;
Unita_organizzativaBulk getUo(UserContext param0, String param1) throws ComponentException;
CdrBulk getCdr(UserContext param0, String param1) throws ComponentException;
UnitaOrganizzativaPecBulk getUnitaOrganizzativaPec(UserContext param0, String param1) throws ComponentException;
boolean isProgettoPianoEconomicoEnabled(it.cnr.jada.UserContext param0, int param1) throws it.cnr.jada.comp.ComponentException;
}
