package Exercice2

class Link (var relation: String = "", var distance: Int = 200) extends Serializable {


  def setRelation(newRelation:String) = {
    this.relation = newRelation
  }

  def getRelation:String = {
    this.relation
  }

  def setDistane(newDistance:Int) = {
    this.distance = newDistance
  }

  def getDistance:Int = {
    this.distance
  }
}
