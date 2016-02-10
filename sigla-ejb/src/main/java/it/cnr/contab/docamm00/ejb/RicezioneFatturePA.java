package it.cnr.contab.docamm00.ejb;

import it.cnr.jada.comp.ComponentException;

import java.math.BigInteger;
import java.util.Date;

import javax.activation.DataHandler;
import javax.ejb.Local;
import javax.mail.Message;
@Local
public interface RicezioneFatturePA {
	public void riceviFatturaSIGLA(BigInteger identificativoSdI, String nomeFile, String replyTo, DataHandler file, String nomeFileMetadati,DataHandler metadati) throws ComponentException;
	public void notificaDecorrenzaTermini(BigInteger identificativoSdI, String nomeFile, DataHandler file);
	public void notificaDecorrenzaTermini(String nomeFile, DataHandler data) throws ComponentException;
	public void notificaScartoEsito(String nomeFile, DataHandler data, Date dataRicevimento) throws ComponentException;
	public void notificaFatturaPassivaConsegnaEsitoPec(String idSdI, Date dataRicevimento) throws ComponentException;
	public void notificaScartoMailNotificaNonRicevibile(Message message, String idSdi, Date dataRicevimentoMail) throws ComponentException;
}
