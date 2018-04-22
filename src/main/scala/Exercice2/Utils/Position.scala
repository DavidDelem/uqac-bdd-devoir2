package Exercice2.Utils

class Position (var x: Double = 0, var y: Double = 0) extends Serializable {

  def distanceToOrigin() : Double = {
    Math.sqrt(Math.pow(this.x, 2)+ Math.pow(this.y, 2))
  }

  def normalize() : Position = {
    val distance = distanceToOrigin();
    var pos = new Position(0,0)

    if(distance!=0){
      pos.x = this.x/distance
      pos.y = this.y/distance
    }
    pos
  }

}

object Position{

  def distanceBetween(p1: Position, p2: Position): Double = {
    Math.sqrt(Math.pow(p2.x-p1.x,2)+Math.pow(p2.y-p1.y,2))
  }

}
