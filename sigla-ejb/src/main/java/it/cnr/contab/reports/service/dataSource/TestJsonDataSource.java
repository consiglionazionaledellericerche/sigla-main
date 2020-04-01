package it.cnr.contab.reports.service.dataSource;

import com.google.gson.GsonBuilder;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.comp.ComponentException;

public class TestJsonDataSource extends AbstractDataSourceOffline {

    protected  String getJson( Object o){
        return new GsonBuilder().create().toJson(o );
    }

    public String getDataSourceOffline(Print_spoolerBulk print_spoolerBulk) throws ComponentException {
        String text ="JsonDataSource";
        String json="{\n" +
                "  \"anno\": 2018,\n" +
                "  \"numero\": 78,\n" +
                "  \"dataInserimento\": \"13/06/2018\",\n" +
                "  \"cognomeRich\": \"ALBERTI\",\n" +
                "  \"nomeRich\": \"LUCIA\",\n" +
                "  \"matricolaRich\": \"11334\",\n" +
                "  \"codiceFiscaleRich\": \"LBRLCU63C55F023B\",\n" +
                "  \"luogoDiNascitaRich\": \"MASSA\",\n" +
                "  \"dataDiNascitaRich\": \"15/03/1963\",\n" +
                "  \"comuneResidenzaRich\": \"POGGIO MIRTETO\",\n" +
                "  \"indirizzoResidenzaRich\": \"VIA CAVOUR 27\",\n" +
                "  \"domicilioFiscaleRich\": \"\",\n" +
                "  \"datoreLavoroRich\": \"ISTITUTO DI STUDI SUL MEDITERRANEO ANTICO\",\n" +
                "  \"qualificaRich\": \"Ricercatore\",\n" +
                "  \"livelloRich\": \"03\",\n" +
                "  \"oggetto\": \"Partecipazione a congresso CICOP 2018 a Matera e sopralluoghi in provincia di Bari e Lecce\",\n" +
                "  \"italiaEstero\": \"ITALIA\",\n" +
                "  \"destinazione\": \"Matera, Bari, Lecce\",\n" +
                "  \"nazione\": \"\",\n" +
                "  \"tipoMissione\": \"I\",\n" +
                "  \"trattamento\": \"Rimborso Documentato\",\n" +
                "  \"dataInizioMissione\": \"17/06/2018 09:00\",\n" +
                "  \"dataFineMissione\": \"24/06/2018 22:16\",\n" +
                "  \"voce\": \"13031 Rimborso spese di missione e trasferta del personale non soggette ai limiti di spesa\",\n" +
                "  \"gae\": \"P0000373 Laboratorio Archeologico Congiunto MONTENEGRO\",\n" +
                "  \"cdrRich\": \"\",\n" +
                "  \"uoRich\": \"092.000 Istituto di Studi sul Mediterraneo Antico - ISMA - Sede Montelibretti\",\n" +
                "  \"cdrSpesa\": \"092.000.000 Istituto di Studi sul Mediterraneo Antico - ISMA -  Sede Montelibretti\",\n" +
                "  \"uoSpesa\": \"092.000 Istituto di Studi sul Mediterraneo Antico - ISMA - Sede Montelibretti\",\n" +
                "  \"modulo\": \"SAC.AD002.028 LABORATORI CONGIUNTI - Archeologici\",\n" +
                "  \"cdsRich\": \"092\",\n" +
                "  \"cdsSpesa\": \"092\",\n" +
                "  \"cup\": \"B76J16001840005\",\n" +
                "  \"pgObbligazione\": \"\",\n" +
                "  \"esercizioOriginaleObbligazione\": \"\",\n" +
                "  \"utilizzoTaxi\": \"No\",\n" +
                "  \"utilizzoAutoNoleggio\": \"No\",\n" +
                "  \"utilizzoAutoServizio\": \"No\",\n" +
                "  \"personaleAlSeguito\": \"No\",\n" +
                "  \"noteUtilizzoTaxiNoleggio\": \"\",\n" +
                "  \"obbligoRientro\": \"No\",\n" +
                "  \"partenzaDa\": \"Residenza/Domicilio Fiscale\",\n" +
                "  \"distanzaDallaSede\": \"\",\n" +
                "  \"note\": \"\",\n" +
                "  \"priorita\": \"IMPORTANTE\",\n" +
                "  \"importoPresunto\": \"600,00\",\n" +
                "  \"missioneGratuita\": \"\",\n" +
                "  \"richiestaAutoPropria\": \"Sì\",\n" +
                "  \"richiestaAnticipo\": \"Sì\",\n" +
                "  \"partenzaDaAltro\": \"\",\n" +
                "  \"tipo\": \"O\"\n" +
                "}";
        //getJson(json);
        return json;
    }
}
