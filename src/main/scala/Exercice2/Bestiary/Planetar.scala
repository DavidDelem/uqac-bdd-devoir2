package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class Planetar (position: Position, id: Int)
  extends LivingEntity(
    id,
    name = "Planetar",
    229,
    229,
    32,
    position,
    "GoodGuys",
    10,
    List(("ft", 5), ("fly", 16)),
    new Attack("+3 holy greatsword", 18, 21, List(27, 22, 17), 5)
  ){
}
