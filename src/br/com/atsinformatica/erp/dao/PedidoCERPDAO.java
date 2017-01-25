package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.EnderecoERPBean;
import br.com.atsinformatica.erp.entity.EstadoERPBean;
import br.com.atsinformatica.erp.entity.PedidoCBean;
import br.com.atsinformatica.erp.entity.PedidoCERPBean;
import br.com.atsinformatica.midler.annotation.GenericType;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.utils.Funcoes;
import br.com.atsinformatica.utils.SingletonUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 * 
 */
@GenericType(typeClass = TipoEntidade.PEDIDO)
public class PedidoCERPDAO extends GenericDAO<PedidoCERPBean> {

    private static Logger logger = Logger.getLogger(PedidoCERPDAO.class);
    private Connection conn;

    @Override
    public Class<PedidoCERPBean> getClasseBean() {
        return PedidoCERPBean.class;
    }
 
    /**
     * Retorna id do pedido na loja
     * @param codPedidoErp cod do pedido do erp
     * @return id do pedido da loja
     */
    public int getIdPedidoEcom(String codPedidoErp){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sqlPedido = "SELECT IDPEDIDOECOM FROM PEDIDOC p WHERE   "
                             + " p.CODPEDIDO = ? AND p.CODEMPRESA = ?      " 
                             + " AND p.TIPOPEDIDO = '55'                   ";
            
            pstmt = conn.prepareStatement(sqlPedido,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY, 
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
            pstmt.setString(1, codPedidoErp);
            pstmt.setString(2, ParaEcomService.getCodEmpresaEcom());
            rs = pstmt.executeQuery();
            
            if(rs.next()){
                return rs.getInt("IDPEDIDOECOM");
            }
            throw new RuntimeException("Pedido não encontrado");
        }catch(Exception e){
            throw new RuntimeException("Erro ao retornar id do pedido na loja.", e);           
        }
    }
    
