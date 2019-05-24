package it.cnr.contab.progettiric00.comp;

import it.cnr.jada.bulk.ValidationException;

public class RimodulazioneNonApprovataException extends ValidationException {
    public RimodulazioneNonApprovataException()
    {
    }

    public RimodulazioneNonApprovataException(String s)
    {
        super(s);
    }
}
