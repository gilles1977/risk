package risk

object Main {
  def main(args: Array[String]) {
    val risk = new SyntheticRisk("""20010103;100
20010104;102
20010105;101
20010112;103
20010119;105
20010126;104
20010202;108""")
    println(risk.toString())
  }
}