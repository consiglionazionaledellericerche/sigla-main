package it.cnr.contab;
public final class Version {
	private static final String APPLICATION_TITLE = "SIGLA - Sistema Informativo per la Gestione delle Linee di Attività";
	private static final String VERSION = "01.001.000";
	private static final String APPLICATION_VERSION = "Documenti contabili/amministrativi transact."+VERSION;
	private static final String APPLICATION_TITLE_VERSION = APPLICATION_TITLE + " - " + APPLICATION_VERSION;

/**
 * Version constructor comment.
 */
private Version() {
	super();
}

public static final String getApplicationTitle() {
	return APPLICATION_TITLE;
}

public static final String getApplicationTitleAndVersion() {
	return APPLICATION_TITLE_VERSION;
}

public static final String getApplicationVersion() {
	return APPLICATION_VERSION;
}

public static final String getVersion() {
	return VERSION;
}
}