package it.almaviva.mds.nutrinform.service;

import it.almaviva.mds.nutrinform.model.Utente;

import java.util.Collection;
import java.util.Properties;

public class UtenteServiceImpl implements UtenteService {
    private SecurityContext securityContext;

    public UtenteServiceImpl(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public Utente loginNSIS(String coin, boolean isTokenSamlEnabled) {
        boolean isLoggedIn = initSecurityContext(coin, isTokenSamlEnabled);
        if (isLoggedIn) {
            return getUtenteFromContext(securityContext);
        } else {
            return null;
        }
    }

    public boolean initSecurityContext(String coin, boolean isTokenSamlEnabled) {
        boolean isLoggedIn;
        Properties properties = new Properties();
        properties.setProperty("DEBUG", "NO");
        properties.setProperty("java.naming.provider.url", "http://wrappercoll.minsanita.it/WRAPPERSAA_V2/");
        properties.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        securityContext = new SecurityContext(properties);
        if (isTokenSamlEnabled) {
            isLoggedIn = securityContext.loginSAML_1_0(coin, "Ministero della Salute", "PORTALEPAGAMENTI");
        } else {
            isLoggedIn = securityContext.loginDn( coin, "Ministero della Salute", "PORTALEPAGAMENTI");
        }
        if (isLoggedIn) {
            securityContext.getApplications().add("PORTALEPAGAMENTI");
        }
        return isLoggedIn;
    }

    @Override
    public Utente loginImpresaGov(String coin) {
        return null;
    }

    private Utente getUtenteFromContext(SecurityContext securityContext) {
        Utente utenteContext = new Utente();
        utenteContext.setNome(securityContext.getUserParameter("NAME"));
        /* ETC ETC VEDERE FOGLIO*/
        /* ETC ETC VEDERE FOGLIO*/
        /* ETC ETC VEDERE FOGLIO*/
        return utenteContext;
    }

    public static class SecurityContext {
        private final Properties properties;
        private String getUserParameter(String parameter) {
            return properties.getProperty(parameter);
        }
        public SecurityContext(Properties properties) {
            this.properties = properties;
        }

        public boolean loginSAML_1_0(String coin, String ministero_della_salute, String portalepagamenti) {
            return false;
        }

        public boolean loginDn(String coin, String ministero_della_salute, String portalepagamenti) {
            return false;
        }

        public Collection<String> getApplications() {
            return null;
        }
    }
}
