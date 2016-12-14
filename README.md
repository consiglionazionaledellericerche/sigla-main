SIGLA

# SERVIZIO REST PER CREARE UNA MISSIONE
PUT /SIGLA/restapi/missioni HTTP/1.1
Host: localhost:8080
Authorization: Basic ????????????
Content-Type: application/json
X-sigla-esercizio: 2016
X-sigla-cd-unita-organizzativa: 084.000
X-sigla-cd-cds: 084
X-sigla-cd-cdr: 084.000.000
Cache-Control: no-cache
Postman-Token: dd27c7b0-a757-1277-f62f-8e93e2a5ab1e

{
		"esercizio" : 2016,
		"cd_cds": "084",
		"cd_unita_organizzativa": "084.000",
		"cd_terzo": 2801,
		"ds_missione" : "Missione di Prova servizi REST",
		"im_spese": 234,
		"im_diaria_netto": 300,
		"im_totale_missione" : 300,
		"im_lordo_percepiente" : 300,
		"im_netto_pecepiente" : 300,
		"im_spese_anticipate" : 0,
		"im_diaria_lorda" : 0,
		"im_quota_esente" : 0,
		"im_rimborso" : 0,
		"ti_istituz_commerc" : "I",
		"fl_comune_proprio" : true,
		"fl_comune_altro" : true,
		"fl_comune_estero" : false,
		"fl_associato_compenso" : false,
		"stato_coge" : "N",
		"stato_cofi" : "I",
		"stato_coan" : "N",
		"stato_pagamento_fondo_eco": "N",
		"stato_liquidazione" : "SOSP",
		"ti_provvisorio_definitivo" : "P",
		"ti_associato_manrev" : "N",
		"dt_inizio_missione": "2016-01-01",
		"dt_fine_missione" : "2016-01-31",
		"ti_anagrafico" :"D",
		"tipo_rapporto" : {
			"cd_tipo_rapporto" : "OCCA"
		},
		"banca" : {
			"pg_banca" : 1 
		},
		"rif_inquadramento" : {
			"pg_rif_inquadramento" : 1
		},
		"modalita_pagamento" : {
			"cd_modalita_pag" : "RD"
		},
		"tappeMissioneColl" : [
			{
				"cambio_tappa" : 1,
				"divisa_tappa" : {
					"cd_divisa": "EURO"
				},
				"fl_rimborso" : true,
				"fl_comune_estero" : false,
				"fl_alloggio_gratuito" : false,
				"fl_navigazione" : false,
				"fl_vitto_gratuito" : false,
				"fl_vitto_alloggio_gratuito" : false,
				"fl_comune_proprio" : false,
				"fl_comune_altro" : false,
				"fl_no_diaria" : true,
				"dt_inizio_tappa": "2016-01-01",
				"dt_fine_tappa" : "2016-01-31",
				"nazione" : {
					"pg_nazione" : 1
				}
			}
		],
		"speseMissioneColl" : [
			{
				"pg_riga" : 1,
				"dt_inizio_tappa": "2016-01-01",
				"ti_spesa_diaria": "S",
				"cd_ti_spesa": "ALTRO",
				"ds_ti_spesa": "RIMBORSO SPESE PASTO",
				"fl_spesa_anticipata": false,
				"fl_diaria_manuale" : false,
				"percentuale_maggiorazione" : 0,
				"im_totale_spesa" : 300,
				"im_maggiorazione": 0,
				"im_spesa_euro" : 300,
				"im_base_maggiorazione": 0,
				"im_spesa_divisa" : 300,
				"im_diaria_lorda" : 0,
				"im_diaria_netto" : 0,
				"im_rimborso": 0,
				"im_spesa_max": 0,
				"im_maggiorazione_euro": 0,
				"im_spesa_max_divisa": 0,
				"im_quota_esente" : 0
			}

		]
}