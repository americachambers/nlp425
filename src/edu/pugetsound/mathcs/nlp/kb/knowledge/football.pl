entityList([X|List]) :- entity(X), entityList(List).

entity(diomedes33).

isAList(X,[Y|List]) :- isA(X,Y), isAList(Y,List).
isAList([X|List],Y) :- isA(X,Y), isAList(List, Y).

isA(football, sport).

isA(afc, conference).
isA(nfc, conference).

isA(afcNorth, division).
isA(ravens, team).
isA(bengals, team).
isA(steelers, team).
isA(browns, team).

isA(division, afcEast).
isA(jets, team).
isA(bills, team).
isA(patriots, team).
isA(dolphins, team).

isA(division, afcSouth).
isA(titans, team).
isA(jaguars, team).
isA(texans, team).
isA(colts, team).

isA(afcWest, division).
isA(broncos, team).
isA(chiefs, team).
isA(chargers, team).
isA(raiders, team).

isA(nfcNorth, division).
isA(packers, team).
isA(lions, team).
isA(vikings, team).
isA(bears, team).

isA(nfcEast, division).
isA(cowboys, team).
isA(redskins, team).
isA(eagles, team).

isA(nfcSouth, division).
isA(falcons, team).
isA(panthers, team).
isA(buccaneers, team).
isA(saints, team).

isA(nfcWest, division).
isA(cardinals, team).
isA(niners, team).
isA(seahawks, team).
isA(rams, team).

isInList(X,[Y|List]) :- isIn(X,Y), isInList(Y,List).
isInList([X|List],Y) :- isIn(X,Y), isInList(List, Y).

isIn(afcNorth, afc).
isIn(ravens, afcNorth).
isIn(bengals, afcNorth).
isIn(steelers, afcNorth).
isIn(browns, afcNorth).

isIn(afcEast, afc).
isIn(jets, afcEast).
isIn(bills, afcEast).
isIn(patriots, afcEast).
isIn(dolphins, afcEast).

isIn(afcSouth, afc).
isIn(titans, afcSouth).
isIn(jaguars, afcSouth).
isIn(texans, afcSouth).
isIn(colts, afcSouth).

isIn(afcWest, afc).
isIn(broncos, afcWest).
isIn(chiefs, afcWest).
isIn(chargers, afcWest).
isIn(raiders, afcWest).

isIn(nfcNorth, nfc).
isIn(packers, nfcNorth).
isIn(lions, nfcNorth).
isIn(vikings, nfcNorth).
isIn(bears, nfcNorth).

isIn(nfcEast, nfc).
isIn(cowboys, nfcEast).
isIn(redskins, nfcEast).
isIn(eagles, nfcEast).
isIn(giants, nfcEast).

isIn(nfcSouth, nfc).
isIn(falcons, nfcSouth).
isIn(panthers, nfcSouth).
isIn(buccaneers, nfcSouth).
isIn(saints, nfcSouth).

isIn(nfcWest, nfc).
isIn(cardinals, nfcWest).
isIn(niners, nfcWest).
isIn(seahawks, nfcWest).
isIn(rams, nfcWest).

locationList(X,[Y|List]) :- location(X,Y), location(Y,List).
locationList([X|List],Y) :- location(X,Y), location(List, Y).

location(ravens, baltimore).
location(bengals, cincinnati).
location(steelers, pittsburgh).
location(browns, cleveland).

location(jets, newYork).
location(bills, buffalo).
location(patriots, newEngland).
location(dolphins, miami).

location(titans, tennessee).
location(jaguars, jacksonville).
location(texans, houston).
location(colts, indianapolis).

location(broncos, denver).
location(chiefs, kansasCity).
location(chargers, sanDiego).
location(raiders, oakland).

location(packers, greenBay).
location(lions, detroit).
location(vikings, minnesota).
location(bears, chicago).

location(cowboys, dallas).
location(redskins, washington).
location(eagles, philidelphia).
location(giants, newYork).

location(falcons, atlanta).
location(panthers, carolina).
location(buccaneers, tampaBay).
location(saints, newOrleans).

location(cardinals, arizona).
location(niners, sanFrancisco).
location(seahawks, seattle).
location(rams, losAngeles).

likesList(X,[Y|List]) :- likes(X,Y), likesList(Y,List).
likesList([X|List],Y) :- likes(X,Y), likesList(List,Y).

likes(diomedes33, football).
likes(diomedes33, niners).

dislikesList(X,[Y|List]) :- dislikes(X,Y), dislikesList(Y,List).
dislikesList([X|List],Y) :- dislikes(X,Y), dislikesList(List,Y).

