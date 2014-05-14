# Gentegning af rendrerede segmenter

Når brugeren navigerer kortet og bliver på samme zoom-niveau vil der være dele af kortet som allerede er tegnet, der skal tegnes til skærmen igen. Der er et overlap mellem det gamle og det nye udsnit af kortet (figure 1).

[FIGURE 1]

Til at starte med tegnede vi det hele igen, inklusiv overlap, men det viste sig ikke at være hurtigt nok, og responstiden blev irriterende lang. Det blev derfor besluttet at vi havde brug for en mere effektiv løsning med de følgende egenskaber:

- Tegning af de udsnit af kortet der ikke er blevet tegnet
- Genbrug af de udsnit af kortet der allerede er blevet tegnet.

# Første løsning

Den første løsning der blev overvejet var at gemme et billede af det gamle udsnit og forskyde det således at det lå rigtigt i forhold til det nye udsnit (figure 2). Efterfølgende tegnes den del af udsnittet som ikke er inkluderet i overlappet.

[FIGURE 2]

Denne løsning var meget simpel, og vi formoder den havde været nem at implementere. Den havde dog en række ulemper:

- Hvis brugeren navigerer tilbage til et udsnit de allerede har set skal dette udsnit tegnes igen.
- Der skal gemmes billeddata til en buffer hver gang udsnittet ændrer sig, således at denne data kan gentegnes som en del af det næste udsnit.
- De udsnit der skal tegnes er tynde, hvilket skaber stor ineffektivitet som følge af den måde som vejstykker hentes fra quadtræer og tegnes (figure 3).

[FIGURE 3]

# Anden løsning

Den anden løsning vi overvejede, og den vi endte med at implementere, var inspireret af online kort-programmer som for eksempel Google Maps og OSM. Det centrale koncept i denne løsning er at dele kortet op i en masse små udsnit (tiles). Størrelsen på udsnit forbliver det samme uanset zoom-niveau. Når der er zoomet langt ud udgøres kortet af få tiles, og når der er zoomet langt ind udgøres kortet af mange tiles (figure 4). Når der skal tegnes et udsnit tegnes de tiles der er indenfor udsnittet.

[FIGURE 4]

Denne løsning løser helt eller delvist problemerne ved den ovenstående løsning:

- Da de tegnede data er strukturerede, og det er dermed muligt at gemme dem og vise dem igen når det er relevant.
- Der skal kun gemmes billeddata når der bliver tegnet nye tiles.
- De udsnit der skal tegnes er firkantede og har en vis størrelse, hvilket gør problemet med overflødig data mindre.

De løsninger vi er inspireret gør to ting markant anderledes end vi gør:

- De begrænser antallet af zoom niveauer. Hvis man ikke gør dette er der uendeligt mange zoom-niveauer, og dermed uendeligt mange tiles der potentielt skal lagres.
- De har en vedligeholdt database over tiles, således at der ikke skal tegnes nye tiles når et udsnit efterspørges, men blot hentes tiles fra databasen.

# Rektangel-algoritme

Fordi vi ikke har en database af tiles, skal de tegnes løbende.








