package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.{Constants, Position}

import scala.collection.mutable.ListBuffer

class GreenGreatWyrmDragon (position: Position, id: Int)
  extends LivingEntity(
    id,
    "Green Great Wyrm Dragon",
    391,
    391,
    37,
    position,
    "BadGuys",
    0,
    List(("ft", 15)),
    new Attack("tail slap", 25, 27, List(31), 5),
    null,
    new Attack("acid", 26, 27, List(999), 70),
    maxTargets = 3
  ){

  var focus:LivingEntity = null

  override def setTargets(newTargets: List[LivingEntity]) {

    newTargets.find(target => target.name == "Solar") match {
      case Some(solar) => this.focus = solar
      case None => this.focus = null
    }

    super.setTargets(newTargets)
  }

  override def computeNormalizedDirection(): Position = {

    //Choose which target to aim
    var targetToFollow:LivingEntity = null

    if(focus != null){
      targetToFollow = focus
    }
    else if(targets.nonEmpty) targetToFollow = targets(0)

    if(targetToFollow != null) {

      val desiredVelocity = new Position(targetToFollow.position.x - this.position.x, targetToFollow.position.y - this.position.y)
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

}
