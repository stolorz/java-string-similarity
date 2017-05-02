package info.debatty.java.stringsimilarity;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class QGramTest {

  /**
   * Test of distance method, of class QGram.
   */
  @Test
  public final void testDistance() {
    System.out.println("distance");
    QGram instance = new QGram(2);
    // AB BC CD CE
    //  1  1  1  0
    //  1  1  0  1
    // Total: 2
    double result = instance.distance("ABCD", "ABCE");
    assertEquals(2.0, result, 0.0);

    assertEquals(0.0, instance.distance("S", "S"), 0.0);

    assertEquals(0.0, instance.distance("012345", "012345"), 0.0);

    // NOTE: not using null/empty tests in NullEmptyTests because QGram is
    // different
    assertEquals(0.0, instance.distance("", ""), 0.1);
    assertEquals(2.0, instance.distance("", "foo"), 0.1);
    assertEquals(2.0, instance.distance("foo", ""), 0.1);

    NullEmptyTests.assertNullPointerExceptions(instance);
  }

  @Test
  public void testDistanceFromHashedProfile() {

    String s1 = "Would you vote for John Millan?";
    String s2 = "Would you vote for John MIller?";

    QGram qGram = new QGram(3);

    Map<String, Integer> profile1 = qGram.getProfile(s1);
    Map<String, Integer> profile2 = qGram.getProfile(s2);

    Map<Integer, Integer> hashedProfile1 = qGram.getHashedProfile(s1);
    Map<Integer, Integer> hashedprofile2 = qGram.getHashedProfile(s2);

    double distanceFromStrings = qGram.distance(s1, s2);
    double distanceFromProfiles = qGram.distance(profile1, profile2);
    double distanceFromHashedProfiles = qGram.distanceFromHashedProfile(hashedProfile1, hashedprofile2);

    assertEquals(distanceFromProfiles, distanceFromStrings, 0.01);
    assertEquals(distanceFromHashedProfiles, distanceFromStrings, 0.01);

    System.out.println(distanceFromStrings);
    System.out.println(distanceFromProfiles);
    System.out.println(distanceFromHashedProfiles);

  }


}
