package risk

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RiskSuite extends FunSuite {
  test("Test risk with arbitrary values") {
    val values = """20010103;100
20010104;102
20010105;101
20010112;103
20010119;105
20010126;104
20010202;108"""
    val s = new SyntheticRisk(values)
    assert(s.returnByDate.length === 6)
    assert(s.returnByPeriod.length === 5)
  }
}