    /**
     * Verifica se pedido ja existe no erp, para que não seja duplicado
     * @param idPedidoEcom id do pedido e-commerce
     * @return verdadeiro caso ja exista
     */
    public boolean pedidoExiste(int idpedidoEcom){
       // int idPedidoEcom = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sqlPedido = "SELECT CODPEDIDO FROM PEDIDOC WHERE  "
                             + "IDPEDIDOECOM = ?                           ";
            pstmt = conn.prepareStatement(sqlPedido);
            pstmt.setInt(1, idpedidoEcom);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }catch(Exception e){            
            logger.error("Erro ao retornar id do pedido na loja: "+e);  
            return false;
        }
    }
    
    public PedidoCERPBean retornaPedidoPorPedidoEcom(int idPedidoEcom) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "Select codpedido from pedidoc where idpedidoecom = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPedidoEcom);
            rs = pstmt.executeQuery();
            PedidoCERPBean pedidoC = null;
            while (rs.next()){
                pedidoC = new PedidoCERPBean();
                pedidoC.setId_erp(rs.getString("codpedido"));               
            }
            return pedidoC;
        }catch(Exception e){
            logger.error("Erro ao retornar codigo do pedidoC: "+e);
            return null;            
        }finally{
            pstmt.close();
            rs.close();
        }
    }
    
    /**
     * Atualiza status do pedido no ERP
     * @param idPedidoEcom id do pedido na loja virtual
     * @param codStatus cod do status do pedido
     * @throws SQLException 
     */
    public void atualizaStatusPedidoEcom(int idPedidoEcom, StatusPedido status) throws SQLException{
        if (idPedidoEcom <= 0) {
            throw new RuntimeException("O ID("+ idPedidoEcom +") do pedido do e-commerce é inválido.");            
        }
        
        PreparedStatement pstmt = null;        
        try{
            conn = ConexaoATS.getConnection();
            String qryPedidoEcom = "UPDATE PEDIDOC SET STATUSPEDIDOECOM = ? "+
                                   "WHERE IDPEDIDOECOM = ?                  ";
            pstmt = conn.prepareStatement(qryPedidoEcom);
            pstmt.setString(1, status.toString());
            pstmt.setInt(2, idPedidoEcom);
            pstmt.executeUpdate();
            logger.info("Status do pedido atualizado com sucesso.");
        }catch(Exception e){
            throw new RuntimeException("Erro ao atualizar status do pedido: ", e);            
        }finally{
            pstmt.close();
        }
    }
    
    

    /**
     * Verificar se numero do pedido informado ja existe no banco do ERP
     *
     * @param codPedidoEcom : Codigo do pedido gerado pelo Ecom
     * @return True se Existir, False se não existir.
     * @throws SQLException
     */
    public boolean verificarPedidoEcomExisteERP(String codPedidoEcom) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();

            String sql = "  SELECT P.IDPEDIDOECOM FROM PEDIDOC P "
                       + "     WHERE IDPEDIDOECOM = ?            ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codPedidoEcom);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Erro na Verificação de Cliente já existente: " + e);
            return false;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    public String gravarPedido(PedidoCERPBean pedidoERPBean, String codClienteERP, String codEmpresa, String codVendedor) {
        PreparedStatement pstmt = null;
        ParaFATDAO parafatDao = new ParaFATDAO();
        try {
            String codPedido = gerarCodPedido();
            
            conn = ConexaoATS.getConnection();
            String sql = " INSERT INTO PEDIDOC "
                    + "                    (CODPEDIDO, IDPEDIDOECOM, CODEMPRESA,                               "
                    + "                     TIPOPEDIDO, DESCONTOVLR,CODCLIENTE,                                "
                    + "                     DATAPEDIDO, FATURADO, HORA, FRETE,                                 "
                    + "                     OBSERVACAO, STATUSPEDIDOECOM, CODPEDIDOECOM, CODOPER,              "
                    + "                     CODVENDEDOR, TIPOFRETE, CODTVENDA                                ) "
                    + "              VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)                             ";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codPedido);
            pstmt.setString(2, pedidoERPBean.getId_ecom());
            pstmt.setString(3, codEmpresa);
            pstmt.setString(4, "55");
            pstmt.setDouble(5, Double.valueOf(pedidoERPBean.getTotal_discounts()));
            pstmt.setString(6, codClienteERP);
            
            // Removendo Hora da data
            java.util.Date date = pedidoERPBean.getDate_add();
            DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");  
            String formattedDate = formato.format(date); 
            date = formato.parse(formattedDate);            
            
            pstmt.setDate(7, new Date(date.getTime()));
            pstmt.setString(8, "N");
            pstmt.setString(9, pedidoERPBean.getHora());
            pstmt.setDouble(10, Double.valueOf(pedidoERPBean.getTotal_shipping()));
            pstmt.setString(11, "FORMA DE PAGAMENTO: " + pedidoERPBean.getModule()
                    + ", FORMA DE ENVIO: " + pedidoERPBean.getObservacao()
                    + ", CÓDIGO DO PEDIDO NA LOJA VIRTUAL: " + pedidoERPBean.getReference());
            pstmt.setString(12, pedidoERPBean.getCurrent_state().toString());
            pstmt.setString(13, pedidoERPBean.getReference());
            pstmt.setString(14, parafatDao.abrir().getTpopPad());
            pstmt.setString(15, codVendedor);
            if (!pedidoERPBean.getTotal_shipping().equals("0.00")) {
                pstmt.setString(16, "2");
            } else {
                pstmt.setString(16, "1");
            }
            pstmt.setString(17, pedidoERPBean.getCodTVenda());
            pstmt.executeUpdate();
            
            return codPedido;
        } catch (Exception e) {
            logger.error("Erro ao gravar pedido", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
    /**
     * Atualiza dados do pedido
     * @param pedidoCBean pedido
     * @throws SQLException 
     */
     public void atualizaPedido(PedidoCBean pedidoCBean) throws SQLException {         
         PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            //java.sql.Date dataPedido = new Date(pedidoERPBean.getDate_add().getTime());
            String sql = " UPDATE PEDIDOC SET CODPEDIDO = ?, IDPEDIDOECOM = ?, CODEMPRESA = ?,               "
                       + " TIPOPEDIDO = ?, DESCONTOVLR = ?, CODCLIENTE = ?,                                  "
                       + " DATAPEDIDO = ?, FATURADO = ?, FRETE = ?,                                          "
                       + " OBSERVACAO = ?, STATUSPEDIDOECOM = ?, CODPEDIDOECOM = ?, CODOPER = ?,             "
                       + " CODVENDEDOR = ?,TOTALPEDIDO = ?, DATAFINALIZAPEDIDOECOM = ?,                      "
                       + " CODPEDIDOORIGINAL = ?, CODPEDIDOVENDA = ?                                         "
                       + " WHERE CODPEDIDO = ? AND CODEMPRESA = ?                                            ";                      
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pedidoCBean.getCodPedido());
            pstmt.setInt(2, pedidoCBean.getIdPedidoEcom());
            pstmt.setString(3, pedidoCBean.getCodEmpresa());
            pstmt.setString(4, pedidoCBean.getTipoPedido());
            pstmt.setDouble(5, pedidoCBean.getDescontoVLR());
            pstmt.setString(6, pedidoCBean.getCodcliente());
            pstmt.setDate(7, new java.sql.Date(pedidoCBean.getDatPedido().getTime()));
            pstmt.setString(8, pedidoCBean.getFaturado());
            pstmt.setDouble(9, pedidoCBean.getFrete());
            pstmt.setString(10, pedidoCBean.getObservacao());
            pstmt.setString(11, pedidoCBean.getStatusPedidoEcom().toString());
            pstmt.setString(12, pedidoCBean.getCodPedidoEcom());
            pstmt.setString(13, pedidoCBean.getCodOper());
            pstmt.setString(14, pedidoCBean.getCodVendedor());
            pstmt.setDouble(15, pedidoCBean.getTotalPedido());
            if (pedidoCBean.getDtFinalizaPedidoEcom() != null) {
                pstmt.setDate(16, new java.sql.Date(pedidoCBean.getDtFinalizaPedidoEcom().getTime()));
            } else {
                pstmt.setDate(16, null);
            }
            pstmt.setString(17, pedidoCBean.getCodPedidoOriginal());
            pstmt.setString(18, pedidoCBean.getCodPedidoVenda());
            pstmt.setString(19, pedidoCBean.getCodPedido());
            pstmt.setString(20, pedidoCBean.getCodEmpresa());
            pstmt.executeUpdate();
            logger.info("Pedido atualizado com sucesso");            
        } catch (Exception e) {
            logger.error("Erro ao atualizar pedido: ", e);
            throw new RuntimeException(e);
        } finally {
            pstmt.close();
        }
    }

    public void gravarPedidoCompl(String codPedidoERP, String codClienteERP, EnderecoERPBean enderecoERPBean, EstadoERPBean estadoERPBean, String codEmpresa){
        ClienteERPDAO cERPdao = new ClienteERPDAO();
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "INSERT INTO PEDIDOCCOMPL "
                    + "    (CODEMPRESA, TIPOPEDIDO, CODPEDIDO, CODCLIENTE, "
                    + "     ENDERECOENT, BAIRROENT, CODCIDADEENT, ESTADOENT, "
                    + "     CEPENT, DATAHORAPREVISAOENT, NUMEROENT, COMPLEMENTOENT) "
                    + "    VALUES "
                    + "    (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, codEmpresa);
            pstmt.setString(2, "55");
            pstmt.setString(3, codPedidoERP);
            pstmt.setString(4, codClienteERP);
            pstmt.setString(5, enderecoERPBean.getEnderecoEnt());
            pstmt.setString(6, enderecoERPBean.getBairroEnt());
            pstmt.setString(7, cERPdao.buscaOuIncluiCidade(enderecoERPBean.getCodCidadeEnt(), estadoERPBean.getSigla()));
            pstmt.setString(8, estadoERPBean.getSigla());
            pstmt.setString(9, enderecoERPBean.getCepEnt().replace("-", ""));
            pstmt.setString(10, enderecoERPBean.getDataHoraPrevEnt());
            pstmt.setString(11, enderecoERPBean.getNumeroEnt());
            pstmt.setString(12, enderecoERPBean.getComplemento());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao gravar complemento do pedido", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }

    public String parametroUsaDAVPDV(String codEmpresa) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String retorno = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = " Select IMPORTAPEDIDO from PARAMECF "
                       + "                        WHERE CODEMPRESA = ? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEmpresa);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("IMPORTAPEDIDO") != null) {
                    retorno = rs.getString("IMPORTAPEDIDO");
                }
            }
            return retorno;
        } catch (Exception e) {
            logger.error("Erro na Verificação de parametro UsaDavPDV: " + e);
            return null;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    /**
     *
     * @return True se utiliza UsarGenPDV
     * @throws SQLException
     */
    public boolean parametroParaCadUsarGenPDV() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = " SELECT USARGENPDV FROM PARACAD ";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getString("USARGENPDV") != null) {
                return rs.getString("USARGENPDV").toUpperCase().equals("S");
            }
            return false;
        } catch (Exception e) {
            logger.error("Erro na Verificação de parametro UsarGenPDV", e);
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
  
    
    /**
     * Retorna pedidos que usam como meio de pagamento o pagseguro, paypal 
     * ou o cielo e que ainda não foram faturados
     * @param codEmpresa
     * @return 
     */
    public List<PedidoCERPBean> listaPedidosPendentes(String codEmpresa) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PedidoCERPBean> listaPedidosPendentes = new ArrayList<>();
        try{
            conn = ConexaoATS.getConnection();            
            String sql = "select pedidoc.codpedido, pedidoc.idpedidoecom from pedidoc\n" +
                         "join complementopedidoecom on complementopedidoecom.codpedido = pedidoc.codpedido\n" +
                         "where ((complementopedidoecom.formapagamento = 1)\n" +
//                         "or (complementopedidoecom.formapagamento = 3)\n" +
                         "or (complementopedidoecom.formapagamento = 2))\n" +
                         "And pedidoc.faturado = 'N'\n" +
                         "And pedidoc.codempresa = ?\n" +
                         " And pedidoc.tipopedido = '55'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEmpresa);
            rs = pstmt.executeQuery();
            while(rs.next()){
                PedidoCERPBean pedidoPendente = new PedidoCERPBean();
                pedidoPendente.setId_erp(rs.getString("codpedido"));
                pedidoPendente.setId_ecom(String.valueOf(rs.getInt("idpedidoecom")));
                listaPedidosPendentes.add(pedidoPendente);                
            }
            logger.info("Lista de pedidos pendentes, retornada com sucesso.");
            return listaPedidosPendentes;
        }catch(Exception e){
            logger.error("Erro ao retornar lista de itens pendentes: "+e);
            return null;
        }finally{
            pstmt.close();
            rs.close();       
        }
    }

    public String parametroParaFat(String campoRetorno) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String retorno = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = " SELECT " + campoRetorno + " FROM PARAFAT ";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getString(campoRetorno) != null) {
                    retorno = rs.getString(campoRetorno);
                }
            }
            return retorno;
        } catch (Exception e) {
            logger.error("Erro na Verificação de Parametro ParaFat do campo " + campoRetorno + ": " + e);
            return null;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    public int erpProximoCodGenPDV(String codEmpresa) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            
            String sql = "Select Gen_ID(GENPDV" + codEmpresa + ",1) as CodPedido from RDB$DATABASE ";
            pstmt = conn.prepareStatement(sql);
            
            Integer codigo = null;
            do {
                rs = pstmt.executeQuery();
                if (!rs.next() || rs.getString("CODPEDIDO") == null) {
                    throw new IllegalArgumentException("O gerador não retornou o valor.");
                }
                
                int value = rs.getInt("CODPEDIDO");
                String cod = Funcoes.preencheCom(String.valueOf(value), "0", 8, Funcoes.LEFT);
                if (!this.erpPedidoCExiste(codEmpresa, "55", cod)) {
                    codigo = value;
                }
                
            } while(codigo == null); 
            return codigo;
        } catch (Exception e) {
            logger.error("Erro na executar função erpProximoCodGenPDV", e);
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

    public int erpGenCodPedido() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = " select Gen_ID(G_CodPedido_PDV, 1) as Cod from RDB$DATABASE ";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getString("COD") != null) {
                return rs.getInt("COD");
            }
            throw new IllegalArgumentException("O gerador não retornou o valor.");
        } catch (Exception e) {
            logger.error("Erro na executar função erpGenCodPedido", e);
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
    
    public boolean pendenteFaturamento(String codPedido, String codEmpresa) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = "select codpedido from pedidoc "
                       + "where pedidoc.codempresa = ?"
                       + "and pedidoc.tipopedido = '55'"
                       + "and pedidoc.codpedido = ?"
                       + "and pedidoc.faturado = 'N'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEmpresa);
            pstmt.setString(2, codPedido);
            rs = pstmt.executeQuery();
            if(rs.next())
                return true;         
            return false;
        }catch(Exception e){
            logger.error("Erro ao retorna consulta de pedido pendente de faturamento: "+e);
            return false;
        }finally{
            rs.close();
            pstmt.close();
        }
    }

    /**
     * Se existir pedido com o mesmo codigo TRUE Se não existir pedido com o
     * mesmo codigo FALSE
     *
     * @param codEmpresa
     * @param tipoPedido
     * @param codPedido Maximo 8 digitos
     * @return
     * @throws SQLException
     */
    public boolean erpPedidoCExiste(String codEmpresa, String tipoPedido, String codPedido) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        /**
         * Passando para 8 digitos para verificar na tabela PEDICOC codPedido =
         * Funcoes.preencheCom(codPedido, "0", 8, Funcoes.LEFT);
         */
        codPedido = Funcoes.preencheCom(codPedido, "0", 8, Funcoes.LEFT);

        try {
            conn = ConexaoATS.getConnection();
            String sql = " SELECT DISTINCT CODPEDIDO                 "
                    + "                 FROM PEDIDOC                 "
                    + "                   WHERE                      "
                    + "                       CODEMPRESA = ?   AND   "
                    + "                       TIPOPEDIDO  = ?  AND   "
                    + "                       CODPEDIDO   = ?        ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEmpresa);
            pstmt.setString(2, tipoPedido);
            pstmt.setString(3, codPedido);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("CODPEDIDO").equals(codPedido);
            }
            return false;
        } catch (Exception e) {
            logger.error("Erro na executar função erpPedidoCExiste", e);
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

    //Se não utiliza UsarGenPDV.
    public int erpMaxNpdv(String codEmpresa) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            
            // Seleciono o último código do pedido passando a filial
            String sql = "SELECT                    "
                       + "    MAX(CODPEDIDO) AS COD "
                       + "FROM                      "
                       + "    PEDIDOC               "
                       + "WHERE                     "
                       + "  CODEMPRESA = ?          ";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEmpresa);
            rs = pstmt.executeQuery();

            Integer codigo = 0;
            if (rs.next() && rs.getString("COD") != null) {
                codigo = rs.getInt("COD");
            }
            // Retorno o próximo id do pedido
            codigo++;
                   
            // Atualizo a tabela NPDV com o código mais novo
            String sql_npdv = "UPDATE NPDV          "
                            + "    SET NUMEROPDV = ?"
                            + "WHERE                "
                            + "    CODEMPRESA = ?   ";
            
            pstmt = conn.prepareStatement(sql_npdv);
            pstmt.setInt(1, codigo);
            pstmt.setString(2, codEmpresa);            
            pstmt.executeUpdate();
            
            return codigo;
        } catch (Exception e) {
            logger.error("Erro na executar função erpMaxNpdv", e);
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

    /**
     * Verificar se utiliza ECF Vai verificar se tem registro na tabela PDVFisc
     * se não tiver é porque não utiliza.
     *
     * @param codEmpresa
     * @return True se utilizar ECF
     * @throws SQLException
     */
    public boolean erpUtilizaECF(String codEmpresa){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM PDVFISC P WHERE P.CODEMPRESA = ? AND P.MAQUINA <> '999'";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codEmpresa);
            rs = pstmt.executeQuery();

            return rs.next();
        } catch (Exception e) {
            logger.error("Erro ao verificar se ERP utiliza ECF", e);
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

    /**
     * Função vai validar todo os parametros para verificar qual a rotina é
     * utilizada para gerado o Código de pedido na tabela PedidoC
     *
     * @return Novo código de pedido com 8 digitos.
     * @throws SQLException
     */
    public String gerarCodPedido() {
        try {
            String codEmpresa = ParaEcomService.getCodEmpresaEcom();
            if (parametroParaFat("NUMERARPDVAUTO").equals("S")) {
                Integer codigoPedido = null;
                // Verifica se utiliza ECF
                if (erpUtilizaECF(codEmpresa)) {
                    codigoPedido = erpProximoCodGenPDV(codEmpresa);
                } else if (parametroParaCadUsarGenPDV()) { // parametroParaCadUsarGenPDV = S
                    codigoPedido = erpGenCodPedido();

                    //Se pedido existe, entra no loop que vai incrementar +1 ate que erpPedidoCExiste seja FALSE
                    while (erpPedidoCExiste(codEmpresa, "55", String.valueOf(codigoPedido))) {
                        codigoPedido += 1;
                    }
                } else { //parametroParaCadUsarGenPDV = N
                    codigoPedido = erpMaxNpdv(codEmpresa);
                }
                return Funcoes.preencheCom(String.valueOf(codigoPedido), "0", 8, Funcoes.LEFT);
            } else {
                throw new IllegalArgumentException("Não foi possível identificar o codigo do pedido.");
            }
        } catch (Exception e) {
            logger.error("Falha ao descobrir proximo numeto de pedido.", e);
            throw new RuntimeException(e);
        }
    }

    public String getCodReferenciaEcom(int codPedidoERP) throws SQLException {
        String codEmpresa = new ParaEcomDAO().listaTodos().get(0).getCodEmpresaEcom();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String referencia = null;

        try {
            conn = ConexaoATS.getConnection();

            String sql = " SELECT P.CODPEDIDOECOM FROM PEDIDOC P      "
                    + " WHERE P.CODPEDIDO = ? AND P.CODEMPRESA = ? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Funcoes.preencheCom(String.valueOf(codPedidoERP), "0", 8, Funcoes.LEFT));
            pstmt.setString(2, codEmpresa);
            rs = pstmt.executeQuery();
             while (rs.next()) {
                referencia = rs.getString("CODPEDIDOECOM");
            }
            return referencia;
        } catch (Exception e) {
            logger.error("Erro na buscar Referencia do pedido Ecom " + e);
            return null;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    public String getCodigoRatreio(Integer idPedidoEcom) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String codEmpresa = new ParaEcomDAO().listaTodos().get(0).getCodEmpresaEcom();

            StringBuilder sql = new StringBuilder()
                    .append("SELECT c.CODRASTREAMENTO FROM CONTROLEENTREGA c    ")
                    .append("   JOIN PedidoC p ON c.CODPEDIDO = p.CODPEDIDO     ")
                    .append("       AND c.CODEMPRESA = p.CODEMPRESA             ")
                    .append("WHERE p.IDPEDIDOECOM = ?");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, idPedidoEcom);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("CODRASTREAMENTO");
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro na buscar Referencia do pedido Ecom " + e);
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
    
    /**
     * Retorna lista de pedidos que ainda não foram faturados e que já foram sincronizados da loja virtual
     * @return List<PedidoCBean>
     * @throws SQLException 
     */
    public List<PedidoCBean> listaPedidosPendentes(){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = ConexaoATS.getConnection();
            String sql = " SELECT CODPEDIDO, IDPEDIDOECOM, CODEMPRESA,                        "
                       + " TIPOPEDIDO, DESCONTOVLR, CODCLIENTE,                               "
                       + " DATAPEDIDO, FATURADO, FRETE,                                       "
                       + " OBSERVACAO, STATUSPEDIDOECOM, CODPEDIDOECOM, CODOPER,              "
                       + " CODVENDEDOR, TIPOFRETE, TOTALPEDIDO, DATAFINALIZAPEDIDOECOM,       "
                       + " CODPEDIDOORIGINAL, CODPEDIDOVENDA                                  "
                       + " FROM PEDIDOC                                                       "
                       + " WHERE CODPEDIDO IS NOT NULL AND CODPEDIDO <> '' AND CODEMPRESA = ?     "
                       + " AND FATURADO = 'N' AND IDPEDIDOECOM IS NOT NULL AND IDPEDIDOECOM <> 0  ";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ParaEcomService.getCodEmpresaEcom());
            rs = pstmt.executeQuery();
            
            List<PedidoCBean> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new PedidoCBean(rs));
            }
            return lista;
        }catch(Exception e){
            logger.error("Erro ao retornar lista de pedidos não faturados e sincronizados na loja", e);
            throw new RuntimeException(e);
        }finally{
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
    /**
     * Retorna pedido do ERP baseado em código do pedido
     * @param id do pedido no erp
     * @return PedidoCBean
     * @throws SQLException 
     */
    public PedidoCBean abrirPedido(String id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null; 
        ParaEcomDAO paraEcomDAO = SingletonUtil.get(ParaEcomDAO.class);
        PedidoIERPDAO pedidoIERPDAO = SingletonUtil.get(PedidoIERPDAO.class);
        try{
            conn = ConexaoATS.getConnection();
            String codEmpresa = paraEcomDAO.listaTodos().get(0).getCodEmpresaEcom();
            String sql = " SELECT CODPEDIDO, IDPEDIDOECOM, CODEMPRESA,                        "
                       + " TIPOPEDIDO, DESCONTOVLR, CODCLIENTE,                               "
                       + " DATAPEDIDO, FATURADO, FRETE,                                       "
                       + " OBSERVACAO, STATUSPEDIDOECOM, CODPEDIDOECOM, CODOPER,              "
                       + " CODVENDEDOR, TIPOFRETE, TOTALPEDIDO, DATAFINALIZAPEDIDOECOM,       "
                       + " CODPEDIDOORIGINAL, CODPEDIDOVENDA                                  "
                       + " FROM PEDIDOC                                                       "
                       + " WHERE CODPEDIDO = ? AND CODEMPRESA = ?                             ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, paraEcomDAO.listaTodos().get(0).getCodEmpresaEcom());
            rs = pstmt.executeQuery();
            PedidoCBean pedidoC = null;
            while(rs.next()){
                pedidoC = new PedidoCBean(rs);               
                pedidoC.setItensPedido(pedidoIERPDAO.listaItensPedido(id, codEmpresa));
            }
            return pedidoC;
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {}
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
    
    /**
     * Cancela pedido no ERP
     * @param pedidoC pedido
     * @return retorn código com status de operação
     * @throws SQLException 
     */
    public int cancelaPedidoERP(PedidoCBean pedidoC){
        PreparedStatement pstmt = null;
        try{
            conn = ConexaoATS.getConnection();
            String querie = "   UPDATE PEDIDOC SET FATURADO = 'X', STATUSPEDIDOECOM = ?    " +
                            "   WHERE CODPEDIDO = ? AND CODEMPRESA = ?                     " +
                            "   AND TIPOPEDIDO = '55'                                      ";
            pstmt = conn.prepareStatement(querie);
            if (pedidoC != null) {
                pstmt.setString(1, StatusPedido.CANCELADO.toString());
                pstmt.setString(2, pedidoC.getCodPedido());
                pstmt.setString(3, pedidoC.getCodEmpresa());
            } else {
                throw new RuntimeException("Pedido não foi informado.");
            }
            return pstmt.executeUpdate();
        }catch(Exception e){
            logger.error("Erro ao cancelar pedido: ", e);
            throw new RuntimeException(e);
        }finally{
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {}
        }
    }
    
}