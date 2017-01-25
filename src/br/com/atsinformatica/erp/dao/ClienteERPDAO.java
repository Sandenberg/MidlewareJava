package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.ClienteERPBean;
import br.com.atsinformatica.erp.entity.EnderecoERPBean;
import br.com.atsinformatica.erp.entity.EstadoERPBean;
import br.com.atsinformatica.erp.entity.TipoPessoa;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.utils.Funcoes;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class ClienteERPDAO implements IGenericDAO<ClienteERPBean> {
    private static Logger logger = Logger.getLogger(ClienteERPDAO.class);
    private Connection conn;

    @Override
    public void gravar(ClienteERPBean cliente) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String gravarClienteComEndereco(ClienteERPBean cliente, EnderecoERPBean endereco, EstadoERPBean estadoERPBean) {
        PreparedStatement pstmt = null;
        try {
            CodigoCliente codigo = montaUltimoRegistro();
            String codCidade = buscaOuIncluiCidade(endereco.getCity(), estadoERPBean.getSigla());
            
            conn = ConexaoATS.getConnection();
            String sql = " INSERT INTO CLIENTE (CODCLIENTE, CODCLIENTEECOM, NOME, NOMEFANTASIA, "
                    + "                      EMAIL, DT_NASCIMENTO,ENDERECO, BAIRRO, CEP, CODCIDADE, FONE, CELULAR, "
                    + "                      CGCCPF, PESSOA_FJ, INSCEST, ESTADO, ENDERECOCOB, BAIRROCOB, "
                    + "                      CODCIDADECOB, ESTADOCOB, CEPCOB,ENDERECOENT,                "
                    + "                      BAIRROENT,CODCIDADEENT, ESTADOENT, DT_CADASTRO, CEPENT     )   "
                    + "              VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, codigo.codigoCliente);
            pstmt.setString(2, cliente.getId().trim());
            pstmt.setString(3, cliente.getNome());
            pstmt.setString(4, cliente.getNomeFantasia());
            pstmt.setString(5, cliente.getEmail().trim());
            
            if (cliente.getAniversario() != null) {
                pstmt.setDate(6, new Date(cliente.getAniversario().getTime()));
            } else {
                pstmt.setDate(6, null);
            }
            
            pstmt.setString(7, endereco.getAddress1() + ", " + endereco.getNumero());
            pstmt.setString(8, endereco.getAddress2());
            pstmt.setString(9, endereco.getPostcode().replaceAll("[^0-9]", "")); // cep sem máscara
            pstmt.setString(10, codCidade);
            pstmt.setString(11, endereco.getPhone());
            pstmt.setString(12, endereco.getPhone_mobile());
            pstmt.setString(13, cliente.getCpfCnpj().replaceAll("[^0-9]", "")); // cpf/cnpj sem máscara
            pstmt.setString(14, cliente.getTipoPessoa().name());
            pstmt.setString(15, cliente.getRgIe());
            
            pstmt.setString(16, estadoERPBean.getSigla());
            pstmt.setString(17, endereco.getEnderecoCob());
            pstmt.setString(18, endereco.getBairroCob());
            pstmt.setString(19, codCidade);
            pstmt.setString(20, estadoERPBean.getSigla());
            pstmt.setString(21, endereco.getCepCob().replaceAll("[^0-9]", ""));
            pstmt.setString(22, endereco.getEnderecoCob());
            pstmt.setString(23, endereco.getBairroCob());
            pstmt.setString(24, codCidade);
            pstmt.setString(25, estadoERPBean.getSigla()); 
            
            java.util.Date dataATual = new java.util.Date();
            pstmt.setDate(26, new java.sql.Date(dataATual.getTime()));
            
            pstmt.setString(27, endereco.getCepCob().replaceAll("[^0-9]", ""));
            pstmt.executeUpdate();
            
            gravaContato(codigo.codigoCliente, endereco, cliente);
            
            if (codigo.usaFilial) {
                sql = "SELECT codempresa FROM ncliente WHERE codempresa = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, codigo.codEmpresa);
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    sql = "UPDATE ncliente SET numeroclientes = ? WHERE codempresa = ?";
                } else {
                    sql = "INSERT INTO ncliente numeroclientes = ?, codempresa = ?";
                }
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setFloat(1, codigo.proxCliente);
                pstmt.setString(2, codigo.codEmpresa);
                
                pstmt.execute();
            }
            
            return codigo.codigoCliente;
        } catch (Exception e) {
            logger.error("Erro ao gravar cliente ERP", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
    public void gravaContato(String codCliente, EnderecoERPBean endereco, ClienteERPBean cliente){
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO CLIENTECONTATOS (CODCLIENTE, CONTATO, TELEFONE, EMAIL) VALUES (?,?,?,?)";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, codCliente);
            pstmt.setString(2, cliente.getNome());
            pstmt.setString(3, endereco.getPhone());
            pstmt.setString(4, cliente.getEmail());
            pstmt.executeUpdate();
            logger.info("Contato do cliente gravado com sucesso!");
        }catch(Exception ex){
            logger.error("Erro ao gravar contato do cliente", ex);
            throw new RuntimeException(ex);
        }finally{
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
        
    }

    public String atualizarClienteComEndereco(String codigoCliente, ClienteERPBean cliente, EnderecoERPBean endereco, EstadoERPBean estadoERPBean) {
        PreparedStatement pstmt = null; 
        ResultSet rs = null;
        try {
            String codCidade = buscaOuIncluiCidade(endereco.getCity(), estadoERPBean.getSigla());
            
            conn = ConexaoATS.getConnection();
            String sql = " UPDATE CLIENTE SET NOME=?, NOMEFANTASIA=?, "
                    + "                      EMAIL=?, DT_NASCIMENTO=?,ENDERECO=?, BAIRRO=?, CEP=?, FONE=?, CELULAR=?, "
                    + "                      CGCCPF=?, PESSOA_FJ=?, INSCEST=?, ESTADO=?, "
                    + "                      ENDERECOCOB=?, BAIRROCOB=?, CODCIDADECOB=?, "
                    + "                      ESTADOCOB=?, CEPCOB=?, ENDERECOENT=?,       "
                    + "                      BAIRROENT=?,CODCIDADEENT=?,                 "
                    + "                      CODCLIENTEECOM=?                            "
                    + "    WHERE CODCLIENTE=?  ";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getNomeFantasia());
            pstmt.setString(3, cliente.getEmail().trim());
            
            if (cliente.getAniversario() != null) {
                pstmt.setDate(4, new Date(cliente.getAniversario().getTime()));
            } else {
                pstmt.setDate(4, null);
            }            
            
            pstmt.setString(5, endereco.getAddress1());
            pstmt.setString(6, endereco.getAddress2());
            pstmt.setString(7, endereco.getPostcode().replaceAll("[^0-9]", ""));
            pstmt.setString(8, endereco.getPhone());
            pstmt.setString(9, endereco.getPhone_mobile());
            pstmt.setString(10, cliente.getCpfCnpj().replaceAll("[^0-9]", "")); // Cpf/Cnpj sem máscara!
            pstmt.setString(11, cliente.getTipoPessoa().name());
            pstmt.setString(12, cliente.getRgIe());
            pstmt.setString(13, estadoERPBean.getSigla());

            pstmt.setString(14, endereco.getEnderecoCob() + ", " + endereco.getNumeroCob());
            pstmt.setString(15, endereco.getBairroCob());
            pstmt.setString(16, codCidade);
            pstmt.setString(17, estadoERPBean.getSiglaCobracao());
            pstmt.setString(18, endereco.getCepCob().replaceAll("[^0-9]", ""));
            pstmt.setString(19, endereco.getEnderecoCob());
            pstmt.setString(20, endereco.getBairroCob());
            pstmt.setString(21, codCidade);
            pstmt.setString(22, cliente.getId().trim()); // CODCLIENTEECOM
            pstmt.setString(23, codigoCliente); // CODCLIENTE
            pstmt.execute();
            
            /* Busca o codigo do cliente 
            sql = "SELECT CODCLIENTE FROM CLIENTE WHERE CODCLIENTEECOM = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cliente.getId().trim());
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getString("CODCLIENTE") != null) { 
                return rs.getString("CODCLIENTE");
            } else {
                throw new RuntimeException("Não foi possivel encontrar o codigo do cliente.");
            }*/
            return codigoCliente;
        } catch (Exception e) {
            logger.error("Erro ao atualizar cliente", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    @Override
    public void alterar(ClienteERPBean object) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClienteERPBean abrir(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ClienteERPBean> listaTodos() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override @Deprecated
    public String ultimoRegistro(){
        throw new UnsupportedOperationException("Médoto não disponível para clientes");
    }
    
    public CodigoCliente montaUltimoRegistro(){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            CodigoCliente codigo = new CodigoCliente();
            codigo.codEmpresa = ParaEcomService.getCodEmpresaEcom();
            
            String sqlFilial = "select CODCLIENTEEMP from PARACAD";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlFilial);
            if (rs.next()) {
                // S = O sistema utiliza filial
                // M = Parâmetro oculto
                // null = O sistema não utiliza filial
                codigo.usaFilial = "S".equals(rs.getString("CODCLIENTEEMP"));
            }
                            
            // Se o sistema separa os clientes por filial
            if (codigo.usaFilial) {
                PreparedStatement pstmt = null;
                String sql = "select NUMEROCLIENTES from NCLIENTE where CODEMPRESA = ?";
                pstmt = conn.prepareStatement(sql);
                
                // Buscando a filial                             
                pstmt.setString(1, codigo.codEmpresa);
                rs = pstmt.executeQuery();
                                
                if (rs.next() && rs.getString("NUMEROCLIENTES") != null) {
                    codigo.proxCliente = rs.getInt("NUMEROCLIENTES") + 1;
                } else {
                    codigo.proxCliente = 1;
                }
                codigo.codigoCliente = codigo.codEmpresa + 
                        Funcoes.preencheCom(codigo.proxCliente.toString(), "0", 6, Funcoes.LEFT);
            } else { // Se o sistema não separa os clientes por filial
                stmt = null;
                String sql = "select max(CODCLIENTE) cod from CLIENTE";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                
                if (rs.next() && rs.getString("cod") != null) {
                    codigo.proxCliente = Integer.valueOf(rs.getString("cod")) + 1;
                } else {
                    codigo.proxCliente = 1;
                }
                codigo.codigoCliente = Funcoes.preencheCom(codigo.proxCliente.toString(), "0", 8, Funcoes.LEFT);
            }           
                        
            while (verificacaoClienteEcomExiste(codigo.codigoCliente)) {
                codigo.proxCliente += 1;
                if (codigo.usaFilial) {
                    codigo.codigoCliente = codigo.codEmpresa + 
                            Funcoes.preencheCom(codigo.proxCliente.toString(), "0", 6, Funcoes.LEFT);
                }
                else {
                    codigo.codigoCliente = Funcoes.preencheCom(codigo.proxCliente.toString(), "0", 8, Funcoes.LEFT);
                }
            }
            
            return codigo;
        } catch (Exception e) {
            logger.error("Erro ao descobrir proximo código para cliente", e);
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
    public boolean verificacaoClienteEcomExiste(String codClienteEcom) {        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODCLIENTEECOM FROM CLIENTE WHERE CODCLIENTE = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codClienteEcom);
            rs = pstmt.executeQuery();
            
            return rs.next();
        } catch (Exception e) {
            logger.error("Erro ao verificar se cliente existe no banco", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
    public String verificaClienteEcomExisteCpfCnpj(TipoPessoa tipoPessoa, String CpfCnpj) {        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT CODCLIENTE, CODCLIENTEECOM FROM CLIENTE WHERE PESSOA_FJ = ? AND CGCCPF = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipoPessoa.name());
            pstmt.setString(2, CpfCnpj.replaceAll("[^0-9]", ""));
            rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getString("CODCLIENTE") != null) {
                return rs.getString("CODCLIENTE");
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Erro ao verificar se cliente existe no banco", e);
            throw new RuntimeException(e);            
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (SQLException | NullPointerException e) {}            
        }
    }

    public String buscaOuIncluiCidade(String nomeCidade, String uf) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT C.CODCIDADE FROM CIDADES  C WHERE C.CIDADE = ? AND C.ESTADO = ? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nomeCidade.toUpperCase());
            pstmt.setString(2, uf.toUpperCase());
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getString("CODCIDADE") != null) {
                return Funcoes.preencheCom(rs.getString("CODCIDADE"), "0", 5, Funcoes.LEFT);
            } else {
                String codigoCidade = this.proximoCodigoCidade();
                sql = "INSERT INTO CIDADES (CODCIDADE, CIDADE, ESTADO, CODMUNICIPIO, CODSIAFI) "
                        + "  VALUES (?, ? , ?, NULL, NULL); ";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, codigoCidade);
                pstmt.setString(2, nomeCidade.toUpperCase());
                pstmt.setString(3, uf.toUpperCase());
                pstmt.execute();
                
                return codigoCidade;
            }
        } catch (Exception e) {
            logger.error("Erro ao retornar/cadastrar codigo da cidade", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    public String proximoCodigoCidade(){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select max(CODCIDADE) cod from CIDADES";
            conn = ConexaoATS.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            Integer codigo = 1;
            if (rs.next() && rs.getString("cod") != null) {
                codigo = (Integer.valueOf(rs.getString("cod")) + 1);
            }
            
            return Funcoes.preencheCom(codigo.toString(), "0", 5, Funcoes.LEFT);
        } catch (Exception e) {
            logger.error("Erro ao descobrir qual o próximo codigo de cidade", e);
            throw new RuntimeException(e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    public ClienteERPBean getClientePorPedido(int codPedidoERP) throws SQLException, InstantiationException {
        String codEmpresa = new ParaEcomDAO().listaTodos().get(0).getCodEmpresaEcom();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ClienteERPBean clienteERPBean = new ClienteERPBean();
        conn = ConexaoATS.getConnection();
        try {
            String sql = "SELECT C.*  FROM PEDIDOC P                    "
                    + "    INNER JOIN                                   "
                    + "        CLIENTE C ON P.CODCLIENTE = C.CODCLIENTE "
                    + "    WHERE P.CODPEDIDO = ?  AND P.CODEMPRESA = ?  ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(codPedidoERP), "0", 8, Funcoes.LEFT));
            pstmt.setString(2, codEmpresa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("CODCLIENTE") != null) {
                    clienteERPBean.setId(rs.getString("CODCLIENTE"));
                    clienteERPBean.setEmail(rs.getString("EMAIL"));
                    clienteERPBean.setNome(rs.getString("NOME"));
                }
            }
            return clienteERPBean;
        } catch (Exception e) {
            logger.error("ERRO ao buscar cliente no banco: " + e);
            return null;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    private class CodigoCliente {
        private String codEmpresa;
        private Integer proxCliente;
        private String codigoCliente;
        private boolean usaFilial;
    }
}