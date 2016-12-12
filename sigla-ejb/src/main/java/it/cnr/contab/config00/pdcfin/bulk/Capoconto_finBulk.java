package it.cnr.contab.config00.pdcfin.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Capoconto_finBulk extends Capoconto_finBase {

	public Capoconto_finBulk() {
		super();
	}
	public Capoconto_finBulk(java.lang.String cd_capoconto_fin) {
		super(cd_capoconto_fin);
	}
}
