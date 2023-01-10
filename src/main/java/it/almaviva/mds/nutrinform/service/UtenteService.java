package it.almaviva.mds.nutrinform.service;

import it.almaviva.mds.nutrinform.model.Utente;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

public interface UtenteService {
    Utente loginNSIS(String coin, boolean isTokenSamlEnabled);
    public boolean initSecurityContext(String coin, boolean isTokenSamlEnabled);
    Utente loginImpresaGov(String coin);
}
