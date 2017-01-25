/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.controller;

import br.com.atsinformatica.erp.entity.TransptERPBean;
import br.com.atsinformatica.prestashop.clientDAO.CarrierPrestashopDAO;
import br.com.atsinformatica.prestashop.model.node.Delay;
import br.com.atsinformatica.prestashop.model.node.Language;
import br.com.atsinformatica.prestashop.model.root.Carrier;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kennedimalheiros
 */
public class CarrierController {

    private static Logger logger = Logger.getLogger(CarrierController.class);

    public String getNameCarrier(int codCarrier) {
        CarrierPrestashopDAO dao = new CarrierPrestashopDAO();
        Carrier carrier;
        try {
            carrier = dao.getId(Carrier.URLCARRIER, codCarrier);
            return carrier.getName();
        } catch (Exception e) {
            logger.error("Erro ao consultar nome da Transportadora ID:(" + codCarrier + "): " + e);
            return null;
        }
    }

    public int insertCarrier(TransptERPBean transp) {
        CarrierPrestashopDAO dao = new CarrierPrestashopDAO();
        Carrier carrier = new Carrier();
        try {
            carrier = dao.postRetorno(Carrier.URLCARRIER, createCarrier(transp));
            return Integer.valueOf(carrier.getIdEcom());
        } catch (Exception e) {
            logger.error("Erro ao sincronizar Transportadora: " + e);
            return 0;
        }

    }

    private Carrier createCarrier(TransptERPBean transp) {
        Carrier carrier = new Carrier();
        if (!transp.getIdEcom().equals("0")) {
            carrier.setIdEcom(String.valueOf(transp.getIdEcom()));
        }
        carrier.setName(transp.getNome());
        carrier.setIdERP(transp.getIdERP());
        carrier.setActive(1); // 1 = TRUE = ATIVO
        Delay delay = new Delay();
        delay.getLanguage().add(new Language(transp.getNome()));
        carrier.setDelay(delay);
        return carrier;
    }

    public int update(TransptERPBean transp) {
        Carrier carrier = new Carrier();
        carrier = getCarrier(Integer.valueOf(transp.getIdEcom()));
        carrier.setName(transp.getNome());
        carrier.setIdReference(transp.getIdEcom());

        return new CarrierPrestashopDAO().put(Carrier.URLCARRIER, carrier);
    }

    public int deleteCarrier(String idCarrier) {

        Carrier carrier = new Carrier();
        carrier = getCarrier(Integer.valueOf(idCarrier));
        //fazendo exclusão lógica.
        carrier.setDeleted("1");
        return new CarrierPrestashopDAO().put(Carrier.URLCARRIER, carrier);
    }

    public Carrier getCarrier(int codCarrierEcom) {
        CarrierPrestashopDAO dao = new CarrierPrestashopDAO();
        //Lista de carriers do prestashop
        List<Carrier> carriers;
        //Lista com referência igual a pesquisada.
        List<Carrier> carriersComReferencia = new ArrayList<Carrier>();
        //Carrier de retorno
        Carrier carrier = null;
        try {
            carriers = dao.get(Carrier.URLCARRIER);

            for (Carrier car : carriers) {
                //Verificando se tem IdReferencia igual
                if (Integer.valueOf(car.getIdReference()) == codCarrierEcom) {
                    carriersComReferencia.add(car);
                }
            }

            /*.
             Verificando qual o maior IDEcom
             */
            for (Carrier car : carriersComReferencia) {
                if (carrier == null) {
                    carrier = car;
                } else if (Integer.valueOf(carrier.getIdEcom()) < Integer.valueOf(car.getIdEcom())) {
                    carrier = car;
                }
            }

            return carrier;
        } catch (Exception e) {
            logger.error("Erro ao consultar nome da Transportadora ID:(" + codCarrierEcom + "): " + e);
            return null;
        }
    }

}
