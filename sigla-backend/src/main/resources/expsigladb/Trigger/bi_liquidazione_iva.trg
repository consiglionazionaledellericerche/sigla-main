CREATE OR REPLACE TRIGGER BI_Liquidazione_iva
BEFORE INSERT
ON LIQUIDAZIONE_IVA
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
Declare
  conta number :=0;
Begin
  	If (:new.stato ='D' and :new.cd_cds='000' )Then
 			 	for uo in
		 			(select * from liquid_iva_esterna
		 				where
		 				ESERCIZIO =:new.esercizio and
		 				TIPO =:new.TIPO_LIQUIDAZIONE) loop
		 				if(uo.CD_UNITA_ORGANIZZATIVA!=:new.cd_unita_organizzativa) then
					    Select count(*) into conta
					    from report_stato
					    Where ESERCIZIO 									= uo.ESERCIZIO
					    And   CD_UNITA_ORGANIZZATIVA     = uo.CD_UNITA_ORGANIZZATIVA
					    and   tipo_report  = 'LIQUIDAZIONE'
					    and   ti_documento  = UO.TIPO
					    and   DT_INIZIO = :new.DT_INIZIO
					    and   DT_FINE = :new.DT_FINE ;

					 	  if conta= 0 then
					    	ibmerr001.RAISE_ERR_GENERICO('Non ? possibile procedere con la liquidazione definitiva prima di effettuare la liquidazione definitiva delle Uo che prevedono liquidazioni esterne.');
					 		End If;
					  else
					     if nvl(:NEW.iva_liq_esterna,0)= 0 then
			    			ibmerr001.RAISE_ERR_GENERICO('Non ? possibile procedere con la liquidazione definitiva prima di valorizzare l''importo delle liquidazioni esterne.');
			 				 End If;
			 			end if;
   		end loop;
  end if;
END BI_Liquidazione_iva;
/


