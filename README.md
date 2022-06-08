# cse455-final
I decided to make a chess AI for my final project. I know it isn't exactly computer vision, but it is related and we did talk about it in class.
Also it was something that I really wanted to do. This was a lot of fun. 

The problem I was doing was simple: build a competent chess AI. At first I just used minimax, but I decided to add in some memory and learning to the mix.
No components of this project were from preexisting work, I built it from scratch, including the chess game. 
After I made the chess game where 2 people could play each other, I made the AI for the enemy side. At first I used minimax, which
was actually quite easy to do, much easier than making a chess emulator. Without any pruning it didn't take much depth in the tree for 
each move to take a long time. Here are some average times I recorded (no pruning):
- depth <= 3, basically instant
- depth == 4, about 1 second, a little longer
- depth == 5, about 20-40 seconds, big range
- I Didn't try any deeper.

Alpha-beta did speed things up, but not as much as I expected
- depth == 4, about half a second
- depth == 5, anywhere from 5-25 seconds, which is a huge range. I guess it depends on how much you can cut off.

At this point I decided to make the AI have some memory of previous game state and if it won the game or not.
First I just had the AI log all of the moves that it made (a move is very simple, it is just the beginning and ending location, it doesn't
even take into account which piece did the movement) in a game, and whether or not it won the game. Then when playing a new game, if one of
these moves came up again, it would take into account that move's win probablilty when deciding whether or not to choose that move. I had all moves 
that hadn't been made start with a 1/1 chance, and a win would add it to 2/2, a loss to 1/2, etc.

I tried training this AI against itself, but a huge problem arised, which was that the AI would always pick the first max move it found, 
so the 2 AIs would always get to a point where they would just move the same piece back and forth forever, and the game would never end.
So then I tried to train the AI against a randomAI, and I gave the randomAI the same learning mechanism. The problem with this is that 
the random never got better because even with some memory, the minimax would win every game because it would look ahead.

At this point I did a massive overhaul of the learning mechanism. I had the AI log the entire chess board instead of just the move, since
the move hardly had any information. I also would weigh later boards in the game heavier than earlier boards, so that early moves wouldn't
be bogged down by losses that they were'nt at fault for. And to fix the never-ending game problem, I changed the way it would pick a maximum.
Instead of picking the first that passes the probability of victory test, I would check every single max. I would weight the probability of victory of
one max against another and then choose whichever came out on top after getting a random value. This tactic would now favor the last moves because 
they have much fewer probabilities to win: If the first move is picked, it has to beat every other move, if the last is picked, it only has to beat one.
So I additionally weighed each move based on its location in the array to account for this.

I then decided to train this against an AI with no memory (but with the infinite-move mitigation). But I accidentaly trained it against
my old AI based on moves, and to my surprise, the move AI significantly outperformed the new boardAI. I had done a little bit of training
on the new AI when I tested some games on it, and the old AI's memory had been wiped, so it was fresh. The new AI won the first game, but 
I guess that was all the old AI needed because it won like 20 straight games after that. It went about 30-2 against the new AI. 

I finally got back to testing the new AI against the plain minimax, and with all these games under its belt, it dominated, winning basically every single game.

This was very surprising to me, that the AI going off of memory with less information significantly out performed the AI with more information. 
Maybe it was overfitting, and the information was too specific to be as useful.

Also it is hard to say whether or not the AIs have improved relative to me, since I am not good at the game, they started out better than me
with just a 3-deep minimax.