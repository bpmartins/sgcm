<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html lang="en">
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
	<jsp:include page="../login/loginCheck.jsp" />
    <title>SGCM - Manutenção de Aluno</title>

    <script language="JavaScript" src="includes/jQuery/jquery-1.8.2.js" type="text/javascript"></script>
    <script src="includes/js/dropdown.js"></script>
    <script language="JavaScript" src="includes/jQuery/jquery-ui.js" type="text/javascript"></script>
	<script language="JavaScript" src="includes/jQuery/jquery.maskedinput-1.2.2.js" type="text/javascript"></script>
	<script src="includes/js/bootstrap-datepicker.js"></script>
	<script src="includes/js/bootstrap-datepicker.pt-BR.js"></script>
	
    <!-- Bootstrap CSS -->
    <link href="includes/css/datepicker.css" rel="stylesheet">
    <link href="includes/css/style.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" media="screen" href="includes/jQuery/jquery-ui.css" />

    <style>
    /* Deixar o rodape no final da pagina -------------------------- */
    html,
    body {
      height: 100%; 
      background-color: #F5F5F5 !important; 
      background-image: url(img/pagina_bkp.jpg); 
      background-attachment: fixed;
      /*Site da imagem: http://hdwallpapercollection.com/music-notes-piano-wallpapers.html#.UlN69669s39*/
    }    
    #wrap {
      min-height: 100%;
      height: auto !important;
      height: 100%;     
      margin: 0 auto -60px;      
      padding: 0 0 60px;
    }
     /* --- */

    /* -Topo- */    
    .topo{
     padding-top: 10px;padding-bottom: 10px;
    }
    /* --- */  

    
    /* -Dados do Usuario- */    
    .usuario-img{
      margin-top: 5px;
      width: 80px !important;
    }
    .usuario-dados{
      padding-top: 25px;
      
    }
    /* --- */ 

    /* -Pagina- */    
    .pagina{
      margin-top: 45px;
      margin-bottom: 45px;
      border-top: 8px solid #20598A;
    }
    /* --- */ 

    /* -Menu- */ 
    .menu{
      padding-bottom: 8px;
    }   
    .menu a{
      color: #000 !important;
    }
    .menu > ul > li > a{
      color: #fff !important;
    }

    .menu > ul > li > a:hover{
      background-color: #428bca !important;
    }
    /* --- */

    /* -Filtros- */    
    .filtros{
      padding-top: 10px;
      padding-bottom: 10px;
    }
    /* --- */

    /* -Acoes- */    
    .acoes{
            padding-top: 20px;
            padding-bottom: 20px;
    }
    /* --- */

 
    /* -Rodape- */
    .footer {
      height: 60px;
      background-color: #f5f5f5;
    }
    
    .footer .container p {
      color: #374245;
      margin: 20px 0;
    }
    /* --- */

    /* -Algumas Classes- */
    .fundo-topo-rodape{
      background-color: #fff !important;
    }
    
    .fundo-azul{
       background-color: #20598A;
    }

    .fundo-filtro-acoes{
      background-color: #CFCFCF !important;
    }

    .fundo-branco{
      background-color: #fff !important;
    }
    /* --- */

    </style>

    <!-- 
      Esses dois arquivos (html5shiv.js e respond.min.js) sÃ£o para o IE suportar Html5 e Media Queries, respectivamente.
    -->
    <!--[if lt IE 9]>
      <script src="includes/js/html5shiv.js"></script>
      <script src="includes/js/respond.min.js"></script>
    <![endif]-->
  </head>
  
