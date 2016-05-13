
entityList([X|List]) :- entity(X), entityList(List).

entity(diomedes33).
entity(fluffy).
entity(nugget).
entity(gadget).

isAList(X,[Y|List]) :- isA(X,Y), isAList(Y,List).
isAList([X|List],Y) :- isA(X,Y), isAList(List, Y).

isA(fluffy,cat).
isA(nugget,cat).
isA(gadget,cat).
isA(cat, feline).
isA(cat, pet).
isA(cat, mammal).
isA(cat, predator).
isA(kitten, cat).
isA(josh, dog).

propertyList(X,[Y|List]) :- property(X,Y), propertyList(Y,List).
propertyList([X|List],Y) :- property(X,Y), propertyList(List,Y).

property(fluffy,white).
property(nugget,gold).
property(gadget,gray).
property(cat, social).
property(cat, territorial).

likesList(X,[Y|List]) :- likes(X,Y), likesList(Y,List).
likesList([X|List],Y) :- likes(X,Y), likesList(List,Y).

likes(fluffy,catnip).
likes(nugget,chocolate).
likes(gadget,nugget).
likes(cat, play).

huntsList(X,[Y|List]) :- hunts(X,Y), huntsList(Y,List).
huntsList([X|List],Y) :- hunts(X,Y), huntsList(List,Y).

hunts(cat, mouse).
hunts(cat, bird).

soundList(X,[Y|List]) :- sound(X,Y), soundList(Y,List).
soundList([X|List],Y) :- sound(X,Y),soundList(List,Y).

sound(cat, meow).

eatsList(X,[Y|List]) :- eats(X,Y), eatsList(Y,List).
eatsList([X|List],Y) :- eats(X,Y), eatsList(List,Y).

eats(cat, mouse).
eats(cat, bird).

drinksList(X,[Y|List]) :- drinks(X,Y), drinksList(Y,List).
drinksList([X|List],Y) :- drinks(X,Y), drinksList(List,Y).

drinks(cat, milk).

speciesList(X,[Y|List]) :- species(X,Y), speciesList(Y,List).
speciesList([X|List],Y) :- species(X,Y), speciesList(List,Y).

species(cat, fcatus).

genusList(X,[Y|List]) :- genus(X,Y), genusList(Y,List).
genusList([X|List],Y) :- genus(X,Y), genusList(List,Y).

genus(cat, felis).

familyList(X,[Y|List]) :- family(X,Y), familyList(Y,List).
familyList([X|List],Y) :- family(X,Y), familyList(List,Y).

family(cat, felidae).

orderList(X,[Y|List]) :- order(X,Y), orderList(Y,List).
orderList([X|List],Y) :- order(X,Y), orderList(List,Y).

order(cat, carnivora).

classList(X,[Y|List]) :- class(X,Y), classList(Y,List).
classList([X|List],Y) :- class(X,Y), classList(List,Y).

class(cat, mammalia).

phylumList(X,[Y|List]) :- phylum(X,Y), phylumList(Y,List).
phylumList([X|List],Y) :- phylum(X,Y), phylumList(List,Y).

phylum(cat, chordata).

kingdomList(X,[Y|List]) :- kingdom(X,Y), kingdomList(Y,List).
kingdomList([X|List],Y) :- kingdom(X,Y), kingdomList(List,Y).

kingdom(cat, animalia).
