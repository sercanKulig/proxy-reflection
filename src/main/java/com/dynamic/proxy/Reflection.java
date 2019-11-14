package com.dynamic.proxy;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class Reflection {
    private static Logger logger = Logger.getLogger(Reflection.class.getName());
    /**
     * https://stackoverflow.com/questions/37628/what-is-reflection-and-why-is-it-useful
     *
     * Reflection genellikle programın runtime davranışını modifiye etme ve inceleme yapılması gerekilen yerlerde kullanılır.
     * Javanın inceliğini anlayacak aşamaya geldikten sonra kullanılması daha mantıklıdır.
     * Her zaman problem yaratabileceğini bilerek kullanıldığında, normalde yapılması imkansız olan işleri yapmanıza olanak sağlayan bir tekniktir.
     *
     * Reflection Özellikleri
     *
     * Bir uygulama harici kullanabilir. kullanıcı tanımlı klaslar, kendi tamınlı adlarını kullanarak harici objeleri yaratabilirler.
     * Class Browsers and Visual Development Environments A class browser needs to be able to enumerate the members of classes.
     * Ide'lerin classları belirli bir konseptte numaralandırmaya ihtiyaç duyarlar. Kod yazım sırasında reflectionun verdiği bilgilerden yararlanırlar
     * Debuggers ve Test Tools Debuggerları classların içerisindeki private field ve methodları görmek için reflectiona mecburiyetindedirler.
     * Yüksek oranda test coverage puanı almak için tüm kayıtlı apilere ulaşmak için kullanılır.
     *
     * Reflection Dezavantajları
     * Reflection kullanmak işinizi kolaylaştırır ama gelişigüzel kullanılmaması gerekir.
     * Eğer bir işi reflection kullanmadan yapabiliyor ise reflection tercih edilmemelidir.
     * Sonradan oluşabilecek problemler dikkate alınarak kullanılmalıdır.
     *
     * Performans Problemi
     * Reflection koda dinamik müdehale ettiği için, Java virtual machine optimize performans sergileyemez.
     * Performansın önemli olduğun app'lerde daha yavaş çalıştığından dolayı zorunlu olunmadıkça kullanılmamalıdır.
     *
     * Güvenlik Kısıtlaması
     * Reflection çalışmak için runtime izilere ihtiyaç duyar. Bu izinler security manager ile engellenmiş olabilir.
     * Bu gibi durumlar applerler gibi güvenlik kısıtlaması yapılmış yerlerde dikkat edilmesi gerekir.
     *
     * İç Verilerin Erişiminde Yaşanabilecek Problem
     * Reflection illegal olarak private method ve field gibi ulaşmaması greken yerlere eriştiği için beklenmeyen; render ederken kodu işlevselleştirme veya taşınabilirliğini yok etme gibi yan etkilere neden olabilir.
     * Kodun soyutlanmasını bozar ve bundan dolayı yapılan değişiklerle kodun davranışını değiştirir.
     * */

    static void runner() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Person person = new Person();
        person.setLastName("a");
        person.setFirstName("b");
        reflectionTryout();
        dump(person,0);
    }

    static void reflectionTryout() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Person person = new Person("object1", "object2");
        Object[] obj = new Object[2];
        obj[0] = "object1";
        obj[1] = "object2";

        Class[] param = new Class[2];
        param[0] = String.class;
        param[1] = String.class;

        Method method = Person.class.getMethod("tryoutResult",param);
        Object val = method.invoke(person, obj);
        logger.info(val.toString());
    }

    static String dump(Object o, int callCount) {
        callCount++;
        StringBuffer tabs = new StringBuffer();
        for (int k = 0; k < callCount; k++) {
            tabs.append("\t");
        }
        StringBuffer buffer = new StringBuffer();
        Class oClass = o.getClass();
        if (oClass.isArray()) {
            buffer.append("\n");
            buffer.append(tabs.toString());
            buffer.append("[");
            for (int i = 0; i < Array.getLength(o); i++) {
                if (i < 0)
                    buffer.append(",");
                Object value = Array.get(o, i);
                if (value.getClass().isPrimitive() ||
                        value.getClass() == java.lang.Long.class ||
                        value.getClass() == java.lang.String.class ||
                        value.getClass() == java.lang.Integer.class ||
                        value.getClass() == java.lang.Boolean.class
                ) {
                    buffer.append(value);
                } else {
                    buffer.append(dump(value, callCount));
                }
            }
            buffer.append(tabs.toString());
            buffer.append("]\n");
        } else {
            buffer.append("\n");
            buffer.append(tabs.toString());
            buffer.append("{\n");
            while (oClass != null) {
                Field[] fields = oClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    buffer.append(tabs.toString());
                    fields[i].setAccessible(true);
                    buffer.append(fields[i].getName());
                    buffer.append("=");
                    try {
                        Object value = fields[i].get(o);
                        if (value != null) {
                            if (value.getClass().isPrimitive() ||
                                    value.getClass() == java.lang.Long.class ||
                                    value.getClass() == java.lang.String.class ||
                                    value.getClass() == java.lang.Integer.class ||
                                    value.getClass() == java.lang.Boolean.class
                            ) {
                                buffer.append(value);
                            } else {
                                buffer.append(dump(value, callCount));
                            }
                        }
                    } catch (IllegalAccessException e) {
                        buffer.append(e.getMessage());
                    }
                    buffer.append("\n");
                }
                oClass = oClass.getSuperclass();
            }
            buffer.append(tabs.toString());
            buffer.append("}\n");
        }
        logger.info(buffer.toString());
        return buffer.toString();
    }
}