<script type="text/javascript">
function validar() {
	var msgErro = 'Os seguintes campos são obrigatórios:\n \n ';
	var controle = 0;
	if($("#idNome").val() == ""){
		msgErro	 = msgErro + "Nome \n";
		controle = 1;
	}
	if($("#idTelefone").val() == ""){
		msgErro	 = msgErro + "Telefone \n";
		controle = 1;
	}
	if($("#idDtNascimento").val() == ""){
		msgErro	 = msgErro + "Data de Nascimento \n";
		controle = 1;
	}
	if($("#idLogradouro").val() == ""){
		msgErro	 = msgErro + "Logradouro \n";
		controle = 1;
	}
	if($("#idBairro").val() == ""){
		msgErro	 = msgErro + "Bairro \n";
		controle = 1;
	}
	if($("#idNumero").val() == ""){
		msgErro	 = msgErro + "Número \n";
		controle = 1;
	}
	if($("#idSexo").val() == ""){
		msgErro	 = msgErro + "Sexo \n";
		controle = 1;
	}
	if($("#idSairSozinho").val() == ""){
		msgErro = msgErro + "Sair Sozinho \n";
		controle = 1;
	}
	 var value = $("#idExperienciaMusical").val();
	if(value == ""){
		msgErro = msgErro + "Experiência Musical \n";
		controle = 1;
	}else if(value == 'S'){
		if($("#idNomeEscola").val() == ""){
			msgErro = msgErro + "Nome da Escola \n";
			controle = 1;
		}
		if($("#idTempoExperiencia").val() == ""){
			msgErro = msgErro + "Tempo de Experiência \n";
			controle = 1;
		}
		if($("#idInstrumAprendido").val() == ""){
			msgErro = msgErro + "Instrumento aprendido \n";
			controle = 1;
		}
	}
	
	if(controle == 1){
		alert(msgErro);
		return false;
	}
	return true;
}

$( document ).ready(function() {
	
	 $("#idTelefone").mask("(99) 9999-9999");
	 $("#idCep").mask("99999-999");
	 $("#idNumero").keypress(verificaNumero);
	 $("#idTelefone").keypress(verificaNumero);
	 $("#idCep").keypress(verificaNumero);
	 
	 $("#idNome").keypress(verificaLetra);
	 $("#idLogradouro").keypress(verificaLetra);
	 $("#idNomePai").keypress(verificaLetra);
	 $("#idNomeMae").keypress(verificaLetra);
	 $("#idNomeDeficiencia").keypress(verificaLetra);
	 $("#idNomeEscola").keypress(verificaLetra);
	 $("#idInstrumAprendido").keypress(verificaLetra);
	 $("#idBairro").keypress(verificaLetra);
	 $("#idPreferencia").keypress(verificaLetra);
	 
	 
		if("${aluno.dataNascimento}" != ""){
			var strDate = "${aluno.dataNascimento}";
			var dateParts = strDate.split("-");
			$('#idDtNascimento').val(dateParts[2] + "/" + (dateParts[1]) + "/" + dateParts[0]);	
		}
	
	$('#idDtNascimento').datepicker({
	    format: "dd/mm/yyyy",
	    language: "pt-BR",
	    autoclose: true
	});
	
	if($( "#idExperienciaMusical" ).val() == "N"){
		$( ".experiencia" ).hide();
	}
	
	$( "#idExperienciaMusical" ).change(function() {
		if(this.value == 'N'){
			$( ".experiencia" ).hide();
		}else{
			$( ".experiencia" ).show();
		}
		
	});
});

function verificaNumero(e) {
    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
        return false;
    }
}

function verificaLetra(){  
    tecla = event.keyCode;  
    if (tecla >= 48 && tecla <= 57){  
        return false;  
    }else{  
       return true;  
    }  
}  

