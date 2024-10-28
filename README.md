# gRPCForAuthentication

## Descrizione del Progetto
Il progetto **gRPCForAuthentication** implementa un sistema di autenticazione distribuito utilizzando gRPC. Il server di autenticazione gestisce le richieste di autenticazione da parte dei client, genera token JWT e verifica la validità di questi token per garantire un accesso sicuro agli utenti.

## Funzionalità Principali
- **Autenticazione con gRPC**: Utilizza gRPC per la comunicazione tra client e server di autenticazione.
- **Generazione di Token JWT**: Implementazione della logica per generare token JWT per sessioni sicure.
- **Client Multipli**: Sono inclusi client differenti (`AuthClient`, `AnotherAuthClient`) per mostrare la capacità del server di autenticare più applicazioni.
- **DAO e Persistenza**: Utilizza un DAO (`UserDAO`) per gestire l'interazione con il database degli utenti.

## Struttura del Progetto
- **`src/main/java`**: Contiene il codice sorgente dell'applicazione.
  - **Server e Servizio**:
    - **`AuthServer.java`**: Classe per avviare il server gRPC.
    - **`AuthServiceImpl.java`**: Implementazione del servizio gRPC che gestisce l'autenticazione e la generazione dei token JWT.
  - **Client**: Classi per interagire con il server gRPC (`AuthClient.java`, `AnotherAuthClient.java`).
  - **Domain e DAO**:
    - **`User.java`**: Classe di dominio che rappresenta un utente.
    - **`UserDAO.java`**: Classe per gestire l'interazione con il database degli utenti.
  - **Utility**:
    - **`JwtUtil.java`**: Classe per generare e validare i token JWT.
    - **`HibernateUtil.java`**: Gestione della configurazione di Hibernate per la persistenza.

## Tecnologie Utilizzate
- **Java 17**: Linguaggio di programmazione principale.
- **gRPC**: Framework di comunicazione per creare servizi di autenticazione distribuiti.
- **JWT (JSON Web Token)**: Per l'autenticazione e la gestione delle sessioni.
- **Hibernate**: Utilizzato per la gestione della persistenza degli utenti nel database.
- **Maven**: Strumento di build per gestire le dipendenze.

## Configurazione
1. **Prerequisiti**:
   - Java 17
   - Maven
   - Database configurato per memorizzare gli utenti
2. **Clonare il Repository**:
   ```sh
   git clone <URL-del-repository>
   ```
3. **Configurare il Database**:
   - Aggiornare il file `application.properties` o le configurazioni Hibernate con le credenziali del database.
4. **Compilare e Avviare il Server**:
   ```sh
   mvn clean install
   java -jar target/gRPCForAuthentication.jar
   ```

## Utilizzo
- **Avvio del Server**: Eseguire `AuthServer` per avviare il servizio di autenticazione.
- **Client di Autenticazione**: Utilizzare `AuthClient` o `AnotherAuthClient` per testare l'autenticazione con il server.

## Funzionalità Aggiunta di Recente
- **Integrazione Keycloak**: Introduzione della gestione dei token Keycloak conforme a Oauth 2.0.

## Configurazione Keycloak
1. Configurare un server Keycloak.
2. Aggiornare le configurazioni per riflettere le impostazioni di Keycloak.

## Utilizzo Keycloak
- **Avviare il server di autenticazione**: Eseguire `AuthServer` per avviare il servizio di autenticazione con gestione token Keycloak.


