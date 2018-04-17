package Exercice2.Utils

class Position (var x: Int = 0, var y: Int = 0) extends Serializable {

  def distance(v: Position): Double = {
    val vx = v.x - this.x
    val vy = v.y - this.y
    Math.sqrt(vx * vx + vy * vy)
  }

}
