### WikiSpeedrun ###

This project seeks to automate the trend of 'speedrunnning' wikipedia by starting on one page and clicking links until you manage to get to the desired destination page.


**Functionality:**

Wikiracer uses a binary max heap of 'ladders', where a ladder is a mapping of a wikipedia page to the number of links that page has in common with the desired result page, to decide which pages to traverse. It will recursively select the page with the most links in common with the end page, and find the best link on that page until it reaches the end result.

Instructions for running
WikiRacer.java will require two command line arguments - the last portion of the start and end pages respectively.
For example, if you wanted to go from the Wikipedia page for Strawberry (https://en.wikipedia.org/wiki/Strawberry) to the page for Applesauce (https://en.wikipedia.org/wiki/Apple_sauce),
your arguments would be Strawberry & Apple_sauce
These arguments are case sensitive to account for duplicate wiki page nonsense that exists behind the scenes.



**Environment**: This project was developed in Java 15.

### Features in development:
Handle HTTPS 429 error (very rare) by catching error and sleeping, then retrying.

A select few wikipedia pages are broken. Few and far between but need to be manually accounted for.

Making more user friendly by accounting for bad input instead of throwing errors.



### Potential Future Features:
Adding a GUI for ease of use

Reordering page links to visit based on what a human might associate with the final result

