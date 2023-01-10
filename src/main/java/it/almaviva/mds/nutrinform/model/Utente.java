package it.almaviva.mds.nutrinform.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Utente {
    private String nome;
    private String cognome;
    private String miUtente;
    private String codiceFiscale;
    private String codiceFiscaleDelegante;
    private String mail;
    private String indirizzo;
    private String username;
    private String ruolo;
    private String token;
    private String tipologia;
    private boolean isInterno;
}
