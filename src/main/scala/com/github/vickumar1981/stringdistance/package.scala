package com.github.vickumar1981

package object stringdistance {
  import implicits._

  /**
    * A marker interface for the string metric algorithm.
    */
  trait StringMetricAlgorithm

  /**
    * A marker interface for the cosine similarity algorithm.
    */
  trait CosineAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the damerau levenshtein distance algorithm.
    */
  trait DamerauLevenshteinAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the dice coefficient algorithm.
    */
  trait DiceCoefficientAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the hamming distance algorithm.
    */
  trait HammingAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for a jaccard similarity algorithm.
    */
  trait JaccardAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the jaro similarity algorithm.
    */
  trait JaroAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the jaro winkler algorithm.
    */
  trait JaroWinklerAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the levenshtein distance algorithm.
    */
  trait LevenshteinAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the longest common subsequence algorithm.
    */
  trait LongestCommonSeqAlorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the metaphone algorithm.
    */
  trait MetaphoneAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the n-gram similarity algorithm.
    */
  trait NGramAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the overlap similarity algorithm.
    */
  trait OverlapAlgorithm extends StringMetricAlgorithm

  /**
    * A marker interface for the soundex similarity algorithm.
    */
  trait SoundexAlgorithm extends StringMetricAlgorithm

  /**
    * The Strategy object has two strategies(reg ex) expressions on which to split input.
    * [[Strategy.splitWord]] splits a word into a sequence of characters.
    * [[Strategy.splitSentence]] splits a sentence into a sequence of words.
    */
  object Strategy {
    final lazy val splitWord = "(?!^)"
    final lazy val splitSentence = "\\W+"
  }

  /**
    * A type class to extend a distance method to [[StringMetricAlgorithm]].
    */
  trait DistanceAlgorithm[+T <: StringMetricAlgorithm] {
    /**
      * The distance method takes two strings and returns a distance between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the distance between Strings s1 and s2.
      */
    def distance(s1: String, s2: String): Int
  }

  /**
    * A type class to extend a distance method with a 2nd typed parameter
    * to [[StringMetricAlgorithm]].
    */
  trait WeightedDistanceAlgorithm[+A <: StringMetricAlgorithm, B] {
    /**
      * The score method takes two strings and returns a distance between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the fuzzy score between Strings s1 and s2.
      */
    def distance(s1: String, s2: String, weight: B): Int
  }

  /**
    * A type class to extend a score method to [[StringMetricAlgorithm]].
    */
  trait ScoringAlgorithm[+T <: StringMetricAlgorithm] {
    /**
      * The score method takes two strings and returns a fuzzy score (0-1) between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the fuzzy score between Strings s1 and s2.
      */
    def score(s1: String, s2: String): Double
  }

  /**
    * A type class to extend a score method with a 2nd typed parameter
    * to [[StringMetricAlgorithm]].
    */
  trait WeightedScoringAlgorithm[+A <: StringMetricAlgorithm, B] {
    /**
      * The score method takes two strings and returns a fuzzy score (0-1) between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the fuzzy score between Strings s1 and s2.
      */
    def score(s1: String, s2: String, weight: B): Double
  }

  /**
    * A type class to extend a sound score method to [[StringMetricAlgorithm]].
    */
  trait SoundScoringAlgorithm[+T <: StringMetricAlgorithm] {
    /**
      * The score method takes two strings and returns true or false,
      * if string s1 sounds like string s2.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns whether String s1 sounds like s2.
      */
    def score(s1: String, s2: String): Boolean
  }

  /**
    * A mix-in trait to extend a score method using the distance method
    * to [[StringMetricAlgorithm]].
    */
  trait ScorableFromDistance[+T <: StringMetricAlgorithm] extends ScoringAlgorithm[T] { self: DistanceAlgorithm[T] =>
    /**
      * The score method takes two strings and returns a fuzzy score (0-1) between them.
      * This mix-in implements the fuzzy score based on the distance.
      * Score = (l - distance) / l, where l is the maximum length of strings s1 and s2.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the fuzzy score between Strings s1 and s2.
      */
    def score(s1: String, s2: String): Double = {
      val maxLen = math.max(s1.length, s2.length)
      val minLen = maxLen - distance(s1, s2)
      (if (minLen < 0 || minLen > maxLen) 0d else minLen * 1d) / maxLen
    }
  }

  /**
    * Defines implementation for [[StringMetricAlgorithm]] by adding
    * implicit definitions from [[DistanceAlgorithm]], [[ScoringAlgorithm]],
    * [[WeightedDistanceAlgorithm]], or [[WeightedScoringAlgorithm]]
    */
  trait StringMetric[A <: StringMetricAlgorithm] {
    def distance(s1: String, s2: String)
                (implicit algo: DistanceAlgorithm[A]): Int = algo.distance(s1, s2)
    def distance[B](s1: String, s2: String, weight: B)
                   (implicit algo: WeightedDistanceAlgorithm[A, B]): Int = algo.distance(s1, s2, weight)
    def score(s1: String, s2: String)
             (implicit algo: ScoringAlgorithm[A]): Double = algo.score(s1, s2)
    def score[B](s1: String, s2: String, weight: B)
                (implicit algo: WeightedScoringAlgorithm[A, B]): Double = algo.score(s1, s2, weight)
    def score(s1: String, s2: String)
             (implicit algo: SoundScoringAlgorithm[A]): Boolean = algo.score(s1, s2)
  }

  /**
    * Object to extend operations to the String class.
    *
    * {{{
    * import com.github.vickumar1981.stringdistance.StringConverter._
    *
    * // Scores between two strings
    * val cosSimilarity: Double = "hello".cosine("chello")
    * val damerau: Double = "martha".damerau("marhta")
    * val diceCoefficient: Double = "martha".diceCoefficient("marhta")
    * val hamming: Double = "martha".hamming("marhta")
    * val jaccard: Double = "karolin".jaccard("kathrin")
    * val jaro: Double = "martha".jaro("marhta")
    * val jaroWinkler: Double = "martha".jaroWinkler("marhta")
    * val levenshtein: Double = "martha".levenshtein("marhta")
    * val ngramSimilarity: Double = "karolin".nGram("kathrin")
    * val bigramSimilarity: Double = "karolin".nGram("kathrin", 2)
    * val overlap: Double = "karolin".overlap("kathrin")
    *
    * // Distances between two strings
    * val damerauDist: int = "martha".damerauDist("marhta")
    * val hammingDist: Int = "martha".hammingDist("marhta")
    * val levenshteinDist: Int = "martha".levenshteinDist("marhta")
    * val longestCommonSeq: Int = "martha".longestCommonSeq("marhta")
    * val ngramDist: Int = "karolin".nGramDist("kathrin")
    * val bigramDist: Int = "karolin".nGramDist("kathrin", 2)
    *
    * // Phonetic similarity of two strings
    * val metaphone: Boolean = "merci".metaphone("mercy")
    * val soundex: Boolean = "merci".soundex("mercy")
    * }}}
    */
  object StringConverter {
    import StringDistance._
    import StringSound._

    implicit class StringToStringDistanceConverter(s1: String) {
      def cosine(s2: String, splitOn: String = Strategy.splitWord): Double = Cosine.score(s1, s2, splitOn)
      def damerau(s2: String): Double = Damerau.score(s1, s2)
      def damerauDist(s2: String): Int = Damerau.distance(s1, s2)
      def diceCoefficient(s2: String): Double = DiceCoefficient.score(s1, s2)
      def hamming(s2: String): Double = Hamming.score(s1, s2)
      def hammingDist(s2: String): Int = Hamming.distance(s1, s2)
      def jaccard(s2: String, nGram: Int = 1): Double = Jaccard.score(s1, s2, nGram)
      def jaro(s2: String): Double = Jaro.score(s1, s2)
      def jaroWinkler(s2: String, weight: Double = 0.1): Double =
        JaroWinkler.score(s1, s2, weight)
      def levenshtein(s2: String): Double = Levenshtein.score(s1, s2)
      def levenshteinDist(s2: String): Int = Levenshtein.distance(s1, s2)
      def longestCommonSeq(s2: String): Int = LongestCommonSeq.distance(s1, s2)
      def nGram(s2: String, nGram: Int = 1): Double = NGram.score(s1, s2, nGram)
      def nGramDist(s2: String, nGram: Int = 1): Double = NGram.distance(s1, s2, nGram)
      def overlap(s2: String, nGram: Int = 1): Double = Overlap.score(s1, s2, nGram)

      def metaphone(s2: String): Boolean = Metaphone.score(s1, s2)
      def soundex(s2: String): Boolean = Soundex.score(s1, s2)
    }
  }
}
