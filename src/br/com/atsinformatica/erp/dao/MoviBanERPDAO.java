package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.utils.Funcoes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author AlexsanderPimenta
 */
public class MoviBanERPDAO implements IGenericDAO<MovibanERPBean> {
    private Connection conn;
    private static final Logger logger = Logger.getLogger(MovibanERPBean.class);

    @Override
    public void gravar(MovibanERPBean object) throws SQLException {
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO MOVIBAN (CODEMPRESA, CODBANCO, CODAGEN, CONTA, DATAMOV,  "
                          + "NUMORD, TIPOLANC ) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getCodEmpresa());
            pstmt.setString(2, object.getCodBanco());
            pstmt.setString(3, object.getCodAgen());
            pstmt.setString(4, object.getConta());
            pstmt.setDate(5, new java.sql.Date(object.getDataMov().getTime()));
            String numoRd = ultimoRegistro();
            pstmt.setString(6, numoRd);
            pstmt.setString(7, "007");
            pstmt.executeUpdate();
            logger.info("Movimentação bancária, gerada com sucesso.");
        }catch(Exception e){
            logger.error("Erro ao gerar movimentação bancária: "+e);            
        }finally{
            pstmt.close();
        }
    }

    @Override
    public void alterar(MovibanERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MovibanERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MovibanERPBean> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "select max(numord) cod from moviban";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            int cod = 0;
            while (rs.next()) {
                if (rs.getString("cod") != null) {
                    cod = Integer.valueOf(rs.getString("cod")) + 1;
                }
            }
            return Funcoes.preencheCom(Integer.toString(cod), "0", 12, Funcoes.LEFT);
        } catch (Exception e) {
            logger.error("ERRO ao Consultar ID ERP do ultimo registro Cadastrado: " + e);
            return null;
        } finally {
            stmt.close();
            rs.close();
        }
    }
    
}
