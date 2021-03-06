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
    <title>SGCM - Manuten��o de Usu�rio</title>

    <!-- Bootstrap CSS -->
    <link href="includes/css/style.min.css" rel="stylesheet">

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
      Esses dois arquivos (html5shiv.js e respond.min.js) são para o IE suportar Html5 e Media Queries, respectivamente.
    -->
    <!--[if lt IE 9]>
      <script src="includes/js/html5shiv.js"></script>
      <script src="includes/js/respond.min.js"></script>
    <![endif]-->
  </head>
  
<script type="text/javascript">
		function validar() {
			var msgErro = 'Os seguintes campos s�o obrigat�rios:\n \n ';
			var controle = 0;
			if(jQuery("#idLogin").val() == ""){
				msgErro	 = msgErro + "Login \n";
				controle = 1;
			}
			if(jQuery("#idSenha").val() == ""){
				msgErro = msgErro + "Senha \n";
				controle = 1;
			}
			if(jQuery("#idPerfil").val() == -1){
				msgErro = msgErro + "Perfil \n";
				controle = 1;
			}
		
			if(controle == 1){
				alert(msgErro);
				return false;
			}
			return true;
			
		}
</script>
  <body>
    
    <div id="wrap"> <!-- Essa div é utilizada para forçar o rodape a ficar no final da página -->
    
    <!-- Estrutura do Layout: http://getbootstrap.com/css/#grid -->

    <div class="topo fundo-topo-rodape">
      <div class="container"> 
          <div class="row">
              <div class="col-md-9">
                <!-- Logo -->
              </div>
              <div class="col-md-2 text-center usuario-dados">
                Ol�,  <b>${session.usuario.login} <b/>.
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
              <li><s:a action="formListarAluno.action">Alunos</s:a></li> 
			  </s:if>		
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
              <li><s:a action="formListarTurma.action">Turmas</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarInstrumento.action">Instrumentos</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarEmprestimo.action">Empr�stimos</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarProfessor.action">Professor</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarEvento.action">Eventos</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarRelatorio.action">Relat�rios</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 1">
			  <li><s:a action="formListarPerfil.action">Perfis</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 1">
			  <li class="active"><s:a action="formListarUsuario.action">Usu�rios</s:a></li>   
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
          
          <!-- Formul�rio -->
            <div class="row" style="min-height: 500px;background-color: #fff;padding: 10px 0 10px 0;">
            
            <div class="text-center">
	            <s:form id="formCadastro"  method="POST" action="executeUsuario" onsubmit="return validar();" cssClass=".form-horizontal text-center" role="form">
	       			<legend>Formul�rio de Usu�rio</legend>
						<s:hidden id="idUsuario" name="usuario.id" />
						<table align="center">
						<tr>
						    <td><label class="control-label">Usu�rio*:</label></td>
							<td><s:textfield cssClass="form-control input-medium "  name="usuario.login" required="true" id="idLogin"/></td>
						</tr>
						<tr>
						    <td><label class="control-label">Senha*:</label></td>
							<td><s:textfield cssClass="form-control input-medium " name="usuario.senha"  id="idSenha"/></td>
						</tr>
						<tr>
						    <td><label class="control-label">Perfil*:</label></td>
							<td><s:select list="perfis" cssClass="form-control" listKey="id" listValue="descricao" label="Perfil" headerKey="-1" headerValue="Selecione" name="usuario.perfil.id" required="true" id="idPerfil"/></td>
						</tr>    
						<tr>
						    <td><label class="control-label">Pessoa:</label></td>
							<td><s:select list="pessoas" cssClass="form-control" listKey="id" listValue="nome" label="Pessoa" headerKey="-1" headerValue="Selecione" name="usuario.pessoa.id" required="false" id="idPessoa"/></td>
						</tr>
						</table>
						  <div class="row acoes">
			                  <div class="col-md-12 text-center">
			                  	<s:submit  action="executeUsuario" cssClass="btn btn-primary" value="Gravar"/>
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
        <p>� Copyright 2013 Centro Cultural e Art�stico Crescendo com M�sica (CCACM).</p>
      </div>
    </div>

    <!-- js, fica no final da pagina para carregar mais rapido -->
    <script language="JavaScript" src="includes/jQuery/jquery-1.8.2.js" type="text/javascript"></script>
    <script src="includes/js/dropdown.js"></script>

  </body>
</html>
