# Risikoliste

| ID | Risiko | Sannsynlighet (1-5) | Konsekvens (1-5) | Tiltak (Mitigation) |
| :--- | :--- | :---: | :---: | :--- |
| R1 | Feil i vinner-logikk | 3 | 5 | Omfattende JUnit-testing av alle hånd-kombinasjoner. |
| R2 | Server-krasj (TomEE) | 2 | 4 | Hyppig deployment-testing og feilhåndtering. |
| R3 | SQL-Injection i saldo | 2 | 5 | Bruke Prepared Statements og sikker lagdelt arkitektur. |
| R4 | Gruppemedlem blir syk | 3 | 3 | God dokumentasjon i Git slik at andre kan ta over oppgaver. |
