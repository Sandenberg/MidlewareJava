package br.com.atsinformatica.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author niwrodrigues
 */
public class DateUtil {
    private static final Logger logger = Logger.getLogger(DateUtil.class);
    
    private static final SimpleDateFormat FORMATO_DATA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
    public static Date DATA_MINIMO = stringParaData("01/01/1900");
    public static Date DATA_MAXIMO = stringParaData("31/12/2999");

    /**
     * Validar se a data é valida
     *
     * @param dateStr
     * @return True ou False
     */
    public static boolean validaData(String dateStr) {
        try {
            Date date = DateUtil.stringParaData(dateStr);
            return DATA_MAXIMO.after(date) && DATA_MINIMO.before(date);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Converte string para data, não é obrigação deste método validar
     * obrigatoriedade da informação.
     *
     * @param str
     * @return
     */
    public static Date stringParaData(String str) {
        try {
            if (str != null && !str.isEmpty()) {
                return FORMATO_DATA.parse(str);
            }
            return null;
        } catch (ParseException e) {
            throw new RuntimeException(MessageFormat.format("\"{0}\" não é uma data válida.", str), e);
        } catch (Throwable e) {
            logger.error("Falha ao converter data.", e);
        }
        return null;
    }

    /**
     * Converte hora para string
     *
     * @param str
     * @return
     */
    public static String dataHoraParaString(Date data) {
        try {
            if (data != null) {
                return FORMATO_DATA_HORA.format(data);
            }
            return null;
        } catch (Throwable e) {
            logger.error("Falha ao converter hora.", e);
        }
        return null;
    }
    
    /**
     * Compara duas datas a verifica se a data inicial é maior que a data final
     *
     * @param dataini data inicial
     * @param dataFim data final
     * @return se verdadeiro ou falso
     */
    public static boolean comparaDatas(Date dataini, Date dataFim) {
        if (dataini.compareTo(dataFim) > 0) {
            return true;
        } else {
            return false;
        }

    }
   
    public static Date combinaDataHora(Date data, Date hora) {
        if (data == null) {
            return hora;
        } else if (hora == null) {
            return data;
        }

        Calendar h = Calendar.getInstance();		
        h.setTime(hora);
        
        Calendar d = Calendar.getInstance();		
        d.setTime(data);
        d.set(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
        d.set(Calendar.MINUTE, h.get(Calendar.MINUTE));
        d.set(Calendar.SECOND, h.get(Calendar.SECOND));
        d.set(Calendar.MILLISECOND, h.get(Calendar.MILLISECOND));
       
        return d.getTime();
    }
}
