package br.com.atsinformatica.midler.facade;

import br.com.atsinformatica.erp.dao.UsuarioERPDAO;
import br.com.atsinformatica.erp.entity.UsuarioERPBean;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public class UsuarioERPFacade {
    private final Logger logger = Logger.getLogger(UsuarioERPFacade.class);
    private UsuarioERPDAO usuarioDAO;

    public boolean validarUsuario(UsuarioERPBean usuario){
        Connection conn = ConexaoATS.getConnection();
        try {
            conn.setAutoCommit(false);
            boolean retorno = this.getUsuarioDAO().validarUsuario(usuario);
            conn.commit();
            return retorno;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {}
            logger.error("Falha ao validar usuário", e);
            return false;
        }
    }
    
    public UsuarioERPDAO getUsuarioDAO() {
        if (this.usuarioDAO == null) {
            this.usuarioDAO = new UsuarioERPDAO();
        }
        return usuarioDAO;
    }
}
