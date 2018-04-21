package Exercice2

import Exercice2.Utils.Position

class LivingEntity (
                     var id:Int = 0,
                     var name: String = "",
                     var hp: Int = 0,
                     var armor: Int = 0,
                     var position: Position = new Position(),
                     var team:String = "",
                     var regeneration : Int = 0,
                     var speeds: List[(String, Int)] = null,
                     var melee: Attack = null,
                     var ranged: Attack = null,
                     var target: LivingEntity = null)
  extends Serializable {

  def takeDamage(amount: Int): Unit = {
    this.hp -= amount
    if(this.hp < 0) this.hp = 0
  }

  def attackTarget() : Int = {

    var attack: Attack = null
    val distance = this.position.distance(target.position)

    if(distance <= melee.minDistance) attack = melee
    else if(distance <= ranged.minDistance) attack = ranged
    else return 0

    val damage = attack.getDamage
    if(damage >= target.armor) damage
    else 0

  }

  override def toString: String = "Name : " + name + ", HP : " + hp + ", Position : (" + position.x + ", " + position.y + ")"

}
