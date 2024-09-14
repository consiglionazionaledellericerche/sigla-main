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

package it.cnr.contab.firma.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import javax.swing.text.html.Option;
import java.util.Optional;

public class FirmaOTPBulk extends OggettoBulk {
	private static final long serialVersionUID = 1L;
	private String userName, password, otp;
	public FirmaOTPBulk() {
		super();
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		Optional.ofNullable(userName).orElseThrow(() -> new ValidationException("Valorizzare 'Username'!"));
		Optional.ofNullable(password).orElseThrow(() -> new ValidationException("Valorizzare 'password'!"));
		Optional.ofNullable(otp).orElseThrow(() -> new ValidationException("Valorizzare 'OTP'!"));
	}

	public static String errorMessage(String messageException) {
	    if (messageException.contains("0001"))
	    	return "Errore generico nel processo di firma";
    	else if (messageException.contains("0002"))
    		return "Parametri non corretti per il tipo di trasporto indicato";
    	else if (messageException.contains("0003"))
    		return "Errore in fase di verifica delle credenziali";
    	else if (messageException.contains("0004"))
    		return "Errore nel PIN";
    	else if (messageException.contains("0005"))
    		return "Tipo di trasporto non valido";
    	else if (messageException.contains("0006"))
    		return "Tipo di trasporto non autorizzato";
    	else if (messageException.contains("0007"))
    		return "Profilo Di firma PDF non valido";
    	else if (messageException.contains("0008"))
    		return "Impossibile completare l'operazione di marcatura temporale (es irraggiungibilit&agrave; del servizio, marche residue terminate, etc..)";
    	else if (messageException.contains("0009"))
    		return "Credenziali di delega non valide";
    	else if (messageException.contains("0010"))
    		return "Lo stato dell'utente non è valido (es. utente sospeso)";
		else if (messageException.contains("0011"))
			return "Il dispositivo che genera l'OTP va sostituito, contattare il supporto tecnico.";
	    return messageException;
	}
}
