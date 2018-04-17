package Exercice2.Bestiary

import Exercice2.Utils.Position
import Exercice2.{Attack, Monster}

class OrcWorgRider (position: Position)
  extends Monster (
    "Orc worg Rider",
    13,
    18,
    position,
    0,
    List(("ft", 20)),
    new Attack("mwk battleaxe", 4, 12, List(6), 5),
    new Attack("shortbow", 1, 6, List(4), 110)
  )
{

}
