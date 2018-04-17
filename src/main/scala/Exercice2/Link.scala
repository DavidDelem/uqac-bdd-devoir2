package Exercice2

class Link (val relation: String = "", val distance: Int = 200) extends Serializable {

  def getRelation:String = {
    this.relation
  }

  def getDistance:Int = {
    this.distance
  }
}
