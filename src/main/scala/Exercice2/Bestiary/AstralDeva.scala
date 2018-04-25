package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class AstralDeva (position: Position, id: Int)
  extends LivingEntity(
    id,
    "Astral Deva",
    172,
    172,
    29,
    position,
    "GoodGuys",
    0,
    List(("ft", 9), ("fly", 18)),
    new Attack("+2 disrupting warhammer", 15, 22, List(26, 21, 16), 5),
    null,
    null
  ){
}
