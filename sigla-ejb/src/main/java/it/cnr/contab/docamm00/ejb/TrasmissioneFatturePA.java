package it.cnr.contab.docamm00.ejb;

import java.io.InputStream;
import java.util.Date;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.activation.DataHandler;
import javax.ejb.Local;
@Local
public interface TrasmissioneFatturePA {
	public void notificaFatturaAttivaRicevutaConsegna(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaMancataConsegna(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaScarto(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaEsito(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public void notificaFatturaAttivaConsegnaPec(UserContext userContext, String nomeFile, Date dataConsegna) throws ComponentException;
	public Boolean notificaFatturaAttivaDecorrenzaTermini(UserContext userContext, String nomeFile, DataHandler data) throws ComponentException;
	public InputStream mancataConsegnaPecInvioFatturaAttiva(UserContext userContext, String nomeFile) throws ComponentException;
}
