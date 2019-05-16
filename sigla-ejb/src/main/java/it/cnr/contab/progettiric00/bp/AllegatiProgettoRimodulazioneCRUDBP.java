package it.cnr.contab.progettiric00.bp;

import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.AllegatoProgettoRimodulazioneBulk;
import it.cnr.contab.util00.bp.AllegatiTypeCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;

public abstract class AllegatiProgettoRimodulazioneCRUDBP<T extends AllegatoGenericoTypeBulk, K extends AllegatoParentBulk> extends AllegatiTypeCRUDBP<T,K> {
    private static final long serialVersionUID = 1L;

    public AllegatiProgettoRimodulazioneCRUDBP() {
        super();
    }

    public AllegatiProgettoRimodulazioneCRUDBP(String s) {
        super(s);
    }

    @Override
    protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
    	if (Optional.ofNullable(allegato).map(AllegatoProgettoRimodulazioneBulk.class::isInstance).orElse(Boolean.FALSE))
    		return !allegato.getDaNonEliminare() && super.isPossibileCancellazione(allegato);
   		return super.isPossibileCancellazione(allegato);
    }
    
    @Override
    protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
    	if (Optional.ofNullable(allegato).map(AllegatoProgettoRimodulazioneBulk.class::isInstance).orElse(Boolean.FALSE))
    		return !allegato.getDaNonEliminare() && super.isPossibileModifica(allegato);
   		return super.isPossibileModifica(allegato);
    }
}
