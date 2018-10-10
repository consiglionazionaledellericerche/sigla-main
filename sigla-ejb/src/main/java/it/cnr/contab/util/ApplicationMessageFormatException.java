package it.cnr.contab.util;

import it.cnr.jada.comp.ApplicationException;

import java.io.Serializable;
import java.text.MessageFormat;

public class ApplicationMessageFormatException extends ApplicationException {
    public ApplicationMessageFormatException(String message, Serializable... parameter) {
        super(MessageFormat.format(message, parameter));
    }
}
