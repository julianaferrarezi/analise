/*
 * Copyright (c) 2006 by UNESP - Universidade Estadual Paulista "JÚLIO DE MESQUITA FILHO"
 *               Faculdade de Ciências, Bauru, São Paulo, Brasil
 *               http://www.fc.unesp.br, http://www.unesp.br
 *
 *
 * Este arquivo é parte do programa Academico.
 *
 * Academico é um software livre; você pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como 
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da 
 * Licença, ou (na sua opnião) qualquer versão.
 *
 * Academico é distribuido na esperança que possa ser util, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer
 * MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a
 * Licença Pública Geral GNU para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU
 * junto com Academico, se não, escreva para a Fundação do Software
 * Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 * This file is part of Academico.
 *
 * Academico is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Academico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Academico; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA *
 *
 *
 * Date:    02/06/2008 16:22:25
 *
 * Author:  André Penteado
 */

package br.unesp;

import br.unesp.core.wrappers.UserLoginWrapper;
import br.unesp.web.SSOLoginAction;

/**
 * @author Andre Penteado
 * @since 02/06/2008 - 16:22:25
 */
public class SSOLoginAcademicoAction extends SSOLoginAction {

    public static final String SELECIONAR_CURSO = "SELECIONAR_CURSO";
    public static final String CONFIRMAR_MATRICULA = "CONFIRMAR_MATRICULA";
    public static final String BLOQUEIO_AVALIACAO = "BLOQUEIO_AVALIACAO";
    
//    private Log4jWrapper log = new Log4jWrapper(SSOLoginAcademicoAction.class, null);
    private UserLoginWrapper userLogin = null;
    
    @Override
    public String execute() throws Exception {
        if (SUCCESS.equals(super.execute())) {
            userLogin = (UserLoginWrapper)getUserSession();
            if (userLogin == null)
                return ERROR;
            return SUCCESS;
        }

        return ERROR;
    }
    
}