</script>
  <body>
    
    <div id="wrap"> <!-- Essa div Ã© utilizada para forÃ§ar o rodape a ficar no final da pÃ¡gina -->
    
    <!-- Estrutura do Layout: http://getbootstrap.com/css/#grid -->

    <div class="topo fundo-topo-rodape">
      <div class="container"> 
          <div class="row">
              <div class="col-md-9">
                <!-- Logo -->
              </div>
              <div class="col-md-2 text-center usuario-dados">
                Olá,  <b>${usuario.login} <b/>.
                <br/>
                <s:a action="logOutUsuario.action">Logout</s:a>
              </div> 
              <div class="col-md-1 text-left">
                <img class="usuario-img" src="img/usuario_default.png" alt="foto_usuario1">
              </div>           
          </div>
      </div>

    </div>

      <div class="container pagina fundo-azul"> <!-- Conteudo -->  

          <div class="menu">
            <ul class="nav nav-pills">	
 			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">			
              <li class="active"><s:a action="formListarAluno.action">Alunos</s:a></li> 
			  </s:if>		
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
              <li><s:a action="formListarTurma.action">Turmas</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarInstrumento.action">Instrumentos</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarEmprestimo.action">Empréstimos</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarProfessor.action">Professor</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarEvento.action">Eventos</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarRelatorio.action">Relatórios</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 1">
			  <li><s:a action="formListarPerfil.action">Perfis</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 1">
			  <li><s:a action="formListarUsuario.action">Usuários</s:a></li>   
			  </s:if>					  					  
            </ul>
          </div>
          
           <!-- Mensagens de erro -->
           <div class="row fundo-branco">
            <div class="col-md-12" id="divMensagens">
				<s:actionerror cssClass="error" />
				<s:actionmessage cssClass="info" />    
            </div>
          </div>
          
          <!-- Formulário -->
		   <div class="row" style="min-height: 500px;background-color: #fff;padding: 10px 0 10px 0;">
		            
		            <div class="text-center">
			            <s:form id="formCadastro"  method="POST" action="executeAluno" onsubmit="return validar();" cssClass=".form-horizontal text-center" role="form">
			       			<legend>Formulário de Aluno</legend>
								<s:hidden id="idAluno" name="aluno.id" />
								<table align="center">
								<tr>
								    <td><label class="control-label">Nome*:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.nome" label="Nome" required="true" id="idNome" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Telefone*:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.telefone" label="Telefone" required="true" id="idTelefone" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Data de Nascimento*:</label></td>
									<td><div class="input-append date"><s:textfield cssClass="form-control span2" name="aluno.dataNascimento" label="Data de Nascimento" required="true" id="idDtNascimento"/> </div></td>
								</tr>
								<tr>
								    <td><label class="control-label">Logradouro*:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.logradouro" label="Logradouro" required="true" id="idLogradouro" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Número*:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.numero" label="Número" required="true" id="idNumero" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Bairro*:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.bairro" label="Bairro" required="true" id="idBairro" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Complemento:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.complemento" label="Complemento" id="idComplemento" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">CEP:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.cep" label="CEP" id="idCep"/></td>
								</tr>
								<tr>
								    <td><label class="control-label">Nome do Pai:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.nomePai" label="Nome do Pai" id="idNomePai" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Nome da Mãe:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.nomeMae" label="Nome da Mãe" id="idNomeMae" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Sexo*:</label></td>
									<td>                        
										<s:select cssClass="form-control" name="aluno.sexo" id="idSexo" list="#{'M':'Masculino','F':'Feminino'}"/>
				                    </td>
								</tr>
								<tr>
								    <td><label class="control-label">Nome da Deficiência:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.nomeDeficiencia" label="Nome da Deficiência" id="idNomeDeficiencia" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Nome do Medicamento:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.nomeMedicamento" label="Nome do Medicamento" id="idNomeMedicamento" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Tem Expeeriência Musical ?*</label></td>
									<td>
										<s:select cssClass="form-control" name="aluno.experienciaMusical" id="idExperienciaMusical" list="#{'S':'Sim','N':'Não'}"/>
								    </td>
								</tr>
								<tr class="experiencia">
								    <td><label class="control-label">Nome da Escola<span id="obrigarExp1">*</span>:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.nomeEscola" label="Nome da Escola" id="idNomeEscola" /></td>
								</tr>
								<tr class="experiencia">
								    <td><label class="control-label">Tempo de Experiência(Em meses)<span id="obrigarExp2">*</span>:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.tempoExperiencia" label="Tempo de Experiência" id="idTempoExperiencia" /></td>
								</tr>
								<tr class="experiencia">
								    <td><label class="control-label">Instrumento Aprendido<span id="obrigarExp3">*</span>:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.instrumAprendido" label="Instrumento Aprendido" id="idInstrumAprendido" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Preferência Instrumental:</label></td>
									<td><s:textfield cssClass="form-control" name="aluno.preferencia" label="Preferência Instrumental" id="idPreferencia" /></td>
								</tr>
								<tr>
								    <td><label class="control-label">Tem permissão para sair sozinho ?*</label></td>
									<td>
										<s:select cssClass="form-control" name="aluno.sairSozinho" id="idSairSozinho" list="#{'S':'Sim','N':'Não'}"/>
									</td>
								</tr>
								</table>
								  <div class="row acoes">
					                  <div class="col-md-12 text-center">
					                  	<s:submit  action="executeAluno" cssClass="btn btn-primary" value="Gravar"/>
					                  	<button type="button" onclick="javascript:history.go(-1);" class="btn">Cancelar</button>
					                  </div>
				                  </div>
						</s:form>
		            </div>
		            </div>
          
      </div> <!-- Fim do Conteudo -->

    </div><!-- Fim do Wrap -->

    <div class="footer fundo-topo-rodape">
      <div class="container">
        <p>© Copyright 2013 Centro Cultural e Artístico Crescendo com Música (CCACM).</p>
      </div>
    </div>

    <!-- js, fica no final da pagina para carregar mais rapido -->

  </body>
</html>
