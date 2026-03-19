# Supplerende Krav

## 1. Pålitelighet
* **Dataintegritet:** Spillersaldo skal lagres permanent i PostgreSQL-databasen.
* **Transaksjoner:** Systemet skal sikre at saldoen oppdateres korrekt selv ved uforutsette avbrudd.

## 2. Ytelse
* **Responstid:** Systemet skal håndtere vinner-logikk og database-oppslag uten merkbar forsinkelse for brukeren.
* **Samtidighet:** Arkitekturen skal støtte flere samtidige spillere i samme sesjon på TomEE-serveren.

## 3. Sikkerhet
* **Tilgang:** Kun registrerte brukere med gyldig sesjon skal kunne utføre innsatser.
* **Databasesikkerhet:** Database-legitimasjon skal administreres via serverkonfigurasjon, ikke hardkoding.

## 4. Designbegrensninger
* **Plattform:** Systemet må kjøre på Java 21 og Apache TomEE.
* **Arkitektur:** Applikasjonen skal følge en lagdelt MVC-arkitektur.
