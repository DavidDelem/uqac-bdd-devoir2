package Exercice2

import Exercice2.Utils.Position

class LivingEntity (var name: String = "", var hp: Int = 0, var armor: Int = 0, var position: Position = null) extends Serializable {

  def takeDamage(amount: Int): Unit = {
    this.hp -= amount
    if(this.hp < 0) this.hp = 0
  }

  override def toString: String = "Name : " + name + ", HP : " + hp + ", Position : (" + position.x + ", " + position.y + ")"
}
