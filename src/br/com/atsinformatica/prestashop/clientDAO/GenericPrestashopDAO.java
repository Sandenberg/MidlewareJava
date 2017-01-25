/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atsinformatica.prestashop.clientDAO;

import br.com.atsinformatica.erp.entity.ParaUrlWsdlBean;
import br.com.atsinformatica.midler.service.ParaEcomService;
import br.com.atsinformatica.prestashop.model.root.Prestashop;
import br.com.atsinformatica.prestashop.model.root.WsItem;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author AlexsanderPimenta
 */
public class GenericPrestashopDAO<T> {
    private final Logger logger = Logger.getLogger(GenericPrestashopDAO.class);
        
    /**
     * Retorna conexão com o WebService
     * @return
     */
    protected WebResource getWebResource() {
        //dao da parametro no resulth
       // ParaUrlDAO paraUrlDAO = new ParaUrlDAO();
        try {
            //cria configuração
            ClientConfig config = new DefaultClientConfig();
            //cliente webservice
            Client client = Client.create(config);
            //lista de parametro do WEB Service
           // List<ParaUrlWsdlBean> listaParaUrlWsdlBean = paraUrlDAO.listaTodos();
            //traz conxão com webservice
            WebResource resource = null;
            
            ParaUrlWsdlBean paraUrl = ParaEcomService.getUrlBean();
            if (paraUrl == null) {
                throw new RuntimeException("Não foi encontrado configuração de WS.");
            }     
            
            client.addFilter(new HTTPBasicAuthFilter(paraUrl.getUrlKey(), ""));
            if (this.getWebService(paraUrl)) {
                resource = client.resource(paraUrl.getUrlWSDL());
            }
            
            // Definindo um User Agent
            // *Alguns servidores bloqueiam se não for informado.
            client.addFilter(new ClientFilter() {
                @Override
                public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
                    request.getHeaders().add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1)");                                
                    // Mozilla/5.0 (Windows NT 6.1) 
                    // AppleWebKit/534.30 (KHTML, like Gecko) 
                    // Chrome/12.0.742.112 
                    // Safari/534.30
                    return getNext().handle(request);
                }
            });
            
            //retorna recurso do WebService em caso de sucesso
            return resource;
        } catch (Exception ex) {
            Logger.getLogger(GenericPrestashopDAO.class).error("Erro ao conectar no WebService "+ ex);
            throw new RuntimeException("Não foi encontrado nenhuma conexão com o WS.");
        }        
    }
    
    public boolean getWebService(ParaUrlWsdlBean bean) {
        try {
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            client.addFilter(new HTTPBasicAuthFilter(bean.getUrlKey(), ""));            
            
            // Definindo um User Agent
            //*Alguns servidores bloqueiam se não for informado.
            client.addFilter(new ClientFilter() {
                @Override
                public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
                    request.getHeaders().add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1)");                                
                    // Mozilla/5.0 (Windows NT 6.1) 
                    // AppleWebKit/534.30 (KHTML, like Gecko) 
                    // Chrome/12.0.742.112 
                    // Safari/534.30
                    return getNext().handle(request);
                }
            });
            
            try {
                client.setConnectTimeout(30000);
                // Essa linha lança Exception se o WS estiver offline
                String response = client.resource(bean.getUrlWSDL())
                        .path("test_connection").type(MediaType.APPLICATION_XML)
                        .get(String.class);
                // Se conectado com sucesso ao WebService
                return response.equals("1");
            } catch (Exception e) {
                logger.error("Falha conectar-se com o web service.", e);
                // Se não houve conexão com o webservice
                return false;
            }
        } catch (UniformInterfaceException ex) {
            Logger.getLogger(GenericPrestashopDAO.class).error(ex);
            return false;
        }
    }

    public static ClientConfig configureClient() {
        TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }
        };
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, certs, new SecureRandom());
        } catch (java.security.GeneralSecurityException ex) {
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

        ClientConfig config = new DefaultClientConfig();
        try {
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
                    new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            },
                    ctx));
        } catch (Exception e) {
        }
        return config;
    }

    //public 
    /**
     * Retorna o xml (função obrigatória);
     *
     * @param ps
     * @return
     */
    public String createTOXML(Prestashop ps) {
        try {
            JAXBContext context = JAXBContext.newInstance(ps.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter out = new StringWriter();
            marshaller.marshal(ps, new StreamResult(out));
            System.out.println(out);
            return out.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(CategoryPrestashopDAO.class).error(ex);
        }
        return "";
    }
    
    public String createTOXML(Object obj) {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter out = new StringWriter();
            marshaller.marshal(obj, new StreamResult(out));
          //  System.out.println(out);
            Logger.getLogger(GenericPrestashopDAO.class).info(out);
            return out.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(GenericPrestashopDAO.class).error(ex);
        }
        return "";
    }

    /**
     * Deserializa o objeto, através do xml
     *
     * @param xml
     * @return
     */
    public Prestashop unmarshallContext(String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(Prestashop.class);
            Unmarshaller unmarshalContext = context.createUnmarshaller();
            Prestashop p = (Prestashop) unmarshalContext.unmarshal(new StringReader(xml));
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
     public Object unmarshallObject(String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(Object.class);
            Unmarshaller unmarshalContext = context.createUnmarshaller();
            Object p = (Object) unmarshalContext.unmarshal(new StringReader(xml));
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    /**
     * Deserializa o objeto, através do xml
     *
     * @param xml
     * @return
     */
    public WsItem unmarshallContext_(String xml) {
        try {            
            JAXBContext context = JAXBContext.newInstance(WsItem.class);
            
            Document dom = new SAXReader().read(new StringReader(xml));
            Element element = dom.getRootElement();
            element.setName("ats");
            String xmlTemp = dom.asXML();
            Unmarshaller unmarshalContext = context.createUnmarshaller();
            WsItem p = (WsItem) unmarshalContext.unmarshal(new StringReader(xmlTemp));
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        
}
