package Exercice2

import Exercice2.Utils.{Constants, Position}

class LivingEntity (
                     var id:Int = 0,
                     var name: String = "",
                     var hpmax: Int = 0,
                     var hp: Int = 0,
                     var armor: Int = 0,
                     var position: Position = new Position(),
                     var team:String = "",
                     var regeneration : Int = 0,
                     var speeds: List[(String, Int)] = null,
                     var melee: Attack = null,
                     var ranged: Attack = null,
                     var special: Attack = null,
                     var maxTargets: Int = 1,
                     var targets: List[LivingEntity] = List.empty[LivingEntity],
                     var hurtDuringRound:Boolean = false)
  extends Serializable {

  // Perte des HP
  // Remise à 0 en cas de valeur négative
  def takeDamage(amount: Int): Unit = {
    hurtDuringRound = true
    this.hp -= amount
    if (this.hp < 0) this.hp = 0
  }

  // Regénération
  // On s'assure de ne pas dépasser les hp initiaux
  def regenerate() : Unit = {
    this.hp = this.hp + this.regeneration
    if (this.hp > this.hpmax) this.hp = this.hpmax
  }

  def attackTarget(target: LivingEntity) : Int = {

    var attack: Attack = null
    val distance = Position.distanceBetween(this.position,target.position)


    if(melee != null && distance <= melee.minDistance) attack = melee
    else if(special != null && distance <= special.minDistance) attack = special
    else if(ranged != null && distance <= ranged.minDistance) attack = ranged
    else return 0

    attack.getDamage(target.armor)

  }

  def computeNormalizedDirection(): Position = {

    //On se déplacera toujours vers le plus proche
    if(targets.length > 0) {

      val desiredVelocity = new Position(targets(0).position.x - this.position.x, targets(0).position.y - this.position.y)
      val normalizedDesiredVelocity = desiredVelocity.normalize()
      val distanceToTarget = desiredVelocity.getDistance()

      if(distanceToTarget < Constants.nearTargetRadius){

        if(distanceToTarget > Constants.stickDistance){
          normalizedDesiredVelocity.x *= (distanceToTarget/Constants.nearTargetRadius)
          normalizedDesiredVelocity.y *= (distanceToTarget/Constants.nearTargetRadius)
        }
        else{
          normalizedDesiredVelocity.x = 0
          normalizedDesiredVelocity.y = 0
        }
      }
      normalizedDesiredVelocity
    }
    else new Position(0,0)
  }

  // Mouvements
  def move(){
    val normalizeDirection = computeNormalizedDirection()
    //TODO : use all different speeds
    this.position.x += normalizeDirection.x * speeds(0)._2
    this.position.y += normalizeDirection.y * speeds(0)._2
  }



  def setTargets(newTargets: List[LivingEntity]) = {
    this.targets = newTargets.sortBy((target) => Position.distanceBetween(this.position, target.position)).take(maxTargets)
  }

  override def toString: String = "Name : " + name + ", HP : " + hp + ", Position : (" + position.x + ", " + position.y + ")"

}
