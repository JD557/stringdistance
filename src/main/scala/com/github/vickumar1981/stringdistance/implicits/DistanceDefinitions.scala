package com.github.vickumar1981.stringdistance.implicits

import com.github.vickumar1981.stringdistance._
import com.github.vickumar1981.stringdistance.impl.{HammingImpl, LevenshteinDistanceImpl, LongestCommonSeqImpl, NGramImpl}

trait DistanceDefinitions {
  /**
    * Implicit definition of damerau levenshtein distance for [[DamerauLevenshteinAlgorithm]].
    */
  implicit object DamerauLevenshteinDistance extends LevenshteinDistanceImpl[Char]
    with DistanceAlgorithm[DamerauLevenshteinAlgorithm] with ScorableFromDistance[DamerauLevenshteinAlgorithm] {
    /**
      * The score method takes two strings and returns the damerau levenshtein distance between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the damerau levenshtein distance between Strings s1 and s2.
      */
    override def distance(s1: String, s2: String): Int = damerauLevenshtein(s1.toCharArray, s2.toCharArray)
  }

  /**
    * Implicit definition of hamming distance for [[HammingAlgorithm]].
    */
  implicit object HammingDistance extends HammingImpl[Char]
    with DistanceAlgorithm[HammingAlgorithm] with ScorableFromDistance[HammingAlgorithm] {
    /**
      * The distance method takes two strings and returns the hamming distance between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the hamming distance between Strings s1 and s2.
      */
    override def distance(s1: String, s2: String): Int = hamming(s1.toCharArray, s2.toCharArray)
  }

  /**
    * Implicit definition of levenshtein distance for [[LevenshteinAlgorithm]].
    */
  implicit object LevenshteinDistance extends LevenshteinDistanceImpl[Char]
    with DistanceAlgorithm[LevenshteinAlgorithm] with ScorableFromDistance[LevenshteinAlgorithm] {
    /**
      * The score method takes two strings and returns the levenshtein distance between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the levenshtein distance between Strings s1 and s2.
      */
    override def distance(s1: String, s2: String): Int = levenshtein(s1.toCharArray, s2.toCharArray)
  }

  /**
    * Implicit definition of longest common subsequence for [[CosineAlgorithm]].
    */
  implicit object LongestCommonSeqDistance extends LongestCommonSeqImpl
    with DistanceAlgorithm[LongestCommonSeqAlorithm] {
    /**
      * The score method takes two strings and returns longest common subsequence distance between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the longest common subsequence distance between Strings s1 and s2.
      */
    override def distance(s1: String, s2: String): Int = longestCommonSeq(s1, s2)
  }

  /**
    * Implicit definition of n-gram distance for [[NGramAlgorithm]].
    */
  implicit object NGramDistance extends NGramImpl[Char] with WeightedDistanceAlgorithm[NGramAlgorithm, Int] {
    /**
      * The score method takes two strings and returns n-gram similarity between them.
      *
      * @param s1 The 1st String.
      * @param s2 The 2nd String.
      * @return Returns the n-gram distance between Strings s1 and s2.
      */
    override def distance(s1: String, s2: String, n: Int): Int = nGramDist(s1.toCharArray, s2.toCharArray, n)
    override def distance(s1: String, s2: String): Int = nGramDist(s1.toCharArray, s2.toCharArray)
  }
}
