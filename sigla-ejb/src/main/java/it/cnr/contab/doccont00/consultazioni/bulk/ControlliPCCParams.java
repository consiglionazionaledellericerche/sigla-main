package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ControlliPCCParams extends OggettoBulk {

    public final static Map<String,String> tipoOperazioneKeys = Arrays.asList(TipoOperazioneType.values())
            .stream()
            .collect(Collectors.toMap(
                    TipoOperazioneType::name,
                    TipoOperazioneType::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
    private String tipoOperazione;
    private String codiceFiscale;

    public String getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazione(String tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        if (Optional.ofNullable(codiceFiscale).isPresent()) {
            Pattern p = Pattern.compile("^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$");
            if (!p.matcher(Optional.ofNullable(getCodiceFiscale()).orElse("")).matches()) {
                throw new ValidationException("Inserire un codice fiscale valido!");
            }
        } else {
            throw new ValidationException("Inserire il Codice Fiscale!");
        }
        if (!Optional.ofNullable(getTipoOperazione()).filter(s -> s.trim().length() > 0 ).isPresent()) {
            throw new ValidationException("Inserire il Tipo di Operazione!");
        }
    }

    public enum TipoOperazioneType {
        SID("Variazione Importi Documento (Set Importi Documento)"),
        MI("Modifica Regime IVA"),
        RC("Ricezione Documento"),
        RF("Rifiuto Documento"),
        CS("Comunicazione Scadenza"),
        CH("Chiusura Documento"),
        RS("Restore Documento Archiviato"),
        RT("Reset Documento");
        private final String label;

        TipoOperazioneType(String label) {
            this.label = label;
        }

        public String label() {
            return label;
        }
    }
}
