package Exercice2.Bestiary

import Exercice2.{Attack, LivingEntity}
import Exercice2.Utils.Position

class DoubleAxeFury (position: Position, id: Int)
  extends LivingEntity (
    id,
    "Double Axe Fury",
    142,
    142,
    17,
    position,
    "BadGuys",
    0,
    List(("ft", 7)),
    new Attack("+1 orc double axe", 11, 18, List(19, 14, 9), 5),
    new Attack("mwk composite longbow", 7, 14, List(16,11,6), 110)
  ){

}
