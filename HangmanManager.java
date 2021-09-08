
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class HangmanManager {
    
    Set<Character> guesses = new TreeSet<Character>();
    Set<String> wordsLeft = new TreeSet<String>();
    String pattern = "";
    int numGuesses = 0;
    
    public HangmanManager(List<String> dictionary, int length, int max)
    {
        if(length<1 || max<1)
            throw new IllegalArgumentException();
        
        numGuesses = max;
        for(int i = 0; i<length; i++)
            pattern += "-";
        for(String words : dictionary)
            if(words.length()==length)
                wordsLeft.add(words);
    }
    
    public Set<String> words()
    {
        return wordsLeft;
    }
    
    public int guessesLeft()
    {
        return numGuesses;
    }
    
    public Set<Character> guesses(){
        return guesses;
    }
    
    public String pattern(){
        if(wordsLeft.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        return pattern;
    }
    
    public int record(char guess){
        
        Map<String, String> families = new TreeMap<String, String>();
        Map<String,Set<String>> familyOccurence = new TreeMap<String,Set<String>>();
        int occurences = 0;

        if(guessesLeft()<1 || wordsLeft.isEmpty())
        {
            throw new IllegalStateException();
        }
        guesses.add(guess);
        
        for(String s : wordsLeft)   //goes thru all words left and saves them along with their patterns into a map
        {
            String newPattern = pattern;
            for(int i = 0; i<s.length(); i++)
            {
                if(s.charAt(i)==guess)
                {
                    newPattern = newPattern.substring(0,i) + guess + newPattern.substring(i+1,newPattern.length());
                    
                }
            }
            families.put(s,newPattern);
        }

        for(String keys : families.keySet()){       //lists all possible patterns with the strings that go with those patterns in a map
            String pat = families.get(keys);
            if(!familyOccurence.containsKey(pat)){
                Set<String> newSet = new TreeSet<String>();
                newSet.add(keys);
                familyOccurence.put(pat, newSet);
            }
            else{
                Set<String> newSet = familyOccurence.get(pat);
                newSet.add(keys);
                familyOccurence.put(pat,newSet);
            }
        }
        int largestPat = 0;     //finds the pattern with the most possibilities
        String bestPat = "";
        for(String pats : familyOccurence.keySet()){
            int count = familyOccurence.get(pats).size();
            if(count>largestPat){
                largestPat = count;
                bestPat = pats;
            }
        }
        for(int x = 0; x<bestPat.length()-1; x++){      //sees how many occurences of the guess are in the pattern
            if(bestPat.charAt(x)==guess)
                occurences += 1;
        }
        if(occurences==0)
           numGuesses -= 1;
        pattern = bestPat;
        wordsLeft = familyOccurence.get(pattern);
        return occurences;
    }
}
