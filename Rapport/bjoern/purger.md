# Purger

Vi udviklede 2 moduler til at filtrere og klargøre data – et for hvert datasæt. Fælles funktionalitet for de to moduler er:

- Frasortere unødvendige data.
- Ensrette formatet som data gemmes i.
- Konvertere koordinater således at de ligger inden for et 1000 gange 1000 koordinatsystem med udganspunkt i øverste venstre hjørne.
- Maks x og y værdier gemmes til brug ved indlæsning.
- Rette op på evt. fejl og mangler i datasæt.

## Krak

For nogle af vejstykkerne i datasættet var hastigheden 0, hvilket vi anser som en fejl i de data vi har modtaget. Dette viste sig at være problematisk, eftersom vores rute-algoritme bruger vejstykkernes hastigheder til at beregne den hurtigste vej. For at afhjælpe dette problem valgte vi at beregne hastigheden ud fra vejstykkets længde og estimerede køretid med følgende formel:

s = (l / 1000) / (t / 60 * (1 / 1.15))

Hvor s er hastigheden i km/t, l er længden i meter, og t er den estimerede køretid i minutter. Formlen tager højde for at krak har lagt 15% til deres estimerede køretider. Denne løsning er ikke perfekt, da det havde været optimalt at aflæse alle hastigheder frem for at udlede dem, men var et nødvendigt unde for at opnå den ønskede funktionalitet.

De kystlinje data vi fik fra en anden gruppe var angivet som en serie af forbindelser mellem koordinater: x1, y1, x2, y2. Disse data bliver i modulet splittet ud i en række punkter og vejstykker.

## OSM