dislikes(diomedes33, seahawks).
dislikes(diomedes33, cowboys).
dislikes(diomedes33, raiders).

playsList(X,[Y|List]) :- plays(X,Y), playsList(Y,List).
playsList([X|List],Y) :- plays(X,Y), playsList(List, Y).

plays(team, sport).

plays(tomBrady, football).
plays(peytonManning, football).
plays(russellWilson, football).
plays(aaronRodgers, football).
plays(dezBryant, football).
plays(odellBeckhamJr, football).
plays(camNewton, football).
plays(robGronkowski, football).
plays(jjWatt, football).
plays(jasonWitten, football).
plays(lukeKuechly, football).
plays(tonyRomo, football).
plays(colinKaepernick, football).
plays(richardSherman, football).
plays(marshawnLynch, football).
plays(marcusMariota, football).
plays(antonioBrown, football).
plays(clayMatthews, football).
plays(andrewLuck, football).
plays(drewBrees, football).
plays(demarcoMurray, football).
plays(jimmyGraham, football).
plays(julianEdelman, football).
plays(vonMiller, football).
plays(teddyBridgewater, football).
plays(larryFitzgerald, football).
plays(jordyNelson, football).
plays(calvinJohnson, football).
plays(derekCarr, football).
plays(jameisWinston, football).
plays(amariCooper, football).
plays(benRoethlisberger, football).
plays(leveonBell, football).
plays(ryanTannehill, football).
plays(eliManning, football).
plays(aJGreen, football).
plays(demaryiusThomas, football).
plays(mattForte, football).
plays(eddieLacy, football).
plays(kamChancellor, football).
plays(joeHaden, football).
plays(khalilMack, football).
plays(jarrydHayne, football).
plays(jamaalCharles, football).
plays(darrelleRevis, football).
plays(philipRivers, football).
plays(sammyWatkins, football).
plays(gregOlsen, football).
plays(leSeanMcCoy, football).
plays(earlThomas, football).
plays(naVarroBowman, football).
plays(carlosHyde, football).

playsForList(X,[Y|List]) :- playsFor(X,Y), playsForList(Y,List).
playsForList([X|List],Y) :- playsFor(X,Y), playsForList(List, Y).

playsFor(tomBrady, patriots).
playsFor(peytonManning, broncos).
playsFor(russellWilson, seahawks).
playsFor(aaronRodgers, packers).
playsFor(dezBryant, cowboys).
playsFor(odellBeckhamJr, giants).
playsFor(camNewton, panthers).
playsFor(robGronkowski, patriots).
playsFor(jjWatt, texans).
playsFor(jasonWitten, cowboys).
playsFor(lukeKuechly, panthers).
playsFor(tonyRomo, cowboys).
playsFor(colinKaepernick, niners).
playsFor(richardSherman, seahawks).
playsFor(marshawnLynch, seahawks).
playsFor(marcusMariota, titans).
playsFor(antonioBrown, steelers).
playsFor(clayMatthews, packers).
playsFor(andrewLuck, colts).
playsFor(drewBrees, saints).
playsFor(demarcoMurray, eagles).
playsFor(jimmyGraham, seahawks).
playsFor(julianEdelman, patriots).
playsFor(vonMiller, broncos).
playsFor(teddyBridgewater, vikings).
playsFor(larryFitzgerald, cardinals).
playsFor(jordyNelson, packers).
playsFor(calvinJohnson, lions).
playsFor(derekCarr, raiders).
playsFor(jameisWinston, buccaneers).
playsFor(amariCooper, raiders).
playsFor(benRoethlisberger, steelers).
playsFor(leveonBell, steelers).
playsFor(ryanTannehill, dolphins).
playsFor(eliManning, giants).
playsFor(aJGreen, bengals).
playsFor(demaryiusThomas, broncos).
playsFor(mattForte, bears).
playsFor(eddieLacy, packers).
playsFor(kamChancellor, seahawks).
playsFor(joeHaden, browns).
playsFor(khalilMack, raiders).
playsFor(jarrydHayne, niners).
playsFor(jamaalCharles, chiefs).
playsFor(darrelleRevis, jets).
playsFor(philipRivers, chargers).
playsFor(sammyWatkins, bills).
playsFor(gregOlsen, panthers).
playsFor(leSeanMcCoy, bills).
playsFor(earlThomas, seahawks).
playsFor(naVarroBowman, niners).
playsFor(carlosHyde, niners).
