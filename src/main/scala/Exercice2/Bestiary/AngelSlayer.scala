package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class AngelSlayer (position: Position, id: Int)
  extends LivingEntity(
    id,
    "Angel Slayer",
    112,
    26,
    position,
    "BadGuys",
    0,
    List(("ft", 40)),
    new Attack("double axe", 8, 15, List(26,16,11), 5),
    new Attack("mwk composite longbow", 7, 14, List(19,14,9), 110)
  ){
}
