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

# Kort på 3 planer

Når løsningen med tiles anvendes er der tre planer der skal tages højde for:

- Model: Er 1000 enheder langt på den længste led. Ændrer aldrig størrelse.
- Tiles: Er dimensioneret efter et bitmap der ville kunne indeholde tiles for hele det aktuelle zoom-niveau. I praksis bliver sådant et bitmap aldrig oprettet, blot tiles som repræsenterer fragmenter af dette bitmap. Ændrer størrelse afhængigt af zoom-niveau.
- Skærm: Størrelsen er dimensionerne på det vindue som kortet vises i på skærmen. Ændrer størrelse når brugeren ændrer størrelsen på vinduet.

# Hvor kigger brugeren

For at holde styr på hvilket udsnit af kortet brugeren kigger på opdateres to uafhængige variable løbende:

- Center: En relativ vektor der definerer centrum for udsnittet. Værdierne for center løber fra 0 til 1 på både x- og y-aksen – altså er (0.5, 0.5) midten af kortet.
- Zoom: En relativ værdi der definerer hvor stor en andel af kortet der vises indenfor udsnittets længste side.
	- Når zoom er mellem 0 og 1 er der zoomet ind, og udsnittet rummer kun en andel af kortet.
	- Når zoom er 1 vises hele kortet.
	- Når zoom er mellem 1 er der zoomet ud, og kortet dækker kun en andel af skærmen.

Ved hjælp af disse to variable kan det beregnes hvilket udsnit af kortet brugeren har efterspurgt.

# Når zoom ændrer sig

Når zoom ændrer sig skal størrelsen af tiles opdateres. 

# Hvilke tiles skal vises

Når kortet skal tegnes skal det afgøres hvilke tiles der skal vises på skærmen.

Først beregnes det hvor udsnittet befinder sig på tiles. Dette gøres ved at tage en boks der har samme dimensioner som skærm, og forskyde den i forhold til tiles således at den nu er centeret omkring center. Denne boks kalder vi for sektion (figure 10, venstre).

Dernæst beregnes hvilke tiles som sektion overlapper (figure 10, højre). Det er disse tiles som skal vises på skærmen.

[FIGURE 10]

# Forudtegning og centraliseret lagring af tiles

De løsninger vi er inspireret gør to ting markant anderledes end vi gør:

- De begrænser antallet af zoom niveauer. Hvis man ikke gør dette er der uendeligt mange zoom-niveauer, og dermed uendeligt mange tiles der potentielt skal lagres.
- De har en vedligeholdt database over tiles. Ved at gøre dette undgår de at tegne nye tiles når et udsnit efterspørges – I stedet hentes tiles fra den centrale database.

Vi overvejede en kort overgang at implementere en lignende løsning, men besluttede os for at det ville være for stor en opgave. Dette ville være en oplagt mulighed for senere optimering af programmet, og kunne både implementeres med en lokal eller en ekstern database.

# Gruppering

Fordi vi ikke har en database af tiles, skal de tegnes løbende. Til at starte med tegnede vi tiles enkeltvis, hvilket viste sig at være meget ineffektivt. En stor andel af de vejstykker der hentes fra modellen, og efterfølgende skaleres og tegnes, er slet ikke er indenfor tilen. Dette problem kan reduceres hvis tiles så vidt muligt grupperes og tegnes i rektangulære blokke. Hermed bliver mængden af spild minimeret (figure 7).

[FIGURE 7]

Efter at gruppen af tiles er blevet tegnet samlet, klippes tiles ud og gemmes individuelt i datastrukturen.

## Største rektangel algoritme

Vi fik hermed brug for en algoritme der kunne gruppere de tiles der endnu ikke var tegnet i rektangulære grupperinger. Algoritmen finder det størst mulige rektangel, og tilføjer det til en liste over rektangler. Algoritmen gentager denne procedure indtil alle tiles er grupperet (figure 5).

[FIGURE 5]

Indledningsvist opbygger algoritmen en tabel hvor det markeres hvilke tiles der mangler at blive tegnet. Herefter opbygges endnu en tabel over sammenhængende søjler ved at traversere kolonnerne i tabellen nedefra og op, og beregne højden af den enkelte søjle dynamisk.

[FIGURE 8]

Herefter skannes rækkerne i tabellen fra venstre mod højre, og arealet af det størst mulige rektangel der indeholder denne celle af tabellen beregnes ved hjælp af data fra søjle tabellen. Undervejs i skanningen gemmes positionen af det største rektangel.

[FIGURE 9]

Afsluttende tilføjes det største rektangel til listen, og de tiles der er indenfor rektanglet markeres som tegnede.

Som følge af at vores tiles er 256 * 256 pixels store, må det formodes at algoritmen typisk køres med N der ~12*8 = 96 eller mindre, forudsat at programmet køres i en opløsning på (256*12) * (256*8) ~= 2880 * 1800 pixels (MacBook Pro 15" Retina skærm) eller mindre. Den valgte algoritme kører i lineær tid O(N), og blev valgt til fordel for en brute-force algoritme... --> MORE TO COME <--

# Tegning af tiles

## Skalering af punkter fra model til rektangel

