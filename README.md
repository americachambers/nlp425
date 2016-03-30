# nlp425

Currently, the conversational agent can only handle the following types of sentences:

1. (Det) N V (Det) N<br>
"Fluffy loves fish"<br>
"The cat loves fish"<br>
"The cat loves the fish"<br>
"I love you"<br>
"I love the cat"<br>
"The cat loves me"<br>

2. (Det) N Copula (Det) N<br>
"Fluffy is a cat"<br>
"The cat is a lion"<br>

3. (Det) N Copula Adj/Adv<br>
"Fluffy is black"<br>
"Fluffy is fast"<br>

4. Copula N N?<br>
"Is Fluffy a cat?"<br>

5. Do/Did/Does [Sentence]?<br>
"Did she love fish?"<br>
"Did Fluffy eat the fish?"<br>
"Does Fluffy enjoy fish?"<br>

DISCLAIMER: The parser struggles with "Fluffy" often marking it as an adjective and the verb as a noun.<br>
