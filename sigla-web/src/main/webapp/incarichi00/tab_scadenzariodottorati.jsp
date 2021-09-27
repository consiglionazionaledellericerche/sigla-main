<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% 	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
 %>
<div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Anagrafica Dottorati
    </div>
    <table width="100%">
      <tr>
        <td><% bp.getController().writeFormLabel(out, "id"); %>	</td>
            <td><% bp.getController().writeFormInput(out,"id");%></td>
            <td><% bp.getController().writeFormLabel(out, "find_AnagraficaDottorati"); %></td>
            <td><% bp.getController().writeFormInput(out, "find_AnagraficaDottorati"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"pgImpegno"); %></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"pgImpegno"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dtRegistrazione"); %></td>
        <td><% bp.getController().writeFormInput(out,"dtRegistrazione"); %></td>
        <td><% bp.getController().writeFormLabel(out,"dsScadDott"); %></td>
        <td><% bp.getController().writeFormInput(out,"dsScadDott"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"tiAnagrafico"); %></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"tiAnagrafico"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"ragioneSociale"); %></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"ragioneSociale"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"cdTrattamento"); %></td>
        <td><% bp.getController().writeFormInput(out,"cdTrattamento"); %></td>
        <td><% bp.getController().writeFormLabel(out,"imTotaleScadDott"); %></td>
        <td><% bp.getController().writeFormInput(out,"imTotaleScadDott"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"numeroRate"); %></td>
        <td><% bp.getController().writeFormInput(out,"numeroRate"); %></td>
        <td><% bp.getController().writeFormLabel(out,"tiAnticipoPosticipo"); %></td>
        <td><% bp.getController().writeFormInput(out,"tiAnticipoPosticipo"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"mesiAnticipoPosticipo"); %></td>
        <td><% bp.getController().writeFormInput(out,"mesiAnticipoPosticipo"); %></td>
        <td><% bp.getController().writeFormLabel(out,"dtInizioScadDott"); %></td>
        <td><% bp.getController().writeFormInput(out,"dtInizioScadDott"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"numeroRate"); %></td>
        <td><% bp.getController().writeFormInput(out,"numeroRate"); %></td>
        <td><% bp.getController().writeFormLabel(out,"tiAnticipoPosticipo"); %></td>
        <td><% bp.getController().writeFormInput(out,"tiAnticipoPosticipo"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dtFineScadDott"); %></td>
        <td><% bp.getController().writeFormInput(out,"dtFineScadDott"); %></td>
        <td><% bp.getController().writeFormLabel(out,"statoAssCompenso"); %></td>
        <td><% bp.getController().writeFormInput(out,"statoAssCompenso"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"stato"); %></td>
        <td><% bp.getController().writeFormInput(out,"stato"); %></td>
        <td><% bp.getController().writeFormLabel(out,"dtSospensione"); %></td>
        <td><% bp.getController().writeFormInput(out,"dtSospensione"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dtRipristino"); %></td>
        <td><% bp.getController().writeFormInput(out,"dtRipristino"); %></td>
        <td><% bp.getController().writeFormLabel(out,"dtRinnovo"); %></td>
        <td><% bp.getController().writeFormInput(out,"dtRinnovo"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"dtCessazione"); %></td>
        <td><% bp.getController().writeFormInput(out,"dtCessazione"); %></td>
        <td><% bp.getController().writeFormLabel(out,"dacr"); %></td>
        <td><% bp.getController().writeFormInput(out,"dacr"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"utcr"); %></td>
        <td><% bp.getController().writeFormInput(out,"utcr"); %></td>
        <td><% bp.getController().writeFormLabel(out,"duva"); %></td>
        <td><% bp.getController().writeFormInput(out,"duva"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"utuv"); %></td>
        <td><% bp.getController().writeFormInput(out,"utuv"); %></td>
        <td><% bp.getController().writeFormLabel(out,"pg_ver_rec"); %></td>
        <td><% bp.getController().writeFormInput(out,"pg_ver_rec"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"utcr"); %></td>
        <td><% bp.getController().writeFormInput(out,"utcr"); %></td>
        <td><% bp.getController().writeFormLabel(out,"duva"); %></td>
        <td><% bp.getController().writeFormInput(out,"duva"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"utcr"); %></td>
        <td><% bp.getController().writeFormInput(out,"utcr"); %></td>
        <td><% bp.getController().writeFormLabel(out,"cdCdsScadDottOri"); %></td>
        <td><% bp.getController().writeFormInput(out,"cdCdsScadDottOri"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"cdUoScadDottOri"); %></td>
        <td><% bp.getController().writeFormInput(out,"cdUoScadDottOri"); %></td>
        <td><% bp.getController().writeFormLabel(out,"esercizioScadDottOri"); %></td>
        <td><% bp.getController().writeFormInput(out,"esercizioScadDottOri"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"tiIstituzCommerc"); %></td>
        <td><% bp.getController().writeFormInput(out,"tiIstituzCommerc"); %></td>
        <td><% bp.getController().writeFormLabel(out,"flTassazioneSeparata"); %></td>
        <td><% bp.getController().writeFormInput(out,"flTassazioneSeparata"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"imponibileIrpefEseprec2"); %></td>
        <td><% bp.getController().writeFormInput(out,"imponibileIrpefEseprec2"); %></td>
        <td><% bp.getController().writeFormLabel(out,"imponibileIrpefEseprec1"); %></td>
        <td><% bp.getController().writeFormInput(out,"imponibileIrpefEseprec1"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"aliquotaIrpefMedia"); %></td>
        <td><% bp.getController().writeFormInput(out,"aliquotaIrpefMedia"); %></td>
        <td><% bp.getController().writeFormLabel(out,"flEscludiQvariaDeduzione"); %></td>
        <td><% bp.getController().writeFormInput(out,"flEscludiQvariaDeduzione"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"esercizioRep"); %></td>
        <td><% bp.getController().writeFormInput(out,"esercizioRep"); %></td>
        <td><% bp.getController().writeFormLabel(out,"pgRepertorio"); %></td>
        <td><% bp.getController().writeFormInput(out,"pgRepertorio"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"tiPrestazione"); %></td>
        <td colspan="3"><% bp.getController().writeFormInput(out,"tiPrestazione"); %></td>
        <td><% bp.getController().writeFormLabel(out,"imponibileIrpefEseprec1"); %></td>
        <td><% bp.getController().writeFormInput(out,"imponibileIrpefEseprec1"); %></td>
      </tr>
    </table>
</div>
<!-- <div class="Group card p-2" style="width:100%">
    <div class="GroupLabel h3 text-primary">
        Mini carriera di origine (Vedere se da eliminare)
        Mini carriera di origine (Vedere se da eliminare)
    </div>
    <table width="100%">
      <tr>
        <% bp.getController().writeFormField(out,"id"); %>
        <% bp.getController().writeFormField(out,"esercizio_scad_dott_ori"); %>
      </tr>
    </table>
</div> -->