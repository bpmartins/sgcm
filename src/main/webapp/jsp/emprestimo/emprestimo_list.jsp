<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html lang="en">
  <head>
  	<link rel="stylesheet" type="text/css" media="screen" href="includes/css/mensagens.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
	<jsp:include page="../login/loginCheck.jsp" />
    <title>SGCM - Manutenção de Empréstimos</title>

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
      Esses dois arquivos (html5shiv.js e respond.min.js) sÃ£o para o IE suportar Html5 e Media Queries, respectivamente.
    -->
    <!--[if lt IE 9]>
      <script src="includes/js/html5shiv.js"></script>
      <script src="includes/js/respond.min.js"></script>
    <![endif]-->
  </head>

<script type="text/javascript">
		function validar() {
			var msgErro = 'Você deve Preencher um valor';
			var controle = 0;
			if(jQuery("#filtro-combo").val() != "-1" && jQuery("#buscaValor").val() == ""){
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
              <li><s:a action="formListarAluno.action">Alunos</s:a></li> 
			  </s:if>		
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
              <li><s:a action="formListarTurma.action">Turmas</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li><s:a action="formListarInstrumento.action">Instrumentos</s:a></li>
			  </s:if>
			  <s:if test="#session.usuario.perfil.id == 2 || #session.usuario.perfil.id == 1 ">
			  <li class="active"><s:a action="formListarEmprestimo.action">Empréstimos</s:a></li>
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
         <!-- Filtros --> 
         <div class="row fundo-filtro-acoes filtros">
            <div class="col-md-12">     
             <form class="form-inline text-center" role="form" id="formCadastro"  method="POST" action="listarEmprestimo" onsubmit="return validar();">
                  <div class="form-group">
                        <select class="form-control" name="criterio.campo" id="filtro-combo">
                          <option value="-1">Selecione</option>
                          <option value="aluno">Aluno</option>
                          <option value="instrumento">Instrumento</option>
                        </select>                    
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control " name="criterio.valor" id="buscaValor" placeholder="Pesquisar">
                  </div>
                  <button type="submit" class="btn btn-primary">Buscar</button>
              </form>                
            </div>
          </div>
          <!-- Tabela -->
          <s:if test="emprestimos.size > 0">
            <div class="row" style="min-height: 500px;background-color: #fff;padding: 10px 0 10px 0;">
            <div class="col-md-12">
				<table border="2" class="table table-striped table-bordered table-hover table-condensed">
					<tr>
						<th class="col-md-2">Código</th>
						<th class="col-md-2">Instrumento</th>
						<th class="col-md-4">Aluno</th>
						<th class="col-md-2">Início</th>
						<th class="col-md-2">Fim</th>
						<th class="col-md-3">Baixado?</th>
						<th class="col-md-2"></th>
						<th class="col-md-2"></th>
					</tr>

					<s:iterator value="emprestimos">
						<tr>
							<td><s:property value="id" /></td>
							<td><s:property value="instrumento.descricao" /></td>
							<td><s:property value="aluno.nome" /></td>
							<td><s:property value="dataInicio" /></td>
							<td><s:property value="dataFim" /></td>
							<td><s:property value="indBaixa" /></td>
							<td><s:url action="formAlterarEmprestimo" var="alt">
									<s:param name="id" value="id" />
								</s:url> <a href='<s:property value="#alt"/>'><img src="img/alterar.png"></a>
							</td>
							<td><s:url action="baixarEmprestimo" var="del">
									<s:param name="id" value="id" />
								</s:url> <a href='<s:property value="#del"/>'><img src="img/baixa.png"></a>
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>
			</div>		
		  </s:if>
          <!-- Botões -->
 	     <div class="row fundo-filtro-acoes acoes">
            <div class="col-md-12 text-center">
            <form class="form-inline text-center" role="form" id="formCadastro"  method="POST" action="cadastrarEmprestimo">
              <button type="submit" class="btn btn-info"  name="action:cadastrarEmprestimo"><span class="glyphicon glyphicon-floppy-disk"></span> Novo</button>
              <button type="submit" class="btn btn-default" name="action:cancelarEmprestimo">Cancelar</button>
			</form>
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
    <script language="JavaScript" src="includes/jQuery/jquery-1.8.2.js" type="text/javascript"></script>
    <script src="includes/js/dropdown.js"></script>

  </body>
</html>
