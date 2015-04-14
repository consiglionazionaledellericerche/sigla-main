package it.cnr.contab.docamm00.ejb;

import it.cnr.jada.comp.ComponentException;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaDecorrenzaTerminiType;

import java.math.BigInteger;

import javax.activation.DataHandler;
import javax.ejb.Local;
@Local
public interface RicezioneFatturePA {
	public void riceviFatturaSIGLA(BigInteger identificativoSdI, String nomeFile, DataHandler file, String nomeFileMetadati,DataHandler metadati) throws ComponentException;
	public void notificaDecorrenzaTermini(BigInteger identificativoSdI, String nomeFile, DataHandler file);
	public void notificaDecorrenzaTermini(String nomeFile, DataHandler data, NotificaDecorrenzaTerminiType notifica) throws ComponentException;
}
