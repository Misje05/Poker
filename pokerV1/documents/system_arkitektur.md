# Systemarkitektur

Dette dokumentet beskriver den tekniske oppbyggingen av Texas Hold'em-systemet.

## 1. Oversikt over arkitekturen
* **3-lags modell:** Systemet er delt i et presentasjonslag, et logikk-lag og et datalag for å sikre stabilitet.
* **MVC-mønster:** Vi benytter Model-View-Controller for å skille brukergrensesnittet fra den underliggende spill-logikken.

## 2. Arkitektoniske elementer
* **Domenemodell:** Kjerne-klasser som `Card`, `Player` og `Hand` utgjør fundamentet i applikasjonen.
* **Spillmotor:** `Evaluator`-klassen håndterer kompleks vinner-logikk i hver spillrunde.
* **Datatilgang:** Vi benytter DAO-mønsteret (Data Access Object) for å isolere all kommunikasjon med PostgreSQL-databasen.

## 3. Infrastruktur og kjøremiljø
* **Web-tjener:** Applikasjonen deployeres på Apache TomEE.
* **Database:** PostgreSQL benyttes for permanent lagring av spillersaldo og brukerprofiler.
* **Versjonskontroll:** Bonobo Git Server brukes for sentral lagring av kildekode og dokumentasjon.

## 4. Verifisering av arkitektur
* **Arkitekturprototype:** En kjørbar versjon er installert på TomEE for å demonstrere at kritiske brukstilfeller fungerer.
* **Risikohåndtering:** Kjøring av prototypen har bekreftet at tekniske risikoer knyttet til database-tilkobling og server-miljø er løst.