package Exercice2

import Exercice2.Utils.Position

abstract class Monster(
                        name: String,
                        hp: Int,
                        armor: Int,
                        position: Position,
                        var regeneration : Int = 0,
                        var speeds: List[(String, Int)] = null,
                        var melee: Attack = null,
                        var ranged: Attack = null,
                        var target: LivingEntity = null)
  extends LivingEntity(
    name,
    hp,
    armor,
    position) {



  def attack(target: LivingEntity, distance: Double) : Unit = {

    var attack: Attack = null

    if(distance <= melee.minDistance) attack = melee
    else if(distance <= ranged.minDistance) attack = ranged
    else return

    val damage = attack.getDamage
    if(damage >= target.armor) target.takeDamage(damage)

  }

  def updateTarget(proposedTarget: LivingEntity) = {
    //exemple : on pourra faire des check plus précis
    if(this.target == null) this.target = proposedTarget
    if(this.target.hp < proposedTarget.hp) this.target = proposedTarget


  }
  override def toString: String = "Name : " + name + ", HP : " + hp + ", Armor : " + armor + ", Regeneration : " + regeneration + ", Speeds : " + speeds

}