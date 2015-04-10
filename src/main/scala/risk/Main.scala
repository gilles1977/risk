package risk

object Main {
  def main(args: Array[String]) {
    val risk = new SyntheticRisk("""D:\Users\gcoenrae\Downloads\Nav.csv""")
    println(risk.toString())
  }
}