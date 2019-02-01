package it.cnr.contab.util;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.jada.bulk.ValidationException;
import org.junit.Test;

public class ValidaIBAN {

    @Test
    public void verificaIBAN() {
        BancaBulk bancaBulk = new BancaBulk();
        NazioneBulk nazioneBulk = new NazioneBulk();
        nazioneBulk.setCd_nazione("IT");
        nazioneBulk.setCd_iso("IT");
        nazioneBulk.setStruttura_iban("NN;A;NNNNN;NNNNN;CCCCCCCCCCCC");
        bancaBulk.setNazione_iban(nazioneBulk);
        bancaBulk.setCodice_iban_parte1("10");
        bancaBulk.setCodice_iban_parte2("H");
        bancaBulk.setCodice_iban_parte3("03062");
        bancaBulk.setCodice_iban_parte4("34210");
        bancaBulk.setCodice_iban_parte5("000000192294");

        bancaBulk.setCodice_iban("IT10H0306234210000000192294");
        try {
            bancaBulk.validaIban();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

}
