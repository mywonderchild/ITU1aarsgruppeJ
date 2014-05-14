# Evaluering af produkt

Programmet understøtter alle de påkrævede funktionaliteter, såvel som flere af de valgfrie. Vi er derfor grundlæggende tilfredse med programmets egenskab. Vi kunne godt have tænkt os at have været mere kreative, og fundet på nogle funktionaliteter selv, men vi havde ikke det overskud til det.

## Hastighed

Programmet er ~15 sekunder om at starte op med indlæsning af Krak data, og ~50 sekunder om at starte op med indlæsning af OSM data. Begge load tider er indenfor den ramme som gruppen finder acceptabel, og vi har derfor ikke brugt særligt mange ressourcer på at forbedre dem.

Der er blevet arbejdet meget med hastighed i forbindelse med navigation af kortet (pan og zoom), og vi mener grundlæggende at programmets responstider i er rigtig gode på dette punkt. Det samme gælder udregning og visning af rutevejledninger.

Udregning af rutevejledning kunne have været hurtigere hvis vi havde implementeret A-stjerne optimeringen, men da vi allerede havde en algoritme på plads der løste opgaven med rimelig hastighed, valgte vi at bruge vores tid på andre mere presserende opgaver.

Tegningen af tiles kunne med fordel være kørt på en separat tråd, således at brugergrænsefladen ikke låser imens denne process finder sted. Dette ville særligt have været en fordel i forbindelse med OSM datasættet, hvor tegningen af tiles kan tage så lang tid at vi formoder at det må være til irritation for brugeren.

Grundlæggende synes vi at programmet er lidt for sløvt i optrækket i forbindelse med brugerinteraktion når OSM datasættet er indlæst. Dette skyldes at mængden af data er meget større, og at beregningerne på disse data som konsekvens tager længere tid. Vi var først klar til at starte programmet med indlæsning af OSM data relativt sent i forløbet, og på dette tidspunkt var langt hovedparten af funktionaliteten allerede var implementeret. Vores muligheder for at teste og forbedre hastigheden af programmet ved kørsel med OSM data var derfor stærkt begrænsede.

## Fejl og mangler

Vores reducerede OSM datasæt indeholder ingen information om hvilken by et vejstykke er associeret med. Det er hermed umuligt at skelne mellem veje der har det samme navn i forskellige byer. Kun ~30% af vejene i vores OSM datasæt har information om hastighedsbegrænsinger fordi vi ikke har formået at udnytte datakilden optimalt. Som en konsekvens af dette er vores rute-beregning upræcis, da det for lang hovedparten af vejstykkerne ikke er de faktiske hastighedsbegrænsninger der anvendes, men derimod en statisk "default" værdi.

Som nævnt ovenfor er vores program ikke så hurtigt som vi ville ønske ved kørsel med OSM data, og dette må anses som en væsentlig mangel jævnfør kravene til produktet.

Vi anser det som en mangel at programmet ikke tager højde for forbudte højre og venstre sving, samt ensrettede og lukkede veje, selvom det ikke er et decideret krav. Uden denne funktionalitet er der ingen garanti for at de ruter som programmet beregner er mulige og lovlige at følge i praksis.

## Brugervenlighed

Vores program giver ikke brugeren nogen informationer om hvad mulighederne for interaktion er, og det er derfor op til brugeren selv at udforske programmets funktionaliteter. Særligt galt kunne det gå, hvis brugeren ikke er bekendt med digitale kort generelt, og derfor ikke har en forhåndsviden om typiske muligheder for interaktion i denne type program. 

Vi havde ambitioner om at lave brugertests af vores program, og forbedre brugervenligheden af vores program baseret på resultaterne af disse. Desværre måtte vi indse at vi ikke havde overblik og tid til at få planlagt og foretaget dem.