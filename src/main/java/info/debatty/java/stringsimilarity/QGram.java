package info.debatty.java.stringsimilarity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import info.debatty.java.stringsimilarity.interfaces.StringDistance;
import net.jcip.annotations.Immutable;

/**
 * Q-gram distance, as defined by Ukkonen in "Approximate string-matching with
 * q-grams and maximal matches". The distance between two strings is defined as
 * the L1 norm of the difference of their profiles (the number of occurences of
 * each n-gram): SUM( |V1_i - V2_i| ). Q-gram distance is a lower bound on
 * Levenshtein distance, but can be computed in O(m + n), where Levenshtein
 * requires O(m.n).
 *
 * @author Thibault Debatty
 */
@Immutable
public class QGram extends ShingleBased implements StringDistance {

  private final boolean takeOnlyProfiles;

  /**
   * Q-gram similarity and distance. Defined by Ukkonen in "Approximate
   * string-matching with q-grams and maximal matches",
   * http://www.sciencedirect.com/science/article/pii/0304397592901434 The
   * distance between two strings is defined as the L1 norm of the difference
   * of their profiles (the number of occurences of each k-shingle). Q-gram
   * distance is a lower bound on Levenshtein distance, but can be computed in
   * O(|A| + |B|), where Levenshtein requires O(|A|.|B|)
   *
   * @param k
   */
  public QGram(final int k) {
    super(k);
    this.takeOnlyProfiles = false;
  }

  /**
   * Q-gram similarity and distance. Defined by Ukkonen in "Approximate
   * string-matching with q-grams and maximal matches",
   * http://www.sciencedirect.com/science/article/pii/0304397592901434 The
   * distance between two strings is defined as the L1 norm of the difference
   * of their profiles (the number of occurence of each k-shingle). Q-gram
   * distance is a lower bound on Levenshtein distance, but can be computed in
   * O(|A| + |B|), where Levenshtein requires O(|A|.|B|)
   * Default k is 3.
   */
  public QGram() {
    super();
    this.takeOnlyProfiles = false;
  }

  public QGram(final boolean takeOnlyProfiles) {
    super();
    this.takeOnlyProfiles = takeOnlyProfiles;
  }

  /**
   * The distance between two strings is defined as the L1 norm of the
   * difference of their profiles (the number of occurence of each k-shingle).
   *
   * @param s1 The first string to compare.
   * @param s2 The second string to compare.
   * @return The computed Q-gram distance.
   * @throws NullPointerException if s1 or s2 is null.
   */
  @Override
  public final double distance(final String s1, final String s2) {

    if (takeOnlyProfiles) {
      throw new IllegalArgumentException("This object can compute similarity only for strings represented by precomputed profiles.");
    }

    if (s1 == null) {
      throw new NullPointerException("s1 must not be null");
    }

    if (s2 == null) {
      throw new NullPointerException("s2 must not be null");
    }

    if (s1.equals(s2)) {
      return 0;
    }

    Map<String, Integer> profile1 = getProfile(s1);
    Map<String, Integer> profile2 = getProfile(s2);

    return distance(profile1, profile2);
  }

  /**
   * Compute QGram distance using precomputed profiles.
   *
   * @param profile1
   * @param profile2
   * @return
   */
  public final double distance(final Map<String, Integer> profile1, final Map<String, Integer> profile2) {

    Set<String> union = new HashSet<String>();
    union.addAll(profile1.keySet());
    union.addAll(profile2.keySet());

    int agg = 0;
    for (String key : union) {
      int v1 = 0;
      int v2 = 0;

      if (profile1.containsKey(key)) {
        v1 = profile1.get(key);
      }

      if (profile2.containsKey(key)) {
        v2 = profile2.get(key);
      }

      agg += Math.abs(v1 - v2);
    }
    return agg;
  }

  /**
   * Compute QGram distance using precomputed and hashed profiles.
   *
   * @param hashedProfile1
   * @param hashedprofile2
   * @return
   */
  public final double distanceFromHashedProfile(final Map<Integer, Integer> profile1, final Map<Integer, Integer> profile2) {

    Set<Integer> union = new HashSet<Integer>();
    union.addAll(profile1.keySet());
    union.addAll(profile2.keySet());

    int agg = 0;
    for (Integer key : union) {
      int v1 = 0;
      int v2 = 0;

      if (profile1.containsKey(key)) {
        v1 = profile1.get(key);
      }

      if (profile2.containsKey(key)) {
        v2 = profile2.get(key);
      }

      agg += Math.abs(v1 - v2);
    }
    return agg;
  }
}
