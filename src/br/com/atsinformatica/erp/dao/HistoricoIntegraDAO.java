package br.com.atsinformatica.erp.dao;

import br.com.atsinformatica.erp.entity.HistoricoIntegraERPBean;
import br.com.atsinformatica.erp.entity.StatusPedidoERP;
import br.com.atsinformatica.erp.entity.SugestVendasERPBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.entity.enumeration.TipoOperacao;
import br.com.atsinformatica.midler.entity.filter.HistoricoFilter;
import br.com.atsinformatica.midler.jdbc.ConexaoATS;
import br.com.atsinformatica.utils.Funcoes;
import br.com.atsinformatica.utils.SingletonUtil;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author AlexsanderPimenta
 */
public class HistoricoIntegraDAO implements IGenericDAO<HistoricoIntegraERPBean> {

    private static Logger logger = Logger.getLogger(HistoricoIntegraDAO.class);
    private Connection conn;

    @Override
    public void gravar(HistoricoIntegraERPBean object) {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "INSERT INTO HISTINTEGECOM (ID, ENTIDADE, CODENTIDADE, DATAENT, DATAINT, TIPOOPER, STATUS) "
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";
            String codigo = ultimoRegistro();
            
            pstmt = conn.prepareStatement(querie); 
            pstmt.setString(1, codigo);
            pstmt.setString(2, object.getEntidade() != null ? object.getEntidade().toString() : null);
            pstmt.setString(3, object.getCodEntidade());
            pstmt.setDate(4, new java.sql.Date(object.getDataEnt().getTime()));
            if (object.getDataInteg() != null) {
                pstmt.setDate(5, new java.sql.Date(object.getDataInteg().getTime()));
            } else {
                pstmt.setDate(5, null);
            }
            pstmt.setString(6, object.getTipoOperacao().toString());
            pstmt.setString(7, object.getStatus() != null ? object.getStatus().toString() : null);
            pstmt.execute();
            
            object.setId(Integer.parseInt(codigo));
        } catch (Exception e) {
            logger.error("Erro ao salvar historico ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException se) {}
        }
    }

