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
    List(("ft", 7), ("fly", 45), ("swim", 7)),
    new Attack("tail slap", 25, 27, List(31), 5),
    null,
    new Attack("acid", 26, 27, List(999), 70),
    3
  ){
    private var focus:LivingEntity = null;

    override def setTargets(newTargets: List[LivingEntity]) = {
      var found: Boolean = false;
      newTargets.foreach(target => {
        if(target.name == "Solar") {
          this.focus = target
          found = true;
        };
      })
      if(!found) this.focus = null;

      val t = newTargets.sortBy((target) => Position.distanceBetween(this.position, target.position)).take(maxTargets)
      this.targets = t
    }

  override def computeNormalizedDirection(): Position = {
    var moveTarget:LivingEntity = null;
    if(focus != null) {
      moveTarget = focus
    } else {
      moveTarget = targets(0)
    }
    //On se déplacera toujours vers le plus proche
    if(targets.length > 0) {

      val desiredVelocity = new Position(moveTarget.position.x - this.position.x, moveTarget.position.y - this.position.y)
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

  override def move(){
    val normalizeDirection = computeNormalizedDirection()
    //TODO : use all different speeds
    this.position.x += normalizeDirection.x * speeds(1)._2
    this.position.y += normalizeDirection.y * speeds(1)._2
  }

}
