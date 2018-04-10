package Exercice1

class Monster extends Serializable {
  var name = ""
  var spells = List.empty[String]

  def this(name: String, spells: List[String]) {
    this()
    this.name = name
    this.spells = spells
  }
}
