
package it.cnr.contab.pagopa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

public class AnnullaPendenza extends AggiornaPendenza implements Serializable {
    public AnnullaPendenza() {
        super("REPLACE", "/stato","ANNULLATA");
    }
}
