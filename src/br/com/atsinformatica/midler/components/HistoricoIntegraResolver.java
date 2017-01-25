package br.com.atsinformatica.midler.components;

import br.com.atsinformatica.erp.dao.PedidoCERPDAO;
import br.com.atsinformatica.erp.entity.HistoricoIntegraERPBean;
import br.com.atsinformatica.erp.entity.PedidoCBean;
import br.com.atsinformatica.erp.entity.PedidoCERPBean;
import br.com.atsinformatica.midler.entity.enumeration.StatusIntegracao;
import br.com.atsinformatica.midler.entity.enumeration.TipoEntidade;
import br.com.atsinformatica.midler.entity.enumeration.TipoOperacao;
import br.com.atsinformatica.utils.SingletonUtil;
import com.towel.el.annotation.Resolvable;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

/**
 *
 * @author niwrodrigues
 */
public class HistoricoIntegraResolver {
    
    @Resolvable(colName = "Id")
    private Integer id;
    @Resolvable(colName = "Item de sincronização")
    private TipoEntidade tipoEntidade;
    @Resolvable(colName = "Cód. ítem sinc. ")
    private String codigoEntidade;
    @Resolvable(colName = "Data de Entrada")
    private Date dataEntrada;
    @Resolvable(colName = "Data de Integração")
    private Date dataIntegracao;
    @Resolvable(colName = "Tipo de Operação")
    private TipoOperacao tipoOperacao;
    @Resolvable(colName = "Status")
    private StatusIntegracao status;
    @Resolvable(colName = "Outras informações")
    private String outrasInformacoes;
    
    private final HistoricoIntegraERPBean bean;

    public HistoricoIntegraResolver(HistoricoIntegraERPBean bean) {
        this.id = bean.getId();
        this.tipoEntidade = bean.getEntidade();
        this.codigoEntidade = bean.getCodEntidade();
        this.dataEntrada = bean.getDataEnt();
        this.dataIntegracao = bean.getDataInteg();
        this.tipoOperacao = bean.getTipoOperacao();
        this.status = bean.getStatus();
        this.bean = bean;

        if (TipoEntidade.STATUSPEDIDOECOM.equals(bean.getEntidade())) {
            try {
                String str[] = bean.getCodEntidade().split(";");
                if (str.length >= 3) {
                    this.codigoEntidade = str[2];
                } else {
                    PedidoCERPBean ped = SingletonUtil.get(PedidoCERPDAO.class).retornaPedidoPorPedidoEcom(Integer.valueOf(str[0]).intValue());
                    if (ped != null) {
                        this.codigoEntidade = ped.getId_erp();
                    } else {
                        this.outrasInformacoes = "Status: Pedido excluído no Resulth";
                        this.codigoEntidade = "N/A";
                    }
                }
            } catch (SQLException ex) {}
        } else if (TipoEntidade.PEDIDO.equals(bean.getEntidade()))  {
            try {
                PedidoCBean ped = SingletonUtil.get(PedidoCERPDAO.class).abrirPedido(bean.getCodEntidade());
                if (ped == null) {
                    this.outrasInformacoes = "Status: Pedido excluído no Resulth";
                    this.codigoEntidade = "N/A";
                } else {
                    this.codigoEntidade = ped.getCodPedidoEcom();
                    if (ped.getStatusPedidoEcom() != null) {
                        this.outrasInformacoes = MessageFormat.format("Código: {0} - Status: {1}", ped.getCodPedido(),
                                ped.getStatusPedidoEcom().getDescricao());
                    }
                }
            } catch (Exception e) {}
        }
    }

    
    
    public HistoricoIntegraERPBean getBean() {
        return bean;
    }
    
}
