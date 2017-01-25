package br.com.atsinformatica.erp.controller;

import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import java.sql.Connection;

/**
 *
 * @author AlexsanderPimenta
 */
public class ConnectionFactory {

    protected Connection conn;

    public ConnectionFactory() {
        this.conn = ConexaoATS.getConnection();
    }

}
