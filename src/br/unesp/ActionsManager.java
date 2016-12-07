/*
 * Copyright (c) 2006 by UNESP - Universidade Estadual Paulista "JÚLIO DE MESQUITA FILHO"
 *               Faculdade de Ciências, Bauru, São Paulo, Brasil
 *               http://www.fc.unesp.br, http://www.unesp.br
 *
 *
 * Este arquivo é parte do programa DocFlow.
 *
 * DocFlow é um software livre; você pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como 
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da 
 * Licença, ou (na sua opnião) qualquer versão.
 *
 * DocFlow é distribuido na esperança que possa ser util, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer
 * MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a
 * Licença Pública Geral GNU para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU
 * junto com DocFlow, se não, escreva para a Fundação do Software
 * Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 * This file is part of DocFlow.
 *
 * DocFlow is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * DocFlow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DocFlow; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA *
 *
 *
 * Date:    06/03/2008 15:37:33
 *
 * Author:  André Penteado
 */

package br.unesp;

import java.io.IOException;

import org.mentawai.core.ActionConfig;
import org.mentawai.core.Context;
import org.mentawai.core.Forward;
import org.mentawai.core.Redirect;

import br.unesp.web.AbstractActionsManager;

/**
 * @author Andre Penteado
 * @since 06/03/2008 - 15:37:33
 */
public class ActionsManager extends AbstractActionsManager {

//    private final Log4jWrapper log = new Log4jWrapper(ActionsManager.class, null);

    @Override
    public void init(Context application) {
        super.init(application);
//        AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_FUNCIONARIO_GRADUACAO));
//        AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_CHEFE_DEPARTAMENTO));
//        AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_ALUNO_GRADUACAO));
//        AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_COORDENADOR_CURSO));
//        AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_MATRICULA_CALOURO));
//        AuthorizationManager.addGroup(new Group(KGlobal.CATEGORIA_CANDIDATO));
    }

    @Override
    public void loadActions() {
        super.loadActions();
        
        //Descomentar caso queira ativar debug do menta -- mostra no fim da pagina todos os filtros, etc.
        //setDebugMode(true);

        ActionConfig ac = null;
        
//        log.debug("SOBRESCREVENDO AÇÃO DE LOGIN");
        ac = new ActionConfig("/ssoLogin", SSOLoginAcademicoAction.class);
        ac.addConsequence(SSOLoginAcademicoAction.SUCCESS, new Redirect());
        ac.addConsequence(SSOLoginAcademicoAction.ERROR, new Redirect("/common.accessDenied.action"));
        ac.addConsequence(SSOLoginAcademicoAction.SELECIONAR_CURSO, new Redirect("/ssoLogin.selecionarCurso.action"));
        ac.addConsequence(SSOLoginAcademicoAction.SUCCESS, "selecionarCurso", new Redirect("/common.home.action"));
        ac.addConsequence(SSOLoginAcademicoAction.SUCCESS, "selecionarAluno", new Redirect("/common.home.action"));
        ac.addConsequence(SSOLoginAcademicoAction.SUCCESS, "bloqueioSistemaAvaliacao", new Forward("/common/bloqueio_avaliacao.jsp"));
        ac.addConsequence(SSOLoginAcademicoAction.BLOQUEIO_AVALIACAO, new Redirect("/ssoLogin.bloqueioSistemaAvaliacao.action"));
        ac.addConsequence(SSOLoginAcademicoAction.CONFIRMAR_MATRICULA, new Redirect("/matricula/calouro/confirmacao.abrir.action"));
        addActionConfig(ac);

      

    }

    @Override
    public void loadLists() throws IOException {
        super.loadLists();

       
    }
}