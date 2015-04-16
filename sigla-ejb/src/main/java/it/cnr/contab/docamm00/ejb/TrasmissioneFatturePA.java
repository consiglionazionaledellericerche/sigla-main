package it.cnr.contab.docamm00.ejb;

import java.util.Date;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.gov.fatturapa.sdi.messaggi.v1.AttestazioneTrasmissioneFatturaType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaDecorrenzaTerminiType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaMancataConsegnaType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaScartoType;
import it.gov.fatturapa.sdi.messaggi.v1.RicevutaConsegnaType;

import javax.activation.DataHandler;
import javax.ejb.Local;
@Local
public interface TrasmissioneFatturePA {
	public void notificaFatturaAttivaRicevutaConsegna(UserContext userContext, String nomeFile, DataHandler data, RicevutaConsegnaType ricevuta) throws ComponentException;
	public void notificaFatturaAttivaMancataConsegna(UserContext userContext, String nomeFile, DataHandler data, NotificaMancataConsegnaType mancataConsegna) throws ComponentException;
	public void notificaFatturaAttivaScarto(UserContext userContext, String nomeFile, DataHandler data, NotificaScartoType notifica) throws ComponentException;
	public void notificaFatturaAttivaDecorrenzaTermini(UserContext userContext, String nomeFile, DataHandler data, NotificaDecorrenzaTerminiType notifica) throws ComponentException;
	public void notificaFatturaAttivaEsito(UserContext userContext, String nomeFile, DataHandler data, NotificaEsitoType notifica) throws ComponentException;
	public void notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(UserContext userContext, String nomeFile, DataHandler data, AttestazioneTrasmissioneFatturaType notifica) throws ComponentException;
	public void notificaFatturaAttivaConsegnaPec(UserContext userContext, String nomeFile, Date dataConsegna) throws ComponentException;
}
