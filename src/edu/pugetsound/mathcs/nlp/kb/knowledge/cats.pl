
entityList0([X|List]) :- entity(X), entityList0(List).
entity(diomedes33).
entity(fluffy).
entity(nugget).
entity(gadget).

isAList1(X,[Y|List]) :- isA(X,Y), isAList1(Y,List).
isAList0([X|List],Y) :- isA(X,Y), isAList0(List, Y).

isA(fluffy,cat).
isA(nugget,cat).
isA(gadget,cat).
isA(cat, feline).
isA(cat, pet).
isA(cat, mammal).
isA(cat, predator).
isA(kitten, cat).
isA(josh, dog).

propertyList1(X,[Y|List]) :- property(X,Y), propertyList1(Y,List).
propertyList0([X|List],Y) :- property(X,Y), propertyList0(List,Y).

property(fluffy,white).
property(nugget,gold).
property(gadget,gray).
property(cat, social).
property(cat, territorial).

likesList1(X,[Y|List]) :- likes(X,Y), likesList1(Y,List).
likesList0([X|List],Y) :- likes(X,Y), likesList0(List,Y).

likes(fluffy,catnip).
likes(nugget,chocolate).
likes(gadget,nugget).
likes(cat, play).

huntsList1(X,[Y|List]) :- hunts(X,Y), huntsList1(Y,List).
huntsList0([X|List],Y) :- hunts(X,Y), huntsList0(List,Y).

hunts(cat, mouse).
hunts(cat, bird).

soundList1(X,[Y|List]) :- sound(X,Y), soundList1(Y,List).
soundList0([X|List],Y) :- sound(X,Y), soundList0(List,Y).

sound(cat, meow).

eatsList1(X,[Y|List]) :- eats(X,Y), eatsList1(Y,List).
eatsList0([X|List],Y) :- eats(X,Y), eatsList0(List,Y).

eats(cat, mouse).
eats(cat, bird).

drinksList1(X,[Y|List]) :- drinks(X,Y), drinksList1(Y,List).
drinksList0([X|List],Y) :- drinks(X,Y), drinksList0(List,Y).

drinks(cat, milk).

speciesList1(X,[Y|List]) :- species(X,Y), speciesList1(Y,List).
speciesList0([X|List],Y) :- species(X,Y), speciesList0(List,Y).

species(cat, fcatus).

genusList1(X,[Y|List]) :- genus(X,Y), genusList1(Y,List).
genusList0([X|List],Y) :- genus(X,Y), genusList0(List,Y).

genus(cat, felis).

familyList1(X,[Y|List]) :- family(X,Y), familyList1(Y,List).
familyList0([X|List],Y) :- family(X,Y), familyList0(List,Y).

family(cat, felidae).

orderList1(X,[Y|List]) :- order(X,Y), orderList1(Y,List).
orderList0([X|List],Y) :- order(X,Y), orderList0(List,Y).

order(cat, carnivora).

classList1(X,[Y|List]) :- class(X,Y), classList1(Y,List).
classList0([X|List],Y) :- class(X,Y), classList0(List,Y).

class(cat, mammalia).

phylumList1(X,[Y|List]) :- phylum(X,Y), phylumList1(Y,List).
phylumList0([X|List],Y) :- phylum(X,Y), phylumList0(List,Y).

phylum(cat, chordata).

kingdomList1(X,[Y|List]) :- kingdom(X,Y), kingdomList1(Y,List).
kingdomList0([X|List],Y) :- kingdom(X,Y), kingdomList0(List,Y).

kingdom(cat, animalia).
