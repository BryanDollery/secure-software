This project is a model for the governance and control process in an organisation.
It is not intended to be reusable, or even exemplary java code. It is meant to allow
me to think about how an organisation works, how approals work, and how software gets 
released into production within such an environment.

I've used this model to investigate, to study, to think, and to learn, but it also
has some explanatory power, so I've used it for communication too. It is here on github for me to use, but I'm not making it private. It's possible that
someone else may find this useful, so I've made it public.

The basic architecture is event driven, and the high-level packages are an attempt at a 
bounded context for each concpet in the model.

The model itself is rather complicated, so its probably better to read the code than 
a text description. I've keep some notes in comments to help you through, and as usual
the best place to start is in the test folder.

Basically, to release software into an environment we need permission. This model
represents the minimal structure we need to represent that. 