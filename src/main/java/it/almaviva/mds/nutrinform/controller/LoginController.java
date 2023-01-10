package it.almaviva.mds.nutrinform.controller;

import it.almaviva.mds.nutrinform.model.Utente;
import it.almaviva.mds.nutrinform.service.UtenteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class LoginController {

    protected UtenteServiceImpl utenteService;

    protected Utente utente;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView login(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = null;
        try {
            // recupero il parametro SAMLResponse se l'utente ha effettuato l'accesso da impresainungiorno.gov.it
            String coin = request.getParameter("SAMLResponse");
            if (!ObjectUtils.isEmpty(coin)) {
                log.info("Richiesta login da impresainungiorno.gov.it - [" + coin + "]");
                //authentication = new UsernamePasswordAuthenticationToken(utenteService.loginImpresaGov(coin).getBody(), null, null);
            } else {
                // recupero il parametro tokenSAML se l'accesso a NSIS deve essere fatto per tokenSAML
                coin = request.getParameter("tokenSAML");
                if (ObjectUtils.isEmpty(coin)) {
                    // se non esiste recupero il parametro USER se l'accesso a NSIS deve essere fatto per
                    // miUtente
                    coin = request.getParameter("USER");
                    // se non esiste, prendo l'user di test dalla configurazione (utilizzare solamente per scopi
                    // di test)
                    if (ObjectUtils.isEmpty(coin) && !ObjectUtils.isEmpty("")) {
                        log.warn(
                                "No Security Coin found from the request but a test user was set! Maybe are you running from a local/development environment ?");
                        log.warn(
                                "******* RETURNING '{}' AS A TEST SECURITY COIN *******",
                                "");
                        coin = "";
                    }
                    // se non esiste il coin, restituisco errore
                    if (ObjectUtils.isEmpty(coin)) {
                        return new RedirectView("/", true);
                    }
                    log.info("ENGIWEB Pre-Authentication based on UserID");
                    authentication = new UsernamePasswordAuthenticationToken(utenteService.loginNSIS(coin, false), null, null);
                    /* OPPURE BASTA COSI */
                    utente = utenteService.loginNSIS(coin, false);
                    /* VEDREMO MA MI SEMBRA STRANO */
                } else {
                    log.info("ENGIWEB Pre-Authentication based on TokenSAML");
                    authentication = new UsernamePasswordAuthenticationToken(utenteService.loginNSIS(coin, true), null, null);
                    /* OPPURE BASTA COSI */
                    utente = utenteService.loginNSIS(coin, false);
                    /* VEDREMO MA MI SEMBRA STRANO */
                }
            }
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // ORA PERO COME MANDO ALLA VISTA, IN CHIARO??? OK
            // SE NO USO CMQ QUESTI SOPRA IN QUALCHE MODO
            return new RedirectView("/", true);
        } catch (Exception e) {
            log.error("Error login - ", e);
            return new RedirectView("/", true);
        }
    }

}
