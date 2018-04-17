package Exercice2

class Attack(
              var name: String,
              var minAccuracy: Int,
              var maxAccuracy: Int,
              var damages: List[Int],
              var minDistance: Int
            ){

  /*var name: String = ""
  var minAccuracy: Int = 0
  var maxAccuracy: Int = 0
  var damages: List[Int] =  List.empty[Int]
  var damageThreshold: Int = 0
  var minDistance: Int = 0*/

  private var currentIndexDamage: Int = 0

  def getDamage : Int = {
    val random = scala.util.Random
    val damage = damages(currentIndexDamage) + ( minAccuracy + random.nextInt(maxAccuracy - minAccuracy))

    if(currentIndexDamage < damages.size - 1) currentIndexDamage += 1

    damage
  }

}
