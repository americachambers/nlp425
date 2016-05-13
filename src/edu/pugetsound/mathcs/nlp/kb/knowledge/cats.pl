
entityList([X|List]) :- entity(X), entityList(List).
entity(diomedes33).
entity(fluffy).
entity(nugget).
entity(gadget).

isAList(X,[Y|List]) :- isA(X,Y), isAList(Y,List).
isA(fluffy,cat).
isA(nugget,cat).
isA(gadget,cat).

property(fluffy,white).
property(nugget,gold).
property(gadget,gray).

likes(fluffy,catnip).
likes(nugget,chocolate).
likes(gadget,nugget).

isA(cat, feline).
isA(cat, pet).
isA(cat, mammal).
isA(cat, predator).
isA(kitten, cat).

likes(cat, play).

property(cat, social).
property(cat, territorial).

hunts(cat, mouse).
hunts(cat, bird).

sound(cat, meow).

eats(cat, mouse).
eats(cat, bird).

drinks(cat, milk).

species(cat, fcatus).
genus(cat, felis).
family(cat, felidae).
order(cat, carnivora).
class(cat, mammalia).
phylum(cat, chordata).
kingdom(cat, animalia).

isA(josh,dog).

isA(josh,dog).

isA(josh,dog).

isA(josh,dog).

isA(josh,dog).

isA(josh,dog).

isA(josh,dog).

isA(josh,dog).
