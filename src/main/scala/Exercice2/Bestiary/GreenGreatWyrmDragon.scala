package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class GreenGreatWyrmDragon (position: Position, id: Int)
  extends LivingEntity(
    id,
    "Green Great Wyrm Dragon",
    391,
    37,
    position,
    "BadGuys",
    0,
    List(("ft", 40), ("fly", 250), ("swim", 40)),
    new Attack("bite", 25, 29, List(33), 5)
  ){
}
