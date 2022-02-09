// Alimurtada Al-Shimari
// This program keeps track of the state of a game of hangman, but it cheats

import java.util.*;

public class HangmanManager {
   private int guessesLeft;
   //nuber of guesses the player has left
   private Set<String> words;
   //words that the program has that could be used as the answer
   private Set<Character> guessedLetters;
   //letters that have been guessed by the player
   private String guessPattern;
   //shows the positioning of letters guessed correct

   //pre: if the given word length is is less than 1 or of the number of guesses is less
   //    than 0 then an IllegalArgumentException is thrown
   //post:  initializes the state of the game.
   //    the set of words initially contains all words from the dictionary
   //    of the given length, and elimantes any duplicates.
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (max < 0 || length < 1) {
         throw new IllegalArgumentException();
      }
      guessesLeft = max;
      words = new TreeSet<String>();
      guessedLetters = new TreeSet<Character>();
      for (String word : dictionary) {
         if (word.length() == length) {
            words.add(word);
         }
      }
      guessPattern = "";
      for (int i = 0; i < length; i++) {
         guessPattern += "- ";
      }
   }

   //post: this method gives access to the current set of words being considered
   //      by the hangman manager.
   public Set<String> words() {
      return words;
   }

   //post: this method returns how many guesses the player has left
   public int guessesLeft() {
      return guessesLeft;
   }

   //post: this method reveals the current set of letters that have been guessed by the user.
   public Set<Character> guesses() {
      return guessedLetters;
   }

   //pre: if the  set of words is empty, a IllegalStateException error is thrown
   //post: returns the current pattern to be displayed for the hangman
   //      game taking into account guesses that have been made. Letters that
   //      have not yet been guessed are displayed as a dash with spaces separating the letters
   public String pattern() {
      if (words.isEmpty()) {
         throw new IllegalStateException();
      }
      return guessPattern;
   }

   //pre: This method throws an IllegalStateException if the number of guesses left is not at
   //     least 1 or if the set of words is empty. It throws an
   //     IllegalArgumentException if the previous exception was not
   //     thrown and the character being guessed was guessed previously.
   //post: This is the method that does most of the work by recording the next
   //      guess made by the user. Using this guess, it decides what set of
   //      words to use going forward. It returns the number of
   //      occurrences of the guessed letter in the new pattern and it
   //      appropriately updates the number of guesses left
   public int record(char guess) {
      if (words.isEmpty() || guessesLeft < 1) {
         throw new IllegalStateException();
      } else if (!words.isEmpty() && guessedLetters.contains(guess)) {
         throw new IllegalArgumentException();
      }
      guessedLetters.add(guess);
      Map<String, Set<String>> wordFamilies = new TreeMap<>();
      for (String word : words) {
         String newPattern = patternFinder(word, guess);
         Set<String> wordSet = new TreeSet<String>();
         if (!wordFamilies.containsKey(newPattern)) {
            wordFamilies.put(newPattern, wordSet);
         }
         wordFamilies.get(newPattern).add(word);
      }
      int max = 0;
      for (String key : wordFamilies.keySet()) {
         Set<String> chosenFamily = wordFamilies.get(key);
         if (chosenFamily.size() > max) {
            max = chosenFamily.size();
            guessPattern = key;
            words = chosenFamily;
         }
      }
      return wordFinder(guess);
   }

   //pre: recieves a character that was guessed
   //post: returns the number of times that character passed in apears in the pattern. If it is 0,
   //      the number of guesses decreases by one
   private int wordFinder (char guess) {
      int similarities = 0;
      for (int i = 0; i < guessPattern.length(); i++) {
         if (guessPattern.charAt(i) == guess) {
            similarities++;
         }
      }
      if (similarities == 0) {
         guessesLeft--;
      }
      return similarities;
   }

   //pre: needs a word and a chracater that the player has guessed
   //post: returns the pattern updated with the character instead of
   //      dashes where appropriate
   private String patternFinder(String word, char guess) {
      String result = "";
      int index = 0;
      for (int i = 0; i < word.length(); i++) {
         if (word.charAt(i) != guess) {
            result += guessPattern.substring(index, index + 2);
         } else {
            result += guess + " ";
         }
         index += 2;
      }
      return result;
   }
}