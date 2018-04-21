package Exercice2

import Exercice2.Utils.Position
import org.apache.spark.graphx.VertexId

abstract class Monster(
                        name: String,
                        hp: Int,
                        armor: Int,
                        position: Position,
                        var regeneration : Int = 0,
                        var speeds: List[(String, Int)] = null,
                        var melee: Attack = null,
                        var ranged: Attack = null,
                        id:Int,
                        team: String,
                        var target: LivingEntity = null)
  extends LivingEntity(
    name,
    hp,
    armor,
    position,
    id,
    team) {



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

//  def updateTarget(proposedTarget: LivingEntity): Unit = {
//
//    println(proposedTarget)
//
//    //exemple : on pourra faire des check plus précis
//    if(this.target == null ||
//      (target.hp == 0 && proposedTarget.hp > 0)  ||
//      (proposedTarget.hp < this.target.hp)
//    ){
//      this.target = proposedTarget
//    }
//
//  }

  override def toString: String = "Name : " + name + ", HP : " + hp + ", Armor : " + armor + ", Regeneration : " + regeneration + ", Speeds : " + speeds

}