# Utviklingsplan

## 1. Metodikk
Vi benytter en smidig prosess inspirert av Scrum.
* **Iterasjoner:** 2-ukers sprinter med fokus på leverbare delmål.
* **Møter:** Ukentlig synkronisering for status og planlegging.

## 2. Teststrategi og Evaluering
Metodene for testing er bevist og beskrevet for å sikre arkitektonisk stabilitet:
* **Enhetstesting:** JUnit 5 for kjerne-logikk i `Evaluator` og `Deck`.
* **Integrasjonstesting:** Verifisering av DAO-mønsteret mot PostgreSQL.
* **Systemtesting:** Testing av fullstendige brukstilfeller i TomEE-miljøet.
* **Akseptansetesting:** Verifisering mot kundens krav før Transition-fasen.

## 3. Ressursbruk
Faktiske ressursutgifter versus planlagte utgifter er definert til å være acceptable (OK) for alle faser.