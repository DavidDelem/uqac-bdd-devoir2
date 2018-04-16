package Exercice2

class LivingEntity extends Serializable {
  protected var name = ""
  protected var hp = 0

  def getName():String = {
    return this.name;
  }
}