    @Override
    public void alterar(HistoricoIntegraERPBean object) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            String querie = "UPDATE HISTINTEGECOM SET entidade = ?, codentidade = ?, dataent = ?,   "
                    + "dataint = ?, tipooper = ?, status = ?                                  "
                    + "where id = ?                                                           ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setString(1, object.getEntidade() != null ? object.getEntidade().toString() : null);
            pstmt.setString(2, object.getCodEntidade());
            pstmt.setDate(3, new java.sql.Date(object.getDataEnt().getTime()));
            if (object.getDataInteg() != null) {
                pstmt.setDate(4, new java.sql.Date(object.getDataInteg().getTime()));
            } else {
                pstmt.setDate(4, null);
            }
            pstmt.setString(5, object.getTipoOperacao().toString());
            pstmt.setString(6, object.getStatus() != null ? object.getStatus().toString() : null);
            pstmt.setInt(7, object.getId());
            pstmt.executeUpdate();
            logger.info("Historico de integração alterado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao salvar historico ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException se) {}
        }
    }

    public void alteraDataInt(int id, String status) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            conn = ConexaoATS.getConnection();
            Date dataAtual = new Date();
            String querie = "UPDATE HISTINTEGECOM SET dataint = ?, status = ?   "
                    + " where id = ?                                      ";
            pstmt = conn.prepareStatement(querie);
            pstmt.setDate(1, new java.sql.Date(dataAtual.getTime()));
            pstmt.setString(2, status);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            logger.info("Historico de integração alterado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao alterar histórico de integração: " + e);
        } finally {
            pstmt.close();
        }
    }

    @Override
    public void deletar(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HistoricoIntegraERPBean abrir(String id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM histintegecom WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(id));
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return this.parseBean(rs);
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro ao salvar historico ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException se) {}
        }
    }
    
    /* Procura histórico pelo codentidade - Usado para não duplicar histórico de erro em Pedidos Cancelados */
    public HistoricoIntegraERPBean procurar(String codentidade) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM (" +
            "    SELECT FIRST 1 * FROM histintegecom h" +
            "        WHERE UPPER(h.entidade) = 'PEDIDO'" +
            "        AND UPPER(h.tipooper) = 'UPDATE'" +
            "        AND h.codentidade = ?" +
            "    ORDER BY h.dataent DESC, h.id DESC) s" +
            "   WHERE UPPER(s.status) = 'ERRO'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codentidade);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return this.parseBean(rs);
            }
            return null;
        } catch (Exception e) {
            logger.error("Erro ao procurar no historico ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException se) {}
        }
    }

    @Override
    public List<HistoricoIntegraERPBean> listaTodos() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "select * from histintegecom";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            List<HistoricoIntegraERPBean> listHistBean = new ArrayList<>();
            while (rs.next()) {
                listHistBean.add(this.parseBean(rs));
            }
            return listHistBean;
        } catch (Exception e) {
            return null;
        } finally {
            stmt.close();
            rs.close();
        }
    }
    
    public synchronized List<HistoricoIntegraERPBean> listaUltimos(HistoricoFilter filter, int qtdeMantidos){        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            
            List<HistoricoIntegraERPBean> listHistBean = new ArrayList<>();
            if (qtdeMantidos != 0) {
                StringBuilder sql = new StringBuilder()
                    .append("SELECT FIRST ").append(qtdeMantidos)
                    .append("   h.id id,                                                    ")
                    .append("   h.entidade entidade,                                        ")
                    .append("   h.codentidade codentidade,                                  ")
                    .append("   h.dataent dataent,                                          ")
                    .append("   h.dataint dataint,                                          ")
                    .append("   h.tipooper tipooper,                                        ")
                    .append("   h.status status                                             ")
                    .append("FROM histintegecom h                                           ")
                    .append("   WHERE 1 = 1                                                 "); //Só para facilitar
                
                List<Object> filtro = new LinkedList<>();                                
                if (filter.getEntidade() != null) {
                    sql.append(" AND UPPER(h.entidade) = ?");
                    filtro.add(filter.getEntidade().toString());
                }
                if (filter.getTipoOperacao() != null) {
                    sql.append(" AND UPPER(h.tipooper) = ?");
                    filtro.add(filter.getTipoOperacao().toString());
                }
                if (filter.getStatus() != null) {
                    sql.append(" AND UPPER(h.status) = ?");
                    filtro.add(filter.getStatus().toString());
                }

                if (filter.getTipoPeriodo() != null) {
                    String campoData = null;
                    switch (filter.getTipoPeriodo()) {
                        case ENTRADA: {
                            campoData = "UPPER(h.dataent)";
                            break;
                        }
                        case INTEGRACAO:{
                            campoData = "UPPER(h.dataint)";
                            break;
                        }
                        default:
                            throw new RuntimeException("Tipo de data não conhecido.");
                    }
                    
                    if (filter.getDataInicio() != null) {
                        sql.append(" AND ").append(campoData).append(" >= ?");
                        filtro.add(new java.sql.Date(filter.getDataInicio().getTime()));
                    }
                    if (filter.getDataFinal() != null) {
                        // Evita problemas com hora
                        Calendar c = Calendar.getInstance();
                        c.setTime(filter.getDataFinal());
                        c.add(Calendar.DATE, 1);
                        
                        sql.append(" AND ").append(campoData).append(" < ?");
                        filtro.add(new java.sql.Date(c.getTime().getTime()));
                    }
                }
                
                sql.append(" ORDER BY h.dataent DESC ");
                //sql.append(" WITH LOCK "); // FOR UPDATE WITH LOCK Testando se resolve problema das threads
                
                pstmt = conn.prepareStatement(sql.toString());
                for (int i = 0; i < filtro.size(); i++) {
                    pstmt.setObject(i + 1, filtro.get(i));
                }
                
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    listHistBean.add(this.parseBean(rs));
                }
            }
            return listHistBean;
        } catch (Exception e) {
            logger.error("Falha ao obter registros de sincronização.", e);
            throw new RuntimeException(e);
        } finally {
            try {
            } catch (Exception e) {
                logger.debug("Falha ao fechar conecção.", e);
            }
            try {
                pstmt.close();
            } catch (Exception e) {
                logger.debug("Falha ao fechar Statement.", e);
            }
            try {
                rs.close();
            } catch (Exception e) {
                logger.debug("Falha ao fechar ResultSet.", e);
            }
        }
    }

    /**
     * Retorna itens pendentes de integração
     *
     * @return Lista com itens pendentes
     */
    public HistoricoIntegraERPBean proximoItemPendente() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT FIRST 1 * FROM histintegecom WHERE UPPER(status) = ? "
                    + " AND UPPER(entidade) <> ? ORDER BY dataent ASC";
            
            // #VS Testando: Erro "Cursor is not open"
            pstmt = conn.prepareStatement(sql);
            /*
            pstmt = conn.prepareStatement(
                sql, 
                ResultSet.TYPE_SCROLL_INSENSITIVE,
		ResultSet.CONCUR_READ_ONLY,
                ResultSet.HOLD_CURSORS_AT_COMMIT
            );
            //*/
            
            pstmt.setString(1, StatusIntegracao.PENDENTE.toString());
            pstmt.setString(2, TipoEntidade.PEDIDO.toString());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                HistoricoIntegraERPBean bean = this.parseBean(rs);
                
                if (bean.getEntidade().toString().contains("STATUS")) {
                    StatusPedidoERP statusPedidoErp = new StatusPedidoERP();
                    if (TipoEntidade.STATUSPEDIDOECOM.equals(bean.getEntidade())) {
                        String str[] = bean.getCodEntidade().split(";");
                        statusPedidoErp = new StatusPedidoERP();
                        statusPedidoErp.setIdPedidoEcom(Integer.valueOf(str[0]));
                        statusPedidoErp.setStatus(StatusPedido.thisValueOf(str[1]));
                    } else if (TipoEntidade.STATUSNFSAIDECOM.equals(bean.getEntidade())) {
                        int idPedidoEcom = this.getPedidoDAO().getIdPedidoEcom(bean.getCodEntidade());
                        if (idPedidoEcom != 0) {
                            statusPedidoErp.setIdPedidoEcom(idPedidoEcom);
                            statusPedidoErp.setStatus(StatusPedido.NF_EMITIDA);
                        }
                        
                    } else if (TipoEntidade.STATUSCONTROLEENTREGA.equals(bean.getEntidade())) {
                        int idPedidoEcom = this.getPedidoDAO().getIdPedidoEcom(bean.getCodEntidade());
                        if (idPedidoEcom != 0) {
                            statusPedidoErp.setIdPedidoEcom(idPedidoEcom);
                            statusPedidoErp.setStatus(StatusPedido.ENVIADO);
                        }
                    } else {
                        throw new RuntimeException("Tipo desconhecido.");
                    }
                    
                    bean.setObjectSinc(statusPedidoErp);
                } else if (bean.getEntidade().toString().contains("PRODAGREGADO")) {
                    bean.setObjectSinc(getSugestVendaObj(bean.getCodEntidade()));
                } else {                    
                    GenericDAO dao = MappedClassesDAO.getInstancia(bean.getEntidade());
                    if (dao != null) {
                        bean.setObjectSinc(dao.abrir(bean.getCodEntidade()));
                    } else {
                        throw new RuntimeException("Não foi possível encontrar o DAO para entidade: " + bean.getEntidade());
                    }
                }
                
                return bean;
            }
            return null;
        } catch (RuntimeException | SQLException e) {
            logger.error("Falha ao obter registros pendentes.", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {
                logger.debug("Falha ao fechar Statement.", e);
            }
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {
                logger.debug("Falha ao fechar ResultSet.", e);
            }
        }
    }
    
    public List<HistoricoIntegraERPBean> listaEmAndamento() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT * FROM histintegecom WHERE UPPER(status) = ? ";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, StatusIntegracao.EM_ANDAMENTO.toString());
            
            rs = pstmt.executeQuery();
            List<HistoricoIntegraERPBean> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(this.parseBean(rs));
            }
            return lista;
        } catch (RuntimeException | SQLException e) {
            logger.error("Falha ao obter registros em andamento.", e);
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {
                logger.debug("Falha ao fechar Statement.", e);
            }
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {
                logger.debug("Falha ao fechar ResultSet.", e);
            }
        }
    }
        
    public boolean possuiPendentesIntegracao() {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoATS.getConnection();
            String sql = "SELECT COUNT(h.id) as total FROM histintegecom h "
                    + "WHERE UPPER(status) = ? OR UPPER(status) = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, StatusIntegracao.PENDENTE.toString());
            pstmt.setString(2, StatusIntegracao.EM_ANDAMENTO.toString()); // Caso tenha fechado no meio do processo
            rs = pstmt.executeQuery();
            
            return rs.next() ? (rs.getInt("total") > 0) : false;
        } catch (RuntimeException | SQLException e) {
            logger.error("Falha ao obter registros pendentes.", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException | NullPointerException e) {
                logger.debug("Falha ao fechar Statement.", e);
            }
            try {
                rs.close();
            } catch (SQLException | NullPointerException e) {
                logger.debug("Falha ao fechar ResultSet.", e);
            }
        }
    }

    @Override
    public String ultimoRegistro() throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT max(ID) cod FROM HISTINTEGECOM";
            conn = ConexaoATS.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            int cod = 1;
            if(rs.next()){
                if (rs.getString("cod") != null) {
                    cod = Integer.valueOf(rs.getString("cod")) + 1;
                }   
            } 
            return Funcoes.preencheCom(Integer.toString(cod), "0", 6, Funcoes.LEFT);
        } catch (Exception e) {
            return null;
        } finally {
            stmt.close();
            rs.close();
        }
    }

    /**
     * Recebe string com código de produtos agregado e retorna objeto de produto
     * agregado
     *
     * @param codItem
     * @return objeto produto agregado
     */
    private SugestVendasERPBean getSugestVendaObj(String codItem) {
        SugestVendasERPBean sugestVendasERPBean = new SugestVendasERPBean();
        String str[] = codItem.split("-");
        sugestVendasERPBean.setIdProdEcom1(Integer.valueOf(str[0]));
        sugestVendasERPBean.setIdProdEcom2(Integer.valueOf(str[1]));
        return sugestVendasERPBean;
    }

    public PedidoCERPDAO getPedidoDAO() {
        return SingletonUtil.get(PedidoCERPDAO.class);
    }
    
    private HistoricoIntegraERPBean parseBean(ResultSet rs) throws SQLException{
        HistoricoIntegraERPBean bean = new HistoricoIntegraERPBean();
        bean.setId(rs.getInt("id"));
        bean.setEntidade(TipoEntidade.converteValor(rs.getString("entidade")));
        bean.setCodEntidade(rs.getString("codentidade"));
        bean.setDataEnt(rs.getDate("dataent"));
        bean.setDataInteg(rs.getDate("dataint"));
        bean.setTipoOperacao(TipoOperacao.converteValor(rs.getString("tipooper")));
        bean.setStatus(StatusIntegracao.converteValor(rs.getString("status")));
        
        return bean;
    }
}
