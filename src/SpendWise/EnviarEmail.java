package SpendWise;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

public class EnviarEmail {
    public static void main(String[] args) {
        String meuEmail = "spendwisetcp@mail.com";
        String minhaSenha = "spendwise123";

        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(meuEmail, minhaSenha));
        email.setSSLOnConnect(true);

        try{
            email.setFrom(meuEmail);
            email.setSubject("jp?");
            email.setMsg("salve corso");
            email.addTo("jp.corso123@gmail.com");
            email.send();
            System.out.println("envieeeeeeeeei!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}