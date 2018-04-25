package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class MovanicDeva (position: Position, id: Int)
  extends LivingEntity(
    id,
    "Movanic Deva",
    126,
    126,
    24,
    position,
    "GoodGuys",
    0,
    List(("ft", 7), ("fly", 10)),
    new Attack("+1 flaming greatsword", 8, 13, List(17, 12, 7), 5),
    null,
    null
  ){
}
