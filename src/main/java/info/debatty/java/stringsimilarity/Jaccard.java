/*
 * The MIT License
 *
 * Copyright 2015 tibo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package info.debatty.java.stringsimilarity;

import java.util.Map;

import info.debatty.java.stringsimilarity.interfaces.MetricStringDistance;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringDistance;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import net.jcip.annotations.Immutable;

/**
 * Each input string is converted into a set of n-grams, the Jaccard index is
 * then computed as |V1 inter V2| / |V1 union V2|. Like Q-Gram distance, the
 * input strings are first converted into sets of n-grams (sequences of n
 * characters, also called k-shingles), but this time the cardinality of each
 * n-gram is not taken into account. Distance is computed as 1 - cosine
 * similarity. Jaccard index is a metric distance.
 * 
 * @author Thibault Debatty
 */
@Immutable
public class Jaccard extends ShingleBased
		implements MetricStringDistance, NormalizedStringDistance, NormalizedStringSimilarity {

	private final boolean takeOnlyProfiles;

	/**
	 * The strings are first transformed into sets of k-shingles (sequences of k
	 * characters), then Jaccard index is computed as |A inter B| / |A union B|.
	 * The default value of k is 3.
	 *
	 * @param k
	 */
	public Jaccard(final int k) {
		super(k);
		this.takeOnlyProfiles = false;
	}

	/**
	 * The strings are first transformed into sets of k-shingles (sequences of k
	 * characters), then Jaccard index is computed as |A inter B| / |A union B|.
	 * The default value of k is 3.
	 */
	public Jaccard() {
		super();
		this.takeOnlyProfiles = false;
	}

	public Jaccard(final boolean takeOnlyProfiles) {
		super();
		this.takeOnlyProfiles = takeOnlyProfiles;
	}

	/**
	 * Compute Jaccard index: |A inter B| / |A union B|.
	 * 
	 * @param s1
	 *            The first string to compare.
	 * @param s2
	 *            The second string to compare.
	 * @return The Jaccard index in the range [0, 1]
	 * @throws NullPointerException
	 *             if s1 or s2 is null.
	 */
	@Override
	public final double similarity(final String s1, final String s2) {

		if (takeOnlyProfiles) {
			throw new IllegalArgumentException(
					"This object can compute similarity only for strings represented by precomputed profiles.");
		}

		if (s1 == null) {
			throw new NullPointerException("s1 must not be null");
		}

		if (s2 == null) {
			throw new NullPointerException("s2 must not be null");
		}

		if (s1.equals(s2)) {
			return 1;
		}

		Map<String, Integer> profile1 = getProfile(s1);
		Map<String, Integer> profile2 = getProfile(s2);

		return similarity(profile1, profile2);
	}

	public final double similarity(final Map<String, Integer> profile1, final Map<String, Integer> profile2) {
		if (profile1 == null) {
			throw new NullPointerException("profile1 must not be null");
		}

		if (profile2 == null) {
			throw new NullPointerException("profile2 must not be null");
		}

		if(profile1.keySet().isEmpty() && profile2.keySet().isEmpty())
			return 1;
		
		int intersection_size = 0;

		for (String key : profile1.keySet()) {
			if (profile2.containsKey(key)) {
				intersection_size++;
			}
		}

		int union_size = profile2.size() + profile2.size() - intersection_size;

		return 1.0 * intersection_size / union_size;
	}

	/**
	 * Distance is computed as 1 - similarity.
	 * 
	 * @param s1
	 *            The first string to compare.
	 * @param s2
	 *            The second string to compare.
	 * @return 1 - the Jaccard similarity.
	 * @throws NullPointerException
	 *             if s1 or s2 is null.
	 */
	@Override
	public final double distance(final String s1, final String s2) {
		return 1.0 - similarity(s1, s2);
	}

	public final double distance(final Map<String, Integer> profile1, final Map<String, Integer> profile2) {
		return 1.0 - similarity(profile1, profile2);
	}

}
