-- Fase 4: corregir los alias de Forex Factory según el manual de reglas (Solid Trading Latam).
UPDATE indicador SET alias_ff = 'FOMC Statement' WHERE id = 'fomc_tasas';
UPDATE indicador SET alias_ff = 'Prelim GDP q/q' WHERE id = 'pib';
UPDATE indicador SET alias_ff = 'Prelim UoM Consumer Sentiment' WHERE id = 'umich';
