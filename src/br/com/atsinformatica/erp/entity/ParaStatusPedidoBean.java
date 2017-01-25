package br.com.atsinformatica.erp.entity;

import br.com.atsinformatica.midler.entity.enumeration.StatusPedido;
import com.towel.el.annotation.Resolvable;

public class ParaStatusPedidoBean {

    @Resolvable(colName = "ID")
    private Integer id;
    @Resolvable(colName = "Nome")
    private String nome;
    @Resolvable(colName = "Módulo")
    private String modulo;
    @Resolvable(colName = "Status correspondente")
    private StatusPedido status;
    @Resolvable(colName = "Principal")
    private boolean principal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

}
