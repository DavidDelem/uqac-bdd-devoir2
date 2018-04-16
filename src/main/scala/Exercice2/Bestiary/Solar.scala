package Exercice2.Bestiary

import Exercice2.Monster

class Solar () extends Monster{
  this.name = "Solar"
  this.hp = 363
  this.armor = 44
  this.regeneration = 15
  this.speeds = ("ft", 50) :: this.speeds
  this.speeds = ("fly", 150) :: this.speeds
  this.melee = ("+5 dancing greatsword", List(35, 30, 25, 20), List(21, 24), 5)
  this.ranged = ("+5 composite longbow", List(31, 26, 21, 16), List(16, 20), 110)
}
